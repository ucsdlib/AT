package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.StringLengthValidationRequried;
import org.archiviststoolkit.structure.DefaultIncludeInSearchEditor;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;

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
 * 
 * The purpose of this class is to capture the quantitative and/or qualitative
 * “collection-level” assessment that an archival repository performs in order to
 * prioritize processing and other work.  The assessment functional area encompasses
 * traditional archival appraisal but also supports other functions relative to
 * prioritizing archival work.
 *
 *
 * @author: Nathan Stevens
 * Date: Mar 26, 2009
 * Time: 1:04:36 PM
 */
public class Assessments extends DomainObject {
    public static final String PROPERTYNAME_CONDITION_OF_MATERIAL_RATING = "conditionOfMaterial";
    public static final String PROPERTYNAME_PHYSICAL_ACCESS_RATING = "physicalAccess";
    public static final String PROPERTYNAME_QUALITY_OF_HOUSING_RATING = "qualityOfHousing";
    public static final String PROPERTYNAME_INTELLECTUAL_ACCESS_RATING = "intellectualAccess";
    public static final String PROPERTYNAME_INTEREST_RATING = "interest";
    public static final String PROPERTYNAME_DOCUMENTATION_QUALITY_RATING = "documentationQuality";
    public static final String PROPERTYNAME_RESEARCH_VALUE_RATING = "researchValue";
    public static final String PROPERTYNAME_USER_NUMERICAL_RATING1 = "userNumericalRating1";
    public static final String PROPERTYNAME_USER_NUMERICAL_RATING2 = "userNumericalRating2";
    public static final String PROPERTYNAME_GENERAL_NOTE = "generalNote";
    public static final String PROPERTYNAME_CONSERVATION_NOTE = "conservationNote";
    public static final String PROPERTYNAME_SPECIAL_FORMAT1 = "architecturalMaterials";
    public static final String PROPERTYNAME_SPECIAL_FORMAT2 = "artOriginals";
    public static final String PROPERTYNAME_SPECIAL_FORMAT3 = "artifacts";
    public static final String PROPERTYNAME_SPECIAL_FORMAT4 = "audioMaterials";
    public static final String PROPERTYNAME_SPECIAL_FORMAT5 = "biologicalSpecimens";
    public static final String PROPERTYNAME_SPECIAL_FORMAT6 = "botanicalSpecimens";
    public static final String PROPERTYNAME_SPECIAL_FORMAT7 = "computerStorageUnits";
    public static final String PROPERTYNAME_SPECIAL_FORMAT8 = "film";
    public static final String PROPERTYNAME_SPECIAL_FORMAT9 = "glass";
    public static final String PROPERTYNAME_SPECIAL_FORMAT10 = "photographs";
    public static final String PROPERTYNAME_SPECIAL_FORMAT11 = "scrapbooks";
    public static final String PROPERTYNAME_SPECIAL_FORMAT12 = "technicalDrawingsAndSchematics";
    public static final String PROPERTYNAME_SPECIAL_FORMAT13 = "textiles";
    public static final String PROPERTYNAME_SPECIAL_FORMAT14 = "vellumAndParchment";
    public static final String PROPERTYNAME_SPECIAL_FORMAT15 = "videoMaterials";
    public static final String PROPERTYNAME_SPECIAL_FORMAT16 = "other";
    public static final String PROPERTYNAME_SPECIAL_FORMAT17 = "specialFormat1";
    public static final String PROPERTYNAME_SPECIAL_FORMAT18 = "specialFormat2";
    public static final String PROPERTYNAME_SPECIAL_FORMAT_NOTE = "specialFormatNote";
    public static final String PROPERTYNAME_SPECIAL_CONSERVATION_ISSUE1 = "potentialMoldOrMoldDamage";
    public static final String PROPERTYNAME_SPECIAL_CONSERVATION_ISSUE2 = "recentPestDamage";
    public static final String PROPERTYNAME_SPECIAL_CONSERVATION_ISSUE3 = "deterioratingFilmBase";
    public static final String PROPERTYNAME_SPECIAL_CONSERVATION_ISSUE4 = "specialConservationIssue1";
    public static final String PROPERTYNAME_SPECIAL_CONSERVATION_ISSUE5 = "specialConservationIssue2";
    public static final String PROPERTYNAME_SPECIAL_CONSERVATION_ISSUE6 = "specialConservationIssue3";
    public static final String PROPERTYNAME_OTHER_CONSERVATION_ISSUE1 = "brittlePaper";
    public static final String PROPERTYNAME_OTHER_CONSERVATION_ISSUE2 = "metalFasteners";
    public static final String PROPERTYNAME_OTHER_CONSERVATION_ISSUE3 = "newspaper";
    public static final String PROPERTYNAME_OTHER_CONSERVATION_ISSUE4 = "tape";
    public static final String PROPERTYNAME_OTHER_CONSERVATION_ISSUE5 = "thermofaxPaper";
    public static final String PROPERTYNAME_OTHER_CONSERVATION_ISSUE6 = "otherConservationIssue1";
    public static final String PROPERTYNAME_OTHER_CONSERVATION_ISSUE7 = "otherConservationIssue2";
    public static final String PROPERTYNAME_OTHER_CONSERVATION_ISSUE8 = "otherConservationIssue3";
    public static final String PROPERTYNAME_EXHIBITION_VALUE_NOTE = "exhibitionValueNote";
    public static final String PROPERTYNAME_MONETARY_VALUE = "monetaryValue";
    public static final String PROPERTYNAME_MONETARY_VALUE_NOTE = "monetaryValueNote";
    public static final String PROPERTYNAME_ESTIMATED_PROCESSING_TIME_PER_FOOT = "estimatedProcessingTimePerFoot";
    public static final String PROPERTYNAME_TOTAL_EXTENT = "totalExtent";
    public static final String PROPERTYNAME_TOTAL_ESTIMATED_PROCESSING_TIME = "totalEstimatedProcessingTime";
    public static final String PROPERTYNAME_WHO_DID_SURVEY = "whoDidSurvey";
    public static final String PROPERTYNAME_AMOUNT_OF_TIME_SURVEY_TOOK = "amountOfTimeSurveyTook";
    public static final String PROPERTYNAME_USER_WHO_CREATED_RECORD = "userWhoCreatedRecord";
    public static final String PROPERTYNAME_DATE_OF_SURVEY = "dateOfSurvey";
    public static final String PROPERTYNAME_REVIEW_NEEDED = "reviewNeeded";
    public static final String PROPERTYNAME_WHO_NEEDS_TO_REVIEW = "whoNeedsToReview";
    public static final String PROPERTYNAME_REVIEW_NOTE = "reviewNote";
    public static final String PROPERTYNAME_INACTIVE = "inactive";

