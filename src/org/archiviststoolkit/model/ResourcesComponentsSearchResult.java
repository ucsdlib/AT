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

import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.structure.*;


@ExcludeFromDefaultValues
public class ResourcesComponentsSearchResult extends DomainObject {


	public static final String PROPERTYNAME_RESOURCE_IDENTIFIER = "resourceIdentifier";
	public static final String PROPERTYNAME_RESOURCE_TITLE = "resourceTitle";
	public static final String PROPERTYNAME_COMPONENT_TITLE = "componentTitle";
	public static final String PROPERTYNAME_COMPONENT_DATE_BEGIN = "componentDateBegin";
    public static final String PROPERTYNAME_COMPONENT_DATE_END = "componentDateEnd";
    public static final String PROPERTYNAME_COMPONENT_DATE_BULKBEGIN = "componentBulkDateBegin";
    public static final String PROPERTYNAME_COMPONENT_DATE_BULKEND = "componentBulkDateEnd";
    public static final String PROPERTYNAME_COMPONENT_DATE = "componentDateExpression";
    public static final String PROPERTYNAME_COMPONENT_LEVEL = "componentLevel";
    public static final String PROPERTYNAME_COMPONENT_UNIQUE_IDENTIFIER = "componentUniqueIdentifier";


	private Long id;

	private Resources parentResource;

	private ResourcesComponents compenent = null;

	@IncludeInApplicationConfiguration(4)
	@ExcludeFromDefaultValues
	private String context;


	/**
	 * Creates a new instance of Subject
	 */
	public ResourcesComponentsSearchResult(Resources resource, String context) {
		setParentResource(resource);
		setContext(context);
	}

	public ResourcesComponentsSearchResult(Resources resource, ResourcesComponents component, String context) {
		setParentResource(resource);
		setCompenent(component);
		setContext(context);
	}


	/**
	 * @return Returns the identifier.
	 */
	public Long getIdentifier() {
		return parentResource.getIdentifier();
	}


	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(Long identifier) {
		this.setId(identifier);
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Resources getParentResource() {
		return parentResource;
	}

	public void setParentResource(Resources parentResource) {
		this.parentResource = parentResource;
	}

	public ResourcesComponents getCompenent() {
		return compenent;
	}

	public void setCompenent(ResourcesComponents compenent) {
		this.compenent = compenent;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	@IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_RESOURCE_IDENTIFIER, value = 3)
	@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	public String getResourceIdentifier() {
		return parentResource.getResourceIdentifier();
	}
	@IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_RESOURCE_TITLE, value = 2)
	@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	public String getResourceTitle() {
		return parentResource.getTitle();
	}
	@IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_COMPONENT_TITLE, value = 1)
	@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	public String getComponentTitle() {
		if (compenent != null) {
			return compenent.getTitle();
		} else {
			return "";
		}
	}

    // Return the iso begin date of the component
    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_COMPONENT_DATE_BEGIN, value = 0)
	@ExcludeFromDefaultValues
	public String getComponentDateBegin() {
		if (compenent != null) {
			return compenent.getIsoDateBegin();
		} else {
			return "";
		}
	}

    // Return the iso end date of the component
    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_COMPONENT_DATE_END, value = 0)
	@ExcludeFromDefaultValues
	public String getComponentDateEnd() {
		if (compenent != null) {
			return compenent.getIsoDateEnd();
		} else {
			return "";
		}
	}

    // Return the iso bulk begin date of the component
    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_COMPONENT_DATE_BULKBEGIN, value = 0)
	@ExcludeFromDefaultValues
	public String getComponentBulkDateBegin() {
		if (compenent != null) {
			return compenent.getIsoBulkDateBegin();
		} else {
			return "";
		}
	}

    // Return the iso bulk end date of the component
    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_COMPONENT_DATE_BULKEND, value = 0)
	@ExcludeFromDefaultValues
	public String getComponentBulkDateEnd() {
		if (compenent != null) {
			return compenent.getIsoBulkDateEnd();
		} else {
			return "";
		}
	}

    // Return the date expression of the component
    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_COMPONENT_DATE, value = 0)
	@ExcludeFromDefaultValues
	public String getComponentDateExpression() {
		if (compenent != null) {
			return compenent.getDateExpression();
		} else {
			return "";
		}
	}

    // Return the level of the component
    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_COMPONENT_LEVEL, value = 0)
	@ExcludeFromDefaultValues
	public String getComponentLevel() {
		if (compenent != null) {
			return compenent.getLevel();
		} else {
			return "";
		}
	}

    // Return the level of the component unique id
    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_COMPONENT_UNIQUE_IDENTIFIER, fieldType = "String", value = 0)
    @ExcludeFromDefaultValues
	public String getComponentUniqueIdentifier() {
		if (compenent != null) {
			return compenent.getComponentUniqueIdentifier();
		} else {
			return "";
		}
	}
}