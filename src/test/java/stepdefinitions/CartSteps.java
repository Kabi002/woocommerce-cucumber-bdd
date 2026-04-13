package stepdefinitions;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import config.ConfigReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.CartPage;
import utils.DriverManager;

public class CartSteps {

    private WebDriver driver;
    private CartPage cartPage;

    public CartSteps() {
        this.driver   = DriverManager.getDriver();
        this.cartPage = new CartPage(driver);
    }

    @And("I navigate to the cart page")
    public void i_navigate_to_the_cart_page() {
        cartPage.navigateToCartPage(ConfigReader.getBaseUrl());
        cartPage.dismissPopups();
    }

    @And("I navigate directly to product and add to cart")
    public void i_navigate_directly_to_product_and_add_to_cart() {
        cartPage.navigateToProductAndAddToCart(ConfigReader.getBaseUrl());
    }

    @Then("I should see product in cart {string}")
    public void i_should_see_product_in_cart(String productName) {
        Assert.assertTrue(cartPage.isProductInCart(productName),
                "Product not found in cart: " + productName);
    }

    @Then("I should see cart total displayed")
    public void i_should_see_cart_total_displayed() {
        Assert.assertTrue(cartPage.isCartTotalDisplayed(),
                "Cart total is not displayed!");
    }

    @When("I increase cart quantity")
    public void i_increase_cart_quantity() {
        cartPage.increaseCartQuantity();
    }

    @When("I decrease cart quantity")
    public void i_decrease_cart_quantity() {
        cartPage.decreaseCartQuantity();
    }

    @Then("I should see updated subtotal")
    public void i_should_see_updated_subtotal() {
        Assert.assertTrue(cartPage.isSubtotalUpdated(),
                "Subtotal is not updated!");
    }

    @Then("I should see complete your order button")
    public void i_should_see_complete_your_order_button() {
        Assert.assertTrue(cartPage.isCheckoutButtonVisible(),
                "Complete your order button is not visible!");
    }

    @When("I click remove product from cart")
    public void i_click_remove_product_from_cart() {
        cartPage.removeProductFromCart();
    }

    @When("I click clear purchase list button")
    public void i_click_clear_purchase_list_button() {
        cartPage.clickClearPurchaseList();
    }

    @Then("I should see empty cart message")
    public void i_should_see_empty_cart_message() {
        Assert.assertTrue(cartPage.isEmptyCartMessageVisible(),
                "Empty cart message is not visible!");
    }
}