    @IncludeInApplicationConfiguration(1)
	@ExcludeFromDefaultValues
    private Long assessmentId;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    @DefaultIncludeInSearchEditor
    private Integer conditionOfMaterial;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    @DefaultIncludeInSearchEditor
    private Integer physicalAccess;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    @DefaultIncludeInSearchEditor
    private Integer qualityOfHousing;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    @DefaultIncludeInSearchEditor
    private Integer intellectualAccess;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    @DefaultIncludeInSearchEditor
    private Integer interest;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    @DefaultIncludeInSearchEditor
    private Integer documentationQuality;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    @DefaultIncludeInSearchEditor
    private Integer researchValue;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Integer userNumericalRating1;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Integer userNumericalRating2;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private String generalNote = "";

    @IncludeInApplicationConfiguration
    @ExcludeFromDefaultValues
    private String conservationNote = "";

    /**
     * Special format fields 1-16
     */
    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean architecturalMaterials = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean artOriginals = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean artifacts = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean audioMaterials = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean biologicalSpecimens = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean botanicalSpecimens = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean computerStorageUnits = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean film = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean glass = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean photographs = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean scrapbooks = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean technicalDrawingsAndSchematics = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean textiles = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean vellumAndParchment = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean videoMaterials = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean other = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean specialFormat1 = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean specialFormat2 = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private String specialFormatNote = "";

