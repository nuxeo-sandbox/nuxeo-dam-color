/*
 * (C) Copyright ${year} Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 */

package org.nuxeo.demo.dam.core.operations;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.impl.ArrayProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@Operation(
        id = ParseColorBlobOp.ID,
        category = Constants.CAT_DOCUMENT,
        label = "Parse Colors",
        description = "Parses a list of colors generated with ImageMagick")
public class ParseColorBlobOp {

    public static final String ID = "Picture.ParseColorBlob";

    private static final Log log = LogFactory.getLog(ParseColorBlobOp.class);

    @Context
    protected CoreSession session;

    @Context
    protected OperationContext ctx;

    @Param(name = "documentModel", required = false)
    protected DocumentModel doc;

    @OperationMethod
    public Blob run(Blob blob) {
        parseBlob(blob);
        return blob;
    }

    public void parseBlob(Blob blob) {
        int totalCount = 0;
        List<HSLColor> hslColors = new ArrayList<>();
        List<String> actualColors = new ArrayList<>();
        List<String> normalizedColors = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(blob.getStream()));
            String line;
            while ((line = reader.readLine()) != null && line.length()>0) {
                String segments[] = line.split(":|\\(|\\)|,|#");
                int count = Integer.parseInt(segments[0].trim());
                int hue = Integer.parseInt(segments[2].trim());
                int saturation = Integer.parseInt(segments[3].trim());
                int lightness = Integer.parseInt(segments[4].trim());
                totalCount +=count;
                hslColors.add(new HSLColor(hue,saturation,lightness,count));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int threshold = (int) (totalCount *0.03f);

        for (HSLColor color : hslColors) {
            if (color.getCount()<threshold) continue;

            // Get actual RGB color
            long rgb = HSLConverter.toRGB(
                    color.getHue() / 255.0f,
                    color.getSaturation() / 255.0f,
                    color.getLightness() / 255.0f);
            String actualColor = String.format("%08X",rgb).substring(2,8);
            actualColors.add(actualColor);

            //get normalized color
            int i=0;
            while (i<NxColors.HUE.length && color.getHue() > NxColors.HUE[i]) {
                i++;
            }

            int hueInDegree = (int)(color.getHue() * (360/255.0f));
            int normalized_hue =
                    hueInDegree - NxColors.HUE[i] < NxColors.HUE[i+1] - hueInDegree ?
                            NxColors.HUE[i] : NxColors.HUE[i+1];

            int normalizedLightness = NxColors.LIGHTNESS;
            int normalizedSaturation = NxColors.SATURATION;

            // Handle gray
            if (color.getSaturation()<35) {
                normalizedSaturation = 0;
                normalizedLightness = 50;
            }

            // Handle black
            if (color.getLightness()<15) {
                normalizedLightness = 0;
            }

            // Handle white
            if (color.getLightness() > 210) {
                normalizedLightness = 100;
            }

            rgb = HSLConverter.toRGB(normalized_hue / 360.0f,
                    normalizedSaturation / 100.0f,
                    normalizedLightness / 100.0f);
            String normalizedColor = String.format("%08X",rgb).substring(2,8);
            normalizedColors.add(normalizedColor);
        }


        if (doc!=null) {
            ArrayProperty actuals = (ArrayProperty) doc.getProperty("colors:actual");
            actuals.setValue(actualColors);
            ArrayProperty normalized = (ArrayProperty) doc.getProperty("colors:normalized");
            normalized.setValue(normalizedColors);
        }
    }

}
