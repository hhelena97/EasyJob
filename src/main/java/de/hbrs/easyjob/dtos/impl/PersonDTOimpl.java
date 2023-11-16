package de.hbrs.easyjob.dtos.impl;

import de.hbrs.easyjob.dtos.PersonDTO;

public class PersonDTOimpl implements PersonDTO {
    private Integer id_Person;

    private String vorname;

    private String nachname;

    private String email;

    private String passwort;

    private String telefon;

    private byte[] foto;

    @Override
    public int getid_Person() {
        return this.id_Person;
    }

    @Override
    public String getVorname() {
        return this.vorname;
    }

    @Override
    public String getNachname() {
        return this.nachname;
    }
}
