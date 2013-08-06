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
 * Date: Jun 27, 2006
 * Time: 3:22:32 PM
 */

package org.archiviststoolkit.model;

import org.archiviststoolkit.structure.*;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.util.ATDateUtils;

import java.util.Set;
import java.util.HashSet;

public abstract class AccessionsResourcesCommon extends ArchDescription{
    public static final String PROPERTYNAME_BULK_DATE_BEGIN = "bulkDateBegin";
    public static final String PROPERTYNAME_BULK_DATE_END = "bulkDateEnd";
    public static final String PROPERTYNAME_ISOBULK_DATE_BEGIN = "isoBulkDateBegin";
    public static final String PROPERTYNAME_ISOBULK_DATE_END = "isoBulkDateEnd";
    public static final String PROPERTYNAME_ISOBULK_DATE_BEGIN_SECONDS = "isoBulkDateBeginSeconds";
    public static final String PROPERTYNAME_ISOBULK_DATE_END_SECONDS = "isoBulkDateEndSeconds";
    public static final String PROPERTYNAME_EXTENT_NUMBER = "extentNumber";
    public static final String PROPERTYNAME_EXTENT_TYPE = "extentType";
	public static final String PROPERTYNAME_CONTAINER_SUMMARY = "containerSummary";
	public static final String PROPERTYNAME_DEACCESSIONS = "deaccessions";

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Integer bulkDateBegin;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Integer bulkDateEnd;

    //@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private String isoBulkDateBegin;

	//@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private String isoBulkDateEnd;

    //@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Long isoBulkDateBeginSeconds;

	//@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Long isoBulkDateEndSeconds;

	@IncludeInApplicationConfiguration(3)
	@ExcludeFromDefaultValues
    private Double extentNumber;

	@IncludeInApplicationConfiguration(4)
	@StringLengthValidationRequried
	private String extentType = "";

	@DefaultIncludeInSearchEditor
    @IncludeInApplicationConfiguration
	@IncludeInRDE
    private String containerSummary = "";

    private Set<Deaccessions> deaccessions = new HashSet<Deaccessions>();

    private Set<ArchDescriptionPhysicalDescriptions> physicalDesctiptions = new HashSet<ArchDescriptionPhysicalDescriptions>();

	/**
     *
     */
    public Integer getBulkDateBegin() {
        return this.bulkDateBegin;
    }

    public void setBulkDateBegin(Integer bulkDateBegin) {
        this.bulkDateBegin = bulkDateBegin;
    }

    /**
     *
     */
    public Integer getBulkDateEnd() {
        return this.bulkDateEnd;
    }

    public void setBulkDateEnd(Integer bulkDateEnd) {
        Object oldValue = getBulkDateEnd();
        this.bulkDateEnd = bulkDateEnd;
        firePropertyChange(PROPERTYNAME_BULK_DATE_END, oldValue, bulkDateEnd);
    }

    /**
     * Method that return the iso bulk date begin reformated to match 
     * @return String containing the iso bluck date begin
     */
    public String getIsoBulkDateBegin() {
        if(isoBulkDateBegin == null) { // return just the year
            if(getBulkDateBegin() != null) {
                return getBulkDateBegin() + "";
            } else {
                return "";
            }
        } else { // return the bulk date begin
            return this.isoBulkDateBegin;
        }
    }

    /**
     * Method to set the ISO bulk date begins
     * @param isoBulkDateBegin The new iso bulk date begins
     */
    public void setIsoBulkDateBegin(String isoBulkDateBegin) {
        this.isoBulkDateBegin = isoBulkDateBegin;

        // for backward compatibility set the year now
        setBulkDateBegin(ATDateUtils.getYearFromISODate(isoBulkDateBegin));

        // set this iso date in seconds
        setIsoBulkDateBeginSeconds(ATDateUtils.getISODateInSeconds(isoBulkDateBegin));
    }

    /**
     * Method to return the iso bulk date begins in seconds
     * @return The bulk date begins in seconds
     */
    public Long getIsoBulkDateBeginSeconds() {
        return isoBulkDateBeginSeconds;
    }

