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
@Table(name = "Chat", schema = "EasyJob")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Chat")
    private Integer id_Chat;

    @ManyToOne
    @JoinColumn(name = "FK_Job")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "FK_Person")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "FK_Unternehmensperson")
    private Unternehmensperson unternehmensperson;

    @Column(name = "Aktiv")
    private boolean aktiv;
    @Column(name = "TopicId")
    private String topicId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return Objects.equals(id_Chat, chat.id_Chat) &&
                Objects.equals(job, chat.job) &&
                Objects.equals(student, chat.student) &&
                Objects.equals(unternehmensperson, chat.unternehmensperson) &&
                Objects.equals(aktiv, chat.aktiv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_Chat, job, student, unternehmensperson,aktiv);
    }
}
