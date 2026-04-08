package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DashboardPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ── Sidebar Links ──────────────────────────────────────────────────
    private By dashboardLink     = By.cssSelector("a.nav-link[href*='/my-account/']");
    private By quotesLink        = By.cssSelector("a.nav-link[href*='/my-account/quotes/']");
    private By ordersLink        = By.cssSelector("a.nav-link[href*='/my-account/orders/']");
    private By buyAgainLink      = By.cssSelector("a.nav-link[href*='/my-account/buy-again/']");
    private By storeCreditLink   = By.cssSelector("a.nav-link[href*='/my-account/account-funds/']");
    private By addressesLink     = By.cssSelector("a.nav-link[href*='/my-account/edit-address/']");
    private By shoppingListsLink = By.cssSelector("a.nav-link[href*='/my-account/purchase-lists/']");
    private By multiAccountsLink = By.cssSelector("a.nav-link[href*='/my-account/multiaccounts/']");
    private By accountDetailsLink= By.cssSelector("a.nav-link[href*='/my-account/edit-account/']");
    private By vatNumberLink     = By.cssSelector("a.nav-link[href*='/my-account/vat-number/']");
    private By logoutLink        = By.cssSelector("a.nav-link[href*='logout']");

    // ── Dashboard Count Widgets ────────────────────────────────────────
    private By ordersAndQuotesWidget  = By.xpath("//p[contains(text(),'Orders and quotes')]");
    private By processingOrdersWidget = By.xpath("//p[contains(text(),'Processing orders')]");
    private By awaitingPaymentsWidget = By.xpath("//p[contains(text(),'Awaiting payments')]");

    public DashboardPage(WebDriver driver) {
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

    // ── Verify All Sidebar Links Visible ──────────────────────────────
    public boolean areAllSidebarLinksVisible() {
        List<By> sidebarLinks = Arrays.asList(
            dashboardLink, quotesLink, ordersLink, buyAgainLink,
            storeCreditLink, addressesLink, shoppingListsLink,
            multiAccountsLink, accountDetailsLink, vatNumberLink, logoutLink
        );
        for (By locator : sidebarLinks) {
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

    // ── Count Widgets ──────────────────────────────────────────────────
    public boolean isOrdersAndQuotesCountVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(ordersAndQuotesWidget)).isDisplayed();
        } catch (Exception e) {
            System.out.println("❌ Orders and quotes widget not found");
            return false;
        }
    }

    public boolean isProcessingOrdersCountVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(processingOrdersWidget)).isDisplayed();
        } catch (Exception e) {
            System.out.println("❌ Processing orders widget not found");
            return false;
        }
    }

    public boolean isAwaitingPaymentsCountVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(awaitingPaymentsWidget)).isDisplayed();
        } catch (Exception e) {
            System.out.println("❌ Awaiting payments widget not found");
            return false;
        }
    }

    // ── Click & Verify All Sidebar Links One By One ───────────────────
    public void clickAndVerifyAllSidebarLinks() {
        dismissPopups();

        LinkedHashMap<String, String> linksMap = new LinkedHashMap<>();
        linksMap.put("/my-account/quotes/",          "Quotes");
        linksMap.put("/my-account/orders/",          "Orders");
        linksMap.put("/my-account/buy-again/",       "Buy again");
        linksMap.put("/my-account/account-funds/",   "Store credit");   // ✅ fixed
        linksMap.put("/my-account/edit-address/",    "Addresses");
        linksMap.put("purchase-lists",               "Shopping Lists"); // ✅ fixed
        linksMap.put("/my-account/multiaccounts/",   "Multi accounts"); // ✅ fixed
        linksMap.put("/my-account/edit-account/",    "Account details");
        linksMap.put("/my-account/vat-number/",      "Vat number");

        for (Map.Entry<String, String> entry : linksMap.entrySet()) {
            String expectedUrl = entry.getKey();
            String linkName    = entry.getValue();

            try {
                By locator = By.cssSelector("a.nav-link[href*='" + expectedUrl + "']");
                WebElement link = wait.until(ExpectedConditions.elementToBeClickable(locator));
                
                // Scroll into view before clicking to avoid intercept
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", link);
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", link);

                // Verify URL changed
                boolean urlMatched = wait.until(ExpectedConditions.urlContains(expectedUrl));
                if (urlMatched) {
                    System.out.println("✅ PASSED - " + linkName + " | URL: " + driver.getCurrentUrl());
                } else {
                    System.out.println("❌ FAILED - " + linkName + " | Expected URL to contain: " + expectedUrl);
                }

                // Navigate back to dashboard
                driver.navigate().to(driver.getCurrentUrl().split("/my-account")[0] + "/my-account/");
                wait.until(ExpectedConditions.urlContains("/my-account"));
                
                // Dismiss popup again if it reappears
                dismissPopups();

            } catch (Exception e) {
                System.out.println("❌ ERROR - " + linkName + " | " + e.getMessage());
                // Navigate back to dashboard on error
                driver.navigate().to("https://www.12taste.com/my-account/");
                wait.until(ExpectedConditions.urlContains("/my-account"));
                dismissPopups();
            }
        }
    }
}