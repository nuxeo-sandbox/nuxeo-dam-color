package org.nuxeo.dam.color.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.dam.color.core.adapter.ColorHolder;
import org.nuxeo.dam.color.core.model.palette.ColorPalette;
import org.nuxeo.dam.color.core.operations.ProcessColorHistogramOp;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationChain;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.DocumentModelImpl;
import org.nuxeo.ecm.core.api.impl.blob.StringBlob;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Michaël on 28/05/2015.
 */

@RunWith(FeaturesRunner.class)
@Features({AutomationFeature.class})
@RepositoryConfig(cleanup = Granularity.METHOD)
@Deploy({
        "org.nuxeo.ecm.platform.picture.core",
        "org.nuxeo.dam.color.core"})
public class TestProcessColorHistogramOp {

    @Inject
    CoreSession session;

    @Before
    public void studioConfigShouldBeLoaded() {
        DocumentModelImpl input = (DocumentModelImpl) session.createDocumentModel("Picture");
    }

    @Test
    public void parseSingleColorBlob() throws Exception {
        StringBlob blob = new StringBlob("      1: (  0,255,128) #009C7A hsl(0%,100%,50%)\n");
        AutomationService as = Framework.getService(AutomationService.class);
        OperationContext ctx = new OperationContext();
        ctx.setInput(blob);
        OperationChain chain = new OperationChain("TestParseColorBlob");
        DocumentModel doc = session.createDocumentModel("Picture");
        chain.add(ProcessColorHistogramOp.ID).
                set("documentModel", doc);
        as.run(ctx, chain);

        ColorHolder holder = doc.getAdapter(ColorHolder.class);
        assertTrue(holder!=null);
        assertTrue(holder.getNormalizedColors().size()==1);
        assertEquals(ColorPalette.getByName("red"),holder.getNormalizedColors().get(0));

    }

}
