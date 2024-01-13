package de.hbrs.easyjob.controllers;

import com.vaadin.flow.component.UI;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Unternehmensperson;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnternehmensperonProfilController extends PersonProfilController{
    public Unternehmen getUnternehmen(){
        Unternehmensperson unternehmensperson;
        unternehmensperson = (Unternehmensperson) UI.getCurrent().getSession().getAttribute("current_User");
        return unternehmensperson.getUnternehmen();
    }
    public String getUnternehmensName(){
        Unternehmensperson unternehmensperson;
        unternehmensperson = (Unternehmensperson) UI.getCurrent().getSession().getAttribute("current_User");
        return unternehmensperson.getUnternehmen().getName();
    }
}
