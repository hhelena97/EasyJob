package de.hbrs.easyjob.views.components;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.icon.IconFactory;
import de.hbrs.easyjob.views.admin.EinstellungenStartView;
import de.hbrs.easyjob.views.admin.MeldungenListeView;
import de.hbrs.easyjob.views.admin.PersonenVerwaltenView;

@StyleSheet("AdminLayout.css")
public class AdminLayout extends NavigationLayout {
    public AdminLayout() {
        super(new IconFactory[]{
                        FontAwesome.Solid.USERS_COG, FontAwesome.Solid.USER_EDIT, FontAwesome.Solid.EXCLAMATION_CIRCLE
                },
                new Class[]{
                        EinstellungenStartView.class,
                        PersonenVerwaltenView.class,
                        MeldungenListeView.class
                });

    }
}
