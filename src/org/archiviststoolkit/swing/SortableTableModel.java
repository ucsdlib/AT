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
 */

package org.archiviststoolkit.swing;

//==============================================================================    
// Import Declarations
//==============================================================================    

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * Facade which allows for sorting of tables.
 */

public class SortableTableModel extends AbstractTableModel {
	/** the table model we sit on. */
    private TableModel tableModel;

    /** are we sorting in descending mode. */
    public static final int DESCENDING = -1;
    /** are we sorted at all.*/
    public static final int NOT_SORTED = 0;
    /** Ascending. */
    public static final int ASCENDING = 1;

    /** Empty Directive. */
    private static final Directive EMPTY_DIRECTIVE = new Directive(-1, NOT_SORTED);

    /** Comparator. */
    public static final Comparator COMPARABLE_COMAPRATOR = new Comparator() {
        public int compare(Object o1, Object o2) {
			if (o1 instanceof String) {
				String string1 = (String)o1;
				String string2 = (String)o2;
				return string1.toLowerCase().compareTo(string2.toLowerCase());

			} else {
				return ((Comparable) o1).compareTo(o2);
 			}
       }
    };
    
    /** Lexical comparitor. */
    public static final Comparator LEXICAL_COMPARATOR = new Comparator() {
        public int compare(Object o1, Object o2) {
			if (o1 instanceof String) {
					String string1 = (String)o1;
					String string2 = (String)o2;
					return string1.toLowerCase().compareTo(string2.toLowerCase());

				} else {
					return ((Comparable) o1).compareTo(o2);
				 }
         }
    };

    /** Row mapping. */
    private Row[] viewToModel;
    
    /** view mapping. */
    private int[] modelToView;

    /** table header. */
    private JTableHeader tableHeader;
    /** mouse listener. */
    private MouseListener mouseListener;
    /** table model listener. */
    private TableModelListener tableModelListener;
    /** column comparitors. */
    private Map columnComparators = new HashMap();
    /** sorting columns list. */
    private List sortingColumns = new ArrayList();

    /**
     * Constructor.
     */
    public SortableTableModel () {
        this.mouseListener = new MouseHandler();
        this.tableModelListener = new TableModelHandler();
    }

    /**
     * Constructor.
     * @param tableModel the table we will facade.
     */
    public SortableTableModel (TableModel tableModel) {
        this();
        setTableModel(tableModel);
    }

    /**
     * Constructor.
     * @param tableModel the table model to facade.
     * @param tableHeader a table header to use.
     */
    public SortableTableModel (TableModel tableModel, JTableHeader tableHeader) {
        this();
        setTableHeader(tableHeader);
        setTableModel(tableModel);
    }

    /**
     * Clear the sorting state.
     */
    private void clearSortingState() {
        viewToModel = null;
        modelToView = null;
    }

    /**
     * Get the table model.
     * @return the table model.
     */
    public TableModel getTableModel() {
        return tableModel;
    }

    /**
     * Set the table model.
     * @param tableModel the table model.
     */
    public void setTableModel(TableModel tableModel) {
        if (this.tableModel != null) {
            this.tableModel.removeTableModelListener(tableModelListener);
        }

        this.tableModel = tableModel;
        if (this.tableModel != null) {
            this.tableModel.addTableModelListener(tableModelListener);
        }

        clearSortingState();
        fireTableStructureChanged();
    }

    /**
     * Get the table header.
     * @return the table header.
     */
    public JTableHeader getTableHeader() {
        return tableHeader;
    }

    /**
     * Set the table header.
     * @param tableHeader the table header.
     */
    public void setTableHeader(JTableHeader tableHeader) {
        if (this.tableHeader != null) {
            this.tableHeader.removeMouseListener(mouseListener);
            TableCellRenderer defaultRenderer = this.tableHeader.getDefaultRenderer();
            if (defaultRenderer instanceof SortableHeaderRenderer) {
                this.tableHeader.setDefaultRenderer(((SortableHeaderRenderer) defaultRenderer).tableCellRenderer);
            }
        }
        this.tableHeader = tableHeader;
        if (this.tableHeader != null) {
            this.tableHeader.addMouseListener(mouseListener);
            this.tableHeader.setDefaultRenderer(
                    new SortableHeaderRenderer(this.tableHeader.getDefaultRenderer()));
        }
    }

    /**
     * Is the column sorted.
     * @return true or false
     */
    public boolean isSorting() {
        return sortingColumns.size() != 0;
    }

