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
@Table(name = "Ort", schema = "EasyJob")
public class Ort {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Ort")
    private Integer id_Ort;

    @Column(name = "PLZ")
    private String PLZ;

    @Column(name = "Ort")
    private String ort;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ort that = (Ort) o;
        return Objects.equals(PLZ, that.PLZ) &&
                Objects.equals(ort, that.ort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ort, PLZ);
    }
}
