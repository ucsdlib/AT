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
 *
 * @author Lee Mandell
 * Date: Jun 29, 2009
 * Time: 12:35:34 PM
 */

package org.archiviststoolkit.model;

import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.IncludeMethodInApplicationConfiguration;
import org.archiviststoolkit.util.ATDateUtils;
import org.archiviststoolkit.mydomain.DomainObject;

import java.text.NumberFormat;
import java.text.ParseException;

import com.jgoodies.validation.formatter.EmptyNumberFormatter;

public class ArchDescriptionPhysicalDescriptions extends DomainObject {


	public static final String PROPERTYNAME_EXTENT_TYPE = "extentType";
	public static final String PROPERTYNAME_EXTENT_NUMBER = "extentNumber";
	public static final String PROPERTYNAME_CONTAINER_SUMMARY = "containerSummary";
	public static final String PROPERTYNAME_PHYSICAL_DETAILS = "physicalDetail";
	public static final String PROPERTYNAME_DIMENSIONS = "dimensions";
	public static final String PROPERTYNAME_EXTENT_DISPLAY = "extentDisplay";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long archDescriptionPhysicalDescriptionsId = null;

	@IncludeInApplicationConfiguration
	private String extentType;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Double extentNumber;

	@IncludeInApplicationConfiguration(2)
	private String containerSummary;

	@IncludeInApplicationConfiguration
	private String physicalDetail;

	@IncludeInApplicationConfiguration
	private String dimensions;

	private ResourcesComponents resourceComponent;

	private Resources resource;

	private Accessions accession;


	/**
	 * No-arg constructor for JavaBean tools.
	 */
	ArchDescriptionPhysicalDescriptions() {
	}


	/**
	 * Full constructor;
	 */

	public ArchDescriptionPhysicalDescriptions(ArchDescription archDescription) {
		linkArchDescription(archDescription);
	}

	/**
	 *
	 * @param archDescription ArchDescription record to attach
	 */
	private void linkArchDescription(ArchDescription archDescription) {
		if (archDescription instanceof Resources) {
			this.resource = (Resources) archDescription;
		} else if (archDescription instanceof ResourcesComponents) {
			this.resourceComponent = (ResourcesComponents) archDescription;
		} else if (archDescription instanceof Accessions) {
			this.accession = (Accessions)archDescription;
		}
	}


	/**
	 *
	 * @return the unique ID
	 */
	public Long getArchDescriptionPhysicalDescriptionsId() {
		return archDescriptionPhysicalDescriptionsId;
	}

	public void setArchDescriptionPhysicalDescriptionsId(Long archDescriptionPhysicalDescriptionsId) {
		this.archDescriptionPhysicalDescriptionsId = archDescriptionPhysicalDescriptionsId;
	}

	/**
	 * @return Returns the identifier.
	 */
	public Long getIdentifier() {
		return this.getArchDescriptionPhysicalDescriptionsId();
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(Long identifier) {
		setArchDescriptionPhysicalDescriptionsId(identifier);
	}

	/**
	 *
	 * @return the resource component associated with this record
	 */

	public ResourcesComponents getResourceComponent() {
		return resourceComponent;
	}

	@IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_EXTENT_DISPLAY, value=1)
  	@ExcludeFromDefaultValues
	public String getExtentDisplay() throws ParseException {

		if (getExtentType() == null || getExtentNumber() == null) {
			return "no extent information";
		} else {
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setGroupingUsed(false);
			EmptyNumberFormatter format = new EmptyNumberFormatter(numberFormat);


			return format.valueToString(getExtentNumber()) + " " + getExtentType();
		}

	}


	/**
	 *
	 * @param resourceComponent The resource component to associate
	 */
	public void setResourceComponent(ResourcesComponents resourceComponent) {
		this.resourceComponent = resourceComponent;
	}

	/**
	 *
	 * @return the resource associated with this record
	 */
	public Resources getResource() {
		return resource;
	}

	/**
	 *
	 * @param resource The resource to attach to this record
	 */
	public void setResource(Resources resource) {
		this.resource = resource;
	}

	/**
	 *
	 * @return The accession associated with this record
	 */
	public Accessions getAccession() {
		return accession;
	}

	/**
	 *
	 * @param accession the accession to associate with this record
	 */
	public void setAccession(Accessions accession) {
		this.accession = accession;
	}

	/**
	 *
	 * @return the extent number
	 */
	public Double getExtentNumber() {
		return extentNumber;
	}

	/**
	 *
	 * @param extentNumber the extent number to set
	 */
	public void setExtentNumber(Double extentNumber) {
		this.extentNumber = extentNumber;
	}

	/**
	 *
	 * @return the container summary
	 */
	public String getContainerSummary() {
		return containerSummary;
	}

	/**
	 *
	 * @param containerSummary the container summary to set
	 */
	public void setContainerSummary(String containerSummary) {
		this.containerSummary = containerSummary;
	}

	/**
	 *
	 * @return the physical description
	 */
	public String getPhysicalDetail() {
		return physicalDetail;
	}

	/**
	 *
	 * @param physicalDetail the physical desciption to set
	 */
	public void setPhysicalDetail(String physicalDetail) {
		this.physicalDetail = physicalDetail;
	}

	/**
	 *
	 * @return the dimenstions
	 */
	public String getDimensions() {
		return dimensions;
	}

	/**
	 *
	 * @param dimensions the dimensions to set
	 */
	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	/**
	 *
	 * @return the extent number
	 */
	public String getExtentType() {
		if (this.extentType != null) {
			return extentType;
		} else {
			return "";
		}
	}

	/**
	 *
	 * @param extentType  the extent type to set
	 */
	public void setExtentType(String extentType) {
		this.extentType = extentType;
	}
}