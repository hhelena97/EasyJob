package de.hbrs.easyjob.control;

import com.vaadin.flow.component.UI;
//import de.hbrs.easyjob.dtos.impl.PersonDTOimpl;
//import de.hbrs.easyjob.dtos.impl.StudentDTOimpl;
//import de.hbrs.easyjob.dtos.impl.UnternehmenspersonDTOimpl;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutControl
{

    //private Person person;
    private PersonRepository personRepository;
    //private PersonDTOimpl personDTO = null;

    public LogoutControl(PersonRepository personRepository)
    {
        this.personRepository = personRepository;
    }


    /**
     * Die Methode logout() setzt die aktuell angemeldete Person auf null und schließt die laufende Session.
     *
     * Noch zu machende Anpassungen:
     *      - Fehler-Handling
     *      - Vaadin-Session durch SpringbootSecure-Session ersetzen
     *
     * An den Tester dieser Methode: Hi, wenn ein Fehler-Handling da ist, also geprüft wird, ob die Person wirklich angemeldet ist,
     * oder nicht, soll die Methode true liefern, wenn man erfolgreich ausgeloggt ist und false liefern, wenn das Ausloggen nicht ging.
     *
     * @return immer true (wenn Fehler-Handling da, nicht immer true)
     */
    public boolean logout ()
    {
        // Grober Ablauf der Funktion "ausloggen":
        // wenn auf den Knopf "ausloggen" gedrückt wurde:
        // Fenster mit "Wirklich ausloggen?"
            // wenn "ja":
                // Person aus dem Zwischenspeicher nehmen
                // *ausloggen*
                // Weiterleitung zur Login-Seite (mit Meldung, "Sie sind abgemeldet" oder so)
            // wenn "nein":
                // Fenster schließen

        System.out.println("logout-Methode aufgerufen");

        // Hier müsste noch ein Fehler-Handling hin

        //if (UI.getCurrent().getSession() != null)
        //{
            //if (UI.getCurrent().getSession().getAttribute("current_user") != null)
            //{

                // Ausgaben zum testen:
                System.out.println("aktuelle Person:");

                Person person = null;
                person = (Person)UI.getCurrent().getSession().getAttribute("current_User");
                System.out.println(person.toString());

                // Eigentliche Methode:
                // -----------------------------------------------------------------------------------------------------
                // ausloggen:
                UI.getCurrent().getSession().setAttribute("current_User", null);
                UI.getCurrent().getSession().close(); // lädt die Seite auch neu
                // -----------------------------------------------------------------------------------------------------

                // Ausgaben zum testen:
                person = (Person)UI.getCurrent().getSession().getAttribute("current_User");
                if (person == null)
                {
                    System.out.println("keine current Person");
                }
                else
                {
                    System.out.println(person.toString());
                }

                return true;
            //}
        //}

        //return false;
    }
}
