package org.nuxeo.dam.color.core.model.palette.group.impl;

import org.nuxeo.dam.color.core.model.hsl.HSLColor;
import org.nuxeo.dam.color.core.model.palette.group.ColorGroup;

/**
 * Created by Michaël on 29/05/2015.
 */
public class ColorGroupImpl implements ColorGroup {

    public static final int DEFAULT_SATURATION = 80;

    public static final int DEFAULT_MIN_SATURATION = 25;

    public static final int DEFAULT_MAX_SATURATION = 100;

    public static final int DEFAULT_LIGHTNESS = 50;

    public static final int DEFAULT_MIN_LIGHTNESS = 30;

    public static final int DEFAULT_MAX_LIGHTNESS = 65;

    protected float hue;
    protected float minHue;
    protected float maxHue;
    protected float saturation;
    protected float minSaturation;
    protected float maxSaturation;
    protected float lightness;
    protected float minLightness;
    protected float maxLightness;
    protected String name;

    public ColorGroupImpl(float hue, float minHue, float maxHue, String name) {
        this.name = name;
        this.hue = hue;
        this.minHue = minHue;
        this.maxHue = maxHue;
        this.saturation = DEFAULT_SATURATION;
        this.minSaturation = DEFAULT_MIN_SATURATION;
        this.maxSaturation = DEFAULT_MAX_SATURATION;
        this.lightness = DEFAULT_LIGHTNESS;
        this.minLightness = DEFAULT_MIN_LIGHTNESS;
        this.maxLightness = DEFAULT_MAX_LIGHTNESS;
    }

    public ColorGroupImpl(float hue, float minHue, float maxHue,
                          float saturation, float minSaturation, float maxSaturation,
                          String name) {
        this(hue,minHue,maxHue,name);
        this.saturation = saturation;
        this.minSaturation = minSaturation;
        this.maxSaturation = maxSaturation;
    }

    public ColorGroupImpl(float hue, float minHue, float maxHue,
                          float saturation, float minSaturation, float maxSaturation,
                          float lightness, float minLightness, float maxLightness,
                          String name) {
        this(hue,minHue,maxHue,saturation,minSaturation,maxSaturation,name);
        this.lightness = lightness;
        this.minLightness = minLightness;
        this.maxLightness = maxLightness;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean belongTo(HSLColor color) {

        // Exclude desaturated color
        if (color.getSaturation()<minSaturation) {
            return false;
        }

        // Exclude very light color
        if (color.getLightness()>= maxLightness) {
            return false;
        }

        // Exclude very dark color
        if (color.getLightness()<= minLightness) {
            return false;
        }

        // Check Hue
        if (minHue > maxHue) {
            if (color.getHue() >= minHue) {
                return  true;
            } else {
                return color.getHue() >= 0 && color.getHue() < maxHue;
            }
        } else {
            return color.getHue() >= minHue && color.getHue() < maxHue;
        }
    }

    @Override
    public HSLColor average() {
        return new HSLColor(hue, saturation, lightness,0);
    }
}
