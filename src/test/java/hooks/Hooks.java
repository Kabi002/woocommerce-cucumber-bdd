package hooks;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import utils.DriverManager;
import config.ConfigReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

                scenario.attach(screenshot, "image/png", scenario.getName());

                Files.createDirectories(Paths.get("reports/screenshots"));

                String fileName = scenario.getName().replaceAll("[^a-zA-Z0-9]", "_") + ".png";

                Files.write(
                        Paths.get("reports/screenshots/" + fileName),
                        screenshot
                );

                System.out.println("Saved screenshot: reports/screenshots/" + fileName);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Finished Scenario: " + scenario.getName());
        System.out.println("Status: " + scenario.getStatus());

        DriverManager.quitDriver();
    }
}