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
 * Date: Nov 8, 2005
 * Time: 2:08:33 PM
 */

package org.archiviststoolkit.mydomain;

import java.io.Serializable;
import java.util.Date;

public final class AuditInfo implements Serializable {
	private Date lastUpdated;
	private Date created;
	private String lastUpdatedBy;
	private String createdBy;

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getLastUpdatedBy() {
		if (this.lastUpdatedBy != null) {
			return this.lastUpdatedBy;
		} else {
			return "";
		}
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getCreatedBy() {
		if (this.createdBy != null) {
			return this.createdBy;
		} else {
			return "";
		}
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void removeAuditInfo() {
		this.setLastUpdated(null);
		this.setLastUpdatedBy(null);
		this.setCreated(null);
		this.setCreatedBy(null);
	}
}

