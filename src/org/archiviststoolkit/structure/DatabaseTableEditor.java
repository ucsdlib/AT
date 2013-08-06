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


package org.archiviststoolkit.structure;

//==============================================================================
// Import Declarations
//==============================================================================

import javax.swing.*;

import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.swing.RecordNavigationButtons;
import org.archiviststoolkit.swing.SortableTableModel;
import org.archiviststoolkit.swing.AlternatingRowColorTable;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.exceptions.DomainEditorCreationException;
import org.archiviststoolkit.model.validators.ValidatorFactory;
import org.archiviststoolkit.model.validators.DatabaseFieldsValidator;

import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.*;

import com.jgoodies.binding.value.BufferedValueModel;

/**
 * Editor for dealing with the contact domain object.
 */

public class DatabaseTableEditor extends DomainEditor implements MouseListener, ActionListener {

	private DomainSortableTable tableDatabaseFields = null;
	private DomainEditor databaseFieldsDialog;
	private int selectedRow;
	private DomainObject currentField;

	/**
	 * Constructor.
	 *
	 * @param parentFrame the parentframe to render this editor to.
	 */
	public DatabaseTableEditor(Dialog parentFrame) {
		super(DatabaseTables.class, parentFrame);

		editorFields = new DatabaseTablesForm(this);
		this.setContentPanel(editorFields);
		((DatabaseTablesForm) editorFields).getDeleteFieldButton().addActionListener(this);
		tableDatabaseFields = ((DatabaseTablesForm) editorFields).getDatabaseFieldsTable();
		tableDatabaseFields.addMouseListener(this);
	}


	public void hideAddRemoveButtons() {
		DatabaseTablesForm fields = (DatabaseTablesForm)editorFields;
		fields.getDeleteFieldButton().setVisible(false);
		fields.getGetFieldsButton().setVisible(false);
	}

