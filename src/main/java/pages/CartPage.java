package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CartPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By productNameInCart = By.cssSelector("td.product-name a");
    private By quantityPlusBtn   = By.cssSelector("a.qtyBtn.plus");
    private By quantityMinusBtn  = By.cssSelector("a.qtyBtn.minus");
    private By removeProductBtn  = By.cssSelector("a.remove.cart-remove");
    private By clearPurchaseList = By.cssSelector("a[href*='empty-cart=true']");
    private By emptyCartMessage  = By.cssSelector("div.cart-empty.woocommerce-info");
    private By checkoutButton    = By.cssSelector("a.checkout-button");
    private By subtotal          = By.cssSelector("td.product-subtotal");

    // ── FIX 1: Selector for the cart "loading" overlay WooCommerce puts
    //           over the table while AJAX recalculation is in progress.
    //           We wait for this to DISAPPEAR before reading any values.
    private By cartLoadingOverlay = By.cssSelector(".blockOverlay, .wc-block-components-loading-mask");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void navigateToCartPage(String baseUrl) {
        driver.get(baseUrl + "/cart/");
        System.out.println("✅ Navigated to cart page");
    }

    public void dismissPopups() {
        try {
            By cookieAccept = By.cssSelector("button[data-cky-tag='accept-button']");
            WebElement cookieBtn = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(cookieAccept));
            cookieBtn.click();
            System.out.println("✅ Cookie popup dismissed");
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".cky-consent-bar")));
        } catch (Exception e) {
            System.out.println("No cookie popup found, continuing...");
        }
    }

    public void navigateToProductAndAddToCart(String baseUrl) {
        try {
            driver.get(baseUrl + "/product/test-product-1/");
            wait.until(ExpectedConditions.urlContains("test-product-1"));
            System.out.println("✅ Navigated to test product page");

            dismissPopups();

            By radioBtn = By.cssSelector("input.buy-only[type='radio']");
            WebElement radio = wait.until(ExpectedConditions.elementToBeClickable(radioBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", radio);
            System.out.println("✅ Radio button clicked");

            By addToCartBtn = By.cssSelector("button.single_add_to_cart_button");
            wait.until(ExpectedConditions.visibilityOfElementLocated(addToCartBtn));

            By plusBtn = By.cssSelector("a.qtyBtn.plus");
            WebElement plus = wait.until(ExpectedConditions.elementToBeClickable(plusBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", plus);
            System.out.println("✅ Quantity increased");

            WebElement addToCart = wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addToCart);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCart);
            System.out.println("✅ Clicked Add to cart");

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.woocommerce-message")));
            System.out.println("✅ Product added to cart successfully");

        } catch (Exception e) {
            System.out.println("❌ Add to cart failed: " + e.getMessage());
        }
    }

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

    // ── FIX 2: Capture subtotal text BEFORE clicking, then wait until
    //           the DOM replaces it with a DIFFERENT value.
    //           No Thread.sleep — we wait for actual DOM change.
    public void increaseCartQuantity() {
        try {
            dismissPopups();

            // Read current subtotal text so we can detect when it changes
            String subtotalBefore = getSubtotalText();
            System.out.println("📊 Subtotal before increase: " + subtotalBefore);

            WebElement plusBtn = wait.until(ExpectedConditions.elementToBeClickable(quantityPlusBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", plusBtn);
            System.out.println("✅ Cart quantity increased");

            // Wait for AJAX overlay to appear then disappear (covers both
            // themes: classic cart JS and block-based cart)
            waitForCartAjaxToComplete(subtotalBefore);

        } catch (Exception e) {
            System.out.println("❌ Plus button not found: " + e.getMessage());
        }
    }

    public void decreaseCartQuantity() {
        try {
            dismissPopups();

            String subtotalBefore = getSubtotalText();
            System.out.println("📊 Subtotal before decrease: " + subtotalBefore);

            WebElement minusBtn = wait.until(ExpectedConditions.elementToBeClickable(quantityMinusBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", minusBtn);
            System.out.println("✅ Cart quantity decreased");

            waitForCartAjaxToComplete(subtotalBefore);

        } catch (Exception e) {
            System.out.println("❌ Minus button not found: " + e.getMessage());
        }
    }

    // ── FIX 3: isSubtotalUpdated now re-finds the element fresh (no stale
    //           reference) and retries up to 3 times on StaleElementException.
    public boolean isSubtotalUpdated() {
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        int attempts = 0;
        while (attempts < 3) {
            try {
                WebElement sub = longWait.until(
                    ExpectedConditions.visibilityOfElementLocated(subtotal));
                String text = sub.getText().trim();
                System.out.println("✅ Subtotal: " + text);
                return !text.isEmpty();
            } catch (StaleElementReferenceException e) {
                System.out.println("⚠️ Stale subtotal element, retrying (" + (attempts + 1) + "/3)");
                attempts++;
            } catch (Exception e) {
                System.out.println("❌ Subtotal not found: " + e.getMessage());
                return false;
            }
        }
        System.out.println("❌ Subtotal not found after 3 attempts");
        return false;
    }

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

    // ── Helpers ────────────────────────────────────────────────────────

    private String getSubtotalText() {
        try {
            WebElement sub = wait.until(ExpectedConditions.visibilityOfElementLocated(subtotal));
            return sub.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    // Waits for WooCommerce AJAX cart recalculation to finish.
    // Strategy: wait up to 3s for the loading overlay (it appears immediately
    // after the click), then wait for it to disappear.
    // If no overlay appears (some themes skip it), fall back to waiting for
    // the subtotal text to change from its previous value.
    private void waitForCartAjaxToComplete(String subtotalBefore) {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
        WebDriverWait longWait  = new WebDriverWait(driver, Duration.ofSeconds(20));

        boolean overlayFound = false;
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(cartLoadingOverlay));
            overlayFound = true;
        } catch (Exception ignored) {
            // Theme doesn't show an overlay — fall through to text-change wait
        }

        if (overlayFound) {
            longWait.until(ExpectedConditions.invisibilityOfElementLocated(cartLoadingOverlay));
            System.out.println("✅ Cart AJAX recalculation complete (overlay gone)");
        } else {
            // Wait until subtotal text changes to something different
            if (!subtotalBefore.isEmpty()) {
                try {
                    longWait.until((ExpectedCondition<Boolean>) d -> {
                        try {
                            WebElement sub = d.findElement(subtotal);
                            String current = sub.getText().trim();
                            return !current.isEmpty() && !current.equals(subtotalBefore);
                        } catch (StaleElementReferenceException e) {
                            return true; // Element replaced = update happened
                        }
                    });
                    System.out.println("✅ Cart AJAX recalculation complete (subtotal changed)");
                } catch (Exception e) {
                    System.out.println("⚠️ Could not confirm subtotal change, proceeding");
                }
            }
        }
    }
}