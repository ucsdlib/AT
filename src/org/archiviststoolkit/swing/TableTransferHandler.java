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

/*
 * TableTransferHandler.java is used by the 1.4
 * ExtendedDnDDemo.java example.
 */

import org.archiviststoolkit.mydomain.DomainTableModel;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.model.ArchDescriptionRepeatingData;

import javax.swing.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.ArrayList;

public class TableTransferHandler extends TransferHandler {
	private int[] rows = null;
	private int addIndex = -1; //Location where items were added
	private int addCount = 0;  //Number of items added.

	public boolean importData(JComponent c, Transferable t) {
		if (canImport(c, t.getTransferDataFlavors())) {
			try {
				Object obj = t.getTransferData(ArrayTransfer.FLAVOUR);
				if (!(obj instanceof ArrayTransfer))
					return false;
				ArrayTransfer at = (ArrayTransfer)obj;
				importArray(c, at);
				return true;
			} catch (UnsupportedFlavorException ufe) {
				System.out.println(ufe.getStackTrace());
			} catch (IOException ioe) {
				System.out.println(ioe.getStackTrace());
			}
		}

		return false;
	}

	public int getSourceActions(JComponent c) {
		if (!(c instanceof JTable))
			return NONE;
		return COPY_OR_MOVE;
	}


	public boolean canImport(JComponent c,
							 DataFlavor[] transferFlavors) {
		if (!(c instanceof JTable))
			return false;
		for (int k=0; k<transferFlavors.length; k++)
			if (transferFlavors[k].equals(ArrayTransfer.FLAVOUR))
				return true;
		return false;
	}

	protected Transferable createTransferable(JComponent c) {
		if (!(c instanceof JTable))
			return null;

		JTable table = (JTable) c;
		rows = table.getSelectedRows();
//		SortableTableModel sortableModel = (SortableTableModel) table.getModel();
		DomainTableModel model = (DomainTableModel)table.getModel();
		ArrayList<DomainObject> values = new ArrayList<DomainObject>();

		for(int row : rows) {
			values.add(model.get(row));
		}

//		Object[] arr = ((JTable)c).getSelectedValues();
		return new ArrayTransfer(c, values.toArray());
	}


//	protected String exportString(JComponent c) {
//		JTable table = (JTable) c;
//		rows = table.getSelectedRows();
//		int colCount = table.getColumnCount();
//
//		StringBuffer buff = new StringBuffer();
//
//		for (int i = 0; i < rows.length; i++) {
//			for (int j = 0; j < colCount; j++) {
//				Object val = table.getValueAt(rows[i], j);
//				buff.append(val == null ? "" : val.toString());
//				if (j != colCount - 1) {
//					buff.append(",");
//				}
//			}
//			if (i != rows.length - 1) {
//				buff.append("\n");
//			}
//		}
//
//		return buff.toString();
//	}

	protected void importArray(JComponent c, ArrayTransfer at) {
		JTable target = (JTable) c;
//		DomainTableModel model = (DomainTableModel) target.getModel();
//		SortableTableModel sortableModel = (SortableTableModel) target.getModel();
		DomainTableModel model = (DomainTableModel)target.getModel();
		int index = target.getSelectedRow();

		//Prevent the user from dropping data back on itself.
		//For example, if the user is moving rows #4,#5,#6 and #7 and
		//attempts to insert the rows after row #5, this would
		//be problematic when removing the original rows.
		//So this is not allowed.
		if (rows != null && index >= rows[0] - 1 &&
				index <= rows[rows.length - 1]) {
			rows = null;
			return;
		}

		int max = model.getRowCount();
		if (index < 0) {
			index = max;
		} else {
			index++;
			if (index > max) {
				index = max;
			}
		}
		addIndex = index;
		Object[] values = at.getData();
		addCount = values.length;
//		int colCount = target.getColumnCount();
		for (Object value: values) {
			model.addDomainObject(index++, (DomainObject)value);
		}

//		for (int i = 0; i < values.length && i < colCount; i++) {
//			model.addDomainObject(index++, values[i].split(","));
//		}
	}

	protected void exportDone(JComponent c, Transferable data, int action) {
		cleanup(c, action == MOVE);
	}

	protected void cleanup(JComponent c, boolean remove) {
		JTable source = (JTable) c;
		if (remove && rows != null) {
//			SortableTableModel sortableModel = (SortableTableModel) source.getModel();
			DomainTableModel model = (DomainTableModel)source.getModel();
//			DefaultTableModel model =
//					(DefaultTableModel) source.getModel();

			//If we are moving items around in the same table, we
			//need to adjust the rows accordingly, since those
			//after the insertion point have moved.
			if (addCount > 0) {
				for (int i = 0; i < rows.length; i++) {
					if (rows[i] > addIndex) {
						rows[i] += addCount;
					}
				}
			}
			for (int i = rows.length - 1; i >= 0; i--) {
				model.removeDomainObject(rows[i]);
			}
			int sequenceNumber = 1;
			for (DomainObject object : model.getDomainCollection()) {
				((ArchDescriptionRepeatingData)object).setSequenceNumber(sequenceNumber++);
			}
			model.fireTableDataChanged();
		}
		rows = null;
		addCount = 0;
		addIndex = -1;
	}
}
