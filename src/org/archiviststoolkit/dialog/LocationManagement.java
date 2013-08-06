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
 * Created by JFormDesigner on Wed Apr 19 14:46:37 EDT 2006
 */

package org.archiviststoolkit.dialog;

import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.ArrayList;
import java.sql.SQLException;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.model.Locations;
import org.archiviststoolkit.swing.*;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.report.ReportFactory;
import org.archiviststoolkit.report.ReportUtils;
import org.archiviststoolkit.report.ReportDestinationProperties;
import org.archiviststoolkit.exceptions.*;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.Main;
import org.hibernate.exception.ConstraintViolationException;
import org.apache.log4j.Logger;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.JRRuntimeException;

public class LocationManagement extends GeneralAdminDialog implements ActionListener {

	private static Logger logger = Logger.getLogger(Main.class.getPackage().getName());

	public LocationManagement(Frame owner) throws DomainEditorCreationException {
		super(owner, false);
		initComponents();
		setClazz(Locations.class);
		initLookup();
	}

	public LocationManagement(Dialog owner) throws DomainEditorCreationException {
		super(owner, false);
		initComponents();
		setClazz(Locations.class);
		initLookup();
	}

	protected void removeRecordButtonActionPerformed(ActionEvent e) {
		super.removeRecordButtonActionPerformed(e);
	}

	public JTextField getFilterField() {
		return filterField;
	}

    //todo move to reports util
    private ArrayList<DomainObject> getResultSetForPrinting(InfiniteProgressPanel progressPanel) {
		ArrayList<DomainObject> listForPrinting = new ArrayList<DomainObject>();
		DomainAccessObject access = null;
		try {
			access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);
			int[] selectedRows = getContentTable().getSelectedRows();
			if (selectedRows.length == 0) {
				int recordsToLoad = textFilteredIssues.size();
				int count = 1;
				for (DomainObject domainObject : textFilteredIssues) {
					listForPrinting.add(access.findByPrimaryKeyLongSession(domainObject.getIdentifier()));
					progressPanel.setTextLine("Loading records for printing " + count++ + " of " + recordsToLoad, 1);
				}
			} else {
				int recordsToLoad = selectedRows.length;
				int count = 1;
				for (int selectedRow : selectedRows) {
					listForPrinting.add(access.findByPrimaryKeyLongSession(getContentTable().getFilteredList().get(selectedRow).getIdentifier()));
					progressPanel.setTextLine("Loading records for printing " + count++ + " of " + recordsToLoad, 1);
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
        contentTable = new DomainSortableTable(Locations.class, filterField);
        buttonBar = new JPanel();
        reportsButton = new JButton();
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
                    subHeaderLabel.setText("Location Management");
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
                            FormFactory.UNRELATED_GAP_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.BUTTON_COLSPEC
                        },
                        RowSpec.decodeSpecs("pref")));

                    //---- reportsButton ----
                    reportsButton.setText("Reports");
                    reportsButton.setOpaque(false);
                    reportsButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            reportButtonActionPerformed(e);
                        }
                    });
                    buttonBar.add(reportsButton, cc.xy(2, 1));

                    //---- batchAddButton ----
                    batchAddButton.setText("Batch Add");
                    batchAddButton.setOpaque(false);
                    batchAddButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            batchAddButtonActionPerformed(e);
                        }
                    });
                    buttonBar.add(batchAddButton, cc.xy(6, 1));

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
		//todo this is really kludgy. Look at the way it works for the domainTableWorksurface
		if (e.getClickCount() == 2) {
			int selectedRow = getContentTable().getSelectedRow();
			dialog.setSelectedRow(selectedRow);

			if (selectedRow != -1) {
				try {
					access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);

					currentDomainObject = (DomainObject) access.findByPrimaryKeyLongSession(textFilteredIssues.get(selectedRow).getIdentifier());
					currentObjectSublist = textFilteredIssues.subList(selectedRow, selectedRow + 1);
					dialog.setModel(currentDomainObject, null);
					dialog.setNavigationButtons();
					int status = dialog.showDialog();

					if (status == javax.swing.JOptionPane.OK_OPTION) {
						access.updateLongSession(currentDomainObject);
						currentObjectSublist.set(0, currentDomainObject);
					} else {
						try {
							access.closeLongSessionRollback();
						} catch (SQLException e1) {
							new ErrorDialog(dialog, "Error canceling record.", e1).showDialog();
						}
					}
				//todo this is common code in many places we need to get it in one place
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

	private void batchAddButtonActionPerformed(ActionEvent e) {
		BatchLocationCreation batchCreate = new BatchLocationCreation(this);
		batchCreate.showDialog();
		findAll();
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
    private JButton reportsButton;
    private JButton batchAddButton;
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
					currentObjectSublist.set(0, currentDomainObject);
					int selectedRow = dialog.getSelectedRow();
					currentDomainObject = access.findByPrimaryKeyLongSession(textFilteredIssues.get(selectedRow).getIdentifier());
					textFilteredIssues.set(selectedRow, currentDomainObject);
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
		lookupTableModel = new EventTableModel(textFilteredIssues, new DomainTableFormat(Locations.class));
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
				final ReportDestinationProperties reportDestinationProperties = new ReportDestinationProperties(clazz, parent);
				if (reportDestinationProperties.showDialog(textFilteredIssues.size())) {
					InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(parent, 1000);
					try {
						ReportUtils.printReport(reportDestinationProperties, getResultSetForPrinting(monitor),  monitor);
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
