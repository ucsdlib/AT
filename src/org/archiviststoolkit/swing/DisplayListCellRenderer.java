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
 * Date: Oct 19, 2006
 * Time: 3:38:14 PM
 */

package org.archiviststoolkit.swing;

import javax.swing.*;
import java.awt.*;

public class DisplayListCellRenderer extends JLabel implements ListCellRenderer {

	public static final Color HIGHLIGHT_COLOR = new Color(151,0,43);

	private Integer maxStringLength = 0;
	private Integer frontClip;
	private Integer endClip;

	public DisplayListCellRenderer() {
		setOpaque(true);
	}

	public DisplayListCellRenderer(Integer maxStringLength) {
		if (maxStringLength > 20) {
			this.maxStringLength = maxStringLength;
			frontClip = maxStringLength - 10;
			endClip = 10;
		}
		setOpaque(true);
	}

	public Component getListCellRendererComponent(
		JList list,
		Object value,
		int index,
		boolean isSelected,
		boolean cellHasFocus)
	{
		if (value != null) {
			String stringValue = value.toString();
			if (maxStringLength > 0) {
				int length = stringValue.length();
				if (length > maxStringLength) {
					setText(stringValue.substring(0, frontClip) + "..." + stringValue.substring(length-endClip, length));
				} else {
					setText(stringValue);
				}
			} else {
				setText(stringValue);
			}
			setBackground(isSelected ? HIGHLIGHT_COLOR : Color.white);
			setForeground(isSelected ? Color.white : Color.black);
		}
		return this;
	}
}

