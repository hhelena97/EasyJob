package de.hbrs.easyjob.views.components;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IconFactory;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.views.admin.EinstellungenStartView;
import de.hbrs.easyjob.views.admin.MeldungenListeView;
import de.hbrs.easyjob.views.admin.PersonenVerwaltenView;

@StyleSheet("AdminLayout.css")
public class AdminLayout extends AppLayout {
    public AdminLayout() {
        Tabs tabs = getTabs();
        addToNavbar(true, tabs);
    }

    /**
     * @return Tabs für die Navigationsleiste der Adminansicht
     */
    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.add(
                createTab(FontAwesome.Solid.USERS_COG, EinstellungenStartView.class),
                createTab(FontAwesome.Solid.USER_EDIT, PersonenVerwaltenView.class),
                createTab(FontAwesome.Solid.EXCLAMATION_CIRCLE, MeldungenListeView.class)
        );
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL,
                TabsVariant.LUMO_EQUAL_WIDTH_TABS);
        return tabs;
    }

    /**
     * @param viewIcon Icon für die Navigationsleiste
     * @param viewRoute Route für die View
     * @return Tab für die Navigationsleiste
     */
    private Tab createTab(IconFactory viewIcon, Class<? extends Component> viewRoute) {
        RouterLink link = new RouterLink(viewRoute);
        Icon icon = viewIcon.create();
        link.add(icon);

        link.setTabIndex(-1);

        Tab tab = new Tab(link);
        return tab;
    }
}
