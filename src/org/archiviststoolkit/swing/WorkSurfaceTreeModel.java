/**
 * Archivists' Toolkit(TM) Copyright © 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
 * All rights reserved.
 *
 * This software is free. You can redistribute it and / or modify it under the terms of the Educational Community License (ECL)
 * version 1.0 (http://www.opensource.org/licenses/ecl1.php)
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the ECL license for more details about permissions and limitations.
 *
 *
 * Archivists' Toolkit(TM)
 * http://www.archiviststoolkit.org
 * info@archiviststoolkit.org
 *
 */

package org.archiviststoolkit.swing;

//==============================================================================
// Import Declarations
//==============================================================================

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * An important part of the Workbench gui is the tree which displays all
 * accessible worksurfaces. This tree is supported by this the workbench
 * tree model
 */

public class WorkSurfaceTreeModel implements TreeModel {
    
	/** Class logger. */ 

    private static Logger logger = Logger.getLogger(WorkSurfaceTreeModel.class.getPackage().getName());

    /** The root string, not displayed but used internally. */
    
    private String root = "workbench";
    
    /** A list of all listeners to this tree model. */
    
    private List treeModelListeners = new ArrayList();
    
    /** The internal model implemented as a list. */
    
    private List internalModel = new ArrayList ();

    /**
     * Default constructor, currently does nothing.
     */
    
    public WorkSurfaceTreeModel () {        
    }
    
    /**
     * Add a new Item to the tree model.
     * @param worksurface the item to add
     */
    
    public final void add (final WorkSurface worksurface) {
        internalModel.add (worksurface);
        refresh ();
    }
    
    /**
     * Refresh any JTree components which are dependent on this
     * model. 
     */
    
    public final void refresh() {
        fireTreeStructureChanged(root);     
    }

    /**
     * Tell all the listeners that the tree has changed.
     * @param oldRoot the root from which this has changed
     */
    
    protected final void fireTreeStructureChanged (final Object oldRoot) {
        int length = treeModelListeners.size();
        
        TreeModelEvent modelEvent = new TreeModelEvent(this, 
                new Object[] {oldRoot});
        
        for (int i = 0; i < length; i++) {
            ((TreeModelListener)treeModelListeners.get(i)).treeStructureChanged(modelEvent);
        }
    }
    
    /**
     * Adds a listener for the TreeModelEvent posted after the tree changes.
     * @param listener the listener to add
     */
    
    public final void addTreeModelListener (final TreeModelListener listener) {
        treeModelListeners.add(listener);
    }
    
    /**
     * Removes a listener previously added with addTreeModelListener().
     * @param listener the listener to remove
     */
    
    public final void removeTreeModelListener(final TreeModelListener listener) {
        treeModelListeners.remove(listener);
    }
    
    /**
     * Get the object which exists as a child at index of the parent.
     * @param parent the parent object
     * @param index the index of the child
     * @return the object at the parents index
     */
    
    public final Object getChild(final Object parent, final int index) {
        try {
            if(parent == root) {
                return internalModel.get(index);
            }/* else if (parent instanceof WorkBenchFeature) {
                WorkBenchFeature feature = (WorkBenchFeature)parent;
                return (feature.getAllWorkSurfaces().get(index));
            }*/
        } catch(ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            logger.log(Level.SEVERE,"Tree tried to access beyond the internal model",arrayIndexOutOfBoundsException);
        }
        
        return null;
    }
    
    /**
     * Get the number of children this parent supports.
     * @param parent the parent for which we want the count
     * @return the number of children
     */
    
    public final int getChildCount(final Object parent) {
        if(parent == root) {
            return internalModel.size();
        } /*else if (parent instanceof WorkBenchFeature) {
            WorkBenchFeature feature = (WorkBenchFeature)parent;
            return (feature.getAllWorkSurfaces().size());
        }*/
        
        return 0;
    }
    
    /**
     * Get the child index of the named object.
     * @param parent the parent of the child were talking about
     * @param child the child which we are talking about
     * @return the index of the child
     */
    
    public final int getIndexOfChild(final Object parent, final Object child) {
        if(parent == root) {
            return internalModel.indexOf(child);
        } /* else if (parent instanceof WorkBenchFeature) {
            WorkBenchFeature feature = (WorkBenchFeature)parent;
            return feature.getAllWorkSurfaces().indexOf(child);
        }*/
        return 0;   
    }
    
    /**
     * Get the root object.
     * @return the root object
     */
    
    public final Object getRoot() {
        return root; 
    }
    
    /** 
     * Returns if this is a leaf object.
     * @param node the node which we are testing
     * @return true if the node is a leaf in the tree model
     */
    
    public final boolean isLeaf(final Object node) {
        
        if(node == root) {
            
            return false;
        } 
        return true;
    }
    
    /**
     * Notifys the model that the value for the path has changed.
     * Only implemented in this case to keep with the TreeModel contract.
     * @param path the treepath
     * @param newVal the value for this node
     */
    
    public final void valueForPathChanged(final TreePath path, final Object newVal) {
        logger.log(Level.INFO,"Who is calling this method, it should be redundant [valueForPathChanged]");
    }    
}

//=============================================================================
// $Log: WorkSurfaceTreeModel.java,v $
// Revision 1.1  2004/12/05 22:02:13  lejames
// First major cut of 1.2.0 including:
// 1. reworking of the workspace container to include selection from the sidebar
// 2. JNLP compliance for singleinstance management
// 3. Main Menubar now in place
//
//
//
//=============================================================================
// EOF
//=============================================================================
