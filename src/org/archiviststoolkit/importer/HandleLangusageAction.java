package org.archiviststoolkit.importer;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.structure.EAD.Langusage;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

public class HandleLangusageAction implements Action
{
    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel){
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
        System.out.println("in Langusage");
        Langusage langusage = ((Langusage)o);
        if(langusage!=null){
        String langusageString = EADHelper.ObjectNodetoString(langusage);
        EADHelper.setProperty(archDescription,"languageOfFindingAid",langusageString,Resources.class);
        }
        }
           
    public List getChildren(Object element)
    {
        return null;
    }
}