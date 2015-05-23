package org.nuxeo.demo.dam.core.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.demo.dam.core.operations.ParseColorBlobOp;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationChain;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.DocumentModelImpl;
import org.nuxeo.ecm.core.api.impl.blob.StringBlob;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.LocalDeploy;

import javax.inject.Inject;

/**
 * Created by Michaël on 18/05/2015.
 */

@RunWith(FeaturesRunner.class)
@Features({AutomationFeature.class, CoreFeature.class})
@RepositoryConfig(cleanup = Granularity.METHOD)
@Deploy ({"studio.extensions.mvachette-SANDBOX"})
@LocalDeploy({"org.nuxeo.demo.dam.core:OSGI-INF/extensions/operations.xml"})
public class TestParseColorOp {

    @Inject
    CoreSession session;

    @Before
    public void studioConfigShouldBeLoaded() {
        DocumentModelImpl input = (DocumentModelImpl) session.createDocumentModel("Picture");
        Assert.assertTrue(input.hasSchema("colors"));
    }

    @Test
    public void parseSingleREDColorString() throws Exception {
        AutomationService as = Framework.getService(AutomationService.class);
        OperationContext ctx = new OperationContext();
        ctx.setInput(new StringBlob("      1: (  0,255,128) #009C7A hsl(0%,100%,50%)\n"));
        OperationChain chain = new OperationChain("TestParseColorBlob");
        DocumentModel input = session.createDocumentModel("Picture");
        chain.add(ParseColorBlobOp.ID).
                set("variableName", "colors").
                set("documentModel", input);
        as.run(ctx,chain);
        String[] actualColors = (String[]) input.getPropertyValue("colors:actual");
        Assert.assertTrue(actualColors!=null);
        Assert.assertEquals(1,actualColors.length);
        Assert.assertEquals("FF0000",actualColors[0]);

        String[] normalizedColors = (String[]) input.getPropertyValue("colors:normalized");
        Assert.assertTrue(normalizedColors!=null);
        Assert.assertEquals(1,normalizedColors.length);
        Assert.assertEquals("E51919",normalizedColors[0]);
    }

    @Test
    public void parseSingleBLUEColorString() throws Exception {
        AutomationService as = Framework.getService(AutomationService.class);
        OperationContext ctx = new OperationContext();
        ctx.setInput(new StringBlob("      1: (  170,255,128) #009C7A hsl(60%,100%,50%)\n"));
        OperationChain chain = new OperationChain("TestParseColorBlob");
        DocumentModel input = session.createDocumentModel("Picture");
        chain.add(ParseColorBlobOp.ID).
                set("variableName", "colors").
                set("documentModel", input);
        as.run(ctx,chain);

        String[] actualColors = (String[]) input.getPropertyValue("colors:actual");
        Assert.assertTrue(actualColors!=null);
        Assert.assertEquals(1,actualColors.length);
        Assert.assertEquals("0000FF",actualColors[0]);

        String[] normalizedColors = (String[]) input.getPropertyValue("colors:normalized");
        Assert.assertTrue(normalizedColors!=null);
        Assert.assertEquals(1,normalizedColors.length);
        Assert.assertEquals("1919E5",normalizedColors[0]);
    }

    @Test
    public void parseSingleBLACKColorString() throws Exception {
        AutomationService as = Framework.getService(AutomationService.class);
        OperationContext ctx = new OperationContext();
        ctx.setInput(new StringBlob("      1: (  0,180,0) #009C7A hsl(50%,0%,0%)\n"));
        OperationChain chain = new OperationChain("TestParseColorBlob");
        DocumentModel input = session.createDocumentModel("Picture");
        chain.add(ParseColorBlobOp.ID).
                set("variableName", "colors").
                set("documentModel", input);
        as.run(ctx,chain);
        String[] actualColors = (String[]) input.getPropertyValue("colors:actual");
        Assert.assertTrue(actualColors!=null);
        Assert.assertEquals(1,actualColors.length);
        Assert.assertEquals("000000",actualColors[0]);

        String[] normalizedColors = (String[]) input.getPropertyValue("colors:normalized");
        Assert.assertTrue(normalizedColors!=null);
        Assert.assertEquals(1,normalizedColors.length);
        Assert.assertEquals("000000",normalizedColors[0]);
    }

    @Test
    public void parseSingleWHITEColorString() throws Exception {
        AutomationService as = Framework.getService(AutomationService.class);
        OperationContext ctx = new OperationContext();
        ctx.setInput(new StringBlob("      1: (  0,180,235) #009C7A hsl(50%,0%,0%)\n"));
        OperationChain chain = new OperationChain("TestParseColorBlob");
        DocumentModel input = session.createDocumentModel("Picture");
        chain.add(ParseColorBlobOp.ID).
                set("variableName", "colors").
                set("documentModel", input);
        as.run(ctx,chain);
        String[] actualColors = (String[]) input.getPropertyValue("colors:actual");
        Assert.assertTrue(actualColors!=null);
        Assert.assertEquals(1,actualColors.length);
        Assert.assertEquals("F8DCDC",actualColors[0]);

        String[] normalizedColors = (String[]) input.getPropertyValue("colors:normalized");
        Assert.assertTrue(normalizedColors!=null);
        Assert.assertEquals(1,normalizedColors.length);
        Assert.assertEquals("FFFFFF",normalizedColors[0]);
    }

    @Test
    public void parseSingleGrayColorString() throws Exception {
        AutomationService as = Framework.getService(AutomationService.class);
        OperationContext ctx = new OperationContext();
        ctx.setInput(new StringBlob("      1: (  127,5,190) #009C7A hsl(50%,0%,0%)\n"));
        OperationChain chain = new OperationChain("TestParseColorBlob");
        DocumentModel input = session.createDocumentModel("Picture");
        chain.add(ParseColorBlobOp.ID).
                set("variableName", "colors").
                set("documentModel", input);
        as.run(ctx,chain);
        String[] actualColors = (String[]) input.getPropertyValue("colors:actual");
        Assert.assertTrue(actualColors!=null);
        Assert.assertEquals(1,actualColors.length);
        Assert.assertEquals("BCBDBD",actualColors[0]);

        String[] normalizedColors = (String[]) input.getPropertyValue("colors:normalized");
        Assert.assertTrue(normalizedColors!=null);
        Assert.assertEquals(1,normalizedColors.length);
        Assert.assertEquals("7F7F7F",normalizedColors[0]);
    }


    @Test
    public void parseSingleColorString() throws Exception {
        AutomationService as = Framework.getService(AutomationService.class);
        OperationContext ctx = new OperationContext();
        ctx.setInput(new StringBlob("      4924: (  5,156,122) #009C7A hsl(0.0656138%,60.9842%,47.8126%)\n"));
        OperationChain chain = new OperationChain("TestParseColorBlob");
        DocumentModel input = session.createDocumentModel("Picture");
        chain.add(ParseColorBlobOp.ID).
                set("variableName", "colors").
                set("documentModel", input);
        as.run(ctx,chain);
        String[] actualColors = (String[]) input.getPropertyValue("colors:actual");
        Assert.assertTrue(actualColors!=null);
        Assert.assertEquals(1,actualColors.length);
    }
}
