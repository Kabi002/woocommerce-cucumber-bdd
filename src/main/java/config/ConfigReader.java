package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

	private static Properties properties;
	private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";

	static {
		try {
			FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH);
			properties = new Properties();
			properties.load(fis);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to load config.properties file");
		}
	}

	public static String getBrowser() {
		return properties.getProperty("browser");
	}

	public static String getBaseUrl() {
		return properties.getProperty("baseUrl");
	}

	public static String getUsername() {
		return properties.getProperty("username");
	}

	public static String getPassword() {
		return properties.getProperty("password");
	}

	public static boolean isHeadless() {
		return Boolean.parseBoolean(properties.getProperty("headless"));
	}

	public static int getImplicitWait() {
		return Integer.parseInt(properties.getProperty("implicitWait"));
	}

	public static int getPageLoadTimeout() {
		return Integer.parseInt(properties.getProperty("pageLoadTimeout"));
	}

	public static boolean isEmailEnabled() {
		return Boolean.parseBoolean(properties.getProperty("emailEnabled"));
	}

	public static String getEmailTo() {
		return properties.getProperty("emailTo");
	}

	public static String getEmailFrom() {
		return properties.getProperty("emailFrom");
	}

	public static String getEmailPassword() {
		return properties.getProperty("emailPassword");
	}

	public static String getEmailHost() {
		return properties.getProperty("emailHost");
	}

	public static int getEmailPort() {
		return Integer.parseInt(properties.getProperty("emailPort"));
	}

	public static String getReportPath() {
		return properties.getProperty("reportPath");
	}

	public static String getScreenshotPath() {
		return properties.getProperty("screenshotPath");
	}
}