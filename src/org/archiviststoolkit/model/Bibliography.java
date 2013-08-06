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


public class Bibliography extends ArchDescriptionStructuredData {

	private SortedSet<ArchDescriptionStructuredDataItems> bibItems = new TreeSet<ArchDescriptionStructuredDataItems>();

	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public Bibliography() {
		super(NotesEtcTypes.DATA_TYPE_BIBLIOGRAPHY);
	}

    public void addItem(ArchDescriptionStructuredDataItems item) {
        addBibItem((BibItems)item);
    }

    public Set<ArchDescriptionStructuredDataItems> getItems() {
        return bibItems;
    }

    /**
     * Full constructor;
	 * @param archDescription the parent record to link to
	 */

	public Bibliography(ArchDescription archDescription) {
        super(archDescription, NotesEtcTypes.DATA_TYPE_BIBLIOGRAPHY);
    }

	public String getType() {
		return NotesEtcTypes.DATA_TYPE_BIBLIOGRAPHY;
	}

	// ********************** Accessor Methods ********************** //

	public SortedSet<ArchDescriptionStructuredDataItems> getBibItems() {
		return bibItems;
	}

	public void setBibItems(SortedSet<ArchDescriptionStructuredDataItems> bibItems) {
		this.bibItems = bibItems;
	}

	public void addBibItem(BibItems bibItem) {
        SequencedObject.adjustSequenceNumberForAdd(this.getBibItems(), bibItem);
		this.bibItems.add(bibItem);
	}

	protected void removeBibItem(BibItems bibItem) {
		if (bibItem == null)
			throw new IllegalArgumentException("Can't remove null bibliographic item.");

		getBibItems().remove(bibItem);
	}

	public void removeRelatedObject(DomainObject domainObject) {
		if (domainObject instanceof BibItems) {
			removeBibItem((BibItems) domainObject);
		}
	}

	public Collection getRelatedCollection(Class clazz) {
		if (clazz == BibItems.class) {
			return getBibItems();
		} else {
			return null;
		}
	}

}
