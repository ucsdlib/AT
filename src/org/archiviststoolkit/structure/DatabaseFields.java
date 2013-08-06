/**
 * Archivists' Toolkit(TM) Copyright � 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
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
 * @author Lee Mandell
 */

package org.archiviststoolkit.structure;

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.util.StringHelper;

import java.util.Set;
import java.util.HashSet;

@ExcludeFromDefaultValues
public class DatabaseFields  extends DomainObject {

    public static final String PROPERTYNAME_FIELD_NAME    = "fieldName";
    public static final String PROPERTYNAME_FIELD_LABEL = "fieldLabel";
    public static final String PROPERTYNAME_DEFINITION    = "definition";
    public static final String PROPERTYNAME_EXAMPLES = "examples";
    public static final String PROPERTYNAME_LOOKUP_LIST    = "lookupList";
    public static final String PROPERTYNAME_INCLUDE_IN_SEARCH_EDITOR    = "includeInSearchEditor";
    public static final String PROPERTYNAME_ALLOW_SORT = "allowSort";
    public static final String PROPERTYNAME_DATA_TYPE = "dataType";
    public static final String PROPERTYNAME_RETURN_SCREEN_ORDER = "returnScreenOrder";

    private Long fieldId;
    @IncludeInApplicationConfiguration(1)
    private String fieldName = "";

	@IncludeInApplicationConfiguration(2)
    private String fieldLabel = "";

	private String definition ="";
    private String examples ="";
    private String lookupList ="";
    private Boolean includeInSearchEditor = false;
	private Boolean excludeFromDefaultValues = false;
	private Boolean includeInRDE = false;
	private Integer stringLengthLimit = null;

    @IncludeInApplicationConfiguration(3)
    private String dataType = "";

	@IncludeInApplicationConfiguration(4)
    private Integer returnScreenOrder = 0;

	//field to help remove unused fields
	private Boolean keep = false;

	private DatabaseTables databaseTable;

    private Set<DefaultValues> defaultValues = new HashSet<DefaultValues>();


    /** Creates a new instance of Subject */
    public DatabaseFields() {
    }

    public DatabaseFields(DatabaseTables parent) {
        this.databaseTable = parent;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
		if (this.fieldName != null) {
			return this.fieldName;
		} else {
			return "";
		}
	}

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
//        this.setFieldLabel(StringHelper.makePrettyName(fieldName));
    }

    public String getFieldLabel() {
		if (this.fieldLabel != null) {
			return this.fieldLabel;
		} else {
			return "";
		}
	}

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public Long getIdentifier() {
        return this.getFieldId();
    }

    public void setIdentifier(Long identifier) {
        this.setFieldId(identifier);
    }


    public String getDefinition() {
		if (this.definition != null) {
			return this.definition;
		} else {
			return "";
		}
	}

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public DatabaseTables getDatabaseTable() {
        return databaseTable;
    }

    public void setDatabaseTable(DatabaseTables databaseTable) {
        this.databaseTable = databaseTable;
    }

    public String getExamples() {
		if (this.examples != null) {
			return this.examples;
		} else {
			return "";
		}
	}

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public String getLookupList() {
		if (this.lookupList != null) {
			return this.lookupList;
		} else {
			return "";
		}
	}

    public void setLookupList(String lookupList) {
        this.lookupList = lookupList;
    }

    public Boolean getIncludeInSearchEditor() {
        return includeInSearchEditor;
    }

    public void setIncludeInSearchEditor(Boolean includeInSearchEditor) {
        this.includeInSearchEditor = includeInSearchEditor;
    }

//    public Boolean getAllowSort() {
//        return allowSort;
//    }
//
//    public void setAllowSort(Boolean allowSort) {
//        this.allowSort = allowSort;
//    }

    public String getDataType() {
		if (this.dataType != null) {
			return this.dataType;
		} else {
			return "";
		}
	}

    public void setDataType(String dataType) {
        this.dataType = dataType;
		if (!canFieldAppearInReturnScreen()) {
			setReturnScreenOrder(0);
		}
	}

    public Integer getReturnScreenOrder() {
        return returnScreenOrder;
    }

    public void setReturnScreenOrder(Integer returnScreenOrder) {
        this.returnScreenOrder = returnScreenOrder;
    }

    public void setReturnScreenOrder(Long returnScreenOrder) {
        this.returnScreenOrder = returnScreenOrder.intValue();
    }

    public Set<DefaultValues> getDefaultValues() {
        return defaultValues;
    }

    public void setDefaultValues(Set<DefaultValues> defaultValues) {
        this.defaultValues = defaultValues;
    }

    public String toString() {
        return this.getFieldLabel();
    }

	public Boolean getKeep() {
		return keep;
	}

	public void setKeep(Boolean keep) {
		this.keep = keep;
	}

	public Boolean canFieldAppearInReturnScreen() {
		if (dataType != null && dataType.equalsIgnoreCase(Set.class.getName())) {
			return false;
		}
		return true;
	}

    // TODO The database will have to be cleaned up to to remove instances that have already been
    // added to the search editor. This, however, is not high priority.
    
    /**
     * Method to see if to add this field to the search editor
     * Right now all instances/sets are excluded from being added to the search editor
     *
     * @return True if it can be included in the seach editor, false otherwise.
     */
    public Boolean canFieldAppearInSearchEditor() {
        if (dataType != null && dataType.equalsIgnoreCase(Set.class.getName())) {
			return false;
		} else if(databaseTable.getTableName().equals("ArchDescriptionInstances")
                || databaseTable.getTableName().equals("ArchDescriptionAnalogInstances")
                || databaseTable.getTableName().equals("ResourcesComponentsSearchResult")) {
            return false;
        }
		return true;
    }

	public Boolean getExcludeFromDefaultValues() {
		return excludeFromDefaultValues;
	}

	public void setExcludeFromDefaultValues(Boolean excludeFromDefaultValues) {
		this.excludeFromDefaultValues = excludeFromDefaultValues;
	}

	public Integer getStringLengthLimit() {
		return stringLengthLimit;
	}

	public void setStringLengthLimit(Integer stringLengthLimit) {
		this.stringLengthLimit = stringLengthLimit;
	}

	public Boolean getIncludeInRDE() {
		return includeInRDE;
	}

	public void setIncludeInRDE(Boolean includeInRDE) {
		this.includeInRDE = includeInRDE;
	}
}
