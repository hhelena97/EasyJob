package de.hbrs.easyjob.control;

import de.hbrs.easyjob.dtos.impl.PersonDTOimpl;
import de.hbrs.easyjob.dtos.impl.StudentDTOimpl;
import de.hbrs.easyjob.dtos.impl.UnternehmenspersonDTOimpl;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.repository.PersonRepository;

public class LogoutControl
{

    private PersonRepository personRepository;
    private PersonDTOimpl personDTO = null;

    //private StudentDTOimpl studentDTO = null;
    //private UnternehmenspersonDTOimpl unternpersonDTO = null;

    public LogoutControl(PersonRepository personRepository)
    {
        this.personRepository = personRepository;
    }


    /**
     *
     *
     * @param person
     * @return
     */
    public boolean logout (Person person)
    {
        // Grober Ablauf der Methode:
        // wenn auf den Knopf "ausloggen" gedrückt wurde:
        // Fenster mit "Wirklich ausloggen?"
        // wenn "ja":
            // Person aus dem Zwischenspeicher nehmen
            // *ausloggen*
            // Weiterleitung zur Login-Seite (mit Meldung, "Sie sind abgemeldet" oder so)
        // wenn "nein":
            // Fenster schließen



        /*  Frage:

            Was ist ein nicht-eingeloggter Benutzer? Die Person, oder?
            Also wenn sich eine Person einloggt, wird ja geprüft, ob es ein Student oder so ist.
            Das heißt, wenn wir von einem Student sprechen, kann ich davon ausgehen, dass der eingeloggt ist?
            Oder woran mache ich fest, dass die Person jetzt wirklich eingeloggt ist?

         */

        return true;
    }
}
