package de.hbrs.easyjob.views.unternehmen;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.services.UnternehmenService;
import de.hbrs.easyjob.views.components.FileUpload;
import de.hbrs.easyjob.views.components.UnternehmenLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;


@StyleSheet("Registrieren.css")
@StyleSheet("DialogLayout.css")
@StyleSheet("UnternehmenRegistrieren.css")
@AnonymousAllowed
@Route(value = "unternehmen/unternehmensprofilbearbeiten" , layout = UnternehmenLayout.class)
public class UnternehmensprofilBearbeiten extends VerticalLayout {

    private Unternehmensperson person ;
    Unternehmen unternehmen;

    private final transient SessionController sessionController;

    @Autowired
    private final UnternehmenService unternehmenService;


    TextArea beschreibung;
    TextField unternehmensname;
    Image logo;

    UnternehmensprofilBearbeiten(SessionController sessionController, UnternehmenService unternehmenService){
        this.sessionController = sessionController;


        UI.getCurrent().getPage().addStyleSheet("UnternehmenspersonProfilBearbeitungView.css");
        UI.getCurrent().getPage().addStyleSheet("UnternehmenspersonProfilView.css");
        UI.getCurrent().getPage().addStyleSheet("Registrieren.css");
        UI.getCurrent().getPage().addStyleSheet("UnternehmenspersonProfilView.css");
        UI.getCurrent().getPage().addStyleSheet("unternehmenProfil_Un.css");



        person = (Unternehmensperson) this.sessionController.getPerson();
        this.unternehmenService = unternehmenService;
        unternehmen = person.getUnternehmen();



        addClassName("all");
        setSizeFull();
        setPadding(false);
        setSpacing(false);


        Div bildUnternehmen = new Div();
        bildUnternehmen.addClassName("bildUnternehmen");
        bildUnternehmen.setWidth(getMaxWidth());

        HorizontalLayout unternehmenInfo = new HorizontalLayout();
        unternehmenInfo.addClassName("unternehmenInfo");
       // unternehmenInfo.setSizeFull();
        unternehmenInfo.setHeight("14%");

        unternehmenInfo.setAlignItems(Alignment.CENTER);






        unternehmensname = new TextField();
        unternehmensname.addClassName("feld");
        unternehmensname.setLabel("Offizieller Name");
        unternehmensname.setSizeFull();
        unternehmensname.setValue(unternehmen.getName());




        HorizontalLayout logoBearbeiten = new HorizontalLayout();

        Div rahmen = new Div();
        //rahmen.addClassName("logo-frame");
        Image platzhalterLogo;
        if(unternehmen.getLogo() !=null){
            platzhalterLogo = new Image(unternehmen.getLogo(), "Logo");
        }else{
            platzhalterLogo = new Image("images/blank-logo.jpeg", "Logo Platzhalter");
        }

        logo = platzhalterLogo;
        Div logoDiv = new Div(platzhalterLogo);
        platzhalterLogo.addClassName("unternehmenIcon");
        rahmen.add(logoDiv);

        Button bildBearbeiten = new Button("Logo bearbeiten", new Icon(VaadinIcon.PENCIL));
        bildBearbeiten.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        bildBearbeiten.addClassName("cancel");

        //Setup Upload
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setDropAllowed(false);
        upload.setAcceptedFileTypes("image/jpeg", "image/jpg, image/png");
        upload.setUploadButton(bildBearbeiten);
        Div uploadListe = new Div();

        //Profilbild Upload
        upload.addSucceededListener(event -> {
            InputStream fileData = buffer.getInputStream();
            String fileName = event.getFileName();

            //Custom Upload Liste
            Div fileInfo = new Div();
            fileInfo.setText(fileName);
            fileInfo.addClassName("file-info");
            Button remove = new Button(new Icon(VaadinIcon.CLOSE));
            remove.addClassName("remove-file");
            remove.addThemeVariants(ButtonVariant.LUMO_SMALL);
            HorizontalLayout dateiName = new HorizontalLayout(fileInfo, remove);
            dateiName.setAlignItems(Alignment.CENTER);

            //Profilbild entsprechend wechseln
            Image neuesLogo = FileUpload.squareImageUpload(fileData, fileName, 242);
            upload.addAllFinishedListener(e ->{
                logo = neuesLogo;
                uploadListe.add(dateiName);
                neuesLogo.addClassName("unternehmenIcon");
                logoDiv.replace(platzhalterLogo, neuesLogo);});
            bildBearbeiten.addClassName("cancel-disabled");
            remove.addClickListener(e -> {
                logo = platzhalterLogo;
                upload.clearFileList();
                logoDiv.replace(neuesLogo, platzhalterLogo);
                dateiName.removeAll();
                bildBearbeiten.addClassName("cancel");
            });
        });

        unternehmenInfo.add(rahmen,unternehmensname);
        logoBearbeiten.add(upload, uploadListe);




        VerticalLayout beschreibung_ = new VerticalLayout();
        beschreibung_.setSizeFull();

        beschreibung = new TextArea("Beschreibung");
        beschreibung.setHelperText("Maximal 400 Zeichen");
        short charLimit = 400;
        beschreibung.setMaxLength(charLimit);
        beschreibung.setValueChangeMode(ValueChangeMode.EAGER);
        beschreibung.addValueChangeListener(e -> e.getSource()
                .setHelperText(e.getValue().length() + "/" + charLimit));
        beschreibung.setValue(unternehmen.getBeschreibung());
        beschreibung.setSizeFull();
        beschreibung_.add(beschreibung);


        // Buttons
        Button next = new Button("fertig", new Icon(VaadinIcon.ARROW_RIGHT));
        next.addClassName("next");
        next.addClickListener(e -> updateInfos());

        Button back = new Button("Zurück", new Icon(VaadinIcon.ARROW_LEFT));
        back.addClassName("back");
        back.addClickListener(e -> UI.getCurrent().getPage().setLocation("/unternehmen/unternehmensprofil"));

        // Buttons Container
        HorizontalLayout actions = new HorizontalLayout(back, next);
        actions.setSpacing(true);
        actions.setJustifyContentMode(JustifyContentMode.CENTER);
        actions.setSizeFull();
        add(bildUnternehmen,unternehmenInfo,logoBearbeiten,beschreibung_,actions);
    }

    private void updateInfos(){
        unternehmen.setName(unternehmensname.getValue());
        unternehmen.setBeschreibung(beschreibung.getValue());
        unternehmen.setLogo(logo.getSrc());

        unternehmenService.saveUnternehmen(unternehmen);

        UI.getCurrent().getPage().setLocation("/unternehmen/unternehmensprofil");
    }
}
