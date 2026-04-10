package stepdefinitions;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.DashboardPage;
import utils.DriverManager;

public class DashboardSteps {

    private WebDriver driver;
    private DashboardPage dashboardPage;

    public DashboardSteps() {
        this.driver        = DriverManager.getDriver();
        this.dashboardPage = new DashboardPage(driver);
    }

    @Then("I should see all sidebar links on the dashboard")
    public void i_should_see_all_sidebar_links_on_the_dashboard() {
        Assert.assertTrue(dashboardPage.areAllSidebarLinksVisible(),
                "One or more sidebar links are not visible!");
    }

    @Then("I should see {string} count on the dashboard")
    public void i_should_see_count_on_the_dashboard(String widgetName) {
        boolean isVisible;
        switch (widgetName) {
            case "Orders and quotes":
                isVisible = dashboardPage.isOrdersAndQuotesCountVisible(); break;
            case "Processing orders":
                isVisible = dashboardPage.isProcessingOrdersCountVisible(); break;
            case "Awaiting payments":
                isVisible = dashboardPage.isAwaitingPaymentsCountVisible(); break;
            default:
                throw new IllegalArgumentException("Unknown widget: " + widgetName);
        }
        Assert.assertTrue(isVisible, widgetName + " count widget is not visible!");
    }

    @When("I click and verify all sidebar links")
    public void i_click_and_verify_all_sidebar_links() {
        dashboardPage.clickAndVerifyAllSidebarLinks();
    }
}