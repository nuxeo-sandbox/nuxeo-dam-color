package org.nuxeo.dam.color.core.adapter;

import org.nuxeo.dam.color.core.model.hsl.HSLColor;

import java.util.List;

/**
 * Created by Michaël on 16/02/2015.
 */
public interface ColorHolder {
    
    public void setActualColors(List<HSLColor> colors);

    public List<HSLColor> getActualColors();

    public void setNormalizedColors(List<HSLColor> colors);

    public List<HSLColor> getNormalizedColors();

    public void setPrincipalColors(List<HSLColor> colors);

    public List<HSLColor> getPrincipalColors();
    
}
