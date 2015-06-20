package org.nuxeo.dam.color.core.tools;

import org.nuxeo.dam.color.core.model.hsl.HSLRGBConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Michaël on 21/05/2015.
 */
public class ColorSpaceGenerator {

    public static void main(String[] args) {

        for (int i = 0; i < 360; i++) {
            BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            for (int x = 0; x < 100; x++) {
                for (int y = 0; y < 100; y++) {
                    image.setRGB(x, 99 - y, (int) HSLRGBConverter.toRGB(i/360.0f, x/100.0f, y/100.0f));
                }
            }
            Image scaled = image.getScaledInstance(1000, 1000, Image.SCALE_DEFAULT);

            // Create a buffered image with transparency
            BufferedImage bimage = new BufferedImage(
                    scaled.getWidth(null), scaled.getHeight(null), BufferedImage.TYPE_INT_ARGB);

            // Draw the image on to the buffered image
            Graphics2D bGr = bimage.createGraphics();
            bGr.drawImage(scaled, 0, 0, null);
            bGr.dispose();

            // Return the buffered image
            File outputfile = new File("hue" + i + ".png");
            try {
                ImageIO.write(bimage, "png", outputfile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
