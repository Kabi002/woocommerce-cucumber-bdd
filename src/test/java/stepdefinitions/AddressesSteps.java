package stepdefinitions;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import config.ConfigReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.AddressesPage;
import utils.DriverManager;

public class AddressesSteps {

    private WebDriver driver;
    private AddressesPage addressesPage;

    public AddressesSteps() {
        this.driver        = DriverManager.getDriver();
        this.addressesPage = new AddressesPage(driver);
    }

    // ── Navigate ───────────────────────────────────────────────────────
    @And("I navigate to the addresses page")
    public void i_navigate_to_the_addresses_page() {
        addressesPage.navigateToAddressesPage(ConfigReader.getBaseUrl());
        addressesPage.dismissPopups();
    }

    // ── Addresses Page Assertions ──────────────────────────────────────
    @Then("I should see billing address section")
    public void i_should_see_billing_address_section() {
        Assert.assertTrue(addressesPage.isBillingAddressSectionVisible(),
                "Billing address section is not visible!");
    }

    @Then("I should see shipping address section")
    public void i_should_see_shipping_address_section() {
        Assert.assertTrue(addressesPage.isShippingAddressSectionVisible(),
                "Shipping address section is not visible!");
    }

    @Then("I should see edit billing address link")
    public void i_should_see_edit_billing_address_link() {
        Assert.assertTrue(addressesPage.isEditBillingLinkVisible(),
                "Edit billing address link is not visible!");
    }

    @Then("I should see edit shipping address link")
    public void i_should_see_edit_shipping_address_link() {
        Assert.assertTrue(addressesPage.isEditShippingLinkVisible(),
                "Edit shipping address link is not visible!");
    }

    // ── Click Edit Links ───────────────────────────────────────────────
    @When("I click on edit billing address link")
    public void i_click_on_edit_billing_address_link() {
        addressesPage.clickEditBillingAddress();
    }

    @When("I click on edit shipping address link")
    public void i_click_on_edit_shipping_address_link() {
        addressesPage.clickEditShippingAddress();
    }

    // ── URL Assertions ─────────────────────────────────────────────────
    @Then("I should be on billing address page")
    public void i_should_be_on_billing_address_page() {
        Assert.assertTrue(addressesPage.isOnBillingAddressPage(),
                "Not on billing address page! URL: " + driver.getCurrentUrl());
    }

    @Then("I should be on shipping address page")
    public void i_should_be_on_shipping_address_page() {
        Assert.assertTrue(addressesPage.isOnShippingAddressPage(),
                "Not on shipping address page! URL: " + driver.getCurrentUrl());
    }

    // ── Billing Address Steps ──────────────────────────────────────────
    @When("I clear and enter billing first name {string}")
    public void i_clear_and_enter_billing_first_name(String value) {
        addressesPage.clearAndEnterBillingFirstName(value);
    }

    @When("I clear and enter billing last name {string}")
    public void i_clear_and_enter_billing_last_name(String value) {
        addressesPage.clearAndEnterBillingLastName(value);
    }

    @When("I clear and enter billing company {string}")
    public void i_clear_and_enter_billing_company(String value) {
        addressesPage.clearAndEnterBillingCompany(value);
    }

    @When("I select billing country {string}")
    public void i_select_billing_country(String countryCode) {
        addressesPage.selectBillingCountry(countryCode);
    }

    @When("I clear and enter billing street address {string}")
    public void i_clear_and_enter_billing_street_address(String value) {
        addressesPage.clearAndEnterBillingAddress(value);
    }

    @When("I clear and enter billing city {string}")
    public void i_clear_and_enter_billing_city(String value) {
        addressesPage.clearAndEnterBillingCity(value);
    }

    @When("I select billing state {string}")
    public void i_select_billing_state(String stateCode) {
        addressesPage.selectBillingState(stateCode);
    }

    @When("I clear and enter billing postcode {string}")
    public void i_clear_and_enter_billing_postcode(String value) {
        addressesPage.clearAndEnterBillingPostcode(value);
    }

    @When("I clear and enter billing phone {string}")
    public void i_clear_and_enter_billing_phone(String value) {
        addressesPage.clearAndEnterBillingPhone(value);
    }

    @When("I clear and enter billing email {string}")
    public void i_clear_and_enter_billing_email(String value) {
        addressesPage.clearAndEnterBillingEmail(value);
    }

    @When("I clear billing first name")
    public void i_clear_billing_first_name() {
        addressesPage.clearBillingFirstName();
    }

    // ── Shipping Address Steps ─────────────────────────────────────────
    @When("I clear and enter shipping first name {string}")
    public void i_clear_and_enter_shipping_first_name(String value) {
        addressesPage.clearAndEnterShippingFirstName(value);
    }

    @When("I clear and enter shipping last name {string}")
    public void i_clear_and_enter_shipping_last_name(String value) {
        addressesPage.clearAndEnterShippingLastName(value);
    }

    @When("I clear and enter shipping company {string}")
    public void i_clear_and_enter_shipping_company(String value) {
        addressesPage.clearAndEnterShippingCompany(value);
    }

    @When("I select shipping country {string}")
    public void i_select_shipping_country(String countryCode) {
        addressesPage.selectShippingCountry(countryCode);
    }

    @When("I clear and enter shipping street address {string}")
    public void i_clear_and_enter_shipping_street_address(String value) {
        addressesPage.clearAndEnterShippingAddress(value);
    }

    @When("I clear and enter shipping city {string}")
    public void i_clear_and_enter_shipping_city(String value) {
        addressesPage.clearAndEnterShippingCity(value);
    }

    @When("I select shipping state {string}")
    public void i_select_shipping_state(String stateCode) {
        addressesPage.selectShippingState(stateCode);
    }

    @When("I clear and enter shipping postcode {string}")
    public void i_clear_and_enter_shipping_postcode(String value) {
        addressesPage.clearAndEnterShippingPostcode(value);
    }

    @When("I clear shipping first name")
    public void i_clear_shipping_first_name() {
        addressesPage.clearShippingFirstName();
    }

    // ── Save Address ───────────────────────────────────────────────────
    @When("I click save address button")
    public void i_click_save_address_button() {
        addressesPage.clickSaveAddress();
    }

    // ── Assertions ─────────────────────────────────────────────────────
    @Then("I should see address saved success message")
    public void i_should_see_address_saved_success_message() {
        Assert.assertTrue(addressesPage.isSuccessMessageDisplayed(),
                "Address saved success message is not displayed!");
    }

    @Then("I should see billing required field error")
    public void i_should_see_billing_required_field_error() {
        Assert.assertTrue(addressesPage.isBillingRequiredFieldErrorDisplayed(),
                "Billing required field error is not displayed!");
    }

    @Then("I should see shipping required field error")
    public void i_should_see_shipping_required_field_error() {
        Assert.assertTrue(addressesPage.isShippingRequiredFieldErrorDisplayed(),
                "Shipping required field error is not displayed!");
    }
}