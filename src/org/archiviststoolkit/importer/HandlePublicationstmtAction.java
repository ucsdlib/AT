package org.archiviststoolkit.importer;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.structure.EAD.Date;
import org.archiviststoolkit.structure.EAD.Publicationstmt;

public class HandlePublicationstmtAction implements Action
{
	private boolean debug = false;

    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel)
    {   
//        ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing publicationstmt element");
		if (debug) {
			System.out.println("in PUBSTMT");
		}
		if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
            
            List dateetc = ((Publicationstmt)o).getPublisherOrDateOrAddress();
            if(dateetc!=null){
                Date date = (Date)EADHelper.getClassFromList(dateetc,Date.class);    
                if(date!=null){
                    String dateString = EADHelper.ObjectNodetoString(date);
                    ((Resources)archDescription).setFindingAidDate(dateString);
                }
            }
    }
    public List getChildren(Object element)
    {
        return null;
    }
}