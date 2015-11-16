package org.nuxeo.dam.color.core.adapter;

import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

/**
 * Created by Michaël on 17/02/2015.
 */
public class ColorHolderAdapterFactory implements DocumentAdapterFactory {
    
    @Override
    public Object getAdapter(DocumentModel doc, Class<?> itf) {
        if (doc.hasFacet("Colors")) {
            return new ColorHolderImpl(doc);
        } else {
            throw new NuxeoException();
        }
    }
}
