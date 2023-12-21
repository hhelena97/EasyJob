package de.hbrs.easyjob.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Table(name = "Unternehmensperson", schema = "EasyJob")
@PrimaryKeyJoinColumn(name = "id_FK_Person")
public class Unternehmensperson extends Person {


    @ManyToOne(cascade = CascadeType.ALL)
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

    public void removeUnternehmen() {
        unternehmen = null;
    }
}
