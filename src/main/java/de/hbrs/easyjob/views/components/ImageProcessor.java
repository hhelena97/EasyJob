package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * ImageProcessor ist eine Utility-Klasse zur Be- und Verarbeitung von Images
 **/
public class ImageProcessor {

    /**
     * cropToSquare cropped und resized ein Buffered Image in ein quadratisches Vaadin Image gewünschter Größe
     * @param originalImage Zu bearbeitende Image Komponente
     * @param targetSize gewünschte Größe in px
     * @return bearbeitetes Image
     */
    public static Image cropToSquare(BufferedImage originalImage, String fileName, int targetSize, boolean png) {

        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        //Bild so schneiden, dass lange Seite gekürzt wird und Zentrum gleich bleibt
        int cropSize = Math.min(originalWidth, originalHeight);
        int startX = (originalWidth - cropSize) / 2;
        int startY = (originalHeight - cropSize) / 2;
        BufferedImage croppedImage = originalImage.getSubimage(startX, startY, cropSize, cropSize);

        //Bild in Vaadin Image konvertieren
        StreamResource streamResource = new StreamResource(fileName, (InputStreamFactory) () -> {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                if(png){
                    ImageIO.write(croppedImage, "png", os);
                }else{
                    ImageIO.write(croppedImage, "jpg", os);
                }return new ByteArrayInputStream(os.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });

        //Resize auf gewünschte Größe
        Image resizedImage = new Image(streamResource, "Square Image");
        resizedImage.setWidth(targetSize + "px");
        resizedImage.setHeight(targetSize + "px");

        return resizedImage;
    }
}
