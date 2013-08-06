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
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.StringLengthValidationRequried;

import java.io.Serializable;
import java.util.Collection;

@ExcludeFromDefaultValues
public class Events  extends SequencedObject implements Serializable, Comparable {

	public Events() {
	}

	public static final String PROPERTYNAME_EVENT_DESCRIPTION    = "eventDescription";


	@IncludeInApplicationConfiguration
	private Long eventId = null;

	@IncludeInApplicationConfiguration(1)
	private String eventDescription = "";

	public Events(ChronologyItems chronologyItem) {
		this.chronologyItem = chronologyItem;
	}

	private ChronologyItems chronologyItem;

	// ********************** Accessor Methods ********************** //

	public Long getEventId() { return eventId; }

	// ********************** Business Methods ********************** //

	/**
	 * Checks if the billing information is correct.
	 * <p>
	 * Check algorithm is implemented in subclasses.
	 *
	 * @return boolean
	 */

	public String getEventDescription() {
		if (this.eventDescription != null) {
			return this.eventDescription;
		} else {
			return "";
		}
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public Long getIdentifier() {
		return this.getEventId();
	}

	public void setIdentifier(Long identifier) {
		this.setEventId(identifier);
	}

	public void setEventId(Long simpleRepatableNoteId) {
		this.eventId = simpleRepatableNoteId;
	}

	public ChronologyItems getChronologyItem() {
		return chronologyItem;
	}

	public void setChronologyItem(ChronologyItems chronologyItem) {
		this.chronologyItem = chronologyItem;
	}
}
