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
 * Created by JFormDesigner on Fri Jan 27 18:19:35 EST 2006
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.InLineTags;
import org.archiviststoolkit.structure.DefaultValues;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.util.InLineTagsUtils;
import org.archiviststoolkit.plugin.ATPlugin;
import org.archiviststoolkit.plugin.ATPluginFactory;

public class ResourceComponentsFields extends ArchDescriptionFields {
    private ArrayList<ATPlugin> plugins = null; // stores any embedded resource component editor plugins

    // boolean to keep if tab zero was removed by a plugin. This needs to be
    // done becuase when the record is saved or changed the commitChanges method
    // is called and it updates the model with values from the hidden panel
    // which are likely empty. This will then cause a validation error which
    // makes no sense.
    private boolean tabPanelZeroRemoved = false;

    public ResourceComponentsFields() {
        super();
        initComponents();
        repeatingDataTable.setTransferable();
        initNotesEtc();
        initUndo(resourcesTitle);
        initPlugins();// initiate any plugins now
    }

    protected void setDisplayToFirstTab() {
        this.tabbedPane.setSelectedIndex(0);
    }

    private void addNoteEtcComboBoxActionPerformed(ActionEvent e) {
        super.addNoteEtcComboBoxAction();
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

//    public JButton getUndoButton() {
//    	return undoButton;
//    }
//
//    public JButton getRedoButton() {
//    	return redoButton;
//    }

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

    private void removeNotesEtcButtonActionPerformed() {
        super.removeNotesEtc();
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
        panel7 = new JPanel();
        panel3 = new JPanel();
        label_resourcesLevel = new JLabel();
        resourcesLevel = ATBasicComponentFactory.createComboBox(detailsModel, ResourcesComponents.PROPERTYNAME_LEVEL, ResourcesComponents.class);
        panel12 = new JPanel();
        label3 = new JLabel();
        resourcesDateBegin2 = ATBasicComponentFactory.createTextField(detailsModel.getModel(ResourcesComponents.PROPERTYNAME_PERSISTENT_ID));
        label_otherLevel = new JLabel();
        resourcesOtherLevel = ATBasicComponentFactory.createTextField(detailsModel.getModel(ResourcesComponents.PROPERTYNAME_OTHER_LEVEL),false);
        label_resourcesTitle = new JLabel();
        scrollPane42 = new JScrollPane();
        resourcesTitle = ATBasicComponentFactory.createTextArea(detailsModel.getModel(ArchDescription.PROPERTYNAME_TITLE),false);
        tagApplicatorPanel = new JPanel();
        insertInlineTag = ATBasicComponentFactory.createUnboundComboBox(InLineTagsUtils.getInLineTagList(InLineTagsUtils.TITLE));
        panel34 = new JPanel();
        panel35 = new JPanel();
        panel36 = new JPanel();
        label_resourcesDateExpression = new JLabel();
        resourcesDateExpression = ATBasicComponentFactory.createTextField(detailsModel.getModel(ArchDescription.PROPERTYNAME_DATE_EXPRESSION),false);
        Date1Label = new JLabel();
        label_resourcesDateBegin = new JLabel();
        resourcesDateBegin = ATBasicComponentFactory.createIntegerField(detailsModel,ArchDescription.PROPERTYNAME_DATE_BEGIN);
        label_resourcesDateEnd = new JLabel();
        resourcesDateEnd = ATBasicComponentFactory.createIntegerField(detailsModel,ArchDescription.PROPERTYNAME_DATE_END);
        BulkDatesLabel = new JLabel();
        label_resourcesBulkDateBegin = new JLabel();
        resourcesBulkDateBegin = ATBasicComponentFactory.createIntegerField(detailsModel,ResourcesComponents.PROPERTYNAME_BULK_DATE_BEGIN);
        label_resourcesBulkDateEnd = new JLabel();
        resourcesBulkDateEnd = ATBasicComponentFactory.createIntegerField(detailsModel,ResourcesComponents.PROPERTYNAME_BULK_DATE_END);
        panel9 = new JPanel();
        label_resourcesLanguageCode2 = new JLabel();
        resourcesLanguageCode = ATBasicComponentFactory.createComboBox(detailsModel, ResourcesComponents.PROPERTYNAME_LANGUAGE_CODE, ResourcesComponents.class);
        panel23 = new JPanel();
        label_resourcesLanguageNote2 = new JLabel();
        scrollPane423 = new JScrollPane();
        resourcesLanguageNote = ATBasicComponentFactory.createTextArea(detailsModel.getModel(ResourcesComponents.PROPERTYNAME_REPOSITORY_PROCESSING_NOTE),false);
        separator2 = new JSeparator();
        panel10 = new JPanel();
        panel1 = new JPanel();
        label_resourcesLevel2 = new JLabel();
        subdivisionIdentifier = ATBasicComponentFactory.createTextField(detailsModel.getModel(ResourcesComponents.PROPERTYNAME_UNIQUE_IDENTIFIER),false);
        panel32 = new JPanel();
        panel5 = new JPanel();
        ExtentNumberLabel2 = new JLabel();
        label_resourcesExtentNumber = new JLabel();
        resourcesExtentNumber = ATBasicComponentFactory.createDoubleField(detailsModel,ResourcesComponents.PROPERTYNAME_EXTENT_NUMBER);
        extentType = ATBasicComponentFactory.createComboBox(detailsModel, ResourcesComponents.PROPERTYNAME_EXTENT_TYPE, ResourcesComponents.class);
        label_resourcesExtentDescription = new JLabel();
        scrollPane422 = new JScrollPane();
        resourcesExtentDescription = ATBasicComponentFactory.createTextArea(detailsModel.getModel(ResourcesComponents.PROPERTYNAME_CONTAINER_SUMMARY),false);
        panel2 = new JPanel();
        panel6 = new JPanel();
        label1 = new JLabel();
        scrollPane4 = new JScrollPane();
        instancesTable = new DomainSortableTable(ArchDescriptionInstances.class, ArchDescriptionInstances.PROPERTYNAME_INSTANCE_TYPE);
        panel13 = new JPanel();
        addInstanceButton = new JButton();
        removeInstanceButton = new JButton();
        panel4 = new JPanel();
        restrictionsApply2 = ATBasicComponentFactory.createCheckBox(detailsModel, ResourcesComponents.PROPERTYNAME_INTERNAL_ONLY, ResourcesComponents.class);
        resourcesRestrictionsApply = ATBasicComponentFactory.createCheckBox(detailsModel, ArchDescription.PROPERTYNAME_RESTRICTIONS_APPLY, ResourcesComponents.class);
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
        scrollPane5 = new JScrollPane();
        repeatingDataTable = new DomainSortedTable(ArchDescriptionRepeatingData.class);
        panel15 = new JPanel();
        addNoteEtcComboBox = new JComboBox();
        removeNotesEtcButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        setBackground(new Color(200, 205, 232));
        setLayout(new FormLayout(
            "default:grow",
            "top:default:grow"));

        //======== tabbedPane ========
        {
            tabbedPane.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            tabbedPane.setBackground(new Color(200, 205, 232));
            tabbedPane.setOpaque(true);

            //======== basicInformationPanel ========
            {
                basicInformationPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                basicInformationPanel.setBackground(new Color(200, 205, 232));
                basicInformationPanel.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        new ColumnSpec(ColumnSpec.LEFT, Sizes.DEFAULT, 0.5),
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        new ColumnSpec("left:max(default;300px):grow(0.5)")
                    },
                    RowSpec.decodeSpecs("fill:default:grow")));
                ((FormLayout)basicInformationPanel.getLayout()).setColumnGroups(new int[][] {{1, 5}});

                //======== panel7 ========
                {
                    panel7.setOpaque(false);
                    panel7.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel7.setBorder(Borders.DLU2_BORDER);
                    panel7.setLayout(new FormLayout(
                        ColumnSpec.decodeSpecs("default:grow"),
                        new RowSpec[] {
                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        }));

                    //======== panel3 ========
                    {
                        panel3.setOpaque(false);
                        panel3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel3.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.MIN_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec(ColumnSpec.RIGHT, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
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
                        ATFieldInfo.assignLabelInfo(label_resourcesLevel, ResourcesComponents.class, ResourcesComponents.PROPERTYNAME_LEVEL);
                        panel3.add(label_resourcesLevel, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

                        //---- resourcesLevel ----
                        resourcesLevel.setOpaque(false);
                        resourcesLevel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        resourcesLevel.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                resourcesLevelActionPerformed();
                            }
                        });
                        panel3.add(resourcesLevel, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                        //======== panel12 ========
                        {
                            panel12.setOpaque(false);
                            panel12.setLayout(new FormLayout(
                                new ColumnSpec[] {
                                    FormFactory.DEFAULT_COLSPEC,
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    FormFactory.DEFAULT_COLSPEC
                                },
                                RowSpec.decodeSpecs("default")));

                            //---- label3 ----
                            label3.setText("Persistent ID");
                            label3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            ATFieldInfo.assignLabelInfo(label3, ResourcesComponents.class, ResourcesComponents.PROPERTYNAME_PERSISTENT_ID);
                            panel12.add(label3, cc.xy(1, 1));

                            //---- resourcesDateBegin2 ----
                            resourcesDateBegin2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            resourcesDateBegin2.setEditable(false);
                            resourcesDateBegin2.setOpaque(false);
                            panel12.add(resourcesDateBegin2, cc.xy(3, 1));
                        }
                        panel3.add(panel12, cc.xy(5, 1));

                        //---- label_otherLevel ----
                        label_otherLevel.setText("Other Level");
                        label_otherLevel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label_otherLevel, ResourcesComponents.class, ResourcesComponents.PROPERTYNAME_OTHER_LEVEL);
                        panel3.add(label_otherLevel, cc.xywh(1, 3, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
                        panel3.add(resourcesOtherLevel, cc.xywh(3, 3, 3, 1));

                        //---- label_resourcesTitle ----
                        label_resourcesTitle.setText("Title");
                        label_resourcesTitle.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label_resourcesTitle, ResourcesComponents.class, ResourcesComponents.PROPERTYNAME_TITLE);
                        panel3.add(label_resourcesTitle, cc.xywh(1, 5, 5, 1));

                        //======== scrollPane42 ========
                        {
                            scrollPane42.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                            scrollPane42.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                            //---- resourcesTitle ----
                            resourcesTitle.setRows(4);
                            resourcesTitle.setLineWrap(true);
                            resourcesTitle.setWrapStyleWord(true);
                            scrollPane42.setViewportView(resourcesTitle);
                        }
                        panel3.add(scrollPane42, cc.xywh(1, 7, 5, 1));

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
                        panel3.add(tagApplicatorPanel, cc.xywh(1, 9, 5, 1));
                    }
                    panel7.add(panel3, cc.xywh(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));

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
                                    FormFactory.DEFAULT_COLSPEC,
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    FormFactory.DEFAULT_COLSPEC,
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    FormFactory.DEFAULT_COLSPEC,
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
                                ATFieldInfo.assignLabelInfo(label_resourcesDateExpression, ResourcesComponents.class, ResourcesComponents.PROPERTYNAME_DATE_EXPRESSION);
                                panel36.add(label_resourcesDateExpression, cc.xy(1, 1));
                                panel36.add(resourcesDateExpression, cc.xy(3, 1));
                            }
                            panel35.add(panel36, cc.xywh(1, 1, 9, 1));

                            //---- Date1Label ----
                            Date1Label.setText("Date");
                            Date1Label.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            panel35.add(Date1Label, new CellConstraints(1, 3, 9, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 5, 0, 0)));

