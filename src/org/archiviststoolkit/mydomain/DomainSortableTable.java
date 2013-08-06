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
 * Date: Jun 11, 2006
 * Time: 9:02:27 PM
 */

package org.archiviststoolkit.mydomain;

import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;

import org.archiviststoolkit.swing.ArrayTransfer;
import org.archiviststoolkit.model.Locations;

public class DomainSortableTable extends DomainGlazedListTable {

	private TableComparatorChooser tableComparatorChooser;

	public DomainSortableTable() {
		super();
	}

	public DomainSortableTable(Class clazz) {
		super(clazz);
		initSortableColumns();
	}

	public DomainSortableTable(Class clazz, String defaultSortFieldName) {
		super(clazz);
		initSortableColumns();
		setSortColumn(defaultSortFieldName);
	}

	public DomainSortableTable(Class clazz, String... fields) {
		super(clazz);
		super.setClazzAndColumns(clazz, fields);
		initSortableColumns();
	}

	public DomainSortableTable(Class clazz, JTextField filterField, DomainTableFormat tableFormat) {
		super(clazz, filterField, tableFormat);
		initSortableColumns();
	}

	public DomainSortableTable(Class clazz, JTextField filterField) {
		super(clazz, filterField);
		initSortableColumns();
	}

	protected void initSortableColumns() {
		tableComparatorChooser = new TableComparatorChooser(this, sortedList, true);
	}

	public void updateColumns(Collection resultSet) {
		super.updateColumns();
		initSortableColumns();
	}

	public void setClazz(Class clazz) {
		super.setClazz(clazz);
		initSortableColumns();
	}

	public void setClazz(Class clazz, String defaultSortFieldName) {
		super.setClazz(clazz);
		initSortableColumns();
		setSortColumn(defaultSortFieldName);
	}

	public void setClazzAndColumns(Class clazz, String... fields) {
		super.setClazzAndColumns(clazz, fields);
		initSortableColumns();
	}

	public void setClazzAndColumns(String defaultSortFieldName, Class clazz, String... fields) {
		super.setClazzAndColumns(clazz, fields);
		initSortableColumns();
		if (defaultSortFieldName != null) {
			setSortColumn(defaultSortFieldName);
		}
	}

	private void setSortColumn(String fieldName) {
		Integer defaultSortColumn = tableFormat.getColumnNumberByFieldName(fieldName);
		if (defaultSortColumn != null) {
			tableComparatorChooser.appendComparator(defaultSortColumn, 0, false);
		}
	}

}
