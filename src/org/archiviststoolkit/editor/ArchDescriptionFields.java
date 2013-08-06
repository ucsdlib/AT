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
 * Date: Mar 19, 2006
 * Time: 4:19:27 PM
 */

package org.archiviststoolkit.editor;

import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.dialog.SubjectTermLookup;
import org.archiviststoolkit.dialog.NameAuthorityLookup;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.dialog.DigitalObjectLookup;
import org.archiviststoolkit.structure.NotesEtcTypes;
import org.archiviststoolkit.structure.DefaultValues;
import org.archiviststoolkit.util.*;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.plugin.ATPlugin;
import org.archiviststoolkit.plugin.ATPluginFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.swing.StandardEditor;
import org.archiviststoolkit.swing.ATProgressUtil;
import org.archiviststoolkit.exceptions.UnsupportedDomainObjectModelException;
import org.archiviststoolkit.exceptions.DomainEditorCreationException;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.util.Vector;
import java.util.Collections;

public abstract class ArchDescriptionFields extends DomainEditorFields implements SubjectEnabledEditorFields, NameEnabledEditor {

	public static final String ADD_AT_BEGINNING = "Add at beginning";
	public static final String ADD_BEFORE_SELECTION = "Add before selection";
	public static final String ADD_AFTER_SELECTION = "Add after selection";
	public static final String ADD_AT_END = "Add at end";

//	protected SubjectTermLookup subjectLookupDialog = null;
//	protected NameAuthorityLookup nameLookupDialog = null;
//	protected ResourceEditor resourceEditor;

	protected DomainEditor dialogRepeatingData;
	protected DomainObject currentRepeatingData;
	protected int selectedRowRepeatingData;
	private JPopupMenu repeatingDataPopupMenu;
	private boolean debug = false;

	protected ArchDescriptionInstancesEditor dialogInstances;
	protected ArchDescriptionInstances currentInstance;
	protected int selectedRowInstances;

	protected String defaultInstanceType = "";


