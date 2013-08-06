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
 * Created by JFormDesigner on Tue Jan 03 12:50:25 EST 2006
 */



package org.archiviststoolkit.dialog;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.sql.SQLException;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.structure.DatabaseTables;
import org.archiviststoolkit.swing.*;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.util.LocationsUtils;
import org.archiviststoolkit.util.RDEUtils;
import org.archiviststoolkit.Main;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.exceptions.DeleteException;
import org.archiviststoolkit.exceptions.DomainEditorCreationException;
import org.hibernate.exception.ConstraintViolationException;
import org.apache.log4j.Logger;

public class GeneralAdminDialog extends JDialog implements ActionListener{

	private static Logger logger = Logger.getLogger(Main.class.getPackage().getName());

	// Method to call when you don't want the this to paint its own components.
  // LocationManagement uses this
  public GeneralAdminDialog(Frame owner, boolean drawGui) {
    super(owner);
    if(drawGui) {
      initComponents();
    }
  }

  // Method to call when you don't want the this to paint its own components.
  public GeneralAdminDialog(Dialog owner, boolean drawGui) {
    super(owner);
    if(drawGui) {
      initComponents();
    }
  }


  public GeneralAdminDialog(Frame owner) {
		super(owner);
		initComponents();
	}

	public GeneralAdminDialog(Dialog owner) {
		super(owner);
		initComponents();
	}

