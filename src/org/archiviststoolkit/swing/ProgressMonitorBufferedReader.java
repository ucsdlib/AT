/**
 * Archivists' Toolkit(TM) Copyright � 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
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
//Import Declarations
//==============================================================================    

import java.awt.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import javax.swing.ProgressMonitor;

/**
 * Allows us to monitor progress on readers.
 */

public class ProgressMonitorBufferedReader extends BufferedReader {
	/**
	 * size of the input stream.
	 */
	private int size;
	/**
	 * location in the file.
	 */
	private int location;
	/**
	 * the monitor.
	 */
	private ProgressMonitor monitor;

	/**
	 * Create the monitor stream.
	 *
	 * @param parent  The parent of the ProgressMonitor
	 * @param message The message for the Progress Monitor.
	 *                Typically something like "Reading <i>filename</i>..."
	 * @param reader  The input stream.
	 */

	public ProgressMonitorBufferedReader(Component parent,
										 Object message, Reader reader) {
		super(reader);
		location = 0;

		monitor = new ProgressMonitor(parent, message, null, 0, 1000);
	}

	/**
	 * Get the ProgressMonitor we are using.
	 *
	 * @return the current monitor
	 */
	public ProgressMonitor getProgressMonitor() {
		return monitor;
	}

	/**
	 * Read the next line of input.
	 *
	 * @return the next line in the stream.
	 * @throws IOException if something really goes wrong
	 */
	public String readLine() throws IOException {
		if (monitor.isCanceled()) {
			throw new IOException("Input cancelled"); //TBD i18n
		}
		String line = super.readLine();
		if (line == null) {
			location = size;	//reached the end
		} else {
			location += line.length();
		}

		monitor.setProgress(location);
		System.out.println("Min:" + monitor.getMinimum() + " Max:" + monitor.getMaximum() + " Location:" + location);
		return line;
	}

	/**
	 * Close our stream and progress monitor.
	 *
	 * @throws IOException if we cant close the underlying stream
	 */
	public void close() throws IOException {
		super.close();
		monitor.close();
	}
}