    /**
     * Conservation Issue Fields
     */

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean potentialMoldOrMoldDamage = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean recentPestDamage = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean deterioratingFilmBase = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean specialConservationIssue1 = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean specialConservationIssue2 = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean specialConservationIssue3 = false;

    /**
     * Other Conservation Issue Fields
     */
    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean brittlePaper = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean metalFasteners = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean newspaper = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean tape = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean thermofaxPaper = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean otherConservationIssue1 = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean otherConservationIssue2 = false;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean otherConservationIssue3 = false;

    /* end of other conservation issue fields */

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private String exhibitionValueNote = "";

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Double monetaryValue;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private String monetaryValueNote = "";

    @IncludeInApplicationConfiguration
    private Double estimatedProcessingTimePerFoot;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Double totalExtent;

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Double totalEstimatedProcessingTime;

    @IncludeInApplicationConfiguration
    private String whoDidSurvey = "";

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Double amountOfTimeSurveyTook;

    @IncludeInApplicationConfiguration
    @ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String userWhoCreatedRecord = "";

    @IncludeInApplicationConfiguration
    @ExcludeFromDefaultValues
    private Date dateOfSurvey;

    @IncludeInApplicationConfiguration
    private Boolean reviewNeeded = false;

    @IncludeInApplicationConfiguration
    private String whoNeedsToReview = "";

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private String reviewNote = "";

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean inactive = false;

    @IncludeInApplicationConfiguration
	//@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	private Repositories repository;

    // some set for storing resources, digital objects, accessions
    @ExcludeFromDefaultValues
    private Set<AssessmentsResources> resources = new HashSet<AssessmentsResources>();

    @ExcludeFromDefaultValues
    private Set<AssessmentsAccessions> accessions = new HashSet<AssessmentsAccessions>();

    @ExcludeFromDefaultValues
    private Set<AssessmentsDigitalObjects> digitalObjects = new HashSet<AssessmentsDigitalObjects>();

    /**
	 * Creates a new instance of an Assessment
	 */
	public Assessments() { }

    /**
     * Method to return the assessment ID set by hibernate
     *
     * @return The assessment ID
     */
    public Long getAssessmentId() {
		return assessmentId;
	}

    /**
     * Method to set the assessment ID. This is called by hibernate
     * @param assessmentId The assessment ID that is set for this object
     */
	public void setAssessmentId(Long assessmentId) {
		this.assessmentId = assessmentId;
	}

	public Long getIdentifier() {
		return this.getAssessmentId();
	}

	public void setIdentifier(Long identifier) {
		this.setAssessmentId(identifier);
	}

    public Integer getConditionOfMaterial() {
        return conditionOfMaterial;
    }

    public void setConditionOfMaterial(Integer conditionOfMaterial) {
        this.conditionOfMaterial = conditionOfMaterial;
    }

    public Integer getPhysicalAccess() {
        return physicalAccess;
    }

    public void setPhysicalAccess(Integer physicalAccess) {
        this.physicalAccess = physicalAccess;
    }

    public Integer getQualityOfHousing() {
        return qualityOfHousing;
    }

    public void setQualityOfHousing(Integer qualityOfHousing) {
        this.qualityOfHousing = qualityOfHousing;
    }

    public Integer getIntellectualAccess() {
        return intellectualAccess;
    }

    public void setIntellectualAccess(Integer intellectualAccess) {
        this.intellectualAccess = intellectualAccess;
    }

    public Integer getInterest() {
        return interest;
    }

    public void setInterest(Integer interest) {
        this.interest = interest;
    }

