package org.nuxeo.demo.dam.core.operations;

/**
 * Created by Michaël on 22/05/2015.
 */
public class HSLColor {

    protected int hue;
    protected int saturation;
    protected int lightness;
    protected int count;

    public HSLColor(int hue, int saturation, int lightness, int count) {
        this.saturation = saturation;
        this.lightness = lightness;
        this.hue = hue;
        this.count = count;
    }

    public int getHue() {
        return hue;
    }

    public int getSaturation() {
        return saturation;
    }

    public int getLightness() {
        return lightness;
    }

    public int getCount() {
        return count;
    }
}
