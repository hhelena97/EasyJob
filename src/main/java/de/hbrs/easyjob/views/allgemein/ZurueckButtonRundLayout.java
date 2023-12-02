package de.hbrs.easyjob.views.allgemein;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

@StyleSheet("AppLayouts.css")
public class ZurueckButtonRundLayout extends Button {

    public ZurueckButtonRundLayout(String user){

        super(new Icon(VaadinIcon.CHEVRON_LEFT));
        super.addThemeVariants(ButtonVariant.LUMO_SMALL);
        super.addClassName("back-button-round");
        if (user.equals("Student")){super.addClassName("student");
        } else if (user.equals("Unternehmen")) {super.addClassName("unternehmen");}
    }
}
