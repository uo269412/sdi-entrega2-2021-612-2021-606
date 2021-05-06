package com.uniovi.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_AddItemView extends PO_NavView {
	static public void fillForm(WebDriver driver, String titlep, String descriptionp, String pricep,
			boolean destacado) {
		WebElement title = driver.findElement(By.name("titulo"));
		title.click();
		title.clear();
		title.sendKeys(titlep);

		WebElement description = driver.findElement(By.name("descripcion"));
		description.click();
		description.clear();
		description.sendKeys(descriptionp);

		WebElement price = driver.findElement(By.name("precio"));
		price.click();
		price.clear();
		price.sendKeys(pricep);

		WebElement checkBoxDestacado = driver.findElement(By.name("estaDestacada"));
		if (destacado) {
			checkBoxDestacado.click();
		}

		By boton = By.className("btn");
		driver.findElement(boton).click();
	}

	/**
	 * Permite a�adir un n�mero de items especificado por par�metro a un usuario
	 * 
	 * @param driver
	 * @param testNumber que puede ser el que quiera. Se decidi� que se
	 *                   correspondiese con el n�mero de test o el n�mero de usuario
	 *                   que llame a este m�todo. Tambi�n ser� el precio de la
	 *                   oferta
	 * @param numero que simboliza la cantidad de ofertas que se quieren a�adir
	 */
	public static void a�adirItems(WebDriver driver, int testNumber, int numero) {
		String identificador = "PR" + testNumber + "_";
		for (int i = 1; i <= numero; i++) {
			identificador += i;
			PO_NavView.accessVender(driver);
			PO_AddItemView.fillForm(driver, identificador + "Oferta", identificador + "Descripcion", "" + testNumber,
					false);
		}
	}
}