	ActionListener menuListenerAddAtBegining = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
				NotesEtcTypes noteEtcType = NoteEtcTypesUtils.lookupNoteEtcTypeByCannonicalName(((JMenuItem) e.getSource()).getText());
				addNoteEtc(SequencedObjectsUtils.ADD_AT_BEGINING, noteEtcType);
			} catch (UnsupportedRepeatingDataTypeException e1) {
				new ErrorDialog("", e1).showDialog();
			}
		}
	};
	ActionListener menuListenerAddBeforeSelection = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
				NotesEtcTypes noteEtcType = NoteEtcTypesUtils.lookupNoteEtcTypeByCannonicalName(((JMenuItem) e.getSource()).getText());
				addNoteEtc(SequencedObjectsUtils.ADD_ABOVE_SELECTION, noteEtcType);
			} catch (UnsupportedRepeatingDataTypeException e1) {
				new ErrorDialog("", e1).showDialog();
			}
		}
	};
	ActionListener menuListenerAddAfterSelection = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
				NotesEtcTypes noteEtcType = NoteEtcTypesUtils.lookupNoteEtcTypeByCannonicalName(((JMenuItem) e.getSource()).getText());
				addNoteEtc(SequencedObjectsUtils.ADD_BELOW_SELECTION, noteEtcType);
			} catch (UnsupportedRepeatingDataTypeException e1) {
				new ErrorDialog("", e1).showDialog();
			}
		}
	};
	ActionListener menuListenerAddAtEnd = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
				NotesEtcTypes noteEtcType = NoteEtcTypesUtils.lookupNoteEtcTypeByCannonicalName(((JMenuItem) e.getSource()).getText());
				addNoteEtc(SequencedObjectsUtils.ADD_AT_END, noteEtcType);
			} catch (UnsupportedRepeatingDataTypeException e1) {
				new ErrorDialog("", e1).showDialog();
			}
		}
	};
	ActionListener menuListenerDelete = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			removeNotesEtc();
		}
	};

	public DomainSortedTable getNotesTable() {
		return null;
	}

	public DomainSortableTable getInstancesTable() {
		return null;
	}

	public JButton getAddInstanceButton() {
		return null;
	}

	public JButton getRemoveInstanceButton() {
		return null;
	}

	public DomainSortedTable getRepeatingDataTable() {
		return null;
	}

	public JComboBox getAddNoteEtcComboBox() {
		return null;
	}

	public JButton getChangeRepositoryButton() {
		return null;
	}

	public JButton getAddRepeatingDataButton() {
		return null;
	}

	public JButton getRemoveRepeatingDataButton() {
		return null;
	}

	public abstract DomainSortableTable getSubjectsTable();

	public abstract JButton getAddSubjectRelationshipButton();

	public abstract JButton getRemoveNameRelationshipButton();

	public abstract DomainSortableTable getNamesTable();

	public abstract JButton getAddNameRelationshipButton();

	public abstract JButton getRemoveSubjectRelationshipButton();

	public SubjectEnabledModel getSubjectEnabledModel() {
		return (SubjectEnabledModel)this.getModel();
	}

	public NameEnabledModel getNameEnabledModel() {
		return (NameEnabledModel)this.getModel();
	}

	/**
	 * Set the domain model for this editor.
	 *
	 * @param model the model
	 */

	public void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		ArchDescription archDescriptionModel = (ArchDescription) super.getModel();
		getNamesTable().updateCollection(archDescriptionModel.getNames());
		if (debug) {
			System.out.println("Load Names: " + MyTimer.toString(ApplicationFrame.getInstance().getTimer().elapsedTimeMillisSplit()));
		}
		getSubjectsTable().updateCollection(archDescriptionModel.getSubjects());
		if (debug) {
			System.out.println("Load Subjects: " + MyTimer.toString(ApplicationFrame.getInstance().getTimer().elapsedTimeMillisSplit()));
		}

		DomainSortedTable repeatingDataTable = getRepeatingDataTable();
		if (repeatingDataTable != null) {
			repeatingDataTable.updateCollection(archDescriptionModel.getRepeatingData());
			if (debug) {
				System.out.println("Load Repeating data: " + MyTimer.toString(ApplicationFrame.getInstance().getTimer().elapsedTimeMillisSplit()));
			}
		}

		DomainSortableTable instanceTable = getInstancesTable();
		if (instanceTable != null) {
			instanceTable.updateCollection(((ResourcesCommon) archDescriptionModel).getInstances());
			if (debug) {
				System.out.println("Load Instances: " + MyTimer.toString(ApplicationFrame.getInstance().getTimer().elapsedTimeMillisSplit()));
			}
		}

	}


	protected void addSubjectRelationship() {
		ArchDescription archDescriptionModel = (ArchDescription) super.getModel();
		SubjectTermLookup subjectLookupDialog = new SubjectTermLookup(getParentEditor(), this);
		subjectLookupDialog.setMainHeaderByClass(archDescriptionModel.getClass());
		subjectLookupDialog.showDialog();
	}

	protected void addNameRelationship() {
		ArchDescription archDescriptionModel = (ArchDescription) super.getModel();
		NameAuthorityLookup nameLookupDialog = new NameAuthorityLookup(getParentEditor(), this);
		nameLookupDialog.setMainHeaderByClass(archDescriptionModel.getClass());
		try {
			nameLookupDialog.setFunctionLookupvalues(archDescriptionModel.getClass());
		} catch (UnsupportedDomainObjectModelException e) {
			new ErrorDialog(getParentEditor(), "Error creating name lookup dialog", e).showDialog();
		}
		nameLookupDialog.showDialog();
	}

	protected void addInstance() {
		ArchDescription archDescriptionModel = (ArchDescription) super.getModel();
		ArchDescriptionInstances newInstance = null;
		Vector<String> possibilities = LookupListUtils.getLookupListValues(LookupListUtils.LIST_NAME_INSTANCE_TYPES);
		ImageIcon icon = null;
		try {
			// add a special entry for digital object link to the possibilities vector
            possibilities.add(ArchDescriptionInstances.DIGITAL_OBJECT_INSTANCE_LINK);
            Collections.sort(possibilities);

            dialogInstances = (ArchDescriptionInstancesEditor) DomainEditorFactory.getInstance().createDomainEditorWithParent(ArchDescriptionInstances.class, getParentEditor(), getInstancesTable());
		} catch (DomainEditorCreationException e) {
			new ErrorDialog(getParentEditor(), "Error creating editor for ArchDescriptionInstances", e).showDialog();
		}

        dialogInstances.setNewRecord(true);

		int returnStatus;
		Boolean done = false;
		while (!done) {
			defaultInstanceType = (String) JOptionPane.showInputDialog(getParentEditor(), "What type of instance would you like to create",
					"", JOptionPane.PLAIN_MESSAGE, icon, possibilities.toArray(), defaultInstanceType);

			if ((defaultInstanceType != null) && (defaultInstanceType.length() > 0)) {
				if (defaultInstanceType.equalsIgnoreCase(ArchDescriptionInstances.DIGITAL_OBJECT_INSTANCE)) {
					newInstance = new ArchDescriptionDigitalInstances((ResourcesCommon) archDescriptionModel,
                            (Resources) getParentEditor().getModel());
				} else if (defaultInstanceType.equalsIgnoreCase(ArchDescriptionInstances.DIGITAL_OBJECT_INSTANCE_LINK)) {
                    // add a digital object link or links instead
                    addDigitalInstanceLink((Resources) getParentEditor().getModel());
                    return;
				} else {
					newInstance = new ArchDescriptionAnalogInstances(archDescriptionModel);
				}

                newInstance.setInstanceType(defaultInstanceType);

                // see whether to use a plugin
                if(usePluginDomainEditor(true, newInstance)) {
                    return;
                }

				dialogInstances.setModel(newInstance, null);
				dialogInstances.setResourceInfo((Resources) getParentEditor().getModel());
				returnStatus = dialogInstances.showDialog();
				if (returnStatus == JOptionPane.OK_OPTION) {
					dialogInstances.commitChangesToCurrentRecord();
					((ResourcesCommon) archDescriptionModel).addInstance(newInstance);
					getInstancesTable().getEventList().add(newInstance);
					findLocationForInstance(newInstance);
                    ApplicationFrame.getInstance().setRecordDirty(); // set the record dirty
					done = true;
				} else if (returnStatus == StandardEditor.OK_AND_ANOTHER_OPTION) {
					dialogInstances.commitChangesToCurrentRecord();
					((ResourcesCommon) archDescriptionModel).addInstance(newInstance);
					getInstancesTable().getEventList().add(newInstance);
					findLocationForInstance(newInstance);
                    ApplicationFrame.getInstance().setRecordDirty(); // set the record dirty 
				} else {
					done = true;
				}
			} else {
				done = true;
			}
		}
		dialogInstances.setNewRecord(false);
	}

    /**
     * Method to open dialog that allows linking of digital objects to this resource
     * or resource component
     * @param parentResource The parent resource component
     */
    private void addDigitalInstanceLink(Resources parentResource) {
        DigitalObjectLookup digitalObjectPicker = new DigitalObjectLookup(getParentEditor(), this);
        digitalObjectPicker.setParentResource(parentResource);
		digitalObjectPicker.showDialog(this);
    }


    public void findLocationForInstance(ArchDescriptionInstances newInstance) {
		if (newInstance instanceof ArchDescriptionAnalogInstances) {
			final Resources parentResource = (Resources) getParentEditor().getModel();
			final ArchDescriptionAnalogInstances analogInstance = (ArchDescriptionAnalogInstances) newInstance;
			Thread performer = new Thread(new Runnable() {
				public void run() {
					InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000);
					monitor.start("Gathering Containers...");
					try {
						analogInstance.setLocation(parentResource.findLocationForContainer(analogInstance.getTopLevelLabel(), monitor));
					} finally {
						monitor.close();
					}
				}
			}, "Performer");
			performer.start();
		}
	}

	protected void removeNotesEtc() {
		try {
			this.removeRelatedTableRow(getRepeatingDataTable(), null, super.getModel());
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("Note not removed", e).showDialog();
		}
		getRepeatingDataTable().updateCollection(((ArchDescription) super.getModel()).getRepeatingData());
	}

	protected void addRepeatingData(Class repeatingDataClass, NotesEtcTypes noteType, String whereString) throws UnsupportedRepeatingDataTypeException {

		int selectedRow = getRepeatingDataTable().getSelectedRow();
		ArchDescription archDescriptionModel = (ArchDescription) super.getModel();

		ArchDescriptionRepeatingData repeatingData = ArchDescriptionRepeatingData.getInstance(archDescriptionModel, repeatingDataClass, noteType);
		repeatingData.setSequenceNumber(SequencedObjectsUtils.determineSequenceOfNewItem(whereString, getRepeatingDataTable()));
		if (this.getParentEditor() instanceof ResourceEditor) {
			repeatingData.setPersistentId(((ResourceEditor) this.getParentEditor()).getNextPersistentId());
		}
		//get default values
		RepositoryNotesDefaultValues defaultValue = DefaultValues.getRepoistoryNoteDefaultValue(ApplicationFrame.getInstance().getCurrentUserRepository(), noteType);
		if (defaultValue != null) {
			repeatingData.setTitle(defaultValue.getDefaultTitle());
			if (repeatingData instanceof ArchDescriptionNotes)
				((ArchDescriptionNotes) repeatingData).setNoteContent(defaultValue.getDefaultContent());

            // must set default note contents for bibliography and other ArchDescription
            if (repeatingData instanceof ArchDescriptionStructuredData)
				((ArchDescriptionStructuredData) repeatingData).setNote(defaultValue.getDefaultContent());
        }
		try {
			dialogRepeatingData = DomainEditorFactory.getInstance().createDomainEditorWithParent(ArchDescriptionRepeatingData.class, getParentEditor(), getRepeatingDataTable());
		} catch (DomainEditorCreationException e) {
			new ErrorDialog(getParentEditor(), "Error creating editor for ArchDescriptionRepeatingData", e).showDialog();
		}
		dialogRepeatingData.setNewRecord(true);
		dialogRepeatingData.setIncludeOkAndAnotherButton(false);
		dialogRepeatingData.setModel(repeatingData, null);
		if (dialogRepeatingData.showDialog() == javax.swing.JOptionPane.OK_OPTION) {
			archDescriptionModel.addRepeatingData(repeatingData);
			getRepeatingDataTable().getEventList().add(repeatingData);
			if (selectedRow != -1) {
				getRepeatingDataTable().setRowSelectionInterval(selectedRow, selectedRow);
			}
		}
//		dialogRepeatingData.setNewRecord(false);
	}


	protected void initDigitalObjectNotes() {
		initMoreStuff(NoteEtcTypesUtils.getDigitalObjectNotesTypesList());
	}

	protected void initNotesEtc() {
		initMoreStuff(NoteEtcTypesUtils.getNotesEtcTypesList());
	}

	protected void addNoteEtc(String where, NotesEtcTypes noteEtcType) {
		try {
			addRepeatingData(NoteEtcTypesUtils.lookupRepeatingDataClass(noteEtcType), noteEtcType, where);
			ArchDescription archDescriptionModel = (ArchDescription) super.getModel();
			if (archDescriptionModel instanceof ResourcesComponents) {
				((ResourcesComponents) archDescriptionModel).setHasNotes(true);
			}
		} catch (UnsupportedRepeatingDataTypeException e) {
			ErrorDialog dialog = new ErrorDialog(getParentEditor(), "Unsupported repeating data type: " + noteEtcType, e);
			dialog.showDialog();
		}
	}

	protected void initAccess() {
		if (!Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_SUPERUSER)) {
			if (getChangeRepositoryButton() != null) {
				getChangeRepositoryButton().setVisible(false);
			}
		}

	}

	protected void initMoreStuff(Vector values) {

		repeatingDataPopupMenu = new JPopupMenu();

		JMenu popupMenu = new JMenu("Insert");
		JMenuItem jPopupMenuItem;
		NotesEtcTypes value;
		for (Object o : values) {
			value = (NotesEtcTypes) o;
			jPopupMenuItem = new JMenuItem(value.getNotesEtcLabel());
			jPopupMenuItem.addActionListener(menuListenerAddBeforeSelection);
			popupMenu.add(jPopupMenuItem);
		}
		repeatingDataPopupMenu.add(popupMenu);

		JMenuItem deleteMenuItem = new JMenuItem("Delete");
		deleteMenuItem.addActionListener(menuListenerDelete);
		repeatingDataPopupMenu.add(deleteMenuItem);

		values.add(0, "Add note etc.");
		getAddNoteEtcComboBox().setModel(new DefaultComboBoxModel(values));
	}

	protected void addDeaccessionsActionPerformed(DomainSortableTable deaccessionsTable) {
		AccessionsResourcesCommon accessionsModel = (AccessionsResourcesCommon) getModel();
		Deaccessions newDeaccessions;
		DomainEditor dialogDeaccessions = null;
		try {
			dialogDeaccessions = DomainEditorFactory.getInstance().createDomainEditorWithParent(Deaccessions.class, this.getParentEditor(), false);
		} catch (DomainEditorCreationException e) {
			new ErrorDialog(getParentEditor(), "Error creating editor for Deaccessions", e).showDialog();
		}
		dialogDeaccessions.setNewRecord(true);

		boolean done = false;
		int returnStatus;
		while (!done) {
			newDeaccessions = new Deaccessions(accessionsModel);
			dialogDeaccessions.setModel(newDeaccessions, null);
			returnStatus = dialogDeaccessions.showDialog();
			if (returnStatus == JOptionPane.OK_OPTION) {
				accessionsModel.addDeaccessions(newDeaccessions);
				deaccessionsTable.updateCollection(accessionsModel.getDeaccessions());
				done = true;
			} else if (returnStatus == StandardEditor.OK_AND_ANOTHER_OPTION) {
				accessionsModel.addDeaccessions(newDeaccessions);
				deaccessionsTable.updateCollection(accessionsModel.getDeaccessions());
			} else {
				done = true;
			}
		}
	}

	protected void removeDeaccessionActionPerformed(DomainSortableTable deaccessionsTable) {
		try {
			this.removeRelatedTableRow(deaccessionsTable, null, super.getModel());
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("Deaccession not removed", e).showDialog();
		}
		deaccessionsTable.updateCollection(((AccessionsResourcesCommon) super.getModel()).getDeaccessions());
	}

	protected void addNoteEtcComboBoxAction() {
		if (getAddNoteEtcComboBox().getSelectedIndex() > 0) {
			if (getRepeatingDataTable().getSelectedRow() == -1) {
				addNoteEtc(SequencedObjectsUtils.ADD_AT_END, (NotesEtcTypes) getAddNoteEtcComboBox().getSelectedItem());
			} else {
				addNoteEtc(SequencedObjectsUtils.ADD_ABOVE_SELECTION, (NotesEtcTypes) getAddNoteEtcComboBox().getSelectedItem());
			}
			getAddNoteEtcComboBox().setSelectedIndex(0);
		}
	}

	protected void setOtherLevelEnabledDisabled(JComboBox levelComboBox, JLabel otherLevelLable, JTextField otherLevelTextField) {
		if (levelComboBox.getSelectedItem() != null) {
			String level = ((LookupListItems) levelComboBox.getSelectedItem()).getListItem();
			if (level.equalsIgnoreCase(Resources.LEVEL_VALUE_OTHERLEVEL)) {
				otherLevelLable.setEnabled(true);
				otherLevelTextField.setEnabled(true);
			} else {
				otherLevelLable.setEnabled(false);
				otherLevelTextField.setEnabled(false);
				otherLevelTextField.setText("");
			}
		} else {
			otherLevelLable.setEnabled(false);
			otherLevelTextField.setEnabled(false);
			otherLevelTextField.setText("");
		}
	}

	protected void handleInstanceTableMouseClick(MouseEvent e) {
		if (e.getClickCount() == 2) {
            // get the current instance record to edit
            DomainObject instanceRecord = null;
            int selectedRow = getInstancesTable().getSelectedRow();
		    if (selectedRow != -1) {
			    instanceRecord = getInstancesTable().getSortedList().get(selectedRow);
            }

            if(usePluginDomainEditor(false, instanceRecord)) {
                return;
            }

			if (handleTableMouseClick(e, getInstancesTable(), ArchDescriptionInstances.class) == JOptionPane.OK_OPTION) {
				findLocationForInstance(currentInstance);
			}
		}
	}

	protected void editNameRelationshipActionPerformed() {
        DomainSortableTable namesTable = getNamesTable();
        int selectedRow = namesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "You must select a name to edit.", "warning", JOptionPane.WARNING_MESSAGE);
        } else {
		editRelatedRecord(namesTable, ArchDescriptionNames.class, true);
		namesTable.invalidate();
		namesTable.validate();
		namesTable.repaint();
        }
    }

	protected void handleNamesTableMouseClick(MouseEvent e) {
		DomainSortableTable namesTable = getNamesTable();
		handleTableMouseClick(e, namesTable, ArchDescriptionNames.class);
		namesTable.invalidate();
		namesTable.validate();
		namesTable.repaint();
	}

    /**
     * Method to load a custom plugin domain editor for viewing the record. Usefull if someone want to
     * implemment an editor that is more suited for their workflow or to load a read only viewer for the
     * record.
     *
     * @param domainObject The record to edit
     * @return Whether any plugin editors where found
     */
    private boolean usePluginDomainEditor(boolean newInstance, DomainObject domainObject) {
        ATPlugin plugin = ATPluginFactory.getInstance().getEditorPlugin(domainObject);

        if (plugin == null) { // just return false and so that the built in domain object can be used
            return false;
        }

        // set the calling table and editor
        plugin.setEditorField(this);
        plugin.setCallingTable(getInstancesTable());

        if(!newInstance) { // this means that it is a record being edited, so may have to do something special
            plugin.setModel(domainObject, null);
        } else { // its a new record to just set the model
            plugin.setModel(domainObject, null);
        }

        // set the main program application frame and display it
        plugin.setApplicationFrame(ApplicationFrame.getInstance());
        plugin.showPlugin(getParentEditor());

        return true;
    }
}
