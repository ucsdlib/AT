package org.archiviststoolkit.importer;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.structure.EAD.Eadheader;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

public class HandleEadheaderAction implements Action {
    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel) {
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
        String findingAidStatus = "";
        findingAidStatus = ((Eadheader)o).getFindaidstatus();
        EADHelper.setProperty(archDescription, "findingAidStatus", 
                              findingAidStatus, Resources.class);

        EADInfo eadInfo = new EADInfo();
        for (Object eadElem: getChildren(o)) {
            Action a = 
                eadInfo.getActionFromClass(eadElem);
            if (progressPanel != null && progressPanel.isProcessCancelled()) {
                return;
            } else if (a != null) {
                a.processElement(archDescription, eadElem, progressPanel);
            }
        }
    }

    public List getChildren(Object element) {
        List a = new ArrayList();
        Eadheader eadHeader = (Eadheader)element;
        if (eadHeader.getEadid() != null)
            a.add(eadHeader.getEadid());
        if (eadHeader.getFiledesc() != null)
            a.add(eadHeader.getFiledesc());
        if (eadHeader.getProfiledesc() != null)
            a.add(eadHeader.getProfiledesc());
        if (eadHeader.getRevisiondesc() != null)
            a.add(eadHeader.getRevisiondesc());
        return a;
    }
}