    /**
     * Method to set the ISO bulk date begins in GMT seconds
     * @param seconds The iso bulk date begins in GMT seconds
     */
    public void setIsoBulkDateBeginSeconds(Long seconds) {
        this.isoBulkDateBeginSeconds = seconds;
    }

    /**
     * Method that return the iso bulk date begin
     * @return String containing the iso bluck date begin
     */
    public String getIsoBulkDateEnd() {
        if(isoBulkDateEnd == null) { // return just the year then
            if(getBulkDateEnd() != null) {
                return getBulkDateEnd() + "";
            } else {
                return "";
            }
        } else { // return the iso bulk date begin
            return this.isoBulkDateEnd;
        }
    }

    /**
     * Method to set the ISO bulk date ends
     * @param isoBulkDateEnd The new iso bulk date ends
     */
    public void setIsoBulkDateEnd(String isoBulkDateEnd) {
        this.isoBulkDateEnd = isoBulkDateEnd;

        // for backward compatibility set the year now
        setBulkDateEnd(ATDateUtils.getYearFromISODate(isoBulkDateEnd));

        // set this iso date in seconds used for searchng purposes
        setIsoBulkDateEndSeconds(ATDateUtils.getISODateInSeconds(isoBulkDateEnd));
    }

    /**
     * Method to return the iso bulk date ends in seconds
     * @return The bulk date end in seconds
     */
    public Long getIsoBulkDateEndSeconds() {
        return isoBulkDateEndSeconds;
    }

    /**
     * Method to set the ISO bulk date ends in GMT seconds
     * @param seconds The date in GMT seconds
     */
    public void setIsoBulkDateEndSeconds(Long seconds) {
        this.isoBulkDateEndSeconds = seconds;
    }

    /**
     *
     */
    public Double getExtentNumber() {
        return this.extentNumber;
    }

    public void setExtentNumber(Double extentNumber) {
        this.extentNumber = extentNumber;
    }

	public void removeRelatedObject(DomainObject domainObject) throws ObjectNotRemovedException {
		if (domainObject instanceof Deaccessions) {
			removeRepeatingData((Deaccessions) domainObject);
		} else if (domainObject instanceof ArchDescriptionPhysicalDescriptions) {
            removePhysicalDesctiptions((ArchDescriptionPhysicalDescriptions)domainObject);
        }else {
			super.removeRelatedObject(domainObject);
		}
	}

	public void addDeaccessions(Deaccessions deaccession) {
		if (deaccession == null)
			throw new IllegalArgumentException("Can't add a null deaccession.");
        this.getDeaccessions().add(deaccession);

	}

	protected void removeRepeatingData(Deaccessions deaccession) {
		if (deaccession == null)
			throw new IllegalArgumentException("Can't remove a deaccession.");

		getDeaccessions().remove(deaccession);
	}



	/**
     *
     */
    public String getContainerSummary() {
//		System.out.println("Get containerSummary: " + this.containerSummary);
		if (this.containerSummary != null) {
			return this.containerSummary;
		} else {
			return "";
		}
	}

    public void setContainerSummary(String containerSummary) {
//		System.out.println("Set containerSummary: " + containerSummary);
		this.containerSummary = containerSummary;
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

    public Set<Deaccessions> getDeaccessions() {
        return deaccessions;
    }

    public void setDeaccessions(Set<Deaccessions> deaccessions) {
        this.deaccessions = deaccessions;
    }

    public Set<ArchDescriptionPhysicalDescriptions> getPhysicalDesctiptions() {
        return physicalDesctiptions;
    }

    public void setPhysicalDesctiptions(Set<ArchDescriptionPhysicalDescriptions> physicalDesctiptions) {
        this.physicalDesctiptions = physicalDesctiptions;
    }

    public void addPhysicalDesctiptions(ArchDescriptionPhysicalDescriptions physicalDescription) {
        if (physicalDescription == null)
            throw new IllegalArgumentException("Can't add a null date.");
        this.getPhysicalDesctiptions().add(physicalDescription);
    }

    public void removePhysicalDesctiptions(ArchDescriptionPhysicalDescriptions physicalDescription) {
        if (physicalDescription == null)
            throw new IllegalArgumentException("Can't remove a date.");
        getPhysicalDesctiptions().remove(physicalDescription);
    }
}
