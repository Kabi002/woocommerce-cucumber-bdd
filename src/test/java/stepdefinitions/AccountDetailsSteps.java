package stepdefinitions;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import config.ConfigReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.AccountDetailsPage;
import utils.DriverManager;

public class AccountDetailsSteps {

    private WebDriver driver;
    private AccountDetailsPage accountDetailsPage;

    public AccountDetailsSteps() {
        this.driver             = DriverManager.getDriver();
        this.accountDetailsPage = new AccountDetailsPage(driver);
    }

    // ── Navigate ───────────────────────────────────────────────────────
    @And("I navigate to the account details page")
    public void i_navigate_to_the_account_details_page() {
        accountDetailsPage.navigateToAccountDetailsPage(ConfigReader.getBaseUrl());
        accountDetailsPage.dismissPopups();
    }

    // ── Verify Fields ──────────────────────────────────────────────────
    @Then("I should see all account detail fields")
    public void i_should_see_all_account_detail_fields() {
        Assert.assertTrue(accountDetailsPage.areAllFieldsVisible(),
                "One or more account detail fields are not visible!");
    }

    // ── Field Steps ───────────────────────────────────────────────────
    @When("I clear and enter first name {string}")
    public void i_clear_and_enter_first_name(String firstName) {
        accountDetailsPage.clearAndEnterFirstName(firstName);
    }

    @When("I clear and enter last name {string}")
    public void i_clear_and_enter_last_name(String lastName) {
        accountDetailsPage.clearAndEnterLastName(lastName);
    }

    @When("I clear and enter display name {string}")
    public void i_clear_and_enter_display_name(String displayName) {
        accountDetailsPage.clearAndEnterDisplayName(displayName);
    }

    @When("I clear and enter account email {string}")
    public void i_clear_and_enter_account_email(String email) {
        accountDetailsPage.clearAndEnterEmail(email);
    }

    @When("I enter current password {string}")
    public void i_enter_current_password(String password) {
        accountDetailsPage.enterCurrentPassword(password);
    }

    @When("I enter new password {string}")
    public void i_enter_new_password(String password) {
        accountDetailsPage.enterNewPassword(password);
    }

    @When("I enter confirm new password {string}")
    public void i_enter_confirm_new_password(String password) {
        accountDetailsPage.enterConfirmPassword(password);
    }

    @When("I click save changes button")
    public void i_click_save_changes_button() {
        accountDetailsPage.clickSaveChanges();
    }
    
    @When("I clear password fields")
    public void i_clear_password_fields() {
        accountDetailsPage.clearAllPasswordFields();
    }

    // ── Assertions ─────────────────────────────────────────────────────
  
    @Then("I should see account details saved success message")
    public void i_should_see_account_details_saved_success_message() {
        Assert.assertTrue(accountDetailsPage.isSuccessMessageDisplayed(),
                "Success message not displayed after saving account details!");
    }

    @Then("I should see password required error message")
    public void i_should_see_password_required_error_message() {
        Assert.assertTrue(accountDetailsPage.isPasswordRequiredErrorDisplayed(),
                "Password required error message is not displayed!");
    }

    @Then("I should see wrong password error message")
    public void i_should_see_wrong_password_error_message() {
        Assert.assertTrue(accountDetailsPage.isWrongPasswordErrorDisplayed(),
                "Wrong password error message is not displayed!");
    }

    @Then("I should see required field validation error")
    public void i_should_see_required_field_validation_error() {
        Assert.assertTrue(accountDetailsPage.isFirstNameRequiredErrorDisplayed(),
                "Required field validation error not displayed!");
    }

    @Then("I should see email format validation error")
    public void i_should_see_email_format_validation_error() {
        String validationMsg = accountDetailsPage.getEmailValidationMessage();
        System.out.println("Email Validation Message: " + validationMsg);
        Assert.assertTrue(
                validationMsg.contains("@") || validationMsg.contains("include") || validationMsg.contains("valid"),
                "Email format validation message not displayed! Got: " + validationMsg);
    }
}