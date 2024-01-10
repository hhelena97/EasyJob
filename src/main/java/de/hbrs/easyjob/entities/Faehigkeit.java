package de.hbrs.easyjob.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "Faehigkeit", schema = "EasyJob")
public class Faehigkeit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Faehigkeit")
    private Integer id_Faehigkeit;

    @Column(name = "Bezeichnung")
    private String bezeichnung;

    @Column(name = "Kategorie")
    private String kategorie;

    public Faehigkeit(String kategorie, String bezeichnung) {
        this.bezeichnung=bezeichnung;
        this.kategorie=kategorie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faehigkeit faehigkeit = (Faehigkeit) o;
        return Objects.equals(id_Faehigkeit, faehigkeit.id_Faehigkeit) &&
                Objects.equals(bezeichnung, faehigkeit.bezeichnung) &&
                Objects.equals(kategorie, faehigkeit.kategorie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_Faehigkeit, bezeichnung, kategorie);
    }

}
