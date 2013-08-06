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

import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.StringLengthValidationRequried;
import org.archiviststoolkit.structure.IncludeInRDE;


public class ResourcesComponents extends ResourcesCommon implements Comparable {

	public static final String PROPERTYNAME_UNIQUE_IDENTIFIER = "componentUniqueIdentifier";
	public static final String PROPERTYNAME_PERSISTENT_ID = "persistentId";
	public static final String PROPERTYNAME_RESOURCES_COMPONENT_ID = "resourceComponentId";
	public static final String PROPERTYNAME_SEQUENCE_NUMBER = "sequenceNumber";

	private boolean debug = false;
	// Fields

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long resourceComponentId;

	private Integer sequenceNumber = 0;
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	@IncludeInRDE
	private String componentUniqueIdentifier = "";

	private String persistentId = "";

	private ResourcesComponents resourceComponentParent = null;

	private Resources resource;
	private boolean hasChild = false;
	private Boolean hasNotes = false;

	public ResourcesComponents() {
	}

	public ResourcesComponents(ResourcesComponents resourceComponentParent) {
		this.resourceComponentParent = resourceComponentParent;
	}

	public ResourcesComponents(Resources resource) {
		this.resource = resource;
	}

	public String getLabelForTree() {
		if (debug) {
			String returnString = sequenceNumber + "- " + super.getLabelForTree() + " (" + getIdentifier() + ")";
//			System.out.println("Label for tree: " + returnString);
			return returnString;
		} else {
			return super.getLabelForTree();
		}
	}

	public String toString() {
		return getLabelForTree();
	}

	/**
	 *
	 */
	public Integer getSequenceNumber() {
		return this.sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Long getIdentifier() {
		return getResourceComponentId();
	}

	public void setIdentifier(Long identifier) {
		this.setResourceComponentId(identifier);
	}

	public Long getResourceComponentId() {
		return resourceComponentId;
	}

	public void setResourceComponentId(Long resourceId) {
		this.resourceComponentId = resourceId;
	}

	public Resources getResource() {
		return resource;
	}

	public void setResource(Resources resource) {
		this.resource = resource;
	}

//	public Set<ResourcesComponents> getResourcesComponentChildren() {
//		return new TreeSet<ResourcesComponents>(resourcesComponents);
//	}

	//

  public ResourcesComponents getResourceComponentParent() {
		return resourceComponentParent;
	}

	public void setResourceComponentParent(ResourcesComponents resourceComponentParent) {
		this.resourceComponentParent = resourceComponentParent;
	}

	public boolean isHasChild() {
		return hasChild;
	}

	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}

	public String getComponentUniqueIdentifier() {
		if (this.componentUniqueIdentifier != null) {
			return this.componentUniqueIdentifier;
		} else {
			return "";
		}
	}

	public void setComponentUniqueIdentifier(String componentUniqueIdentifier) {
		this.componentUniqueIdentifier = componentUniqueIdentifier;
	}

	public String getPersistentId() {
		if (this.persistentId != null) {
			return this.persistentId;
		} else {
			return "";
		}
	}

	public void setPersistentId(String persistentId) {
		this.persistentId = persistentId;
	}

	public String getDescriptionForReference() {
		return this.getTitle();
	}

	public Boolean getHasNotes() {
		return hasNotes;
	}

	public void setHasNotes(Boolean hasNotes) {
		this.hasNotes = hasNotes;
	}

	public ResourcesCommon getParent() {
		if (resource != null) {
			return resource;
		} else {
			return resourceComponentParent;
		}
	}

	public void incrementSequenceNumber() {
		incrementSequenceNumber(1);
	}

	public void incrementSequenceNumber(int incrementAmount) {
		setSequenceNumber(getSequenceNumber() + incrementAmount);
	}
}