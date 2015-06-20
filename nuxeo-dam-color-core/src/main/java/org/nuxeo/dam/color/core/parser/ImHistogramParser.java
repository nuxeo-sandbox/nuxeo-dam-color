package org.nuxeo.dam.color.core.parser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.dam.color.core.model.hsl.HSLColor;
import org.nuxeo.ecm.core.api.Blob;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michaël on 30/05/2015.
 */
public class ImHistogramParser {

    private static final Log log = LogFactory.getLog(ImHistogramParser.class);

    /**
     * @param blob A color text list extracted with ImageMagick
     * @return the list of color objects
     */
    public List<HSLColor> parse(Blob blob) {
        List<HSLColor> hslColors = new ArrayList<>();
        float from255to360scale = 360 / 255.0f;
        float from255to100scale = 100 / 255.0f;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(blob.getStream()));
            String line;
            while ((line = reader.readLine()) != null && line.length()>0) {
                String segments[] = line.split(":|\\(|\\)|,|#");
                int count = Integer.parseInt(segments[0].trim());
                int hue = Integer.parseInt(segments[2].trim());
                int saturation = Integer.parseInt(segments[3].trim());
                int lightness = Integer.parseInt(segments[4].trim());
                hslColors.add(
                        new HSLColor(
                                hue*from255to360scale,
                                saturation*from255to100scale,
                                from255to100scale* lightness,
                                count));
            }
            return hslColors;
        } catch (IOException e) {
            log.error(e);
            return new ArrayList<>();
        }
    }
}
