package de.hbrs.easyjob.views.allgemein;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.views.components.ZurueckButtonText;


@Route("abg")
@PageTitle("Allgemeine Geschäftsbedingungen")
@StyleSheet("Registrieren.css")
public class AGBView extends VerticalLayout {

    protected VerticalLayout frame = new VerticalLayout();


    public AGBView() {
        frame.setClassName("container");

        ZurueckButtonText back = new ZurueckButtonText();

        back.addClickListener(e -> {back.getUI().ifPresent(ui -> ui.navigate("registrieren"));});

        Label agb = new Label("Allgemeine Geschäftsbedingungen (AGB):");
        agb.addClassName("agb");

        Label text = new Label(setAGB());

        frame.add(back, agb, text);
        add(frame);
    }


    // TODO: Formatierung fixen (Siehe com.vaadin.flow.component.OrderedList & com.vaadin.flow.component.Paragraph)
    public String setAGB() {
        return "1. Nutzungsbedingungen:  \n" +
                "\t1.1 Die Nutzung der Jobsuche-Plattform “EasyJob” \n" +
                "      (nachfolgend \"Plattform\") unterliegt diesen AGB.  \n" +
                "\t1.2 Die Plattform wird von der Hochschule \n" +
                "      Bonn-Rhein-Sieg bereitgestellt und dient der \n" +
                "      Vermittlung von Stellenangeboten zwischen \n" +
                "      Studierenden und Unternehmen.  \n" +
                "  \n" +
                "2. Accounterstellung und -verwaltung:  \n" +
                "\t2.1 Studierende und Unternehmenspersonen \n" +
                "       müssen sich registrieren, um die Plattform zu \n" +
                "       nutzen. Die Registrierung erfordert die Bereit-\n" +
                "       stellung genauer und aktueller Informationen.  \n" +
                "\t2.2 Jeder Nutzer ist für die Sicherheit seines Kontos\n" +
                "        verantwortlich und darf seine Zugangsdaten\n" +
                "        nicht an Dritte weitergeben.  \n" +
                "  \n" +
                "3. Stellenanzeigen:  \n" +
                "\t3.1 Unternehmen sind für den Inhalt ihrer Stellen-\n" +
                "       anzeigen verantwortlich und dürfen keine\n" +
                "       irreführenden oder unseriösen Informationen \n" +
                "       bereitstellen.  \n" +
                "\t3.2 Die Hochschule behält sich das Recht vor, \n" +
                "       Stellenanzeigen zu entfernen oder Unternehmen\n" +
                "       zu blacklisten, die gegen diese AGB verstoßen.  \n" +
                "  \n" +
                "4. Kommunikation:  \n" +
                "\t4.1 Studierende und Unternehmenspersonen\n" +
                "       können über die Plattform miteinander \n" +
                "       kommunizieren. Jegliche Kommunikation sollte \n" +
                "       respektvoll und berufsbezogen sein.  \n" +
                "\t4.2 Die Hochschule behält sich das Recht vor, \n" +
                "        bei missbräuchlicher Kommunikation Maß-\n" +
                "        nahmen zu ergreifen, einschließlich der \n" +
                "        Sperrung von Konten.  \n" +
                "  \n" +
                "5. Datenschutz:  \n" +
                "\t5.1 Die Verarbeitung personenbezogener Daten \n" +
                "       erfolgt gemäß den Bestimmungen der \n" +
                "       Datenschutzerklärung.  \n" +
                "\t5.2 Die Hochschule behält sich das Recht vor, \n" +
                "        anonymisierte Daten zu Analysezwecken zu \n" +
                "        verwenden, um die Plattform zu verbessern.  \n" +
                "  \n" +
                "6. Haftungsausschluss:  \n" +
                "\t6.1 Die Hochschule übernimmt keine Verantwortung\n" +
                "       für die Richtigkeit der Stellenanzeigen oder die \n" +
                "       Qualität der Kommunikation zwischen \n" +
                "       Studierenden und Unternehmenspersonen.  \n" +
                "\t6.2 Die Plattform wird \"wie sie ist\" bereitgestellt, \n" +
                "       ohne jegliche Gewährleistung.  \n" +
                "  \n" +
                "7. Änderungen der AGB:  \n" +
                "\t7.1 Die Hochschule behält sich das Recht vor, \n" +
                "      diese AGB jederzeit zu ändern. Nutzer werden \n" +
                "      über Änderungen informiert, und die Nutzung der\n" +
                "      Plattform nach solchen Änderungen gilt als\n" +
                "      Zustimmung zu den aktualisierten AGB.  \n" +
                "  \n" +
                "Datenschutzerklärung:  \n" +
                "  \n" +
                "1. Erhebung und Verarbeitung von personen-\n" +
                "    bezogenen Daten:  \n" +
                "\t1.1 Die Plattform erhebt und verarbeitet personen-\n" +
                "      bezogene Daten von Studierenden\n" +
                "      und Unternehmenspersonen gemäß den\n" +
                "      geltenden Datenschutzgesetzen.  \n" +
                "\t1.2 Zu den verarbeiteten Daten gehören Name, \n" +
                "       E-Mail-Adresse, Profilinformationen und \n" +
                "       Kommunikationsverlauf.  \n" +
                "  \n" +
                "2. Nutzung von Daten:  \n" +
                "\t2.1 Die erhobenen Daten dienen der Bereitstellung\n" +
                "       und Verbesserung der Plattform sowie der\n" +
                "       Kommunikation zwischen Studierenden und\n" +
                "       Unternehmen.  \n" +
                "\t2.2 Anonymisierte Daten können zu Analyse-\n" +
                "       zwecken verwendet werden, um die Plattform\n" +
                "       effizienter zu gestalten.  \n" +
                "  \n" +
                "3. Profildeaktivierung:  \n" +
                "\t3.1 Nutzer haben das Recht, ihr Profil zu\n" +
                "       deaktivieren.  \n" +
                "\t3.2 Die Deaktivierung führt dazu, dass das Profil\n" +
                "        nicht mehr öffentlich sichtbar ist.  \n" +
                "\t3.3 Nutzer haben außerdem das Recht, ihr deakti-\n" +
                "        viertes Profil zu reaktivieren, um die Plattform\n" +
                "        weiter zu nutzen.\n" +
                "\t3.4 Die personenbezogenen Daten werden für einen\n" +
                "        bestimmten Zeitraum aufbewahrt, auch nach\n" +
                "        der Deaktivierung, um eine spätere Reakti-\n" +
                "        vierung zu ermöglichen.\n" +
                "\t3.5 Nach Ablauf des Aufbewahrungszeitraums\n" +
                "        können die Daten gelöscht werden, es sei denn,\n" +
                "        es besteht eine gesetzliche Verpflichtung zur\n" +
                "        weiteren Aufbewahrung.\n" +
                "  \n" +
                "4. Sicherheit:  \n" +
                "\t4.1 Die Plattform ergreift angemessene Sicherheits-\n" +
                "       maßnahmen, um die Integrität und Sicherheit\n" +
                "       der Daten zu gewährleisten.  \n" +
                "\t4.2 Nutzer werden darauf hingewiesen, ihre\n" +
                "        Zugangsdaten sicher zu verwahren.  \n" +
                "  \n" +
                "5. Weitergabe von Daten:  \n" +
                "\t5.1 Die Plattform gibt personenbezogene Daten\n" +
                "       nicht an Dritte weiter, es sei denn, dies ist\n" +
                "       gesetzlich vorgeschrieben oder zur Erfüllung der\n" +
                "       Plattformfunktionen erforderlich.  \n" +
                "\t5.2 Unternehmen können nur auf diejenigen Infor-\n" +
                "       mationen von Studierenden zugreifen, die für die\n" +
                "       Stellenvermittlung relevant sind.  \n" +
                "  \n" +
                "6. Kontakt:  \n" +
                "\t6.1 Bei Fragen zur Datenschutzerklärung können\n" +
                "       Nutzer die Hochschule über die auf der Plattform\n" +
                "       angegebenen Kontaktdaten erreichen.  \n" +
                "\t6.2 Die Datenschutzerklärung kann jederzeit online\n" +
                "       eingesehen werden.\n" +
                "\n" +
                "\n" +
                "Diese Allgemeinen Geschäftsbedingungen wurden\n" +
                "zuletzt aktualisiert am 20.11.2023.\n" +
                "\n" +
                "Die Datenschutzerklärung wurde zuletzt aktualisiert \n" +
                "am 20.11.2023.";
    }



}
