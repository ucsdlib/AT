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
 * Date: Oct 9, 2007
 * Time: 12:38:48 PM
 */

package org.archiviststoolkit.util;

import org.archiviststoolkit.model.ResourcesComponents;

import java.util.Comparator;

public class ResourceComponentComparator implements Comparator {

	private boolean debug = false;

	public int compare(Object o, Object o1) {
		if (o instanceof ResourcesComponents && o1 instanceof ResourcesComponents) {
//			if (debug) {
//				System.out.println("Resource component comparator From: " + o + " To: " + o1);
//			}
			int result = ((ResourcesComponents) o).getSequenceNumber().compareTo(((ResourcesComponents) o1).getSequenceNumber());
			if (result == 0 && o.equals(o1)) {
				//this is necesary so that equals and compareTo are consistent
				return 1;
			} else {
				return result;
			}
		} else {
			return 0;
		}
	}
	
}
