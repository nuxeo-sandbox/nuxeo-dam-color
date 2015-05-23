/*
 * (C) Copyright ${year} Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 */

package org.nuxeo.demo.dam.core.operations;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.impl.ArrayProperty;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@Operation(
        id = ParseColorBlobOp.ID,
        category = Constants.CAT_DOCUMENT,
        label = "Parse Colors",
        description = "Parses a list of colors generated with ImageMagick")
public class ParseColorBlobOp {

    public static final String ID = "Picture.ParseColorBlob";

    private static final Log log = LogFactory.getLog(ParseColorBlobOp.class);


    @Param(name = "documentModel", required = false)
    protected DocumentModel doc;


    @OperationMethod
    public Blob run(Blob blob) {
        parseBlob(blob);
        return blob;
    }

    public void parseBlob(Blob blob) {

        List<HSLColor> actualColors = extractColors(blob);
        int totalPixelCount = getPixelCount(actualColors);
        int normalizeThreshold = (int) (totalPixelCount *0.03f);
        int principalThreshold = (int) (totalPixelCount *0.25f);
        List<HSLColor> normalizedColors = new ArrayList<>();
        List<HSLColor> principalColors = new ArrayList<>();

        // Normalize
        for (HSLColor color : actualColors) {
            if (color.getCount()<normalizeThreshold) continue;
            HSLColor normalizedColor = normalizeColor(color);
            addColorToList(normalizedColors, normalizedColor);
        }

        //Get principal colors
        for (HSLColor color : normalizedColors) {
            if (color.getCount()<principalThreshold) continue;
            addColorToList(principalColors, color);
        }

        if (doc!=null) {
            ArrayProperty actuals = (ArrayProperty) doc.getProperty("colors:actual");
            actuals.setValue(convertToRGB(actualColors));
            ArrayProperty normalizeds = (ArrayProperty) doc.getProperty("colors:normalized");
            normalizeds.setValue(convertToRGB(normalizedColors));
            ArrayProperty principals = (ArrayProperty) doc.getProperty("colors:principal");
            principals.setValue(convertToRGB(principalColors));
        }
    }

    /**
     *
     * @param blob A color text list extracted with ImageMagick
     * @return the list of color objects
     */
    protected List<HSLColor> extractColors(Blob blob) {
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


    /**
     * @param colors A list of color objects
     * @return the total pixel count
     */
    protected int getPixelCount(List<HSLColor> colors) {
        int count = 0;
        for (HSLColor color : colors) {
            count += color.getCount();
        }
        return count;
    }


    /**
     * @param hue
     * @return the closest hue in NxColors
     */
    protected int getNormalizeHue(float hue) {
        int normalizedHue = 0;

        int i=0;
        while (i<NxColors.HUE.length && hue > NxColors.HUE[i]) {
            i++;
        }

        if (i==NxColors.HUE.length-1) {
            normalizedHue = NxColors.HUE[i];
        } else {
            normalizedHue =
                    hue - NxColors.HUE[i] < NxColors.HUE[i+1] - hue ?
                            NxColors.HUE[i] : NxColors.HUE[i+1];
        }
        return normalizedHue;
    }


    /**
     * @param color An HSL color
     * @return the color object corresponding to the closest color defined in {@link NxColors}
     */
    protected HSLColor normalizeColor(HSLColor color) {

        // Handle black
        if (color.getLightness()<NxColors.BLACK_LIGHTNESS_UPPER_THRESHOLD) {
            return HSLColor.black(color.getCount());
        }

        // Handle white
        if (color.getLightness() > NxColors.WHITE_LIGHTNESS_LOWER_THRESHOLD) {
            return HSLColor.white(color.getCount());
        }

        // Handle gray
        if (color.getSaturation()<NxColors.GRAY_SATURATION_LOWER_THRESHOLD) {
            return HSLColor.gray(color.count);
        }

        return new HSLColor(getNormalizeHue(color.hue),NxColors.SATURATION,NxColors.LIGHTNESS,color.getCount());
    }


    /**
     * Adds the color to the list if not already present, otherwise just adds the pixel count
     * @param colors the list of color objects
     * @param color the color object to add
     */
    protected void addColorToList(List<HSLColor> colors, HSLColor color) {
        HSLColor sameColor = null;
        for(HSLColor current : colors) {
            if (current.equals(color)) sameColor = current;
        }
        if (sameColor==null) {
            colors.add(color);
        } else {
            sameColor.addToCount(color.getCount());
        }
    }

    /**
     * Converts the input list to a list of hexadecimal rgb code
     * @param colors the color object list
     * @return a list of hex rgb codes
     */
    protected List<String> convertToRGB(List<HSLColor> colors) {
        List<String> rgbs = new ArrayList<>();
        for (HSLColor color : colors) {
            String hex = String.format("%08X",color.toRGB()).substring(2,8);
            rgbs.add(hex);
        }
        return rgbs;
    }

}
