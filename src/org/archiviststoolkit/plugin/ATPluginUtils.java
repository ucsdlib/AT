package org.archiviststoolkit.plugin;

import org.archiviststoolkit.model.ATPluginData;
import org.archiviststoolkit.model.validators.ATValidator;
import org.archiviststoolkit.model.validators.ValidatorFactory;
import org.archiviststoolkit.mydomain.DomainAccessObject;
import org.archiviststoolkit.mydomain.DomainAccessObjectFactory;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.util.JGoodiesValidationUtils;
import com.thoughtworks.xstream.XStream;
import com.jgoodies.validation.ValidationResult;

import java.util.Collection;
import java.awt.*;

/**
 * Archivists' Toolkit(TM) Copyright � 2005-2009 Regents of the University of California, New York University, & Five Colleges, Inc.
 * All rights reserved.
 *
 * This software is free. You can redistribute it and / or modify it under the terms of the Educational Community License (ECL)
 * version 1.0 (http://www.opensource.org/licenses/ecl1.php)
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the ECL license for more details about permissions and limitations.
 *
 * Archivists' Toolkit(TM)
 * http://www.archiviststoolkit.org
 * info@archiviststoolkit.org
 *
 * Created by IntelliJ IDEA.
 *
 * This is a utility class to make it easier for plugin developers to save
 * data to the AT database and perform validation on AT records.
 *
 * @author: Nathan Stevens
 * Date: Feb 11, 2009
 * Time: 8:07:58 PM
 */
public class ATPluginUtils {
    /**
     * Method to save text data to the database
     *
     * @param pluginName The name of the plugin
     * @param dataVersion The dataVersion
     * @param dataName The name of the data
     * @param dataType The type of data
     * @param dataString The text data to save
     * @throws Exception is thrown if there was a problem saving the data
     */
    public static void saveData(String pluginName, int dataVersion,
                                String dataName, String dataType,
                                String dataString) throws Exception {
        ATPluginData pluginData =
                new ATPluginData(pluginName, false, dataVersion,
                        dataName, dataType, dataString);
        saveToDatabase(pluginData);
    }

    /**
     * Method that first converts a java object to an xml string
     * then save it to the database
     *
     * @param pluginName The name of the plugin
     * @param dataVersion The dataVersion
     * @param dataName The name of the data
     * @param dataType The type of data
     * @param dataObject The object that contains the data or is the data
     * @throws Exception is thrown if there was a problem saving the data
     */
    public static void saveData(String pluginName, int dataVersion,
                                String dataName, String dataType,
                                Object dataObject) throws Exception {
        // use Xstream to convert the java object to an xml string
        XStream xstream = new XStream();
        String dataString = xstream.toXML(dataObject);

        ATPluginData pluginData =
                new ATPluginData(pluginName, true, dataVersion,
                        dataName, dataType, dataString);
        saveToDatabase(pluginData);
    }

    /**
     * Saves plugin data object to the database
     *
     * @param pluginData The plugin data object
     * @throws Exception If there is any problems saving to the database
     */
    public static void saveToDatabase(ATPluginData pluginData) throws Exception {
        try {
            DomainAccessObject access =
                    DomainAccessObjectFactory.getInstance().getDomainAccessObject(ATPluginData.class);
            access.getLongSession();
            access.updateLongSession(pluginData, false);
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception("Error Saving Plugin Data to Database ...");
        }
    }

    /**
     * Method to delete plugin data in the database
     *
     * @param pluginData The plugin data
     * @throws Exception if there was a problem deleting that data
     */
    public static void deletePluginData(ATPluginData pluginData) throws Exception {
        try {
            DomainAccessObject access =
                    DomainAccessObjectFactory.getInstance().getDomainAccessObject(ATPluginData.class);
            access.getLongSession();
            access.deleteLongSession(pluginData);
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception("Error Deleting Plugin Data to Database ...");
        }
    }

