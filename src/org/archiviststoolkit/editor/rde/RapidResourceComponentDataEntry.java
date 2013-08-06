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
 * Created by JFormDesigner on Mon Nov 06 13:12:42 EST 2006
 */

package org.archiviststoolkit.editor.rde;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.validation.ValidationResult;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.StandardEditor;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.NotesEtcTypes;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.util.JGoodiesValidationUtils;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.NoteEtcTypesUtils;
import org.archiviststoolkit.editor.ResourceEditor;
import org.archiviststoolkit.editor.rde.RapidResourceComponentDataEntryValidator;

public class RapidResourceComponentDataEntry extends JDialog {

	private RapidResourceComponentDataEntryValidator validator;

	public RapidResourceComponentDataEntry(Frame owner) {
		super(owner);
		initComponents();
		setInstanceTypeList();
		validator = new RapidResourceComponentDataEntryValidator(this);
		this.getRootPane().setDefaultButton(this.okAndAnotherButton);
	}

	public RapidResourceComponentDataEntry(Dialog owner) {
		super(owner);
		initComponents();
		setInstanceTypeList();
		validator = new RapidResourceComponentDataEntryValidator(this);
		this.getRootPane().setDefaultButton(this.okAndAnotherButton);
	}

	private void okButtonActionPerformed() {
		if (validateData()) {
			status = JOptionPane.OK_OPTION;
			this.setVisible(false);
		}
	}

	private void cancelButtonActionPerformed() {
		status = javax.swing.JOptionPane.CANCEL_OPTION;
		this.setVisible(false);
	}

	public JButton getOkAndAnotherButton() {
		return okAndAnotherButton;
	}

	private void okAndAnotherButtonActionPerformed() {
		if (validateData()) {
			status = StandardEditor.OK_AND_ANOTHER_OPTION;
			this.setVisible(false);
		}
	}

