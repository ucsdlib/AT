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
 * Date: Jun 22, 2006
 * Time: 10:26:02 AM
 */

package org.archiviststoolkit.util;

import org.archiviststoolkit.mydomain.DomainSortedTable;

import javax.swing.*;
import java.util.Vector;
import java.awt.event.ActionListener;

public class SequencedObjectsUtils {

    public static final String ADD_AT_BEGINING = "At begining";
    public static final String ADD_ABOVE_SELECTION = "Before selection";
    public static final String ADD_BELOW_SELECTION = "After selection";
    public static final String ADD_AT_END = "At End";
    public static final String DELETE = "Delete";


    public static DefaultComboBoxModel generateInsertWhereComboBoxModel(String label) {
        Vector<String> values = new Vector<String>();
        values.add(label);
        values.add(ADD_AT_BEGINING);
        values.add(ADD_ABOVE_SELECTION);
        values.add(ADD_BELOW_SELECTION);
        values.add(ADD_AT_END);
        return new DefaultComboBoxModel(values);
    }

    public static int determineSequenceOfNewItem(JComboBox comboBox, DomainSortedTable sortedTable) {
        return determineSequenceOfNewItem((String)comboBox.getSelectedItem(), sortedTable);
    }

    public static int determineSequenceOfNewItem(String whereString, DomainSortedTable sortedTable) {
        if (whereString.equalsIgnoreCase(ADD_AT_BEGINING)) {
            return 0;
        } else if (whereString.equalsIgnoreCase(ADD_ABOVE_SELECTION)) {
            int selectedRow = sortedTable.getSelectedRow();
            if (selectedRow == -1) {
                return 0;
            } else {
                return selectedRow;
            }
        } else if (whereString.equalsIgnoreCase(ADD_BELOW_SELECTION)) {
            int selectedRow = sortedTable.getSelectedRow();
            if (selectedRow == -1) {
                return 0;
            } else {
                return selectedRow + 1;
            }
        } else {
            return sortedTable.getRowCount();
        }
    }

    public static void createInsertPopupMenu(JPopupMenu menu, ActionListener listener, ActionListener deleteListener) {
        createPopupMenu(SequencedObjectsUtils.ADD_AT_BEGINING, menu, listener);
        createPopupMenu(SequencedObjectsUtils.ADD_ABOVE_SELECTION, menu, listener);
        createPopupMenu(SequencedObjectsUtils.ADD_BELOW_SELECTION, menu, listener);
        createPopupMenu(SequencedObjectsUtils.ADD_AT_END, menu, listener);
        createPopupMenu(SequencedObjectsUtils.DELETE, menu, deleteListener);
    }

    private static void createPopupMenu(String menuText, JPopupMenu menu, ActionListener listener) {
        menu.add(createMenuItem(menuText, listener));
    }

    public static void createInsertMenu(JMenu menu, ActionListener listener) {
        createMenu(SequencedObjectsUtils.ADD_AT_BEGINING, menu, listener);
        createMenu(SequencedObjectsUtils.ADD_ABOVE_SELECTION, menu, listener);
        createMenu(SequencedObjectsUtils.ADD_BELOW_SELECTION, menu, listener);
        createMenu(SequencedObjectsUtils.ADD_AT_END, menu, listener);
    }

    private static void createMenu(String menuText, JMenu menu, ActionListener listener) {
        menu.add(createMenuItem(menuText, listener));
    }

    private static JMenuItem createMenuItem(String menuText, ActionListener listener) {
        JMenuItem menuItem = new JMenuItem(menuText);
        menuItem.addActionListener(listener);
        return menuItem;
    }
}