	private void batchAddButtonActionPerformed(ActionEvent e) {
		BatchLocationCreation batchCreate = new BatchLocationCreation(this);
		batchCreate.showDialog();
		findAll();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		dialogTitle = new JLabel();
		scrollPane1 = new JScrollPane();
		contentTable = new DomainSortableTable();
		buttonBar = new JPanel();
		batchAddButton = new JButton();
		addRecordButton = new JButton();
		removeRecordButton = new JButton();
		doneButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setModal(true);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(Borders.DIALOG_BORDER);
			dialogPane.setBackground(new Color(200, 205, 232));
			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{
				contentPanel.setOpaque(false);
				contentPanel.setLayout(new FormLayout(
					ColumnSpec.decodeSpecs("default:grow"),
					new RowSpec[] {
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC
					}));

				//---- dialogTitle ----
				dialogTitle.setText("dialogTitle");
				dialogTitle.setFont(new Font("Lucida Grande", Font.BOLD, 16));
				contentPanel.add(dialogTitle, cc.xywh(1, 1, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

				//======== scrollPane1 ========
				{

					//---- contentTable ----
					contentTable.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							contentTableMouseClicked(e);
						}
					});
					scrollPane1.setViewportView(contentTable);
				}
				contentPanel.add(scrollPane1, cc.xy(1, 3));

				//======== buttonBar ========
				{
					buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
					buttonBar.setOpaque(false);
					buttonBar.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.GLUE_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.UNRELATED_GAP_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.BUTTON_COLSPEC
						},
						RowSpec.decodeSpecs("pref")));

					//---- batchAddButton ----
					batchAddButton.setText("Batch Add");
					batchAddButton.setOpaque(false);
					batchAddButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							batchAddButtonActionPerformed(e);
						}
					});
					buttonBar.add(batchAddButton, cc.xy(2, 1));

					//---- addRecordButton ----
					addRecordButton.setText("Add Record");
					addRecordButton.setOpaque(false);
					addRecordButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							addRecordButtonActionPerformed(e);
						}
					});
					buttonBar.add(addRecordButton, cc.xy(6, 1));

					//---- removeRecordButton ----
					removeRecordButton.setText("Remove Records");
					removeRecordButton.setOpaque(false);
					removeRecordButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							removeRecordButtonActionPerformed(e);
						}
					});
					buttonBar.add(removeRecordButton, cc.xy(8, 1));

					//---- doneButton ----
					doneButton.setText("Done");
					doneButton.setOpaque(false);
					doneButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							doneButtonActionPerformed(e);
						}
					});
					buttonBar.add(doneButton, cc.xy(12, 1));
				}
				contentPanel.add(buttonBar, cc.xy(1, 5));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	protected void doneButtonActionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

	protected void addRecordButtonActionPerformed(ActionEvent e) {
		Object instance = null;

		DomainEditor dialog = null;
		try {
			dialog = DomainEditorFactory.getInstance().createDomainEditorWithParent(clazz, this, getContentTable());
		} catch (DomainEditorCreationException e1) {
			new ErrorDialog("There was a problem create an editor", e1).showDialog();
		}
		dialog.setNewRecord(true);
		Boolean done = false;
		int returnStatus;

		while (!done) {
			try {
				instance = clazz.newInstance();
			} catch (InstantiationException instantiationException) {
				new ErrorDialog("Error deleting records", StringHelper.getStackTrace(instantiationException)).showDialog();
			} catch (IllegalAccessException illegalAccessException) {
				new ErrorDialog("Error deleting records", StringHelper.getStackTrace(illegalAccessException)).showDialog();
			}

			if (instance instanceof Repositories) {
				((Repositories)instance).createNoteDefaultValueLinks();
			}

            // see whether to display the save and +1 button if it is an assessment
            // record
            if(instance instanceof Assessments) {
                ((Assessments)instance).setRepository(ApplicationFrame.getInstance().getCurrentUserRepository());
                dialog.setIncludeSaveButton(true);
            }

            dialog.setModel((DomainObject) instance, null);

			returnStatus = dialog.showDialog();
			try {
				if (returnStatus == javax.swing.JOptionPane.OK_OPTION) {
					DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);

                    // if it is an assessment record then must add search result to table instead ob domain object
                    if(this.clazz == Assessments.class) {
                        access.getLongSession(); // make sure we have a long session
                        access.updateLongSession(dialog.getModel());
                        DomainObject assessmentSearchResult = new AssessmentsSearchResult((Assessments)instance);
                        getContentTable().getEventList().add(assessmentSearchResult);
                    } else {
                        access.add(dialog.getModel());
                        getContentTable().getEventList().add((DomainObject) instance);
					    addToPickers((DomainObject)instance);
                    }
					done = true;
				} else if (returnStatus == StandardEditor.OK_AND_ANOTHER_OPTION) {
					DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);

                    // if it is an assessment record then must add search result to table instead to domain object
                    if(this.clazz == Assessments.class) {
                        access.getLongSession(); // make sure we have a long session
                        access.updateLongSession(dialog.getModel());
                        DomainObject assessmentSearchResult = new AssessmentsSearchResult((Assessments)instance);
                        getContentTable().getEventList().add(assessmentSearchResult);
                    } else {
                        access.add(dialog.getModel());
                        getContentTable().getEventList().add((DomainObject) instance);
					    addToPickers((DomainObject)instance);
                    }

                    dialog.setNewRecord(true); // tell the dialog this is a new record
				} else {
					done = true;
				}
			} catch (ConstraintViolationException persistenceException) {
				JOptionPane.showMessageDialog(this, "Can't save, Duplicate record:" + instance);
				((DomainObject) instance).removeIdAndAuditInfo();
			} catch (PersistenceException persistenceException) {
				if (persistenceException.getCause() instanceof ConstraintViolationException) {
					JOptionPane.showMessageDialog(this,  "Can't save, Duplicate record:" + instance);
				} else {
					done = true;
					new ErrorDialog("Error saving new record.",
							StringHelper.getStackTrace(persistenceException)).showDialog();
				}
			}
		}
		dialog.setNewRecord(false);
		findAll();
	}

	private void addToPickers(DomainObject instance) {
		if (this.clazz == Locations.class) {
			LocationsUtils.addLocationToLookupList((Locations)instance);
		}
	}

	protected void removeRecordButtonActionPerformed(ActionEvent e) {

		int response = JOptionPane.showConfirmDialog(this,
				"Are you sure you want to delete " + getContentTable().getSelectedRows().length + " record(s)",
				"Delete records", JOptionPane.YES_NO_OPTION);

		if (response == JOptionPane.OK_OPTION) {
			int selectedRow = getContentTable().getSelectedRow();

			if (selectedRow != -1) {
				final ArrayList<DomainObject> deleteList = new ArrayList<DomainObject>();
				for (int index : getContentTable().getSelectedRows()) {
					if (getContentTable().getFilteredList()!= null) {
						deleteList.add(getContentTable().getFilteredList().get(index));
					} else {
						deleteList.add(getContentTable().getSortedList().get(index));
					}
				}

                // run the deletion in a thread to enable using a progress monitor
                Thread performer = new Thread(new Runnable() {
                    public void run() {
                        String deleteProblems = "";

                        DomainAccessObject access = null;
                        try {
                            access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);
                        } catch (PersistenceException e1) {
                            new ErrorDialog("Error deleting records", StringHelper.getStackTrace(e1)).showDialog();
                        }


                        // get the progress monitor which allows for canceling
                        InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(getThisAsJDialog(), 1000, true);
                        monitor.start("Deleting Records...");

                        int count = 1; // keep track of the number of record(s) deleted
                        int size = deleteList.size();
                        for (DomainObject domainObject : deleteList) {
                            try {
                                access.deleteLongSession(domainObject, false);
                                getContentTable().getEventList().remove(domainObject);
                                if (getClazz() == Locations.class) {
                                    LocationsUtils.removeLocationToLookupList((Locations) domainObject);
                                }

                                // update the monitor
                                monitor.setTextLine(count + " of " + size + " record(s) deleted", 2);
                                count++;
                                
                                // check to see if the cancel button was pressed
                                if (monitor.isProcessCancelled()) {
                                    break;
                                }
                            } catch (PersistenceException e1) {
                                deleteProblems += "\nCould not delete " + domainObject + ". \nReason: " + e1.getMessage();
                            } catch (DeleteException e1) {
                                deleteProblems += "\nCould not delete " + domainObject + ". \nReason: " + e1.getMessage();
                            }
                        }

                        // close the monitor
                        monitor.close();

                        if (deleteProblems.length() > 0) {
                            JOptionPane.showMessageDialog(getThisAsJDialog(), "There were problems deleting some records\n" + deleteProblems);
                        }
                        findAll();
                    }
                }, "Deleting Records");
                performer.start();
			}
		}
	}

	protected void contentTableMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			int selectedRow = getContentTable().getSelectedRow();
			if (selectedRow != -1) {
				try {
					// need to create a new dialog to fix repaint problem with glazed list table 
                    dialog = DomainEditorFactory.getInstance().createDomainEditorWithParent(clazz, this, getContentTable(), true);
                    dialog.setSelectedRow(selectedRow);
					dialog.setNavigationButtons();
					access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);

					currentDomainObject = (DomainObject) access.findByPrimaryKeyLongSession(getContentTable().getSortedList().get(selectedRow).getIdentifier());
					currentObjectSublist = getContentTable().getSortedList().subList(selectedRow, selectedRow + 1);
					dialog.setModel(currentDomainObject, null);
//					setNavigationButtons();
					int status = dialog.showDialog();

					if (status == JOptionPane.OK_OPTION) {
						access.updateLongSession(currentDomainObject);
						currentObjectSublist.set(0, currentDomainObject);
					} else {
						try {
							access.closeLongSessionRollback();
						} catch (SQLException e1) {
							new ErrorDialog(dialog, "Error canceling record.", e1).showDialog();
						}
					}
				} catch (PersistenceException e1) {
					if (e1.getCause() instanceof ConstraintViolationException) {
						String message = "Can't save, duplicate record" + currentDomainObject;
						JOptionPane.showMessageDialog(this, message);
						logger.error(message, e1);
					} else {
						new ErrorDialog(this, "Error saving new record", e1).showDialog();
					}
					try {
						access.closeLongSessionRollback();
					} catch (SQLException sqlex) {
						new ErrorDialog(dialog, "Error canceling record.", sqlex).showDialog();
					}
				} catch (ConstraintViolationException persistenceException) {
					String message = "Can't save, duplicate record" + currentDomainObject;
					JOptionPane.showMessageDialog(this, message);
					logger.error(message, persistenceException);
					try {
						access.closeLongSessionRollback();
					} catch (SQLException e1) {
						new ErrorDialog(dialog, "Error canceling record.", e1).showDialog();
					}
				} catch (LookupException e1) {
					new ErrorDialog(this, "Error saving new record", e1).showDialog();
					try {
						access.closeLongSessionRollback();
					} catch (SQLException sqlex) {
						new ErrorDialog(dialog, "Error canceling record.", sqlex).showDialog();
					}
				} catch(DomainEditorCreationException dece) {
                    new ErrorDialog(this, "Error creating editor", dece).showDialog();
                }

			}
		}
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel dialogTitle;
	private JScrollPane scrollPane1;
	private DomainSortableTable contentTable;
	private JPanel buttonBar;
	private JButton batchAddButton;
	private JButton addRecordButton;
	private JButton removeRecordButton;
	private JButton doneButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

