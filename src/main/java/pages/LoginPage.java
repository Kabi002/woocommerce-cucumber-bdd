package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.name("login");
    private By errorMessage = By.xpath("//strong[text()='ERROR']/parent::li");

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Actions
    public void navigateToLoginPage(String url) {
        driver.get(url + "/my-account");
    }

    public void enterUsername(String username) {
        System.out.println("===== CI DEBUG START =====");
        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println("Page Title: " + driver.getTitle());

        String source = driver.getPageSource();
        System.out.println("Page Source Snippet:");
        System.out.println(source.substring(0, Math.min(source.length(), 1000)));

        System.out.println("===== CI DEBUG END =====");

        WebElement usernameElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(usernameField)
        );

        usernameElement.clear();
        usernameElement.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement passwordElement = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        passwordElement.clear();
        passwordElement.sendKeys(password);
    }

    public void clickLoginButton() {
        try {
            WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", loginBtn);
            Thread.sleep(500);
            loginBtn.click();
        } catch (Exception e) {
            try {
                WebElement loginBtn = driver.findElement(loginButton);
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", loginBtn);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException("Unable to click login button");
            }
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return driver.findElement(errorMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoginSuccessful() {
        try {
            Thread.sleep(2000);
            return driver.getCurrentUrl().contains("my-account") &&
                    !driver.getCurrentUrl().contains("lost-password") &&
                    (driver.getPageSource().contains("Dashboard") ||
                            driver.getPageSource().contains("Logout") ||
                            driver.getPageSource().contains("Log out"));
        } catch (Exception e) {
            return false;
        }
    }

    public void dismissPopups() {
        try {
            List<WebElement> cookieBanners = driver.findElements(
                    By.cssSelector("button.accept-cookies, button[aria-label='Accept'], .cookie-consent button"));

            if (!cookieBanners.isEmpty()) {
                cookieBanners.get(0).click();
                Thread.sleep(500);
            }

            List<WebElement> closeButtons = driver.findElements(
                    By.cssSelector(".modal .close, .popup .close, button.close"));

            if (!closeButtons.isEmpty()) {
                closeButtons.get(0).click();
                Thread.sleep(500);
            }

        } catch (Exception e) {
            // Ignore
        }
    }

    public void logout() {
        try {
            List<WebElement> logoutLinks = driver.findElements(By.linkText("Logout"));

            if (logoutLinks.isEmpty()) {
                logoutLinks = driver.findElements(By.linkText("Log out"));
            }

            if (logoutLinks.isEmpty()) {
                logoutLinks = driver.findElements(By.partialLinkText("logout"));
            }

            if (logoutLinks.isEmpty()) {
                logoutLinks = driver.findElements(By.cssSelector("a[href*='customer-logout']"));
            }

            if (!logoutLinks.isEmpty()) {
                logoutLinks.get(0).click();
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            System.out.println("Logout failed: " + e.getMessage());
        }
    }

    public boolean isUsernameFieldRequired() {
        try {
            WebElement usernameElement = driver.findElement(usernameField);
            String requiredAttribute = usernameElement.getAttribute("required");

            if (requiredAttribute != null) {
                return true;
            }

            String validationMessage = usernameElement.getAttribute("validationMessage");
            return validationMessage != null && !validationMessage.isEmpty();

        } catch (Exception e) {
            return false;
        }
    }

    public String getValidationMessage() {
        try {
            WebElement usernameElement = driver.findElement(usernameField);
            return usernameElement.getAttribute("validationMessage");
        } catch (Exception e) {
            return "";
        }
    }
}