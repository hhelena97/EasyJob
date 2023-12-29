package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class MeldungService {

    private final MeldungRepository meldungRepository;
    private final PersonRepository personRepository;
    private final UnternehmenRepository unternehmenRepository;
    private final JobRepository jobRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    public MeldungService(MeldungRepository meldungRepository,
                          PersonRepository personRepository,
                          UnternehmenRepository unternehmenRepository,
                          JobRepository jobRepository) {
        this.meldungRepository = meldungRepository;
        this.personRepository = personRepository;
        this.unternehmenRepository = unternehmenRepository;
        this.jobRepository = jobRepository;
    }

    public List<Meldung> findAllByUnternehmenId(int unternehmenId) {
        return meldungRepository.findAllByUnternehmenId(unternehmenId);
    }

    @Transactional
    public Meldung saveMeldung(Meldung meldung) {
        Person gefundenePerson = personRepository.findByEmail(Meldung.getPerson().getEmail());
        Meldung.setPerson(gefundenePerson);

        Unternehmen gefundenesUnternehmen = unternehmenRepository.findByName(Meldung.getUnternehmen().getName());
        Meldung.setUnternehmen(gefundenesUnternehmen);

        Job gefundenerJob = jobRepository.findByTitel(Meldung.getJob().getName());
        Meldung.setJob(gefundenerJob);

        return meldungRepository.save(meldung);
    }


    public List<Meldung> getAllMeldungen() {
        return meldungRepository.findAllMeldungen();
    }


    public List<Meldung> getMeldungensByIds(List<Integer> ids) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Meldung> cq = cb.createQuery(Meldung.class);
        Root<Meldung> meldung = cq.from(Meldung.class);

        cq.select(meldung).where(meldung.get("id_Meldung").in(ids));
        return entityManager.createQuery(cq).getResultList();
    }


}
