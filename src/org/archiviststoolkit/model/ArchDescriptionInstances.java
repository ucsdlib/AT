/**
 * Archivists' Toolkit(TM) Copyright � 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
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
import org.archiviststoolkit.structure.StringLengthValidationRequried;

import java.io.Serializable;


@ExcludeFromDefaultValues
public abstract class ArchDescriptionInstances extends DomainObject implements Serializable, Comparable {

    public static final String DIGITAL_OBJECT_INSTANCE = "Digital Object";
    public static final String DIGITAL_OBJECT_INSTANCE_LINK = "Digital object link";

	public static final String PROPERTYNAME_RESOURCE    = "resource";
	public static final String PROPERTYNAME_RESOURCE_COMPONENT    = "resourceComponent";
	public static final String PROPERTYNAME_INSTANCE_TYPE    = "instanceType";
	public static final String PROPERTYNAME_INSTANCE_LABEL    = "instanceLabel";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Long archDescriptionInstancesId = null;

	private ResourcesComponents resourceComponent;

	private Resources resource;

	@IncludeInApplicationConfiguration(1)
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(30)
	private String instanceType;


    /**
	 * No-arg constructor for JavaBean tools.
	 */
	public ArchDescriptionInstances() {
	}

	/**
	 * Resource component constructor;
	 */
	public ArchDescriptionInstances(ArchDescription archDescription) {
		if (archDescription instanceof Resources) {
			this.resource = (Resources)archDescription;
		} else if (archDescription instanceof ResourcesComponents) {
			this.resourceComponent = (ResourcesComponents)archDescription;
		}
	}

	// ********************** Accessor Methods ********************** //

	public Long getArchDescriptionInstancesId() {
		return archDescriptionInstancesId;
	}

	public void setArchDescriptionInstancesId(Long archDescriptionInstancesId) {
		this.archDescriptionInstancesId = archDescriptionInstancesId;
	}

	@IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_INSTANCE_LABEL, value=2)
  @ExcludeFromDefaultValues
    public abstract String getInstanceLabel();


    public Long getIdentifier() {
		return this.getArchDescriptionInstancesId();
	}

	public void setIdentifier(Long identifier) {
		this.setArchDescriptionInstancesId(identifier);
	}

	/**
	 * Compares this object to another.
	 *
	 * @param object the object to compare this to.
	 * @return a integer result of the comparison.
	 */
	public int compareTo(Object object) {
		if (object instanceof ArchDescriptionInstances) {
			ArchDescriptionInstances o2 = (ArchDescriptionInstances) object;
			return this.instanceType.compareTo(o2.instanceType);
		} else {
			return super.compareTo(object);
		}
	}
	public ResourcesComponents getResourceComponent() {
		return resourceComponent;
	}

	public void setResourcesComponents(ResourcesComponents resourceComponent) {
		this.resourceComponent = resourceComponent;
	}


	public Resources getResource() {
		return resource;
	}

	public void setResource(Resources resource) {
		this.resource = resource;
	}

    public String getInstanceType() {
		if (this.instanceType != null) {
			return this.instanceType;
		} else {
			return "";
		}
	}

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }
}
