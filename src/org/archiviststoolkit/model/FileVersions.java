package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.SequencedObject;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.StringLengthValidationRequried;

import java.io.Serializable;

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
 * Date: Mar 23, 2006
 * Time: 12:53:16 PM
 */

public class FileVersions extends SequencedObject implements Serializable {

	public static final String PROPERTYNAME_FILE_VERSIONS_URI = "uri";
	public static final String PROPERTYNAME_FILE_VERSIONS_USE_STATEMENT = "useStatement";
	public static final String PROPERTYNAME_EAD_DAO_ACTUATE = "eadDaoActuate";
	public static final String PROPERTYNAME_EAD_DAO_SHOW = "eadDaoShow";

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Long fileVersionId;

	@IncludeInApplicationConfiguration(2)
	@ExcludeFromDefaultValues
    private String uri="";

	@IncludeInApplicationConfiguration(1)
    private String useStatement="";

	private DigitalObjects digitalObject;
	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String eadDaoActuate = "";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String eadDaoShow = "";

    public FileVersions() {
        super();
    }

	public FileVersions(DigitalObjects digitalObject) {
		super();
		this.digitalObject = digitalObject;
	}

	public Long getIdentifier() {
		 return getFileVersionId();
	 }

     public void setIdentifier(Long identifier) {
         this.setFileVersionId(identifier);
     }

    public Long getFileVersionId() {
        return fileVersionId;
    }

    public void setFileVersionId(Long fileVersionId) {
        this.fileVersionId = fileVersionId;
    }

    public String getUri() {
		if (this.uri != null) {
			return this.uri;
		} else {
			return "";
		}
	}

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUseStatement() {
		if (this.useStatement != null) {
			return this.useStatement;
		} else {
			return "";
		}
	}

    public void setUseStatement(String useStatement) {
        this.useStatement = useStatement;
    }

    public DigitalObjects getDigitalObject() {
        return digitalObject;
    }

    public void setDigitalObject(DigitalObjects digitalObject) {
        this.digitalObject = digitalObject;
    }

	public String getEadDaoActuate() {
		if (this.eadDaoActuate != null) {
			return this.eadDaoActuate;
		} else {
			return "";
		}
	}

	public void setEadDaoActuate(String eadDaoActuate) {
		this.eadDaoActuate = eadDaoActuate;
	}

	public String getEadDaoShow() {
		if (this.eadDaoShow != null) {
			return this.eadDaoShow;
		} else {
			return "";
		}
	}

	public void setEadDaoShow(String eadDaoShow) {
		this.eadDaoShow = eadDaoShow;
	}
}
