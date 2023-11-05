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

    @Column(name = "Beschreibung")
    private String beschreibung;

    @Lob
    @Column(name = "Logo")
    private byte[] logo;

    @Column(name = "bezahlt")
    private boolean bezahlt;

    @OneToOne
    @JoinColumn(name = "FK_Person")
    private Unternehmensperson unternehmensperson;

    @ManyToOne
    @JoinColumn(name = "FK_Bezahlung")
    private Bezahlung bezahlung;

    @ManyToMany
    @JoinTable(
            name = "Standort",
            joinColumns = @JoinColumn(name = "id_Unternehmen"),
            inverseJoinColumns = @JoinColumn(name = "id_Ort")
    )
    private Set<Ort> standorte;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unternehmen unternehmen = (Unternehmen) o;
        return Objects.equals(id_Unternehmen, unternehmen.id_Unternehmen) &&
                Objects.equals(name, unternehmen.name) &&
                Objects.equals(kontaktdaten, unternehmen.kontaktdaten) &&
                Objects.equals(beschreibung, unternehmen.beschreibung) &&
                Arrays.equals(logo, unternehmen.logo) &&
                Objects.equals(standorte, unternehmen.standorte) &&
                Objects.equals(bezahlt, unternehmen.bezahlt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_Unternehmen, name, kontaktdaten, beschreibung, Arrays.hashCode(logo), standorte, bezahlt);
    }
}
