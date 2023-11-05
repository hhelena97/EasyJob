package de.hbrs.easyjob.entities;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@Setter
@Table(name = "Admin", schema = "EasyJob")
@PrimaryKeyJoinColumn(name = "id_Person")
public class Admin extends Person {

}