	private void setInstanceTypeList() {
		Vector<String> values = LookupListUtils.getLookupListValues(ArchDescriptionInstances.class, ArchDescriptionInstances.PROPERTYNAME_INSTANCE_TYPE);
		values.remove("Digital object");
		instanceType.setModel(new DefaultComboBoxModel(values));
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
		panel4 = new JPanel();
		label_resourcesLevel = new JLabel();
		level = ATBasicComponentFactory.createUnboundComboBox(LookupListUtils.getLookupListValues(ResourcesComponents.class, ResourcesComponents.PROPERTYNAME_LEVEL));
		internalOnly = new JCheckBox();
		restrictionsApply = new JCheckBox();
		panel13 = new JPanel();
		label_resourcesTitle2 = new JLabel();
		componentTitle = new JTextField();
		label_resourcesLevel2 = new JLabel();
		componentUniqueIdentifier = new JTextField();
		panel5 = new JPanel();
		label_resourcesDateExpression = new JLabel();
		dateExpression = new JTextField();
		label_resourcesDateBegin = new JLabel();
		dateBegin = ATBasicComponentFactory.createUnboundIntegerField(false);
		label_resourcesDateEnd = new JLabel();
		dateEnd = ATBasicComponentFactory.createUnboundIntegerField(false);
		panel6 = new JPanel();
		label_resourcesExtentNumber = new JLabel();
		extent = ATBasicComponentFactory.createUnboundDoubleField();
		label_resourcesExtentNumber2 = new JLabel();
		extentType = ATBasicComponentFactory.createUnboundComboBox(LookupListUtils.getLookupListValues(Resources.class, Resources.PROPERTYNAME_EXTENT_TYPE));
		separator3 = new JSeparator();
		panel9 = new JPanel();
		label_resourcesExtentNumber4 = new JLabel();
		noteType1 = ATBasicComponentFactory.createUnboundComboBox(NoteEtcTypesUtils.getNotesOnlyTypesList(true));
		scrollPane1 = new JScrollPane();
		note1 = new JTextArea();
		separator4 = new JSeparator();
		panel10 = new JPanel();
		label_resourcesExtentNumber6 = new JLabel();
		noteType2 = ATBasicComponentFactory.createUnboundComboBox(NoteEtcTypesUtils.getNotesOnlyTypesList(true));
		scrollPane2 = new JScrollPane();
		note2 = new JTextArea();
		separator5 = new JSeparator();
		panel11 = new JPanel();
		label_resourcesExtentNumber7 = new JLabel();
		noteType3 = ATBasicComponentFactory.createUnboundComboBox(NoteEtcTypesUtils.getNotesOnlyTypesList(true));
		scrollPane3 = new JScrollPane();
		note3 = new JTextArea();
		separator6 = new JSeparator();
		panel12 = new JPanel();
		label_resourcesExtentNumber3 = new JLabel();
		instanceType = new JComboBox();
		panel7 = new JPanel();
		label_subjectTermType = new JLabel();
		container1Type = ATBasicComponentFactory.createUnboundComboBox(LookupListUtils.getLookupListValues(ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER1_TYPE));
		label_subjectSource = new JLabel();
		container1Number = ATBasicComponentFactory.createUnboundDoubleField();
		label_subjectSource3 = new JLabel();
		container1AlphaNum = new JTextField();
		panel8 = new JPanel();
		label_subjectTermType2 = new JLabel();
		container2Type = ATBasicComponentFactory.createUnboundComboBox(LookupListUtils.getLookupListValues(ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER2_TYPE));
		label_subjectSource2 = new JLabel();
		container2Number = ATBasicComponentFactory.createUnboundDoubleField();
		label_subjectSource4 = new JLabel();
		container2AlphaNum = new JTextField();
		panel14 = new JPanel();
		label_resourcesExtentNumber5 = new JLabel();
		barcode = new JTextField();
		panel1 = new JPanel();
		separator2 = new JSeparator();
		buttonPanel = new JPanel();
		cancelButton = new JButton();
		okButton = new JButton();
		okAndAnotherButton = new JButton();
		cancelButtonLabel = new JLabel();
		okButtonLabel = new JLabel();
		okAndAnotherButtonLabel = new JLabel();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBackground(new Color(200, 205, 232));
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
					panel2.setBackground(new Color(73, 43, 104));
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
					mainHeaderLabel.setText("Resources");
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
					subHeaderLabel.setText("Rapid Data Entry");
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
				contentPanel.setBorder(Borders.DIALOG_BORDER);
				contentPanel.setLayout(new FormLayout(
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
						new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
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

				//======== panel4 ========
				{
					panel4.setOpaque(false);
					panel4.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC
						},
						RowSpec.decodeSpecs("default")));

					//---- label_resourcesLevel ----
					label_resourcesLevel.setText("Level");
					label_resourcesLevel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_resourcesLevel, Resources.class, Resources.PROPERTYNAME_LEVEL);
					panel4.add(label_resourcesLevel, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

					//---- level ----
					level.setOpaque(false);
					panel4.add(level, cc.xy(3, 1));

					//---- internalOnly ----
					internalOnly.setText("Internal Only");
					internalOnly.setOpaque(false);
					internalOnly.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					internalOnly.setText(ATFieldInfo.getLabel(ResourcesComponents.class, ResourcesComponents.PROPERTYNAME_INTERNAL_ONLY));
					panel4.add(internalOnly, cc.xy(5, 1));

					//---- restrictionsApply ----
					restrictionsApply.setText("Restrictions Apply");
					restrictionsApply.setOpaque(false);
					restrictionsApply.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					restrictionsApply.setText(ATFieldInfo.getLabel(ResourcesComponents.class, ArchDescription.PROPERTYNAME_RESTRICTIONS_APPLY));
					panel4.add(restrictionsApply, cc.xy(7, 1));
				}
				contentPanel.add(panel4, cc.xywh(1, 1, 3, 1));

