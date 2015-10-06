package org.nuxeo.dam.color.core.normalize;

import org.nuxeo.dam.color.core.adapter.ColorHolder;
import org.nuxeo.dam.color.core.model.hsl.HSLColor;
import org.nuxeo.dam.color.core.model.palette.ColorPalette;
import org.nuxeo.dam.color.core.model.palette.group.ColorGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Michaël on 24/05/2015.
 */
public class HSLNormalizer {

    /**
     * The normalize the actual colors and store the normalized value in the color holder
     * @param actualColors
     * @param colorHolder
     */
    public static void normalizeColor(List<HSLColor> actualColors, ColorHolder colorHolder) {

        int totalPixelCount = getPixelCount(actualColors);
        int significantThreshold = (int) (totalPixelCount *0.01f);
        int principalThreshold = (int) (totalPixelCount *0.25f);
        List<HSLColor> normalizedColors = new ArrayList<>();
        List<HSLColor> principalColors = new ArrayList<>();

        // Normalize
        for (HSLColor color : actualColors) {
            HSLColor normalizedColor = normalizeColor(color);
            if (normalizedColor!=null) addColorToList(normalizedColors, normalizedColor);
        }

        // Filter insignificant colors
        Iterator<HSLColor> iterator = normalizedColors.iterator();
        while(iterator.hasNext()) {
            HSLColor current = iterator.next();
            if (current.getCount()<significantThreshold) iterator.remove();
        }

        //Get principal colors
        for (HSLColor color : normalizedColors) {
            if (color.getCount()<principalThreshold) continue;
            addColorToList(principalColors, color);
        }

        //colorHolder.setActualColors(actualColors);
        colorHolder.setNormalizedColors(normalizedColors);
        colorHolder.setPrincipalColors(principalColors);
    }


    /**
     * @param colors A list of color objects
     * @return the total pixel count
     */
    protected static int getPixelCount(List<HSLColor> colors) {
        int count = 0;
        for (HSLColor color : colors) {
            count += color.getCount();
        }
        return count;
    }


    /**
     * @param color An HSL color
     * @return the color object corresponding to the closest color defined in {@link ColorPalette}
     */
    protected static HSLColor normalizeColor(HSLColor color) {
        HSLColor normalized = null;
        for (ColorGroup group : ColorPalette.COLORS_LIST) {
            if (group.belongTo(color)) {
                normalized = group.average();
                normalized.addToCount(color.getCount());
                break;
            }
        }
        return normalized;
    }


    /**
     * Adds the color to the list if not already present, otherwise just adds the pixel count
     * @param colors the list of color objects
     * @param color the color object to add
     */
    protected static void addColorToList(List<HSLColor> colors, HSLColor color) {
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


}
