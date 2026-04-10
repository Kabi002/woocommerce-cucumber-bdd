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

    // ── Field Locators ─────────────────────────────────────────────────
    private By firstNameField       = By.id("account_first_name");
    private By lastNameField        = By.id("account_last_name");
    private By displayNameField     = By.id("account_display_name");
    private By emailField           = By.id("account_email");
    private By currentPasswordField = By.id("password_current");
    private By newPasswordField     = By.id("password_1");
    private By confirmPasswordField = By.id("password_2");
    private By saveChangesButton    = By.cssSelector("button[name='save_account_details']");

    // ── Success & Error Messages ───────────────────────────────────────
    private By successMessage = By.cssSelector("div.woocommerce-message");
    private By errorMessage   = By.cssSelector("ul.woocommerce-error");

    public AccountDetailsPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ── Navigate ───────────────────────────────────────────────────────
    public void navigateToAccountDetailsPage(String baseUrl) {
        driver.get(baseUrl + "/my-account/edit-account/");
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

    // ── Verify All Fields Visible ──────────────────────────────────────
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

    // ── Field Actions ──────────────────────────────────────────────────
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

    // ── Click Save Changes ─────────────────────────────────────────────
//    public void clickSaveChanges() {
//        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(saveChangesButton));
//        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
//    }

    public void clickSaveChanges() {

        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(saveChangesButton));

        // ✅ Scroll to center
        ((JavascriptExecutor) driver)
            .executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);

        // ✅ Move slightly up to avoid footer overlap
        ((JavascriptExecutor) driver)
            .executeScript("window.scrollBy(0, -120);");

        // ✅ Wait for clickable
        wait.until(ExpectedConditions.elementToBeClickable(btn));

        // ✅ Click with fallback
        try {
            btn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", btn);
        }
    }
    
    // ── Success Message ────────────────────────────────────────────────
    public boolean isSuccessMessageDisplayed() {
        try {
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
            WebElement msg = longWait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            String text = msg.getText().trim();
            System.out.println("✅ Success message: " + text);
            return text.contains("Account details changed successfully");
        } catch (Exception e) {
            System.out.println("❌ Success message not found | Current URL: " + driver.getCurrentUrl());
            return false;
        }
    }

 // ── Password Required Error ────────────────────────────────────────
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
    // ── Required Field Error (Server side) ────────────────────────────
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

    // ── Wrong Password Error ───────────────────────────────────────────
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

    // ── HTML5 Validation Messages ──────────────────────────────────────
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

        // ✅ Extra safety (handles JS issues)
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].value=''; arguments[1].value=''; arguments[2].value='';",
            current, newPass, confirm
        );
    }
}