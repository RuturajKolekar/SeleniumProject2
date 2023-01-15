package com.azure.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.azure.base.TestBase;
import com.azure.utils.Utils;

public class HomePage extends TestBase {

	Utils util;

	public HomePage() {
		PageFactory.initElements(driver, this);
	}

	@FindBy(id = "toast-container")
	WebElement toastMessage;

	@FindBy(xpath = "//button[@routerlink='/dashboard/cart']")
	WebElement cartButton;
	
	@FindBy(xpath = "//button[@routerlink='/dashboard/cart']/label")
	WebElement CountOfItems;

	public void addItemToCart(String productName) {
		util = new Utils();
		util.WaitUntilVisibilityOfElement(By.cssSelector(".mb-3"));
		List<WebElement> products = driver.findElements(By.cssSelector(".mb-3"));
		WebElement prod = products.stream()
				.filter(product -> product.findElement(By.cssSelector("b")).getText().equalsIgnoreCase(productName)).findFirst()
				.orElse(null);
		prod.findElement(By.cssSelector(".card-body button:last-of-type")).click();
	}

	public void viewCart() {
		util.WaitUntilVisibilityOfElement(toastMessage);
		util.WaitUntilElementToBeClickable(cartButton);
		cartButton.click();
	}
	
	public String getCountOfItemAddedToCart() {
		util.WaitUntilVisibilityOfElement(CountOfItems);
		return CountOfItems.getText();
	}

}
