package com.uniovi.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_SearchBox extends PO_NavView {
	/**
	 * Permite realizar una búsqueda de oferta
	 * @param driver
	 * @param searchText palabra que se buscará que corresponda con alguna de las ofertas
	 */
	static public void fillForm(WebDriver driver, String searchText) {
		WebElement searchBox = driver.findElement(By.name("busqueda"));
		searchBox.click();
		searchBox.clear();
		searchBox.sendKeys(searchText);
		
		By boton = By.name("search_button");
		driver.findElement(boton).click();
	}
}