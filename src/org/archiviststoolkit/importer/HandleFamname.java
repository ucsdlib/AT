package org.archiviststoolkit.importer;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.structure.EAD.Famname;
import org.archiviststoolkit.util.NameUtils;
import org.archiviststoolkit.util.StringHelper;

public class HandleFamname implements Action
{
    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel){
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
//    ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing famname element");
    ArrayList pNameList = (ArrayList) getChildren(o);
    String pname = (String) EADHelper.getClassFromList(pNameList,String.class);                
    if(pname!=null)
        pname = StringHelper.cleanUpWhiteSpace(pname);
    String role = "Subject";
    Names name = new Names();
    //name.setSortName(pname);
    //name.setFamilyName(pname);
    EADHelper.setProperty(name, "sortName", pname, 
            Names.class);
    EADHelper.setProperty(name, "familyName", pname, 
            Names.class);    
    name.setNameType(Names.FAMILY_TYPE);

    try{
        NameUtils.addName(archDescription,name,role);
    }
    catch (PersistenceException pe){
        pe.printStackTrace();
    }
    catch (UnknownLookupListException pe){
        pe.printStackTrace();
    } 
    catch (DuplicateLinkException e) {
		e.printStackTrace();
	}
    }
    public List getChildren(Object element){
    Famname fam = (Famname)element;
    return fam.getContent();
    }
}