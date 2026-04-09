package runners;

import org.testng.annotations.DataProvider;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/features/AccountDetails.feature",
    glue = {"stepdefinitions", "hooks"},
    plugin = {
        "pretty",
        "html:reports/cucumber-reports/accountdetails-report.html",
        "json:reports/cucumber-reports/accountdetails.json",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    },
    monochrome = true,
    dryRun = false,
   tags = "@accountdetails"
)
public class AccountDetailsTestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}