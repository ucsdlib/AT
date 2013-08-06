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
 */

package org.archiviststoolkit.editor;

import java.awt.event.*;
import com.jgoodies.forms.factories.*;
import org.archiviststoolkit.swing.*;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.DefaultValues;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.exceptions.DomainEditorCreationException;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.plugin.ATPlugin;
import org.archiviststoolkit.plugin.ATPluginFactory;
import org.archiviststoolkit.ApplicationFrame;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.text.ParseException;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.jgoodies.forms.layout.*;
import com.jgoodies.forms.factories.FormFactory;

public class DigitalObjectFields extends ArchDescriptionFields  {
    private ArrayList<ATPlugin> plugins = null; // stores any embedded digital object editor plugins

    /**
     * Constructor that doesn't take any arguments
     */
    public DigitalObjectFields() {
        this(null);
    }

	public DigitalObjectFields(DomainEditor parent) {
		super();
		this.setParentEditor(parent);
		initComponents();
		notesTable.setTransferable();
		initDigitalObjectNotes();
		initAccess();
        addRelatedTableInformation();
        initPlugins();
	}

	protected void setDisplayToFirstTab() {
		this.tabbedPane.setSelectedIndex(0);
	}

    public JLabel getLabel_resourcesDateBegin() {
    	return label_resourcesDateBegin;
    }

    private void removeNoteButtonActionPerformed(ActionEvent e) {
    	super.removeNotesEtc();
    }

    public DomainSortedTable getNotesTable() {
    	return notesTable;
    }

//    private void fileVersionsTableMousePressed(MouseEvent e) {
//        if (e.isPopupTrigger()) {
//            insertFileVersionPopUpMenu.show(e.getComponent(), e.getX(), e.getY());
//        }
//    }
//
//    private void fileVersionsTableMouseReleased(MouseEvent e) {
//        if (e.isPopupTrigger()) {
//            insertFileVersionPopUpMenu.show(e.getComponent(), e.getX(), e.getY());
//        }
//    }

    private void addNoteEtcComboBoxActionPerformed(ActionEvent e) {
    	super.addNoteEtcComboBoxAction();
    }

   private void namesTableMouseClicked(MouseEvent e) {
	   super.handleNamesTableMouseClick(e);
   }

    private void addFileVersionButtonActionPerformed() {
		DigitalObjects digitalObjectModel = (DigitalObjects) super.getModel();
		FileVersions newFileVersions;
		DomainEditor dialogFileVersions = null;
		try {
			dialogFileVersions = DomainEditorFactory.getInstance().createDomainEditorWithParent(FileVersions.class, getParentEditor(), fileVersionsTable);
		} catch (DomainEditorCreationException e) {
			new ErrorDialog(getParentEditor(), "Error creating editor for FileVersions", e).showDialog();

		}
		dialogFileVersions.setNewRecord(true);

        int returnStatus;
        Boolean done = false;
        while (!done) {
            newFileVersions = new FileVersions(digitalObjectModel);
            dialogFileVersions.setModel(newFileVersions, null);
            returnStatus = dialogFileVersions.showDialog();
            if (returnStatus == JOptionPane.OK_OPTION) {
                digitalObjectModel.addFileVersion(newFileVersions);
                fileVersionsTable.getEventList().add(newFileVersions);
                done = true;
            } else if (returnStatus == StandardEditor.OK_AND_ANOTHER_OPTION) {
                digitalObjectModel.addFileVersion(newFileVersions);
                fileVersionsTable.getEventList().add(newFileVersions);
            } else {
                done = true;
            }
        }
        dialogFileVersions.setNewRecord(false);
    }

    private void editNameRelationshipButtonActionPerformed() {
		super.editNameRelationshipActionPerformed();
    }

    private void addNameRelationshipButtonActionPerformed() {
    	addNameRelationship();
    }

    private void removeNameRelationshipButtonActionPerformed() {
        ArchDescription archDescriptionModel = (ArchDescription) super.getModel();
		try {
			this.removeRelatedTableRow(getNamesTable(), archDescriptionModel);
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("Name link not removed", e).showDialog();
		}
	}

    private void addSubjectRelationshipButtonActionPerformed() {
    	addSubjectRelationship();
    }

    private void removeSubjectRelationshipButtonActionPerformed() {
        ArchDescription archDescriptionModel = (ArchDescription) super.getModel();
		try {
			this.removeRelatedTableRow(getSubjectsTable(), archDescriptionModel);
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("Subject link not removed", e).showDialog();
		}
	}

    private void notesTableMouseClicked(MouseEvent e) {
		handleTableMouseClick(e, notesTable, ArchDescriptionRepeatingData.class);
    }
    /**
     * Method to change the current repository. This functionality is available only to
     * super users
     */
    private void changeRepositoryButtonActionPerformed() {
        Vector repositories = Repositories.getRepositoryList();
		Repositories currentRepostory = digitalObjectModel.getRepository();
        SelectFromList dialog = new SelectFromList(this.getParentEditor(), "Select a repository", repositories.toArray());
        dialog.setSelectedValue(currentRepostory);
        if (dialog.showDialog() == JOptionPane.OK_OPTION) {
            digitalObjectModel.setRepository((Repositories)dialog.getSelectedValue());
            setRepositoryText();
        }
    }

    /**
     * Update the repository text field
     */
    private void setRepositoryText() {
		if (digitalObjectModel.getRepository() == null) {
			repositoryName.setText("");
		} else {
			repositoryName.setText(digitalObjectModel.getRepository().getShortName());
		}
	}

    public JButton getChangeRepositoryButton() {
        return changeRepositoryButton;
    }

