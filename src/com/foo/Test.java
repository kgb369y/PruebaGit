/***
 * 
 */
package com.foo;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.asserts.SoftAssert;

public class Test {

    String browser = "firefox";
    String siteToGo = "http://www.google.com";
    public static WebDriver driver;

    @BeforeTest
    public void beforeTest() throws Exception {
	runDrivers(browser);
	driver.get(siteToGo);
    }

    @AfterTest
    public void afterTest() {
	closeBrowser(browser);
    }

    @org.testng.annotations.Test(groups={"grupo1"})
    public void test1() throws Exception {
	SoftAssert sA = new SoftAssert();
	try {
	    sA.assertTrue(true);
	    sA.assertAll();
	} catch (AssertionError e) {
	    throw e;
	}
    }

    @org.testng.annotations.Test(groups={"grupo1"})
    public void test2() throws Exception {
	SoftAssert sA = new SoftAssert();
	try {
	    sA.assertTrue(false);
	    sA.assertAll();
	} catch (AssertionError e) {
	    throw e;
	}
    }

    public void runDrivers(String browser) throws IOException {
	WebDriver driver;
	driver = DriverSetUp.getDriver(browser);
	Test.driver = driver;
    }

    public static void closeBrowser(String browser) {
	try {
	    if (browser.contains("ie") || browser.contains("IE")) {
		driver.quit();
	    } else
		driver.close();
	} catch (Exception e) {
	    System.out.println(e);
	}
    }
}
