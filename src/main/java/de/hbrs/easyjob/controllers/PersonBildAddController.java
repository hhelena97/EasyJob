package de.hbrs.easyjob.controllers;

import com.vaadin.flow.component.UI;
import de.hbrs.easyjob.entities.Person;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


@RestController
public class PersonBildAddController {
    private void setPersonPfad(Person person, String pfad){
        //speichert pfad in Datenbank
        person.setFoto(pfad);
    }

    private String createPfad(){
        //noch statischer pfad
        return "src/main/resources/userData/Image";
    }
    //GIF, JPEG und PNG gehen
    public String saveImage(BufferedImage image, String dateiname, String type){//type z.b. png
        String pfad = createPfad();

        File verzeichnis = new File(pfad);
        if (!verzeichnis.exists()) {
            verzeichnis.mkdirs(); // Erstelle das Verzeichnis, wenn es nicht existiert
        }
        try {
            if (verzeichnis.createNewFile()){
                ImageIO.write(image, type,verzeichnis);}
        } catch (IOException e) {
            e.printStackTrace();
        }
        Person person;
        person = (Person) UI.getCurrent().getSession().getAttribute("current_User");
        setPersonPfad(person,pfad);

        return pfad;
    }
    /* beispiel Aufruf
    public static void main(String[] args) {
        BufferedImageToFile bIF = new BufferedImageToFile();
        bIF.saveImage(bIF.createBI(), "testbild.png", "png");
    }

    private BufferedImage createBI() {
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(new File("img/test.jpg"));
        } catch (IOException e) {
            System.err.println("Datei nicht lesbar!");
        }
        return bi;
    }
     */

}
