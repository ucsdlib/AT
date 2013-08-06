package org.archiviststoolkit.importer;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.structure.EAD.Profiledesc;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

public class HandleProfiledescAction implements Action
{
    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel){
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
        EADInfo eadInfo = new EADInfo();
        for (Object eadElem: getChildren(o)) {
            Action a = 
                eadInfo.getActionFromClass(eadElem);
            if (a != null) {
                a.processElement(archDescription, eadElem, progressPanel);
            }
        }
    }   
    public List getChildren(Object element)
    {
        Profiledesc profileDesc = (Profiledesc)element;
        List list = new ArrayList();
        if(profileDesc.getDescrules()!=null)
            list.add(profileDesc.getDescrules());
        if(profileDesc.getLangusage()!=null)
            list.add(profileDesc.getLangusage());
        if(profileDesc.getCreation()!=null)
            list.add(profileDesc.getCreation());
        return list;
    }
}