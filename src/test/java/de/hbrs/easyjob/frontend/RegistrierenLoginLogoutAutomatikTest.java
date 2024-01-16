package de.hbrs.easyjob.frontend;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;
import java.time.Duration;

import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class RegistrierenLoginLogoutAutomatikTest {
    // WebDriver
    private final WebDriver[] drivers = new WebDriver[3];
    private final WebDriverManager wdmFirefox = WebDriverManager.firefoxdriver().browserInDocker().enableVnc();
    private final WebDriverManager wdmSafari = WebDriverManager.safaridriver().browserInDocker().enableVnc();

    @Value("${selenium.password}")
    private String signUpPassword;

    @BeforeAll
    static void setUp() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUpEach() throws Exception {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        drivers[1] = wdmFirefox.create();
        drivers[0] = new ChromeDriver(options);
        drivers[2] = wdmSafari.create();
        Thread.sleep(Duration.ofSeconds(10).toMillis());
    }

    @AfterEach
    void tearDownEach() {
        wdmFirefox.quit();
        wdmSafari.quit();
        drivers[0].quit();
    }

    @Test
    @DisplayName("Geht einmal den Registrierungsprozess bis Login & Logout durch")
    @Transactional
    void SeleniumTest() {
        int i = 0;
        for(WebDriver driver : drivers) {
            String email = "jack" + i++ + ".sparrow@black.pearl"; // damit man die Datenbank nicht verwirrt
            driver.manage().window().maximize();
            automatisierteRegistrierungUndLogin(driver,
                    new WebDriverWait(driver, ofSeconds(30), ofSeconds(1)),
                    email,
                    signUpPassword,
                    i != 0
            );
            //personRepository.delete(personRepository.findByEmail(email)); <- klappt noch nicht
        }
    }

    void automatisierteRegistrierungUndLogin(WebDriver driver, WebDriverWait wait, String email, String password, boolean docker) {
        // Den Driver zwingen, 5 Sekunden zu warten
        WebDriverWait tempWait = new WebDriverWait(driver, ofSeconds(5));
        try {
            tempWait.until(titleIs("Login"));
        }
        catch (TimeoutException e) {
            // nichts soll hier passieren
        }

        String local;
        if (docker) {
            local = "http://host.docker.internal";
        } else {
            local = "http://localhost";
        }

        // Login-Seite laden
        driver.get(local + ":8080/login");
        wait.until(titleIs("Login | EasyJob"));

        // ********************************************************************************* Allgemein Registrieren View
        // Registrieren-Seite laden
        driver.findElement(By.id("registrierenbutton_id")).click();
        wait.until(titleIs("Registrieren"));
        assertEquals(local + ":8080/registrieren", driver.getCurrentUrl());

        // "Ich studiere." auswählen
        driver.findElement(By.id("label-vaadin-radio-button-11")).click();

        // E-Mail eingeben
        WebElement emailRegistrierenInput = driver.findElement(By.cssSelector("#email_registrieren_id > input"));
        emailRegistrierenInput.click();
        emailRegistrierenInput.sendKeys(email);

        // Passwort eingeben
        WebElement passwortRegistrierenInput = driver.findElement(
                By.cssSelector("#passwort1_registrieren_id > input")
        );
        passwortRegistrierenInput.click();
        passwortRegistrierenInput.sendKeys(password);

        // Passwort anzeigen und wieder ausblenden
        WebElement passwortRegistrierenInputAuge = driver.findElement(
                By.cssSelector("#passwort1_registrieren_id > vaadin-password-field-button")
        );
        passwortRegistrierenInputAuge.click();
        passwortRegistrierenInputAuge.click();

        // Passwort wiederholen
        WebElement passwortRegistrierenWdhInput = driver.findElement(
                By.cssSelector("#passwort2_registrieren_id > input")
        );
        passwortRegistrierenWdhInput.click();
        passwortRegistrierenWdhInput.sendKeys(password);

        // Wiederholtes Passwort anzeigen
        WebElement passwortRegistrierenWdhInputAuge = driver.findElement(
                By.cssSelector("#passwort2_registrieren_id > vaadin-password-field-button")
        );
        passwortRegistrierenWdhInputAuge.click();

        // AGBs akzeptieren
        driver.findElement(By.id("checkbox_agbs_registrieren_id")).click();

        // Seite wechseln
        driver.findElement(By.className("register")).click();

        // ************************************************************************************** Registrieren Schritt 1

        // Warten, bis Seite geladen + Vorname eingeben
        WebElement vorname_feld = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#vorname_registrieren_1 > input")
        ));
        vorname_feld.click();
        vorname_feld.sendKeys("Jack");

        // Nachname eingeben
        WebElement nachname_feld = driver.findElement(By.cssSelector("#nachname_registrieren_1 > input"));
        nachname_feld.click();
        nachname_feld.sendKeys("Sparrow");

        // Abschluss auswählen (hier Master)
        WebElement abschlussCombobox = driver.findElement(By.id("abschluss_combobox_id"));
        abschlussCombobox.click();
        WebElement masterCombobox = driver.findElement(By.id("vaadin-combo-box-item-1"));
        masterCombobox.click();


        // Studienfach auswählen (hier CyberSecurity bzw. Molekularbiologie)
        WebElement studienfachCombobox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#studienfach_combobox_id > input")));
        studienfachCombobox.click();

        // Den Driver zwingen, 2 Sekunden zu warten
        tempWait = new WebDriverWait(driver, ofSeconds(2));
        try {
            tempWait.until(titleIs("Login"));
        }
        catch (TimeoutException e) {
            // nichts soll hier passieren
        }

        studienfachCombobox.sendKeys(Keys.UP);
        studienfachCombobox.sendKeys(Keys.UP);
        studienfachCombobox.sendKeys(Keys.UP);
        studienfachCombobox.sendKeys(Keys.RETURN);

        // Seite wechseln
        driver.findElement(By.className("next")).click();

        // ************************************************************************************** Registrieren Schritt 2
        // Warten, bis Seite geladen + Berufsbezeichnungen auswählen (mehrere, hier Berufsstarter & Teilzeit)
        WebElement berufsbezeichnung_feld = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#berufsbezeichnung_feld_id > input")
        ));
        berufsbezeichnung_feld.click();
        berufsbezeichnung_feld.sendKeys(Keys.UP);
        berufsbezeichnung_feld.sendKeys(Keys.RETURN);
        berufsbezeichnung_feld.sendKeys(Keys.UP);
        berufsbezeichnung_feld.sendKeys(Keys.UP);
        berufsbezeichnung_feld.sendKeys(Keys.RETURN);
        berufsbezeichnung_feld.sendKeys(Keys.TAB);

        // Branche auswählen (hier: Automobilindustrie)
        WebElement branche_feld = driver.findElement(By.cssSelector("#branche_feld_id > input"));
        branche_feld.click();
        branche_feld.sendKeys(Keys.UP);
        branche_feld.sendKeys(Keys.RETURN);
        branche_feld.sendKeys(Keys.TAB);

        // Standort auswählen (hier: Bonn)
        WebElement standort = driver.findElement(By.cssSelector("#standort_feld_id > input"));
        standort.click();
        standort.sendKeys(Keys.DOWN);
        standort.sendKeys(Keys.DOWN);
        standort.sendKeys(Keys.DOWN);
        standort.sendKeys(Keys.DOWN);
        standort.sendKeys(Keys.DOWN);
        standort.sendKeys(Keys.RETURN);
        standort.sendKeys(Keys.TAB);

        // Seite wechseln
        driver.findElement(By.className("next")).click();

        // ************************************************************************************** Registrieren Schritt 3
        // Warten bis Seite geladen + auf Zurück-Button drücken
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("back"))).click();

        // Den Driver zwingen, 2 Sekunden zu warten
        tempWait = new WebDriverWait(driver, ofSeconds(2));
        try {
            tempWait.until(titleIs("Login"));
        }
        catch (TimeoutException e) {
            // nichts soll hier passieren
        }

        // Jetzt wieder auf Seite zuvor, wieder auf next-Button (zum Checken, ob Daten wirklich gespeichert)
        driver.findElement(By.className("next")).click();

        // Den Driver zwingen, 2 Sekunden zu warten
        tempWait = new WebDriverWait(driver, ofSeconds(2));
        try {
            tempWait.until(titleIs("Login"));
        }
        catch (TimeoutException e) {
            // nichts soll hier passieren
        }

        // Abschließen der Registrierung
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("next"))).click();

        // Den Driver zwingen, 2 Sekunden zu warten
        tempWait = new WebDriverWait(driver, ofSeconds(2));
        try {
            tempWait.until(titleIs("Login"));
        }
        catch (TimeoutException e) {
            // nichts soll hier passieren
        }

        // Zurück auf Login-Seite
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("close-student"))).click();

        // Den Driver zwingen, 2 Sekunden zu warten
        tempWait = new WebDriverWait(driver, ofSeconds(2));
        try {
            tempWait.until(titleIs("Login"));
        }
        catch (TimeoutException e) {
            // nichts soll hier passieren
        }

        // *************************************************************************************** Einloggen bei Student
        // Warten, bis Seite geladen
        wait.until(titleIs("Login | EasyJob"));
        assertEquals(local + ":8080/login", driver.getCurrentUrl());

        // Seite neu laden, weil Selenium da nicht gut hinterherkommt
        driver.get(local + ":8080/login");

        // E-Mail eingeben
        WebElement emailTextInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#emailloginfeld_id > input")));
        emailTextInput.click();
        emailTextInput.sendKeys(email);

        // Passwort eingeben
        WebElement passwordTextInput = driver.findElement(By.cssSelector("#passwordloginfeld_id > input"));
        passwordTextInput.click();
        passwordTextInput.sendKeys(password);

        // Passwort anzeigen
        WebElement passwortLoginInputAuge = driver.findElement(By.cssSelector("#passwordloginfeld_id > vaadin-password-field-button"));
        passwortLoginInputAuge.click();

        // Auf Login-Button drücken
        driver.findElement(By.id("loginbutton_id_loginpage")).click();

        // *************************************************************************************** Ausloggen als Student
        // Warten, bis Seite geladen
        wait.until(titleIs("Profil"));
        assertEquals(local + ":8080/student", driver.getCurrentUrl());

        // in Einstellungen wechseln
        driver.findElement(By.xpath("//a[@href='student/einstellungen']")).click();

        // Warten, bis Seite geladen
        wait.until(titleIs("Einstellungen"));
        assertEquals(local + ":8080/student/einstellungen", driver.getCurrentUrl());

        // Auf Ausloggen-Button drücken
        driver.findElement(By.className("ausloggen")).click();

        // Den Driver zwingen, 2 Sekunden zu warten
        tempWait = new WebDriverWait(driver, ofSeconds(2));
        try {
            tempWait.until(titleIs("Login"));
        }
        catch (TimeoutException e) {
            // nichts soll hier passieren
        }

        // Zuerst den Dialog nochmal schließen
        driver.findElement(By.className("close-student")).click();

        // Den Driver zwingen, 2 Sekunden zu warten
        tempWait = new WebDriverWait(driver, ofSeconds(2));
        try {
            tempWait.until(titleIs("Login"));
        }
        catch (TimeoutException e) {
            // nichts soll hier passieren
        }

        // Nochmal auf Ausloggen-Button drücken
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ausloggen")));
        driver.findElement(By.className("ausloggen")).click();

        // Den Driver zwingen, 2 Sekunden zu warten
        tempWait = new WebDriverWait(driver, ofSeconds(2));
        try {
            tempWait.until(titleIs("Login"));
        }
        catch (TimeoutException e) {
            // nichts soll hier passieren
        }

        // Wirklich ausloggen
        driver.findElement(By.className("confirm")).click();

        // Den Driver zwingen, 2 Sekunden zu warten
        tempWait = new WebDriverWait(driver, ofSeconds(2));
        try {
            tempWait.until(titleIs("Login"));
        }
        catch (TimeoutException e) {
            // nichts soll hier passieren
        }

        // Checken, ob wieder auf Login-Seite
        wait.until(titleIs("Login | EasyJob"));
        // ---------------------------------------------------------------------------------------kann weg, wenn  gefixt
        driver.get(local + ":8080/login");
        //--------------------------------------------------------------------------------------------------------------
        assertEquals(local + ":8080/login", driver.getCurrentUrl());
    }
}