    /**
     * Get the current directive.
     * @param column the column
     * @return the directive.
     */
    private Directive getDirective(int column) {
        for (int i = 0; i < sortingColumns.size(); i++) {
            Directive directive = (Directive)sortingColumns.get(i);
            if (directive.column == column) {
                return directive;
            }
        }
        return EMPTY_DIRECTIVE;
    }

    /**
     * Get the sorting status.
     * @param column the column
     * @return the status.
     */
    public int getSortingStatus(int column) {
        return getDirective(column).direction;
    }

    /**
     * change the sorting status.
     */
    private void sortingStatusChanged() {
        clearSortingState();
        fireTableDataChanged();
        if (tableHeader != null) {
            tableHeader.repaint();
        }
    }

    /**
     * Set the sorting status.
     * @param column the column
     * @param status the status.
     */
    public void setSortingStatus(int column, int status) {
        Directive directive = getDirective(column);
        if (directive != EMPTY_DIRECTIVE) {
            sortingColumns.remove(directive);
        }
        if (status != NOT_SORTED) {
            sortingColumns.add(new Directive(column, status));
        }
        sortingStatusChanged();
    }

    /**
     * Get the header icon to render.
     * @param column the column
     * @param size the size
     * @return the icon
     */
    protected Icon getHeaderRendererIcon(int column, int size) {
        Directive directive = getDirective(column);
        if (directive == EMPTY_DIRECTIVE) {
            return null;
        }
        return new Arrow(directive.direction == DESCENDING, size, sortingColumns.indexOf(directive));
    }

    /**
     * Cancel the sorting.
     */
    private void cancelSorting() {
        sortingColumns.clear();
        sortingStatusChanged();
    }

    /**
     * Set the comparator for a column.
     * @param type the type.
     * @param comparator the comparator.
     */
    public void setColumnComparator(Class type, Comparator comparator) {
        if (comparator == null) {
            columnComparators.remove(type);
        } else {
            columnComparators.put(type, comparator);
        }
    }

    /** 
     * Get the comparator for a column.
     * @param column the column.
     * @return the comparator.
     */
    protected Comparator getComparator(int column) {
        Class columnType = tableModel.getColumnClass(column);
        Comparator comparator = (Comparator) columnComparators.get(columnType);
        if (comparator != null) {
            return comparator;
        }
        if (Comparable.class.isAssignableFrom(columnType)) {
            return COMPARABLE_COMAPRATOR;
        }
        return LEXICAL_COMPARATOR;
    }

    /**
     * Map the view to a model.
     * @return the view to model map.
     */
    private Row[] getViewToModel() {
        if (viewToModel == null) {
            int tableModelRowCount = tableModel.getRowCount();
            viewToModel = new Row[tableModelRowCount];
            for (int row = 0; row < tableModelRowCount; row++) {
                viewToModel[row] = new Row(row);
            }

            if (isSorting()) {
                Arrays.sort(viewToModel);
            }
        }
        return viewToModel;
    }

    /**
     * Get the model index.
     * @param viewIndex the index
     * @return the model index
     */
    public int modelIndex(int viewIndex) {
        return getViewToModel()[viewIndex].modelIndex;
    }

    /**
     * Get the model to view map.
     * @return the map
     */
    private int[] getModelToView() {
        if (modelToView == null) {
            int n = getViewToModel().length;
            modelToView = new int[n];
            for (int i = 0; i < n; i++) {
                modelToView[modelIndex(i)] = i;
            }
        }
        return modelToView;
    }

    /**
     * Get the row count.
     * @return the row count.
     */
    public int getRowCount() {
    	if (tableModel == null) {
    		return (0);
    	}
    	
        return (tableModel.getRowCount());
    }

    /**
     * Get the column  count.
     * @return the number of columns.
     */
    public int getColumnCount() {
    	if (tableModel == null) {
    		return (0);
    	}
    	
        return(tableModel.getColumnCount());
    }

    /**
     * Get the column name.
     * @param column the index of the column
     * @return the name of the column.
     */
    public String getColumnName(int column) {
        return tableModel.getColumnName(column);
    }

    /**
     * Get the column class.
     * @param column the column index.
     * @return the class of the column
     */
    public Class getColumnClass(int column) {
        return tableModel.getColumnClass(column);
    }

    /**
     * Is the cell editable.
     * @param row the row index.
     * @param column the column index.
     * @return is the cell editable.
     */
    public boolean isCellEditable(int row, int column) {
        return tableModel.isCellEditable(modelIndex(row), column);
    }

