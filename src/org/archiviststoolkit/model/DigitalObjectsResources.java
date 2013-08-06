package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.IncludeMethodInApplicationConfiguration;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;

import java.io.Serializable;

/**
 * Archivists' Toolkit(TM) Copyright © 2005-2009 Regents of the University of California, New York University, & Five Colleges, Inc.
 * All rights reserved.
 * <p/>
 * This software is free. You can redistribute it and / or modify it under the terms of the Educational Community License (ECL)
 * version 1.0 (http://www.opensource.org/licenses/ecl1.php)
 * <p/>
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the ECL license for more details about permissions and limitations.
 * <p/>
 * <p/>
 * Archivists' Toolkit(TM)
 * http://www.archiviststoolkit.org
 * info@archiviststoolkit.org
 * <p/>
 * Created by IntelliJ IDEA.
 *
 * Linker class for DigitalObjects and Resources
 *
 * @author: Nathan Stevens
 * Date: June 11, 2009
 * Time: 9:46:05 AM
 */
@ExcludeFromDefaultValues
public class DigitalObjectsResources extends DomainObject implements Serializable, Comparable {
    public static final String PROPERTYNAME_DIGITAL_OBJECT_IDENTIFIER    = "digitalObjectIdentifier";
	public static final String PROPERTYNAME_RESOURCE_IDENTIFIER    = "resourceIdentifier";
	public static final String PROPERTYNAME_RESOURCE_TITLE    = "resourceTitle";
	public static final String PROPERTYNAME_RESOURCE    = "resource";
	public static final String PROPERTYNAME_DIGITAL_OBJECT    = "digitalObject";

	private Long digitalObjectsResourcesId = null;

	@IncludeInApplicationConfiguration
	private Resources resource;

	@IncludeInApplicationConfiguration
	private DigitalObjects digitalObject;

	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public DigitalObjectsResources() {}

	/**
	 * Full constructor;
	 */
	public DigitalObjectsResources(Resources resource, DigitalObjects digitalObject) {
		this.setResource(resource);
		this.setDigitalObject(digitalObject);
	}

	// ********************** Accessor Methods ********************** //


	public Long getIdentifier() {
		return this.getDigitalObjectsResourcesId();
	}

	public void setIdentifier(Long identifier) {
		this.setDigitalObjectsResourcesId(identifier);
	}

	public DigitalObjects getDigitalObject() {
		return digitalObject;
	}

	public void setDigitalObject(DigitalObjects digitalObject) {
		this.digitalObject = digitalObject;
	}

	public Long getDigitalObjectsResourcesId() {
		return digitalObjectsResourcesId;
	}

	public void setDigitalObjectsResourcesId(Long DigitalObjectsResourcesId) {
		this.digitalObjectsResourcesId = DigitalObjectsResourcesId;
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

    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_DIGITAL_OBJECT_IDENTIFIER)
	public String getDigitalObjectIdentifier() {
		if (this.getDigitalObject() != null) {
			return this.getDigitalObject().getMetsIdentifier();
		} else {
			return "";
		}
	}
}