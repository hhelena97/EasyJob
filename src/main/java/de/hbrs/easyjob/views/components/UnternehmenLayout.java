package de.hbrs.easyjob.views.components;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.icon.IconFactory;
import com.vaadin.flow.component.icon.VaadinIcon;
import de.hbrs.easyjob.views.unternehmen.*;

/**
 * Layout für die Unternehmensansicht
 */
@StyleSheet("UnternehmenLayout.css")
public class UnternehmenLayout extends NavigationLayout {
    public UnternehmenLayout() {
        super(new IconFactory[]{
                        FontAwesome.Solid.USER,
                        VaadinIcon.BAR_CHART,
                        FontAwesome.Solid.USERS,
                        VaadinIcon.INBOX,
                        FontAwesome.Solid.ENVELOPE
                },
                new Class[]{
                        UnternehmenspersonProfilView.class,
                        StatistikenView.class,
                        MitarbeiterFindenView.class,
                        BewerbungenView.class,
                        ChatsView.class
                }
        );
    }
}
