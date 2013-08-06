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
 * Created by JFormDesigner on Wed Apr 19 14:46:37 EDT 2006
 */

package org.archiviststoolkit.dialog;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.sql.SQLException;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.swing.*;
import org.archiviststoolkit.report.ReportUtils;
import org.archiviststoolkit.report.ReportDestinationProperties;
import org.archiviststoolkit.exceptions.*;
import org.archiviststoolkit.Main;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.util.StringHelper;
import org.hibernate.exception.ConstraintViolationException;
import org.apache.log4j.Logger;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;

public class AssessmentManagement extends GeneralAdminDialog implements ActionListener {

	private static Logger logger = Logger.getLogger(Main.class.getPackage().getName());
    private DomainObject currentSearchResultObject;
    private QueryEditor searchDialog;
	public AssessmentManagement(Frame owner) throws DomainEditorCreationException {
		super(owner, false);
		initComponents();
		setClazz(Assessments.class);
		initLookup();

        // initialize the search dialog
        searchDialog = new QueryEditor(this, Assessments.class);
	}

	public AssessmentManagement(Dialog owner) throws DomainEditorCreationException {
		super(owner, false);
		initComponents();
		setClazz(Assessments.class);
		initLookup();
	}

	// TODO 5/5/2009 This method might have to be modified based on usability issue
    protected void removeRecordButtonActionPerformed(ActionEvent e) {
		int response = JOptionPane.showConfirmDialog(this,
				"Are you sure you want to delete the following assessment record(s)",
				"Delete records", JOptionPane.YES_NO_OPTION);

		if (response == JOptionPane.OK_OPTION) {
			int selectedRow = getContentTable().getSelectedRow();

			if (selectedRow != -1) {
				// get a list of AssessmentSearch Results to delete
                ArrayList<DomainObject> deleteList = new ArrayList<DomainObject>();
				for (int index : getContentTable().getSelectedRows()) {
					if (getContentTable().getFilteredList()!= null) {
						deleteList.add(getContentTable().getFilteredList().get(index));
					} else {
						deleteList.add(getContentTable().getSortedList().get(index));
					}
				}

                // Now get a list of assessment records to deleted based on if they no longer
                // have any linked records other
                final HashSet<Assessments> assessmentDeleteList = new HashSet<Assessments>();

                for (DomainObject object: deleteList) {
                    AssessmentsSearchResult searchResult = (AssessmentsSearchResult)object;
                    Assessments assessment = searchResult.getAssessment();
                    assessmentDeleteList.add(assessment);
                }

                // run the deletion in a thread to enable using a progress monitor
                Thread performer = new Thread(new Runnable() {
                    public void run() {
                        String deleteProblems = "";

                        DomainAccessObject access = null;
                        try {
                            access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);

                            // open log session since it may have been closed
                            access.getLongSession();
                        } catch (PersistenceException e1) {
                            new ErrorDialog("Error deleting records", StringHelper.getStackTrace(e1)).showDialog();
                        }

                        // get the progress monitor which allows for canceling
                        InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(getThisAsJDialog(), 1000, true);
                        monitor.start("Deleting Records...");

                        int count = 1; // keep track of the number of record(s) deleted
                        int size = assessmentDeleteList.size();
                        for (DomainObject domainObject : assessmentDeleteList) {
                            try {
                                access.deleteLongSession(domainObject, false);
                                //getContentTable().getEventList().remove(domainObject);
                                
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

	public JTextField getFilterField() {
		return filterField;
	}

    /**
     * Method to print jasper report of assessments records
     *
     * @param progressPanel The progress panel
     * @param printScreenReport Is this report a print screen report
     * @return The list of records to be printing
     */
    private ArrayList<DomainObject> getResultSetForPrinting(InfiniteProgressPanel progressPanel, boolean printScreenReport) {
		ArrayList<DomainObject> listForPrinting = new ArrayList<DomainObject>();
		DomainAccessObject access = null;
		try {
			access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);
			int[] selectedRows = getContentTable().getSelectedRows();

            if(printScreenReport) { // just get all records listed for doing print screen
                for (DomainObject domainObject : textFilteredIssues) {
                    listForPrinting.add(domainObject);

                    // check to see if cancel wasn't press. If it was then return null
                    if(progressPanel.isProcessCancelled()) {
                        return null;
                    }
				}
            } else if (selectedRows.length == 0) {
				int count = 1;
				for (DomainObject domainObject : textFilteredIssues) {
					Assessments assessment = (Assessments)access.findByPrimaryKeyLongSession(domainObject.getIdentifier());

                    if (!listForPrinting.contains(assessment) && !assessment.getInactive()) {
                        listForPrinting.add(assessment);
                        progressPanel.setTextLine("Loading record number " + count++ + " for printing", 1);
                    }

                    // check to see if cancel wasn't press. If it was then return null
                    if(progressPanel.isProcessCancelled()) {
                        return null;
                    }
				}
			} else {
				int count = 1;
				for (int selectedRow : selectedRows) {
					Assessments assessment = (Assessments)access.findByPrimaryKeyLongSession(getContentTable().getFilteredList().get(selectedRow).getIdentifier());

                    if(!listForPrinting.contains(assessment) && !assessment.getInactive()) {
                        listForPrinting.add(assessment);
                        progressPanel.setTextLine("Loading record number " + count++ + " for printing", 1);
                    }

                    // check to see if cancel wasn't press in that case return null;
                    if(progressPanel.isProcessCancelled()) {
                        return null;
                    }
				}
			}
		} catch (PersistenceException e) {
			new ErrorDialog("Error gathering records for printing", e).showDialog();
		} catch (LookupException e) {
			new ErrorDialog("Error gathering records for printing", e).showDialog();
		}

		return listForPrinting;
	}

	private void reportButtonActionPerformed(ActionEvent e) {
		ReportWorkerRunnable runnable = new ReportWorkerRunnable(this);
		Thread backgroundWorker = new Thread(runnable);
		backgroundWorker.start();
	}

    /**
     * Performs searching on assessment records
     */
    private void searchActionPerformed() {
        final DomainAccessObject access = new DomainAccessObjectImpl(clazz);

        if (searchDialog.showDialog() == javax.swing.JOptionPane.OK_OPTION) {
            Thread performer = new Thread(new Runnable() {
                public void run() {
                    InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(getThisAsJDialog(), 0);
                    monitor.start("Performing search...");
                    try {
                        Collection results = access.findByQueryEditorLongSession(searchDialog, monitor);
                        updateListWithNewResultSet(monitor, results);
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
    }

    /**
     * Update the table with results from a search. This is done in the EDT thread to
     * avoid a potential freezing problem.
     * @param resultSet The new result to update the table with
     */

    protected synchronized void updateListWithNewResultSet(InfiniteProgressPanel monitor, Collection resultSet) {
        // create assessment search objects now
        final Collection newResultSet = getAssessmentsResultSet(monitor, resultSet, true);

        // update the content table with any results that were found
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					contentTable.updateCollection(newResultSet);
				}
			});
		}
	}

    /**
     * Method to refresh the records displayed in the contentTable
     */
    private void refreshActionPerformed() {
        Thread performer = new Thread(new Runnable() {
                public void run() {

                findAll();

                }
        });
        performer.start();
    }

    protected void findAll() {
        // disable the list all button
        button2.setEnabled(false);

        // get the progress monitor which allows for canceling
        InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(getThisAsJDialog(), 1000, true);
        monitor.start("Loading Assessment Records ...");

        DomainAccessObject access = null;
        try {
            access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);

        } catch (PersistenceException e) {
            monitor.close();
            new ErrorDialog("Error finding all records", e).showDialog();
            return;
        }

        Collection resultSet = null;

        try {
            if (sortField == null) {
                resultSet = access.findAllLongSession();
            } else {
                resultSet = access.findAllLongSession(sortField);
            }
        } catch (LookupException e) {
            monitor.close();
            new ErrorDialog("Error finding all records", e).showDialog();
            return;
        }

        // if the class is an Assessment class the we need to create the results now
        if(!resultSet.isEmpty()) {
            getContentTable().updateCollection(getAssessmentsResultSet(monitor, resultSet, false));
        }

        // re-enable the "List All" button and close the monitor
        monitor.close();
        button2.setEnabled(true);
	}

    /**
     * Method to return the result for Assessment records which contains
     * AssessmentsSearchResult object instead of Assessments.
     * @param resultSet The collection containing Assessments objects
     * @param removeInActive boolean specifying whether to allow inatice records to be displayed in table
     * @return Collection containing AssessmentsSearchResult
     */
    protected Collection getAssessmentsResultSet(InfiniteProgressPanel monitor, Collection resultSet, boolean removeInActive) {
        int count = 0;
        int size = resultSet.size();

        Collection<AssessmentsSearchResult> newResultSet = new ArrayList<AssessmentsSearchResult>();

        // interate through the results set
        for (Iterator resultIterator = resultSet.iterator(); resultIterator.hasNext();) {
            // check to see if the cancel button was pressed
            if (monitor != null && monitor.isProcessCancelled()) {
                break;
            }

            count++;

            Assessments assessments = (Assessments) resultIterator.next();

            // check if to allow this assessment in the result set
            if (removeInActive && assessments.getInactive()) {
                continue;
            }

            boolean hasLinkRecord = false; // keeps track if the assessment has any records link to it

            // add any linked resources now
            if (!assessments.getResources().isEmpty()) {
                Set<AssessmentsResources> assessmentsResources = assessments.getResources();

                for (AssessmentsResources assessmentResource : assessmentsResources) {
                    newResultSet.add(new AssessmentsSearchResult(assessments, assessmentResource.getResource()));
                }
                hasLinkRecord = true;
            }

            // add any linked accessions now
            if (!assessments.getAccessions().isEmpty()) {
                Set<AssessmentsAccessions> assessmentsAccessions = assessments.getAccessions();

                for (AssessmentsAccessions assessmentsAccession : assessmentsAccessions) {
                    newResultSet.add(new AssessmentsSearchResult(assessments, assessmentsAccession.getAccession()));
                }
                hasLinkRecord = true;
            }

            // add any link digital objects now
            if (!assessments.getDigitalObjects().isEmpty()) {
                Set<AssessmentsDigitalObjects> assessmentsDigitalObjectses = assessments.getDigitalObjects();

                for (AssessmentsDigitalObjects assessmentsDigitalObject : assessmentsDigitalObjectses) {
                    newResultSet.add(new AssessmentsSearchResult(assessments, assessmentsDigitalObject.getDigitalObject()));
                }
                hasLinkRecord = true;
            }

            // if this assessment has no link records the just return assessment.
            // such records with no link records should never exist, and this is
            // only for testing and spoting bad records
            if (!hasLinkRecord) {
                newResultSet.add(new AssessmentsSearchResult(assessments));
            }

            // update the progress monitor
            if(monitor != null) {
                monitor.setTextLine(count + " of " + size + " record(s) loaded", 2);
            }
        }

        return newResultSet;
    }

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        HeaderPanel = new JPanel();
        panel2 = new JPanel();
        mainHeaderLabel = new JLabel();
        panel3 = new JPanel();
        subHeaderLabel = new JLabel();
        contentPanel = new JPanel();
        panel1 = new JPanel();
        label2 = new JLabel();
        filterField = new JTextField();
        scrollPane1 = new JScrollPane();
        contentTable = new DomainSortableTable(AssessmentsSearchResult.class, filterField);
        buttonBar = new JPanel();
        button1 = new JButton();
        button2 = new JButton();
        reportsButton = new JButton();
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
            dialogPane.setBorder(null);
            dialogPane.setBackground(new Color(200, 205, 232));
            dialogPane.setLayout(new BorderLayout());

            //======== HeaderPanel ========
            {
                HeaderPanel.setBackground(new Color(80, 69, 57));
                HeaderPanel.setOpaque(false);
                HeaderPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                HeaderPanel.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        new ColumnSpec(Sizes.bounded(Sizes.MINIMUM, Sizes.dluX(100), Sizes.dluX(200))),
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                    },
                    RowSpec.decodeSpecs("default")));

                //======== panel2 ========
                {
                    panel2.setBackground(new Color(80, 69, 57));
                    panel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel2.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.RELATED_GAP_COLSPEC,
                            new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        },
                        new RowSpec[] {
                            FormFactory.RELATED_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.RELATED_GAP_ROWSPEC
                        }));

                    //---- mainHeaderLabel ----
                    mainHeaderLabel.setText("Administration");
                    mainHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
                    mainHeaderLabel.setForeground(Color.white);
                    panel2.add(mainHeaderLabel, cc.xy(2, 2));
                }
                HeaderPanel.add(panel2, cc.xy(1, 1));

                //======== panel3 ========
                {
                    panel3.setBackground(new Color(66, 60, 111));
                    panel3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel3.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.RELATED_GAP_COLSPEC,
                            new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        },
                        new RowSpec[] {
                            FormFactory.RELATED_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.RELATED_GAP_ROWSPEC
                        }));

                    //---- subHeaderLabel ----
                    subHeaderLabel.setText("Assessment");
                    subHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
                    subHeaderLabel.setForeground(Color.white);
                    panel3.add(subHeaderLabel, cc.xy(2, 2));
                }
                HeaderPanel.add(panel3, cc.xy(2, 1));
            }
            dialogPane.add(HeaderPanel, BorderLayout.NORTH);

            //======== contentPanel ========
            {
                contentPanel.setOpaque(false);
                contentPanel.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        FormFactory.RELATED_GAP_COLSPEC,
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.RELATED_GAP_COLSPEC
                    },
                    new RowSpec[] {
                        FormFactory.UNRELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.UNRELATED_GAP_ROWSPEC
                    }));

                //======== panel1 ========
                {
                    panel1.setOpaque(false);
                    panel1.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        },
                        RowSpec.decodeSpecs("default")));

                    //---- label2 ----
                    label2.setText("Filter:");
                    panel1.add(label2, cc.xy(1, 1));
                    panel1.add(filterField, cc.xy(3, 1));
                }
                contentPanel.add(panel1, cc.xy(2, 2));

                //======== scrollPane1 ========
                {

                    //---- contentTable ----
                    contentTable.setPreferredScrollableViewportSize(new Dimension(800, 400));
                    contentTable.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            contentTableMouseClicked(e);
                        }
                    });
                    scrollPane1.setViewportView(contentTable);
                }
                contentPanel.add(scrollPane1, cc.xy(2, 4));

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
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.GLUE_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.BUTTON_COLSPEC
                        },
                        RowSpec.decodeSpecs("pref")));

                    //---- button1 ----
                    button1.setText("Search");
                    button1.setOpaque(false);
                    button1.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            searchActionPerformed();
                        }
                    });
                    buttonBar.add(button1, cc.xy(2, 1));

                    //---- button2 ----
                    button2.setText("List All");
                    button2.setSelectedIcon(null);
                    button2.setOpaque(false);
                    button2.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            refreshActionPerformed();
                        }
                    });
                    buttonBar.add(button2, cc.xy(4, 1));

                    //---- reportsButton ----
                    reportsButton.setText("Reports");
                    reportsButton.setOpaque(false);
                    reportsButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            reportButtonActionPerformed(e);
                        }
                    });
                    buttonBar.add(reportsButton, cc.xy(6, 1));

                    //---- addRecordButton ----
                    addRecordButton.setText("Add Record");
                    addRecordButton.setOpaque(false);
                    addRecordButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            addRecordButtonActionPerformed(e);
                        }
                    });
                    buttonBar.add(addRecordButton, cc.xy(8, 1));

                    //---- removeRecordButton ----
                    removeRecordButton.setText("Remove Record");
                    removeRecordButton.setOpaque(false);
                    removeRecordButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            removeRecordButtonActionPerformed(e);
                        }
                    });
                    buttonBar.add(removeRecordButton, cc.xy(10, 1));

                    //---- doneButton ----
                    doneButton.setText("Done");
                    doneButton.setOpaque(false);
                    doneButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            doneButtonActionPerformed(e);
                        }
                    });
                    buttonBar.add(doneButton, cc.xy(14, 1));
                }
                contentPanel.add(buttonBar, cc.xy(2, 6));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	protected void contentTableMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			int selectedRow = getContentTable().getSelectedRow();
			dialog.setSelectedRow(selectedRow);

			if (selectedRow != -1) {
				try {
					access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);

                    currentSearchResultObject = textFilteredIssues.get(selectedRow);
					currentDomainObject = (DomainObject) access.findByPrimaryKeyLongSession(currentSearchResultObject.getIdentifier());
					currentObjectSublist = textFilteredIssues.subList(selectedRow, selectedRow + 1);
					dialog.setModel(currentDomainObject, null);
                    dialog.setNavigationButtons();
                    dialog.setIncludeSaveButton(true);
                    dialog.clearRecordPositionText(); // printing record position in this dialog makes no sense

                    int status = dialog.showDialog();

					if (status == JOptionPane.OK_OPTION) {
						access.updateLongSession(currentDomainObject);
						currentObjectSublist.set(0, currentSearchResultObject);
                        // refresh the main display
                        findAll();
					} else {
						try {
							access.closeLongSessionRollback();
						} catch (SQLException e1) {
							new ErrorDialog(dialog, "Error canceling record.", e1).showDialog();
						}
					}
				} catch (PersistenceException persistenceException) {
					if (persistenceException.getCause() instanceof ConstraintViolationException) {
						String message = "Can't save, duplicate record" + currentDomainObject;
						JOptionPane.showMessageDialog(this, message);
						logger.error(message, persistenceException);
					} else {
						new ErrorDialog(this, "Error saving new record", persistenceException).showDialog();
					}
					try {
						access.closeLongSessionRollback();
					} catch (SQLException sqlex) {
						new ErrorDialog(this, "Error canceling record.", sqlex).showDialog();
					}
				} catch (ConstraintViolationException persistenceException) {
					String message = "Can't save, duplicate record" + currentDomainObject;
					JOptionPane.showMessageDialog(this, message);
					logger.error(message, persistenceException);
					try {
						access.closeLongSessionRollback();
					} catch (SQLException e1) {
						new ErrorDialog(this, "Error canceling record.", e1).showDialog();
					}
				} catch (LookupException lookupException) {
					new ErrorDialog(this, "Error saving new record", lookupException).showDialog();
					try {
						access.closeLongSessionRollback();
					} catch (SQLException sqlex) {
						new ErrorDialog(this, "Error canceling record.", sqlex).showDialog();
					}
				}

			}
		}
	}

	protected void addRecordButtonActionPerformed(ActionEvent e) {
		super.addRecordButtonActionPerformed(e);
	}

	protected void doneButtonActionPerformed(ActionEvent e) {
		super.doneButtonActionPerformed(e);
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel HeaderPanel;
    private JPanel panel2;
    private JLabel mainHeaderLabel;
    private JPanel panel3;
    private JLabel subHeaderLabel;
    private JPanel contentPanel;
    private JPanel panel1;
    private JLabel label2;
    private JTextField filterField;
    private JScrollPane scrollPane1;
    private DomainSortableTable contentTable;
    private JPanel buttonBar;
    private JButton button1;
    private JButton button2;
    private JButton reportsButton;
    private JButton addRecordButton;
    private JButton removeRecordButton;
    private JButton doneButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	FilterList<DomainObject> textFilteredIssues;
	EventTableModel lookupTableModel;

	public DomainSortableTable getContentTable() {
		return contentTable;
	}

	public void setContentTable(DomainSortableTable contentTable) {
		this.contentTable = contentTable;
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
			if (dialog.getModel().validateAndDisplayDialog(e)) {
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
				dialog.setNavigationButtons();

				try {
//					int index = getTableModel().getIndex(currentDomainObject);
//					getTableModel().setDomainObject(index, currentDomainObject);
					access.updateLongSession(currentDomainObject);
					currentObjectSublist.set(0, currentSearchResultObject);
					int selectedRow = dialog.getSelectedRow();

                    currentSearchResultObject = textFilteredIssues.get(selectedRow);
					currentDomainObject = access.findByPrimaryKeyLongSession(currentSearchResultObject.getIdentifier());
					textFilteredIssues.set(selectedRow, currentSearchResultObject);
					currentObjectSublist = textFilteredIssues.subList(selectedRow, selectedRow + 1);
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


	private void initLookup() {
		SortedList sortedLocations = new SortedList(contentTable.getEventList());
		textFilteredIssues = new FilterList<DomainObject>(sortedLocations, new TextComponentMatcherEditor(filterField, new DomainFilterator()));
		lookupTableModel = new EventTableModel(textFilteredIssues, new DomainTableFormat(AssessmentsSearchResult.class));
		contentTable.setModel(lookupTableModel);
		TableComparatorChooser tableSorter = new TableComparatorChooser(contentTable, sortedLocations, true);
		filterField.requestFocusInWindow();
	}

  // dummy method to set the dialog title this prevents errors from being thrown
  public void setDialogTitle(String title) { }

  class ReportWorkerRunnable implements Runnable {
		private Dialog parent;

		ReportWorkerRunnable(Dialog parent) {
			this.parent = parent;
		}

		public void run() {
            
			try {
				ReportDestinationProperties reportDestinationProperties = new ReportDestinationProperties(clazz, parent, true);

                if (reportDestinationProperties.showDialog(textFilteredIssues.size())) {
					InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(parent, 1000, true);
					try {
                        // decide on what records to return based whether we are printing the screen
                        String reportTitle = reportDestinationProperties.getSelectedReport().getReportTitle();
                        ArrayList setForPrinting;

                        if(reportTitle.equals("Print Screen")) {
                            ReportDestinationProperties reportDestinationProperties2 = new ReportDestinationProperties(AssessmentsSearchResult.class, parent, true);
                            reportDestinationProperties2.setReportDesitation(reportDestinationProperties.getReportDesitation());
                            reportDestinationProperties2.reportSaveDestination = reportDestinationProperties.reportSaveDestination;
                            reportDestinationProperties = reportDestinationProperties2;

                            setForPrinting = getResultSetForPrinting(monitor, true); // Get records for print screen
                        } else {
                            setForPrinting = getResultSetForPrinting(monitor, false); // Just get assessment records
                        }

                        if(setForPrinting != null) {
                            ReportUtils.printReport(reportDestinationProperties, setForPrinting, monitor);
                        }
					} catch (UnsupportedParentComponentException e) {
						monitor.close();
						new ErrorDialog("There was a problem printing the report", e).showDialog();
					} finally {
						// to ensure that progress dlg is closed in case of any exception
						monitor.close();
					}
				}
			} catch (UnsupportedClassException e) {
				new ErrorDialog("There was a problem printing the report", e).showDialog();
			} catch (UnsupportedReportDestination unsupportedReportDestination) {
				new ErrorDialog("There was a problem printing the report", unsupportedReportDestination).showDialog();
			} catch (UnsupportedReportType unsupportedReportType) {
				new ErrorDialog("There was a problem printing the report", unsupportedReportType).showDialog();
			}
		}
	}

}