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
 */


package org.archiviststoolkit.mydomain;

//==============================================================================
//Import Declarations
//==============================================================================

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collection;
import java.lang.reflect.Method;

/**
 * The model used to display lists of domain objects inside a JTable.
 */

public class DomainTableModel extends AbstractTableModel implements DomainAccessListener {

	/**
	 * The class of this domain table.
	 */

	protected Class clazz = null;

	/**
	 * The list of domainobjects to display.
	 */

	protected ArrayList<DomainObject> domainCollection = null;

	/**
	 * A list of column models.
	 */

	protected ArrayList columnModel = null;
	/**
	 * Get the name of a specific column.
	 * @param index the index of the column
	 * @return The name of the column
	 */

	/**
	 * Get the table model for a specific column.
	 *
	 * @param index the index of the column
	 * @return the table model for that column
	 */

	public DomainTableModelColumn getTableModelColumn(final int index) {
		return (DomainTableModelColumn) columnModel.get(index);
	}

	/**
	 * Get the name of a specific column.
	 *
	 * @param index the index of the column
	 * @return The name of the column
	 */

	public String getColumnName(final int index) {
		return (getTableModelColumn(index).getColumnName());
	}

	/**
	 * Get the number of columns.
	 *
	 * @return the number of columns
	 */

	public int getColumnCount() {
		return (columnModel.size());
	}

	/**
	 * Get the class associated with a column.
	 *
	 * @param index the index of the column
	 * @return the class for that column
	 */

	public Class getColumnClass(final int index) {
		return (getTableModelColumn(index).getColumnClass());
	}

	/**
	 * Get the value for a specific column.
	 *
	 * @param object the domain object used for this row
	 * @param column the column to get
	 * @return the value
	 */

	public Object getColumnValue(final Object object, final int column) {
		try {
			Method method = getTableModelColumn(column).getReadColumnMethod();

			if (method != null) {
				return method.invoke(object);
			} else {
				System.out.println("Method was null " + object + " " + column);
			}
		} catch (Throwable th) {
			System.out.println("Failed to get column Value " + th);
		}

		return null;
	}

	/**
	 * Get the value for a specific column.
	 *
	 * @param object the domain object used for this row
	 * @param column the column to get
	 * @return the value
	 */

	public Object setColumnValue(final Object object, final int column, Object value) {
		try {
			Method method = getTableModelColumn(column).getWriteColumnMethod();

			if (method != null) {
				return method.invoke(object, value);
			} else {
				System.out.println("Method was null " + object + " " + column);
			}
		} catch (Throwable th) {
			System.out.println("Failed to get column Value " + th);
		}

		return null;
	}

	/**
	 * Get the domain model class.
	 *
	 * @return the domain model class
	 */

	public boolean isCellEditable(int row, int col) {
		return getTableModelColumn(col).getEditable();
	}

	public void setValueAt(Object value, int row, int col) {

		DomainObject domainObject = get(row);
		setColumnValue(domainObject, col, value);
		fireTableCellUpdated(row, col);
	}

	/**
	 * Get the domain model class.
	 *
	 * @return the domain model class
	 */

	public Class getDomainClass() {
		return (clazz);
	}

	/**
	 * Notify this model that the domain has changed state.
	 *
	 * @param event the type of state change
	 */

	public void domainChanged(final DomainAccessEvent event) {
		fireTableDataChanged();
	}

	/**
	 * Get the number of rows in this domain.
	 *
	 * @return the number of rows
	 */

	public int getRowCount() {
		return (getDomainCollection().size());
	}

	/**
	 * Get the value at a specific row or column.
	 *
	 * @param row	the row
	 * @param column the column
	 * @return the value at that location
	 */

	public Object getValueAt(final int row, final int column) {
		return (getColumnValue(getDomainCollection().get(row), column));
	}

	/**
	 * Get a domain object for a specific row in the list.
	 *
	 * @param index the index within the row
	 * @return the domain object
	 */

	public DomainObject get(final int index) {
		return (getDomainCollection().get(index));
	}

	/**
	 * Get the row index of a specific domain object.
	 *
	 * @param object the object in question
	 * @return the index within the row list
	 */

	public int getIndex(final DomainObject object) {
		return (getDomainCollection().indexOf(object));
	}

	public void setDomainObject(final int index, final DomainObject object) {
		getDomainCollection().set(index, object);
	}

	public void addDomainObject(final DomainObject object) {
		domainCollection.add(object);
	}

	public void addDomainObject(final int index, final DomainObject object) {
		domainCollection.add(index, object);
	}

	public void removeDomainObject(final int index) {
		domainCollection.remove(index);
	}

	public void updateCollection(Collection newCollection) {

		if (newCollection == null) {
			domainCollection = new ArrayList<DomainObject>();
		} else {
			domainCollection = new ArrayList<DomainObject>(newCollection);
		}

	}

	public ArrayList<DomainObject> getDomainCollection() {
		return domainCollection;
	}

}