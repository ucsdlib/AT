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
 * Created on July 19, 2005, 11:54 AM
 * @author leemandell
 *
 */

package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.swing.SortableTableModel;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.DefaultIncludeInSearchEditor;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.StringLengthValidationRequried;
import org.archiviststoolkit.exceptions.DuplicateLinkException;

import java.util.*;

import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.BasicEventList;

public class Subjects extends DomainObject {

	// Names of the Bound Bean Properties *************************************

	public static final String PROPERTYNAME_SUBJECT_TERM = "subjectTerm";
	public static final String PROPERTYNAME_SUBJECT_TERM_TYPE = "subjectTermType";
    public static final String PROPERTYNAME_SUBJECT_TERM1 = "subjectTerm1";
	public static final String PROPERTYNAME_SUBJECT_TERM_TYPE1 = "subjectTermType1";
    public static final String PROPERTYNAME_SUBJECT_TERM2 = "subjectTerm2";
	public static final String PROPERTYNAME_SUBJECT_TERM_TYPE2 = "subjectTermType2";
    public static final String PROPERTYNAME_SUBJECT_TERM3 = "subjectTerm3";
	public static final String PROPERTYNAME_SUBJECT_TERM_TYPE3 = "subjectTermType3";
    public static final String PROPERTYNAME_SUBJECT_TERM4 = "subjectTerm4";
	public static final String PROPERTYNAME_SUBJECT_TERM_TYPE4 = "subjectTermType4";
    public static final String PROPERTYNAME_SUBJECT_TERM5 = "subjectTerm5";
	public static final String PROPERTYNAME_SUBJECT_TERM_TYPE5 = "subjectTermType5";
    public static final String PROPERTYNAME_DISPLAY_FORM = "displayForm";
	public static final String PROPERTYNAME_SUBJECT_SOURCE = "subjectSource";
	public static final String PROPERTYNAME_SUBJECT_SCOPE_NOTE = "subjectScopeNote";
	public static final String PROPERTYNAME_SUBJECT_ID = "subjectId";
	public static final String PROPERTYNAME_STRING_SUBJECT_ID = "stringSubjectId"; // this stores the string subject ID

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long subjectId;

	@IncludeInApplicationConfiguration (1)
	@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(150)
	private String subjectTerm = "";

	@IncludeInApplicationConfiguration(2)
	@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(50)
	private String subjectTermType = "";

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(150)
	private String subjectTerm1 = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(50)
	private String subjectTermType1 = "";

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(150)
	private String subjectTerm2 = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(50)
	private String subjectTermType2 = "";

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(150)
	private String subjectTerm3 = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(50)
	private String subjectTermType3 = "";

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(150)
	private String subjectTerm4 = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(50)
	private String subjectTermType4 = "";

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(150)
	private String subjectTerm5 = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(50)
	private String subjectTermType5 = "";

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(150)
	private String subjectTerm6 = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(50)
	private String subjectTermType6 = "";
    
	@IncludeInApplicationConfiguration(3)
	@DefaultIncludeInSearchEditor
	@StringLengthValidationRequried(100)
	private String subjectSource = "";

	@IncludeInApplicationConfiguration
	@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	private String subjectScopeNote = "";

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private String displayForm = "";

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private String stringSubjectId = "";

	private static SortedList subjectsGlazedList;
	/** event list that hosts the issues */
	private static EventList subjectEventList = new BasicEventList();
	private static ArrayList subjectList;

	private Set<ArchDescriptionSubjects> archDescriptionSubjects = new HashSet<ArchDescriptionSubjects>();

	/**
	 * Creates a new instance of Subject
	 */
	public Subjects() {
	}

	public Subjects(String subjectTerm) {
		this.subjectTerm = subjectTerm;
	}

	/**
	 * @return Returns the identifier.
	 */
	public Long getIdentifier() {
		return getSubjectId();
	}

