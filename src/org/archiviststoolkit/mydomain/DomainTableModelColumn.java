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
 */


package org.archiviststoolkit.mydomain;

//==============================================================================
//Import Declarations
//==============================================================================

import java.lang.reflect.Method;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.beans.Introspector;
import java.beans.IntrospectionException;

/**
 * A Table Column Model for JTable implementations.
 */

public class DomainTableModelColumn {

	/**
	 * The name to be displayed.
	 */

	private String name = null;

	/**
	 * The readMethod call to get the value for this column.
	 */

	private Method readMethod = null;

	/**
	 * The readMethod call to set the value for this column.
	 */

	private Method writeMethod = null;

	/**
	 * The class of the values in this column.
	 */

	private Class clazz = null;
	private String fieldName = null;
	private Boolean editable = false;

	/**
	 * Default Constructor.
	 *
	 * @param name	   the name of the column
	 * @param fieldName  the field name associated with recall the value
	 * @param modelClass the class of the field name associated with recall the value
	 * @param clazz	  the class of the values within the column
	 */

	public DomainTableModelColumn(final String name, String fieldName, final Class modelClass, final Class clazz) {
		this.name = name;
		this.setFieldName(fieldName);
		this.readMethod = readMethodFromName(fieldName, modelClass);
		this.clazz = clazz;
	}

	/**
	 * Default Constructor.
	 *
	 * @param name	   the name of the column
	 * @param fieldName  the field name associated with recall the value
	 * @param modelClass the class of the field name associated with recall the value
	 * @param clazz	  the class of the values within the column
	 */

	public DomainTableModelColumn(final String name, String fieldName, final Class modelClass, final Class clazz, final Boolean editable) {
		this.name = name;
		this.setFieldName(fieldName);
		this.readMethod = readMethodFromName(fieldName, modelClass);
		this.writeMethod = writeMethodFromName(fieldName, modelClass);
		this.clazz = clazz;
		this.editable = editable;
	}

	/**
	 * Get the name of the column.
	 *
	 * @return the name of the column
	 */

	public final String getColumnName() {
		return this.name;
	}

	/**
	 * Get the readMethod used to gather the value for display.
	 *
	 * @return the readMethod
	 */

	public final Method getReadColumnMethod() {
		return this.readMethod;
	}

	/**
	 * Get the writeMethod used to gather the value for display.
	 *
	 * @return the writeMethod
	 */

	public final Method getWriteColumnMethod() {
		return this.writeMethod;
	}

	/**
	 * Get the class of the column values.
	 *
	 * @return the class of the column values
	 */

	public final Class getColumnClass() {
		return this.clazz;
	}

	private Method readMethodFromName(String fieldName, Class clazz) {
		BeanInfo beanInfo;
		PropertyDescriptor [] propertyDescriptors = null;
		try {
			beanInfo = Introspector.getBeanInfo(clazz);
			propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor p : propertyDescriptors) {
				if (p.getName().equals(fieldName)) {
					return p.getReadMethod();
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

		return null;

	}

	private Method writeMethodFromName(String fieldName, Class clazz) {
		BeanInfo beanInfo;
		PropertyDescriptor [] propertyDescriptors = null;
		try {
			beanInfo = Introspector.getBeanInfo(clazz);
			propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor p : propertyDescriptors) {
				if (p.getName().equals(fieldName)) {
					return p.getWriteMethod();
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

		return null;

	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}
}