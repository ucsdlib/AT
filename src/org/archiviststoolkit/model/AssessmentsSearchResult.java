package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.IncludeMethodInApplicationConfiguration;
import org.archiviststoolkit.structure.DefaultIncludeInSearchEditor;

import java.util.Date;

/**
 * Archivists' Toolkit(TM) Copyright � 2005-2009 Regents of the University of California, New York University, & Five Colleges, Inc.
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
 * This class stores the search results from an assessment record search
 *
 * @author: Nathan Stevens
 * Date: May 1, 2009
 * Time: 2:53:27 PM
 */
@ExcludeFromDefaultValues
public class AssessmentsSearchResult extends DomainObject {
    public static final String PROPERTYNAME_ASSESSMENT_ID = "assessmentId";
	public static final String PROPERTYNAME_SURVEYED_MATERIAL_TITLE = "surveyedMaterialTitle";
	public static final String PROPERTYNAME_SURVEYED_MATERIAL_ID = "surveyedMaterialId";
	public static final String PROPERTYNAME_CONDITION_OF_MATERIAL_RATING = "conditionOfMaterialRating";
	public static final String PROPERTYNAME_RESEARCH_VALUE_RATING = "researchValueRating";
	public static final String PROPERTYNAME_PHYSICAL_ACCESS_RATING = "physicalAccessRating";
	public static final String PROPERTYNAME_INTELLECTUAL_ACCESS_RATING = "intellectualAccessRating";
	public static final String PROPERTYNAME_QUALITY_OF_HOUSING_RATING = "qualityOfHousingRating";
    public static final String PROPERTYNAME_USER_NUMERICAL_RATING1 = "userNumericalRating1";
    public static final String PROPERTYNAME_USER_NUMERICAL_RATING2 = "userNumericalRating2";

    private Assessments assessment; // The assessment that was found
    private Resources resource = null; // The resource record
    private Accessions accession = null; // The accession record
    private DigitalObjects digitalObject = null; // The digital object record
    private Long id; // The identifier for this search result

    // The defualt constructor
    public AssessmentsSearchResult() {}

    // constructor that only accepts an assessment record
    public AssessmentsSearchResult(Assessments assessment) {
        this.assessment = assessment;
    }

    // constructor that takes a assessment and resource record
    public AssessmentsSearchResult(Assessments assessment, Resources resource) {
        this.assessment = assessment;
        this.resource = resource;
    }

    // constructor that takes a assessment and accession record
    public AssessmentsSearchResult(Assessments assessment, Accessions accession) {
        this.assessment = assessment;
        this.accession = accession;
    }

    // constructor that takes a assessment and digital object record
    public AssessmentsSearchResult(Assessments assessment, DigitalObjects digitalObject) {
        this.assessment = assessment;
        this.digitalObject = digitalObject;
    }

    /**
	 * @return Returns the identifier.
	 */
    public Long getIdentifier() {
		return assessment.getIdentifier();
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

    // Method to return the assessment id
    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_ASSESSMENT_ID, value = 1)
	@DefaultIncludeInSearchEditor
    public Long getAssessmentId() {
        return assessment.getIdentifier();
    }

    // Method to return the title
    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_SURVEYED_MATERIAL_ID, value = 2)
	@DefaultIncludeInSearchEditor
    public String getSurveyedMaterialId() {
        if(resource != null) {
            return resource.getResourceIdentifier();
        } else if(accession != null) {
            return accession.getAccessionNumber();
        } else if(digitalObject != null) {
            return digitalObject.getMetsIdentifier();
        } else {
            return "";
        }
    }

    // Method to return the title of link resource
    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_SURVEYED_MATERIAL_TITLE, value = 3)
	@DefaultIncludeInSearchEditor
    public String getSurveyedMaterialTitle() {
        if(resource != null) {
            return resource.getTitle();
        } else if(accession != null) {
            return accession.getTitle();
        } else if(digitalObject != null) {
            return digitalObject.getTitle();
        } else {
            return "";
        }
    }

    // Method to return the condition of material rating
    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_CONDITION_OF_MATERIAL_RATING, value = 4)
	@DefaultIncludeInSearchEditor
    public String getConditionOfMaterialRating() {
        return getRating(assessment.getConditionOfMaterial());
    }

    // Method to return the research value rating
    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_RESEARCH_VALUE_RATING, value = 5)
	@DefaultIncludeInSearchEditor
    public String getResearchValueRating() {
        return getRating(assessment.getResearchValue());
    }

    // Method to return the physical access rating
    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_PHYSICAL_ACCESS_RATING, value = 0)
	@DefaultIncludeInSearchEditor
    public String getPhysicalAccessRating() {
        return getRating(assessment.getPhysicalAccess());
    }

    // Method to return the intellectual access rating
    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_INTELLECTUAL_ACCESS_RATING, value = 0)
	@DefaultIncludeInSearchEditor
    public String getIntellectualAccessRating() {
        return getRating(assessment.getIntellectualAccess());
    }

    // Method to return the quality of housing rating
    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_QUALITY_OF_HOUSING_RATING, value = 0)
	@DefaultIncludeInSearchEditor
    public String getQualityOfHousingRating() {
        return getRating(assessment.getQualityOfHousing());
    }

    // Method to return the 1st user defined numerical rating
    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_USER_NUMERICAL_RATING1, value = 0)
	@DefaultIncludeInSearchEditor
    public String getUserNumericalRating1() {
        return getRating(assessment.getUserNumericalRating1());
    }

    // Method to return the 2nd user defined numerical rating
    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_USER_NUMERICAL_RATING2, value = 0)
	@DefaultIncludeInSearchEditor
    public String getUserNumericalRating2() {
        return getRating(assessment.getUserNumericalRating2());
    }

    /**
     * Method to return the assessment
     * @return The assessment associated with this object
     */
    public Assessments getAssessment() {
        return assessment;
    }

    /**
     * Method to return the resource record
     * @return The resource record or null if no resource record is associated with this object
     */
    public Resources getResource() {
        return resource;
    }

    /**
     * Method to return the accession record
     * @return The accession record or null if no accession record is associated with this object
     */
    public Accessions getAccession() {
        return accession;
    }

    /**
     * Method to return the digital object record
     * 
     * @return The digital object record or null if no digital object is associated with this object
     */
    public DigitalObjects getDigitalObject() {
        return digitalObject;
    }

    /**
     * Get the created date from the assessment record
     *
     * @return Date the record was created
     */
    public Date getCreated() {
        return assessment.getCreated();    
    }

    /**
     * Get who created the assessment record
     *
     * @return name of person who created the assessment record
     */
    public String getCreatedBy() {
        return assessment.getCreatedBy();
    }

    /**
     * Get the date the assessment record was last updated
     *
     * @return Date assessment was last updated
     */
    public Date getLastUpdated() {
        return assessment.getLastUpdated();
    }

    /**
     * Get the person who last updated the assessment record
     *
     * @return The person who last updated the assessment record
     */
    public String getLastUpdatedBy() {
        return assessment.getLastUpdatedBy();
    }

    /**
     * Method to convert a rating integer to a string
     * @param rating The rating integer
     * @return The string representation of the integer or a blank string if the rating is null
     */
    private String getRating(Integer rating) {
        if(rating != null && rating > 0) {
            return rating.toString();
        } else {
            return "";
        }
    }
}
