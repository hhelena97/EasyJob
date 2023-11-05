package de.hbrs.easyjob.entities;
import lombok.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.Objects;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Table(name = "Unternehmensperson", schema = "EasyJob")
@PrimaryKeyJoinColumn(name = "id_FK_Person")
public class Unternehmensperson extends Person {

    @ManyToOne
    @JoinColumn(name = "FK_Unternehmen")
    private Unternehmen unternehmen;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unternehmensperson person = (Unternehmensperson) o;
        return Objects.equals(unternehmen, person.unternehmen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unternehmen);
    }
}
