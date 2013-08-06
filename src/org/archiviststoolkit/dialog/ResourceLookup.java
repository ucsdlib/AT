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
 * Created by JFormDesigner on Mon Mar 13 09:05:40 PST 2006
 */

package org.archiviststoolkit.dialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.editor.*;
import org.archiviststoolkit.swing.StandardEditor;
import org.archiviststoolkit.ApplicationFrame;
import org.hibernate.exception.ConstraintViolationException;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;

public class ResourceLookup extends JDialog {

	public static final String PARENT_EDITOR_TYPE_ACCESSIONS = "accessions";
	public static final String PARENT_EDITOR_TYPE_RESOURCES = "resources";
    public static final String PARENT_EDITOR_TYPE_ASSESSMENTS = "assessment";
    public static final String PARENT_EDITOR_TYPE_DIGITALOBJECT = "digitalobject";

    public ResourceLookup(Dialog owner, String parentEditorType) {
        super(owner);
        initComponents();
        this.parentEditorType = parentEditorType;
        initClassBased();
        this.getRootPane().setDefaultButton(this.select);
    }

	public ResourceLookup(Dialog owner, DomainEditorFields parentEditorFields) {
        super(owner);
		this.parentEditorFields = parentEditorFields;
        initComponents();
		if (parentEditorFields instanceof AccessionFields) {
			parentEditorType = PARENT_EDITOR_TYPE_ACCESSIONS;
		} else if (parentEditorFields instanceof ResourceTreeViewer) {
			parentEditorType = PARENT_EDITOR_TYPE_RESOURCES;
		} else if (parentEditorFields instanceof AssessmentsFields) {
            parentEditorType = PARENT_EDITOR_TYPE_ASSESSMENTS;
        }
		initClassBased();
		this.getRootPane().setDefaultButton(this.select);
	}

	private void initClassBased() {
		if (parentEditorType.equals(PARENT_EDITOR_TYPE_ACCESSIONS)) {
			StandardEditor.setMainHeaderColorAndTextByClass(Accessions.class, mainHeaderPanel, mainHeaderLabel);
			getSelect().setVisible(false);
		} else if (parentEditorType.equals(PARENT_EDITOR_TYPE_RESOURCES)) {
			StandardEditor.setMainHeaderColorAndTextByClass(Resources.class, mainHeaderPanel, mainHeaderLabel);
			getLinkButton().setVisible(false);
			getCreateButton().setVisible(false);
		} else if (parentEditorType.equals(PARENT_EDITOR_TYPE_ASSESSMENTS)) {
			StandardEditor.setMainHeaderColorAndTextByClass(Assessments.class, mainHeaderPanel, mainHeaderLabel);
			getCreateButton().setVisible(false);
            getSelect().setVisible(false);
        } else if (parentEditorType.equals(PARENT_EDITOR_TYPE_DIGITALOBJECT)) {
            StandardEditor.setMainHeaderColorAndTextByClass(DigitalObjects.class, mainHeaderPanel, mainHeaderLabel);
			getLinkButton().setVisible(false);
			getCreateButton().setVisible(false);
        }
	}

