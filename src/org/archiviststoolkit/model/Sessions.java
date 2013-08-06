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
 * Created on July 19, 2005, 11:54 AM
 * @author leemandell
 *
 */

package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.mydomain.Auditable;
import org.archiviststoolkit.mydomain.AuditInfo;
import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;

import java.beans.PropertyChangeListener;
import java.util.Set;
import java.util.HashSet;
import java.util.Date;

public class Sessions extends DomainObject{

	// Names of the Bound Bean Properties *************************************

	private Long sessionId;
	private String userName = "";
	private Date logonTimeStamp;

	public Sessions(String userName, Date logonTimeStamp) {
		this.userName = userName;
		this.logonTimeStamp = logonTimeStamp;
	}

	private ExtendedPropertyChangeSupport changeSupport = new ExtendedPropertyChangeSupport(this);


	/**
	 * Creates a new instance of Subject
	 */
	public Sessions() {
	}

	/**
	 * @return Returns the identifier.
	 */
	public Long getIdentifier() {
		return getSessionId();
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(Long identifier) {
		this.setSessionId(identifier);
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public String getUserName() {
		if (this.userName != null) {
			return this.userName;
		} else {
			return "";
		}
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getLogonTimeStamp() {
		return logonTimeStamp;
	}

	public void setLogonTimeStamp(Date logonTimeStamp) {
		this.logonTimeStamp = logonTimeStamp;
	}
}
