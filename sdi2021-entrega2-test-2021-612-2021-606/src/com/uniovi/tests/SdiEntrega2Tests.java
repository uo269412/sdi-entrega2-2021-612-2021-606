package com.uniovi.tests;

//Paquetes Java
import java.util.List;
//Paquetes JUnit 
import org.junit.*;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

//Paquetes Selenium 
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;
//Paquetes Utilidades de Testing Propias
import com.uniovi.tests.util.SeleniumUtils;
//Paquetes con los Page Object
import com.uniovi.tests.pageobjects.*;

//Ordenamos las pruebas por el nombre del método
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SdiEntrega2Tests {
	static String PathFirefox65 = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
	static String Geckdriver024 = "E:\\Uni\\SDI\\Selenium\\geckodriver024win64.exe";
	static WebDriver driver = getDriver(PathFirefox65, Geckdriver024);
	static String URL = "https://localhost:8082";

	public static WebDriver getDriver(String PathFirefox, String Geckdriver) {
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		System.setProperty("webdriver.gecko.driver", Geckdriver);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	@Before
	public void setUp() {
		resetdb();
		driver.navigate().to(URL);
	}

	private void resetdb() {
		driver.navigate().to(URL + "/restoreDatabase");

	}

	@After
	public void tearDown() {
		driver.manage().deleteAllCookies();
	}

	@BeforeClass
	static public void begin() {
		// COnfiguramos las pruebas.
		// Fijamos el timeout en cada opción de carga de una vista. 2 segundos.
		PO_View.setTimeout(3);

	}

	@AfterClass
	static public void end() {
		// Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}

	
//	// SIGN UP
//
//	// PR01. Registro de Usuario con datos v�lidos.
//	@Test
//	public void PR01() {
//		PO_HomeView.clickOption(driver, "/registrarse", "class", "w-100 btn btn-lg btn-primary");
//		// Rellenamos el formulario.
//		PO_RegisterView.fillForm(driver, "PR01@PR01.com", "NombrePR01", "ApellidoPR01", "123456", "123456");
//		// Comprobamos que entramos en la secci�n privada
//		PO_View.checkElement(driver, "text", "Bienvenido/a a MyWallapop");
//	}
//
//	// PR02. Registro de Usuario con datos inv�lidos (email vac�o, nombre vac�o,
//	// apellidos vac�os).
//	@Test
//	public void PR02() {
//		PO_HomeView.clickOption(driver, "/registrarse", "class", "w-100 btn btn-lg btn-primary");
//		// Comprobamos el error de nombre vac�o
//		PO_RegisterView.fillForm(driver,  "PR02@PR02.com", "", "ApellidoPR02", "123456", "123456");
//		PO_View.checkElement(driver, "text", "Se tiene que añadir un nombre");
//		// Comprobamos el error de email vac�o
//	 	PO_RegisterView.fillForm(driver,  "", "NombrePR02", "ApellidoPR02", "123456", "123456");
//		PO_View.checkElement(driver, "text", "Se tiene que añadir un email");
//		//Comprobamos el error de apellidos vac�os
//		PO_RegisterView.fillForm(driver,  "PR02@PR02.com", "NombrePR02", "", "123456", "123456");
//		PO_View.checkElement(driver, "text", "Se tiene que insertar un apellido");
//		//Comprobamos los tres errores a la vez
//		PO_RegisterView.fillForm(driver,  "", "", "", "123456", "123456");
//		PO_View.checkElement(driver, "text", "Se tiene que añadir un nombre");
//		PO_View.checkElement(driver, "text", "Se tiene que añadir un email");
//		PO_View.checkElement(driver, "text", "Se tiene que insertar un apellido");
//	}
//
//	// PR03. Registro de Usuario con datos inv�lidos (repetici�n de contrase�a
//	// inv�lida).
//	@Test
//	public void PR03() {
//		PO_HomeView.clickOption(driver, "/registrarse", "class", "w-100 btn btn-lg btn-primary");
//		// Rellenamos el formulario.
//		PO_RegisterView.fillForm(driver, "PR03@PR03.com", "NombrePR03", "ApellidoPR03", "123456", "654321");
//		//Se nos muestra el error de que las contrase�as no coinciden
//		PO_View.checkElement(driver, "text", "Las contraseñas no coinciden");
//	}
//
//	// PR04. Registro de Usuario con datos inv�lidos (email existente).
//	@Test
//	public void PR04() {
//		PO_HomeView.clickOption(driver, "/registrarse", "class", "w-100 btn btn-lg btn-primary");
//		// Rellenamos el formulario.
//		PO_RegisterView.fillForm(driver, "admin@email.com", "YaExisto", "YaExisto", "123456", "123456");
//		PO_View.getP();
//		// Comprobamos el error de email ya registrado
//		PO_View.checkElement(driver, "text", "El usuario ya se encuentra registrado");
//	}
//
//	// LOGIN
//
//
//	// PR05. Inicio de sesi�n con datos v�lidos.
//	@Test
//	public void PR05() {
//		//Clickamos en la opci�n de loggearse
//		PO_HomeView.clickOption(driver, "/identificarse", "class", "w-100 btn btn-lg btn-primary");
//		// Rellenamos el formulario con los datos del usuario que acabamos de crear
//		PO_LoginView.fillForm(driver, "laura_mar@gmail.com", "123456");
//		// Comprobamos que entramos en la pagina privada
//		PO_View.checkElement(driver, "text", "Bienvenido/a a MyWallapop");
//	}
//	
//
//	// PR06. Inicio de sesi�n con datos inv�lidos (usuario est�ndar, email existente,
//	// pero contrase�a incorrecta).
//	@Test
//	public void PR06() {
//		PO_HomeView.clickOption(driver, "/identificarse", "class", "w-100 btn btn-lg btn-primary");
//		// Rellenamos el formulario
//		PO_LoginView.fillForm(driver, "admin@email.com", "123");
//		// Comprobamos que la contrase�a para el administrador es incorrecta
//		PO_View.checkElement(driver, "text", "El usuario no existe en la base de datos o la contraseña es incorrecta");
//	}
//
//	// PR07. Inicio de sesi�n con datos inv�lidos (usuario est�ndar, campo email y
//	// contrase�a vac�os).
//	@Test
//	public void PR07() {
//		PO_HomeView.clickOption(driver, "/identificarse", "class", "w-100 btn btn-lg btn-primary");
//		// Rellenamos el formulario
//		PO_LoginView.fillForm(driver, "", "123");
//		// Comprobamos que nos pide un email
//		PO_View.checkElement(driver, "text", "Se tiene que añadir un email");
//		
//		// Rellenamos el formulario
//		PO_LoginView.fillForm(driver, "admin@email.com", "");
//		// Comprobamos que nos pide una contrase�a
//		PO_View.checkElement(driver, "text", "Se tiene que añadir una contraseña");	
//
//		// Rellenamos el formulario
//		PO_LoginView.fillForm(driver, "", "");
//		// Comprobamos que si dejamos los dos campos vac�os, se nos muestran los dos errores
//		PO_View.checkElement(driver, "text", "Se tiene que añadir un email");
//		PO_View.checkElement(driver, "text", "Se tiene que añadir una contraseña");
//		
//	}
//
//
//	// PR08. Inicio de sesi�n con datos inv�lidos (usuario est�ndar, email no
//	// existente en la aplicaci�n).
//	@Test
//	public void PR08() {
//		PO_HomeView.clickOption(driver, "/identificarse", "class", "w-100 btn btn-lg btn-primary");
//		// Rellenamos el formulario con un usuario que no existe en la base de datos
//		PO_LoginView.fillForm(driver, "usuarioNoExiste@usuarioNoExiste.com", "123");
//		// Comprobamos que nos sale el error correspondiente
//		PO_View.checkElement(driver, "text", "El usuario no existe en la base de datos o la contraseña es incorrecta");
//	}
//
//	// LOGOUT
//
//	// PR09. Hacer click en la opci�n de salir de sesi�n y comprobar que se redirige
//	// a la p�gina de inicio de sesi�n (Login).
//	@Test
//	public void PR09() {
//		//Accedemos a identificarnos
//		PO_HomeView.clickOption(driver, "/identificarse", "class", "w-100 btn btn-lg btn-primary");
//		// Rellenamos el formulario con el usuario que acabamos de crear
//		PO_LoginView.fillForm(driver, "laura_mar@gmail.com", "123456");
//		// Comprobamos que entramos en la pagina privada
//		PO_View.checkElement(driver, "text", "Bienvenido/a a MyWallapop");
//		//Nos desconectamos
//		PO_NavView.disconnect(driver);
//		//Ahora estamos en la p�gina de login
//		PO_View.checkElement(driver, "text", "Accede a la aplicación con un usuario ya identificado");
//
//	}
//
//	// PR10. Comprobar que el bot�n cerrar sesi�n no est� visible si el usuario no
//	// est� autenticado.
//	@Test
//	public void PR10() {
//		//Nos intentamos identificar como un usuario que no existe
//		PO_HomeView.clickOption(driver, "/identificarse", "class", "w-100 btn btn-lg btn-primary");
//		PO_LoginView.fillForm(driver, "usuarioNoExiste@usuarioNoExiste.com", "123");
//		PO_View.checkElement(driver, "text", "El usuario no existe en la base de datos o la contraseña es incorrecta");
//		//El enlace para desconectarse no estar� presente ya que se encuentra en la p�gina privada a la cual solo podemos acceder una vez nos hayamos identificado
//		SeleniumUtils.textoNoPresentePagina(driver, "enlace-logout");
//	}
//
//	// LISTA USUARIOS
//
//	// PR11. Mostrar el listado de usuarios y comprobar que se muestran todos los
//	// que existen en el sistema.
//	@Test
//	public void PR11() {
//		//Accedemos como administrador
//		PO_LoginView.accederAdmin(driver);
//		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
//				PO_View.getTimeout());
//		//Comprobamos que se encuentran los siete usuarios que hay en la base de datos
//		assertTrue(elementos.size() == 7);
//	}
//
//
//	// BORRAR USUARIOS
//
//	// PR12. Ir a la lista de usuarios, borrar el primer usuario de la lista,
//	// comprobar que la lista se actualiza y que el usuario desaparece.
//	@Test
//	public void PR12() {
//		//Accedemos como administrador
//		PO_LoginView.accederAdmin(driver);
//		List<WebElement> elementos = driver.findElements(By.name("deleteUser"));
//		//Comprobamos que se encuentran los siete usuarios que hay en la base de datos
//		assertTrue(elementos.size() == 7);
//		//Clickamos el checkbox del primero
//		elementos.get(0).click();
//		//Y ahora le damos a borrar
//		elementos = driver.findElements(By.name("buttonEliminar"));
//		elementos.get(0).click();
//		//As� comprobamos que ahora hay seis usuarios
//		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
//		assertTrue(elementos.size() == 6);
//	}
//
//	// PR13. Ir a la lista de usuarios, borrar el �ltimo usuario de la lista,
//	// comprobar que la lista se actualiza y que el usuario desaparece.
//	@Test
//	public void PR13() {
//		//Accedemos como administrador
//		PO_LoginView.accederAdmin(driver);
//		List<WebElement> elementos = driver.findElements(By.name("deleteUser"));
//		//Comprobamos que se encuentran los siete usuarios que hay en la base de datos
//		assertTrue(elementos.size() == 7);
//		//Clickamos el checkbox del �ltimo
//		elementos.get(elementos.size()-1).click();
//		//Y ahora le damos a borrar
//		elementos = driver.findElements(By.name("buttonEliminar"));
//		elementos.get(0).click();
//		//As� comprobamos que ahora hay seis usuarios en el listado
//		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
//		assertTrue(elementos.size() == 6);	
//	}
//
//	// PR14.Ir a la lista de usuarios, borrar 3 usuarios, comprobar que la lista se
//	// actualiza y que los usuarios desaparecen.
//	@Test
//	public void PR14() {
//		//Accedemos como administrador
//		PO_LoginView.accederAdmin(driver);
//		List<WebElement> elementos = driver.findElements(By.name("deleteUser"));
//		//Comprobamos que se encuentran los siete usuarios
//		assertTrue(elementos.size() == 7);
//		//Seleccionamos los checkbox de los tres primeros
//		elementos.get(0).click();
//		elementos.get(1).click();
//		elementos.get(2).click();
//		//Y los borramos
//		elementos = driver.findElements(By.name("buttonEliminar"));
//		elementos.get(0).click();
//		//Ahora vemos que hay cuatro usuarios en el listado
//		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
//		assertTrue(elementos.size() == 4);
//	}
//
//	// A�ADIR OFERTA
//
//	// PR15. Ir al formulario de alta de oferta, rellenarla con datos v�lidos y
//	// pulsar el bot�n Submit. Comprobar que la oferta sale en el listado de ofertas
//	// de dicho usuario.
//	@Test
//	public void PR15() {
//		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
//		//Vamos a vender
//		PO_NavView.accessVender(driver);
//		//A�adimos una oferta
//		PO_AddItemView.fillForm(driver, "PR15Oferta", "PR15Descripcion", "15", false);
//		//Ahora accedemos al listado de ofertas propias y vemos que se ha registrado en el listado
//		PO_NavView.verListaPropia(driver);
//		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
//				PO_View.getTimeout());
//		assertTrue(elementos.size() == 6);
//	}
//
//	// PR16. Ir al formulario de alta de oferta, rellenarla con datos inv�lidos
//	// (campo t�tulo vac�o y precio en negativo) y pulsar el bot�n Submit. Comprobar que se muestra el
//	// mensaje de campo obligatorio.
//	@Test
//	public void PR16() {
//		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
//		//Vamos a vender
//		PO_NavView.accessVender(driver);
//		//A�adimos una oferta que no tenga t�tulo y con precio negativo
//		PO_AddItemView.fillForm(driver, "", "PR16Descripcion", "-5.0", false);
//		//Vemos que nos aparecen los dos errores
//		PO_View.checkElement(driver, "text", "Se tiene que añadir un título");
//		PO_View.checkElement(driver, "text", "El precio no puede ser negativo o cero");
//	}
//
//	// LISTADO OFERTAS PROPIAS
//
//	// PR17. Mostrar el listado de ofertas para dicho usuario y comprobar que se
//	// muestran todas los que existen para este usuario.
//	@Test
//	public void PR17() {
//		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
//		//Vamos a la lista de ofertas propias
//		PO_NavView.verListaPropia(driver);
//		//Vemos que el usuario tiene las tres ofertas que ha creado
//		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
//				PO_View.getTimeout());
//		assertTrue(elementos.size() == 5);
//	}
//
//	// ELIMINAR OFERTA
//
//	// PR18. Ir a la lista de ofertas, borrar la primera oferta de la lista,
//	// comprobar que la lista se actualiza y que la oferta desaparece.
//	@Test
//	public void PR18() {	
//		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
//		//Vamos a la lista de ofertas propias
//		PO_NavView.verListaPropia(driver);
//		//Vemos que el usuario tiene cinco ofertas
//		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
//				PO_View.getTimeout());
//		assertTrue(elementos.size() == 5);
//		//Ahora le damos a el enlace eliminar la oferta
//		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/oferta/eliminar')]");
//		elementos.get(0).click();
//		//Ahora habr� cuatro elementos
//		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
//		assertTrue(elementos.size() == 4);
//	}
//
//	// PR19. Ir a la lista de ofertas, borrar la �ltima oferta de la lista,
//	// comprobar que la lista se actualiza y que la oferta desaparece.
//	@Test
//	public void PR19() {
//		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
//		//Vamos a la lista de ofertas propias
//		PO_NavView.verListaPropia(driver);
//		//Vemos que el usuario tiene cinco ofertas
//		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
//				PO_View.getTimeout());
//		assertTrue(elementos.size() == 5);
//		//Ahora le damos a el enlace eliminar la oferta
//		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/oferta/eliminar')]");
//		elementos.get(elementos.size()-1).click();
//		//Ahora habr� cuatro elementos
//		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
//		assertTrue(elementos.size() == 4);
//	}
//	
//	
//
//	// BUSCAR OFERTAS
//
//	// PR20. Hacer una b�squeda con el campo vac�o y comprobar que se muestra la
//	// p�gina que corresponde con el listado de las ofertas existentes en el sistema
//	@Test
//	public void PR20() {
//		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
//		//Accedemos a la lista de todos los usuarios
//		PO_NavView.verListaTodos(driver);
//		//Comprobamos que hay cuatro ofertas disponibles
//		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
//		assertTrue(elementos.size() == 5);
//		//Comprobamos que si no introducimos nada en la barra de b�squeda y le damos a buscar...
//		PO_SearchBox.fillForm(driver, "");
//		//... sigue habiendo cuatro elementos justo
//		elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
//		assertTrue(elementos.size() == 5);
//	}
//
//	// PR21. Hacer una b�squeda escribiendo en el campo un texto que no exista y
//	// comprobar que se muestra la p�gina que corresponde, con la lista de ofertas
//	// vac�a.
//	@Test
//	public void PR21() {
//		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
//		//Accedemos a la lista de todos los usuarios
//		PO_NavView.verListaTodos(driver);
//		//Comprobamos que hay cuatro ofertas disponibles
//		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
//		assertTrue(elementos.size() == 5);
//		//Comprobamos que si introducimos un t�tulo cuyo nombre no se corresponde a ninguno, en la barra de b�squeda y le damos a buscar...
//		PO_SearchBox.fillForm(driver, "no existo");
//		//... ya no hay ning�n elemento
//		SeleniumUtils.textoNoPresentePagina(driver, "Comprar");
//	}
//
//	// PR22. Hacer una b�squeda escribiendo en el campo un texto en min�scula o
//	// may�scula y
//	// comprobar que se muestra la p�gina que corresponde, con la lista de ofertas
//	// que contengan
//	// dicho texto, independientemente que el t�tulo est� almacenado en min�sculas o
//	// may�scula.
//	@Test
//	public void PR22() {
//		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
//		//Accedemos a la lista de todas los ofertas
//		PO_NavView.verListaTodos(driver);
//		//Buscamos la oferta de canicas...
//		PO_SearchBox.fillForm(driver, "Canicas");
//		//... ahora solo hay un elemento
//		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
//		assertTrue(elementos.size() == 1);
//	}
//
//	// COMPRAR OFERTAS
//
//	// PR23. Sobre una b�squeda determinada (a elecci�n del desarrollador), comprar
//	// una oferta que deja un saldo positivo en el contador del comprador. Comprobar
//	// que el contador se actualiza correctamente en la vista del comprador.
//	@Test
//	public void PR23() {	
//		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
//		//Accedemos a la lista de todas los ofertas
//		PO_NavView.verListaTodos(driver);
//		//Buscamos la oferta que compraremos
//		PO_SearchBox.fillForm(driver, "Canicas");
//		//Le damos al bot�n de comprar, que nos dejar� comprar la �nica b�squeda resultante, canicas
//		driver.findElements(By.name("buttonComprar")).get(0).click();
//		//Comprobamos que el saldo se actualiza a 100-2 = 98
//		PO_NavView.comprobarSaldo(driver, "98 €");
//	}
//
//	// PR24. Sobre una b�squeda determinada (a elecci�n del desarrollador), comprar
//	// una oferta que deja un saldo 0 en el contador del comprador. Comprobar que el
//	// contador se actualiza correctamente en la vista del comprador.
//	@Test
//	public void PR24() {
//		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
//		//Accedemos a la lista de todas los ofertas
//		PO_NavView.verListaTodos(driver);
//		//Buscamos la oferta que compraremos
//		PO_SearchBox.fillForm(driver, "Bicicleta");
//		//Le damos al bot�n de comprar, que nos dejar� comprar la �nica b�squeda resultante, canicas
//		driver.findElements(By.name("buttonComprar")).get(0).click();
//		//Comprobamos que el saldo se actualiza a 100-100 = 0
//		PO_NavView.comprobarSaldo(driver, "0 €");
//	}
//
//	// PR25. Sobre una b�squeda determinada (a elecci�n del desarrollador), intentar
//	// comprar una oferta que est� por encima de saldo disponible del comprador. Y
//	// comprobar que se muestra el mensaje de saldo no suficiente.
//	@Test
//	public void PR25() {
//		PO_LoginView.accederUsuario(driver, "futbol345@gmail.com");
//		//Accedemos a la lista de todas los ofertas
//		PO_NavView.verListaTodos(driver);
//		//Buscamos la oferta que compraremos
//		PO_SearchBox.fillForm(driver, "Barco");
//		try {
//		driver.findElements(By.name("buttonComprar")).get(0).click();
//		} catch (ElementClickInterceptedException ex) {
//			//El bot�n est� deshabilitado
//		}
//		//Comprobamos que el saldo no se actualiza ya que el bot�n de comprar est� deshabilitado para aquellas ofertas que valgan m�s de lo que el usuario tiene de saldo
//		PO_NavView.comprobarSaldo(driver, "100 €");
//	}
//
//
//	// VER LISTADO COMPRAS PROPIAS
//
//	// PR26. Ir a la opci�n de ofertas compradas del usuario y mostrar la lista.
//	// Comprobar que aparecen las ofertas que deben aparecer.
//	@Test
//	public void PR26() {
//		PO_LoginView.accederUsuario(driver, "jueguetesParaTodos@gmail.com");
//		//Vemos la lista de ofertas compradas
//		PO_NavView.verListaComprados(driver);
//		//Comprobamos que tiene una oferta comprada
//		List<WebElement> elementosComprados = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
//				PO_View.getTimeout());
//		assertTrue(elementosComprados.size() == 3);
//	}
//		
//	// DESTACADO
//
//	// PR27. Al crear una oferta marcar dicha oferta como destacada y a continuaci�n comprobar: i)
//	// que aparece en el listado de ofertas destacadas para los usuarios y que el saldo del usuario se
//	// actualiza adecuadamente en la vista del ofertante (-20).
//	@Test
//	public void PR27() {
//		PO_LoginView.accederUsuario(driver, "jueguetesParaTodos@gmail.com");
//		PO_NavView.accessVender(driver);
//		//Al ponerle true le estamos diciendo que la oferta se destacar�
//		PO_AddItemView.fillForm(driver, "PR27Oferta", "PR27Descripcion", "15", true);
//		//Comprobamos que efectivamente el saldo se ha reducido en 20
//		PO_NavView.comprobarSaldo(driver, "80 €");
//		PO_NavView.disconnect(driver);
//		driver.navigate().to(URL);
//		
//		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
//		//Accedemos a la lista de todas las ofertas
//		PO_NavView.verListaTodos(driver);
//		//Comprobamos que hay una oferta destacada
//		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "PR27Oferta", PO_View.getTimeout());
//		assertEquals(elementos.size(), 1);
//	}
//		
//
//	
//	// PR28. Sobre el listado de ofertas de un usuario con m�s de 20 euros de saldo, pinchar en el
//	// enlace Destacada y a continuaci�n comprobar: i) que aparece en el listado de ofertas destacadas
//	// para los usuarios y que el saldo del usuario se actualiza adecuadamente en la vista del ofertante (-
//	// 20).
//	@Test
//	public void PR28() {
//		//Entramos con el usuario que luego comprobar� el n�mero de destacadas
//		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
//		//Accedemos a la lista de todas las ofertas
//		PO_NavView.verListaTodos(driver);
//		//Comprobamos primero el n�mero de ofertas destacadas, que son seis
//		List<WebElement> elementos = driver.findElements(By.name("buttonComprarDestacada"));
//		assertEquals(elementos.size(), 3);
//		PO_NavView.disconnect(driver);
//		driver.navigate().to(URL);
//		
//		//Entramos con el usuario que destacar� la oferta
//		PO_LoginView.accederUsuario(driver, "jueguetesParaTodos@gmail.com");
//		//Vamos a la lista de ofertas propias
//		PO_NavView.verListaPropia(driver);
//		//Vemos que el usuario tiene las tres ofertas que ha creado
//		 elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
//				PO_View.getTimeout());
//		//Ahora buscamos los botones para destacar
//		elementos = driver.findElements(By.name("buttonDestacar"));
//		//Y destacamos la primera
//		elementos.get(0).click();
//		//Vemos que el saldo se nos reduce en 20
//		PO_NavView.comprobarSaldo(driver, "80 €");
//		//Nos salimos de este usuario
//		PO_NavView.disconnect(driver);
//		driver.navigate().to(URL);
//		
//		//Entramos con otro usuario distinto para comprobar que se ve la nueva oferta destacada
//		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
//		//Accedemos a la lista de todas las ofertas
//		PO_NavView.verListaTodos(driver);
//		//Comprobamos primero el n�mero de ofertas destacadas, que son seis
//		elementos = driver.findElements(By.name("buttonComprarDestacada"));
//		assertEquals(elementos.size(), 4);
//	}
//
//
//	// PR29. Sobre el listado de ofertas de un usuario con menos de 20 euros de
//	// saldo, pinchar en el
//	// enlace Destacada y a continuaci�n comprobar que se muestra el mensaje de
//	// saldo no suficiente.
//	@Test
//	public void PR29() {
//		//Entramos como Laura y la bajamos el dinero a menos de 20 euros
//		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
//		//Accedemos a la lista de todas los ofertas
//		PO_NavView.verListaTodos(driver);
//		//Buscamos la oferta que compraremos
//		PO_SearchBox.fillForm(driver, "Raqueta de frontón");
//		//Le damos al bot�n de comprar, que nos dejar� comprar la �nica b�squeda resultante, canicas
//		driver.findElements(By.name("buttonComprar")).get(0).click();
//		PO_NavView.verListaPropia(driver);
//		List<WebElement> elementos = driver.findElements(By.name("buttonDestacar"));
//		//Y destacamos la primera
//		try {
//			elementos.get(0).click();
//		} catch (ElementClickInterceptedException ex) {
//			//El bot�n est� deshabilitado
//		}
//		//Vemos que el saldo no se ha reducido ya que no se ha destacado la oferta
//		PO_NavView.comprobarSaldo(driver, "10 €");
//	}
//	
//	
//	
//	// PREX01. Tras destacar una oferta, ver que podemos dejar de destacarla lo que nos devolver� 20 euros
//	@Test
//	public void PREX01() {
//		PO_LoginView.accederUsuario(driver, "jueguetesParaTodos@gmail.com");
//		//Vamos a ver la lista de ofertas propias
//		PO_NavView.verListaPropia(driver);
//		//Ahora buscamos los botones para destacar
//		List<WebElement> elementos = driver.findElements(By.name("buttonDejarDeDestacar"));
//		//Y destacamos la primera
//		elementos.get(0).click();
//		//Vemos que el saldo nos aumenta en 20 euros
//		PO_NavView.comprobarSaldo(driver, "120 €");
//	}
//
//
//	// PR30: Inicio de sesión con datos válidos
//	@Test
//	public void PR30() {
//		driver.navigate().to("https://localhost:8082/cliente.html?w=login");
//		// Llenamos con datos válidos
//		PO_LoginView.fillForm(driver, "carcargol@gmail.com", "123456");
//		// Nos aparece la vista de ofertas
//		PO_View.checkElement(driver, "text", "Ofertas");
//	}
//	
//	// PR31: Inicio de sesión con datos inválidos (email existente, pero contraseña incorrecta)
//	@Test
//	public void PR31() {
//		driver.navigate().to("https://localhost:8082/cliente.html?w=login");
//		// Llenamos con datos inválidos
//		PO_LoginView.fillForm(driver, "carcargol@gmail.com", "noesesta");
//		// Nos aparece un error
//		PO_View.checkElement(driver, "text", "Usuario no encontrado");
//	}
//	
//	// PR32: Inicio de sesión con datos válidos (campo email o contraseña vacíos)
//	@Test
//	public void PR32() {
//		driver.navigate().to("https://localhost:8082/cliente.html?w=login");
//		// Llenamos con datos inválidos
//		PO_LoginView.fillForm(driver, "carcargol@gmail.com", "");
//		// Nos aparece un error
//		PO_View.checkElement(driver, "text", "Usuario no encontrado");
//	}
//	
//	// PR33: Mostrar el listado de ofertas disponibles y comprobar que se muestran todas las que 
//	// existen, menos las del usuario identificado.
//	@Test
//	public void PR33() {
//		driver.navigate().to("https://localhost:8082/cliente.html?w=login");
//		// Llenamos con datos válidos
//		PO_LoginView.fillForm(driver, "carcargol@gmail.com", "123456");
//		
//		// Nos lleva a las ofertas
//		PO_View.checkElement(driver, "text", "Ofertas");
//		
//		// Comprobamos que estan todas 
//		PO_View.checkElement(driver, "text", "Con madera de roble");
//		PO_View.checkElement(driver, "text", "Hecha a mano");
//		PO_View.checkElement(driver, "text", "De la marca que marca más tendencia actualmente");
//		PO_View.checkElement(driver, "text", "Desgastados pero funcionales");
//		PO_View.checkElement(driver, "text", "Se incluye red");
//		PO_View.checkElement(driver, "text", "Versión exclusiva de la película titanic");
//		PO_View.checkElement(driver, "text", "Medio rota, pero sirve para piezas");
//		PO_View.checkElement(driver, "text", "Relleno de amor");
//		PO_View.checkElement(driver, "text", "Lleva lo que quieras a donde quieras");
//		PO_View.checkElement(driver, "text", "De excelente calidad");
//		PO_View.checkElement(driver, "text", "Un pequeño barco con apenas uso");
//		PO_View.checkElement(driver, "text", "De la última película");
//		PO_View.checkElement(driver, "text", "Se incluyen dos pelotas");
//		PO_View.checkElement(driver, "text", "La mejor bicicleta del país");
//		PO_View.checkElement(driver, "text", "Set de 12");
//	}
	
	// PR34: Sobre una búsqueda determinada de ofertas (a elección de desarrollador), enviar un 
	// mensaje a una oferta concreta. Se abriría dicha conversación por primera vez. Comprobar que el 
	// mensaje aparece en el listado de mensajes
	@Test
	public void PR34_35() {
		driver.navigate().to("https://localhost:8082/cliente.html?w=login");
		// Iniciamos sesión
		PO_LoginView.fillForm(driver, "carcargol@gmail.com", "123456");
		// Nos lleva a las ofertas
		PO_View.checkElement(driver, "text", "Ofertas");
		SeleniumUtils.esperarSegundos(driver, 2);
		
		// Buscamos el uinput para escribir el mensaje
		WebElement mensaje = driver.findElement(By.name("mensaje"));
		mensaje.click();
		mensaje.clear();
		mensaje.sendKeys("Hola");
		
		// Enviamos el mensaje
		WebElement botones = driver.findElement(By.name("enviar"));
		botones.click();	
		
		//Nos lleva a la conversación donde esta el mensaje
		SeleniumUtils.esperarSegundos(driver, 2);
		PO_View.checkElement(driver, "text", "Hola");
		PO_View.checkElement(driver, "text", "carcargol@gmail.com");
		
		// PR35:Sobre el listado de conversaciones enviar un mensaje a una conversación ya abierta. 
		// Comprobar que el mensaje aparece en el listado de mensajes.
		
		// Navegamos por el nav a las conversaciones
		WebElement tuCuenta = driver.findElement(By.id("navbarDarkAccount"));
		tuCuenta.click();
		
		WebElement conversaciones = driver.findElement(By.id("conversaciones"));
		conversaciones.click();
		
		SeleniumUtils.esperarSegundos(driver, 2);
		
		// Encontramos la conversacion y clickamos acceder 
		WebElement acceder = driver.findElement(By.id("acceder"));
		acceder.click();
		
		SeleniumUtils.esperarSegundos(driver, 2);
		
		// llenamos el mensaje a enviar
		WebElement mensaje2 = driver.findElement(By.name("elMensaje"));
		mensaje2.click();
		mensaje2.clear();
		mensaje2.sendKeys("que tal?");
		
		// Enviamos el mensaje
		By boton2 = By.name("enviar");
		driver.findElement(boton2).click();
		
		// Nos aparece el mensaje recien añadido
		SeleniumUtils.esperarSegundos(driver, 2);
		PO_View.checkElement(driver, "text", "que tal?");
		
	}
	
	// VER LISTADO CONVERSACIONES
	// PR36. Mostrar el listado de conversaciones ya abiertas. Comprobar que el
	// listado contiene las conversaciones que deben ser.
	@Test
	public void PR36() {
		driver.navigate().to("https://localhost:8082/cliente.html?w=login");
		// Iniciamos sesión
		PO_LoginView.fillForm(driver, "futbol345@gmail.com", "123456");
		// Nos lleva a las ofertas
		PO_View.checkElement(driver, "text", "Ofertas");
		SeleniumUtils.esperarSegundos(driver, 2);
		// Navegamos por el nav a las conversaciones
		WebElement tuCuenta = driver.findElement(By.id("navbarDarkAccount"));
		tuCuenta.click();
		
		WebElement conversaciones = driver.findElement(By.id("conversaciones"));
		conversaciones.click();
		
		SeleniumUtils.esperarSegundos(driver, 2);
		
		//Ahora buscamos los botones para acceder
		List<WebElement> botones = driver.findElements(By.id("acceder"));
		assertEquals(botones.size(),1);
	}
	
	// PR37. Sobre el listado de conversaciones ya abiertas. Pinchar el enlace
	// Eliminar de la primera y comprobar que el listado se actualiza correctamente.
	@Test
	public void PR37() {
		driver.navigate().to("https://localhost:8082/cliente.html?w=login");
		// Iniciamos sesión
		PO_LoginView.fillForm(driver, "futbol345@gmail.com", "123456");
		// Nos lleva a las ofertas
		PO_View.checkElement(driver, "text", "Ofertas");
		SeleniumUtils.esperarSegundos(driver, 2);
		
		// Buscamos el uinput para escribir el mensaje
		WebElement mensaje = driver.findElement(By.name("mensaje"));
		mensaje.click();
		mensaje.clear();
		mensaje.sendKeys("Hola");
		
		// Enviamos el mensaje
		List<WebElement> boton = driver.findElements(By.id("123jeremy@gmail.com"));
		boton.get(0).click();	
		
		SeleniumUtils.esperarSegundos(driver, 2);
		
		// Navegamos por el nav a las conversaciones
		WebElement tuCuenta = driver.findElement(By.id("navbarDarkAccount"));
		tuCuenta.click();
		
		WebElement conversaciones = driver.findElement(By.id("conversaciones"));
		conversaciones.click();
		
		SeleniumUtils.esperarSegundos(driver, 4);
		
		//Ahora buscamos los botones para acceder
		List<WebElement> botones = driver.findElements(By.id("acceder"));
		assertEquals(botones.size(),2);
		
		// Clickamos en eliminar la conversacion
		List<WebElement> eliminar = driver.findElements(By.id("eliminar"));
		eliminar.get(0).click();
		
		SeleniumUtils.esperarSegundos(driver, 2);
		// No se encuentra niguna
		botones = driver.findElements(By.id("acceder"));
		assertEquals(botones.size(),1);
		
		PO_View.checkElement(driver, "text", "123jeremy@gmail.com");
		
	}

	// PR38. Sobre el listado de conversaciones ya abiertas, pulsar el enlace
	// Eliminar de la �ltima y comprobar que el listado se actualiza correctamente.
	@Test
	public void PR38() {
		driver.navigate().to("https://localhost:8082/cliente.html?w=login");
		// Iniciamos sesión
		PO_LoginView.fillForm(driver, "123jeremy@gmail.com", "123456");
		// Nos lleva a las ofertas
		PO_View.checkElement(driver, "text", "Ofertas");
		SeleniumUtils.esperarSegundos(driver, 2);
		
		// Navegamos por el nav a las conversaciones
		WebElement tuCuenta = driver.findElement(By.id("navbarDarkAccount"));
		tuCuenta.click();
		
		WebElement conversaciones = driver.findElement(By.id("conversaciones"));
		conversaciones.click();
		
		SeleniumUtils.esperarSegundos(driver, 2);
		
		//Ahora buscamos los botones para acceder
		List<WebElement> botones = driver.findElements(By.id("acceder"));
		assertEquals(botones.size(),2);
		
		// Clickamos en eliminar la conversacion
		List<WebElement> eliminar = driver.findElements(By.id("eliminar"));
		eliminar.get(1).click();
		
		SeleniumUtils.esperarSegundos(driver, 2);
		
		botones = driver.findElements(By.id("acceder"));
		assertEquals(botones.size(),1);
		
		PO_View.checkElement(driver, "text", "paula_22@gmail.com");
	}
	
	// PR39.Identificarse en la aplicación y enviar un mensaje a una oferta, validar que el mensaje 
	// enviado aparece en el chat. Identificarse después con el usuario propietario de la oferta y validar 
	// que tiene un mensaje sin leer, entrar en el chat y comprobar que el mensaje pasa a tener el estado 
	// leído
	@Test
	public void PR39() {
		driver.navigate().to("https://localhost:8082/cliente.html?w=login");
		// Iniciamos sesión
		PO_LoginView.fillForm(driver, "cuatro23@gmail.com", "123456");
		// Nos lleva a las ofertas
		PO_View.checkElement(driver, "text", "Ofertas");

		SeleniumUtils.esperarSegundos(driver, 2);
		
		// Buscamos el uinput para escribir el mensaje
		WebElement mensaje = driver.findElement(By.name("mensaje"));
		mensaje.click();
		mensaje.clear();
		mensaje.sendKeys("Hola");
		
		// Enviamos el mensaje
		List<WebElement> botones = driver.findElements(By.id("futbol345@gmail.com"));
		botones.get(0).click();	
		
		//Nos lleva a la conversación donde esta el mensaje
		
		SeleniumUtils.esperarSegundos(driver, 4);
		// Navegamos por el nav a las conversaciones
		WebElement tuCuenta = driver.findElement(By.id("navbarDarkAccount"));
		tuCuenta.click();
		
		WebElement conversaciones = driver.findElement(By.id("conversaciones"));
		conversaciones.click();
		
		SeleniumUtils.esperarSegundos(driver, 5);
		
		// Aparece 0 como mensajes no leidos
		PO_View.checkElement(driver, "text", "0");
		
		driver.findElement(By.id("acceder")).click();
		
		SeleniumUtils.esperarSegundos(driver, 2);
		
		// Llenamos el mensaje a enviar
		WebElement mensaje2 = driver.findElement(By.name("elMensaje"));
		mensaje2.click();
		mensaje2.clear();
		mensaje2.sendKeys("que tal?");
		
		// Enviamos el mensaje
		By boton2 = By.name("enviar");
		driver.findElement(boton2).click();
		
		SeleniumUtils.esperarSegundos(driver, 2);
		
		//Aparece el mensaje
		PO_View.checkElement(driver, "text", "que tal?");
		
		//Volvemos a la pagina 
		driver.navigate().to("https://localhost:8082/cliente.html?w=login");
		
		// Iniciamos sesión con el propietario de la ofertas
		PO_LoginView.fillForm(driver, "futbol345@gmail.com", "123456");
		// Nos lleva a las ofertas
		PO_View.checkElement(driver, "text", "Ofertas");
		SeleniumUtils.esperarSegundos(driver, 2);
		// Navegamos por el nav a las conversaciones
		WebElement tuCuenta2 = driver.findElement(By.id("navbarDarkAccount"));
		tuCuenta2.click();
		
		SeleniumUtils.esperarSegundos(driver, 2);
		
		WebElement conversaciones2 = driver.findElement(By.id("conversaciones"));
		conversaciones2.click();
		
		SeleniumUtils.esperarSegundos(driver, 7);
		
		driver.findElement(By.name("cuatro23@gmail.com")).click();
		
		SeleniumUtils.esperarSegundos(driver, 5);
		
		// Navegamos por el nav a las conversaciones
		WebElement tuCuenta3 = driver.findElement(By.id("navbarDarkAccount"));
		tuCuenta3.click();
		
		WebElement conversaciones3 = driver.findElement(By.id("conversaciones"));
		conversaciones3.click();
		
		SeleniumUtils.esperarSegundos(driver, 2);
		
		tuCuenta3.click();
		conversaciones3.click();

		SeleniumUtils.esperarSegundos(driver, 7);
		
		// Aparece 0 como mensajes no leidos
		PO_View.checkElement(driver, "text", "0");
		
	}
	
	// PR40:Identificarse en la aplicación y enviar tres mensajes a una oferta, validar que los mensajes 
	//enviados aparecen en el chat. Identificarse después con el usuario propietario de la oferta y 
	//validar que el número de mensajes sin leer aparece en su oferta
	@Test
	public void PR40() {
		driver.navigate().to("https://localhost:8082/cliente.html?w=login");
		// Iniciamos sesión
		PO_LoginView.fillForm(driver, "cuatro23@gmail.com", "123456");
		// Nos lleva a las ofertas
		PO_View.checkElement(driver, "text", "Ofertas");

		SeleniumUtils.esperarSegundos(driver, 2);
		
		// Buscamos el uinput para escribir el mensaje
		WebElement mensaje = driver.findElement(By.name("mensaje"));
		mensaje.click();
		mensaje.clear();
		mensaje.sendKeys("Hola");
		
		// Enviamos el mensaje
		List<WebElement> botones = driver.findElements(By.id("futbol345@gmail.com"));
		botones.get(0).click();		
		
		//Nos lleva a la conversación donde esta el mensaje
		
		SeleniumUtils.esperarSegundos(driver, 4);
		
		// Llenamos el mensaje a enviar
		WebElement mensaje2 = driver.findElement(By.name("elMensaje"));
		mensaje2.click();
		mensaje2.clear();
		mensaje2.sendKeys("que tal?");
		
		// Enviamos el mensaje
		By boton = By.name("enviar");
		driver.findElement(boton).click();
		
		WebElement mensaje3 = driver.findElement(By.name("elMensaje"));
		mensaje3.click();
		mensaje3.clear();
		mensaje3.sendKeys("todo bien?");
		
		// Enviamos el mensaje
		By boton2 = By.name("enviar");
		driver.findElement(boton2).click();
		
		SeleniumUtils.esperarSegundos(driver, 2);
		
		PO_View.checkElement(driver, "text", "Hola");
		PO_View.checkElement(driver, "text", "que tal?");
		PO_View.checkElement(driver, "text", "todo bien?");
		
		//Volvemos a la pagina 
		driver.navigate().to("https://localhost:8082/cliente.html?w=login");
		
		// Iniciamos sesión con el propietario de la ofertas
		PO_LoginView.fillForm(driver, "futbol345@gmail.com", "123456");
		// Nos lleva a las ofertas
		PO_View.checkElement(driver, "text", "Ofertas");
		SeleniumUtils.esperarSegundos(driver, 2);
		// Navegamos por el nav a las conversaciones
		WebElement tuCuenta2 = driver.findElement(By.id("navbarDarkAccount"));
		tuCuenta2.click();
		
		WebElement conversaciones2 = driver.findElement(By.id("conversaciones"));
		conversaciones2.click();
		tuCuenta2.click();
		conversaciones2.click();
		
		SeleniumUtils.esperarSegundos(driver, 7);
		
		// Vemos que hay 3 mensajes sin leer
		WebElement noLeidos = driver.findElement(By.id("02"));
		assertTrue(noLeidos != null);
	}
	
}
