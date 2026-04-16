package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AccountDetailsPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By firstNameField       = By.id("account_first_name");
    private By lastNameField        = By.id("account_last_name");
    private By displayNameField     = By.id("account_display_name");
    private By emailField           = By.id("account_email");
    private By currentPasswordField = By.id("password_current");
    private By newPasswordField     = By.id("password_1");
    private By confirmPasswordField = By.id("password_2");
    private By saveChangesButton    = By.cssSelector("button[name='save_account_details']");

    private By successMessage = By.cssSelector("div.woocommerce-message");
    private By errorMessage   = By.cssSelector("ul.woocommerce-error");

    public AccountDetailsPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void navigateToAccountDetailsPage(String baseUrl) {
        driver.get(baseUrl + "/my-account/edit-account/");
        // ── FIX: Wait for the form to be fully ready before any interaction.
        //         This ensures the nonce embedded in the form is fresh for
        //         THIS browser session and not shared with another parallel thread.
        wait.until(ExpectedConditions.visibilityOfElementLocated(saveChangesButton));
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

    public boolean areAllFieldsVisible() {
        By[] fields = {
            firstNameField, lastNameField, displayNameField,
            emailField, currentPasswordField, newPasswordField,
            confirmPasswordField, saveChangesButton
        };
        for (By locator : fields) {
            try {
                WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                if (!el.isDisplayed()) {
                    System.out.println("❌ NOT visible: " + locator);
                    return false;
                }
                System.out.println("✅ Visible: " + locator);
            } catch (Exception e) {
                System.out.println("❌ NOT found: " + locator);
                return false;
            }
        }
        return true;
    }

    public void clearAndEnterFirstName(String firstName) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
        field.clear();
        field.sendKeys(firstName);
    }

    public void clearAndEnterLastName(String lastName) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameField));
        field.clear();
        field.sendKeys(lastName);
    }

    public void clearAndEnterDisplayName(String displayName) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(displayNameField));
        field.clear();
        field.sendKeys(displayName);
    }

    public void clearAndEnterEmail(String email) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        field.clear();
        field.sendKeys(email);
    }

    public void enterCurrentPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(currentPasswordField));
        field.clear();
        field.sendKeys(password);
    }

    public void enterNewPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(newPasswordField));
        field.clear();
        field.sendKeys(password);
    }

    public void enterConfirmPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(confirmPasswordField));
        field.clear();
        field.sendKeys(password);
    }

    // ── FIX: After clicking Save, WooCommerce does a full page POST then
    //         reloads /my-account/edit-account/ with a success notice at top.
    //         The key problem in parallel runs: another thread hitting the
    //         same account invalidates this thread's nonce.
    //         Solution: navigate to the page fresh right before saving
    //         (done in navigateToAccountDetailsPage already), and after
    //         clicking Save we wait for the page to complete its reload
    //         before isSuccessMessageDisplayed() checks for the notice.
    public void clickSaveChanges() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(saveChangesButton));

        ((JavascriptExecutor) driver)
            .executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
        ((JavascriptExecutor) driver)
            .executeScript("window.scrollBy(0, -120);");

        wait.until(ExpectedConditions.elementToBeClickable(btn));

        // Capture current URL so we can detect page reload
        String urlBefore = driver.getCurrentUrl();

        try {
            btn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", btn);
        }

        // ── FIX: Wait for the page to reload (URL stays the same but the
        //         DOM is replaced). We detect this by waiting for the save
        //         button to go stale (it belongs to the old DOM), then
        //         waiting for it to re-appear in the new DOM.
        //         This replaces the fixed Thread.sleep approach.
        try {
            wait.until(ExpectedConditions.stalenessOf(btn));
        } catch (Exception ignored) {
            // If staleness isn't detected, the page may use AJAX — proceed
        }

        // Re-wait for page to be interactive before the assertion step runs
        new WebDriverWait(driver, Duration.ofSeconds(20))
            .until(ExpectedConditions.visibilityOfElementLocated(saveChangesButton));
    }

    // ── FIX: Extended wait + explicit URL check so we know we're on
    //         the reloaded page, not the pre-submit page.
    public boolean isSuccessMessageDisplayed() {
        try {
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));

            // First confirm we're on the edit-account page (post-reload)
            longWait.until(ExpectedConditions.urlContains("/my-account/edit-account/"));

            // Then wait for the WooCommerce notice to appear
            WebElement msg = longWait.until(
                ExpectedConditions.visibilityOfElementLocated(successMessage));

            String text = msg.getText().trim();
            System.out.println("✅ Success message: " + text);
            return text.contains("Account details changed successfully");

        } catch (Exception e) {
            System.out.println("❌ Success message not found | Current URL: " + driver.getCurrentUrl());
            return false;
        }
    }

    public boolean isPasswordRequiredErrorDisplayed() {
        try {
            WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            String text = msg.getText().toLowerCase();
            System.out.println("Actual error message: " + text);
            return text.contains("password") && text.contains("required");
        } catch (Exception e) {
            System.out.println("❌ Password required error not found");
            return false;
        }
    }

    public boolean isFirstNameRequiredErrorDisplayed() {
        try {
            By firstNameError = By.cssSelector("li[data-id='account_first_name']");
            WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameError));
            String text = msg.getText().toLowerCase();
            System.out.println("✅ First name error: " + text);
            return text.contains("first name") && text.contains("required");
        } catch (Exception e) {
            System.out.println("❌ First name required error not found");
            return false;
        }
    }

    public boolean isWrongPasswordErrorDisplayed() {
        try {
            WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            String text = msg.getText().toLowerCase();
            System.out.println("✅ Error message: " + text);
            return text.contains("password")
                || text.contains("incorrect")
                || text.contains("wrong")
                || text.contains("invalid");
        } catch (Exception e) {
            System.out.println("❌ Wrong password error not found");
            return false;
        }
    }

    public String getEmailValidationMessage() {
        WebElement field = driver.findElement(emailField);
        return (String) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].validationMessage;", field);
    }

    public String getFirstNameValidationMessage() {
        WebElement field = driver.findElement(firstNameField);
        return (String) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].validationMessage;", field);
    }

    public void clearAllPasswordFields() {
        WebElement current = wait.until(ExpectedConditions.visibilityOfElementLocated(currentPasswordField));
        WebElement newPass = wait.until(ExpectedConditions.visibilityOfElementLocated(newPasswordField));
        WebElement confirm = wait.until(ExpectedConditions.visibilityOfElementLocated(confirmPasswordField));

        current.clear();
        newPass.clear();
        confirm.clear();

        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].value=''; arguments[1].value=''; arguments[2].value='';",
            current, newPass, confirm
        );
    }
}