//	private int selectedRow;
//	private DomainTableModel tableModel = null;
	protected DomainAccessObject access;
	protected DomainObject currentDomainObject;
	protected DomainEditor dialog;
	protected String sortField = null;
	protected Class clazz = null;
    protected Class rdeClazz = null;
	protected java.util.List<DomainObject> currentObjectSublist;


	public final void showDialog() {

		this.pack();
		setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void setDialogTitle(String title) {
		this.dialogTitle.setText(title);
	}

    /**
     * Method to set the RDE class so that the appropriate rde screens can
     * be loaded based on if the class is resources components or digital objects
     * @param rdeClazz The RDEClazz Either ResourceComponents or DigitalObjects
     * @throws DomainEditorCreationException
     */
    public void setRDEClazz(Class rdeClazz) throws DomainEditorCreationException {
        this.rdeClazz = rdeClazz;
        setClazz(RDEScreen.class, null);
    }

	public void setClazz(Class clazz) throws DomainEditorCreationException {
		setClazz(clazz, null);
	}

	public void setClazz(Class clazz, String sortField) throws DomainEditorCreationException {
		this.clazz = clazz;
		this.sortField = sortField;

        // check that the class is not the assessment class
        if(clazz != Assessments.class) {
            getContentTable().setClazz(clazz, sortField);
            findAll();
        } else {
            getContentTable().setClazz(AssessmentsSearchResult.class, sortField);
        }

		//set up the dialog for editing records
		dialog = DomainEditorFactory.getInstance().createDomainEditorWithParent(clazz, this, getContentTable(), true);

        if (clazz != Locations.class && clazz != Assessments.class) {
			this.batchAddButton.setVisible(false);
		}
	}

	public void hideAddRemoveButtons() {
		this.addRecordButton.setVisible(false);
		this.removeRecordButton.setVisible(false);
	}

	protected void findAll() {
        DomainAccessObject access = null;
        try {
            access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);
            
        } catch (PersistenceException e) {
            new ErrorDialog("Error finding all records", e).showDialog();
        }
        Collection resultSet = null;
        try {
            if (sortField == null) {
                resultSet = access.findAllLongSession();
            } else {
                resultSet = access.findAllLongSession(sortField);
            }
        } catch (LookupException e) {
            new ErrorDialog("Error finding all records", e).showDialog();
        }

        // Based on the RDEClazz then remove any screens that don't belong to that
        // class
        if (clazz == RDEScreen.class) {
            RDEUtils.removeRDEScreens(resultSet, rdeClazz);
        }

        // Need to call method which will remove none AT classes from result set
        if(clazz == DatabaseTables.class) {
            cleanDatabaseTableNames(resultSet);
        }

        getContentTable().updateCollection(resultSet);
	}

    /**
     * This method is used to remove any none core AT table names from
     * returned results. Not doing so will result in an error when user tries
     * to open up the record
     *
     * @param resultSet
     */
    private void cleanDatabaseTableNames(Collection resultSet) {
        ArrayList results = new ArrayList(resultSet);

        // now go through list and remove any none AT database tables
        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
            DatabaseTables databaseTable = (DatabaseTables) iterator.next();
            if (!databaseTable.getClassName().contains("org.archiviststoolkit")) {
                resultSet.remove(databaseTable);
            }
        }
    }

	protected final void setNavigationButtons() {
		int numberOfRows = getContentTable().getRowCount();
		int selectedRow = dialog.getSelectedRow();
		if (selectedRow <= 0) {
			dialog.setSelectedRow(0);
			dialog.getPreviousButton().setEnabled(false);
			dialog.getFirstButton().setEnabled(false);
			dialog.getNextButton().setEnabled(true);
			dialog.getLastButton().setEnabled(true);
		} else if (selectedRow >= numberOfRows - 1) {
			dialog.setSelectedRow(numberOfRows - 1);
			dialog.getPreviousButton().setEnabled(true);
			dialog.getFirstButton().setEnabled(true);
			dialog.getNextButton().setEnabled(false);
			dialog.getLastButton().setEnabled(false);

		} else {
			dialog.getPreviousButton().setEnabled(true);
			dialog.getFirstButton().setEnabled(true);
			dialog.getNextButton().setEnabled(true);
			dialog.getLastButton().setEnabled(true);
		}

	}

	/**
	 * Invoked when an action occurs.
	 */
	public void actionPerformed(ActionEvent e) {
		Object actionSource = e.getSource();
		if (actionSource == dialog.getNextButton() ||
				actionSource == dialog.getPreviousButton() ||
				actionSource == dialog.getFirstButton() ||
				actionSource == dialog.getLastButton()) {
			//todo this is really kludgy. Look at the way it works for the domainTableWorksurface
			if (dialog.getReadOnly() || dialog.getModel().validateAndDisplayDialog(e)) {
				//adjust the selected row
				int numberOfRows = getContentTable().getRowCount();
				if (actionSource == dialog.getNextButton()) {
					dialog.incrementSelectedRow();
				} else if (actionSource == dialog.getPreviousButton()) {
					dialog.decrementSelectedRow();
				} else if (actionSource == dialog.getFirstButton()) {
					dialog.setSelectedRow(0);
				} else if (actionSource == dialog.getLastButton()) {
					dialog.setSelectedRow(numberOfRows - 1);
				}
				setNavigationButtons();

				try {
//					int index = getTableModel().getIndex(currentDomainObject);
//					getTableModel().setDomainObject(index, currentDomainObject);
					access.updateLongSession(currentDomainObject);
					currentObjectSublist.set(0, currentDomainObject);

					int selectedRow = dialog.getSelectedRow();
					currentDomainObject = access.findByPrimaryKeyLongSession(getContentTable().getSortedList().get(selectedRow).getIdentifier());
					getContentTable().getSortedList().set(selectedRow, currentDomainObject);
                    getContentTable().setRowSelectionInterval(selectedRow, selectedRow);
                    currentObjectSublist = getContentTable().getSortedList().subList(selectedRow, selectedRow + 1);
					dialog.setModel(currentDomainObject, null);
					dialog.repaint();
				} catch (PersistenceException persistenceException) {
					System.out.println("Persistence Exception " + persistenceException);
				} catch (LookupException lookupException) {
					System.out.println("Lookup Exception " + lookupException);
				}
			}
		}
	}
	public DomainSortableTable getContentTable() {
		return contentTable;
	}

	public void setContentTable(DomainSortableTable contentTable) {
		this.contentTable = contentTable;
	}

    /**
     * Method to return the class this editor is for
     * @return The class this editor is for
     */
    public Class getClazz() {
        return clazz;
    }

    /**
     * Method to return this as a JDialog. Used by internal threads that need
     * to open up a dialog or progress monitor.
     * @return This as a Jdialog
     */
    public JDialog getThisAsJDialog() {
        return this;
    }
}
