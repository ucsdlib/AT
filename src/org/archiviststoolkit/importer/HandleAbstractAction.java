package org.archiviststoolkit.importer;

import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.commons.beanutils.PropertyUtils;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.ArchDescriptionNotes;
import org.archiviststoolkit.model.UnsupportedRepeatingDataTypeException;
import org.archiviststoolkit.structure.EAD.Abstract;
import org.archiviststoolkit.structure.NotesEtcTypes;
import org.archiviststoolkit.util.NoteEtcTypesUtils;
import org.archiviststoolkit.util.StringHelper;

public class HandleAbstractAction implements Action {
    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel) {
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
//        ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing Abstract element");
        ArrayList abstractChildren = (ArrayList)getChildren(o);
        StringBuffer abstractString = new StringBuffer();
        Iterator it = null;
        it = abstractChildren.iterator();
        Object ob = null;
        while (it.hasNext()) {
            ob = it.next();
            if (ob instanceof String)
                abstractString.append((String)ob);
            else
                abstractString.append(EADHelper.ObjectNodetoString(ob));
        }

        String label = null;
        label = ((Abstract)o).getLabel();

        ArchDescriptionNotes adn = new ArchDescriptionNotes(archDescription);
        try {
            NotesEtcTypes type = 
                NoteEtcTypesUtils.lookupNoteEtcTypeByCannonicalName("Abstract");
            PropertyUtils.setProperty(adn, "notesEtcType", type);

            String audience = "";
            audience = (String)PropertyUtils.getProperty(o, "audience");
            if(StringHelper.isNotEmpty(audience) && audience.equalsIgnoreCase("internal")){
                adn.setInternalOnly(true);
            }
        
        
        } catch (UnsupportedRepeatingDataTypeException e) {
            e.printStackTrace();
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return;
        }
        

        
        //adn.setNoteType("Abstract");
        EADHelper.setProperty(adn, "noteContent", abstractString.toString(), 
                              ArchDescriptionNotes.class);
        //adn.setNoteContent(abstractString.toString());
        if (label != null)
            EADHelper.setProperty(adn, "title", label, 
                                  ArchDescriptionNotes.class);
        //adn.setTitle(label);
        else
            EADHelper.setProperty(adn, "title", "Abstract", 
                                  ArchDescriptionNotes.class);
        //adn.setTitle("Abstract");
        adn.setPersistentId(EADInfo.getReferenceTOResources().getNextPersistentIdAndIncrement());
        String id = ((Abstract)o).getId();
        EADInfo.addIdPairs(id, adn.getPersistentId());
        adn.setSequenceNumber(EADInfo.sequence++);
        EADHelper.setProperty(adn, "repeatingDataType", "Note", 
                              ArchDescriptionNotes.class);
        //adn.setRepeatingDataType("Note");
        archDescription.addRepeatingData(adn);
    }

    public List getChildren(Object element) {
        return ((Abstract)element).getContent();
    }
}
