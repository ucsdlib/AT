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
 * Date: Jun 20, 2006
 * Time: 9:28:42 PM
 */

package org.archiviststoolkit.mydomain;

import ca.odell.glazedlists.*;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Enumeration;

import org.archiviststoolkit.swing.AlternatingRowColorTable;

public class DomainGlazedListTable extends AlternatingRowColorTable {
	protected DomainEventTableModel tableModel;
	protected EventList<DomainObject> eventList = GlazedLists.threadSafeList(new BasicEventList());
	protected SortedList<DomainObject> sortedList;
	protected FilterList<DomainObject> filteredList = null;
	protected DomainTableFormat tableFormat;
	protected JTextField filterField;
	protected Class clazz;

	public DomainGlazedListTable() {
		super();
	}

	public DomainGlazedListTable(Class clazz) {
		super();
		init(clazz, null);
	}

	public DomainGlazedListTable(Class clazz, JTextField filterField) {
		super();
		init(clazz, filterField);
	}

	public DomainGlazedListTable(Class clazz, JTextField filterField, DomainTableFormat tableFormat) {
		super();
		init(clazz, filterField, tableFormat);
	}

	public void updateCollection(Collection newCollection) {
        getSelectionModel().clearSelection();
        eventList.clear();
		if (newCollection != null) {
			eventList.addAll(newCollection);
		}
	}

	public void addDomainObject(DomainObject domainObject) {
		eventList.add(domainObject);
	}

	public ArrayList<DomainObject> removeSelectedRows() {
		ArrayList<DomainObject> selectedDomainObjects = getSelectedObjects();
        for (DomainObject selectedDomainObject: selectedDomainObjects) {
            eventList.remove(selectedDomainObject);
        }
        return selectedDomainObjects;
	}

	public ArrayList<DomainObject> getSelectedObjects() {
		int[] selectedRows = this.getSelectedRows();
        ArrayList<DomainObject> domainObjects = new ArrayList<DomainObject>();
        for (int selectedRow: selectedRows) {
            domainObjects.add(getDomainObject(selectedRow));
        }
        return domainObjects;
	}

	public DomainObject getDomainObject(int index) {
		DomainObject selectedDomainObject = null;
		if (filteredList == null) {
            if(sortedList.size() != 0 && index < sortedList.size()) {
                selectedDomainObject = sortedList.get(index);
            }
        } else {
			selectedDomainObject = filteredList.get(index);
		}
		return selectedDomainObject;
	}

	public void setDomainObject(int index, DomainObject domainObject) {
		if (filteredList == null) {
			sortedList.set(index, domainObject);
		} else {
			filteredList.set(index, domainObject);
		}
	}

	public void removeDomainObject(DomainObject domainObject) {
		eventList.remove(domainObject);
	}

	public SortedList<DomainObject> getSortedList() {
		return sortedList;
	}

	public FilterList<DomainObject> getFilteredList() {
		return filteredList;
	}

	public EventList<DomainObject> getEventList() {
		return eventList;
	}

	public DomainTableFormat getTableFormat() {
		return tableFormat;
	}

	protected void init(Class clazz, JTextField filterField) {
		init(clazz, filterField, null);
	}

	protected void init(Class clazz, JTextField filterField, DomainTableFormat tableFormat) {
		if (tableFormat == null) {
			this.tableFormat = new DomainTableFormat(clazz);
		} else {
			this.tableFormat = tableFormat;
		}
		this.clazz = clazz;
		this.filterField = filterField;
		sortedList = new SortedList<DomainObject>(eventList);

		if (filterField == null) {
			tableModel = new DomainEventTableModel(sortedList, this.tableFormat);
		} else {
			TextComponentMatcherEditor textMatcherEditor = new TextComponentMatcherEditor(filterField, new DomainFilterator());
//			textMatcherEditor.setStrategy(TextComponentMatcherEditor.UNICODE_STRATEGY);
			filteredList = new FilterList<DomainObject>(sortedList, textMatcherEditor);
			tableModel = new DomainEventTableModel(filteredList, this.tableFormat);
		}
		super.setModel(tableModel);
	}

	public void updateColumns() {
		tableFormat = new DomainTableFormat(clazz);
		tableModel.setTableFormat(tableFormat);
//		tableModel.fireTableStructureChanged();
	}

	public void setClazz(Class clazz) {
		init(clazz, null);
	}

	public void setClazzAndColumns(Class clazz, String... fields) {
		DomainTableFormat tableFormat = new DomainTableFormat(clazz, fields);
		init(clazz, null, tableFormat);
	}

	public void setCellRenderer(TableCellRenderer cellRenderer) {
		Enumeration<TableColumn> columns = this.getColumnModel().getColumns();
		TableColumn column;
		while (columns.hasMoreElements()) {
			column = columns.nextElement();
			column.setCellRenderer(cellRenderer);
		}
	}

}
