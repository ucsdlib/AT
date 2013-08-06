package org.archiviststoolkit.importer;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.structure.EAD.Dao;
import org.archiviststoolkit.structure.EAD.Daodesc;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.ApplicationFrame;

public class HandleDaoAction implements Action{

    public  void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel){
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
//    ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing dao element");
        Dao dao = (Dao) o;


        FileVersions fv = new FileVersions();

        String href = "";
        ArchDescriptionDigitalInstances digInstance = new ArchDescriptionDigitalInstances((ResourcesCommon) archDescription);
        DigitalObjects dObjects = new DigitalObjects();
        dObjects.setRepository(ApplicationFrame.getInstance().getCurrentUserRepository());

    Daodesc daoDesc = dao.getDaodesc();
    if(daoDesc!=null){
    	HandleNotesAction hna = new HandleNotesAction();
    	hna.processElement(dObjects, daoDesc, progressPanel);
    }
    
    digInstance.setInstanceType(ArchDescriptionDigitalInstances.DIGITAL_OBJECT_INSTANCE);
    href = dao.getHref();
    String role="";
    String title = "";
    title = dao.getTitle();
    
    EADHelper.setProperty(dObjects,"title",title,DigitalObjects.class);

    role = dao.getRole();
    String actuate="";
    String show = "";
    actuate = dao.getActuate();
    show = dao.getShow();
    
    if(href!=null)
        fv.setUri(href);
    if(StringHelper.isNotEmpty(role))
        EADHelper.setProperty(fv,"useStatement",role,FileVersions.class);
        //fv.setUseStatement(role);
    EADHelper.setProperty(fv,"eadDaoActuate",actuate,FileVersions.class);
    EADHelper.setProperty(fv,"eadDaoShow",show,FileVersions.class);

        
        
    fv.setDigitalObject(dObjects);
    dObjects.addFileVersion(fv);
    dObjects.setDigitalInstance(digInstance);
    digInstance.setDigitalObject(dObjects);
    digInstance.setParentResource(EADImportHandler.getResourceRecord());

    ((ResourcesCommon)archDescription).addInstance(digInstance);
    }
    
    public List getChildren(Object element){
        return null;
    }
}
