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
 * Date: Jun 29, 2009
 * Time: 12:35:34 PM
 */

package org.archiviststoolkit.model;

import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.IncludeMethodInApplicationConfiguration;
import org.archiviststoolkit.util.ATDateUtils;
import org.archiviststoolkit.mydomain.DomainObject;

public class ArchDescriptionDates extends DomainObject {


	public static final String PROPERTYNAME_BULK_DATE_BEGIN = "bulkDateBegin";
	public static final String PROPERTYNAME_BULK_DATE_END = "bulkDateEnd";
	public static final String PROPERTYNAME_ISOBULK_DATE_BEGIN = "isoBulkDateBegin";
	public static final String PROPERTYNAME_ISOBULK_DATE_END = "isoBulkDateEnd";
	public static final String PROPERTYNAME_ISOBULK_DATE_BEGIN_SECONDS = "isoBulkDateBeginSeconds";
	public static final String PROPERTYNAME_ISOBULK_DATE_END_SECONDS = "isoBulkDateEndSeconds";

	public static final String PROPERTYNAME_DATE_BEGIN = "dateBegin";
	public static final String PROPERTYNAME_DATE_END = "dateEnd";
    public static final String PROPERTYNAME_ISODATE_BEGIN = "isoDateBegin";
	public static final String PROPERTYNAME_ISODATE_END = "isoDateEnd";
    public static final String PROPERTYNAME_ISODATE_BEGIN_SECONDS = "isoDateBeginSeconds";
	public static final String PROPERTYNAME_ISODATE_END_SECONDS = "isoDateEndSeconds";

	public static final String PROPERTYNAME_DATE_EXPRESSION = "dateExpression";
	public static final String PROPERTYNAME_CERTAINTY = "certainty";
	public static final String PROPERTYNAME_DATE_TYPE = "dateType";
	public static final String PROPERTYNAME_ERA = "era";
	public static final String PROPERTYNAME_CALENDAR = "calendar";

	public static final String PROPERTYNAME_DATE_DISPLAY = "dateDisplay";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long archDescriptionDatesId = null;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private String dateExpression;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Integer dateBegin;

	//@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Integer dateEnd;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private String isoDateBegin;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private String isoDateEnd;

    //@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Long isoDateBeginSeconds;

	//@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Long isoDateEndSeconds;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Integer bulkDateBegin;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Integer bulkDateEnd;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private String isoBulkDateBegin;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private String isoBulkDateEnd;

	//@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long isoBulkDateBeginSeconds;

	//@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long isoBulkDateEndSeconds;

	@IncludeInApplicationConfiguration
	private Boolean certainty;

	@IncludeInApplicationConfiguration(1)
	private String dateType;

	@IncludeInApplicationConfiguration
	private String era;

	@IncludeInApplicationConfiguration
	private String calendar;

	private ResourcesComponents resourceComponent;

	private Resources resource;

	private DigitalObjects digitalObject;

	private Accessions accession;


	/**
	 * No-arg constructor for JavaBean tools.
	 */
	ArchDescriptionDates() {
	}


	/**
	 * Full constructor;
	 */

	public ArchDescriptionDates(ArchDescription archDescription) {
		linkArchDescription(archDescription);
	}

	private void linkArchDescription(ArchDescription archDescription) {
		if (archDescription instanceof Resources) {
			this.resource = (Resources) archDescription;
		} else if (archDescription instanceof ResourcesComponents) {
			this.resourceComponent = (ResourcesComponents) archDescription;
		} else if (archDescription instanceof DigitalObjects) {
			this.digitalObject = (DigitalObjects)archDescription;
		} else if (archDescription instanceof Accessions) {
			this.accession = (Accessions)archDescription;
		}
	}


	public Long getArchDescriptionDatesId() {
		return archDescriptionDatesId;
	}

	public void setArchDescriptionDatesId(Long archDescriptionDatesId) {
		this.archDescriptionDatesId = archDescriptionDatesId;
	}

