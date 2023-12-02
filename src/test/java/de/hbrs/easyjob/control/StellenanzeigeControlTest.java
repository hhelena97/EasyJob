package de.hbrs.easyjob.control;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repository.*;
import de.hbrs.easyjob.service.JobService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class StellenanzeigeControlTest {
    // Repositories
    private final static JobRepository jobRepository = mock(JobRepository.class);
    private final static StudienfachRepository studienfachRepository = mock(StudienfachRepository.class);
    private final static JobKategorieRepository jobKategorieRepository = mock(JobKategorieRepository.class);
    private final static PersonRepository personRepository = mock(PersonRepository.class);
    private final static UnternehmenRepository unternehmenRepository = mock(UnternehmenRepository.class);
    private final static OrtRepository ortRepository = mock(OrtRepository.class);

    // Services
    @InjectMocks
    private final static JobService jobService = mock(JobService.class);

    // Controllers
    private static final StellenanzeigeControl stellenanzeigeControl = new StellenanzeigeControl(jobService);

    // Entities
    private static JobKategorie jobKategorie;
    private static Ort ort;
    private static String titel;
    private static String freitext;
    private static Studienfach studienfach;
    private static Unternehmensperson unternehmensperson;
    private static Unternehmen unternehmen;

    @BeforeAll
    static void setUp() {
        // Setup entities
        jobKategorie = new JobKategorie();
        jobKategorie.setKategorie("Werkstudent");

        ort = new Ort();
        ort.setOrt("Bonn");
        ort.setPLZ("53111");

        titel = "Werkstudent (m/w/d) Softwareentwicklung";
        freitext = "Wir suchen einen Werkstudenten (m/w/d) im Bereich Softwareentwicklung.";

        studienfach = new Studienfach();
        studienfach.setFach("Informatik");
        studienfach.setAbschluss("Bachelor");

        unternehmensperson = new Unternehmensperson();
        unternehmensperson.setVorname("Max");
        unternehmensperson.setNachname("Mustermann");
        unternehmensperson.setEmail("max.mustermann@easyqube.de");

        unternehmen = new Unternehmen();
        unternehmen.setName("EasyQube GmbH");
        unternehmen.setUnternehmensperson(unternehmensperson);

        Job job = new Job();

        job.setTitel(titel);
        job.setFreitext(freitext);
        job.setErstellt_am(new Date());
        job.setEintritt(Date.from(new Date().toInstant().plus(java.time.Duration.ofDays(30))));
        job.setUnternehmen(unternehmen);
        job.setPerson(unternehmensperson);
        job.setOrt(ort);
        job.setJobKategorie(jobKategorie);
        job.setStudienfacher(Set.of(studienfach));

        // Arrange mocks
        when(ortRepository.findByPLZAndOrt(anyString(), anyString())).thenReturn(ort);
        when(unternehmenRepository.findByName(anyString())).thenReturn(unternehmen);
        when(personRepository.findByEmail(anyString())).thenReturn(unternehmensperson);
        when(jobKategorieRepository.findByKategorie(anyString())).thenReturn(jobKategorie);
        when(studienfachRepository.findByFachAndAbschluss(anyString(), anyString())).thenReturn(studienfach);
        when(jobRepository.save(job)).thenReturn(job);
        when(jobService.saveJob(any(Job.class))).thenReturn(job);

        Job job2 = new Job();
        job2.setUnternehmen(unternehmen);
        job2.setTitel("Werkstudent (m/w/d) Java-Entwicklung");
        job2.setFreitext("Wir suchen einen Werkstudenten (m/w/d) im Bereich Java-Entwicklung.");
        job2.setErstellt_am(new Date());
        job2.setEintritt(Date.from(new Date().toInstant().plus(java.time.Duration.ofDays(60))));
        job2.setOrt(ort);
        job2.setJobKategorie(jobKategorie);
        job2.setPerson(unternehmensperson);
        job2.setStudienfacher(Set.of(studienfach));
        when(jobRepository.findAllByUnternehmenId(any(Integer.class))).thenReturn(List.of(job, job2));
        when(jobService.findAllByUnternehmenId(any(Integer.class))).thenReturn(List.of(job, job2));
    }

    @Test
    @DisplayName("Gültige Stellenanzeige erstellen")
    void stelleAnzeigeErstellen() {
        // Exercise
        Job result = stellenanzeigeControl.stellenanzeigeErstellen(titel, freitext, new java.util.Date(), unternehmen, unternehmensperson, ort, jobKategorie, Set.of(studienfach));

        // Verify
        assertNotNull(result);
        assertEquals(titel, result.getTitel());
        assertEquals(freitext, result.getFreitext());
        assertEquals(ort.getOrt(), result.getOrt().getOrt());
        assertEquals(ort.getPLZ(), result.getOrt().getPLZ());
        assertEquals(unternehmen.getName(), result.getUnternehmen().getName());
        assertEquals(unternehmensperson.getEmail(), result.getPerson().getEmail());
        assertEquals(jobKategorie.getKategorie(), result.getJobKategorie().getKategorie());
        assertEquals(studienfach.getFach(), result.getStudienfacher().iterator().next().getFach());
        assertEquals(studienfach.getAbschluss(), result.getStudienfacher().iterator().next().getAbschluss());
    }

    @Test
    @DisplayName("Stellenanzeige mit titel == null erstellen")
    void stellenAnzeigeMitUngueltigemTitel() {
        // Exercise
        Job result = stellenanzeigeControl.stellenanzeigeErstellen(null, freitext, new java.util.Date(), unternehmen, unternehmensperson, ort, jobKategorie, Set.of(studienfach));

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("Stellenanzeige mit freitext == null erstellen")
    void stellenAnzeigeMitUngueltigemFreitext() {
        // Exercise
        Job result = stellenanzeigeControl.stellenanzeigeErstellen(titel, null, new java.util.Date(), unternehmen, unternehmensperson, ort, jobKategorie, Set.of(studienfach));

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("Stellenanzeige mit eintrittsdatum == null erstellen")
    void stellenAnzeigeMitUngueltigemEintrittsdatum() {
        // Exercise
        Job result = stellenanzeigeControl.stellenanzeigeErstellen(titel, freitext, null, unternehmen, unternehmensperson, ort, jobKategorie, Set.of(studienfach));

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("Stellenanzeige mit unternehmen == null erstellen")
    void stellenAnzeigeMitUngueltigemUnternehmen() {
        // Exercise
        Job result = stellenanzeigeControl.stellenanzeigeErstellen(titel, freitext, new java.util.Date(), null, unternehmensperson, ort, jobKategorie, Set.of(studienfach));

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("Stellenanzeige mit unternehmensperson == null erstellen")
    void stellenAnzeigeMitUngueltigerUnternehmensperson() {
        // Exercise
        Job result = stellenanzeigeControl.stellenanzeigeErstellen(titel, freitext, new java.util.Date(), unternehmen, null, ort, jobKategorie, Set.of(studienfach));

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("Stellenanzeige mit ort == null erstellen")
    void stellenAnzeigeMitUngueltigemOrt() {
        // Exercise
        Job result = stellenanzeigeControl.stellenanzeigeErstellen(titel, freitext, new java.util.Date(), unternehmen, unternehmensperson, null, jobKategorie, Set.of(studienfach));

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("Stellenanzeige mit jobKategorie == null erstellen")
    void stellenAnzeigeMitUngueltigerJobKategorie() {
        // Exercise
        Job result = stellenanzeigeControl.stellenanzeigeErstellen(titel, freitext, new java.util.Date(), unternehmen, unternehmensperson, ort, null, Set.of(studienfach));

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("Stellenanzeige mit studienfaecher == null erstellen")
    void stellenAnzeigeMitUngueltigenStudienfaechern() {
        // Exercise
        Job result = stellenanzeigeControl.stellenanzeigeErstellen(titel, freitext, new java.util.Date(), unternehmen, unternehmensperson, ort, jobKategorie, null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("Stellenanzeige mit allen Parametern == null erstellen")
    void stellenAnzeigeMitAllenUngueltigenParametern() {
        // Exercise
        Job result = stellenanzeigeControl.stellenanzeigeErstellen(null, null, null, null, null, null, null, null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("Gebe alle Stellenanzeigen von EasyQube GmbH zurück")
    void stellenanzeigenEinesUnternehmens() {
        // Exercise
        List<Job> jobs = stellenanzeigeControl.stellenanzeigenEinesUnternehmens(1);

        // Verify
        assertNotNull(jobs);
        assertEquals(2, jobs.size());
        assertEquals("Werkstudent (m/w/d) Softwareentwicklung", jobs.get(0).getTitel());
        assertEquals("Werkstudent (m/w/d) Java-Entwicklung", jobs.get(1).getTitel());
        assertEquals("EasyQube GmbH", jobs.get(0).getUnternehmen().getName());
        assertEquals("EasyQube GmbH", jobs.get(1).getUnternehmen().getName());
        assertEquals("Max", jobs.get(0).getPerson().getVorname());
        assertEquals("Max", jobs.get(1).getPerson().getVorname());
        assertEquals("Mustermann", jobs.get(0).getPerson().getNachname());
        assertEquals("Mustermann", jobs.get(1).getPerson().getNachname());
    }
}