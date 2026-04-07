package hooks;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.DriverManager;
import config.ConfigReader;

public class Hooks {
    
    private WebDriver driver;
    
    @Before
    public void setUp(Scenario scenario) {
        System.out.println("Starting Scenario: " + scenario.getName());
        driver = DriverManager.getDriver();
        driver.get(ConfigReader.getBaseUrl());
    }
    
    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            // Take screenshot on failure
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
        }
        
        System.out.println("Finished Scenario: " + scenario.getName());
        System.out.println("Status: " + scenario.getStatus());
        
        DriverManager.quitDriver();
    }
}