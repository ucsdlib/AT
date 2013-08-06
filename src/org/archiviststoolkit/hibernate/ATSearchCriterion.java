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
 * Date: Aug 11, 2008
 * Time: 5:32:05 PM
 */

package org.archiviststoolkit.hibernate;

import org.hibernate.criterion.Criterion;


/**
 * a wrapper class to add human
 */
public class ATSearchCriterion {

	private Criterion citerion;
	private String searchString = "";
	private String context = "";

	public ATSearchCriterion(Criterion citerion, String searchString, String context) {
		this.citerion = citerion;
		this.searchString = searchString;
		this.context = context;
	}

	public Criterion getCiterion() {
		return citerion;
	}

	public void setCiterion(Criterion citerion) {
		this.citerion = citerion;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}
}
