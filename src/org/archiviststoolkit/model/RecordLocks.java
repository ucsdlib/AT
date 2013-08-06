package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;

/**
 * Archivists' Toolkit(TM) Copyright © 2005-2008 Regents of the University of California, New York University, & Five Colleges, Inc.
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
 * Created by IntelliJ IDEA.
 * @author: Nathan Stevens
 * Create Date: Nov 25, 2008
 * Time: 12:44:55 PM
 */
@ExcludeFromDefaultValues
public class RecordLocks extends DomainObject {
    public static final Long RECORD_LOCK_EXPIRE_TIME = 300000L; // The time the lock expires in milliseconds (5 min)

    @ExcludeFromDefaultValues
	private Long recordLockId; // The primary key assigned to this object

    @ExcludeFromDefaultValues
    private String className; // Name of the class the record belongs to

    @ExcludeFromDefaultValues
    private Long recordId; // The record id that is being lock

    @ExcludeFromDefaultValues
    private String userName;  // Name of the user who lock the record

    @ExcludeFromDefaultValues
    private Long previousUpdateTime; // The previous time the record lock was updated

    @ExcludeFromDefaultValues
    private String hostIP; // The ip of this machine

    /**
     * Method to return the host ip of this machine
     * @return The Host IP
     */
    public String getHostIP() {
        return hostIP;
    }

    /**
     * Method to set the host IP address
     * @param hostIP
     */
    public void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }

    /**
     * The default constructor
     */
    public RecordLocks() {

    }

    public RecordLocks(String className, Long recordId, String userName, Long previousUpdateTime, String hostIP) {
        this.className = className;
        this.recordId = recordId;
        this.userName = userName;
        this.previousUpdateTime = previousUpdateTime;
        this.hostIP = hostIP;
    }

    /**
	 * @return Returns the identifier.
	 */
	public Long getIdentifier() {
		return getRecordLockId();
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(Long identifier) {
		this.setRecordLockId(identifier);
	}

    /**
     * Get the record lock id
     * @return Return the record lock id
     */
    public Long getRecordLockId() {
		return recordLockId;
	}

    /**
     * Sets the record lock id
     * @param recordLockId
     */
    public void setRecordLockId(Long recordLockId) {
		this.recordLockId = recordLockId;
	}

    /**
     * Return the class name for the record
     * @return The class name of the record that is locked
     */
    public String getClassName() {
        return className;
    }

    /**
     * Set the class name for the record that is locked
     * @param className
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Get the record id of the locked record
     * @return Returns the record id of the record that is locked
     */
    public Long getRecordId() {
        return recordId;
    }

    /**
     * Set the record id for the record being locked
     * @param recordId
     */
    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    /**
     * get the name of the user locking the record
     * @return The name of the user who locked the record
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set the name of the user who locking the record
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Get the previous time the record lock was updated in milliseconds
     * @return time last updated in milliseconds
     */
    public Long getPreviousUpdateTime() {
        return previousUpdateTime;
    }

    /**
     * set previous time the record lock was updated
     * @param previousUpdateTime
     */
    public void setPreviousUpdateTime(Long previousUpdateTime) {
        this.previousUpdateTime = previousUpdateTime;
    }
}