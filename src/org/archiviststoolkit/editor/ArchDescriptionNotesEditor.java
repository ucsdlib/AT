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
 * Programmer: Lee Mandell
 */

package org.archiviststoolkit.editor;

import org.archiviststoolkit.mydomain.DomainEditor;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.model.ArchDescriptionNotes;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

import javax.swing.*;
import java.util.Vector;

/**
 * Editor for dealing with the contact domain object.
 */

public class ArchDescriptionNotesEditor extends DomainEditor {

    /**
     * Constructor.
     *
     * @param parentFrame the parentframe to render this editor to.
     */
    public ArchDescriptionNotesEditor(JDialog parentFrame) {
        super(ArchDescriptionNotes.class, parentFrame, "Resources Components Notes");

        editorFields = new ArchDescriptionNotesFields(this);
		((ArchDescriptionNotesFields)editorFields).setNotesEditor(this);
        this.setContentPanel(editorFields);
    }

	/**
	 * Sets the model for this editor.
	 *
	 * @param model the model to be used
	 */

	public void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		this.invalidate();
		this.validate();
		this.repaint();
	}

	protected void setNotesEtcPopupValues(Vector values) {
		((ArchDescriptionNotesFields)editorFields).setNotesEtcDropDownValues(values);
	}
}
