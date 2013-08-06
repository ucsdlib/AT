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
 * Linker class for Assessments and Resources
 *
 * @author: Nathan Stevens
 * Date: May 1, 2009
 * Time: 9:46:05 AM
 */
@ExcludeFromDefaultValues
public class AssessmentsResources extends DomainObject implements Serializable, Comparable {
    public static final String PROPERTYNAME_ASSESSMENT_IDENTIFIER    = "assessmentIdentifier";
	public static final String PROPERTYNAME_RESOURCE_IDENTIFIER    = "resourceIdentifier";
	public static final String PROPERTYNAME_RESOURCE_TITLE    = "resourceTitle";
	public static final String PROPERTYNAME_RESOURCE    = "resource";
	public static final String PROPERTYNAME_ASSESSMENT    = "assessment";

	private Long AssessmentsResourcesId = null;

	@IncludeInApplicationConfiguration(1)
	private Resources resource;

	@IncludeInApplicationConfiguration
	private Assessments assessment;

	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public AssessmentsResources() {}

	/**
	 * Full constructor;
	 */
	public AssessmentsResources(Resources resource, Assessments assesment) {
		this.setResource(resource);
		this.setAssessment(assesment);
	}

	// ********************** Accessor Methods ********************** //


	public Long getIdentifier() {
		return this.getAssessmentsResourcesId();
	}

	public void setIdentifier(Long identifier) {
		this.setAssessmentsResourcesId(identifier);
	}

	public Assessments getAssessment() {
		return assessment;
	}

	public void setAssessment(Assessments assessment) {
		this.assessment = assessment;
	}

	public Long getAssessmentsResourcesId() {
		return AssessmentsResourcesId;
	}

	public void setAssessmentsResourcesId(Long AssessmentsResourcesId) {
		this.AssessmentsResourcesId = AssessmentsResourcesId;
	}

	public Resources getResource() {
		return resource;
	}

	public void setResource(Resources resource) {
		this.resource = resource;
	}

    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_RESOURCE_IDENTIFIER)
	public String getResourceIdentifier() {
		if (this.getResource() != null) {
			return this.getResource().getResourceIdentifier();
		} else {
			return "";
		}
	}

    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_RESOURCE_TITLE)
	public String getResourceTitle() {
		if (this.getResource() != null) {
			return this.getResource().getTitle();
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
