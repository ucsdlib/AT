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
 * @author Lee Mandell
 * Created by JFormDesigner on Tue Oct 25 14:53:49 EDT 2005
 */

package org.archiviststoolkit.structure;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.mydomain.DomainSortableTable;
import org.archiviststoolkit.swing.ATBasicComponentFactory;

public class DatabaseTablesForm extends DomainEditorFields {

	public DatabaseTablesForm(DatabaseTableEditor editor) {
		super();
		this.editor = editor;
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		label1 = new JLabel();
		tableName = ATBasicComponentFactory.createTextField(detailsModel.getModel(DatabaseTables.PROPERTYNAME_TABLE_NAME));
		panel1 = new JPanel();
		label2 = new JLabel();
		scrollPane1 = new JScrollPane();
		databaseFieldsTable = new DomainSortableTable(DatabaseFields.class, DatabaseFields.PROPERTYNAME_FIELD_NAME);
		panel2 = new JPanel();
		deleteFieldButton = new JButton();
		getFieldsButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBorder(Borders.DLU4_BORDER);
		setOpaque(false);
		setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		setPreferredSize(new Dimension(800, 500));
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec("max(default;400px):grow")
			},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
			}));

		//---- label1 ----
		label1.setText("Table Name");
		label1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(label1, cc.xy(1, 1));

		//---- tableName ----
		tableName.setEditable(false);
		tableName.setOpaque(false);
		tableName.setBorder(null);
		tableName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(tableName, cc.xy(3, 1));

		//======== panel1 ========
		{
			panel1.setBackground(new Color(234, 201, 250));
			panel1.setOpaque(false);
			panel1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			panel1.setLayout(new FormLayout(
				ColumnSpec.decodeSpecs("default:grow"),
				new RowSpec[] {
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC
				}));

			//---- label2 ----
			label2.setText("Fields");
			label2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			panel1.add(label2, cc.xy(1, 1));

			//======== scrollPane1 ========
			{
				scrollPane1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

				//---- databaseFieldsTable ----
				databaseFieldsTable.setPreferredScrollableViewportSize(new Dimension(450, 200));
				scrollPane1.setViewportView(databaseFieldsTable);
			}
			panel1.add(scrollPane1, cc.xywh(1, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL));

			//======== panel2 ========
			{
				panel2.setBackground(new Color(234, 201, 250));
				panel2.setOpaque(false);
				panel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				panel2.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC
					},
					RowSpec.decodeSpecs("default")));

				//---- deleteFieldButton ----
				deleteFieldButton.setText("Delete Field");
				deleteFieldButton.setBackground(new Color(234, 201, 250));
				deleteFieldButton.setOpaque(false);
				deleteFieldButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				panel2.add(deleteFieldButton, cc.xy(1, 1));

				//---- getFieldsButton ----
				getFieldsButton.setText("Get Fields");
				getFieldsButton.setBackground(new Color(234, 201, 250));
				getFieldsButton.setOpaque(false);
				getFieldsButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				getFieldsButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						getFieldsActionPerformed(e);
					}
				});
				panel2.add(getFieldsButton, cc.xy(3, 1));
			}
			panel1.add(panel2, cc.xywh(1, 5, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
		}
		add(panel1, cc.xywh(1, 3, 3, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	private void getFieldsActionPerformed(ActionEvent e) {
		DatabaseTables databaseTablesModel = (DatabaseTables) this.getModel();
		databaseTablesModel.addFieldInfo();
		editor.updateDatabaseFieldsTable();
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel label1;
	public JTextField tableName;
	private JPanel panel1;
	private JLabel label2;
	private JScrollPane scrollPane1;
	private DomainSortableTable databaseFieldsTable;
	private JPanel panel2;
	private JButton deleteFieldButton;
	private JButton getFieldsButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	private DatabaseTableEditor editor;

	public DomainSortableTable getDatabaseFieldsTable() {
		return databaseFieldsTable;
	}

	public JButton getDeleteFieldButton() {
		return deleteFieldButton;
	}

	public JButton getGetFieldsButton() {
		return getFieldsButton;
	}

	public Component getInitialFocusComponent() {
		return null;
	}
}
