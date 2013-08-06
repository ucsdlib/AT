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
import org.archiviststoolkit.mydomain.DomainObject;

@ExcludeFromDefaultValues
public class ListDefinitionItems extends ArchDescriptionStructuredDataItems {

	public static final String PROPERTYNAME_LABEL = "label";
	public static final String PROPERTYNAME_ITEM_VALUE = "itemValue";

	@IncludeInApplicationConfiguration(1)
	private String label = "";

	@IncludeInApplicationConfiguration(2)
	private String itemValue = "";

	/**
	 * Creates a new instance of Bibliography Items
	 */
	public ListDefinitionItems() {
	}

	public ListDefinitionItems(DomainObject parent) {
		this.structuredDataParent = (ListDefinition)parent;
	}

	public String getItemValue() {
		if (this.itemValue != null) {
			return this.itemValue;
		} else {
			return "";
		}
	}

	public void setItemValue(String itemValue) {
		Object oldValue = getItemValue();
		this.itemValue = itemValue;
		firePropertyChange(ListDefinitionItems.PROPERTYNAME_ITEM_VALUE, oldValue, itemValue);
	}

	public String getLabel() {
		if (this.label != null) {
			return this.label;
		} else {
			return "";
		}
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String toString() {
		return label + ": " + itemValue;
	}
}
