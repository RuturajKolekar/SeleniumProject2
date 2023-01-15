package com.azure.Tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.azure.base.TestBase;
import com.azure.pages.HomePage;
import com.azure.pages.LoginPage;

public class HomePageTest extends TestBase {

	LoginPageTest lpTest;
	LoginPage lp;
	HomePage hp;

	public HomePageTest() {
		super();
	}

	@BeforeMethod
	public void preconfig() {
		initialize();
		lp = new LoginPage();
		hp = new HomePage();
	}

	@Test
	public void CheckProductaddedToCard() {
		lp.login();
		hp.addItemToCart("adidas original");
		String ExpectedItemsAddedToCart = "1";
		String ActualItemsAddedToCart = hp.getCountOfItemAddedToCart();
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Assert.assertEquals(ActualItemsAddedToCart, ExpectedItemsAddedToCart);
	}
	
	
	@AfterMethod
	public void tearDown() {
		driver.quit();
	}

}
