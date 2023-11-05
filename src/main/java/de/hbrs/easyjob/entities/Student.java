package de.hbrs.easyjob.entities;
import lombok.*;


import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Table(name = "Student", schema = "EasyJob")
@PrimaryKeyJoinColumn(name = "id_FK_Person")
public class Student extends Person {
    @ManyToOne
    @JoinColumn(name = "FK_Studienfach")
    private Studienfach studienfach;

    @ManyToMany
    @JoinTable(name = "Student_JobKategorie",
            joinColumns = @JoinColumn(name = "id_Student"),
            inverseJoinColumns = @JoinColumn(name = "id_Job_Kategorie"))
    private Set<JobKategorie> jobKategorien;

    @ManyToMany
    @JoinTable(name = "Student_Ort",
            joinColumns = @JoinColumn(name = "id_Student"),
            inverseJoinColumns = @JoinColumn(name = "id_Ort"))
    private Set<Ort> orte;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(studienfach, student.studienfach) &&
                Objects.equals(jobKategorien, student.jobKategorien) &&
                Objects.equals(orte, student.orte);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studienfach, jobKategorien, orte);
    }
}
