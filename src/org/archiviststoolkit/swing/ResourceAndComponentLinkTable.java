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

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.mydomain.DomainSortableTable;
import org.archiviststoolkit.model.Resources;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

import ca.odell.glazedlists.SortedList;

public class ResourceAndComponentLinkTable extends DomainSortableTable {

	private Color alternateForegroundColor = Color.RED;

	public ResourceAndComponentLinkTable(Class clazz, String defaultSortFieldName) {
		super(clazz, defaultSortFieldName);
	}

	public ResourceAndComponentLinkTable(Class clazz) {
		super(clazz);
	}

    /**
     * Constructs a default <code>JTable</code> that is initialized with a default
     * data model, a default column model, and a default selection
     * model.
     *
     * @see #createDefaultDataModel
     * @see #createDefaultColumnModel
     * @see #createDefaultSelectionModel
     */
    public ResourceAndComponentLinkTable() {
        super();
    }

	protected Color forgroundColorForRow(int row) {
		SortedList<DomainObject> sortedList = (SortedList<DomainObject>)this.getSortedList();
		Resources resource = (Resources)sortedList.get(row);
		return resource.isHitAtComponentLevel() ? alternateForegroundColor : getForeground();
	}

	/**
	 * Shades alternate rows in different colors.
	 */
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		Component c = super.prepareRenderer(renderer, row, column);
		if (!isCellSelected(row, column)) {
			c.setBackground(colorForRow(row));
			c.setForeground(forgroundColorForRow(row));
		} else {
			c.setBackground(UIManager.getColor("Table.selectionBackground"));
			c.setForeground(forgroundColorForRow(row));
		}
		return c;
	}

}
