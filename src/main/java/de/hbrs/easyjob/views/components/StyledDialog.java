package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;

@StyleSheet("DialogLayout.css")
public class StyledDialog extends Dialog {
    public StyledDialog(String title, String text, Button... buttons) {
        super();
        this.setHeaderTitle(title);
        this.add(text);
        for (Button button : buttons) {
            button.addClickListener(e -> this.close());
            this.getFooter().add(button);
        }
    }
}
