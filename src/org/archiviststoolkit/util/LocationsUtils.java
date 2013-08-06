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
 * @author Lee Mandell
 * Date: Apr 18, 2007
 * Time: 11:27:52 AM
 */

package org.archiviststoolkit.util;

import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.model.Locations;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.hibernate.LockMode;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.BasicEventList;

import java.sql.SQLException;
import java.util.Collection;

public class LocationsUtils {

    private static SortedList locationsGlazedList;
    /**
     * event list that hosts the issues
     */
    private static EventList locationEventList = new BasicEventList();

    public static SortedList getLocationsGlazedList() {
        return locationsGlazedList;
    }

    public static void initLocationLookupList() {
        try {
            // clear the event list so we done get duplicates if this
            // method is called more than once
            locationEventList.clear();

            DomainAccessObject dao = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Locations.class);
            locationEventList.addAll(dao.findAll(LockMode.READ));
            locationsGlazedList = new SortedList(locationEventList);
        } catch (LookupException e) {
            new ErrorDialog("", e).showDialog();
        } catch (PersistenceException e) {
            new ErrorDialog("", e).showDialog();
        }
    }

    public static void addLocationToLookupList(Locations location) {
        locationEventList.add(location);
    }

    public static void removeLocationToLookupList(Locations location) {
        locationEventList.remove(location);
    }

    public static void parseLocationRecords() {
        try {
            DomainAccessObject dao = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Locations.class);
            Locations location;
            int lastSpaceLocation;
            String oldLabel;
            Double number;
            Boolean update;
            String beginning;
            String end;
            Collection locations = dao.findAllLongSession();
            int count = locations.size();


            for (Object object : locations) {
                System.out.println(count--);
                location = (Locations) object;
                oldLabel = location.getCoordinate1Label();
                update = false;
                if (oldLabel != null && oldLabel.length() > 0) {
                    lastSpaceLocation = oldLabel.lastIndexOf(" ");
                    if (lastSpaceLocation == -1) {
                        //just check to see if this is a number
                        try {
                            number = Double.valueOf(oldLabel);
                            location.setCoordinate1NumericIndicator(number);
                            location.setCoordinate1Label("");
                            update = true;
                        } catch (NumberFormatException e) {
                            //do nothing and leave things alone
                        }
                    } else {
                        beginning = oldLabel.substring(0, lastSpaceLocation).trim();
                        end = oldLabel.substring(lastSpaceLocation).trim();
                        try {
                            number = Double.valueOf(end);
                            location.setCoordinate1NumericIndicator(number);
                            location.setCoordinate1Label(beginning);
                            update = true;
                        } catch (NumberFormatException e) {
                            location.setCoordinate1AlphaNumIndicator(end);
                            location.setCoordinate1Label(beginning);
                            update = true;
                        }
                    }
                }

                if (update) {
                    dao.updateLongSession(location, false);
                }
            }
            dao.closeLongSession();

        } catch (PersistenceException e) {
            new ErrorDialog("", e).showDialog();
        } catch (LookupException e) {
            new ErrorDialog("", e).showDialog();
        } catch (SQLException e) {
            new ErrorDialog("", e).showDialog();
        }

    }


}
