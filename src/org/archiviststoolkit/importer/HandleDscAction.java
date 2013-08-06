package org.archiviststoolkit.importer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.structure.EAD.Dsc;

public class HandleDscAction implements Action {
    public  void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel){
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
//        ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing dsc element");
        ArrayList dscChildren = (ArrayList)getChildren(o);
        Iterator it=null;
        it = dscChildren.iterator();
        Object eadElem;
        EADInfo eadInfo = new EADInfo();
        Action action = null;
        while(it.hasNext()){
            eadElem = it.next();
            action = eadInfo.getActionFromClass(eadElem);
            if(null!=action){
                action.processElement(archDescription,eadElem, progressPanel);
            }
            else{
               // archDescription.set
            }
        }
    }
    
    public List getChildren(Object element){
        List list1 = ((Dsc)element).getMBlocks();
        List dscList = null;
        dscList  = ((Dsc)element).getDsc();
        List list2 = ((Dsc)element).getCAndThead();
        List sum = new ArrayList();
        Iterator it1;
        Iterator it2;
        Iterator dscIt;
        Object obj = null;
        it1  = list1.iterator();
        it2 = list2.iterator();
        dscIt = dscList.iterator();
        while(it1.hasNext()){
            sum.add(it1.next());
        }
        while(it2.hasNext()){
            sum.add(it2.next());
        } 
        while(dscIt.hasNext()){
            obj = dscIt.next();
            System.out.println(obj.getClass().getName());
            sum.add(obj);
        }        
        return sum;
}
}