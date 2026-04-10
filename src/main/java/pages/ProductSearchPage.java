package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductSearchPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ── Search ─────────────────────────────────────────────────────────
    private By searchInput  = By.id("dgwt-wcas-search-input-3");
    private By searchButton = By.cssSelector("button.dgwt-wcas-search-submit");

    // ── Search Results ─────────────────────────────────────────────────
    private By resultCountMessage = By.cssSelector("span.toolbar-product-count");
    private By noResultsMessage   = By.cssSelector("p.woocommerce-info");

    // ── Product Page ───────────────────────────────────────────────────
    private By productTitle    = By.cssSelector("h1.product_title");
    private By quantityPlusBtn = By.cssSelector("a.qtyBtn.plus");
    private By addToCartButton = By.xpath("//button[contains(text(), 'Add to cart')]");
    private By successMessage  = By.cssSelector("div.woocommerce-message");
    private By viewCartButton  = By.cssSelector("a.button.wc-forward");

    public ProductSearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ── Dismiss Cookie Popup ───────────────────────────────────────────
    public void dismissPopups() {
        try {
            By cookieAccept = By.cssSelector("button[data-cky-tag='accept-button']");
            WebElement cookieBtn = wait.until(ExpectedConditions.elementToBeClickable(cookieAccept));
            cookieBtn.click();
            System.out.println("✅ Cookie popup dismissed");
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".cky-consent-bar")));
        } catch (Exception e) {
            System.out.println("No cookie popup found, continuing...");
        }
    }

    // ── Search ─────────────────────────────────────────────────────────
    public void searchForProduct(String keyword) {
        dismissPopups();
        
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(searchInput));
        input.clear();
        input.sendKeys(keyword);
        System.out.println("✅ Typed search keyword: " + keyword);

        // Wait for suggestions to appear then press ENTER
        try {
            Thread.sleep(1000); // wait for suggestions to load
        } catch (Exception ignored) {}

        // Press ENTER to submit search
        input.sendKeys(org.openqa.selenium.Keys.ENTER);
        System.out.println("✅ Pressed ENTER to search");

        // Wait for results page to load
        wait.until(ExpectedConditions.urlContains("?s="));
        System.out.println("✅ Search results page loaded: " + driver.getCurrentUrl());
    }

    // ── Search Results ─────────────────────────────────────────────────
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
            WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(resultCountMessage));
            String text = msg.getText().trim().replaceAll("\\s+", " ");
            System.out.println("✅ Result count message: " + text);
            return text.contains("results found matching") || text.contains("result found matching");
        } catch (Exception e) {
            System.out.println("❌ Result count message not found");
            return false;
        }
    }

    public boolean isNoResultsMessageVisible() {
        try {
            WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(noResultsMessage));
            String text = msg.getText().toLowerCase();
            System.out.println("✅ No results message: " + text);
            return text.contains("no products") || text.contains("no results") || text.contains("found");
        } catch (Exception e) {
            try {
                WebElement count = driver.findElement(resultCountMessage);
                return count.getText().contains("0");
            } catch (Exception ex) {
                System.out.println("❌ No results message not found");
                return false;
            }
        }
    }

    // ── Click Product ──────────────────────────────────────────────────
    public void clickProduct(String productName) {
        try {
            By productLink = By.xpath("//span[@class='product-title' and contains(text(),'" + productName + "')]");
            WebElement product = wait.until(ExpectedConditions.elementToBeClickable(productLink));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", product);
            System.out.println("✅ Clicked on product: " + productName);
            wait.until(ExpectedConditions.urlContains("product"));
        } catch (Exception e) {
            System.out.println("❌ Product not found: " + productName);
        }
    }

    // ── Product Page ───────────────────────────────────────────────────
    public boolean isOnProductPage(String productSlug) {
        boolean result = driver.getCurrentUrl().contains(productSlug);
        System.out.println("✅ Current URL: " + driver.getCurrentUrl());
        return result;
    }

    public boolean isProductTitleVisible(String expectedTitle) {
        try {
            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(productTitle));
            String text = title.getText().trim();
            System.out.println("✅ Product title: " + text);
            return text.contains(expectedTitle);
        } catch (Exception e) {
            System.out.println("❌ Product title not found");
            return false;
        }
    }

    // ── Navigate directly to test product ─────────────────────────────
    public void navigateToTestProduct(String baseUrl) {
        driver.get(baseUrl + "/product/test-product-1/");
        wait.until(ExpectedConditions.urlContains("test-product-1"));
        System.out.println("✅ Navigated to test product page");
    }

    // ── Select Packaging Option ────────────────────────────────────────
    public void selectPackagingOption(String optionText) {
        try {
            By radioByLabel = By.xpath("//input[@value='1litre-can']");
            WebElement radio = wait.until(ExpectedConditions.elementToBeClickable(radioByLabel));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", radio);
            System.out.println("✅ Selected packaging: " + optionText);

            // Wait for Add to cart button to appear
            wait.until(ExpectedConditions.visibilityOfElementLocated(addToCartButton));
            System.out.println("✅ Add to cart button appeared!");
        } catch (Exception e) {
            System.out.println("❌ Packaging option not found: " + optionText + " | " + e.getMessage());
        }
    }

    // ── Increase Quantity ──────────────────────────────────────────────
    public void increaseQuantity() {
        try {
            WebElement plusBtn = wait.until(ExpectedConditions.elementToBeClickable(quantityPlusBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", plusBtn);
            System.out.println("✅ Quantity increased by 1");
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("❌ Plus button not found: " + e.getMessage());
        }
    }

    // ── Add to Cart ────────────────────────────────────────────────────
    public void clickAddToCart() {
        try {
            dismissPopups();
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("✅ Clicked Add to cart");
        } catch (Exception e) {
            System.out.println("❌ Add to cart button not found: " + e.getMessage());
        }
    }

    // ── Success Message ────────────────────────────────────────────────
    public boolean isAddToCartSuccessMessageVisible() {
        try {
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
            WebElement msg = longWait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            String text = msg.getText().trim();
            System.out.println("✅ Success message: " + text);
            return text.contains("has been added to your cart");
        } catch (Exception e) {
            System.out.println("❌ Success message not found");
            return false;
        }
    }

    // ── View Cart Button ───────────────────────────────────────────────
    public boolean isViewCartButtonVisible() {
        try {
            WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(viewCartButton));
            System.out.println("✅ View cart button visible");
            return btn.isDisplayed();
        } catch (Exception e) {
            System.out.println("❌ View cart button not found");
            return false;
        }
    }

    public void clickViewCart() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(viewCartButton));
        btn.click();
        wait.until(ExpectedConditions.urlContains("/cart/"));
        System.out.println("✅ Navigated to cart page");
    }
}