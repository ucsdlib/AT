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
 * Created by JFormDesigner on Fri Dec 16 10:51:57 EST 2005
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.structure.DefaultValues;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.util.SequencedObjectsUtils;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.exceptions.AddRelatedObjectException;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;

public class RepositoryFields extends DomainEditorFields {

	protected RepositoryFields() {
		super();
		initComponents();
		notesTable.setTransferable();
		if (!Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_REPOSITORY_MANAGER)) {
			this.setFormToReadOnly();
		}
	}

	private void addDefaultValueActionPerformed() {
		try {
			addRelatedObject(DefaultValues.class, defaultValuesTable);
		} catch (AddRelatedObjectException e) {
			new ErrorDialog(getParentEditor(), "Error adding default value", e).showDialog();
		} catch (DuplicateLinkException e) {
			new ErrorDialog(getParentEditor(), "Error adding default value", e).showDialog();
		}
	}

	private void removeDefaultValueActionPerformed() {
		try {
			removeRelatedTableRow(defaultValuesTable, super.getModel());
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("Default value not removed", e).showDialog();
		}
	}

    private void defaultValuesMouseClicked(MouseEvent e) {
		handleTableMouseClick(e, defaultValuesTable, DefaultValues.class);
	}

    private void removeNoteButtonActionPerformed(ActionEvent e) {
		removeNote();
   }

    private void notesTableMousePressed(MouseEvent e) {
		if (e.isPopupTrigger()) {
			insertNotePopUpMenu.show(e.getComponent(), e.getX(), e.getY());
		}
    }

    private void notesTableMouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			insertNotePopUpMenu.show(e.getComponent(), e.getX(), e.getY());
		}
    }

    private void notesTableMouseClicked(MouseEvent e) {
		handleTableMouseClick(e, notesTable, RepositoryNotes.class);
    }

    private void noteDefaultValuesMouseClicked(MouseEvent e) {
		handleTableMouseClick(e, noteDefaultValuesTable, RepositoryNotesDefaultValues.class, true);
    }

    private void addNoteButtonActionPerformed(ActionEvent e) {
		if (notesTable.getSelectedRow() == -1) {
			addNote(SequencedObjectsUtils.ADD_AT_END);
		} else {
			addNote(SequencedObjectsUtils.ADD_ABOVE_SELECTION);
		}
    }

    public JButton getAddNoteButton() {
    	return addNoteButton;
    }

   private void removeStatisticsActionPerformed(ActionEvent e) {
	   try {
		   removeRelatedTableRow(statisticsTable, super.getModel());
	   } catch (ObjectNotRemovedException e1) {
		   new ErrorDialog("Statistics not removed", e1).showDialog();
	   }
   }

    private void statisticsTableMouseClicked(MouseEvent e) {
		handleTableMouseClick(e, statisticsTable, RepositoryStatistics.class);
    }

    private void addStatisticsActionPerformed() {
		try {
			addRelatedObject(RepositoryStatistics.class, statisticsTable);
		} catch (AddRelatedObjectException e) {
			new ErrorDialog(getParentEditor(), "Error adding default value", e).showDialog();
		} catch (DuplicateLinkException e) {
			new ErrorDialog(getParentEditor(), "Error adding default value", e).showDialog();
		}
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        repositoryName15 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Repositories.PROPERTYNAME_REPOSITORY_NAME));
        tabbedPane1 = new JTabbedPane();
        repositoryInfo = new JPanel();
        label1 = new JLabel();
        repositoryName = ATBasicComponentFactory.createTextField(detailsModel.getModel(Repositories.PROPERTYNAME_REPOSITORY_NAME));
        label15 = new JLabel();
        repositoryName16 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Repositories.PROPERTYNAME_SHORT_NAME));
        label20 = new JLabel();
        repositoryName18 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Repositories.PROPERTYNAME_INSTITUTION_NAME));
        label2 = new JLabel();
        repositoryName2 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Repositories.PROPERTYNAME_REPOSITORY_ADDRESS1));
        label3 = new JLabel();
        repositoryName3 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Repositories.PROPERTYNAME_REPOSITORY_ADDRESS2));
        label14 = new JLabel();
        repositoryName14 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Repositories.PROPERTYNAME_REPOSITORY_ADDRESS3));
        label4 = new JLabel();
        repositoryName4 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Repositories.PROPERTYNAME_REPOSITORY_CITY));
        label5 = new JLabel();
        repositoryName5 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Repositories.PROPERTYNAME_REPOSITORY_REGION));
        label6 = new JLabel();
        repositoryName6 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Repositories.PROPERTYNAME_REPOSITORY_COUNTRY));
        label7 = new JLabel();
        repositoryName7 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Repositories.PROPERTYNAME_REPOSITORY_MAILCODE));
        label8 = new JLabel();
        repositoryName8 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Repositories.PROPERTYNAME_REPOSITORY_TELEPHONE));
        label9 = new JLabel();
        repositoryName9 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Repositories.PROPERTYNAME_REPOSITORY_FAX));
        label10 = new JLabel();
        repositoryName10 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Repositories.PROPERTYNAME_REPOSITORY_EMAIL));
        label11 = new JLabel();
        repositoryName11 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Repositories.PROPERTYNAME_REPOSITORY_URL));
        label12 = new JLabel();
        repositoryName12 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Repositories.PROPERTYNAME_REPOSITORY_COUNTRY_CODE));
        label13 = new JLabel();
        repositoryName13 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Repositories.PROPERTYNAME_REPOSITORY_AGENCY_CODE));
        label41 = new JLabel();
        repositoryName19 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Repositories.PROPERTYNAME_NCES_ID));
        label18 = new JLabel();
        repositoryName17 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Repositories.PROPERTYNAME_BRANDING_DEVICE));
        label19 = new JLabel();
        resourcesLanguageCode = ATBasicComponentFactory.createComboBox(detailsModel, Repositories.PROPERTYNAME_DESCRIPTIVE_LANGUAGE, Repositories.class);
        repositoryStatistics = new JPanel();
        scrollPane4 = new JScrollPane();
        statisticsTable = new DomainSortableTable(RepositoryStatistics.class);
        panel2 = new JPanel();
        addStatistics = new JButton();
        removeStatistics = new JButton();
        defaultValues = new JPanel();
        label16 = new JLabel();
        scrollPane1 = new JScrollPane();
        defaultValuesTable = new DomainSortableTable(DefaultValues.class);
        panel1 = new JPanel();
        addDefaultValue = new JButton();
        removeDefaultValue = new JButton();
        separator5 = new JSeparator();
        label17 = new JLabel();
        scrollPane2 = new JScrollPane();
        noteDefaultValuesTable = new DomainSortableTable(RepositoryNotesDefaultValues.class, RepositoryNotesDefaultValues.PROPERTYNAME_NOTE_TYPE);
        notes = new JPanel();
        scrollPane3 = new JScrollPane();
        notesTable = new DomainSortedTable(RepositoryNotes.class);
        panel3 = new JPanel();
        addNoteButton = new JButton();
        removeNoteButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        setBackground(new Color(200, 205, 232));
        setLayout(new FormLayout(
            new ColumnSpec[] {
                FormFactory.RELATED_GAP_COLSPEC,
                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
            },
            RowSpec.decodeSpecs("default, fill:default:grow")));

        //---- repositoryName15 ----
        repositoryName15.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        repositoryName15.setEditable(false);
        repositoryName15.setOpaque(false);
        repositoryName15.setBorder(null);
        add(repositoryName15, cc.xy(2, 1));

        //======== tabbedPane1 ========
        {
            tabbedPane1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

            //======== repositoryInfo ========
            {
                repositoryInfo.setBorder(Borders.DLU4_BORDER);
                repositoryInfo.setBackground(new Color(200, 205, 232));
                repositoryInfo.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                repositoryInfo.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        new ColumnSpec("max(default;400px):grow")
                    },
                    new RowSpec[] {
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC
                    }));

                //---- label1 ----
                label1.setText("Repository Name");
                label1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                ATFieldInfo.assignLabelInfo(label1, Repositories.class, Repositories.PROPERTYNAME_REPOSITORY_NAME);
                repositoryInfo.add(label1, cc.xy(1, 1));
                repositoryInfo.add(repositoryName, cc.xy(3, 1));

                //---- label15 ----
                label15.setText("Short Name");
                label15.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                ATFieldInfo.assignLabelInfo(label15, Repositories.class, Repositories.PROPERTYNAME_SHORT_NAME);
                repositoryInfo.add(label15, cc.xy(1, 3));
                repositoryInfo.add(repositoryName16, cc.xy(3, 3));

                //---- label20 ----
                label20.setText("Instution Name");
                label20.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                ATFieldInfo.assignLabelInfo(label20, Repositories.class, Repositories.PROPERTYNAME_INSTITUTION_NAME);
                repositoryInfo.add(label20, cc.xy(1, 5));
                repositoryInfo.add(repositoryName18, cc.xy(3, 5));

                //---- label2 ----
                label2.setText("Address");
                label2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                ATFieldInfo.assignLabelInfo(label2, Repositories.class, Repositories.PROPERTYNAME_REPOSITORY_ADDRESS1);
                repositoryInfo.add(label2, cc.xy(1, 7));
                repositoryInfo.add(repositoryName2, cc.xy(3, 7));

                //---- label3 ----
                label3.setText("Address");
                label3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                ATFieldInfo.assignLabelInfo(label3, Repositories.class, Repositories.PROPERTYNAME_REPOSITORY_ADDRESS2);
                repositoryInfo.add(label3, cc.xy(1, 9));
                repositoryInfo.add(repositoryName3, cc.xy(3, 9));

                //---- label14 ----
                label14.setText("Address");
                label14.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                ATFieldInfo.assignLabelInfo(label14, Repositories.class, Repositories.PROPERTYNAME_REPOSITORY_ADDRESS3);
                repositoryInfo.add(label14, cc.xy(1, 11));
                repositoryInfo.add(repositoryName14, cc.xy(3, 11));

                //---- label4 ----
                label4.setText("City");
                label4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                ATFieldInfo.assignLabelInfo(label4, Repositories.class, Repositories.PROPERTYNAME_REPOSITORY_CITY);
                repositoryInfo.add(label4, cc.xy(1, 13));
                repositoryInfo.add(repositoryName4, cc.xy(3, 13));

                //---- label5 ----
                label5.setText("Region");
                label5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                ATFieldInfo.assignLabelInfo(label5, Repositories.class, Repositories.PROPERTYNAME_REPOSITORY_REGION);
                repositoryInfo.add(label5, cc.xy(1, 15));
                repositoryInfo.add(repositoryName5, cc.xy(3, 15));

                //---- label6 ----
                label6.setText("Country");
                label6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                ATFieldInfo.assignLabelInfo(label6, Repositories.class, Repositories.PROPERTYNAME_REPOSITORY_COUNTRY);
                repositoryInfo.add(label6, cc.xy(1, 17));
                repositoryInfo.add(repositoryName6, cc.xy(3, 17));

                //---- label7 ----
                label7.setText("Mail Code");
                label7.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                ATFieldInfo.assignLabelInfo(label7, Repositories.class, Repositories.PROPERTYNAME_REPOSITORY_MAILCODE);
                repositoryInfo.add(label7, cc.xy(1, 19));
                repositoryInfo.add(repositoryName7, cc.xy(3, 19));

                //---- label8 ----
                label8.setText("Telephone");
                label8.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                ATFieldInfo.assignLabelInfo(label8, Repositories.class, Repositories.PROPERTYNAME_REPOSITORY_TELEPHONE);
                repositoryInfo.add(label8, cc.xy(1, 21));
                repositoryInfo.add(repositoryName8, cc.xy(3, 21));

                //---- label9 ----
                label9.setText("FAX");
                label9.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                ATFieldInfo.assignLabelInfo(label9, Repositories.class, Repositories.PROPERTYNAME_REPOSITORY_FAX);
                repositoryInfo.add(label9, cc.xy(1, 23));
                repositoryInfo.add(repositoryName9, cc.xy(3, 23));

                //---- label10 ----
                label10.setText("Email");
                label10.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                ATFieldInfo.assignLabelInfo(label10, Repositories.class, Repositories.PROPERTYNAME_REPOSITORY_EMAIL);
                repositoryInfo.add(label10, cc.xy(1, 25));
                repositoryInfo.add(repositoryName10, cc.xy(3, 25));

                //---- label11 ----
                label11.setText("URL");
                label11.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                ATFieldInfo.assignLabelInfo(label11, Repositories.class, Repositories.PROPERTYNAME_REPOSITORY_URL);
                repositoryInfo.add(label11, cc.xy(1, 27));
                repositoryInfo.add(repositoryName11, cc.xy(3, 27));

                //---- label12 ----
                label12.setText("Country Code");
                label12.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                ATFieldInfo.assignLabelInfo(label12, Repositories.class, Repositories.PROPERTYNAME_REPOSITORY_COUNTRY_CODE);
                repositoryInfo.add(label12, cc.xy(1, 29));
                repositoryInfo.add(repositoryName12, cc.xy(3, 29));

                //---- label13 ----
                label13.setText("Agency Code");
                label13.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                ATFieldInfo.assignLabelInfo(label13, Repositories.class, Repositories.PROPERTYNAME_REPOSITORY_AGENCY_CODE);
                repositoryInfo.add(label13, cc.xy(1, 31));
                repositoryInfo.add(repositoryName13, cc.xy(3, 31));

                //---- label41 ----
                label41.setText("NCES Unit ID");
                label41.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                ATFieldInfo.assignLabelInfo(label41, Repositories.class, Repositories.PROPERTYNAME_NCES_ID);
                repositoryInfo.add(label41, cc.xy(1, 33));

                //---- repositoryName19 ----
                repositoryName19.setColumns(20);
                repositoryInfo.add(repositoryName19, cc.xywh(3, 33, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                //---- label18 ----
                label18.setText("Branding Device");
                label18.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                ATFieldInfo.assignLabelInfo(label18, Repositories.class, Repositories.PROPERTYNAME_BRANDING_DEVICE);
                repositoryInfo.add(label18, cc.xy(1, 35));
                repositoryInfo.add(repositoryName17, cc.xy(3, 35));

                //---- label19 ----
                label19.setText("Descriptive Language");
                label19.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                ATFieldInfo.assignLabelInfo(label19, Repositories.class, Repositories.PROPERTYNAME_DESCRIPTIVE_LANGUAGE);
                repositoryInfo.add(label19, cc.xy(1, 37));

                //---- resourcesLanguageCode ----
                resourcesLanguageCode.setMaximumSize(new Dimension(50, 27));
                resourcesLanguageCode.setOpaque(false);
                resourcesLanguageCode.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                repositoryInfo.add(resourcesLanguageCode, cc.xywh(3, 37, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
            }
            tabbedPane1.addTab("Repository Info", repositoryInfo);


            //======== repositoryStatistics ========
            {
                repositoryStatistics.setBackground(new Color(200, 205, 232));
                repositoryStatistics.setLayout(new FormLayout(
                    ColumnSpec.decodeSpecs("default:grow"),
                    new RowSpec[] {
                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC
                    }));

                //======== scrollPane4 ========
                {
                    scrollPane4.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                    scrollPane4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    scrollPane4.setPreferredSize(new Dimension(469, 250));

                    //---- statisticsTable ----
                    statisticsTable.setPreferredScrollableViewportSize(new Dimension(450, 250));
                    statisticsTable.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            statisticsTableMouseClicked(e);
                        }
                    });
                    scrollPane4.setViewportView(statisticsTable);
                }
                repositoryStatistics.add(scrollPane4, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL));

                //======== panel2 ========
                {
                    panel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel2.setOpaque(false);
                    panel2.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC
                        },
                        RowSpec.decodeSpecs("default")));

                    //---- addStatistics ----
                    addStatistics.setText("Add Statistics");
                    addStatistics.setOpaque(false);
                    addStatistics.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    addStatistics.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            addStatisticsActionPerformed();
                        }
                    });
                    panel2.add(addStatistics, cc.xy(1, 1));

                    //---- removeStatistics ----
                    removeStatistics.setText("Remove Statistics");
                    removeStatistics.setOpaque(false);
                    removeStatistics.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    removeStatistics.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            removeStatisticsActionPerformed(e);
                        }
                    });
                    panel2.add(removeStatistics, cc.xy(3, 1));
                }
                repositoryStatistics.add(panel2, cc.xywh(1, 3, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
            }
            tabbedPane1.addTab("Repository Statistics", repositoryStatistics);


            //======== defaultValues ========
            {
                defaultValues.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                defaultValues.setBackground(new Color(200, 205, 232));
                defaultValues.setLayout(new FormLayout(
                    ColumnSpec.decodeSpecs("default:grow"),
                    new RowSpec[] {
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                    }));

                //---- label16 ----
                label16.setText("Fields");
                defaultValues.add(label16, cc.xy(1, 1));

                //======== scrollPane1 ========
                {
                    scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                    scrollPane1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    scrollPane1.setPreferredSize(new Dimension(469, 250));

                    //---- defaultValuesTable ----
                    defaultValuesTable.setPreferredScrollableViewportSize(new Dimension(450, 250));
                    defaultValuesTable.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            defaultValuesMouseClicked(e);
                        }
                    });
                    scrollPane1.setViewportView(defaultValuesTable);
                }
                defaultValues.add(scrollPane1, cc.xywh(1, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL));

                //======== panel1 ========
                {
                    panel1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel1.setOpaque(false);
                    panel1.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC
                        },
                        RowSpec.decodeSpecs("default")));

                    //---- addDefaultValue ----
                    addDefaultValue.setText("Add Default Value");
                    addDefaultValue.setOpaque(false);
                    addDefaultValue.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    addDefaultValue.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            addDefaultValueActionPerformed();
                        }
                    });
                    panel1.add(addDefaultValue, cc.xy(1, 1));

                    //---- removeDefaultValue ----
                    removeDefaultValue.setText("Remove Default Value");
                    removeDefaultValue.setOpaque(false);
                    removeDefaultValue.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    removeDefaultValue.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            removeDefaultValueActionPerformed();
                        }
                    });
                    panel1.add(removeDefaultValue, cc.xy(3, 1));
                }
                defaultValues.add(panel1, cc.xywh(1, 5, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

                //---- separator5 ----
                separator5.setBackground(new Color(220, 220, 232));
                separator5.setForeground(new Color(147, 131, 86));
                separator5.setMinimumSize(new Dimension(1, 10));
                separator5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                defaultValues.add(separator5, cc.xy(1, 7));

                //---- label17 ----
                label17.setText("Default Values for notes");
                defaultValues.add(label17, cc.xy(1, 9));

                //======== scrollPane2 ========
                {
                    scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                    scrollPane2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    scrollPane2.setPreferredSize(new Dimension(469, 250));

                    //---- noteDefaultValuesTable ----
                    noteDefaultValuesTable.setPreferredScrollableViewportSize(new Dimension(450, 250));
                    noteDefaultValuesTable.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            noteDefaultValuesMouseClicked(e);
                        }
                    });
                    scrollPane2.setViewportView(noteDefaultValuesTable);
                }
                defaultValues.add(scrollPane2, cc.xywh(1, 11, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
            }
            tabbedPane1.addTab("Default Values", defaultValues);


            //======== notes ========
            {
                notes.setBackground(new Color(200, 205, 232));
                notes.setLayout(new FormLayout(
                    ColumnSpec.decodeSpecs("default:grow"),
                    new RowSpec[] {
                        new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC
                    }));

                //======== scrollPane3 ========
                {
                    scrollPane3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                    scrollPane3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                    //---- notesTable ----
                    notesTable.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            notesTableMouseClicked(e);
                        }
                        @Override
                        public void mousePressed(MouseEvent e) {
                            notesTableMousePressed(e);
                        }
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            notesTableMouseReleased(e);
                        }
                    });
                    scrollPane3.setViewportView(notesTable);
                }
                notes.add(scrollPane3, cc.xywh(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));

                //======== panel3 ========
                {
                    panel3.setBackground(new Color(231, 188, 251));
                    panel3.setOpaque(false);
                    panel3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel3.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC
                        },
                        RowSpec.decodeSpecs("default")));

                    //---- addNoteButton ----
                    addNoteButton.setText("Add Note");
                    addNoteButton.setOpaque(false);
                    addNoteButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    addNoteButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            addNoteButtonActionPerformed(e);
                        }
                    });
                    panel3.add(addNoteButton, cc.xy(1, 1));

                    //---- removeNoteButton ----
                    removeNoteButton.setText("Remove Note");
                    removeNoteButton.setOpaque(false);
                    removeNoteButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    removeNoteButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            removeNoteButtonActionPerformed(e);
                        }
                    });
                    panel3.add(removeNoteButton, cc.xy(3, 1));
                }
                notes.add(panel3, cc.xywh(1, 3, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
            }
            tabbedPane1.addTab("Notes", notes);

        }
        add(tabbedPane1, cc.xywh(1, 2, 2, 1));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JTextField repositoryName15;
    private JTabbedPane tabbedPane1;
    private JPanel repositoryInfo;
    private JLabel label1;
    private JTextField repositoryName;
    private JLabel label15;
    private JTextField repositoryName16;
    private JLabel label20;
    private JTextField repositoryName18;
    private JLabel label2;
    private JTextField repositoryName2;
    private JLabel label3;
    private JTextField repositoryName3;
    private JLabel label14;
    private JTextField repositoryName14;
    private JLabel label4;
    private JTextField repositoryName4;
    private JLabel label5;
    private JTextField repositoryName5;
    private JLabel label6;
    private JTextField repositoryName6;
    private JLabel label7;
    private JTextField repositoryName7;
    private JLabel label8;
    private JTextField repositoryName8;
    private JLabel label9;
    private JTextField repositoryName9;
    private JLabel label10;
    private JTextField repositoryName10;
    private JLabel label11;
    private JTextField repositoryName11;
    private JLabel label12;
    private JTextField repositoryName12;
    private JLabel label13;
    private JTextField repositoryName13;
    private JLabel label41;
    private JTextField repositoryName19;
    private JLabel label18;
    private JTextField repositoryName17;
    private JLabel label19;
    public JComboBox resourcesLanguageCode;
    private JPanel repositoryStatistics;
    private JScrollPane scrollPane4;
    private DomainSortableTable statisticsTable;
    private JPanel panel2;
    private JButton addStatistics;
    private JButton removeStatistics;
    private JPanel defaultValues;
    private JLabel label16;
    private JScrollPane scrollPane1;
    private DomainSortableTable defaultValuesTable;
    private JPanel panel1;
    private JButton addDefaultValue;
    private JButton removeDefaultValue;
    private JSeparator separator5;
    private JLabel label17;
    private JScrollPane scrollPane2;
    private DomainSortableTable noteDefaultValuesTable;
    private JPanel notes;
    private JScrollPane scrollPane3;
    private DomainSortedTable notesTable;
    private JPanel panel3;
    private JButton addNoteButton;
    private JButton removeNoteButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

	protected JPopupMenu insertNotePopUpMenu = new JPopupMenu();

	public void setModel(DomainObject model, InfiniteProgressPanel progressPanel) {
        super.setModel(model, progressPanel);
		Repositories repositoryModel = (Repositories) model;
		defaultValuesTable.updateCollection(repositoryModel.getDefaultValues());
		notesTable.updateCollection(repositoryModel.getNotes());
		noteDefaultValuesTable.updateCollection(repositoryModel.getNoteDefaultValues());
		statisticsTable.updateCollection(repositoryModel.getStatistics());
	}

	public Component getInitialFocusComponent() {
		return repositoryName;
	}

	public JTextField getRepositoryName() {
		return repositoryName;
	}

    public void setRepositoryName(JTextField repositoryName) {
        this.repositoryName = repositoryName;
    }

	protected void removeNote() {
		try {
			this.removeRelatedTableRow(notesTable, null, super.getModel());
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("Note not removed", e).showDialog();
		}
		notesTable.updateCollection(((Repositories)super.getModel()).getNotes());

	}

	protected void addNote(String whereString) {

		try {
			addRelatedObject(whereString, RepositoryNotes.class, notesTable);
		} catch (AddRelatedObjectException e) {
			new ErrorDialog(getParentEditor(), "Error adding note", e).showDialog();
		} catch (DuplicateLinkException e) {
			new ErrorDialog(getParentEditor(), "Error adding note", e).showDialog();
		}

	}

	protected ActionListener menuDeleteListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			removeNote();
		}
	};

}
