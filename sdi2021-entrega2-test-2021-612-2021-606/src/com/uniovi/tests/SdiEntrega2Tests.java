package com.uniovi.tests;

//Paquetes Java
import java.util.List;
//Paquetes JUnit 
import org.junit.*;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
	static String Geckdriver024 = "C:\\Program Files\\Mozilla Firefox\\geckodriver024win64.exe";
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

	
	// SIGN UP

	// PR01. Registro de Usuario con datos v�lidos.
	@Test
	public void PR01() {
		PO_HomeView.clickOption(driver, "/registrarse", "class", "w-100 btn btn-lg btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "PR01@PR01.com", "NombrePR01", "ApellidoPR01", "123456", "123456");
		// Comprobamos que entramos en la secci�n privada
		PO_View.checkElement(driver, "text", "Bienvenido/a a MyWallapop");
	}

	// PR02. Registro de Usuario con datos inv�lidos (email vac�o, nombre vac�o,
	// apellidos vac�os).
	@Test
	public void PR02() {
		PO_HomeView.clickOption(driver, "/registrarse", "class", "w-100 btn btn-lg btn-primary");
		// Comprobamos el error de nombre vac�o
		PO_RegisterView.fillForm(driver,  "PR02@PR02.com", "", "ApellidoPR02", "123456", "123456");
		PO_View.checkElement(driver, "text", "Se tiene que a�adir un nombre");
		// Comprobamos el error de email vac�o
	 	PO_RegisterView.fillForm(driver,  "", "NombrePR02", "ApellidoPR02", "123456", "123456");
		PO_View.checkElement(driver, "text", "Se tiene que a�adir un email");
		//Comprobamos el error de apellidos vac�os
		PO_RegisterView.fillForm(driver,  "PR02@PR02.com", "NombrePR02", "", "123456", "123456");
		PO_View.checkElement(driver, "text", "Se tiene que insertar un apellido");
		//Comprobamos los tres errores a la vez
		PO_RegisterView.fillForm(driver,  "", "", "", "123456", "123456");
		PO_View.checkElement(driver, "text", "Se tiene que a�adir un nombre");
		PO_View.checkElement(driver, "text", "Se tiene que a�adir un email");
		PO_View.checkElement(driver, "text", "Se tiene que insertar un apellido");
	}

	// PR03. Registro de Usuario con datos inv�lidos (repetici�n de contrase�a
	// inv�lida).
	@Test
	public void PR03() {
		PO_HomeView.clickOption(driver, "/registrarse", "class", "w-100 btn btn-lg btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "PR03@PR03.com", "NombrePR03", "ApellidoPR03", "123456", "654321");
		//Se nos muestra el error de que las contrase�as no coinciden
		PO_View.checkElement(driver, "text", "Las contrase�as no coinciden");
	}

	// PR04. Registro de Usuario con datos inv�lidos (email existente).
	@Test
	public void PR04() {
		PO_HomeView.clickOption(driver, "/registrarse", "class", "w-100 btn btn-lg btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "admin@email.com", "YaExisto", "YaExisto", "123456", "123456");
		PO_View.getP();
		// Comprobamos el error de email ya registrado
		PO_View.checkElement(driver, "text", "El usuario ya se encuentra registrado");
	}

	// LOGIN


	// PR05. Inicio de sesi�n con datos v�lidos.
	@Test
	public void PR05() {
		//Clickamos en la opci�n de loggearse
		PO_HomeView.clickOption(driver, "/identificarse", "class", "w-100 btn btn-lg btn-primary");
		// Rellenamos el formulario con los datos del usuario que acabamos de crear
		PO_LoginView.fillForm(driver, "laura_mar@gmail.com", "123456");
		// Comprobamos que entramos en la pagina privada
		PO_View.checkElement(driver, "text", "Bienvenido/a a MyWallapop");
	}
	

	// PR06. Inicio de sesi�n con datos inv�lidos (usuario est�ndar, email existente,
	// pero contrase�a incorrecta).
	@Test
	public void PR06() {
		PO_HomeView.clickOption(driver, "/identificarse", "class", "w-100 btn btn-lg btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "admin@email.com", "123");
		// Comprobamos que la contrase�a para el administrador es incorrecta
		PO_View.checkElement(driver, "text", "El usuario no existe en la base de datos o la contrase�a es incorrecta");
	}

	// PR07. Inicio de sesi�n con datos inv�lidos (usuario est�ndar, campo email y
	// contrase�a vac�os).
	@Test
	public void PR07() {
		PO_HomeView.clickOption(driver, "/identificarse", "class", "w-100 btn btn-lg btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "", "123");
		// Comprobamos que nos pide un email
		PO_View.checkElement(driver, "text", "Se tiene que a�adir un email");
		
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "admin@email.com", "");
		// Comprobamos que nos pide una contrase�a
		PO_View.checkElement(driver, "text", "Se tiene que a�adir una contrase�a");	

		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "", "");
		// Comprobamos que si dejamos los dos campos vac�os, se nos muestran los dos errores
		PO_View.checkElement(driver, "text", "Se tiene que a�adir un email");
		PO_View.checkElement(driver, "text", "Se tiene que a�adir una contrase�a");
		
	}


	// PR08. Inicio de sesi�n con datos inv�lidos (usuario est�ndar, email no
	// existente en la aplicaci�n).
	@Test
	public void PR08() {
		PO_HomeView.clickOption(driver, "/identificarse", "class", "w-100 btn btn-lg btn-primary");
		// Rellenamos el formulario con un usuario que no existe en la base de datos
		PO_LoginView.fillForm(driver, "usuarioNoExiste@usuarioNoExiste.com", "123");
		// Comprobamos que nos sale el error correspondiente
		PO_View.checkElement(driver, "text", "El usuario no existe en la base de datos o la contrase�a es incorrecta");
	}

	// LOGOUT

	// PR09. Hacer click en la opci�n de salir de sesi�n y comprobar que se redirige
	// a la p�gina de inicio de sesi�n (Login).
	@Test
	public void PR09() {
		//Accedemos a identificarnos
		PO_HomeView.clickOption(driver, "/identificarse", "class", "w-100 btn btn-lg btn-primary");
		// Rellenamos el formulario con el usuario que acabamos de crear
		PO_LoginView.fillForm(driver, "laura_mar@gmail.com", "123456");
		// Comprobamos que entramos en la pagina privada
		PO_View.checkElement(driver, "text", "Bienvenido/a a MyWallapop");
		//Nos desconectamos
		PO_NavView.disconnect(driver);
		//Ahora estamos en la p�gina de login
		PO_View.checkElement(driver, "text", "Accede a la aplicaci�n con un usuario ya identificado");

	}

	// PR10. Comprobar que el bot�n cerrar sesi�n no est� visible si el usuario no
	// est� autenticado.
	@Test
	public void PR10() {
		//Nos intentamos identificar como un usuario que no existe
		PO_HomeView.clickOption(driver, "/identificarse", "class", "w-100 btn btn-lg btn-primary");
		PO_LoginView.fillForm(driver, "usuarioNoExiste@usuarioNoExiste.com", "123");
		PO_View.checkElement(driver, "text", "El usuario no existe en la base de datos o la contrase�a es incorrecta");
		//El enlace para desconectarse no estar� presente ya que se encuentra en la p�gina privada a la cual solo podemos acceder una vez nos hayamos identificado
		SeleniumUtils.textoNoPresentePagina(driver, "enlace-logout");
	}

	// LISTA USUARIOS

	// PR11. Mostrar el listado de usuarios y comprobar que se muestran todos los
	// que existen en el sistema.
	@Test
	public void PR11() {
		//Accedemos como administrador
		PO_LoginView.accederAdmin(driver);
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		//Comprobamos que se encuentran los siete usuarios que hay en la base de datos
		assertTrue(elementos.size() == 7);
	}


	// BORRAR USUARIOS

	// PR12. Ir a la lista de usuarios, borrar el primer usuario de la lista,
	// comprobar que la lista se actualiza y que el usuario desaparece.
	@Test
	public void PR12() {
		//Accedemos como administrador
		PO_LoginView.accederAdmin(driver);
		List<WebElement> elementos = driver.findElements(By.name("deleteUser"));
		//Comprobamos que se encuentran los siete usuarios que hay en la base de datos
		assertTrue(elementos.size() == 7);
		//Clickamos el checkbox del primero
		elementos.get(0).click();
		//Y ahora le damos a borrar
		elementos = driver.findElements(By.name("buttonEliminar"));
		elementos.get(0).click();
		//As� comprobamos que ahora hay seis usuarios
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 6);
	}

	// PR13. Ir a la lista de usuarios, borrar el �ltimo usuario de la lista,
	// comprobar que la lista se actualiza y que el usuario desaparece.
	@Test
	public void PR13() {
		//Accedemos como administrador
		PO_LoginView.accederAdmin(driver);
		List<WebElement> elementos = driver.findElements(By.name("deleteUser"));
		//Comprobamos que se encuentran los siete usuarios que hay en la base de datos
		assertTrue(elementos.size() == 7);
		//Clickamos el checkbox del �ltimo
		elementos.get(elementos.size()-1).click();
		//Y ahora le damos a borrar
		elementos = driver.findElements(By.name("buttonEliminar"));
		elementos.get(0).click();
		//As� comprobamos que ahora hay seis usuarios en el listado
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 6);	
	}

	// PR14.Ir a la lista de usuarios, borrar 3 usuarios, comprobar que la lista se
	// actualiza y que los usuarios desaparecen.
	@Test
	public void PR14() {
		//Accedemos como administrador
		PO_LoginView.accederAdmin(driver);
		List<WebElement> elementos = driver.findElements(By.name("deleteUser"));
		//Comprobamos que se encuentran los siete usuarios
		assertTrue(elementos.size() == 7);
		//Seleccionamos los checkbox de los tres primeros
		elementos.get(0).click();
		elementos.get(1).click();
		elementos.get(2).click();
		//Y los borramos
		elementos = driver.findElements(By.name("buttonEliminar"));
		elementos.get(0).click();
		//Ahora vemos que hay cuatro usuarios en el listado
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 4);
	}

	// A�ADIR OFERTA

	// PR15. Ir al formulario de alta de oferta, rellenarla con datos v�lidos y
	// pulsar el bot�n Submit. Comprobar que la oferta sale en el listado de ofertas
	// de dicho usuario.
	@Test
	public void PR15() {
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Vamos a vender
		PO_NavView.accessVender(driver);
		//A�adimos una oferta
		PO_AddItemView.fillForm(driver, "PR15Oferta", "PR15Descripcion", "15", false);
		//Ahora accedemos al listado de ofertas propias y vemos que se ha registrado en el listado
		PO_NavView.verListaPropia(driver);
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 6);
	}

	// PR16. Ir al formulario de alta de oferta, rellenarla con datos inv�lidos
	// (campo t�tulo vac�o y precio en negativo) y pulsar el bot�n Submit. Comprobar que se muestra el
	// mensaje de campo obligatorio.
	@Test
	public void PR16() {
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Vamos a vender
		PO_NavView.accessVender(driver);
		//A�adimos una oferta que no tenga t�tulo y con precio negativo
		PO_AddItemView.fillForm(driver, "", "PR16Descripcion", "-5.0", false);
		//Vemos que nos aparecen los dos errores
		PO_View.checkElement(driver, "text", "Se tiene que a�adir un t�tulo");
		PO_View.checkElement(driver, "text", "El precio no puede ser negativo o cero");
	}

	// LISTADO OFERTAS PROPIAS

	// PR17. Mostrar el listado de ofertas para dicho usuario y comprobar que se
	// muestran todas los que existen para este usuario.
	@Test
	public void PR17() {
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Vamos a la lista de ofertas propias
		PO_NavView.verListaPropia(driver);
		//Vemos que el usuario tiene las tres ofertas que ha creado
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
	}

	// ELIMINAR OFERTA

	// PR18. Ir a la lista de ofertas, borrar la primera oferta de la lista,
	// comprobar que la lista se actualiza y que la oferta desaparece.
	@Test
	public void PR18() {	
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Vamos a la lista de ofertas propias
		PO_NavView.verListaPropia(driver);
		//Vemos que el usuario tiene cinco ofertas
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
		//Ahora le damos a el enlace eliminar la oferta
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/oferta/eliminar')]");
		elementos.get(0).click();
		//Ahora habr� cuatro elementos
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 4);
	}

	// PR19. Ir a la lista de ofertas, borrar la �ltima oferta de la lista,
	// comprobar que la lista se actualiza y que la oferta desaparece.
	@Test
	public void PR19() {
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Vamos a la lista de ofertas propias
		PO_NavView.verListaPropia(driver);
		//Vemos que el usuario tiene cinco ofertas
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
		//Ahora le damos a el enlace eliminar la oferta
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/oferta/eliminar')]");
		elementos.get(elementos.size()-1).click();
		//Ahora habr� cuatro elementos
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 4);
	}
	
	

	// BUSCAR OFERTAS

	// PR20. Hacer una b�squeda con el campo vac�o y comprobar que se muestra la
	// p�gina que corresponde con el listado de las ofertas existentes en el sistema
	@Test
	public void PR20() {
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Accedemos a la lista de todos los usuarios
		PO_NavView.verListaTodos(driver);
		//Comprobamos que hay cuatro ofertas disponibles
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
		//Comprobamos que si no introducimos nada en la barra de b�squeda y le damos a buscar...
		PO_SearchBox.fillForm(driver, "");
		//... sigue habiendo cuatro elementos justo
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
	}

	// PR21. Hacer una b�squeda escribiendo en el campo un texto que no exista y
	// comprobar que se muestra la p�gina que corresponde, con la lista de ofertas
	// vac�a.
	@Test
	public void PR21() {
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Accedemos a la lista de todos los usuarios
		PO_NavView.verListaTodos(driver);
		//Comprobamos que hay cuatro ofertas disponibles
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
		//Comprobamos que si introducimos un t�tulo cuyo nombre no se corresponde a ninguno, en la barra de b�squeda y le damos a buscar...
		PO_SearchBox.fillForm(driver, "no existo");
		//... ya no hay ning�n elemento
		SeleniumUtils.textoNoPresentePagina(driver, "Comprar");
	}

	// PR22. Hacer una b�squeda escribiendo en el campo un texto en min�scula o
	// may�scula y
	// comprobar que se muestra la p�gina que corresponde, con la lista de ofertas
	// que contengan
	// dicho texto, independientemente que el t�tulo est� almacenado en min�sculas o
	// may�scula.
	@Test
	public void PR22() {
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Accedemos a la lista de todas los ofertas
		PO_NavView.verListaTodos(driver);
		//Buscamos la oferta de canicas...
		PO_SearchBox.fillForm(driver, "Canicas");
		//... ahora solo hay un elemento
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
		assertTrue(elementos.size() == 1);
	}

	// COMPRAR OFERTAS

	// PR23. Sobre una b�squeda determinada (a elecci�n del desarrollador), comprar
	// una oferta que deja un saldo positivo en el contador del comprador. Comprobar
	// que el contador se actualiza correctamente en la vista del comprador.
	@Test
	public void PR23() {	
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Accedemos a la lista de todas los ofertas
		PO_NavView.verListaTodos(driver);
		//Buscamos la oferta que compraremos
		PO_SearchBox.fillForm(driver, "Canicas");
		//Le damos al bot�n de comprar, que nos dejar� comprar la �nica b�squeda resultante, canicas
		driver.findElements(By.name("buttonComprar")).get(0).click();
		//Comprobamos que el saldo se actualiza a 100-2 = 98
		PO_NavView.comprobarSaldo(driver, "98 �");
	}

	// PR24. Sobre una b�squeda determinada (a elecci�n del desarrollador), comprar
	// una oferta que deja un saldo 0 en el contador del comprador. Comprobar que el
	// contador se actualiza correctamente en la vista del comprador.
	@Test
	public void PR24() {
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Accedemos a la lista de todas los ofertas
		PO_NavView.verListaTodos(driver);
		//Buscamos la oferta que compraremos
		PO_SearchBox.fillForm(driver, "Bicicleta");
		//Le damos al bot�n de comprar, que nos dejar� comprar la �nica b�squeda resultante, canicas
		driver.findElements(By.name("buttonComprar")).get(0).click();
		//Comprobamos que el saldo se actualiza a 100-100 = 0
		PO_NavView.comprobarSaldo(driver, "0 �");
	}

	// PR25. Sobre una b�squeda determinada (a elecci�n del desarrollador), intentar
	// comprar una oferta que est� por encima de saldo disponible del comprador. Y
	// comprobar que se muestra el mensaje de saldo no suficiente.
	@Test
	public void PR25() {
		PO_LoginView.accederUsuario(driver, "futbol345@gmail.com");
		//Accedemos a la lista de todas los ofertas
		PO_NavView.verListaTodos(driver);
		//Buscamos la oferta que compraremos
		PO_SearchBox.fillForm(driver, "Barco");
		try {
		driver.findElements(By.name("buttonComprar")).get(0).click();
		} catch (ElementClickInterceptedException ex) {
			//El bot�n est� deshabilitado
		}
		//Comprobamos que el saldo no se actualiza ya que el bot�n de comprar est� deshabilitado para aquellas ofertas que valgan m�s de lo que el usuario tiene de saldo
		PO_NavView.comprobarSaldo(driver, "100 �");
	}


	// VER LISTADO COMPRAS PROPIAS

	// PR26. Ir a la opci�n de ofertas compradas del usuario y mostrar la lista.
	// Comprobar que aparecen las ofertas que deben aparecer.
	@Test
	public void PR26() {
		PO_LoginView.accederUsuario(driver, "jueguetesParaTodos@gmail.com");
		//Vemos la lista de ofertas compradas
		PO_NavView.verListaComprados(driver);
		//Comprobamos que tiene una oferta comprada
		List<WebElement> elementosComprados = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementosComprados.size() == 3);
	}
		
	// DESTACADO

	// PR27. Al crear una oferta marcar dicha oferta como destacada y a continuaci�n comprobar: i)
	// que aparece en el listado de ofertas destacadas para los usuarios y que el saldo del usuario se
	// actualiza adecuadamente en la vista del ofertante (-20).
	@Test
	public void PR27() {
		PO_LoginView.accederUsuario(driver, "jueguetesParaTodos@gmail.com");
		PO_NavView.accessVender(driver);
		//Al ponerle true le estamos diciendo que la oferta se destacar�
		PO_AddItemView.fillForm(driver, "PR27Oferta", "PR27Descripcion", "15", true);
		//Comprobamos que efectivamente el saldo se ha reducido en 20
		PO_NavView.comprobarSaldo(driver, "80 �");
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Accedemos a la lista de todas las ofertas
		PO_NavView.verListaTodos(driver);
		//Comprobamos que hay una oferta destacada
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "PR27Oferta", PO_View.getTimeout());
		assertEquals(elementos.size(), 1);
	}
		

	
	// PR28. Sobre el listado de ofertas de un usuario con m�s de 20 euros de saldo, pinchar en el
	// enlace Destacada y a continuaci�n comprobar: i) que aparece en el listado de ofertas destacadas
	// para los usuarios y que el saldo del usuario se actualiza adecuadamente en la vista del ofertante (-
	// 20).
	@Test
	public void PR28() {
		//Entramos con el usuario que luego comprobar� el n�mero de destacadas
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Accedemos a la lista de todas las ofertas
		PO_NavView.verListaTodos(driver);
		//Comprobamos primero el n�mero de ofertas destacadas, que son seis
		List<WebElement> elementos = driver.findElements(By.name("buttonComprarDestacada"));
		assertEquals(elementos.size(), 3);
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		//Entramos con el usuario que destacar� la oferta
		PO_LoginView.accederUsuario(driver, "jueguetesParaTodos@gmail.com");
		//Vamos a la lista de ofertas propias
		PO_NavView.verListaPropia(driver);
		//Vemos que el usuario tiene las tres ofertas que ha creado
		 elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		//Ahora buscamos los botones para destacar
		elementos = driver.findElements(By.name("buttonDestacar"));
		//Y destacamos la primera
		elementos.get(0).click();
		//Vemos que el saldo se nos reduce en 20
		PO_NavView.comprobarSaldo(driver, "80 �");
		//Nos salimos de este usuario
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		//Entramos con otro usuario distinto para comprobar que se ve la nueva oferta destacada
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Accedemos a la lista de todas las ofertas
		PO_NavView.verListaTodos(driver);
		//Comprobamos primero el n�mero de ofertas destacadas, que son seis
		elementos = driver.findElements(By.name("buttonComprarDestacada"));
		assertEquals(elementos.size(), 4);
	}


	// PR29. Sobre el listado de ofertas de un usuario con menos de 20 euros de
	// saldo, pinchar en el
	// enlace Destacada y a continuaci�n comprobar que se muestra el mensaje de
	// saldo no suficiente.
	@Test
	public void PR29() {
		//Entramos como Laura y la bajamos el dinero a menos de 20 euros
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Accedemos a la lista de todas los ofertas
		PO_NavView.verListaTodos(driver);
		//Buscamos la oferta que compraremos
		PO_SearchBox.fillForm(driver, "Raqueta de front�n");
		//Le damos al bot�n de comprar, que nos dejar� comprar la �nica b�squeda resultante, canicas
		driver.findElements(By.name("buttonComprar")).get(0).click();
		PO_NavView.verListaPropia(driver);
		List<WebElement> elementos = driver.findElements(By.name("buttonDestacar"));
		//Y destacamos la primera
		try {
			elementos.get(0).click();
		} catch (ElementClickInterceptedException ex) {
			//El bot�n est� deshabilitado
		}
		//Vemos que el saldo no se ha reducido ya que no se ha destacado la oferta
		PO_NavView.comprobarSaldo(driver, "10 �");
	}
	
	
	
	// PREX01. Tras destacar una oferta, ver que podemos dejar de destacarla lo que nos devolver� 20 euros
	@Test
	public void PREX01() {
		PO_LoginView.accederUsuario(driver, "jueguetesParaTodos@gmail.com");
		//Vamos a ver la lista de ofertas propias
		PO_NavView.verListaPropia(driver);
		//Ahora buscamos los botones para destacar
		List<WebElement> elementos = driver.findElements(By.name("buttonDejarDeDestacar"));
		//Y destacamos la primera
		elementos.get(0).click();
		//Vemos que el saldo nos aumenta en 20 euros
		PO_NavView.comprobarSaldo(driver, "120 �");
	}


