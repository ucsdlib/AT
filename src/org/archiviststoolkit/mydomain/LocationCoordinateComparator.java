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
 * Date: Jan 31, 2008
 * Time: 2:41:26 PM
 */

package org.archiviststoolkit.mydomain;

import ca.odell.glazedlists.impl.sort.TableColumnComparator;
import ca.odell.glazedlists.gui.TableFormat;
import org.archiviststoolkit.model.Locations;

public class LocationCoordinateComparator extends TableColumnComparator {

	public LocationCoordinateComparator(TableFormat tableFormat, int i) {
		super(tableFormat, i);
	}

	public int compare(Object o, Object o1) {
		Locations location1 = (Locations) o;
		Locations location2 = (Locations) o1;

		return location1.getCoordinateSortString().compareTo(location2.getCoordinateSortString());
	}
}
