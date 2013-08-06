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

import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.NotesEtcTypes;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.report.ReportUtils;
import org.archiviststoolkit.util.StringHelper;

import java.io.Serializable;

import java.util.Collection;

@ExcludeFromDefaultValues
public class ArchDescriptionNotes  extends ArchDescriptionRepeatingData implements Serializable, Comparable {

	public static final String PROPERTYNAME_ARCH_DESCRIPTION_NOTE_TYPE    = "notesEtcType";
	public static final String PROPERTYNAME_ARCH_DESCRIPTION_NOTE_CONTENT    = "noteContent";
	public static final String PROPERTYNAME_ARCH_DESCRIPTION_NOTE_INTERNAL_ONLY    = "internalOnly";
	public static final String PROPERTYNAME_ARCH_DESCRIPTION_MULTI_PART    = "multiPart";
	public static final String PROPERTYNAME_ARCH_DESCRIPTION_BASIC    = "basic";

	private NotesEtcTypes notesEtcType;

//	@IncludeInApplicationConfiguration
//	@ExcludeFromDefaultValues
//	private String noteType = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private String noteContent = "";

	@IncludeInApplicationConfiguration
	private Boolean internalOnly = false;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Boolean multiPart = false;

	private Boolean basic = false;

	/**
	 * No-arg constructor for JavaBean tools.
	 */
	ArchDescriptionNotes() {}

	/**
	 * Full constructor;
	 */

	public ArchDescriptionNotes(ArchDescription archDescription) {
		super(archDescription);
	}

	public ArchDescriptionNotes(ArchDescription archDescription,
								String repeatingDataType,
								NotesEtcTypes noteEtcType) {
		super(archDescription, repeatingDataType);
		this.notesEtcType = noteEtcType;
	}

	public ArchDescriptionNotes(ArchDescription archDescription,
								String title,
								String repeatingDataType,
								NotesEtcTypes noteEtcType,
								String noteContent) {
		super(archDescription, title, repeatingDataType);
		this.notesEtcType = noteEtcType;
		this.noteContent = noteContent;
	}

	public ArchDescriptionNotes(ArchDescription archDescription,
								String title,
								String repeatingDataType,
								Integer sequenceOrder,
								NotesEtcTypes noteEtcType,
								String noteContent) {
		super(archDescription, title, repeatingDataType, sequenceOrder);
		this.notesEtcType = noteEtcType;
		this.noteContent = noteContent;
	}

	public ArchDescriptionNotes(ArchDescriptionNotes archDescriptionNote, String repeatingDataType) {
		super(archDescriptionNote, repeatingDataType);
	}

	public String getContent() {
		return getNoteContent();
	}

	public String getType() {
		return getNoteType();
	}

	public String getFullDescription() {
		return getNoteType() + ": " + getNoteContent();
	}

	public String getNoteType() {
		if (basic) {
			return "Text";
		} else if (getNotesEtcType() == null) {
            return "";
        } else {
            //return getNotesEtcType().getNotesEtcName();
			return getNotesEtcType().getNotesEtcLabel();
        }
	}

//	public void setNoteType(String noteType) {
//		this.noteType = noteType;
//	}
//
	public String getNoteContent() {
	if (this.noteContent != null) {
        if(ReportUtils.printingJR) {
             return StringHelper.tagRemover(this.noteContent);
        }
        else {
            return this.noteContent;
        }
	} else {
		return "";
	}
}

	public void setNoteContent(String noteContent) {
//		String oldValue = this.noteContent;
		this.noteContent = noteContent;
//		firePropertyChange(PROPERTYNAME_ARCH_DESCRIPTION_NOTE_CONTENT, oldValue, this.noteContent);
	}

	public Boolean getInternalOnly() {
		return internalOnly;
	}

	public void setInternalOnly(Boolean internalOnly) {
		this.internalOnly = internalOnly;
	}

	public Boolean getMultiPart() {
		return multiPart;
	}

	public void setMultiPart(Boolean multiPart) {
		this.multiPart = multiPart;
	}

	public Boolean getBasic() {
		return basic;
	}

	public void setBasic(Boolean basic) {
		this.basic = basic;
	}

	public void removeRelatedObject(DomainObject domainObject) {
		if (domainObject instanceof ArchDescriptionRepeatingData) {
			removeRepeatingData((ArchDescriptionRepeatingData) domainObject);
		}
	}

	public Collection getRelatedCollection(DomainObject domainObject) {
		if (domainObject instanceof ArchDescriptionRepeatingData) {
			return getChildren();
		} else {
			return null;
		}
	}

	public NotesEtcTypes getNotesEtcType() {
		return notesEtcType;
	}

	public void setNotesEtcType(NotesEtcTypes notesEtcType) {
		this.notesEtcType = notesEtcType;
	}
}
