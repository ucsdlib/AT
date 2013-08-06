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
 * Date: Nov 4, 2006
 * Time: 6:16:36 PM
 */

package org.archiviststoolkit.report;

public class ExtentStats {

	private String extentType;
	private Double resources = 0d;
	private Double accessions = 0d;

	public ExtentStats(String extentType) {
		this.extentType = extentType;
	}

	public String getExtentType() {
		return extentType;
	}

	public void setExtentType(String extentType) {
		this.extentType = extentType;
	}

	public Double getResources() {
		return resources;
	}

	public void setResources(Double resources) {
		this.resources = resources;
	}

	public Double getAccessions() {
		return accessions;
	}

	public void setAccessions(Double accessions) {
		this.accessions = accessions;
	}

	public void addResourceExtent(Double extent) {
		resources += extent;
	}
	public void addAccessionExtent(Double extent) {
		accessions += extent;
	}
}