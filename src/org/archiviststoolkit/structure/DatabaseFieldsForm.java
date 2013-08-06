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
 * @author Lee Mandell
 * Created by JFormDesigner on Wed Oct 26 11:46:21 EDT 2005
 */

package org.archiviststoolkit.structure;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.BufferedValueModel;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.model.LookupList;
import org.archiviststoolkit.model.ArchDescriptionNotes;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.util.LookupListUtils;

public class DatabaseFieldsForm extends DomainEditorFields {
	public DatabaseFieldsForm() {
		super();
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		label1 = new JLabel();
		fieldName = ATBasicComponentFactory.createTextField(detailsModel.getModel(DatabaseFields.PROPERTYNAME_FIELD_NAME));
		label16 = new JLabel();
		fieldLabel = ATBasicComponentFactory.createTextField(detailsModel.getBufferedModel(DatabaseFields.PROPERTYNAME_FIELD_LABEL));
		label12 = new JLabel();
		dataType = ATBasicComponentFactory.createTextField(detailsModel.getModel(DatabaseFields.PROPERTYNAME_DATA_TYPE));
		lookupListLabel = new JLabel();
		lookupList = ATBasicComponentFactory.createTextField(detailsModel.getModel(DatabaseFields.PROPERTYNAME_LOOKUP_LIST));
		panel1 = new JPanel();
		includeInSearchEditor = ATBasicComponentFactory.createCheckBox(detailsModel, DatabaseFields.PROPERTYNAME_INCLUDE_IN_SEARCH_EDITOR, DatabaseFields.class, true);
		label17 = new JLabel();
		returnScreenOrder = ATBasicComponentFactory.createBufferedIntegerField(detailsModel,DatabaseFields.PROPERTYNAME_RETURN_SCREEN_ORDER);
		label14 = new JLabel();
		scrollPane2 = new JScrollPane();
		definition = ATBasicComponentFactory.createTextArea(detailsModel.getBufferedModel(DatabaseFields.PROPERTYNAME_DEFINITION));
		label13 = new JLabel();
		scrollPane1 = new JScrollPane();
		examples = ATBasicComponentFactory.createTextArea(detailsModel.getBufferedModel(DatabaseFields.PROPERTYNAME_EXAMPLES));
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBorder(Borders.DLU4_BORDER);
		setOpaque(false);
		setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		setPreferredSize(new Dimension(600, 500));
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec("30px"),
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
				new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
				FormFactory.LINE_GAP_ROWSPEC,
				new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
			}));

		//---- label1 ----
		label1.setText("Field Name");
		label1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(label1, cc.xy(1, 1));

		//---- fieldName ----
		fieldName.setEditable(false);
		fieldName.setOpaque(false);
		fieldName.setBorder(null);
		fieldName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(fieldName, cc.xywh(3, 1, 3, 1));

		//---- label16 ----
		label16.setText("Field Label");
		label16.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(label16, cc.xy(1, 3));

		//---- fieldLabel ----
		fieldLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(fieldLabel, cc.xywh(3, 3, 3, 1));

		//---- label12 ----
		label12.setText("Data Type");
		label12.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(label12, cc.xy(1, 5));

		//---- dataType ----
		dataType.setEditable(false);
		dataType.setBorder(null);
		dataType.setOpaque(false);
		dataType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(dataType, cc.xywh(3, 5, 3, 1));

		//---- lookupListLabel ----
		lookupListLabel.setText("Lookup List");
		lookupListLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(lookupListLabel, cc.xy(1, 7));

		//---- lookupList ----
		lookupList.setOpaque(false);
		lookupList.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		lookupList.setEditable(false);
		lookupList.setBorder(null);
		add(lookupList, cc.xywh(3, 7, 3, 1));

		//======== panel1 ========
		{
			panel1.setBackground(new Color(234, 201, 250));
			panel1.setOpaque(false);
			panel1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			panel1.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC
				},
				RowSpec.decodeSpecs("default")));

			//---- includeInSearchEditor ----
			includeInSearchEditor.setBackground(new Color(234, 201, 250));
			includeInSearchEditor.setText("Include In Search Editor");
			includeInSearchEditor.setOpaque(false);
			includeInSearchEditor.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			panel1.add(includeInSearchEditor, cc.xy(1, 1));
		}
		add(panel1, cc.xywh(3, 9, 3, 1));

		//---- label17 ----
		label17.setText("Return Screen Order");
		label17.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(label17, cc.xy(1, 11));

		//---- returnScreenOrder ----
		returnScreenOrder.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(returnScreenOrder, cc.xywh(3, 11, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

		//---- label14 ----
		label14.setText("Definition");
		label14.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(label14, cc.xywh(1, 13, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

		//======== scrollPane2 ========
		{
			scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

			//---- definition ----
			definition.setRows(5);
			definition.setLineWrap(true);
			definition.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			definition.setWrapStyleWord(true);
			scrollPane2.setViewportView(definition);
		}
		add(scrollPane2, cc.xywh(3, 13, 3, 1));

		//---- label13 ----
		label13.setText("Examples");
		label13.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(label13, cc.xywh(1, 15, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

		//======== scrollPane1 ========
		{
			scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

			//---- examples ----
			examples.setRows(5);
			examples.setLineWrap(true);
			examples.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			examples.setWrapStyleWord(true);
			scrollPane1.setViewportView(examples);
		}
		add(scrollPane1, cc.xywh(3, 15, 3, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel label1;
	public JTextField fieldName;
	private JLabel label16;
	public JTextField fieldLabel;
	private JLabel label12;
	public JTextField dataType;
	private JLabel lookupListLabel;
	public JTextField lookupList;
	private JPanel panel1;
	public JCheckBox includeInSearchEditor;
	private JLabel label17;
	public JFormattedTextField returnScreenOrder;
	private JLabel label14;
	private JScrollPane scrollPane2;
	public JTextArea definition;
	private JLabel label13;
	private JScrollPane scrollPane1;
	public JTextArea examples;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	private DatabaseFields model;

	/**
	 * Sets the model for this editor.
	 *
	 * @param model the model to be used
	 */

	public final void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		this.model = (DatabaseFields) model;
		if (!this.model.getDataType().equalsIgnoreCase(String.class.getName())) {
			this.lookupList.setVisible(false);
			this.lookupListLabel.setVisible(false);
		} else if (this.model.getDatabaseTable().getClazz() == ArchDescriptionNotes.class &&
				this.model.getFieldName().equalsIgnoreCase(ArchDescriptionNotes.PROPERTYNAME_ARCH_DESCRIPTION_NOTE_TYPE)) {
			this.lookupList.setVisible(false);
			this.lookupListLabel.setVisible(false);
        } else if (this.model.getLookupList().length() == 0) {
            this.lookupList.setVisible(false);
            this.lookupListLabel.setVisible(false);
        } else {
			this.lookupList.setVisible(true);
			this.lookupListLabel.setVisible(true);
		}
		//check to see if the search editor is valid for the table associated with
		//this field
		if (ApplicationFrame.getInstance().hasWorkSurface(this.model.getDatabaseTable().getClazz())) {
			this.includeInSearchEditor.setVisible(true);
		} else {
			this.includeInSearchEditor.setVisible(false);
		}
		//check to see if this field is allowed in a return screen
		if (this.model.canFieldAppearInReturnScreen() && !this.model.getDataType().contains("org.archiviststoolkit")) {
			returnScreenOrder.setEnabled(true);
		} else {
			returnScreenOrder.setEnabled(false);
		}

        // check to see if we should allow this to be included in the search editor
        if (this.model.canFieldAppearInSearchEditor()) {
			this.includeInSearchEditor.setVisible(true);
		} else {
			this.includeInSearchEditor.setVisible(false);
		}
	}

	public Component getInitialFocusComponent() {
		return fieldLabel;
	}
}
