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
 * Date: Feb 12, 2008
 * Time: 10:52:46 AM
 */

package org.archiviststoolkit.model;

public class LocationsCoordinatesDisplay implements Comparable{

	private String displayString;
	private String sortString;

	public LocationsCoordinatesDisplay(String displayString, String sortString) {
		this.displayString = displayString;
		this.sortString = sortString;
	}

	public String toString() {
		return getDisplayString();
	}

	public String getDisplayString() {
		return displayString;
	}

	public void setDisplayString(String displayString) {
		this.displayString = displayString;
	}

	public String getSortString() {
		return sortString;
	}

	public void setSortString(String sortString) {
		this.sortString = sortString;
	}

	public int compareTo(Object o) {
		if (o instanceof LocationsCoordinatesDisplay) {
			LocationsCoordinatesDisplay compareInstance = (LocationsCoordinatesDisplay)o;
			return this.getSortString().compareTo(compareInstance.getSortString());
		} else {
			throw new ClassCastException();
		}
	}
}

