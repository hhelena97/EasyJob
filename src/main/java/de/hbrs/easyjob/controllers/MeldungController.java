package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.MeldungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MeldungController {

    private final MeldungRepository meldungRepository;

    @Autowired
    public MeldungController(MeldungRepository meldungRepository) {
        this.meldungRepository = meldungRepository;
    }

    /**
     * zum Speichern von Meldungen
     * Kann eine Person, ein Unternehmen, einen Job oder einen Chat bekommen
     * @param meldung die neue Meldung, die gespeichert werden soll
     * @param person, unternehmen, job oder chat der gemeldet wird
     * @return true, wenn gespeichert
     */
    public boolean saveMeldung(Meldung meldung, Person person) {
        meldung.setPerson(person);
        meldung.setBearbeitet(false);
        meldungRepository.save(meldung);
        Meldung t = meldungRepository.findById(meldung.getId_Meldung()).get();
        return !t.isBearbeitet();
    }

    public boolean saveMeldung(Meldung meldung, Unternehmen unternehmen) {
        meldung.setUnternehmen(unternehmen);
        meldung.setBearbeitet(false);
        meldungRepository.save(meldung);
        Meldung t = meldungRepository.findById(meldung.getId_Meldung()).get();
        return !t.isBearbeitet();
    }

    public boolean saveMeldung(Meldung meldung, Job job) {
        meldung.setJob(job);
        meldung.setBearbeitet(false);
        meldungRepository.save(meldung);
        Meldung t = meldungRepository.findById(meldung.getId_Meldung()).get();
        return !t.isBearbeitet();
    }

    public boolean saveMeldung(Meldung meldung, Chat chat, Person person) {
        meldung.setChat(chat);
        meldung.setPerson(person);
        meldung.setBearbeitet(false);
        meldungRepository.save(meldung);
        Meldung t = meldungRepository.findById(meldung.getId_Meldung()).get();
        return !t.isBearbeitet();
    }


    /**
     * Finde alle Meldungen zu Personen
     * @return Liste aller Meldungen zu Personen
     */
    public List<Meldung> getAllGemeldetePersonen(){
        return meldungRepository.findAllPersonen();
    }

    /**
     * Finde alle Meldungen zu Unternehmen
     * @return Liste aller Meldungen zu Unternehmen
     */
    public List<Meldung> getAllGemeldeteUnternehmen(){
        return meldungRepository.findAllUnternehmen();
    }


    /**
     * Finde alle Meldungen zu Jobs
     * @return Liste aller Meldungen zu Jobs
     */
    public List<Meldung> getAllGemeldeteJobs(){
        return meldungRepository.findAllJobs();
    }

    /**
     * Finde alle gemeldeten Meldungen zu Chats
     * @return Liste aller Meldungen zu Chats
     */
    public List<Meldung> getAllGemeldeteChats(){
        return meldungRepository.findAllChats();
    }

    /**
     * Meldung bearbeiten
     * setzt die mitgebene Meldung auf "bearbeitet" und schließt damit den Meldungsvorgang ab
     * @param meldung, die Meldung, die bearbeitet wird
     * @return true, wenn Meldung als bearbeitet gespeichert.
     */
    public boolean meldungBearbeiten(Meldung meldung) {
        if (meldung == null) {
            return false;
        }
        meldung.setBearbeitet(true);
        meldungRepository.save(meldung);

        return meldungRepository.findById(meldung.getId_Meldung()).get().isBearbeitet();
    }

    //saveMeldungUnternehmen - erledigt

    // sollte man wirklich eine null-Meldung zuweisen können? -> Vorschlag: Grund als Pflichtfeld - Grund wird nicht umgesetzt Es gibt nur "leere" Meldungen.

    // so kann man einer Meldung sowohl eine Person, einen Chat und einen Job zu weisen (soll das so)? Ja, genau. Man gibt der Meldung das mit, was gemeldet wird.
    //Beim Chat muss eine Person mitgegeben werden, weil nicht der gemeldete Chat, sondern die gemeldete Person dem Admin zum möglichen Sperren angezeigt wird.

    //TODO: JavaDocs anpassen bei getAlleGemeldetenXXX
    // Die getAlleGemeldetenPersonen/Jobs/etc.-Methoden sind verwirrend. Sollen die Jobs/Personen/etc. oder die Meldungen dazu gefunden werden?

    //TODO: JavaDocs anpassen bei meldungBearbeiten
    // Wozu ist diese Funktion? Wie bearbeitet man im Endeffekt Meldung?
    //Der Admin prüft die Meldung und sperrt dann den Account (Profil deaktivieren) oder lässt es und drückt den Knopf "Meldung abschließen" dadurch wird sie "bearbeitet"
}