				//======== panel13 ========
				{
					panel13.setOpaque(false);
					panel13.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						},
						RowSpec.decodeSpecs("default")));

					//---- label_resourcesTitle2 ----
					label_resourcesTitle2.setText("Title");
					label_resourcesTitle2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_resourcesTitle2, Resources.class, Resources.PROPERTYNAME_TITLE);
					panel13.add(label_resourcesTitle2, cc.xy(1, 1));
					panel13.add(componentTitle, cc.xy(3, 1));
				}
				contentPanel.add(panel13, cc.xywh(1, 3, 3, 1));

				//---- label_resourcesLevel2 ----
				label_resourcesLevel2.setText("Component Unique Identifier");
				label_resourcesLevel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_resourcesLevel2, ResourcesComponents.class, ResourcesComponents.PROPERTYNAME_UNIQUE_IDENTIFIER);
				contentPanel.add(label_resourcesLevel2, cc.xywh(1, 5, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
				contentPanel.add(componentUniqueIdentifier, cc.xy(3, 5));

				//======== panel5 ========
				{
					panel5.setOpaque(false);
					panel5.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
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

					//---- label_resourcesDateExpression ----
					label_resourcesDateExpression.setText("Date Expression");
					label_resourcesDateExpression.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_resourcesDateExpression, Resources.class, Resources.PROPERTYNAME_DATE_EXPRESSION);
					panel5.add(label_resourcesDateExpression, cc.xywh(1, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
					panel5.add(dateExpression, cc.xy(3, 1));

					//---- label_resourcesDateBegin ----
					label_resourcesDateBegin.setText("Begin");
					label_resourcesDateBegin.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_resourcesDateBegin, Resources.class, Resources.PROPERTYNAME_DATE_BEGIN);
					panel5.add(label_resourcesDateBegin, cc.xy(5, 1));

					//---- dateBegin ----
					dateBegin.setColumns(5);
					panel5.add(dateBegin, cc.xywh(7, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

					//---- label_resourcesDateEnd ----
					label_resourcesDateEnd.setText("End");
					label_resourcesDateEnd.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_resourcesDateEnd, Resources.class, Resources.PROPERTYNAME_DATE_END);
					panel5.add(label_resourcesDateEnd, cc.xy(9, 1));

					//---- dateEnd ----
					dateEnd.setColumns(5);
					panel5.add(dateEnd, cc.xywh(11, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
				}
				contentPanel.add(panel5, cc.xywh(1, 7, 3, 1));

				//======== panel6 ========
				{
					panel6.setOpaque(false);
					panel6.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						},
						RowSpec.decodeSpecs("default")));

					//---- label_resourcesExtentNumber ----
					label_resourcesExtentNumber.setText("Extent");
					label_resourcesExtentNumber.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_resourcesExtentNumber, Resources.class, Resources.PROPERTYNAME_EXTENT_NUMBER);
					panel6.add(label_resourcesExtentNumber, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

					//---- extent ----
					extent.setColumns(5);
					panel6.add(extent, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

					//---- label_resourcesExtentNumber2 ----
					label_resourcesExtentNumber2.setText("Extent Type");
					label_resourcesExtentNumber2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_resourcesExtentNumber2, Resources.class, Resources.PROPERTYNAME_EXTENT_TYPE);
					panel6.add(label_resourcesExtentNumber2, cc.xywh(5, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

					//---- extentType ----
					extentType.setOpaque(false);
					panel6.add(extentType, cc.xywh(7, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
				}
				contentPanel.add(panel6, cc.xywh(1, 9, 3, 1));

				//---- separator3 ----
				separator3.setBackground(new Color(220, 220, 232));
				separator3.setForeground(new Color(147, 131, 86));
				separator3.setMinimumSize(new Dimension(1, 10));
				separator3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contentPanel.add(separator3, cc.xywh(1, 11, 3, 1));

				//======== panel9 ========
				{
					panel9.setOpaque(false);
					panel9.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						},
						RowSpec.decodeSpecs("default")));

					//---- label_resourcesExtentNumber4 ----
					label_resourcesExtentNumber4.setText("Note 1");
					label_resourcesExtentNumber4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel9.add(label_resourcesExtentNumber4, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

					//---- noteType1 ----
					noteType1.setOpaque(false);
					noteType1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel9.add(noteType1, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
				}
				contentPanel.add(panel9, cc.xywh(1, 13, 3, 1));

				//======== scrollPane1 ========
				{

					//---- note1 ----
					note1.setRows(4);
					note1.setWrapStyleWord(true);
					note1.setLineWrap(true);
					scrollPane1.setViewportView(note1);
				}
				contentPanel.add(scrollPane1, cc.xywh(1, 15, 3, 1));

				//---- separator4 ----
				separator4.setBackground(new Color(220, 220, 232));
				separator4.setForeground(new Color(147, 131, 86));
				separator4.setMinimumSize(new Dimension(1, 10));
				separator4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contentPanel.add(separator4, cc.xywh(1, 17, 3, 1));

				//======== panel10 ========
				{
					panel10.setOpaque(false);
					panel10.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						},
						RowSpec.decodeSpecs("default")));

					//---- label_resourcesExtentNumber6 ----
					label_resourcesExtentNumber6.setText("Note 2");
					label_resourcesExtentNumber6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel10.add(label_resourcesExtentNumber6, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

					//---- noteType2 ----
					noteType2.setOpaque(false);
					noteType2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel10.add(noteType2, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
				}
				contentPanel.add(panel10, cc.xywh(1, 19, 3, 1));

				//======== scrollPane2 ========
				{

					//---- note2 ----
					note2.setRows(4);
					note2.setWrapStyleWord(true);
					note2.setLineWrap(true);
					scrollPane2.setViewportView(note2);
				}
				contentPanel.add(scrollPane2, cc.xywh(1, 21, 3, 1));

				//---- separator5 ----
				separator5.setBackground(new Color(220, 220, 232));
				separator5.setForeground(new Color(147, 131, 86));
				separator5.setMinimumSize(new Dimension(1, 10));
				separator5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contentPanel.add(separator5, cc.xywh(1, 23, 3, 1));

				//======== panel11 ========
				{
					panel11.setOpaque(false);
					panel11.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						},
						RowSpec.decodeSpecs("default")));

					//---- label_resourcesExtentNumber7 ----
					label_resourcesExtentNumber7.setText("Note 3");
					label_resourcesExtentNumber7.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel11.add(label_resourcesExtentNumber7, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

					//---- noteType3 ----
					noteType3.setOpaque(false);
					noteType3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel11.add(noteType3, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
				}
				contentPanel.add(panel11, cc.xywh(1, 25, 3, 1));

				//======== scrollPane3 ========
				{

					//---- note3 ----
					note3.setRows(4);
					note3.setWrapStyleWord(true);
					note3.setLineWrap(true);
					scrollPane3.setViewportView(note3);
				}
				contentPanel.add(scrollPane3, cc.xywh(1, 27, 3, 1));

				//---- separator6 ----
				separator6.setBackground(new Color(220, 220, 232));
				separator6.setForeground(new Color(147, 131, 86));
				separator6.setMinimumSize(new Dimension(1, 10));
				separator6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contentPanel.add(separator6, cc.xywh(1, 29, 3, 1));

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

					//---- label_resourcesExtentNumber3 ----
					label_resourcesExtentNumber3.setText("Instance Type");
					label_resourcesExtentNumber3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_resourcesExtentNumber3, ArchDescriptionInstances.class, ArchDescriptionInstances.PROPERTYNAME_INSTANCE_TYPE);
					panel12.add(label_resourcesExtentNumber3, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

					//---- instanceType ----
					instanceType.setOpaque(false);
					panel12.add(instanceType, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
				}
				contentPanel.add(panel12, cc.xywh(1, 31, 3, 1));

				//======== panel7 ========
				{
					panel7.setOpaque(false);
					panel7.setPreferredSize(new Dimension(900, 27));
					panel7.setLayout(new FormLayout(
						new ColumnSpec[] {
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
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						},
						RowSpec.decodeSpecs("default")));

					//---- label_subjectTermType ----
					label_subjectTermType.setText("Container 1 Type");
					label_subjectTermType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_subjectTermType, ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER1_TYPE);
					panel7.add(label_subjectTermType, cc.xy(1, 1));

					//---- container1Type ----
					container1Type.setOpaque(false);
					panel7.add(container1Type, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

					//---- label_subjectSource ----
					label_subjectSource.setText("Container 1 Numeric Indicator");
					label_subjectSource.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_subjectSource, ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER1_NUMERIC_INDICATOR);
					panel7.add(label_subjectSource, cc.xy(5, 1));

					//---- container1Number ----
					container1Number.setColumns(5);
					panel7.add(container1Number, cc.xywh(7, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

					//---- label_subjectSource3 ----
					label_subjectSource3.setText("Container 1 Alphanumeric Indicator");
					label_subjectSource3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_subjectSource3, ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER1_ALPHA_NUMERIC_INDICATOR);
					panel7.add(label_subjectSource3, cc.xy(9, 1));
					panel7.add(container1AlphaNum, cc.xywh(11, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
				}
				contentPanel.add(panel7, cc.xywh(1, 33, 3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

				//======== panel8 ========
				{
					panel8.setOpaque(false);
					panel8.setLayout(new FormLayout(
						new ColumnSpec[] {
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
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						},
						RowSpec.decodeSpecs("default")));

					//---- label_subjectTermType2 ----
					label_subjectTermType2.setText("Container 2 Type");
					label_subjectTermType2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_subjectTermType2, ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER2_TYPE);
					panel8.add(label_subjectTermType2, cc.xy(1, 1));

					//---- container2Type ----
					container2Type.setOpaque(false);
					panel8.add(container2Type, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

					//---- label_subjectSource2 ----
					label_subjectSource2.setText("Container 2 Numeric Indicator");
					label_subjectSource2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_subjectSource2, ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER2_NUMERIC_INDICATOR);
					panel8.add(label_subjectSource2, cc.xy(5, 1));

					//---- container2Number ----
					container2Number.setColumns(5);
					panel8.add(container2Number, cc.xywh(7, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

					//---- label_subjectSource4 ----
					label_subjectSource4.setText("Container 2 Alphanumeric Indicator");
					label_subjectSource4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_subjectSource4, ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER2_ALPHA_NUMERIC_INDICATOR);
					panel8.add(label_subjectSource4, cc.xy(9, 1));
					panel8.add(container2AlphaNum, cc.xywh(11, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
				}
				contentPanel.add(panel8, cc.xywh(1, 35, 3, 1));

				//======== panel14 ========
				{
					panel14.setOpaque(false);
					panel14.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						},
						RowSpec.decodeSpecs("default")));

					//---- label_resourcesExtentNumber5 ----
					label_resourcesExtentNumber5.setText("Barcode");
					label_resourcesExtentNumber5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_resourcesExtentNumber5, ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_BARCODE);
					panel14.add(label_resourcesExtentNumber5, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
					panel14.add(barcode, cc.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
				}
				contentPanel.add(panel14, cc.xywh(1, 37, 3, 1));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			//======== panel1 ========
			{
				panel1.setOpaque(false);
				panel1.setBorder(Borders.DLU2_BORDER);
				panel1.setLayout(new FormLayout(
					"default:grow",
					"default, default:grow"));

				//---- separator2 ----
				separator2.setBackground(new Color(220, 220, 232));
				separator2.setForeground(new Color(147, 131, 86));
				separator2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				panel1.add(separator2, cc.xy(1, 1));

				//======== buttonPanel ========
				{
					buttonPanel.setBorder(null);
					buttonPanel.setBackground(new Color(200, 205, 232));
					buttonPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					buttonPanel.setMinimumSize(new Dimension(380, 60));
					buttonPanel.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.UNRELATED_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.UNRELATED_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC
						},
						RowSpec.decodeSpecs("default, default")));

					//---- cancelButton ----
					cancelButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/cancel.jpg")));
					cancelButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					cancelButton.setOpaque(false);
					cancelButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							cancelButtonActionPerformed();
						}
					});
					buttonPanel.add(cancelButton, cc.xy(1, 1));

					//---- okButton ----
					okButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/ok.jpg")));
					okButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					okButton.setOpaque(false);
					okButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							okButtonActionPerformed();
						}
					});
					buttonPanel.add(okButton, cc.xy(3, 1));

					//---- okAndAnotherButton ----
					okAndAnotherButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/okPlus1.jpg")));
					okAndAnotherButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					okAndAnotherButton.setOpaque(false);
					okAndAnotherButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							okAndAnotherButtonActionPerformed();
						}
					});
					buttonPanel.add(okAndAnotherButton, cc.xy(5, 1));

					//---- cancelButtonLabel ----
					cancelButtonLabel.setText("Cancel");
					cancelButtonLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					buttonPanel.add(cancelButtonLabel, cc.xywh(1, 2, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

					//---- okButtonLabel ----
					okButtonLabel.setText("OK");
					okButtonLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					buttonPanel.add(okButtonLabel, cc.xywh(3, 2, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

					//---- okAndAnotherButtonLabel ----
					okAndAnotherButtonLabel.setText("OK + 1");
					okAndAnotherButtonLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					buttonPanel.add(okAndAnotherButtonLabel, cc.xywh(5, 2, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
				}
				panel1.add(buttonPanel, cc.xywh(1, 2, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
			}
			dialogPane.add(panel1, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
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
	private JPanel panel4;
	private JLabel label_resourcesLevel;
	private JComboBox level;
	public JCheckBox internalOnly;
	public JCheckBox restrictionsApply;
	private JPanel panel13;
	private JLabel label_resourcesTitle2;
	private JTextField componentTitle;
	private JLabel label_resourcesLevel2;
	private JTextField componentUniqueIdentifier;
	private JPanel panel5;
	private JLabel label_resourcesDateExpression;
	private JTextField dateExpression;
	private JLabel label_resourcesDateBegin;
	private JFormattedTextField dateBegin;
	private JLabel label_resourcesDateEnd;
	private JFormattedTextField dateEnd;
	private JPanel panel6;
	private JLabel label_resourcesExtentNumber;
	private JFormattedTextField extent;
	private JLabel label_resourcesExtentNumber2;
	private JComboBox extentType;
	private JSeparator separator3;
	private JPanel panel9;
	private JLabel label_resourcesExtentNumber4;
	public JComboBox noteType1;
	private JScrollPane scrollPane1;
	private JTextArea note1;
	private JSeparator separator4;
	private JPanel panel10;
	private JLabel label_resourcesExtentNumber6;
	public JComboBox noteType2;
	private JScrollPane scrollPane2;
	private JTextArea note2;
	private JSeparator separator5;
	private JPanel panel11;
	private JLabel label_resourcesExtentNumber7;
	public JComboBox noteType3;
	private JScrollPane scrollPane3;
	private JTextArea note3;
	private JSeparator separator6;
	private JPanel panel12;
	private JLabel label_resourcesExtentNumber3;
	private JComboBox instanceType;
	private JPanel panel7;
	private JLabel label_subjectTermType;
	private JComboBox container1Type;
	private JLabel label_subjectSource;
	private JFormattedTextField container1Number;
	private JLabel label_subjectSource3;
	private JTextField container1AlphaNum;
	private JPanel panel8;
	private JLabel label_subjectTermType2;
	private JComboBox container2Type;
	private JLabel label_subjectSource2;
	private JFormattedTextField container2Number;
	private JLabel label_subjectSource4;
	private JTextField container2AlphaNum;
	private JPanel panel14;
	private JLabel label_resourcesExtentNumber5;
	private JTextField barcode;
	private JPanel panel1;
	private JSeparator separator2;
	private JPanel buttonPanel;
	protected JButton cancelButton;
	protected JButton okButton;
	protected JButton okAndAnotherButton;
	private JLabel cancelButtonLabel;
	private JLabel okButtonLabel;
	private JLabel okAndAnotherButtonLabel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	/**
	 * The status of the editor.
	 */
	protected int status = 0;

	/**
	 * Displays the dialog box representing the editor.
	 *
	 * @return true if it displayed okay
	 */

	public final int showDialog() {

		this.pack();
		setLocationRelativeTo(this.getOwner());
		clearFields();
		this.setVisible(true);
		if (getComponentTitle().length() != 0) {
			componentTitle.requestFocusInWindow();
		} else {
			level.requestFocusInWindow();
		}
		return (status);
	}

	private void clearFields() {
		componentTitle.setText("");
		componentUniqueIdentifier.setText("");
		dateExpression.setText("");
		dateBegin.setText("");
		dateEnd.setText("");
		extent.setText("");
		container2Number.setText("");
		container2AlphaNum.setText("");
		barcode.setText("");
		internalOnly.setSelected(false);
		restrictionsApply.setSelected(false);
	}

	protected Integer getDateBegin() {
		if (dateBegin.getValue() == null) {
			return 0;
		} else {
			return (Integer)dateBegin.getValue();
		}
	}

	protected Integer getDateEnd() {
		if (dateEnd.getValue() == null) {
			return 0;
		} else {
			return (Integer)dateEnd.getValue();
		}
	}

	protected String getLevel() {
		if (level.getSelectedItem() == null) {
			return "";
		} else {
			return (String)level.getSelectedItem();
		}
	}

	public String getDateExpression() {
		if (dateExpression.getText() == null) {
			return "";
		} else {
			return dateExpression.getText();
		}
	}

	protected String getContainer1Type() {
		if (container1Type.getSelectedItem() == null) {
			return "";
		} else {
			return (String)container1Type.getSelectedItem();
		}
	}

	protected String getContainer2Type() {
		if (container2Type.getSelectedItem() == null) {
			return "";
		} else {
			return (String)container2Type.getSelectedItem();
		}
	}

	protected String getContainer1AlphaNumericValue() {
		if (container1AlphaNum.getText() == null) {
			return "";
		} else {
			return (String)container1AlphaNum.getText();
		}
	}

	protected String getContainer2AlphaNumericValue() {
		if (container2AlphaNum.getText() == null) {
			return "";
		} else {
			return (String)container2AlphaNum.getText();
		}
	}

	protected Double getContainer1NumericValue() {
		if (container1Number.getValue() == null) {
			return 0d;
		} else {
			return (Double)container1Number.getValue();
		}
	}

	protected Double getContainer2NumericValue() {
		if (container2Number.getValue() == null) {
			return 0d;
		} else {
			return (Double)container2Number.getValue();
		}
	}

	protected String getComponentTitle() {
		if (componentTitle.getText() == null) {
			return "";
		} else {
			return componentTitle.getText();
		}
	}

	protected String getInstanceType() {
		if (instanceType.getSelectedItem() == null) {
			return "";
		} else {
			return (String)instanceType.getSelectedItem();
		}
	}

	protected String getComponentUniqueIdentifier() {
		if (componentUniqueIdentifier.getText() == null) {
			return "";
		} else {
			return componentUniqueIdentifier.getText();
		}
	}

	protected String getBarcode() {
		if (barcode.getText() == null) {
			return "";
		} else {
			return barcode.getText();
		}
	}

	public ResourcesComponents generateComponentRecord() {
		ResourcesComponents component = new ResourcesComponents();
		component.setTitle(getComponentTitle());
		component.setLevel(getLevel());
		component.setDateExpression(getDateExpression());
		if (getDateBegin() != 0) {
			component.setDateBegin(getDateBegin());
		}
		if (getDateEnd() != 0) {
			component.setDateEnd(getDateEnd());
		}
		component.setExtentNumber((Double)extent.getValue());
		component.setExtentType((String)extentType.getSelectedItem());
		component.setComponentUniqueIdentifier(getComponentUniqueIdentifier());
		component.setInternalOnly(internalOnly.isSelected());
		component.setRestrictionsApply(restrictionsApply.isSelected());
		if (getContainer1Type().length() > 0) {
			ArchDescriptionAnalogInstances instance = new ArchDescriptionAnalogInstances(component);
			instance.setInstanceType(getInstanceType());
			instance.setContainer1Type(getContainer1Type());
			instance.setContainer2Type(getContainer2Type());
			instance.setContainer1NumericIndicator(getContainer1NumericValue());
			instance.setContainer1AlphaNumericIndicator(getContainer1AlphaNumericValue());
			instance.setContainer2NumericIndicator(getContainer2NumericValue());
			instance.setContainer2AlphaNumericIndicator(getContainer2AlphaNumericValue());
			instance.setBarcode(getBarcode());
			component.addInstance(instance);
		}
		addNoteToComponent(component, (NotesEtcTypes)noteType1.getSelectedItem(), note1.getText(), 1);
		addNoteToComponent(component, (NotesEtcTypes)noteType2.getSelectedItem(), note2.getText(), 2);
		addNoteToComponent(component, (NotesEtcTypes)noteType3.getSelectedItem(), note3.getText(), 3);
		component.resequenceSequencedObjects();
		return component;
	}

	private void addNoteToComponent(ResourcesComponents component, NotesEtcTypes noteType, String noteContent, int location) {
		if (noteType != null && noteContent != null && noteContent.length() > 0) {
			ArchDescriptionNotes note = new ArchDescriptionNotes(component, "", NotesEtcTypes.DATA_TYPE_NOTE, noteType, noteContent);
			if (this.getOwner() instanceof ResourceEditor) {
				note.setPersistentId(((ResourceEditor) this.getOwner()).getNextPersistentId());
			}
			note.setSequenceNumber(location);
			component.addRepeatingData(note);
			component.setHasNotes(true);
		}
	}

	private boolean validateData() {
		ValidationResult validationResult = validator.validate();
		if (validationResult.hasErrors()) {
			JGoodiesValidationUtils.showValidationMessage(
					this,
					"To save the record, please fix the following errors:",
					validationResult);
			return false;
		}
		if (validationResult.hasWarnings()) {
			JGoodiesValidationUtils.showValidationMessage(
					this,
					"Note: some fields are invalid.",
					validationResult);
		}
		return true;

	}

}
