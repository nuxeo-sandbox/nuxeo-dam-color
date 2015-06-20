package org.nuxeo.dam.color.test;

import org.junit.Test;
import org.nuxeo.dam.color.core.model.hsl.HSLColor;
import org.nuxeo.dam.color.core.parser.ImHistogramParser;
import org.nuxeo.ecm.core.api.impl.blob.StringBlob;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Michaël on 18/05/2015.
 */

public class TestHistogramParser {

    @Test
    public void parseEmptyColorString() throws Exception {
        StringBlob input = new StringBlob("");
        List<HSLColor> colors = new ImHistogramParser().parse(input);
        assertTrue("Color list should be empty",colors.size()==0);
    }

    @Test
    public void parseSingleColorString() throws Exception {
        StringBlob input = new StringBlob("      1: (  0,255,128) #009C7A hsl(0%,100%,50%)\n");
        List<HSLColor> colors = new ImHistogramParser().parse(input);
        assertTrue("One color",colors.size()==1);
        assertEquals("Output is not red", new HSLColor(0,100,50,0),colors.get(0));
    }

    @Test
    public void parseMultipleColorString() throws Exception {
        StringBlob input = new StringBlob(
                "      1: (  0,255,128) #009C7A hsl(0%,100%,50%)\n"+
                "      1: (  170,255,128) #009C7A hsl(60%,100%,50%)\n");
        List<HSLColor> colors = new ImHistogramParser().parse(input);
        assertTrue("Two color",colors.size()==2);
        assertEquals("Output is not red", new HSLColor(0,100,50,0),colors.get(0));
        assertEquals("Output is not blue", new HSLColor(240,100,50,0), colors.get(1));
    }

}
