package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.*;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StudentRegistrierungsControllerTest {
    private static final StudentRepository studentRep = Mockito.mock(StudentRepository.class);
    private static final PersonRepository personRep = Mockito.mock(PersonRepository.class);
    private static final StudienfachRepository studienfachRep = Mockito.mock(StudienfachRepository.class);
    private static final JobKategorieRepository jobKategorieRep = Mockito.mock(JobKategorieRepository.class);
    private static final BrancheRepository brancheRep = Mockito.mock(BrancheRepository.class);
    private static final BerufsFeldRepository berufsFeldRep = Mockito.mock(BerufsFeldRepository.class);
    private static final OrtRepository ortRep = Mockito.mock(OrtRepository.class);

    @InjectMocks
    private static final StudentRegistrierungsController regCStudent = new StudentRegistrierungsController(studentRep, studienfachRep, jobKategorieRep, brancheRep, berufsFeldRep, ortRep);
    private static final LoginController loginC = new LoginController(personRep);

    static boolean AGB_ACCEPTED = true;
    static boolean AGB_NOTACCEPTED = false;

    static Student testStudent1 = new Student();
    static Student testStudent2 = new Student();
    static Student testStudent3 = new Student();
    static Student testStudent4 = new Student();
    static Student testStudent5 = new Student();

    static Studienfach studienfach1 = new Studienfach();
    static Studienfach studienfach2 = new Studienfach();
    static Studienfach studienfach3 = new Studienfach();
    static Studienfach studienfach4 = new Studienfach();
    static Studienfach studienfach5 = new Studienfach();

    static JobKategorie jobKategorie1 = new JobKategorie();
    static JobKategorie jobKategorie2 = new JobKategorie();
    static JobKategorie jobKategorie3 = new JobKategorie();
    static JobKategorie jobKategorie4 = new JobKategorie();
    static JobKategorie jobKategorie5 = new JobKategorie();

    static BerufsFelder berufsFeld1 = new BerufsFelder();
    static BerufsFelder berufsFeld2 = new BerufsFelder();
    static BerufsFelder berufsFeld3 = new BerufsFelder();
    static BerufsFelder berufsFeld4 = new BerufsFelder();
    static BerufsFelder berufsFeld5 = new BerufsFelder();

    static Branche branche1 = new Branche();
    static Branche branche2 = new Branche();
    static Branche branche3 = new Branche();
    static Branche branche4 = new Branche();
    static Branche branche5 = new Branche();

    static Ort ort1 = new Ort();
    static Ort ort2 = new Ort();
    static Ort ort3 = new Ort();
    static Ort ort4 = new Ort();
    static Ort ort5 = new Ort();


    @BeforeAll
    static void setUp() {

        // -------------------------------------------------------------------------------------------------------------
        // Test Studenten:

        testStudent1.setId_Person(1);
        testStudent1.setVorname("Erster");
        testStudent1.setNachname("TestStudent");
        testStudent1.setEmail("test.student-1@email.com");
        testStudent1.setPasswort("Pass-Wort-1");

        testStudent2.setId_Person(2);
        testStudent2.setVorname("Zweiter");
        testStudent2.setNachname("TestStudent");
        testStudent2.setEmail("test.student-2@email.com");
        testStudent2.setPasswort("Pass-Wort-2");

        testStudent3.setId_Person(3);
        testStudent3.setVorname("Dritter");
        testStudent3.setNachname("TestStudent");
        testStudent3.setEmail("test.student-3@email.com");
        testStudent3.setPasswort("Pass-Wort-3");

        testStudent4.setId_Person(4);
        testStudent4.setVorname("Vierter");
        testStudent4.setNachname("TestStudent");
        testStudent4.setEmail("test.student-4@email.com");
        testStudent4.setPasswort("Pass-Wort-4");

        testStudent5.setId_Person(5);
        testStudent1.setVorname("Erster");
        testStudent1.setNachname("TestStudent");
        testStudent1.setEmail("test.student-1@email.com");
        testStudent1.setPasswort("Pass-Wort-1");


        // -------------------------------------------------------------------------------------------------------------
        // Studienfächer:

        studienfach1.setFach("Informatik");
        studienfach1.setAbschluss("Bsc");

        studienfach2.setFach("Wirtschaftsinformatik");
        studienfach2.setAbschluss("Bachelor of Science");

        studienfach3.setFach("");
        studienfach3.setAbschluss("");

        studienfach4.setFach(null);
        studienfach4.setAbschluss("Master of Science");

        studienfach5.setFach(null);
        studienfach5.setAbschluss(null);


        // -------------------------------------------------------------------------------------------------------------
        // JobKategorien:

        jobKategorie1.setKategorie("Bachelorarbeit");
        jobKategorie2.setKategorie("Masterarbeit");
        jobKategorie3.setKategorie("Studentische Hilfskraft");
        jobKategorie4.setKategorie("");
        jobKategorie5.setKategorie(null);


        // -------------------------------------------------------------------------------------------------------------
        // Branchen:

        branche1.setName("Bankwesen");
        branche2.setName("Buchhaltung");
        branche3.setName("Wissenschaft und Forschung");
        branche4.setName("");
        branche5.setName(null);


        // -------------------------------------------------------------------------------------------------------------
        // BerufsFelder:

        berufsFeld1.setName("Cyber-Security");
        berufsFeld2.setName("Data");
        berufsFeld3.setName("Embedded Systems");
        berufsFeld4.setName("");
        berufsFeld5.setName(null);


        // -------------------------------------------------------------------------------------------------------------
        // Orte:

        ort1.setOrt("Bonn");
        ort1.setPLZ("12345");

        ort2.setOrt("Sankt Augustin");
        ort2.setPLZ("");

        ort3.setOrt("Köln");
        ort3.setPLZ(null);

        ort4.setOrt("");
        ort4.setPLZ("");

        ort5.setOrt(null);
        ort5.setPLZ(null);


        // -------------------------------------------------------------------------------------------------------------
        // Mockito:

        Mockito.when(personRep.findByEmail("test.student-1@email.com")).thenReturn(testStudent1);
        Mockito.when(personRep.findByEmail("test.student-2@email.com")).thenReturn(testStudent2);
        Mockito.when(personRep.findByEmail("test.student-3@email.com")).thenReturn(testStudent3);
        Mockito.when(personRep.findByEmail("test.student-4@email.com")).thenReturn(testStudent4);
        Mockito.when(personRep.findByEmail("test.student-5@email.com")).thenReturn(testStudent5);


        Mockito.when(studienfachRep.findByFachAndAbschluss("Informatik", "Bsc")).thenReturn(studienfach1);
        Mockito.when(studienfachRep.findByFachAndAbschluss("Wirtschaftsinformatik", "Bachelor of Science")).thenReturn(studienfach2);
        Mockito.when(studienfachRep.findByFachAndAbschluss("", "")).thenReturn(studienfach3);
        Mockito.when(studienfachRep.findByFachAndAbschluss(null, "Master of Science")).thenReturn(studienfach4);
        Mockito.when(studienfachRep.findByFachAndAbschluss(null, null)).thenReturn(studienfach5);

        Mockito.when(jobKategorieRep.findByKategorie("Bachelorarbeit")).thenReturn(jobKategorie1);
        Mockito.when(jobKategorieRep.findByKategorie("Masterarbeit")).thenReturn(jobKategorie2);
        Mockito.when(jobKategorieRep.findByKategorie("Studentische Hilfskraft")).thenReturn(jobKategorie3);
        Mockito.when(jobKategorieRep.findByKategorie("")).thenReturn(jobKategorie4);
        Mockito.when(jobKategorieRep.findByKategorie(null)).thenReturn(jobKategorie5);

        Mockito.when(brancheRep.findByName("Bankwesen")).thenReturn(branche1);
        Mockito.when(brancheRep.findByName("Buchhaltung")).thenReturn(branche2);
        Mockito.when(brancheRep.findByName("Wissenschaft und Forschung")).thenReturn(branche3);
        Mockito.when(brancheRep.findByName("")).thenReturn(branche4);
        Mockito.when(brancheRep.findByName(null)).thenReturn(branche5);

        Mockito.when(berufsFeldRep.findByName("Cyber-Security")).thenReturn(berufsFeld1);
        Mockito.when(berufsFeldRep.findByName("Data")).thenReturn(berufsFeld2);
        Mockito.when(berufsFeldRep.findByName("Embedded Systems")).thenReturn(berufsFeld3);
        Mockito.when(berufsFeldRep.findByName("")).thenReturn(berufsFeld4);
        Mockito.when(berufsFeldRep.findByName(null)).thenReturn(berufsFeld5);

        Mockito.when(ortRep.findByOrt("Bonn")).thenReturn(ort1);
        Mockito.when(ortRep.findByOrt("Sankt Augustin")).thenReturn(ort2);
        Mockito.when(ortRep.findByOrt("Köln")).thenReturn(ort3);
        Mockito.when(ortRep.findByOrt("")).thenReturn(ort4);
        Mockito.when(ortRep.findByOrt(null)).thenReturn(ort5);


        Mockito.when(personRep.save(Mockito.any(Person.class))).thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(studentRep.save(Mockito.any(Student.class))).thenAnswer(i -> i.getArguments()[0]);
    }

  /*
    @BeforeEach
    @DisplayName("Belege die zu testenden Attribute der Studenten wieder mit null")
    void beforeEach() {

        testStudent1.setStudienfach(null);
        testStudent1.setJobKategorien(null);
        testStudent1.setBranchen(null);
        testStudent1.setBerufsFelder(null);
        testStudent1.setOrte(null);

        testStudent2.setStudienfach(null);
        testStudent2.setJobKategorien(null);
        testStudent2.setBranchen(null);
        testStudent2.setBerufsFelder(null);
        testStudent2.setOrte(null);

        testStudent3.setStudienfach(null);
        testStudent3.setJobKategorien(null);
        testStudent3.setBranchen(null);
        testStudent3.setBerufsFelder(null);
        testStudent3.setOrte(null);

        testStudent4.setStudienfach(null);
        testStudent4.setJobKategorien(null);
        testStudent4.setBranchen(null);
        testStudent4.setBerufsFelder(null);
        testStudent4.setOrte(null);

        testStudent5.setStudienfach(null);
        testStudent5.setJobKategorien(null);
        testStudent5.setBranchen(null);
        testStudent5.setBerufsFelder(null);
        testStudent5.setOrte(null);
    }
*/


    @Test
    @DisplayName("Studentenprofil anlegen fehlgeschlagen, da invalide Eingaben")
    void createStudentProfileFailedInvalidStudent() {

        // Erstelle Sets:
        Set<Ort> ortSet2 = new HashSet<>();
        ortSet2.add(ort1);
        ortSet2.add(ort2);

        // Belege TestStudenten mit Attributen:
        testStudent1.setStudienfach(studienfach1);
        testStudent1.setJobKategorien(null);
        testStudent1.setBranchen(null);
        testStudent1.setBerufsFelder(null);
        testStudent1.setOrte(ortSet2);

        // Studentenprofil anlegen fehlgeschlagen:
        assertFalse(regCStudent.createPerson(testStudent1, AGB_NOTACCEPTED));
    }

    @Test
    @DisplayName("Studentenprofil anlegen fehlgeschlagen, da keine AGBs akzeptiert")
    void createStudentProfileFailedInvalidStudentNoAGB() {

        // Erstelle Sets:
        Set<JobKategorie> jobKategorieSet1 = new HashSet<>();
        jobKategorieSet1.add(jobKategorie1);

        Set<Branche> brancheSet1 = new HashSet<>();
        brancheSet1.add(branche1);

        Set<BerufsFelder> berufsFeldSet1 = new HashSet<>();
        berufsFeldSet1.add(berufsFeld1);

        Set<Ort> ortSet1 = new HashSet<>();
        ortSet1.add(ort1);

        // Belege TestStudenten mit Attributen:
        testStudent1.setStudienfach(studienfach1);
        testStudent1.setJobKategorien(jobKategorieSet1);
        testStudent1.setBranchen(brancheSet1);
        testStudent1.setBerufsFelder(berufsFeldSet1);
        testStudent1.setOrte(ortSet1);

        // Studentenprofil anlegen fehlgeschlagen:
        assertFalse(regCStudent.createPerson(testStudent1, AGB_NOTACCEPTED));
    }

    @Test
    @DisplayName("Studentenprofil anlegen erfolgreich, da alles valide Eingaben")
    void createStudentProfileSuccessfulAllValid() {

        // Erstelle Sets:
        Set<JobKategorie> jobKategorieSet1 = new HashSet<>();
        jobKategorieSet1.add(jobKategorie1);

        Set<Branche> brancheSet1 = new HashSet<>();
        brancheSet1.add(branche1);

        Set<BerufsFelder> berufsFeldSet1 = new HashSet<>();
        berufsFeldSet1.add(berufsFeld1);

        Set<Ort> ortSet1 = new HashSet<>();
        ortSet1.add(ort1);

        // Belege TestStudenten mit Attributen:
        testStudent1.setStudienfach(studienfach1);
        testStudent1.setJobKategorien(jobKategorieSet1);
        testStudent1.setBranchen(brancheSet1);
        testStudent1.setBerufsFelder(berufsFeldSet1);
        testStudent1.setOrte(ortSet1);

        // Studentenprofil anlegen erfolgreich:
        assertTrue(regCStudent.createPerson(testStudent1, AGB_ACCEPTED));
    }

    @Test
    @DisplayName("Studentenprofil anlegen erfolgreich, da alles valide Eingaben -> Login möglich")
    void createStudentProfileSuccessfulAllValidLoginPossible() {

        // Erstelle Sets:
        Set<JobKategorie> jobKategorieSet1 = new HashSet<>();
        jobKategorieSet1.add(jobKategorie1);

        Set<Branche> brancheSet1 = new HashSet<>();
        brancheSet1.add(branche1);

        Set<BerufsFelder> berufsFeldSet1 = new HashSet<>();
        berufsFeldSet1.add(berufsFeld1);

        Set<Ort> ortSet1 = new HashSet<>();
        ortSet1.add(ort1);

        // Belege TestStudenten mit Attributen:
        testStudent1.setStudienfach(studienfach1);
        testStudent1.setJobKategorien(jobKategorieSet1);
        testStudent1.setBranchen(brancheSet1);
        testStudent1.setBerufsFelder(berufsFeldSet1);
        testStudent1.setOrte(ortSet1);

        // Studentenprofil anlegen erfolgreich:
        assertTrue(regCStudent.createPerson(testStudent1, AGB_ACCEPTED));

        // Login möglich:
        assertTrue(loginC.authenticate(testStudent1.getEmail(), testStudent1.getPasswort()));
    }

    @Test
    @DisplayName("Teste verschiedene (falsche und richtige) Studiengänge, um die isValidStudiengang-Methode zu prüfen")
    void testDifferentStudiengang() {

        // Belege TestStudenten mit Studiengängen:
        testStudent1.setStudienfach(studienfach1);
        testStudent2.setStudienfach(studienfach2);
        testStudent3.setStudienfach(studienfach3);
        testStudent4.setStudienfach(studienfach4);
        testStudent5.setStudienfach(studienfach5);

        // Teste richtige Studiengänge:
        assertTrue(regCStudent.isValidStudiengang(testStudent1));
        assertTrue(regCStudent.isValidStudiengang(testStudent2));

        // Teste falsche Studiengänge:
        assertFalse(regCStudent.isValidStudiengang(testStudent3));
        assertFalse(regCStudent.isValidStudiengang(testStudent4));
        assertFalse(regCStudent.isValidStudiengang(testStudent5));
    }

    @Test
    @DisplayName("Teste verschiedene (falsche und richtige) JobKategorien, um die isValidJobKategorie-Methode zu prüfen")
    void testDifferentJobKategorie() {

        // Estelle JobKategorie-Sets:
        Set<JobKategorie> jobKategorieSet1 = new HashSet<>();
        jobKategorieSet1.add(jobKategorie1);

        Set<JobKategorie> jobKategorieSet2 = new HashSet<>();
        jobKategorieSet2.add(jobKategorie1);
        jobKategorieSet2.add(jobKategorie2);

        Set<JobKategorie> jobKategorieSet3 = new HashSet<>();
        jobKategorieSet3.add(jobKategorie3);
        jobKategorieSet3.add(jobKategorie4);

        Set<JobKategorie> jobKategorieSet4 = new HashSet<>();
        jobKategorieSet4.add(jobKategorie1);
        jobKategorieSet4.add(jobKategorie2);
        jobKategorieSet4.add(jobKategorie5);

        Set<JobKategorie> jobKategorieSet5 = new HashSet<>();
        jobKategorieSet5.add(jobKategorie4);
        jobKategorieSet5.add(jobKategorie5);


        // Belege TestStudenten mit JobKategorien:
        testStudent1.setJobKategorien(jobKategorieSet1);
        testStudent2.setJobKategorien(jobKategorieSet2);
        testStudent3.setJobKategorien(jobKategorieSet3);
        testStudent4.setJobKategorien(jobKategorieSet4);
        testStudent5.setJobKategorien(jobKategorieSet5);


        // Teste richtige JobKategorien:
        assertTrue(regCStudent.isValidJobKategorie(testStudent1));
        assertTrue(regCStudent.isValidJobKategorie(testStudent2));
        assertTrue(regCStudent.isValidJobKategorie(testStudent3));

        // Teste falsche JobKategorien:
        assertFalse(regCStudent.isValidJobKategorie(testStudent4));
        assertFalse(regCStudent.isValidJobKategorie(testStudent5));
    }

    @Test
    @DisplayName("Teste verschiedene (falsche und richtige) Branchen, um die isValidBranche-Methode zu prüfen")
    void testDifferentBranche() {

        // Erstelle Branchen-Sets:
        Set<Branche> brancheSet1 = new HashSet<>();
        brancheSet1.add(branche1);

        Set<Branche> brancheSet2 = new HashSet<>();
        brancheSet2.add(branche1);
        brancheSet2.add(branche2);

        Set<Branche> brancheSet3 = new HashSet<>();
        brancheSet3.add(branche3);
        brancheSet3.add(branche4);

        Set<Branche> brancheSet4 = new HashSet<>();
        brancheSet4.add(branche1);
        brancheSet4.add(branche2);
        brancheSet4.add(branche5);

        Set<Branche> brancheSet5 = new HashSet<>();
        brancheSet5.add(branche4);
        brancheSet5.add(branche5);

        // Belege TestStudenten mit Branchen:
        testStudent1.setBranchen(brancheSet1);
        testStudent2.setBranchen(brancheSet2);
        testStudent3.setBranchen(brancheSet3);
        testStudent4.setBranchen(brancheSet4);
        testStudent5.setBranchen(brancheSet5);

        // Teste richtige Branchen:
        assertTrue(regCStudent.isValidBranche(testStudent1));
        assertTrue(regCStudent.isValidBranche(testStudent2));
        assertTrue(regCStudent.isValidBranche(testStudent3));

        // Teste falsche Branchen:
        assertFalse(regCStudent.isValidBranche(testStudent4));
        assertFalse(regCStudent.isValidBranche(testStudent5));
    }

    @Test
    @DisplayName("Teste verschiedene (falsche und richtige) Berufsfelder, um die isValidBerufsFeld-Methode zu prüfen")
    void testDifferentBerufsFeld() {

        // Erstelle BerufsFeld-Sets:
        Set<BerufsFelder> berufsFeldSet1 = new HashSet<>();
        berufsFeldSet1.add(berufsFeld1);

        Set<BerufsFelder> berufsFeldSet2 = new HashSet<>();
        berufsFeldSet2.add(berufsFeld1);
        berufsFeldSet2.add(berufsFeld2);

        Set<BerufsFelder> berufsFeldSet3 = new HashSet<>();
        berufsFeldSet3.add(berufsFeld3);
        berufsFeldSet3.add(berufsFeld4);

        Set<BerufsFelder> berufsFeldSet4 = new HashSet<>();
        berufsFeldSet4.add(berufsFeld1);
        berufsFeldSet4.add(berufsFeld2);
        berufsFeldSet4.add(berufsFeld5);

        Set<BerufsFelder> berufsFeldSet5 = new HashSet<>();
        berufsFeldSet5.add(berufsFeld4);
        berufsFeldSet5.add(berufsFeld5);

        // Belege TestStudenten mit Berufsfeldern:
        testStudent1.setBerufsFelder(berufsFeldSet1);
        testStudent2.setBerufsFelder(berufsFeldSet2);
        testStudent3.setBerufsFelder(berufsFeldSet3);
        testStudent4.setBerufsFelder(berufsFeldSet4);
        testStudent5.setBerufsFelder(berufsFeldSet5);

        // Teste richtige Berufsfelder:
        assertTrue(regCStudent.isValidBerufsFeld(testStudent1));
        assertTrue(regCStudent.isValidBerufsFeld(testStudent2));
        assertTrue(regCStudent.isValidBerufsFeld(testStudent3));

        // Teste falsche Berufsfelder:
        assertFalse(regCStudent.isValidBerufsFeld(testStudent4));
        assertFalse(regCStudent.isValidBerufsFeld(testStudent5));
    }


    @Test
    @DisplayName("Teste verschiedene (falsche und richtige) Orte, um die isValidOrt-Methode zu prüfen")
    void testDifferentOrt() {

        // Erstelle Ort-Sets:
        Set<Ort> ortSet1 = new HashSet<>();
        ortSet1.add(ort1);

        Set<Ort> ortSet2 = new HashSet<>();
        ortSet2.add(ort1);
        ortSet2.add(ort2);

        Set<Ort> ortSet3 = new HashSet<>();
        ortSet3.add(ort3);
        ortSet3.add(ort4);

        Set<Ort> ortSet4 = new HashSet<>();
        ortSet4.add(ort1);
        ortSet4.add(ort2);
        ortSet4.add(ort5);

        Set<Ort> ortSet5 = new HashSet<>();
        ortSet5.add(ort4);
        ortSet5.add(ort5);

        // Belege TestStudenten mit Orten:
        testStudent1.setOrte(ortSet1);
        testStudent2.setOrte(ortSet2);
        testStudent3.setOrte(ortSet3);
        testStudent4.setOrte(ortSet4);
        testStudent5.setOrte(ortSet5);

        // Teste richtige Orte:
        assertTrue(regCStudent.isValidOrt(testStudent1));
        assertTrue(regCStudent.isValidOrt(testStudent2));
        assertTrue(regCStudent.isValidOrt(testStudent3));

        // Teste falsche Orte:
        assertFalse(regCStudent.isValidOrt(testStudent4));
        assertFalse(regCStudent.isValidOrt(testStudent5));
    }


    /*  Noch ausstehend: (Sprint 2)

        - Studienfach und andere Attribute aus dem 2. Sprint überprüfen
        - Front-End testen, sobald es da ist

     */

    @AfterEach
    @DisplayName("Belege die zu testenden Attribute der Studenten wieder mit null")
    void afterEach() {

        testStudent1.setStudienfach(null);
        testStudent1.setJobKategorien(null);
        testStudent1.setBranchen(null);
        testStudent1.setBerufsFelder(null);
        testStudent1.setOrte(null);

        testStudent2.setStudienfach(null);
        testStudent2.setJobKategorien(null);
        testStudent2.setBranchen(null);
        testStudent2.setBerufsFelder(null);
        testStudent2.setOrte(null);

        testStudent3.setStudienfach(null);
        testStudent3.setJobKategorien(null);
        testStudent3.setBranchen(null);
        testStudent3.setBerufsFelder(null);
        testStudent3.setOrte(null);

        testStudent4.setStudienfach(null);
        testStudent4.setJobKategorien(null);
        testStudent4.setBranchen(null);
        testStudent4.setBerufsFelder(null);
        testStudent4.setOrte(null);

        testStudent5.setStudienfach(null);
        testStudent5.setJobKategorien(null);
        testStudent5.setBranchen(null);
        testStudent5.setBerufsFelder(null);
        testStudent5.setOrte(null);
    }

    @AfterAll
    static void tearDown() {

        Mockito.reset(studentRep);
        Mockito.reset(personRep);
        Mockito.reset(studienfachRep);
        Mockito.reset(jobKategorieRep);
        Mockito.reset(berufsFeldRep);
        Mockito.reset(brancheRep);
        Mockito.reset(ortRep);
    }
}