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
    @Column(name = "ID_ORT")
    private Integer id_Ort;

    @Column(name = "PLZ")
    private String PLZ;

    @Column(name = "ORT")
    private String ort;

    public Ort(String plz, String ort) {
        this.ort=ort;
        this.PLZ=plz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ort that = (Ort) o;
        return Objects.equals(id_Ort, that.id_Ort) && Objects.equals(PLZ, that.PLZ) && Objects.equals(ort, that.ort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ort, PLZ);
    }
}
