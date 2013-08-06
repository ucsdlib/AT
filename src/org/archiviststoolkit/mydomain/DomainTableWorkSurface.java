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

import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import net.sf.jasperreports.engine.JRField;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.Main;
import org.archiviststoolkit.plugin.ATPluginFactory;
import org.archiviststoolkit.plugin.ATPlugin;
import org.archiviststoolkit.dialog.ATFileChooser;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.dialog.QueryEditor;
import org.archiviststoolkit.dialog.ATDebugDialog;
import org.archiviststoolkit.exceptions.*;
import org.archiviststoolkit.exporter.EADExportHandler;
import org.archiviststoolkit.exporter.MARCExportHandler;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.report.ATReport;
import org.archiviststoolkit.report.CompiledJasperReport;
import org.archiviststoolkit.swing.*;
import org.archiviststoolkit.util.FileUtils;
import org.archiviststoolkit.util.MyTimer;
import org.archiviststoolkit.util.ResourceUtils;
import org.archiviststoolkit.util.RecordLockUtils;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.*;
import java.util.List;


/**
 * A worksurface which can display a domain table.
 */

public class DomainTableWorkSurface implements WorkSurface, MouseListener, ActionListener {


	private static Logger logger = Logger.getLogger(Main.class.getPackage().getName());
	private boolean debug = false;

	private boolean done;

    // boolean to say when a record is being open. This is to prevent a bug
    // ART-2331 "Illegal access to loading collection" from occurring
    private boolean recordOpening = false;

  /**
   * Variable to see if to display the confirm dialog that comes up on save button press
   */
  public static boolean showConfirmDialog = true;


  /**
	 * new action.
	 */
	private ConcreteAction newAction = null;

	/**
	 * edit action.
	 */
	private ConcreteAction editAction = null;

//	/**
//	 * copy action.
//	 */
//	private ConcreteAction copyAction = null;

	/**
	 * query action.
	 */
	private ConcreteAction searchAction = null;
	private ConcreteAction findAllAction = null;
	private ConcreteAction showSelectedAction = null;
	private ConcreteAction showUnSelectedAction = null;
	private ConcreteAction assignPersistentIds = null;

//	/**
//	 * paste action.
//	 */
//	private ConcreteAction pasteAction = null;
//
//	/**
//	 * cut action.
//	 */
//	private ConcreteAction cutAction = null;

	/**
	 * delete action.
	 */
	private ConcreteAction deleteAction = null;

    // debug action used for testing thread locking
    private ConcreteAction debugAction = null;

    /**
	 * The class of this worksurface.
	 */

	protected Class clazz = null;
	protected JTextField filterField;

	/**
	 * The JTable used to render internally.
	 */

	protected DomainSortableTable table = null;

	/**
	 * The root component of this worksurface.
	 */
	protected JPanel rootComponent = new JPanel();
	protected JScrollPane scrollPane;
	protected DomainTableListEventListener eventListener;

	protected JLabel resultSizeDisplay;
	protected String humanReadableSearchString = "";

	/**
	 * A popup menu used to manipulate entries in the worksurface.
	 */
	private JPopupMenu pm = null;

	/**
	 * An icon for the worksurface.
	 */
	private Icon icon = null;

	/**
	 * A displayable name for the worksurface.
	 */
	private String name = null;

	/**
	 * A displayable tooltip for a specific worksuface.
	 */
	private String tooltip = null;

	/**
	 * the sortable table model.
	 */
//	private SortableTableModel sortableTableModel = null;
//	private DomainEventTableModel domainObjectTableModel;

	protected DomainEditor dialog;
	protected DomainAccessObject access;
	protected DomainObject currentDomainObject;
	protected int selectedRow;
	protected Collection<DomainObject> resultSet = new ArrayList<DomainObject>();

	private ATFileChooser filechooser;

	//	private MARCExportHandler marcHandler;
	Session longSessionForPrinting;

	/**
	 * event list that hosts the issues
	 */
	protected List<DomainObject> currentObjectSublist;

	int rowCount = 0;

    // Stores the current record lock which needs to be removed
    protected RecordLocks currentRecordLock;

    /**
	 * Constructor.
	 *
	 * @param clazz the domain model class
	 * @param name  the name of this worksurface
	 * @param icon  the icon of this worksurface
	 */
	public DomainTableWorkSurface(final Class clazz, final String name, final Icon icon) {
		init(clazz, name, icon, null);
	}

	/**
	 * Constructor.
	 *
	 * @param clazz	   the domain model class
	 * @param name		the name of this worksurface
	 * @param icon		the icon of this worksurface
	 * @param tableFormat the table format to use for the domain table
	 */
	public DomainTableWorkSurface(final Class clazz, final String name, final Icon icon, DomainTableFormat tableFormat) {
		init(clazz, name, icon, tableFormat);
	}

	public DomainTableWorkSurface() {
	}

	protected void init(Class clazz, String name, Icon icon, DomainTableFormat tableFormat) {
		this.icon = icon;
		this.name = name;

		filterField = ApplicationFrame.getInstance().getFilterField(clazz);
		resultSizeDisplay = new JLabel("0 Record(s)");
		this.clazz = clazz;

		rootComponent.setLayout(new BorderLayout());
		initTable(false, tableFormat);

		// build a panel to hold the filter
		JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//		filterPanel.add(new JLabel("Filter:"));
//		filterPanel.add(filterField);
		filterPanel.add(resultSizeDisplay);
		filterPanel.setBackground(ApplicationFrame.BACKGROUND_COLOR);
		rootComponent.add(filterPanel, BorderLayout.NORTH);

		newAction = new ConcreteAction("new");
		editAction = new ConcreteAction("edit");
//		copyAction = new ConcreteAction("copy");
		searchAction = new ConcreteAction("search");
		findAllAction = new ConcreteAction("list all");
		showSelectedAction = new ConcreteAction("list selected records");
		showUnSelectedAction = new ConcreteAction("omit selected records");
//		pasteAction = new ConcreteAction("paste");
//		cutAction = new ConcreteAction("paste");
		deleteAction = new ConcreteAction("delete");
		assignPersistentIds = new ConcreteAction("assign persistent ids");
        debugAction = new ConcreteAction("Debug");

        pm = new JPopupMenu();
		pm.add(new JMenuItem(newAction));
		pm.add(new JMenuItem(editAction));

        if(Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_ADVANCED_DATA_ENTRY)) {
		    pm.add(new JMenuItem(deleteAction));
        }

        pm.add(new JMenuItem(searchAction));
		pm.add(new JMenuItem(findAllAction));
		pm.add(new JMenuItem(showSelectedAction));
		pm.add(new JMenuItem(showUnSelectedAction));

