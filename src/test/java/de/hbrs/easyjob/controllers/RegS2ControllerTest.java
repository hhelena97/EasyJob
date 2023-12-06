package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.BerufsFeldRepository;
import de.hbrs.easyjob.repositories.BrancheRepository;
import de.hbrs.easyjob.repositories.JobKategorieRepository;
import de.hbrs.easyjob.repositories.OrtRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class RegS2ControllerTest {

    // Repositories
    private final static OrtRepository ortRepository = Mockito.mock(OrtRepository.class);
    private final static BrancheRepository brancheRepository = Mockito.mock(BrancheRepository.class);
    private final static BerufsFeldRepository berufsFeldRepository = Mockito.mock(BerufsFeldRepository.class);
    private final static JobKategorieRepository jobKategorieRepository = Mockito.mock(JobKategorieRepository.class);

    // Controller
    @InjectMocks
    private RegS2Controller regS2Controller;

    // Entities
    private final static Ort ort1 = new Ort();
    private final static Ort ort2 = new Ort();
    private final static Branche branche1 = new Branche();
    private final static Branche branche2 = new Branche();
    private final static BerufsFelder bf1 = new BerufsFelder();
    private final static BerufsFelder bf2 = new BerufsFelder();
    private final static JobKategorie joKa1 = new JobKategorie();
    private final static JobKategorie joKa2 = new JobKategorie();

    /**
     * erstellt den zu testenden Controller
     */
    @BeforeEach
    void setUp() {
        regS2Controller = new RegS2Controller(jobKategorieRepository, brancheRepository, berufsFeldRepository, ortRepository);
    }

    /**
     * nullt den zu testenden Controller, sodass pro Test eine neue Instanz von dem Controller aufgerufen werden kann
     */
    @AfterEach
    void tearDown() {
        regS2Controller = null;
    }

    /**
     * testet die Methode getBerufsbezeichnung(), die alle Berufsbezeichnungen aus der Repository ausgeben soll
     */
    @Test
    void getBerufsbezeichnungTest() {
        // ************* Arrange *************
        List<JobKategorie> expected = List.of(joKa1, joKa2);

        Mockito.when(jobKategorieRepository.findAll()).thenReturn(List.of(joKa1, joKa2));

        // *************** Act ***************
        List<JobKategorie> actual = regS2Controller.getBerufsbezeichnung();

        // ************* Assert **************
        assertEquals(expected, actual);

    }

    /**
     * testet die Methode getBranche(), die alle Branchen aus der Repository ausgeben soll
     */
    @Test
    void getBrancheTest() {
        // ************* Arrange *************
        List<Branche> expected = List.of(branche1, branche2);

        Mockito.when(brancheRepository.findAll()).thenReturn(List.of(branche1, branche2));

        // *************** Act ***************
        List<Branche> actual = regS2Controller.getBranche();

        // ************* Assert **************
        assertEquals(expected, actual);
    }

    /**
     * testet die Methode getBerufsFelder(), die alle Berufsfelder aus der Repository ausgeben soll
     */
    @Test
    void getBerufsFelderTest() {
        // ************* Arrange *************
        List<BerufsFelder> expected = List.of(bf1, bf2);

        Mockito.when(berufsFeldRepository.findAll()).thenReturn(List.of(bf1, bf2));

        // *************** Act ***************
        List<BerufsFelder> actual = regS2Controller.getBerufsFelder();

        // ************* Assert **************
        assertEquals(expected, actual);
    }

    /**
     * testet die Methode getOrte(), die alle Orte aus der Repository ausgeben soll
     */
    @Test
    void getOrteTest() {
        // ************* Arrange *************
        List<Ort> expected = List.of(ort1, ort2);

        Mockito.when(ortRepository.findAll()).thenReturn(List.of(ort1, ort2));

        // *************** Act ***************
        List<Ort> actual = regS2Controller.getOrte();

        // ************* Assert **************
        assertEquals(expected, actual);
    }
}