	public String toString() {
		return this.subjectTerm;
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(Long identifier) {
		this.setSubjectId(identifier);
	}

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectTerm() {
		if (this.subjectTerm != null) {
			return this.subjectTerm;
		} else {
			return "";
		}
	}

	public void setSubjectTerm(String subjectTerm) {
//		Object oldValue = getSubjectTerm();
		this.subjectTerm = subjectTerm;
//		firePropertyChange(PROPERTYNAME_SUBJECT_TERM, oldValue, subjectTerm);

	}

	public String getSubjectTermType() {
		if (this.subjectTermType != null) {
			return this.subjectTermType;
		} else {
			return "";
		}
	}

	public void setSubjectTermType(String subjectTermType) {
//		Object oldValue = getSubjectTermType();
		this.subjectTermType = subjectTermType;
//		firePropertyChange(PROPERTYNAME_SUBJECT_TERM_TYPE, oldValue, subjectTermType);
	}

	public String getSubjectSource() {
		if (this.subjectSource != null) {
			return this.subjectSource;
		} else {
			return "";
		}
	}

	public void setSubjectSource(String subjectSource) {
//		Object oldValue = getSubjectSource();
		this.subjectSource = subjectSource;
//		firePropertyChange(PROPERTYNAME_SUBJECT_SOURCE, oldValue, subjectSource);
	}

	public String getSubjectScopeNote() {
		if (this.subjectScopeNote != null) {
			return this.subjectScopeNote;
		} else {
			return "";
		}
	}

	public void setSubjectScopeNote(String subjectScopeNote) {
//		Object oldValue = getSubjectScopeNote();
		this.subjectScopeNote = subjectScopeNote;
//		firePropertyChange(PROPERTYNAME_SUBJECT_SCOPE_NOTE, oldValue, subjectScopeNote);
	}

	public Set<ArchDescriptionSubjects> getArchDescriptionSubjects() {
		return archDescriptionSubjects;
	}

	public void setArchDescriptions(Set<ArchDescriptionSubjects> archDescriptionSubjects) {
		this.archDescriptionSubjects = archDescriptionSubjects;
	}

	public void addArchDesctiption(ArchDescription archDescription) throws DuplicateLinkException {
		if (containsArchDescriptionLink(archDescription)) {
			throw new DuplicateLinkException(archDescription.toString());
		} else {
			getArchDescriptionSubjects().add(new ArchDescriptionSubjects(this, archDescription));
		}
	}

	private boolean containsArchDescriptionLink(ArchDescription archDescription) {
		for (ArchDescriptionSubjects link: getArchDescriptionSubjects()) {
			if (archDescription instanceof Resources &&
					link.getResource() != null &&
					link.getResource().equals(archDescription)) {
				return true;
			} else if (archDescription instanceof ResourcesComponents &&
					link.getResourceComponent() != null &&
					link.getResourceComponent().equals(archDescription)) {
				return true;
			} else if (archDescription instanceof Accessions &&
					link.getAccession() != null &&
					link.getAccession().equals(archDescription)) {
				return true;
			} else if (archDescription instanceof DigitalObjects &&
					link.getDigitalObject() != null &&
					link.getDigitalObject().equals(archDescription)) {
				return true;
			}
		}
		return false;
	}


	//convienence methods for getting just accessions and just resources

	public Set getResources () {
		Set<Resources> resources = new TreeSet<Resources>();
		ResourcesDAO access = new ResourcesDAO();
		for(ArchDescriptionSubjects archDescriptionSubject: archDescriptionSubjects) {
			if (archDescriptionSubject.getResource() != null) {
				resources.add(archDescriptionSubject.getResource());
			} else if (archDescriptionSubject.getResourceComponent() != null) {
				resources.add(access.findResourceByComponent(archDescriptionSubject.getResourceComponent()));
			}
		}
		return resources;
	}

	public Set getAccessions () {
		Set<Accessions> accessions = new TreeSet<Accessions>();
		for(ArchDescriptionSubjects archDescriptionSubject: archDescriptionSubjects) {
			if (archDescriptionSubject.getAccession() != null) {
				accessions.add(archDescriptionSubject.getAccession());
			}
		}
		return accessions;
	}

    /**
     * Method to return digital objects which link to this subject
     *
     * @return Set containing any digital objects link to this subject
     */
    public Set getDigitalObjects () {
		Set<DigitalObjects> digitalObjects = new TreeSet<DigitalObjects>();
		for(ArchDescriptionSubjects archDescriptionSubject: archDescriptionSubjects) {
			if (archDescriptionSubject.getDigitalObject() != null) {
				digitalObjects.add(archDescriptionSubject.getDigitalObject());
			}
		}
		return digitalObjects;
	}

	public static void initSubjectLookupList() {
		DomainAccessObject access = new DomainAccessObjectImpl(Subjects.class);
		try {
			Collection subjects = access.findByNamedQuery("findAllSubjectsSorted");
			subjectList = new ArrayList(subjects);
			Collections.sort(subjectList);
			subjectEventList.addAll(subjects);
			subjectsGlazedList = new SortedList(subjectEventList);
		} catch (LookupException e) {
			e.printStackTrace();
		}
	}

	public static void addSubjectToLookupList(Subjects subject) {
		subjectList.add(subject);
		Collections.sort(subjectList);
		subjectEventList.add(subject);
	}

	public static void removeSubjectFromLookupList(Subjects subject) {
		subjectList.remove(subject);
		Collections.sort(subjectList);
		subjectEventList.remove(subject);
	}

	public static SortedList getSubjectsGlazedList() {
		return subjectsGlazedList;
	}

    public String getSubjectTerm1() {
        return subjectTerm1;
    }

    public void setSubjectTerm1(String subjectTerm1) {
        this.subjectTerm1 = subjectTerm1;
    }

    public String getSubjectTermType1() {
        return subjectTermType1;
    }

    public void setSubjectTermType1(String subjectTermType1) {
        this.subjectTermType1 = subjectTermType1;
    }

    public String getSubjectTerm2() {
        return subjectTerm2;
    }

    public void setSubjectTerm2(String subjectTerm2) {
        this.subjectTerm2 = subjectTerm2;
    }

    public String getSubjectTermType2() {
        return subjectTermType2;
    }

    public void setSubjectTermType2(String subjectTermType2) {
        this.subjectTermType2 = subjectTermType2;
    }

    public String getSubjectTerm3() {
        return subjectTerm3;
    }

    public void setSubjectTerm3(String subjectTerm3) {
        this.subjectTerm3 = subjectTerm3;
    }

    public String getSubjectTermType3() {
        return subjectTermType3;
    }

    public void setSubjectTermType3(String subjectTermType3) {
        this.subjectTermType3 = subjectTermType3;
    }

    public String getSubjectTerm4() {
        return subjectTerm4;
    }

    public void setSubjectTerm4(String subjectTerm4) {
        this.subjectTerm4 = subjectTerm4;
    }

    public String getSubjectTermType4() {
        return subjectTermType4;
    }

    public void setSubjectTermType4(String subjectTermType4) {
        this.subjectTermType4 = subjectTermType4;
    }

    public String getSubjectTerm5() {
        return subjectTerm5;
    }

    public void setSubjectTerm5(String subjectTerm5) {
        this.subjectTerm5 = subjectTerm5;
    }

    public String getSubjectTermType5() {
        return subjectTermType5;
    }

    public void setSubjectTermType5(String subjectTermType5) {
        this.subjectTermType5 = subjectTermType5;
    }

    public String getSubjectTerm6() {
        return subjectTerm6;
    }

    public void setSubjectTerm6(String subjectTerm6) {
        this.subjectTerm6 = subjectTerm6;
    }

    public String getSubjectTermType6() {
        return subjectTermType6;
    }

    public void setSubjectTermType6(String subjectTermType6) {
        this.subjectTermType6 = subjectTermType6;
    }

    public String getDisplayForm() {
        return displayForm;
    }

    public void setDisplayForm(String displayForm) {
        this.displayForm = displayForm;
    }

    public String getStringSubjectId() {
        return stringSubjectId;
    }

    public void setStringSubjectId(String stringSubjectId) {
        this.stringSubjectId = stringSubjectId;
    }
}
