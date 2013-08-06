package org.archiviststoolkit.importer.MARCXML;

import java.util.List;

import java.util.Vector;

import org.archiviststoolkit.model.ArchDescriptionNotes;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.model.UnsupportedRepeatingDataTypeException;
import org.archiviststoolkit.structure.MARCXML.DataFieldType;
import org.archiviststoolkit.structure.NotesEtcTypes;
import org.archiviststoolkit.util.NoteEtcTypesUtils;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

public class Handle357Action implements MARCXMLAction
{
    public void processElement(Resources resource, Object o, InfiniteProgressPanel progressPanel) throws UnsupportedRepeatingDataTypeException {
		DataFieldType dataField = (DataFieldType) o;
		String titles[] = {"a","b","c","g"};
		Vector <String> titlesV;
		titlesV = MARCIngest.arrayToVector(titles);
		String title = MARCIngest.getSpecificSubCodeValuesAsDelimitedString(dataField,titlesV,",");
		NotesEtcTypes noteType = NoteEtcTypesUtils.lookupNoteEtcTypeByCannonicalName("Conditions Governing Access note");
		ArchDescriptionNotes adn = new ArchDescriptionNotes(resource,"Originator Dissemination Control",NotesEtcTypes.DATA_TYPE_NOTE,resource.getRepeatingData().size()+1,noteType,title);
        adn.setPersistentId(resource.getNextPersistentIdAndIncrement());
        resource.addRepeatingData(adn);
	}
    public List getChildren(Object element)
    {
        return null;
    }
}