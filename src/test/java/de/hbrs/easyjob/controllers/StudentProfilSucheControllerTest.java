package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.repositories.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StudentProfilSucheControllerTest {
    // Repositories
    @Autowired
    private StudentRepository studentRepository;

    // Controllers
    @Autowired
    private StudentProfilSucheController studentProfilSucheController;

    @Test
    @DisplayName("Suche Student mit Volltext")
    void searchStudentVolltext() throws Exception {
        // Arrange
        Optional<Student>[] students_op = new Optional[4];
        Student[] students = new Student[4];
        students_op[0] = studentRepository.findById(23);
        students_op[1] = studentRepository.findById(2);
        students_op[2] = studentRepository.findById(25);
        students_op[3] = studentRepository.findById(35);

        int i = 0;
        for (Optional<Student> student : students_op) {
            if (student.isEmpty()) {
                throw new Exception("Kein Objekt gefunden in der Datenbank!");
            } else {
                students[i] = student.get();
                i++;
            }
        }

        List<Student> expected = List.of(students);

        // Act
        List<Student> actual = studentProfilSucheController.searchStudent("Informatik");

        // Assert
        for (i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getId_Person(), actual.get(i).getId_Person());
        }
    }

    @Test
    @DisplayName("Suche Student mit Teilwort")
    void searchStudentTeilwort() throws Exception {
        // ************* Arrange *************
        Optional<Student>[] p = new Optional[9];
        Student[] students = new Student[9];
        p[2] = studentRepository.findById(23);
        p[4] = studentRepository.findById(26);
        p[6] = studentRepository.findById(28);
        p[1] = studentRepository.findById(2);
        p[3] = studentRepository.findById(25);
        p[0] = studentRepository.findById(1);
        p[5] = studentRepository.findById(27);
        p[7] = studentRepository.findById(30);
        p[8] = studentRepository.findById(35);

        for (int i = 0; i < p.length; i++) {
            Optional<Student> stu = p[i];
            if (stu.isEmpty()) {
                throw new Exception("Kein Objekt in der Datenbank gefunden!");
            } else {
                students[i] = stu.get();
            }
        }
        List<Student> expected = List.of(students);

        // *************** Act ***************
        List<Student> actual = studentProfilSucheController.searchStudent("Bon");

        // ************* Assert **************
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getId_Person(), actual.get(i).getId_Person());
        }
    }
}