    public Integer getDocumentationQuality() {
        return documentationQuality;
    }

    public void setDocumentationQuality(Integer documentationQuality) {
        this.documentationQuality = documentationQuality;
    }

    public Integer getResearchValue() {
        if(documentationQuality != null && interest != null) {
            return documentationQuality + interest;
        } else {
            return null;
        }
    }

    public void setResearchValue(Integer researchValue) {
        this.researchValue = researchValue;
    }

    public Integer getUserNumericalRating1() {
        return userNumericalRating1;
    }

    public void setUserNumericalRating1(Integer userNumericalRating1) {
        this.userNumericalRating1 = userNumericalRating1;
    }

    public Integer getUserNumericalRating2() {
        return userNumericalRating2;
    }

    public void setUserNumericalRating2(Integer userNumericalRating2) {
        this.userNumericalRating2 = userNumericalRating2;
    }

    public String getGeneralNote() {
        return generalNote;
    }

    public void setGeneralNote(String generalNote) {
        this.generalNote = generalNote;
    }

    public String getConservationNote() {
        return conservationNote;
    }

    public void setConservationNote(String conservationNote) {
        this.conservationNote = conservationNote;
    }

    public Boolean getArchitecturalMaterials() {
        return architecturalMaterials;
    }

    public void setArchitecturalMaterials(Boolean architecturalMaterials) {
        this.architecturalMaterials = architecturalMaterials;
    }

    public Boolean getArtOriginals() {
        return artOriginals;
    }

    public void setArtOriginals(Boolean artOriginals) {
        this.artOriginals = artOriginals;
    }

    public Boolean getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(Boolean artifacts) {
        this.artifacts = artifacts;
    }

    public Boolean getAudioMaterials() {
        return audioMaterials;
    }

    public void setAudioMaterials(Boolean audioMaterials) {
        this.audioMaterials = audioMaterials;
    }

    public Boolean getBiologicalSpecimens() {
        return biologicalSpecimens;
    }

    public void setBiologicalSpecimens(Boolean biologicalSpecimens) {
        this.biologicalSpecimens = biologicalSpecimens;
    }

    public Boolean getBotanicalSpecimens() {
        return botanicalSpecimens;
    }

    public void setBotanicalSpecimens(Boolean botanicalSpecimens) {
        this.botanicalSpecimens = botanicalSpecimens;
    }

    public Boolean getComputerStorageUnits() {
        return computerStorageUnits;
    }

    public void setComputerStorageUnits(Boolean computerStorageUnits) {
        this.computerStorageUnits = computerStorageUnits;
    }

    public Boolean getFilm() {
        return film;
    }

    public void setFilm(Boolean film) {
        this.film = film;
    }

    public Boolean getGlass() {
        return glass;
    }

    public void setGlass(Boolean glass) {
        this.glass = glass;
    }

    public Boolean getPhotographs() {
        return photographs;
    }

    public void setPhotographs(Boolean photographs) {
        this.photographs = photographs;
    }

    public Boolean getScrapbooks() {
        return scrapbooks;
    }

    public void setScrapbooks(Boolean scrapbooks) {
        this.scrapbooks = scrapbooks;
    }

    public Boolean getTechnicalDrawingsAndSchematics() {
        return technicalDrawingsAndSchematics;
    }

    public void setTechnicalDrawingsAndSchematics(Boolean technicalDrawingsAndSchematics) {
        this.technicalDrawingsAndSchematics = technicalDrawingsAndSchematics;
    }

    public Boolean getTextiles() {
        return textiles;
    }

    public void setTextiles(Boolean textiles) {
        this.textiles = textiles;
    }

    public Boolean getVellumAndParchment() {
        return vellumAndParchment;
    }

    public void setVellumAndParchment(Boolean vellumAndParchment) {
        this.vellumAndParchment = vellumAndParchment;
    }

    public Boolean getVideoMaterials() {
        return videoMaterials;
    }

