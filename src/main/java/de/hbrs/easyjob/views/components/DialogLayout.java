package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

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

    public void simpleDialog(String header, Button button, String actionRoute){
        button.addClickListener(e -> {button.getUI().ifPresent(ui -> ui.navigate(actionRoute));
            dialog.close();});
        dialog.setHeaderTitle(header);
        dialog.getFooter().add(button);
    }

    public void openDialogOverlay(){dialog.open();}

    public void closeDialogOverlay(){dialog.close();}
}
