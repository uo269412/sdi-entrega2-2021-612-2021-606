package com.uniovi.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_RegisterView extends PO_NavView {

	static public void fillForm(WebDriver driver, String emailp, String namep, String lastnamep, String passwordp,
			String passwordconfp) {
		WebElement email = driver.findElement(By.name("email"));
		email.click();
		email.clear();
		email.sendKeys(emailp);
		WebElement name = driver.findElement(By.name("name"));
		name.click();
		name.clear();
		name.sendKeys(namep);
		WebElement lastname = driver.findElement(By.name("lastName"));
		lastname.click();
		lastname.clear();
		lastname.sendKeys(lastnamep);
		WebElement password = driver.findElement(By.name("password"));
		password.click();
		password.clear();
		password.sendKeys(passwordp);
		WebElement passwordConfirm = driver.findElement(By.name("passwordConfirm"));
		passwordConfirm.click();
		passwordConfirm.clear();
		passwordConfirm.sendKeys(passwordconfp);
		// Pulsar el boton de Alta.
		By boton = By.className("btn");
		driver.findElement(boton).click();
	}

	public static void createUsers(WebDriver driver, String URL, int testNumber, int number) {
		String user = "PR" + testNumber + "_";
		for (int i = 1; i <= number; i++) {
			user += i;
			PO_HomeView.clickOption(driver, "/registrarse", "class", "w-100 btn btn-lg btn-primary");
			// Rellenamos el formulario.
			PO_RegisterView.fillForm(driver, user + "@" + user + ".com", "Nombre" + user, "Apellido" + user, "123456",
					"123456");
			// Comprobamos que entramos en la sección privada
			PO_View.checkElement(driver, "text", "Bienvenido/a a MyWallapop");
			PO_NavView.disconnect(driver);
			driver.navigate().to(URL);
		}
	}

	public static void registrarUsuarioTest(WebDriver driver, int testNumber) {
		PO_HomeView.clickOption(driver, "/registrarse", "class", "w-100 btn btn-lg btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "PR" + testNumber + "@PR" + testNumber + ".com", "NombrePR" + testNumber,
				"ApellidoPR" + testNumber, "123456", "123456");
		// Comprobamos que entramos en la sección privada
		PO_View.checkElement(driver, "text", "Bienvenido/a a MyWallapop");

	}

}
