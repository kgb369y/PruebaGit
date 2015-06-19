package com.foo;
/***********************************************************************************************************************
 * FileName - DriverSetUp.java
 *
 * (c) Disney. All rights reserved.
 *
 * $Author: kaiy001 $
 * $Revision: #1 $
 * $Change: 715510 $
 * $Date: Sep 4, 2012 $
 ***********************************************************************************************************************/


import java.io.File;
import java.io.FileInputStream;
//import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;


/**
 * Functions to set up driver
 * 
 * @author kaiy001 $Change: Returned the static method getDriver. Delete comment
 *         TDO starting two windows, need to fix. Katy Garcia
 */
public class DriverSetUp {

    /**
     * Returns a new instance of WebDriver
     * 
     * @param driverType
     *            Driver type (firefox, chrome, iexplore) - defaults to driver
     *            specified in config file otherwise
     * @return WebDriver to use
     */
    public static WebDriver getDriver(String driverType) {
	Properties property = new Properties();

	String archType = System.getProperty("sun.arch.data.model");
	String osName = System.getProperty("os.name").toLowerCase();
	String directorySeparator;
	if (osName.contains("win")) {
	    directorySeparator = "\\";
	} else {
	    directorySeparator = "/";
	}

	String path;
	try {
	    path = new java.io.File(".").getCanonicalPath();
	    FileInputStream inputStream = new FileInputStream(path + directorySeparator + "config.properties");
	    property.load(inputStream);
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}

	boolean maximize = "true".equals(property.getProperty("maximize"));

	char driverOption;
	if (driverType.toLowerCase().equals("firefox")) {
	    driverOption = 'F';
	} else if (driverType.toLowerCase().equals("chrome")) {
	    driverOption = 'C';
	} else if (driverType.toLowerCase().equals("iexplore")) {
	    driverOption = 'I';
	} else {
	    driverOption = property.getProperty("driver").toUpperCase().charAt(0);
	}

	/*
	 * Choosing which driver to use
	 */
	WebDriver driver;
	switch (driverOption) {
	case 'F':
	    // FirefoxDriver
	    // Using Firefox profile or not
	    if ("true".equals(property.getProperty("firefoxProfile"))) {
		File profileDirectory;
		profileDirectory = new File(path + directorySeparator + "drivers" + directorySeparator + "firefox" + directorySeparator + "firefox_profile"
			+ directorySeparator);
		FirefoxProfile profile = new FirefoxProfile(profileDirectory);

		driver = new FirefoxDriver(profile);
	    } else {
		driver = new FirefoxDriver();
	    }
	    break;
	case 'C':
	    // ChromeDriver
	    // To set environment variable for ChromeDriver
	    String chromeDriverPath;
	    if (osName.contains("win")) {
		chromeDriverPath = path + directorySeparator + "drivers" + directorySeparator + "chrome" + directorySeparator + "win_chromedriver.exe";
	    } else if (osName.contains("mac")) {
		chromeDriverPath = path + directorySeparator + "drivers" + directorySeparator + "chrome" + directorySeparator + "mac_chromedriver";
	    } else {
		if (archType.equals("64")) {
		    chromeDriverPath = path + directorySeparator + "drivers" + directorySeparator + "chrome" + directorySeparator + "linux64_chromedriver";
		} else {
		    chromeDriverPath = path + directorySeparator + "drivers" + directorySeparator + "chrome" + directorySeparator + "linux32_chromedriver";
		}
	    }
	    System.setProperty("webdriver.chrome.driver", chromeDriverPath);
	    if (maximize) {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		return new ChromeDriver(options);
	    } else {
		return new ChromeDriver();
	    }
	case 'I':
	    // InternetExplorerDriver
	    // To set environment variable for InternetExplorerDriver
	    String IEDriverPath;
	    if (osName.contains("win")) {
		if (archType.equals("64")) {
		    IEDriverPath = path + directorySeparator + "drivers" + directorySeparator + "ie" + directorySeparator + "win64_IEDriverServer.exe";
		} else {
		    IEDriverPath = path + directorySeparator + "drivers" + directorySeparator + "ie" + directorySeparator + "win32_IEDriverServer.exe";
		}
	    } else {
		return null;
	    }
	    System.setProperty("webdriver.ie.driver", IEDriverPath);
	    DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
	    caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
	    caps.setCapability("ignoreZoomSetting", true);
	    driver = new InternetExplorerDriver(caps);
	    break;
	case 'H':
	    // HtmlUnitDriver
	    return new HtmlUnitDriver();
	default:
	    // Invalid driver type
	    System.out.println("Invalid driver type. Configure proper type in the config.properties file.");
	    return null;
	}

	// Would only reach here if FirefoxDriver or InternetExplorerDriver
	if (maximize) {
	    driver.manage().window().maximize();
	}
	return driver;
    }

}
