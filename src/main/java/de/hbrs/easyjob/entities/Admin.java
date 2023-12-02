package de.hbrs.easyjob.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Getter
@AllArgsConstructor
@Setter
@Table(name = "Admin", schema = "EasyJob")
@PrimaryKeyJoinColumn(name = "id_Person")
public class Admin extends Person {

}
