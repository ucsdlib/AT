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

import java.util.Set;

public abstract class ArchDescriptionStructuredData extends ArchDescriptionRepeatingData {

	public static final String PROPERTYNAME_NOTE = "note";
  public static final String PROPERTYNAME_ARCH_DESCRIPTION_NOTE_INTERNAL_ONLY = "internalOnly";

  @IncludeInApplicationConfiguration
	private String note;

    @IncludeInApplicationConfiguration
    private Boolean internalOnly = false;

  /**
	 * No-arg constructor for JavaBean tools.
	 */
	public ArchDescriptionStructuredData() {
	}

	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public ArchDescriptionStructuredData(String repeatingDataType) {
		super(repeatingDataType);
	}

	protected ArchDescriptionStructuredData(ArchDescriptionNotes archDescriptionNote, String repeatingDataType) {
		super(archDescriptionNote, repeatingDataType);
	}

	public abstract void addItem(ArchDescriptionStructuredDataItems item);

	public abstract Set<ArchDescriptionStructuredDataItems> getItems();
	/**
	 * Full constructor;
	 */
	public ArchDescriptionStructuredData(ArchDescription archDescription) {
		super(archDescription);
	}

	public String getContent() {
		return getNote();
	}

	public String getFullDescription() {
		return getRepeatingDataType() + ": " + getContent();
	}

	public ArchDescriptionStructuredData(ArchDescription archDescription, String repeatingDataType) {
		super(archDescription, repeatingDataType);
	}

	public ArchDescriptionStructuredData(ArchDescription archDescription, String title, String repeatingDataType, String description) {
		super(archDescription, title, repeatingDataType);
		this.note = description;
	}


	public String getNote() {
		if (this.note != null) {
			return this.note;
		} else {
			return "";
		}
	}

	public void setNote(String note) {
			this.note = note;
	}

  public Boolean getInternalOnly() {
		return internalOnly;
	}

	public void setInternalOnly(Boolean internalOnly) {
		this.internalOnly = internalOnly;
	}
}
