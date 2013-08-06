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
 * Created by JFormDesigner on Thu Sep 01 22:56:03 EDT 2005
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
import com.centerkey.utils.BareBonesBrowserLaunch;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.dialog.ResourceLookup;
import org.archiviststoolkit.dialog.LocationAssignmentAccessions;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.SelectFromList;
import org.archiviststoolkit.swing.StandardEditor;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.exceptions.DomainEditorCreationException;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.plugin.ATPlugin;
import org.archiviststoolkit.plugin.ATPluginFactory;

public class AccessionFields extends ArchDescriptionFields {
	private ArrayList<ATPlugin> plugins = null; // stores any embedded editor plugins

	public AccessionFields() {
		super();
		initComponents();
		addRelatedTableInformation();
		initAccess();
        initPlugins();
	}

	private void addExternalLinkButtonActionPerformed(ActionEvent e) {

		ArchDescription archDescriptionModel = (ArchDescription) super.getModel();
		ExternalReference externalReference;
		ArchDescriptionRepeatingDataEditor externalReferenceEditor = null;
		try {
			externalReferenceEditor = (ArchDescriptionRepeatingDataEditor) DomainEditorFactory.getInstance()
					.createDomainEditorWithParent(ArchDescriptionRepeatingData.class, this.getParentEditor(), false);
		} catch (DomainEditorCreationException e1) {
			new ErrorDialog(getParentEditor(), "Error creating editor for ArchDescriptionRepeatingData", e1).showDialog();
		}
		externalReferenceEditor.setNewRecord(true);

        boolean done = false;
        int returnStatus;
        while (!done) {
            externalReference = new ExternalReference(archDescriptionModel);
            externalReferenceEditor.setModel(externalReference, null);

            returnStatus = externalReferenceEditor.showDialog();
            if (returnStatus == JOptionPane.OK_OPTION) {
                archDescriptionModel.addRepeatingData(externalReference);
                externalDocumentsTable.getEventList().add(externalReference);
                done = true;
            } else if (returnStatus == StandardEditor.OK_AND_ANOTHER_OPTION) {
                archDescriptionModel.addRepeatingData(externalReference);
                externalDocumentsTable.getEventList().add(externalReference);
             } else {
                done = true;
            }
        }
    }

	private void removeExternalLinkButtonActionPerformed(ActionEvent e) {
		try {
			this.removeRelatedTableRow(externalDocumentsTable, null, super.getModel());
		} catch (ObjectNotRemovedException e1) {
			new ErrorDialog("External Link not removed", e1).showDialog();
		}
	}

	private void externalDocumentsTableMouseClicked(MouseEvent e) {
		handleTableMouseClick(e, externalDocumentsTable, ArchDescriptionRepeatingData.class);
	}

	private void changeRepositoryButtonActionPerformed() {
		Vector repositories = Repositories.getRepositoryList();
		Repositories currentRepostory = ((Accessions) this.getModel()).getRepository();
		Accessions model = (Accessions) this.getModel();
        SelectFromList dialog = new SelectFromList(this.getParentEditor(), "Select a repository", repositories.toArray());
        dialog.setSelectedValue(currentRepostory);
        if (dialog.showDialog() == JOptionPane.OK_OPTION) {
            model.setRepository((Repositories)dialog.getSelectedValue());
            setRepositoryText(model);
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

	public JButton getAddButton() {
		return addButton;
	}

	private void addLocationButtonActionPerformed(ActionEvent e) {
		LocationAssignmentAccessions dialog = new LocationAssignmentAccessions(getParentEditor(), this);
		dialog.setMainHeaderByClass(Accessions.class);
		dialog.showDialog();
	}

	private void removeLocationButtonActionPerformed(ActionEvent e) {
		try {
			this.removeRelatedTableRow(locationsTable, null, super.getModel());
		} catch (ObjectNotRemovedException e1) {
			new ErrorDialog("Location not removed", e1).showDialog();
		}
		locationsTable.updateCollection(((Accessions) super.getModel()).getLocations());
	}

	public DomainSortableTable getLocationsTable() {
		return locationsTable;
	}

	private void openInBrowserActionPerformed() {
        int selectedRow = externalDocumentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "You must select a row.");
        } else {
            ExternalReference externalReference =
                    (ExternalReference) externalDocumentsTable.getSortedList().get(selectedRow);
            BareBonesBrowserLaunch.openURL(externalReference.getHref());
        }
	}

	private void namesTableMouseClicked(MouseEvent e) {
		super.handleNamesTableMouseClick(e);
	}

	private void locationsTableMouseClicked(MouseEvent e) {
		handleTableMouseClick(e, locationsTable, AccessionsLocations.class);
	}

	private void editNameRelationshipButtonActionPerformed() {
		super.editNameRelationshipActionPerformed();
	}

	private void addNameRelationshipButtonActionPerformed(ActionEvent e) {
		addNameRelationship();
	}

	private void removeNameRelationshipButtonActionPerformed(ActionEvent e) {
		ArchDescription archDescriptionModel = (ArchDescription) super.getModel();
		try {
			this.removeRelatedTableRow(getNamesTable(), null, archDescriptionModel);
		} catch (ObjectNotRemovedException e1) {
			new ErrorDialog("Name link not removed", e1).showDialog();
		}
	}

	private void addSubjectRelationshipButtonActionPerformed(ActionEvent e) {
		addSubjectRelationship();
	}

	private void removeSubjectRelationshipButtonActionPerformed(ActionEvent e) {
		ArchDescription archDescriptionModel = (ArchDescription) super.getModel();
		try {
			this.removeRelatedTableRow(getSubjectsTable(), archDescriptionModel);
		} catch (ObjectNotRemovedException e1) {
			new ErrorDialog("Subject link not removed", e1).showDialog();
		}
	}

