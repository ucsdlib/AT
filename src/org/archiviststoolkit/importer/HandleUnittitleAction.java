package org.archiviststoolkit.importer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.structure.EAD.Unittitle;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.Resources;

public class HandleUnittitleAction implements Action {

    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel) {
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
//        ApplicationFrame.getInstance().updateProgressMessageSecondLine("importing unittitle element");
        //ArrayList handledElements = new ArrayList();
        //handledElements.add(Unitdate.class);
        //handledElements.add(JAXBElement.class);
        //System.out.println("IN UNITITLE");
        String unittitle = "";

        ArrayList utc = (ArrayList)getChildren(o);
        Iterator it = null;
        Object eadElem = null;
        Action action = null;
        EADInfo eadInfo = new EADInfo();
        it = utc.iterator();
        while (it.hasNext()) {
            eadElem = it.next();

            //if (handledElements.contains(eadElem.getClass())) {System.out.println("HERE0");
                
                action = eadInfo.getActionFromClass(eadElem);
                if (null != action && action instanceof HandleUnitdateAction){
                    action.processElement(archDescription, eadElem, progressPanel);
                }
            //} 
            else {
                if (eadElem instanceof String) {
                    unittitle = unittitle + eadElem;
                } else {
                    unittitle = 
                            unittitle + EADHelper.ObjectNodetoString(eadElem);
                }
            }
        }
        EADHelper.setProperty(archDescription, "title", unittitle, 
                              Resources.class);

    }

    public List getChildren(Object element) {
        return ((Unittitle)element).getContent();
    }
}
