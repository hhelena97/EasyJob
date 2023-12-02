package de.hbrs.easyjob.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
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

    @Column(name= "Home_Office")
    private boolean homeOffice;

    @ManyToOne
    @JoinColumn(name = "FK_Studienfach")
    private Studienfach studienfach;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Student_JobKategorie",
            joinColumns = @JoinColumn(name = "id_Student"),
            inverseJoinColumns = @JoinColumn(name = "id_Job_Kategorie"))
    private Set<JobKategorie> jobKategorien=  new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Student_Ort",
            joinColumns = @JoinColumn(name = "id_Student"),
            inverseJoinColumns = @JoinColumn(name = "id_Ort"))
    private Set<Ort> orte=new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Student_Branche",
            joinColumns = @JoinColumn(name = "id_Student"),
            inverseJoinColumns = @JoinColumn(name = "ID_Branche"))
    private Set<Branche> branchen=  new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Student_Berufs_Felder",
            joinColumns = @JoinColumn(name = "id_Student"),
            inverseJoinColumns = @JoinColumn(name = "ID_Berufs_Felder"))
    private Set<BerufsFelder> berufsFelder=  new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(studienfach, student.studienfach) &&
                Objects.equals(jobKategorien, student.jobKategorien) &&
                Objects.equals(orte, student.orte) &&
                Objects.equals(branchen, student.branchen) &&
                Objects.equals(berufsFelder, student.berufsFelder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studienfach, jobKategorien, orte, branchen, berufsFelder);
    }
}
