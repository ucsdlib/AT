package org.archiviststoolkit.importer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.bind.JAXBElement;

import org.apache.commons.beanutils.PropertyUtils;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.IndexItems;
import org.archiviststoolkit.structure.EAD.Head;
import org.archiviststoolkit.structure.EAD.Indexentry;
import org.archiviststoolkit.structure.EAD.P;
import org.archiviststoolkit.structure.EAD.Ref;
import org.archiviststoolkit.util.StringHelper;

public class HandleIndexAction implements Action
{
    public void processElement(ArchDescription archDescription, Object indexx, InfiniteProgressPanel progressPanel){
        if(indexx instanceof JAXBElement)
            indexx = ((JAXBElement)indexx).getValue();
//        ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing index element");
        org.archiviststoolkit.model.Index index = new org.archiviststoolkit.model.Index(archDescription);
        org.archiviststoolkit.structure.EAD.Index eadIndex = new org.archiviststoolkit.structure.EAD.Index();
        Iterator it3;
        ArrayList pList = new ArrayList();
        ArrayList notHandled = new ArrayList();
        ArrayList <Class> handled = new ArrayList <Class> ();
        handled.add(org.archiviststoolkit.structure.EAD.Bibliography.class);
        handled.add(org.archiviststoolkit.structure.EAD.Bibref.class);
            eadIndex = (org.archiviststoolkit.structure.EAD.Index) indexx;
            EADHelper.getClassesFromListandGroupOthers(eadIndex.getMBlocks(),P.class,pList,notHandled,handled);
            Head h = eadIndex.getHead();
            String description = EADHelper.ArrayListofNodesToString(pList);
            System.out.println(description);
            index.setNote(EADHelper.removeTagsFromString(description));
            
            try{
                String audience = "";
                audience = (String)PropertyUtils.getProperty(indexx, "audience");
                if(StringHelper.isNotEmpty(audience) && audience.equalsIgnoreCase("internal")){
                    index.setInternalOnly(true);
             }
            }   
            catch (Exception e){
           	 e.printStackTrace();
            }
            
            if(h!=null)
                index.setTitle((String)EADHelper.getClassFromList(h.getContent(),String.class));
            it3 = eadIndex.getIndexentry().iterator();
            int indexItemNumber=0;
            Object obj = null;
            Indexentry indexEntry = null;
            while(it3.hasNext()){
                IndexItems indexItem = new IndexItems();
                if((obj=it3.next()) instanceof Indexentry){
                indexEntry = (Indexentry)obj;
                //indexItem.setItemValue(EADHelper.removeTagsFromString((EADHelper.ObjectNodetoString(obj))));
                handleIndexEntry(indexItem, indexEntry);
                //indexItem.setItemType("Subject");
                indexItem.setSequenceNumber(indexItemNumber++);
                index.addIndexItem(indexItem);
                indexItem.setStructuredDataParent(index);
                }
            }
                int sequenceNum = archDescription.getRepeatingData().size();
                index.setSequenceNumber(sequenceNum);
                index.setPersistentId(EADInfo.getReferenceTOResources().getNextPersistentIdAndIncrement());
                EADInfo.addIdPairs(eadIndex.getId(),index.getPersistentId());
                archDescription.addRepeatingData(index);
    }
    
    private void handleIndexEntry(IndexItems indexItem, Indexentry indexEntry){
        Vector nameTypes = new Vector();
        addToList(nameTypes,indexEntry.getCorpname());
        addToList(nameTypes,indexEntry.getFamname());
        addToList(nameTypes,indexEntry.getFunction());
        addToList(nameTypes,indexEntry.getGenreform());
        addToList(nameTypes,indexEntry.getGeogname());
        addToList(nameTypes,indexEntry.getName());
        addToList(nameTypes,indexEntry.getOccupation());
        addToList(nameTypes,indexEntry.getPersname());
        addToList(nameTypes,indexEntry.getSubject());
        addToList(nameTypes,indexEntry.getTitle());

        List list = null;
        
        if(indexEntry.getNamegrp()!=null)
            list = indexEntry.getNamegrp().getCorpnameOrFamnameOrGeogname();
        
        if(list!=null){
            for(int a=0;a<list.size();a++){
                addToList(nameTypes,list.get(a));
            }
        }
        
        String itemValue = "";
        String type = "";
        for(Object obj:nameTypes){
            ArrayList line = (ArrayList)EADHelper.getProperty(obj,"content");
            String lineS = EADHelper.ArrayListofNodesToString((line));
            itemValue = itemValue+lineS;
            type = (String)EADHelper.getIndexTypeMap().get(obj.getClass());
        }
        indexItem.setItemValue(itemValue);
        if(type==null || type.length()==0)
        	type = "Subject";
       	indexItem.setItemType(type);
        Ref ref = indexEntry.getRef();
        String target = "";
        String targetText = "";
        if(ref!=null){
            Object refTarget =  ref.getTarget();
            ArrayList refContent = (ArrayList)ref.getContent();
            String refContentS = EADHelper.ArrayListofNodesToString(refContent);
            //System.out.println("ref Target = "+refTarget.toString());
            //indexItem.setReference(refTarget.toString());
            indexItem.setReferenceText(refContentS);
        }
        
        
    }
    
    private void addToList(Vector v, Object obj){
    if(obj!=null)
        v.add(obj);
    }
    
    
    public List getChildren(Object element){
    return null;
    }
}