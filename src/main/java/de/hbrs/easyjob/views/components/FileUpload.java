package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * FileUpload Hilfsklasse für den Upload von verschiedenen Dateitypen.
 * Für weitere Bearbeitung siehe Processor-Klassen.
 */

public class FileUpload {
    private static final Logger logger = LoggerFactory.getLogger(FileUpload.class);

    /**
     * imageUpload konvertiert eine hochgeladene Bilddatei zu einer Vaadin Image Komponente
     * @param fileData Imputstream von der Upload Komponente
     * @param fileName String Dateiname
     * @return Bild in Originalgröße
     */
    public static Image imageUpload(InputStream fileData, String fileName) {
        Image image = new Image();
        try {
            byte[] bytes = IOUtils.toByteArray(fileData);
            image.getElement().setAttribute("src", new StreamResource(
                    fileName, () -> new ByteArrayInputStream(bytes)));
            try (ImageInputStream in = ImageIO.createImageInputStream(
                    new ByteArrayInputStream(bytes))) {
                final Iterator<ImageReader> readers = ImageIO
                        .getImageReaders(in);
                if (readers.hasNext()) {
                    ImageReader reader = readers.next();
                    try {
                        //Set Originalgröße
                        reader.setInput(in);
                        image.setWidth(reader.getWidth(0) + "px");
                        image.setHeight(reader.getHeight(0) + "px");
                    } finally {
                        reader.dispose();
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Fehler beim Speichern des Bildes", e);
        }
        return image;
    }

    /**
     * squareImageUpload konvertiert eine hochgeladene Bilddatei im Querformat zu einer quadratischen Image-Komponente
     * @param fileData Imputstream von der Upload-Komponente
     * @param fileName String Dateiname
     * @param targetSize Wunschgröße in px
     * @return quadratisches Bild
     */
    public static Image squareImageUpload(InputStream fileData, String fileName, int targetSize) {
        Image image = new Image();
        BufferedImage bufferedImage;
        boolean png = false;
        try {
            if (fileName.contains("png")){
                bufferedImage = ImageIO.read(fileData);
                png = true;
            }else {
                byte[] bytes = IOUtils.toByteArray(fileData);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                bufferedImage = ImageIO.read(inputStream);
            }
            //Weiter zu Image Processor für crop
            image = ImageProcessor.cropToSquare(bufferedImage, fileName, targetSize, png);
        } catch (IOException e) {
            logger.error("Fehler beim Speichern des Bildes", e);
        }
        return image;
    }
}
