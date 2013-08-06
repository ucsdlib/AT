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
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.swing.StandardEditor;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

import javax.swing.*;

/**
 * Editor for dealing with the contact domain object.
 */

public class ArchDescriptionRepeatingDataEditor extends DomainEditor {

	private ArchDescriptionNotesFields notesEditorFields;
	private BibliographyFields bibliographyEditorFields;
	private ChronologyFields chronologyEditorFields;
	private IndexFields indexEditorFields;
	private ExternalReferenceFields externalReferenceFields;
	private BasicNoteFields basicNoteFields;
	private ListOrderedFields listOrderedFields;
	private ListDefinitionFields listDefinitionFields;

	private ResourceEditor resourceEditor;

	/**
	 * Constructor.
	 *
	 * @param parentFrame the parentframe to render this editor to.
	 */
	public ArchDescriptionRepeatingDataEditor(JFrame parentFrame) {
		super(ArchDescriptionRepeatingData.class, parentFrame);
		init();

		this.setContentPanel(editorFields);
	}

	/**
	 * Constructor.
	 *
	 * @param parentFrame the parentframe to render this editor to.
	 */
	public ArchDescriptionRepeatingDataEditor(JDialog parentFrame) {
		super(ArchDescriptionRepeatingData.class, parentFrame);
		init();

	}

	private void init() {
		notesEditorFields = new ArchDescriptionNotesFields(this);
		notesEditorFields.setNotesEditor(this);
		bibliographyEditorFields = new BibliographyFields(this);
		chronologyEditorFields = new ChronologyFields(this);
		indexEditorFields = new IndexFields(this);
		externalReferenceFields = new ExternalReferenceFields(this);
		basicNoteFields = new BasicNoteFields(this);
		listOrderedFields = new ListOrderedFields(this);
		listDefinitionFields = new ListDefinitionFields(this);
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
		return super.showDialog();
	}

	public void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		ArchDescriptionRepeatingData archDescriptionRepeatingDataModel = (ArchDescriptionRepeatingData)model;
		if (archDescriptionRepeatingDataModel.getResource() != null) {
			setMainHeaderColorAndTextByClass(Resources.class);
		} else if (archDescriptionRepeatingDataModel.getResourceComponent() != null) {
			setMainHeaderColorAndTextByClass(ResourcesComponents.class);
		} else if (archDescriptionRepeatingDataModel.getAccession() != null) {
			setMainHeaderColorAndTextByClass(Accessions.class);
		} else if (archDescriptionRepeatingDataModel.getDigitalObject() != null) {
			setMainHeaderColorAndTextByClass(DigitalObjects.class);
		} else if (archDescriptionRepeatingDataModel.getParentNote() != null) {
			setMainHeaderColorAndTextByClass(ArchDescriptionNotes.class);
		}

		if (model instanceof ArchDescriptionNotes) {
			ArchDescriptionNotes noteModel = (ArchDescriptionNotes)model;
			if (noteModel.getBasic()) {
				editorFields = basicNoteFields;
				this.setMainSubHeaderLabel(StandardEditor.MODULE_SUB_HEADER_TEXT);
			} else {
				editorFields = notesEditorFields;
				this.setMainSubHeaderLabel(StandardEditor.MODULE_SUB_HEADER_NOTES);
			}
		} else if (model instanceof Bibliography) {
			editorFields = bibliographyEditorFields;
			this.setMainSubHeaderLabel(StandardEditor.MODULE_SUB_HEADER_BIBLIOGORAPHY);
		} else if (model instanceof ChronologyList) {
			editorFields = chronologyEditorFields;
			this.setMainSubHeaderLabel(StandardEditor.MODULE_SUB_HEADER_CHRONOLOGY);
		} else if (model instanceof Index) {
			editorFields = indexEditorFields;
			this.setMainSubHeaderLabel(StandardEditor.MODULE_SUB_HEADER_INDEX);
		} else if (model instanceof ListOrdered) {
			editorFields = listOrderedFields;
			this.setMainSubHeaderLabel(StandardEditor.MODULE_SUB_HEADER_LIST_ORDERED);
		} else if (model instanceof ListDefinition) {
			editorFields = listDefinitionFields;
			this.setMainSubHeaderLabel(StandardEditor.MODULE_SUB_HEADER_LIST_DEFINITION);
		} else if (model instanceof ExternalReference) {
			editorFields = externalReferenceFields;
			this.setMainSubHeaderLabel(StandardEditor.MODULE_SUB_HEADER_EXTERNAL_REFERENCE);
			//if this is linked to an accession hide the show and actuate stuff

			if (((ExternalReference)model).getAccession() != null) {
			   externalReferenceFields.setVisibleShowAndActuate(false);
			} else {
			   externalReferenceFields.setVisibleShowAndActuate(true);
			}
		}

		this.setContentPanel(editorFields);
//		this.pack();
		this.invalidate();
		this.validate();
		this.repaint();
		super.setModel(model, progressPanel);
	}

	public ResourceEditor getResourceEditor() {
		return resourceEditor;
	}

	public void setResourceEditor(ResourceEditor resourceEditor) {
		this.resourceEditor = resourceEditor;
	}
}
