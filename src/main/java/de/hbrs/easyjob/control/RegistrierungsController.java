package de.hbrs.easyjob.control;

import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@RestController
public class RegistrierungsController{
    @Autowired
    PersonRepository personRepository;


    public boolean createPerson(Person person,boolean AGB ){
        //prüfe Email und Passwort
        boolean ret =(isValidEmail(person.getEmail()))&&(isValidPassword(person.getPasswort())&&isAGBAccepted(AGB)&&isValidVorname(person.getVorname())&&isValidNachname(person.getNachname())&&isValidTelefonnummer(person.getTelefon()));
        if(ret){
            personRepository.save(person);
        }
        return ret;
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    //TODO password richtig erkennen
    public boolean isValidPassword(String password) {
        // Mindestens 8 Zeichen, maximal 64 Zeichen
        // Mindestens 1 Sonderzeichen
        //Groß- und Kleinschreibung
        if (passwordHaveCorrectLength(password)&&passwordHaveUpperAndLowerCase(password)&&passwordHasSpecialCharacters(password)&&passwordHasNumbers(password)){return true;}
        return false;

    }
    public boolean passwordHaveCorrectLength(String password){
        if (password.length() < 8 || password.length() > 64) {
            return false;
        }
        return true;
    }
    public boolean passwordHaveUpperAndLowerCase(String password){
        boolean hatGross = false;
        boolean hatKlein = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hatGross = true;
            } else if (Character.isLowerCase(c)) {
                hatKlein = true;
            }

            if (hatGross && hatKlein) {
                return true; // Der String enthält sowohl Großbuchstaben als auch Kleinbuchstaben
            }
        }

        return false; // Der String enthält entweder nur Großbuchstaben oder nur Kleinbuchstaben oder beides nicht
    }
    public boolean passwordHasSpecialCharacters(String password){
        for (char c : password.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return true; // Der String enthält ein Sonderzeichen
            }
        }

        return false; // Der String enthält keine Sonderzeichen
    }
    public boolean passwordHasNumbers(String password){
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                return true; // Der String enthält eine Zahl
            }
        }

        return false; // Der String enthält keine Zahl
    }
    public  boolean isAGBAccepted(boolean b) {
       return b;
    }
    public  boolean isValidVorname(String vorname) {
        int minLaenge = 3;
        int maxLaenge = 20;
        return vorname.length() >= minLaenge && vorname.length() <= maxLaenge;
    }
    public boolean isValidNachname(String nachname) {
        int minLaenge = 3;
        int maxLaenge = 20;
        return nachname.length() >= minLaenge && nachname.length() <= maxLaenge;
    }
    public boolean isValidTelefonnummer(String telefonnummer) {
        // akzeptiert Ziffern, Bindestriche oder Leerzeichen
        String telefonnummerRegex = "^[0-9\\s\\-]+$";

        // Erzeuge einen Pattern-Objekt
        Pattern pattern = Pattern.compile(telefonnummerRegex);
        // Erzeuge einen Matcher-Objekt
        Matcher matcher = pattern.matcher(telefonnummer);
        // Überprüfe Telefonnummer
        return matcher.matches();
    }
    public boolean isPasswordTheSame(String password1,String password2){return password1.equals(password2);}

}