package de.hbrs.easyjob.repositories;

import de.hbrs.easyjob.entities.Chat;
import de.hbrs.easyjob.entities.Nachricht;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface NachrichtRepository extends JpaRepository<Nachricht, Integer> {

    List<Nachricht>findByChat(Chat chat);

}
