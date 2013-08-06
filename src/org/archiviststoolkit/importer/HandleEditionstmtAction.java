package org.archiviststoolkit.importer;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.structure.EAD.Editionstmt;

public class HandleEditionstmtAction implements Action
{
    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel)
    {
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
//        ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing editionstmt element");
        Editionstmt editionStatement = ((Editionstmt)o);
        if(editionStatement!=null){
        String editionStatementString = EADHelper.ObjectNodetoString(editionStatement);
         editionStatementString = EADHelper.removeTagsFromString(editionStatementString);
         if(editionStatementString!=null)
             editionStatementString = editionStatementString.replaceAll("\n","").replaceAll("<p>","").replaceAll("</p>","\n\n");
        EADHelper.setProperty(archDescription,"editionStatement",editionStatementString,Resources.class);
        //((Resources)archDescription).setEditionStatement(editionStatementString);
        }
    }
    public List getChildren(Object element)
    {
        return null;
    }
}