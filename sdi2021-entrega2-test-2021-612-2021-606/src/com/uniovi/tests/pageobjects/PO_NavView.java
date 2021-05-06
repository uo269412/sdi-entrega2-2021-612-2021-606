package com.uniovi.tests.pageobjects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.uniovi.tests.util.SeleniumUtils;

public class PO_NavView  extends PO_View{

	/**
	 * * CLicka una de las opciones principales (a href) y comprueba que se vaya a
	 * la vista con el elemento de tipo type con el texto Destino * @param driver:
	 * apuntando al navegador abierto actualmente. * @param textOption: Texto de la
	 * opción principal. * @param criterio: "id" or "class" or "text" or
	 * "@attribute" or "free". Si el valor de criterio es free es una expresion
	 * xpath completa. * @param textoDestino: texto correspondiente a la búsqueda de
	 * la página destino.
	 */
	public static void clickOption(WebDriver driver, String textOption, String criterio, String textoDestino) {
		// CLickamos en la opción de registro y esperamos a que se cargue el enlace de
		// Registro.
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "@href", textOption, getTimeout());
		// Tiene que haber un sólo elemento.
		assertTrue(elementos.size() == 1);
		// Ahora lo clickamos
		elementos.get(0).click();
		// Esperamos a que sea visible un elemento concreto
		elementos = SeleniumUtils.EsperaCargaPagina(driver, criterio, textoDestino, getTimeout());
		// Tiene que haber un sólo elemento.
		assertTrue(elementos.size() == 1);
	}

	/**
	 * * Selecciona el enlace de idioma correspondiente al texto textLanguage
	 * * @param driver: apuntando al navegador abierto actualmente. * @param
	 * textLanguage: el texto que aparece en el enlace de idioma ("English" o
	 * "Spanish")
	 */
	public static void changeIdiom(WebDriver driver, String textLanguage) {
        // clickamos la opción Idioma.
        List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "navbarDarkDropdownMenuLink", getTimeout());
        if (elementos.size() == 2) {
        	elementos.get(1).click();
        } else {
            elementos.get(0).click();
        }
        // Esperamos a que aparezca el menú de opciones.
        List<WebElement> elementos2 = SeleniumUtils.EsperaCargaPagina(driver, "id", "languageDropdownMenuButton", getTimeout());
         SeleniumUtils.esperarSegundos(driver, 2);
        // CLickamos la opción Inglés partiendo de la opción Español
        elementos2 = SeleniumUtils.EsperaCargaPagina(driver, "id", textLanguage, getTimeout());
        elementos2.get(0).click();
    }
	
	/**
	 * Permite acceder a tu cuenta
	 * @param driver
	 */
	static private void tuCuenta(WebDriver driver) {
	    List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "navbarDarkAccount", getTimeout());
	    elementos.get(0).click();
	}
	
	/**
	 * Desconecta al usuario
	 * @param driver
	 */
	static public void disconnect(WebDriver driver) {
	    tuCuenta(driver);
	    List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "enlace-logout", getTimeout());
	    elementos.get(0).click();
	 
	}
	
	/**
	 * Permite ver la lista de ofertas propias
	 * @param driver
	 */
	static public void verListaPropia(WebDriver driver) {
	    tuCuenta(driver);
	    List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "lista-propios", getTimeout());
	    elementos.get(0).click();
	}
	
	/**
	 * Permite ver la lista de conversaciones
	 * @param driver
	 */
	static public void verListaConversaciones(WebDriver driver) {
	    tuCuenta(driver);
	    List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "lista-conversaciones", getTimeout());
	    elementos.get(0).click();
	}
	
	/**
	 * Permite ver la lista de comprados
	 * @param driver
	 */
	static public void verListaComprados(WebDriver driver) {
	    tuCuenta(driver);
	    List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "lista-comprados", getTimeout());
	    elementos.get(0).click();
	}
	
	/**
	 * Permite ver la la lista de usuarios
	 * @param driver
	 */
	static public void accessList(WebDriver driver) {
	    List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "gestion-usuarios", getTimeout());
	    elementos.get(0).click();
	}
	
	/**
	 * Permite añadir uan oferta
	 * @param driver
	 */
	static public void accessVender(WebDriver driver) {
	    List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "vender", getTimeout());
	    elementos.get(0).click();
	}
	
	/**
	 * Permite ver el listado de todas las ofertas
	 * @param driver
	 */
	static public void verListaTodos(WebDriver driver) {
	    List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "buscar-ofertas", getTimeout());
	    elementos.get(0).click();
	}
	
	/**
	 * Comprueba que el saldo del usuario se corresponde al pasado por parámetro
	 * @param driver
	 * @param saldoEsperado que se espera que el usuario tenga
	 */
	static public void comprobarSaldo(WebDriver driver, String saldoEsperado) {
	    List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "userSaldoA", getTimeout());
	    System.out.println(elementos.get(0));
	    assertEquals(elementos.get(0).getText(), saldoEsperado);
	}

}
