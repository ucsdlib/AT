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
 * Created by JFormDesigner on Sat Jun 24 13:05:41 EDT 2006
 */

package org.archiviststoolkit.dialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.archiviststoolkit.model.Repositories;
import org.archiviststoolkit.hibernate.ATSearchCriterion;
import org.archiviststoolkit.util.StringHelper;

public class QueryEditorRepositoryPanel extends QueryEditorPanel {
	public QueryEditorRepositoryPanel() {
		initComponents();
		initComparatorComboBox();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		comparator = new JComboBox();
		repositoryList = new JComboBox();
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
		add(comparator, cc.xy(1, 1));

		//---- repositoryList ----
		repositoryList.setOpaque(false);
		add(repositoryList, cc.xy(3, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JComboBox comparator;
	private JComboBox repositoryList;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public ATSearchCriterion getQueryCriterion(Class clazz, String field) {

		Criterion criteria = null;
		String comparatorString = (String) comparator.getSelectedItem();
		Repositories repository = (Repositories)repositoryList.getSelectedItem();
		if (comparatorString.equalsIgnoreCase("equals")) {
			criteria = Expression.eq(field, repository);
		} else if (comparatorString.equalsIgnoreCase("is not equal to")) {
			criteria = Expression.ne(field, repository);
		}
		String humanReadableSearchString = StringHelper.concat(" ", getFieldLabel(clazz, field), comparatorString, repository.getShortName());
		return new ATSearchCriterion(criteria, humanReadableSearchString, field);
	}

	public void requestInitialFocus() {
		repositoryList.requestFocusInWindow();
	}

	public boolean validDataEntered() {
		if (repositoryList.getSelectedItem() == null) {
			return false;
		} else {
			return true;
		}
	}

	private void initComparatorComboBox() {
		String [] values = {QueryEditorPanel.EQUALS,
				QueryEditorPanel.NOT_EQUALS};
		comparator.setModel(new DefaultComboBoxModel(values));
		repositoryList.setModel(new DefaultComboBoxModel(Repositories.getRepositoryList()));
	}

}
