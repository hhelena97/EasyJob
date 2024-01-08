package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IconFactory;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.RouterLink;

@StyleSheet("NavigationLayout.css")
public abstract class NavigationLayout extends AppLayout {
    private final IconFactory[] icons;
    private final Class<? extends Component>[] routes;
    protected NavigationLayout(IconFactory[] icons, Class<? extends Component>[] routes) {
        this.icons = icons;
        this.routes = routes;
        Tabs tabs = getTabs();
        addToNavbar(true, tabs);
    }

    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.addClassName("navbar-tabs");
        for (int i = 0; i < icons.length; i++) {
            tabs.add(createTab(icons[i], routes[i]));
        }
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
        return new Tab(link);
    }
}
