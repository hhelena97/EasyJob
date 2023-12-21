package de.hbrs.easyjob.controllers;

import com.vaadin.flow.component.UI;
import de.hbrs.easyjob.entities.Person;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonProfilController {

/*
@Autowired
    PersonRepository personRepository;

public PersonProfilController(PersonRepository personRepository){
        this.personRepository = personRepository;
    }
 */



    public String getVorname(){
        Person person;
        person = (Person) UI.getCurrent().getSession().getAttribute("current_User");
        return person.getVorname();
    }
    public String getNachname(){
        Person person;
        person = (Person) UI.getCurrent().getSession().getAttribute("current_User");
        return person.getNachname();
    }
    public String getEmail(){
        Person person;
        person = (Person) UI.getCurrent().getSession().getAttribute("current_User");
        return person.getEmail();
    }
    public String getTelefon(){
        Person person;
        person = (Person) UI.getCurrent().getSession().getAttribute("current_User");
        return person.getTelefon();
    }
    public String getFoto(){
        Person person;
        person = (Person) UI.getCurrent().getSession().getAttribute("current_User");
        return person.getFoto();
    }

    public void setVorname(String vorname){
        Person person;
        person = (Person) UI.getCurrent().getSession().getAttribute("current_User");
        person.setVorname(vorname);
    }

    public void setNachname(String nachname){
        Person person;
        person = (Person) UI.getCurrent().getSession().getAttribute("current_User");
        person.setNachname(nachname);
    }

    public void setTelefon(String telefon){
        Person person;
        person = (Person) UI.getCurrent().getSession().getAttribute("current_User");
        person.setTelefon(telefon);
    }

    public void setEmail(String email){
        Person person;
        person = (Person) UI.getCurrent().getSession().getAttribute("current_User");
        person.setEmail(email);
    }
}
