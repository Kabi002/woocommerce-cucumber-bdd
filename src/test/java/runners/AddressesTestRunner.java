package runners;

import org.testng.annotations.DataProvider;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/features/Addresses.feature",
    glue = {"stepdefinitions", "hooks"},
    plugin = {
        "pretty",
        "html:reports/cucumber-reports/addresses-report.html",
        "json:reports/cucumber-reports/addresses.json",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    },
    monochrome = true,
    dryRun = false,
   tags = "@addresses"
)
public class AddressesTestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}