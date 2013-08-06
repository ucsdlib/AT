package org.archiviststoolkit.importer;


import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.ListDefinition;
import org.archiviststoolkit.model.ListDefinitionItems;
import org.archiviststoolkit.model.ListOrdered;
import org.archiviststoolkit.model.ListOrderedItems;
import org.archiviststoolkit.structure.EAD.Defitem;
import org.archiviststoolkit.structure.EAD.Head;
import org.archiviststoolkit.structure.EAD.Item;
import org.archiviststoolkit.structure.EAD.List;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

public class HandleListAction implements Action
{
    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel)
    {
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
        List eadList = (List)o;
        String type = eadList.getType();
        if(type==null){
            if(eadList.getDefitem().size()>0){
            	type="deflist";
            }
            else
            	type="ordered";
        }
        String title="";
        Head h = eadList.getHead();
        if(h!= null)
            title = (String)EADHelper.getClassFromList(h.getContent(),String.class);
        String numeration = eadList.getNumeration();

        if(type.equals("deflist")){
            ListDefinition listDefinition = new ListDefinition();
            listDefinition.setParentNote(EADInfo.getReferenceTOParentNotes());
            listDefinition.setSequenceNumber(EADInfo.getReferenceTOParentNotes().getChildren().size());
            EADInfo.getReferenceTOParentNotes().addRepeatingData(listDefinition);
            listDefinition.setPersistentId(EADInfo.getReferenceTOResources().getNextPersistentIdAndIncrement());
            EADInfo.addIdPairs(eadList.getId(),listDefinition.getPersistentId());
            if(title!=null)
                listDefinition.setTitle(title);
            for(Defitem defItem:eadList.getDefitem()){
                ListDefinitionItems listDefItems = handleListDefinitionItem(defItem);
                if(listDefItems!=null){
                    listDefItems.setStructuredDataParent(listDefinition);
                    listDefItems.setSequenceNumber(listDefinition.getItems().size());
                    listDefinition.addListItem(listDefItems);

                }
            }
        }
        
        else{
            ListOrdered listOrdered = new ListOrdered();
            listOrdered.setParentNote(EADInfo.getReferenceTOParentNotes());
System.out.println("list seq = "+EADInfo.getReferenceTOParentNotes().getChildren().size());
            listOrdered.setSequenceNumber(EADInfo.getReferenceTOParentNotes().getChildren().size());
            EADInfo.getReferenceTOParentNotes().addRepeatingData(listOrdered);
            listOrdered.setPersistentId(EADInfo.getReferenceTOResources().getNextPersistentIdAndIncrement());
            EADInfo.addIdPairs(eadList.getId(),listOrdered.getPersistentId());
            if(title!=null)
                listOrdered.setTitle(title);
            if(numeration!=null)
                listOrdered.setNumeration(numeration);
            for(Item item:eadList.getItem()){
               ListOrderedItems listOrderedItem = handleListOrderedItem(item);
               if(listOrderedItem!=null){
                listOrderedItem.setStructuredDataParent(listOrdered);
                listOrderedItem.setSequenceNumber(listOrdered.getItems().size());
                listOrdered.addListItem(listOrderedItem);
                
               }
            }
        }

    }
    
    private ListOrderedItems handleListOrderedItem(Item item){
        ListOrderedItems listItem = new ListOrderedItems();
        String value = EADHelper.ObjectNodetoString(item);
        //EADHelper.setProperty(listItem,"itemValue",EADHelper.removeTagsFromString(value));
        EADHelper.setProperty(listItem,"itemValue",EADHelper.ObjectNodetoString(value));

        return listItem;
    }
    
    private ListDefinitionItems handleListDefinitionItem(Defitem defItem){
        ListDefinitionItems listDefItem = new ListDefinitionItems();
        String value = EADHelper.ObjectNodetoString(defItem.getItem());
        EADHelper.setProperty(listDefItem,"itemValue",EADHelper.removeTagsFromString(value));
        //listDefItem.setItemValue(EADHelper.removeTagsFromString(value));
        value = EADHelper.ObjectNodetoString(defItem.getLabel());
        EADHelper.setProperty(listDefItem,"label",value);
        //listDefItem.setLabel(EADHelper.removeTagsFromString(value));
        if((StringHelper.isNotEmpty(listDefItem.getItemValue())) || (StringHelper.isNotEmpty(listDefItem.getLabel())))
            return listDefItem;
        else 
            return null;
    }
    public java.util.List getChildren(Object element)
    {
        return null;
    }
}