package org.archiviststoolkit.importer;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.structure.EAD.Daodesc;
import org.archiviststoolkit.structure.EAD.Daoloc;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.ApplicationFrame;

public class HandleDaolocAction implements Action{

    public  void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel){
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
//    ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing daoloc element");
    Daoloc daoloc = (Daoloc)o;
    FileVersions fv = new FileVersions();
    String href = "";
    ArchDescriptionDigitalInstances digInstance = new ArchDescriptionDigitalInstances((ResourcesCommon)archDescription);
    DigitalObjects dObjects = new DigitalObjects();
        dObjects.setRepository(ApplicationFrame.getInstance().getCurrentUserRepository());

    digInstance.setInstanceType(ArchDescriptionDigitalInstances.DIGITAL_OBJECT_INSTANCE);
    href = daoloc.getHref();
    String role="";
    role = daoloc.getRole();
    if(href!=null)
        fv.setUri(href);
    if(StringHelper.isNotEmpty(role))
        EADHelper.setProperty(fv,"useStatement",role,FileVersions.class);
    
    String label = daoloc.getLabel();
    System.out.println("Label="+label);
    if(StringHelper.isNotEmpty(label)){
        EADHelper.setProperty(dObjects,"label",label,DigitalObjects.class);    
    }
    
    
    Daodesc daoDesc = daoloc.getDaodesc();
   if(daoDesc!=null){
    EADInfo eadInfo = new EADInfo();
    Action action = eadInfo.getActionFromClass(daoDesc);
        if (null != action) { System.out.println("HEreDAODESC");
            action.processElement(dObjects, daoDesc, progressPanel);
        }
   }
    
    
    /*if(daoDesc!=null){
        String daoDescS = EADHelper.ObjectNodetoString(daoDesc);
        daoDescS = EADHelper.removeTagsFromString(daoDescS);
        ArchDescriptionNotes adn = new ArchDescriptionNotes(dObjects);
        adn.setNoteType("General note");
        adn.setTitle("General note");
        adn.setNoteContent(daoDescS);
        dObjects.addRepeatingData(adn);
    }   */
    
    fv.setDigitalObject(dObjects);
    dObjects.addFileVersion(fv);
    dObjects.setDigitalInstance(digInstance);
    digInstance.setDigitalObject(dObjects);
    digInstance.setParentResource(EADImportHandler.getResourceRecord());
    ((ResourcesCommon)archDescription).addInstance(digInstance);
    }
    
    public List getChildren(Object element){
        List al = new ArrayList();
        if(((Daoloc)element).getDaodesc()!=null)
            al.add(((Daoloc)element).getDaodesc());
        return al;
    }
}
