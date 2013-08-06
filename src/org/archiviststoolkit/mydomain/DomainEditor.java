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
 */

package org.archiviststoolkit.mydomain;

//==============================================================================
// Import Declarations
//==============================================================================

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.sql.SQLException;

import org.archiviststoolkit.swing.StandardEditor;
import org.archiviststoolkit.swing.SortableTableModel;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.swing.ATProgressUtil;
import org.archiviststoolkit.report.ReportFactory;
import org.archiviststoolkit.report.ReportUtils;
import org.archiviststoolkit.report.ReportDestinationProperties;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.editor.ResourceEditor;
import org.archiviststoolkit.editor.DigitalObjectEditor;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.structure.DefaultValues;
import org.archiviststoolkit.structure.DatabaseTables;
import org.archiviststoolkit.structure.DatabaseFields;
import org.archiviststoolkit.structure.NotesEtcTypes;
import org.archiviststoolkit.exceptions.*;
import org.archiviststoolkit.ApplicationFrame;
import org.hibernate.exception.ConstraintViolationException;

import javax.swing.*;

/**
 * Domain Object Editor.
 */

public class DomainEditor extends StandardEditor {

	public DomainTableWorkSurface workSurface;

	public DomainEditorFields editorFields;

	private Class clazz;
    private DomainObject callingModel = null;
	private DomainTableModel callingTableModel = null;
	private DomainGlazedListTable callingGlazedTable = null;
	private Boolean readOnly = false;
	private Boolean buffered = false;

	/**
	 * Constructor taking the parent frame and a title.
	 *
	 * @param parent the parent frame
	 */

	public DomainEditor(Class clazz, final Frame parent) {
		super(parent);
		this.init(clazz);
		setMainHeaderColorAndTextByClass();
	}

	/**
	 * Constructor taking the parent frame and a title.
	 *
	 * @param parent the parent frame
	 */

	public DomainEditor(Class clazz, final Dialog parent) {
		super(parent);
		this.init(clazz);
		setMainHeaderColorAndTextByClass();
	}

	/**
	 * Constructor taking the parent frame and a mainHeader.
	 *
	 * @param parent	 the parent frame
	 * @param mainHeader the mainHeader of this editor
	 */

	public DomainEditor(Class clazz, final Frame parent, final String mainHeader) {
		super(parent, mainHeader);
		this.init(clazz);
	}

	/**
	 * Constructor taking the parent frame and a mainHeader.
	 *
	 * @param parent	 the parent frame
	 * @param mainHeader the mainHeader of this editor
	 */

	public DomainEditor(Class clazz, final Dialog parent, final String mainHeader) {
		super(parent, mainHeader);
		this.init(clazz);
	}


	public DomainEditor(Class clazz, final Frame parent, final String mainHeader, final String subHeader) {
		super(parent, mainHeader, subHeader);
		this.init(clazz);
	}

	public DomainEditor(Class clazz, final Dialog parent, final String mainHeader, final String subHeader) {
		super(parent, mainHeader, subHeader);
		this.init(clazz);
	}


	public DomainEditor(Class clazz, final Dialog parent, final DomainEditorFields editorFields) {
		super(parent);
		this.init(clazz);
		this.setMainHeaderColorAndTextByClass(clazz);
		this.editorFields = editorFields;
		this.setContentPanel(editorFields);
	}

	public DomainEditor(Class clazz, final Frame parent, final DomainEditorFields editorFields) {
		super(parent);
		this.init(clazz);
		this.setMainHeaderColorAndTextByClass(clazz);
		this.editorFields = editorFields;
		this.setContentPanel(editorFields);
	}


	public DomainEditor(Class clazz, final Dialog parent, final String mainHeader, final DomainEditorFields editorFields) {
		super(parent, mainHeader);
		this.init(clazz);
		this.editorFields = editorFields;
		this.setContentPanel(editorFields);
	}

	public DomainEditor(Class clazz, final Frame parent, final String mainHeader, final DomainEditorFields editorFields) {
		super(parent, mainHeader);
		this.init(clazz);
		this.editorFields = editorFields;
		this.setContentPanel(editorFields);
	}


	public DomainEditor(Class clazz, final Dialog parent, final String mainHeader, final String subHeader, final DomainEditorFields editorFields) {
		super(parent, mainHeader, subHeader);
		this.init(clazz);
		this.editorFields = editorFields;
		this.setContentPanel(editorFields);
	}

