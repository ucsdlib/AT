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
 */

package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;

import java.io.Serializable;


@ExcludeFromDefaultValues
public class ArchDescriptionSubjects  extends DomainObject implements Serializable, Comparable {

	public static final String PROPERTYNAME_SUBJECT_TERM = "subjectTerm";
	public static final String PROPERTYNAME_SUBJECT = "subject";

	@IncludeInApplicationConfiguration
	private Long archDescriptionSubjectsId = null;

	@IncludeInApplicationConfiguration(1)
	private Subjects subject;

	private Resources resource;
	private ResourcesComponents resourceComponent;
	private Accessions accession;
	private DigitalObjects digitalObject;

	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public ArchDescriptionSubjects() {}

	public String toString() {
		return getSubjectTerm();
	}

	/**
	 * Full constructor;
	 */
	public ArchDescriptionSubjects(Subjects subject, ArchDescription archDescription) {
		this.setSubject(subject);
		if (archDescription instanceof Resources) {
			this.resource = (Resources)archDescription;
		} else if (archDescription instanceof ResourcesComponents) {
			this.resourceComponent = (ResourcesComponents)archDescription;
		} else if (archDescription instanceof DigitalObjects) {
			this.digitalObject = (DigitalObjects)archDescription;
		} else if (archDescription instanceof Accessions) {
			this.accession = (Accessions)archDescription;
		}
	}

	// ********************** Accessor Methods ********************** //

	public Long getArchDescriptionSubjectsId() {
		return archDescriptionSubjectsId;
	}

	public void setArchDescriptionSubjectsId(Long archDescriptionSubjectsId) {
		this.archDescriptionSubjectsId = archDescriptionSubjectsId;
	}


	public Long getIdentifier() {
		return this.getArchDescriptionSubjectsId();
	}

	public void setIdentifier(Long identifier) {
		this.setArchDescriptionSubjectsId(identifier);
	}

	public Resources getResource() {
		return resource;
	}

	public void setResource(Resources resource) {
		this.resource = resource;
	}

	public void setSubject(Subjects subject) {
		this.subject = subject;
	}

	public Subjects getSubject() {
		return subject;
	}

	public String getSubjectTerm() {
		return subject.getSubjectTerm();
	}

	public String getSubjectSource() {
		return subject.getSubjectSource();
	}

	public ResourcesComponents getResourceComponent() {
		return resourceComponent;
	}

	public void setResourceComponent(ResourcesComponents resourceComponent) {
		this.resourceComponent = resourceComponent;
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

	public ArchDescription getArchDescription() {
		if (getAccession() != null) {
			return getAccession();
		} else if (getDigitalObject() != null) {
			return getDigitalObject();
		} else if (getResource() != null) {
			return getResource();
		} else if (getResourceComponent() != null) {
			return getResourceComponent();
		} else {
			return null;
		}
	}
}