    private void initComponents() {

        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        panel3 = new JPanel();
        label2 = new JLabel();
        digitalObjectTitle = new JTextField();
        tabbedPane = new JTabbedPane();
        basicInformationPanel = new JPanel();
        panel16 = new JPanel();
        panel1 = new JPanel();
        label_resourcesLanguageCode4 = new JLabel();
        label = ATBasicComponentFactory.createTextField(detailsModel.getModel(DigitalObjects.PROPERTYNAME_LABEL),false);
        panel19 = new JPanel();
        label_resourcesTitle = new JLabel();
        scrollPane42 = new JScrollPane();
        title = ATBasicComponentFactory.createTextArea(detailsModel.getModel(ArchDescription.PROPERTYNAME_TITLE),false);
        resourcesPanel = new JPanel();
        resourcesLabel = new JLabel();
        scrollPane4 = new JScrollPane();
        resourcesTable = new DomainSortableTable();
        panel13 = new JPanel();
        panel17 = new JPanel();
        panel18 = new JPanel();
        panel15 = new JPanel();
        panel20 = new JPanel();
        ExtentNumberLabel3 = new JLabel();
        label_resourcesDateExpression = new JLabel();
        dateExpression = ATBasicComponentFactory.createTextField(detailsModel.getModel(ArchDescription.PROPERTYNAME_DATE_EXPRESSION),false);
        panel9 = new JPanel();
        Date1Label = new JLabel();
        label_resourcesDateBegin = new JLabel();
        dateBegin = ATBasicComponentFactory.createIntegerField(detailsModel,ArchDescription.PROPERTYNAME_DATE_BEGIN);
        label_resourcesDateEnd = new JLabel();
        dateEnd = ATBasicComponentFactory.createIntegerField(detailsModel,ArchDescription.PROPERTYNAME_DATE_END);
        digitalObjectResourceRecordOnly = new JPanel();
        restrictionsApply = ATBasicComponentFactory.createCheckBox(detailsModel, ArchDescription.PROPERTYNAME_RESTRICTIONS_APPLY, DigitalObjects.class);
        label_resourcesLanguageCode3 = new JLabel();
        objectType = ATBasicComponentFactory.createComboBox(detailsModel, DigitalObjects.PROPERTYNAME_OBJECT_TYPE, DigitalObjects.class);
        actuateLabel2 = new JLabel();
        scrollPane43 = new JScrollPane();
        title2 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(DigitalObjects.PROPERTYNAME_METS_IDENTIFIER),false);
        actuateLabel = new JLabel();
        actuate = ATBasicComponentFactory.createComboBox(detailsModel, DigitalObjects.PROPERTYNAME_EAD_DAO_ACTUATE, DigitalObjects.class);
        showLabel = new JLabel();
        show = ATBasicComponentFactory.createComboBox(detailsModel, DigitalObjects.PROPERTYNAME_EAD_DAO_SHOW, DigitalObjects.class);
        componentIDPanel = new JPanel();
        componentLabel1 = new JLabel();
        dateExpression2 = ATBasicComponentFactory.createTextField(detailsModel.getModel(DigitalObjects.PROPERTYNAME_COMPONENT_ID),false);
        panel5 = new JPanel();
        label_resourcesLanguageCode = new JLabel();
        languageCode = ATBasicComponentFactory.createComboBox(detailsModel, DigitalObjects.PROPERTYNAME_LANGUAGE_CODE, DigitalObjects.class);
        repositoryPanel = new JPanel();
        label_repositoryName = new JLabel();
        repositoryName = new JTextField();
        changeRepositoryButton = new JButton();
        panel2 = new JPanel();
        label1 = new JLabel();
        scrollPane6 = new JScrollPane();
        fileVersionsTable = new DomainSortableTable(FileVersions.class, FileVersions.PROPERTYNAME_FILE_VERSIONS_USE_STATEMENT);
        panel29 = new JPanel();
        addFileVersionButton = new JButton();
        removeFileVersionButton = new JButton();
        namesPanel = new JPanel();
        SubjectsLabel2 = new JLabel();
        scrollPane1 = new JScrollPane();
        namesTable = new DomainSortableTable(ArchDescriptionNames.class, ArchDescriptionNames.PROPERTYNAME_SORT_NAME);
        panel8 = new JPanel();
        editNameRelationshipButton = new JButton();
        addNameRelationshipButton = new JButton();
        removeNameRelationshipButton = new JButton();
        separator5 = new JSeparator();
        SubjectsLabel3 = new JLabel();
        scrollPane3 = new JScrollPane();
        subjectsTable = new DomainSortableTable(ArchDescriptionSubjects.class, ArchDescriptionSubjects.PROPERTYNAME_SUBJECT_TERM);
        panel11 = new JPanel();
        addSubjectRelationshipButton = new JButton();
        removeSubjectRelationshipButton = new JButton();
        panel6 = new JPanel();
        scrollPane5 = new JScrollPane();
        notesTable = new DomainSortedTable(ArchDescriptionRepeatingData.class);
        panel14 = new JPanel();
        addNoteEtcComboBox = new JComboBox();
        removeNoteButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        setBackground(new Color(200, 205, 232));
        setLayout(new FormLayout(
            "left:default:grow",
            "default, fill:default:grow"));

