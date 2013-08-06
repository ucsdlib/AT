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
 * Programmer: Lee Mandell
 * Date: Oct 11, 2005
 * Time: 9:52:07 PM
 */

package org.archiviststoolkit.dialog;

import org.archiviststoolkit.model.Accessions;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.model.LookupListItems;
import org.archiviststoolkit.util.ATBeanUtils;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.DatabaseFields;

import javax.swing.*;
import java.util.Date;
import java.util.Vector;
import java.beans.IntrospectionException;
import java.awt.*;

public class QueryField implements Comparable {

	QueryEditorPanel valueComponent;

	String tableName;

	String fieldName;

	String displayFieldName;

	public QueryField(DatabaseFields field, Class clazz) {
		this.fieldName = field.getFieldName();
		this.displayFieldName = field.getFieldLabel();
		this.tableName = clazz.getName();
		//first intercept accession number and resource id for special treatment
		if (this.fieldName.equalsIgnoreCase("AccessionNumber") &&
				clazz == Accessions.class) {
			setValueComponent(new QueryEditor4BucketPanel(clazz));
		} else if (this.fieldName.equalsIgnoreCase("ResourceIdentifier") &&
				clazz == Resources.class) {
			setValueComponent(new QueryEditor4BucketPanel(clazz));
		} else if (this.fieldName.equalsIgnoreCase("Repository")) {
			setValueComponent(new QueryEditorRepositoryPanel());
		} else if (this.fieldName.equalsIgnoreCase("createdBy")) {
			this.fieldName = "auditInfo.createdBy";
			setValueComponent(new QueryEditorTextPanel());
		} else if (this.fieldName.equalsIgnoreCase("lastUpdatedBy")) {
			this.fieldName = "auditInfo.lastUpdatedBy";
			setValueComponent(new QueryEditorTextPanel());
		} else if (this.fieldName.equalsIgnoreCase("created")) {
			this.fieldName = "auditInfo.created";
			setValueComponent(new QueryEditorDateTimePanel());
		} else if (this.fieldName.equalsIgnoreCase("lastUpdated")) {
			this.fieldName = "auditInfo.lastUpdated";
			setValueComponent(new QueryEditorDateTimePanel());
		} else {
			try {
				Class fieldType = ATBeanUtils.getPropertyType(clazz, fieldName);
				if (fieldType == String.class) {
					ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(this.tableName + "." + this.fieldName);
					Vector<LookupListItems> values = LookupListUtils.getLookupListValues2(fieldInfo.getLookupList());
					if (values.size() == 0) {
						setValueComponent(new QueryEditorTextPanel());
					} else {
						JComboBox comboBox = new JComboBox(new DefaultComboBoxModel(values));
						comboBox.setOpaque(false);
                        comboBox.setMaximumSize(new Dimension(110, 32767)); // set the size so that it shows up properly in subject search
                        comboBox.setMinimumSize(new Dimension(110, 27));
                        setValueComponent(new QueryEditorTextPanel(comboBox));
					}
                } else if (fieldType == Date.class) {
                    setValueComponent(new QueryEditorDatePanel());
                } else if (fieldType == Integer.class) {
                    setValueComponent(new QueryEditorIntegerPanel(QueryEditorPanel.RETURN_INTEGER_VALUES));
                } else if (fieldType == Long.class) {
                    setValueComponent(new QueryEditorIntegerPanel(QueryEditorPanel.RETURN_LONG_VALUES));
				} else if (fieldType == Double.class) {
					setValueComponent(new QueryEditorDoublePanel());
				} else if (fieldType == Boolean.class) {
					setValueComponent(new QueryEditorBooleanPanel());
				}
			} catch (IntrospectionException e) {
				new ErrorDialog("", e).showDialog();
			}
		}
   }

	public QueryEditorPanel getValueComponent() {
		return valueComponent;
	}

	private void setValueComponent(QueryEditorPanel valueComponent) {
		this.valueComponent = valueComponent;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String toString() {
		return displayFieldName;
	}

	/**
	 * Compares this object with the specified object for order. Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object.<p>
	 * <p/>
	 * In the foregoing description, the notation
	 * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
	 * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
	 * <tt>0</tt>, or <tt>1</tt> according to whether the value of <i>expression</i>
	 * is negative, zero or positive.
	 * <p/>
	 * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
	 * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>. (This
	 * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
	 * <tt>y.compareTo(x)</tt> throws an exception.)<p>
	 * <p/>
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
	 * <tt>x.compareTo(z)&gt;0</tt>.<p>
	 * <p/>
	 * Finally, the implementer must ensure that <tt>x.compareTo(y)==0</tt>
	 * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
	 * all <tt>z</tt>.<p>
	 * <p/>
	 * It is strongly recommended, but <i>not</i> strictly required that
	 * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>. Generally speaking, any
	 * class that implements the <tt>Comparable</tt> interface and violates
	 * this condition should clearly indicate this fact. The recommended
	 * language is "Note: this class has a natural ordering that is
	 * inconsistent with equals."
	 *
	 * @param o the Object to be compared.
	 * @return a negative integer, zero, or a positive integer as this object
	 *         is less than, equal to, or greater than the specified object.
	 * @throws ClassCastException if the specified object's type prevents it
	 *                            from being compared to this Object.
	 */
	/**
	 * Compares this object to another.
	 *
	 * @param object the object to compare this to.
	 * @return a integer result of the comparison.
	 */
	public int compareTo(Object object) {
		if (this.getClass() != object.getClass()) {
			throw (new ClassCastException("Cannot compare unlike objects"));
		}

		return (toString().compareToIgnoreCase(object.toString()));
	}
}
