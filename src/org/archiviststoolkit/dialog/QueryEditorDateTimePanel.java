/*
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
 * Created by JFormDesigner on Tue Dec 04 16:48:00 EST 2007
 */

package org.archiviststoolkit.dialog;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.hibernate.ATSearchCriterion;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class QueryEditorDateTimePanel extends QueryEditorPanel {
	public QueryEditorDateTimePanel() {
		initComponents();
		setVisibleComponents();
	}

	private void comparatorActionPerformed(ActionEvent e) {
		setVisibleComponents();
		this.invalidate();
		this.validate();
		this.repaint();
	}

	public JFormattedTextField getStartDate() {
		return startDate;
	}

	public JFormattedTextField getEndDate() {
		return endDate;
	}

	private void initComponents() {
		DateFormat shortFormat = DateFormat.getDateInstance(DateFormat.SHORT);
		shortFormat.setLenient(false);
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		comparator = new JComboBox();
		startDate = ATBasicComponentFactory.createUnboundDateField();
		label2 = new JLabel();
		endDate = ATBasicComponentFactory.createUnboundDateField();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBackground(new Color(231, 188, 251));
		setOpaque(false);
		setLayout(new FormLayout(
				new ColumnSpec[]{
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(ColumnSpec.FILL, Sizes.dluX(10), FormSpec.DEFAULT_GROW),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(ColumnSpec.FILL, Sizes.dluX(10), FormSpec.DEFAULT_GROW)
				},
				RowSpec.decodeSpecs("default")));
		((FormLayout) getLayout()).setColumnGroups(new int[][]{{3, 7}});

		//---- comparator ----
		comparator.setModel(new DefaultComboBoxModel(new String[]{
				"Equals",
				"Is between",
				"Is greater than",
				"Is greater than or equal to",
				"Is less than",
				"Is less than or equal to"
		}));
		comparator.setOpaque(false);
		comparator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comparatorActionPerformed(e);
			}
		});
		add(comparator, cc.xy(1, 1));
		add(startDate, cc.xy(3, 1));

		//---- label2 ----
		label2.setText("-");
		add(label2, cc.xy(5, 1));
		add(endDate, cc.xy(7, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JComboBox comparator;
	private JFormattedTextField startDate;
	private JLabel label2;
	private JFormattedTextField endDate;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public ATSearchCriterion getQueryCriterion(Class clazz, String field) {
		Calendar startDateValue = Calendar.getInstance();
		startDateValue.setTime((Date) startDate.getValue());
		Calendar startDateValuePlusOne = Calendar.getInstance();
		startDateValuePlusOne.setTime((Date) startDate.getValue());
		startDateValuePlusOne.add(Calendar.DATE, 1);
		String startDateStringValue = startDateValue.toString();

		Calendar endDateValue = Calendar.getInstance();
		Calendar endDateValuePlusOne = null;
		String endDateStringValue = "";
		if ((Date) endDate.getValue() != null) {
			endDateValue.setTime((Date) endDate.getValue());
			endDateValuePlusOne = Calendar.getInstance();
			endDateValuePlusOne.setTime((Date) endDate.getValue());
			endDateValuePlusOne.add(Calendar.DATE, 1);
			endDateStringValue = endDateValue.toString();
		}


		Criterion criteria = null;
		String comparatorString = (String) comparator.getSelectedItem();
		String humanReadableSearchString = getFieldLabel(clazz, field) + " " + comparatorString + " " + startDate.getText();
		if (comparatorString.equalsIgnoreCase("Equals")) {
			criteria = Expression.between(field, startDateValue.getTime(), startDateValuePlusOne.getTime());
//				criteria = Expression.eq(field, startDateValue);
		} else if (comparatorString.equalsIgnoreCase("is between")) {
			criteria = Expression.between(field, startDateValue.getTime(), endDateValuePlusOne.getTime());
			humanReadableSearchString += " and " + endDate.getText();
		} else if (comparatorString.equalsIgnoreCase("is greater than")) {
			criteria = Expression.ge(field, startDateValuePlusOne.getTime());
		} else if (comparatorString.equalsIgnoreCase("is greater than or equal to")) {
			criteria = Expression.ge(field, startDateValue.getTime());
		} else if (comparatorString.equalsIgnoreCase("is less than")) {
			criteria = Expression.lt(field, startDateValue.getTime());
		} else if (comparatorString.equalsIgnoreCase("is less than or equal to")) {
			criteria = Expression.lt(field, startDateValuePlusOne.getTime());
		}
		return new ATSearchCriterion(criteria, humanReadableSearchString, field);

	}

	public void requestInitialFocus() {
		startDate.requestFocusInWindow();
	}

	public boolean validDataEntered() {
		String comparatorString = (String) comparator.getSelectedItem();
		Date startDateValue = (Date) startDate.getValue();
		Date endDateValue = (Date) endDate.getValue();
		if (comparatorString.equalsIgnoreCase("is between")) {
			if (startDateValue == null || endDateValue == null) {
				return false;
			} else {
				return true;
			}
		} else {
			if (startDateValue == null) {
				return false;
			} else {
				return true;
			}
		}
	}

	private void setVisibleComponents() {
		String comparatorString = (String) comparator.getSelectedItem();
		if (comparatorString.equalsIgnoreCase("is between")) {
			label2.setVisible(true);
			endDate.setVisible(true);
		} else {
			label2.setVisible(false);
			endDate.setVisible(false);
		}
	}

}
