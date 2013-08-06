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

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Renders java.util.Date class within a table cell.
 */

public final class BooleanTableCellRenderer extends JCheckBox implements TableCellRenderer {


	private Color whiteColor = new Color(254, 254, 254);
	private Color alternateColor = new Color(240, 240, 255);
	private Color selectedColor = new Color(61, 128, 223);

	public BooleanTableCellRenderer() {
		super();
		setHorizontalAlignment(JCheckBox.CENTER);
	}

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

//		if (value == null) {
//			this.setText("");
//		} else if (value instanceof Boolean) {
//			Boolean b = (Boolean)value;
//			setSelected(b.booleanValue());
//		}
		setSelected((value != null && ((Boolean) value).booleanValue()));

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
		Color fg;
		if (selected)
			fg = Color.white;
		else
			fg = Color.black;
		setForeground(fg);

	}

}
