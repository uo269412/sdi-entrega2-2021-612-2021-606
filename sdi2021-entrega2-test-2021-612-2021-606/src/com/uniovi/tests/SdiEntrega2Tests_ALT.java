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

//Ordenamos las pruebas por el nombre del mÃ©todo
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SdiEntrega2Tests_ALT {
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
		driver.navigate().to(URL + "/resetDatabase");

	}

	@After
	public void tearDown() {
		driver.manage().deleteAllCookies();
	}

	@BeforeClass
	static public void begin() {
		// COnfiguramos las pruebas.
		// Fijamos el timeout en cada opciÃ³n de carga de una vista. 2 segundos.
		PO_View.setTimeout(3);

	}

	@AfterClass
	static public void end() {
		// Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}

	// LOGIN

	// PR05. Inicio de sesión con datos válidos.
	@Test
	public void PR05() {
		//Primero registramos al usuario
		PO_RegisterView.registrarUsuarioTest(driver, 5);
		//Después de haberlo creado nos desconectamos
		PO_NavView.disconnect(driver);
		//Y volvemos a la pantalla principal
		driver.navigate().to(URL);
		//Clickamos en la opción de loggearse
		PO_HomeView.clickOption(driver, "/identificarse", "class", "w-100 btn btn-lg btn-primary");
		// Rellenamos el formulario con los datos del usuario que acabamos de crear
		PO_LoginView.fillForm(driver, "PR5@PR5.com", "123456");
		// Comprobamos que entramos en la pagina privada
		PO_View.checkElement(driver, "text", "Bienvenido/a a MyWallapop");
	}
	

	// PR08. Inicio de sesión con datos inválidos (usuario estándar, email no
	// existente en la aplicación).
	@Test
	public void PR08() {
		PO_HomeView.clickOption(driver, "/identificarse", "class", "w-100 btn btn-lg btn-primary");
		// Rellenamos el formulario con un usuario que no existe en la base de datos
		PO_LoginView.fillForm(driver, "usuarioNoExiste@usuarioNoExiste.com", "123");
		// Comprobamos que nos sale el error correspondiente
		PO_View.checkElement(driver, "text", "El usuario no existe en la base de datos o la contraseña es incorrecta");
	}

	// LOGOUT

	// PR09. Hacer click en la opción de salir de sesión y comprobar que se redirige
	// a la página de inicio de sesión (Login).
	@Test
	public void PR09() {
		//Primero registramos el usuario que vamos a utilizar
		PO_RegisterView.registrarUsuarioTest(driver, 9);
		//Nos desconectamos correctamente
		PO_NavView.disconnect(driver);
		//Volvemos a la página principal
		driver.navigate().to(URL);
		
		//Accedemos a identificarnos
		PO_HomeView.clickOption(driver, "/identificarse", "class", "w-100 btn btn-lg btn-primary");
		// Rellenamos el formulario con el usuario que acabamos de crear
		PO_LoginView.fillForm(driver, "PR9@PR9.com", "123456");
		// Comprobamos que entramos en la pagina privada
		PO_View.checkElement(driver, "text", "Bienvenido/a a MyWallapop");
		//Nos desconectamos
		PO_NavView.disconnect(driver);
		//Ahora estamos en la página de login
		PO_View.checkElement(driver, "text", "Accede a la aplicación con un usuario ya identificado");

	}

	// LISTA USUARIOS

	// PR11. Mostrar el listado de usuarios y comprobar que se muestran todos los
	// que existen en el sistema.
	@Test
	public void PR11() {
		//Utilizamos este método para crear tres usuarios en la base de datos
		PO_RegisterView.createUsers(driver, URL, 11, 3);
		//Accedemos como administrador
		PO_LoginView.accederAdmin(driver);
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		//Comprobamos que se encuentran los tres usuarios que creamos previamente
		assertTrue(elementos.size() == 3);
	}


	// BORRAR USUARIOS

	// PR12. Ir a la lista de usuarios, borrar el primer usuario de la lista,
	// comprobar que la lista se actualiza y que el usuario desaparece.
	@Test
	public void PR12() {
		//Utilizamos este método para crear tres usuarios en la base de datos
		PO_RegisterView.createUsers(driver, URL, 12, 3);
		//Accedemos como administrador
		PO_LoginView.accederAdmin(driver);
		List<WebElement> elementos = driver.findElements(By.name("deleteUser"));
		//Comprobamos que se encuentran los tres usuarios que creamos previamente
		assertTrue(elementos.size() == 3);
		//Clickamos el checkbox del primero
		elementos.get(0).click();
		//Y ahora le damos a borrar
		elementos = driver.findElements(By.name("buttonEliminar"));
		elementos.get(0).click();
		//Así comprobamos que ahora hay dos usuarios en el listado
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 2);
	}

	// PR13. Ir a la lista de usuarios, borrar el último usuario de la lista,
	// comprobar que la lista se actualiza y que el usuario desaparece.
	@Test
	public void PR13() {
		//Utilizamos este método para crear tres usuarios en la base de datos
		PO_RegisterView.createUsers(driver, URL, 13, 3);
		//Accedemos como administrador
		PO_LoginView.accederAdmin(driver);
		List<WebElement> elementos = driver.findElements(By.name("deleteUser"));
		//Comprobamos que se encuentran los tres usuarios que creamos previamente
		assertTrue(elementos.size() == 3);
		//Clickamos el checkbox del último
		elementos.get(elementos.size()-1).click();
		//Y ahora le damos a borrar
		elementos = driver.findElements(By.name("buttonEliminar"));
		elementos.get(0).click();
		//Así comprobamos que ahora hay dos usuarios en el listado
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 2);	
	}

	// PR14.Ir a la lista de usuarios, borrar 3 usuarios, comprobar que la lista se
	// actualiza y que los usuarios desaparecen.
	@Test
	public void PR14() {
		//Utilizamos este método para crear seis usuarios en la base de datos
		PO_RegisterView.createUsers(driver, URL, 14, 6);
		//Accedemos como administrador
		PO_LoginView.accederAdmin(driver);
		List<WebElement> elementos = driver.findElements(By.name("deleteUser"));
		//Comprobamos que se encuentran los seis usuarios que creamos previamente
		assertTrue(elementos.size() == 6);
		//Seleccionamos los checkbox de los tres primeros
		elementos.get(0).click();
		elementos.get(1).click();
		elementos.get(2).click();
		//Y los borramos
		elementos = driver.findElements(By.name("buttonEliminar"));
		elementos.get(0).click();
		//Ahora vemos que hay tres usuarios en el listado
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 3);
	}

	// AÑADIR OFERTA

	// PR15. Ir al formulario de alta de oferta, rellenarla con datos válidos y
	// pulsar el botón Submit. Comprobar que la oferta sale en el listado de ofertas
	// de dicho usuario.
	@Test
	public void PR15() {
		//Nos registramos con el usuario que vamos a utilizar para el test
		PO_RegisterView.registrarUsuarioTest(driver, 15);
		//Vamos a vender
		PO_NavView.accessVender(driver);
		//Añadimos una oferta
		PO_AddItemView.fillForm(driver, "PR15Oferta", "PR15Descripcion", "15", false);
		//Ahora accedemos al listado de ofertas propias y vemos que se ha registrado en el listado
		PO_NavView.verListaPropia(driver);
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 1);
	}

	// PR16. Ir al formulario de alta de oferta, rellenarla con datos inválidos
	// (campo título vacío y precio en negativo) y pulsar el botón Submit. Comprobar que se muestra el
	// mensaje de campo obligatorio.
	@Test
	public void PR16() {
		//Nos registramos con el usuario que vamos a utilizar para el test
		PO_RegisterView.registrarUsuarioTest(driver, 16);
		//Vamos a vender
		PO_NavView.accessVender(driver);
		//Añadimos una oferta que no tenga título y con precio negativo
		PO_AddItemView.fillForm(driver, "", "PR16Descripcion", "-5.0", false);
		//Vemos que nos aparecen los dos errores
		PO_View.checkElement(driver, "text", "Se tiene que añadir un título");
		PO_View.checkElement(driver, "text", "El precio no puede ser negativo o cero");
	}

	// LISTADO OFERTAS PROPIAS

	// PR17. Mostrar el listado de ofertas para dicho usuario y comprobar que se
	// muestran todas los que existen para este usuario.
	@Test
	public void PR17() {
		//Nos registramos con el usuario que vamos a utilizar para el test
		PO_RegisterView.registrarUsuarioTest(driver, 17);
		//Ahora añadimos tres ofertas con ese usuario
		PO_AddItemView.añadirItems(driver, 17, 3);
		//Vamos a la lista de ofertas propias
		PO_NavView.verListaPropia(driver);
		//Vemos que el usuario tiene las tres ofertas que ha creado
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 3);
	}

	// ELIMINAR OFERTA

	// PR18. Ir a la lista de ofertas, borrar la primera oferta de la lista,
	// comprobar que la lista se actualiza y que la oferta desaparece.
	@Test
	public void PR18() {	
		//Nos registramos con el usuario que vamos a utilizar para el test
		PO_RegisterView.registrarUsuarioTest(driver, 18);
		//Ahora añadimos tres ofertas con ese usuario
		PO_AddItemView.añadirItems(driver, 18, 3);
		//Vamos a la lista de ofertas propias
		PO_NavView.verListaPropia(driver);
		//Vemos que el usuario tiene las tres ofertas que ha creado
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 3);
		//Ahora le damos a el enlace eliminar la oferta
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/oferta/eliminar')]");
		elementos.get(0).click();
		//Ahora habrá dos elementos
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 2);
	}

	// PR19. Ir a la lista de ofertas, borrar la última oferta de la lista,
	// comprobar que la lista se actualiza y que la oferta desaparece.
	@Test
	public void PR19() {
		//Nos registramos con el usuario que vamos a utilizar para el test
		PO_RegisterView.registrarUsuarioTest(driver, 19);
		//Ahora añadimos tres ofertas con ese usuario
		PO_AddItemView.añadirItems(driver, 19, 3);
		//Vamos a la lista de ofertas propias
		PO_NavView.verListaPropia(driver);
		//Vemos que el usuario tiene las tres ofertas que ha creado
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 3);
		//Ahora le damos a el enlace eliminar la oferta
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/oferta/eliminar')]");
		elementos.get(elementos.size()-1).click();
		//Ahora habrá dos elementos
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 2);
	}

	// BUSCAR OFERTAS

	// PR20. Hacer una búsqueda con el campo vacío y comprobar que se muestra la
	// página que corresponde con el listado de las ofertas existentes en el sistema
	@Test
	public void PR20() {
		//Creamos el usuario 18 y vendemos con él 2 ofertas
		PO_RegisterView.registrarUsuarioTest(driver, 18);
		PO_AddItemView.añadirItems(driver, 18, 2);
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		//Creamos el usuario 19 y vendemos con él otras 2 ofertas
		PO_RegisterView.registrarUsuarioTest(driver, 19);
		PO_AddItemView.añadirItems(driver, 19, 2);
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		//Nos registramos como el usuario que buscará las ofertas
		PO_RegisterView.registrarUsuarioTest(driver, 20);
		//Accedemos a la lista de todos los usuarios
		PO_NavView.verListaTodos(driver);
		//Comprobamos que hay cuatro ofertas disponibles
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
		assertTrue(elementos.size() == 4);
		//Comprobamos que si no introducimos nada en la barra de búsqueda y le damos a buscar...
		PO_SearchBox.fillForm(driver, "");
		//... sigue habiendo cuatro elementos justo
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
		assertTrue(elementos.size() == 4);
	}

	// PR21. Hacer una búsqueda escribiendo en el campo un texto que no exista y
	// comprobar que se muestra la página que corresponde, con la lista de ofertas
	// vacía.
	@Test
	public void PR21() {
		//Creamos el usuario 18 y vendemos con él 2 ofertas
		PO_RegisterView.registrarUsuarioTest(driver, 18);
		PO_AddItemView.añadirItems(driver, 18, 2);
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		//Creamos el usuario 19 y vendemos con él otras 2 ofertas
		PO_RegisterView.registrarUsuarioTest(driver, 19);
		PO_AddItemView.añadirItems(driver, 19, 2);
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		//Nos registramos como el usuario que buscará las ofertas
		PO_RegisterView.registrarUsuarioTest(driver, 21);
		//Accedemos a la lista de todos los usuarios
		PO_NavView.verListaTodos(driver);
		//Comprobamos que hay cuatro ofertas disponibles
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
		assertTrue(elementos.size() == 4);
		//Comprobamos que si introducimos un título cuyo nombre no se corresponde a ninguno, en la barra de búsqueda y le damos a buscar...
		PO_SearchBox.fillForm(driver, "no existo");
		//... ya no hay ningún elemento
		SeleniumUtils.textoNoPresentePagina(driver, "Comprar");
	}

	// PR22. Hacer una búsqueda escribiendo en el campo un texto en minúscula o
	// mayúscula y
	// comprobar que se muestra la página que corresponde, con la lista de ofertas
	// que contengan
	// dicho texto, independientemente que el título esté almacenado en minúsculas o
	// mayúscula.
	@Test
	public void PR22() {
		//Creamos el usuario 18 y vendemos con él 3 ofertas
		PO_RegisterView.registrarUsuarioTest(driver, 18);
		PO_AddItemView.añadirItems(driver, 18, 3);
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		//Creamos el usuario 19 y vendemos con él la oferta que buscaremos
		PO_RegisterView.registrarUsuarioTest(driver, 19);
		PO_NavView.accessVender(driver);
		PO_AddItemView.fillForm(driver, "muñeco", "antiguo", "20", false);
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		//Nos registramos como el usuario que buscará las ofertas
		PO_RegisterView.registrarUsuarioTest(driver, 22);
		//Accedemos a la lista de todas los ofertas
		PO_NavView.verListaTodos(driver);
		//Comprobamos que hay cuatro ofertas disponibles
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
		assertTrue(elementos.size() == 4);
		//Comprobamos que si no introducimos nada en la barra de búsqueda y le damos a buscar...
		PO_SearchBox.fillForm(driver, "mu");
		//... ahora solo hay un elemento
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
		assertTrue(elementos.size() == 1);
	}

	// COMPRAR OFERTAS

	// PR23. Sobre una búsqueda determinada (a elección del desarrollador), comprar
	// una oferta que deja un saldo positivo en el contador del comprador. Comprobar
	// que el contador se actualiza correctamente en la vista del comprador.
	@Test
	public void PR23() {	
		//Creamos el usuario 18 y vendemos con él 3 ofertas
		PO_RegisterView.registrarUsuarioTest(driver, 18);
		PO_AddItemView.añadirItems(driver, 18, 3);
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		//Creamos el usuario 19 y vendemos desde su perfil el objeto a comprar
		PO_RegisterView.registrarUsuarioTest(driver, 19);
		PO_NavView.accessVender(driver);
		PO_AddItemView.fillForm(driver, "objetoCompra", "objeto que se va a comprar", "30", false);
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		//Nos registramos como el usuario que buscará las ofertas
		PO_RegisterView.registrarUsuarioTest(driver, 23);
		//Accedemos a la lista de todas los ofertas
		PO_NavView.verListaTodos(driver);
		//Comprobamos que hay cuatro ofertas disponibles
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
		assertTrue(elementos.size() == 4);
		//Buscamos la oferta que compraremos
		PO_SearchBox.fillForm(driver, "objeto");
		//Vemos que solo hay una
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
		assertTrue(elementos.size() == 1);
		//Le damos al botón de comprar
		driver.findElements(By.name("buttonComprar")).get(0).click();
		//Comprobamos que el saldo se actualiza a 100-30 = 70
		PO_NavView.comprobarSaldo(driver, "70 €");
	}

	// PR24. Sobre una búsqueda determinada (a elección del desarrollador), comprar
	// una oferta que deja un saldo 0 en el contador del comprador. Comprobar que el
	// contador se actualiza correctamente en la vista del comprador.
	@Test
	public void PR24() {
		//Creamos el usuario 18 y vendemos con él 3 ofertas
		PO_RegisterView.registrarUsuarioTest(driver, 18);
		PO_AddItemView.añadirItems(driver, 18, 3);
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		//Creamos el usuario 19 y vendemos desde su perfil el objeto a comprar
		PO_RegisterView.registrarUsuarioTest(driver, 19);
		PO_NavView.accessVender(driver);
		PO_AddItemView.fillForm(driver, "objetoCaro", "objeto que se va a comprar", "100", false);
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		//Nos registramos como el usuario que buscará las ofertas
		PO_RegisterView.registrarUsuarioTest(driver, 24);
		//Accedemos a la lista de todas los ofertas
		PO_NavView.verListaTodos(driver);
		//Comprobamos que hay cuatro ofertas disponibles
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
		assertTrue(elementos.size() == 4);
		//Buscamos la oferta que compraremos
		PO_SearchBox.fillForm(driver, "objeto");
		//Vemos que solo hay una
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
		assertTrue(elementos.size() == 1);
		//Le damos al botón de comprar
		driver.findElements(By.name("buttonComprar")).get(0).click();
		//Comprobamos que el saldo se actualiza a 100-100 = 0
		PO_NavView.comprobarSaldo(driver, "0 €");
	}

	// PR25. Sobre una búsqueda determinada (a elección del desarrollador), intentar
	// comprar una oferta que esté por encima de saldo disponible del comprador. Y
	// comprobar que se muestra el mensaje de saldo no suficiente.
	@Test
	public void PR25() {
		//Creamos el usuario 18 y vendemos con él 3 ofertas
		PO_RegisterView.registrarUsuarioTest(driver, 18);
		PO_AddItemView.añadirItems(driver, 18, 3);
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		//Creamos el usuario 19 y vendemos desde su perfil el objeto a comprar
		PO_RegisterView.registrarUsuarioTest(driver, 19);
		PO_NavView.accessVender(driver);
		PO_AddItemView.fillForm(driver, "objetoIncomprable", "objeto que no se puede comprar", "200", false);
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		//Nos registramos como el usuario que buscará las ofertas
		PO_RegisterView.registrarUsuarioTest(driver, 25);
		//Accedemos a la lista de todas los ofertas
		PO_NavView.verListaTodos(driver);
		//Comprobamos que hay cuatro ofertas disponibles
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
		assertTrue(elementos.size() == 4);
		//Buscamos la oferta que compraremos
		PO_SearchBox.fillForm(driver, "objeto");
		//Vemos que solo hay una
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
		assertTrue(elementos.size() == 1);
		//Le damos al botón de comprar
		try {
		driver.findElements(By.name("buttonComprar")).get(0).click();
		} catch (ElementClickInterceptedException ex) {
			//El botón está deshabilitado
		}
		//Comprobamos que el saldo no se actualiza ya que el botón de comprar está deshabilitado para aquellas ofertas que valgan más de lo que el usuario tiene de saldo
		PO_NavView.comprobarSaldo(driver, "100 €");
	}


	// VER LISTADO COMPRAS PROPIAS

	// PR26. Ir a la opción de ofertas compradas del usuario y mostrar la lista.
	// Comprobar que aparecen las ofertas que deben aparecer.
	@Test
	public void PR26() {
		//Creamos el usuario 10 y vendemos con él 3 ofertas
		PO_RegisterView.registrarUsuarioTest(driver, 10);
		PO_AddItemView.añadirItems(driver, 10, 3);
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		
		//Nos registramos como el usuario que comprará
		PO_RegisterView.registrarUsuarioTest(driver, 26);
		//Accedemos a la lista de todas los ofertas
		PO_NavView.verListaTodos(driver);
		//Comprobamos que hay cuatro ofertas disponibles
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
		assertTrue(elementos.size() == 3);
		//Compramos una de las ofertas
		driver.findElements(By.name("buttonComprar")).get(0).click();
		PO_NavView.verListaComprados(driver);
		//Ahora en la lista de comprados vemos que nos aparece correctamente la oferta que hemos comprado
		List<WebElement> elementosComprados = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementosComprados.size() == 1);
		PO_NavView.verListaTodos(driver);
		//Si volvemos a comprar otra oferta
		driver.findElements(By.name("buttonComprar")).get(0).click();
		PO_NavView.verListaComprados(driver);
		//Cuando volvamos a las compras del usuario veremos que ahora hay otra más
		elementosComprados = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementosComprados.size() == 2);
	}
		
	// DESTACADO

	// PR27. Al crear una oferta marcar dicha oferta como destacada y a continuación comprobar: i)
	// que aparece en el listado de ofertas destacadas para los usuarios y que el saldo del usuario se
	// actualiza adecuadamente en la vista del ofertante (-20).
	@Test
	public void PR27() {
		//Entramos como el usuario que destacará la oferta
		PO_RegisterView.registrarUsuarioTest(driver, 27);
		PO_NavView.accessVender(driver);
		//Al ponerle true le estamos diciendo que la oferta se destacará
		PO_AddItemView.fillForm(driver, "PR27Oferta", "PR27Descripcion", "15", true);
		//Comprobamos que efectivamente el saldo se ha reducido en 20
		PO_NavView.comprobarSaldo(driver, "80 €");
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		//Entramos con otro usuario distinto para comprobar que se ve la nueva oferta destacada
		PO_RegisterView.registrarUsuarioTest(driver, 28);
		//Accedemos a la lista de todas las ofertas
		PO_NavView.verListaTodos(driver);
		//Comprobamos que hay una oferta destacada
		List<WebElement> elementos = driver.findElements(By.name("buttonComprarDestacada"));
		assertEquals(elementos.size(), 1);
	}
	
	// PR28. Sobre el listado de ofertas de un usuario con más de 20 euros de saldo, pinchar en el
	// enlace Destacada y a continuación comprobar: i) que aparece en el listado de ofertas destacadas
	// para los usuarios y que el saldo del usuario se actualiza adecuadamente en la vista del ofertante (-
	// 20).
	@Test
	public void PR28() {
		//Nos registramos con el usuario que vamos a utilizar para el test
		PO_RegisterView.registrarUsuarioTest(driver, 28);
		//Ahora añadimos tres ofertas normales con ese usuario
		PO_AddItemView.añadirItems(driver, 28, 3);
		//Vamos a la lista de ofertas propias
		PO_NavView.verListaPropia(driver);
		//Vemos que el usuario tiene las tres ofertas que ha creado
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 3);
		//Ahora buscamos los botones para destacar
		elementos = driver.findElements(By.name("buttonDestacar"));
		//Y destacamos la primera
		elementos.get(0).click();
		//Vemos que el saldo se nos reduce en 20
		PO_NavView.comprobarSaldo(driver, "80 €");
		//Nos salimos de este usuario
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		//Entramos con otro usuario distinto para comprobar que se ve la nueva oferta destacada
		PO_RegisterView.registrarUsuarioTest(driver, 29);
		//Accedemos a la lista de todas las ofertas
		PO_NavView.verListaTodos(driver);
		//Comprobamos que hay una oferta destacada
		elementos = driver.findElements(By.name("buttonComprarDestacada"));
		assertEquals(elementos.size(), 1);
	}

	// PR29. Sobre el listado de ofertas de un usuario con menos de 20 euros de
	// saldo, pinchar en el
	// enlace Destacada y a continuación comprobar que se muestra el mensaje de
	// saldo no suficiente.
	@Test
	public void PR29() {
		//Creamos el usuario 28 y vendemos desde su perfil el objeto a comprar que deje el saldo en un número menor que 20
		PO_RegisterView.registrarUsuarioTest(driver, 19);
		PO_NavView.accessVender(driver);
		PO_AddItemView.fillForm(driver, "objetoCaro", "objeto que se va a comprar", "90", false);
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		//Nos registramos como el usuario que buscará las ofertas
		PO_RegisterView.registrarUsuarioTest(driver, 29);
		//Accedemos a la lista de todas los ofertas
		PO_NavView.verListaTodos(driver);
		//Comprobamos que la oferta se encuentra en el listado
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
		assertTrue(elementos.size() == 1);
		//Buscamos la oferta que compraremos
		PO_SearchBox.fillForm(driver, "objeto");
		//Vemos que solo hay una
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
		assertTrue(elementos.size() == 1);
		//Le damos al botón de comprar
		driver.findElements(By.name("buttonComprar")).get(0).click();
		//Comprobamos que el saldo se actualiza a 100-90 = 10
		PO_NavView.comprobarSaldo(driver, "10 €");
		
		//Ahora vamos al listado propio a ver si podemos destacarlo
		//Añadimos un item que será el que intentemos destacar
		PO_AddItemView.añadirItems(driver, 29, 1);
		//Vamos a la lista de ofertas propias
		PO_NavView.verListaPropia(driver);
		//Vemos que el usuario tiene la oferta que ha creado
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 1);
		//Ahora buscamos los botones para destacar
		elementos = driver.findElements(By.name("buttonDestacar"));
		//Y destacamos la primera
		try {
			elementos.get(0).click();
		} catch (ElementClickInterceptedException ex) {
			//El botón está deshabilitado
		}
		//Vemos que el saldo no se ha reducido ya que no se ha destacado la oferta
		PO_NavView.comprobarSaldo(driver, "10 €");
	}
	
	
	// PREX01. Tras destacar una oferta, ver que podemos dejar de destacarla, lo que devolverá 20 euros y hará que no se siga viendo en el listado de destacadas
	@Test
	public void PREX01() {
		//Entramos como el usuario que destacará la oferta
		PO_RegisterView.registrarUsuarioTest(driver, 41);
		PO_NavView.accessVender(driver);
		//Al ponerle true le estamos diciendo que la oferta se destacará
		PO_AddItemView.fillForm(driver, "PR41Oferta", "PR41Descripcion", "41", true);
		//Comprobamos que efectivamente el saldo se ha reducido en 20
		PO_NavView.comprobarSaldo(driver, "80 €");
		//Vamos a ver la lista de ofertas propias
		PO_NavView.verListaPropia(driver);
		//Vemos que el usuario tiene la oferta que acaba de crear
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 1);
		//Ahora buscamos los botones para destacar
		elementos = driver.findElements(By.name("buttonDestacar"));
		//Y destacamos la primera
		elementos.get(0).click();
		//Vemos que el saldo nos vuelve a aumentar en 20 euros
		PO_NavView.comprobarSaldo(driver, "100 €");
		
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		//Entramos con otro usuario distinto para comprobar que se ve la nueva oferta destacada
		PO_RegisterView.registrarUsuarioTest(driver, 42);
		//Accedemos a la lista de todas las ofertas
		PO_NavView.verListaTodos(driver);
		//Comprobamos que no hay ofertas destacadas
		SeleniumUtils.textoNoPresentePagina(driver, "Comprar destacada");
	}
}
