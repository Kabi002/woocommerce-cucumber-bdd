package runners;

import org.testng.annotations.DataProvider;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/features",           // Path to feature files
    glue = {"stepdefinitions", "hooks"},    // Path to step definitions and hooks
    plugin = {
        "pretty",                                        // Console output
        "html:reports/cucumber-reports/cucumber.html",   // HTML report
        "json:reports/cucumber-reports/cucumber.json",   // JSON report
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:" // Extent Report
    },
    monochrome = true,                                   // Readable console output
    dryRun = false,                                      // Set true to check mapping without execution
    tags = "@login or @register or @dashboard or "
    		+ "@accountdetails or @addresses or @productsearch"   // Run specific scenarios using tags
)
public class TestRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}