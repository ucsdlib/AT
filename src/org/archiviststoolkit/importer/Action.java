package org.archiviststoolkit.importer;
import java.util.List;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

public interface Action {

    public abstract void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel);
    public abstract List getChildren(Object element);
}
