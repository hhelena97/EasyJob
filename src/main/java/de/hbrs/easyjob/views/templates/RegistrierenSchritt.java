package de.hbrs.easyjob.views.templates;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class RegistrierenSchritt extends VerticalLayout {
    public abstract boolean checkRequirementsAndSave();
    public abstract void insertContent();
}
