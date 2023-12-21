package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.*;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface ValidationController {
    /**
     * Prüft, ob Email gültig ist.
     *
     * @param email Email, die geprüft werden soll
     * @return true, wenn Email gültig ist & false, wenn nicht
     */
    static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Prüft, ob Passwort gültig ist.
     * Passwort muss mindestens 8 Zeichen lang sein
     * Passwort darf maximal 64 Zeichen lang sein
     * Passwort muss mindestens 1 Sonderzeichen enthalten
     * Passwort muss Groß- und Kleinschreibung enthalten
     *
     * @param password Passwort, das geprüft werden soll
     * @return true, wenn Passwort gültig ist & false, wenn nicht
     */
    static boolean isValidPassword(String password) {
        // TODO: password richtig erkennen
        return passwordHaveCorrectLength(password) &&
                passwordHasUpperAndLowerCase(password) &&
                passwordHasSpecialCharacters(password) &&
                passwordHasNumbers(password);

    }

    /**
     * Prüft, ob Passwort die richtige Länge hat
     *
     * @param password Passwort, das geprüft werden soll
     * @return true, wenn Passwort die richtige Länge hat & false, wenn nicht
     */
    static boolean passwordHaveCorrectLength(String password) {
        return password.length() >= 8 && password.length() <= 64;
    }

    /**
     * Prüft, ob Passwort Groß- und Kleinschreibung enthält.
     *
     * @param password Passwort, das geprüft werden soll
     * @return true, wenn Passwort Groß- und Kleinschreibung enthält & false, wenn nicht
     */
    static boolean passwordHasUpperAndLowerCase(String password) {
        boolean hasUpperCase = false;
        boolean hasLoweCase = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLoweCase = true;
            }

            if (hasUpperCase && hasLoweCase) {
                // Der String enthält sowohl Großbuchstaben als auch Kleinbuchstaben
                return true;
            }
        }
        // Der String enthält entweder nur Großbuchstaben oder nur Kleinbuchstaben oder beides nicht
        return false;
    }

    /**
     * Prüft, ob Passwort Sonderzeichen enthält.
     *
     * @param password Passwort, das geprüft werden soll
     * @return true, wenn Passwort Sonderzeichen enthält & false, wenn nicht
     */
    static boolean passwordHasSpecialCharacters(String password) {
        for (char c : password.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return true; // Der String enthält ein Sonderzeichen
            }
        }
        return false; // Der String enthält keine Sonderzeichen
    }

    /**
     * Prüft, ob Passwort Zahlen enthält.
     *
     * @param password Passwort, das geprüft werden soll
     * @return true, wenn Passwort Zahlen enthält & false, wenn nicht
     */
    static boolean passwordHasNumbers(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                return true; // Der String enthält eine Zahl
            }
        }
        return false; // Der String enthält keine Zahl
    }

    /**
     * Prüft, ob Name gültig ist.
     * Name muss mindestens 1 Zeichen und maximal 32 Zeichen lang sein
     * Name darf nur Buchstaben und Bindestriche enthalten
     *
     * @param name Vorname, der geprüft werden soll
     * @return true, wenn Vorname gültig ist & false, wenn nicht
     */
    static boolean isValidName(String name) {
        // TODO: an Test Cases aus ValidationControllerTest anpassen -> (mehrere) Zweitnamen zulassen, Umlaute zulassen!, Nur "-" als Namen nicht zulassen!
        String nameRegex = "^[a-zA-Z\\-]{1,32}$";

        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    /** Prüft, ob der Name eines Unternehmens gültig ist.
     * Name muss mindestens 2 Zeichen und maximal 64 Zeichen lang sein
     * Name darf keine Leerzeichen am Anfang oder Ende enthalten
     * @param companyName Name, der geprüft werden soll
     * @return true, wenn Name gültig ist & false, wenn nicht
     */
    static boolean isValidCompanyName(String companyName) {
        String nameRegex = "^\\S.{0,62}\\S$";

        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(companyName);
        return matcher.matches();
    }

    /**
     * Prüft, ob gültige Branchen angegeben wurden.
     *
     * @param branchen          Branchen, die geprüft werden sollen
     * @param brancheRepository Repository für Branchen
     * @return true, wenn Branchen gültig sind & false, wenn nicht
     */
    static boolean isValidBranche(Set<Branche> branchen, BrancheRepository brancheRepository) {
        for (Branche branche : branchen) {
            Branche gefundenBranche = brancheRepository.findByName(branche.getName());
            if (gefundenBranche == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prüft, ob gültige BerufsFelder angegeben wurden.
     *
     * @param berufsFelder         BerufsFelder, die geprüft werden sollen
     * @param berufsFeldRepository Repository für BerufsFelder
     * @return true, wenn BerufsFelder gültig sind & false, wenn nicht
     */
    static boolean isValidBerufsFeld(Set<BerufsFelder> berufsFelder, BerufsFeldRepository berufsFeldRepository) {
        for (BerufsFelder berufsFeld : berufsFelder) {
            BerufsFelder gefundeneBerufsFelder = berufsFeldRepository.findByName(berufsFeld.getName());
            if (gefundeneBerufsFelder == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prüft, ob Telefonnummer gültig ist.
     * Telefonnummer darf nur Ziffern, Bindestriche oder Leerzeichen enthalten
     *
     * @param telefonnummer Telefonnummer, die geprüft werden soll
     * @return true, wenn Telefonnummer gültig ist & false, wenn nicht
     */
    static boolean isValidTelefonnummer(String telefonnummer) {
        // TODO: Telefonnummern mit Bindestrichen und Leerzeichen anpassen (siehe ValidationControllerTest)
        // Erkennung von korrekten deutschen Telefonnummern mit oder ohne Ländervorwahl und mit oder ohne Leerzeichen oder Bindestriche
        String telefonnummerRegex = "^(\\+49|0)[\\s\\-]?[1-9][0-9]{6,14}$";

        // Erzeuge ein Pattern-Objekt
        Pattern pattern = Pattern.compile(telefonnummerRegex);
        // Erzeuge ein Matcher-Objekt
        Matcher matcher = pattern.matcher(telefonnummer);
        // Überprüfe Telefonnummer
        return matcher.matches();
    }

    /**
     * Prüft, ob die angegebenen JobKategorien gültig sind.
     *
     * @param jobKategorien JobKategorien, die geprüft werden sollen
     * @return ob die JobKategorien gültig sind
     */
    static boolean isValidJobKategorie(Set<JobKategorie> jobKategorien, JobKategorieRepository jobKategorieRepository) {
        for (JobKategorie jobKategorie : jobKategorien) {
            JobKategorie gefundenJobKategorie = jobKategorieRepository.findByKategorie(jobKategorie.getKategorie());
            if (gefundenJobKategorie == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prüft, ob die angegebenen Orte gültig sind.
     *
     * @param orte Orte, die geprüft werden sollen
     * @return ob die Orte gültig sind
     */
    static boolean isValidOrt(Set<Ort> orte, OrtRepository ortRepository) {
        for (Ort ort : orte) {
            Ort gefundenOrte = ortRepository.findByPLZAndOrt(ort.getPLZ(), ort.getOrt());
            if (gefundenOrte == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prüft, ob das angegebene Studienfach gültig ist.
     *
     * @param studienfach Studienfach, das geprüft werden soll
     * @return ob das Studienfach gültig ist
     */
    static boolean isValidStudienfach(Studienfach studienfach, StudienfachRepository studienfachRepository) {
        return studienfachRepository.findByFachAndAbschluss(studienfach.getFach(), studienfach.getAbschluss()) != null;
    }

    /**
     * Überprüft, ob die Beschreibung eines Unternehmens gültig ist.
     *
     * @param beschreibung Beschreibung, die geprüft werden soll
     * @return true, wenn Beschreibung gültig ist & false, wenn nicht
     */
    static boolean isValidUnternehmensbeschreibung(String beschreibung) {
        return beschreibung != null && beschreibung.length() <= 400;
    }
}
