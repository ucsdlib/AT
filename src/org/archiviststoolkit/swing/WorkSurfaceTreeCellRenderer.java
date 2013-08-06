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

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * This cell renderer uses the worksurface defaults to allow specialist rendering.
 */

public class WorkSurfaceTreeCellRenderer extends DefaultTreeCellRenderer {
    
    /**
     * The method which configures how the tree cell will look.
     * @param tree the tree to be rendered
     * @param value the object to be rendered
     * @param selected is it selected
     * @param expanded is it expanded
     * @param leaf is it a leaf
     * @param row what row is it on
     * @param hasFocus does it have focus
     * @return the cell rendering component
     */
	 
    public final Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean selected, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
        
        super.getTreeCellRendererComponent(
    			tree, value, selected, expanded, leaf, row, hasFocus);
        
        if (value instanceof WorkSurface) {
            WorkSurface surface = (WorkSurface)value;
            setText(surface.getName());
            //this.setIcon(surface.getIcon());
            setLeafIcon(surface.getIcon());
            this.setIcon(surface.getIcon());
            this.setOpenIcon(surface.getIcon());
        }
        
        return(this);
    }
}

//=============================================================================
// $Log: WorkSurfaceTreeCellRenderer.java,v $
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