    public void setVideoMaterials(Boolean videoMaterials) {
        this.videoMaterials = videoMaterials;
    }

    public Boolean getOther() {
        return other;
    }

    public void setOther(Boolean other) {
        this.other = other;
    }

    public Boolean getSpecialFormat1() {
        return specialFormat1;
    }

    public void setSpecialFormat1(Boolean specialFormat1) {
        this.specialFormat1 = specialFormat1;
    }

    public Boolean getSpecialFormat2() {
        return specialFormat2;
    }

    public void setSpecialFormat2(Boolean specialFormat2) {
        this.specialFormat2 = specialFormat2;
    }

    public String getSpecialFormatNote() {
        return specialFormatNote;
    }

    public void setSpecialFormatNote(String specialFormatNote) {
        this.specialFormatNote = specialFormatNote;
    }

    public Boolean getPotentialMoldOrMoldDamage() {
        return potentialMoldOrMoldDamage;
    }

    public void setPotentialMoldOrMoldDamage(Boolean potentialMoldOrMoldDamage) {
        this.potentialMoldOrMoldDamage = potentialMoldOrMoldDamage;
    }

    public Boolean getRecentPestDamage() {
        return recentPestDamage;
    }

    public void setRecentPestDamage(Boolean recentPestDamage) {
        this.recentPestDamage = recentPestDamage;
    }

    public Boolean getDeterioratingFilmBase() {
        return deterioratingFilmBase;
    }

    public void setDeterioratingFilmBase(Boolean deterioratingFilmBase) {
        this.deterioratingFilmBase = deterioratingFilmBase;
    }

    public Boolean getSpecialConservationIssue1() {
        return specialConservationIssue1;
    }

    public void setSpecialConservationIssue1(Boolean specialConservationIssue1) {
        this.specialConservationIssue1 = specialConservationIssue1;
    }

    public Boolean getSpecialConservationIssue2() {
        return specialConservationIssue2;
    }

    public void setSpecialConservationIssue2(Boolean specialConservationIssue2) {
        this.specialConservationIssue2 = specialConservationIssue2;
    }

    public Boolean getSpecialConservationIssue3() {
        return specialConservationIssue3;
    }

    public void setSpecialConservationIssue3(Boolean specialConservationIssue3) {
        this.specialConservationIssue3 = specialConservationIssue3;
    }

    public Boolean getBrittlePaper() {
        return brittlePaper;
    }

    public void setBrittlePaper(Boolean brittlePaper) {
        this.brittlePaper = brittlePaper;
    }

    public Boolean getMetalFasteners() {
        return metalFasteners;
    }

    public void setMetalFasteners(Boolean metalFasteners) {
        this.metalFasteners = metalFasteners;
    }

    public Boolean getNewspaper() {
        return newspaper;
    }

    public void setNewspaper(Boolean newspaper) {
        this.newspaper = newspaper;
    }

    public Boolean getTape() {
        return tape;
    }

    public void setTape(Boolean tape) {
        this.tape = tape;
    }

    public Boolean getThermofaxPaper() {
        return thermofaxPaper;
    }

    public void setThermofaxPaper(Boolean thermofaxPaper) {
        this.thermofaxPaper = thermofaxPaper;
    }

    public Boolean getOtherConservationIssue1() {
        return otherConservationIssue1;
    }

    public void setOtherConservationIssue1(Boolean otherConservationIssue1) {
        this.otherConservationIssue1 = otherConservationIssue1;
    }

    public Boolean getOtherConservationIssue2() {
        return otherConservationIssue2;
    }

    public void setOtherConservationIssue2(Boolean otherConservationIssue2) {
        this.otherConservationIssue2 = otherConservationIssue2;
    }

    public Boolean getOtherConservationIssue3() {
        return otherConservationIssue3;
    }

    public void setOtherConservationIssue3(Boolean otherConservationIssue3) {
        this.otherConservationIssue3 = otherConservationIssue3;
    }

