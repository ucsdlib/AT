package org.archiviststoolkit.importer;

import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.commons.beanutils.PropertyUtils;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.util.NoteEtcTypesUtils;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.ArchDescriptionNotes;
import org.archiviststoolkit.model.UnsupportedRepeatingDataTypeException;
import org.archiviststoolkit.structure.EAD.Physloc;
import org.archiviststoolkit.structure.NotesEtcTypes;

public class HandlePhyslocAction implements Action {
    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel) {
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
//        ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing physloc element");
        ArrayList physlocChildren = (ArrayList)getChildren(o);
        StringBuffer physlocString = new StringBuffer();
        Iterator it = null;
        it = physlocChildren.iterator();
        Object ob = null;
        while (it.hasNext()) {
            ob = it.next();
            if (ob instanceof String)
                physlocString.append((String)ob);
            else
                physlocString.append(EADHelper.ObjectNodetoString(ob));
        }
        String label = null;
        label = ((Physloc)o).getLabel();
        ArchDescriptionNotes adn = new ArchDescriptionNotes(archDescription);
        //adn.setNoteType("Location note");

        try {
            NotesEtcTypes type = 
                NoteEtcTypesUtils.lookupNoteEtcTypeByCannonicalName("Location note");
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
        EADHelper.setProperty(adn, "noteContent", physlocString.toString());
        //adn.setNoteContent(physlocString.toString());
        if (label != null)
            EADHelper.setProperty(adn, "title", label);
        //adn.setTitle(label);
        else
            EADHelper.setProperty(adn, "title", "Location note");
        //adn.setTitle("Location Note");    
        adn.setSequenceNumber(EADInfo.sequence++);
        adn.setPersistentId(EADInfo.getReferenceTOResources().getNextPersistentIdAndIncrement());
        EADInfo.addIdPairs(((Physloc)o).getId(), adn.getPersistentId());
        adn.setRepeatingDataType("Note");
        archDescription.addRepeatingData(adn);
    }

    public List getChildren(Object element) {
        return ((Physloc)element).getContent();
    }
}
