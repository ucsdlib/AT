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
 * @author: Nathan Stevens
 * Date: May 1, 2009
 * Time: 10:10:48 AM
 */
@ExcludeFromDefaultValues
public class AssessmentsAccessions extends DomainObject implements Serializable, Comparable {
    public static final String PROPERTYNAME_ASSESSMENT_IDENTIFIER    = "assessmentIdentifier";
	public static final String PROPERTYNAME_ACCESSION_NUMBER    = "accessionNumber";
	public static final String PROPERTYNAME_ACCESSION_TITLE    = "accessionTitle";
	public static final String PROPERTYNAME_ACCESSION    = "accession";
	public static final String PROPERTYNAME_ASSESSMENT    = "assessment";

	private Long AssessmentsAccessionsId = null;

	@IncludeInApplicationConfiguration(1)
	private Accessions accession;

	@IncludeInApplicationConfiguration
	private Assessments assessment;

	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public AssessmentsAccessions() {}

	/**
	 * Full constructor;
	 */
	public AssessmentsAccessions(Accessions accessions, Assessments assesment) {
		this.setAccession(accessions);
		this.setAssessment(assesment);
	}

	// ********************** Accessor Methods ********************** //


	public Long getIdentifier() {
		return this.getAssessmentsAccessionsId();
	}

	public void setIdentifier(Long identifier) {
		this.setAssessmentsAccessionsId(identifier);
	}

	public Assessments getAssessment() {
		return assessment;
	}

	public void setAssessment(Assessments assessment) {
		this.assessment = assessment;
	}

	public Long getAssessmentsAccessionsId() {
		return AssessmentsAccessionsId;
	}

	public void setAssessmentsAccessionsId(Long AssessmentsAccessionsId) {
		this.AssessmentsAccessionsId = AssessmentsAccessionsId;
	}

	public Accessions getAccession() {
		return accession;
	}

	public void setAccession(Accessions accession) {
		this.accession = accession;
	}

    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_ACCESSION_NUMBER)
	public String getAccessionNumber() {
		if (this.getAccession() != null) {
			return this.getAccession().getAccessionNumber();
		} else {
			return "";
		}
	}

    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_ACCESSION_TITLE)
	public String getAccessionTitle() {
		if (this.getAccession() != null) {
			return this.getAccession().getTitle();
		} else {
			return "";
		}
	}

    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_ASSESSMENT_IDENTIFIER)
	public Long getAssessmentIdentifier() {
		if (this.getAssessment() != null) {
			return this.getAssessment().getIdentifier();
		} else {
			return null;
		}
	}
}
