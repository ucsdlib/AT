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
 * Time: 4:06:32 PM
 */

package org.archiviststoolkit.swing;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.datatransfer.Transferable;
import java.io.IOException;

public class RowListTransfer implements Transferable {
	public static DataFlavor FLAVOUR;
	static {
		try {
			FLAVOUR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected JComponent m_source;	// It is very important to know your source and block transfer to the same component
	protected int[] m_arr;

	public RowListTransfer(JComponent source, int[] arr) {
		m_source = source;
		m_arr = arr;
	}

	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (!isDataFlavorSupported(flavor))
			throw new UnsupportedFlavorException(flavor);
		return this;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return FLAVOUR.equals(flavor);
	}

	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { FLAVOUR };
	}

	public JComponent getSource() {
		return m_source;
	}

	public int[] getData() {
		return m_arr;
	}

}
