package de.hbrs.easyjob.entities;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Table(name = "Unternehmen", schema = "EasyJob")
public class Unternehmen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Unternehmen")
    private Integer id_Unternehmen;

    //der "Manager", der das Unternehmen anlegt
    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_Person")
    private Unternehmensperson unternehmensperson;

    @Column(name = "Name")
    private String name;

    @Column(name = "Logo")
    private String logo;

    @Column(name = "Bild")
    private String bild;

    //Adresse des Stammsitzes oder der Personalabteilung
    @Column(name = "Kontaktdaten")
    private String kontaktdaten;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Standort",
            joinColumns = @JoinColumn(name = "id_Unternehmen"),
            inverseJoinColumns = @JoinColumn(name = "id_Ort")
    )
    private Set<Ort> standorte;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Unternehmen_haben_Branchen",
            joinColumns = @JoinColumn(name = "id_Unternehmen"),
            inverseJoinColumns = @JoinColumn(name = "ID_Branche")
    )
    private Set<Branche> branchen;

    @Column(name = "Beschreibung", length = 800)
    private String beschreibung;

    @Column(name = "Aktiv")
    private boolean aktiv;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unternehmen unternehmen = (Unternehmen) o;
        return Objects.equals(id_Unternehmen, unternehmen.id_Unternehmen) &&
                Objects.equals(unternehmensperson, unternehmen.unternehmensperson) &&
                Objects.equals(name, unternehmen.name) &&
                Objects.equals(logo, unternehmen.logo) &&
                Objects.equals(bild, unternehmen.bild) &&
                Objects.equals(kontaktdaten, unternehmen.kontaktdaten) &&
                Objects.equals(standorte, unternehmen.standorte) &&
                Objects.equals(branchen, unternehmen.branchen) &&
                Objects.equals(beschreibung, unternehmen.beschreibung) &&
                Objects.equals(aktiv, unternehmen.aktiv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_Unternehmen, unternehmensperson, name, logo, bild, kontaktdaten, standorte,
                branchen, beschreibung);
    }
}
