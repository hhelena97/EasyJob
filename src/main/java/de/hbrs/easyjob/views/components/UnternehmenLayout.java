package de.hbrs.easyjob.views.components;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IconFactory;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.views.unternehmen.*;

/**
 * Layout für die Unternehmensansicht
 */
public class UnternehmenLayout extends AppLayout {

    public UnternehmenLayout() {
        UI.getCurrent().getPage().addStyleSheet("UnternehmenLayout.css");

        Tabs tabs = getTabs();
        addToNavbar(true, tabs);
    }

    /**
     * @return Tabs für die Navigationsleiste der Unternehmensansicht
     */
    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.add(
                createTab(FontAwesome.Solid.USER, UnternehmenspersonProfilView.class),
                createTab(VaadinIcon.BAR_CHART, StatistikenView.class),
                createTab(FontAwesome.Solid.USERS, MitarbeiterFindenView.class),
                createTab(VaadinIcon.INBOX, BewerbungenView.class),
                createTab(FontAwesome.Solid.ENVELOPE, ChatsView.class)
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
        Tab tab = new Tab(link);
        return tab;
    }
}
