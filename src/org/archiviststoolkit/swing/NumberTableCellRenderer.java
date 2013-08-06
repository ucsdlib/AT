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

package org.archiviststoolkit.swing;

import com.jgoodies.validation.formatter.EmptyNumberFormatter;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.NumberFormat;

import org.archiviststoolkit.dialog.ErrorDialog;

/**
 * Renders java.util.Date class within a table cell.
 */

public final class NumberTableCellRenderer extends AlternatingRowColorCellRenderer {


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

		if (value == null) {
			this.setText("");
		} else if (value instanceof Number || value instanceof Double) {
			this.setHorizontalAlignment(SwingConstants.RIGHT);
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setGroupingUsed(false);
			EmptyNumberFormatter format = new EmptyNumberFormatter(numberFormat);
			try {
				this.setText(format.valueToString(value));
			} catch (ParseException e) {
				new ErrorDialog("", e).showDialog();
			}
		}

		setForegroundAndBackground(isSelected,  rowIndex);
		return this;
	}
}
