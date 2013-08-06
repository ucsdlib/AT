package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.StringLengthValidationRequried;

import java.io.Serializable;
import java.util.Date;

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
 * Date: Aug 3, 2006
 * Time: 10:29:30 AM
 */
public class Deaccessions extends DomainObject implements Serializable, Comparable{

	public static final String PROPERTYNAME_DESCRIPTION = "description";
	public static final String PROPERTYNAME_DATE = "deaccessionDate";
	public static final String PROPERTYNAME_REASON = "reason";
	public static final String PROPERTYNAME_EXTENT = "extent";
	public static final String PROPERTYNAME_EXTENT_TYPE = "extentType";
	public static final String PROPERTYNAME_DISPOSITION = "disposition";
	public static final String PROPERTYNAME_NOTIFICATION = "notification";


	public Deaccessions(ArchDescription archDescription) {
		linkArchDescription(archDescription);
	}

	public Deaccessions() {
	}

	private void linkArchDescription(ArchDescription archDescription) {
		if (archDescription instanceof Resources) {
			this.resource = (Resources) archDescription;
		} else if (archDescription instanceof Accessions) {
			this.accession = (Accessions)archDescription;
		}
	}
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Long deaccessionsId = null;

    @IncludeInApplicationConfiguration
    private String description = "";

    @IncludeInApplicationConfiguration(1)
	@ExcludeFromDefaultValues
    private Date deaccessionDate = null;

    @IncludeInApplicationConfiguration
    private String reason = "";

    @IncludeInApplicationConfiguration(2)
    @ExcludeFromDefaultValues
    private Double extent = 0d;

    @IncludeInApplicationConfiguration(3)
	@StringLengthValidationRequried
	private String extentType = "";

    @IncludeInApplicationConfiguration
	@StringLengthValidationRequried
    private String disposition = "";

    @IncludeInApplicationConfiguration
    private Boolean notification = false;

    private Resources resource;
    private Accessions accession;


    public Long getDeaccessionsId() {
        return deaccessionsId;
    }

    public void setDeaccessionsId(Long deaccessionsId) {
        this.deaccessionsId = deaccessionsId;
    }

    public String getDescription() {
		if (this.description != null) {
			return this.description;
		} else {
			return "";
		}
	}

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeaccessionDate() {
        return deaccessionDate;
    }

    public void setDeaccessionDate(Date date) {
        this.deaccessionDate = date;
    }

    public String getReason() {
		if (this.reason != null) {
			return this.reason;
		} else {
			return "";
		}
	}

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Double getExtent() {
        return extent;
    }

    public void setExtent(Double extent) {
        this.extent = extent;
    }

    public String getExtentType() {
		if (this.extentType != null) {
			return this.extentType;
		} else {
			return "";
		}
	}

    public void setExtentType(String extentType) {
        this.extentType = extentType;
    }

    public String getDisposition() {
		if (this.disposition != null) {
			return this.disposition;
		} else {
			return "";
		}
	}

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    /**
     * @return Returns the identifier.
     */
    public Long getIdentifier() {
        return this.getDeaccessionsId();
    }

    /**
     * @param identifier The identifier to set.
     */
    public void setIdentifier(Long identifier) {
        this.setDeaccessionsId(identifier);
    }

    public Resources getResource() {
        return resource;
    }

    public void setResource(Resources resource) {
        this.resource = resource;
    }

    public Accessions getAccession() {
        return accession;
    }

    public void setAccession(Accessions accession) {
        this.accession = accession;
    }

	public Boolean getNotification() {
		return notification;
	}

	public void setNotification(Boolean notification) {
		this.notification = notification;
	}
}
