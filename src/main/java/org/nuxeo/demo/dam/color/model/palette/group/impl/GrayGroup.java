package org.nuxeo.demo.dam.color.model.palette.group.impl;

import org.nuxeo.demo.dam.color.model.hsl.HSLColor;
import org.nuxeo.demo.dam.color.model.palette.group.ColorGroup;

/**
 * Created by Michaël on 30/05/2015.
 */
public class GrayGroup implements ColorGroup {

    public static final int SATURATION_LOWER_THRESHOLD = 15;

    @Override
    public String getName() {
        return "gray";
    }

    @Override
    public boolean belongTo(HSLColor color) {
        if (color.getLightness()<BlackGroup.LIGHTNESS_UPPER_THRESHOLD ||
                color.getLightness() > WhiteGroup.LIGHTNESS_LOWER_THRESHOLD) {
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