    /**
     * Method to get all the saved data for a certain plugin
     *
     * @param pluginName The name of the plugin
     * @return Collection containing any data they found
     * @throws Exception
     */
    public static Collection getData(String pluginName) throws Exception {
        try {
            DomainAccessObject access =
                    DomainAccessObjectFactory.getInstance().getDomainAccessObject(ATPluginData.class);
            return access.findByPropertyValue("pluginName", pluginName);
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception("Error Getting Plugin Data from Database ...");
        }

    }

    /**
     * Method to get all the saved data for a particular plugin and data type
     *
     * @param pluginName The name of the plugin
     * @param dataType The data type of the plugin
     * @return A collection containing any data that was found
     * @throws Exception is thrown of there is a problem find the data
     */
    public static Collection getData(String pluginName, String dataType) throws Exception {
        try {
            DomainAccessObject access =
                    DomainAccessObjectFactory.getInstance().getDomainAccessObject(ATPluginData.class);
            ATPluginData pluginData = new ATPluginData();
            pluginData.setPluginName(pluginName);
            pluginData.setDataType(dataType);
            return access.findByExample(pluginData);
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception("Error Getting Plugin Data from Database ...");
        }
    }

    /**
     * Method to return a string object or xml encoded object
     * found in the database. If more than one data object with
     * the same name is found then the first one is return.
     *
     * @param pluginName The name of the plugin
     * @param dataName The name of the data
     * @return The data object
     * @throws Exception If there is a problem finding the data from the database
     */
    public static Object getDataByName(String pluginName, String dataName) throws Exception {
        try {
            DomainAccessObject access =
                    DomainAccessObjectFactory.getInstance().getDomainAccessObject(ATPluginData.class);
            ATPluginData pluginData = new ATPluginData();
            pluginData.setPluginName(pluginName);
            pluginData.setDataName(dataName);
            Collection collection = access.findByExample(pluginData);

            // get the plugin data object returned from the database only return the first one
            if(collection != null) {
                Object[] dataFound = collection.toArray();
                pluginData = (ATPluginData)dataFound[0];
                if(pluginData.getIsObject()) { // xml encoded object so convert it to an object
                    return getObjectFromPluginData(pluginData);
                } else { // just return the plain data string
                    return pluginData.getDataString();
                }
            } else {
                return null;
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception("Error Getting Plugin Data from Database ...");
        }
    }

    /**
     * Method to return an object from plugin data using
     * xstream to convert the saved xml to an object
     *
     * @param pluginData The ATPluginDataContaining the xml encoded object
     * @return The converted object or null if conversion can't be done
     */
    public static Object getObjectFromPluginData(ATPluginData pluginData) {
        if(pluginData != null && pluginData.getIsObject()) {
            XStream xstream = new XStream();
            return xstream.fromXML(pluginData.getDataString());
        } else {
            return null;
        }
    }

    /**
     * Method to save an AT record to the database.
     *
     * @param record The AT record to save to the database.
     * @throws Exception if there is a problem saving the record to the database
     */
    public static void saveRecordToDatabase(DomainObject record) throws Exception {
        try {
            Class clazz = record.getClass();

            DomainAccessObject access =
                    DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);
            access.getLongSession();
            access.updateLongSession(record);
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception("Error Saving Record to Database ...");
        }
    }

    /**
     * Method to valid a AT record. Calling this ensures that no invalid
     * records are saved to the database
     *
     * @param component UI component that is requesting validation of the record
     * @param record The AT record to validate
     * @return true if the record valide, false otherwise
     */
    public static boolean validateRecord(Component component, DomainObject record) {
        ATValidator validator = ValidatorFactory.getInstance().getValidator(record);
		if (validator == null) {
			//nothing registered so just return true
			return true;
		} else {
			ValidationResult validationResult = validator.validate();
			if (validationResult.hasErrors()) {
				JGoodiesValidationUtils.showValidationMessage(
						component,
						"To save the record, please fix the following errors:",
						validationResult);
				return false;
			}
			if (validationResult.hasWarnings()) {
				JGoodiesValidationUtils.showValidationMessage(
						component,
						"Note: some fields are invalid.",
						validationResult);
			}
			return true;
		}
    }
}
