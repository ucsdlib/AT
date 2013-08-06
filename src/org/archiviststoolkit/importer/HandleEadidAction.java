package org.archiviststoolkit.importer;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.structure.EAD.Eadid;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

public class HandleEadidAction implements Action
{
    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel)
    {
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
        String falocation = ((Eadid)o).getUrl();
        String id = ((Eadid)o).getContent();
        if(id!=null)
            ((Resources)archDescription).setEadFaUniqueIdentifier(id);
        if(falocation!=null)
            ((Resources)archDescription).setEadFaLocation(falocation);

    }
    public List getChildren(Object element)
    {
        return null;
    }
}