	public DomainEditor(Class clazz, final Frame parent, final String mainHeader, final String subHeader, final DomainEditorFields editorFields) {
		super(parent, mainHeader, subHeader);
		this.init(clazz);
		this.editorFields = editorFields;
		this.setContentPanel(editorFields);
	}

	public void setContentPanel(DomainEditorFields newContentPanel) {
		super.setContentPanel(newContentPanel);
		newContentPanel.setParentEditor(this);
	}

	/**
	 * Displays the dialog box representing the editor.
	 *
	 * @return true if it displayed okay
	 */

	public int showDialog() {
		if (editorFields.getInitialFocusComponent() != null) {
			editorFields.getInitialFocusComponent().requestFocusInWindow();
		}
		return super.showDialog();
	}

	public void setMainHeaderColorAndTextByClass() {
		setMainHeaderColorAndTextByClass(clazz);
	}


	public void setMainHeaderColorAndTextByClass(Class clazz, Class parentClass) {
		if (clazz == Deaccessions.class) {
			if (parentClass == Accessions.class) {
				this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_ACCESSIONS);
				this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_ACCESSIONS);
			} else if (parentClass == Resources.class) {
				this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_RESOURCES);
				this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_RESOURCES);
			} else {
				try {
					throw new UnsupportedClassException(parentClass);
				} catch (UnsupportedClassException e) {
					new ErrorDialog(this, "Error creating domain editor. " + e.getMessage(), e).showDialog();
				}
			}
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_DEACCESSIONS);
		} else {
			try {
				throw new UnsupportedClassException(clazz);
			} catch (UnsupportedClassException e) {
				new ErrorDialog(this, "Error creating domain editor. " + e.getMessage(), e).showDialog();
			}
		}
	}

	public void setMainHeaderColorAndTextByClass(Class clazz) {
		if (clazz == Accessions.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_ACCESSIONS);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_ACCESSIONS);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_BLANK);

		} else if (clazz == AccessionsLocations.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_ACCESSIONS);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_ACCESSIONS);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_LOCATIONS);

		} else if (clazz == Resources.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_RESOURCES);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_RESOURCES);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_BLANK);

		} else if (clazz == ResourcesComponents.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_RESOURCES);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_RESOURCES);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_BLANK);

		} else if (clazz == ArchDescriptionNotes.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_RESOURCES);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_NOTE);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_BLANK);

		} else if (clazz == DigitalObjects.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_DIGITAL_OBJECTS);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_DIGITAL_OBJECTS);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_BLANK);

		} else if (clazz == ArchDescriptionInstances.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_RESOURCES);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_RESOURCES);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_BLANK);

		} else if (clazz == Names.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_NAMES);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_NAMES);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_BLANK);

		} else if (clazz == NonPreferredNames.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_NAMES);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_NAMES);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_NON_PREFERRED_NAMES);

		} else if (clazz == NameContactNotes.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_NAMES);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_NAMES);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_NAME_CONTACT_NOTES);

		} else if (clazz == FileVersions.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_DIGITAL_OBJECTS);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_DIGITAL_OBJECTS);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_FILE_VERSIONS);

		} else if (clazz == Subjects.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_SUBJECTS);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_SUBJECTS);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_BLANK);

		} else if (clazz == ChronologyItems.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_RESOURCES);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_NOTE);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_CHRONOLOGY_ITEMS);

		} else if (clazz == Events.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_RESOURCES);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_NOTE);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_EVENTS);

		} else if (clazz == Repositories.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_RESOURCES);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_ADMINISTRATION);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_REPOSITORIES);

		} else if (clazz == DatabaseTables.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_ADMINISTRATION);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_CONFIGURE_APPLICATION);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_DATABASE_TABLES);

		} else if (clazz == DatabaseFields.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_ADMINISTRATION);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_CONFIGURE_APPLICATION);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_DATABASE_FIELDS);

		} else if (clazz == RepositoryNotes.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_ADMINISTRATION);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_REPOSITORIES);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_NOTES);

		} else if (clazz == RepositoryStatistics.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_ADMINISTRATION);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_REPOSITORIES);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_REPOSITORY_STATISTICS);

		} else if (clazz == RepositoryNotesDefaultValues.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_ADMINISTRATION);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_REPOSITORIES);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_NOTES_DEFAULT_VALUES);

		} else if (clazz == DefaultValues.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_ADMINISTRATION);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_REPOSITORIES);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_DEFAULT_VALUES);

		} else if (clazz == Users.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_ADMINISTRATION);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_ADMINISTRATION);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_USERS);

		} else if (clazz == LookupList.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_ADMINISTRATION);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_ADMINISTRATION);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_LOOKUP_LISTS);

		} else if (clazz == RDEScreen.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_ADMINISTRATION);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_ADMINISTRATION);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_RDE);

		} else if (clazz == RDEScreenPanels.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_ADMINISTRATION);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_ADMINISTRATION);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_RDE);

		} else if (clazz == Locations.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_ADMINISTRATION);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_ADMINISTRATION);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_LOCATIONS);

		} else if (clazz == NotesEtcTypes.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_ADMINISTRATION);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_ADMINISTRATION);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_NOTES_ETC);

		} else if (clazz == Constants.class) {
			this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_ADMINISTRATION);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_ADMINISTRATION);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_CONSTANTS);

		} else if (clazz == Assessments.class) {
            this.getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_ASSESSMENTS);
			this.getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_ASSESSMENTS);
			this.getSubHeaderLabel().setText(StandardEditor.MODULE_SUB_HEADER_BLANK);
        }
        else if (clazz == ArchDescriptionRepeatingData.class || clazz == Deaccessions.class) {
			//do nothing. the header is set dynamically when dialog model is set
		} else {
			try {
				throw new UnsupportedClassException(clazz);
			} catch (UnsupportedClassException e) {
				new ErrorDialog(this, "Error creating domain editor. " + e.getMessage(), e).showDialog();
			}
		}
	}


	public void setBean(Object newBean) {
		editorFields.setBean(newBean);
	}


