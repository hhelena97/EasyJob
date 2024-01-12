package de.hbrs.easyjob.repositories;

import de.hbrs.easyjob.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface MeldungRepository extends JpaRepository<Meldung, Integer> {

    @Query("SELECT m FROM Meldung m")
    List<Meldung> findAllMeldungen();

    @Query("SELECT m FROM Meldung m WHERE m.bearbeitet = false")
    List<Meldung> findAllNochNichtBearbeitet();


    @Query ("SELECT m FROM Meldung m WHERE m.bearbeitet = false AND m.person != null")
    List<Meldung> findAllPersonen();

    @Query ("SELECT m FROM Meldung m WHERE m.bearbeitet = false AND m.unternehmen != null")
    List<Meldung> findAllUnternehmen();

    @Query ("SELECT m FROM Meldung m WHERE m.bearbeitet = false AND m.job != null")
    List<Meldung> findAllJobs();

    @Query ("SELECT m FROM Meldung m WHERE m.bearbeitet = false AND m.chat != null")
    List<Meldung> findAllChats();


}