    /**
     * Get the value at a cell.
     * @param row the row
     * @param column the column
     * @return the object.
     */
    public Object getValueAt(int row, int column) {
        return tableModel.getValueAt(modelIndex(row), column);
    }

    /** Set value at a cell.
     * @param aValue the value
     * @param row the row
     * @param column the column
     */
    public void setValueAt(Object aValue, int row, int column) {
        tableModel.setValueAt(aValue, modelIndex(row), column);
    }


    /**
     * A row comparison helper class.
     */
    private class Row implements Comparable {
    	/** model index. */
        private int modelIndex;

        /** 
         * Constructor.
         * @param index the index
         */
        public Row(int index) {
            this.modelIndex = index;
        }

        /**
         * Comparison.
         * @param o the object.
         * @return a measure of comparison.
         */
        public int compareTo(Object o) {
            int row1 = modelIndex;
            int row2 = ((Row) o).modelIndex;

            for (Iterator it = sortingColumns.iterator(); it.hasNext();) {
                Directive directive = (Directive) it.next();
                int column = directive.column;
                Object o1 = tableModel.getValueAt(row1, column);
                Object o2 = tableModel.getValueAt(row2, column);

                int comparison = 0;
                // Define null less than everything, except null.
                if (o1 == null && o2 == null) {
                    comparison = 0;
                } else if (o1 == null) {
                    comparison = -1;
                } else if (o2 == null) {
                    comparison = 1;
                } else {
                    comparison = getComparator(column).compare(o1, o2);
                }
                if (comparison != 0) {
                	if (directive.direction == DESCENDING) {
                		return (-comparison);
                	} 
                	return (comparison);
                }
            }
            return 0;
        }
    }

    /**
     * TableModel handler helper class.
     */
    private class TableModelHandler implements TableModelListener {
    	
    	/** 
    	 * Notification that the able has changed.
    	 * @param e the event.
    	 */
        public void tableChanged(TableModelEvent e) {
            // If we're not sorting by anything, just pass the event along.             
            if (!isSorting()) {
                clearSortingState();
                fireTableChanged(e);
                return;
            }
                
            // If the table structure has changed, cancel the sorting; the             
            // sorting columns may have been either moved or deleted from             
            // the model. 
            if (e.getFirstRow() == TableModelEvent.HEADER_ROW) {
                cancelSorting();
                fireTableChanged(e);
                return;
            }

            // We can map a cell event through to the view without widening             
            // when the following conditions apply: 
            // 
            // a) all the changes are on one row (e.getFirstRow() == e.getLastRow()) and, 
            // b) all the changes are in one column (column != TableModelEvent.ALL_COLUMNS) and,
            // c) we are not sorting on that column (getSortingStatus(column) == NOT_SORTED) and, 
            // d) a reverse lookup will not trigger a sort (modelToView != null)
            //
            // Note: INSERT and DELETE events fail this test as they have column == ALL_COLUMNS.
            // 
            // The last check, for (modelToView != null) is to see if modelToView 
            // is already allocated. If we don't do this check; sorting can become 
            // a performance bottleneck for applications where cells  
            // change rapidly in different parts of the table. If cells 
            // change alternately in the sorting column and then outside of             
            // it this class can end up re-sorting on alternate cell updates - 
            // which can be a performance problem for large tables. The last 
            // clause avoids this problem. 
            int column = e.getColumn();
            if (e.getFirstRow() == e.getLastRow()
                    && column != TableModelEvent.ALL_COLUMNS
                    && getSortingStatus(column) == NOT_SORTED
                    && modelToView != null) {
                int viewIndex = getModelToView()[e.getFirstRow()];
                fireTableChanged(new TableModelEvent(SortableTableModel.this, 
                                                     viewIndex, viewIndex, 
                                                     column, e.getType()));
                return;
            }

            // Something has happened to the data that may have invalidated the row order. 
            clearSortingState();
            fireTableDataChanged();
            return;
        }
    }

    /**
     * Mouse handler for table headers, helper class.
     */

