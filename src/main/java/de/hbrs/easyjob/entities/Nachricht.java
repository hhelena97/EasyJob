package de.hbrs.easyjob.entities;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Table(name = "Nachricht", schema = "EasyJob")
public class Nachricht {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Nachricht")
    private Integer id_Nachricht;

    @ManyToOne
    @JoinColumn(name = "FK_Chat")
    private Chat chat;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "FK_Person")
    private Person absender;

    //Empfänger ergibt sich aus dem Chat
    @Column(name = "Zeitpunkt", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant zeitpunkt;
    @Column(name = "TopicId")
    private String topicId;

    @Column(name = "Gelesen")
    private boolean gelesen;

    @Column(name = "Textfeld", length = 1000)
    private String textfeld;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nachricht nachricht = (Nachricht) o;
        return Objects.equals(id_Nachricht, nachricht.id_Nachricht) &&
                Objects.equals(chat, nachricht.chat) &&
                Objects.equals(absender, nachricht.absender) &&
                Objects.equals(zeitpunkt, nachricht.zeitpunkt) &&
                Objects.equals(gelesen, nachricht.gelesen) &&
                Objects.equals(textfeld, nachricht.textfeld);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_Nachricht, chat, absender, zeitpunkt, gelesen, textfeld);
    }
}
