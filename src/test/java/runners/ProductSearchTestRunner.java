package runners;

import org.testng.annotations.DataProvider;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/features/ProductSearch.feature",
    glue = {"stepdefinitions", "hooks"},
    plugin = {
        "pretty",
        "html:reports/cucumber-reports/productsearch-report.html",
        "json:reports/cucumber-reports/productsearch.json",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    },
    monochrome = true,
    dryRun = false,
    tags = "@productsearch"
)
public class ProductSearchTestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}