    private class MouseHandler extends MouseAdapter {
    	/**
    	 * Mouse has been clicked.
    	 * @param e the event
    	 */
        public void mouseClicked(MouseEvent e) {
        	System.out.println ("Got a mouse event in the header");
            JTableHeader h = (JTableHeader) e.getSource();
            TableColumnModel columnModel = h.getColumnModel();
            int viewColumn = columnModel.getColumnIndexAtX(e.getX());
            int column = columnModel.getColumn(viewColumn).getModelIndex();
            if (column != -1) {
                int status = getSortingStatus(column);
                if (!e.isControlDown()) {
                    cancelSorting();
                }
                // Cycle the sorting states through {NOT_SORTED, ASCENDING, DESCENDING} or 
                // {NOT_SORTED, DESCENDING, ASCENDING} depending on whether shift is pressed.
                if (e.isShiftDown()) {
                	status = status -1;
                } else {
                	status += 1;
                }
                
                status = (status + 4) % 3 - 1; // signed mod, returning {-1, 0, 1}
                setSortingStatus(column, status);
            }
        }
    }

    /** Arrow Icon helper class. */
    
    private static class Arrow implements Icon {
    	/** Icon is descending. */
        private boolean descending;
        /** Icon size. */
        private int size;
        /** Icon priority. */
        private int priority;

        /**
         * Constructor.
         * @param descending is descending
         * @param size of the icon
         * @param priority does this column have priority
         */
        public Arrow(boolean descending, int size, int priority) {
            this.descending = descending;
            this.size = size;
            this.priority = priority;
        }

        /**
         * paint the icon.
         * @param c the component to paint to
         * @param g the graphics context to use
         * @param x the x coord
         * @param y the y coord
         */
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Color color = null;
            
            if (c == null) {
            	color = Color.GRAY;
            } else {
            	color = c.getBackground();
            }
            
            // In a compound sort, make each succesive triangle 20% 
            // smaller than the previous one. 
            int dx = (int)(size/2*Math.pow(0.8, priority));
            int dy = -dx;
            
            if (descending) {
            	dy = dx;
            }
            
            // Align icon (roughly) with font baseline.
            if (descending) {
            	y = y + 5*size/6 + -dy;	
            } else {
            	y = y + 5*size/6;
            }
            
            
            int shift = -1;
            
            if (descending) {
            	shift = 1;
            }
            
            g.translate(x, y);

            // Right diagonal. 
            g.setColor(color.darker());
            g.drawLine(dx / 2, dy, 0, 0);
            g.drawLine(dx / 2, dy + shift, 0, shift);
            
            // Left diagonal. 
            g.setColor(color.brighter());
            g.drawLine(dx / 2, dy, dx, 0);
            g.drawLine(dx / 2, dy + shift, dx, shift);
            
            // Horizontal line. 
            if (descending) {
                g.setColor(color.darker().darker());
            } else {
                g.setColor(color.brighter().brighter());
            }
            g.drawLine(dx, 0, 0, 0);

            g.setColor(color);
            g.translate(-x, -y);
        }

        /**
         * Get the icon width.
         * @return the width of the icon.
         */
        public int getIconWidth() {
            return size;
        }

        /**
         * Get the icon height.
         * @return the height of the icon.
         */
        public int getIconHeight() {
            return size;
        }
    }

    /**
     * Sortable header renderer helper class.
     */
    private class SortableHeaderRenderer implements TableCellRenderer {
    	/** the table cell renderer. */
        private TableCellRenderer tableCellRenderer;

        /**
         * Constructor.
         * @param tableCellRenderer the cell renderer.
         */
        public SortableHeaderRenderer(TableCellRenderer tableCellRenderer) {
            this.tableCellRenderer = tableCellRenderer;
        }

        /**
         * Get the cell renderer component.
         * @param table the table to render to
         * @param value the value to use
         * @param isSelected is the cell selected
         * @param hasFocus does the cell have focus
         * @param row the row
         * @param column the column
         * @return the component to be rendered
         */
        public Component getTableCellRendererComponent(JTable table, 
                                                       Object value,
                                                       boolean isSelected, 
                                                       boolean hasFocus,
                                                       int row, 
                                                       int column) {
            Component c = tableCellRenderer.getTableCellRendererComponent(table, 
                    value, isSelected, hasFocus, row, column);
            if (c instanceof JLabel) {
                JLabel l = (JLabel) c;
                l.setHorizontalTextPosition(JLabel.LEFT);
                int modelColumn = table.convertColumnIndexToModel(column);
                l.setIcon(getHeaderRendererIcon(modelColumn, l.getFont().getSize()));
            }
            return c;
        }
    }

    /**
     * Directive help class.
     */
    private static class Directive {
    	/** Column. */
        private int column;
        /** Direction. */
        private int direction;

        /** 
         * Constructor.
         * @param column the column
         * @param direction the direction.
         */
        public Directive(int column, int direction) {
            this.column = column;
            this.direction = direction;
        }
    }
}