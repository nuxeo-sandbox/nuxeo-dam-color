package org.nuxeo.demo.dam.color.test;

import org.junit.Test;
import org.nuxeo.demo.dam.color.model.hsl.HSLColor;
import org.nuxeo.demo.dam.color.model.palette.ColorPalette;
import org.nuxeo.demo.dam.color.model.palette.group.impl.BlackGroup;
import org.nuxeo.demo.dam.color.model.palette.group.impl.GrayGroup;
import org.nuxeo.demo.dam.color.model.palette.group.impl.ColorGroupImpl;
import org.nuxeo.demo.dam.color.model.palette.group.impl.WhiteGroup;

import static org.junit.Assert.assertEquals;

/**
 * Created by Michaël on 30/05/2015.
 */
public class TestColorPalette {

    @Test
    public void color() throws Exception {
        HSLColor red = new HSLColor(5,50,50);
        assertEquals("didn't fin red", ColorPalette.getByName("red"), ColorPalette.getClosest(red));
    }

    @Test
    public void noResult() throws Exception {
        HSLColor no = new HSLColor(15, ColorGroupImpl.DEFAULT_MIN_SATURATION -1,WhiteGroup.LIGHTNESS_LOWER_THRESHOLD-1);
        assertEquals("found a color in the palette",null,ColorPalette.getClosest(no));
    }

    @Test
    public void white() throws Exception {
        HSLColor white = new HSLColor(15,10, WhiteGroup.LIGHTNESS_LOWER_THRESHOLD+1);
        assertEquals("didn't find white",ColorPalette.getByName("white"),ColorPalette.getClosest(white));
    }

    @Test
    public void black() throws Exception {
        HSLColor black = new HSLColor(15,10, BlackGroup.LIGHTNESS_UPPER_THRESHOLD-1);
        assertEquals("didn't find black",ColorPalette.getByName("black"),ColorPalette.getClosest(black));
    }

    @Test
    public void gray() throws Exception {
        HSLColor gray = new HSLColor(15, GrayGroup.SATURATION_LOWER_THRESHOLD-1,BlackGroup.LIGHTNESS_UPPER_THRESHOLD+5);
        assertEquals("didn't find gray",ColorPalette.getByName("gray"),ColorPalette.getClosest(gray));
    }
}
