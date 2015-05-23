package org.nuxeo.demo.dam.core.operations;

/**
 * Created by Michaël on 22/05/2015.
 */
public class HSLColor {

    public static final int MIN_HUE = 0;
    public static final int MAX_HUE = 360;
    public static final int MIN_SATURATION = 0;
    public static final int MAX_SATURATION = 100;
    public static final int MIN_LIGHTNESS = 0;
    public static final int MAX_LIGHTNESS = 100;

    public static HSLColor black(int count) {
        return new HSLColor(MIN_HUE,MIN_SATURATION,MIN_LIGHTNESS,count);
    }

    public static HSLColor white(int count) {
        return new HSLColor(MIN_HUE,MIN_SATURATION,MAX_LIGHTNESS,count);
    }

    public static HSLColor gray(int count) {
        return new HSLColor(MIN_HUE,MIN_SATURATION,MAX_LIGHTNESS/2,count);
    }

    // Hue angle (0-360)
    protected float hue;
    // Saturation percentage (0-100)
    protected float saturation;
    // Lightness percentage (0-100)
    protected float lightness;
    //Pixel counr
    protected int count;

    public HSLColor(int hue, int saturation, int lightness, int count) {
        this.saturation = saturation;
        this.lightness = lightness;
        this.hue = hue;
        this.count = count;
    }

    public HSLColor(float hue, float saturation, float lightness, int count) {
        this.saturation = (int) saturation;
        this.lightness = (int) lightness;
        this.hue = (int) hue;
        this.count = count;
    }

    public float getHue() {
        return hue;
    }

    public float getSaturation() {
        return saturation;
    }

    public float getLightness() {
        return lightness;
    }

    public int getCount() {
        return count;
    }

    public void addToCount(int count) {
        this.count += count;
    }

    public long toRGB() {
        return HSLConverter.toRGB(hue / 360.0f, saturation / 100.0f, lightness / 100.0f);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HSLColor color = (HSLColor) o;
        if (hue != color.hue) return false;
        if (saturation != color.saturation) return false;
        return lightness == color.lightness;
    }
}
