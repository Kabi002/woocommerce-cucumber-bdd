package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CartPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ── Cart Page Locators ─────────────────────────────────────────────
    private By productNameInCart = By.cssSelector("td.product-name a");
    private By quantityPlusBtn   = By.cssSelector("a.qtyBtn.plus");
    private By quantityMinusBtn  = By.cssSelector("a.qtyBtn.minus");
    private By removeProductBtn  = By.cssSelector("a.remove.cart-remove");
    private By clearPurchaseList = By.cssSelector("a[href*='empty-cart=true']");
    private By emptyCartMessage  = By.cssSelector("div.cart-empty.woocommerce-info");
    private By checkoutButton    = By.cssSelector("a.checkout-button");
    private By subtotal          = By.cssSelector("td.product-subtotal");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ── Navigate ───────────────────────────────────────────────────────
    public void navigateToCartPage(String baseUrl) {
        driver.get(baseUrl + "/cart/");
        System.out.println("✅ Navigated to cart page");
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

    // ── Add Product to Cart ────────────────────────────────────────────
    public void navigateToProductAndAddToCart(String baseUrl) {
        try {
            driver.get(baseUrl + "/product/test-product-1/");
            wait.until(ExpectedConditions.urlContains("test-product-1"));
            System.out.println("✅ Navigated to test product page");

            // Dismiss popup
            dismissPopups();

            // Click radio button
            By radioBtn = By.cssSelector("input.buy-only[type='radio']");
            WebElement radio = wait.until(ExpectedConditions.elementToBeClickable(radioBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", radio);
            System.out.println("✅ Radio button clicked");

            // Wait for add to cart button to appear
            By addToCartBtn = By.cssSelector("button.single_add_to_cart_button");
            wait.until(ExpectedConditions.visibilityOfElementLocated(addToCartBtn));

            // Increase quantity by 1
            By plusBtn = By.cssSelector("a.qtyBtn.plus");
            WebElement plus = wait.until(ExpectedConditions.elementToBeClickable(plusBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", plus);
            Thread.sleep(500);
            System.out.println("✅ Quantity increased");

            // Click add to cart
            WebElement addToCart = wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addToCart);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCart);
            System.out.println("✅ Clicked Add to cart");

            // Wait for success message
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.woocommerce-message")));
            System.out.println("✅ Product added to cart successfully");

        } catch (Exception e) {
            System.out.println("❌ Add to cart failed: " + e.getMessage());
        }
    }

    // ── Verify Product in Cart ─────────────────────────────────────────
    public boolean isProductInCart(String productName) {
        try {
            WebElement product = wait.until(ExpectedConditions.visibilityOfElementLocated(productNameInCart));
            String text = product.getText().trim();
            System.out.println("✅ Product in cart: " + text);
            return text.contains(productName);
        } catch (Exception e) {
            System.out.println("❌ Product not found in cart");
            return false;
        }
    }

    // ── Cart Total ─────────────────────────────────────────────────────
    public boolean isCartTotalDisplayed() {
        try {
            By totalTitle = By.cssSelector("span.cart-subtotal-title");
            WebElement total = wait.until(ExpectedConditions.visibilityOfElementLocated(totalTitle));
            System.out.println("✅ Cart total displayed");
            return total.isDisplayed();
        } catch (Exception e) {
            System.out.println("❌ Cart total not found");
            return false;
        }
    }

    // ── Increase Quantity ──────────────────────────────────────────────
    public void increaseCartQuantity() {
        try {
            dismissPopups();
            WebElement plusBtn = wait.until(ExpectedConditions.elementToBeClickable(quantityPlusBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", plusBtn);
            System.out.println("✅ Cart quantity increased");
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println("❌ Plus button not found: " + e.getMessage());
        }
    }

    // ── Decrease Quantity ──────────────────────────────────────────────
    public void decreaseCartQuantity() {
        try {
            dismissPopups();
            WebElement minusBtn = wait.until(ExpectedConditions.elementToBeClickable(quantityMinusBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", minusBtn);
            System.out.println("✅ Cart quantity decreased");
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println("❌ Minus button not found: " + e.getMessage());
        }
    }

    // ── Verify Updated Subtotal ────────────────────────────────────────
    public boolean isSubtotalUpdated() {
        try {
            WebElement sub = wait.until(ExpectedConditions.visibilityOfElementLocated(subtotal));
            System.out.println("✅ Subtotal: " + sub.getText());
            return sub.isDisplayed();
        } catch (Exception e) {
            System.out.println("❌ Subtotal not found");
            return false;
        }
    }

    // ── Checkout Button ────────────────────────────────────────────────
    public boolean isCheckoutButtonVisible() {
        try {
            WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(checkoutButton));
            System.out.println("✅ Checkout button visible: " + btn.getText());
            return btn.isDisplayed();
        } catch (Exception e) {
            System.out.println("❌ Checkout button not found");
            return false;
        }
    }

    // ── Remove Product ─────────────────────────────────────────────────
    public void removeProductFromCart() {
        try {
            dismissPopups();
            WebElement removeBtn = wait.until(ExpectedConditions.elementToBeClickable(removeProductBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", removeBtn);
            System.out.println("✅ Clicked remove product");
        } catch (Exception e) {
            System.out.println("❌ Remove button not found: " + e.getMessage());
        }
    }

    // ── Clear Purchase List ────────────────────────────────────────────
    public void clickClearPurchaseList() {
        try {
            dismissPopups();
            WebElement clearBtn = wait.until(ExpectedConditions.elementToBeClickable(clearPurchaseList));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", clearBtn);
            System.out.println("✅ Clicked clear purchase list");
        } catch (Exception e) {
            System.out.println("❌ Clear purchase list button not found: " + e.getMessage());
        }
    }

    // ── Empty Cart Message ─────────────────────────────────────────────
    public boolean isEmptyCartMessageVisible() {
        try {
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
            WebElement msg = longWait.until(ExpectedConditions.visibilityOfElementLocated(emptyCartMessage));
            String text = msg.getText().trim();
            System.out.println("✅ Empty cart message: " + text);
            return text.contains("currently empty") || text.contains("empty");
        } catch (Exception e) {
            System.out.println("❌ Empty cart message not found");
            return false;
        }
    }
}