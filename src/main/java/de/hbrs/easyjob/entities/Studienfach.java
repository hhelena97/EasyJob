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
@Table(name = "Studienfach", schema = "EasyJob")
public class Studienfach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Studienfach")
    private Integer id_Studienfach;

    @Column(name = "Fach")
    private String fach;

    @Column(name = "Abschluss")
    private String abschluss;

    public Studienfach(String fach, String abschluss) {
        this.abschluss=abschluss;
        this.fach=fach;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Studienfach fach = (Studienfach) o;
        return Objects.equals(fach, fach.fach) &&
                Objects.equals(abschluss, fach.abschluss);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fach, abschluss);
    }

}
