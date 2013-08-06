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
 * Date: Aug 10, 2006
 * Time: 10:18:46 PM
 */

package org.archiviststoolkit.swing;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import java.awt.*;

public class AlternatingRowColorCellRenderer extends DefaultTableCellRenderer {

	protected Color whiteColor = new Color(254, 254, 254);
	protected Color alternateColor = new Color(240, 240, 255);
	protected Color selectedColor = new Color(61, 128, 223);

	public Component getTableCellRendererComponent(JTable table,
												   Object value, boolean selected, boolean focused,
												   int row, int column) {
		super.getTableCellRendererComponent(table, value,
				selected, focused, row, column);

		setForegroundAndBackground(selected,  row);
		if (value instanceof String) {
			setToolTipText((String)value);
		}
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
