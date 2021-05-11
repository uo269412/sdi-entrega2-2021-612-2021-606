package com.uniovi.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_LoginView extends PO_NavView {

	/**
	 * Rellena el formulario de login
	 * @param driver
	 * @param dnip que será el email del usuario
	 * @param passwordp contraseña del usuario
	 */
	static public void fillForm(WebDriver driver, String dnip, String passwordp) {
		WebElement dni = driver.findElement(By.name("email"));
		dni.click();
		dni.clear();
		dni.sendKeys(dnip);
		WebElement password = driver.findElement(By.name("password"));
		password.click();
		password.clear();
		password.sendKeys(passwordp);
		//Pulsar el boton de Alta.
		By boton = By.className("btn");
		driver.findElement(boton).click();	
	}

	/**
	 * Permite el acceso directo como administrador
	 * @param driver
	 */
	public static void accederAdmin(WebDriver driver) {
		PO_HomeView.clickOption(driver, "/identificarse", "class", "w-100 btn btn-lg btn-primary");
		PO_LoginView.fillForm(driver, "admin@email.com", "admin");
	}
	
	/**
	 * Permite el acceso como un usuario especificado por parámetros
	 * @param driver
	 * @param email del usuario
	 */
	public static void accederUsuario(WebDriver driver, String email) {
		PO_HomeView.clickOption(driver, "/identificarse", "class", "w-100 btn btn-lg btn-primary");
		PO_LoginView.fillForm(driver, email, "123456");
	}
	
}
