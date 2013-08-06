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
import org.archiviststoolkit.mydomain.SequencedObject;
import org.archiviststoolkit.mydomain.DomainObject;

import java.util.*;

public class ListDefinition extends ArchDescriptionStructuredData {

	private SortedSet<ArchDescriptionStructuredDataItems> listItems = new TreeSet<ArchDescriptionStructuredDataItems>();

	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public ListDefinition() {
		super(NotesEtcTypes.DATA_TYPE_LIST_DEFINITION);
	}

	public void addItem(ArchDescriptionStructuredDataItems item) {
		addListItem((ListDefinitionItems)item);
	}

	public Set<ArchDescriptionStructuredDataItems> getItems() {
		return listItems;
	}

	/**
	 * Full constructor;
	 */
	public ListDefinition(ArchDescriptionNotes archDescriptionNote) {
		super(archDescriptionNote, NotesEtcTypes.DATA_TYPE_LIST_DEFINITION);
	}

	public String getContent() {
		int numberOfEvents = getListItems().size();
		if (numberOfEvents == 0) {
			return "";
		} else if (numberOfEvents == 1) {
			return getListItems().iterator().next().toString();
		} else {
			return getListItems().iterator().next().toString() + " ...";
		}
	}

	public String getType() {
		return NotesEtcTypes.DATA_TYPE_LIST_DEFINITION;
	}

	// ********************** Accessor Methods ********************** //

	public SortedSet<ArchDescriptionStructuredDataItems> getListItems() {
		return listItems;
	}

	public void setListItems(SortedSet<ArchDescriptionStructuredDataItems> listItems) {
		this.listItems = listItems;
	}

	public void addListItem(ListDefinitionItems listItem) {
		SequencedObject.adjustSequenceNumberForAdd(this.getListItems(), listItem);
		this.listItems.add(listItem);
	}

	protected void removeListItem(ListDefinitionItems listItem) {
		if (listItem == null)
			throw new IllegalArgumentException("Can't add a null bibliographic item.");

		getListItems().remove(listItem);
	}

	public void removeRelatedObject(DomainObject domainObject) {
		if (domainObject instanceof ListDefinitionItems) {
			removeListItem((ListDefinitionItems) domainObject);
		}
	}

	public Collection getRelatedCollection(Class clazz) {
		if (clazz == ListDefinitionItems.class) {
			return getListItems();
		} else {
			return null;
		}
	}
}
