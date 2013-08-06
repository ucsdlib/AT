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
 * Created by JFormDesigner on Tue Mar 14 10:35:04 PST 2006
 */

package org.archiviststoolkit.dialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.DateFormatter;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Conjunction;
import org.hibernate.Criteria;
import org.archiviststoolkit.model.Accessions;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.hibernate.ATSearchCriterion;
import org.archiviststoolkit.util.StringHelper;

public class QueryEditor4BucketPanel extends QueryEditorPanel {

    private Class clazz;

    public QueryEditor4BucketPanel(Class clazz) {
        initComponents();
        this.clazz = clazz;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        comparator = new JLabel();
        bucket1 = new JTextField();
        bucket2 = new JTextField();
        bucket3 = new JTextField();
        bucket4 = new JTextField();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBackground(new Color(231, 188, 251));
        setOpaque(false);
        setLayout(new FormLayout(
        	new ColumnSpec[] {
        		FormFactory.DEFAULT_COLSPEC,
        		FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
        		new ColumnSpec(ColumnSpec.FILL, Sizes.dluX(10), FormSpec.DEFAULT_GROW),
        		FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
        		new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
        		FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
        		new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
        		FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
        		new ColumnSpec(ColumnSpec.FILL, Sizes.dluX(10), FormSpec.DEFAULT_GROW)
        	},
        	RowSpec.decodeSpecs("default")));
        ((FormLayout)getLayout()).setColumnGroups(new int[][] {{3, 5, 7, 9}});

        //---- comparator ----
        comparator.setBackground(new Color(231, 188, 251));
        comparator.setText("Equals");
        add(comparator, cc.xy(1, 1));
        add(bucket1, cc.xy(3, 1));
        add(bucket2, cc.xy(5, 1));
        add(bucket3, cc.xy(7, 1));
        add(bucket4, cc.xy(9, 1));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public JTextField getBucket1() {
        return bucket1;
    }

    public JTextField getBucket2() {
        return bucket2;
    }

    public JTextField getBucket3() {
        return bucket3;
    }

    public JTextField getBucket4() {
        return bucket4;
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel comparator;
    private JTextField bucket1;
    private JTextField bucket2;
    private JTextField bucket3;
    private JTextField bucket4;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public ATSearchCriterion getQueryCriterion(Class clazz, String field) {

		String humanReadableSearchString = "";
		if (clazz == Accessions.class) {
            Conjunction criterion = new Conjunction();

            String stringValue = getBucket1().getText();
            if (stringValue.length() > 0) {
				humanReadableSearchString = StringHelper.concat(" <font color='red'>and</font> ", humanReadableSearchString,
						getFieldLabel(Accessions.class, Accessions.PROPERTYNAME_ACCESSION_NUMBER_1) + " begins with " + stringValue);
				criterion.add(Expression.ilike(Accessions.PROPERTYNAME_ACCESSION_NUMBER_1,
                        stringValue, MatchMode.START));
            }
            stringValue = getBucket2().getText();
            if (stringValue.length() > 0) {
				humanReadableSearchString = StringHelper.concat(" <font color='red'>and</font> ", humanReadableSearchString,
						getFieldLabel(Accessions.class, Accessions.PROPERTYNAME_ACCESSION_NUMBER_2) + " begins with " + stringValue);
                criterion.add(Expression.ilike(Accessions.PROPERTYNAME_ACCESSION_NUMBER_2,
                        stringValue, MatchMode.START));
            }
            stringValue = getBucket3().getText();
            if (stringValue.length() > 0) {
				humanReadableSearchString = StringHelper.concat(" <font color='red'>and</font> ", humanReadableSearchString,
						getFieldLabel(Accessions.class, Accessions.PROPERTYNAME_ACCESSION_NUMBER_3) + " begins with " + stringValue);
                criterion.add(Expression.ilike(Accessions.PROPERTYNAME_ACCESSION_NUMBER_3,
                        stringValue, MatchMode.START));
            }
            stringValue = getBucket4().getText();
            if (stringValue.length() > 0) {
				humanReadableSearchString = StringHelper.concat(" <font color='red'>and</font> ", humanReadableSearchString,
						getFieldLabel(Accessions.class, Accessions.PROPERTYNAME_ACCESSION_NUMBER_4) + " begins with " + stringValue);
                criterion.add(Expression.ilike(Accessions.PROPERTYNAME_ACCESSION_NUMBER_4,
                        stringValue, MatchMode.START));
            }

            return new ATSearchCriterion(criterion, humanReadableSearchString, field);
        } else if (clazz == Resources.class) {
            Conjunction criterion = new Conjunction();

            String stringValue = getBucket1().getText();
            if (stringValue.length() > 0) {
				humanReadableSearchString = StringHelper.concat(" <font color='red'>and</font> ", humanReadableSearchString,
						getFieldLabel(Resources.class, Resources.PROPERTYNAME_RESOURCE_IDENTIFIER_1) + " begins with " + stringValue);
                criterion.add(Expression.ilike(Resources.PROPERTYNAME_RESOURCE_IDENTIFIER_1,
                        stringValue, MatchMode.START));
            }
            stringValue = getBucket2().getText();
            if (stringValue.length() > 0) {
				humanReadableSearchString = StringHelper.concat(" <font color='red'>and</font> ", humanReadableSearchString,
						getFieldLabel(Resources.class, Resources.PROPERTYNAME_RESOURCE_IDENTIFIER_2 )+ " begins with " + stringValue);
                criterion.add(Expression.ilike(Resources.PROPERTYNAME_RESOURCE_IDENTIFIER_2,
                        stringValue, MatchMode.START));
            }
            stringValue = getBucket3().getText();
            if (stringValue.length() > 0) {
				humanReadableSearchString = StringHelper.concat(" <font color='red'>and</font> ", humanReadableSearchString,
						getFieldLabel(Resources.class, Resources.PROPERTYNAME_RESOURCE_IDENTIFIER_3) + " begins with " + stringValue);
                criterion.add(Expression.ilike(Resources.PROPERTYNAME_RESOURCE_IDENTIFIER_3,
                        stringValue, MatchMode.START));
            }
            stringValue = getBucket4().getText();
            if (stringValue.length() > 0) {
				humanReadableSearchString = StringHelper.concat(" <font color='red'>and</font> ", humanReadableSearchString,
						getFieldLabel(Resources.class, Resources.PROPERTYNAME_RESOURCE_IDENTIFIER_4) + " begins with " + stringValue);
                criterion.add(Expression.ilike(Resources.PROPERTYNAME_RESOURCE_IDENTIFIER_4,
                        stringValue, MatchMode.START));
            }

			return new ATSearchCriterion(criterion, humanReadableSearchString, field);
         } else {
            return null;
        }
    }

	public void requestInitialFocus() {
		getBucket1().requestFocusInWindow();
	}

	public boolean validDataEntered() {
		if (getBucket1().getText().length() == 0 &&
				getBucket1().getText().length() == 0 &&
				getBucket1().getText().length() == 0 &&
				getBucket1().getText().length() == 0) {
			return false;
		} else {
			return true;
		}
	}
}
