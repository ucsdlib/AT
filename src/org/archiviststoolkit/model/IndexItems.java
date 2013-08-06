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
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.StringLengthValidationRequried;


@ExcludeFromDefaultValues
public class IndexItems extends ArchDescriptionStructuredDataItems {

public static final String PROPERTYNAME_ITEM_VALUE = "itemValue";
public static final String PROPERTYNAME_ITEM_TYPE = "itemType";
public static final String PROPERTYNAME_REFERENCE = "reference";
public static final String PROPERTYNAME_REFERENCE_TEXT = "referenceText";

@IncludeInApplicationConfiguration(1)
private String itemValue = "";

@IncludeInApplicationConfiguration(2)
private String itemType = "";

@IncludeInApplicationConfiguration(3)
private String reference = "";

@IncludeInApplicationConfiguration(4)
@StringLengthValidationRequried
private String referenceText = "";
/**
 * Creates a new instance of Bibliography
 */
public IndexItems() {
}

public IndexItems(DomainObject parent) {
	this.structuredDataParent = (Index)parent;
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
	firePropertyChange(PROPERTYNAME_ITEM_VALUE, oldValue, itemValue);
}

public String getItemType() {
	if (this.itemType != null) {
		return this.itemType;
	} else {
		return "";
	}
}

public void setItemType(String itemType) {
	this.itemType = itemType;
}

public String getReference() {
	if (this.reference != null) {
		return this.reference;
	} else {
		return "";
	}
}

public void setReference(String reference) {
	this.reference = reference;
}

public String getReferenceText() {
	if (this.referenceText != null) {
		return this.referenceText;
	} else {
		return "";
	}
}

public void setReferenceText(String referenceText) {
	this.referenceText = referenceText;
}
}
