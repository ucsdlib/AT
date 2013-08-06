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
 * Created by JFormDesigner on Thu Oct 06 10:11:13 EDT 2005
 */

package org.archiviststoolkit.editor;


import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import org.archiviststoolkit.dialog.ATFileChooser;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.exporter.EADExportHandler;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.swing.SelectFromList;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.InLineTags;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.util.InLineTagsUtils;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.plugin.ATPluginFactory;
import org.archiviststoolkit.plugin.ATPlugin;
import ca.odell.glazedlists.swing.AutoCompleteSupport;

public class ResourceFields extends ArchDescriptionFields {
    private ArrayList<ATPlugin> plugins = null; // stores any embedded resource editor plugin

	public ResourceFields() {
		super();
		initComponents();
		repeatingDataTable.setTransferable();
		initNotesEtc();
		addRelatedTableInformation();
		initUndo(resourcesTitle);
		initAccess();
        initPlugins();// initiate any plugins now
	}

	protected void setDisplayToFirstTab() {
		this.tabbedPane.setSelectedIndex(0);
	}

	private void changeRepositoryButtonActionPerformed() {
		Vector repositories = Repositories.getRepositoryList();
		ImageIcon icon = null;
		Repositories currentRepostory = ((Resources) this.getModel()).getRepository();
		Resources model = (Resources) this.getModel();
        SelectFromList dialog = new SelectFromList(this.getParentEditor(), "Select a repository", repositories.toArray());
        dialog.setSelectedValue(currentRepostory);
        if (dialog.showDialog() == JOptionPane.OK_OPTION) {
            model.setRepository((Repositories)dialog.getSelectedValue());
            setRepositoryText(model);
            ApplicationFrame.getInstance().setRecordDirty(); // set the record dirty
        }
	}

	public JButton getChangeRepositoryButton() {
		return changeRepositoryButton;
	}

	private void deaccessionsTableMouseClicked(MouseEvent e) {
		handleTableMouseClick(e, deaccessionsTable, Deaccessions.class);
	}

	private void addDeaccessionsActionPerformed() {
		super.addDeaccessionsActionPerformed(deaccessionsTable);
	}

	private void removeDeaccessionActionPerformed() {
		super.removeDeaccessionActionPerformed(deaccessionsTable);
	}

	public DomainSortableTable getDeaccessionsTable() {
		return deaccessionsTable;
	}

	private void addNoteEtcComboBoxActionPerformed(ActionEvent e) {
		super.addNoteEtcComboBoxAction();
	}

    private void removeNotesEtcButtonActionPerformed() {
        super.removeNotesEtc();
    }

	private void namesTableMouseClicked(MouseEvent e) {
		super.handleNamesTableMouseClick(e);
	}

	private void editNameRelationshipButtonActionPerformed() {
		super.editNameRelationshipActionPerformed();
	}

	private void insertInlineTagActionPerformed() {
		InLineTagsUtils.wrapInTagActionPerformed(insertInlineTag, resourcesTitle, this.getParentEditor());
	}

	private void undoButtonActionPerformed() {
		handleUndoButtonAction();
	}

	private void redoButtonActionPerformed() {
		handleRedoButtonAction();
	}

//	public JButton getUndoButton() {
//		return undoButton;
//	}
//
//	public JButton getRedoButton() {
//		return redoButton;
//	}
//
	private void resourcesLevelActionPerformed() {
		setOtherLevelEnabledDisabled(resourcesLevel, label_otherLevel, resourcesOtherLevel);
	}

	private void addInstanceButtonActionPerformed() {
		super.addInstance();
	}

