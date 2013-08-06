package org.archiviststoolkit.importer;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.structure.EAD.Filedesc;

public class HandleFiledescAction implements Action {
    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel) {
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
//        ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing filedesc element");

        EADInfo eadInfo = new EADInfo();        
        for(Object eadElem:getChildren(o)){
            Action a = eadInfo.getActionFromClass(eadElem);
                if(a!=null){
                    a.processElement(archDescription,eadElem, progressPanel);
                }
        }
    }

    public List getChildren(Object element) {
        List list = new ArrayList();
        Filedesc filedesc = (Filedesc)element;
        if(filedesc.getEditionstmt()!=null)
            list.add(filedesc.getEditionstmt());
        if(filedesc.getNotestmt()!=null)
            list.add(filedesc.getNotestmt());
        if(filedesc.getPublicationstmt()!=null)
            list.add(filedesc.getPublicationstmt());
        if(filedesc.getSeriesstmt()!=null)
            list.add(filedesc.getSeriesstmt());
        if(filedesc.getTitlestmt()!=null)
            list.add(filedesc.getTitlestmt());
       System.out.println("--------------------->"+EADHelper.ObjectNodetoString(filedesc.getTitlestmt()));
        return list;
    }
}
