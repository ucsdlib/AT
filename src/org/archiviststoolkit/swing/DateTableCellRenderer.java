//==============================================================================
//            _            __
//  ___  __ _| | ___  ___ / _| ___  _ __ __ _  ___
// / __|/ _` | |/ _ \/ __| |_ / _ \| '__/ _` |/ _ \
// \__ \ (_| | |  __/\__ \  _| (_) | | | (_| |  __/
// |___/\__,_|_|\___||___/_|  \___/|_|  \__, |\___|
//                                      |___/
//
// 01110011 01100001 01101100 01100101 01110011
// 01100110 01101111 01110010 01100111 01100101
//
//==============================================================================
// Copyright 2004 (c) the salesforge community. All Rights Reserved.
//==============================================================================
// Licensed under the Open Software License version 2.1
//==============================================================================
// $Author: lejames $
// $Date: 2004/12/12 02:06:49 $
// $Revision: 1.2 $
//==============================================================================

package org.archiviststoolkit.swing;

//==============================================================================
//Import Declarations
//==============================================================================

import com.jgoodies.validation.formatter.EmptyDateFormatter;

import java.awt.Component;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.archiviststoolkit.ApplicationFrame;

/**
 * Renders java.util.Date class within a table cell.
 */

public final class DateTableCellRenderer extends AlternatingRowColorCellRenderer {


	/**
	 * return the cell component to be rendered.
	 * @param table the jtable to render into
	 * @param value the value to  be rendered
	 * @param isSelected whether the cell is selected
	 * @param hasFocus has focus or not
	 * @param rowIndex the row within the table
	 * @param vColIndex the col within the table
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 * @return the component to be returned
	 */

	public Component getTableCellRendererComponent(final JTable table, final Object value,
												   final boolean isSelected, final boolean hasFocus, final int rowIndex, final int vColIndex) {
		Font font = null;

		this.setOpaque(true);
		setFont((font != null) ? font : table.getFont());

		if (value == null) {
			this.setText("");
		} else if (value instanceof Date) {
			EmptyDateFormatter format = new EmptyDateFormatter(ApplicationFrame.applicationDateFormat);
			try {
				this.setText(format.valueToString(value));
			} catch (ParseException e) {
				e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			}
		}

		setForegroundAndBackground(isSelected,  rowIndex);
		return this;
	}
}