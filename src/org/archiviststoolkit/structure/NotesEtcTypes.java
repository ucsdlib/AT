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
 * Date: Jun 23, 2006
 * Time: 11:22:28 AM
 */

package org.archiviststoolkit.structure;

import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.importer.ImportUtils;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.model.UnsupportedRepeatingDataTypeException;
import org.archiviststoolkit.model.RepositoryNotesDefaultValues;
import org.archiviststoolkit.model.ArchDescriptionNotes;
import org.archiviststoolkit.ApplicationFrame;
import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.archiviststoolkit.structure.EAD.Abstract;
import org.archiviststoolkit.structure.EAD.Accessrestrict;
import org.archiviststoolkit.structure.EAD.Accruals;
import org.archiviststoolkit.structure.EAD.Acqinfo;
import org.archiviststoolkit.structure.EAD.Altformavail;
import org.archiviststoolkit.structure.EAD.Appraisal;
import org.archiviststoolkit.structure.EAD.Arrangement;
import org.archiviststoolkit.structure.EAD.Bibliography;
import org.archiviststoolkit.structure.EAD.Bioghist;
import org.archiviststoolkit.structure.EAD.Chronlist;
import org.archiviststoolkit.structure.EAD.Custodhist;
import org.archiviststoolkit.structure.EAD.Daodesc;
import org.archiviststoolkit.structure.EAD.Dimensions;
import org.archiviststoolkit.structure.EAD.Fileplan;
import org.archiviststoolkit.structure.EAD.Index;
import org.archiviststoolkit.structure.EAD.Langmaterial;
import org.archiviststoolkit.structure.EAD.Legalstatus;
import org.archiviststoolkit.structure.EAD.Materialspec;
import org.archiviststoolkit.structure.EAD.Note;
import org.archiviststoolkit.structure.EAD.Odd;
import org.archiviststoolkit.structure.EAD.Originalsloc;
import org.archiviststoolkit.structure.EAD.Otherfindaid;
import org.archiviststoolkit.structure.EAD.Physdesc;
import org.archiviststoolkit.structure.EAD.Physfacet;
import org.archiviststoolkit.structure.EAD.Phystech;
import org.archiviststoolkit.structure.EAD.Prefercite;
import org.archiviststoolkit.structure.EAD.Processinfo;
import org.archiviststoolkit.structure.EAD.Relatedmaterial;
import org.archiviststoolkit.structure.EAD.Scopecontent;
import org.archiviststoolkit.structure.EAD.Separatedmaterial;
import org.archiviststoolkit.structure.EAD.Userestrict;
import org.netbeans.spi.wizard.DeferredWizardResult;

@ExcludeFromDefaultValues
public class NotesEtcTypes extends DomainObject {

	public static final String PROPERTYNAME_REPEATING_DATA_NAME = "notesEtcName";
	public static final String PROPERTYNAME_REPEATING_DATA_LABEL = "notesEtcLabel";
	public static final String PROPERTYNAME_DEFAULT_TITLE = "defaultTitle";
	public static final String PROPERTYNAME_CLASS_NAME = "className";
	public static final String PROPERTYNAME_REPEATING_DATA_TYPE = "repeatingDataType";
	public static final String PROPERTYNAME_ALLOWS_MULTI_PART = "allowsMultiPart";
	public static final String PROPERTYNAME_INCLUDE_IN_DIGITAL_OBJECTS = "includeInDigitalObjects";

	public static final String DATA_TYPE_NOTE = "Note";
	public static final String DATA_TYPE_BIBLIOGRAPHY = "Bibliography";
	public static final String DATA_TYPE_INDEX = "Index";
	public static final String DATA_TYPE_CHRONOLOGY = "Chronology";
	public static final String DATA_TYPE_EXTERNAL_REFERENCE = "External Document";
	public static final String DATA_TYPE_LIST_ORDERED = "List: ordered";
	public static final String DATA_TYPE_LIST_DEFINITION = "List: definition";
	public static final String DATA_TYPE_LIST_TEXT = "Text";


