package org.archiviststoolkit.importer;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.ChronologyItems;
import org.archiviststoolkit.model.ChronologyList;
import org.archiviststoolkit.model.Events;
import org.archiviststoolkit.structure.EAD.Chronitem;
import org.archiviststoolkit.structure.EAD.Chronlist;
import org.archiviststoolkit.structure.EAD.Date;
import org.archiviststoolkit.structure.EAD.Event;
import org.archiviststoolkit.structure.EAD.Head;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

public class HandleChronListAction implements Action
{
    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel)
    {
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
        ChronologyList chronList = new ChronologyList();
        ChronologyItems chronItem = new ChronologyItems();
        Chronitem eadChronItem = new Chronitem();
        Chronlist eadChronlist = new Chronlist();
        chronList.setParentNote(EADInfo.getReferenceTOParentNotes());
        int sequenceNum = EADInfo.getReferenceTOParentNotes().getChildren().size();
        chronList.setSequenceNumber(sequenceNum);
        chronList.setPersistentId(EADInfo.getReferenceTOResources().getNextPersistentIdAndIncrement());;
        EADInfo.addIdPairs(((Chronlist)o).getId(),chronList.getPersistentId());
        EADInfo.getReferenceTOParentNotes().addRepeatingData(chronList);
        Iterator i2;
            eadChronlist = (Chronlist)o;
            Head h = eadChronlist.getHead();
            if(h!=null)
                EADHelper.setProperty(chronList,"title",(String)EADHelper.getClassFromList(h.getContent(),String.class));
                //chronList.setTitle((String)EADHelper.getClassFromList(h.getContent(),String.class));
            i2 = eadChronlist.getChronitem().iterator();
            Object obj=null;
            Date date = null;
            Event event = null;
            int sequence=0;
            while(i2.hasNext()){
                Vector<Event> events = new Vector<Event>();
                chronItem = new ChronologyItems();
                //TODO fix this
                if((obj=i2.next()) instanceof Chronitem){
                eadChronItem = (Chronitem) obj;
                date = eadChronItem.getDate();
                event = eadChronItem.getEvent();
                if(event!=null)
                    events.add(event);
                if(eadChronItem.getEventgrp()!=null){    
                for(Event evnt:eadChronItem.getEventgrp().getEvent()){
                    events.add(evnt);
                }
                }
                
                chronItem.setEventDate(EADHelper.removeTagsFromString((EADHelper.ObjectNodetoString(date))));

                int eventsequence = 0;
                for(Event evnts:events){
                    Events e = new Events();
                    e.setEventDescription("eventD");
                    //e.setEventDescription(EADHelper.removeTagsFromString(EADHelper.ObjectNodetoString(evnts)));
                    e.setEventDescription(EADHelper.ObjectNodetoString(evnts));

                    e.setChronologyItem(chronItem);
                    e.setSequenceNumber(eventsequence++);
                    chronItem.addEvent(e);    
                }
                
                chronItem.setStructuredDataParent(chronList);
                chronItem.setSequenceNumber(sequence++);
                chronList.addChronologyItem(chronItem);
                }
            }

    }
    public List getChildren(Object element)
    {
        return null;
    }
}