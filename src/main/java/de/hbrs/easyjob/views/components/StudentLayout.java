package de.hbrs.easyjob.views.components;


import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.icon.IconFactory;
import de.hbrs.easyjob.views.student.*;

@StyleSheet("StudentLayout.css")
public class StudentLayout extends NavigationLayout {
    public StudentLayout() {
        super(new IconFactory[]{
                        FontAwesome.Solid.USER,
                        FontAwesome.Solid.BRIEFCASE,
                        FontAwesome.Solid.USERS,
                        FontAwesome.Solid.BELL,
                        FontAwesome.Solid.ENVELOPE
                },
                new Class[]{
                        StudentProfilView.class,
                        JobsUebersichtView.class,
                        SwapStudentView.class,
                        BenachrichtigungenView.class,
                        ChatsView.class
                }
        );
    }
}




