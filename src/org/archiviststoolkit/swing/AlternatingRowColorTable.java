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
 * Date: May 17, 2006
 * Time: 4:11:29 PM
 */

package org.archiviststoolkit.swing;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Date;

public class AlternatingRowColorTable extends JTable {

	public static final boolean TABLE_TYPE_NORMAL = false;
	public static final boolean TABLE_TYPE_PAINT_EMPTY_ROWS = true;


	private Color alternateColor = new Color(240, 240, 255);
	private boolean paintEmptyRows = TABLE_TYPE_NORMAL;

	/**
	 * Constructs a default <code>JTable</code> that is initialized with a default
	 * data model, a default column model, and a default selection
	 * model.
	 *
	 * @see #createDefaultDataModel
	 * @see #createDefaultColumnModel
	 * @see #createDefaultSelectionModel
	 */
	public AlternatingRowColorTable() {
		super();
		this.setIntercellSpacing(new Dimension(1, 1));
		this.setShowVerticalLines(true);
		this.setShowHorizontalLines(false);
		this.setGridColor(Color.lightGray);
		this.setDefaultRenderer(Date.class, new DateTableCellRenderer());
		this.setDefaultRenderer(Number.class, new NumberTableCellRenderer());
		this.setDefaultRenderer(Double.class, new NumberTableCellRenderer());
		this.setDefaultRenderer(Boolean.class, new BooleanTableCellRenderer());

		setTableType(paintEmptyRows);
	}

	public AlternatingRowColorTable(boolean tableType) {
		super();
		this.setIntercellSpacing(new Dimension(1, 1));
		this.setShowVerticalLines(true);
		this.setShowHorizontalLines(false);
		this.setGridColor(Color.lightGray);
		this.setDefaultRenderer(Date.class, new DateTableCellRenderer());
		this.setDefaultRenderer(Number.class, new NumberTableCellRenderer());
		this.setDefaultRenderer(Double.class, new NumberTableCellRenderer());
		this.setDefaultRenderer(Boolean.class, new BooleanTableCellRenderer());
		setTableType(tableType);
	}

	public void setTableType(boolean tableType) {
		paintEmptyRows = tableType;
		if (!paintEmptyRows) {
			this.setDefaultRenderer(Object.class, new AlternatingRowColorCellRenderer());
		}
	}

//	public AlternatingRowColorTable(Class clazz) {
//		super(clazz);
//	}
//
//	public AlternatingRowColorTable(Class clazz, String defaultSortFieldName) {
//		super(clazz, defaultSortFieldName);
//	}
//
//	public AlternatingRowColorTable(Class clazz, JTextField filterField) {
//		super(clazz, filterField);
//	}
//
	/**
	 * Paints empty rows too, after letting the UI delegate do
	 * its painting.
	 */
	public void paint(Graphics g) {
		super.paint(g);
		if (paintEmptyRows) {
			paintEmptyRows(g);
		}
	}

	/**
	 * Paints the backgrounds of the implied empty rows when the
	 * table model is insufficient to fill all the visible area
	 * available to us. We don't involve cell renderers, because
	 * we have no data.
	 */
	protected void paintEmptyRows(Graphics g) {
		final int rowCount = getRowCount();
		final Rectangle clip = this.getVisibleRect();
		if (rowCount * rowHeight < clip.height) {
			for (int i = rowCount; i <= clip.height / rowHeight; ++i) {
				g.setColor(colorForRow(i));
				g.fillRect(clip.x, i * rowHeight, clip.width, rowHeight);
			}
		}
	}

	/**
	 * Changes the behavior of a table in a JScrollPane to be more like
	 * the behavior of JList, which expands to fill the available space.
	 * JTable normally restricts its size to just what's needed by its
	 * model.
	 */
	public boolean getScrollableTracksViewportHeight() {
		if (paintEmptyRows) {
			if (getParent() instanceof JViewport) {
				JViewport parent = (JViewport) getParent();
				return (parent.getHeight() > getPreferredSize().height);
			}
			return false;
		} else {
			return false;
		}
	}

	/**
	 * Returns the appropriate background color for the given row.
	 */
	protected Color colorForRow(int row) {
		return (row % 2 == 0) ? alternateColor : getBackground();
	}

	/**
	 * Shades alternate rows in different colors.
	 */
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		if (paintEmptyRows) {
			Component c = super.prepareRenderer(renderer, row, column);
			if (!isCellSelected(row, column)) {
				c.setBackground(colorForRow(row));
				c.setForeground(getForeground());
			} else {
				c.setBackground(UIManager.getColor("Table.selectionBackground"));
				c.setForeground(getForeground());
			}
			return c;
		} else {
			return super.prepareRenderer(renderer, row, column);
		}
	}

}