        //======== panel3 ========
        {
            panel3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            panel3.setBackground(new Color(200, 205, 232));
            panel3.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                },
                RowSpec.decodeSpecs("default")));

            //---- label2 ----
            label2.setText("Title: ");
            label2.setForeground(new Color(0, 0, 102));
            label2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            panel3.add(label2, cc.xy(1, 1));

            //---- digitalObjectTitle ----
            digitalObjectTitle.setEditable(false);
            digitalObjectTitle.setOpaque(false);
            digitalObjectTitle.setBorder(null);
            panel3.add(digitalObjectTitle, cc.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        }
        add(panel3, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

        //======== tabbedPane ========
        {
            tabbedPane.setMinimumSize(new Dimension(635, 408));
            tabbedPane.setFocusable(false);
            tabbedPane.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            tabbedPane.setBackground(new Color(200, 205, 232));
            tabbedPane.setOpaque(true);

            //======== basicInformationPanel ========
            {
                basicInformationPanel.setBackground(new Color(200, 205, 232));
                basicInformationPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                basicInformationPanel.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, 0.5),
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        new ColumnSpec("left:max(default;300px):grow(0.5)")
                    },
                    new RowSpec[] {
                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                    }));
                ((FormLayout)basicInformationPanel.getLayout()).setColumnGroups(new int[][] {{1, 3}});

                //======== panel16 ========
                {
                    panel16.setOpaque(false);
                    panel16.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel16.setLayout(new FormLayout(
                        ColumnSpec.decodeSpecs("default:grow"),
                        new RowSpec[] {
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, 0.30000000000000004)
                        }));

                    //======== panel1 ========
                    {
                        panel1.setOpaque(false);
                        panel1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel1.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                            },
                            RowSpec.decodeSpecs("default")));

                        //---- label_resourcesLanguageCode4 ----
                        label_resourcesLanguageCode4.setText("Label");
                        label_resourcesLanguageCode4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label_resourcesLanguageCode4, DigitalObjects.class, DigitalObjects.PROPERTYNAME_LABEL);
                        panel1.add(label_resourcesLanguageCode4, cc.xy(1, 1));
                        panel1.add(label, new CellConstraints(3, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP, new Insets( 0, 0, 0, 5)));
                    }
                    panel16.add(panel1, cc.xy(1, 1));

                    //======== panel19 ========
                    {
                        panel19.setOpaque(false);
                        panel19.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel19.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                            },
                            new RowSpec[] {
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC
                            }));

                        //---- label_resourcesTitle ----
                        label_resourcesTitle.setText("Title");
                        label_resourcesTitle.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label_resourcesTitle, DigitalObjects.class, DigitalObjects.PROPERTYNAME_TITLE);
                        panel19.add(label_resourcesTitle, cc.xy(1, 1));

                        //======== scrollPane42 ========
                        {
                            scrollPane42.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                            scrollPane42.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                            //---- title ----
                            title.setRows(4);
                            title.setLineWrap(true);
                            title.setWrapStyleWord(true);
                            scrollPane42.setViewportView(title);
                        }
                        panel19.add(scrollPane42, cc.xywh(1, 3, 3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));

                        //======== resourcesPanel ========
                        {
                            resourcesPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            resourcesPanel.setOpaque(false);
                            resourcesPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
                            resourcesPanel.setLayout(new FormLayout(
                                ColumnSpec.decodeSpecs("default:grow"),
                                new RowSpec[] {
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.LINE_GAP_ROWSPEC,
                                    new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                                }));

                            //---- resourcesLabel ----
                            resourcesLabel.setText("Resource Linked to this Digital Object");
                            resourcesLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            resourcesPanel.add(resourcesLabel, cc.xy(1, 1));

                            //======== scrollPane4 ========
                            {
                                scrollPane4.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                                scrollPane4.setPreferredSize(new Dimension(300, 50));

                                //---- resourcesTable ----
                                resourcesTable.setPreferredScrollableViewportSize(new Dimension(300, 100));
                                resourcesTable.setFocusable(false);
                                scrollPane4.setViewportView(resourcesTable);
                            }
                            resourcesPanel.add(scrollPane4, cc.xywh(1, 3, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
                        }
                        panel19.add(resourcesPanel, cc.xywh(1, 5, 3, 1));
                    }
                    panel16.add(panel19, cc.xy(1, 3));
                }
                basicInformationPanel.add(panel16, cc.xy(1, 1));

                //======== panel13 ========
                {
                    panel13.setOpaque(false);
                    panel13.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel13.setLayout(new FormLayout(
                        "left:default:grow",
                        "fill:default:grow"));

                    //======== panel17 ========
                    {
                        panel17.setOpaque(false);
                        panel17.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel17.setLayout(new FormLayout(
                            ColumnSpec.decodeSpecs("default:grow"),
                            new RowSpec[] {
                                new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
                                FormFactory.LINE_GAP_ROWSPEC,
                                new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC
                            }));

                        //======== panel18 ========
                        {
                            panel18.setBorder(null);
                            panel18.setOpaque(false);
                            panel18.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            panel18.setLayout(new FormLayout(
                                "default:grow",
                                "fill:default:grow"));

                            //======== panel15 ========
                            {
                                panel15.setBorder(new BevelBorder(BevelBorder.LOWERED));
                                panel15.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                panel15.setBackground(new Color(182, 187, 212));
                                panel15.setLayout(new FormLayout(
                                    "default:grow",
                                    "fill:default:grow"));

                                //======== panel20 ========
                                {
                                    panel20.setOpaque(false);
                                    panel20.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                    panel20.setBorder(Borders.DLU2_BORDER);
                                    panel20.setLayout(new FormLayout(
                                        new ColumnSpec[] {
                                            FormFactory.UNRELATED_GAP_COLSPEC,
                                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                            FormFactory.DEFAULT_COLSPEC,
                                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                            new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                                        },
                                        new RowSpec[] {
                                            FormFactory.DEFAULT_ROWSPEC,
                                            FormFactory.LINE_GAP_ROWSPEC,
                                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW),
                                            FormFactory.LINE_GAP_ROWSPEC,
                                            FormFactory.DEFAULT_ROWSPEC
                                        }));

                                    //---- ExtentNumberLabel3 ----
                                    ExtentNumberLabel3.setText("Dates");
                                    ExtentNumberLabel3.setForeground(new Color(0, 0, 102));
                                    ExtentNumberLabel3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                    panel20.add(ExtentNumberLabel3, cc.xywh(1, 1, 4, 1));

                                    //---- label_resourcesDateExpression ----
                                    label_resourcesDateExpression.setText("Date Expression");
                                    label_resourcesDateExpression.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                    ATFieldInfo.assignLabelInfo(label_resourcesDateExpression, DigitalObjects.class, DigitalObjects.PROPERTYNAME_DATE_EXPRESSION);
                                    panel20.add(label_resourcesDateExpression, cc.xy(3, 3));
                                    panel20.add(dateExpression, cc.xy(5, 3));

                                    //======== panel9 ========
                                    {
                                        panel9.setOpaque(false);
                                        panel9.setLayout(new FormLayout(
                                            new ColumnSpec[] {
                                                FormFactory.DEFAULT_COLSPEC,
                                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                                FormFactory.DEFAULT_COLSPEC,
                                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                                FormFactory.DEFAULT_COLSPEC,
                                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                                FormFactory.DEFAULT_COLSPEC,
                                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                                FormFactory.DEFAULT_COLSPEC
                                            },
                                            RowSpec.decodeSpecs("default")));

                                        //---- Date1Label ----
                                        Date1Label.setText("Date");
                                        Date1Label.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                        panel9.add(Date1Label, cc.xy(1, 1));

                                        //---- label_resourcesDateBegin ----
                                        label_resourcesDateBegin.setText("Begin");
                                        label_resourcesDateBegin.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                        ATFieldInfo.assignLabelInfo(label_resourcesDateBegin, DigitalObjects.class, DigitalObjects.PROPERTYNAME_DATE_BEGIN);
                                        panel9.add(label_resourcesDateBegin, cc.xy(3, 1));

                                        //---- dateBegin ----
                                        dateBegin.setColumns(4);
                                        panel9.add(dateBegin, cc.xy(5, 1));

                                        //---- label_resourcesDateEnd ----
                                        label_resourcesDateEnd.setText("End");
                                        label_resourcesDateEnd.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                        ATFieldInfo.assignLabelInfo(label_resourcesDateEnd, DigitalObjects.class, DigitalObjects.PROPERTYNAME_DATE_END);
                                        panel9.add(label_resourcesDateEnd, cc.xy(7, 1));

                                        //---- dateEnd ----
                                        dateEnd.setColumns(4);
                                        panel9.add(dateEnd, cc.xy(9, 1));
                                    }
                                    panel20.add(panel9, cc.xywh(3, 5, 3, 1));
                                }
                                panel15.add(panel20, cc.xy(1, 1));
                            }
                            panel18.add(panel15, cc.xywh(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
                        }
                        panel17.add(panel18, cc.xywh(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));

                        //======== digitalObjectResourceRecordOnly ========
                        {
                            digitalObjectResourceRecordOnly.setOpaque(false);
                            digitalObjectResourceRecordOnly.setLayout(new FormLayout(
                                new ColumnSpec[] {
                                    FormFactory.DEFAULT_COLSPEC,
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    FormFactory.DEFAULT_COLSPEC
                                },
                                new RowSpec[] {
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.LINE_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.LINE_GAP_ROWSPEC,
                                    new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
                                    FormFactory.LINE_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.LINE_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC
                                }));

                            //---- restrictionsApply ----
                            restrictionsApply.setBackground(new Color(231, 188, 251));
                            restrictionsApply.setText("Restrictions Apply");
                            restrictionsApply.setOpaque(false);
                            restrictionsApply.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            restrictionsApply.setText(ATFieldInfo.getLabel(DigitalObjects.class, ArchDescription.PROPERTYNAME_RESTRICTIONS_APPLY));
                            digitalObjectResourceRecordOnly.add(restrictionsApply, cc.xywh(1, 1, 3, 1));

                            //---- label_resourcesLanguageCode3 ----
                            label_resourcesLanguageCode3.setText("Object Type");
                            label_resourcesLanguageCode3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            ATFieldInfo.assignLabelInfo(label_resourcesLanguageCode3, DigitalObjects.class, DigitalObjects.PROPERTYNAME_OBJECT_TYPE);
                            digitalObjectResourceRecordOnly.add(label_resourcesLanguageCode3, cc.xy(1, 3));

                            //---- objectType ----
                            objectType.setMaximumSize(new Dimension(50, 27));
                            objectType.setOpaque(false);
                            objectType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            digitalObjectResourceRecordOnly.add(objectType, cc.xywh(3, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                            //---- actuateLabel2 ----
                            actuateLabel2.setText("Digital Object ID");
                            actuateLabel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            ATFieldInfo.assignLabelInfo(actuateLabel2, DigitalObjects.class, DigitalObjects.PROPERTYNAME_METS_IDENTIFIER);
                            digitalObjectResourceRecordOnly.add(actuateLabel2, cc.xy(1, 5));

                            //======== scrollPane43 ========
                            {
                                scrollPane43.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                                scrollPane43.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                                //---- title2 ----
                                title2.setRows(3);
                                title2.setLineWrap(true);
                                title2.setWrapStyleWord(true);
                                scrollPane43.setViewportView(title2);
                            }
                            digitalObjectResourceRecordOnly.add(scrollPane43, cc.xywh(3, 5, 3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));

                            //---- actuateLabel ----
                            actuateLabel.setText("EAD DAO Actuate");
                            actuateLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            ATFieldInfo.assignLabelInfo(actuateLabel, DigitalObjects.class, DigitalObjects.PROPERTYNAME_EAD_DAO_ACTUATE);
                            digitalObjectResourceRecordOnly.add(actuateLabel, cc.xy(1, 7));

                            //---- actuate ----
                            actuate.setOpaque(false);
                            actuate.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            digitalObjectResourceRecordOnly.add(actuate, cc.xywh(3, 7, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                            //---- showLabel ----
                            showLabel.setText("EAD DAO Show");
                            showLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            ATFieldInfo.assignLabelInfo(showLabel, DigitalObjects.class, DigitalObjects.PROPERTYNAME_EAD_DAO_SHOW);
                            digitalObjectResourceRecordOnly.add(showLabel, cc.xy(1, 9));

                            //---- show ----
                            show.setOpaque(false);
                            show.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            digitalObjectResourceRecordOnly.add(show, cc.xywh(3, 9, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
                        }
                        panel17.add(digitalObjectResourceRecordOnly, cc.xy(1, 3));

                        //======== componentIDPanel ========
                        {
                            componentIDPanel.setBackground(new Color(200, 205, 232));
                            componentIDPanel.setLayout(new FormLayout(
                                new ColumnSpec[] {
                                    FormFactory.DEFAULT_COLSPEC,
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                                },
                                RowSpec.decodeSpecs("default")));

                            //---- componentLabel1 ----
                            componentLabel1.setText("Component ID");
                            componentLabel1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            ATFieldInfo.assignLabelInfo(componentLabel1, DigitalObjects.class, DigitalObjects.PROPERTYNAME_COMPONENT_ID);
                            componentIDPanel.add(componentLabel1, cc.xy(1, 1));
                            componentIDPanel.add(dateExpression2, cc.xy(3, 1));
                        }
                        panel17.add(componentIDPanel, cc.xy(1, 5));
                    }
                    panel13.add(panel17, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
                }
                basicInformationPanel.add(panel13, cc.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

                //======== panel5 ========
                {
                    panel5.setOpaque(false);
                    panel5.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        },
                        new RowSpec[] {
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC
                        }));

                    //---- label_resourcesLanguageCode ----
                    label_resourcesLanguageCode.setText("Language");
                    label_resourcesLanguageCode.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_resourcesLanguageCode, DigitalObjects.class, DigitalObjects.PROPERTYNAME_LANGUAGE_CODE);
                    panel5.add(label_resourcesLanguageCode, cc.xy(1, 1));

                    //---- languageCode ----
                    languageCode.setMaximumSize(new Dimension(50, 27));
                    languageCode.setOpaque(false);
                    languageCode.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel5.add(languageCode, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                    //======== repositoryPanel ========
                    {
                        repositoryPanel.setOpaque(false);
                        repositoryPanel.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                new ColumnSpec(Sizes.dluX(44)),
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec(ColumnSpec.LEFT, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC
                            },
                            RowSpec.decodeSpecs("default")));

                        //---- label_repositoryName ----
                        label_repositoryName.setText("Repository :");
                        label_repositoryName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label_repositoryName, Accessions.class, Accessions.PROPERTYNAME_REPOSITORY);
                        repositoryPanel.add(label_repositoryName, cc.xy(1, 1));

                        //---- repositoryName ----
                        repositoryName.setEditable(false);
                        repositoryName.setFocusable(false);
                        repositoryName.setBorder(null);
                        repositoryName.setOpaque(false);
                        repositoryName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        repositoryName.setHorizontalAlignment(SwingConstants.LEFT);
                        repositoryPanel.add(repositoryName, cc.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

                        //---- changeRepositoryButton ----
                        changeRepositoryButton.setText("Change Repository");
                        changeRepositoryButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        changeRepositoryButton.setOpaque(false);
                        changeRepositoryButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                changeRepositoryButtonActionPerformed();
                            }
                        });
                        repositoryPanel.add(changeRepositoryButton, cc.xy(5, 1));
                    }
                    panel5.add(repositoryPanel, cc.xywh(1, 3, 3, 1));
                }
                basicInformationPanel.add(panel5, cc.xywh(1, 3, 3, 1));

                //======== panel2 ========
                {
                    panel2.setBorder(new BevelBorder(BevelBorder.LOWERED));
                    panel2.setOpaque(false);
                    panel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel2.setLayout(new FormLayout(
                        ColumnSpec.decodeSpecs("default:grow"),
                        new RowSpec[] {
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC
                        }));

                    //---- label1 ----
                    label1.setText("File Versions");
                    label1.setForeground(new Color(0, 0, 102));
                    label1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label1, DigitalObjects.class, DigitalObjects.PROPERTYNAME_FILE_VERSIONS);
                    panel2.add(label1, new CellConstraints(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 5, 5, 0, 0)));

                    //======== scrollPane6 ========
                    {
                        scrollPane6.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                        //---- fileVersionsTable ----
                        fileVersionsTable.setPreferredScrollableViewportSize(new Dimension(200, 75));
                        fileVersionsTable.setRowHeight(20);
                        fileVersionsTable.setFocusable(false);
                        fileVersionsTable.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                fileVersionTableMouseClicked(e);
                            }
                        });
                        scrollPane6.setViewportView(fileVersionsTable);
                    }
                    panel2.add(scrollPane6, new CellConstraints(1, 3, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 10, 0, 5)));

                    //======== panel29 ========
                    {
                        panel29.setBackground(new Color(231, 188, 251));
                        panel29.setOpaque(false);
                        panel29.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel29.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC
                            },
                            RowSpec.decodeSpecs("default")));

                        //---- addFileVersionButton ----
                        addFileVersionButton.setText("Add File Version");
                        addFileVersionButton.setOpaque(false);
                        addFileVersionButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        addFileVersionButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                addFileVersionButtonActionPerformed();
                            }
                        });
                        panel29.add(addFileVersionButton, cc.xy(1, 1));

                        //---- removeFileVersionButton ----
                        removeFileVersionButton.setText("Remove File Version");
                        removeFileVersionButton.setOpaque(false);
                        removeFileVersionButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        removeFileVersionButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                removeFileVersionButtonActionPerformed(e);
                            }
                        });
                        panel29.add(removeFileVersionButton, cc.xy(3, 1));
                    }
                    panel2.add(panel29, cc.xywh(1, 5, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
                }
                basicInformationPanel.add(panel2, cc.xywh(1, 5, 3, 1));
            }
            tabbedPane.addTab("Basic Description", basicInformationPanel);


            //======== namesPanel ========
            {
                namesPanel.setBackground(new Color(200, 205, 232));
                namesPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                namesPanel.setLayout(new FormLayout(
                    ColumnSpec.decodeSpecs("default:grow"),
                    new RowSpec[] {
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC
                    }));
                ((FormLayout)namesPanel.getLayout()).setRowGroups(new int[][] {{3, 11}});

                //---- SubjectsLabel2 ----
                SubjectsLabel2.setText("Names");
                SubjectsLabel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                namesPanel.add(SubjectsLabel2, cc.xy(1, 1));

                //======== scrollPane1 ========
                {
                    scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                    scrollPane1.setPreferredSize(new Dimension(600, 320));
                    scrollPane1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                    //---- namesTable ----
                    namesTable.setPreferredScrollableViewportSize(new Dimension(450, 300));
                    namesTable.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            namesTableMouseClicked(e);
                        }
                    });
                    scrollPane1.setViewportView(namesTable);
                }
                namesPanel.add(scrollPane1, cc.xywh(1, 3, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));

                //======== panel8 ========
                {
                    panel8.setBackground(new Color(231, 188, 251));
                    panel8.setOpaque(false);
                    panel8.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel8.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC
                        },
                        RowSpec.decodeSpecs("default")));

                    //---- editNameRelationshipButton ----
                    editNameRelationshipButton.setText("Edit Name Link");
                    editNameRelationshipButton.setOpaque(false);
                    editNameRelationshipButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    editNameRelationshipButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            editNameRelationshipButtonActionPerformed();
                        }
                    });
                    panel8.add(editNameRelationshipButton, cc.xy(1, 1));

                    //---- addNameRelationshipButton ----
                    addNameRelationshipButton.setBackground(new Color(231, 188, 251));
                    addNameRelationshipButton.setText("Add Name Link");
                    addNameRelationshipButton.setOpaque(false);
                    addNameRelationshipButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    addNameRelationshipButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            addNameRelationshipButtonActionPerformed();
                        }
                    });
                    panel8.add(addNameRelationshipButton, cc.xy(3, 1));

                    //---- removeNameRelationshipButton ----
                    removeNameRelationshipButton.setBackground(new Color(231, 188, 251));
                    removeNameRelationshipButton.setText("Remove Name Link");
                    removeNameRelationshipButton.setOpaque(false);
                    removeNameRelationshipButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    removeNameRelationshipButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            removeNameRelationshipButtonActionPerformed();
                        }
                    });
                    panel8.add(removeNameRelationshipButton, cc.xy(5, 1));
                }
                namesPanel.add(panel8, cc.xywh(1, 5, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

                //---- separator5 ----
                separator5.setBackground(new Color(220, 220, 232));
                separator5.setForeground(new Color(147, 131, 86));
                separator5.setMinimumSize(new Dimension(1, 10));
                separator5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                namesPanel.add(separator5, cc.xy(1, 7));

                //---- SubjectsLabel3 ----
                SubjectsLabel3.setText("Subjects");
                SubjectsLabel3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                namesPanel.add(SubjectsLabel3, cc.xy(1, 9));

                //======== scrollPane3 ========
                {
                    scrollPane3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                    scrollPane3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                    //---- subjectsTable ----
                    subjectsTable.setPreferredScrollableViewportSize(new Dimension(200, 200));
                    scrollPane3.setViewportView(subjectsTable);
                }
                namesPanel.add(scrollPane3, cc.xy(1, 11));

                //======== panel11 ========
                {
                    panel11.setBackground(new Color(231, 188, 251));
                    panel11.setOpaque(false);
                    panel11.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel11.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC
                        },
                        RowSpec.decodeSpecs("default")));

                    //---- addSubjectRelationshipButton ----
                    addSubjectRelationshipButton.setBackground(new Color(231, 188, 251));
                    addSubjectRelationshipButton.setText("Add Subject Link");
                    addSubjectRelationshipButton.setOpaque(false);
                    addSubjectRelationshipButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    addSubjectRelationshipButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            addSubjectRelationshipButtonActionPerformed();
                        }
                    });
                    panel11.add(addSubjectRelationshipButton, cc.xy(1, 1));

                    //---- removeSubjectRelationshipButton ----
                    removeSubjectRelationshipButton.setBackground(new Color(231, 188, 251));
                    removeSubjectRelationshipButton.setText("Remove Subject Link");
                    removeSubjectRelationshipButton.setOpaque(false);
                    removeSubjectRelationshipButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    removeSubjectRelationshipButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            removeSubjectRelationshipButtonActionPerformed();
                        }
                    });
                    panel11.add(removeSubjectRelationshipButton, cc.xy(3, 1));
                }
                namesPanel.add(panel11, cc.xywh(1, 13, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
            }
            tabbedPane.addTab("Names & Subjects", namesPanel);


            //======== panel6 ========
            {
                panel6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                panel6.setBackground(new Color(200, 205, 232));
                panel6.setLayout(new FormLayout(
                    ColumnSpec.decodeSpecs("default:grow"),
                    new RowSpec[] {
                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC
                    }));

                //======== scrollPane5 ========
                {
                    scrollPane5.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                    scrollPane5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                    //---- notesTable ----
                    notesTable.setFocusable(false);
                    notesTable.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            notesTableMouseClicked(e);
                        }
                    });
                    scrollPane5.setViewportView(notesTable);
                }
                panel6.add(scrollPane5, cc.xy(1, 1));

                //======== panel14 ========
                {
                    panel14.setBackground(new Color(231, 188, 251));
                    panel14.setOpaque(false);
                    panel14.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel14.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC
                        },
                        RowSpec.decodeSpecs("default")));

                    //---- addNoteEtcComboBox ----
                    addNoteEtcComboBox.setOpaque(false);
                    addNoteEtcComboBox.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            addNoteEtcComboBoxActionPerformed(e);
                        }
                    });
                    panel14.add(addNoteEtcComboBox, cc.xy(1, 1));

                    //---- removeNoteButton ----
                    removeNoteButton.setBackground(new Color(231, 188, 251));
                    removeNoteButton.setText("Remove Note");
                    removeNoteButton.setOpaque(false);
                    removeNoteButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    removeNoteButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            removeNoteButtonActionPerformed(e);
                        }
                    });
                    panel14.add(removeNoteButton, cc.xy(3, 1));
                }
                panel6.add(panel14, cc.xywh(1, 3, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
            }
            tabbedPane.addTab("Notes", panel6);

        }
        add(tabbedPane, cc.xywh(1, 2, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
//        instancesTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    }


    public JButton getRemoveFileVersionButton() {
    	return removeFileVersionButton;
    }

    private void removeFileVersionButtonActionPerformed(ActionEvent e) {
        removeFileVersion();
    }

    private void removeFileVersion() {
		try {
			this.removeRelatedTableRow(fileVersionsTable, (ArchDescription)super.getModel());
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("File version not removed", e).showDialog();
		}
	}

    private void fileVersionTableMouseClicked(MouseEvent e) {
		handleTableMouseClick(e, fileVersionsTable, FileVersions.class);
    }

    public DomainSortableTable getFileVersionsTable() {
    	return fileVersionsTable;
    }

	public DomainSortedTable getRepeatingDataTable() {
		return notesTable;
	}

	public JComboBox getAddNoteEtcComboBox() {
		return addNoteEtcComboBox;
	}

	public JButton getRemoveNoteButton() {
    	return removeNoteButton;
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel panel3;
    private JLabel label2;
    private JTextField digitalObjectTitle;
    private JTabbedPane tabbedPane;
    private JPanel basicInformationPanel;
    private JPanel panel16;
    private JPanel panel1;
    private JLabel label_resourcesLanguageCode4;
    public JTextField label;
    private JPanel panel19;
    private JLabel label_resourcesTitle;
    private JScrollPane scrollPane42;
    public JTextArea title;
    private JPanel resourcesPanel;
    private JLabel resourcesLabel;
    private JScrollPane scrollPane4;
    private DomainSortableTable resourcesTable;
    private JPanel panel13;
    private JPanel panel17;
    private JPanel panel18;
    private JPanel panel15;
    private JPanel panel20;
    private JLabel ExtentNumberLabel3;
    private JLabel label_resourcesDateExpression;
    public JTextField dateExpression;
    private JPanel panel9;
    private JLabel Date1Label;
    private JLabel label_resourcesDateBegin;
    public JFormattedTextField dateBegin;
    private JLabel label_resourcesDateEnd;
    public JFormattedTextField dateEnd;
    private JPanel digitalObjectResourceRecordOnly;
    public JCheckBox restrictionsApply;
    private JLabel label_resourcesLanguageCode3;
    public JComboBox objectType;
    private JLabel actuateLabel2;
    private JScrollPane scrollPane43;
    public JTextArea title2;
    private JLabel actuateLabel;
    public JComboBox actuate;
    private JLabel showLabel;
    public JComboBox show;
    private JPanel componentIDPanel;
    private JLabel componentLabel1;
    public JTextField dateExpression2;
    private JPanel panel5;
    private JLabel label_resourcesLanguageCode;
    public JComboBox languageCode;
    private JPanel repositoryPanel;
    private JLabel label_repositoryName;
    public JTextField repositoryName;
    private JButton changeRepositoryButton;
    private JPanel panel2;
    private JLabel label1;
    private JScrollPane scrollPane6;
    private DomainSortableTable fileVersionsTable;
    private JPanel panel29;
    private JButton addFileVersionButton;
    private JButton removeFileVersionButton;
    private JPanel namesPanel;
    private JLabel SubjectsLabel2;
    private JScrollPane scrollPane1;
    private DomainSortableTable namesTable;
    private JPanel panel8;
    private JButton editNameRelationshipButton;
    private JButton addNameRelationshipButton;
    private JButton removeNameRelationshipButton;
    private JSeparator separator5;
    private JLabel SubjectsLabel3;
    private JScrollPane scrollPane3;
    private DomainSortableTable subjectsTable;
    private JPanel panel11;
    private JButton addSubjectRelationshipButton;
    private JButton removeSubjectRelationshipButton;
    private JPanel panel6;
    private JScrollPane scrollPane5;
    private DomainSortedTable notesTable;
    private JPanel panel14;
    private JComboBox addNoteEtcComboBox;
    private JButton removeNoteButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    protected JPopupMenu insertFileVersionPopUpMenu = new JPopupMenu();

    protected ActionListener menuDeleteListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            removeFileVersion();
        }
    };
    protected DomainRelatedTableModel tableModelFileVersions;
    protected SortableTableModel sortableTableModelFileVersions = null;
	protected DomainEditor dialogFileVersions;
	private DigitalObjects digitalObjectModel;
	private int selectedRowFileVersions;
	private FileVersions currentFileVersion;

	public final void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
        if (model.isNewRecord()) {
            DefaultValues.assignDefaultValues(model);
        }

        super.setModel(model, progressPanel);
        digitalObjectModel = (DigitalObjects) super.getModel();
		fileVersionsTable.updateCollection(digitalObjectModel.getFileVersions());

        //suppress d.o. resource record only fields for components
		if (digitalObjectModel.getParent() != null) {
			digitalObjectResourceRecordOnly.setVisible(false);
            repositoryPanel.setVisible(false);
            componentIDPanel.setVisible(true);
            resourcesPanel.setVisible(false);
		} else { // this is a parent digital object
			digitalObjectResourceRecordOnly.setVisible(true);
            componentIDPanel.setVisible(false);
            resourcesPanel.setVisible(true);
            if(ApplicationFrame.getInstance().getCurrentUserAccessClass() == Users.ACCESS_CLASS_SUPERUSER) {
                repositoryPanel.setVisible(true);
                setRepositoryText();
            }

            // display any resource that links this digital object
            displayLinkedResource();
		}

		digitalObjectTitle.setText(digitalObjectModel.getTitle());

        setPluginModel(); // update any plugins with this new domain object
	}

	public Component getInitialFocusComponent() {
		return label;
	}

	public final void commitChanges() {
		//this is a hack to work around the problem that if you are in a formatted
		//text field and click on another node in the tree the value is not committed
		try {
			dateBegin.commitEdit();
			dateEnd.commitEdit();
		} catch (ParseException e) {
			System.out.println("Error committing from do: " + e);
			//just chew up the error and don't worry about it
		}
	}

	public DomainSortableTable getSubjectsTable() {
        return subjectsTable;
    }

    public JButton getAddSubjectRelationshipButton() {
        return addSubjectRelationshipButton;
    }

    public void setAddSubjectRelationshipButton(JButton addSubjectRelationshipButton) {
        this.addSubjectRelationshipButton = addSubjectRelationshipButton;
    }

    public JButton getAddNameRelationshipButton() {
        return addNameRelationshipButton;
    }

    public void setAddNameRelationshipButton(JButton addNameRelationshipButton) {
        this.addNameRelationshipButton = addNameRelationshipButton;
    }

    public DomainSortableTable getNamesTable() {
        return namesTable;
    }

    public JButton getRemoveNameRelationshipButton() {
        return removeNameRelationshipButton;
    }

    public void setRemoveNameRelationshipButton(JButton removeNameRelationshipButton) {
        this.removeNameRelationshipButton = removeNameRelationshipButton;
    }

    public JButton getRemoveSubjectRelationshipButton() {
        return removeSubjectRelationshipButton;
    }

    public void setRemoveSubjectRelationshipButton(JButton removeSubjectRelationshipButton) {
        this.removeSubjectRelationshipButton = removeSubjectRelationshipButton;
    }

    /**
     * Method to add information as to which resources in linked to this digital object
     */
    protected void addRelatedTableInformation() {
		resourcesTable.setClazzAndColumns(DigitalObjectsResources.PROPERTYNAME_RESOURCE_IDENTIFIER,
				DigitalObjectsResources.class,
				DigitalObjectsResources.PROPERTYNAME_RESOURCE_IDENTIFIER,
				DigitalObjectsResources.PROPERTYNAME_RESOURCE_TITLE);
	}

	protected void initAccess() {
		//if user is not at least advanced data entry set the record to read only
		if (!Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_ADVANCED_DATA_ENTRY)) {
			setFormToReadOnly();
			languageCode.setEnabled(false);
			objectType.setEnabled(false);
//			convertComboBoxToNonEnterableTextField(languageCode, DigitalObjects.PROPERTYNAME_LANGUAGE_CODE);
//			convertComboBoxToNonEnterableTextField(objectType, DigitalObjects.PROPERTYNAME_OBJECT_TYPE);
//			convertComboBoxToNonEnterableTextField(unitType, DigitalObjects.PROPERTYNAME_STRUCTURE_TYPE);
			addFileVersionButton.setVisible(false);
			removeFileVersionButton.setVisible(false);
			addNameRelationshipButton.setVisible(false);
			removeNameRelationshipButton.setVisible(false);
			addSubjectRelationshipButton.setVisible(false);
			removeSubjectRelationshipButton.setVisible(false);
			addNoteEtcComboBox.setVisible(false);
			removeNoteButton.setVisible(false);
		}
	}

    /**
     * Method to display any resource linked to this digital objects
     */
    private void displayLinkedResource() {
        // clear out any resource in the table
        resourcesTable.updateCollection(null);

        // now add any resource linked to this digital object
        ArchDescriptionDigitalInstances digitalInstance = digitalObjectModel.getDigitalInstance();
        if(digitalInstance != null) {
            DigitalObjectDAO digitalObjectDAO = new DigitalObjectDAO();
            Resources resource = digitalObjectDAO.findResourceByDigitalObject(digitalInstance);

            if(resource != null) {
                resourcesTable.addDomainObject(new DigitalObjectsResources(resource,digitalObjectModel));
            }
        }
    }

    /**
     * Method that initializes any embedded plugins that would add an editor
     */
    private void initPlugins() {
        plugins = ATPluginFactory.getInstance().getEmbeddedDigitalObjectEditorPlugins();
        if(plugins != null) {
            for(ATPlugin plugin : plugins) {
                plugin.setEditorField(this);
                HashMap pluginPanels = plugin.getEmbeddedPanels();
                for(Object key : pluginPanels.keySet()) {
                    String panelName = (String)key;
                    JPanel pluginPanel = (JPanel)pluginPanels.get(key);

                    // see where to add the tab and if to remove any tabs that are
                    // already there
                    if(panelName.indexOf("::") == -1) { // add it to end
                        tabbedPane.addTab(panelName, pluginPanel);
                    } else { // adding table to specific location in tab
                        // get tab placement information from the name string
                        // panelPlacementInfo[0] = panel name
                        // panelPlacementInfo[1] = panel index
                        // panelPlacementInfo[2] = yes if to replace the panel already there,
                        //                         no to just insert at the index
                        String[] panelPlacementInfo = panelName.split("::");
                        try {
                            int index  = Integer.parseInt(panelPlacementInfo[1]);

                            // see whether to remove the index already there
                            if(panelPlacementInfo[2].equalsIgnoreCase("yes")) {
                                tabbedPane.remove(index);
                            }

                            // now insert the panel at the location specified by the index
                            tabbedPane.insertTab(panelPlacementInfo[0], null, pluginPanel, "", index);

                        } catch(NumberFormatException nfe) {
                            System.out.println("Invalid tab index for the plugin panel named " + panelPlacementInfo[2]);
                        }
                    }
                }
            }
        }
    }

    /**
     * Method set the model for any plugins loaded
     */
    private void setPluginModel() {
        if(plugins != null) {
            for(ATPlugin plugin : plugins) {
                plugin.setModel(getModel(), null);
            }
        }
    }
}
