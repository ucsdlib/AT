package org.archiviststoolkit.model;

import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.mydomain.DomainObject;

/**
 * Archivists' Toolkit(TM) Copyright © 2005-2009 Regents of the University of California, New York University, & Five Colleges, Inc.
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
 * This class stores data for plugins that are loaded by the AT
 * Data has to be stored as String, but can be xml even objects converted to xml using
 * the xstream jar
 * Created by IntelliJ IDEA.
 *
 * @author: Nathan Stevens
 * Date: Feb 11, 2009
 * Time: 8:20:36 PM
 */
@ExcludeFromDefaultValues
public class ATPluginData extends DomainObject {
    @ExcludeFromDefaultValues
	private Long atPluginDataId; // The primary key assigned to this object

    @ExcludeFromDefaultValues
    private String pluginName; // Name of the plugin for which this data belongs to

    @ExcludeFromDefaultValues
    private Boolean isObject;  // Does the dataString represent a converted java object

    @ExcludeFromDefaultValues
    private int dataVersion;  // The version number of the data

    @ExcludeFromDefaultValues
    private String dataName;  // The name of data the text represent

    @ExcludeFromDefaultValues
    private String dataType;  // The type of data the text represent

    @ExcludeFromDefaultValues
    private String dataString;  // The string that holds the data

    /**
     * The default constructor
     */
    public ATPluginData() { }

    public ATPluginData(String pluginName, Boolean isObject, int dataVersion, String dataName, String dataType, String dataString) {
        this.pluginName = pluginName;
        this.isObject = isObject;
        this.dataVersion = dataVersion;
        this.dataName = dataName;
        this.dataType = dataType;
        this.dataString = dataString;
    }

    /**
     * Method to return the identifier for this record
     * @return Record Identifier
     */
    public Long getAtPluginDataId() {
        return atPluginDataId;
    }

    /**
     * Method to set the record identifier
     * @param atPluginDataId long containing record identifier
     */
    public void setAtPluginDataId(Long atPluginDataId) {
        this.atPluginDataId = atPluginDataId;
    }

    /**
     * Method to return the data string. can be plain text or xml representation of a java object
     * @return The data string
     */
    public String getDataString() {
        return dataString;
    }

    /**
     * Method to set the data string
     * @param dataString String from database
     */
    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    /**
     * Method to return the type of data this is. The plugin developer sets this to anything
     * that works for them
     * @return The type of data this stores
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * Method to set the type of this is. The plugin developer sets this
     * @param dataType The type of this stores
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     * Method to get the name of this data
     * @return The name of the data given by the user or developer
     */
    public String getDataName() {
        return dataName;
    }

    /**
     * Method to set the name of the data
     * @param dataName The name of the data
     */
    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    /**
     * @return The version of the data. used to keep track of version number of data
     */
    public int getDataVersion() {
        return dataVersion;
    }

    /**
     * @param dataVersion Set the data version
     */
    public void setDataVersion(int dataVersion) {
        this.dataVersion = dataVersion;
    }

    /**
     * @return Boolean to see if store text data presents a java object
     */
    public Boolean getIsObject() {
        return isObject;
    }

    /**
     * @param object set whether this is an object or not
     */
    public void setIsObject(Boolean object) {
        isObject = object;
    }

    /**
     * @return The name of the plugin this data belongs to
     */
    public String getPluginName() {
        return pluginName;
    }

    /**
     * @param pluginName Set the name of the plugin this data belongs to
     */
    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    /**
     * Method to return the identifire for this record
     * @return Long containing the identifier
     */
    public Long getIdentifier() {
		return atPluginDataId;
	}

    /**
     * Method to return the
     * @param identifier The identifier to set.
     */
    public void setIdentifier(Long identifier) {
        atPluginDataId = identifier;
    }
    
    public String toString()  {
        return dataName;
    }
}
