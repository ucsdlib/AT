package org.archiviststoolkit.importer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.ArchDescriptionNames;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.mydomain.NamesDAO;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.structure.EAD.Corpname;
import org.archiviststoolkit.util.NameUtils;
import org.archiviststoolkit.util.StringHelper;

public class HandleCorpnameAction implements Action
{
    public  void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel){
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
//        ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing persname element");
        ArrayList pNameList = (ArrayList) getChildren(o);
        String pname = (String) EADHelper.getClassFromList(pNameList,String.class);                
        if(pname!=null)
            pname = StringHelper.cleanUpWhiteSpace(pname);
        String role = "Subject";
        Names name = new Names();
        //name.setSortName(pname);
        //name.setCorporatePrimaryName(pname);
        EADHelper.setProperty(name, "sortName", pname, 
                Names.class);
        EADHelper.setProperty(name, "corporatePrimaryName", pname, 
                Names.class);
        
        name.setNameType(Names.CORPORATE_BODY_TYPE);

        try{
            NameUtils.addName(archDescription,name,role);
        }
        catch (PersistenceException pe){
            pe.printStackTrace();
        }
        catch (UnknownLookupListException pe){
            pe.printStackTrace();
        } 
        catch (DuplicateLinkException dle) {
			dle.printStackTrace();
		}
    }
    public List getChildren(Object element){
        Corpname pers = (Corpname)element;
        return pers.getContent();
    }
    
	private void addName(ArchDescription accession, Names name, String nameRole) throws PersistenceException, UnknownLookupListException {
        NamesDAO nameDao = new NamesDAO();
        ArchDescriptionNames accessionName;

        if (name.getNameSource().length() == 0) {
            name.setNameSource("ingest");
        }
        name = nameDao.lookupName(name, true);
        accessionName = new ArchDescriptionNames(name, accession);
        accessionName.setNameLinkFunction(nameRole);
        try {
            accession.addName(accessionName);
        } catch (DuplicateLinkException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}