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
 */

package org.archiviststoolkit.structure;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Vector;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.LookupList;
import org.archiviststoolkit.model.LookupListItems;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.util.LookupListUtils;

public class DefaultValuesFields extends DomainEditorFields {
	public DefaultValuesFields() {
		super();
		initComponents();
		FormLayout layout = (FormLayout) this.getLayout();
		valueCellConstraints = layout.getConstraints(valuePlaceHolder);
		this.remove(valuePlaceHolder);
	}

	private void tableActionPerformed(ActionEvent e) {
		if (!init) {
			DefaultValues defaultValueModel = (DefaultValues) super.getModel();
			DatabaseTables selectedTable = null;

            if(table.getSelectedIndex() >= 1) { // check to make sure a valid database table is selected
                selectedTable = (DatabaseTables) table.getSelectedItem();
            }

            if (selectedTable == null) {
				field.setModel(new DefaultComboBoxModel(new String[]{}));
			} else {
				defaultValueModel.setTableName(selectedTable.getTableName());
				field.setModel(new DefaultComboBoxModel(ATFieldInfo.getFieldListByTableNameForDefaultValues(selectedTable)));
				fieldActionPerformed(e);
			}
		}
	}

	private void fieldActionPerformed(ActionEvent e) {
		if (field.getSelectedIndex() >= 1) {
			DefaultValues defaultValueModel = (DefaultValues) super.getModel();
			DatabaseFields databaseField = (DatabaseFields) field.getSelectedItem();
			if (!init) {
				defaultValueModel.nullValueFields();
				defaultValueModel.setAtField(databaseField);
			}
			//remove the valueComponent if it is there
			if (currentValueComponent != null) {
				this.remove(currentValueComponent);
			}

			Component valueComponent = null;
			if (databaseField != null) {
				if (databaseField.getDataType().equalsIgnoreCase(String.class.getName())) {
					String className = ATFieldInfo.getTableByTableName(defaultValueModel.getTableName()).getClassName();
					ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(className, databaseField.getFieldName());
					if (fieldInfo.getLookupList() != null && fieldInfo.getLookupList().length() != 0) {
						Vector<LookupListItems> lookupListItems = LookupListUtils.getLookupListValues2(fieldInfo.getLookupList());

						valueComponent = ATBasicComponentFactory.createComboBoxWithConverter(detailsModel,
								DefaultValues.PROPERTYNAME_STRING_VALUE, lookupListItems, fieldInfo.getLookupList());
						String stringValue = defaultValueModel.getStringValue();
						if (stringValue == null || stringValue.length() == 0) {
							((JComboBox) valueComponent).setSelectedIndex(0);
						} else {
							for (LookupListItems item: lookupListItems) {
								if (item.getListItem().equals(stringValue)){
									((JComboBox) valueComponent).setSelectedItem(item);
								}

							}
						}
					} else {
                        if (fieldInfo.getStringLengthLimit() != null && fieldInfo.getStringLengthLimit() > 0) {
							valueComponent = ATBasicComponentFactory.createTextField(detailsModel.getModel(DefaultValues.PROPERTYNAME_STRING_VALUE));
                        } else {
							valueComponent = new JScrollPane();
							((JScrollPane)valueComponent).setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                            JTextArea textArea = ATBasicComponentFactory.createTextArea(detailsModel.getModel(DefaultValues.PROPERTYNAME_TEXT_VALUE));
							textArea.setRows(5);
							textArea.setLineWrap(true);
							textArea.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
							textArea.setWrapStyleWord(true);
							((JScrollPane)valueComponent).setViewportView(textArea);
                        }
                    }

				} else if (databaseField.getDataType().equalsIgnoreCase(Long.class.getName())) {
					valueComponent = ATBasicComponentFactory.createIntegerField(detailsModel, DefaultValues.PROPERTYNAME_LONG_VALUE);

				} else if (databaseField.getDataType().equalsIgnoreCase(Integer.class.getName())) {
					valueComponent = ATBasicComponentFactory.createIntegerField(detailsModel, DefaultValues.PROPERTYNAME_INT_VALUE);

				} else if (databaseField.getDataType().equalsIgnoreCase(Double.class.getName())) {
					valueComponent = ATBasicComponentFactory.createDoubleField(detailsModel, DefaultValues.PROPERTYNAME_DOUBLE_VALUE);

				} else if (databaseField.getDataType().equalsIgnoreCase(Date.class.getName())) {
					valueComponent = ATBasicComponentFactory.createDateField(detailsModel.getModel(DefaultValues.PROPERTYNAME_DATE_VALUE));

				} else if (databaseField.getDataType().equalsIgnoreCase(Boolean.class.getName())) {
					valueComponent = ATBasicComponentFactory.createCheckBox(detailsModel, DefaultValues.PROPERTYNAME_BOOLEAN_VALUE, DefaultValues.class);
				}
				if (valueComponent != null) {
					this.add(valueComponent, valueCellConstraints);
					this.invalidate();
					this.validate();
					this.repaint();
					currentValueComponent = valueComponent;
				}
			}
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		lookupListLabel = new JLabel();
		table = new JComboBox(new DefaultComboBoxModel(ATFieldInfo.getTableListForDefaultValues()));
		lookupListLabel2 = new JLabel();
		field = new JComboBox();
		label17 = new JLabel();
		valuePlaceHolder = new JTextField();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBorder(Borders.DLU4_BORDER);
		setOpaque(false);
		setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		setPreferredSize(new Dimension(400, 130));
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec("30px:grow")
			},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC
			}));

		//---- lookupListLabel ----
		lookupListLabel.setText("Table");
		lookupListLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(lookupListLabel, cc.xy(1, 1));

		//---- table ----
		table.setOpaque(false);
		table.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		table.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableActionPerformed(e);
			}
		});
		add(table, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

		//---- lookupListLabel2 ----
		lookupListLabel2.setText("Field");
		lookupListLabel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(lookupListLabel2, cc.xy(1, 3));

		//---- field ----
		field.setOpaque(false);
		field.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		field.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fieldActionPerformed(e);
			}
		});
		add(field, cc.xywh(3, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

		//---- label17 ----
		label17.setText("Value");
		label17.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(label17, cc.xy(1, 5));

		//---- valuePlaceHolder ----
		valuePlaceHolder.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(valuePlaceHolder, cc.xywh(3, 5, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel lookupListLabel;
	public JComboBox table;
	private JLabel lookupListLabel2;
	public JComboBox field;
	private JLabel label17;
	public JTextField valuePlaceHolder;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	private CellConstraints valueCellConstraints;
	private Component currentValueComponent = null;
	private Boolean init = false;

	/**
	 * Set the domain model for this editor.
	 *
	 * @param model the model
	 */

	@Override
	public void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		DefaultValues defaultValueModel = (DefaultValues) model;
		init = true;
		String tableName = defaultValueModel.getTableName();
		table.setSelectedItem(ATFieldInfo.getTableByTableName(tableName));
		field.setModel(new DefaultComboBoxModel(ATFieldInfo.getFieldListByTableName(tableName)));
		field.setSelectedItem(defaultValueModel.getAtField());
		init = false;
	}

	public Component getInitialFocusComponent() {
		return table;
	}
}
