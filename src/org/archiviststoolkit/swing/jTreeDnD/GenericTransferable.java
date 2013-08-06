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
 * Date: May 8, 2008
 * Time: 1:06:36 PM
 */

package org.archiviststoolkit.swing.jTreeDnD;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * GenericTransferable.java
 * <p/>
 * <p>This transferable takes an object as data that is to be transferred. It
 * uses DataFlavor.stringFlavor, which is supported by all objects. This transferable
 * can be used in cases where a special handling in terms of which data flavors are
 * acceptable or which data is transported do not matter.</p>
 *
 * @author Ulrich Hilger
 * @author Light Development
 * @author <a href="http://www.lightdev.com">http://www.lightdev.com</a>
 * @author <a href="mailto:info@lightdev.com">info@lightdev.com</a>
 * @author published under the terms and conditions of the
 *         GNU General Public License,
 *         for details see file gpl.txt in the distribution
 *         package of this software
 * @version 1, 30.07.2005
 */
public class GenericTransferable implements Transferable {

	/**
	 * construct a transferabe with a given object to transfer
	 *
	 * @param data the data object to transfer
	 */
	public GenericTransferable(Object data) {
		super();
		this.data = data;
	}

	/**
	 * get the data flavors supported by this object
	 *
	 * @return an array of supported data flavors
	 */
	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}

	/**
	 * determine whether or not a given data flavor is supported by this transferable
	 *
	 * @return true, if the given data flavor is supported
	 */
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return true;
	}

	/**
	 * get the data this transferable transports
	 *
	 * @return the data transported by this transferable
	 */
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		return data;
	}

	/**
	 * the data this transferable transports
	 */
	private Object data;

	/**
	 * storage for data flavors supported of this transferable
	 */
	private static final DataFlavor[] flavors = new DataFlavor[1];

	/** the actual flavors supported by this transferable */
	static {
		flavors[0] = DataFlavor.stringFlavor;
	}
}
