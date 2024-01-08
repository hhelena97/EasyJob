package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.services.OrtService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

@SpringBootTest
class OrtControllerTest {
    // Services
    private final static OrtService ortService = Mockito.mock(OrtService.class);

    // Controllers
    @InjectMocks
    private OrtController ortController;

    // Entites
    private static Ort ort1;
    private static Ort ort2;
    private static Ort ort3;

    /**
     * setzt die Werte für die Ort-Objekte
     */
    @BeforeAll
    static void setUp() {
        ort1 = new Ort();
        ort1.setOrt("Bonn");
        ort1.setPLZ("53111");

        ort2 = new Ort();
        ort2.setOrt("Köln");
        ort2.setPLZ("50667");

        ort3 = new Ort();
        ort3.setOrt("Bonn");
        ort3.setPLZ("53119");

        ortService.saveOrt(ort1);
        ortService.saveOrt(ort2);
        ortService.saveOrt(ort3);
    }

    /**
     * erstellt den zu testenden Controller
     */
    @BeforeEach
    void setUpEach() {
        ortController = new OrtController(ortService);
    }

    /**
     * nullt den zu testenden Controller, damit pro Test eine neue Controller-Instanz aufgerufen werden kann
     */
    @AfterEach
    void tearDown() {
        ortController = null;
    }

