package de.hbrs.easyjob;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.junit.jupiter.api.*;

public class SearchStudentProfilesTests {
    WebDriver[] drivers = new WebDriver[2];

    @BeforeAll
    static void setUp() {
        // Chrome
        WebDriverManager.chromedriver().setup();

        // Firefox
        WebDriverManager.firefoxdriver().setup();
    }

    @BeforeEach
    void setUpEach() {
        // Chrome
        ChromeOptions optionsChrome = new ChromeOptions();
        optionsChrome.addArguments("--remote-allow-origins=");
        drivers[0] = new ChromeDriver(optionsChrome);

        // Firefox
        drivers[1] = new FirefoxDriver();
    }

    @Test
    @DisplayName("Seite laden lassen")
    void lodePage() {
        for (WebDriver driver : drivers) {
            driver.get("http://localhost:8080/login");
            Assertions.assertEquals("Please sign in", driver.getTitle());

        }
    }

    @AfterEach
    void tearDownEach() {
        for (WebDriver driver : drivers) {
            driver.quit();
        }
    }

}
