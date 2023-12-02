package de.hbrs.easyjob.views.student;


import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IconFactory;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;


@Route("m")
public class StudentenLayout extends AppLayout {

    public StudentenLayout() {


        UI.getCurrent().getPage().addStyleSheet("StudentenLayout.css");


        Tabs tabs = getTabs();

        //addToDrawer(tabs);
        addToNavbar(true, tabs);


    }


    private Tabs getTabs() {

        Tabs tabs = new Tabs();
        tabs.add(createTab(FontAwesome.Solid.USER, "meinProfil", studierendProfil.class),
                createTab(FontAwesome.Solid.BRIEFCASE, "Orders", Jobs.class),
                createTab(FontAwesome.Solid.USERS, "Unternehmen", SwapStudent.class),
                createTab(FontAwesome.Solid.BELL, "Benachrichtigungen", Benachrichtigungen.class),
                createTab(FontAwesome.Solid.ENVELOPE, "Nachrichten", Nachrichten.class));
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL,
                TabsVariant.LUMO_EQUAL_WIDTH_TABS);

        return tabs;
    }

    private Tab createTab(IconFactory viewIcon, String viewName, Class<? extends com.vaadin.flow.component.Component> navigationTarget) {
        Icon icon = viewIcon.create();
        icon.addClassName("icon");
        RouterLink link = new RouterLink();
        link.add(icon);
        link.setRoute(navigationTarget);
        link.setTabIndex(-1);

        return new Tab(link);
    }


}




