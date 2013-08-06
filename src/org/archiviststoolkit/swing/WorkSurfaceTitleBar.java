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

//==============================================================================
//Import Declarations
//==============================================================================

import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.*;

/**
 * The TitleBar is attached to areas within the workbench to support navigation. 
 */

public class WorkSurfaceTitleBar extends JLabel {

	/** MAGIC NUMBER which represents the default font size. */

	private static final int FONT_SIZE = 12;

	/** MAGIC NUMBER which represents the default font family. */

	private static final String FONT_FAMILY = "SansSerif";


	/**
	 * Default constructor which sets everything up.
	 * @param icon the icon to use on the title bar
	 * @param text the text to place next to the icon
	 */

	public WorkSurfaceTitleBar (final Icon icon, final String text) {
		this.setIcon(icon);
		this.setText(text);
		this.setBackground(SystemColor.activeCaption);
		//this.setBackground(SystemColor.inactiveCaption);
		this.setOpaque(true);
		this.setForeground(SystemColor.activeCaptionText);
		//this.setForeground(SystemColor.inactiveCaptionText);
		this.setRequestFocusEnabled(true);
		this.setToolTipText("tooltip test");
		this.setFont(new Font(FONT_FAMILY, Font.PLAIN, FONT_SIZE));
		this.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		JToolBar toolBar = new JToolBar();

		toolBar.setFloatable(false);
//		ResourceBundle resourceBundle = ResourceBundle.getBundle("org.archiviststoolkit.resources.messages.messages");
		ConcreteAction importAction = new ConcreteAction(text);
		//importAction.addActionListener(this);
		toolBar.add(importAction);
		this.add(toolBar);

	}
}