        if (this.clazz == Resources.class && Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_SUPERUSER)) {
			pm.add(new JMenuItem(assignPersistentIds));
		}

        if(debug) {
            pm.addSeparator();
            pm.add(new JMenuItem(debugAction));
            debugAction.addActionListener(this);
        }

        pm.addMouseListener(this);

		newAction.addActionListener(this);
		editAction.addActionListener(this);
		deleteAction.addActionListener(this);
		searchAction.addActionListener(this);
		findAllAction.addActionListener(this);
		showSelectedAction.addActionListener(this);
		showUnSelectedAction.addActionListener(this);
		assignPersistentIds.addActionListener(this);
//		cutAction.setEnabled(false);
//		copyAction.setEnabled(false);
//		pasteAction.setEnabled(false);
	}

	private void initTable(boolean update, DomainTableFormat tableFormat) {
		if (update) {
			table.removeMouseListener(this);
			table.getFilteredList().removeListEventListener(eventListener);
		}
		table = new DomainSortableTable(clazz, filterField, tableFormat);
		table.setTableType(AlternatingRowColorTable.TABLE_TYPE_NORMAL);
		eventListener = new DomainTableListEventListener();
		table.getFilteredList().addListEventListener(eventListener);
		if (update) {
			rootComponent.remove(scrollPane);
		} else {
			table.addMouseListener(this);
		}
		table.setDefaultRenderer(Date.class, new DateTableCellRenderer());
		scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		rootComponent.add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * get the action which creates.
	 *
	 * @return the action
	 */

	public final ConcreteAction getNewAction() {
		return newAction;
	}

	/**
	 * called when the mouse is pressed.
	 *
	 * @param evt the mouse event
	 */
	public final void mousePressed(final MouseEvent evt) {
		if (evt.isPopupTrigger()) {
			pm.show(evt.getComponent(), evt.getX(), evt.getY());
		}

	}

	/**
	 * called when the mouse is released.
	 *
	 * @param evt the mouse event
	 */

	public final void mouseReleased(final MouseEvent evt) {
		if (evt.isPopupTrigger()) {
			pm.show(evt.getComponent(), evt.getX(), evt.getY());
		}

	}

	/**
	 * Deals with a double click and therefore edit event on the table.
	 *
	 * @param e the mouse event
	 */

	public final void mouseClicked(final MouseEvent e) {
		if (debug) {
			System.out.println("click from:" + this);
		}
		if (e.getClickCount() == 2) {
			onUpdate();
		}
	}

	/**
	 * mouse has entered the component.
	 *
	 * @param e the event
	 */

	public final void mouseEntered(final MouseEvent e) {
	}

	/**
	 * mouse has left the component.
	 *
	 * @param e the event
	 */

	public final void mouseExited(final MouseEvent e) {
	}

	/**
	 * Get the JTable used for this worksurface.
	 *
	 * @return the JTable
	 */

	public final JTable getTable() {
		return table;
	}

//	public DomainEventTableModel getTableModel() {
//		return this.domainObjectTableModel;
//	}

	/**
	 * Get the icon associated with this worksurface.
	 *
	 * @return icon the icon
	 */

	public final Icon getIcon() {
		return (this.icon);
	}

	/**
	 * Get the name of this worksurface.
	 *
	 * @return the name
	 */

	public final String getName() {
		return (this.name);
	}

	/**
	 * Get the underlying component for this worksurface.
	 *
	 * @return the root component
	 */

	public final JComponent getComponent() {
		return (rootComponent);
	}

	/**
	 * Get the tooltip string for this worksurface.
	 *
	 * @return the tooltip
	 */

	public final String getTooltip() {
		return (this.tooltip);
	}

	protected void updateRowCount() {
		rowCount = table.getFilteredList().size();
		int totalRecords = table.getEventList().size();
		String resultString;
		if (rowCount == totalRecords) {
			resultString = rowCount + " Record(s)";
		} else {
			resultString = rowCount + " of " + totalRecords + " Record(s)";
		}
		if (humanReadableSearchString != null && humanReadableSearchString.length() > 0) {
			resultString ="<html>" + resultString + " found for search \"<FONT COLOR='blue'>" + humanReadableSearchString + "</FONT>\"</html>";
		}
		resultSizeDisplay.setText(resultString);
	}

	/**
	 * do something useful when an event occurs.
	 *
	 * @param actionEvent the event to process
	 */

	public final void actionPerformed(final ActionEvent actionEvent) {
		Object actionSource = actionEvent.getSource();
		if (actionSource == this.newAction) {
			onInsert();

		} else if (actionSource == this.deleteAction) {
			onDelete();

		} else if (actionSource == this.editAction) {
			onUpdate();

		} else if (actionSource == this.searchAction) {
			onSearch();

		} else if (actionSource == this.findAllAction) {
			onFindAll();

		} else if (actionSource == this.showSelectedAction) {
			if (table.getSelectedRows().length > 0) {
				ArrayList<DomainObject> newResultSet = new ArrayList<DomainObject>();
				for (int index : table.getSelectedRows()) {
					newResultSet.add(table.getFilteredList().get(index));
				}
				humanReadableSearchString = "";
				updateListWithNewResultSet(newResultSet);
			} else {
				JOptionPane.showMessageDialog(dialog, "You must select at least one row to list");
			}

		} else if (actionSource == this.assignPersistentIds) {

			Thread performer = new Thread(new Runnable() {
				public void run() {
					InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000);
					monitor.start("Assigning persistent Ids...");
					try {
						Resources resource;
						int numberOfSelectedRecords = table.getSelectedRows().length;
						int count = 1;
						for (int index : table.getSelectedRows()) {
							resource = (Resources) table.getFilteredList().get(index);
							monitor.setTextLine("Processing" + resource.getTitle() + " (" + count++ + " of " + numberOfSelectedRecords + ")", 1);
							try {
								ResourceUtils.assignPersistentIds(resource.getIdentifier(), monitor);
							} catch (LookupException e) {
								monitor.close();
								new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
							} catch (PersistenceException e) {
								monitor.close();
								new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
							}
						}
					} finally {
						monitor.close();
					}
				}
			}, "persistanceIds");
			performer.start();


		} else if (actionSource == this.showUnSelectedAction) {
			if (table.getSelectedRows().length > 0) {
				ArrayList<Integer> selectedIndexes = new ArrayList<Integer>();
				for (int index : table.getSelectedRows()) {
					selectedIndexes.add(index);
				}


				ArrayList<DomainObject> newResultSet = new ArrayList<DomainObject>();
				for (int i = 0; i < table.getRowCount(); i++) {
					if (!selectedIndexes.contains(i)) {
						newResultSet.add(table.getFilteredList().get(i));
					}
				}
				humanReadableSearchString = "";
				updateListWithNewResultSet(newResultSet);
			} else {
				JOptionPane.showMessageDialog(dialog, "You must select at least one row to omit");
			}

		} else if(actionSource == this.debugAction) {
            ATDebugDialog debugDialog = new ATDebugDialog(this, dialog);
            debugDialog.setVisible(true);
        } else if (actionSource == dialog.getNextButton() ||
				actionSource == dialog.getPreviousButton() ||
				actionSource == dialog.getFirstButton() ||
				actionSource == dialog.getLastButton()) {
			if (dialog.getModel().validateAndDisplayDialog(actionEvent)) {

				try {
					//before anything else try saving the record to see if it fails if it is not read only
					if(!dialog.getReadOnly()) {
                        access.updateLongSession(currentDomainObject);
                    } else { // need to roll back any changes that were made
                        try {
                            access.closeLongSessionRollback();
                        } catch (SQLException e) {
                            new ErrorDialog(dialog, "Error canceling record.", e).showDialog();
                        }
                    }

                    if (debug) {
						System.out.println("Thread: " + Thread.currentThread());
					}

                    // remove the current record lock
                    clearCurrentRecordLock();

                    //adjust the selected row
					if (actionSource == dialog.getNextButton()) {
						selectedRow++;
					} else if (actionSource == dialog.getPreviousButton()) {
						selectedRow--;
					} else if (actionSource == dialog.getFirstButton()) {
						selectedRow = 0;
					} else if (actionSource == dialog.getLastButton()) {
						selectedRow = rowCount - 1;
					}

					dialog.setSelectedRow(selectedRow);
					dialog.setNavigationButtons();
					table.setRowSelectionInterval(selectedRow, selectedRow);

					setSublistWithCurrentDomainObject();

					DomainObject domainObject = table.getFilteredList().get(selectedRow);
					if (this.clazz == Resources.class) {
						if (debug) {
							ApplicationFrame.getInstance().getTimer().reset();
							System.out.println("Loading resource");
						}
					}

                    // check to see if this record is locked and if it is then ask user
                    // if the want to open in read only mode
                    RecordLocks recordLock = isRecordLocked(domainObject);
                    if (recordLock != null) {
                        // ask the user if the want to open record read only
                        if (!openRecordReadOnly(recordLock)) {
                            dialog.closeAndNoSave();
                            return;
                        } else {
                            dialog.setReadOnly(true); // not sure what this those
                        }
                    } else { // set read only to false
                        dialog.setReadOnly(false);
                    }

                    getCurrentDomainObjectFromDatabase(domainObject);

                    Thread performer = new Thread(new Runnable() {
						public void run() {
							InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000);
							monitor.start("Loading Record...");
							try {
								if (debug) {
									System.out.println("Thread: " + Thread.currentThread());
								}
								setFilteredListWithCurrentDomainObject();
								currentObjectSublist = table.getFilteredList().subList(selectedRow, selectedRow + 1);
								dialog.setModel(currentDomainObject, monitor);
								dialog.setRecordPositionText(selectedRow, table.getFilteredList().size());
								if (debug) {
									System.out.println("Final Load record: " + MyTimer.toString(ApplicationFrame.getInstance().getTimer().elapsedTimeMillisSplit()));
									System.out.println("Total: " + MyTimer.toString(ApplicationFrame.getInstance().getTimer().elapsedTimeMillis()) + "\n");
								}
								monitor.close();
								finishNavigateRecord();
							} finally {
								monitor.close();
							}
						}
					}, "Record Navigation");
					performer.start();
				} catch (PersistenceException persistenceException) {
					if (persistenceException.getCause() instanceof ConstraintViolationException) {
						String message = "Can't save, duplicate record " + currentDomainObject;
						JOptionPane.showMessageDialog(ApplicationFrame.getInstance(), message);
						logger.error(message, persistenceException);
						try {
							access.closeLongSessionRollback();
//							Session longSession = access.getLongSession();
//							longSession.lock(currentDomainObject, LockMode.NONE);
						} catch (SQLException e) {
							new ErrorDialog(dialog, "Error reverting record", e).showDialog();
						}
					} else {
						new ErrorDialog(dialog, "Error saving new record.", persistenceException).showDialog();
					}
				} catch (LookupException lookupException) {
					new ErrorDialog(dialog, "Error saving new record.", lookupException).showDialog();
				}
			}
		}
    }

	protected void setFilteredListWithCurrentDomainObject() {
		table.getFilteredList().set(selectedRow, currentDomainObject);
	}

	protected void setSublistWithCurrentDomainObject() {
        if(currentDomainObject == null) {
            currentObjectSublist.set(0, null);

        } else {
          long id = currentDomainObject.getIdentifier();
          currentObjectSublist.set(0, getSimpleDomainObject(id));
        }
    }

    // function to get a simple domain object to add tojlist
    private DomainObject getSimpleDomainObject(long id) {
        DomainObject domainObject = null;
        try {
            DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);
            domainObject = access.findByPrimaryKey(id);
        } catch(PersistenceException e) {
            new ErrorDialog("", e).showDialog();
        } catch(LookupException e) {
            new ErrorDialog("", e).showDialog();
        }
        return domainObject;
    }

    private void finishNavigateRecord() {
		dialog.repaint();
	}

	public void onDelete() {
		selectedRow = this.getTable().getSelectedRow();
		if (selectedRow != -1) {
			int response = JOptionPane.showConfirmDialog(ApplicationFrame.getInstance(),
					"Are you sure you want to delete " + table.getSelectedRows().length + " record(s)",
					"Delete records", JOptionPane.YES_NO_OPTION);
			if (response == JOptionPane.OK_OPTION) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						int[] selectedRows = table.getSelectedRows();

						if (selectedRows.length != 0) {
							onRemoveGroup(selectedRows);
						}
					}
				});
			}
		} else {
			JOptionPane.showMessageDialog(dialog, "You must select at least one record to delete.");
		}
	}

	/**
	 * onUpdate show the appropriate editor.
	 */

	public final void onUpdate() {
		selectedRow = this.getTable().getSelectedRow();

        // check to see if the record is currently being
        // opened. A bug will be thrown if we try an open the record twice
        if(recordOpening) {
            return;
        }

		if (selectedRow != -1) {
            recordOpening = true;

            try {
				access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);

				DomainObject domainObject = table.getFilteredList().get(selectedRow);
				if (debug && this.clazz == Resources.class) {
					ApplicationFrame.getInstance().getTimer().reset();
					System.out.println("Loading resource");
				}
				LookupWorkerRunnable runnable = new LookupWorkerRunnable(domainObject);
				Thread backgroundWorker = new Thread(runnable);
				backgroundWorker.start();
			} catch (PersistenceException e) {
                recordOpening = false;
				new ErrorDialog(dialog, "Error updating record", e).showDialog();
			}
		} else {
			JOptionPane.showMessageDialog(dialog, "You must select a record to edit first");

		}
	}

	private void finishOnUpdate() {
        done = false;
        while (!done) {
            if (debug) {
                System.out.println("Thread: " + Thread.currentThread());
            }

            // check to see if the record is not locked
            RecordLocks recordLock = isRecordLocked(currentDomainObject);
            if(recordLock != null) {
                // ask the user if the want to open record read only
                if(!openRecordReadOnly(recordLock)) {
                    break;
                } else {
                    dialog.setReadOnly(true); // not sure what this those
                }
            }

            if (dialog.showDialog() == javax.swing.JOptionPane.OK_OPTION) {
                int choice = dialog.getConfirmDialogReturn();

                if (choice == JOptionPane.YES_OPTION) {
                    saveRecord();
                    //wait for the save record to complete
                    try {
                        performer.join();
                    } catch (InterruptedException e) {
                        new ErrorDialog("", e).showDialog();
                    }
                }
            } else if (dialog.getConfirmDialogReturn() == JOptionPane.YES_OPTION) { // called when window is closed and data validated
                saveRecord();
                //wait for the save record to complete
                try {
                    performer.join();
                } catch (InterruptedException e) {
                    new ErrorDialog("", e).showDialog();
                }
                done = true;
            } else if (dialog.getConfirmDialogReturn() == StandardEditor.NO_SAVE_OPTION) {
                try {
                    access.closeLongSessionRollback();
                    setSublistWithCurrentDomainObject(); // call this to update the display
                } catch (SQLException e) {
                    new ErrorDialog(dialog, "Error canceling record.", e).showDialog();
                }
                done = true;
            } else { // No Option was selected so roll back changes
                done = true;
                try {
                    access.closeLongSessionRollback();
                } catch (SQLException e) {
                    new ErrorDialog(dialog, "Error canceling record.", e).showDialog();
                }
            }

            // Remove any locks created while accessing record and set to read only false
            clearCurrentRecordLock();
            dialog.setReadOnly(false);
        }
	}

	private Thread performer;

	private void saveRecord() {
		performer = new Thread(new Runnable() {
			public void run() {
				InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000);
				monitor.start("Saving Record...");
                DomainObject domainObject = getCurrentDomainObject();
				try {
                    access.updateLongSession(domainObject);
					setSublistWithCurrentDomainObject();
					done = true;
				} catch (PersistenceException e) {
					if (e.getCause() instanceof ConstraintViolationException) {
						monitor.close();
						String message = "Can't save, duplicate record " + domainObject;
						JOptionPane.showMessageDialog(dialog, message);
						logger.error(message, e);
						monitor.close();
					} else {
						try {
							access.closeLongSessionRollback();
						} catch (SQLException e1) {
							monitor.close();
							new ErrorDialog("", e1).showDialog();
						}
						monitor.close();
						new ErrorDialog(dialog, "Error saving record.", e).showDialog();
						done = true;
						dialog.setVisible(false);
					}
				} finally {
					monitor.close();
				}
			}
		}, "SaveRecord");
		performer.start();
	}

	/**
	 * onRemoveGroup delete a specific domain object.
	 *
	 * @param selectedIndexes the indexes of the domain objects
	 */

	public final void onRemoveGroup(final int[] selectedIndexes) {
		try {

			final ArrayList<DomainObject> deleteList = new ArrayList<DomainObject>();

			final DomainAccessObject access;
			if (clazz == Resources.class) {
				access = new ResourcesDAO();
			} else {
				access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);
			}

            String deleteProblems = ""; // alert thev user that some records couldn't be deleted cause they are in use

            for (int loop = 0; loop < selectedIndexes.length; loop++) {
				if (debug) {
					System.out.println("Adding " + selectedIndexes[loop]);
				}
				// check to see if this record is not locked before deleting it
                DomainObject domainObject = table.getFilteredList().get(selectedIndexes[loop]);
                if(isRecordLocked(domainObject, false) == null) {
                    deleteList.add(domainObject);
                } else {
                    // add text message that will alert the user that this records can't be deleted cause it's locked
                    deleteProblems += "The record \"" + domainObject + "\" is in use by another user\n";
                }
			}

            if (clazz == Resources.class) {
				Thread performer = new Thread(new Runnable() {
					public void run() {
						InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000, true);
						monitor.start("Deleting records...");
						try {
							((ResourcesDAO) access).deleteGroup(deleteList, monitor);
							for (DomainObject o : deleteList) {
								resultSet.remove(o);
								table.getEventList().remove(o);
								removeFromPickers(o);
							}
							updateRowCount();
						} catch (PersistenceException e) {
							monitor.close();
							new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
						} finally {
							monitor.close();
						}
					}
				}, "RemoveGroup");
				performer.start();
			} else {
				access.deleteGroup(deleteList);
				for (DomainObject o : deleteList) {
					resultSet.remove(o);
					table.getEventList().remove(o);
					removeFromPickers(o);
				}
				updateRowCount();
			}

            // check to see if any of the record could not be deleted cause it was locked
            if (deleteProblems.length() > 0) {
                JOptionPane.showMessageDialog(ApplicationFrame.getInstance(), "There were problems deleting some records\n" + deleteProblems);
            }
		} catch (LookupException lookupException) {
			new ErrorDialog(ApplicationFrame.getInstance(), "Error removing records", lookupException).showDialog();
		} catch (PersistenceException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "Error removing records", e).showDialog();
		}
	}

	private void removeFromPickers(DomainObject instance) {
		if (this.clazz == Subjects.class) {
			Subjects.removeSubjectFromLookupList((Subjects) instance);
		} else if (this.clazz == Names.class) {
			Names.removeNameFromLookupList((Names) instance);
		} else if (this.clazz == Resources.class) {
            if(instance instanceof Resources) { // check to make sure this id resource before it remove from the lookup list
			    Resources.removeResourceFromLookupList((Resources) instance);
            } else {
                ResourcesComponentsSearchResult searchResult = (ResourcesComponentsSearchResult) instance;

                // if the return search result is not a component
                // get the resource record and delete it
                if (searchResult.getCompenent() == null) {
                    Resources.removeResourceFromLookupList(searchResult.getParentResource());
                }
            }
		}
	}

	/**
	 * on insert display a blank editor and insert the results.
	 */

	public final void onInsert() {
        // see if to load a plugin editor instead of using the one built into the AT
        if (usePluginDomainEditor(null, null)) {
            return; // custom editor was used to view/edit this record so just return
        }

        ApplicationFrame.editorOpen = true;

        Object instance = null;
        boolean done = false;
        boolean createNewInstance = true;
        DomainEditor dialog = DomainEditorFactory.getInstance().getDialog(clazz);
        dialog.disableNavigationButtons();
        dialog.clearRecordPositionText();
        dialog.setNewRecord(true);
        int returnStatus;

        while (!done) {
            boolean createObject = true;
            if (createNewInstance) {
                try {
                    instance = clazz.newInstance();
                } catch (InstantiationException instantiationException) {
                    new ErrorDialog(dialog, "Error creating new record.", instantiationException).showDialog();
                } catch (IllegalAccessException illegalAccessException) {
                    new ErrorDialog(dialog, "Error creating new record.", illegalAccessException).showDialog();
                }
                if (clazz == Names.class) {
                    String nameType = Names.selectNameType(rootComponent);
                    if ((nameType != null) && (nameType.length() > 0)) {
                        ((Names) instance).setNameType(nameType);
                    } else {
                        createObject = false;
                        done = true;
                    }

                } else if (clazz == Accessions.class) {
                    ((Accessions) instance).setRepository(ApplicationFrame.getInstance().getCurrentUserRepository());
                } else if (clazz == Resources.class) {
                    ((Resources) instance).setRepository(ApplicationFrame.getInstance().getCurrentUserRepository());
                    ((Resources) instance).markRecordAsNew();
                } else if (clazz == DigitalObjects.class) {
                    ((DigitalObjects) instance).setRepository(ApplicationFrame.getInstance().getCurrentUserRepository());
                }
            } else {
                createNewInstance = true;
            }


            if (createObject) {

                dialog.setModel((DomainObject) instance, null);
                returnStatus = dialog.showDialog();
                int choice = dialog.getConfirmDialogReturn();
                Boolean savedNewRecord = dialog.getSavedNewRecord();

                try {
                    if (returnStatus == javax.swing.JOptionPane.OK_OPTION) {
                        if (choice == JOptionPane.YES_OPTION) {
                            DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);
                            if (!savedNewRecord) {
                                access.add((DomainObject) instance);
                            } else { // just update the record
                                access.updateLongSession((DomainObject) instance);
                            }

                            table.getEventList().add((DomainObject) instance);
                            updateRowCount();
                            addToPickers((DomainObject) instance);
                            done = true;
                        }
                    } else if (returnStatus == StandardEditor.OK_AND_ANOTHER_OPTION) {
                        DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);
                        if (!savedNewRecord) {
                            access.add(dialog.getModel());
                        } else {
                            access.updateLongSession((DomainObject) instance);
                        }
                        table.getEventList().add((DomainObject) instance);
                        updateRowCount();
                        addToPickers((DomainObject) instance);
                        dialog.setNewRecord(true);
                    } else if (choice == JOptionPane.YES_OPTION) { // called when window is closed and data validated
                        DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);
                        if (!savedNewRecord) {
                            access.add((DomainObject) instance);
                        } else {
                            access.updateLongSession((DomainObject) instance);
                        }

                        table.getEventList().add((DomainObject) instance);
                        updateRowCount();
                        addToPickers((DomainObject) instance);
                        done = true;
                    } else if (choice == JOptionPane.NO_OPTION) {
                        if (savedNewRecord) { // already saved a new record so remove it
                            try {
                                DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);
                                access.closeLongSessionRollback();
                            } catch (SQLException e) {
                                new ErrorDialog(dialog, "Error canceling record.", e).showDialog();
                            }

                            //table.getEventList().add((DomainObject) instance);
                            long id = ((DomainObject) instance).getIdentifier();
                            table.getEventList().add(getSimpleDomainObject(id));
                            updateRowCount();
                            addToPickers((DomainObject) instance);
                        }
                        done = true;
                    } else {
                        done = true;
                    }

                    // reset the savedNew field to false;
                    dialog.setSavedNewRecord(false);

                    // remove any record locks that may have been created
                    clearCurrentRecordLock();
                } catch (ConstraintViolationException persistenceException) {
                    String message = "Can't save, duplicate record " + instance;
                    JOptionPane.showMessageDialog(ApplicationFrame.getInstance(), message);
                    logger.error(message, persistenceException);
                    ((DomainObject) instance).removeIdAndAuditInfo();
                    createNewInstance = false;
                } catch (PersistenceException persistenceException) {
                    if (persistenceException.getCause() instanceof ConstraintViolationException) {
                        String message = "Can't save, duplicate record " + instance;
                        JOptionPane.showMessageDialog(ApplicationFrame.getInstance(), message);
                        logger.error(message, persistenceException);
                        ((DomainObject) instance).removeIdAndAuditInfo();
                        createNewInstance = false;
                    } else {
                        done = true;
                        new ErrorDialog(dialog, "Error saving new record.", persistenceException).showDialog();
                    }
                } /*catch (DeleteException deleteException) {
                    done = true;
                    new ErrorDialog(dialog, "Error deleting saved record.", deleteException).showDialog();
                }*/
            }
        }
        dialog.setNewRecord(false);

        ApplicationFrame.editorOpen = false;
	}

	private void addToPickers(DomainObject instance) {
		if (this.clazz == Subjects.class) {
			Subjects.addSubjectToLookupList((Subjects) instance);
		} else if (this.clazz == Names.class) {
			Names.addNameToLookupList((Names) instance);
		} else if (this.clazz == Resources.class) {
			Resources.addResourceToLookupList((Resources) instance);
		}
	}

	public void addToResultSet(DomainObject domainObject) {
		table.getEventList().add(domainObject);
		updateRowCount();
	}

	public void onSearch() {

		try {
			final DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);
			final QueryEditor dialog = DomainEditorFactory.getInstance().getSearchDialog(clazz);

			if (dialog.showDialog() == javax.swing.JOptionPane.OK_OPTION) {
				Thread performer = new Thread(new Runnable() {
					public void run() {
						InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 0);
						monitor.start("Performing search...");
						try {
							Collection results = access.findByQueryEditor(dialog, monitor);
							monitor.close();
							humanReadableSearchString = access.getHumanReadableSearchString();
							updateListWithNewResultSet(results);
							monitor.close();
						} catch (LookupException e) {
							monitor.close();
							new ErrorDialog(ApplicationFrame.getInstance(), "Error searching.", e).showDialog();
						} finally {
							monitor.close();
						}
					}
				}, "Search");
				performer.start();
			}

		} catch (PersistenceException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "Error searching.", e).showDialog();
		}
	}

	public final void onFindAll() {

		Thread performer = new Thread(new Runnable() {
			public void run() {
				InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 0);
				monitor.start("Finding All Records...");
				DomainAccessObject access = null;
				try {
					access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);
					humanReadableSearchString = "list all";
					updateListWithNewResultSet(access.findAll(LockMode.READ));
					filterField.setText("");
				} catch (PersistenceException e) {
					monitor.close();
					new ErrorDialog(ApplicationFrame.getInstance(), "Error finding all.", e).showDialog();
				} catch (LookupException e) {
					monitor.close();
					new ErrorDialog(ApplicationFrame.getInstance(), "Error finding all.", e).showDialog();
				} finally {
					monitor.close();
				}
			}
		}, "FindAll");
		performer.start();
	}

	protected synchronized void updateListWithNewResultSet(Collection newResultSet) {
		this.resultSet = newResultSet;
		table.updateCollection(newResultSet);
		updateRowCount();
	}

	public Collection getResultSet() {
		return resultSet;
	}

	public ArrayList<DomainObject> getResultSetForPrinting(InfiniteProgressPanel progressPanel, ATReport report) throws PersistenceException, LookupException, UnsupportedClassException, ReportExecutionException {
		ArrayList<DomainObject> listForPrinting = new ArrayList<DomainObject>();
		DomainAccessObject access = null;
		access = new DomainAccessObjectImpl(clazz);
		int count = 1;
		int filteredListSize = table.getFilteredList().size();
//		ArrayList<Long> ids = new ArrayList<Long>();

		longSessionForPrinting = access.getLongSession();

		//if nothing is selected print all rows otherwise only selected
		//rows
		int[] selectedRows = table.getSelectedRows();
		if (selectedRows.length == 0) {
			for (DomainObject domainObject : table.getFilteredList()) {
				count = addObjectToPrintList(progressPanel, count, filteredListSize, domainObject, report, listForPrinting);
                if(progressPanel.isProcessCancelled()) { // cancel button pressed so break out of loop
                    break;
                }
			}
		} else {
			for (int selectedRow : selectedRows) {
				count = addObjectToPrintList(progressPanel, count, selectedRows.length, table.getFilteredList().get(selectedRow), report, listForPrinting);
                if(progressPanel.isProcessCancelled()) { // cancel button pressed so break out of loop
                    break;
                }
			}
		}
		return listForPrinting;
	}

	public int getSelectedRowCount() {
		return table.getSelectedRows().length;
	}

	private int addObjectToPrintList(InfiniteProgressPanel progressPanel,
									 int count,
									 int filteredListSize,
									 DomainObject domainObject,
									 ATReport report,
									 ArrayList<DomainObject> listForPrinting) throws ReportExecutionException {
		if (progressPanel != null) {
			progressPanel.setTextLine("Loading records for printing " + count++ + " of " + filteredListSize, 1);
		}

        // since resource components searvh results are not stored on the database then
        // not need to try to retrive them
        if(!(domainObject instanceof ResourcesComponentsSearchResult)) {
            longSessionForPrinting.lock(domainObject, LockMode.NONE);
        }

		initializeDomainObject(domainObject, report, progressPanel);
		listForPrinting.add(domainObject);
		return count;
	}

	public void closeLongSessionForPrinting() {
		if (longSessionForPrinting != null) {
			if (longSessionForPrinting.isOpen()) {
				longSessionForPrinting.close();
			}
		}

	}

	private void initializeDomainObject(DomainObject domainObject, ATReport report, InfiniteProgressPanel progressPanel) throws ReportExecutionException {
		if (report instanceof CompiledJasperReport) {
			for (JRField field : ((CompiledJasperReport) report).getFields()) {
				try {
					if (field.getName().equals(Resources.PROPERTYNAME_LOCATION_LIST)) {
						((Resources) domainObject).getLocationList(progressPanel);
					} else {
						BeanUtils.getProperty(domainObject, field.getName());
					}
				} catch (IllegalAccessException e) {
					throw new ReportExecutionException(e);
				} catch (InvocationTargetException e) {
					throw new ReportExecutionException(e);
				} catch (NoSuchMethodException e) {
					throw new ReportExecutionException(e);
				}
			}
		}
	}

	public DomainTableFormat getTableFormat() {
		return table.getTableFormat();
	}

	public Class getClazz() {
		return clazz;
	}

    /**
     * Method to return the clazz based on first domain object being displayed.
     * This is used for reports because for searches using resource records then
     * it is not resources objects that's listed it's actually
     * @return
     */
    public Class getClazzForReport() {
        DomainObject record = table.getFilteredList().get(0);
        if(record != null && record instanceof ResourcesComponentsSearchResult) {
            return ResourcesComponentsSearchResult.class;    
        } else {
            return getClazz();
        }
    }

	public void updateColumns() {
		table.updateColumns(resultSet);
		table.invalidate();
		table.validate();
		table.repaint();
	}

	protected class DomainTableListEventListener implements ListEventListener {
		public void listChanged(ListEvent event) {
			updateRowCount();
		}

	}

	public void exportMARC() {
		final int[] selectedIndexes = table.getSelectedRows();
		final ExportOptionsMARC exportOptions = new ExportOptionsMARC();
		final File selectedFileOrDirectory = FileUtils.chooseFileOrDirectory(selectedIndexes.length, exportOptions);

		if (selectedFileOrDirectory != null) {
			Thread performer = new Thread(new Runnable() {
				public void run() {
					// see whether to show the cancel button
                    boolean allowCancel = false;
                    if(selectedIndexes.length > 1) {
                        allowCancel = true;
                    }

                    InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000, allowCancel);
					monitor.start("Exporting...");
					MARCExportHandler batchMARCHandler = new MARCExportHandler(exportOptions);
					try {
						int[] selectedIndexes = table.getSelectedRows();
						Vector<DomainObject> resources = new Vector<DomainObject>();
						DomainObject domainObject = null;
						DomainObject fullDomainObject = null;

						for (int loop = 0; loop < selectedIndexes.length; loop++) {
							// check to see if this operation wasnt cancelled
                            if(monitor.isProcessCancelled()) {
                                break;
                            }

							domainObject = (table.getFilteredList().get(selectedIndexes[loop]));
							resources.add(domainObject);
						}

                        if(!monitor.isProcessCancelled()) {
                            Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler());
						    batchMARCHandler.export(selectedFileOrDirectory, resources, monitor);
                        }
					} catch (Exception e) {
						monitor.close();
						new ErrorDialog("", e).showDialog();
					} finally {
						monitor.close();
					}
				}
			}, "ExportMarc");
			performer.start();
		}

	}

    /**
     * Method to export METS. Get overriden in Digital Object Table Worksurface
     */
    public void exportMETS() { }

    /**
     * Method to export MODS. Get overriden in Digital Object Table Worksurface
     */
    public void exportMODS() { }

    /**
     * Method to export Dublin Core. Get overriden in Digital Table Object Worksurface
     */
    public void exportDublinCore() { }

	public void exportEAD() {
		final int[] selectedIndexes = table.getSelectedRows();
		final ExportOptionsEAD exportOptions = new ExportOptionsEAD();
		final File selectedFileOrDirectory = FileUtils.chooseFileOrDirectory(selectedIndexes.length, exportOptions);

		if (selectedFileOrDirectory != null) {
			Thread performer = new Thread(new Runnable() {
				public void run() {
                    // see whether to show the cancel button
                    boolean allowCancel = false;
                    if(selectedIndexes.length > 1) {
                        allowCancel = true;
                    }

                    InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000, allowCancel);
					monitor.start("Exporting...");
					EADExportHandler batchEADHandler = new EADExportHandler(exportOptions);

                    try {
						Vector<DomainObject> resources = new Vector<DomainObject>();
						DomainObject domainObject = null;
						DomainObject fullDomainObject = null;

						for (int loop = 0; loop < selectedIndexes.length; loop++) {
                            // check to see if this operation wasnt cancelled
                            if(monitor.isProcessCancelled()) {
                                break;
                            }

                            access = new ResourcesDAO();
							domainObject = (table.getFilteredList().get(selectedIndexes[loop]));
							//try {
							fullDomainObject = access.findByPrimaryKeyLongSession(domainObject.getIdentifier());
							//}
							//catch (LookupException le) {
							//monitor.close();
							///new ErrorDialog(ApplicationFrame.getInstance(), "", le).showDialog();
							//}
							resources.add(fullDomainObject);
							//resources.add(domainObject);
						}

                        if(!monitor.isProcessCancelled()) {
                            Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler());
						    batchEADHandler.export(selectedFileOrDirectory, resources, monitor);
                        }
					} catch (Exception e) {
						monitor.close();
						new ErrorDialog("", e).showDialog();
					} finally {
						monitor.close();
					}
				}
			}, "ExportEAD");
			performer.start();
		}

	}

	public void merge() {
		int[] selectedRows = table.getSelectedRows();
		if (selectedRows.length < 2) {
			JOptionPane.showMessageDialog(ApplicationFrame.getInstance(), "You must select at least 2 records to merge");
		} else {
			Vector<DomainObject> selectedDomainObjects = new Vector<DomainObject>();
			for (int loop = 0; loop < selectedRows.length; loop++) {
				selectedDomainObjects.add(table.getFilteredList().get(selectedRows[loop]));
			}

			SelectFromTable dialog = new SelectFromTable(clazz, ApplicationFrame.getInstance(), "Select an item to merge into", selectedDomainObjects);
			if (dialog.showDialog() == JOptionPane.OK_OPTION) {

				DomainAccessObject dao;
				if (clazz == Names.class) {
					dao = new NamesDAO();
					mergeRecords(dialog, selectedDomainObjects, dao);
				} else if (clazz == Subjects.class) {
					dao = new SubjectsDAO();
					mergeRecords(dialog, selectedDomainObjects, dao);
				} else if (clazz == Resources.class) {
					dao = new ResourcesDAO();
					mergeRecords(dialog, selectedDomainObjects, dao);
				}
			}
		}
	}

	private void mergeRecords(final SelectFromTable dialog,
							  final Vector<DomainObject> selectedDomainObjects,
							  final DomainAccessObject dao) {

		final int numberOfRecords = selectedDomainObjects.size();
		final DomainObject selectedDomainObject = dialog.getSelectedValue();
		String message;
		if (clazz == Resources.class) {
			message = "Are you sure you want to merge " + (numberOfRecords - 1) + " resource(s) into " + selectedDomainObject + "? \nThis will delete the other resource(s) including all top-level information.";
		} else {
			message = "Are you sure you want to merge " + (numberOfRecords - 1) + " record(s) into " + selectedDomainObject + "?";
		}
		if (JOptionPane.showConfirmDialog(ApplicationFrame.getInstance(), message, "Merge", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			Thread performer = new Thread(new Runnable() {
				public void run() {
					InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000);
					monitor.start("Merging Records...");
					try {
						dao.merge(selectedDomainObjects, selectedDomainObject, monitor);
						for (DomainObject domainObject : selectedDomainObjects) {
							if (!domainObject.equals(selectedDomainObject)) {
								resultSet.remove(domainObject);
								table.getEventList().remove(domainObject);
								removeFromPickers(domainObject);
							}
						}
					} catch (MergeException e) {
						monitor.close();
						new ErrorDialog(e).showDialog();
					} catch (Exception e) {
						monitor.close();
						new ErrorDialog(e).showDialog();
					} finally {
						monitor.close();
					}
				}
			}, "MergeRecords");
			performer.start();
		}
	}

	protected void getCurrentDomainObjectFromDatabase(DomainObject domainObject) throws LookupException {
        //currentDomainObject = access.findByPrimaryKeyLongSession(domainObject.getIdentifier());
        setCurrentDomainObject(access.findByPrimaryKeyLongSession(domainObject.getIdentifier()));
	}

    /**
     * Synchonized method to prevent a saved thread and on update thread from having simultanoues access
     * to the current domain object
     * @return The current domain object
     */
    public synchronized DomainObject getCurrentDomainObject() {
        return currentDomainObject;
    }

    protected synchronized void setCurrentDomainObject(DomainObject domainObject) {
        currentDomainObject = domainObject;
    }

    /**
     * Method to return the currently selected domain object. Currently used for debuging purposes 
     * @return The currently selected domain object
     */
    public DomainObject getCurrentDomainObjectFromDatabase() {
        selectedRow = this.getTable().getSelectedRow();
        if(selectedRow == -1) {
            return null; // no row selected to return null
        }

        try {
            access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);
	        DomainObject domainObject = table.getFilteredList().get(selectedRow);
            return access.findByPrimaryKeyLongSession(domainObject.getIdentifier());
        }
        catch(PersistenceException pe) {
            pe.printStackTrace();
            return null;
        } catch(LookupException le) {
            le.printStackTrace();
            return null;
        }
    }

	class LookupWorkerRunnable implements Runnable {
		private DomainObject domainObject;
        private InfiniteProgressPanel monitor;

		LookupWorkerRunnable(DomainObject domainObject) {
			this.domainObject = domainObject;
		}

		public void run() {
			final InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000);
            monitor.start("Loading Record ...");

            // see if to load a plugin editor instead of using the one built into the AT
            if(usePluginDomainEditor(monitor, domainObject)) {
                return; // custom editor was used to view/edit this record so just return
            }

            try {
				if (debug) {
					System.out.println("Initial Load record: " + MyTimer.toString(ApplicationFrame.getInstance().getTimer().elapsedTimeMillisSplit()));
				}
				currentObjectSublist = table.getFilteredList().subList(selectedRow, selectedRow + 1);

				dialog = DomainEditorFactory.getInstance().getDialog(clazz);
				dialog.setCallingTable(table);
				dialog.setSelectedRow(selectedRow);
				if (domainObject instanceof Resources) {
					monitor.setTextLine("Loading components...", 1);
				}
				getCurrentDomainObjectFromDatabase(domainObject);
				dialog.setModel(currentDomainObject, monitor);
				dialog.setRecordPositionText(selectedRow, table.getFilteredList().size());
				if (debug) {
					System.out.println("Final Load record: " + MyTimer.toString(ApplicationFrame.getInstance().getTimer().elapsedTimeMillisSplit()));
					System.out.println("Total: " + MyTimer.toString(ApplicationFrame.getInstance().getTimer().elapsedTimeMillis()) + "\n");
				}
				monitor.close();
                recordOpening = false;
			} catch (UnsupportedTableModelException e) {
				monitor.close();
                recordOpening = false;
				new ErrorDialog("", e).showDialog();
			} catch (LookupException e) {
				monitor.close();
                recordOpening = false;
				new ErrorDialog("", e).showDialog();
			} finally {
				// to ensure that progress dlg is closed in case of any exception
				monitor.close();
                recordOpening = false;
			}

            // launch the GUI through the event dispatch thread to prevent
            // any thread blocking when using the spell checker
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ApplicationFrame.editorOpen = true; // set to false to prevent update bug
                    finishOnUpdate();
                    ApplicationFrame.editorOpen = false;
                }
            }); 
		}
	}

    /**
     * Method that display a message to see if the user wants to open a record read only
     * if is locked by another record
     * @param recordLock The lock for the record
     * @return The user feedback
     */
    private boolean openRecordReadOnly(RecordLocks recordLock) {
        String message = "This record is currently in use by " + recordLock.getUserName(); 
        JOptionPane.showMessageDialog(ApplicationFrame.getInstance(), message, "Record in Use", JOptionPane.ERROR_MESSAGE);

        return false;
    }

    /**
     * Method to see if the record that the user is trying to open is locked. If it is then
     * return the record lock. If it is not locked then add a lock for it and return null;
     * @param domainObject the domain object to check
     * @param addLock Specifies whether a record lock should be created if one doesn't exist
     * @return The record lock that was found if any
     */
    private RecordLocks isRecordLocked(DomainObject domainObject, boolean addLock) {
        RecordLocks recordLock = null;

        recordLock = RecordLockUtils.getRecordLock(domainObject);

        if(recordLock == null && addLock) { // add a lock if it does not have one
            currentRecordLock = RecordLockUtils.addRecordLock(domainObject);
        } else {
            currentRecordLock = null;
        }

        return recordLock;
    }

    /**
     * Overloaded method for checking if a record is locked that will add one if it is not
     * @param domainObject The domain object to check to see if is locked
     * @return The record look that was found if any
     */
    private RecordLocks isRecordLocked(DomainObject domainObject) {
        return isRecordLocked(domainObject, true);
    }

    /**
     * Method to to set the record lock for this object
     * @param domainObject The record to add a lock for
     */
    public void addRecordLock(DomainObject domainObject) {
        currentRecordLock = RecordLockUtils.addRecordLock(domainObject);
    }

    /**
     * Method to clear the current record lock
     */
    public void clearCurrentRecordLock() {
        if(currentRecordLock != null) {
            RecordLockUtils.clearRecordLock(currentRecordLock);
            currentRecordLock = null;
        }
    }

    /**
     * Method to set the new selected row. This method is currently used for debuging purposes
     * @param newSelectedRow The new selected row
     * @return Whether the record for the selected row is locked already
     */
    public boolean setSelectedRow(int newSelectedRow) {
        DomainObject domainObject = table.getFilteredList().get(newSelectedRow);
        if(isRecordLocked(domainObject, false) == null) {
            selectedRow = newSelectedRow;
            dialog.setSelectedRow(selectedRow);
		    dialog.setNavigationButtons();
		    table.setRowSelectionInterval(selectedRow, selectedRow);
            return false;
        } else { // record already locked by another user so return true
            return true;
        }
    }

    /**
     * Method to return the selected row. Used mainly for debuging
     * @return The currently selected row
     */
    public int getSelectedRow() {
        return selectedRow;
    }

    /**
     * Method to return the domain editor for this worksurface. Method is used
     * by plugin which need to display the dialog.
     *
     * @return The domain editor for this worksurface
     */
    public DomainEditor getDomainEditor() {
        return dialog;
    }

    /**
     * Method to load a custom plugin domain editor for viewing the record. Usefull if someone want to
     * implemment an editor that is more suited for their workflow or to load a read only viewer for the
     * record.
     *
     * @return Whether any plugin editors where found
     */
    private boolean usePluginDomainEditor(InfiniteProgressPanel monitor, DomainObject domainObject) {
        ATPlugin plugin;
        boolean newInstance = false; // specifies whether this record is a new record

        // if the domain object is null then create a new instance
        if(domainObject == null) {
            if(clazz == Names.class) {
                domainObject = new Names();
            } else if(clazz == Subjects.class) {
                domainObject = new Subjects();
            } else if(clazz == Accessions.class) {
                domainObject = new Accessions();
                ((Accessions) domainObject).setRepository(ApplicationFrame.getInstance().getCurrentUserRepository());
            } else if(clazz == Resources.class) {
                domainObject = new Resources();
                ((Resources) domainObject).setRepository(ApplicationFrame.getInstance().getCurrentUserRepository());
                ((Resources) domainObject).markRecordAsNew();
            } else if(clazz == DigitalObjects.class) {
                domainObject = new DigitalObjects();
                ((DigitalObjects) domainObject).setRepository(ApplicationFrame.getInstance().getCurrentUserRepository());
            }

            // let the program know that this is a new record and not one being edited
            newInstance = true;
        }

        // get the access class of the current user and see if not to load a viewer
        int accessClass = ApplicationFrame.getInstance().getCurrentUserAccessClass();

        if (accessClass == Users.ACCESS_CLASS_REFERENCE_STAFF) {
            plugin = ATPluginFactory.getInstance().getViewerPlugin(domainObject);
        } else {
            plugin = ATPluginFactory.getInstance().getEditorPlugin(domainObject);
        }

        // if no plugin found and access class  = 0 then alert the user that no viwer plugin was found.
        if (plugin == null && accessClass == Users.ACCESS_CLASS_REFERENCE_STAFF) {
            String message = "Can't find viewer to open record\n" + domainObject;
            JOptionPane.showMessageDialog(ApplicationFrame.getInstance(), message);
            if(monitor != null) { // this might be null so make it null safe
                monitor.close();
            }
            return true;
        } else if (plugin == null) { // just return false and so that the built in domain object can be used
            return false;
        }

        // if class equals Names then set the name type now
        if(clazz == Names.class) {
            String nameType = Names.selectNameType(rootComponent);
            if ((nameType != null) && (nameType.length() > 0)) {
                ((Names) domainObject).setNameType(nameType);
            } else { // cancel was selected to just return
                return true;
            }
        }

        // display the record now if it is a record that is being edited
        plugin.setCallingTable(table);
        plugin.setSelectedRow(selectedRow);

        if(!newInstance) { // this means that it is a record being edited
            try {
                if (domainObject instanceof Resources) {
                    monitor.setTextLine("Loading components...", 1);
                }

                domainObject = getCurrentDomainObjectFromDatabase();
                plugin.setModel(domainObject, monitor);
                plugin.setRecordPositionText(selectedRow, table.getFilteredList().size());

                monitor.close();
            } catch (Exception e) {
                if(monitor != null) {
                    monitor.close();
                }
                new ErrorDialog("", e).showDialog();
            } finally {
                // to ensure that progress dlg is closed in case of any exception
                if(monitor != null) {
                    monitor.close();
                }
            }
        } else { // its a new record to just set the model
            plugin.setModel(domainObject, null);
        }

        // set the main program application frame and display it
        plugin.setApplicationFrame(ApplicationFrame.getInstance());
        plugin.showPlugin(ApplicationFrame.getInstance());

        return true;
    }
}

