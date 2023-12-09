package de.hbrs.easyjob.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static de.hbrs.easyjob.controllers.ValidationController.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationControllerTest {

    @Test
    @DisplayName("Teste verschiedene (falsche und richtige) Email-Adressen, um die isValidEmail-Methode zu prüfen")
    void isValidEmailTest() {

        // Teste richtige Email-Adressen:
        assertTrue(isValidEmail("max-muster@mann.de"));
        assertTrue(isValidEmail("max.muster@mann.com"));
        assertTrue(isValidEmail("max.mustermann@smail.inf.h-brs.de"));

        // Teste falsche Email-Adressen:
        assertFalse(isValidEmail("1"));
        assertFalse(isValidEmail("a"));
        assertFalse(isValidEmail("muster-mann.de"));
        assertFalse(isValidEmail("muster@mann"));
        assertFalse(isValidEmail("VollLangeEmail-NochVielLaenger@EMailInLang.SoEinLangesLaenderKÜrzel"));
    }

    @Test
    @DisplayName("Teste verschiedene (falsche und richtige) Passwörter, um die isValidPasswort-Methode zu prüfen")
    void isValidPasswordTest() {

        // Teste richtige Passwörter:
        assertTrue(isValidPassword("Passwort-123"));
        assertTrue(isValidPassword("Mein-Tolles-Passw0rt!"));
        assertTrue(isValidPassword("pass-W0rt"));

        // Teste falsche Passwörter:
        assertFalse(isValidPassword("a"));
        assertFalse(isValidPassword("passwort"));
        assertFalse(isValidPassword("passwort123"));
        assertFalse(isValidPassword("passwort-123"));
        assertFalse(isValidPassword("12345678"));
        assertFalse(isValidPassword("Passwort"));
        assertFalse(isValidPassword("Passwort123"));
    }

    @Test
    @DisplayName("Teste verschiedene (falsche und richtige) Namen, um die isValidName-Methode zu prüfen")
    void isValidNameTest() {

        // Teste richtige Vornamen:
        assertTrue(isValidName("Hans"));
        assertTrue(isValidName("Karl-Heinz"));
        assertTrue(isValidName("Anna Marie"));
        assertTrue(isValidName("Klaus M."));      // wäre das wirklich valide? gibts solche Namen?

        // Teste falsche Vornamen:
        assertFalse(isValidName("123"));
        assertFalse(isValidName("Fritz!!1"));
        assertFalse(isValidName("a"));
        assertFalse(isValidName("HalloDasIstEinSuperLangerNameUndWeilDerSoLangIstIstDerFalsch"));

        // Teste richtige Nachnamen:
        assertTrue(isValidName("Müller"));
        assertTrue(isValidName("Schmitz"));
        assertTrue(isValidName("Meier-Schmitz"));

        // Teste falsche Nachnamen:
        assertFalse(isValidName("123"));
        assertFalse(isValidName("Schmitz!!1"));
        assertFalse(isValidName("a"));
        assertFalse(isValidName("HalloDasIstEinSuperLangerNameUndWeilDerSoLangIstIstDerFalsch"));
    }

    @Test
    @DisplayName("Teste verschiedene (falsche und richtige) Telefonnummern, um die isValidTelefonnummer-Methode zu prüfen")
    void isValidTelefonnummerTest() {

        // Teste richtige Telefonnummern:
        assertTrue(isValidTelefonnummer("+49170123456"));
        assertTrue(isValidTelefonnummer("0221 123456"));
        assertTrue(isValidTelefonnummer("0221-123456"));
        assertTrue(isValidTelefonnummer("+49 170 123456"));
        assertTrue(isValidTelefonnummer("01681234567"));

        // Teste falsche Telefonnummern:
        assertFalse(isValidTelefonnummer("1"));
        assertFalse(isValidTelefonnummer("1234! 23456"));
        assertFalse(isValidTelefonnummer("+490170123456"));
        assertFalse(isValidTelefonnummer("a"));
        assertFalse(isValidTelefonnummer("abcde abcdefgh"));
        assertFalse(isValidTelefonnummer("165234975831687579413425794164547613946385429654185458167433749184576"));
    }

    // TODO: restliche Methoden aus ValidationController testen
}
