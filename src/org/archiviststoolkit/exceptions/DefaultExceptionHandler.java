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
 * Time: 10:53:45 PM
 */

package org.archiviststoolkit.exceptions;

import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.Main;
import org.archiviststoolkit.ApplicationFrame;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
// did you know that you could import inner classes?
import java.lang.Thread.*;

public class DefaultExceptionHandler implements UncaughtExceptionHandler {

//	private static Logger logger = Logger.getLogger(Main.class.getPackage().getName());

	public void uncaughtException(Thread t, Throwable e) {
		// Here you should have a more robust, permanent record of problems
		if (ApplicationFrame.getInstance() == null) {
			new ErrorDialog(e).showDialog();
		} else {
			new ErrorDialog(ApplicationFrame.getInstance(), e).showDialog();
		}
//		logger.error(e.getMessage(), e);
	}

	private Frame findActiveFrame() {
		Frame[] frames = JFrame.getFrames();
		for (int i = 0; i < frames.length; i++) {
			if (frames[i].isVisible()) {
				return frames[i];
			}
		}
		return null;
	}
}