	private void lookupTableMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			if (parentEditorType.equals(PARENT_EDITOR_TYPE_ACCESSIONS) || parentEditorType.equals(PARENT_EDITOR_TYPE_ASSESSMENTS)) {
				linkResourceButtonActionPerformed();
			} else if (parentEditorType.equals(PARENT_EDITOR_TYPE_RESOURCES) || parentEditorType.equals(PARENT_EDITOR_TYPE_DIGITALOBJECT)) {
				setSelectedResourceAndExitWithOK();
			}
		}
	}

    private void selectActionPerformed() {
		if (getLookupTable().getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(this, "You must select a resource");
		} else {
			setSelectedResourceAndExitWithOK();
		}
	}


	private void setSelectedResourceAndExitWithOK() {
		int selectedRow = lookupTable.getSelectedRow();
		selectedResource = (Resources)lookupTableModel.getElementAt(selectedRow);
		status = JOptionPane.OK_OPTION;
		this.setVisible(false);
	}

	private void linkButtonActionPerformed() {
		if (getLookupTable().getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(this, "You must select a resource to link");
		} else {
			linkResourceButtonActionPerformed();
		}
    }

    private void createButtonActionPerformed(ActionEvent e) {
        if (parentEditorType.equals(PARENT_EDITOR_TYPE_ACCESSIONS)) {
            // todo see why this is not going through the factory
            DomainEditor dialog = new ResourceEditor(this);
            dialog.setIncludeSaveButton(true);
            dialog.setIncludeOkAndAnotherButton(false);
            dialog.setButtonListeners();
            dialog.setNewRecord(true);
            Accessions accessionsModel = (Accessions) parentEditorFields.getModel();
            Resources instance = new Resources();
            instance.markRecordAsNew();
            try {
                instance.populateResourceFromAccession(accessionsModel);
            } catch (DuplicateLinkException e1) {
                new ErrorDialog(this, "Error creating resource record", e1).showDialog();
            }
            dialog.setModel(instance, null);
            dialog.disableNavigationButtons();

            boolean done = false;
            while (!done) {
                int option = dialog.showDialog();
                int choice = dialog.getConfirmDialogReturn();
                Boolean savedNewRecord = dialog.getSavedNewRecord();

                if (option == JOptionPane.OK_OPTION) {
                    if (choice == JOptionPane.YES_OPTION) {
                        done = saveAndLinkResource(savedNewRecord, instance, accessionsModel);
                    } else if (choice == JOptionPane.NO_OPTION) {
                        //done = deleteResource(savedNewRecord, instance);
                        done = true;
                    } else { // do nothing just, redisplay window
                        done = false;
                    }

                } else if(choice == JOptionPane.YES_OPTION) { // called when window is closed and data validated
                    done = saveAndLinkResource(savedNewRecord, instance, accessionsModel);
                } else if (choice == JOptionPane.NO_OPTION) {
                    //done = deleteResource(savedNewRecord, instance);
                    done = true;
                } else if (choice == -1) { // called when window is closed and data did not validate even though YES was choosen for saving data
                    done = false;
                } else { // close window if nothing else
                    done = true;
                }
            }
        } else {
        }
	}

    // Method to save and link a resouce record to the accession
    private boolean saveAndLinkResource(Boolean savedNewRecord, Resources instance, Accessions accessionsModel) {
        boolean done = false;
        try {
            DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Resources.class);
            if (!savedNewRecord) {
                access.add(instance);
            } else {
                access.updateLongSession(instance);
            }

            DomainObject link = accessionsModel.addResource(instance);
            ((AccessionFields) parentEditorFields).getTableAccessionsResources().addDomainObject(link);
            Resources.addResourceToLookupList(instance);
            initLookup();
            filterField.setText("");
            done = true;

        } catch (ConstraintViolationException persistenceException) {
            JOptionPane.showMessageDialog(this, "Can't save, Duplicate record:" + instance);
            done = true;
        } catch (PersistenceException persistenceException) {
            if (persistenceException.getCause() instanceof ConstraintViolationException) {
                JOptionPane.showMessageDialog(this, "Can't save, Duplicate record:" + instance);
                done = true;
            }
            new ErrorDialog(this, "Error saving new record.", persistenceException).showDialog();
            done = true;
        } catch (DuplicateLinkException e1) {
            JOptionPane.showMessageDialog(this, e1.getMessage() + " is already linked to this record");
            done = true;
        }

        return done;
    }

    /* Method to delete a record that already saved
    private boolean deleteResource(Boolean savedNewRecord, Resources instance) {
        boolean done = false;
        try {
            if (savedNewRecord) { // already saved a new record so remove it
                DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Resources.class);
                access.deleteLongSession(instance);
            }
            done = true;
        } catch (PersistenceException persistenceException) {
            if (persistenceException.getCause() instanceof ConstraintViolationException) {
                JOptionPane.showMessageDialog(this, "Can't save, Duplicate record:" + instance);
                done = true;
            }
            new ErrorDialog(this, "Error saving new record.", persistenceException).showDialog();
            done = true;
        } catch (DeleteException deleteException) {
            new ErrorDialog(this, "Error deleting saved record.", deleteException).showDialog();
            done = true;
        }
        return done;
    } */

    private void doneButtonActionPerformed(ActionEvent e) {
		status = javax.swing.JOptionPane.CANCEL_OPTION;
		this.setVisible(false);
    }

    public DomainSortableTable getLookupTable() {
    	return lookupTable;
    }

    public JTextField getFilterField() {
    	return filterField;
    }

    public JButton getLinkButton() {
    	return linkButton;
    }

    public JButton getCreateButton() {
    	return createButton;
    }

    private void thisWindowClosing() {
		status = javax.swing.JOptionPane.CANCEL_OPTION;
		this.setVisible(false);
     }

     public JButton getSelect() {
     	return select;
     }

     private void lookupTableKeyTyped(KeyEvent e) {
     	if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			  selectActionPerformed();
		 }
     }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        HeaderPanel = new JPanel();
        mainHeaderPanel = new JPanel();
        mainHeaderLabel = new JLabel();
        panel3 = new JPanel();
        subHeaderLabel = new JLabel();
        contentPane = new JPanel();
        label3 = new JLabel();
        label4 = new JLabel();
        scrollPane1 = new JScrollPane();
        lookupTable = new DomainSortableTable(Resources.class, filterField);
        label1 = new JLabel();
        filterField = new JTextField();
        buttonBar = new JPanel();
        select = new JButton();
        linkButton = new JButton();
        createButton = new JButton();
        doneButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing();
            }
        });
        Container contentPane2 = getContentPane();
        contentPane2.setLayout(new BorderLayout());

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

                //======== mainHeaderPanel ========
                {
                    mainHeaderPanel.setBackground(new Color(80, 35, 45));
                    mainHeaderPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    mainHeaderPanel.setLayout(new FormLayout(
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
                    mainHeaderLabel.setText("Main Header");
                    mainHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
                    mainHeaderLabel.setForeground(Color.white);
                    mainHeaderPanel.add(mainHeaderLabel, cc.xy(2, 2));
                }
                HeaderPanel.add(mainHeaderPanel, cc.xy(1, 1));

                //======== panel3 ========
                {
                    panel3.setBackground(new Color(66, 60, 111));
                    panel3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel3.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.RELATED_GAP_COLSPEC,
                            new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                            FormFactory.UNRELATED_GAP_COLSPEC
                        },
                        new RowSpec[] {
                            FormFactory.RELATED_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.RELATED_GAP_ROWSPEC
                        }));

                    //---- subHeaderLabel ----
                    subHeaderLabel.setText("Lookup Resource");
                    subHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
                    subHeaderLabel.setForeground(Color.white);
                    panel3.add(subHeaderLabel, cc.xy(2, 2));
                }
                HeaderPanel.add(panel3, cc.xy(2, 1));
            }
            dialogPane.add(HeaderPanel, BorderLayout.NORTH);

            //======== contentPane ========
            {
                contentPane.setBackground(new Color(231, 188, 251));
                contentPane.setOpaque(false);
                contentPane.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        FormFactory.UNRELATED_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.UNRELATED_GAP_COLSPEC
                    },
                    new RowSpec[] {
                        FormFactory.UNRELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.UNRELATED_GAP_ROWSPEC
                    }));

                //---- label3 ----
                label3.setText("Double click on a Resource to select it.");
                contentPane.add(label3, cc.xywh(2, 2, 3, 1));

                //---- label4 ----
                label4.setText("Or hit enter if a Resource is highlighted.");
                contentPane.add(label4, cc.xywh(2, 4, 3, 1));

                //======== scrollPane1 ========
                {
                    scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                    //---- lookupTable ----
                    lookupTable.setPreferredScrollableViewportSize(new Dimension(450, 300));
                    lookupTable.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            lookupTableMouseClicked(e);
                        }
                    });
                    lookupTable.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            lookupTableKeyTyped(e);
                        }
                    });
                    scrollPane1.setViewportView(lookupTable);
                }
                contentPane.add(scrollPane1, cc.xywh(2, 6, 3, 1));

                //---- label1 ----
                label1.setText("Filter:");
                contentPane.add(label1, cc.xy(2, 8));
                contentPane.add(filterField, cc.xy(4, 8));

                //======== buttonBar ========
                {
                    buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
                    buttonBar.setBackground(new Color(231, 188, 251));
                    buttonBar.setOpaque(false);
                    buttonBar.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.BUTTON_COLSPEC,
                            FormFactory.RELATED_GAP_COLSPEC,
                            FormFactory.BUTTON_COLSPEC
                        },
                        RowSpec.decodeSpecs("pref")));

                    //---- select ----
                    select.setText("Select");
                    select.setOpaque(false);
                    select.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            selectActionPerformed();
                        }
                    });
                    buttonBar.add(select, cc.xy(1, 1));

                    //---- linkButton ----
                    linkButton.setText("Link");
                    linkButton.setOpaque(false);
                    linkButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            linkButtonActionPerformed();
                        }
                    });
                    buttonBar.add(linkButton, cc.xy(3, 1));

                    //---- createButton ----
                    createButton.setText("Create Resource");
                    createButton.setOpaque(false);
                    createButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            createButtonActionPerformed(e);
                        }
                    });
                    buttonBar.add(createButton, cc.xy(5, 1));

                    //---- doneButton ----
                    doneButton.setText("Close Window");
                    doneButton.setOpaque(false);
                    doneButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            doneButtonActionPerformed(e);
                        }
                    });
                    buttonBar.add(doneButton, cc.xy(7, 1));
                }
                contentPane.add(buttonBar, cc.xywh(2, 10, 3, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT));
            }
            dialogPane.add(contentPane, BorderLayout.CENTER);
        }
        contentPane2.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

	private void initLookup() {
		SortedList sortedItems = Resources.getResourcesGlazedList();
		textFilteredIssues = new FilterList(sortedItems, new TextComponentMatcherEditor(filterField, new DomainFilterator()));
		lookupTableModel = new EventTableModel(textFilteredIssues, new DomainTableFormat(Resources.class));
		lookupTable.setModel(lookupTableModel);
		TableComparatorChooser tableSorter = new TableComparatorChooser(lookupTable, sortedItems, true);
		filterField.requestFocusInWindow();
	}

    private void linkResourceButtonActionPerformed() {
        int[] selectedRows = lookupTable.getSelectedRows();
        for(int i = 0; i < selectedRows.length; i++) {
            int selectedRow = selectedRows[i];
            Resources resource = (Resources) lookupTableModel.getElementAt(selectedRow);
            try {
                DomainObject link;
                if (parentEditorType.equals(PARENT_EDITOR_TYPE_ASSESSMENTS)) {
                    Assessments assessmentsModel = (Assessments) parentEditorFields.getModel();
                    link = assessmentsModel.addResource(resource);
                } else {
                    Accessions accessionsModel = (Accessions) parentEditorFields.getModel();
                    link = accessionsModel.addResource(resource);
                }

                if (link != null) {
                    if (parentEditorType.equals(PARENT_EDITOR_TYPE_ASSESSMENTS)) {
                        ((AssessmentsFields) parentEditorFields).getTableAssessmentsResources().addDomainObject(link);
                    } else { // This must be an accession editor
                        ((AccessionFields) parentEditorFields).getTableAccessionsResources().addDomainObject(link);
                    }
                }
                //set the record to dirty
                ApplicationFrame.getInstance().setRecordDirty();
            } catch (DuplicateLinkException e) {
                JOptionPane.showMessageDialog(this, e.getMessage() + " is already linked to this record");
            }
        }
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel HeaderPanel;
    private JPanel mainHeaderPanel;
    private JLabel mainHeaderLabel;
    private JPanel panel3;
    private JLabel subHeaderLabel;
    private JPanel contentPane;
    private JLabel label3;
    private JLabel label4;
    private JScrollPane scrollPane1;
    private DomainSortableTable lookupTable;
    private JLabel label1;
    private JTextField filterField;
    private JPanel buttonBar;
    private JButton select;
    private JButton linkButton;
    private JButton createButton;
    private JButton doneButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    protected int status = 0;
	FilterList textFilteredIssues;
	EventTableModel lookupTableModel;
    private Resources selectedResource;
	private DomainEditorFields parentEditorFields;
	private String parentEditorType;

	public final int showDialog(Component c) {

        this.pack();
		initLookup();
		this.setLocationRelativeTo(this.getOwner());
		this.setVisible(true);

        return (status);
    }

    public Resources getSelectedResource() {
        return selectedResource;
    }

    public void setSelectedResource(Resources selectedResource) {
        this.selectedResource = selectedResource;
    }
}
