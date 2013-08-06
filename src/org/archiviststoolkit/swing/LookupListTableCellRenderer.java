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
 */

package org.archiviststoolkit.swing;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import org.archiviststoolkit.model.LookupListItems;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.mydomain.DomainSortableTable;

/**
 * Renders java.util.Date class within a table cell.
 */

public final class LookupListTableCellRenderer extends AlternatingRowColorCellRenderer {


	public static final Color AT_INITIAL_VALUE_COLOR = new Color(45,78,114);
	public static final Color NON_EDITABLE_COLOR = new Color(130,0,0);


	public LookupListTableCellRenderer(DomainSortableTable lookupListItems, int listType) {
		super();
		this.listType = listType;
		this.lookupListItems = lookupListItems;
	}

	public LookupListTableCellRenderer(DomainSortableTable lookupListItems, int listType, boolean includeRecordCount) {
		super();
		this.listType = listType;
		this.includeRecordCount = includeRecordCount;
		this.lookupListItems = lookupListItems;
	}

	private int listType;
	private boolean includeRecordCount = false;
	private DomainSortableTable lookupListItems;
	/**
	 * return the cell component to be rendered.
	 * @param table the jtable to render into
	 * @param value the value to  be rendered
	 * @param isSelected whether the cell is selected
	 * @param hasFocus has focus or not
	 * @param rowIndex the row within the table
	 * @param vColIndex the col within the table
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, Object, boolean, boolean, int, int)
	 * @return the component to be returned
	 */

	public Component getTableCellRendererComponent(final JTable table, final Object value,
												   final boolean isSelected, final boolean hasFocus, final int rowIndex, final int vColIndex) {
		Font font = null;

		this.setOpaque(true);
		setFont((font != null) ? font : table.getFont());

		LookupListItems item = (LookupListItems)lookupListItems.getSortedList().get(rowIndex);
		if (listType == LookupListUtils.EDITABLE) {
			if(item.getAtInitialValue()) {
				setForeground(AT_INITIAL_VALUE_COLOR);
			} else {
				setForeground(Color.black);
			}
		} else if (listType == LookupListUtils.MIXED) {
			if(!item.getEditable()) {
				setForeground(NON_EDITABLE_COLOR);
			} else if(item.getAtInitialValue()) {
				setForeground(AT_INITIAL_VALUE_COLOR);
			} else {
				setForeground(Color.black);
			}
		} else { //an uneditable list
			if(item.getEditable()) {
				setForeground(Color.black);
			} else {
				setForeground(NON_EDITABLE_COLOR);
			}
		}
//		setForeground(Color.green);
		if (value == null) {
			this.setText("");
		} else if (includeRecordCount && vColIndex == 0) {
			this.setText(value.toString() + " - " + item.getRecordCount()+ " record(s)");
		} else {
			this.setText(value.toString());
		}

		setForegroundAndBackground(isSelected,  rowIndex);
		return this;
	}

	protected void setForegroundAndBackground(boolean selected, int row) {
// Set the background color
		Color bg;
		if (!selected)
			bg = (row % 2 == 0 ? alternateColor : whiteColor);
		else
			bg = selectedColor;
		setBackground(bg);

// Set the foreground to white when selected
		if (selected) {
			setForeground(Color.white);
		}


	}

	public boolean isIncludeRecordCount() {
		return includeRecordCount;
	}

	public void setIncludeRecordCount(boolean includeRecordCount) {
		this.includeRecordCount = includeRecordCount;
	}

	public DomainSortableTable getLookupListItems() {
		return lookupListItems;
	}

	public void setLookupListItems(DomainSortableTable lookupListItems) {
		this.lookupListItems = lookupListItems;
	}
}
