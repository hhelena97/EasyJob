package de.hbrs.easyjob.views.components;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.icon.IconFactory;
import de.hbrs.easyjob.views.unternehmen.*;

/**
 * Layout für die Unternehmensansicht
 */
@StyleSheet("UnternehmenLayout.css")
public class UnternehmenLayout extends NavigationLayout {
    public UnternehmenLayout() {
        super(new IconFactory[]{
                        FontAwesome.Solid.USER,
                        FontAwesome.Solid.USER_GRADUATE,
                        FontAwesome.Solid.ENVELOPE
                },
                new Class[]{
                        UnternehmenspersonProfilView.class,
                        MitarbeiterFindenView.class,
                        ChatsView.class
                }
        );
    }
}