    public String getExhibitionValueNote() {
        return exhibitionValueNote;
    }

    public void setExhibitionValueNote(String exhibitionValueNote) {
        this.exhibitionValueNote = exhibitionValueNote;
    }

    public Double getMonetaryValue() {
        return monetaryValue;
    }

    public void setMonetaryValue(Double monetaryValue) {
        this.monetaryValue = monetaryValue;
    }

    public String getMonetaryValueNote() {
        return monetaryValueNote;
    }

    public void setMonetaryValueNote(String monetaryValueNote) {
        this.monetaryValueNote = monetaryValueNote;
    }

    public Double getEstimatedProcessingTimePerFoot() {
        return estimatedProcessingTimePerFoot;
    }

    public void setEstimatedProcessingTimePerFoot(Double estimatedProcessingTimePerFoot) {
        this.estimatedProcessingTimePerFoot = estimatedProcessingTimePerFoot;
    }

    public Double getTotalExtent() {
        return totalExtent;
    }

    public void setTotalExtent(Double totalExtent) {
        this.totalExtent = totalExtent;
    }

    public Double getTotalEstimatedProcessingTime() {
        if(estimatedProcessingTimePerFoot != null && totalExtent != null) {
            return estimatedProcessingTimePerFoot*totalExtent;
        } else {
            return null;
        }
    }

    public void setTotalEstimatedProcessingTime(Double totalEstimatedProcessingTime) {
        this.totalEstimatedProcessingTime = totalEstimatedProcessingTime;
    }

    public String getWhoDidSurvey() {
        return whoDidSurvey;
    }

    public void setWhoDidSurvey(String whoDidSurvey) {
        this.whoDidSurvey = whoDidSurvey;
    }

    public Double getAmountOfTimeSurveyTook() {
        return amountOfTimeSurveyTook;
    }

    public void setAmountOfTimeSurveyTook(Double amountOfTimeSurveyTook) {
        this.amountOfTimeSurveyTook = amountOfTimeSurveyTook;
    }

    public String getUserWhoCreatedRecord() {
        return userWhoCreatedRecord;
    }

    public void setUserWhoCreatedRecord(String userWhoCreatedRecord) {
        this.userWhoCreatedRecord = userWhoCreatedRecord;
    }

    public Date getDateOfSurvey() {
        return dateOfSurvey;
    }

    public void setDateOfSurvey(Date dateOfSurvey) {
        this.dateOfSurvey = dateOfSurvey;
    }

    public Boolean getReviewNeeded() {
        return reviewNeeded;
    }

    public void setReviewNeeded(Boolean reviewNeeded) {
        this.reviewNeeded = reviewNeeded;
    }

    public String getWhoNeedsToReview() {
        return whoNeedsToReview;
    }

    public void setWhoNeedsToReview(String whoNeedsToReview) {
        this.whoNeedsToReview = whoNeedsToReview;
    }

    public String getReviewNote() {
        return reviewNote;
    }

