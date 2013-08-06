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
import org.archiviststoolkit.structure.NotesEtcTypes;

import java.util.*;


public class ChronologyList extends ArchDescriptionStructuredData {

	private SortedSet<ArchDescriptionStructuredDataItems> chronologyItems = new TreeSet<ArchDescriptionStructuredDataItems>();

	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public ChronologyList() {
		super(NotesEtcTypes.DATA_TYPE_CHRONOLOGY);
	}

    public void addItem(ArchDescriptionStructuredDataItems item) {
        addChronologyItem((ChronologyItems)item);
    }

    public Set<ArchDescriptionStructuredDataItems> getItems() {
        return chronologyItems;
    }

    /**
     * Full constructor;
     */
	public ChronologyList(ArchDescription archDescription) {
		super(archDescription, NotesEtcTypes.DATA_TYPE_CHRONOLOGY);
	}

	public ChronologyList(ArchDescriptionNotes archDescriptionNote) {
		super(archDescriptionNote, NotesEtcTypes.DATA_TYPE_CHRONOLOGY);
	}


	public String getType() {
		return NotesEtcTypes.DATA_TYPE_CHRONOLOGY;
	}

	public SortedSet<ArchDescriptionStructuredDataItems> getChronologyItems() {
		return chronologyItems;
	}

	public void setChronologyItems(SortedSet<ArchDescriptionStructuredDataItems> chronologyItems) {
		this.chronologyItems = chronologyItems;
	}

	public void addChronologyItem(ChronologyItems chronologyItem) {
        SequencedObject.adjustSequenceNumberForAdd(this.getChronologyItems(), chronologyItem);
        this.chronologyItems.add(chronologyItem);
	}

	protected void removeChronItem(ChronologyItems chronItem) {
		if (chronItem == null)
			throw new IllegalArgumentException("Can't remove a null index item.");

        getChronologyItems().remove(chronItem);
        SequencedObject.resequenceSequencedObjects(this.getChronologyItems());
    }

	public void removeRelatedObject(DomainObject domainObject) {
		if (domainObject instanceof ChronologyItems) {
			removeChronItem((ChronologyItems) domainObject);
		}
	}

	public Collection getRelatedCollection(Class clazz) {
		if (clazz == ChronologyItems.class) {
			return getChronologyItems();
		} else {
			return null;
		}
	}

	public String getContent() {
		int numberOfItems = getChronologyItems().size();
		if (numberOfItems == 0) {
			return "";
		} else if (numberOfItems == 1) {
			return getChronologyItems().iterator().next().toString();
		} else {
			return getChronologyItems().iterator().next().toString() + " ...";
		}
	}
}