    /**
     * testet die Methode getOrtItemFilter()
     */
    @Test
    @DisplayName("Test für getOrtItemFilter()")
    void getOrtItemFilterAndLabelFilterGeneratorTest() {
        /* ************* Arrange ************* */
        // WebDriver Setup
        System.setProperty("webdriver.gecko.driver", "/C:/Users/Vanessa/Downloads/geckodriver-v0.33.0-win64/geckodriver.exe");
        WebDriverManager.firefoxdriver().setup();
        WebDriver driver = new FirefoxDriver();
        WebDriverWait wait = new WebDriverWait(driver, ofSeconds(30), ofSeconds(1));

        // Warten, bis Login-Seite geladen
        driver.manage().window().maximize();
        driver.get("http://localhost:8080/login");
        wait.until(titleIs("Login | EasyJob"));
        assertEquals("http://localhost:8080/login", driver.getCurrentUrl());

        // Seite neu laden, weil Selenium doof ist
        driver.get("http://localhost:8080/login");

        // E-Mail eingeben
        WebElement emailTextInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#emailloginfeld_id > input")));
        emailTextInput.click();
        emailTextInput.sendKeys("rudolf@christmas.org");

        // Passwort eingeben
        WebElement passwordTextInput = driver.findElement(By.cssSelector("#passwordloginfeld_id > input"));
        passwordTextInput.click();
        passwordTextInput.sendKeys("sicheresPasswort123");

        // Passwort anzeigen
        WebElement passwortLoginInputAuge = driver.findElement(By.cssSelector("#passwordloginfeld_id > vaadin-password-field-button"));
        passwortLoginInputAuge.click();

        // Auf Login-Button drücken
        driver.findElement(By.id("loginbutton_id_loginpage")).click();

        // Auf Unternehmensperson-Profil landen
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("unternehmenProfil")));
        assertEquals("http://localhost:8080/unternehmen/unternehmenperson", driver.getCurrentUrl());

        // Zur Stellenanzeige Erstellen-View wechseln
        driver.findElement(By.xpath("//a[@href='unternehmen/unternehmensprofil']")).click();

        // Auf Unternehmensprofil landen
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("grosseTitleUnternehmen")));
        assertEquals("http://localhost:8080/unternehmen/unternehmensprofil", driver.getCurrentUrl());

        // Auf Stellenangebot hinzufuegen klicken
        driver.findElement(By.xpath("//a[@href='unternehmen/jobdetails']"));
        driver.get("http://localhost:8080/unternehmen/stellenanzeige/erstellen"); // eigentlich müsste man ja über den Link dahinkommen, aber scheinbar ist die Weiterleitung noch nicht richtig

        // Auf Stellenanzeige erstellen landen
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("zurueck-button")));
        assertEquals("http://localhost:8080/unternehmen/stellenanzeige/erstellen", driver.getCurrentUrl());

        /* **************** Act *************** */
        WebElement date = driver.findElement(By.id("input-vaadin-date-picker-16"));
        date.click();
        date.sendKeys("1.12.2024");
        date.sendKeys(Keys.RETURN);

        WebElement berufsbezeichnung = driver.findElement(By.id("input-vaadin-combo-box-18"));
        berufsbezeichnung.click();
        berufsbezeichnung.sendKeys(Keys.UP);
        berufsbezeichnung.sendKeys(Keys.UP);
        berufsbezeichnung.sendKeys(Keys.RETURN);

        WebElement standort = driver.findElement(By.id("input-vaadin-combo-box-20"));
        standort.click();
        standort.sendKeys(Keys.DOWN);
        standort.sendKeys(Keys.RETURN);

        WebElement titel = driver.findElement(By.id("input-vaadin-text-field-21"));
        titel.click();
        titel.sendKeys("Kapitän der Flying Dutchman");

        WebElement stellenbeschreibung = driver.findElement(By.id("textarea-vaadin-text-area-22"));
        stellenbeschreibung.click();
        stellenbeschreibung.sendKeys("Dies ist voll die tolle Stelle, ich hoffe, Sie wollen hier WIRKLICH arbeiten. Die Flying Dutchman braucht nämlich einen neuen Kapitän, weil Will Turner zurück nachhause zu seiner Family will.");

        driver.findElement(By.id("input-vaadin-checkbox-23")).click();

        driver.findElement(By.className("abbrechen-button")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("close-unternehmen")));
        driver.findElement(By.className("close-unternehmen")).click();

        WebDriverWait tempWait = new WebDriverWait(driver, ofSeconds(2)); // define local/temp wait only for the "sleep"
        try {
            tempWait.until(titleIs("Login")); // condition you are certain won't be true
        }
        catch (TimeoutException e) {
             // catch the exception and continue the code
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("fertig-button")));
        driver.findElement(By.className("fertig-button")).click();

        //TODO: Fall -> was passiert, wenn Vorgaben nicht erfüllt?
        //TODO: weiterleitung & speicherung überprüfen

        /* ************* Tear down ************ */
        driver.quit();
    }


    /**
     * testet die Methode getAlleOrte() mit einer ungeordneten Liste
     */
    @Test
    @DisplayName("Test für getAlleOrte()")
    void getAlleOrteTest() {
        // ************* Arrange *************
        List<Ort> expected = new ArrayList<>();
        expected.add(ort1);
        expected.add(ort3);
        expected.add(ort2);

        List<Ort> unsorted = new ArrayList<>();
        unsorted.add(ort1);
        unsorted.add(ort2);
        unsorted.add(ort3);

        Mockito.when(ortService.getAlleOrte()).thenReturn(unsorted);

        // *************** Act ***************
        List<Ort> actual = ortController.getAlleOrte();

        // ************* Assert **************
        assertEquals(expected, actual);
    }

    /**
     * testet die Methode getAlleOrte() mit einer leeren Liste
     */
    @Test
    @DisplayName("Test für getAlleOrte() mit leerer Liste")
    void getAlleOrteEmptyListTest() {
        // ************* Arrange *************
        List<Ort> expected = new ArrayList<>();
        List<Ort> unsorted = new ArrayList<>();

        Mockito.when(ortService.getAlleOrte()).thenReturn(unsorted);

        // *************** Act ***************
        List<Ort> actual = ortController.getAlleOrte();

        // ************* Assert **************
        assertEquals(expected, actual);
    }

    /**
     * testet die Methode getAlleOrte() mit einer geordneten Liste
     */
    @Test
    @DisplayName("Test für getAlleOrte() mit geordneter Liste")
    void getAlleOrteSortedListTest() {
        // ************* Arrange *************
        List<Ort> expected = new ArrayList<>();
        expected.add(ort1);
        expected.add(ort3);
        expected.add(ort2);

        List<Ort> sorted = new ArrayList<>();
        sorted.add(ort1);
        sorted.add(ort3);
        sorted.add(ort2);

        Mockito.when(ortService.getAlleOrte()).thenReturn(sorted);

        // *************** Act ***************
        List<Ort> actual = ortController.getAlleOrte();

        // ************* Assert **************
        assertEquals(expected, actual);
    }

    /**
     * testet die Methode createOrt() mit einem Ort-Objekt
     */
    @Test
    @DisplayName("Testet die Methode createOrt")
    void createOrtTest() {
        // ************* Arrange *************
        ResponseEntity<Ort> expected = new ResponseEntity<>(ort1, HttpStatus.CREATED);

        Mockito.when(ortService.saveOrt(ort1)).thenReturn(ort1);

        // *************** Act ***************
        ResponseEntity<Ort> actual = ortController.createOrt(ort1);

        // ************* Assert **************
        assertEquals(expected, actual);
    }

    /**
     * testet die Methode createOrt() mit null
     */
    @Test
    @DisplayName("Testet die Methode createOrt")
    void createNullOrtTest() {
        // ************* Arrange *************
        ResponseEntity<Ort> expected = new ResponseEntity<>(null, HttpStatus.CREATED);

        Mockito.when(ortService.saveOrt(ort1)).thenReturn(null);

        // *************** Act ***************
        ResponseEntity<Ort> actual = ortController.createOrt(ort1);

        // ************* Assert **************
        assertEquals(expected, actual);
    }
}