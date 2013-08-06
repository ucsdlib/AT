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
 * Created by JFormDesigner on Tue Dec 20 13:32:21 EST 2005
 */

package org.archiviststoolkit.dialog;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.hibernate.ATSearchCriterion;
import org.archiviststoolkit.model.LookupListItems;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QueryEditorTextPanel extends QueryEditorPanel {

	public QueryEditorTextPanel() {
		initComponents();
		initComparatorComboBox(false);
	}

	public QueryEditorTextPanel(JComboBox comboBox) {
		initComponents();
		initComparatorComboBox(true);
		this.setComboBox(comboBox);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		comparator = new JComboBox();
		value = new JTextField();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBackground(new Color(231, 188, 251));
		setOpaque(false);
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
			},
			RowSpec.decodeSpecs("default")));

		//---- comparator ----
		comparator.setOpaque(false);
		comparator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comparatorActionPerformed(e);
			}
		});
		add(comparator, cc.xy(1, 1));
		add(value, cc.xy(3, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}


	public JTextField getValue() {
		return value;
	}

	private void comparatorActionPerformed(ActionEvent e) {
		String comparatorString = (String) comparator.getSelectedItem();
		if (comparatorString.equalsIgnoreCase(QueryEditorPanel.EMPTY) ||
				comparatorString.equalsIgnoreCase(QueryEditorPanel.NOT_EMPTY)) {
			valueSetVisible(false);
		} else {
			valueSetVisible(true);
		}
	}

	private void valueSetVisible(boolean visible) {
		if (useComboBox) {
			valueComboBox.setVisible(visible);
		} else {
			value.setVisible(visible);
		}
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JComboBox comparator;
	private JTextField value;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	private void setComboBox(JComboBox comboBox) {
		FormLayout formLayout = (FormLayout) this.getLayout();
		CellConstraints cc = formLayout.getConstraints(value);
		this.valueComboBox = comboBox;
		this.remove(value);
		this.add(valueComboBox, cc);
		useComboBox = true;
	}

	private boolean useComboBox = false;
	private JComboBox valueComboBox;

	public ATSearchCriterion getQueryCriterion(Class clazz, String field) {

		String humanReadableSearchString = "";
		String stringValue;
		if (useComboBox) {
			stringValue = ((LookupListItems) valueComboBox.getSelectedItem()).getListItem();
		} else {
			stringValue = value.getText();
		}
		Criterion criteria = null;
		String comparatorString = (String) comparator.getSelectedItem();

		humanReadableSearchString = getFieldLabel(clazz, field) + " "  + comparatorString;
		if (comparatorString.equalsIgnoreCase("is empty")) {
			if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_ORACLE)) {
				criteria = Expression.isNull(field);
			} else if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_MICROSOFT_SQL_SERVER)) {
				criteria = Expression.like(field, "", MatchMode.EXACT);
			} else {
				criteria = Expression.eq(field, "");
			}
		} else if (comparatorString.equalsIgnoreCase("is not empty")) {
			if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_ORACLE)) {
				criteria = Expression.isNotNull(field);
			} else if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_MICROSOFT_SQL_SERVER)) {
				criteria = Expression.not(Expression.like(field, "", MatchMode.EXACT));
			} else {
				criteria = Expression.ne(field, "");
			}
		} else {
			if (stringValue.length() != 0) {
				humanReadableSearchString += " " + stringValue;

				if (comparatorString.equalsIgnoreCase("begins with")) {
					if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_MICROSOFT_SQL_SERVER)) {
						criteria = Expression.like(field, stringValue, MatchMode.START);
					} else {
						criteria = Expression.ilike(field, stringValue, MatchMode.START);
					}
				} else if (comparatorString.equalsIgnoreCase("contains")) {
					if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_MICROSOFT_SQL_SERVER)) {
						criteria = Expression.like(field, stringValue, MatchMode.ANYWHERE);
					} else {
						criteria = Expression.ilike(field, stringValue, MatchMode.ANYWHERE);
					}
				} else if (comparatorString.equalsIgnoreCase("equals")) {
					if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_ORACLE)) {
						criteria = Expression.ilike(field, stringValue, MatchMode.EXACT);
					} else
					if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_MICROSOFT_SQL_SERVER)) {
						criteria = Expression.like(field, stringValue, MatchMode.EXACT);
					} else {
						criteria = Expression.eq(field, stringValue);
					}
				} else if (comparatorString.equalsIgnoreCase("is not equal to")) {
					if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_ORACLE)) {
						criteria = Expression.or(Expression.not(Expression.ilike(field, stringValue, MatchMode.EXACT)),
								Expression.isNull(field));
					} else
					if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_MICROSOFT_SQL_SERVER)) {
						criteria = Expression.not(Expression.like(field, stringValue, MatchMode.EXACT));
					} else {
						criteria = Expression.ne(field, stringValue);
					}
				}
			}
		}

		return new ATSearchCriterion(criteria, humanReadableSearchString, field);

	}

	public void requestInitialFocus() {
		value.requestFocusInWindow();
	}

	public boolean validDataEntered() {
		String comparatorString = (String) comparator.getSelectedItem();
		if (comparatorString.equalsIgnoreCase("is empty")) {
			return true;
		} else if (comparatorString.equalsIgnoreCase("is not empty")) {
			return true;
		} else {
			String stringValue;
			if (useComboBox) {
				stringValue = ((LookupListItems) valueComboBox.getSelectedItem()).getListItem();
			} else {
				stringValue = value.getText();
			}
			if (stringValue.length() > 0) {
				return true;
			} else {
				return false;
			}
		}
	}

	public void disableValueField() {
		value.setEnabled(false);
	}

	private void initComparatorComboBox(boolean controlledList) {

		if (controlledList) {
			String[] values = {QueryEditorPanel.EQUALS,
					QueryEditorPanel.NOT_EQUALS,
					QueryEditorPanel.EMPTY,
					QueryEditorPanel.NOT_EMPTY};
			comparator.setModel(new DefaultComboBoxModel(values));
		} else {
			String[] values = {QueryEditorPanel.CONTAINS,
					QueryEditorPanel.BEGINS_WITH,
                    QueryEditorPanel.EQUALS,
					QueryEditorPanel.NOT_EQUALS,
					QueryEditorPanel.EMPTY,
					QueryEditorPanel.NOT_EMPTY};
			comparator.setModel(new DefaultComboBoxModel(values));
		}
	}

}
