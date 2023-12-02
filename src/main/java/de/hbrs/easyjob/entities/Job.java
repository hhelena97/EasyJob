package de.hbrs.easyjob.entities;
import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Table(name = "Job", schema = "EasyJob")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Job")
    private Integer id_Job;

    @Column(name = "Titel")
    private String titel;

    @Column(name="Eintritt")
    private Date eintritt;

    @Column(name= "Home_Office")
    private boolean homeOffice;

    @Column(name = "Erstellt_am")
    private Date erstellt_am;

    @Column(name = "Freitext", length = 4000)
    private String freitext;

    @ManyToOne
    @JoinColumn(name = "FK_Job_Kategorie")
    private JobKategorie jobKategorie;

    @ManyToOne
    @JoinColumn(name = "FK_Person")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "FK_Unternehmen")
    private Unternehmen unternehmen;

    @ManyToOne
    @JoinColumn(name = "FK_Ort")
    private Ort ort;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Job_sucht_Studienfach",
            joinColumns = @JoinColumn(name = "id_Job"),
            inverseJoinColumns = @JoinColumn(name = "id_Studienfach")
    )
    private Set<Studienfach> studienfacher;

    @Transient
    @Formula("to_tsvector('german', coalesce(titel,'') || ' ' || coalesce((SELECT name FROM Unternehmen WHERE id_Unternehmen = FK_Unternehmen),'') || ' ' || coalesce(freitext,''))")
    private String tsv;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return Objects.equals(id_Job, job.id_Job) &&
                Objects.equals(titel, job.titel) &&
                Objects.equals(erstellt_am, job.erstellt_am) &&
                Objects.equals(person, job.person) &&
                Objects.equals(unternehmen, job.unternehmen) &&
                Objects.equals(ort, job.ort) &&
                Objects.equals(freitext, job.freitext);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_Job, titel, erstellt_am, person,unternehmen,ort,freitext );
    }
}
