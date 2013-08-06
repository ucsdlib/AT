package org.archiviststoolkit.importer;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.structure.EAD.Seriesstmt;

public class HandleSeriesstmtAction implements Action
{
    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel)
    {   
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
//        ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing seriesstmt element");
        Seriesstmt seriesStatement = (Seriesstmt)o;
        if(seriesStatement!=null){
        String seriesStatementString = EADHelper.ObjectNodetoString(seriesStatement);
        seriesStatementString = EADHelper.removeTagsFromString(seriesStatementString);
        EADHelper.setProperty(archDescription,"series",seriesStatementString,Resources.class);
        //((Resources)archDescription).setSeries(seriesStatementString);
        }
        
    }
    public List getChildren(Object element)
    {
        return null;
    }
}