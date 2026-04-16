package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProductSearchPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Search
    private By searchInputs = By.cssSelector("input.dgwt-wcas-search-input");

    // Search Results
    private By resultCountMessage = By.cssSelector("span.toolbar-product-count");
    private By noResultsMessage = By.cssSelector("p.woocommerce-info");

    // Product Page
    private By productTitle = By.cssSelector("h1.product_title");
    private By quantityPlusBtn = By.cssSelector("a.qtyBtn.plus");
    private By addToCartButton = By.cssSelector("button.single_add_to_cart_button");
    private By successMessage = By.cssSelector("div.woocommerce-message");
    private By viewCartButton = By.cssSelector("a.button.wc-forward");

    public ProductSearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ===============================
    // Dismiss Popups / Cookie Banner
    // ===============================
    public void dismissPopups() {
        try {
            By cookieAccept = By.cssSelector("button[data-cky-tag='accept-button']");
            WebElement cookieBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(cookieAccept)
            );

            cookieBtn.click();

            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector(".cky-consent-bar")
            ));

            System.out.println("✅ Cookie popup dismissed");

        } catch (Exception e) {
            System.out.println("No cookie popup found, continuing...");
        }
    }

    // ===============================
    // Search Product
    // ===============================
    public void searchForProduct(String keyword) {
        try {
            dismissPopups();

            WebElement input = wait.until(driver -> {
                List<WebElement> inputs = driver.findElements(searchInputs);

                for (WebElement el : inputs) {
                    if (el.isDisplayed() && el.isEnabled()) {
                        return el;
                    }
                }

                return null;
            });

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});",
                    input
            );

            input.clear();
            input.sendKeys(keyword);

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".dgwt-wcas-suggestions-wrapp")
            ));

            input.sendKeys(Keys.ENTER);

            wait.until(ExpectedConditions.urlContains("?s="));

            System.out.println("✅ Search completed for: " + keyword);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Search failed for keyword: " + keyword, e
            );
        }
    }

    // ===============================
    // Search Result Validation
    // ===============================
    public boolean isSearchResultsVisible(String keyword) {
        try {
            By productCards = By.cssSelector("div.product-name");

            wait.until(ExpectedConditions.visibilityOfElementLocated(productCards));

            System.out.println("✅ Search results visible for: " + keyword);
            return true;

        } catch (Exception e) {
            System.out.println("❌ No search results visible for: " + keyword);
            return false;
        }
    }

    public boolean isResultCountMessageVisible() {
        try {
            WebElement msg = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(resultCountMessage)
            );

            String text = msg.getText().trim().replaceAll("\\s+", " ");

            System.out.println("✅ Result count message: " + text);

            return text.contains("results found matching")
                    || text.contains("result found matching");

        } catch (Exception e) {
            System.out.println("❌ Result count message not found");
            return false;
        }
    }

    public boolean isNoResultsMessageVisible() {
        try {
            WebElement countMsg = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(resultCountMessage)
            );

            String text = countMsg.getText().trim().toLowerCase();

            System.out.println("No Results Validation Text: " + text);

            return text.contains("0 results found");

        } catch (Exception e) {
            System.out.println("❌ No result count message found");
            return false;
        }
    }

    // ===============================
    // Click Product
    // ===============================
    public void clickProduct(String productName) {
        try {
            By productLink = By.xpath(
                    "//span[@class='product-title' and contains(text(),'"
                            + productName + "')]"
            );

            WebElement product = wait.until(
                    ExpectedConditions.elementToBeClickable(productLink)
            );

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();",
                    product
            );

            wait.until(ExpectedConditions.urlContains("product"));

            System.out.println("✅ Clicked product: " + productName);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Product not found/clickable: " + productName, e
            );
        }
    }

    // ===============================
    // Product Page Validations
    // ===============================
    public boolean isOnProductPage(String productSlug) {
        return driver.getCurrentUrl().contains(productSlug);
    }

    public boolean isProductTitleVisible(String expectedTitle) {
        try {
            WebElement title = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(productTitle)
            );

            return title.getText().trim().contains(expectedTitle);

        } catch (Exception e) {
            return false;
        }
    }

    // ===============================
    // Navigate Direct Product
    // ===============================
    public void navigateToTestProduct(String baseUrl) {
        driver.get(baseUrl + "/product/test-product-1");
        System.out.println("✅ Navigated directly to test product");
    }

    // ===============================
    // Select Packaging / Variation
    // ===============================
    public void selectPackagingOption(String optionText) {
        try {
            By optionLocator = By.xpath(
                    "//label[contains(normalize-space(),'"
                            + optionText + "')]"
            );

            WebElement option = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(optionLocator)
            );

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});",
                    option
            );

            wait.until(ExpectedConditions.elementToBeClickable(option));

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();",
                    option
            );

            System.out.println("✅ Packaging selected: " + optionText);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Packaging option not found: " + optionText, e
            );
        }
    }

    // ===============================
    // Quantity Increase
    // ===============================
    public void increaseQuantity() {
        try {
            WebElement plusBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(quantityPlusBtn)
            );

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();",
                    plusBtn
            );

            System.out.println("✅ Quantity increased");

        } catch (Exception e) {
            throw new RuntimeException("Quantity increase failed", e);
        }
    }

    // ===============================
    // Add To Cart
    // ===============================
    public void clickAddToCart() {
        try {
            dismissPopups();

            WebElement btn = wait.until(driver -> {
                WebElement el = driver.findElement(addToCartButton);

                return (el.isDisplayed() && el.isEnabled())
                        ? el
                        : null;
            });

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});",
                    btn
            );

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();",
                    btn
            );

            System.out.println("✅ Clicked Add to Cart");

        } catch (Exception e) {
            throw new RuntimeException("Add To Cart failed", e);
        }
    }

    // ===============================
    // Success Message
    // ===============================
    public boolean isAddToCartSuccessMessageVisible() {
        try {
            WebDriverWait longWait = new WebDriverWait(
                    driver,
                    Duration.ofSeconds(30)
            );

            WebElement msg = longWait.until(
                    ExpectedConditions.visibilityOfElementLocated(successMessage)
            );

            return msg.getText().contains("has been added to your cart");

        } catch (Exception e) {
            return false;
        }
    }

    // ===============================
    // View Cart
    // ===============================
    public boolean isViewCartButtonVisible() {
        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(viewCartButton)
            ).isDisplayed();

        } catch (Exception e) {
            return false;
        }
    }

    public void clickViewCart() {
        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(viewCartButton)
        );

        btn.click();

        wait.until(ExpectedConditions.urlContains("/cart/"));
    }
}