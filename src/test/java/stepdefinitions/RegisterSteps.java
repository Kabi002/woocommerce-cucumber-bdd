package stepdefinitions;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import config.ConfigReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.RegisterPage;
import utils.DriverManager;

public class RegisterSteps {
    
    private WebDriver driver;
    private RegisterPage registerPage;
    
    public RegisterSteps() {
        this.driver = DriverManager.getDriver();
        this.registerPage = new RegisterPage(driver);
    }
    
    @Given("I navigate to the registration page")
    public void i_navigate_to_the_registration_page() {
        registerPage.navigateToRegistrationPage(ConfigReader.getBaseUrl());
        registerPage.dismissPopups();
    }
    
    @When("I enter first name {string}")
    public void i_enter_first_name(String firstName) {
        registerPage.enterFirstName(firstName);
    }
    
    @When("I enter last name {string}")
    public void i_enter_last_name(String lastName) {
        registerPage.enterLastName(lastName);
    }
    
    @When("I enter email address {string}")
    public void i_enter_email_address(String email) {
        registerPage.enterEmail(email);
    }
    
    @When("I click on the register button")
    public void i_click_on_the_register_button() {
        registerPage.clickRegisterButton();
    }
    
    @Then("I should be registered successfully")
    public void i_should_be_registered_successfully() {
        Assert.assertTrue(registerPage.isRegistrationSuccessful(), "Registration was not successful!");
    }
    
    @Then("I should see the dashboard")
    public void i_should_see_the_dashboard() {
        Assert.assertTrue(registerPage.isDashboardVisible(), "Dashboard is not visible!");
    }
    
    @Then("I should see registration HTML5 validation message")
    public void i_should_see_registration_html5_validation_message() {
        String validationMsg = registerPage.getFirstNameValidationMessage();
        System.out.println("Validation Message: " + validationMsg);
        Assert.assertTrue(validationMsg.contains("Please fill") || validationMsg.contains("fill out"), 
                         "HTML5 validation message not displayed! Got: " + validationMsg);
    }
    
    @Then("I should see email format validation message")
    public void i_should_see_email_format_validation_message() {
        String validationMsg = registerPage.getEmailValidationMessage();
        System.out.println("Email Validation Message: " + validationMsg);
        Assert.assertTrue(validationMsg.contains("@") || validationMsg.contains("include") || validationMsg.contains("valid"), 
                         "Email format validation message not displayed! Got: " + validationMsg);
    }
    
    @Then("I should see duplicate email error message")
    public void i_should_see_duplicate_email_error_message() {
        Assert.assertTrue(registerPage.isErrorMessageDisplayed(), "Error message is not displayed!");
        String errorMsg = registerPage.getErrorMessage();
        System.out.println("Error Message: " + errorMsg);
        Assert.assertTrue(errorMsg.contains("already registered") || errorMsg.contains("already") || errorMsg.contains("exist"), 
                         "Duplicate email error not displayed! Got: " + errorMsg);
    }
}