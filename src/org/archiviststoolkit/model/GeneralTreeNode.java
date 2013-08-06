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
 */

package org.archiviststoolkit.model;

import java.io.Serializable;

public class GeneralTreeNode implements Serializable {

    //todo this is view and not model information

    private String nodeTitle = null;

	private Class linkedRecordClass = null;

	private Long linkedRecordId = null;

	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public GeneralTreeNode() {
	}

	// ********************** Accessor Methods ********************** //

	public Long getLinkedRecordId() {
		return linkedRecordId;
	}

	public void setLinkedRecordId(Long linkedRecordId) {
		this.linkedRecordId = linkedRecordId;
	}


	public Long getIdentifier() {
		return this.linkedRecordId;
	}

	public void setIdentifier(Long identifier) {
		this.setLinkedRecordId(identifier);
	}


	public void setNodeTitle(String nodeTitle) {
		this.nodeTitle = nodeTitle;
	}

	public String getNodeTitle() {
		if (nodeTitle == null) {
			return "";
		} else {
			return nodeTitle;
		}
	}

	public void setLinkedRecordClass(Class linkedRecordClass) {
		this.linkedRecordClass = linkedRecordClass;
	}

	public Class getLinkedRecordClass() {
		return linkedRecordClass;
	}

	public String toString() {
		return nodeTitle;
	}
}
