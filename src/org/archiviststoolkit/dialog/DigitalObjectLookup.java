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
import java.util.ArrayList;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.editor.*;
import org.archiviststoolkit.swing.StandardEditor;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.util.StringHelper;
import org.hibernate.LockMode;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;

public class DigitalObjectLookup extends JDialog {

    public static final String PARENT_EDITOR_TYPE_ASSESSMENTS = "assessment";
    public static final String PARENT_EDITOR_TYPE_RESOURCES = "resources";

    // list to store the digital object returned from the database
    private EventList eventList = null;

    // Method to set the parent resource when link digital objects to resources
    private Resources parentResource = null;

	public DigitalObjectLookup(Dialog owner, DomainEditorFields parentEditorFields) {
        super(owner);
		this.parentEditorFields = parentEditorFields;
        initComponents();

        if (parentEditorFields instanceof AssessmentsFields) {
            parentEditorType = PARENT_EDITOR_TYPE_ASSESSMENTS;
        } else if(parentEditorFields instanceof ArchDescriptionFields) {
            parentEditorType = PARENT_EDITOR_TYPE_RESOURCES;
        }

        initClassBased();

        // set the columns that are display in the lookup table. This doesn't seem
        // to work if the domain object already defines it's own return screen order
        lookupTable.setClazzAndColumns(DigitalObjects.PROPERTYNAME_TITLE,
				DigitalObjects.class,
                DigitalObjects.PROPERTYNAME_TITLE,
				DigitalObjects.PROPERTYNAME_OBJECT_TYPE,
				DigitalObjects.PROPERTYNAME_METS_IDENTIFIER);

		// set the default button that is selected
        this.getRootPane().setDefaultButton(this.linkButton);
	}

    // Method to get the parent resource
    public Resources getParentResource() {
        return parentResource;
    }

    /**
     * Method to set the parent resource. Only used when linking Digital Object
     * to a Resource
     * @param parentResource The parent resource digital objects are linked to
     */
    public void setParentResource(Resources parentResource) {
        this.parentResource = parentResource;
    }

    private void initClassBased() {
		if (parentEditorType.equals(PARENT_EDITOR_TYPE_ASSESSMENTS)) {
			StandardEditor.setMainHeaderColorAndTextByClass(Assessments.class, mainHeaderPanel, mainHeaderLabel);
        } else if(parentEditorType.equals(PARENT_EDITOR_TYPE_RESOURCES)) {
            StandardEditor.setMainHeaderColorAndTextByClass(Resources.class, mainHeaderPanel, mainHeaderLabel);
        }
	}

