package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AddressesPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ── Addresses Page ─────────────────────────────────────────────────
    private By billingAddressSection  = By.xpath("//h2[contains(text(),'Billing address')]");
    private By shippingAddressSection = By.xpath("//h2[contains(text(),'Shipping address')]");
    private By editBillingLink        = By.cssSelector("a.edit[href*='/edit-address/billing/']");
    private By editShippingLink       = By.cssSelector("a.edit[href*='/edit-address/shipping/']");

    // ── Billing Address Fields ─────────────────────────────────────────
    private By billingFirstName  = By.id("billing_first_name");
    private By billingLastName   = By.id("billing_last_name");
    private By billingCompany    = By.id("billing_company");
    private By billingAddress1   = By.id("billing_address_1");
    private By billingCity       = By.id("billing_city");
    private By billingPostcode   = By.id("billing_postcode");
    private By billingPhone      = By.id("billing_phone");
    private By billingEmail      = By.id("billing_email");
    private By billingCountrySelect = By.id("billing_country");
    private By billingStateSelect   = By.id("billing_state");

    // ── Shipping Address Fields ────────────────────────────────────────
    private By shippingFirstName = By.id("shipping_first_name");
    private By shippingLastName  = By.id("shipping_last_name");
    private By shippingCompany   = By.id("shipping_company");
    private By shippingAddress1  = By.id("shipping_address_1");
    private By shippingCity      = By.id("shipping_city");
    private By shippingPostcode  = By.id("shipping_postcode");
    private By shippingCountrySelect = By.id("shipping_country");
    private By shippingStateSelect   = By.id("shipping_state");

    // ── Save Button & Messages ─────────────────────────────────────────
    private By saveAddressButton = By.cssSelector("button[name='save_address']");
    private By successMessage    = By.cssSelector("div.woocommerce-message");
    private By errorMessage      = By.cssSelector("ul.woocommerce-error");

    public AddressesPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ── Navigate ───────────────────────────────────────────────────────
    public void navigateToAddressesPage(String baseUrl) {
        driver.get(baseUrl + "/my-account/edit-address/");
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

    // ── Select2 Dropdown Helper ────────────────────────────────────────
    // Since country/state use Select2 (hidden <select>),
    // we use JavaScript to set value and trigger change event
    private void selectDropdownByValue(By selectLocator, String value) {
        try {
            WebElement selectElement = driver.findElement(selectLocator);
            ((JavascriptExecutor) driver).executeScript(
                "var select = arguments[0];" +
                "var value = arguments[1];" +
                "select.value = value;" +
                "var event = new Event('change', { bubbles: true });" +
                "select.dispatchEvent(event);",
                selectElement, value
            );
            System.out.println("✅ Dropdown selected: " + value);
            // Wait for page to update after country selection
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("❌ Dropdown selection failed: " + e.getMessage());
        }
    }

    // ── Billing Country & State ────────────────────────────────────────
    public void selectBillingCountry(String countryCode) {
        // e.g. countryCode = "IN" for India, "NL" for Netherlands
        selectDropdownByValue(billingCountrySelect, countryCode);
    }

    public void selectBillingState(String stateCode) {
        // e.g. stateCode = "TN" for Tamil Nadu
        // Wait for state dropdown to appear after country selection
        wait.until(ExpectedConditions.visibilityOfElementLocated(billingStateSelect));
        selectDropdownByValue(billingStateSelect, stateCode);
    }

    // ── Shipping Country & State ───────────────────────────────────────
    public void selectShippingCountry(String countryCode) {
        selectDropdownByValue(shippingCountrySelect, countryCode);
    }

    public void selectShippingState(String stateCode) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(shippingStateSelect));
        selectDropdownByValue(shippingStateSelect, stateCode);
    }

    // ── Addresses Page Visibility ──────────────────────────────────────
    public boolean isBillingAddressSectionVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(billingAddressSection)).isDisplayed();
        } catch (Exception e) {
            System.out.println("❌ Billing address section not visible");
            return false;
        }
    }

    public boolean isShippingAddressSectionVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(shippingAddressSection)).isDisplayed();
        } catch (Exception e) {
            System.out.println("❌ Shipping address section not visible");
            return false;
        }
    }

    public boolean isEditBillingLinkVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(editBillingLink)).isDisplayed();
        } catch (Exception e) {
            System.out.println("❌ Edit billing link not visible");
            return false;
        }
    }

    public boolean isEditShippingLinkVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(editShippingLink)).isDisplayed();
        } catch (Exception e) {
            System.out.println("❌ Edit shipping link not visible");
            return false;
        }
    }

    // ── Click Edit Links ───────────────────────────────────────────────
    public void clickEditBillingAddress() {
        try {
            dismissPopups();

            WebElement billingLink = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(editBillingLink)
            );

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});",
                    billingLink
            );

            Thread.sleep(500);

            try {
                billingLink.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].click();",
                        billingLink
                );
            }

            wait.until(ExpectedConditions.urlContains("/edit-address/billing/"));
            System.out.println("✅ Navigated to billing address page");

        } catch (Exception e) {
            throw new RuntimeException("Unable to click Billing Address Edit Link", e);
        }
    }

    public void clickEditShippingAddress() {
        try {
            dismissPopups();

            WebElement shippingLink = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(editShippingLink)
            );

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});",
                    shippingLink
            );

            Thread.sleep(500);

            try {
                shippingLink.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].click();",
                        shippingLink
                );
            }

            wait.until(ExpectedConditions.urlContains("/edit-address/shipping/"));
            System.out.println("✅ Navigated to shipping address page");

        } catch (Exception e) {
            throw new RuntimeException("Unable to click Shipping Address Edit Link", e);
        }
    }

    // ── URL Verification ──────────────────────────────────────────────
    public boolean isOnBillingAddressPage() {
        return driver.getCurrentUrl().contains("/edit-address/billing/");
    }

    public boolean isOnShippingAddressPage() {
        return driver.getCurrentUrl().contains("/edit-address/shipping/");
    }

    // ── Billing Address Actions ────────────────────────────────────────
    public void clearAndEnterBillingFirstName(String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(billingFirstName));
        field.clear();
        field.sendKeys(value);
    }

    public void clearAndEnterBillingLastName(String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(billingLastName));
        field.clear();
        field.sendKeys(value);
    }

    public void clearAndEnterBillingCompany(String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(billingCompany));
        field.clear();
        field.sendKeys(value);
    }

    public void clearAndEnterBillingAddress(String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(billingAddress1));
        field.clear();
        field.sendKeys(value);
    }

    public void clearAndEnterBillingCity(String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(billingCity));
        field.clear();
        field.sendKeys(value);
    }

    public void clearAndEnterBillingPostcode(String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(billingPostcode));
        field.clear();
        field.sendKeys(value);
    }

    public void clearAndEnterBillingPhone(String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(billingPhone));
        field.clear();
        field.sendKeys(value);
    }

    public void clearAndEnterBillingEmail(String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(billingEmail));
        field.clear();
        field.sendKeys(value);
    }

    public void clearBillingFirstName() {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(billingFirstName));
        field.clear();
    }

    // ── Shipping Address Actions ───────────────────────────────────────
    public void clearAndEnterShippingFirstName(String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(shippingFirstName));
        field.clear();
        field.sendKeys(value);
    }

    public void clearAndEnterShippingLastName(String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(shippingLastName));
        field.clear();
        field.sendKeys(value);
    }

    public void clearAndEnterShippingCompany(String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(shippingCompany));
        field.clear();
        field.sendKeys(value);
    }

    public void clearAndEnterShippingAddress(String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(shippingAddress1));
        field.clear();
        field.sendKeys(value);
    }

    public void clearAndEnterShippingCity(String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(shippingCity));
        field.clear();
        field.sendKeys(value);
    }

    public void clearAndEnterShippingPostcode(String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(shippingPostcode));
        field.clear();
        field.sendKeys(value);
    }

    public void clearShippingFirstName() {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(shippingFirstName));
        field.clear();
    }

    // ── Click Save Address ─────────────────────────────────────────────
    public void clickSaveAddress() {
        try {
            // Dismiss cookie popup if visible
            dismissPopups();
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(saveAddressButton));
            // Scroll into view first
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
            // JS click to bypass any overlay
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        } catch (Exception e) {
            System.out.println("❌ Save address button click failed: " + e.getMessage());
        }
    }

    // ── Success Message ────────────────────────────────────────────────
    public boolean isSuccessMessageDisplayed() {
        try {
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
            WebElement msg = longWait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            String text = msg.getText().trim();
            System.out.println("✅ Success message: " + text);
            return text.contains("Address changed successfully") || text.contains("successfully");
        } catch (Exception e) {
            System.out.println("❌ Success message not found | URL: " + driver.getCurrentUrl());
            return false;
        }
    }

    // ── Error Messages ─────────────────────────────────────────────────
    public boolean isBillingRequiredFieldErrorDisplayed() {
        try {
            WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            String text = msg.getText().toLowerCase();
            System.out.println("✅ Billing error: " + text);
            return text.contains("first name") || text.contains("required") || text.contains("billing");
        } catch (Exception e) {
            System.out.println("❌ Billing required field error not found");
            return false;
        }
    }

    public boolean isShippingRequiredFieldErrorDisplayed() {
        try {
            WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            String text = msg.getText().toLowerCase();
            System.out.println("✅ Shipping error: " + text);
            return text.contains("first name") || text.contains("required") || text.contains("shipping");
        } catch (Exception e) {
            System.out.println("❌ Shipping required field error not found");
            return false;
        }
    }
}