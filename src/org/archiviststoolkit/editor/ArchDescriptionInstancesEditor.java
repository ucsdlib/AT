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
import org.archiviststoolkit.model.ArchDescriptionInstances;
import org.archiviststoolkit.model.ArchDescriptionAnalogInstances;
import org.archiviststoolkit.model.ArchDescriptionDigitalInstances;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.swing.StandardEditor;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Editor for dealing with the contact domain object.
 */

public class ArchDescriptionInstancesEditor extends DomainEditor {

	private DigitalObjectTreeViewer digitalEditorFields;
	private ArchDescriptionAnalogInstancesFields analogEditorFields;

//    /**
//     * Constructor.
//     *
//     * @param parentFrame the parentframe to render this editor to.
//     */
//    public ArchDescriptionInstancesEditor(JFrame parentFrame) {
//        super(ArchDescriptionInstances.class, parentFrame);
//        init();
//
//        this.setContentPanel(editorFields);
//    }

	/**
	 * Constructor.
	 *
	 * @param parentFrame the parentframe to render this editor to.
	 */
	public ArchDescriptionInstancesEditor(JDialog parentFrame) {
		super(ArchDescriptionInstances.class, parentFrame);
		init();

	}

	private void init() {
		analogEditorFields = new ArchDescriptionAnalogInstancesFields(this);
		digitalEditorFields = new DigitalObjectTreeViewer(this);
	}

	public void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		if (model instanceof ArchDescriptionAnalogInstances) {
			editorFields = analogEditorFields;
			this.setMainSubHeaderLabel(StandardEditor.MODULE_SUB_HEADER_RESOURCE_ANALOG_INSTANCE);
		} else if (model instanceof ArchDescriptionDigitalInstances) {
			editorFields = digitalEditorFields;
			this.setMainSubHeaderLabel(StandardEditor.MODULE_SUB_HEADER_RESOURCE_DIGITAL_INSTANCE);
		}

		this.setContentPanel(editorFields);
		this.pack();
		this.invalidate();
		this.repaint();
		super.setModel(model, progressPanel);
	}

	/**
	 * capture and handle action events.
	 *
	 * @param ae the action event
	 */

	public void actionPerformed(final ActionEvent ae) {
		if (editorFields == digitalEditorFields) {
			if (ae.getSource() == getOkButton() ||
					ae.getSource() == getNextButton() ||
					ae.getSource() == getPreviousButton() ||
					ae.getSource() == getFirstButton() ||
					ae.getSource() == getLastButton() ||
                    ae.getSource() == getOkAndAnotherButton()) {
				if (digitalEditorFields.commitChangesToCurrentDigitalObject(ae)) {
					super.actionPerformed(ae);
				}
			} else {
				super.actionPerformed(ae);
			}

		} else {
			super.actionPerformed(ae);
		}
	}

	public void setResourceInfo(Resources resource) {
		digitalEditorFields.setResourceInfo(resource);
	}

	public void commitChangesToCurrentRecord() {
//		digitalEditorFields.commitChangesToCurrentRecord();
	}

	/**
	 * Displays the dialog box representing the editor.
	 *
	 * @return true if it displayed okay
	 */

	public int showDialog() {
		digitalEditorFields.setDisplayToFirstTab();
		return super.showDialog();	//To change body of overridden methods use File | Settings | File Templates.
	}

}
