package org.archiviststoolkit.importer.MARCXML;
import java.util.List;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.model.UnsupportedRepeatingDataTypeException;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

public interface MARCXMLAction {

	public abstract void processElement(Resources resource, Object o, InfiniteProgressPanel progressPanel) throws UnsupportedRepeatingDataTypeException;
	public abstract List getChildren(Object element);
}
