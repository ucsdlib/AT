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
 * Created by JFormDesigner on Wed Jul 05 14:43:28 PDT 2006
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

public class QueryEditorIntegerPanel extends QueryEditorPanel {

	private Boolean returnLongValues = false;

	public QueryEditorIntegerPanel() {
		initComponents();
		setVisibleComponents();
	}

	public QueryEditorIntegerPanel(Boolean returnLongValues) {
		initComponents();
		this.returnLongValues = returnLongValues;
		setVisibleComponents();
	}

	private void comparatorActionPerformed(ActionEvent e) {
		setVisibleComponents();
		this.invalidate();
		this.validate();
		this.repaint();
	}

	public JFormattedTextField getStartInteger() {
		return startInteger;
	}

	public JFormattedTextField getEndInteger() {
		return endInteger;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		comparator = new JComboBox();
		startInteger = ATBasicComponentFactory.createUnboundIntegerField(false);
		label2 = new JLabel();
		endInteger = ATBasicComponentFactory.createUnboundIntegerField(false);
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
		add(startInteger, cc.xy(3, 1));

		//---- label2 ----
		label2.setText("-");
		add(label2, cc.xy(5, 1));
		add(endInteger, cc.xy(7, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JComboBox comparator;
	private JFormattedTextField startInteger;
	private JLabel label2;
	private JFormattedTextField endInteger;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public ATSearchCriterion getQueryCriterion(Class clazz, String field) {
		Number startNumberValue;
		Number endNumberValue;
		if (returnLongValues) {
			startNumberValue = ((Integer) startInteger.getValue()).longValue();
			endNumberValue = ((Integer) endInteger.getValue()).longValue();
		} else {
			startNumberValue = (Integer) startInteger.getValue();
			endNumberValue = (Integer) endInteger.getValue();
		}
		Criterion criteria = null;
		String comparatorString = (String) comparator.getSelectedItem();
		String humanReadableSearchString = getFieldLabel(clazz, field) + " " + comparatorString + " " + startNumberValue;
		if (comparatorString.equalsIgnoreCase("Equals")) {
			criteria = Expression.eq(field, startNumberValue);
		} else if (comparatorString.equalsIgnoreCase("Is between")) {
			criteria = Expression.between(field, startNumberValue, endNumberValue);
			humanReadableSearchString += " and " + endNumberValue;
		} else if (comparatorString.equalsIgnoreCase("Is greater than")) {
			criteria = Expression.gt(field, startNumberValue);
		} else if (comparatorString.equalsIgnoreCase("Is greater than or equal to")) {
			criteria = Expression.ge(field, startNumberValue);
		} else if (comparatorString.equalsIgnoreCase("Is less than")) {
			criteria = Expression.lt(field, startNumberValue);
		} else if (comparatorString.equalsIgnoreCase("Is less than or equal to")) {
			criteria = Expression.le(field, startNumberValue);
		}
		return new ATSearchCriterion(criteria, humanReadableSearchString, field);

	}

	public void requestInitialFocus() {
		startInteger.requestFocusInWindow();
	}

	public boolean validDataEntered() {
		String comparatorString = (String) comparator.getSelectedItem();
		Integer startNumberValue = (Integer) startInteger.getValue();
		Integer endNumberValue = (Integer) endInteger.getValue();
		if (comparatorString.equalsIgnoreCase("Is between")) {
			if (startNumberValue == null || endNumberValue == null) {
				return false;
			} else {
				return true;
			}
		} else {
			if (startNumberValue == null) {
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
			endInteger.setVisible(true);
		} else {
			label2.setVisible(false);
			endInteger.setVisible(false);
		}
	}

	public void setReturnLongValues(Boolean returnLongValues) {
		this.returnLongValues = returnLongValues;
	}
}
