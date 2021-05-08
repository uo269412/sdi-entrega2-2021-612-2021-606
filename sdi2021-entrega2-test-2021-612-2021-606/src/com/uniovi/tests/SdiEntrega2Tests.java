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
		// Fijamos el timeout en cada opciÃ³n de carga de una vista. 2 segundos.
		PO_View.setTimeout(3);

	}

	@AfterClass
	static public void end() {
		// Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}

	
	// SIGN UP

	// PR01. Registro de Usuario con datos válidos.
	@Test
	public void PR01() {
		PO_HomeView.clickOption(driver, "/registrarse", "class", "w-100 btn btn-lg btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "PR01@PR01.com", "NombrePR01", "ApellidoPR01", "123456", "123456");
		// Comprobamos que entramos en la sección privada
		PO_View.checkElement(driver, "text", "Bienvenido/a a MyWallapop");
	}

	// PR02. Registro de Usuario con datos inválidos (email vacío, nombre vacío,
	// apellidos vacíos).
	@Test
	public void PR02() {
		PO_HomeView.clickOption(driver, "/registrarse", "class", "w-100 btn btn-lg btn-primary");
		// Comprobamos el error de nombre vacío
		PO_RegisterView.fillForm(driver,  "PR02@PR02.com", "", "ApellidoPR02", "123456", "123456");
		PO_View.checkElement(driver, "text", "Se tiene que añadir un nombre");
		// Comprobamos el error de email vacío
	 	PO_RegisterView.fillForm(driver,  "", "NombrePR02", "ApellidoPR02", "123456", "123456");
		PO_View.checkElement(driver, "text", "Se tiene que añadir un email");
		//Comprobamos el error de apellidos vacíos
		PO_RegisterView.fillForm(driver,  "PR02@PR02.com", "NombrePR02", "", "123456", "123456");
		PO_View.checkElement(driver, "text", "Se tiene que insertar un apellido");
		//Comprobamos los tres errores a la vez
		PO_RegisterView.fillForm(driver,  "", "", "", "123456", "123456");
		PO_View.checkElement(driver, "text", "Se tiene que añadir un nombre");
		PO_View.checkElement(driver, "text", "Se tiene que añadir un email");
		PO_View.checkElement(driver, "text", "Se tiene que insertar un apellido");
	}

	// PR03. Registro de Usuario con datos inválidos (repetición de contraseña
	// inválida).
	@Test
	public void PR03() {
		PO_HomeView.clickOption(driver, "/registrarse", "class", "w-100 btn btn-lg btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "PR03@PR03.com", "NombrePR03", "ApellidoPR03", "123456", "654321");
		//Se nos muestra el error de que las contraseñas no coinciden
		PO_View.checkElement(driver, "text", "Las contraseñas no coinciden");
	}

	// PR04. Registro de Usuario con datos inválidos (email existente).
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


	// PR05. Inicio de sesión con datos válidos.
	@Test
	public void PR05() {
		//Clickamos en la opción de loggearse
		PO_HomeView.clickOption(driver, "/identificarse", "class", "w-100 btn btn-lg btn-primary");
		// Rellenamos el formulario con los datos del usuario que acabamos de crear
		PO_LoginView.fillForm(driver, "laura_mar@gmail.com", "123456");
		// Comprobamos que entramos en la pagina privada
		PO_View.checkElement(driver, "text", "Bienvenido/a a MyWallapop");
	}
	

	// PR06. Inicio de sesión con datos inválidos (usuario estándar, email existente,
	// pero contraseña incorrecta).
	@Test
	public void PR06() {
		PO_HomeView.clickOption(driver, "/identificarse", "class", "w-100 btn btn-lg btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "admin@email.com", "123");
		// Comprobamos que la contraseña para el administrador es incorrecta
		PO_View.checkElement(driver, "text", "El usuario no existe en la base de datos o la contraseña es incorrecta");
	}

	// PR07. Inicio de sesión con datos inválidos (usuario estándar, campo email y
	// contraseña vacíos).
	@Test
	public void PR07() {
		PO_HomeView.clickOption(driver, "/identificarse", "class", "w-100 btn btn-lg btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "", "123");
		// Comprobamos que nos pide un email
		PO_View.checkElement(driver, "text", "Se tiene que añadir un email");
		
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "admin@email.com", "");
		// Comprobamos que nos pide una contraseña
		PO_View.checkElement(driver, "text", "Se tiene que añadir una contraseña");	

		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "", "");
		// Comprobamos que si dejamos los dos campos vacíos, se nos muestran los dos errores
		PO_View.checkElement(driver, "text", "Se tiene que añadir un email");
		PO_View.checkElement(driver, "text", "Se tiene que añadir una contraseña");
		
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
		//Accedemos a identificarnos
		PO_HomeView.clickOption(driver, "/identificarse", "class", "w-100 btn btn-lg btn-primary");
		// Rellenamos el formulario con el usuario que acabamos de crear
		PO_LoginView.fillForm(driver, "laura_mar@gmail.com", "123456");
		// Comprobamos que entramos en la pagina privada
		PO_View.checkElement(driver, "text", "Bienvenido/a a MyWallapop");
		//Nos desconectamos
		PO_NavView.disconnect(driver);
		//Ahora estamos en la página de login
		PO_View.checkElement(driver, "text", "Accede a la aplicación con un usuario ya identificado");

	}

	// PR10. Comprobar que el botón cerrar sesión no está visible si el usuario no
	// está autenticado.
	@Test
	public void PR10() {
		//Nos intentamos identificar como un usuario que no existe
		PO_HomeView.clickOption(driver, "/identificarse", "class", "w-100 btn btn-lg btn-primary");
		PO_LoginView.fillForm(driver, "usuarioNoExiste@usuarioNoExiste.com", "123");
		PO_View.checkElement(driver, "text", "El usuario no existe en la base de datos o la contraseña es incorrecta");
		//El enlace para desconectarse no estará presente ya que se encuentra en la página privada a la cual solo podemos acceder una vez nos hayamos identificado
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
		//Así comprobamos que ahora hay seis usuarios
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 6);
	}

	// PR13. Ir a la lista de usuarios, borrar el último usuario de la lista,
	// comprobar que la lista se actualiza y que el usuario desaparece.
	@Test
	public void PR13() {
		//Accedemos como administrador
		PO_LoginView.accederAdmin(driver);
		List<WebElement> elementos = driver.findElements(By.name("deleteUser"));
		//Comprobamos que se encuentran los siete usuarios que hay en la base de datos
		assertTrue(elementos.size() == 7);
		//Clickamos el checkbox del último
		elementos.get(elementos.size()-1).click();
		//Y ahora le damos a borrar
		elementos = driver.findElements(By.name("buttonEliminar"));
		elementos.get(0).click();
		//Así comprobamos que ahora hay seis usuarios en el listado
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

	// AÑADIR OFERTA

	// PR15. Ir al formulario de alta de oferta, rellenarla con datos válidos y
	// pulsar el botón Submit. Comprobar que la oferta sale en el listado de ofertas
	// de dicho usuario.
	@Test
	public void PR15() {
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Vamos a vender
		PO_NavView.accessVender(driver);
		//Añadimos una oferta
		PO_AddItemView.fillForm(driver, "PR15Oferta", "PR15Descripcion", "15", false);
		//Ahora accedemos al listado de ofertas propias y vemos que se ha registrado en el listado
		PO_NavView.verListaPropia(driver);
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 6);
	}

	// PR16. Ir al formulario de alta de oferta, rellenarla con datos inválidos
	// (campo título vacío y precio en negativo) y pulsar el botón Submit. Comprobar que se muestra el
	// mensaje de campo obligatorio.
	@Test
	public void PR16() {
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
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
		//Ahora habrá cuatro elementos
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 4);
	}

	// PR19. Ir a la lista de ofertas, borrar la última oferta de la lista,
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
		//Ahora habrá cuatro elementos
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 4);
	}
	
	

	// BUSCAR OFERTAS

	// PR20. Hacer una búsqueda con el campo vacío y comprobar que se muestra la
	// página que corresponde con el listado de las ofertas existentes en el sistema
	@Test
	public void PR20() {
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Accedemos a la lista de todos los usuarios
		PO_NavView.verListaTodos(driver);
		//Comprobamos que hay cuatro ofertas disponibles
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
		//Comprobamos que si no introducimos nada en la barra de búsqueda y le damos a buscar...
		PO_SearchBox.fillForm(driver, "");
		//... sigue habiendo cuatro elementos justo
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
	}

	// PR21. Hacer una búsqueda escribiendo en el campo un texto que no exista y
	// comprobar que se muestra la página que corresponde, con la lista de ofertas
	// vacía.
	@Test
	public void PR21() {
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Accedemos a la lista de todos los usuarios
		PO_NavView.verListaTodos(driver);
		//Comprobamos que hay cuatro ofertas disponibles
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "Comprar", PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
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

	// PR23. Sobre una búsqueda determinada (a elección del desarrollador), comprar
	// una oferta que deja un saldo positivo en el contador del comprador. Comprobar
	// que el contador se actualiza correctamente en la vista del comprador.
	@Test
	public void PR23() {	
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Accedemos a la lista de todas los ofertas
		PO_NavView.verListaTodos(driver);
		//Buscamos la oferta que compraremos
		PO_SearchBox.fillForm(driver, "Canicas");
		//Le damos al botón de comprar, que nos dejará comprar la única búsqueda resultante, canicas
		driver.findElements(By.name("buttonComprar")).get(0).click();
		//Comprobamos que el saldo se actualiza a 100-2 = 98
		PO_NavView.comprobarSaldo(driver, "98 €");
	}

	// PR24. Sobre una búsqueda determinada (a elección del desarrollador), comprar
	// una oferta que deja un saldo 0 en el contador del comprador. Comprobar que el
	// contador se actualiza correctamente en la vista del comprador.
	@Test
	public void PR24() {
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Accedemos a la lista de todas los ofertas
		PO_NavView.verListaTodos(driver);
		//Buscamos la oferta que compraremos
		PO_SearchBox.fillForm(driver, "Bicicleta");
		//Le damos al botón de comprar, que nos dejará comprar la única búsqueda resultante, canicas
		driver.findElements(By.name("buttonComprar")).get(0).click();
		//Comprobamos que el saldo se actualiza a 100-100 = 0
		PO_NavView.comprobarSaldo(driver, "0 €");
	}

	// PR25. Sobre una búsqueda determinada (a elección del desarrollador), intentar
	// comprar una oferta que esté por encima de saldo disponible del comprador. Y
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
		PO_LoginView.accederUsuario(driver, "jueguetesParaTodos@gmail.com");
		//Vemos la lista de ofertas compradas
		PO_NavView.verListaComprados(driver);
		//Comprobamos que tiene una oferta comprada
		List<WebElement> elementosComprados = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementosComprados.size() == 3);
	}
		
	// DESTACADO

	// PR27. Al crear una oferta marcar dicha oferta como destacada y a continuación comprobar: i)
	// que aparece en el listado de ofertas destacadas para los usuarios y que el saldo del usuario se
	// actualiza adecuadamente en la vista del ofertante (-20).
	@Test
	public void PR27() {
		PO_LoginView.accederUsuario(driver, "jueguetesParaTodos@gmail.com");
		PO_NavView.accessVender(driver);
		//Al ponerle true le estamos diciendo que la oferta se destacará
		PO_AddItemView.fillForm(driver, "PR27Oferta", "PR27Descripcion", "15", true);
		//Comprobamos que efectivamente el saldo se ha reducido en 20
		PO_NavView.comprobarSaldo(driver, "80 €");
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Accedemos a la lista de todas las ofertas
		PO_NavView.verListaTodos(driver);
		//Comprobamos que hay una oferta destacada
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", "PR27Oferta", PO_View.getTimeout());
		assertEquals(elementos.size(), 1);
	}
		

	
	// PR28. Sobre el listado de ofertas de un usuario con más de 20 euros de saldo, pinchar en el
	// enlace Destacada y a continuación comprobar: i) que aparece en el listado de ofertas destacadas
	// para los usuarios y que el saldo del usuario se actualiza adecuadamente en la vista del ofertante (-
	// 20).
	@Test
	public void PR28() {
		//Entramos con el usuario que luego comprobará el número de destacadas
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Accedemos a la lista de todas las ofertas
		PO_NavView.verListaTodos(driver);
		//Comprobamos primero el número de ofertas destacadas, que son seis
		List<WebElement> elementos = driver.findElements(By.name("buttonComprarDestacada"));
		assertEquals(elementos.size(), 3);
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		//Entramos con el usuario que destacará la oferta
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
		PO_NavView.comprobarSaldo(driver, "80 €");
		//Nos salimos de este usuario
		PO_NavView.disconnect(driver);
		driver.navigate().to(URL);
		
		//Entramos con otro usuario distinto para comprobar que se ve la nueva oferta destacada
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Accedemos a la lista de todas las ofertas
		PO_NavView.verListaTodos(driver);
		//Comprobamos primero el número de ofertas destacadas, que son seis
		elementos = driver.findElements(By.name("buttonComprarDestacada"));
		assertEquals(elementos.size(), 4);
	}


	// PR29. Sobre el listado de ofertas de un usuario con menos de 20 euros de
	// saldo, pinchar en el
	// enlace Destacada y a continuación comprobar que se muestra el mensaje de
	// saldo no suficiente.
	@Test
	public void PR29() {
		//Entramos como Laura y la bajamos el dinero a menos de 20 euros
		PO_LoginView.accederUsuario(driver, "laura_mar@gmail.com");
		//Accedemos a la lista de todas los ofertas
		PO_NavView.verListaTodos(driver);
		//Buscamos la oferta que compraremos
		PO_SearchBox.fillForm(driver, "Raqueta de frontón");
		//Le damos al botón de comprar, que nos dejará comprar la única búsqueda resultante, canicas
		driver.findElements(By.name("buttonComprar")).get(0).click();
		PO_NavView.verListaPropia(driver);
		List<WebElement> elementos = driver.findElements(By.name("buttonDestacar"));
		//Y destacamos la primera
		try {
			elementos.get(0).click();
		} catch (ElementClickInterceptedException ex) {
			//El botón está deshabilitado
		}
		//Vemos que el saldo no se ha reducido ya que no se ha destacado la oferta
		PO_NavView.comprobarSaldo(driver, "10 €");
	}
	
	
	
	// PREX01. Tras destacar una oferta, ver que podemos dejar de destacarla lo que nos devolverá 20 euros
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
		PO_NavView.comprobarSaldo(driver, "120 €");
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
//	// Eliminar de la última y comprobar que el listado se actualiza correctamente.
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
