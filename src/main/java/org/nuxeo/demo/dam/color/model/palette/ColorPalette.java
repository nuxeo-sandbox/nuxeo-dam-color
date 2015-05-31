package org.nuxeo.demo.dam.color.model.palette;

import org.nuxeo.demo.dam.color.model.hsl.HSLColor;
import org.nuxeo.demo.dam.color.model.palette.group.ColorGroup;
import org.nuxeo.demo.dam.color.model.palette.group.impl.BlackGroup;
import org.nuxeo.demo.dam.color.model.palette.group.impl.GrayGroup;
import org.nuxeo.demo.dam.color.model.palette.group.impl.ColorGroupImpl;
import org.nuxeo.demo.dam.color.model.palette.group.impl.WhiteGroup;

import java.util.*;

/**
 * Created by Michaël on 22/05/2015.
 */
public class ColorPalette {

    public static final Map<String, ColorGroup> COLORS_MAP;

    public static final List<ColorGroup> COLORS_LIST;

    static {
        List<ColorGroup> list = new ArrayList<>();
        list.add(new ColorGroupImpl(0, 350, 10, "red"));
        list.add(new ColorGroupImpl(30, 10, 55, 80, 70, 100, "orange"));
        list.add(new ColorGroupImpl(60, 55, 63, 95, 75, 100, "yellow"));
        list.add(new ColorGroupImpl(120, 63, 175, "green"));
        list.add(new ColorGroupImpl(180, 175, 185, "cyan"));
        list.add(new ColorGroupImpl(240, 185, 270, "blue"));
        list.add(new ColorGroupImpl(280, 270, 290, "purple"));
        list.add(new ColorGroupImpl(300, 290, 310, "magenta"));
        list.add(new ColorGroupImpl(330, 310, 350, "pink"));
        list.add(new WhiteGroup());
        list.add(new GrayGroup());
        list.add(new BlackGroup());
        COLORS_LIST = Collections.unmodifiableList(list);

        Map<String, ColorGroup> map = new HashMap<>();
        for (ColorGroup group : list) {
            map.put(group.getName(),group);
        }
        COLORS_MAP = Collections.unmodifiableMap(map);
    }


    /**
     * @param name a color name
     * @return the color object corresponding to given name
     */
    public static HSLColor getByName(String name) {
        return COLORS_MAP.get(name).average();
    }


    /**
     * @param color An HSL color
     * @return the color object corresponding to the closest color in the palette
     */
    public static HSLColor getClosest(HSLColor color) {
        HSLColor normalized = null;
        for (ColorGroup group : ColorPalette.COLORS_LIST) {
            if (group.belongTo(color)) {
                normalized = group.average();
                break;
            }
        }
        return normalized;
    }
}