                            //---- label_resourcesDateBegin ----
                            label_resourcesDateBegin.setText("Begin");
                            label_resourcesDateBegin.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            ATFieldInfo.assignLabelInfo(label_resourcesDateBegin, ResourcesComponents.class, ResourcesComponents.PROPERTYNAME_DATE_BEGIN);
                            panel35.add(label_resourcesDateBegin, cc.xy(3, 5));

                            //---- resourcesDateBegin ----
                            resourcesDateBegin.setColumns(4);
                            panel35.add(resourcesDateBegin, cc.xy(5, 5));

                            //---- label_resourcesDateEnd ----
                            label_resourcesDateEnd.setText("End");
                            label_resourcesDateEnd.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            ATFieldInfo.assignLabelInfo(label_resourcesDateEnd, ResourcesComponents.class, ResourcesComponents.PROPERTYNAME_DATE_END);
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
                            ATFieldInfo.assignLabelInfo(label_resourcesBulkDateBegin, ResourcesComponents.class, ResourcesComponents.PROPERTYNAME_BULK_DATE_BEGIN);
                            panel35.add(label_resourcesBulkDateBegin, cc.xy(3, 9));

                            //---- resourcesBulkDateBegin ----
                            resourcesBulkDateBegin.setColumns(4);
                            panel35.add(resourcesBulkDateBegin, cc.xy(5, 9));

