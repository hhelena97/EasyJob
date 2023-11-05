package de.hbrs.easyjob.entities;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Table(name = "JobKategorie", schema = "EasyJob")
public class JobKategorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_JobKategorie")
    private Integer id_JobKategorie;

    @Column(name = "Kategorie")
    private String kategorie;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobKategorie that = (JobKategorie) o;
        return Objects.equals(kategorie, that.kategorie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kategorie);
    }
}
