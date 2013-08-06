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
 * Created by JFormDesigner on Tue Jul 18 10:39:47 EDT 2006
 */

package org.archiviststoolkit.dialog;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Expression;
import org.archiviststoolkit.hibernate.ATSearchCriterion;

public class QueryEditorBooleanPanel extends QueryEditorPanel {
	public QueryEditorBooleanPanel() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		label1 = new JLabel();
		radioButtonTrue = new JRadioButton();
		radioButtonFalse = new JRadioButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBackground(new Color(231, 188, 251));
		setOpaque(false);
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC
			},
			RowSpec.decodeSpecs("default")));

		//---- label1 ----
		label1.setText("is");
		add(label1, cc.xy(1, 1));

		//---- radioButtonTrue ----
		radioButtonTrue.setText("True");
		radioButtonTrue.setSelected(true);
		radioButtonTrue.setOpaque(false);
		add(radioButtonTrue, cc.xy(3, 1));

		//---- radioButtonFalse ----
		radioButtonFalse.setText("False");
		radioButtonFalse.setSelected(true);
		radioButtonFalse.setOpaque(false);
		add(radioButtonFalse, cc.xy(5, 1));

		//---- buttonGroup1 ----
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(radioButtonTrue);
		buttonGroup1.add(radioButtonFalse);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel label1;
	private JRadioButton radioButtonTrue;
	private JRadioButton radioButtonFalse;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public ATSearchCriterion getQueryCriterion(Class clazz, String field) {

		Criterion criteria = null;
		String humanReadableSearchString = getFieldLabel(clazz, field) + " is ";
		if (radioButtonTrue.isSelected()) {
			criteria = Expression.eq(field, true);
			humanReadableSearchString += "true";
		} else {
            // a lot of the fields are set null and user expects them to be
            // false then consider false as null or or actually false
			Disjunction disjunction = Expression.disjunction();
            disjunction.add(Expression.eq(field, false));
            disjunction.add(Expression.isNull(field));

            criteria = disjunction;
            //criteria = Expression.eq(field, false);
            
			humanReadableSearchString += "false";
		}

		return new ATSearchCriterion(criteria, humanReadableSearchString, field);
	}

	public void requestInitialFocus() {
		radioButtonTrue.requestFocusInWindow();
	}

	public boolean validDataEntered() {
		return true;
	}

}
