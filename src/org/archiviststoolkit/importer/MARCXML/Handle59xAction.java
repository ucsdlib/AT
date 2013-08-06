package org.archiviststoolkit.importer.MARCXML;

import java.util.List;

import org.archiviststoolkit.model.ArchDescriptionNotes;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.model.UnsupportedRepeatingDataTypeException;
import org.archiviststoolkit.structure.MARCXML.DataFieldType;
import org.archiviststoolkit.structure.NotesEtcTypes;
import org.archiviststoolkit.util.NoteEtcTypesUtils;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

public class Handle59xAction implements MARCXMLAction
{
    public void processElement(Resources resource, Object o, InfiniteProgressPanel progressPanel) throws UnsupportedRepeatingDataTypeException {
		DataFieldType dataField = (DataFieldType) o;
		String noteTitle = "Local Note";
		NotesEtcTypes noteType = NoteEtcTypesUtils.lookupNoteEtcTypeByCannonicalName("General note");
		String title = MARCIngest.getAllSubCodeValuesAsDelimitedString(dataField,"\n");
		ArchDescriptionNotes adn = new ArchDescriptionNotes(resource,noteTitle,NotesEtcTypes.DATA_TYPE_NOTE,resource.getRepeatingData().size()+1,noteType,title);
		resource.addRepeatingData(adn);
	}
    public List getChildren(Object element)
    {
        return null;
    }
}