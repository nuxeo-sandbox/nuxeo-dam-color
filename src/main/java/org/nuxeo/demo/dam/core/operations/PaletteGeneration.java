package org.nuxeo.demo.dam.core.operations;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Michaël on 21/05/2015.
 */
public class PaletteGeneration {
    public static void main(String[] args) {
        BufferedImage image = new BufferedImage(NxColors.HUE.length+2,1,BufferedImage.TYPE_INT_ARGB);
        for (int i =0;i<NxColors.HUE.length-1;i++) {
                float h = NxColors.HUE[i] / 360.0f;
                float s = NxColors.SATURATION / 100.0f;
                float l = NxColors.LIGHTNESS / 100.0f;
                image.setRGB(i,0, (int)HSLConverter.toRGB(h, s, l));
        }
        //Set white
        image.setRGB(NxColors.HUE.length-1,0, (int)HSLConverter.toRGB(0, 0, 1.0f));
        //Set gray
        image.setRGB(NxColors.HUE.length,0, (int)HSLConverter.toRGB(0, 0, 0.5f));
        //Set Black
        image.setRGB(NxColors.HUE.length+1,0, (int)HSLConverter.toRGB(0, 0, 0));

        Image scaled = image.getScaledInstance(NxColors.HUE.length*10,10,Image.SCALE_DEFAULT);

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(
                scaled.getWidth(null), scaled.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(scaled, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        File outputfile = new File("image.png");
        try {
            ImageIO.write(bimage, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