//	// VER LISTADO CONVERSACIONES
//
//	// PR36. Mostrar el listado de conversaciones ya abiertas. Comprobar que el
//	// listado contiene las conversaciones que deben ser.
//	@Test
//	public void PR36() {
//		PO_LoginView.defaultLogin(driver, "user1@gmail.com");
//		PO_NavView.verListaConversaciones(driver);
//		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
//				PO_View.getTimeout());
//		assertTrue(elementos.size() == 2);
//	}
//	
//	// PR37. Sobre el listado de conversaciones ya abiertas. Pinchar el enlace
//	// Eliminar de la primera y comprobar que el listado se actualiza correctamente.
//	public void PR37() {
//		PO_LoginView.defaultLogin(driver, "user1@gmail.com");
//		PO_NavView.verListaConversaciones(driver);
//		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
//				PO_View.getTimeout());
//		assertTrue(elementos.size() == 2);
//
//		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'conversaciones/delete')]");
//		elementos.get(0).click();
//		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
//		assertTrue(elementos.size() == 1);
//	}
//
//	// PR38. Sobre el listado de conversaciones ya abiertas, pulsar el enlace
//	// Eliminar de la �ltima y comprobar que el listado se actualiza correctamente.
//	@Test
//	public void PR38() {
//		PO_LoginView.defaultLogin(driver, "user1@gmail.com");
//		PO_NavView.verListaConversaciones(driver);
//		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
//				PO_View.getTimeout());
//		assertTrue(elementos.size() == 2);
//
//		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'conversaciones/delete')]");
//		elementos.get(1).click();
//		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
//		assertTrue(elementos.size() == 1);
//	}
//	
}
