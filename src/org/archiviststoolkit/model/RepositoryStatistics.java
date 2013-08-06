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
 * Date: Mar 30, 2007
 * Time: 1:36:44 PM
 */

package org.archiviststoolkit.model;

import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.mydomain.DomainObject;

public class RepositoryStatistics extends DomainObject {

    public static final String PROPERTYNAME_YEAR_OF_REPORT = "yearOfReport";
    public static final String PROPERTYNAME_DIGITIZATION= "digitization";
    public static final String PROPERTYNAME_EXHIBIT_LOANS = "exhibitLoans";
    public static final String PROPERTYNAME_FOOD_BEVERAGE = "foodBeverage";
    public static final String PROPERTYNAME_PHOTOGRAPHIC_REPRODUCTION = "photographicReproduction";
    public static final String PROPERTYNAME_RETAIL_GIFT_SALES = "retailGiftSales";
    public static final String PROPERTYNAME_PROFESSIONAL_FTE = "professionalFTE";
    public static final String PROPERTYNAME_NON_PROFESSIONAL_FTE = "nonProfessionalFTE";
    public static final String PROPERTYNAME_STUDENT_FTE = "studentFTE";
    public static final String PROPERTYNAME_VOLUNTEER_FTE = "volunteerFTE";
    public static final String PROPERTYNAME_FUNCT_DIST_ADMIN= "functDistAdmin";
    public static final String PROPERTYNAME_FUNCT_DIST_PROCESSING = "functDistProcessing";
    public static final String PROPERTYNAME_FUNCT_DIST_PRESERVATION = "functDistPreservation";
    public static final String PROPERTYNAME_FUNCT_DIST_REFERENCE = "functDistReference";
    public static final String PROPERTYNAME_COLL_FOCI_HISTORICAL = "collFociHistorical";
    public static final String PROPERTYNAME_COLL_FOCI_INSTITUTIONAL = "collFociInstitutional";
    public static final String PROPERTYNAME_COLL_FOCI_MANUSCRIPT = "collFociManuscript";
    public static final String PROPERTYNAME_COLL_FOCI_PERSONAL_PAPERS= "collFociPersonalPapers";
    public static final String PROPERTYNAME_COLL_FOCI_OTHER = "collFociOther";
    public static final String PROPERTYNAME_MAJOR_SUBJECT_AREAS = "majorSubjectAreas";
    public static final String PROPERTYNAME_PERCENTAGE_OFF_SITE = "percentageOffSite";
    public static final String PROPERTYNAME_NET_USABLE_AREA = "netUsableArea";
    public static final String PROPERTYNAME_ADMINISTRATION_OFFICES = "administrationOffices";
    public static final String PROPERTYNAME_CLASSROOMS = "classrooms";
    public static final String PROPERTYNAME_COLLECTIONS_STORAGE = "collectionsStorage";
    public static final String PROPERTYNAME_READING_ROOM = "readingRoom";

    @IncludeInApplicationConfiguration
    private Long RepositoryStatisticsId;

    private Repositories repository;

    @IncludeInApplicationConfiguration(1)
    @ExcludeFromDefaultValues
    private Integer yearOfReport;

    @IncludeInApplicationConfiguration
    private Boolean digitization = false;

    @IncludeInApplicationConfiguration
    private Boolean exhibitLoans = false;

    @IncludeInApplicationConfiguration
    private Boolean foodBeverage = false;

    @IncludeInApplicationConfiguration
    private Boolean photographicReproduction = false;

    @IncludeInApplicationConfiguration
    private Boolean retailGiftSales = false;

    @IncludeInApplicationConfiguration
    private Double professionalFTE;

    @IncludeInApplicationConfiguration
    private Double nonProfessionalFTE;

    @IncludeInApplicationConfiguration
    private Double studentFTE;

    @IncludeInApplicationConfiguration
    private Double volunteerFTE;

    @IncludeInApplicationConfiguration
    private Double functDistAdmin;

    @IncludeInApplicationConfiguration
    private Double functDistProcessing;

    @IncludeInApplicationConfiguration
    private Double functDistPreservation;

    @IncludeInApplicationConfiguration
    @ExcludeFromDefaultValues
    private Double functDistReference;

    @IncludeInApplicationConfiguration
    private Boolean collFociHistorical = false;

    @IncludeInApplicationConfiguration
    private Boolean collFociInstitutional = false;

    @IncludeInApplicationConfiguration
    private Boolean collFociManuscript = false;

    @IncludeInApplicationConfiguration
    private Boolean collFociPersonalPapers = false;

    @IncludeInApplicationConfiguration
    private Boolean collFociOther = false;

    @IncludeInApplicationConfiguration
    private String majorSubjectAreas ="";

    @IncludeInApplicationConfiguration
    private Double percentageOffSite;

    @IncludeInApplicationConfiguration
    private Integer netUsableArea;

    @IncludeInApplicationConfiguration
    private Integer administrationOffices;

    @IncludeInApplicationConfiguration
    private Integer classrooms;

    @IncludeInApplicationConfiguration
    private Integer collectionsStorage;

    @IncludeInApplicationConfiguration
    private Integer readingRoom;

    public RepositoryStatistics(Repositories parentRepository) {
        super();
        this.repository = parentRepository;
    }

	public RepositoryStatistics() {
		super();
	}

	public Long getRepositoryStatisticsId() {
        return RepositoryStatisticsId;
    }

    public void setRepositoryStatisticsId(Long repositoryStatisticsId) {
        RepositoryStatisticsId = repositoryStatisticsId;
    }

