package de.hbrs.easyjob.repository;

import de.hbrs.easyjob.entities.Ort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface OrtRepository extends JpaRepository<Ort, Integer> {

    Ort findByPLZAndOrt(String plz, String ort);
    Ort findByOrt(String ort);
}