    public void setReviewNote(String reviewNote) {
        this.reviewNote = reviewNote;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public Repositories getRepository() {
		return repository;
	}

	public void setRepository(Repositories repository) {
		this.repository = repository;
	}

    public Set<AssessmentsResources> getResources() {
        return resources;
    }

    public void setResources(Set<AssessmentsResources> resources) {
        this.resources = resources;
    }

    public Set<AssessmentsAccessions> getAccessions() {
        return accessions;
    }

    public void setAccessions(Set<AssessmentsAccessions> accessions) {
        this.accessions = accessions;
    }

    public Set<AssessmentsDigitalObjects> getDigitalObjects() {
        return digitalObjects;
    }

    public void setDigitalObjects(Set<AssessmentsDigitalObjects> digitalObjects) {
        this.digitalObjects = digitalObjects;
    }

    /**
     * Method to see if this assessment has any records linked to it.
     * @return true if there are any records links to this assessment
     */
    public boolean hasLinkRecords() {
        if(!resources.isEmpty()) {
            return true;
        } else if(!accessions.isEmpty()) {
            return true;
        } else if(!digitalObjects.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method to link a accession to the assessment object
     * @param accession the accession to link
     * @return The AssessmentsAccession linking object
     */
    public AssessmentsAccessions addAccession(Accessions accession) throws DuplicateLinkException {
		if (accession == null)
			throw new IllegalArgumentException("Can't add a null accession.");
		if (containsAccessionLink(accession)) {
			throw new DuplicateLinkException(accession.toString());
		} else {
			AssessmentsAccessions link = new AssessmentsAccessions(accession, this);
			this.getAccessions().add(link);
			return link;
		}
	}

    /**
     * Method to link a resource to the assessment object
     * @param resource the resouce to link
     * @return The AssessmentsResource linking object
     */
    public AssessmentsResources addResource(Resources resource) throws DuplicateLinkException {
		if (resource == null)
			throw new IllegalArgumentException("Can't add a null resource.");
		if (containsResourceLink(resource)) {
			throw new DuplicateLinkException(resource.toString());
		} else {
			AssessmentsResources link = new AssessmentsResources(resource, this);
			this.getResources().add(link);
			return link;
		}
	}

    /**
     * Method to link a DigitalObject to the assessment object
     * @param digitalObject the Digital Object to link
     * @return The AssessmentsDigitalObject linking object
     */
    public AssessmentsDigitalObjects addDigitalObject(DigitalObjects digitalObject) throws DuplicateLinkException {
		if (digitalObject == null)
			throw new IllegalArgumentException("Can't add a null digital object.");
		if (containsDigitalObjectLink(digitalObject)) {
			throw new DuplicateLinkException(digitalObject.toString());
		} else {
			AssessmentsDigitalObjects link = new AssessmentsDigitalObjects(digitalObject, this);
			this.getDigitalObjects().add(link);
			return link;
		}
	}

    /**
     * Method to check to see if this accession is already linked to this object
     * @param accession The accession to look for
     * @return true if the accession is already link or false if it is not
     */
    public boolean containsAccessionLink(Accessions accession) {
		for (AssessmentsAccessions accessionLink: getAccessions()) {
			if (accessionLink.getAccession().getAccessionNumber().equals(accession.getAccessionNumber())) {
				return true;
			}
		}
		return false;
	}

    /**
     * Method to check to see if this resource is already linked to this object
     * @param resource The resource to look for
     * @return true if the resource is already link or false if it is not
     */
    public boolean containsResourceLink(Resources resource) {
		for (AssessmentsResources resourceLink: getResources()) {
			if (resourceLink.getResource() == resource) {
				return true;
			}
		}
		return false;
	}

    /**
     * Method to check to see if this digital object is already linked to this object
     * @param digitalObject The Digital Object to look for
     * @return true if the DigitalObject is already link or false if it is not
     */
    public boolean containsDigitalObjectLink(DigitalObjects digitalObject) {
		for (AssessmentsDigitalObjects digitalObjectLink: getDigitalObjects()) {
			if (digitalObjectLink.getDigitalObject() == digitalObject) {
				return true;
			}
		}
		return false;
	}

    /**
     * Method to remove linked domain object records from this record
     * @param domainObject the domain object to be removed
     * @throws ObjectNotRemovedException If the object can't be removed
     */
    public void removeRelatedObject(DomainObject domainObject) throws ObjectNotRemovedException {
		if (domainObject instanceof AssessmentsAccessions) {
			getAccessions().remove((AssessmentsAccessions)domainObject);
		} else if (domainObject instanceof AssessmentsResources) {
			getResources().remove((AssessmentsResources)domainObject);
		} else if (domainObject instanceof AssessmentsDigitalObjects) {
			getDigitalObjects().remove((AssessmentsDigitalObjects)domainObject);
		} else {
			super.removeRelatedObject(domainObject);
		}
	}
}
