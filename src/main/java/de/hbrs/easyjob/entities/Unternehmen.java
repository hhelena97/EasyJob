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

    @Column(name = "Name")
    private String name;

    @Column(name = "Kontaktdaten")
    private String kontaktdaten;

    @Column(name = "Beschreibung", length = 800)
    private String beschreibung;


    @Column(name = "Logo")
    private String logo;

    @Column(name = "Aktiv")
    private boolean aktiv;

    @Column(name = "bezahlt")
    private boolean bezahlt;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_Person")
    private Unternehmensperson unternehmensperson;

    @ManyToOne
    @JoinColumn(name = "FK_Bezahlung")
    private Bezahlung bezahlung;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unternehmen unternehmen = (Unternehmen) o;
        return Objects.equals(id_Unternehmen, unternehmen.id_Unternehmen) &&
                Objects.equals(name, unternehmen.name) &&
                Objects.equals(kontaktdaten, unternehmen.kontaktdaten) &&
                Objects.equals(beschreibung, unternehmen.beschreibung) &&
                Objects.equals(logo, unternehmen.logo) &&
                Objects.equals(standorte, unternehmen.standorte) &&
                Objects.equals(bezahlt, unternehmen.bezahlt) &&
                Objects.equals(aktiv, unternehmen.aktiv) &&
                Objects.equals(branchen, unternehmen.branchen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_Unternehmen, name, kontaktdaten, beschreibung, logo, standorte, bezahlt, aktiv ,branchen);
    }
}