	public final void mouseClicked(final MouseEvent e) {
		if (e.getClickCount() == 2) {

			if (e.getSource() == tableDatabaseFields) {
				selectedRow = tableDatabaseFields.getSelectedRow();
				if (selectedRow != -1) {
					currentField = tableDatabaseFields.getSortedList().get(selectedRow);
					try {
						databaseFieldsDialog = DomainEditorFactory.getInstance().createDomainEditorWithParent(DatabaseFields.class, this, tableDatabaseFields);
					} catch (DomainEditorCreationException e1) {
						new ErrorDialog(this, "Error creating editor for DatabaseFields", e1).showDialog();

					}
					databaseFieldsDialog.setSelectedRow(selectedRow);
					databaseFieldsDialog.setNavigationButtons();
					databaseFieldsDialog.setBuffered(true);
					databaseFieldsDialog.setModel(currentField, null);
					//set the buffered model in the validator
					//todo there must be a better way to do validation on a buffered field.
					DatabaseFieldsValidator validator = (DatabaseFieldsValidator)ValidatorFactory.getInstance().getValidator(currentField);
					DatabaseFieldsForm form = (DatabaseFieldsForm)databaseFieldsDialog.editorFields;
					BufferedValueModel bufferedValueModel = form.detailsModel.getBufferedModel(DatabaseFields.PROPERTYNAME_RETURN_SCREEN_ORDER);
					validator.setReturnScreenOrderBufferedModel(bufferedValueModel);

					setNavigationButtonsContactNotes();
					int status = databaseFieldsDialog.showDialog();

					if (status == javax.swing.JOptionPane.OK_OPTION) {
						databaseFieldsDialog.editorFields.acceptEdit();
					} else {
						databaseFieldsDialog.editorFields.cancelEdit();
					}
				}
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public void mouseReleased(MouseEvent e) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public void mouseEntered(MouseEvent e) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public void mouseExited(MouseEvent e) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	private final void setNavigationButtonsContactNotes() {

		int numberOfRows = tableDatabaseFields.getRowCount();
		if (numberOfRows <= 1) {
			selectedRow = 0;
			databaseFieldsDialog.getPreviousButton().setEnabled(false);
			databaseFieldsDialog.getFirstButton().setEnabled(false);
			databaseFieldsDialog.getNextButton().setEnabled(false);
			databaseFieldsDialog.getLastButton().setEnabled(false);

		} else if (selectedRow <= 0) {
			selectedRow = 0;
			databaseFieldsDialog.getPreviousButton().setEnabled(false);
			databaseFieldsDialog.getFirstButton().setEnabled(false);
			databaseFieldsDialog.getNextButton().setEnabled(true);
			databaseFieldsDialog.getLastButton().setEnabled(true);
		} else if (selectedRow >= numberOfRows - 1) {
			selectedRow = numberOfRows - 1;
			databaseFieldsDialog.getPreviousButton().setEnabled(true);
			databaseFieldsDialog.getFirstButton().setEnabled(true);
			databaseFieldsDialog.getNextButton().setEnabled(false);
			databaseFieldsDialog.getLastButton().setEnabled(false);

		} else {
			databaseFieldsDialog.getPreviousButton().setEnabled(true);
			databaseFieldsDialog.getFirstButton().setEnabled(true);
			databaseFieldsDialog.getNextButton().setEnabled(true);
			databaseFieldsDialog.getLastButton().setEnabled(true);
		}

	}

	public void actionPerformed(final ActionEvent ae) {
        //todo this should be able to be replaced with the removeRelatedTableRow method
        //todo in the domainEditor

        DatabaseTables databaseTablesModel = (DatabaseTables)this.getModel();
		if (ae.getSource() == ((DatabaseTablesForm) editorFields).getDeleteFieldButton()) {
			int selectedRow = tableDatabaseFields.getSelectedRow();
			if (selectedRow != -1) {
				DatabaseFields field = (DatabaseFields) tableDatabaseFields.getSortedList().get(selectedRow);
				databaseTablesModel.removeDatabaseField(field);
				updateDatabaseFieldsTable();
				if (selectedRow >= tableDatabaseFields.getRowCount()) {
					tableDatabaseFields.setRowSelectionInterval(tableDatabaseFields.getRowCount() - 1, tableDatabaseFields.getRowCount() - 1);
				} else {
					tableDatabaseFields.setRowSelectionInterval(selectedRow, selectedRow);
				}
			}
//		} else if (ae.getSource() == this.okButton ||
//				ae.getSource() == this.cancelButton) {
//			super.actionPerformed(ae);
//		} else if (ae.getSource() == databaseFieldsDialog.getNextButton() ||
//				ae.getSource() == databaseFieldsDialog.getPreviousButton() ||
//				ae.getSource() == databaseFieldsDialog.getFirstButton() ||
//				ae.getSource() == databaseFieldsDialog.getLastButton()) {
//			//adjust the selected row
//			int numberOfRows = tableDatabaseFields.getRowCount();
//			if (ae.getSource() == databaseFieldsDialog.getNextButton()) {
//				selectedRow++;
//			} else if (ae.getSource() == databaseFieldsDialog.getPreviousButton()) {
//				selectedRow--;
//			} else if (ae.getSource() == databaseFieldsDialog.getFirstButton()) {
//				selectedRow = 0;
//			} else if (ae.getSource() == databaseFieldsDialog.getLastButton()) {
//				selectedRow = numberOfRows - 1;
//			}
//			databaseFieldsDialog.editorFields.acceptEdit();
//			setNavigationButtonsContactNotes();
//
//			currentField = databaseTablesModel.getDatabaseField(selectedRow);
//			databaseFieldsDialog.setModel(currentField, null);
		} else {
			super.actionPerformed(ae);
		}
	}

	/**
	 * Sets the model for this editor.
	 *
	 * @param model the model to be used
	 */

	public final void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		updateDatabaseFieldsTable();
	}

	public void updateDatabaseFieldsTable() {
		tableDatabaseFields.updateCollection(((DatabaseTables)this.getModel()).getDatabaseFields());
	}

}