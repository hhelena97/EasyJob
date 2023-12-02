package de.hbrs.easyjob.views.allgemein;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

@StyleSheet("AppLayouts.css")
public class ZurueckButtonText extends Button {

    public ZurueckButtonText(){
        super("zurück", new Icon(VaadinIcon.CHEVRON_LEFT));
        super.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        super.addClassName("back-button-text");
    }
}
