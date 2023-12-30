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

    public Meldung savePersonMeldung(Meldung meldung, Person person) {

        meldung.setPerson(person);
        meldung.setBearbeitet(false);
        return meldungRepository.save(meldung);
    }

    public Meldung saveUnternehmenMeldung(Meldung meldung, Unternehmen u) {

        meldung.setUnternehmen(u);
        meldung.setBearbeitet(false);
        return meldungRepository.save(meldung);
    }

    public Meldung saveJobMeldung(Meldung meldung, Job job) {

        meldung.setJob(job);
        meldung.setBearbeitet(false);
        return meldungRepository.save(meldung);
    }

    /*
    public Meldung saveChatMeldung(Meldung meldung, Chat chat) {

        meldung.setChat(chat);
        meldung.setBearbeitet(false);
        return meldungRepository.save(meldung);
    }
     */

    public List<Meldung> getAlleMeldungen() {
        return meldungRepository.findAll();
    }

    /** Wie kann ich alle Meldungen zu Personen finden, also wo meldung.person != null und bearbeitet == false
    public List<Meldung> getAllePersonenMeldungen(){
    }

    /**
     * Meldung bearbeiten
     */
    public boolean meldungBearbeiten(Meldung meldung) {
        if (meldung == null) {
            return false;
        }
        meldung.setBearbeitet(true);
        meldungRepository.save(meldung);

        return true;
    }

    @Transactional
    public Meldung saveMeldung(Meldung meldung) {
        Person gefundenePerson = personRepository.findByEmail(meldung.getPerson().getEmail());
        meldung.setPerson(gefundenePerson);

        Unternehmen gefundenesUnternehmen = unternehmenRepository.findByName(meldung.getUnternehmen().getName());
        meldung.setUnternehmen(gefundenesUnternehmen);

        Job gefundenerJob = jobRepository.findByTitel(meldung.getJob().getTitel());
        meldung.setJob(gefundenerJob);

        return meldungRepository.save(meldung);
    }


    public List<Meldung> getAllMeldungen() {
        return meldungRepository.findAllMeldungen();
    }


    public List<Meldung> getMeldungenByIds(List<Integer> ids) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Meldung> cq = cb.createQuery(Meldung.class);
        Root<Meldung> meldung = cq.from(Meldung.class);

        cq.select(meldung).where(meldung.get("id_Meldung").in(ids));
        return entityManager.createQuery(cq).getResultList();
    }


}
