/**
 * Archivists' Toolkit(TM) Copyright � 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
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
 * Date: Jul 3, 2006
 * Time: 4:19:04 PM
 */

package org.archiviststoolkit.editor;

import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.util.SequencedObjectsUtils;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.swing.StandardEditor;
import org.archiviststoolkit.exceptions.DomainEditorCreationException;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public abstract class ArchDescriptionStructuredDataFields extends DomainEditorFields {

	protected abstract DomainSortedTable getItemsTable();

	protected abstract JButton getAddItemButton();

	protected void init() {
		getItemsTable().setTransferable();
	}

	protected JPopupMenu insertItemPopUpMenu = new JPopupMenu();

	protected ActionListener menuDeleteListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			removeItem();
		}
	};

	protected void handleTableMouseClicked(Class clazz) {

		int selectedRow = getItemsTable().getSelectedRow();
		if (selectedRow != -1) {
			DomainObject currentNote = getItemsTable().getSortedList().get(selectedRow);
			DomainEditor dialog = null;
			try {
				dialog = DomainEditorFactory.getInstance().createDomainEditorWithParent(clazz, getParentEditor(), getItemsTable(), super.getModel());
			} catch (DomainEditorCreationException e) {
				new ErrorDialog(getParentEditor(), "Error creating editor for " + clazz.getSimpleName(), e).showDialog();
				
			}
			dialog.setSelectedRow(selectedRow);
			dialog.setModel(currentNote, null);
			dialog.setNavigationButtons();
			int status = dialog.showDialog();

			if (status == javax.swing.JOptionPane.OK_OPTION) {
				getItemsTable().getSortedList().set(getItemsTable().getSelectedRow(),
						getItemsTable().getSortedList().get(getItemsTable().getSelectedRow()));
			}
		}
	}

	protected void removeItem() {
		try {
			this.removeRelatedTableRow(getItemsTable(), null, super.getModel());
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("Item not removed", e).showDialog();
		}
		getItemsTable().updateCollection(((ArchDescriptionStructuredData)super.getModel()).getItems());

	}

	protected void addItem(String whereString) throws UnsupportedRepeatingDataTypeException {

		Class classOfNewItem;
		if (this instanceof ChronologyFields) {
			classOfNewItem = ChronologyItems.class;
		} else if (this instanceof BibliographyFields) {
			classOfNewItem = BibItems.class;
		} else if (this instanceof IndexFields) {
			classOfNewItem = IndexItems.class;
		} else if (this instanceof ListOrderedFields) {
			classOfNewItem = ListOrderedItems.class;
		} else if (this instanceof ListDefinitionFields) {
			classOfNewItem = ListDefinitionItems.class;
		} else {
			throw new UnsupportedRepeatingDataTypeException(this.getClass().getName());
		}
		ArchDescriptionStructuredData structuredDataModel = (ArchDescriptionStructuredData) super.getModel();
		DomainEditor dialogStructuredData = null;
		try {
			dialogStructuredData = DomainEditorFactory.getInstance().createDomainEditorWithParent(classOfNewItem, this.getParentEditor(), false);
		} catch (DomainEditorCreationException e) {
			new ErrorDialog(getParentEditor(), "Error creating editor for " + classOfNewItem.getSimpleName(), e).showDialog();

		}
		dialogStructuredData.setNewRecord(true);
		Boolean done = false;
		int sequenceNumber = 0;
		Boolean first = true;
		int returnStatus;
		ArchDescriptionStructuredDataItems newItem;

		while (!done) {
			if (this instanceof ChronologyFields) {
				newItem = new ChronologyItems(super.getModel());
			} else if (this instanceof BibliographyFields) {
				newItem = new BibItems(super.getModel());
			} else if (this instanceof IndexFields) {
				newItem = new IndexItems(super.getModel());
			} else if (this instanceof ListOrderedFields) {
				newItem = new ListOrderedItems(super.getModel());
			} else if (this instanceof ListDefinitionFields) {
				newItem = new ListDefinitionItems(super.getModel());
			} else {
				throw new UnsupportedRepeatingDataTypeException(this.getClass().getName());
			}
			if (first) {
				sequenceNumber = SequencedObjectsUtils.determineSequenceOfNewItem(whereString, getItemsTable());
				first = false;
			} else {
				sequenceNumber++;
			}
			newItem.setSequenceNumber(sequenceNumber);
			dialogStructuredData.setModel(newItem, null);
			returnStatus = dialogStructuredData.showDialog();
			if (returnStatus == javax.swing.JOptionPane.OK_OPTION) {
				structuredDataModel.addItem(newItem);
				getItemsTable().getEventList().add(newItem);
				done = true;
			} else if (returnStatus == StandardEditor.OK_AND_ANOTHER_OPTION) {
				structuredDataModel.addItem(newItem);
				getItemsTable().getEventList().add(newItem);
			} else {
				done = true;
			}
		}
		dialogStructuredData.setNewRecord(false);

	}

	protected void addItemButtonAction() {
		try {
			if (getItemsTable().getSelectedRow() == -1) {
				addItem(SequencedObjectsUtils.ADD_AT_END);
			} else {
				addItem(SequencedObjectsUtils.ADD_ABOVE_SELECTION);
			}
		} catch (UnsupportedRepeatingDataTypeException e1) {
			new ErrorDialog(getParentEditor(), "", e1).showDialog();
		}
	}

}
