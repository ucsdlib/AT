package org.archiviststoolkit.importer;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.structure.EAD.Daogrp;

public class HandleDaogrpAction implements Action{

    public  void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel){
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
//    ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing daoloc element");
    Daogrp daogrp = (Daogrp)o;
        Action action = null;
        EADInfo eadInfo = new EADInfo();
        for(Object eadElem:(ArrayList)getChildren(daogrp)) {
            action = 
                    eadInfo.getActionFromClass(eadElem);
            if (null != action) {
                action.processElement(archDescription, eadElem, progressPanel);
            }
        }
    }
    
    public List getChildren(Object element){
        Daogrp daogrp = (Daogrp)element;

        List al = new ArrayList(); 
        if (daogrp.getDaolocOrResourceOrArc()!=null)
            al = daogrp.getDaolocOrResourceOrArc();
            
        if(daogrp.getDaodesc()!=null)
            al.add(daogrp.getDaodesc());
        return al;
    }
}
