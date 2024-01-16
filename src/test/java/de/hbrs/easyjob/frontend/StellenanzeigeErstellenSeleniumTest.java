package de.hbrs.easyjob.frontend;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

class StellenanzeigeErstellenSeleniumTest {

    @Test
    @DisplayName("Testet den Stellenanzeigen erstellen-Vorgang in Selenium")
    void stellenanzeigeErstellenTest() {
        /* ************* Arrange ************* */
        // WebDriver Setup
        System.setProperty("webdriver.gecko.driver", "/C:/Users/Vanessa/Downloads/geckodriver-v0.33.0-win64/geckodriver.exe");
        WebDriverManager.firefoxdriver().setup();
        WebDriverManager.chromedriver().setup();
        WebDriver[] drivers = new WebDriver[2];
        drivers[0] = new FirefoxDriver();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        drivers[1] = new ChromeDriver(options);

        for(WebDriver driver : drivers) {
            WebDriverWait wait = new WebDriverWait(driver, ofSeconds(30), ofSeconds(1));

            // Warten, bis Login-Seite geladen
            driver.manage().window().maximize();
            driver.get("http://localhost:8080/login");
            wait.until(titleIs("Login | EasyJob"));
            assertEquals("http://localhost:8080/login", driver.getCurrentUrl());

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
            stellenbeschreibung.sendKeys(
                    "Dies ist voll die tolle Stelle, ich hoffe, Sie wollen hier WIRKLICH" +
                    " arbeiten. Die Flying Dutchman braucht nämlich einen neuen Kapitän, weil Will Turner zurück " +
                    "nachhause zu seiner Family will."
            );

            driver.findElement(By.id("input-vaadin-checkbox-23")).click();

            driver.findElement(By.className("abbrechen-button")).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("close-unternehmen")));
            driver.findElement(By.className("close-unternehmen")).click();

            WebDriverWait tempWait = new WebDriverWait(driver, ofSeconds(2)); // define local/temp wait only for the "sleep"
            try {
                tempWait.until(titleIs("Login")); // condition you are certain won't be true
            } catch (TimeoutException e) {
                // catch the exception and continue the code
            }

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("fertig-button")));
            driver.findElement(By.className("fertig-button")).click();

            /* ************* Tear down ************ */
            driver.quit();
        }
    }
}
