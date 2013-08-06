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
 *
 * Created on July 19, 2005, 11:54 AM
 * @author leemandell
 *
 */

package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.StringLengthValidationRequried;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.util.StringHelper;

@ExcludeFromDefaultValues
public class LookupListItems extends DomainObject {

	public static final String PROPERTYNAME_LIST_ITEM = "listItem";
	public static final String PROPERTYNAME_CODE = "code";

	@IncludeInApplicationConfiguration(1)
	@StringLengthValidationRequried
	@ExcludeFromDefaultValues
  private String listItem = "";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
  @ExcludeFromDefaultValues
  private String code = "";

	@IncludeInApplicationConfiguration
	private Long lookupListItemId;

	@IncludeInApplicationConfiguration
  @ExcludeFromDefaultValues
  private Boolean editable = true;

  private Boolean atInitialValue = false;

	private LookupList lookUpList;


	private long recordCount=0;

	/**
	 * Creates a new instance of Subject
	 */
	public LookupListItems() {
	}

	/**
	 * Full constructor;
	 */
	public LookupListItems(LookupList lookUpList) {
		this.setLookUpList(lookUpList);
	}

	public LookupListItems(LookupList lookUpList, String item) {
		this.setLookUpList(lookUpList);
		this.listItem = item;
		fillOutCodeIfNecessary(code);
	}

	public LookupListItems(LookupList lookUpList, String item, String code) {
		this.setLookUpList(lookUpList);
		this.listItem = item;
		this.code = code;
		fillOutCodeIfNecessary(code);
	}

	public LookupListItems(LookupList lookUpList, String item, Boolean editable) {
		this.setLookUpList(lookUpList);
		this.listItem = item;
		if (editable != null) {
			this.editable = editable;
		}
		fillOutCodeIfNecessary(code);
	}

	public LookupListItems(LookupList lookUpList, String item, String code, Boolean editable) {
		this.setLookUpList(lookUpList);
		this.listItem = item;
		this.code = code;
		if (editable != null) {
			this.editable = editable;
		}
		fillOutCodeIfNecessary(code);
	}

	private void fillOutCodeIfNecessary(String code) {
		if (this.lookUpList.getPairedValues()) {
			if (code == null || code.length() == 0) {
				this.code = StringHelper.convertToNmtoken(this.listItem);
			}
		}
	}

	public LookupList getLookUpList() {
		return lookUpList;
	}

	public String getListItem() {
		if (this.listItem != null) {
			return this.listItem;
		} else {
			return "";
		}
	}

	public void setListItem(String listItem) {
		this.listItem = listItem;
	}

	public void setLookUpList(LookupList lookUpList) {
		this.lookUpList = lookUpList;
	}

	public String toString() {
		String returnString = this.listItem;
		if (listItem.length() == 0) {
			return  " ";
		}

		if (lookUpList.getPairedValues()) {
			if (code != null) {
				returnString += " (" + code + ")";
			} else {
				returnString += " ()";
			}
		}
		return returnString;
	}

	public Long getIdentifier() {
		return getLookupListItemId();
	}

	public void setIdentifier(Long identifier) {
		this.setLookupListItemId(identifier);
	}

	public Long getLookupListItemId() {
		return lookupListItemId;
	}

	public void setLookupListItemId(Long lookupListItemId) {
		this.lookupListItemId = lookupListItemId;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public Boolean getAtInitialValue() {
		return atInitialValue;
	}

	public void setAtInitialValue(Boolean atInitialValue) {
		this.atInitialValue = atInitialValue;
	}

	public long getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(long recordCount) {
		this.recordCount = recordCount;
	}

	public String getCode() {
		if (this.code != null) {
			return this.code;
		} else {
			return "";
		}
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void parseCodes() {
		int position = listItem.indexOf(";");
		if (position != -1) {
			code = listItem.substring(position + 1).trim();
			listItem = listItem.substring(0, position).trim();
		} else {
			position = listItem.lastIndexOf("(");
			if (position != -1) {
				code = listItem.substring(position + 1);
				code = code.replace(")", "");
				code = code.trim();
				listItem = listItem.substring(0, position).trim();
			}
		}
	}
}
