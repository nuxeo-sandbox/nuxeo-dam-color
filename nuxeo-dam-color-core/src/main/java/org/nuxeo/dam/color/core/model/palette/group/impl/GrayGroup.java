package org.nuxeo.dam.color.core.model.palette.group.impl;

import org.nuxeo.dam.color.core.model.hsl.HSLColor;
import org.nuxeo.dam.color.core.model.palette.group.ColorGroup;

/**
 * Created by Michaël on 30/05/2015.
 */
public class GrayGroup implements ColorGroup {

    public static final int SATURATION_LOWER_THRESHOLD = 10;

    public static final int LIGHTNESS_LOWER_THRESHOLD = 30;

    public static final int LIGHTNESS_UPPER_THRESHOLD = 80;

    @Override
    public String getName() {
        return "gray";
    }

    @Override
    public boolean belongTo(HSLColor color) {
        if (color.getLightness()<LIGHTNESS_LOWER_THRESHOLD ||
                color.getLightness() > LIGHTNESS_UPPER_THRESHOLD) {
            return false;
        } else {
            return color.getSaturation() < SATURATION_LOWER_THRESHOLD;
        }
    }

    @Override
    public HSLColor average() {
        return new HSLColor(0,0,50,0);
    }
}
