package de.hbrs.easyjob.entities;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Table(name = "Meldung", schema = "EasyJob")
public class Meldung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Meldung")
    private Integer id_Meldung;

    @Column(name = "Bearbeitet")
    private boolean bearbeitet;

    @ManyToOne
    @JoinColumn(name = "FK_Person")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "FK_Unternehmen")
    private Unternehmen unternehmen;

    @ManyToOne
    @JoinColumn(name = "FK_Job")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "FK_Chat")
    private Chat chat;

    //für Erweiterung um Grund
    @Column(name = "Grund", length = 1000)
    private String grund;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meldung meldung = (Meldung) o;
        return Objects.equals(id_Meldung, meldung.id_Meldung) &&
                Objects.equals(bearbeitet, meldung.bearbeitet) &&
                Objects.equals(person, meldung.person) &&
                Objects.equals(unternehmen, meldung.unternehmen) &&
                Objects.equals(job, meldung.job) &&
                Objects.equals(chat, meldung.chat) &&
                Objects.equals(grund, meldung.grund);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_Meldung, bearbeitet, person, unternehmen, job, chat, grund);
    }
}
