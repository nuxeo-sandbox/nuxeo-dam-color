package org.nuxeo.demo.dam.color.test;

import org.junit.Test;
import org.nuxeo.demo.dam.color.model.hsl.HSLColor;
import org.nuxeo.demo.dam.color.model.palette.ColorPalette;
import org.nuxeo.demo.dam.color.model.palette.group.ColorGroup;

import static org.junit.Assert.assertEquals;

/**
 * Created by Michaël on 31/05/2015.
 */
public class TestRGBHSLConversion {

    @Test
    public void convertPalette() {
        for (ColorGroup group : ColorPalette.COLORS_LIST) {
            HSLColor color = group.average();
            String hex = color.toRGBHex();
            HSLColor decoded = new HSLColor(hex);
            assertEquals(color,decoded);
        }
    }

}
