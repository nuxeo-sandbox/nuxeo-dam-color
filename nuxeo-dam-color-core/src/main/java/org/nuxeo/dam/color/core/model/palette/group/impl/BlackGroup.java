package org.nuxeo.dam.color.core.model.palette.group.impl;

import org.nuxeo.dam.color.core.model.hsl.HSLColor;
import org.nuxeo.dam.color.core.model.palette.group.ColorGroup;

/**
 * Created by Michaël on 30/05/2015.
 */
public class BlackGroup implements ColorGroup {

    public static final int LIGHTNESS_UPPER_THRESHOLD = 10;

    @Override
    public String getName() {
        return "black";
    }

    @Override
    public boolean belongTo(HSLColor color) {
        return color.getLightness() < LIGHTNESS_UPPER_THRESHOLD;
    }

    @Override
    public HSLColor average() {
        return new HSLColor(0,0,0,0);
    }
}
