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

public class Handle240Action implements MARCXMLAction
{
    public static int titlePrecedence = 3;

    public void processElement(Resources resource, Object o, InfiniteProgressPanel progressPanel) throws UnsupportedRepeatingDataTypeException {
		DataFieldType dataField = (DataFieldType) o;
		String titles[] = {"a","b"};
		Vector <String> titlesV;
		titlesV = MARCIngest.arrayToVector(titles);
		String title = MARCIngest.getSpecificSubCodeValuesAsDelimitedString(dataField,titlesV,",");
		if(MARCIngest.resourceTitle==null){
			resource.setTitle(title);
			MARCIngest.resourceTitle = (DataFieldType)o;
			MARCIngest.resourceTitlePriority=this.titlePrecedence;
		}
		else if(MARCIngest.resourceTitle!=null && MARCIngest.resourceTitlePriority<this.titlePrecedence){
			DataFieldType resTitle = MARCIngest.getResourceTitleTag();
			MARCIngest.resourceTitle = (DataFieldType)o;
			MARCIngest.resourceTitlePriority=this.titlePrecedence;
			MARCIngest.processElement(resTitle,resource,true);
			resource.setTitle(title);
		}
		else{
			NotesEtcTypes noteType = NoteEtcTypesUtils.lookupNoteEtcTypeByCannonicalName("General note");
			ArchDescriptionNotes adn = new ArchDescriptionNotes(resource,"Uniform Title",NotesEtcTypes.DATA_TYPE_NOTE,resource.getRepeatingData().size()+1,noteType,title);
			resource.addRepeatingData(adn);
		}

	}
    public List getChildren(Object element)
    {
        return null;
    }
}
