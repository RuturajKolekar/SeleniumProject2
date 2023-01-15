package com.azure.demo1;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class Test1 {

	WebDriver driver;
	Properties prop;
	SoftAssert softAsssert;
	ChromeOptions options;

	
	@BeforeMethod
	public void setup() throws Exception {
		File file = new File("./src/test/resources/config.properties");
		System.out.println(file.exists());
		FileInputStream fis = new FileInputStream(file);
		prop= new Properties();
		prop.load(fis);

		
		 options = new ChromeOptions();
		options.addArguments("start-maximized"); // open Browser in maximized mode
		options.addArguments("disable-infobars"); // disabling infobars
		options.addArguments("--disable-extensions"); // disabling extensions
		options.addArguments("--disable-gpu"); // applicable to windows os only
		options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
		options.addArguments("--no-sandbox"); // Bypass OS security model

		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		driver.get(prop.getProperty("url"));
		 softAsssert = new SoftAssert();
	}
	
	
	@Test
	public void login() {
		
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys(prop.getProperty("username"));
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys(prop.getProperty("password"));
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		String Title=driver.getTitle();
		softAsssert.assertEquals("OrangeHRM",Title);
	}
	
	@AfterMethod
	public void teardown() throws Exception {
		Thread.sleep(5000);
		driver.quit();
		softAsssert.assertAll();
	}

}
