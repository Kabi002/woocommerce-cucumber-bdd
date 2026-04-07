package stepdefinitions;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import config.ConfigReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.LoginPage;
import utils.DriverManager;

public class LoginSteps {

    private WebDriver driver;
    private LoginPage loginPage;

    public LoginSteps() {
        this.driver = DriverManager.getDriver();
        this.loginPage = new LoginPage(driver);
    }

    @Given("I navigate to the login page")
    public void i_navigate_to_the_login_page() {
        loginPage.navigateToLoginPage(ConfigReader.getBaseUrl());
        loginPage.dismissPopups();
    }

    @When("I enter username {string}")
    public void i_enter_username(String username) {
        loginPage.enterUsername(username);
    }

    @When("I enter password {string}")
    public void i_enter_password(String password) {
        loginPage.enterPassword(password);
    }

    @When("I click on the login button")
    public void i_click_on_the_login_button() {
        loginPage.clickLoginButton();
    }

    @Then("I should be logged in successfully")
    public void i_should_be_logged_in_successfully() {
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login was not successful!");
    }

    @Then("I should see an error message")
    public void i_should_see_an_error_message() {
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message is not displayed!");
    }

    @Then("I should see HTML5 validation message")
    public void i_should_see_html5_validation_message() {
        String validationMsg = loginPage.getValidationMessage();
        System.out.println("Validation Message: " + validationMsg);
        
        // Verify the message contains expected text
        Assert.assertTrue(validationMsg.contains("Please fill") || validationMsg.contains("fill out"), 
                         "HTML5 validation message not displayed! Got: " + validationMsg);
    }
}