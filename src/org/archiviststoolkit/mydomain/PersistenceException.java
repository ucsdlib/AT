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

package org.archiviststoolkit.mydomain;

//==============================================================================
// Import Declarations
//==============================================================================

/**
 * Thrown when we see a serious persistence issue.
 */

public class PersistenceException extends Exception {

	/**
	 * Constructor for wrapping internal exceptions.
	 *
	 * @param message string for human consumption
	 * @param cause   the original exception which caused this one
	 */

	public PersistenceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor for construction of a new persistence exception.
	 *
	 * @param message string for human consumption
	 */

	public PersistenceException(final String message) {
		super(message);
	}
}