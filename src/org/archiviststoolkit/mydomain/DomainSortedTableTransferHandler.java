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
 * Date: Jun 21, 2006
 * Time: 3:57:40 PM
 */

package org.archiviststoolkit.mydomain;

import org.archiviststoolkit.swing.ArrayTransfer;

import javax.swing.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.util.ArrayList;

import ca.odell.glazedlists.SortedList;

public class DomainSortedTableTransferHandler extends TransferHandler {

	private int[] rows = null;
	private int addIndex = -1; //Location where items were added
	private int addCount = 0;  //Number of items added.
	private DomainSortedTable sortedTable;

	/**
	 * Convenience constructor for subclasses.
	 */
	public DomainSortedTableTransferHandler(DomainSortedTable sortedTable) {
		this.sortedTable = sortedTable;
	}

	public boolean importData(JComponent c, Transferable t) {
		if (canImport(c, t.getTransferDataFlavors())) {
			try {
				Object obj = t.getTransferData(ArrayTransfer.FLAVOUR);
				if (!(obj instanceof ArrayTransfer))
					return false;
				ArrayTransfer at = (ArrayTransfer) obj;
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
		for (int k = 0; k < transferFlavors.length; k++)
			if (transferFlavors[k].equals(ArrayTransfer.FLAVOUR))
				return true;
		return false;
	}

	protected Transferable createTransferable(JComponent c) {
		if (!(c instanceof JTable))
			return null;

		JTable table = (JTable) c;
		rows = table.getSelectedRows();
		ArrayList<DomainObject> values = new ArrayList<DomainObject>();

		for (int row : rows) {
			values.add(sortedTable.getSortedList().get(row));
		}

		return new ArrayTransfer(c, values.toArray());
	}

	protected void importArray(JComponent c, ArrayTransfer at) {
		JTable target = (JTable) c;
		int index = target.getSelectedRow();

		//Prevent the user from dropping data back on itself.
		//For example, if the user is moving rows #4,#5,#6 and #7 and
		//attempts to insert the rows after row #5, this would
		//be problematic when removing the original rows.
		//So this is not allowed.
		if (rows != null && index >= rows[0] &&
				index <= rows[rows.length - 1]) {
			rows = null;
			JOptionPane.showMessageDialog(null, "Dragging an item onto itself is not allowed.");
			return;
		}

		//open up a hole for the elements to be inserted
		for (int i = sortedTable.getSortedList().size() - 1; i > index; i--) {
			SequencedObject sequencedObject = (SequencedObject) sortedTable.getSortedList().get(i);
			sequencedObject.incrementSequenceNumber(at.numberOfObjectsTransferred());
		}

		//set the sequence number for each of the transferred objects
		Object[] transferedObjects = at.getData();
		int sequenceNumber = index + 1;
		for (Object transferedObject : transferedObjects) {
			sortedTable.getSortedList().remove(transferedObject);
			((SequencedObject) transferedObject).setSequenceNumber(sequenceNumber++);
			sortedTable.getSortedList().add((DomainObject) transferedObject);
		}

		//resequence the whole list
		sequenceNumber = 0;
		for (DomainObject domainObject : sortedTable.getSortedList()) {
			((SequencedObject) domainObject).setSequenceNumber(sequenceNumber++);
		}
	}
}
