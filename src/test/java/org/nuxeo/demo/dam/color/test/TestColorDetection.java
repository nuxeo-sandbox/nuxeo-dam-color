package org.nuxeo.demo.dam.color.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.demo.dam.color.adapter.ColorHolder;
import org.nuxeo.demo.dam.color.model.hsl.HSLColor;
import org.nuxeo.demo.dam.color.model.palette.ColorPalette;
import org.nuxeo.demo.dam.color.operations.ProcessColorHistogramOp;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationChain;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Michaël on 30/05/2015.
 */

@RunWith(FeaturesRunner.class)
@Features({AutomationFeature.class})
@RepositoryConfig(cleanup = Granularity.METHOD)
@Deploy({
        "org.nuxeo.ecm.platform.picture.core",
        "org.nuxeo.demo.dam.color"})
public class TestColorDetection {

    @Inject
    CoreSession session;

    @Test
    public void detectColorInImage() throws Exception {
        File file = new File(getClass().getResource("/files/tests.csv").getPath());
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line;
        //read first line (header)
        reader.readLine();
        while ((line = reader.readLine()) != null && line.length()>0) {
            runOnTestImage(line);
        }
        reader.close();
    }


    protected void runOnTestImage(String input) throws OperationException {
        String[] columns = input.split(";");
        String path = columns[0];
        String[] contains =  columns[1].split(",");
        String[] doesNotcontain =  columns[2].split(",");
        String[] isMainColor =  columns[3].split(",");
        String[] isNotMainColor =  columns[4].split(",");

        File file = new File(getClass().getResource(path).getPath());
        assertTrue(path+" does not exist", file.exists());
        DocumentModel doc = session.createDocumentModel("Picture");

        AutomationService as = Framework.getService(AutomationService.class);
        OperationContext ctx = new OperationContext();
        ctx.setInput(new FileBlob(file));
        OperationChain chain = new OperationChain("TestParseColorBlob");
        chain.add(ProcessColorHistogramOp.ID).
                set("documentModel", doc);
        as.run(ctx, chain);

        ColorHolder holder = doc.getAdapter(ColorHolder.class);
        List<HSLColor> normalizedColors = holder.getNormalizedColors();
        List<HSLColor> mainColors = holder.getPrincipalColors();

        //check contains
        for (String name : contains) {
            if (name.equals("none")) break;
            HSLColor color = ColorPalette.getByName(name);
            assertTrue(path+" should contains "+ name ,normalizedColors.contains(color));
        }

        //check doesNotContain
        for (String name : doesNotcontain) {
            if (name.equals("none")) break;
            HSLColor color = ColorPalette.getByName(name);
            assertTrue(path+" should not contain "+ name ,!normalizedColors.contains(color));
        }

        //check main colors
        for (String name : isMainColor) {
            if (name.equals("none")) break;
            HSLColor color = ColorPalette.getByName(name);
            assertTrue(path+" should have main color "+ name , mainColors.contains(color));
        }

        //check not main colors
        for (String name : isNotMainColor) {
            if (name.equals("none")) break;
            HSLColor color = ColorPalette.getByName(name);
            assertTrue(path+" should not have main color "+ name ,!mainColors.contains(color));
        }

    }

}
