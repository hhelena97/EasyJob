package de.hbrs.easyjob.frontend;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

class StellenanzeigeErstellenSeleniumTest {

    // WebDriver
    private final WebDriver[] drivers = new WebDriver[1];

    @BeforeAll
    static void setUp() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUpEach() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        drivers[0] = new ChromeDriver(options);
    }

    @AfterEach
    void tearDownEach() {
        drivers[0].quit();
    }

    /**
     * eine Wartefunktion für den WebDriver, da er manchmal zu schnell durchklickt
     * @param wd        Webdriver, der warten soll
     */
    private void warteWebDriver(WebDriver wd, int sekunden) {
        WebDriverWait tempWait = new WebDriverWait(wd, ofSeconds(sekunden));
        try {
            tempWait.until(titleIs("Nicht existierende Seite"));
        } catch (TimeoutException e) {
            // nichts soll hier passieren
        }
    }

    private void warteWebDriver(WebDriver wd) {
        warteWebDriver(wd, 2);
    }

    @Test
    @DisplayName("Testet den Stellenanzeigen erstellen-Vorgang in Selenium")
    void SeleniumTest() {
        int i = 0;
        for(WebDriver driver : drivers) {
            driver.manage().window().maximize();
            stellenanzeigeErstellenTest(driver,
                    new WebDriverWait(driver, ofSeconds(30), ofSeconds(1)),
                    i != 0
            );
            i++;
        }
    }


    void stellenanzeigeErstellenTest(WebDriver driver, WebDriverWait wait, boolean docker) {

        String local;
        if (docker) {
            local = "http://host.docker.internal";
        } else {
            local = "http://localhost";
        }

        // Warten, bis Login-Seite geladen
        warteWebDriver(driver, 10);
        driver.get(local + ":8080/login");
        wait.until(titleIs("Login | EasyJob"));
        assertEquals(local + ":8080/login", driver.getCurrentUrl());

        // E-Mail eingeben
        warteWebDriver(driver);
        WebElement emailTextInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#emailloginfeld_id > input")));
        emailTextInput.click();
        emailTextInput.sendKeys("rudolf@christmas.org");

        // Passwort eingeben
        WebElement passwordTextInput = driver.findElement(By.cssSelector("#passwordloginfeld_id > input"));
        passwordTextInput.click();
        passwordTextInput.sendKeys("sicheresPasswort123");

        // Passwort anzeigen
        warteWebDriver(driver);
        WebElement passwortLoginInputAuge = driver.findElement(By.cssSelector("#passwordloginfeld_id > vaadin-password-field-button"));
        passwortLoginInputAuge.click();

        // Auf Login-Button drücken
        driver.findElement(By.id("loginbutton_id_loginpage")).click();

        // Auf Unternehmensperson-Profil landen
        warteWebDriver(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("unternehmenProfil")));
        assertEquals(local + ":8080/unternehmen/unternehmenperson", driver.getCurrentUrl());

        // Zur Stellenanzeige Erstellen-View wechseln
        warteWebDriver(driver);
        driver.findElement(By.xpath("//a[@href='unternehmen/']")).click();

        // Auf Unternehmensprofil landen
        warteWebDriver(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("grosseTitleUnternehmen")));
        assertEquals(local + ":8080/unternehmen", driver.getCurrentUrl());

        // Auf Stellenangebot hinzufuegen klicken
        warteWebDriver(driver);
        driver.findElement(By.xpath("//a[@href='unternehmen/job/create']")).click();

        // Auf Stellenanzeige erstellen landen
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("zurueck-button"))).click();

        /* **************** Act *************** */
        warteWebDriver(driver, 5);
        WebElement date = driver.findElement(By.cssSelector("#DatumAuswaehlenId > input"));
        date.click();
        date.sendKeys("1.12.2024");
        date.sendKeys(Keys.RETURN);
        date.sendKeys(Keys.RETURN);

        warteWebDriver(driver);
        WebElement berufsbezeichnung = driver.findElement(By.cssSelector("#BerufsbezeichnungWaehlenID > input"));
        berufsbezeichnung.click();
        warteWebDriver(driver);
        berufsbezeichnung.sendKeys(Keys.UP);
        berufsbezeichnung.sendKeys(Keys.UP);
        berufsbezeichnung.sendKeys(Keys.RETURN);
        berufsbezeichnung.sendKeys(Keys.RETURN);

        warteWebDriver(driver);
        WebElement standort = driver.findElement(By.cssSelector("#standortAuswaehlenId > input"));
        standort.click();
        warteWebDriver(driver);
        standort.sendKeys(Keys.DOWN);
        standort.sendKeys(Keys.DOWN);
        standort.sendKeys(Keys.DOWN);
        standort.sendKeys(Keys.RETURN);

        warteWebDriver(driver);
        WebElement titel = driver.findElement(By.cssSelector("#titleID > input"));
        titel.click();
        warteWebDriver(driver);
        titel.sendKeys("Kapitän der Flying Dutchman");

        WebElement stellenbeschreibung = driver.findElement(By.cssSelector("#stellenbeschreibungEinfuegenId"));
        stellenbeschreibung.click();
        warteWebDriver(driver);
        stellenbeschreibung.sendKeys(
                "Die Flying Dutchman braucht einen neuen Captain."
        );

        driver.findElement(By.id("HomeofficeJaNeinId")).click();

        warteWebDriver(driver);
        driver.findElement(By.className("abbrechen-button")).click();
        warteWebDriver(driver);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("close-unternehmen")));
        driver.findElement(By.className("close-unternehmen")).click();
        warteWebDriver(driver);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("fertig-button")));
        driver.findElement(By.className("fertig-button")).click();
    }
}
