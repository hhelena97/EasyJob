package de.hbrs.easyjob.repositories;

import de.hbrs.easyjob.entities.Faehigkeit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
public interface FaehigkeitRepository extends JpaRepository<Faehigkeit, Integer>{

    List<Faehigkeit> findByKategorie (String kategorie);

}
