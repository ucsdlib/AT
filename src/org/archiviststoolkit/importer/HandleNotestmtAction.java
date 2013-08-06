package org.archiviststoolkit.importer;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.structure.EAD.Note;
import org.archiviststoolkit.structure.EAD.Notestmt;

public class HandleNotestmtAction implements Action
{
    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel)
    {   
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
//        ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing notestmt element");
        Notestmt notestmt = ((Notestmt)o);
        List<Note> notes;
        String noteString = "";
        if(notestmt!=null){
            notes = notestmt.getNote();
        for(Note note:notes){
            noteString = EADHelper.ObjectNodetoString(note);
            noteString = EADHelper.removeTagsFromString(noteString);
            noteString = noteString.replaceAll("\n","").replaceAll("<p>","").replaceAll("</p>","\n\n");
            EADHelper.setProperty(archDescription,"findingAidNote",((Resources)archDescription).getFindingAidNote()+noteString,Resources.class);
            //((Resources)archDescription).setFindingAidNote(((Resources)archDescription).getFindingAidNote()+noteString);
        }
        }
        
    }
    public List getChildren(Object element)
    {
        return null;
    }
}