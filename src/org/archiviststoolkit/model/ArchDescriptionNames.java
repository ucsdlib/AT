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
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.StringLengthValidationRequried;

import java.io.Serializable;


public class ArchDescriptionNames  extends DomainObject implements Serializable, Comparable {

	public static final String PROPERTYNAME_SORT_NAME = "sortName";
	public static final String PROPERTYNAME_ROLE = "role";
	public static final String PROPERTYNAME_FORM = "form";
	public static final String PROPERTYNAME_NAME_LINK_FUNCTION = "nameLinkFunction";
	public static final String PROPERTYNAME_NAME = "name";
    public static final String PROPERTYNAME_FUNCTION_CREATOR = "creator";
    public static final String PROPERTYNAME_FUNCTION_SOURCE = "source";
    public static final String PROPERTYNAME_FUNCTION_SUBJECT = "subject";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long archDescriptionNamesId = null;

	@IncludeInApplicationConfiguration(3)
	@StringLengthValidationRequried
	private String role = "";

	@IncludeInApplicationConfiguration(2)
	@StringLengthValidationRequried
	private String nameLinkFunction = "";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String form = "";

	@IncludeInApplicationConfiguration(1)
	@ExcludeFromDefaultValues
	private Names name;

	private Resources resource;
	private ResourcesComponents resourceComponent;
	private Accessions accession;
	private DigitalObjects digitalObject;

	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public ArchDescriptionNames() {}

	/**
	 * Full constructor;
	 */
	public ArchDescriptionNames(Names name, ArchDescription archDescription) {
		this.setName(name);
		this.linkArchDescription(archDescription);
	}

	public ArchDescriptionNames(Names name,
								ArchDescription archDescription,
								String role,
								String function,
								String form) {
		this.setName(name);
		this.linkArchDescription(archDescription);
		this.role = role;
		this.nameLinkFunction = function;
		this.form = form;
	}

	private void linkArchDescription(ArchDescription archDescription) {
		if (archDescription instanceof Resources) {
			this.resource = (Resources)archDescription;
		} else if (archDescription instanceof ResourcesComponents) {
			this.resourceComponent = (ResourcesComponents)archDescription;
		} else if (archDescription instanceof DigitalObjects) {
			this.digitalObject = (DigitalObjects)archDescription;
		} else if (archDescription instanceof Accessions) {
			this.accession = (Accessions)archDescription;
		}
	}

	// ********************** Accessor Methods ********************** //

	public Long getArchDescriptionNamesId() {
		return archDescriptionNamesId;
	}

	public void setArchDescriptionNamesId(Long archDescriptionNamesId) {
		this.archDescriptionNamesId = archDescriptionNamesId;
	}

	public String getRole() {
		if (this.role != null) {
			return this.role;
		} else {
			return "";
		}
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Long getIdentifier() {
		return this.getArchDescriptionNamesId();
	}

	public void setIdentifier(Long identifier) {
		this.setArchDescriptionNamesId(identifier);
	}

	public String toString() {
		return getSortName()+role+nameLinkFunction+form;
	}

	public Resources getResource() {
		return resource;
	}

	public void setResource(Resources resource) {
		this.resource = resource;
	}

	public void setName(Names name) {
		this.name = name;
	}

	public Names getName() {
		return name;
	}

	public String getSortName() {
		return name.getSortName();
	}

	public String getNameLinkFunction() {
		if (this.nameLinkFunction != null) {
			return this.nameLinkFunction;
		} else {
			return "";
		}
	}

	public void setNameLinkFunction(String nameLinkFunction) {
		this.nameLinkFunction = nameLinkFunction;
	}

	public String getForm() {
		if (this.form != null) {
			return this.form;
		} else {
			return "";
		}
	}

	public void setForm(String form) {
		this.form = form;
	}

	public ResourcesComponents getResourceComponent() {
		return resourceComponent;
	}

	public void setResourceComponent(ResourcesComponents resourceComponent) {
		this.resourceComponent = resourceComponent;
	}

	public DigitalObjects getDigitalObject() {
		return digitalObject;
	}

	public void setDigitalObject(DigitalObjects digitalObject) {
		this.digitalObject = digitalObject;
	}

	public Accessions getAccession() {
		return accession;
	}

	public void setAccession(Accessions accession) {
		this.accession = accession;
	}

	public ArchDescription getArchDescription() {
		if (getAccession() != null) {
			return getAccession();
		} else if (getDigitalObject() != null) {
			return getDigitalObject();
		} else if (getResource() != null) {
			return getResource();
		} else if (getResourceComponent() != null) {
			return getResourceComponent();
		} else {
			return null;
		}
	}
}
