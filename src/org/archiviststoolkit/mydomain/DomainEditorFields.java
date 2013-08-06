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
 * @author Lee Mandell
 * Date: Oct 27, 2005
 * Time: 12:18:13 PM
 */

package org.archiviststoolkit.mydomain;

import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.util.SequencedObjectsUtils;
import org.archiviststoolkit.swing.StandardEditor;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.exceptions.AddRelatedObjectException;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.DomainEditorCreationException;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.model.ArchDescriptionDigitalInstances;
import org.archiviststoolkit.model.DigitalObjects;

import javax.swing.*;
import javax.swing.event.UndoableEditListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoManager;
import javax.swing.text.JTextComponent;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.ValueHolder;

public abstract class DomainEditorFields extends JPanel {

	public ArrayList fieldsToBind = new ArrayList();
	public DateFormat customFormat = new SimpleDateFormat("M/d/yyyy");
	ValueHolder valueHolder = new ValueHolder(null, true);
	public PresentationModel detailsModel = new PresentationModel(valueHolder);
	private DomainObject model = null;
	private DomainEditor parentEditor;
	protected UndoManager undoManager = new UndoManager();
	protected boolean inSetModel = false;

	public DomainEditorFields() {
		 detailsModel.observeChanged(valueHolder);
	}

	public void setFormToReadOnly() {
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			try {
				if (field.getType() == JTextField.class
						|| field.getType() == JTextArea.class
						|| field.getType() == JFormattedTextField.class) {
					field.setAccessible(true);
					Object object = field.get(this);
					((JTextComponent) object).setEditable(false);
					((JTextComponent) object).setOpaque(false);
				} else if (field.getType() == JScrollPane.class) {
					field.setAccessible(true);
					((JScrollPane) field.get(this)).setOpaque(false);
				} else if (field.getType() == JCheckBox.class) {
					field.setAccessible(true);
					((JCheckBox) field.get(this)).setEnabled(false);
				}
			} catch (IllegalAccessException e) {
				new ErrorDialog("", StringHelper.getStackTrace(e)).showDialog();
			}
		}
	}

	public void convertComboBoxToNonEnterableTextField(JComboBox comboBox, String fieldName) {
		Container parent = comboBox.getParent();
		FormLayout formLayout = (FormLayout) parent.getLayout();
		CellConstraints cellConstraints = formLayout.getConstraints(comboBox);
		parent.remove(comboBox);
		JTextField textField = BasicComponentFactory.createTextField(detailsModel.getModel(fieldName));
		textField.setEditable(false);
		textField.setOpaque(false);
		parent.add(textField, cellConstraints);
	}

	public void setBean(final Object newBean) {
        // call this in the swing dispatch thread to prevent any
        // locking problems when using the spell checker highlighter

        // if the editor is already opened don't call in EDT thread since it's already running in thread
        if (ApplicationFrame.editorOpen) {
            detailsModel.setBean(newBean);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    detailsModel.setBean(newBean);
                }
            });
        }
	}

	/**
	 * get the model that this editor is using.
	 *
	 * @return the domain model
	 */

	public DomainObject getModel() {
		return (model);
	}

