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

package org.nuxeo.demo.dam.color.operations;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.demo.dam.color.adapter.ColorHolder;
import org.nuxeo.demo.dam.color.model.hsl.HSLColor;
import org.nuxeo.demo.dam.color.normalize.HSLNormalizer;
import org.nuxeo.demo.dam.color.parser.ImHistogramParser;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;

import java.util.List;


@Operation(
        id = ProcessColorHistogramOp.ID,
        category = Constants.CAT_CONVERSION,
        label = "Parse and normalize color histogram",
        description = "Parse and normalize a list of colors generated with ImageMagick and store it in the given " +
                      "document model. Return the document model")
public class ProcessColorHistogramOp {

    public static final String ID = "Conversion.ProcessColorHistogramOp";

    private static final Log log = LogFactory.getLog(ProcessColorHistogramOp.class);

    @Param(name = "documentModel", required = true)
    protected DocumentModel doc;

    @OperationMethod
    public DocumentModel run(Blob blob) {
        return parseBlob(blob);
    }

    public DocumentModel parseBlob(Blob blob) {
        List<HSLColor> actualColors = new ImHistogramParser().parse(blob);
        if (!doc.hasFacet("Colors")) doc.addFacet("Colors");
        HSLNormalizer.normalizeColor(actualColors,doc.getAdapter(ColorHolder.class));
        return doc;
    }

}
