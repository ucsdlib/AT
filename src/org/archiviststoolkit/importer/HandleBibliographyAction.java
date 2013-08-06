package org.archiviststoolkit.importer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.commons.beanutils.PropertyUtils;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.BibItems;
import org.archiviststoolkit.structure.EAD.Bibref;
import org.archiviststoolkit.structure.EAD.Head;
import org.archiviststoolkit.structure.EAD.P;
import org.archiviststoolkit.util.StringHelper;


public class HandleBibliographyAction implements Action 
{
    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel)
    {       
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
//            ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing Bibliography element");
            org.archiviststoolkit.structure.EAD.Bibliography eadBibElement = (org.archiviststoolkit.structure.EAD.Bibliography)o;
            org.archiviststoolkit.model.Bibliography appBibElement = new org.archiviststoolkit.model.Bibliography(archDescription);
            ArrayList bibChildren = null;
            List aList = eadBibElement.getAddressOrChronlistOrList();
            String id = eadBibElement.getId();
            ArrayList pList = new ArrayList();
            ArrayList notHandled = new ArrayList();
            ArrayList <Class> handled = new ArrayList <Class> ();
            handled.add(org.archiviststoolkit.structure.EAD.Bibliography.class);
            handled.add(org.archiviststoolkit.structure.EAD.Bibref.class);        
            EADHelper.getClassesFromListandGroupOthers(aList,P.class,pList,notHandled,handled);
            Head h = null;
            String head = "";
            h = eadBibElement.getHead();
            if(h!=null)
                head = (String) EADHelper.getClassFromList(h.getContent(),String.class);
            //TODO work around to fix strange errror
            //SQLERROR: Driver can not re-execute prepared statement when a parameter 
            //          has been changed from a streaming type to an intrinsic data type 
            //          without calling clearParameters() first.
            //if (head==null)head = "";            
            //appBibElement.setTitle(head);
            EADHelper.setProperty(appBibElement,"title",head);
            String nh = EADHelper.ArrayListofNodesToString(notHandled);
            //TODO work around to fix strange errror
            //SQLERROR: Driver can not re-execute prepared statement when a parameter 
            //          has been changed from a streaming type to an intrinsic data type 
            //          without calling clearParameters() first.
            if (nh!=null)            
            appBibElement.setEadIngestProblem(nh);
            String description = null;;
            description = EADHelper.ArrayListofNodesToString(pList);
            if (description!=null)
                description = description.replaceAll("\n","").replaceAll("<p>","").replaceAll("</p>","\n\n");
            //TODO work around to fix strange errror
            //SQLERROR: Driver can not re-execute prepared statement when a parameter 
            //          has been changed from a streaming type to an intrinsic data type 
            //          without calling clearParameters() first.
            if (description==null)description = "";                
                EADHelper.setProperty(appBibElement,"note",description);
                //appBibElement.setNote(description);
                
            try{
                String audience = "";
                audience = (String)PropertyUtils.getProperty(o, "audience");
                if(StringHelper.isNotEmpty(audience) && audience.equalsIgnoreCase("internal")){
                    appBibElement.setInternalOnly(true);
             }
            }   
            catch (Exception e){
           	 e.printStackTrace();
            }
          
            ArrayList bibItems = (ArrayList) EADHelper.getClassesFromList(eadBibElement.getAddressOrChronlistOrList(),Bibref.class);
            if(bibItems!=null){
                Iterator bibItemsIterator = bibItems.iterator();
                int bibItemNumber=0;
                while(bibItemsIterator.hasNext()){
                    Bibref bibRef = (Bibref) bibItemsIterator.next();
                    BibItems bibItem = new BibItems();
                    bibItem.setSequenceNumber(bibItemNumber++);
                    //String bibR = EADHelper.removeTagsFromString(EADHelper.ObjectNodetoString(bibRef));
                    String bibR = EADHelper.ObjectNodetoString(bibRef);

                    EADHelper.setProperty(bibItem,"itemValue",bibR);
                    //bibItem.setItemValue(bibR);
                    bibItem.setStructuredDataParent(appBibElement);
                    appBibElement.addBibItem(bibItem);
                }
            }
            //appBibElement.setResource((Resoures)archDescription);
            int sequenceNum = archDescription.getRepeatingData().size();
            appBibElement.setSequenceNumber(sequenceNum);
            appBibElement.setPersistentId(EADInfo.getReferenceTOResources().getNextPersistentIdAndIncrement());
            EADInfo.addIdPairs(id,appBibElement.getPersistentId());
            archDescription.addRepeatingData(appBibElement);
            bibChildren = (ArrayList) EADHelper.getClassesFromList(eadBibElement.getAddressOrChronlistOrList(),org.archiviststoolkit.structure.EAD.Bibliography.class);
            handleBibliographySiblingNodes(archDescription,bibChildren, progressPanel);
            return;
        
    }
    public List getChildren(Object element)
    {
        return null;
    }
    
    private void handleBibliographySiblingNodes(ArchDescription res,ArrayList bibliographies, InfiniteProgressPanel progressPanel){
        if(bibliographies==null||bibliographies.size()==0)
            return;
        Iterator it = bibliographies.iterator();
        while (it.hasNext()) {
            processElement(res,(org.archiviststoolkit.structure.EAD.Bibliography)it.next(), progressPanel);
        }
        return;
    }    
}