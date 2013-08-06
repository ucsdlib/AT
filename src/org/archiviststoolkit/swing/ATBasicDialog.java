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
 * Date: Nov 9, 2006
 * Time: 8:49:14 PM
 */

package org.archiviststoolkit.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ATBasicDialog extends JDialog {
	protected int status = 0;

	public ATBasicDialog() throws HeadlessException {
		super();
		init();
	}

	public ATBasicDialog(java.awt.Dialog dialog) throws HeadlessException {
		super(dialog);
		init();
	}

	public ATBasicDialog(Frame frame) throws HeadlessException {
		super(frame);
		init();
	}

	private void init() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				thisWindowClosing();
			}
		});
	}

	private void thisWindowClosing() {
		status = JOptionPane.CANCEL_OPTION;
	}

	public void performOkAction() {
		status = JOptionPane.OK_OPTION;
		this.setVisible(false);
	}

	public void performCancelAction() {
		status = JOptionPane.CANCEL_OPTION;
		this.setVisible(false);
	}

	public final int showDialog() {

		//send to logger
		this.pack();
		setLocationRelativeTo(null);
		this.setVisible(true);

		return (status);

	}
}
