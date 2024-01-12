package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;

@StyleSheet("DialogLayout.css")
public class DialogLayout {

    private Dialog dialog = new Dialog();
    public DialogLayout(boolean answerRequired){

        VerticalLayout dialogLayout = new VerticalLayout();
        dialog.add(dialogLayout);
        if(answerRequired) {dialog.setCloseOnOutsideClick(false);}
    }

    public void insertDialogContent(String header, String text, String closeTitel, String confirmTitel, String user
            , String actionRoute){

        Button close = new Button(closeTitel, e -> dialog.close());
        Button confirm = new Button(confirmTitel);
        confirm.addClassName("confirm");
        confirm.addClickListener(e -> {confirm.getUI().ifPresent(ui -> ui.navigate(actionRoute));
            dialog.close();});
        dialog.setHeaderTitle(header);
        dialog.add(text);
        dialog.getFooter().add(confirm);
        dialog.getFooter().add(close);

        if(user.equals("Student")){
            close.addClassName("close-student");
        } else if (user.equals("Unternehmen")) {
            close.addClassName("close-unternehmen");
        }
    }

    /**
     * Katharina braucht das für den Admin. Da gibt es Dialoge, in denen man ein Passwort eintragen muss usw.
     */
    public void insertContentDialogContent(String header, Div content, String closeTitel, String confirmTitel) {
        Button close = new Button(closeTitel, e -> dialog.close());
        Button confirm = new Button(confirmTitel);
        confirm.addClassName("confirm");
        confirm.addClickListener(e -> {
            dialog.close();}); //TODO: noch Info anzeigen dass es gespeichert wurde
        dialog.setHeaderTitle(header);
        dialog.add(content);
        dialog.getFooter().add(confirm);
        dialog.getFooter().add(close);
    }

    public void simpleDialog(String header, Button button, String actionRoute){
        button.addClickListener(e -> {button.getUI().ifPresent(ui -> ui.navigate(actionRoute));
            dialog.close();});
        dialog.setHeaderTitle(header);
        dialog.getFooter().add(button);
    }

    public void openDialogOverlay(){dialog.open();}

    public void closeDialogOverlay(){dialog.close();}
}
