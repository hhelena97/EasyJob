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
    public int get_idPerson() {
        return 0;
    }

    @Override
    public String getVorname() {
        return null;
    }

    @Override
    public String getNachname() {
        return null;
    }
}
