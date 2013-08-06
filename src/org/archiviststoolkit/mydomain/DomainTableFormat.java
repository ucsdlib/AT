package org.archiviststoolkit.mydomain;

import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.GlazedLists;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Comparator;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.TableNotConfiguredException;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.exceptions.FieldNotConfiguredException;
import org.archiviststoolkit.model.Locations;
import org.apache.commons.beanutils.BeanUtils;

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
 * @author Lee Mandell
 * Date: Jun 8, 2006
 * Time: 9:15:11 PM
 */
public class DomainTableFormat implements AdvancedTableFormat {


	public DomainTableFormat(Class clazz) {
		this.clazz = clazz;
		columnNames = new ArrayList<String>();
		getMethods = new ArrayList<Method>();
		columnFieldNames = new ArrayList<String>();
		columnClasses = new ArrayList<Class>();


		try {
			TreeSet<ATFieldInfo> fieldList = ATFieldInfo.getReturnScreenList(this.clazz);
			for (ATFieldInfo field : fieldList) {
				columnNames.add(field.getFieldLabel());
				if (field.getGetMethod() == null) {
					throw new FieldNotConfiguredException("Field " + field.getClazz().getName() + ":" + field.getFieldName() + " has no getter method");
				} else {
					getMethods.add(field.getGetMethod());
				}
				columnFieldNames.add(field.getFieldName());
				columnClasses.add(field.getFieldClass());
			}
		} catch (TableNotConfiguredException e) {
			ErrorDialog dialog = new ErrorDialog("Table not configured: " + clazz.getSimpleName(),
					StringHelper.getStackTrace(e));
			dialog.showDialog();
		} catch (FieldNotConfiguredException e) {
			new ErrorDialog("Error creating table format", e).showDialog();
		}
	}

	public DomainTableFormat(Class clazz, String... fields) {
		this.clazz = clazz;
		columnNames = new ArrayList<String>();
		getMethods = new ArrayList<Method>();
		columnFieldNames = new ArrayList<String>();
		columnClasses = new ArrayList<Class>();
		ATFieldInfo fieldInfo;

		for (String field : fields) {
			fieldInfo = ATFieldInfo.getFieldInfo(clazz, field);
			if (fieldInfo != null) {
				columnNames.add(fieldInfo.getFieldLabel());
				getMethods.add(fieldInfo.getGetMethod());
				columnFieldNames.add(fieldInfo.getFieldName());
				columnClasses.add(fieldInfo.getFieldClass());
			}
		}
	}

	private Class clazz;

	private ArrayList<String> columnNames;
	private ArrayList<Method> getMethods;
	private ArrayList<String> columnFieldNames;
	private ArrayList<Class> columnClasses;

	public int getColumnCount() {
		return columnNames.size();
	}

	public String getColumnName(int i) {
		return columnNames.get(i);
	}

	public Object getColumnValue(Object o, int i) {
		try {
//			System.out.println("Object:" + o + " Method:" + getMethods.get(i));
			//return getMethods.get(i).invoke(o);
      Object ro = getMethods.get(i).invoke(o); // get the object to return
      if(ro != null && ro instanceof String) {
        String s = (String)ro;
        return StringHelper.tagRemover(s);
      }
      else {
        return ro;
      }
    } catch (IllegalAccessException e) {
			new ErrorDialog("Error getting column value", StringHelper.getStackTrace(e)).showDialog();
			return null;
		} catch (InvocationTargetException e) {
			new ErrorDialog("Error getting column value", StringHelper.getStackTrace(e)).showDialog();
			return null;
		}
	}

	public String getColumnFieldName(int i) {
		return columnFieldNames.get(i);
	}

	public Class getColumnClass(int i) {
		return columnClasses.get(i);
	}

	/**
	 * Returns the default {@link java.util.Comparator} to use for the specified column.
	 * This {@link java.util.Comparator} may be used to determine how a table will be sorted.
	 *
	 * @return the {@link java.util.Comparator} to use or <code>null</code> for an unsortable
	 *         column.
	 * @see ca.odell.glazedlists.GlazedLists
	 */
	public Comparator getColumnComparator(int column) {
//		if (clazz == Locations.class && getColumnName(column).equalsIgnoreCase(Locations.PROPERTYNAME_COORDINATES)) {
//			return new LocationCoordinateComparator(this, column);
//		} else
		if (getColumnClass(column) == String.class){
			return GlazedLists.caseInsensitiveComparator();
		} else {
			return GlazedLists.comparableComparator();
		}
	}

	public Integer getColumnNumberByFieldName(String fieldName) {
		for (int i = 0; i < columnFieldNames.size(); i++) {
			if (columnFieldNames.get(i).equalsIgnoreCase(fieldName)) {
				return i;
			}
		}
		return null;
	}
}
