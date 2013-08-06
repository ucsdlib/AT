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
 * @author Lee Mandell
 */


package org.archiviststoolkit.editor;

//==============================================================================
// Import Declarations
//==============================================================================

import javax.swing.*;

import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.model.*;

import java.util.EventObject;
import java.util.Vector;

/**
 * Editor for dealing with resources.
 */

public class ResourceEditor extends DomainEditor{

	/**
	 * Constructor.
	 *
	 * @param parentFrame the parentframe to render this editor to.
	 */
	public ResourceEditor(JFrame parentFrame) {
		super(Resources.class, parentFrame, new ResourceTreeViewer());
		((ResourceTreeViewer)editorFields).setParentEditors();
	}

	public ResourceEditor(JDialog parentFrame) {
		super(Resources.class, parentFrame, new ResourceTreeViewer());
		((ResourceTreeViewer)editorFields).setParentEditors();
	}

	public boolean commitChangesToCurrentResourceComponent(EventObject event) {
		return ((ResourceTreeViewer)editorFields).commitChangesToCurrentResourceComponent(event);
	}

	public String getNextPersistentId() {
		return ((ResourceTreeViewer)editorFields).getNextPersistentId();
	}

  // Method to update the JTree display
  public void doSaveSpecificUpdates() {
    ((ResourceTreeViewer)editorFields).updateJTree();
  }

  /**
	 * Displays the dialog box representing the editor.
	 *
	 * @return true if it displayed okay
	 */

	public int showDialog() {
		this.invalidate();
		this.validate();
		this.repaint();
		((ResourceTreeViewer)editorFields).setDisplayToFirstTab();
		return super.showDialog();
	}
}