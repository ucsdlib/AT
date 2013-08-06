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

// SimpleFileFilter.java
// A straightforward extension-based example of a file filter. This should be
// replaced by a "first class" Swing class in a later release of Swing.
//

import javax.swing.filechooser.*;
import java.io.File;

public class SimpleFileFilter extends FileFilter {

	private String extension;
	String description;

	public SimpleFileFilter(String ext) {
		this(ext, null);
	}

	public SimpleFileFilter(String ext, String description) {

		extension = ext;

		// Make sure we have a valid (if simplistic) description
		this.description = (description == null ? ext + " files" : description);
	}

	public boolean accept(File f) {
		// We always allow directories, regardless of their extension
		if (f.isDirectory()) {
			return true;
		}

		// Ok, it’s a regular file, so check the extension
		String name = f.getName().toLowerCase();

		if (name.endsWith(extension)) {
			return true;
		}

		return false;
	}

	public String getDescription() {
		return description;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
}
