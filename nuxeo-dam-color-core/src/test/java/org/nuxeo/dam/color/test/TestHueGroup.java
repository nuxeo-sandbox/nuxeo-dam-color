package org.nuxeo.dam.color.test;

import org.junit.Test;
import org.nuxeo.dam.color.core.model.hsl.HSLColor;
import org.nuxeo.dam.color.core.model.palette.group.impl.ColorGroupImpl;

import static org.junit.Assert.assertTrue;

/**
 * Created by Michaël on 30/05/2015.
 */

public class TestHueGroup {

    @Test
    public void detectRed() throws Exception {

        ColorGroupImpl red = new ColorGroupImpl(0,350,10,"red");

        HSLColor color1 = new HSLColor(0,50,50,0);
        assertTrue("Color should be red", red.belongTo(color1));

        HSLColor color2 = new HSLColor(356,60,45,0);
        assertTrue("Color should be red", red.belongTo(color2));

        HSLColor color3 = new HSLColor(5,60,40,0);
        assertTrue("Color should be red", red.belongTo(color3));

        HSLColor color4 = new HSLColor(360,50,50,0);
        assertTrue("Color should be red",red.belongTo(color4));

        HSLColor color5 = new HSLColor(350,50,50,0);
        assertTrue("Color should be red",red.belongTo(color5));

        HSLColor color6 = new HSLColor(10,50,50,0);
        assertTrue("Color should not be red",!red.belongTo(color6));
    }

    @Test
    public void detectDesaturated() throws Exception {
        float hue = 30;
        ColorGroupImpl group = new ColorGroupImpl(hue,hue-5,hue+5,"test");

        // test with pure gray (saturation = 0%, lightness = 50%)
        HSLColor pureGray = new HSLColor(hue,0, ColorGroupImpl.DEFAULT_LIGHTNESS,0);
        assertTrue("Pure gray should not belong to group", !group.belongTo(pureGray));

        // test with desaturated (saturation = 0%, lightness = 50%)
        HSLColor desaturated = new HSLColor(hue, ColorGroupImpl.DEFAULT_MIN_SATURATION -1, ColorGroupImpl.DEFAULT_LIGHTNESS,0);
        assertTrue("desaturated should not belong to group", !group.belongTo(desaturated));

        // test with saturated (saturation = 50%, lightness = 50%)
        HSLColor saturated = new HSLColor(hue, ColorGroupImpl.DEFAULT_MIN_SATURATION +1, ColorGroupImpl.DEFAULT_LIGHTNESS,0);
        assertTrue("saturated should belong to group", group.belongTo(saturated));
    }

    @Test
    public void excludeLightColor() throws Exception {
        float hue = 30;
        ColorGroupImpl group = new ColorGroupImpl(hue,hue-5,hue+5,"test");

        // test with pure white saturation = 0%, lightness = 100%)
        HSLColor white = new HSLColor(hue,0,100,0);
        assertTrue("pure white should not belong to group", !group.belongTo(white));

        // test with tainted white saturation = 30%, lightness = 80%)
        HSLColor taintedWhite = new HSLColor(hue,30,80,0);
        assertTrue("Tainted white should not belong to group", !group.belongTo(taintedWhite));
    }


    @Test
    public void excludeDarkColor() throws Exception {
        float hue = 30;
        ColorGroupImpl group = new ColorGroupImpl(hue,hue-5,hue+5,"test");

        // test with pure black saturation = 30%, lightness = 0%)
        HSLColor black = new HSLColor(hue,30,0,0);
        assertTrue("pure black should not belong to group", !group.belongTo(black));

        // test with tainted white saturation = 30%, lightness = 15%)
        HSLColor taintedBlack = new HSLColor(hue,30,15,0);
        assertTrue("Tainted white should not belong to group", !group.belongTo(taintedBlack));
    }

}
