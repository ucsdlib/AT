package org.archiviststoolkit.importer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.structure.EAD.Revisiondesc;

public class HandleRevisiondescAction implements Action
{
    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel)
    {
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
//        ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing revisiondesc element");
        String revisionDesc = EADHelper.ObjectNodetoString(o);
        revisionDesc = EADHelper.removeTagsFromString(revisionDesc);
        EADHelper.setProperty(archDescription,"revisionDescription",revisionDesc,Resources.class);
        //((Resources)archDescription).setRevisionDescription(revisionDesc);
    }
    public List getChildren(Object element)
    {
        List list = new ArrayList();
        list.add( ((Revisiondesc) element).getChange());
        list.add( ((Revisiondesc) element).getList());
        return list;
    }
}


