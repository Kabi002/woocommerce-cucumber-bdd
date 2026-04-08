package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class RegisterPage {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    // Locators - Update based on your WooCommerce registration form
    private By firstNameField = By.id("first_name");
    private By lastNameField = By.id("last_name");
    private By emailField = By.id("reg_email");
    private By registerButton = By.name("register");
    private By errorMessage = By.xpath("//ul[@class='woocommerce-error']//li");
    private By dashboardLink = By.linkText("Dashboard");
    
    // Constructor
    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    // Actions
    public void navigateToRegistrationPage(String url) {
        driver.get(url + "/my-account");
    }
    
    public void enterFirstName(String firstName) {
        WebElement firstNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
        firstNameElement.clear();
        firstNameElement.sendKeys(firstName);
    }
    
    public void enterLastName(String lastName) {
        WebElement lastNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameField));
        lastNameElement.clear();
        lastNameElement.sendKeys(lastName);
    }
    
    public void enterEmail(String email) {
        WebElement emailElement = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        emailElement.clear();
        emailElement.sendKeys(email);
    }
    
    public void clickRegisterButton() {
        try {
            WebElement registerBtn = wait.until(ExpectedConditions.elementToBeClickable(registerButton));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", registerBtn);
            Thread.sleep(500);
            registerBtn.click();
        } catch (Exception e) {
            try {
                WebElement registerBtn = driver.findElement(registerButton);
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", registerBtn);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException("Unable to click register button");
            }
        }
    }
    
    public boolean isRegistrationSuccessful() {
        try {
            Thread.sleep(3000);
            return driver.getCurrentUrl().contains("my-account") && 
                   !driver.getCurrentUrl().contains("lost-password") &&
                   (driver.getPageSource().contains("Dashboard") || 
                    driver.getPageSource().contains("Logout") ||
                    driver.getPageSource().contains("My account"));
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isDashboardVisible() {
        try {
            return driver.getPageSource().contains("Dashboard") ||
                   driver.findElements(dashboardLink).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isErrorMessageDisplayed() {
        try {
            return driver.findElements(errorMessage).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getErrorMessage() {
        try {
            return driver.findElement(errorMessage).getText();
        } catch (Exception e) {
            return "";
        }
    }
    
    public boolean isFirstNameFieldRequired() {
        try {
            WebElement firstNameElement = driver.findElement(firstNameField);
            String requiredAttribute = firstNameElement.getAttribute("required");
            return requiredAttribute != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getFirstNameValidationMessage() {
        try {
            WebElement firstNameElement = driver.findElement(firstNameField);
            return firstNameElement.getAttribute("validationMessage");
        } catch (Exception e) {
            return "";
        }
    }
    
    public String getEmailValidationMessage() {
        try {
            WebElement emailElement = driver.findElement(emailField);
            return emailElement.getAttribute("validationMessage");
        } catch (Exception e) {
            return "";
        }
    }
    
    public void dismissPopups() {
        try {
            List<WebElement> cookieBanners = driver.findElements(By.cssSelector("button.accept-cookies, button[aria-label='Accept'], .cookie-consent button"));
            if (!cookieBanners.isEmpty()) {
                cookieBanners.get(0).click();
                Thread.sleep(500);
            }
            
            List<WebElement> closeButtons = driver.findElements(By.cssSelector(".modal .close, .popup .close, button.close"));
            if (!closeButtons.isEmpty()) {
                closeButtons.get(0).click();
                Thread.sleep(500);
            }
        } catch (Exception e) {
            // Ignore
        }
    }
}