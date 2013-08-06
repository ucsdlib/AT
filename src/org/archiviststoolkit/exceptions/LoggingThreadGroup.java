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
 * Date: Jul 30, 2006
 * Time: 9:31:06 PM
 */

package org.archiviststoolkit.exceptions;

import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.util.StringHelper;

import java.util.logging.*;
import java.io.StringWriter;
import java.io.PrintWriter;

public class LoggingThreadGroup extends ThreadGroup {
//  private static Logger logger;
  public LoggingThreadGroup(String name) {
	super(name);
  }
  public void uncaughtException(Thread t, Throwable e) {
	  new ErrorDialog("Uncaught error","Thread:" + t.getName() +
			  StringHelper.getStackTrace(e)).showDialog();

  }
}
