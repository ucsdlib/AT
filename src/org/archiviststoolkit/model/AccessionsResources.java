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

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.IncludeMethodInApplicationConfiguration;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;

import java.io.Serializable;


@ExcludeFromDefaultValues
public class AccessionsResources  extends DomainObject implements Serializable, Comparable {

	public static final String PROPERTYNAME_ACCESSION_NUMBER    = "accessionNumber";
	public static final String PROPERTYNAME_ACCESSION_TITLE    = "accessionTitle";
	public static final String PROPERTYNAME_RESOURCE_IDENTIFIER    = "resourceIdentifier";
	public static final String PROPERTYNAME_RESOURCE_TITLE    = "resourceTitle";
	public static final String PROPERTYNAME_RESOURCE    = "resource";
	public static final String PROPERTYNAME_ACCESSION    = "accession";

	private Long accessionsResourcesId = null;

	@IncludeInApplicationConfiguration(1)
	private Resources resource;

	@IncludeInApplicationConfiguration
	private Accessions accession;

	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public AccessionsResources() {}

	/**
	 * Full constructor;
	 */
	public AccessionsResources(Resources resource, Accessions accession) {
		this.setResource(resource);
		this.setAccession(accession);
	}

	// ********************** Accessor Methods ********************** //


	public Long getIdentifier() {
		return this.getAccessionsResourcesId();
	}

	public void setIdentifier(Long identifier) {
		this.setAccessionsResourcesId(identifier);
	}

	public Accessions getAccession() {
		return accession;
	}

	public void setAccession(Accessions accession) {
		this.accession = accession;
	}

	public Long getAccessionsResourcesId() {
		return accessionsResourcesId;
	}

	public void setAccessionsResourcesId(Long accessionsResourcesId) {
		this.accessionsResourcesId = accessionsResourcesId;
	}

	public Resources getResource() {
		return resource;
	}

	public void setResource(Resources resource) {
		this.resource = resource;
	}

    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_RESOURCE_IDENTIFIER)
	public String getResourceIdentifier() {
		if (this.getResource() != null) {
			return this.getResource().getResourceIdentifier();
		} else {
			return "";
		}
	}

    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_RESOURCE_TITLE)
	public String getResourceTitle() {
		if (this.getResource() != null) {
			return this.getResource().getTitle();
		} else {
			return "";
		}
	}

    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_ACCESSION_NUMBER)
	public String getAccessionNumber() {
		if (this.getAccession() != null) {
			return this.getAccession().getAccessionNumber();
		} else {
			return "";
		}
	}

    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_ACCESSION_TITLE)
	public String getAccessionTitle() {
		if (this.getAccession() != null) {
			return this.getAccession().getTitle();
		} else {
			return "";
		}
	}


}