                            //---- label_resourcesBulkDateEnd ----
                            label_resourcesBulkDateEnd.setText("End");
                            label_resourcesBulkDateEnd.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            ATFieldInfo.assignLabelInfo(label_resourcesBulkDateEnd, ResourcesComponents.class, ResourcesComponents.PROPERTYNAME_BULK_DATE_END);
                            panel35.add(label_resourcesBulkDateEnd, cc.xy(7, 9));

                            //---- resourcesBulkDateEnd ----
                            resourcesBulkDateEnd.setColumns(4);
                            panel35.add(resourcesBulkDateEnd, new CellConstraints(9, 9, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT, new Insets( 0, 0, 0, 5)));
                        }
                        panel34.add(panel35, cc.xy(1, 1));
                    }
                    panel7.add(panel34, cc.xy(1, 3));

                    //======== panel9 ========
                    {
                        panel9.setOpaque(false);
                        panel9.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec("left:min(default;200px)")
                            },
                            RowSpec.decodeSpecs("default")));

                        //---- label_resourcesLanguageCode2 ----
                        label_resourcesLanguageCode2.setText("Lanaguage");
                        label_resourcesLanguageCode2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label_resourcesLanguageCode2, ResourcesComponents.class, ResourcesComponents.PROPERTYNAME_LANGUAGE_CODE);
                        panel9.add(label_resourcesLanguageCode2, cc.xy(1, 1));

                        //---- resourcesLanguageCode ----
                        resourcesLanguageCode.setMaximumSize(new Dimension(150, 32767));
                        resourcesLanguageCode.setOpaque(false);
                        resourcesLanguageCode.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel9.add(resourcesLanguageCode, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
                    }
                    panel7.add(panel9, cc.xy(1, 5));

                    //======== panel23 ========
                    {
                        panel23.setOpaque(false);
                        panel23.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel23.setLayout(new FormLayout(
                            ColumnSpec.decodeSpecs("default:grow"),
                            new RowSpec[] {
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                            }));

                        //---- label_resourcesLanguageNote2 ----
                        label_resourcesLanguageNote2.setText("Repository Processing Note");
                        label_resourcesLanguageNote2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label_resourcesLanguageNote2, ResourcesComponents.class, ResourcesComponents.PROPERTYNAME_REPOSITORY_PROCESSING_NOTE);
                        panel23.add(label_resourcesLanguageNote2, new CellConstraints(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 10, 0, 0)));

                        //======== scrollPane423 ========
                        {
                            scrollPane423.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                            scrollPane423.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                            //---- resourcesLanguageNote ----
                            resourcesLanguageNote.setRows(4);
                            resourcesLanguageNote.setWrapStyleWord(true);
                            scrollPane423.setViewportView(resourcesLanguageNote);
                        }
                        panel23.add(scrollPane423, new CellConstraints(1, 3, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 10, 0, 0)));
                    }
                    panel7.add(panel23, cc.xy(1, 7));
                }
                basicInformationPanel.add(panel7, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

                //---- separator2 ----
                separator2.setForeground(new Color(147, 131, 86));
                separator2.setOrientation(SwingConstants.VERTICAL);
                basicInformationPanel.add(separator2, cc.xywh(3, 1, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

                //======== panel10 ========
                {
                    panel10.setOpaque(false);
                    panel10.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel10.setBorder(Borders.DLU2_BORDER);
                    panel10.setLayout(new FormLayout(
                        ColumnSpec.decodeSpecs("default:grow"),
                        new RowSpec[] {
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC
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

                        //---- label_resourcesLevel2 ----
                        label_resourcesLevel2.setText("Component Unique Identifier");
                        label_resourcesLevel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label_resourcesLevel2, ResourcesComponents.class, ResourcesComponents.PROPERTYNAME_UNIQUE_IDENTIFIER);
                        panel1.add(label_resourcesLevel2, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

                        //---- subdivisionIdentifier ----
                        subdivisionIdentifier.setColumns(5);
                        panel1.add(subdivisionIdentifier, cc.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
                    }
                    panel10.add(panel1, cc.xy(1, 1));

                    //======== panel32 ========
                    {
                        panel32.setBorder(new BevelBorder(BevelBorder.LOWERED));
                        panel32.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel32.setBackground(new Color(182, 187, 212));
                        panel32.setLayout(new FormLayout(
                            "60px:grow",
                            "fill:default:grow"));

                        //======== panel5 ========
                        {
                            panel5.setBorder(Borders.DLU2_BORDER);
                            panel5.setOpaque(false);
                            panel5.setLayout(new FormLayout(
                                new ColumnSpec[] {
                                    FormFactory.RELATED_GAP_COLSPEC,
                                    FormFactory.DEFAULT_COLSPEC,
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    FormFactory.DEFAULT_COLSPEC,
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
                                    new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                                }));

                            //---- ExtentNumberLabel2 ----
                            ExtentNumberLabel2.setText("Extent");
                            ExtentNumberLabel2.setForeground(new Color(0, 0, 102));
                            ExtentNumberLabel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            panel5.add(ExtentNumberLabel2, cc.xywh(1, 1, 2, 1));

                            //---- label_resourcesExtentNumber ----
                            label_resourcesExtentNumber.setText("Extent");
                            label_resourcesExtentNumber.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            ATFieldInfo.assignLabelInfo(label_resourcesExtentNumber, ResourcesComponents.class, ResourcesComponents.PROPERTYNAME_EXTENT_NUMBER);
                            panel5.add(label_resourcesExtentNumber, cc.xy(2, 3));

                            //---- resourcesExtentNumber ----
                            resourcesExtentNumber.setColumns(5);
                            panel5.add(resourcesExtentNumber, cc.xy(4, 3));

                            //---- extentType ----
                            extentType.setOpaque(false);
                            extentType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            panel5.add(extentType, cc.xywh(6, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                            //---- label_resourcesExtentDescription ----
                            label_resourcesExtentDescription.setText("Container Summary");
                            label_resourcesExtentDescription.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            ATFieldInfo.assignLabelInfo(label_resourcesExtentDescription, ResourcesComponents.class, ResourcesComponents.PROPERTYNAME_CONTAINER_SUMMARY);
                            panel5.add(label_resourcesExtentDescription, cc.xywh(2, 5, 5, 1));

                            //======== scrollPane422 ========
                            {
                                scrollPane422.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                                scrollPane422.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                                //---- resourcesExtentDescription ----
                                resourcesExtentDescription.setRows(4);
                                resourcesExtentDescription.setWrapStyleWord(true);
                                scrollPane422.setViewportView(resourcesExtentDescription);
                            }
                            panel5.add(scrollPane422, cc.xywh(2, 7, 5, 1));
                        }
                        panel32.add(panel5, cc.xy(1, 1));
                    }
                    panel10.add(panel32, cc.xy(1, 3));

                    //======== panel2 ========
                    {
                        panel2.setBackground(new Color(182, 187, 212));
                        panel2.setBorder(new BevelBorder(BevelBorder.LOWERED));
                        panel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel2.setLayout(new FormLayout(
                            "default:grow",
                            "fill:default:grow"));

                        //======== panel6 ========
                        {
                            panel6.setOpaque(false);
                            panel6.setBorder(Borders.DLU2_BORDER);
                            panel6.setLayout(new FormLayout(
                                new ColumnSpec[] {
                                    FormFactory.RELATED_GAP_COLSPEC,
                                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                                },
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
                            ATFieldInfo.assignLabelInfo(label1, ResourcesComponents.class, ResourcesComponents.PROPERTYNAME_INSTANCES);
                            panel6.add(label1, cc.xywh(1, 1, 2, 1));

                            //======== scrollPane4 ========
                            {
                                scrollPane4.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                                //---- instancesTable ----
                                instancesTable.setPreferredScrollableViewportSize(new Dimension(200, 75));
                                instancesTable.setRowHeight(20);
                                instancesTable.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {
                                        instancesTableMouseClicked(e);
                                    }
                                });
                                scrollPane4.setViewportView(instancesTable);
                            }
                            panel6.add(scrollPane4, cc.xy(2, 3));

                            //======== panel13 ========
                            {
                                panel13.setBackground(new Color(231, 188, 251));
                                panel13.setOpaque(false);
                                panel13.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                panel13.setLayout(new FormLayout(
                                    new ColumnSpec[] {
                                        FormFactory.DEFAULT_COLSPEC,
                                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                        FormFactory.DEFAULT_COLSPEC
                                    },
                                    RowSpec.decodeSpecs("default")));

                                //---- addInstanceButton ----
                                addInstanceButton.setBackground(new Color(231, 188, 251));
                                addInstanceButton.setText("Add Instance");
                                addInstanceButton.setOpaque(false);
                                addInstanceButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                addInstanceButton.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        addInstanceButtonActionPerformed();
                                    }
                                });
                                panel13.add(addInstanceButton, cc.xy(1, 1));

                                //---- removeInstanceButton ----
                                removeInstanceButton.setBackground(new Color(231, 188, 251));
                                removeInstanceButton.setText("Remove Instance");
                                removeInstanceButton.setOpaque(false);
                                removeInstanceButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                removeInstanceButton.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        removeInstanceButtonActionPerformed();
                                    }
                                });
                                panel13.add(removeInstanceButton, cc.xy(3, 1));
                            }
                            panel6.add(panel13, cc.xywh(1, 5, 2, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
                        }
                        panel2.add(panel6, cc.xy(1, 1));
                    }
                    panel10.add(panel2, cc.xy(1, 5));

                    //======== panel4 ========
                    {
                        panel4.setOpaque(false);
                        panel4.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC
                            },
                            RowSpec.decodeSpecs("default")));

                        //---- restrictionsApply2 ----
                        restrictionsApply2.setBackground(new Color(231, 188, 251));
                        restrictionsApply2.setText("Internal Only");
                        restrictionsApply2.setOpaque(false);
                        restrictionsApply2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        restrictionsApply2.setText(ATFieldInfo.getLabel(ResourcesComponents.class, ResourcesComponents.PROPERTYNAME_INTERNAL_ONLY));
                        panel4.add(restrictionsApply2, cc.xy(1, 1));

                        //---- resourcesRestrictionsApply ----
                        resourcesRestrictionsApply.setBackground(new Color(231, 188, 251));
                        resourcesRestrictionsApply.setText("Restrictions Apply");
                        resourcesRestrictionsApply.setOpaque(false);
                        resourcesRestrictionsApply.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        resourcesRestrictionsApply.setText(ATFieldInfo.getLabel(ResourcesComponents.class, ArchDescription.PROPERTYNAME_RESTRICTIONS_APPLY));
                        panel4.add(resourcesRestrictionsApply, cc.xy(3, 1));
                    }
                    panel10.add(panel4, cc.xy(1, 7));
                }
                basicInformationPanel.add(panel10, cc.xywh(5, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL));
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

                //---- SubjectsLabel ----
                SubjectsLabel.setText("Subjects");
                SubjectsLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                namesPanel.add(SubjectsLabel, cc.xy(1, 9));

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


            //======== notesPanel ========
            {
                notesPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                notesPanel.setBackground(new Color(200, 205, 232));
                notesPanel.setLayout(new FormLayout(
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

                    //---- repeatingDataTable ----
                    repeatingDataTable.setDragEnabled(true);
                    repeatingDataTable.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            repeatingDataTableMouseClicked(e);
                        }
                    });
                    scrollPane5.setViewportView(repeatingDataTable);
                }
                notesPanel.add(scrollPane5, cc.xy(1, 1));

                //======== panel15 ========
                {
                    panel15.setBackground(new Color(231, 188, 251));
                    panel15.setOpaque(false);
                    panel15.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel15.setLayout(new FormLayout(
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
                    panel15.add(addNoteEtcComboBox, cc.xy(1, 1));

                    //---- removeNotesEtcButton ----
                    removeNotesEtcButton.setBackground(new Color(231, 188, 251));
                    removeNotesEtcButton.setText("Remove Note etc.");
                    removeNotesEtcButton.setOpaque(false);
                    removeNotesEtcButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    removeNotesEtcButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            removeNotesEtcButtonActionPerformed();
                        }
                    });
                    panel15.add(removeNotesEtcButton, cc.xy(3, 1));
                }
                notesPanel.add(panel15, cc.xywh(1, 3, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
            }
            tabbedPane.addTab("Notes Etc.", notesPanel);

        }
        add(tabbedPane, cc.xywh(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
        instancesTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    }

    public JButton getAddInstanceButton() {
        return addInstanceButton;
    }

    public JButton getRemoveInstanceButton() {
        return removeInstanceButton;
    }

    public DomainSortableTable getNamesTable() {
        return namesTable;
    }

    public DomainSortedTable getRepeatingDataTable() {
        return repeatingDataTable;
    }

    public DomainSortableTable getInstancesTable() {
        return instancesTable;
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JTabbedPane tabbedPane;
    private JPanel basicInformationPanel;
    private JPanel panel7;
    private JPanel panel3;
    private JLabel label_resourcesLevel;
    public JComboBox resourcesLevel;
    private JPanel panel12;
    private JLabel label3;
    public JTextField resourcesDateBegin2;
    private JLabel label_otherLevel;
    public JTextField resourcesOtherLevel;
    private JLabel label_resourcesTitle;
    private JScrollPane scrollPane42;
    public JTextArea resourcesTitle;
    private JPanel tagApplicatorPanel;
    public JComboBox insertInlineTag;
    private JPanel panel34;
    private JPanel panel35;
    private JPanel panel36;
    private JLabel label_resourcesDateExpression;
    public JTextField resourcesDateExpression;
    private JLabel Date1Label;
    private JLabel label_resourcesDateBegin;
    public JFormattedTextField resourcesDateBegin;
    private JLabel label_resourcesDateEnd;
    public JFormattedTextField resourcesDateEnd;
    private JLabel BulkDatesLabel;
    private JLabel label_resourcesBulkDateBegin;
    public JFormattedTextField resourcesBulkDateBegin;
    private JLabel label_resourcesBulkDateEnd;
    public JFormattedTextField resourcesBulkDateEnd;
    private JPanel panel9;
    private JLabel label_resourcesLanguageCode2;
    public JComboBox resourcesLanguageCode;
    private JPanel panel23;
    private JLabel label_resourcesLanguageNote2;
    private JScrollPane scrollPane423;
    public JTextArea resourcesLanguageNote;
    private JSeparator separator2;
    private JPanel panel10;
    private JPanel panel1;
    private JLabel label_resourcesLevel2;
    public JTextField subdivisionIdentifier;
    private JPanel panel32;
    private JPanel panel5;
    private JLabel ExtentNumberLabel2;
    private JLabel label_resourcesExtentNumber;
    public JFormattedTextField resourcesExtentNumber;
    public JComboBox extentType;
    private JLabel label_resourcesExtentDescription;
    private JScrollPane scrollPane422;
    public JTextArea resourcesExtentDescription;
    private JPanel panel2;
    private JPanel panel6;
    private JLabel label1;
    private JScrollPane scrollPane4;
    private DomainSortableTable instancesTable;
    private JPanel panel13;
    private JButton addInstanceButton;
    private JButton removeInstanceButton;
    private JPanel panel4;
    public JCheckBox restrictionsApply2;
    public JCheckBox resourcesRestrictionsApply;
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
    private JScrollPane scrollPane5;
    private DomainSortedTable repeatingDataTable;
    private JPanel panel15;
    private JComboBox addNoteEtcComboBox;
    private JButton removeNotesEtcButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private ButtonGroup addWhereButtonGroup;


    public JComboBox getAddNoteEtcComboBox() {
        return addNoteEtcComboBox;
    }

    public final void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
        inSetModel = true;
        if (model.isNewRecord()) {
            DefaultValues.assignDefaultValues(model);
        }
        super.setModel(model, progressPanel);
        ResourcesComponents resourcesComponents = (ResourcesComponents) model;
        resourcesDateBegin.setValue(resourcesComponents.getDateBegin());
        resourcesDateEnd.setValue(resourcesComponents.getDateEnd());
        resourcesBulkDateBegin.setValue(resourcesComponents.getBulkDateBegin());
        resourcesBulkDateEnd.setValue(resourcesComponents.getBulkDateEnd());
        resourcesExtentNumber.setValue(resourcesComponents.getExtentNumber());
        inSetModel = false;
        setPluginModel(); // update any plugins with this new domain object
    }

    public Component getInitialFocusComponent() {
        return resourcesLevel;
    }

    public final void commitChanges() {
        //this is a hack to work around the problem that if you are in a formatted
        //text field and click on another node in the tree the value is not committed
        try {
            if (!tabPanelZeroRemoved) {
                resourcesDateBegin.commitEdit();
                resourcesDateEnd.commitEdit();
                resourcesBulkDateBegin.commitEdit();
                resourcesBulkDateEnd.commitEdit();
                resourcesExtentNumber.commitEdit();
            }
        } catch (ParseException e) {
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
     * Method that initializes any embedded plugins that would add an editor
     */
    private void initPlugins() {
        plugins = ATPluginFactory.getInstance().getEmbeddedResourceComponentEditorPlugins();
        if (plugins != null) {
            for (ATPlugin plugin : plugins) {
                plugin.setEditorField(this);
                HashMap pluginPanels = plugin.getEmbeddedPanels();
                for (Object key : pluginPanels.keySet()) {
                    String panelName = (String) key;
                    JPanel pluginPanel = (JPanel) pluginPanels.get(key);

                    // see where to add the tab and if to remove any tabs that are
                    // already there
                    if (panelName.indexOf("::") == -1) { // add it to end
                        tabbedPane.addTab(panelName, pluginPanel);
                    } else { // adding table to specific location in tab
                        // get tab placement information from the name string
                        // panelPlacementInfo[0] = panel name
                        // panelPlacementInfo[1] = panel index
                        // panelPlacementInfo[2] = yes if to replace the panel already there,
                        //                         no to just insert at the index
                        String[] panelPlacementInfo = panelName.split("::");
                        try {
                            int index = Integer.parseInt(panelPlacementInfo[1]);

                            // see whether to remove the index already there
                            if (panelPlacementInfo[2].equalsIgnoreCase("yes")) {
                                tabbedPane.remove(index);

                                // see whether to change the tab zero panel removed boolean to true
                                if (index == 0) {
                                    tabPanelZeroRemoved = true;
                                }
                            }

                            // now insert the panel at the location specified by the index
                            tabbedPane.insertTab(panelPlacementInfo[0], null, pluginPanel, "", index);

                        } catch (NumberFormatException nfe) {
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
        if (plugins != null) {
            for (ATPlugin plugin : plugins) {
                plugin.setModel(getModel(), null);
            }
        }
    }
}