	private void initComponents() {

		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        panel6 = new JPanel();
        label2 = new JLabel();
        accessionTitle = new JTextField();
        label3 = new JLabel();
        accessionNumber = new JTextField();
        tabbedPane = new JTabbedPane();
        detailsPanel = new JPanel();
        panel11 = new JPanel();
        panel12 = new JPanel();
        label_accessionNumber1 = new JLabel();
        accessionNumber1 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Accessions.PROPERTYNAME_ACCESSION_NUMBER_1));
        accessionNumber2 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Accessions.PROPERTYNAME_ACCESSION_NUMBER_2));
        accessionNumber3 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Accessions.PROPERTYNAME_ACCESSION_NUMBER_3));
        accessionNumber4 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Accessions.PROPERTYNAME_ACCESSION_NUMBER_4));
        panel34 = new JPanel();
        label_accessionDate = new JLabel();
        accessionDate = ATBasicComponentFactory.createDateField(detailsModel.getModel(Accessions.PROPERTYNAME_ACCESSION_DATE));
        panel2 = new JPanel();
        panel15 = new JPanel();
        OtherAccessionsLabel = new JLabel();
        scrollPane4 = new JScrollPane();
        tableAccessionsResources = new DomainSortableTable();
        panel19 = new JPanel();
        linkResource = new JButton();
        removeResourceLink = new JButton();
        panel27 = new JPanel();
        label_resourceType = new JLabel();
        resourceType = ATBasicComponentFactory.createComboBox(detailsModel, Accessions.PROPERTYNAME_RESOURCE_TYPE, Accessions.class, 10);
        label_title = new JLabel();
        scrollPane42 = new JScrollPane();
        title = ATBasicComponentFactory.createTextArea(detailsModel.getModel(ArchDescription.PROPERTYNAME_TITLE));
        panel33 = new JPanel();
        panel20 = new JPanel();
        ExtentNumberLabel2 = new JLabel();
        panel21 = new JPanel();
        labelExtentNumber = new JLabel();
        extentNumber = ATBasicComponentFactory.createDoubleField(detailsModel,Accessions.PROPERTYNAME_EXTENT_NUMBER);
        extentType = ATBasicComponentFactory.createComboBox(detailsModel, Accessions.PROPERTYNAME_EXTENT_TYPE, Accessions.class);
        labelExtent = new JLabel();
        scrollPane423 = new JScrollPane();
        containerSummary = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Accessions.PROPERTYNAME_CONTAINER_SUMMARY),false);
        panel14 = new JPanel();
        label_repositoryName = new JLabel();
        repositoryName = new JTextField();
        changeRepositoryButton = new JButton();
        panel13 = new JPanel();
        panel22 = new JPanel();
        panel5 = new JPanel();
        panel4 = new JPanel();
        label_dateExpression = new JLabel();
        dateExpression = ATBasicComponentFactory.createTextField(detailsModel.getModel(ArchDescription.PROPERTYNAME_DATE_EXPRESSION),false);
        Date1Label = new JLabel();
        label_date1Begin = new JLabel();
        date1Begin = ATBasicComponentFactory.createIntegerField(detailsModel,ArchDescription.PROPERTYNAME_DATE_BEGIN);
        label_date1End = new JLabel();
        date1End = ATBasicComponentFactory.createIntegerField(detailsModel,ArchDescription.PROPERTYNAME_DATE_END);
        BulkDatesLabel = new JLabel();
        label_bulkDateBegin = new JLabel();
        bulkDateBegin = ATBasicComponentFactory.createIntegerField(detailsModel,Accessions.PROPERTYNAME_BULK_DATE_BEGIN);
        label_bulkDateEnd = new JLabel();
        bulkDateEnd = ATBasicComponentFactory.createIntegerField(detailsModel,Accessions.PROPERTYNAME_BULK_DATE_END);
        label_repositoryName3 = new JLabel();
        scrollPane6 = new JScrollPane();
        deaccessionsTable = new DomainSortableTable(Deaccessions.class);
        panel18 = new JPanel();
        addDeaccessions = new JButton();
        removeDeaccession = new JButton();
        label_repositoryName2 = new JLabel();
        scrollPane7 = new JScrollPane();
        locationsTable = new DomainSortableTable(AccessionsLocations.class);
        panel26 = new JPanel();
        addButton = new JButton();
        removeLocationButton = new JButton();
        label_title2 = new JLabel();
        scrollPane43 = new JScrollPane();
        title2 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Accessions.PROPERTYNAME_GENERAL_ACCESSION_NOTE));
        accesionNotesPanel = new JPanel();
        panel28 = new JPanel();
        panel29 = new JPanel();
        label_acquisitionType = new JLabel();
        acquisitionType = ATBasicComponentFactory.createComboBox(detailsModel, Accessions.PROPERTYNAME_ACQUISITION_TYPE, Accessions.class);
        label_RetentionRule = new JLabel();
        scrollPane4224 = new JScrollPane();
        accessionTransactionNote = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Accessions.PROPERTYNAME_RETENTION_RULE));
        label_description = new JLabel();
        scrollPane2 = new JScrollPane();
        description = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Accessions.PROPERTYNAME_DESCRIPTION));
        label_condition = new JLabel();
        scrollPane22 = new JScrollPane();
        condition = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Accessions.PROPERTYNAME_CONDITION_NOTE));
        panel30 = new JPanel();
        label_inventory = new JLabel();
        scrollPane23 = new JScrollPane();
        inventory = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Accessions.PROPERTYNAME_INVENTORY));
        label_accessionDispositionNote = new JLabel();
        scrollPane4223 = new JScrollPane();
        accessionDispositionNote = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Accessions.PROPERTYNAME_DISPOSITION_NOTE));
        label_resourceType2 = new JLabel();
        scrollPane5 = new JScrollPane();
        externalDocumentsTable = new DomainSortableTable(ExternalReference.class);
        panel16 = new JPanel();
        addExternalLinkButton = new JButton();
        removeExternalLinkButton = new JButton();
        openInBrowser = new JButton();
        panel31 = new JPanel();
        panel32 = new JPanel();
        label_acknowledgementDate2 = new JLabel();
        acknowledgementDate2 = ATBasicComponentFactory.createDateField(detailsModel.getModel(Accessions.PROPERTYNAME_USER_DEFINED_DATE1));
        label_acknowledgementDate3 = new JLabel();
        acknowledgementDate3 = ATBasicComponentFactory.createDateField(detailsModel.getModel(Accessions.PROPERTYNAME_USER_DEFINED_DATE2));
        rights2 = ATBasicComponentFactory.createCheckBox(detailsModel, Accessions.PROPERTYNAME_USER_DEFINED_BOOLEAN1, Accessions.class);
        rights3 = ATBasicComponentFactory.createCheckBox(detailsModel, Accessions.PROPERTYNAME_USER_DEFINED_BOOLEAN2, Accessions.class);
        label_date1Begin2 = new JLabel();
        date1Begin2 = ATBasicComponentFactory.createIntegerField(detailsModel,Accessions.PROPERTYNAME_USER_DEFINED_INTEGER1);
        label_date1Begin3 = new JLabel();
        date1Begin3 = ATBasicComponentFactory.createIntegerField(detailsModel,Accessions.PROPERTYNAME_USER_DEFINED_INTEGER2);
        label_date1Begin4 = new JLabel();
        extentNumber2 = ATBasicComponentFactory.createDoubleField(detailsModel,Accessions.PROPERTYNAME_USER_DEFINED_REAL1);
        label_date1Begin5 = new JLabel();
        extentNumber3 = ATBasicComponentFactory.createDoubleField(detailsModel,Accessions.PROPERTYNAME_USER_DEFINED_REAL2);
        label_date1Begin6 = new JLabel();
        dateExpression2 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Accessions.PROPERTYNAME_USER_DEFINED_STRING1),false);
        label_date1Begin7 = new JLabel();
        dateExpression3 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Accessions.PROPERTYNAME_USER_DEFINED_STRING2),false);
        label_date1Begin8 = new JLabel();
        dateExpression4 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Accessions.PROPERTYNAME_USER_DEFINED_STRING3),false);
        label_date1Begin9 = new JLabel();
        scrollPane44 = new JScrollPane();
        title3 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Accessions.PROPERTYNAME_USER_DEFINED_TEXT1));
        panel35 = new JPanel();
        label_date1Begin10 = new JLabel();
        scrollPane45 = new JScrollPane();
        title4 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Accessions.PROPERTYNAME_USER_DEFINED_TEXT2));
        label_date1Begin11 = new JLabel();
        scrollPane46 = new JScrollPane();
        title5 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Accessions.PROPERTYNAME_USER_DEFINED_TEXT3));
        label_date1Begin12 = new JLabel();
        scrollPane47 = new JScrollPane();
        title6 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Accessions.PROPERTYNAME_USER_DEFINED_TEXT4));
        nonPreferredNamePanel = new JPanel();
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
        panel10 = new JPanel();
        addSubjectRelationshipButton = new JButton();
        removeSubjectRelationshipButton = new JButton();
        collectionInfoPanel = new JPanel();
        panel39 = new JPanel();
        ExtentNumberLabel3 = new JLabel();
        rights5 = ATBasicComponentFactory.createCheckBox(detailsModel, Accessions.PROPERTYNAME_ACKNOWLEDGEMENT_SENT, Accessions.class);
        label_acknowledgementDate4 = new JLabel();
        acknowledgementDate4 = ATBasicComponentFactory.createDateField(detailsModel.getModel(Accessions.PROPERTYNAME_ACKNOWLEDGEMENT_DATE));
        rights6 = ATBasicComponentFactory.createCheckBox(detailsModel, Accessions.PROPERTYNAME_AGREEMENT_SENT, Accessions.class);
        label_agreementSent2 = new JLabel();
        agreementSent2 = ATBasicComponentFactory.createDateField(detailsModel.getModel(Accessions.PROPERTYNAME_AGREEMENT_SENT_DATE));
        rights7 = ATBasicComponentFactory.createCheckBox(detailsModel, Accessions.PROPERTYNAME_AGREEMENT_RECEIVED, Accessions.class);
        label_agreementReceived = new JLabel();
        agreementReceived = ATBasicComponentFactory.createDateField(detailsModel.getModel(Accessions.PROPERTYNAME_AGREEMENT_RECEIVED_DATE));
        rights = ATBasicComponentFactory.createCheckBox(detailsModel, Accessions.PROPERTYNAME_RIGHTS_TRANSFERRED, Accessions.class);
        label_agreementReceived2 = new JLabel();
        agreementReceived2 = ATBasicComponentFactory.createDateField(detailsModel.getModel(Accessions.PROPERTYNAME_RIGHTS_TRANSFERRED_DATE));
        labelAccess3 = new JLabel();
        scrollPane435 = new JScrollPane();
        terms3 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Accessions.PROPERTYNAME_RIGHTS_TRANSFERRED_NOTE));
        restrictionsApply = ATBasicComponentFactory.createCheckBox(detailsModel, Accessions.PROPERTYNAME_RESTRICTIONS_APPLY, Accessions.class);
        restrictionsApply2 = ATBasicComponentFactory.createCheckBox(detailsModel, Accessions.PROPERTYNAME_ACCESS_RESTRICTIONS, Accessions.class);
        labelAccess = new JLabel();
        scrollPane432 = new JScrollPane();
        terms = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Accessions.PROPERTYNAME_ACCESS_RESTRICTIONS_NOTE));
        restrictionsApply3 = ATBasicComponentFactory.createCheckBox(detailsModel, Accessions.PROPERTYNAME_USE_RESTRICTIONS, Accessions.class);
        labelAccess2 = new JLabel();
        scrollPane433 = new JScrollPane();
        terms2 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Accessions.PROPERTYNAME_USE_RESTRICTIONS_NOTE));
        separator1 = new JSeparator();
        panel37 = new JPanel();
        ExtentNumberLabel4 = new JLabel();
        label_resourceType3 = new JLabel();
        panel3 = new JPanel();
        resourceType2 = ATBasicComponentFactory.createComboBox(detailsModel, Accessions.PROPERTYNAME_PROCESSING_PRIORITY, Accessions.class,40);
        label_resourceType4 = new JLabel();
        dateExpression5 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Accessions.PROPERTYNAME_PROCESSORS),false);
        label_processingPlan = new JLabel();
        scrollPane4222 = new JScrollPane();
        processingPlan = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Accessions.PROPERTYNAME_PROCESSING_PLAN));
        label_dateProcessed3 = new JLabel();
        dateProcessed3 = ATBasicComponentFactory.createDateField(detailsModel.getModel(Accessions.PROPERTYNAME_PROCESSING_STARTED_DATE));
        label_resourceType5 = new JLabel();
        panel1 = new JPanel();
        resourceType3 = ATBasicComponentFactory.createComboBox(detailsModel, Accessions.PROPERTYNAME_PROCESSING_STATUS, Accessions.class, 40);
        cataloged2 = ATBasicComponentFactory.createCheckBox(detailsModel, Accessions.PROPERTYNAME_ACCESSION_PROCESSED, Accessions.class);
        label_dateProcessed = new JLabel();
        dateProcessed = ATBasicComponentFactory.createDateField(detailsModel.getModel(Accessions.PROPERTYNAME_ACCESSION_PROCESSED_DATE));
        cataloged = ATBasicComponentFactory.createCheckBox(detailsModel, Accessions.PROPERTYNAME_CATALOGED, Accessions.class);
        label_dateProcessed2 = new JLabel();
        dateProcessed2 = ATBasicComponentFactory.createDateField(detailsModel.getModel(Accessions.PROPERTYNAME_CATALOGED_DATE));
        label_terms2 = new JLabel();
        scrollPane434 = new JScrollPane();
        catalogedNote = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Accessions.PROPERTYNAME_CATALOGED_NOTE));
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        setBackground(new Color(200, 205, 232));
        setPreferredSize(new Dimension(1000, 560));
        setLayout(new FormLayout(
            "default:grow",
            "default, top:default:grow"));

        //======== panel6 ========
        {
            panel6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            panel6.setBackground(new Color(200, 205, 232));
            panel6.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.UNRELATED_GAP_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.UNRELATED_GAP_COLSPEC
                },
                RowSpec.decodeSpecs("default")));

            //---- label2 ----
            label2.setText("Title: ");
            label2.setForeground(new Color(0, 0, 102));
            label2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            panel6.add(label2, cc.xy(3, 1));

            //---- accessionTitle ----
            accessionTitle.setEditable(false);
            accessionTitle.setOpaque(false);
            accessionTitle.setBorder(null);
            panel6.add(accessionTitle, cc.xy(5, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

            //---- label3 ----
            label3.setText("Accession #: ");
            label3.setForeground(new Color(0, 0, 102));
            label3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            label3.setHorizontalAlignment(SwingConstants.LEFT);
            panel6.add(label3, cc.xy(7, 1));

            //---- accessionNumber ----
            accessionNumber.setEditable(false);
            accessionNumber.setOpaque(false);
            accessionNumber.setBorder(null);
            accessionNumber.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            accessionNumber.setHorizontalAlignment(SwingConstants.RIGHT);
            panel6.add(accessionNumber, cc.xy(10, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        }
        add(panel6, cc.xy(1, 1));

        //======== tabbedPane ========
        {
            tabbedPane.setFocusable(false);
            tabbedPane.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            tabbedPane.setBackground(new Color(200, 205, 232));
            tabbedPane.setOpaque(true);
            tabbedPane.setMinimumSize(new Dimension(600, 449));

            //======== detailsPanel ========
            {
                detailsPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                detailsPanel.setBackground(new Color(200, 205, 232));
                detailsPanel.setBorder(Borders.DLU2_BORDER);
                detailsPanel.setMinimumSize(new Dimension(640, 380));
                detailsPanel.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        new ColumnSpec(ColumnSpec.FILL, Sizes.PREFERRED, FormSpec.DEFAULT_GROW),
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        new ColumnSpec(ColumnSpec.FILL, Sizes.PREFERRED, FormSpec.DEFAULT_GROW)
                    },
                    RowSpec.decodeSpecs("fill:default:grow")));
                ((FormLayout)detailsPanel.getLayout()).setColumnGroups(new int[][] {{1, 3}});

                //======== panel11 ========
                {
                    panel11.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel11.setBackground(new Color(200, 205, 232));
                    panel11.setMinimumSize(new Dimension(200, 206));
                    panel11.setPreferredSize(new Dimension(200, 278));
                    panel11.setLayout(new FormLayout(
                        ColumnSpec.decodeSpecs("default:grow"),
                        new RowSpec[] {
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
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC
                        }));

                    //======== panel12 ========
                    {
                        panel12.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel12.setBackground(new Color(200, 205, 232));
                        panel12.setMinimumSize(new Dimension(200, 22));
                        panel12.setPreferredSize(new Dimension(200, 22));
                        panel12.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec("50px:grow"),
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec("50px:grow"),
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec("50px:grow"),
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec("50px:grow")
                            },
                            RowSpec.decodeSpecs("default")));
                        ((FormLayout)panel12.getLayout()).setColumnGroups(new int[][] {{3, 5, 7, 9}});

                        //---- label_accessionNumber1 ----
                        label_accessionNumber1.setText("Accession No.");
                        label_accessionNumber1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label_accessionNumber1, Accessions.class, Accessions.PROPERTYNAME_ACCESSION_NUMBER);
                        panel12.add(label_accessionNumber1, cc.xy(1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
                        panel12.add(accessionNumber1, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
                        panel12.add(accessionNumber2, cc.xy(5, 1));
                        panel12.add(accessionNumber3, cc.xy(7, 1));
                        panel12.add(accessionNumber4, cc.xy(9, 1));
                    }
                    panel11.add(panel12, cc.xy(1, 1));

                    //======== panel34 ========
                    {
                        panel34.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel34.setBackground(new Color(200, 205, 232));
                        panel34.setPreferredSize(new Dimension(200, 22));
                        panel34.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                new ColumnSpec(ColumnSpec.LEFT, Sizes.PREFERRED, FormSpec.NO_GROW),
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                            },
                            RowSpec.decodeSpecs("default")));

                        //---- label_accessionDate ----
                        label_accessionDate.setText("Accession Date");
                        label_accessionDate.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label_accessionDate, Accessions.class, Accessions.PROPERTYNAME_ACCESSION_DATE);
                        panel34.add(label_accessionDate, cc.xy(1, 1));

                        //---- accessionDate ----
                        accessionDate.setColumns(10);
                        panel34.add(accessionDate, cc.xy(3, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
                    }
                    panel11.add(panel34, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.DEFAULT));

                    //======== panel2 ========
                    {
                        panel2.setBorder(new BevelBorder(BevelBorder.LOWERED));
                        panel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel2.setBackground(new Color(182, 187, 212));
                        panel2.setMinimumSize(new Dimension(200, 92));
                        panel2.setPreferredSize(new Dimension(200, 119));
                        panel2.setLayout(new FormLayout(
                            "default:grow",
                            "fill:default:grow"));

                        //======== panel15 ========
                        {
                            panel15.setOpaque(false);
                            panel15.setBorder(Borders.DLU2_BORDER);
                            panel15.setMinimumSize(new Dimension(200, 88));
                            panel15.setPreferredSize(new Dimension(200, 115));
                            panel15.setLayout(new FormLayout(
                                ColumnSpec.decodeSpecs("default:grow"),
                                new RowSpec[] {
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.LINE_GAP_ROWSPEC,
                                    new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                    FormFactory.LINE_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC
                                }));

                            //---- OtherAccessionsLabel ----
                            OtherAccessionsLabel.setText("Resources Linked to this accession");
                            OtherAccessionsLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            panel15.add(OtherAccessionsLabel, cc.xy(1, 1));

                            //======== scrollPane4 ========
                            {
                                scrollPane4.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                                scrollPane4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                scrollPane4.setPreferredSize(new Dimension(200, 54));

                                //---- tableAccessionsResources ----
                                tableAccessionsResources.setPreferredScrollableViewportSize(new Dimension(450, 50));
                                tableAccessionsResources.setFocusable(false);
                                tableAccessionsResources.setSelectionBackground(Color.magenta);
                                scrollPane4.setViewportView(tableAccessionsResources);
                            }
                            panel15.add(scrollPane4, cc.xy(1, 3));

                            //======== panel19 ========
                            {
                                panel19.setOpaque(false);
                                panel19.setMinimumSize(new Dimension(100, 29));
                                panel19.setLayout(new FormLayout(
                                    new ColumnSpec[] {
                                        FormFactory.DEFAULT_COLSPEC,
                                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                        FormFactory.DEFAULT_COLSPEC
                                    },
                                    RowSpec.decodeSpecs("default")));

                                //---- linkResource ----
                                linkResource.setText("Link Resource");
                                linkResource.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                linkResource.setOpaque(false);
                                linkResource.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        linkResourceActionPerformed(e);
                                    }
                                });
                                panel19.add(linkResource, cc.xy(1, 1));

                                //---- removeResourceLink ----
                                removeResourceLink.setText("Remove Link");
                                removeResourceLink.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                removeResourceLink.setOpaque(false);
                                removeResourceLink.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        removeResourceLinkActionPerformed(e);
                                    }
                                });
                                panel19.add(removeResourceLink, cc.xy(3, 1));
                            }
                            panel15.add(panel19, cc.xy(1, 5, CellConstraints.CENTER, CellConstraints.DEFAULT));
                        }
                        panel2.add(panel15, cc.xy(1, 1));
                    }
                    panel11.add(panel2, cc.xy(1, 5));

                    //======== panel27 ========
                    {
                        panel27.setOpaque(false);
                        panel27.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                            },
                            RowSpec.decodeSpecs("default")));

                        //---- label_resourceType ----
                        label_resourceType.setText("Resource Type");
                        label_resourceType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label_resourceType, Accessions.class, Accessions.PROPERTYNAME_RESOURCE_TYPE);
                        panel27.add(label_resourceType, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

                        //---- resourceType ----
                        resourceType.setOpaque(false);
                        panel27.add(resourceType, cc.xy(3, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
                    }
                    panel11.add(panel27, cc.xy(1, 7));

                    //---- label_title ----
                    label_title.setText("Title");
                    label_title.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_title, Accessions.class, Accessions.PROPERTYNAME_TITLE);
                    panel11.add(label_title, cc.xy(1, 9));

                    //======== scrollPane42 ========
                    {
                        scrollPane42.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        scrollPane42.setPreferredSize(new Dimension(200, 68));
                        scrollPane42.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                        //---- title ----
                        title.setRows(4);
                        title.setLineWrap(true);
                        title.setWrapStyleWord(true);
                        scrollPane42.setViewportView(title);
                    }
                    panel11.add(scrollPane42, cc.xy(1, 11));

                    //======== panel33 ========
                    {
                        panel33.setBorder(new BevelBorder(BevelBorder.LOWERED));
                        panel33.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel33.setBackground(new Color(182, 187, 212));
                        panel33.setLayout(new FormLayout(
                            "60px:grow",
                            "fill:default:grow"));

                        //======== panel20 ========
                        {
                            panel20.setOpaque(false);
                            panel20.setBorder(Borders.DLU2_BORDER);
                            panel20.setPreferredSize(new Dimension(200, 148));
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

                            //---- ExtentNumberLabel2 ----
                            ExtentNumberLabel2.setText("Extent");
                            ExtentNumberLabel2.setForeground(new Color(0, 0, 102));
                            ExtentNumberLabel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            panel20.add(ExtentNumberLabel2, cc.xywh(1, 1, 2, 1));

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

                                //---- labelExtentNumber ----
                                labelExtentNumber.setText("Extent");
                                labelExtentNumber.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                ATFieldInfo.assignLabelInfo(labelExtentNumber, Accessions.class, Accessions.PROPERTYNAME_EXTENT_NUMBER);
                                panel21.add(labelExtentNumber, cc.xy(1, 1));

                                //---- extentNumber ----
                                extentNumber.setColumns(5);
                                panel21.add(extentNumber, cc.xy(3, 1));

                                //---- extentType ----
                                extentType.setOpaque(false);
                                panel21.add(extentType, cc.xy(5, 1));
                            }
                            panel20.add(panel21, cc.xy(2, 3));

                            //---- labelExtent ----
                            labelExtent.setText("Container Summary");
                            labelExtent.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            ATFieldInfo.assignLabelInfo(labelExtent, Accessions.class, Accessions.PROPERTYNAME_CONTAINER_SUMMARY);
                            panel20.add(labelExtent, cc.xy(2, 5));

                            //======== scrollPane423 ========
                            {
                                scrollPane423.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                                scrollPane423.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                scrollPane423.setPreferredSize(new Dimension(200, 68));

                                //---- containerSummary ----
                                containerSummary.setRows(4);
                                containerSummary.setLineWrap(true);
                                containerSummary.setWrapStyleWord(true);
                                scrollPane423.setViewportView(containerSummary);
                            }
                            panel20.add(scrollPane423, cc.xy(2, 7, CellConstraints.DEFAULT, CellConstraints.FILL));
                        }
                        panel33.add(panel20, cc.xy(1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
                    }
                    panel11.add(panel33, cc.xy(1, 13));

                    //======== panel14 ========
                    {
                        panel14.setOpaque(false);
                        panel14.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec(ColumnSpec.LEFT, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC
                            },
                            RowSpec.decodeSpecs("default")));

                        //---- label_repositoryName ----
                        label_repositoryName.setText("Repository");
                        label_repositoryName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label_repositoryName, Accessions.class, Accessions.PROPERTYNAME_REPOSITORY);
                        panel14.add(label_repositoryName, cc.xy(1, 1));

                        //---- repositoryName ----
                        repositoryName.setEditable(false);
                        repositoryName.setFocusable(false);
                        repositoryName.setBorder(null);
                        repositoryName.setOpaque(false);
                        repositoryName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        repositoryName.setHorizontalAlignment(SwingConstants.LEFT);
                        panel14.add(repositoryName, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

                        //---- changeRepositoryButton ----
                        changeRepositoryButton.setText("Change Repository");
                        changeRepositoryButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        changeRepositoryButton.setOpaque(false);
                        changeRepositoryButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                changeRepositoryButtonActionPerformed();
                            }
                        });
                        panel14.add(changeRepositoryButton, cc.xy(5, 1));
                    }
                    panel11.add(panel14, cc.xy(1, 15));
                }
                detailsPanel.add(panel11, cc.xy(1, 1));

                //======== panel13 ========
                {
                    panel13.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel13.setBackground(new Color(200, 205, 232));
                    panel13.setPreferredSize(new Dimension(200, 317));
                    panel13.setLayout(new FormLayout(
                        ColumnSpec.decodeSpecs("default:grow"),
                        new RowSpec[] {
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, 0.4),
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, 0.4),
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, 0.19999999999999998)
                        }));
                    ((FormLayout)panel13.getLayout()).setRowGroups(new int[][] {{5, 11, 17}});

                    //======== panel22 ========
                    {
                        panel22.setBorder(new BevelBorder(BevelBorder.LOWERED));
                        panel22.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel22.setBackground(new Color(182, 187, 212));
                        panel22.setLayout(new FormLayout(
                            ColumnSpec.decodeSpecs("default:grow"),
                            new RowSpec[] {
                                new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                FormFactory.RELATED_GAP_ROWSPEC
                            }));

                        //======== panel5 ========
                        {
                            panel5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            panel5.setOpaque(false);
                            panel5.setBorder(Borders.DLU2_BORDER);
                            panel5.setLayout(new FormLayout(
                                new ColumnSpec[] {
                                    new ColumnSpec("max(min;50px)"),
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

                            //======== panel4 ========
                            {
                                panel4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                panel4.setOpaque(false);
                                panel4.setLayout(new FormLayout(
                                    new ColumnSpec[] {
                                        new ColumnSpec(ColumnSpec.LEFT, Sizes.PREFERRED, FormSpec.NO_GROW),
                                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                                    },
                                    RowSpec.decodeSpecs("default:grow")));

                                //---- label_dateExpression ----
                                label_dateExpression.setText("Date Expression");
                                label_dateExpression.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                ATFieldInfo.assignLabelInfo(label_dateExpression, Accessions.class, Accessions.PROPERTYNAME_DATE_EXPRESSION);
                                panel4.add(label_dateExpression, cc.xy(1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
                                panel4.add(dateExpression, new CellConstraints(3, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 0, 0, 5)));
                            }
                            panel5.add(panel4, cc.xywh(1, 1, 9, 1));

                            //---- Date1Label ----
                            Date1Label.setText("Date");
                            Date1Label.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            panel5.add(Date1Label, cc.xywh(1, 3, 9, 1));

                            //---- label_date1Begin ----
                            label_date1Begin.setText("Begin");
                            label_date1Begin.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            ATFieldInfo.assignLabelInfo(label_date1Begin, Accessions.class, Accessions.PROPERTYNAME_DATE_BEGIN);
                            panel5.add(label_date1Begin, cc.xy(3, 5));
                            panel5.add(date1Begin, cc.xy(5, 5, CellConstraints.FILL, CellConstraints.DEFAULT));

                            //---- label_date1End ----
                            label_date1End.setText("End");
                            label_date1End.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            ATFieldInfo.assignLabelInfo(label_date1End, Accessions.class, Accessions.PROPERTYNAME_DATE_END);
                            panel5.add(label_date1End, cc.xy(7, 5));

                            //---- date1End ----
                            date1End.addFocusListener(new FocusAdapter() {
                                @Override
                                public void focusGained(FocusEvent e) {
                                    dateEndFocusGained();
                                }
                            });
                            panel5.add(date1End, new CellConstraints(9, 5, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT, new Insets(0, 0, 0, 5)));

                            //---- BulkDatesLabel ----
                            BulkDatesLabel.setText("Bulk Dates");
                            BulkDatesLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            panel5.add(BulkDatesLabel, cc.xywh(1, 7, 9, 1));

                            //---- label_bulkDateBegin ----
                            label_bulkDateBegin.setText("Begin");
                            label_bulkDateBegin.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            ATFieldInfo.assignLabelInfo(label_bulkDateBegin, Accessions.class, Accessions.PROPERTYNAME_BULK_DATE_BEGIN);
                            panel5.add(label_bulkDateBegin, cc.xy(3, 9));
                            panel5.add(bulkDateBegin, cc.xy(5, 9, CellConstraints.FILL, CellConstraints.DEFAULT));

                            //---- label_bulkDateEnd ----
                            label_bulkDateEnd.setText("End");
                            label_bulkDateEnd.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            ATFieldInfo.assignLabelInfo(label_bulkDateEnd, Accessions.class, Accessions.PROPERTYNAME_BULK_DATE_END);
                            panel5.add(label_bulkDateEnd, cc.xy(7, 9));

                            //---- bulkDateEnd ----
                            bulkDateEnd.addFocusListener(new FocusAdapter() {
                                @Override
                                public void focusGained(FocusEvent e) {
                                    bulkDateEndFocusGained();
                                }
                            });
                            panel5.add(bulkDateEnd, new CellConstraints(9, 9, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT, new Insets(0, 0, 0, 5)));
                        }
                        panel22.add(panel5, cc.xy(1, 1));
                    }
                    panel13.add(panel22, cc.xy(1, 1));

                    //---- label_repositoryName3 ----
                    label_repositoryName3.setText("Deaccessions");
                    label_repositoryName3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel13.add(label_repositoryName3, cc.xy(1, 3));

                    //======== scrollPane6 ========
                    {
                        scrollPane6.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        scrollPane6.setPreferredSize(new Dimension(200, 104));

                        //---- deaccessionsTable ----
                        deaccessionsTable.setPreferredScrollableViewportSize(new Dimension(200, 100));
                        deaccessionsTable.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                deaccessionsTableMouseClicked(e);
                            }
                        });
                        scrollPane6.setViewportView(deaccessionsTable);
                    }
                    panel13.add(scrollPane6, cc.xy(1, 5, CellConstraints.DEFAULT, CellConstraints.FILL));

                    //======== panel18 ========
                    {
                        panel18.setBackground(new Color(231, 188, 251));
                        panel18.setOpaque(false);
                        panel18.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel18.setMinimumSize(new Dimension(100, 29));
                        panel18.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC
                            },
                            RowSpec.decodeSpecs("default")));

                        //---- addDeaccessions ----
                        addDeaccessions.setText("Add Deaccession");
                        addDeaccessions.setOpaque(false);
                        addDeaccessions.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        addDeaccessions.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                addDeaccessionsActionPerformed();
                            }
                        });
                        panel18.add(addDeaccessions, cc.xy(1, 1));

                        //---- removeDeaccession ----
                        removeDeaccession.setText("Remove Deaccession");
                        removeDeaccession.setOpaque(false);
                        removeDeaccession.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        removeDeaccession.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                removeDeaccessionActionPerformed();
                            }
                        });
                        panel18.add(removeDeaccession, cc.xy(3, 1));
                    }
                    panel13.add(panel18, cc.xy(1, 7, CellConstraints.CENTER, CellConstraints.DEFAULT));

                    //---- label_repositoryName2 ----
                    label_repositoryName2.setText("Locations");
                    label_repositoryName2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_repositoryName2, Accessions.class, Accessions.PROPERTYNAME_LOCATIONS);
                    panel13.add(label_repositoryName2, cc.xy(1, 9));

                    //======== scrollPane7 ========
                    {
                        scrollPane7.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane7.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        scrollPane7.setPreferredSize(new Dimension(200, 64));

                        //---- locationsTable ----
                        locationsTable.setPreferredScrollableViewportSize(new Dimension(200, 60));
                        locationsTable.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                locationsTableMouseClicked(e);
                            }
                        });
                        scrollPane7.setViewportView(locationsTable);
                    }
                    panel13.add(scrollPane7, cc.xy(1, 11, CellConstraints.DEFAULT, CellConstraints.FILL));

                    //======== panel26 ========
                    {
                        panel26.setOpaque(false);
                        panel26.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC
                            },
                            RowSpec.decodeSpecs("default")));

                        //---- addButton ----
                        addButton.setText("Add Location");
                        addButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        addButton.setOpaque(false);
                        addButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                addLocationButtonActionPerformed(e);
                            }
                        });
                        panel26.add(addButton, cc.xy(1, 1));

                        //---- removeLocationButton ----
                        removeLocationButton.setText("Remove Location");
                        removeLocationButton.setOpaque(false);
                        removeLocationButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        removeLocationButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                removeLocationButtonActionPerformed(e);
                            }
                        });
                        panel26.add(removeLocationButton, cc.xy(3, 1));
                    }
                    panel13.add(panel26, cc.xy(1, 13, CellConstraints.CENTER, CellConstraints.DEFAULT));

                    //---- label_title2 ----
                    label_title2.setText("General accession note");
                    label_title2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_title2, Accessions.class, Accessions.PROPERTYNAME_GENERAL_ACCESSION_NOTE);
                    panel13.add(label_title2, cc.xy(1, 15));

                    //======== scrollPane43 ========
                    {
                        scrollPane43.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane43.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        scrollPane43.setPreferredSize(new Dimension(200, 68));

                        //---- title2 ----
                        title2.setRows(4);
                        title2.setLineWrap(true);
                        title2.setWrapStyleWord(true);
                        scrollPane43.setViewportView(title2);
                    }
                    panel13.add(scrollPane43, cc.xy(1, 17));
                }
                detailsPanel.add(panel13, cc.xy(3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
            }
            tabbedPane.addTab("Basic Information", detailsPanel);


            //======== accesionNotesPanel ========
            {
                accesionNotesPanel.setBackground(new Color(200, 205, 232));
                accesionNotesPanel.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                    },
                    RowSpec.decodeSpecs("fill:default:grow")));
                ((FormLayout)accesionNotesPanel.getLayout()).setColumnGroups(new int[][] {{1, 3}});

                //======== panel28 ========
                {
                    panel28.setOpaque(false);
                    panel28.setLayout(new FormLayout(
                        ColumnSpec.decodeSpecs("default:grow"),
                        new RowSpec[] {
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        }));

                    //======== panel29 ========
                    {
                        panel29.setOpaque(false);
                        panel29.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC
                            },
                            RowSpec.decodeSpecs("default")));

                        //---- label_acquisitionType ----
                        label_acquisitionType.setText("Acquisition Type");
                        label_acquisitionType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        ATFieldInfo.assignLabelInfo(label_acquisitionType, Accessions.class, Accessions.PROPERTYNAME_ACQUISITION_TYPE);
                        panel29.add(label_acquisitionType, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

                        //---- acquisitionType ----
                        acquisitionType.setOpaque(false);
                        acquisitionType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel29.add(acquisitionType, cc.xy(3, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
                    }
                    panel28.add(panel29, cc.xy(1, 1));

                    //---- label_RetentionRule ----
                    label_RetentionRule.setText("Retention Rule");
                    label_RetentionRule.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_RetentionRule, Accessions.class, Accessions.PROPERTYNAME_RETENTION_RULE);
                    panel28.add(label_RetentionRule, cc.xy(1, 3));

                    //======== scrollPane4224 ========
                    {
                        scrollPane4224.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane4224.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        scrollPane4224.setPreferredSize(new Dimension(200, 68));

                        //---- accessionTransactionNote ----
                        accessionTransactionNote.setRows(4);
                        accessionTransactionNote.setLineWrap(true);
                        accessionTransactionNote.setWrapStyleWord(true);
                        accessionTransactionNote.setMinimumSize(new Dimension(200, 16));
                        scrollPane4224.setViewportView(accessionTransactionNote);
                    }
                    panel28.add(scrollPane4224, cc.xy(1, 5, CellConstraints.FILL, CellConstraints.FILL));

                    //---- label_description ----
                    label_description.setText("Description");
                    label_description.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_description, Accessions.class, Accessions.PROPERTYNAME_DESCRIPTION);
                    panel28.add(label_description, cc.xy(1, 7));

                    //======== scrollPane2 ========
                    {
                        scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                        //---- description ----
                        description.setRows(5);
                        description.setLineWrap(true);
                        description.setWrapStyleWord(true);
                        description.setMinimumSize(new Dimension(200, 16));
                        scrollPane2.setViewportView(description);
                    }
                    panel28.add(scrollPane2, cc.xy(1, 9, CellConstraints.DEFAULT, CellConstraints.FILL));

                    //---- label_condition ----
                    label_condition.setText("Condition");
                    label_condition.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_condition, Accessions.class, Accessions.PROPERTYNAME_CONDITION_NOTE);
                    panel28.add(label_condition, cc.xy(1, 11));

                    //======== scrollPane22 ========
                    {
                        scrollPane22.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane22.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                        //---- condition ----
                        condition.setRows(5);
                        condition.setLineWrap(true);
                        condition.setWrapStyleWord(true);
                        condition.setMinimumSize(new Dimension(200, 16));
                        scrollPane22.setViewportView(condition);
                    }
                    panel28.add(scrollPane22, cc.xy(1, 13, CellConstraints.DEFAULT, CellConstraints.FILL));
                }
                accesionNotesPanel.add(panel28, cc.xy(1, 1));

                //======== panel30 ========
                {
                    panel30.setOpaque(false);
                    panel30.setLayout(new FormLayout(
                        ColumnSpec.decodeSpecs("default:grow"),
                        new RowSpec[] {
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC
                        }));

                    //---- label_inventory ----
                    label_inventory.setText("Inventory");
                    label_inventory.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_inventory, Accessions.class, Accessions.PROPERTYNAME_INVENTORY);
                    panel30.add(label_inventory, cc.xy(1, 1));

                    //======== scrollPane23 ========
                    {
                        scrollPane23.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane23.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        scrollPane23.setPreferredSize(new Dimension(200, 84));

                        //---- inventory ----
                        inventory.setRows(5);
                        inventory.setLineWrap(true);
                        inventory.setWrapStyleWord(true);
                        inventory.setMinimumSize(new Dimension(200, 16));
                        scrollPane23.setViewportView(inventory);
                    }
                    panel30.add(scrollPane23, cc.xy(1, 3, CellConstraints.DEFAULT, CellConstraints.FILL));

                    //---- label_accessionDispositionNote ----
                    label_accessionDispositionNote.setText("Accession Disposition Note");
                    label_accessionDispositionNote.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_accessionDispositionNote, Accessions.class, Accessions.PROPERTYNAME_DISPOSITION_NOTE);
                    panel30.add(label_accessionDispositionNote, cc.xy(1, 5));

                    //======== scrollPane4223 ========
                    {
                        scrollPane4223.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane4223.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        scrollPane4223.setPreferredSize(new Dimension(200, 68));

                        //---- accessionDispositionNote ----
                        accessionDispositionNote.setRows(4);
                        accessionDispositionNote.setLineWrap(true);
                        accessionDispositionNote.setWrapStyleWord(true);
                        accessionDispositionNote.setMinimumSize(new Dimension(200, 16));
                        scrollPane4223.setViewportView(accessionDispositionNote);
                    }
                    panel30.add(scrollPane4223, cc.xy(1, 7, CellConstraints.FILL, CellConstraints.FILL));

                    //---- label_resourceType2 ----
                    label_resourceType2.setText("External Documents");
                    label_resourceType2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel30.add(label_resourceType2, cc.xy(1, 9));

                    //======== scrollPane5 ========
                    {
                        scrollPane5.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        scrollPane5.setPreferredSize(new Dimension(200, 104));

                        //---- externalDocumentsTable ----
                        externalDocumentsTable.setPreferredScrollableViewportSize(new Dimension(200, 100));
                        externalDocumentsTable.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                externalDocumentsTableMouseClicked(e);
                            }
                        });
                        scrollPane5.setViewportView(externalDocumentsTable);
                    }
                    panel30.add(scrollPane5, cc.xy(1, 11, CellConstraints.DEFAULT, CellConstraints.FILL));

                    //======== panel16 ========
                    {
                        panel16.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        panel16.setBackground(new Color(200, 205, 232));
                        panel16.setMinimumSize(new Dimension(100, 29));
                        panel16.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC
                            },
                            RowSpec.decodeSpecs("default")));

                        //---- addExternalLinkButton ----
                        addExternalLinkButton.setText("Add Document");
                        addExternalLinkButton.setOpaque(false);
                        addExternalLinkButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        addExternalLinkButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                addExternalLinkButtonActionPerformed(e);
                            }
                        });
                        panel16.add(addExternalLinkButton, cc.xy(1, 1));

                        //---- removeExternalLinkButton ----
                        removeExternalLinkButton.setText("Remove Document");
                        removeExternalLinkButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        removeExternalLinkButton.setOpaque(false);
                        removeExternalLinkButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                removeExternalLinkButtonActionPerformed(e);
                            }
                        });
                        panel16.add(removeExternalLinkButton, cc.xy(3, 1));

                        //---- openInBrowser ----
                        openInBrowser.setText("Open in Browser");
                        openInBrowser.setOpaque(false);
                        openInBrowser.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                openInBrowserActionPerformed();
                            }
                        });
                        panel16.add(openInBrowser, cc.xy(5, 1));
                    }
                    panel30.add(panel16, cc.xy(1, 13, CellConstraints.CENTER, CellConstraints.DEFAULT));
                }
                accesionNotesPanel.add(panel30, cc.xy(3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
            }
            tabbedPane.addTab("Accession Notes", accesionNotesPanel);


            //======== panel31 ========
            {
                panel31.setBackground(new Color(200, 205, 232));
                panel31.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                    },
                    RowSpec.decodeSpecs("fill:default:grow")));
                ((FormLayout)panel31.getLayout()).setColumnGroups(new int[][] {{1, 3}});

                //======== panel32 ========
                {
                    panel32.setOpaque(false);
                    panel32.setLayout(new FormLayout(
                        new ColumnSpec[] {
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
                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        }));

                    //---- label_acknowledgementDate2 ----
                    label_acknowledgementDate2.setText("User Defined Date 1");
                    label_acknowledgementDate2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_acknowledgementDate2, Accessions.class, Accessions.PROPERTYNAME_USER_DEFINED_DATE1);
                    panel32.add(label_acknowledgementDate2, cc.xy(1, 1));

                    //---- acknowledgementDate2 ----
                    acknowledgementDate2.setColumns(10);
                    panel32.add(acknowledgementDate2, cc.xy(3, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                    //---- label_acknowledgementDate3 ----
                    label_acknowledgementDate3.setText("User Defined Date 2");
                    label_acknowledgementDate3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_acknowledgementDate3, Accessions.class, Accessions.PROPERTYNAME_USER_DEFINED_DATE2);
                    panel32.add(label_acknowledgementDate3, cc.xy(1, 3));

                    //---- acknowledgementDate3 ----
                    acknowledgementDate3.setColumns(10);
                    panel32.add(acknowledgementDate3, cc.xy(3, 3, CellConstraints.LEFT, CellConstraints.DEFAULT));

                    //---- rights2 ----
                    rights2.setText("User Defined Boolean 1");
                    rights2.setOpaque(false);
                    rights2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    rights2.setText(ATFieldInfo.getLabel(Accessions.class, Accessions.PROPERTYNAME_USER_DEFINED_BOOLEAN1));
                    panel32.add(rights2, cc.xywh(1, 5, 3, 1));

                    //---- rights3 ----
                    rights3.setText("User Defined Boolean 2");
                    rights3.setOpaque(false);
                    rights3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    rights3.setText(ATFieldInfo.getLabel(Accessions.class, Accessions.PROPERTYNAME_USER_DEFINED_BOOLEAN2));
                    panel32.add(rights3, cc.xywh(1, 7, 3, 1));

                    //---- label_date1Begin2 ----
                    label_date1Begin2.setText("User Defined Integer 1");
                    label_date1Begin2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_date1Begin2, Accessions.class, Accessions.PROPERTYNAME_USER_DEFINED_INTEGER1);
                    panel32.add(label_date1Begin2, cc.xy(1, 9));

                    //---- date1Begin2 ----
                    date1Begin2.setColumns(6);
                    panel32.add(date1Begin2, cc.xy(3, 9, CellConstraints.LEFT, CellConstraints.DEFAULT));

                    //---- label_date1Begin3 ----
                    label_date1Begin3.setText("User Defined Integer 2");
                    label_date1Begin3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_date1Begin3, Accessions.class, Accessions.PROPERTYNAME_USER_DEFINED_INTEGER2);
                    panel32.add(label_date1Begin3, cc.xy(1, 11));

                    //---- date1Begin3 ----
                    date1Begin3.setColumns(6);
                    panel32.add(date1Begin3, cc.xy(3, 11, CellConstraints.LEFT, CellConstraints.DEFAULT));

                    //---- label_date1Begin4 ----
                    label_date1Begin4.setText("User Defined Real 1");
                    label_date1Begin4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_date1Begin4, Accessions.class, Accessions.PROPERTYNAME_USER_DEFINED_REAL1);
                    panel32.add(label_date1Begin4, cc.xy(1, 13));

                    //---- extentNumber2 ----
                    extentNumber2.setColumns(5);
                    panel32.add(extentNumber2, cc.xy(3, 13, CellConstraints.LEFT, CellConstraints.DEFAULT));

                    //---- label_date1Begin5 ----
                    label_date1Begin5.setText("User Defined Real 2");
                    label_date1Begin5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_date1Begin5, Accessions.class, Accessions.PROPERTYNAME_USER_DEFINED_REAL2);
                    panel32.add(label_date1Begin5, cc.xy(1, 15));

                    //---- extentNumber3 ----
                    extentNumber3.setColumns(5);
                    panel32.add(extentNumber3, cc.xy(3, 15, CellConstraints.LEFT, CellConstraints.DEFAULT));

                    //---- label_date1Begin6 ----
                    label_date1Begin6.setText("User Defined String 1");
                    label_date1Begin6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_date1Begin6, Accessions.class, Accessions.PROPERTYNAME_USER_DEFINED_STRING1);
                    panel32.add(label_date1Begin6, cc.xy(1, 17));
                    panel32.add(dateExpression2, new CellConstraints(3, 17, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 0, 0, 5)));

                    //---- label_date1Begin7 ----
                    label_date1Begin7.setText("User Defined String 2");
                    label_date1Begin7.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_date1Begin7, Accessions.class, Accessions.PROPERTYNAME_USER_DEFINED_STRING2);
                    panel32.add(label_date1Begin7, cc.xy(1, 19));
                    panel32.add(dateExpression3, new CellConstraints(3, 19, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 0, 0, 5)));

                    //---- label_date1Begin8 ----
                    label_date1Begin8.setText("User Defined String 3");
                    label_date1Begin8.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_date1Begin8, Accessions.class, Accessions.PROPERTYNAME_USER_DEFINED_STRING3);
                    panel32.add(label_date1Begin8, cc.xy(1, 21));
                    panel32.add(dateExpression4, new CellConstraints(3, 21, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 0, 0, 5)));

                    //---- label_date1Begin9 ----
                    label_date1Begin9.setText("User Defined Text 1");
                    label_date1Begin9.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_date1Begin9, Accessions.class, Accessions.PROPERTYNAME_USER_DEFINED_TEXT1);
                    panel32.add(label_date1Begin9, cc.xy(1, 23));

                    //======== scrollPane44 ========
                    {
                        scrollPane44.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane44.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        scrollPane44.setPreferredSize(new Dimension(200, 68));

                        //---- title3 ----
                        title3.setRows(4);
                        title3.setLineWrap(true);
                        title3.setWrapStyleWord(true);
                        title3.setMinimumSize(new Dimension(200, 16));
                        scrollPane44.setViewportView(title3);
                    }
                    panel32.add(scrollPane44, cc.xywh(1, 25, 3, 1));
                }
                panel31.add(panel32, cc.xy(1, 1));

                //======== panel35 ========
                {
                    panel35.setOpaque(false);
                    panel35.setLayout(new FormLayout(
                        ColumnSpec.decodeSpecs("default:grow"),
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
                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        }));

                    //---- label_date1Begin10 ----
                    label_date1Begin10.setText("User Defined Text 2");
                    label_date1Begin10.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_date1Begin10, Accessions.class, Accessions.PROPERTYNAME_USER_DEFINED_TEXT2);
                    panel35.add(label_date1Begin10, cc.xy(1, 1));

                    //======== scrollPane45 ========
                    {
                        scrollPane45.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane45.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        scrollPane45.setPreferredSize(new Dimension(200, 68));

                        //---- title4 ----
                        title4.setRows(4);
                        title4.setLineWrap(true);
                        title4.setWrapStyleWord(true);
                        title4.setMinimumSize(new Dimension(200, 16));
                        scrollPane45.setViewportView(title4);
                    }
                    panel35.add(scrollPane45, cc.xy(1, 3));

                    //---- label_date1Begin11 ----
                    label_date1Begin11.setText("User Defined Text 3");
                    label_date1Begin11.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_date1Begin11, Accessions.class, Accessions.PROPERTYNAME_USER_DEFINED_TEXT3);
                    panel35.add(label_date1Begin11, cc.xy(1, 5));

                    //======== scrollPane46 ========
                    {
                        scrollPane46.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane46.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        scrollPane46.setPreferredSize(new Dimension(200, 68));

                        //---- title5 ----
                        title5.setRows(4);
                        title5.setLineWrap(true);
                        title5.setWrapStyleWord(true);
                        title5.setMinimumSize(new Dimension(200, 16));
                        scrollPane46.setViewportView(title5);
                    }
                    panel35.add(scrollPane46, cc.xy(1, 7));

                    //---- label_date1Begin12 ----
                    label_date1Begin12.setText("User Defined Text 4");
                    label_date1Begin12.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_date1Begin12, Accessions.class, Accessions.PROPERTYNAME_USER_DEFINED_TEXT4);
                    panel35.add(label_date1Begin12, cc.xy(1, 9));

                    //======== scrollPane47 ========
                    {
                        scrollPane47.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane47.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        scrollPane47.setPreferredSize(new Dimension(200, 68));

                        //---- title6 ----
                        title6.setRows(4);
                        title6.setLineWrap(true);
                        title6.setWrapStyleWord(true);
                        title6.setMinimumSize(new Dimension(200, 16));
                        scrollPane47.setViewportView(title6);
                    }
                    panel35.add(scrollPane47, cc.xy(1, 11));
                }
                panel31.add(panel35, cc.xy(3, 1));
            }
            tabbedPane.addTab("User defined fields", panel31);


            //======== nonPreferredNamePanel ========
            {
                nonPreferredNamePanel.setBackground(new Color(200, 205, 232));
                nonPreferredNamePanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                nonPreferredNamePanel.setBorder(Borders.DLU2_BORDER);
                nonPreferredNamePanel.setLayout(new FormLayout(
                    ColumnSpec.decodeSpecs("default:grow"),
                    new RowSpec[] {
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
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
                ((FormLayout)nonPreferredNamePanel.getLayout()).setRowGroups(new int[][] {{3, 11}});

                //---- SubjectsLabel2 ----
                SubjectsLabel2.setText("Names");
                SubjectsLabel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                nonPreferredNamePanel.add(SubjectsLabel2, cc.xy(1, 1));

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
                nonPreferredNamePanel.add(scrollPane1, cc.xy(1, 3, CellConstraints.DEFAULT, CellConstraints.FILL));

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
                    addNameRelationshipButton.setText("Add Name Link");
                    addNameRelationshipButton.setOpaque(false);
                    addNameRelationshipButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    addNameRelationshipButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            addNameRelationshipButtonActionPerformed(e);
                        }
                    });
                    panel8.add(addNameRelationshipButton, cc.xy(3, 1));

                    //---- removeNameRelationshipButton ----
                    removeNameRelationshipButton.setText("Remove Name Link");
                    removeNameRelationshipButton.setOpaque(false);
                    removeNameRelationshipButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    removeNameRelationshipButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            removeNameRelationshipButtonActionPerformed(e);
                        }
                    });
                    panel8.add(removeNameRelationshipButton, cc.xy(5, 1));
                }
                nonPreferredNamePanel.add(panel8, cc.xy(1, 5, CellConstraints.CENTER, CellConstraints.DEFAULT));

                //---- separator5 ----
                separator5.setBackground(new Color(220, 220, 232));
                separator5.setForeground(new Color(147, 131, 86));
                separator5.setMinimumSize(new Dimension(1, 10));
                separator5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                nonPreferredNamePanel.add(separator5, cc.xy(1, 7));

                //---- SubjectsLabel ----
                SubjectsLabel.setText("Subjects");
                SubjectsLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                nonPreferredNamePanel.add(SubjectsLabel, cc.xy(1, 9));

                //======== scrollPane3 ========
                {
                    scrollPane3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                    scrollPane3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                    //---- subjectsTable ----
                    subjectsTable.setPreferredScrollableViewportSize(new Dimension(200, 200));
                    scrollPane3.setViewportView(subjectsTable);
                }
                nonPreferredNamePanel.add(scrollPane3, cc.xy(1, 11));

                //======== panel10 ========
                {
                    panel10.setBackground(new Color(231, 188, 251));
                    panel10.setOpaque(false);
                    panel10.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel10.setLayout(new FormLayout(
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
                            addSubjectRelationshipButtonActionPerformed(e);
                        }
                    });
                    panel10.add(addSubjectRelationshipButton, cc.xy(1, 1));

                    //---- removeSubjectRelationshipButton ----
                    removeSubjectRelationshipButton.setText("Remove Subject Link");
                    removeSubjectRelationshipButton.setOpaque(false);
                    removeSubjectRelationshipButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    removeSubjectRelationshipButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            removeSubjectRelationshipButtonActionPerformed(e);
                        }
                    });
                    panel10.add(removeSubjectRelationshipButton, cc.xy(3, 1));
                }
                nonPreferredNamePanel.add(panel10, cc.xy(1, 13, CellConstraints.CENTER, CellConstraints.DEFAULT));
            }
            tabbedPane.addTab("Names & Subjects", nonPreferredNamePanel);


            //======== collectionInfoPanel ========
            {
                collectionInfoPanel.setBackground(new Color(200, 205, 232));
                collectionInfoPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                collectionInfoPanel.setBorder(Borders.DLU2_BORDER);
                collectionInfoPanel.setMinimumSize(new Dimension(640, 403));
                collectionInfoPanel.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                    },
                    RowSpec.decodeSpecs("fill:default:grow")));
                ((FormLayout)collectionInfoPanel.getLayout()).setColumnGroups(new int[][] {{1, 5}});

                //======== panel39 ========
                {
                    panel39.setOpaque(false);
                    panel39.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            new ColumnSpec(ColumnSpec.LEFT, Sizes.DLUX14, FormSpec.NO_GROW),
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
                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        }));

                    //---- ExtentNumberLabel3 ----
                    ExtentNumberLabel3.setText("Acknowledgements & Restrictions");
                    ExtentNumberLabel3.setForeground(new Color(0, 0, 102));
                    ExtentNumberLabel3.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
                    panel39.add(ExtentNumberLabel3, cc.xywh(1, 1, 7, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

                    //---- rights5 ----
                    rights5.setText("Acknowledgement Sent");
                    rights5.setOpaque(false);
                    rights5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    rights5.setText(ATFieldInfo.getLabel(Accessions.class, Accessions.PROPERTYNAME_ACKNOWLEDGEMENT_SENT));
                    panel39.add(rights5, cc.xy(1, 3));

                    //---- label_acknowledgementDate4 ----
                    label_acknowledgementDate4.setText("Acknowledgement Date");
                    label_acknowledgementDate4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_acknowledgementDate4, Accessions.class, Accessions.PROPERTYNAME_ACKNOWLEDGEMENT_DATE);
                    panel39.add(label_acknowledgementDate4, cc.xy(5, 3));

                    //---- acknowledgementDate4 ----
                    acknowledgementDate4.setColumns(10);
                    panel39.add(acknowledgementDate4, cc.xy(7, 3, CellConstraints.LEFT, CellConstraints.DEFAULT));

                    //---- rights6 ----
                    rights6.setText("Agreement Sent");
                    rights6.setOpaque(false);
                    rights6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    rights6.setText(ATFieldInfo.getLabel(Accessions.class, Accessions.PROPERTYNAME_AGREEMENT_SENT));
                    panel39.add(rights6, cc.xy(1, 5));

                    //---- label_agreementSent2 ----
                    label_agreementSent2.setText("Agreement Sent Date");
                    label_agreementSent2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_agreementSent2, Accessions.class, Accessions.PROPERTYNAME_AGREEMENT_SENT_DATE);
                    panel39.add(label_agreementSent2, cc.xy(5, 5));

                    //---- agreementSent2 ----
                    agreementSent2.setColumns(10);
                    panel39.add(agreementSent2, cc.xy(7, 5, CellConstraints.LEFT, CellConstraints.DEFAULT));

                    //---- rights7 ----
                    rights7.setText("Agreement Received");
                    rights7.setOpaque(false);
                    rights7.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    rights7.setText(ATFieldInfo.getLabel(Accessions.class, Accessions.PROPERTYNAME_AGREEMENT_RECEIVED));
                    panel39.add(rights7, cc.xy(1, 7));

                    //---- label_agreementReceived ----
                    label_agreementReceived.setText("Agreement Received Date");
                    label_agreementReceived.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_agreementReceived, Accessions.class, Accessions.PROPERTYNAME_AGREEMENT_RECEIVED_DATE);
                    panel39.add(label_agreementReceived, cc.xy(5, 7));

                    //---- agreementReceived ----
                    agreementReceived.setColumns(10);
                    panel39.add(agreementReceived, cc.xy(7, 7, CellConstraints.LEFT, CellConstraints.DEFAULT));

                    //---- rights ----
                    rights.setText("Rights Transferred");
                    rights.setOpaque(false);
                    rights.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    rights.setText(ATFieldInfo.getLabel(Accessions.class, Accessions.PROPERTYNAME_RIGHTS_TRANSFERRED));
                    panel39.add(rights, cc.xy(1, 9));

                    //---- label_agreementReceived2 ----
                    label_agreementReceived2.setText("Rights Transferred Date");
                    label_agreementReceived2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_agreementReceived2, Accessions.class, Accessions.PROPERTYNAME_RIGHTS_TRANSFERRED_DATE);
                    panel39.add(label_agreementReceived2, cc.xy(5, 9));

                    //---- agreementReceived2 ----
                    agreementReceived2.setColumns(10);
                    panel39.add(agreementReceived2, cc.xy(7, 9, CellConstraints.LEFT, CellConstraints.DEFAULT));

                    //---- labelAccess3 ----
                    labelAccess3.setText("Rights Transferred Note");
                    labelAccess3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(labelAccess3, Accessions.class, Accessions.PROPERTYNAME_RIGHTS_TRANSFERRED_NOTE);
                    panel39.add(labelAccess3, cc.xy(1, 11));

                    //======== scrollPane435 ========
                    {
                        scrollPane435.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane435.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                        //---- terms3 ----
                        terms3.setRows(4);
                        terms3.setLineWrap(true);
                        terms3.setWrapStyleWord(true);
                        terms3.setMinimumSize(new Dimension(200, 16));
                        scrollPane435.setViewportView(terms3);
                    }
                    panel39.add(scrollPane435, cc.xywh(1, 13, 7, 1));

                    //---- restrictionsApply ----
                    restrictionsApply.setText("Restrictions Apply");
                    restrictionsApply.setOpaque(false);
                    restrictionsApply.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    restrictionsApply.setText(ATFieldInfo.getLabel(Accessions.class, Accessions.PROPERTYNAME_RESTRICTIONS_APPLY));
                    panel39.add(restrictionsApply, cc.xy(1, 15));

                    //---- restrictionsApply2 ----
                    restrictionsApply2.setText("Access Restrictions");
                    restrictionsApply2.setOpaque(false);
                    restrictionsApply2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    restrictionsApply2.setText(ATFieldInfo.getLabel(Accessions.class, Accessions.PROPERTYNAME_ACCESS_RESTRICTIONS));
                    panel39.add(restrictionsApply2, cc.xy(1, 17));

                    //---- labelAccess ----
                    labelAccess.setText("Access Restrictions Note");
                    labelAccess.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(labelAccess, Accessions.class, Accessions.PROPERTYNAME_ACCESS_RESTRICTIONS_NOTE);
                    panel39.add(labelAccess, cc.xy(1, 19));

                    //======== scrollPane432 ========
                    {
                        scrollPane432.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane432.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        scrollPane432.setPreferredSize(new Dimension(200, 68));

                        //---- terms ----
                        terms.setRows(4);
                        terms.setLineWrap(true);
                        terms.setWrapStyleWord(true);
                        terms.setMinimumSize(new Dimension(200, 16));
                        scrollPane432.setViewportView(terms);
                    }
                    panel39.add(scrollPane432, cc.xywh(1, 21, 7, 1));

                    //---- restrictionsApply3 ----
                    restrictionsApply3.setText("Use Restrictions");
                    restrictionsApply3.setOpaque(false);
                    restrictionsApply3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    restrictionsApply3.setText(ATFieldInfo.getLabel(Accessions.class, Accessions.PROPERTYNAME_USE_RESTRICTIONS));
                    panel39.add(restrictionsApply3, cc.xy(1, 23));

                    //---- labelAccess2 ----
                    labelAccess2.setText("Use Restrictions Note");
                    labelAccess2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(labelAccess2, Accessions.class, Accessions.PROPERTYNAME_USE_RESTRICTIONS_NOTE);
                    panel39.add(labelAccess2, cc.xy(1, 25));

                    //======== scrollPane433 ========
                    {
                        scrollPane433.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane433.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                        //---- terms2 ----
                        terms2.setRows(4);
                        terms2.setLineWrap(true);
                        terms2.setWrapStyleWord(true);
                        terms2.setMinimumSize(new Dimension(200, 16));
                        scrollPane433.setViewportView(terms2);
                    }
                    panel39.add(scrollPane433, cc.xywh(1, 27, 7, 1));
                }
                collectionInfoPanel.add(panel39, cc.xy(1, 1));

                //---- separator1 ----
                separator1.setOrientation(SwingConstants.VERTICAL);
                collectionInfoPanel.add(separator1, cc.xy(3, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

                //======== panel37 ========
                {
                    panel37.setOpaque(false);
                    panel37.setLayout(new FormLayout(
                        new ColumnSpec[] {
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
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        }));

                    //---- ExtentNumberLabel4 ----
                    ExtentNumberLabel4.setText("Processing Tasks");
                    ExtentNumberLabel4.setForeground(new Color(0, 0, 102));
                    ExtentNumberLabel4.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
                    panel37.add(ExtentNumberLabel4, cc.xywh(1, 1, 5, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

                    //---- label_resourceType3 ----
                    label_resourceType3.setText("Processing Priority");
                    label_resourceType3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_resourceType3, Accessions.class, Accessions.PROPERTYNAME_PROCESSING_PRIORITY);
                    panel37.add(label_resourceType3, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.DEFAULT));

                    //======== panel3 ========
                    {
                        panel3.setOpaque(false);
                        panel3.setLayout(new FormLayout(
                            "default:grow",
                            "default"));

                        //---- resourceType2 ----
                        resourceType2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        resourceType2.setOpaque(false);
                        panel3.add(resourceType2, cc.xy(1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
                    }
                    panel37.add(panel3, cc.xywh(3, 3, 3, 1));

                    //---- label_resourceType4 ----
                    label_resourceType4.setText("Processor");
                    label_resourceType4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_resourceType4, Accessions.class, Accessions.PROPERTYNAME_PROCESSORS);
                    panel37.add(label_resourceType4, cc.xy(1, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
                    panel37.add(dateExpression5, new CellConstraints(3, 5, 3, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 0, 0, 5)));

                    //---- label_processingPlan ----
                    label_processingPlan.setText("Processing Plan");
                    label_processingPlan.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_processingPlan, Accessions.class, Accessions.PROPERTYNAME_PROCESSING_PLAN);
                    panel37.add(label_processingPlan, cc.xywh(1, 7, 3, 1));

                    //======== scrollPane4222 ========
                    {
                        scrollPane4222.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane4222.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        scrollPane4222.setPreferredSize(new Dimension(200, 68));

                        //---- processingPlan ----
                        processingPlan.setRows(4);
                        processingPlan.setLineWrap(true);
                        processingPlan.setWrapStyleWord(true);
                        processingPlan.setMinimumSize(new Dimension(200, 16));
                        scrollPane4222.setViewportView(processingPlan);
                    }
                    panel37.add(scrollPane4222, cc.xywh(1, 9, 5, 1));

                    //---- label_dateProcessed3 ----
                    label_dateProcessed3.setText("Processing Started");
                    label_dateProcessed3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_dateProcessed3, Accessions.class, Accessions.PROPERTYNAME_PROCESSING_STARTED_DATE);
                    panel37.add(label_dateProcessed3, cc.xy(1, 11));

                    //---- dateProcessed3 ----
                    dateProcessed3.setColumns(10);
                    panel37.add(dateProcessed3, cc.xy(3, 11, CellConstraints.LEFT, CellConstraints.DEFAULT));

                    //---- label_resourceType5 ----
                    label_resourceType5.setText("Processing Status");
                    label_resourceType5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_resourceType5, Accessions.class, Accessions.PROPERTYNAME_PROCESSING_STATUS);
                    panel37.add(label_resourceType5, cc.xy(1, 13, CellConstraints.FILL, CellConstraints.DEFAULT));

                    //======== panel1 ========
                    {
                        panel1.setOpaque(false);
                        panel1.setLayout(new FormLayout(
                            "default:grow",
                            "default"));

                        //---- resourceType3 ----
                        resourceType3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        resourceType3.setOpaque(false);
                        panel1.add(resourceType3, cc.xy(1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
                    }
                    panel37.add(panel1, cc.xywh(3, 13, 3, 1));

                    //---- cataloged2 ----
                    cataloged2.setText("Accession Processed");
                    cataloged2.setOpaque(false);
                    cataloged2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    cataloged2.setText(ATFieldInfo.getLabel(Accessions.class, Accessions.PROPERTYNAME_ACCESSION_PROCESSED));
                    panel37.add(cataloged2, cc.xy(1, 15));

                    //---- label_dateProcessed ----
                    label_dateProcessed.setText("Date Processed");
                    label_dateProcessed.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_dateProcessed, Accessions.class, Accessions.PROPERTYNAME_ACCESSION_PROCESSED_DATE);
                    panel37.add(label_dateProcessed, cc.xy(3, 15));

                    //---- dateProcessed ----
                    dateProcessed.setColumns(10);
                    panel37.add(dateProcessed, cc.xy(5, 15, CellConstraints.LEFT, CellConstraints.DEFAULT));

                    //---- cataloged ----
                    cataloged.setText("Cataloged");
                    cataloged.setOpaque(false);
                    cataloged.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    cataloged.setText(ATFieldInfo.getLabel(Accessions.class, Accessions.PROPERTYNAME_CATALOGED));
                    panel37.add(cataloged, cc.xy(1, 17));

                    //---- label_dateProcessed2 ----
                    label_dateProcessed2.setText("Date Cataloged");
                    label_dateProcessed2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_dateProcessed2, Accessions.class, Accessions.PROPERTYNAME_CATALOGED_DATE);
                    panel37.add(label_dateProcessed2, cc.xy(3, 17));

                    //---- dateProcessed2 ----
                    dateProcessed2.setColumns(10);
                    panel37.add(dateProcessed2, cc.xy(5, 17, CellConstraints.LEFT, CellConstraints.DEFAULT));

                    //---- label_terms2 ----
                    label_terms2.setText("Cataloged Note:");
                    label_terms2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label_terms2, Accessions.class, Accessions.PROPERTYNAME_CATALOGED_NOTE);
                    panel37.add(label_terms2, cc.xywh(1, 19, 3, 1));

                    //======== scrollPane434 ========
                    {
                        scrollPane434.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane434.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        scrollPane434.setPreferredSize(new Dimension(200, 68));

                        //---- catalogedNote ----
                        catalogedNote.setRows(4);
                        catalogedNote.setLineWrap(true);
                        catalogedNote.setWrapStyleWord(true);
                        catalogedNote.setMinimumSize(new Dimension(200, 16));
                        scrollPane434.setViewportView(catalogedNote);
                    }
                    panel37.add(scrollPane434, cc.xywh(1, 21, 5, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
                }
                collectionInfoPanel.add(panel37, cc.xy(5, 1));
            }
            tabbedPane.addTab("Acknowledgements, Restrictions & Processing Tasks", collectionInfoPanel);

        }
        add(tabbedPane, cc.xy(1, 2, CellConstraints.DEFAULT, CellConstraints.FILL));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}


	protected void addRelatedTableInformation() {
		tableAccessionsResources.setClazzAndColumns(AccessionsResources.PROPERTYNAME_RESOURCE_IDENTIFIER,
				AccessionsResources.class,
				AccessionsResources.PROPERTYNAME_RESOURCE_IDENTIFIER,
				AccessionsResources.PROPERTYNAME_RESOURCE_TITLE);
	}

	private void linkResourceActionPerformed(ActionEvent e) {
		ResourceLookup resourcePicker = new ResourceLookup(getParentEditor(), this);
		resourcePicker.showDialog(this);
//		if (resourcePicker.showDialog(this) == javax.swing.JOptionPane.OK_OPTION) {
//			Accessions accession = (Accessions) super.getModel();
//			AccessionsResources accessionResource =
//					new AccessionsResources(resourcePicker.getSelectedResource(), accession);
//			accession.addAccessionsResources(accessionResource);
//			tableAccessionsResources.updateCollection(accession.getResources());
//		}
	}

	private void removeResourceLinkActionPerformed(ActionEvent e) {
		try {
			this.removeRelatedTableRow(tableAccessionsResources, null, super.getModel());
		} catch (ObjectNotRemovedException e1) {
			new ErrorDialog("Resource link not removed", e1).showDialog();
		}
	}

	private void dateEndFocusGained() {
		Accessions accession = (Accessions) super.getModel();
		if (accession.getDateEnd() == null) {
			accession.setDateEnd((Integer) date1Begin.getValue());
			/* After a formatted text field gains focus, it replaces its text with its
			 * current value, formatted appropriately of course. It does this _after_
			 * any focus listeners are notified. So, if we are editable, we queue
			 * up a selectAll to be done after the current events in the thread are done. */
			Runnable doSelect = new Runnable() {
				public void run() {
					date1End.selectAll();
				}
			};
			SwingUtilities.invokeLater(doSelect);
		}
	}

	private void bulkDateEndFocusGained() {
		Accessions accession = (Accessions) super.getModel();
		if (accession.getBulkDateEnd() == null) {
			accession.setBulkDateEnd((Integer) bulkDateBegin.getValue());
			Runnable doSelect = new Runnable() {
				public void run() {
					bulkDateEnd.selectAll();
				}
			};
			SwingUtilities.invokeLater(doSelect);
		}
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel panel6;
    private JLabel label2;
    private JTextField accessionTitle;
    private JLabel label3;
    private JTextField accessionNumber;
    private JTabbedPane tabbedPane;
    private JPanel detailsPanel;
    private JPanel panel11;
    private JPanel panel12;
    private JLabel label_accessionNumber1;
    public JTextField accessionNumber1;
    public JTextField accessionNumber2;
    public JTextField accessionNumber3;
    public JTextField accessionNumber4;
    private JPanel panel34;
    private JLabel label_accessionDate;
    private JFormattedTextField accessionDate;
    private JPanel panel2;
    private JPanel panel15;
    private JLabel OtherAccessionsLabel;
    private JScrollPane scrollPane4;
    private DomainSortableTable tableAccessionsResources;
    private JPanel panel19;
    private JButton linkResource;
    private JButton removeResourceLink;
    private JPanel panel27;
    private JLabel label_resourceType;
    public JComboBox resourceType;
    private JLabel label_title;
    private JScrollPane scrollPane42;
    public JTextArea title;
    private JPanel panel33;
    private JPanel panel20;
    private JLabel ExtentNumberLabel2;
    private JPanel panel21;
    private JLabel labelExtentNumber;
    public JFormattedTextField extentNumber;
    public JComboBox extentType;
    private JLabel labelExtent;
    private JScrollPane scrollPane423;
    public JTextArea containerSummary;
    private JPanel panel14;
    private JLabel label_repositoryName;
    public JTextField repositoryName;
    private JButton changeRepositoryButton;
    private JPanel panel13;
    private JPanel panel22;
    private JPanel panel5;
    private JPanel panel4;
    private JLabel label_dateExpression;
    public JTextField dateExpression;
    private JLabel Date1Label;
    private JLabel label_date1Begin;
    public JFormattedTextField date1Begin;
    private JLabel label_date1End;
    public JFormattedTextField date1End;
    private JLabel BulkDatesLabel;
    private JLabel label_bulkDateBegin;
    public JFormattedTextField bulkDateBegin;
    private JLabel label_bulkDateEnd;
    public JFormattedTextField bulkDateEnd;
    private JLabel label_repositoryName3;
    private JScrollPane scrollPane6;
    private DomainSortableTable deaccessionsTable;
    private JPanel panel18;
    private JButton addDeaccessions;
    private JButton removeDeaccession;
    private JLabel label_repositoryName2;
    private JScrollPane scrollPane7;
    private DomainSortableTable locationsTable;
    private JPanel panel26;
    private JButton addButton;
    private JButton removeLocationButton;
    private JLabel label_title2;
    private JScrollPane scrollPane43;
    public JTextArea title2;
    private JPanel accesionNotesPanel;
    private JPanel panel28;
    private JPanel panel29;
    private JLabel label_acquisitionType;
    public JComboBox acquisitionType;
    private JLabel label_RetentionRule;
    private JScrollPane scrollPane4224;
    public JTextArea accessionTransactionNote;
    private JLabel label_description;
    private JScrollPane scrollPane2;
    public JTextArea description;
    private JLabel label_condition;
    private JScrollPane scrollPane22;
    public JTextArea condition;
    private JPanel panel30;
    private JLabel label_inventory;
    private JScrollPane scrollPane23;
    public JTextArea inventory;
    private JLabel label_accessionDispositionNote;
    private JScrollPane scrollPane4223;
    public JTextArea accessionDispositionNote;
    private JLabel label_resourceType2;
    private JScrollPane scrollPane5;
    private DomainSortableTable externalDocumentsTable;
    private JPanel panel16;
    private JButton addExternalLinkButton;
    private JButton removeExternalLinkButton;
    private JButton openInBrowser;
    private JPanel panel31;
    private JPanel panel32;
    private JLabel label_acknowledgementDate2;
    public JFormattedTextField acknowledgementDate2;
    private JLabel label_acknowledgementDate3;
    public JFormattedTextField acknowledgementDate3;
    public JCheckBox rights2;
    public JCheckBox rights3;
    private JLabel label_date1Begin2;
    public JFormattedTextField date1Begin2;
    private JLabel label_date1Begin3;
    public JFormattedTextField date1Begin3;
    private JLabel label_date1Begin4;
    public JFormattedTextField extentNumber2;
    private JLabel label_date1Begin5;
    public JFormattedTextField extentNumber3;
    private JLabel label_date1Begin6;
    public JTextField dateExpression2;
    private JLabel label_date1Begin7;
    public JTextField dateExpression3;
    private JLabel label_date1Begin8;
    public JTextField dateExpression4;
    private JLabel label_date1Begin9;
    private JScrollPane scrollPane44;
    public JTextArea title3;
    private JPanel panel35;
    private JLabel label_date1Begin10;
    private JScrollPane scrollPane45;
    public JTextArea title4;
    private JLabel label_date1Begin11;
    private JScrollPane scrollPane46;
    public JTextArea title5;
    private JLabel label_date1Begin12;
    private JScrollPane scrollPane47;
    public JTextArea title6;
    private JPanel nonPreferredNamePanel;
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
    private JPanel panel10;
    private JButton addSubjectRelationshipButton;
    private JButton removeSubjectRelationshipButton;
    private JPanel collectionInfoPanel;
    private JPanel panel39;
    private JLabel ExtentNumberLabel3;
    public JCheckBox rights5;
    private JLabel label_acknowledgementDate4;
    public JFormattedTextField acknowledgementDate4;
    public JCheckBox rights6;
    private JLabel label_agreementSent2;
    public JFormattedTextField agreementSent2;
    public JCheckBox rights7;
    private JLabel label_agreementReceived;
    public JFormattedTextField agreementReceived;
    public JCheckBox rights;
    private JLabel label_agreementReceived2;
    public JFormattedTextField agreementReceived2;
    private JLabel labelAccess3;
    private JScrollPane scrollPane435;
    public JTextArea terms3;
    public JCheckBox restrictionsApply;
    public JCheckBox restrictionsApply2;
    private JLabel labelAccess;
    private JScrollPane scrollPane432;
    public JTextArea terms;
    public JCheckBox restrictionsApply3;
    private JLabel labelAccess2;
    private JScrollPane scrollPane433;
    public JTextArea terms2;
    private JSeparator separator1;
    private JPanel panel37;
    private JLabel ExtentNumberLabel4;
    private JLabel label_resourceType3;
    private JPanel panel3;
    public JComboBox resourceType2;
    private JLabel label_resourceType4;
    public JTextField dateExpression5;
    private JLabel label_processingPlan;
    private JScrollPane scrollPane4222;
    public JTextArea processingPlan;
    private JLabel label_dateProcessed3;
    public JFormattedTextField dateProcessed3;
    private JLabel label_resourceType5;
    private JPanel panel1;
    public JComboBox resourceType3;
    public JCheckBox cataloged2;
    private JLabel label_dateProcessed;
    public JFormattedTextField dateProcessed;
    public JCheckBox cataloged;
    private JLabel label_dateProcessed2;
    public JFormattedTextField dateProcessed2;
    private JLabel label_terms2;
    private JScrollPane scrollPane434;
    public JTextArea catalogedNote;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	//

	public final void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		Accessions accessionsModel = (Accessions) model;
		namesTable.updateCollection(accessionsModel.getNames());
		subjectsTable.updateCollection(accessionsModel.getSubjects());
		tableAccessionsResources.updateCollection(accessionsModel.getResources());
		externalDocumentsTable.updateCollection(accessionsModel.getRepeatingData());
		deaccessionsTable.updateCollection(accessionsModel.getDeaccessions());
		locationsTable.updateCollection(accessionsModel.getLocations());
		accessionTitle.setText(accessionsModel.getTitle());
		accessionNumber.setText(accessionsModel.getAccessionNumber());
		setRepositoryText(accessionsModel);
        setPluginModel();
	}

	public Component getInitialFocusComponent() {
		return accessionNumber1;
	}

	private void setRepositoryText(Accessions model) {
		if (model.getRepository() == null) {
			this.repositoryName.setText("");
		} else {
			this.repositoryName.setText(model.getRepository().getShortName());
		}
	}

	public DomainSortableTable getNamesTable() {
		return namesTable;
	}

	public JButton getAddNameRelationshipButton() {
		return addNameRelationshipButton;
	}

	public void setAddNameRelationshipButton(JButton addNameRelationshipButton) {
		this.addNameRelationshipButton = addNameRelationshipButton;
	}

	public JButton getAddSubjectRelationshipButton() {
		return addSubjectRelationshipButton;
	}

	public void setAddSubjectRelationshipButton(JButton addSubjectRelationshipButton) {
		this.addSubjectRelationshipButton = addSubjectRelationshipButton;
	}

	public DomainSortableTable getSubjectsTable() {
		return subjectsTable;
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

	public DomainSortableTable getTableAccessionsResources() {
		return tableAccessionsResources;
	}

	protected void initAccess() {
		super.initAccess();
		if (!Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_ADVANCED_DATA_ENTRY)) {
			setFormToReadOnly();

            addDeaccessions.setVisible(false);
            removeDeaccession.setVisible(false);
            extentType.setEnabled(false);
			resourceType.setEnabled(false);
			acquisitionType.setEnabled(false);
//			convertComboBoxToNonEnterableTextField(panel33, extentType, Accessions.PROPERTYNAME_EXTENT_TYPE);
//			convertComboBoxToNonEnterableTextField(panel3, resourceType, Accessions.PROPERTYNAME_RESOURCE_TYPE);
//			convertComboBoxToNonEnterableTextField(panel7, acquisitionType, Accessions.PROPERTYNAME_ACQUISITION_TYPE);
			linkResource.setVisible(false);
			removeResourceLink.setVisible(false);
			addExternalLinkButton.setVisible(false);
			removeExternalLinkButton.setVisible(false);
            editNameRelationshipButton.setVisible(false);
			addNameRelationshipButton.setVisible(false);
			removeNameRelationshipButton.setVisible(false);
			addSubjectRelationshipButton.setVisible(false);
			removeSubjectRelationshipButton.setVisible(false);

            // disable the  "Processing Priority" and "Processing Status" combo boxes
            resourceType2.setEnabled(false);
            resourceType3.setEnabled(false);
		}

	}

	protected void setDisplayToFirstTab() {
		this.tabbedPane.setSelectedIndex(0);
	}

    /**
     * Method that initializes any embedded plugins that would add an editor
     */
    private void initPlugins() {
        plugins = ATPluginFactory.getInstance().getEmbeddedAccessionEditorPlugins();
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
