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

import org.archiviststoolkit.structure.NotesEtcTypes;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.StringLengthValidationRequried;
import org.archiviststoolkit.mydomain.SequencedObject;
import org.archiviststoolkit.mydomain.DomainObject;

import java.util.*;

public class ListOrdered extends ArchDescriptionStructuredData {

	public static final String PROPERTYNAME_NUMERATION = "numeration";

	private SortedSet<ArchDescriptionStructuredDataItems> listItems = new TreeSet<ArchDescriptionStructuredDataItems>();

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String numeration = "";

	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public ListOrdered() {
		super(NotesEtcTypes.DATA_TYPE_LIST_ORDERED);
	}

	public void addItem(ArchDescriptionStructuredDataItems item) {
		addListItem((ListOrderedItems)item);
	}

	public Set<ArchDescriptionStructuredDataItems> getItems() {
		return listItems;
	}

	/**
	 * Full constructor;
	 */
	public ListOrdered(ArchDescriptionNotes archDescriptionNote) {
		super(archDescriptionNote, NotesEtcTypes.DATA_TYPE_LIST_ORDERED);
	}

	public String getType() {
		return NotesEtcTypes.DATA_TYPE_LIST_ORDERED;
	}

	// ********************** Accessor Methods ********************** //

	public SortedSet<ArchDescriptionStructuredDataItems> getListItems() {
		return listItems;
	}

	public void setListItems(SortedSet<ArchDescriptionStructuredDataItems> listItems) {
		this.listItems = listItems;
	}

	public void addListItem(ListOrderedItems listItem) {
		SequencedObject.adjustSequenceNumberForAdd(this.getListItems(), listItem);
		this.listItems.add(listItem);
	}

	protected void removeListItem(ListOrderedItems listItem) {
		if (listItem == null)
			throw new IllegalArgumentException("Can't add a null bibliographic item.");

		getListItems().remove(listItem);
	}

	public void removeRelatedObject(DomainObject domainObject) {
		if (domainObject instanceof ListOrderedItems) {
			removeListItem((ListOrderedItems) domainObject);
		}
	}

	public Collection getRelatedCollection(Class clazz) {
		if (clazz == ListOrderedItems.class) {
			return getListItems();
		} else {
			return null;
		}
	}

	public String getNumeration() {
		if (this.numeration != null) {
			return this.numeration;
		} else {
			return "";
		}
	}

	public void setNumeration(String numeration) {
		this.numeration = numeration;
	}

	public String getContent() {
		int numberOfItems = getListItems().size();
		if (numberOfItems == 0) {
			return "";
		} else if (numberOfItems == 1) {
			return getListItems().iterator().next().toString();
		} else {
			return getListItems().iterator().next().toString() + " ...";
		}
	}	
}
