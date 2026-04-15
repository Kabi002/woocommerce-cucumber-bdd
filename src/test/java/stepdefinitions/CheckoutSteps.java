package stepdefinitions;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.CheckoutPage;
import utils.DriverManager;

public class CheckoutSteps {

    private WebDriver driver;
    private CheckoutPage checkoutPage;

    public CheckoutSteps() {
        this.driver       = DriverManager.getDriver();
        this.checkoutPage = new CheckoutPage(driver);
    }

    // ── Navigate to Checkout ───────────────────────────────────────────
    @And("I click complete your order button")
    public void i_click_complete_your_order_button() {
        checkoutPage.clickCompleteYourOrder();
    }

    // ── Billing Details ────────────────────────────────────────────────
    @Then("I should be on checkout page")
    public void i_should_be_on_checkout_page() {
        Assert.assertTrue(checkoutPage.isOnCheckoutPage(),
                "Not on checkout page! URL: " + driver.getCurrentUrl());
    }

    @And("I should see billing details pre-filled")
    public void i_should_see_billing_details_pre_filled() {
        Assert.assertTrue(checkoutPage.isBillingDetailsPreFilled(),
                "Billing details are not pre-filled!");
    }

    // ── Order Summary ──────────────────────────────────────────────────
    @Then("I should see order summary with product {string}")
    public void i_should_see_order_summary_with_product(String productName) {
        Assert.assertTrue(checkoutPage.isOrderSummaryProductVisible(productName),
                "Product not visible in order summary: " + productName);
    }

    @And("I should see order total displayed")
    public void i_should_see_order_total_displayed() {
        Assert.assertTrue(checkoutPage.isOrderTotalDisplayed(),
                "Order total is not displayed!");
    }

    // ── Navigation Buttons ─────────────────────────────────────────────
    @When("I click to shipping button")
    public void i_click_to_shipping_button() {
        checkoutPage.clickToShipping();
    }

    @Then("I should see shipping details page")
    public void i_should_see_shipping_details_page() {
        Assert.assertTrue(checkoutPage.isShippingDetailsPageVisible(),
                "Shipping details page is not visible!");
    }

    @When("I click to comments button")
    public void i_click_to_comments_button() {
        checkoutPage.clickToComments();
    }

    @Then("I should see order comments page")
    public void i_should_see_order_comments_page() {
        Assert.assertTrue(checkoutPage.isOrderCommentsPageVisible(),
                "Order comments page is not visible!");
    }

    @When("I click to payment button")
    public void i_click_to_payment_button() {
        checkoutPage.clickToPayment();
    }

    @Then("I should see finalize order page")
    public void i_should_see_finalize_order_page() {
        Assert.assertTrue(checkoutPage.isFinalizeOrderPageVisible(),
                "Finalize order page is not visible!");
    }

    // ── Order Comments ─────────────────────────────────────────────────
    @When("I enter purchase order number {string}")
    public void i_enter_purchase_order_number(String poNumber) {
        checkoutPage.enterPurchaseOrderNumber(poNumber);
    }

    @And("I enter order notes {string}")
    public void i_enter_order_notes(String notes) {
        checkoutPage.enterOrderNotes(notes);
    }

    // ── Payment ────────────────────────────────────────────────────────
    @When("I select payment method {string}")
    public void i_select_payment_method(String method) {
        checkoutPage.selectPaymentMethod(method);
    }

    @And("I accept terms and conditions")
    public void i_accept_terms_and_conditions() {
        checkoutPage.acceptTermsAndConditions();
    }

    @And("I click place order button")
    public void i_click_place_order_button() {
        checkoutPage.clickPlaceOrder();
    }

    // ── Order Confirmation ─────────────────────────────────────────────
    @Then("I should see order confirmation message")
    public void i_should_see_order_confirmation_message() {
        Assert.assertTrue(checkoutPage.isOrderConfirmationMessageVisible(),
                "Order confirmation message is not visible!");
    }

    @And("I should see order number displayed")
    public void i_should_see_order_number_displayed() {
        Assert.assertTrue(checkoutPage.isOrderNumberDisplayed(),
                "Order number is not displayed!");
    }
}
