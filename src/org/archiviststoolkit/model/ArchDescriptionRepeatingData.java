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
 */

package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.SequencedObject;
import org.archiviststoolkit.structure.*;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;

@ExcludeFromDefaultValues
public abstract class ArchDescriptionRepeatingData extends SequencedObject implements Serializable, Comparable {

	public static final String PROPERTYNAME_REPEATING_DATA_TYPE = "repeatingDataType";
	public static final String PROPERTYNAME_TITLE = "title";
	public static final String PROPERTYNAME_EAD_INGEST_PROBLEMS = "eadIngestProblem";
	public static final String PROPERTYNAME_TYPE = "type";
	public static final String PROPERTYNAME_CONTENT = "content";
	public static final String PROPERTYNAME_PERSISTENT_ID    = "persistentId";
	public static final String PROPERTYNAME_RESOURCE    = "resource";
	public static final String PROPERTYNAME_RESOURCE_COMPONENT    = "resourceComponent";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long archDescriptionRepeatingDataId = null;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String repeatingDataType = "";

	@IncludeInApplicationConfiguration(2)
	@StringLengthValidationRequried(255)
	private String title = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private String eadIngestProblem = "";

	private ResourcesComponents resourceComponent;

	private Resources resource;

	private DigitalObjects digitalObject;

	private Accessions accession;
	private ArchDescriptionNotes parentNote;
	private Set<ArchDescriptionRepeatingData> children = new TreeSet<ArchDescriptionRepeatingData>();
	private String persistentId = "";


	/**
	 * No-arg constructor for JavaBean tools.
	 */
	ArchDescriptionRepeatingData() {
	}

	/**
	 * Full constructor;
	 */

	public ArchDescriptionRepeatingData(ArchDescription archDescription) {
		linkArchDescription(archDescription);
	}

	public ArchDescriptionRepeatingData(String repeatingDataType) {
		this.repeatingDataType = repeatingDataType;
	}

	public ArchDescriptionRepeatingData(ArchDescription archDescription, String title, String repeatingDataType) {
		linkArchDescription(archDescription);
		this.title = title;
		this.repeatingDataType = repeatingDataType;
	}

	public ArchDescriptionRepeatingData(ArchDescription archDescription, String repeatingDataType) {
		linkArchDescription(archDescription);
		this.repeatingDataType = repeatingDataType;
	}

	public ArchDescriptionRepeatingData(ArchDescription archDescription, String title, String repeatingDataType, Integer sequenceNumber) {
		linkArchDescription(archDescription);
		this.setSequenceNumber(sequenceNumber);
		this.title = title;
		this.repeatingDataType = repeatingDataType;
	}

	public void linkArchDescription(ArchDescription archDescription) {
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

	public ArchDescriptionRepeatingData(ArchDescriptionNotes archDescriptionNote, String repeatingDataType) {
		this.parentNote = archDescriptionNote;
		this.repeatingDataType = repeatingDataType;
	}


	public Long getIdentifier() {
		return this.getArchDescriptionRepeatingDataId();
	}

	public void setIdentifier(Long identifier) {
		this.setArchDescriptionRepeatingDataId(identifier);
	}

	public Long getArchDescriptionRepeatingDataId() {
		return archDescriptionRepeatingDataId;
	}

	public void setArchDescriptionRepeatingDataId(Long archDescriptionRepeatingDataId) {
		this.archDescriptionRepeatingDataId = archDescriptionRepeatingDataId;
	}

	@IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_CONTENT, value=3)
	@ExcludeFromDefaultValues
	public abstract String getContent();

	@IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_TYPE, value=1)
	@ExcludeFromDefaultValues
	public abstract String getType();

	public abstract String getFullDescription();

	public String getTitle() {
		if (this.title != null) {
			return this.title;
		} else {
			return "";
		}
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRepeatingDataType() {
		if (this.repeatingDataType != null) {
			return this.repeatingDataType;
		} else {
			return "";
		}
	}

	public void setRepeatingDataType(String repeatingDataType) {
		this.repeatingDataType = repeatingDataType;
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

	public String getEadIngestProblem() {
		if (this.eadIngestProblem != null) {
			return this.eadIngestProblem;
		} else {
			return "";
		}
	}

	public void setEadIngestProblem(String eadIngestProblem) {
		this.eadIngestProblem = eadIngestProblem;
	}

	public DigitalObjects getDigitalObject() {
		return digitalObject;
	}

	public void setDigitalObject(DigitalObjects digitalObject) {
		this.digitalObject = digitalObject;
	}

	public static ArchDescriptionRepeatingData getInstance(ArchDescription archDescriptionModel,
														   Class repeatingDataClass,
														   NotesEtcTypes noteType) throws UnsupportedRepeatingDataTypeException {
		if (repeatingDataClass == ArchDescriptionNotes.class) {
			return new ArchDescriptionNotes(archDescriptionModel, NotesEtcTypes.DATA_TYPE_NOTE, noteType);
		} else if (repeatingDataClass == Bibliography.class) {
			return new Bibliography(archDescriptionModel);
		} else if (repeatingDataClass == Index.class) {
			return new Index(archDescriptionModel);
		} else if (repeatingDataClass == ExternalReference.class) {
			return new ExternalReference(archDescriptionModel);
		} else {
			throw new UnsupportedRepeatingDataTypeException(repeatingDataClass.getName());
		}
	}

	public static ArchDescriptionRepeatingData getInstance(ArchDescriptionNotes archDescriptionNotesModel,
														   Class repeatingDataClass) throws UnsupportedRepeatingDataTypeException {
		if (repeatingDataClass == ArchDescriptionNotes.class) {
			ArchDescriptionNotes newNote = new ArchDescriptionNotes(archDescriptionNotesModel, NotesEtcTypes.DATA_TYPE_NOTE);
			newNote.setBasic(true);
			return newNote;
		} else if (repeatingDataClass == ChronologyList.class) {
			return new ChronologyList(archDescriptionNotesModel);
		} else if (repeatingDataClass == ListOrdered.class) {
			return new ListOrdered(archDescriptionNotesModel);
		} else if (repeatingDataClass == ListDefinition.class) {
			return new ListDefinition(archDescriptionNotesModel);
		} else {
			throw new UnsupportedRepeatingDataTypeException(repeatingDataClass.getName());
		}
	}

	public Accessions getAccession() {
		return accession;
	}

	public void setAccession(Accessions accession) {
		this.accession = accession;
	}

	public ArchDescriptionNotes getParentNote() {
		return parentNote;
	}

	public void setParentNote(ArchDescriptionNotes parentNote) {
		this.parentNote = parentNote;
	}

	public Set<ArchDescriptionRepeatingData> getChildren() {
		return children;
	}

	public void setChildren(Set<ArchDescriptionRepeatingData> children) {
		this.children = children;
	}

	public void addRepeatingData(ArchDescriptionRepeatingData repeatingData) {
		if (repeatingData == null)
			throw new IllegalArgumentException("Can't add a null repeating value.");
		SequencedObject.adjustSequenceNumberForAdd(this.getChildren(), repeatingData);
		this.getChildren().add(repeatingData);

	}

	protected void removeRepeatingData(ArchDescriptionRepeatingData repeatingData) {
		if (repeatingData == null)
			throw new IllegalArgumentException("Can't remove a null repeating value.");

		getChildren().remove(repeatingData);
		SequencedObject.resequenceSequencedObjects(this.getChildren());
	}

	public String getPersistentId() {
		if (this.persistentId != null) {
			return this.persistentId;
		} else {
			return "";
		}
	}

	public void setPersistentId(String persistentId) {
		this.persistentId = persistentId;
	}
}
