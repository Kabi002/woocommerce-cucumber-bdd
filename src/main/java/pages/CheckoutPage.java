package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ── Checkout URL ───────────────────────────────────────────────────
    private By checkoutPageTitle    = By.cssSelector("h1.entry-title, h1, .page-title");

    // ── Billing Details ────────────────────────────────────────────────
    private By billingFirstName     = By.id("billing_first_name");
    private By billingLastName      = By.id("billing_last_name");
    private By billingCompany       = By.id("billing_company");
    private By billingEmail         = By.id("billing_email");

    // ── Navigation Buttons ─────────────────────────────────────────────
    private By toShippingBtn        = By.cssSelector("button.btnNext[name='Continue']");
    private By toCommentsBtn        = By.xpath("//button[contains(@class,'btnNext') and contains(text(),'comments') or contains(.,'comments')]");
    private By toPaymentBtn         = By.xpath("//button[contains(@class,'btnNext') and contains(.,'payment')]");

    // ── Shipping Details ───────────────────────────────────────────────
    private By shippingDetailsTitle = By.xpath("//h3[contains(text(),'Shipping details')]");
    private By shipToDifferentAddr  = By.id("ship-to-different-address-checkbox");

    // ── Order Comments ─────────────────────────────────────────────────
    private By orderCommentsTitle   = By.xpath("//h3[contains(text(),'Your order details')]");
    private By poNumberField        = By.id("po_number");
    private By orderNotesField      = By.id("order_comments");

    // ── Finalize Order ─────────────────────────────────────────────────
    private By finalizeOrderTitle   = By.xpath("//h3[contains(text(),'Finalize order')]");
    private By payByInvoiceRadio    = By.id("payment_method_jetpack_custom_gateway");
    private By termsCheckbox        = By.id("terms");
    private By placeOrderBtn        = By.id("place_order");

    // ── Order Summary ──────────────────────────────────────────────────
    private By orderSummaryProduct  = By.cssSelector("td.product-name");
    private By orderTotal           = By.cssSelector("tr.order-total td strong");

    // ── Order Confirmation ─────────────────────────────────────────────
    private By confirmationMessage  = By.cssSelector("p.woocommerce-notice--success");
    private By orderNumber          = By.cssSelector("p.text-order.badge.bg-success");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(20));
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

    // ── Click Complete Your Order from Cart ────────────────────────────
    public void clickCompleteYourOrder() {
        try {
            By checkoutBtn = By.cssSelector("a.checkout-button");
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(checkoutBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            wait.until(ExpectedConditions.urlContains("/checkout/"));
            System.out.println("✅ Navigated to checkout page");
        } catch (Exception e) {
            System.out.println("❌ Complete your order button not found: " + e.getMessage());
        }
    }

    // ── Verify Checkout Page ───────────────────────────────────────────
    public boolean isOnCheckoutPage() {
        boolean result = driver.getCurrentUrl().contains("/checkout/");
        System.out.println("✅ Current URL: " + driver.getCurrentUrl());
        return result;
    }

    // ── Verify Billing Details Pre-filled ─────────────────────────────
    public boolean isBillingDetailsPreFilled() {
        try {
            WebElement firstName = wait.until(ExpectedConditions.visibilityOfElementLocated(billingFirstName));
            WebElement lastName  = driver.findElement(billingLastName);
            WebElement email     = driver.findElement(billingEmail);

            String fn = firstName.getAttribute("value");
            String ln = lastName.getAttribute("value");
            String em = email.getAttribute("value");

            System.out.println("✅ Billing First Name: " + fn);
            System.out.println("✅ Billing Last Name: " + ln);
            System.out.println("✅ Billing Email: " + em);

            return !fn.isEmpty() && !ln.isEmpty() && !em.isEmpty();
        } catch (Exception e) {
            System.out.println("❌ Billing details not pre-filled: " + e.getMessage());
            return false;
        }
    }

    // ── Verify Order Summary ───────────────────────────────────────────
    public boolean isOrderSummaryProductVisible(String productName) {
        try {
            WebElement product = wait.until(ExpectedConditions.visibilityOfElementLocated(orderSummaryProduct));
            String text = product.getText().trim();
            System.out.println("✅ Order summary product: " + text);
            return text.contains(productName);
        } catch (Exception e) {
            System.out.println("❌ Order summary product not found");
            return false;
        }
    }

    public boolean isOrderTotalDisplayed() {
        try {
            // Check total in right side order summary
            By totalAmount = By.xpath("//*[contains(@class,'order-total') or contains(text(),'Total')]");
            WebElement total = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("tr.order-total, .cart-subtotal")));
            System.out.println("✅ Order total displayed");
            return total.isDisplayed();
        } catch (Exception e) {
            try {
                // Alternative locator
                By altTotal = By.xpath("//td[contains(text(),'Total') or contains(@data-title,'Total')]");
                WebElement total = driver.findElement(altTotal);
                return total.isDisplayed();
            } catch (Exception ex) {
                System.out.println("❌ Order total not found");
                return false;
            }
        }
    }

    // ── Navigation Buttons ─────────────────────────────────────────────
    public void clickToShipping() {
        try {
            dismissPopups();
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(toShippingBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("✅ Clicked To shipping button");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("❌ To shipping button not found: " + e.getMessage());
        }
    }

    public void clickToComments() {
        try {
            dismissPopups();
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(toCommentsBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("✅ Clicked To comments button");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("❌ To comments button not found: " + e.getMessage());
        }
    }

    public void clickToPayment() {
        try {
            dismissPopups();
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(toPaymentBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("✅ Clicked To payment button");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("❌ To payment button not found: " + e.getMessage());
        }
    }

    // ── Verify Pages ───────────────────────────────────────────────────
    public boolean isShippingDetailsPageVisible() {
        try {
            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(shippingDetailsTitle));
            System.out.println("✅ Shipping details page visible");
            return title.isDisplayed();
        } catch (Exception e) {
            // Try alternative
            try {
                By altTitle = By.xpath("//*[contains(text(),'Shipping details')]");
                WebElement alt = wait.until(ExpectedConditions.visibilityOfElementLocated(altTitle));
                return alt.isDisplayed();
            } catch (Exception ex) {
                System.out.println("❌ Shipping details page not visible");
                return false;
            }
        }
    }

    public boolean isOrderCommentsPageVisible() {
        try {
            WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(poNumberField));
            System.out.println("✅ Order comments page visible");
            return field.isDisplayed();
        } catch (Exception e) {
            System.out.println("❌ Order comments page not visible");
            return false;
        }
    }

    public boolean isFinalizeOrderPageVisible() {
        try {
            WebElement radio = wait.until(ExpectedConditions.visibilityOfElementLocated(payByInvoiceRadio));
            System.out.println("✅ Finalize order page visible");
            return radio.isDisplayed();
        } catch (Exception e) {
            System.out.println("❌ Finalize order page not visible");
            return false;
        }
    }

    // ── Order Comments ─────────────────────────────────────────────────
    public void enterPurchaseOrderNumber(String poNumber) {
        try {
            WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(poNumberField));
            field.clear();
            field.sendKeys(poNumber);
            System.out.println("✅ Entered PO number: " + poNumber);
        } catch (Exception e) {
            System.out.println("❌ PO number field not found: " + e.getMessage());
        }
    }

    public void enterOrderNotes(String notes) {
        try {
            WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(orderNotesField));
            field.clear();
            field.sendKeys(notes);
            System.out.println("✅ Entered order notes: " + notes);
        } catch (Exception e) {
            System.out.println("❌ Order notes field not found: " + e.getMessage());
        }
    }

    // ── Payment Method ─────────────────────────────────────────────────
    public void selectPaymentMethod(String method) {
        try {
            // Pay by invoice is default selected
            if (method.equalsIgnoreCase("Pay by invoice")) {
                WebElement radio = wait.until(ExpectedConditions.elementToBeClickable(payByInvoiceRadio));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", radio);
                System.out.println("✅ Selected payment method: " + method);
            }
        } catch (Exception e) {
            System.out.println("❌ Payment method not found: " + e.getMessage());
        }
    }

    // ── Terms & Conditions ─────────────────────────────────────────────
    public void acceptTermsAndConditions() {
        try {
            WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(termsCheckbox));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
            System.out.println("✅ Accepted terms and conditions");
        } catch (Exception e) {
            System.out.println("❌ Terms checkbox not found: " + e.getMessage());
        }
    }

    // ── Place Order ────────────────────────────────────────────────────
    public void clickPlaceOrder() {
        try {
            dismissPopups();
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(placeOrderBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("✅ Clicked Place order");
            // Wait for confirmation page
            wait.until(ExpectedConditions.urlContains("order-received"));
            System.out.println("✅ Order placed successfully!");
        } catch (Exception e) {
            System.out.println("❌ Place order button not found: " + e.getMessage());
        }
    }

    // ── Order Confirmation ─────────────────────────────────────────────
    public boolean isOrderConfirmationMessageVisible() {
        try {
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
            WebElement msg = longWait.until(ExpectedConditions.visibilityOfElementLocated(confirmationMessage));
            String text = msg.getText().trim();
            System.out.println("✅ Confirmation message: " + text);
            return text.contains("Thank you") || text.contains("order has been received");
        } catch (Exception e) {
            System.out.println("❌ Confirmation message not found");
            return false;
        }
    }

    public boolean isOrderNumberDisplayed() {
        try {
            WebElement orderNum = wait.until(ExpectedConditions.visibilityOfElementLocated(orderNumber));
            String text = orderNum.getText().trim();
            System.out.println("✅ Order number: " + text);
            return text.contains("Order number");
        } catch (Exception e) {
            System.out.println("❌ Order number not found");
            return false;
        }
    }
}