    public Long getIdentifier() {
        return this.getRepositoryStatisticsId();
    }

    public void setIdentifier(Long identifier) {
        this.setRepositoryStatisticsId(identifier);
    }

    public Repositories getRepository() {
        return repository;
    }

    public void setRepository(Repositories repository) {
        this.repository = repository;
    }
    public Integer getYearOfReport() {
        return yearOfReport;
    }

    public void setYearOfReport(Integer yearOfReport) {
        this.yearOfReport = yearOfReport;
    }

    public Boolean getDigitization() {
        return digitization;
    }

    public void setDigitization(Boolean digitization) {
        this.digitization = digitization;
    }

    public Boolean getExhibitLoans() {
        return exhibitLoans;
    }

    public void setExhibitLoans(Boolean exhibitLoans) {
        this.exhibitLoans = exhibitLoans;
    }

    public Boolean getFoodBeverage() {
        return foodBeverage;
    }

    public void setFoodBeverage(Boolean foodBeverage) {
        this.foodBeverage = foodBeverage;
    }

    public Boolean getPhotographicReproduction() {
        return photographicReproduction;
    }

    public void setPhotographicReproduction(Boolean photographicReproduction) {
        this.photographicReproduction = photographicReproduction;
    }

    public Boolean getRetailGiftSales() {
        return retailGiftSales;
    }

    public void setRetailGiftSales(Boolean retailGiftSales) {
        this.retailGiftSales = retailGiftSales;
    }

    public Double getProfessionalFTE() {
        return professionalFTE;
    }

    public void setProfessionalFTE(Double professionalFTE) {
        this.professionalFTE = professionalFTE;
    }

    public Double getNonProfessionalFTE() {
        return nonProfessionalFTE;
    }

    public void setNonProfessionalFTE(Double nonProfessionalFTE) {
        this.nonProfessionalFTE = nonProfessionalFTE;
    }

    public Double getStudentFTE() {
        return studentFTE;
    }

    public void setStudentFTE(Double studentFTE) {
        this.studentFTE = studentFTE;
    }

    public Double getVolunteerFTE() {
        return volunteerFTE;
    }

    public void setVolunteerFTE(Double volunteerFTE) {
        this.volunteerFTE = volunteerFTE;
    }

    public Double getFunctDistAdmin() {
        return functDistAdmin;
    }

    public void setFunctDistAdmin(Double functDistAdmin) {
        this.functDistAdmin = functDistAdmin;
    }

    public Double getFunctDistProcessing() {
        return functDistProcessing;
    }

    public void setFunctDistProcessing(Double functDistProcessing) {
        this.functDistProcessing = functDistProcessing;
    }

    public Double getFunctDistPreservation() {
        return functDistPreservation;
    }

    public void setFunctDistPreservation(Double functDistPreservation) {
        this.functDistPreservation = functDistPreservation;
    }

    public Double getFunctDistReference() {
        return functDistReference;
    }

    public void setFunctDistReference(Double functDistReference) {
        this.functDistReference = functDistReference;
    }

    public Boolean getCollFociHistorical() {
        return collFociHistorical;
    }

    public void setCollFociHistorical(Boolean collFociHistorical) {
        this.collFociHistorical = collFociHistorical;
    }

    public Boolean getCollFociInstitutional() {
        return collFociInstitutional;
    }

    public void setCollFociInstitutional(Boolean collFociInstitutional) {
        this.collFociInstitutional = collFociInstitutional;
    }

    public Boolean getCollFociManuscript() {
        return collFociManuscript;
    }

    public void setCollFociManuscript(Boolean collFociManuscript) {
        this.collFociManuscript = collFociManuscript;
    }

    public Boolean getCollFociPersonalPapers() {
        return collFociPersonalPapers;
    }

    public void setCollFociPersonalPapers(Boolean collFociPersonalPapers) {
        this.collFociPersonalPapers = collFociPersonalPapers;
    }

    public Boolean getCollFociOther() {
        return collFociOther;
    }

    public void setCollFociOther(Boolean collFociOther) {
        this.collFociOther = collFociOther;
    }

    public String getMajorSubjectAreas() {
		if (this.majorSubjectAreas != null) {
			return this.majorSubjectAreas;
		} else {
			return "";
		}
	}

    public void setMajorSubjectAreas(String majorSubjectAreas) {
        this.majorSubjectAreas = majorSubjectAreas;
    }

    public Double getPercentageOffSite() {
        return percentageOffSite;
    }

    public void setPercentageOffSite(Double percentageOffSite) {
        this.percentageOffSite = percentageOffSite;
    }

    public Integer getNetUsableArea() {
        return netUsableArea;
    }

    public void setNetUsableArea(Integer netUsableArea) {
        this.netUsableArea = netUsableArea;
    }

    public Integer getAdministrationOffices() {
        return administrationOffices;
    }

    public void setAdministrationOffices(Integer administrationOffices) {
        this.administrationOffices = administrationOffices;
    }

    public Integer getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(Integer classrooms) {
        this.classrooms = classrooms;
    }

    public Integer getCollectionsStorage() {
        return collectionsStorage;
    }

    public void setCollectionsStorage(Integer collectionsStorage) {
        this.collectionsStorage = collectionsStorage;
    }

    public Integer getReadingRoom() {
        return readingRoom;
    }

    public void setReadingRoom(Integer readingRoom) {
        this.readingRoom = readingRoom;
    }

}
