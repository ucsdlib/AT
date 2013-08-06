package org.archiviststoolkit.importer;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.structure.EAD.Descrules;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

public class HandleDescrulesAction implements Action
{
    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel){
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();

        Descrules descrules = ((Descrules)o);
        if(descrules!=null){
        String descrulesString = EADHelper.ObjectNodetoString(descrules);
        descrulesString = EADHelper.removeTagsFromString(descrulesString);
        EADHelper.setProperty(archDescription,"descriptionRules",descrulesString,Resources.class);
        //((Resources)archDescription).setDescriptionRules(descrulesString);    
        }
    }   
    public List getChildren(Object element)
    {
        return null;
    }
}