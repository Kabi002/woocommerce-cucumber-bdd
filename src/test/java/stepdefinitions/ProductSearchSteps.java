package stepdefinitions;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import config.ConfigReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.ProductSearchPage;
import utils.DriverManager;

public class ProductSearchSteps {

    private WebDriver driver;
    private ProductSearchPage productSearchPage;

    public ProductSearchSteps() {
        this.driver            = DriverManager.getDriver();
        this.productSearchPage = new ProductSearchPage(driver);
    }

    @When("I search for product {string}")
    public void i_search_for_product(String keyword) {
        productSearchPage.searchForProduct(keyword);
    }

    @Then("I should see search results for {string}")
    public void i_should_see_search_results_for(String keyword) {
        Assert.assertTrue(productSearchPage.isSearchResultsVisible(keyword),
                "Search results not visible for: " + keyword);
    }

    @Then("I should see result count message")
    public void i_should_see_result_count_message() {
        Assert.assertTrue(productSearchPage.isResultCountMessageVisible(),
                "Result count message is not visible!");
    }

    @Then("I should see no results message")
    public void i_should_see_no_results_message() {
        Assert.assertTrue(productSearchPage.isNoResultsMessageVisible(),
                "No results message is not visible!");
    }

    @When("I click on product {string}")
    public void i_click_on_product(String productName) {
        productSearchPage.clickProduct(productName);
    }

    @Then("I should be on product page {string}")
    public void i_should_be_on_product_page(String productSlug) {
        Assert.assertTrue(productSearchPage.isOnProductPage(productSlug),
                "Not on product page: " + productSlug);
    }

    @Then("I should see product title {string}")
    public void i_should_see_product_title(String expectedTitle) {
        Assert.assertTrue(productSearchPage.isProductTitleVisible(expectedTitle),
                "Product title not visible: " + expectedTitle);
    }

    @When("I navigate directly to product page")
    public void i_navigate_directly_to_product_page() {
        productSearchPage.navigateToTestProduct(ConfigReader.getBaseUrl());
    }

    @And("I select packaging option {string}")
    public void i_select_packaging_option(String optionText) {
        productSearchPage.selectPackagingOption(optionText);
    }

    @And("I increase quantity by 1")
    public void i_increase_quantity_by_1() {
        productSearchPage.increaseQuantity();
    }

    @And("I click add to cart button")
    public void i_click_add_to_cart_button() {
        productSearchPage.clickAddToCart();
    }

    @Then("I should see add to cart success message")
    public void i_should_see_add_to_cart_success_message() {
        Assert.assertTrue(productSearchPage.isAddToCartSuccessMessageVisible(),
                "Add to cart success message is not visible!");
    }

    @Then("I should see view cart button")
    public void i_should_see_view_cart_button() {
        Assert.assertTrue(productSearchPage.isViewCartButtonVisible(),
                "View cart button is not visible!");
    }
}