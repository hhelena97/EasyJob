package de.hbrs.easyjob.control;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repository.BerufsFeldRepository;
import de.hbrs.easyjob.repository.BrancheRepository;
import de.hbrs.easyjob.repository.JobKategorieRepository;
import de.hbrs.easyjob.repository.OrtRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ControllerRegS2Test {

    // Repositories
    private final static OrtRepository ortRepository = Mockito.mock(OrtRepository.class);
    private final static BrancheRepository brancheRepository = Mockito.mock(BrancheRepository.class);
    private final static BerufsFeldRepository berufsFeldRepository = Mockito.mock(BerufsFeldRepository.class);
    private final static JobKategorieRepository jobKategorieRepository = Mockito.mock(JobKategorieRepository.class);

    // Controller
    @InjectMocks
    private ControllerRegS2 controllerRegS2;

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
        controllerRegS2 = new ControllerRegS2(jobKategorieRepository, brancheRepository, berufsFeldRepository, ortRepository);
    }

    /**
     * nullt den zu testenden Controller, sodass pro Test eine neue Instanz von dem Controller aufgerufen werden kann
     */
    @AfterEach
    void tearDown() {
        controllerRegS2 = null;
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
        List<JobKategorie> actual = controllerRegS2.getBerufsbezeichnung();

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
        List<Branche> actual = controllerRegS2.getBranche();

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
        List<BerufsFelder> actual = controllerRegS2.getBerufsFelder();

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
        List<Ort> actual = controllerRegS2.getOrte();

        // ************* Assert **************
        assertEquals(expected, actual);
    }
}