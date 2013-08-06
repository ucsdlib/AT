/**
 * Archivists' Toolkit(TM) Copyright © 2005-2009 Regents of the University of California, New York University, & Five Colleges, Inc.
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
 * @author Nathan Stevens
 */


package org.archiviststoolkit.editor;

//==============================================================================
// Import Declarations
//==============================================================================

import javax.swing.*;

import org.archiviststoolkit.mydomain.DomainEditor;
import org.archiviststoolkit.model.DigitalObjects;


/**
 * Editor for dealing with dgital objects
 */

public class DigitalObjectEditor extends DomainEditor {

	/**
	 * Constructor.
	 *
	 * @param parentFrame the parentframe to render this editor to.
	 */
	public DigitalObjectEditor(JFrame parentFrame) {
		super(DigitalObjects.class, parentFrame, new DigitalObjectTreeViewer());

        // must call this to prevent bug in java 1.5 on Mac OSX
        editorFields = new DigitalObjectTreeViewer(this);
		setContentPanel(editorFields);
	}

	/**
	 * Constructor.
	 *
	 * @param parentFrame the parentframe to render this editor to.
	 */
	public DigitalObjectEditor(JDialog parentFrame) {
		super(DigitalObjects.class, parentFrame, new DigitalObjectTreeViewer());

        // must call this to prevent bug in java 1.5 on Mac OSX
        editorFields = new DigitalObjectTreeViewer(this);
		setContentPanel(editorFields);
	}

    /**
     * Method to update the JTree display
     */
    public void doSaveSpecificUpdates() {
        ((DigitalObjectTreeViewer)editorFields).updateJTree();
    }
}