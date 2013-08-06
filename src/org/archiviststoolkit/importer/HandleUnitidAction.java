package org.archiviststoolkit.importer;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.model.ResourcesComponents;
import org.archiviststoolkit.structure.EAD.Unitid;

public class HandleUnitidAction implements Action{
    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel){
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
//        ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing unitid element");
        Unitid uid = (Unitid) o;
        String unitid = "";
        if(uid!=null)
            unitid = (String) EADHelper.getClassFromList(uid.getContent(),String.class);
        if(archDescription instanceof Resources){
            EADHelper.setProperty(archDescription,"resourceIdentifier1",unitid,Resources.class);
            //((Resources)archDescription).setResourceIdentifier1(unitid);
        }
        else if(archDescription instanceof ResourcesComponents){
            //((ResourcesComponents)archDescription).setComponentUniqueIdentifier(unitid);
            EADHelper.setProperty(archDescription,"componentUniqueIdentifier",unitid,ResourcesComponents.class);

        }
    }
    public List getChildren(Object element){
        return null;
    }
}
