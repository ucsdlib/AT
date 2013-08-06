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
 * @author leemandell
 */

package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.util.LookupListUtils;
import java.util.*;
import org.archiviststoolkit.structure.*;
import org.archiviststoolkit.swing.CaseInsensitiveComparator;

@ExcludeFromDefaultValues
public class LookupList extends DomainObject {

    public static final String PROPERTYNAME_LIST_NAME = "listName";
    public static final String PROPERTYNAME_LIST_TYPE = "listType";
	public static final String PROPERTYNAME_PAIRED_VALUES = "pairedValues";
	public static final String PROPERTYNAME_RESTRICT_TO_NMTOKEN = "restrictToNmtoken";

    @IncludeInApplicationConfiguration
    private Long lookupListId;

    @IncludeInApplicationConfiguration(1)
	@StringLengthValidationRequried
    private String listName = "";

    @IncludeInApplicationConfiguration
    private Integer listType = LookupListUtils.EDITABLE;

	@IncludeInApplicationConfiguration
	private Boolean pairedValues = false;

	@IncludeInApplicationConfiguration
	private Boolean restrictToNmtoken = false;

	private Set<LookupListItems> listItems = new TreeSet<LookupListItems>();


    /**
     * Creates a new instance of LookupList
     */
    public LookupList() {
    }


    public String getListName() {
		if (this.listName != null) {
			return this.listName;
		} else {
			return "";
		}
	}

    public void setListName(String listName) {
        this.listName = listName;
    }


    /**
     * Adds a <tt>BillingDetails</tt> to the set.
     * <p/>
     * This method checks if there is only one billing method
     * in the set, then makes this the default.
     *
     * @param listItem
     */
    public boolean addListItem(LookupListItems listItem) {
      boolean added = false;
        if (listItem == null)
            throw new IllegalArgumentException("Can't add a null list item.");
		String listItemValue = listItem.getListItem();
		if (!this.containsItem(listItemValue)) {
            added = this.getListItems().add(listItem);
        }
      return added;
    }

	public boolean addListItem(String value) {
		return addListItem(new LookupListItems(this, value));
	}

	public boolean addListItem(String value, String code) {
		return addListItem(new LookupListItems(this, value, code));
	}

	public boolean addListItem(String value, String code, Boolean editable) {
	   return addListItem(new LookupListItems(this, value, code, editable));
   }

	public boolean addListItem(String value, Boolean editable) {
	   return addListItem(new LookupListItems(this, value, editable));
   }

    /**
     * Removes a <tt>BillingDetails</tt> from the set.
     * <p/>
     * This method checks if the removed is the default element,
     * and will throw a BusinessException if there is more than one
     * left to chose from. This might actually not be the best way
     * to handle this situation.
     *
     * @param listItem
     */
    public void removeListItem(LookupListItems listItem) {
        if (listItem == null)
            throw new IllegalArgumentException("Can't add a null list item.");

        getListItems().remove(listItem);
    }

    public void addListItems(ArrayList array) {
      Iterator it = array.iterator();
        String item;
        while (it.hasNext()) {
            item = (String) it.next();
            if (item != null && item.length() != 0) {
                this.getListItems().add(new LookupListItems(this, item));
            }
        }
    }

    public Set<LookupListItems> getListItems() {
        return listItems;
    }

    public boolean containsItem(String item) {
        for (LookupListItems listItem : listItems) {
            if (listItem.getListItem().equalsIgnoreCase(item)) {
                return true;
            }
        }
        return false;
    }

	public LookupListItems getItem(String item) {
		for (LookupListItems listItem : listItems) {
			if (listItem.getListItem().equalsIgnoreCase(item)) {
				return listItem;
			}
		}
		return null;
	}

	public TreeSet<String> getListItemsWithBlankAtTop() {
		TreeSet<String> set = new TreeSet<String>(new CaseInsensitiveComparator());
		set.add("");
		for (LookupListItems listItem : listItems) {
			set.add(listItem.toString());
		}
		return set;
	}

	public TreeSet<LookupListItems> getListItemsWithBlankAtTopObjects() {
		TreeSet<LookupListItems> set = new TreeSet<LookupListItems>(new CaseInsensitiveComparator());
		set.add(new LookupListItems());
		for (LookupListItems listItem : listItems) {
			set.add(listItem);
		}
		return set;
	}

    public TreeSet<String> getListItemsAsArrayList() {
        TreeSet<String> returnArrayList = new TreeSet<String>(new CaseInsensitiveComparator());
        for (LookupListItems listItem : listItems) {
            returnArrayList.add(listItem.toString());
        }
        return returnArrayList;
    }

    public void setListItems(Set<LookupListItems> listItems) {
        this.listItems = listItems;
    }

    public Long getIdentifier() {
        return getLookupListId();
    }

    public void setIdentifier(Long identifier) {
        this.setLookupListId(identifier);
    }

    public Long getLookupListId() {
        return lookupListId;
    }

    public void setLookupListId(Long lookupListId) {
        this.lookupListId = lookupListId;
    }

    public Integer getListType() {
        return listType;
    }

    public void setListType(Integer listType) {
        this.listType = listType;
    }

	public Boolean getPairedValues() {
		return pairedValues;
	}

	public void setPairedValues(Boolean pairedValues) {
		this.pairedValues = pairedValues;
	}

	public Boolean getRestrictToNmtoken() {
		return restrictToNmtoken;
	}

	public void setRestrictToNmtoken(Boolean restrictToNmtoken) {
		this.restrictToNmtoken = restrictToNmtoken;
	}
}
