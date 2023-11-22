package de.hbrs.easyjob.entities;
import lombok.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "Person", schema = "EasyJob")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Pesron")
    private Integer id_Person;

    @Column(name = "Vorname")
    private String vorname;

    @Column(name = "Nachname")
    private String nachname;

    @Column(name = "Email")
    private String email;

    @Column(name = "Passwort")
    private String passwort;

    @Column(name = "Telefon")
    private String telefon;

    @Lob
    @Column(name = "Foto")
    private byte[] foto;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return  Objects.equals(id_Person, person.id_Person) &&
                Objects.equals(vorname, person.vorname) &&
                Objects.equals(nachname, person.nachname) &&
                Objects.equals(email, person.email) &&
                Objects.equals(passwort, person.passwort) &&
                Objects.equals(telefon, person.telefon) &&
                Arrays.equals(foto, person.foto);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id_Person, vorname, nachname, email, passwort, telefon);
        result = result + Arrays.hashCode(foto);
        return result;
    }

    public String toString(){
        return vorname + " " + nachname + ", id: " + id_Person;
    }

    public String getPasswort(){
        return this.passwort;
    }
}
