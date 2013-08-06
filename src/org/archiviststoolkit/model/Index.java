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
import org.archiviststoolkit.mydomain.SequencedObject;
import org.archiviststoolkit.structure.NotesEtcTypes;

import java.util.*;


public class Index extends ArchDescriptionStructuredData {

	private SortedSet<ArchDescriptionStructuredDataItems> indexItems = new TreeSet<ArchDescriptionStructuredDataItems>();

	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public Index() {
		super(NotesEtcTypes.DATA_TYPE_INDEX);
	}

    public void addItem(ArchDescriptionStructuredDataItems item) {
        addIndexItem((IndexItems)item);
    }

    public Set<ArchDescriptionStructuredDataItems> getItems() {
        return indexItems;
    }

    /**
     * Full constructor;
	 * @param archDescription - the parent record to link to
	 */

	public Index(ArchDescription archDescription) {
        super(archDescription,NotesEtcTypes.DATA_TYPE_INDEX);
    }


	public String getType() {
		return NotesEtcTypes.DATA_TYPE_INDEX;
	}

	public SortedSet<ArchDescriptionStructuredDataItems> getIndexItems() {
	   return indexItems;
   }

	public void setIndexItems(SortedSet<ArchDescriptionStructuredDataItems> indexItems) {
		this.indexItems = indexItems;
	}

	public void addIndexItem(IndexItems indexItem) {
        SequencedObject.adjustSequenceNumberForAdd(this.getIndexItems(), indexItem);
		this.indexItems.add(indexItem);
	}

	protected void removeIndexItem(IndexItems indexItem) {
		if (indexItem == null)
			throw new IllegalArgumentException("Can't remove a null index item.");

		getIndexItems().remove(indexItem);
	}

	public void removeRelatedObject(DomainObject domainObject) {
		if (domainObject instanceof IndexItems) {
			removeIndexItem((IndexItems) domainObject);
		}
	}

	public Collection getRelatedCollection(Class clazz) {
		if (clazz == IndexItems.class) {
			return getIndexItems();
		} else {
			return null;
		}
	}
	
}