	private void lookupTableMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			if (parentEditorType.equals(PARENT_EDITOR_TYPE_ASSESSMENTS)) {
				linkDigitalObjectButtonActionPerformed();
			}
		}
	}

	private void linkButtonActionPerformed() {
		if (getLookupTable().getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(this, "You must select a digital object to link");
		} else {
			linkDigitalObjectButtonActionPerformed();
		}
    }

    private void doneButtonActionPerformed(ActionEvent e) {
		status = JOptionPane.CANCEL_OPTION;
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

    private void thisWindowClosing() {
		status = JOptionPane.CANCEL_OPTION;
		this.setVisible(false);
    }

    private void lookupTableKeyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            selectActionPerformed();
        }
    }

    private void selectActionPerformed() {
		if (getLookupTable().getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(this, "You must select a digital object to link");
		} else {
			setSelectedDigitalObjectAndExitWithOK();
		}
	}

    private void setSelectedDigitalObjectAndExitWithOK() {
		int selectedRow = lookupTable.getSelectedRow();
		selectedDigitalObject = (DigitalObjects)lookupTableModel.getElementAt(selectedRow);
		status = JOptionPane.OK_OPTION;
		this.setVisible(false);
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
        lookupTable = new DomainSortableTable();
        label1 = new JLabel();
        filterField = new JTextField();
        buttonBar = new JPanel();
        linkButton = new JButton();
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
                    subHeaderLabel.setText("Lookup Digital Object");
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
                label3.setText("Double click on a Digital Object to select it.");
                contentPane.add(label3, cc.xywh(2, 2, 3, 1));

                //---- label4 ----
                label4.setText("Or hit enter if a Digital Object is highlighted.");
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

                    //---- linkButton ----
                    linkButton.setText("Link");
                    linkButton.setOpaque(false);
                    linkButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            linkButtonActionPerformed();
                        }
                    });
                    buttonBar.add(linkButton, cc.xy(3, 1));

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
		try {
			eventList = new BasicEventList();

            if (parentEditorType.equals(PARENT_EDITOR_TYPE_ASSESSMENTS)) {
                eventList.addAll(new DigitalObjectDAO().findAll(LockMode.READ));
            } else if (parentEditorType.equals(PARENT_EDITOR_TYPE_RESOURCES)) {
                eventList.addAll(new DigitalObjectDAO().findAllUnlinked(LockMode.READ));
            }

            SortedList sortedItems = new SortedList(eventList);

		    textFilteredIssues = new FilterList(sortedItems, new TextComponentMatcherEditor(filterField, new DomainFilterator()));
		    lookupTableModel = new EventTableModel(textFilteredIssues, new DomainTableFormat(DigitalObjects.class));
		    lookupTable.setModel(lookupTableModel);
		    TableComparatorChooser tableSorter = new TableComparatorChooser(lookupTable, sortedItems, true);
		    filterField.requestFocusInWindow();
		} catch (LookupException e) {
			new ErrorDialog("", StringHelper.getStackTrace(e)).showDialog();
		}
	}

    private void linkDigitalObjectButtonActionPerformed() {
        ArrayList<DigitalObjects> removeList = new ArrayList<DigitalObjects>();
        int[] selectedRows = lookupTable.getSelectedRows();

        for(int i = 0; i < selectedRows.length; i++) {
            int selectedRow = selectedRows[i];
            DigitalObjects digitalObject = (DigitalObjects) lookupTableModel.getElementAt(selectedRow);
            try {
                if (parentEditorType.equals(PARENT_EDITOR_TYPE_ASSESSMENTS)) {
                    linkDigitalObjectToAssessment(digitalObject);
                } else if (parentEditorType.equals(PARENT_EDITOR_TYPE_RESOURCES)) {
                    removeList.add(digitalObject);
                    linkDigitalObjectToResource(digitalObject);
                }

                //set the record to dirty
                ApplicationFrame.getInstance().setRecordDirty();
            } catch (DuplicateLinkException e) {
                JOptionPane.showMessageDialog(this, e.getMessage() + " is already linked to this record");
            }
        }

        // remove digital objects from the list that were linked, so they can't be linked twice
        for(DigitalObjects digitalObject : removeList) {
            eventList.remove(digitalObject);
        }
    }

    /**
     * Method to link a digital object to an assessment record
     * @param digitalObject The digital object to link to
     * @throws DuplicateLinkException If the digital object is already linked to the record
     */
    private void linkDigitalObjectToAssessment(DigitalObjects digitalObject) throws DuplicateLinkException {
        Assessments assessmentsModel = (Assessments) parentEditorFields.getModel();
        DomainObject link = assessmentsModel.addDigitalObject(digitalObject);

        if (link != null) {
            ((AssessmentsFields) parentEditorFields).getTableAssessmentsDigitalObjects().addDomainObject(link);
        }
    }

    /**
     * Method to link a digital object to an resouce or resource component
     * @param digitalObject The digital object to link to
     * @throws DuplicateLinkException If the digital object is already linked to the record
     */
    private void linkDigitalObjectToResource(DigitalObjects digitalObject) throws DuplicateLinkException {
        ArchDescriptionFields archDescriptionFields = (ArchDescriptionFields)parentEditorFields;
        ResourcesCommon resourceCommon = (ResourcesCommon)archDescriptionFields.getModel();
        ArchDescriptionInstances newInstance = new ArchDescriptionDigitalInstances(resourceCommon, digitalObject, parentResource);
        newInstance.setInstanceType(ArchDescriptionInstances.DIGITAL_OBJECT_INSTANCE);
        archDescriptionFields.getInstancesTable().getEventList().add(newInstance);
		archDescriptionFields.findLocationForInstance(newInstance);
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
    private JButton linkButton;
    private JButton doneButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    protected int status = 0;
	FilterList textFilteredIssues;
	EventTableModel lookupTableModel;
    private DigitalObjects selectedDigitalObject;
	private DomainEditorFields parentEditorFields;
	private String parentEditorType;

	public final int showDialog(Component c) {

        this.pack();
		initLookup();
		this.setLocationRelativeTo(this.getOwner());
		this.setVisible(true);

        return (status);
    }

    public DigitalObjects getSelectedDigitalObjects() {
        return selectedDigitalObject;
    }

    public void setSelectedDigitalObject(DigitalObjects selectedDigitalObject) {
        this.selectedDigitalObject = selectedDigitalObject;
    }
}