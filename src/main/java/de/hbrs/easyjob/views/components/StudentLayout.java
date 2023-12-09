package de.hbrs.easyjob.views.components;


import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IconFactory;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.views.student.*;


public class StudentLayout extends AppLayout {

    public StudentLayout() {


        UI.getCurrent().getPage().addStyleSheet("StudentLayout.css");


        Tabs tabs = getTabs();

        //addToDrawer(tabs);
        addToNavbar(true, tabs);


    }


    private Tabs getTabs() {

        Tabs tabs = new Tabs();
        tabs.add(createTab(FontAwesome.Solid.USER, "meinProfil", StudentProfilView.class),
                createTab(FontAwesome.Solid.BRIEFCASE, "Orders", JobsUebersichtView.class),
                createTab(FontAwesome.Solid.USERS, "Unternehmen", SwapStudentView.class),
                createTab(FontAwesome.Solid.BELL, "BenachrichtigungenView", BenachrichtigungenView.class),
                createTab(FontAwesome.Solid.ENVELOPE, "ChatsView", ChatsView.class));
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




