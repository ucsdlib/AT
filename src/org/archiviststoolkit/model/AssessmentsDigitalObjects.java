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
 * Time: 10:31:56 AM
 */
@ExcludeFromDefaultValues
public class AssessmentsDigitalObjects extends DomainObject implements Serializable, Comparable {
    public static final String PROPERTYNAME_ASSESSMENT_IDENTIFIER    = "assessmentIdentifier";
	public static final String PROPERTYNAME_DIGITALOBJECT_IDENTIFIER    = "digitalObjectIdentifier";
	public static final String PROPERTYNAME_DIGITALOBJECT_LABEL    = "digitalObjectLabel";
	public static final String PROPERTYNAME_DIGITALOBJECT    = "digitalObject";
	public static final String PROPERTYNAME_ASSESSMENT    = "assessment";

	private Long AssessmentsDigitalObjectsId = null;

	@IncludeInApplicationConfiguration(1)
	private DigitalObjects digitalObject;

	@IncludeInApplicationConfiguration
	private Assessments assessment;

	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public AssessmentsDigitalObjects() {}

	/**
	 * Full constructor;
	 */
	public AssessmentsDigitalObjects(DigitalObjects digitalObject, Assessments assesment) {
		this.setDigitalObject(digitalObject);
		this.setAssessment(assesment);
	}

	// ********************** Accessor Methods ********************** //


	public Long getIdentifier() {
		return this.getAssessmentsDigitalObjectsId();
	}

	public void setIdentifier(Long identifier) {
		this.setAssessmentsDigitalObjectsId(identifier);
	}

	public Assessments getAssessment() {
		return assessment;
	}

	public void setAssessment(Assessments assessment) {
		this.assessment = assessment;
	}

	public Long getAssessmentsDigitalObjectsId() {
		return AssessmentsDigitalObjectsId;
	}

	public void setAssessmentsDigitalObjectsId(Long AssessmentsDigitalObjectsId) {
		this.AssessmentsDigitalObjectsId = AssessmentsDigitalObjectsId;
	}

	public DigitalObjects getDigitalObject() {
		return digitalObject;
	}

	public void setDigitalObject(DigitalObjects digitalObject) {
		this.digitalObject = digitalObject;
	}

    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_DIGITALOBJECT_IDENTIFIER)
	public String getDigitalObjectIdentifier() {
		if (this.getDigitalObject() != null) {
			return this.getDigitalObject().getMetsIdentifier();
		} else {
			return "";
		}
	}

    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_DIGITALOBJECT_LABEL)
	public String getDigitalObjectLabel() {
		if (this.getDigitalObject() != null) {
			return this.getDigitalObject().getObjectLabel();
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