	private void removeInstanceButtonActionPerformed() {
        ArchDescription archDescriptionModel = (ArchDescription) super.getModel();
		try {
			this.removeRelatedTableRow(getInstancesTable(), null, archDescriptionModel);
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("Instance not removed", e).showDialog();
		}
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

	public JButton getRemoveNotesEtcButton() {
		return removeNotesEtcButton;
	}

	private void instancesTableMouseClicked(MouseEvent e) {
		handleInstanceTableMouseClick(e);
	}

	private void repeatingDataTableMouseClicked(MouseEvent e) {
		handleTableMouseClick(e, repeatingDataTable, ArchDescriptionRepeatingData.class);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        tabbedPane = new JTabbedPane();
        basicInformationPanel = new JPanel();
        panel16 = new JPanel();
        panel19 = new JPanel();
        label_resourcesLevel = new JLabel();
        resourcesLevel = ATBasicComponentFactory.createComboBox(detailsModel, Resources.PROPERTYNAME_LEVEL, Resources.class);
        label_otherLevel = new JLabel();
        resourcesOtherLevel = ATBasicComponentFactory.createTextField(detailsModel.getModel(Resources.PROPERTYNAME_OTHER_LEVEL),false);
        label_resourcesTitle = new JLabel();
        scrollPane2 = new JScrollPane();
        resourcesTitle = ATBasicComponentFactory.createTextArea(detailsModel.getModel(ArchDescription.PROPERTYNAME_TITLE),false);
        tagApplicatorPanel = new JPanel();
        insertInlineTag = ATBasicComponentFactory.createUnboundComboBox(InLineTagsUtils.getInLineTagList(InLineTagsUtils.TITLE));
        panel34 = new JPanel();
        panel35 = new JPanel();
        panel36 = new JPanel();
        label_resourcesDateExpression = new JLabel();
        resourcesDateExpression = ATBasicComponentFactory.createTextField(detailsModel.getModel(ArchDescription.PROPERTYNAME_DATE_EXPRESSION),false);
        Date1Label1 = new JLabel();
        label_resourcesDateBegin = new JLabel();
        resourcesDateBegin = ATBasicComponentFactory.createIntegerField(detailsModel,ArchDescription.PROPERTYNAME_DATE_BEGIN);
        label_resourcesDateEnd = new JLabel();
        resourcesDateEnd = ATBasicComponentFactory.createIntegerField(detailsModel,ArchDescription.PROPERTYNAME_DATE_END);
        BulkDatesLabel = new JLabel();
        label_resourcesBulkDateBegin = new JLabel();
        resourcesBulkDateBegin = ATBasicComponentFactory.createIntegerField(detailsModel,Resources.PROPERTYNAME_BULK_DATE_BEGIN);
        label_resourcesBulkDateEnd = new JLabel();
        resourcesBulkDateEnd = ATBasicComponentFactory.createIntegerField(detailsModel,Resources.PROPERTYNAME_BULK_DATE_END);
        panel1 = new JPanel();
        label_resourcesLanguageCode = new JLabel();
        resourcesLanguageCode = ATBasicComponentFactory.createComboBox(detailsModel, Resources.PROPERTYNAME_LANGUAGE_CODE, Resources.class);
        label_resourcesLanguageNote = new JLabel();
        scrollPane423 = new JScrollPane();
        resourcesLanguageNote = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Resources.PROPERTYNAME_REPOSITORY_PROCESSING_NOTE),false);
        panel6 = new JPanel();
        label_agreementReceived2 = new JLabel();
        repositoryName = new JTextField();
        changeRepositoryButton = new JButton();
        panel13 = new JPanel();
        panel17 = new JPanel();
        panel12 = new JPanel();
        label_resourceIdentifier1 = new JLabel();
        resourceIdentifier1 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Resources.PROPERTYNAME_RESOURCE_IDENTIFIER_1));
        resourceIdentifier2 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Resources.PROPERTYNAME_RESOURCE_IDENTIFIER_2));
        resourceIdentifier3 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Resources.PROPERTYNAME_RESOURCE_IDENTIFIER_3));
        resourceIdentifier4 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Resources.PROPERTYNAME_RESOURCE_IDENTIFIER_4));
        panel42 = new JPanel();
        panel43 = new JPanel();
        OtherAccessionsLabel = new JLabel();
        scrollPane4 = new JScrollPane();
        accessionsTable = new DomainSortableTable();
        panel37 = new JPanel();
        panel20 = new JPanel();
        ExtentLabel = new JLabel();
        panel21 = new JPanel();
        label_resourcesExtentNumber = new JLabel();
        resourcesExtentNumber = ATBasicComponentFactory.createDoubleField(detailsModel,Resources.PROPERTYNAME_EXTENT_NUMBER);
        extentType = ATBasicComponentFactory.createComboBox(detailsModel, Resources.PROPERTYNAME_EXTENT_TYPE, Resources.class);
        label_resourcesExtentDescription = new JLabel();
        scrollPane422 = new JScrollPane();
        containerSummary = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Resources.PROPERTYNAME_CONTAINER_SUMMARY),false);
        panel39 = new JPanel();
        panel40 = new JPanel();
        label1 = new JLabel();
        scrollPane6 = new JScrollPane();
        instancesTable = new DomainSortableTable(ArchDescriptionInstances.class, ArchDescriptionInstances.PROPERTYNAME_INSTANCE_TYPE);
        panel29 = new JPanel();
        addInstanceButton = new JButton();
        removeInstanceButton = new JButton();
        panel2 = new JPanel();
        restrictionsApply = ATBasicComponentFactory.createCheckBox(detailsModel, ArchDescription.PROPERTYNAME_RESTRICTIONS_APPLY, Resources.class);
        separator2 = new JSeparator();
        namesPanel = new JPanel();
        SubjectsLabel2 = new JLabel();
        scrollPane1 = new JScrollPane();
        namesTable = new DomainSortableTable(ArchDescriptionNames.class, ArchDescriptionNames.PROPERTYNAME_SORT_NAME);
        panel8 = new JPanel();
        editNameRelationshipButton = new JButton();
        addNameRelationshipButton = new JButton();
        removeNameRelationshipButton = new JButton();
        separator5 = new JSeparator();
        SubjectsLabel = new JLabel();
        scrollPane3 = new JScrollPane();
        subjectsTable = new DomainSortableTable(ArchDescriptionSubjects.class, ArchDescriptionSubjects.PROPERTYNAME_SUBJECT_TERM);
        panel11 = new JPanel();
        addSubjectRelationshipButton = new JButton();
        removeSubjectRelationshipButton = new JButton();
        notesPanel = new JPanel();
        SubjectsLabel3 = new JLabel();
        scrollPane5 = new JScrollPane();
        repeatingDataTable = new DomainSortedTable(ArchDescriptionRepeatingData.class);
        panel14 = new JPanel();
        addNoteEtcComboBox = new JComboBox();
        removeNotesEtcButton = new JButton();
        separator6 = new JSeparator();
        label_repositoryName3 = new JLabel();
        scrollPane7 = new JScrollPane();
        deaccessionsTable = new DomainSortableTable(Deaccessions.class);
        panel33 = new JPanel();
        addDeaccessions = new JButton();
        removeDeaccession = new JButton();
        basicInformationPanel2 = new JPanel();
        panel30 = new JPanel();
        panel9 = new JPanel();
        label6 = new JLabel();
        eadIdentifier5 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Resources.PROPERTYNAME_EAD_FA_UNIQUE_IDENTIFIER),false);
        label15 = new JLabel();
        eadIdentifier13 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Resources.PROPERTYNAME_EAD_FA_LOCATION),false);
        label7 = new JLabel();
        scrollPane45 = new JScrollPane();
        findingAidTitle = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Resources.PROPERTYNAME_FINDING_AID_TITLE));
        label8 = new JLabel();
        scrollPane46 = new JScrollPane();
        findingAidSubtitle = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Resources.PROPERTYNAME_FINDING_AID_SUBTITLE));
        label2 = new JLabel();
        scrollPane8 = new JScrollPane();
        findingAidFilingTitle = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Resources.PROPERTYNAME_FINDING_AID_FILING_TITLE));
        label9 = new JLabel();
        eadIdentifier10 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Resources.PROPERTYNAME_FINDING_AID_DATE),false);
        label10 = new JLabel();
        scrollPane47 = new JScrollPane();
        author = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Resources.PROPERTYNAME_AUTHOR));
        label11 = new JLabel();
        extentType2 = ATBasicComponentFactory.createComboBox(detailsModel, Resources.PROPERTYNAME_DESCRIPTION_RULES, Resources.class, 30);
        label12 = new JLabel();
        scrollPane48 = new JScrollPane();
        languageOfFindingAid = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Resources.PROPERTYNAME_LANGUAGE_OF_FINDING_AID));
        separator1 = new JSeparator();
        panel41 = new JPanel();
        panel31 = new JPanel();
        label_resourcesTitle2 = new JLabel();
        scrollPane43 = new JScrollPane();
        sponsorNote = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Resources.PROPERTYNAME_SPONSOR_NOTE));
        label13 = new JLabel();
        scrollPane49 = new JScrollPane();
        editionStatement = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Resources.PROPERTYNAME_EDITION_STATEMENT));
        label14 = new JLabel();
        scrollPane50 = new JScrollPane();
        series = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Resources.PROPERTYNAME_SERIES));
        label16 = new JLabel();
        eadIdentifier15 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Resources.PROPERTYNAME_REVISION_DATE),false);
        label17 = new JLabel();
        scrollPane51 = new JScrollPane();
        revisionDescription = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Resources.PROPERTYNAME_REVISION_DESCRIPTION));
        label18 = new JLabel();
        panel3 = new JPanel();
        extentType3 = ATBasicComponentFactory.createComboBox(detailsModel, Resources.PROPERTYNAME_FINDING_AID_STATUS, Resources.class);
        label_resourcesTitle3 = new JLabel();
        scrollPane44 = new JScrollPane();
        sponsorNote2 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Resources.PROPERTYNAME_FINDING_AID_NOTE));
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        setBackground(new Color(200, 205, 232));
        setLayout(new FormLayout(
            "default:grow",
            "fill:default:grow"));

        //======== tabbedPane ========
        {
            tabbedPane.setMinimumSize(new Dimension(635, 408));
            tabbedPane.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            tabbedPane.setPreferredSize(new Dimension(750, 520));
            tabbedPane.setBackground(new Color(200, 205, 232));

            //======== basicInformationPanel ========
            {
                basicInformationPanel.setBackground(new Color(200, 205, 232));
                basicInformationPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                basicInformationPanel.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                    },
                    RowSpec.decodeSpecs("fill:default:grow")));
                ((FormLayout)basicInformationPanel.getLayout()).setColumnGroups(new int[][] {{1, 5}});

                //======== panel16 ========
                {
                    panel16.setOpaque(false);
                    panel16.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel16.setBorder(Borders.DLU2_BORDER);
                    panel16.setLayout(new FormLayout(
                        ColumnSpec.decodeSpecs("default:grow"),
                        new RowSpec[] {
                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC
                        }));

                    //======== panel19 ========
                    {
                        panel19.setOpaque(false);
                        panel19.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel19.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec(ColumnSpec.LEFT, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                            },
                            new RowSpec[] {
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

                        //---- label_resourcesLevel ----
                        label_resourcesLevel.setText("Level");
                        label_resourcesLevel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label_resourcesLevel, Resources.class, Resources.PROPERTYNAME_LEVEL);
                        panel19.add(label_resourcesLevel, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

                        //---- resourcesLevel ----
                        resourcesLevel.setOpaque(false);
                        resourcesLevel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        resourcesLevel.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                resourcesLevelActionPerformed();
                            }
                        });
                        panel19.add(resourcesLevel, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                        //---- label_otherLevel ----
                        label_otherLevel.setText("Other Level");
                        label_otherLevel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label_otherLevel, Resources.class, Resources.PROPERTYNAME_OTHER_LEVEL);
                        panel19.add(label_otherLevel, cc.xy(1, 3));
                        panel19.add(resourcesOtherLevel, new CellConstraints(3, 3, 1, 1, CellConstraints.FILL, CellConstraints.TOP, new Insets( 0, 0, 0, 5)));

                        //---- label_resourcesTitle ----
                        label_resourcesTitle.setText("Title");
                        label_resourcesTitle.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label_resourcesTitle, Resources.class, Resources.PROPERTYNAME_TITLE);
                        panel19.add(label_resourcesTitle, cc.xywh(1, 5, 3, 1));

                        //======== scrollPane2 ========
                        {

                            //---- resourcesTitle ----
                            resourcesTitle.setRows(4);
                            resourcesTitle.setLineWrap(true);
                            resourcesTitle.setWrapStyleWord(true);
                            scrollPane2.setViewportView(resourcesTitle);
                        }
                        panel19.add(scrollPane2, cc.xywh(1, 7, 3, 1));

                        //======== tagApplicatorPanel ========
                        {
                            tagApplicatorPanel.setOpaque(false);
                            tagApplicatorPanel.setLayout(new FormLayout(
                                new ColumnSpec[] {
                                    FormFactory.DEFAULT_COLSPEC,
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    FormFactory.DEFAULT_COLSPEC,
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    FormFactory.DEFAULT_COLSPEC
                                },
                                RowSpec.decodeSpecs("default")));

                            //---- insertInlineTag ----
                            insertInlineTag.setOpaque(false);
                            insertInlineTag.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            insertInlineTag.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    insertInlineTagActionPerformed();
                                }
                            });
                            tagApplicatorPanel.add(insertInlineTag, cc.xy(1, 1));
                        }
                        panel19.add(tagApplicatorPanel, cc.xywh(1, 9, 3, 1));
                    }
                    panel16.add(panel19, cc.xy(1, 1));

                    //======== panel34 ========
                    {
                        panel34.setBorder(new BevelBorder(BevelBorder.LOWERED));
                        panel34.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel34.setBackground(new Color(182, 187, 212));
                        panel34.setLayout(new FormLayout(
                            ColumnSpec.decodeSpecs("default:grow"),
                            new RowSpec[] {
                                new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                FormFactory.RELATED_GAP_ROWSPEC
                            }));

                        //======== panel35 ========
                        {
                            panel35.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            panel35.setOpaque(false);
                            panel35.setBorder(Borders.DLU2_BORDER);
                            panel35.setLayout(new FormLayout(
                                new ColumnSpec[] {
                                    FormFactory.UNRELATED_GAP_COLSPEC,
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    FormFactory.DEFAULT_COLSPEC,
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
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
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.LINE_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.LINE_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC
                                }));

                            //======== panel36 ========
                            {
                                panel36.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                panel36.setOpaque(false);
                                panel36.setLayout(new FormLayout(
                                    new ColumnSpec[] {
                                        new ColumnSpec(ColumnSpec.LEFT, Sizes.PREFERRED, FormSpec.NO_GROW),
                                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                                    },
                                    RowSpec.decodeSpecs("default:grow")));

                                //---- label_resourcesDateExpression ----
                                label_resourcesDateExpression.setText("Date Expression");
                                label_resourcesDateExpression.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                ATFieldInfo.assignLabelInfo(label_resourcesDateExpression, Resources.class, Resources.PROPERTYNAME_DATE_EXPRESSION);
                                panel36.add(label_resourcesDateExpression, cc.xywh(1, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
                                panel36.add(resourcesDateExpression, new CellConstraints(3, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP, new Insets( 0, 0, 0, 5)));
                            }
                            panel35.add(panel36, cc.xywh(1, 1, 9, 1));

                            //---- Date1Label1 ----
                            Date1Label1.setText("Inclusive Dates");
                            Date1Label1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            panel35.add(Date1Label1, new CellConstraints(3, 3, 9, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 5, 0, 0)));

                            //---- label_resourcesDateBegin ----
                            label_resourcesDateBegin.setText("Begin");
                            label_resourcesDateBegin.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            ATFieldInfo.assignLabelInfo(label_resourcesDateBegin, Resources.class, Resources.PROPERTYNAME_DATE_BEGIN);
                            panel35.add(label_resourcesDateBegin, cc.xy(3, 5));

                            //---- resourcesDateBegin ----
                            resourcesDateBegin.setColumns(4);
                            panel35.add(resourcesDateBegin, cc.xywh(5, 5, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

                            //---- label_resourcesDateEnd ----
                            label_resourcesDateEnd.setText("End");
                            label_resourcesDateEnd.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            ATFieldInfo.assignLabelInfo(label_resourcesDateEnd, Resources.class, Resources.PROPERTYNAME_DATE_END);
                            panel35.add(label_resourcesDateEnd, cc.xy(7, 5));

                            //---- resourcesDateEnd ----
                            resourcesDateEnd.setColumns(4);
                            panel35.add(resourcesDateEnd, new CellConstraints(9, 5, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT, new Insets( 0, 0, 0, 5)));

                            //---- BulkDatesLabel ----
                            BulkDatesLabel.setText("Bulk Dates");
                            BulkDatesLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            panel35.add(BulkDatesLabel, new CellConstraints(1, 7, 9, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 5, 0, 0)));

                            //---- label_resourcesBulkDateBegin ----
                            label_resourcesBulkDateBegin.setText("Begin");
                            label_resourcesBulkDateBegin.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            ATFieldInfo.assignLabelInfo(label_resourcesBulkDateBegin, Resources.class, Resources.PROPERTYNAME_BULK_DATE_BEGIN);
                            panel35.add(label_resourcesBulkDateBegin, cc.xy(3, 9));

                            //---- resourcesBulkDateBegin ----
                            resourcesBulkDateBegin.setColumns(4);
                            panel35.add(resourcesBulkDateBegin, cc.xywh(5, 9, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

                            //---- label_resourcesBulkDateEnd ----
                            label_resourcesBulkDateEnd.setText("End");
                            label_resourcesBulkDateEnd.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            ATFieldInfo.assignLabelInfo(label_resourcesBulkDateEnd, Resources.class, Resources.PROPERTYNAME_BULK_DATE_END);
                            panel35.add(label_resourcesBulkDateEnd, cc.xy(7, 9));

                            //---- resourcesBulkDateEnd ----
                            resourcesBulkDateEnd.setColumns(4);
                            panel35.add(resourcesBulkDateEnd, new CellConstraints(9, 9, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT, new Insets( 0, 0, 0, 5)));
                        }
                        panel34.add(panel35, cc.xy(1, 1));
                    }
                    panel16.add(panel34, cc.xy(1, 3));

                    //======== panel1 ========
                    {
                        panel1.setOpaque(false);
                        panel1.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                new ColumnSpec(ColumnSpec.LEFT, Sizes.DEFAULT, FormSpec.NO_GROW),
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec("left:min(default;200px)")
                            },
                            RowSpec.decodeSpecs("default")));

                        //---- label_resourcesLanguageCode ----
                        label_resourcesLanguageCode.setText("Language");
                        label_resourcesLanguageCode.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label_resourcesLanguageCode, Resources.class, Resources.PROPERTYNAME_LANGUAGE_CODE);
                        panel1.add(label_resourcesLanguageCode, cc.xy(1, 1));

                        //---- resourcesLanguageCode ----
                        resourcesLanguageCode.setMaximumSize(new Dimension(50, 27));
                        resourcesLanguageCode.setOpaque(false);
                        resourcesLanguageCode.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel1.add(resourcesLanguageCode, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
                    }
                    panel16.add(panel1, cc.xy(1, 5));

                    //---- label_resourcesLanguageNote ----
                    label_resourcesLanguageNote.setText("Repository Processing Note");
                    label_resourcesLanguageNote.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_resourcesLanguageNote, Resources.class, Resources.PROPERTYNAME_REPOSITORY_PROCESSING_NOTE);
                    panel16.add(label_resourcesLanguageNote, cc.xy(1, 7));

                    //======== scrollPane423 ========
                    {
                        scrollPane423.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane423.setOpaque(false);
                        scrollPane423.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                        //---- resourcesLanguageNote ----
                        resourcesLanguageNote.setRows(4);
                        resourcesLanguageNote.setLineWrap(true);
                        resourcesLanguageNote.setWrapStyleWord(true);
                        scrollPane423.setViewportView(resourcesLanguageNote);
                    }
                    panel16.add(scrollPane423, cc.xy(1, 9));

                    //======== panel6 ========
                    {
                        panel6.setOpaque(false);
                        panel6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel6.setLayout(new FormLayout(
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

                        //---- label_agreementReceived2 ----
                        label_agreementReceived2.setText("Repository");
                        label_agreementReceived2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label_agreementReceived2, Resources.class, Resources.PROPERTYNAME_REPOSITORY);
                        panel6.add(label_agreementReceived2, cc.xy(1, 1));

                        //---- repositoryName ----
                        repositoryName.setEditable(false);
                        repositoryName.setOpaque(false);
                        repositoryName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        repositoryName.setBorder(null);
                        panel6.add(repositoryName, cc.xy(3, 1));

                        //---- changeRepositoryButton ----
                        changeRepositoryButton.setText("Change Repository");
                        changeRepositoryButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        changeRepositoryButton.setOpaque(false);
                        changeRepositoryButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                changeRepositoryButtonActionPerformed();
                            }
                        });
                        panel6.add(changeRepositoryButton, cc.xywh(3, 3, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
                    }
                    panel16.add(panel6, cc.xy(1, 11));
                }
                basicInformationPanel.add(panel16, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

                //======== panel13 ========
                {
                    panel13.setOpaque(false);
                    panel13.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel13.setBorder(Borders.DLU2_BORDER);
                    panel13.setLayout(new FormLayout(
                        "default:grow",
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
                                new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                FormFactory.LINE_GAP_ROWSPEC,
                                new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                FormFactory.LINE_GAP_ROWSPEC,
                                new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC
                            }));

                        //======== panel12 ========
                        {
                            panel12.setBackground(new Color(231, 188, 251));
                            panel12.setOpaque(false);
                            panel12.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            panel12.setLayout(new FormLayout(
                                new ColumnSpec[] {
                                    FormFactory.DEFAULT_COLSPEC,
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                                },
                                RowSpec.decodeSpecs("default")));
                            ((FormLayout)panel12.getLayout()).setColumnGroups(new int[][] {{3, 5, 7, 9}});

                            //---- label_resourceIdentifier1 ----
                            label_resourceIdentifier1.setText("Resource ID");
                            label_resourceIdentifier1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            ATFieldInfo.assignLabelInfo(label_resourceIdentifier1, Resources.class, Resources.PROPERTYNAME_RESOURCE_IDENTIFIER);
                            panel12.add(label_resourceIdentifier1, cc.xy(1, 1));
                            panel12.add(resourceIdentifier1, cc.xy(3, 1));
                            panel12.add(resourceIdentifier2, cc.xy(5, 1));
                            panel12.add(resourceIdentifier3, cc.xy(7, 1));
                            panel12.add(resourceIdentifier4, cc.xy(9, 1));
                        }
                        panel17.add(panel12, cc.xy(1, 1));

                        //======== panel42 ========
                        {
                            panel42.setBorder(new BevelBorder(BevelBorder.LOWERED));
                            panel42.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            panel42.setBackground(new Color(182, 187, 212));
                            panel42.setLayout(new FormLayout(
                                ColumnSpec.decodeSpecs("default:grow"),
                                new RowSpec[] {
                                    new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                    FormFactory.RELATED_GAP_ROWSPEC
                                }));

                            //======== panel43 ========
                            {
                                panel43.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                panel43.setOpaque(false);
                                panel43.setBorder(Borders.DLU2_BORDER);
                                panel43.setLayout(new FormLayout(
                                    ColumnSpec.decodeSpecs("default:grow"),
                                    new RowSpec[] {
                                        FormFactory.DEFAULT_ROWSPEC,
                                        FormFactory.LINE_GAP_ROWSPEC,
                                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                                    }));

                                //---- OtherAccessionsLabel ----
                                OtherAccessionsLabel.setText("Accessions linked to this Resource ID:");
                                OtherAccessionsLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                panel43.add(OtherAccessionsLabel, cc.xy(1, 1));

                                //======== scrollPane4 ========
                                {
                                    scrollPane4.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                                    scrollPane4.setPreferredSize(new Dimension(300, 100));

                                    //---- accessionsTable ----
                                    accessionsTable.setPreferredScrollableViewportSize(new Dimension(300, 100));
                                    accessionsTable.setFocusable(false);
                                    scrollPane4.setViewportView(accessionsTable);
                                }
                                panel43.add(scrollPane4, cc.xywh(1, 3, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
                            }
                            panel42.add(panel43, cc.xy(1, 1));
                        }
                        panel17.add(panel42, cc.xy(1, 3));

                        //======== panel37 ========
                        {
                            panel37.setBorder(new BevelBorder(BevelBorder.LOWERED));
                            panel37.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            panel37.setBackground(new Color(182, 187, 212));
                            panel37.setLayout(new FormLayout(
                                "60px:grow",
                                "fill:default:grow"));

                            //======== panel20 ========
                            {
                                panel20.setOpaque(false);
                                panel20.setBorder(Borders.DLU2_BORDER);
                                panel20.setLayout(new FormLayout(
                                    new ColumnSpec[] {
                                        FormFactory.UNRELATED_GAP_COLSPEC,
                                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                                    },
                                    new RowSpec[] {
                                        FormFactory.DEFAULT_ROWSPEC,
                                        FormFactory.LINE_GAP_ROWSPEC,
                                        FormFactory.DEFAULT_ROWSPEC,
                                        FormFactory.LINE_GAP_ROWSPEC,
                                        FormFactory.DEFAULT_ROWSPEC,
                                        FormFactory.LINE_GAP_ROWSPEC,
                                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                                    }));

                                //---- ExtentLabel ----
                                ExtentLabel.setText("Extent");
                                ExtentLabel.setForeground(new Color(0, 0, 102));
                                ExtentLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                panel20.add(ExtentLabel, cc.xywh(1, 1, 2, 1));

                                //======== panel21 ========
                                {
                                    panel21.setOpaque(false);
                                    panel21.setLayout(new FormLayout(
                                        new ColumnSpec[] {
                                            FormFactory.DEFAULT_COLSPEC,
                                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                            FormFactory.DEFAULT_COLSPEC,
                                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                            FormFactory.DEFAULT_COLSPEC
                                        },
                                        RowSpec.decodeSpecs("default")));

                                    //---- label_resourcesExtentNumber ----
                                    label_resourcesExtentNumber.setText("Extent");
                                    label_resourcesExtentNumber.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                    ATFieldInfo.assignLabelInfo(label_resourcesExtentNumber, Resources.class, Resources.PROPERTYNAME_EXTENT_NUMBER);
                                    panel21.add(label_resourcesExtentNumber, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

                                    //---- resourcesExtentNumber ----
                                    resourcesExtentNumber.setColumns(4);
                                    panel21.add(resourcesExtentNumber, cc.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

                                    //---- extentType ----
                                    extentType.setOpaque(false);
                                    panel21.add(extentType, new CellConstraints(5, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT, new Insets( 0, 5, 5, 5)));
                                }
                                panel20.add(panel21, cc.xy(2, 3));

                                //---- label_resourcesExtentDescription ----
                                label_resourcesExtentDescription.setText("Container Summary");
                                label_resourcesExtentDescription.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                ATFieldInfo.assignLabelInfo(label_resourcesExtentDescription, Resources.class, Resources.PROPERTYNAME_CONTAINER_SUMMARY);
                                panel20.add(label_resourcesExtentDescription, cc.xy(2, 5));

                                //======== scrollPane422 ========
                                {
                                    scrollPane422.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                                    scrollPane422.setOpaque(false);
                                    scrollPane422.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                                    //---- containerSummary ----
                                    containerSummary.setRows(4);
                                    containerSummary.setWrapStyleWord(true);
                                    containerSummary.setLineWrap(true);
                                    scrollPane422.setViewportView(containerSummary);
                                }
                                panel20.add(scrollPane422, new CellConstraints(1, 7, 2, 1, CellConstraints.DEFAULT, CellConstraints.FILL, new Insets( 0, 15, 5, 5)));
                            }
                            panel37.add(panel20, cc.xywh(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
                        }
                        panel17.add(panel37, cc.xywh(1, 5, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));

                        //======== panel39 ========
                        {
                            panel39.setBorder(new BevelBorder(BevelBorder.LOWERED));
                            panel39.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            panel39.setBackground(new Color(182, 187, 212));
                            panel39.setLayout(new FormLayout(
                                ColumnSpec.decodeSpecs("default:grow"),
                                new RowSpec[] {
                                    new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                    FormFactory.RELATED_GAP_ROWSPEC
                                }));

                            //======== panel40 ========
                            {
                                panel40.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                panel40.setOpaque(false);
                                panel40.setBorder(Borders.DLU2_BORDER);
                                panel40.setLayout(new FormLayout(
                                    ColumnSpec.decodeSpecs("default:grow"),
                                    new RowSpec[] {
                                        FormFactory.DEFAULT_ROWSPEC,
                                        FormFactory.LINE_GAP_ROWSPEC,
                                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                        FormFactory.LINE_GAP_ROWSPEC,
                                        FormFactory.DEFAULT_ROWSPEC
                                    }));

                                //---- label1 ----
                                label1.setText("Instances");
                                label1.setForeground(new Color(0, 0, 102));
                                label1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                ATFieldInfo.assignLabelInfo(label1, Resources.class, ResourcesComponents.PROPERTYNAME_INSTANCES);
                                panel40.add(label1, cc.xy(1, 1));

                                //======== scrollPane6 ========
                                {
                                    scrollPane6.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                                    scrollPane6.setOpaque(false);

                                    //---- instancesTable ----
                                    instancesTable.setPreferredScrollableViewportSize(new Dimension(200, 75));
                                    instancesTable.setRowHeight(20);
                                    instancesTable.setFocusable(false);
                                    instancesTable.addMouseListener(new MouseAdapter() {
                                        @Override
                                        public void mouseClicked(MouseEvent e) {
                                            instancesTableMouseClicked(e);
                                        }
                                    });
                                    scrollPane6.setViewportView(instancesTable);
                                }
                                panel40.add(scrollPane6, cc.xywh(1, 3, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));

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

                                    //---- addInstanceButton ----
                                    addInstanceButton.setText("Add Instance");
                                    addInstanceButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                    addInstanceButton.setOpaque(false);
                                    addInstanceButton.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            addInstanceButtonActionPerformed();
                                        }
                                    });
                                    panel29.add(addInstanceButton, cc.xy(1, 1));

                                    //---- removeInstanceButton ----
                                    removeInstanceButton.setText("Remove Instance");
                                    removeInstanceButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                    removeInstanceButton.setOpaque(false);
                                    removeInstanceButton.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            removeInstanceButtonActionPerformed();
                                        }
                                    });
                                    panel29.add(removeInstanceButton, cc.xy(3, 1));
                                }
                                panel40.add(panel29, cc.xywh(1, 5, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
                            }
                            panel39.add(panel40, cc.xy(1, 1));
                        }
                        panel17.add(panel39, cc.xywh(1, 7, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));

                        //======== panel2 ========
                        {
                            panel2.setOpaque(false);
                            panel2.setLayout(new FormLayout(
                                "default",
                                "default"));

                            //---- restrictionsApply ----
                            restrictionsApply.setText("Restrictions Apply");
                            restrictionsApply.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            restrictionsApply.setOpaque(false);
                            restrictionsApply.setText(ATFieldInfo.getLabel(Resources.class, ArchDescription.PROPERTYNAME_RESTRICTIONS_APPLY));
                            panel2.add(restrictionsApply, cc.xy(1, 1));
                        }
                        panel17.add(panel2, cc.xy(1, 9));
                    }
                    panel13.add(panel17, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
                }
                basicInformationPanel.add(panel13, cc.xywh(5, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

                //---- separator2 ----
                separator2.setForeground(new Color(147, 131, 86));
                separator2.setOrientation(SwingConstants.VERTICAL);
                basicInformationPanel.add(separator2, cc.xywh(3, 1, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
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
                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
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

                    //---- namesTable ----
                    namesTable.setFocusable(false);
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

                //---- SubjectsLabel ----
                SubjectsLabel.setText("Subjects");
                SubjectsLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                namesPanel.add(SubjectsLabel, cc.xy(1, 9));

                //======== scrollPane3 ========
                {
                    scrollPane3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                    //---- subjectsTable ----
                    subjectsTable.setFocusable(false);
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


            //======== notesPanel ========
            {
                notesPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                notesPanel.setBackground(new Color(200, 205, 232));
                notesPanel.setLayout(new FormLayout(
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
                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC
                    }));

                //---- SubjectsLabel3 ----
                SubjectsLabel3.setText("Notes etc.");
                SubjectsLabel3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                notesPanel.add(SubjectsLabel3, cc.xy(1, 1));

                //======== scrollPane5 ========
                {
                    scrollPane5.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                    scrollPane5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                    //---- repeatingDataTable ----
                    repeatingDataTable.setFocusable(false);
                    repeatingDataTable.setDragEnabled(true);
                    repeatingDataTable.setPreferredScrollableViewportSize(new Dimension(450, 200));
                    repeatingDataTable.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            repeatingDataTableMouseClicked(e);
                        }
                    });
                    scrollPane5.setViewportView(repeatingDataTable);
                }
                notesPanel.add(scrollPane5, cc.xy(1, 3));

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
                        RowSpec.decodeSpecs("default:grow")));

                    //---- addNoteEtcComboBox ----
                    addNoteEtcComboBox.setOpaque(false);
                    addNoteEtcComboBox.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            addNoteEtcComboBoxActionPerformed(e);
                        }
                    });
                    panel14.add(addNoteEtcComboBox, cc.xy(1, 1));

                    //---- removeNotesEtcButton ----
                    removeNotesEtcButton.setText("Remove Note etc.");
                    removeNotesEtcButton.setOpaque(false);
                    removeNotesEtcButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    removeNotesEtcButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            removeNotesEtcButtonActionPerformed();
                        }
                    });
                    panel14.add(removeNotesEtcButton, cc.xy(3, 1));
                }
                notesPanel.add(panel14, cc.xywh(1, 5, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

                //---- separator6 ----
                separator6.setBackground(new Color(220, 220, 232));
                separator6.setForeground(new Color(147, 131, 86));
                separator6.setMinimumSize(new Dimension(1, 10));
                separator6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                notesPanel.add(separator6, cc.xy(1, 7));

                //---- label_repositoryName3 ----
                label_repositoryName3.setText("Deaccessions");
                label_repositoryName3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                notesPanel.add(label_repositoryName3, cc.xy(1, 9));

                //======== scrollPane7 ========
                {
                    scrollPane7.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                    scrollPane7.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                    //---- deaccessionsTable ----
                    deaccessionsTable.setPreferredScrollableViewportSize(new Dimension(450, 100));
                    deaccessionsTable.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            deaccessionsTableMouseClicked(e);
                        }
                    });
                    scrollPane7.setViewportView(deaccessionsTable);
                }
                notesPanel.add(scrollPane7, cc.xy(1, 11));

                //======== panel33 ========
                {
                    panel33.setBackground(new Color(231, 188, 251));
                    panel33.setOpaque(false);
                    panel33.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel33.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC
                        },
                        RowSpec.decodeSpecs("default")));

                    //---- addDeaccessions ----
                    addDeaccessions.setBackground(new Color(231, 188, 251));
                    addDeaccessions.setText("Add Deaccession");
                    addDeaccessions.setOpaque(false);
                    addDeaccessions.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    addDeaccessions.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            addDeaccessionsActionPerformed();
                        }
                    });
                    panel33.add(addDeaccessions, cc.xy(1, 1));

                    //---- removeDeaccession ----
                    removeDeaccession.setBackground(new Color(231, 188, 251));
                    removeDeaccession.setText("Remove Deaccession");
                    removeDeaccession.setOpaque(false);
                    removeDeaccession.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    removeDeaccession.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            removeDeaccessionActionPerformed();
                        }
                    });
                    panel33.add(removeDeaccession, cc.xy(3, 1));
                }
                notesPanel.add(panel33, cc.xywh(1, 13, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
            }
            tabbedPane.addTab("Notes Etc. & Deaccessions", notesPanel);


            //======== basicInformationPanel2 ========
            {
                basicInformationPanel2.setBackground(new Color(200, 205, 232));
                basicInformationPanel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                basicInformationPanel2.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, 0.5),
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        new ColumnSpec("left:max(default;300px):grow(0.5)")
                    },
                    RowSpec.decodeSpecs("fill:default:grow")));
                ((FormLayout)basicInformationPanel2.getLayout()).setColumnGroups(new int[][] {{1, 5}});

                //======== panel30 ========
                {
                    panel30.setOpaque(false);
                    panel30.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel30.setLayout(new FormLayout(
                        "default:grow",
                        "fill:default:grow"));

                    //======== panel9 ========
                    {
                        panel9.setOpaque(false);
                        panel9.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel9.setBorder(Borders.DLU2_BORDER);
                        panel9.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.PREF_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                            },
                            new RowSpec[] {
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                FormFactory.LINE_GAP_ROWSPEC,
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

                        //---- label6 ----
                        label6.setText("EAD FA Unique Identifier");
                        label6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label6, Resources.class, Resources.PROPERTYNAME_EAD_FA_UNIQUE_IDENTIFIER);
                        panel9.add(label6, cc.xywh(1, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
                        panel9.add(eadIdentifier5, cc.xywh(3, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

                        //---- label15 ----
                        label15.setText("EAD Location");
                        label15.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label15, Resources.class, Resources.PROPERTYNAME_EAD_FA_LOCATION);
                        panel9.add(label15, cc.xywh(1, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
                        panel9.add(eadIdentifier13, cc.xywh(3, 3, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

                        //---- label7 ----
                        label7.setText("Finding Aid Title");
                        label7.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label7, Resources.class, Resources.PROPERTYNAME_FINDING_AID_TITLE);
                        panel9.add(label7, cc.xywh(1, 5, 3, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                        //======== scrollPane45 ========
                        {
                            scrollPane45.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                            scrollPane45.setPreferredSize(new Dimension(300, 68));
                            scrollPane45.setOpaque(false);
                            scrollPane45.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                            //---- findingAidTitle ----
                            findingAidTitle.setRows(4);
                            findingAidTitle.setLineWrap(true);
                            findingAidTitle.setWrapStyleWord(true);
                            findingAidTitle.setMinimumSize(new Dimension(200, 16));
                            scrollPane45.setViewportView(findingAidTitle);
                        }
                        panel9.add(scrollPane45, cc.xywh(1, 7, 3, 1));

                        //---- label8 ----
                        label8.setText("Finding Aid Subtitle");
                        label8.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label8, Resources.class, Resources.PROPERTYNAME_FINDING_AID_SUBTITLE);
                        panel9.add(label8, cc.xywh(1, 9, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                        //======== scrollPane46 ========
                        {
                            scrollPane46.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                            scrollPane46.setPreferredSize(new Dimension(300, 68));
                            scrollPane46.setOpaque(false);
                            scrollPane46.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                            //---- findingAidSubtitle ----
                            findingAidSubtitle.setRows(4);
                            findingAidSubtitle.setLineWrap(true);
                            findingAidSubtitle.setWrapStyleWord(true);
                            findingAidSubtitle.setMinimumSize(new Dimension(200, 16));
                            scrollPane46.setViewportView(findingAidSubtitle);
                        }
                        panel9.add(scrollPane46, cc.xywh(1, 11, 3, 1));

                        //---- label2 ----
                        label2.setText("Finding Aid Filing Title");
                        label2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label2, Resources.class, Resources.PROPERTYNAME_FINDING_AID_FILING_TITLE);
                        panel9.add(label2, cc.xy(1, 13));

                        //======== scrollPane8 ========
                        {

                            //---- findingAidFilingTitle ----
                            findingAidFilingTitle.setRows(4);
                            findingAidFilingTitle.setLineWrap(true);
                            findingAidFilingTitle.setWrapStyleWord(true);
                            scrollPane8.setViewportView(findingAidFilingTitle);
                        }
                        panel9.add(scrollPane8, cc.xywh(1, 15, 3, 1));

                        //---- label9 ----
                        label9.setText("Finding Aid Date");
                        label9.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label9, Resources.class, Resources.PROPERTYNAME_FINDING_AID_DATE);
                        panel9.add(label9, cc.xywh(1, 17, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
                        panel9.add(eadIdentifier10, cc.xywh(3, 17, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

                        //---- label10 ----
                        label10.setText("Author");
                        label10.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label10, Resources.class, Resources.PROPERTYNAME_AUTHOR);
                        panel9.add(label10, cc.xywh(1, 19, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                        //======== scrollPane47 ========
                        {
                            scrollPane47.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                            scrollPane47.setPreferredSize(new Dimension(300, 68));
                            scrollPane47.setOpaque(false);
                            scrollPane47.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                            //---- author ----
                            author.setRows(4);
                            author.setLineWrap(true);
                            author.setWrapStyleWord(true);
                            author.setMinimumSize(new Dimension(200, 16));
                            scrollPane47.setViewportView(author);
                        }
                        panel9.add(scrollPane47, cc.xywh(1, 21, 3, 1));

                        //---- label11 ----
                        label11.setText("Description Rules");
                        label11.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label11, Resources.class, Resources.PROPERTYNAME_DESCRIPTION_RULES);
                        panel9.add(label11, cc.xywh(1, 23, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                        //---- extentType2 ----
                        extentType2.setOpaque(false);
                        extentType2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel9.add(extentType2, cc.xywh(1, 25, 3, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                        //---- label12 ----
                        label12.setText("Language of Finding Aid");
                        label12.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label12, Resources.class, Resources.PROPERTYNAME_LANGUAGE_OF_FINDING_AID);
                        panel9.add(label12, cc.xywh(1, 27, 3, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                        //======== scrollPane48 ========
                        {
                            scrollPane48.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                            scrollPane48.setPreferredSize(new Dimension(300, 68));
                            scrollPane48.setOpaque(false);
                            scrollPane48.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                            //---- languageOfFindingAid ----
                            languageOfFindingAid.setRows(4);
                            languageOfFindingAid.setLineWrap(true);
                            languageOfFindingAid.setWrapStyleWord(true);
                            languageOfFindingAid.setMinimumSize(new Dimension(200, 16));
                            scrollPane48.setViewportView(languageOfFindingAid);
                        }
                        panel9.add(scrollPane48, cc.xywh(1, 29, 3, 1));
                    }
                    panel30.add(panel9, cc.xy(1, 1));
                }
                basicInformationPanel2.add(panel30, cc.xy(1, 1));

                //---- separator1 ----
                separator1.setForeground(new Color(147, 131, 86));
                separator1.setOrientation(SwingConstants.VERTICAL);
                basicInformationPanel2.add(separator1, cc.xywh(3, 1, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

                //======== panel41 ========
                {
                    panel41.setOpaque(false);
                    panel41.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel41.setBorder(Borders.DLU2_BORDER);
                    panel41.setLayout(new FormLayout(
                        "left:default:grow",
                        "fill:default:grow"));

                    //======== panel31 ========
                    {
                        panel31.setOpaque(false);
                        panel31.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel31.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.PREF_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                            },
                            new RowSpec[] {
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                new RowSpec(RowSpec.BOTTOM, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                            }));

                        //---- label_resourcesTitle2 ----
                        label_resourcesTitle2.setText("Sponsor Note");
                        label_resourcesTitle2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label_resourcesTitle2, Resources.class, Resources.PROPERTYNAME_SPONSOR_NOTE);
                        panel31.add(label_resourcesTitle2, cc.xy(1, 1));

                        //======== scrollPane43 ========
                        {
                            scrollPane43.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                            scrollPane43.setPreferredSize(new Dimension(300, 68));
                            scrollPane43.setOpaque(false);
                            scrollPane43.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                            //---- sponsorNote ----
                            sponsorNote.setRows(4);
                            sponsorNote.setLineWrap(true);
                            sponsorNote.setWrapStyleWord(true);
                            sponsorNote.setMinimumSize(new Dimension(200, 16));
                            scrollPane43.setViewportView(sponsorNote);
                        }
                        panel31.add(scrollPane43, cc.xywh(1, 3, 3, 1));

                        //---- label13 ----
                        label13.setText("Edition Statement");
                        label13.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label13, Resources.class, Resources.PROPERTYNAME_EDITION_STATEMENT);
                        panel31.add(label13, cc.xywh(1, 5, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                        //======== scrollPane49 ========
                        {
                            scrollPane49.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                            scrollPane49.setPreferredSize(new Dimension(300, 68));
                            scrollPane49.setOpaque(false);
                            scrollPane49.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                            //---- editionStatement ----
                            editionStatement.setRows(4);
                            editionStatement.setLineWrap(true);
                            editionStatement.setWrapStyleWord(true);
                            editionStatement.setMinimumSize(new Dimension(200, 16));
                            scrollPane49.setViewportView(editionStatement);
                        }
                        panel31.add(scrollPane49, cc.xywh(1, 7, 3, 1));

                        //---- label14 ----
                        label14.setText("Series");
                        label14.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label14, Resources.class, Resources.PROPERTYNAME_SERIES);
                        panel31.add(label14, cc.xywh(1, 9, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                        //======== scrollPane50 ========
                        {
                            scrollPane50.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                            scrollPane50.setPreferredSize(new Dimension(300, 68));
                            scrollPane50.setOpaque(false);
                            scrollPane50.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                            //---- series ----
                            series.setRows(4);
                            series.setLineWrap(true);
                            series.setWrapStyleWord(true);
                            series.setMinimumSize(new Dimension(200, 16));
                            scrollPane50.setViewportView(series);
                        }
                        panel31.add(scrollPane50, cc.xywh(1, 11, 3, 1));

                        //---- label16 ----
                        label16.setText("Revision Date");
                        label16.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label16, Resources.class, Resources.PROPERTYNAME_REVISION_DATE);
                        panel31.add(label16, cc.xywh(1, 13, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
                        panel31.add(eadIdentifier15, cc.xywh(3, 13, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

                        //---- label17 ----
                        label17.setText("Revision Description");
                        label17.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label17, Resources.class, Resources.PROPERTYNAME_REVISION_DESCRIPTION);
                        panel31.add(label17, cc.xywh(1, 15, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                        //======== scrollPane51 ========
                        {
                            scrollPane51.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                            scrollPane51.setPreferredSize(new Dimension(300, 68));
                            scrollPane51.setOpaque(false);
                            scrollPane51.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                            //---- revisionDescription ----
                            revisionDescription.setRows(4);
                            revisionDescription.setLineWrap(true);
                            revisionDescription.setWrapStyleWord(true);
                            revisionDescription.setMinimumSize(new Dimension(200, 16));
                            scrollPane51.setViewportView(revisionDescription);
                        }
                        panel31.add(scrollPane51, cc.xywh(1, 17, 3, 1));

                        //---- label18 ----
                        label18.setText("Finding Aid Status");
                        label18.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label18, Resources.class, Resources.PROPERTYNAME_FINDING_AID_STATUS);
                        panel31.add(label18, cc.xywh(1, 19, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                        //======== panel3 ========
                        {
                            panel3.setOpaque(false);
                            panel3.setLayout(new FormLayout(
                                "min(default;200px)",
                                "default"));

                            //---- extentType3 ----
                            extentType3.setOpaque(false);
                            extentType3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            extentType3.setMaximumSize(new Dimension(100, 32767));
                            panel3.add(extentType3, cc.xy(1, 1));
                        }
                        panel31.add(panel3, cc.xy(3, 19));

                        //---- label_resourcesTitle3 ----
                        label_resourcesTitle3.setText("Finding Aid Note");
                        label_resourcesTitle3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label_resourcesTitle3, Resources.class, Resources.PROPERTYNAME_FINDING_AID_NOTE);
                        panel31.add(label_resourcesTitle3, cc.xy(1, 21));

                        //======== scrollPane44 ========
                        {
                            scrollPane44.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                            scrollPane44.setPreferredSize(new Dimension(300, 68));
                            scrollPane44.setOpaque(false);
                            scrollPane44.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                            //---- sponsorNote2 ----
                            sponsorNote2.setRows(4);
                            sponsorNote2.setLineWrap(true);
                            sponsorNote2.setWrapStyleWord(true);
                            sponsorNote2.setMinimumSize(new Dimension(200, 16));
                            scrollPane44.setViewportView(sponsorNote2);
                        }
                        panel31.add(scrollPane44, cc.xywh(1, 23, 3, 1));
                    }
                    panel41.add(panel31, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
                }
                basicInformationPanel2.add(panel41, cc.xywh(5, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
            }
            tabbedPane.addTab("Finding Aid Data", basicInformationPanel2);

        }
        add(tabbedPane, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		instancesTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
	}

	public JComboBox getAddNoteEtcComboBox() {
		return this.addNoteEtcComboBox;
	}

	protected void addRelatedTableInformation() {
		accessionsTable.setClazzAndColumns(AccessionsResources.PROPERTYNAME_ACCESSION_NUMBER,
				AccessionsResources.class,
				AccessionsResources.PROPERTYNAME_ACCESSION_NUMBER,
				AccessionsResources.PROPERTYNAME_ACCESSION_TITLE);
	}

	public DomainSortableTable getInstancesTable() {
		return instancesTable;
	}

	public JButton getAddInstanceButton() {
		return addInstanceButton;
	}

	public JButton getRemoveInstanceButton() {
		return removeInstanceButton;
	}

	public DomainSortedTable getRepeatingDataTable() {
		return repeatingDataTable;
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JTabbedPane tabbedPane;
    private JPanel basicInformationPanel;
    private JPanel panel16;
    private JPanel panel19;
    private JLabel label_resourcesLevel;
    public JComboBox resourcesLevel;
    private JLabel label_otherLevel;
    public JTextField resourcesOtherLevel;
    private JLabel label_resourcesTitle;
    private JScrollPane scrollPane2;
    public JTextArea resourcesTitle;
    private JPanel tagApplicatorPanel;
    public JComboBox insertInlineTag;
    private JPanel panel34;
    private JPanel panel35;
    private JPanel panel36;
    private JLabel label_resourcesDateExpression;
    public JTextField resourcesDateExpression;
    private JLabel Date1Label1;
    private JLabel label_resourcesDateBegin;
    public JFormattedTextField resourcesDateBegin;
    private JLabel label_resourcesDateEnd;
    public JFormattedTextField resourcesDateEnd;
    private JLabel BulkDatesLabel;
    private JLabel label_resourcesBulkDateBegin;
    public JFormattedTextField resourcesBulkDateBegin;
    private JLabel label_resourcesBulkDateEnd;
    public JFormattedTextField resourcesBulkDateEnd;
    private JPanel panel1;
    private JLabel label_resourcesLanguageCode;
    public JComboBox resourcesLanguageCode;
    private JLabel label_resourcesLanguageNote;
    private JScrollPane scrollPane423;
    public JTextArea resourcesLanguageNote;
    private JPanel panel6;
    private JLabel label_agreementReceived2;
    public JTextField repositoryName;
    private JButton changeRepositoryButton;
    private JPanel panel13;
    private JPanel panel17;
    private JPanel panel12;
    private JLabel label_resourceIdentifier1;
    public JTextField resourceIdentifier1;
    public JTextField resourceIdentifier2;
    public JTextField resourceIdentifier3;
    public JTextField resourceIdentifier4;
    private JPanel panel42;
    private JPanel panel43;
    private JLabel OtherAccessionsLabel;
    private JScrollPane scrollPane4;
    private DomainSortableTable accessionsTable;
    private JPanel panel37;
    private JPanel panel20;
    private JLabel ExtentLabel;
    private JPanel panel21;
    private JLabel label_resourcesExtentNumber;
    public JFormattedTextField resourcesExtentNumber;
    public JComboBox extentType;
    private JLabel label_resourcesExtentDescription;
    private JScrollPane scrollPane422;
    public JTextArea containerSummary;
    private JPanel panel39;
    private JPanel panel40;
    private JLabel label1;
    private JScrollPane scrollPane6;
    private DomainSortableTable instancesTable;
    private JPanel panel29;
    private JButton addInstanceButton;
    private JButton removeInstanceButton;
    private JPanel panel2;
    public JCheckBox restrictionsApply;
    private JSeparator separator2;
    private JPanel namesPanel;
    private JLabel SubjectsLabel2;
    private JScrollPane scrollPane1;
    private DomainSortableTable namesTable;
    private JPanel panel8;
    private JButton editNameRelationshipButton;
    private JButton addNameRelationshipButton;
    private JButton removeNameRelationshipButton;
    private JSeparator separator5;
    private JLabel SubjectsLabel;
    private JScrollPane scrollPane3;
    private DomainSortableTable subjectsTable;
    private JPanel panel11;
    private JButton addSubjectRelationshipButton;
    private JButton removeSubjectRelationshipButton;
    private JPanel notesPanel;
    private JLabel SubjectsLabel3;
    private JScrollPane scrollPane5;
    private DomainSortedTable repeatingDataTable;
    private JPanel panel14;
    private JComboBox addNoteEtcComboBox;
    private JButton removeNotesEtcButton;
    private JSeparator separator6;
    private JLabel label_repositoryName3;
    private JScrollPane scrollPane7;
    private DomainSortableTable deaccessionsTable;
    private JPanel panel33;
    private JButton addDeaccessions;
    private JButton removeDeaccession;
    private JPanel basicInformationPanel2;
    private JPanel panel30;
    private JPanel panel9;
    private JLabel label6;
    public JTextField eadIdentifier5;
    private JLabel label15;
    public JTextField eadIdentifier13;
    private JLabel label7;
    private JScrollPane scrollPane45;
    public JTextArea findingAidTitle;
    private JLabel label8;
    private JScrollPane scrollPane46;
    public JTextArea findingAidSubtitle;
    private JLabel label2;
    private JScrollPane scrollPane8;
    private JTextArea findingAidFilingTitle;
    private JLabel label9;
    public JTextField eadIdentifier10;
    private JLabel label10;
    private JScrollPane scrollPane47;
    public JTextArea author;
    private JLabel label11;
    public JComboBox extentType2;
    private JLabel label12;
    private JScrollPane scrollPane48;
    public JTextArea languageOfFindingAid;
    private JSeparator separator1;
    private JPanel panel41;
    private JPanel panel31;
    private JLabel label_resourcesTitle2;
    private JScrollPane scrollPane43;
    public JTextArea sponsorNote;
    private JLabel label13;
    private JScrollPane scrollPane49;
    public JTextArea editionStatement;
    private JLabel label14;
    private JScrollPane scrollPane50;
    public JTextArea series;
    private JLabel label16;
    public JTextField eadIdentifier15;
    private JLabel label17;
    private JScrollPane scrollPane51;
    public JTextArea revisionDescription;
    private JLabel label18;
    private JPanel panel3;
    public JComboBox extentType3;
    private JLabel label_resourcesTitle3;
    private JScrollPane scrollPane44;
    public JTextArea sponsorNote2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

//	protected DomainRelatedTableModel tableModelAccessionsResources;
//	private ButtonGroup addWhereButtonGroup;
//	EventList<String> languageCodes;
	private AutoCompleteSupport support = null;
	private ATFileChooser filechooser;
	private EADExportHandler handler;
	private Resources res;

	public final void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		inSetModel = true;
		super.setModel(model, progressPanel);
		Resources resourcesModel = (Resources) super.getModel();
		accessionsTable.updateCollection(resourcesModel.getAccessions());
		deaccessionsTable.updateCollection(resourcesModel.getDeaccessions());
		setRepositoryText(resourcesModel);
		inSetModel = false;
        setPluginModel(); // update any plugins with this new domain object
	}

	public Component getInitialFocusComponent() {
		return resourcesLevel;
	}

	private void setRepositoryText(Resources model) {
		if (model.getRepository() == null) {
			this.repositoryName.setText("");
		} else {
			this.repositoryName.setText(model.getRepository().getShortName());
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

	public JTable getAccessionsTable() {
		return accessionsTable;
	}

    /**
     * Method that initializes any embedded plugins that would add an editor
     */
    private void initPlugins() {
        plugins = ATPluginFactory.getInstance().getEmbeddedResourceEditorPlugins();
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
