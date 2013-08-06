package org.archiviststoolkit.exceptions;

/**
 * Archivists' Toolkit(TM) Copyright © 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
 * All rights reserved.
 * <p/>
 * This software is free. You can redistribute it and / or modify it under the terms of the Educational Community License (ECL)
 * version 1.0 (http://www.opensource.org/licenses/ecl1.php)
 * <p/>
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the ECL license for more details about permissions and limitations.
 * <p/>
 * <p/>
 * Archivists' Toolkit(TM)
 * http://www.archiviststoolkit.org
 * info@archiviststoolkit.org
 *
 * @author Lee Mandell
 *         Date: Jul 22, 2008
 *         Time: 4:02:23 PM
 */

public class ReportExecutionException extends Exception {

	public ReportExecutionException() {
	}

	public ReportExecutionException(String message) {
		super(message);
	}

	public ReportExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReportExecutionException(Throwable cause) {
		super(cause);
	}
}