//	/**
//	 * Sets the model for this editor.
//	 *
//	 * @param model the model to be used
//	 */
//
//	public void setModel(final DomainObject model) {
//		setModel(model, null);
//	}

	/**
	 * Sets the model for this editor.
	 *
	 * @param model the model to be used
	 * @param progressPanel - the glass pane to be used to display a progress message
	 */

	public void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		this.model = model;
		ApplicationFrame.getInstance().setRecordClean();
//		if (this.clazz == Subjects.class) {
//			ApplicationFrame.getInstance().setRecordDirty(true);
//		} else {
//			ApplicationFrame.getInstance().setRecordDirty(true);
//		}

		if (model.isNewRecord()) {
			clearRecordPositionText();
			DefaultValues.assignDefaultValues(model);
		} else {
			if (callingGlazedTable != null) {
				if (callingGlazedTable.getFilteredList() != null) {
					setRecordPositionText(selectedRow, callingGlazedTable.getFilteredList().size());
				} else {
					setRecordPositionText(selectedRow, callingGlazedTable.getSortedList().size());
				}
			}
		}
		if (editorFields != null) {
			editorFields.setModel(model, progressPanel);
		}
		addAuditPanel(model);
	}



	/**
	 * Get the current model being used by this editor.
	 *
	 * @return the domain object model
	 */

	public DomainObject getModel() {
		return (this.model);
	}

	public void setWorkSurface(DomainTableWorkSurface workSurface) {
		this.workSurface = workSurface;
	}

	public void setNavigationButtonListeners(ActionListener listener) {
		getFirstButton().addActionListener(listener);
		getPreviousButton().addActionListener(listener);
		getNextButton().addActionListener(listener);
		getLastButton().addActionListener(listener);
		setButtonListeners();
	}

	public void setButtonListeners() {
		this.setOkButton(getOkButton());
		getOkButton().addActionListener(this);
		this.setCancelButton(getCancelButton());
		getCancelButton().addActionListener(this);
		getOkAndAnotherButton().addActionListener(this);
		getPrintButton().addActionListener(this);
		getSaveButton().addActionListener(this);
	}

	public void setSelectedRow(int selectedRow) {
		this.selectedRow = selectedRow;
	}

	public void incrementSelectedRow() {
		this.selectedRow++;
	}

	public void decrementSelectedRow() {
		this.selectedRow--;
	}

	public int getSelectedRow() {
		return this.selectedRow;
	}

	/**
	 * capture and handle action events.
	 *
	 * @param ae the action event
	 */

	public void actionPerformed(final ActionEvent ae) {
		if (ae.getSource() == getOkButton()) {
			handleOkButtons(ae, javax.swing.JOptionPane.OK_OPTION);

		} else if (ae.getSource() == getOkAndAnotherButton()) {
			handleOkButtons(ae, StandardEditor.OK_AND_ANOTHER_OPTION);

		} else if (ae.getSource() == getCancelButton()) {
			if (buffered) {
				editorFields.cancelEdit();
			}
			status = javax.swing.JOptionPane.CANCEL_OPTION;
			this.setVisible(false);

        } else if (ae.getSource() == getSaveButton()) {
            saveRecord(ae);
		} else if (ae.getSource() == getNextButton() ||
				ae.getSource() == getPreviousButton() ||
				ae.getSource() == getFirstButton() ||
				ae.getSource() == getLastButton()) {

			//check to make sure the record is valid
			if (readOnly || this.getModel().validateAndDisplayDialog(ae)) {
				//adjust the selected row
				int numberOfRows = getCallingTable().getRowCount();
				if (ae.getSource() == getNextButton()) {
					selectedRow++;
				} else if (ae.getSource() == getPreviousButton()) {
					selectedRow--;
				} else if (ae.getSource() == getFirstButton()) {
					selectedRow = 0;
				} else if (ae.getSource() == getLastButton()) {
					selectedRow = numberOfRows - 1;
				}
				if (buffered) {
					editorFields.acceptEdit();
				}
				setNavigationButtons();
				if (callingGlazedTable != null) {
					setModel(callingGlazedTable.getDomainObject(selectedRow), null);
					callingGlazedTable.setRowSelectionInterval(selectedRow, selectedRow);
				} else {
					setModel(callingTableModel.get(selectedRow), null);
					callingTableModel.fireTableDataChanged();
				}
				if (editorFields.getInitialFocusComponent() != null) {
					editorFields.getInitialFocusComponent().requestFocusInWindow();
				}
			}

		} else if (ae.getSource() == getPrintButton()) {
			try {
				final ReportDestinationProperties reportDestinationProperties = new ReportDestinationProperties(clazz, this);
				if (reportDestinationProperties.showDialog(1)) {
					Thread performer = new Thread(new Runnable() {
						public void run() {
							InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(DomainEditor.this, 1000, true);
							monitor.start("Gathering records to print");
							try {
								Collection<DomainObject> collectionForReport = new ArrayList<DomainObject>();
								collectionForReport.add(getModel());
								ReportUtils.printReport(reportDestinationProperties, collectionForReport, monitor);
							} catch (UnsupportedParentComponentException e) {
								monitor.close();
								new ErrorDialog("There is a problem printing the report", e).showDialog();
							} catch (UnsupportedReportDestination unsupportedReportDestination) {
								monitor.close();
								new ErrorDialog("There is a problem printing the report", unsupportedReportDestination).showDialog();
							} catch (UnsupportedReportType unsupportedReportType) {
								monitor.close();
								new ErrorDialog("There is a problem printing the report", unsupportedReportType).showDialog();
							} finally {
								monitor.close();
							}
						}
					}, "Printing");
					performer.start();

				}
			} catch (UnsupportedClassException e) {
				new ErrorDialog("There is a problem printing the report", e).showDialog();
			} catch (UnsupportedReportDestination unsupportedReportDestination) {
				new ErrorDialog("There is a problem printing the report", unsupportedReportDestination).showDialog();
			} catch (UnsupportedReportType unsupportedReportType) {
				new ErrorDialog("There is a problem printing the report", unsupportedReportType).showDialog();
			}
		} else {
			super.actionPerformed(ae);
		}

	}

	private void handleOkButtons(ActionEvent ae, int status) {
        if(readOnly) { // read only so just close and don't save anything
            closeAndNoSave();
        } else if (showCloseConfirmDialog(status) == JOptionPane.YES_OPTION) {
            if (this.getModel().validateAndDisplayDialog(ae)) {
                if (this.getClass() == ResourceEditor.class) {
                    if (((ResourceEditor) this).commitChangesToCurrentResourceComponent(ae)) {
                        if (buffered) {
                            editorFields.acceptEdit();
                        }
                        this.status = status;
                        this.setVisible(false);
                    }
                } else {
                    if (buffered) {
                        editorFields.acceptEdit();
                    }
                    this.status = status;
                    this.setVisible(false);
                }
            }
        }
    }

	public void updateRelatedTable(DomainRelatedTableModel tableModel, Collection updatedCollection) {
		tableModel.updateCollection(updatedCollection);
		tableModel.fireTableDataChanged();
	}

	public void removeRelatedTableRow(JTable relatedTable, DomainRelatedTableModel tableModel, DomainObject model) throws ObjectNotRemovedException {
		int selectedRow = relatedTable.getSelectedRow();
		if (selectedRow != -1) {
			DomainObject relatedObject = tableModel.get(selectedRow);
			model.removeRelatedObject(relatedObject);
			updateRelatedTable(tableModel, model.getRelatedCollection(relatedObject));
			int rowCount = relatedTable.getRowCount();
			if (rowCount == 0) {
				// do nothing
			} else if (selectedRow >= rowCount) {
				relatedTable.setRowSelectionInterval(rowCount - 1, rowCount - 1);
			} else {
				relatedTable.setRowSelectionInterval(selectedRow, selectedRow);
			}
		}

	}

	public void setCallingTable(JTable callingTable) throws UnsupportedTableModelException {
		super.setCallingTable(callingTable);
		Object tableModel = callingTable.getModel();
		if (tableModel instanceof SortableTableModel) {
			SortableTableModel temp = (SortableTableModel) tableModel;
			callingTableModel = (DomainTableModel) temp.getTableModel();
		} else if (tableModel instanceof DomainTableModel) {
			callingTableModel = (DomainTableModel) tableModel;
		} else if (callingTable instanceof DomainGlazedListTable) {
			callingGlazedTable = (DomainGlazedListTable) callingTable;
		} else {
			throw new UnsupportedTableModelException();
		}
	}

	public Class getClazz() {
		return clazz;
	}

	public void init(Class clazz) {
		this.clazz = clazz;
		if (!ReportFactory.getInstance().doesClassHaveReports(clazz)) {
			getPrintButton().setVisible(false);
			getPrintLabel().setVisible(false);
		}
	}

	public DomainObject getCallingModel() {
		return callingModel;
	}

	public void setCallingModel(DomainObject callingModel) {
		this.callingModel = callingModel;
	}

	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public Boolean getBuffered() {
		return buffered;
	}

	public void setBuffered(Boolean buffered) {
		this.buffered = buffered;
	}

    /**
     * Method to save the record.
     *
     * @param event The event object which resulted in calling this method
     */
    public void saveRecord(EventObject event) {
        if (readOnly || this.getModel().validateAndDisplayDialog(event)) {
            if (this.getClass() == ResourceEditor.class) {
                if (((ResourceEditor) this).commitChangesToCurrentResourceComponent(event)) {
                    if (buffered) {
                        editorFields.acceptEdit();
                    }
                }
            } else {
                if (buffered) {
                    editorFields.acceptEdit();
                }
            }

            DomainAccessObject dao = null;
            try {
                dao = DomainAccessObjectFactory.getInstance().getDomainAccessObject(this.clazz);
                dao.getLongSession();
                if (newRecord) { // check to see if new record and if it is then creat it then save it
                    dao.updateLongSession(this.getModel(), false);
                    // set new record to false so that we don't call this again
                    newRecord = false;
                    savedNewRecord = true;

                    // add a record lock for this new object
                    if (workSurface != null) {
                        workSurface.addRecordLock(this.getModel());
                    }
                } else if (savedNewRecord) {
                    dao.updateLongSession(this.getModel(), false);
                } else {
                    dao.updateLongSession(this.getModel(), false);

                    if (callingGlazedTable != null) {
                        // calling this method here cause an error on save (ART-1531)
                        //callingGlazedTable.setDomainObject(selectedRow, this.getModel());
                    } else {
                        callingTableModel.fireTableDataChanged();
                    }
                }

                // do any class specfic updates to the UI here
                if (this.getClass() == ResourceEditor.class) {
                    ((ResourceEditor) this).doSaveSpecificUpdates();
                } else if (this.getClass() == DigitalObjectEditor.class) {
                    ((DigitalObjectEditor) this).doSaveSpecificUpdates();
                }

                // set the record dirty flag to false so that the dialog box does not come up asking for it to be saved
                ApplicationFrame.getInstance().setRecordClean();

                // set the choice to the already saved option so that we can save properly
                choice = ALREADY_SAVED;
            } catch (PersistenceException e) {
                if (e.getCause() instanceof ConstraintViolationException) {
                    String message = "Can't save, duplicate record " + this.getModel();
                    JOptionPane.showMessageDialog(this, message);
                } else {
                    new ErrorDialog("Error saving record", e).showDialog();
                }
                // need to get a new session here to prevent the occurance of ART-1674
                try {
                    dao.closeLongSessionRollback();
                    dao.getLongSession().refresh(this.getModel());
                } catch (SQLException e2) {
                    new ErrorDialog("Error closing session", e).showDialog();
                }

                // set the choice to JOptionPane yes option so that we can save properly
                choice = JOptionPane.NO_OPTION;
            }
        }

    }
}

