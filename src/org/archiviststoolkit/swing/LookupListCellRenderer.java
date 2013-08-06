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
 * Date: Oct 6, 2006
 * Time: 4:36:22 PM
 */

package org.archiviststoolkit.swing;

import org.archiviststoolkit.model.LookupListItems;
import org.archiviststoolkit.util.LookupListUtils;

import javax.swing.*;
import java.awt.*;

public class LookupListCellRenderer extends DefaultListCellRenderer {

	public static final Color AT_INITIAL_VALUE_COLOR = new Color(45,78,114);
	public static final Color NON_EDITABLE_COLOR = new Color(130,0,0);


	public LookupListCellRenderer(int listType) {
		super();
		this.listType = listType;
	}

	public LookupListCellRenderer(int listType, boolean includeRecordCount) {
		super();
		this.listType = listType;
		this.includeRecordCount = includeRecordCount;
	}

	private int listType;
	private boolean includeRecordCount = false;

	public Component getListCellRendererComponent(JList list,
														   Object value,
														   int index,
														   boolean isSelected,
														   boolean cellHasFocus) {

	  super.getListCellRendererComponent(list,
										 value,
										 index,
										 isSelected,
										 cellHasFocus);
	  if (value instanceof LookupListItems) {
		  LookupListItems item = (LookupListItems)value;
		  if (listType == LookupListUtils.EDITABLE) {
			  if(item.getAtInitialValue()) {
				  setForeground(AT_INITIAL_VALUE_COLOR);
			  } else {
				  setForeground(Color.black);
			  }
		  } else if (listType == LookupListUtils.MIXED) {
			  if(!item.getEditable()) {
				  setForeground(NON_EDITABLE_COLOR);
			  } else if(item.getAtInitialValue()) {
				  setForeground(AT_INITIAL_VALUE_COLOR);
			  } else {
				  setForeground(Color.black);
			  }
		  } else { //an uneditable list
			  if(item.getEditable()) {
				  setForeground(Color.black);
			  } else {
				  setForeground(NON_EDITABLE_COLOR);
			  }
		  }
		  if (includeRecordCount) {
			  this.setText(item.toString() + " - " + item.getRecordCount()+ " record(s)");
		  } else {
			  this.setText(item.toString());
		  }
	  }
	  return this;
	}

	public void setIncludeRecordCount(boolean includeRecordCount) {
		this.includeRecordCount = includeRecordCount;
	}
}
