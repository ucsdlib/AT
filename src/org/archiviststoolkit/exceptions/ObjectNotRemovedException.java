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
 * Date: Aug 19, 2006
 * Time: 1:08:38 PM
 */

package org.archiviststoolkit.exceptions;

public class ObjectNotRemovedException extends Exception {

	public ObjectNotRemovedException() {
	}

	public ObjectNotRemovedException(Class clazz) {
		super("The class " + clazz.getName() + " is not supported here.");
	}

	public ObjectNotRemovedException(Class clazz, Throwable cause) {
		super("The class " + clazz.getName() + " is not supported here.", cause);
	}

	public ObjectNotRemovedException(Throwable cause) {
		super(cause);
	}
}