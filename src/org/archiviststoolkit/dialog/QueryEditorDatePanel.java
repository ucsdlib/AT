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
 * Created by JFormDesigner on Mon Dec 19 17:37:03 EST 2005
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
import java.util.Date;

public class QueryEditorDatePanel extends QueryEditorPanel {
	public QueryEditorDatePanel() {
		initComponents();
		setVisibleComponents();
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

	public JFormattedTextField getStartDate() {
		return startDate;
	}

	public JFormattedTextField getEndDate() {
		return endDate;
	}

	private void comparatorActionPerformed(ActionEvent e) {
		setVisibleComponents();
		this.invalidate();
		this.validate();
		this.repaint();
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JComboBox comparator;
	private JFormattedTextField startDate;
	private JLabel label2;
	private JFormattedTextField endDate;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public ATSearchCriterion getQueryCriterion(Class clazz, String field) {
		Date startDateValue = (Date) startDate.getValue();
		Date endDateValue = (Date) endDate.getValue();
		Criterion criteria = null;
		String comparatorString = (String) comparator.getSelectedItem();
		String humanReadableSearchString = getFieldLabel(clazz, field) + " " + comparatorString + " " + startDate.getText();
		if (comparatorString.equalsIgnoreCase("Equals")) {
			criteria = Expression.eq(field, startDateValue);
		} else if (comparatorString.equalsIgnoreCase("Is between")) {
			criteria = Expression.between(field, startDateValue, endDateValue);
			humanReadableSearchString += " and " + endDate.getText();
		} else if (comparatorString.equalsIgnoreCase("Is greater than")) {
			criteria = Expression.gt(field, startDateValue);
		} else if (comparatorString.equalsIgnoreCase("Is greater than or equal to")) {
			criteria = Expression.ge(field, startDateValue);
		} else if (comparatorString.equalsIgnoreCase("Is less than")) {
			criteria = Expression.lt(field, startDateValue);
		} else if (comparatorString.equalsIgnoreCase("Is less than or equal to")) {
			criteria = Expression.le(field, startDateValue);
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
		if (comparatorString.equalsIgnoreCase("Is between")) {
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
		if (comparatorString.equalsIgnoreCase("Is between")) {
			label2.setVisible(true);
			endDate.setVisible(true);
		} else {
			label2.setVisible(false);
			endDate.setVisible(false);
		}
	}
}
