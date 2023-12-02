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
@Table(name = "Berufs_Felder", schema = "EasyJob")
public class BerufsFelder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Berufs_Felder")
    private Integer id_BerufsFelder;

    @Column(name= "name")
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BerufsFelder that = (BerufsFelder) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