	/**
	 * @return Returns the identifier.
	 */
	public Long getIdentifier() {
		return this.getArchDescriptionDatesId();
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(Long identifier) {
		setArchDescriptionDatesId(identifier);
	}

//	public String getContent() {
//		return return ;
//	}
//
//	public String getType() {
//		return null;
//	}
//
//	public String getFullDescription() {
//		return null;
//	}

	public String getDateExpression() {
		return dateExpression;
	}

	public void setDateExpression(String dateExpression) {
		this.dateExpression = dateExpression;
	}

	public Boolean getCertainty() {
		return certainty;
	}

	public void setCertainty(Boolean certainty) {
		this.certainty = certainty;
	}

	public String getDateType() {
		if (this.dateType != null) {
			return dateType;
		} else {
			return "";
		}
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public String getEra() {
		if (this.era != null) {
			return era;
		} else {
			return "";
		}
	}

	public void setEra(String era) {
		this.era = era;
	}

	public String getCalendar() {
		if (this.calendar != null) {
			return calendar;
		} else {
			return "";
		}
	}

	public void setCalendar(String calendar) {
		this.calendar = calendar;
	}


	public Integer getDateBegin() {
		return this.dateBegin;
	}

	public void setDateBegin(Integer dateBegin) {
		this.dateBegin = dateBegin;
	}

	/**
	 *
	 */
	public Integer getDateEnd() {
		return this.dateEnd;
	}

	public void setDateEnd(Integer dateEnd) {
        this.dateEnd = dateEnd;
	}

    /**
     * Method that return the iso date begin reformated to match
     * @return String containing the iso bluck date begin
     */
    public String getIsoDateBegin() {
        if(isoDateBegin == null) { // return just the year
            if(getDateBegin() != null) {
                return getDateBegin() + "";
            } else {
                return "";
            }
        } else { // return the date begin in the correct date format
            return this.isoDateBegin;
        }
    }

    /**
     * Method to set the ISO date begins
     * @param isoDateBegin The new iso  date begins
     */
    public void setIsoDateBegin(String isoDateBegin) {
        this.isoDateBegin = isoDateBegin;

        // for backward compatibility set the year now
        setDateBegin(ATDateUtils.getYearFromISODate(isoDateBegin));

        // set this iso date in seconds used for searchng purposes
        setIsoDateBeginSeconds(ATDateUtils.getISODateInSeconds(isoDateBegin));
    }

    /**
     * Method to return the iso date begins in seconds
     * @return The date begins in seconds
     */
    public Long getIsoDateBeginSeconds() {
        return isoDateBeginSeconds;
    }

    /**
     * Method to set the ISO date begins in GMT seconds
     * @param seconds The iso date begins in GMT seconds
     */
    public void setIsoDateBeginSeconds(Long seconds) {
        this.isoDateBeginSeconds = seconds;
    }

    /**
     * Method that return the iso date begin reformated to match
     * @return String containing the iso bluck date begin
     */
    public String getIsoDateEnd() {
        if(isoDateEnd == null) { // return just the year then
            if(getDateEnd() != null) {
                return getDateEnd() + "";
            } else {
                return "";
            }
        } else { // return the date begin
            return this.isoDateEnd;
        }
    }

    /**
     * Method to set the ISO  date ends
     * @param isoDateEnd The new iso  date ends
     */
    public void setIsoDateEnd(String isoDateEnd) {
        this.isoDateEnd = isoDateEnd;

        // for backward compatibility set the year now
        setDateEnd(ATDateUtils.getYearFromISODate(isoDateEnd));

        // set this iso date in seconds used for searchng purposes
        setIsoDateEndSeconds(ATDateUtils.getISODateInSeconds(isoDateEnd));
    }

    /**
     * Method to return the iso date end in seconds
     * @return The date end in seconds
     */
    public Long getIsoDateEndSeconds() {
        return isoDateEndSeconds;
    }

    /**
     * Method to set the ISO date ends in GMT seconds
     * @param seconds The iso date ends in GMT seconds
     */
    public void setIsoDateEndSeconds(Long seconds) {
        this.isoDateEndSeconds = seconds;
    }

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
        this.bulkDateEnd = bulkDateEnd;
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

	@IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_DATE_DISPLAY, value=2)
  @ExcludeFromDefaultValues
	public String getDateDisplay() {
		if (getDateExpression() != null && getDateExpression().length() > 0) {
			return getDateExpression();
		} else {
			return getIsoDateBegin() + "-" +getIsoDateEnd();
		}
	}


	public ResourcesComponents getResourceComponent() {
		return resourceComponent;
	}

	public void setResourceComponent(ResourcesComponents resourceComponent) {
		this.resourceComponent = resourceComponent;
	}

	public Resources getResource() {
		return resource;
	}

	public void setResource(Resources resource) {
		this.resource = resource;
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

}
