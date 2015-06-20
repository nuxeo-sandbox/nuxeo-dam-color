package org.nuxeo.dam.color.core.model.palette.group;

import org.nuxeo.dam.color.core.model.hsl.HSLColor;

/**
 * Created by Michaël on 30/05/2015.
 */
public interface ColorGroup {

    String getName();

    boolean belongTo(HSLColor color);

    HSLColor average();
}
