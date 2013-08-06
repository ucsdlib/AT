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

import org.archiviststoolkit.mydomain.SequencedObject;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.StringLengthValidationRequried;

import java.io.Serializable;


public abstract class SimpleRepeatableNotes  extends SequencedObject implements Serializable, Comparable {

	public static final String PROPERTYNAME_LABEL    = "label";
	public static final String PROPERTYNAME_NOTE_TEXT    = "noteText";


	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long simpleRepeatableNoteId = null;

	@IncludeInApplicationConfiguration(1)
	@StringLengthValidationRequried(255)
  private String label = "";

	@IncludeInApplicationConfiguration(2)
	@ExcludeFromDefaultValues
	private String noteText = "";

	// ********************** Accessor Methods ********************** //

	public Long getSimpleRepeatableNoteId() { return simpleRepeatableNoteId; }

	public String getLabel() {
		if (this.label != null) {
			return this.label;
		} else {
			return "";
		}
	}

	public void setSimpleRepeatableNoteLabel(String simpleRepatableNoteLabel) {
		this.label = simpleRepatableNoteLabel;
	}



	// ********************** Business Methods ********************** //

	/**
	 * Checks if the billing information is correct.
	 * <p>
	 * Check algorithm is implemented in subclasses.
	 *
	 * @return boolean
	 */

	public String getNoteText() {
		if (this.noteText != null) {
			return this.noteText;
		} else {
			return "";
		}
	}

	public void setNoteText(String noteText) {
		this.noteText = noteText;
	}

	public Long getIdentifier() {
		return this.getSimpleRepeatableNoteId();
	}

	public void setIdentifier(Long identifier) {
		this.setSimpleRepeatableNoteId(identifier);
	}

	public void setSimpleRepeatableNoteId(Long simpleRepatableNoteId) {
		this.simpleRepeatableNoteId = simpleRepatableNoteId;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
