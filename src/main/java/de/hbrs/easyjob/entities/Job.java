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

    //die Person, die den Job anlegt
    @ManyToOne
    @JoinColumn(name = "FK_Person")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "FK_Unternehmen")
    private Unternehmen unternehmen;

// Job hat:
    @Column(name = "Titel", length = 255)
    private String titel;

    @ManyToOne
    @JoinColumn(name = "FK_Job_Kategorie")
    private JobKategorie jobKategorie;

    @ManyToOne
    @JoinColumn(name = "FK_BerufsFelder")
    private Ort berufsfeld;

    @Column(name="Eintritt")
    private Date eintritt;

    @ManyToOne
    @JoinColumn(name = "FK_Ort")
    private Ort ort;

    @Column(name= "Home_Office")
    private boolean homeOffice;

    @Column(name = "Bild")
    private String bild;

    @Column(name = "Freitext", length = 4000)
    private String freitext;

    @Column(name = "Erstellt_am")
    private Date erstellt_am;

    @Column(name = "Aktiv")
    private Boolean aktiv;

//Job sucht:

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Job_sucht_Studienfach",
            joinColumns = @JoinColumn(name = "id_Job"),
            inverseJoinColumns = @JoinColumn(name = "id_Studienfach")
    )
    private Set<Studienfach> studienfacher;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Job_sucht_Faehigkeit",
            joinColumns = @JoinColumn(name = "id_Job"),
            inverseJoinColumns = @JoinColumn(name = "id_Faehigkeit")
    )
    private Set<Studienfach> faehigkeiten;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return Objects.equals(id_Job, job.id_Job) &&
                Objects.equals(person, job.person) &&
                Objects.equals(unternehmen, job.unternehmen) &&
                Objects.equals(titel, job.titel) &&
                Objects.equals(jobKategorie, job.jobKategorie)&&
                Objects.equals(berufsfeld, job.berufsfeld) &&
                Objects.equals(eintritt, job.eintritt) &&
                Objects.equals(ort, job.ort) &&
                Objects.equals(homeOffice, job.homeOffice) &&
                Objects.equals(bild, job.bild) &&
                Objects.equals(freitext, job.freitext) &&
                Objects.equals(erstellt_am, job.erstellt_am) &&
                Objects.equals(aktiv, job.aktiv) &&
                Objects.equals(studienfacher, job.studienfacher) &&
                Objects.equals(faehigkeiten, job.faehigkeiten);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_Job, person, unternehmen, titel, jobKategorie, berufsfeld, eintritt,
                ort, homeOffice, bild, freitext, erstellt_am, aktiv, studienfacher, faehigkeiten);
    }
}