//	/**
//	 * Set the domain model for this editor.
//	 *
//	 * @param model the model
//	 */
//
//	public void setModel(final DomainObject model) {
//		setModel(model, null);
//	}
	/**
	 * Set the domain model for this editor.
	 *
	 * @param model the model
	 */

	public void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		inSetModel = true;
		this.model = model;
		this.setBean(model);
		inSetModel = false;
	}

	public void updateRelatedTable(DomainRelatedTableModel tableModel, Collection updatedCollection) {
		tableModel.updateCollection(updatedCollection);
		tableModel.fireTableDataChanged();
	}

	public void addRelatedTableRecord() {

	}

	public void removeRelatedTableRow(DomainGlazedListTable relatedTable, DomainObject model) throws ObjectNotRemovedException {
		removeRelatedTableRow(relatedTable, null, model);
	}

	public abstract Component getInitialFocusComponent();

	public void removeRelatedTableRow(DomainGlazedListTable relatedTable, DomainRelatedTableModel tableModel, DomainObject model) throws ObjectNotRemovedException {
		int selectedRow = relatedTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "You must select a row to remove.", "warning", JOptionPane.WARNING_MESSAGE);
		} else {
           int response = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete " + relatedTable.getSelectedRows().length + " record(s)",
                    "Delete records", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.OK_OPTION) {
                ArrayList<DomainObject> relatedObjects = relatedTable.removeSelectedRows();
                for (DomainObject relatedObject: relatedObjects) {
                    model.removeRelatedObject(relatedObject);
                }
                int rowCount = relatedTable.getRowCount();
                if (rowCount == 0) {
                    // do nothing
                } else if (selectedRow >= rowCount) {
                    relatedTable.setRowSelectionInterval(rowCount - 1, rowCount - 1);
                } else {
                    relatedTable.setRowSelectionInterval(selectedRow, selectedRow);
                }
				//set record to dirty
				ApplicationFrame.getInstance().setRecordDirty();
            }
        }
	}

	protected void initUndo(JTextArea... textAreas) {
		for (JTextArea textArea: textAreas) {
			textArea.getDocument().addUndoableEditListener(
					new UndoableEditListener() {
						public void undoableEditHappened(UndoableEditEvent undoableEditEvent) {
							if (!inSetModel) {
								undoManager.addEdit(undoableEditEvent.getEdit());
							}
							updateUndoButtons();
						}
					}
			);
		}
	}


	protected void updateUndoButtons() {
//		undoButton.setText(undoManager.getUndoPresentationName());
//		redoButton.setText(undoManager.getRedoPresentationName());
		if (getUndoButton() != null) {
			getUndoButton().setEnabled(undoManager.canUndo());
		}
		if (getRedoButton() != null) {
			getRedoButton().setEnabled(undoManager.canRedo());
		}
	}

	public JButton getUndoButton() {
		return null;
	}

	public JButton getRedoButton() {
		return null;
	}

	public DomainEditor getParentEditor() {
		return parentEditor;
	}

	public void setParentEditor(DomainEditor parentEditor) {
		this.parentEditor = parentEditor;
	}

	protected int handleTableMouseClick(MouseEvent e, DomainGlazedListTable table, Class clazz) {
		if (e.getClickCount() == 2) {
			return editRelatedRecord(table, clazz, true);
		} else {
			return JOptionPane.CANCEL_OPTION;
		}
	}

	protected int handleTableMouseClick(MouseEvent e, DomainGlazedListTable table, Class clazz, Boolean buffered) {
		if (e.getClickCount() == 2) {
			return editRelatedRecord(table, clazz, buffered);
		} else {
			return JOptionPane.CANCEL_OPTION;
		}
	}

	protected int  editRelatedRecord(DomainGlazedListTable table, Class clazz) {
		return editRelatedRecord(table, clazz, false);
	}

	protected int editRelatedRecord(DomainGlazedListTable table, Class clazz, Boolean buffered) {
		int selectedRow = table.getSelectedRow();
		if (selectedRow != -1) {
			DomainObject domainObject = table.getSortedList().get(selectedRow);
			DomainEditor domainEditor = null;
			try {
				domainEditor = DomainEditorFactory.getInstance()
					.createDomainEditorWithParent(clazz, this.getParentEditor(), false);
				domainEditor.setCallingTable(table);
			} catch (UnsupportedTableModelException e1) {
				new ErrorDialog(getParentEditor(), "Error creating editor for " + clazz.getSimpleName(), e1).showDialog();
			} catch (DomainEditorCreationException e) {
				new ErrorDialog(getParentEditor(), "Error creating editor for " + clazz.getSimpleName(), e).showDialog();
			}
			domainEditor.setBuffered(buffered);
			domainEditor.setSelectedRow(selectedRow);
			domainEditor.setNavigationButtons();
			domainEditor.setModel(domainObject, null);
			int returnValue =  domainEditor.showDialog();
			if (domainEditor.getBuffered()) {
				if (returnValue == JOptionPane.CANCEL_OPTION) {
					domainEditor.editorFields.cancelEdit();
				} else {
					domainEditor.editorFields.acceptEdit();
                    ApplicationFrame.getInstance().setRecordDirty(); // ok an edit was made, so set the record dirty
				}
			}
			return returnValue;
		} else {
			return JOptionPane.CANCEL_OPTION;
		}
	}

	protected void addRelatedObject(Class objectClass, DomainGlazedListTable table) throws AddRelatedObjectException, DuplicateLinkException {
		addRelatedObject(null, objectClass, table);
	}

	protected void addRelatedObject(String whereString, Class objectClass, DomainGlazedListTable table) throws AddRelatedObjectException, DuplicateLinkException {
		DomainObject parentDomainObject = this.getModel();
		DomainObject objectToAdd = null;
		DomainEditor dialog = null;
		try {
			dialog = DomainEditorFactory.getInstance()
					.createDomainEditorWithParent(objectClass, this.getParentEditor(), false);
		} catch (DomainEditorCreationException e) {
			new ErrorDialog(getParentEditor(), "Error creating editor for " + objectClass.getSimpleName(), e).showDialog();
		}
		dialog.setNewRecord(true);
		Boolean done = false;
		int sequenceNumber = 0;
		Boolean first = true;
		int returnStatus;
		try {
		Constructor constructor = objectClass.getConstructor(parentDomainObject.getClass());

			while (!done) {
				objectToAdd = (DomainObject)constructor.newInstance(parentDomainObject);
				if (objectToAdd instanceof SequencedObject) {
					if (first) {
						sequenceNumber = SequencedObjectsUtils.determineSequenceOfNewItem(whereString, (DomainSortedTable)table);
						first = false;
					} else {
						sequenceNumber++;
					}
					((SequencedObject)objectToAdd).setSequenceNumber(sequenceNumber);
				}
				dialog.setModel(objectToAdd, null);
				returnStatus = dialog.showDialog();
				if (returnStatus == JOptionPane.OK_OPTION) {
					parentDomainObject.addRelatedObject(objectToAdd);
					table.getEventList().add(objectToAdd);
					done = true;
				} else if (returnStatus == StandardEditor.OK_AND_ANOTHER_OPTION) {
					parentDomainObject.addRelatedObject(objectToAdd);
					table.getEventList().add(objectToAdd);
				} else {
					done = true;
				}
			}
		} catch (InstantiationException e) {
			dialog.setNewRecord(false);
			throw new AddRelatedObjectException(parentDomainObject, objectToAdd, e);
		} catch (IllegalAccessException e) {
			dialog.setNewRecord(false);
			throw new AddRelatedObjectException(parentDomainObject, objectToAdd, e);
		} catch (InvocationTargetException e) {
			dialog.setNewRecord(false);
			throw new AddRelatedObjectException(parentDomainObject, objectToAdd, e);
		} catch (NoSuchMethodException e) {
			dialog.setNewRecord(false);
			throw new AddRelatedObjectException(parentDomainObject, objectToAdd, e);
		}
		dialog.setNewRecord(false);
	}

	protected void handleUndoButtonAction() {
		undoManager.undo();
		updateUndoButtons();
	}

	protected void handleRedoButtonAction() {
		undoManager.redo();
		updateUndoButtons();
	}

	public void acceptEdit() {
		detailsModel.triggerCommit();
	}

	public void cancelEdit() {
		detailsModel.triggerFlush();
	}

}
