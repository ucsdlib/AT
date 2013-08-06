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

import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.IncludeMethodInApplicationConfiguration;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;


@ExcludeFromDefaultValues
public class ChronologyItems extends ArchDescriptionStructuredDataItems {

	public static final String PROPERTYNAME_EVENT_DATE = "eventDate";
	public static final String PROPERTYNAME_EVENT_DESCRIPTION = "eventDescription";

	@IncludeInApplicationConfiguration(1)
	private String eventDate = "";

	private Set<Events> events = new HashSet<Events>();



	/**
	 * Creates a new instance of Bibliography
	 */
	public ChronologyItems() {
	}

	public ChronologyItems(DomainObject parent) {
		this.structuredDataParent = (ChronologyList)parent;
	}

	public String getEventDate() {
		if (this.eventDate != null) {
			return this.eventDate;
		} else {
			return "";
		}
	}

	public void setEventDate(String eventDate) {
		Object oldValue = getEventDate();
		this.eventDate = eventDate;
		firePropertyChange(PROPERTYNAME_EVENT_DATE, oldValue, eventDate);
	}

	public Set<Events> getEvents() {
		return events;
	}

	public void setEvents(Set<Events> events) {
		this.events = events;
	}

	public void addEvent(Events event) {
		SequencedObject.adjustSequenceNumberForAdd(this.getEvents(), event);
		this.events.add(event);
	}

	protected void removeEvent(Events event) {
		if (event == null)
			throw new IllegalArgumentException("Can't remove a null index item.");

		getEvents().remove(event);
		SequencedObject.resequenceSequencedObjects(this.getEvents());
	}

	public void removeRelatedObject(DomainObject domainObject) {
		if (domainObject instanceof Events) {
			removeEvent((Events) domainObject);
		}
	}

	public Collection getRelatedCollection(Class clazz) {
		if (clazz == Events.class) {
			return getEvents();
		} else {
			return null;
		}
	}

	@IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_EVENT_DESCRIPTION, value = 2)
	public String getEventDescription() {
		int numberOfEvents = getEvents().size();
		if (numberOfEvents == 0) {
			return "";
		} else if (numberOfEvents == 1) {
			return getEvents().iterator().next().getEventDescription();
		} else {
			return getEvents().iterator().next().getEventDescription() + " ...";
		}
	}

	public String toString() {
		int numberOfEvents = getEvents().size();
		if (numberOfEvents == 0) {
			return "";
		} else {
			return eventDate + ": " + getEvents().iterator().next().getEventDescription() + " ...";
		}
	}
}