	public static final String CANNONICAL_NAME_CONDITIONS_GIVERNING_ACCESS = "Conditions Governing Access note";
	public static final String CANNONICAL_NAME_SCOPE_CONTENT = "Scope and Contents note";
	public static final String CANNONICAL_NAME_GENERAL_PHYSICAL_DESCRIPTION = "General Physical Description note";
	public static final String CANNONICAL_NAME_GENERAL_NOTE = "General note";
	public static final String CANNONICAL_NAME_ARRANGEMENT = "Arrangement note";
	public static final String CANNONICAL_NAME_ABSTRACT = "Abstract";
	public static final String CANNONICAL_NAME_LOCATION_NOTE = "Location note";
	public static final String CANNONICAL_NAME_LEGAL_STATUS_NOTE = "Legal Status note";
	public static final String CANNONICAL_NAME_LANGUAGE_OF_MATERIALS = "Language of Materials note";
	public static final String CANNONICAL_NAME_DIMENSIONS = "Dimensions note";
	public static final String CANNONICAL_NAME_MATERIAL_SPECIFIC_DETAILS = "Material Specific Details note";
	public static final String CANNONICAL_NAME_PHYSICAL_FACET = "Physical Facet note";

	private Long notesEtcTypeId;
	@IncludeInApplicationConfiguration(1)
	@StringLengthValidationRequried(255)

	private String notesEtcName = "";
	@StringLengthValidationRequried(255)
	@IncludeInApplicationConfiguration(2)
	private String notesEtcLabel = "";
	private String className = "";
	private String repeatingDataType = "";
	private Boolean embeded = false;
	private Boolean allowsMultiPart = false;
	private Boolean includeInDigitalObjects = false;
	private Set<RepositoryNotesDefaultValues> noteDefaultValues = new HashSet<RepositoryNotesDefaultValues>();
	private Set<ArchDescriptionNotes> notes = new HashSet<ArchDescriptionNotes>();

	public Long getNotesEtcTypeId() {
		return notesEtcTypeId;
	}

	public void setNotesEtcTypeId(Long notesEtcTypeId) {
		this.notesEtcTypeId = notesEtcTypeId;
	}

	public String getNotesEtcName() {
		if (this.notesEtcName != null) {
			return this.notesEtcName;
		} else {
			return "";
		}
	}

	public void setNotesEtcName(String notesEtcName) {
		this.notesEtcName = notesEtcName;
	}

	public String getNotesEtcLabel() {
		if (this.notesEtcLabel != null) {
			return this.notesEtcLabel;
		} else {
			return "";
		}
	}

	public void setNotesEtcLabel(String notesEtcLabel) {
		this.notesEtcLabel = notesEtcLabel;
	}

	public String getClassName() {
		if (this.className != null) {
			return this.className;
		} else {
			return "";
		}
	}

	public void setClassName(String className) {
		this.className = className;
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

	public String toString() {
		return this.getNotesEtcLabel();
	}

	/**
	 * @return Returns the identifier.
	 */
	public Long getIdentifier() {
		return this.getNotesEtcTypeId();
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(Long identifier) {
		this.setNotesEtcTypeId(identifier);
	}

	public Boolean getEmbeded() {
		return embeded;
	}

	public void setEmbeded(Boolean embeded) {
		this.embeded = embeded;
	}

	public Boolean getAllowsMultiPart() {
		return allowsMultiPart;
	}

	public void setAllowsMultiPart(Boolean allowsMultiPart) {
		this.allowsMultiPart = allowsMultiPart;
	}

	public Boolean getIncludeInDigitalObjects() {
		return includeInDigitalObjects;
	}

	public void setIncludeInDigitalObjects(Boolean includeInDigitalObjects) {
		this.includeInDigitalObjects = includeInDigitalObjects;
	}

	public Set<RepositoryNotesDefaultValues> getNoteDefaultValues() {
		return noteDefaultValues;
	}

	public void setNoteDefaultValues(Set<RepositoryNotesDefaultValues> noteDefaultValues) {
		this.noteDefaultValues = noteDefaultValues;
	}


	public Set<ArchDescriptionNotes> getNotes() {
		return notes;
	}

	public void setNotes(Set<ArchDescriptionNotes> notes) {
		this.notes = notes;
	}
}
