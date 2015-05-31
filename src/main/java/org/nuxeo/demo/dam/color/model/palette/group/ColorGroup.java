package org.nuxeo.demo.dam.color.model.palette.group;

import org.nuxeo.demo.dam.color.model.hsl.HSLColor;

/**
 * Created by Michaël on 30/05/2015.
 */
public interface ColorGroup {

    String getName();

    boolean belongTo(HSLColor color);

    HSLColor average();
}
