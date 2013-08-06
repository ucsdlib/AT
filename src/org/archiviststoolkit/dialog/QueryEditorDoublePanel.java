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
 * Created by JFormDesigner on Wed Jul 05 15:01:01 PDT 2006
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

public class QueryEditorDoublePanel extends QueryEditorPanel {
	public QueryEditorDoublePanel() {
		initComponents();
		setVisibleComponents();
	}

	private void comparatorActionPerformed(ActionEvent e) {
		setVisibleComponents();
		this.invalidate();
		this.validate();
		this.repaint();
	}

	public JFormattedTextField getStartDouble() {
		return startDouble;
	}

	public JFormattedTextField getEndDouble() {
		return endDouble;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		comparator = new JComboBox();
		startDouble = ATBasicComponentFactory.createUnboundDoubleField();
		label2 = new JLabel();
		endDouble = ATBasicComponentFactory.createUnboundDoubleField();
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
		add(startDouble, cc.xy(3, 1));

		//---- label2 ----
		label2.setText("-");
		add(label2, cc.xy(5, 1));
		add(endDouble, cc.xy(7, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JComboBox comparator;
	private JFormattedTextField startDouble;
	private JLabel label2;
	private JFormattedTextField endDouble;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public ATSearchCriterion getQueryCriterion(Class clazz, String field) {
		Double startDoubleValue = (Double) startDouble.getValue();
		Double endDoubleValue = (Double) endDouble.getValue();
		Criterion criteria = null;
		String comparatorString = (String) comparator.getSelectedItem();
		String humanReadableSearchString = getFieldLabel(clazz, field) + " " + comparatorString + " " + startDoubleValue;
		if (comparatorString.equalsIgnoreCase("Equals")) {
			criteria = Expression.eq(field, startDoubleValue);
		} else if (comparatorString.equalsIgnoreCase("Is between")) {
			criteria = Expression.between(field, startDoubleValue, endDoubleValue);
			humanReadableSearchString += " and " + endDoubleValue;
		} else if (comparatorString.equalsIgnoreCase("Is greater than")) {
			criteria = Expression.gt(field, startDoubleValue);
		} else if (comparatorString.equalsIgnoreCase("Is greater than or equal to")) {
			criteria = Expression.ge(field, startDoubleValue);
		} else if (comparatorString.equalsIgnoreCase("Is less than")) {
			criteria = Expression.lt(field, startDoubleValue);
		} else if (comparatorString.equalsIgnoreCase("Is less than or equal to")) {
			criteria = Expression.le(field, startDoubleValue);
		}
		return new ATSearchCriterion(criteria, humanReadableSearchString, field);

	}

	public void requestInitialFocus() {
		startDouble.requestFocusInWindow();
	}

	public boolean validDataEntered() {
		String comparatorString = (String) comparator.getSelectedItem();
		Double startDoubleValue = (Double) startDouble.getValue();
		Double endDoubleValue = (Double) endDouble.getValue();
		if (comparatorString.equalsIgnoreCase("Is between")) {
			if (startDoubleValue == null || endDoubleValue == null) {
				return false;
			} else {
				return true;
			}
		} else {
			if (startDoubleValue == null) {
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
			endDouble.setVisible(true);
		} else {
			label2.setVisible(false);
			endDouble.setVisible(false);
		}
	}

}
