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
 * Date: Jul 29, 2008
 * Time: 6:20:01 PM
 */

package org.archiviststoolkit.util;

import org.archiviststoolkit.model.RDEScreenPanels;
import org.archiviststoolkit.model.RDEScreen;
import org.archiviststoolkit.model.Users;
import org.archiviststoolkit.ApplicationFrame;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

public class RDEUtils {

    public static String getPrettyName(String rdePanelType) {
        if (rdePanelType.equals(RDEScreenPanels.PANEL_TYPE_ANALOG_INSTANCE)) {
            return "Analog instance";
        } else if (rdePanelType.equals(RDEScreenPanels.PANEL_TYPE_DIGITAL_INSTANCE)) {
            return "Digital instance";
        } else if (rdePanelType.equals(RDEScreenPanels.PANEL_TYPE_EXTENT)) {
            return "Extent";
        } else if (rdePanelType.equals(RDEScreenPanels.PANEL_TYPE_NAME_LINK)) {
            return "Name link";
        } else if (rdePanelType.equals(RDEScreenPanels.PANEL_TYPE_NOTE)) {
            return "Note";
        } else if (rdePanelType.equals(RDEScreenPanels.PANEL_TYPE_SIMPLE)) {
            return "Simple";
        } else if (rdePanelType.equals(RDEScreenPanels.PANEL_TYPE_SUBJECT_LINK)) {
            return "Subject link";
        } else if (rdePanelType.equals(RDEScreenPanels.PANEL_TYPE_YEAR_RANGE_BULK)) {
            return "Bulk dates";
        } else if (rdePanelType.equals(RDEScreenPanels.PANEL_TYPE_YEAR_RANGE_INCLUSIVE)) {
            return "Inclusive dates";
        } else {
            return "Unknown panel type";
        }
    }

    // Method to check to see if an RDEScreen can be used by the current user
    public static Boolean canUseRDEScreen(RDEScreen screen) {
        Boolean use = false;
        if (Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_BEGINNING_DATA_ENTRY)) {
            use = true;
        }

        /*Users currentUser = ApplicationFrame.getInstance().getCurrentUser();
        Users rdeOwner = screen.getUser();

        // if superuser then they can view everything so just return
        if(currentUser.getAccessClass() >= Users.ACCESS_CLASS_SUPERUSER) {
          use = true;
        }
        else if(rdeOwner == null) { // no owner so use it
          use = true;
        }
        else if(currentUser.equals(rdeOwner)) { // belongs to current user so they can use it
          use = true;
        }
        else if(screen.getKeepScreenPrivate() == null || !screen.getKeepScreenPrivate()) { // not to be keep private so use it
          use = true;
        }*/

        return use;
    }

    // Method to see if the current user can edit the rdescreen past as an argument
    public static Boolean canEditRDEScreen(RDEScreen screen) {
        Boolean edit = false;
        Users currentUser = ApplicationFrame.getInstance().getCurrentUser();
        Users rdeOwner = screen.getUser();

        if (rdeOwner == null) { // no owner so use it
            edit = true;
        } else if (currentUser.equals(rdeOwner)) { // belongs to current user so they can edit it
            edit = true;
        } else if (screen.getLetOthersEdit() != null && screen.getLetOthersEdit()) { // not to be keep private so use it
            edit = true;
        }

        return edit;
    }

    /**
     * Methodv to remove rde screens from a result set if they don't belong
     * a particular class
     * @param screens The list of rde screens to iterate through
     * @param clazz The class to check against
     */
    public static void removeRDEScreens(Collection screens, Class clazz) {
        // have to put collection elements in an array list so we can both iterate over and remove from list
        ArrayList screen_list = new ArrayList(screens);

        // now go through list and see what to remove it if current user can't edit it
        for (Iterator iterator = screen_list.iterator(); iterator.hasNext();) {
            RDEScreen rdeScreen = (RDEScreen) iterator.next();
            if (!rdeScreen.getClassName().equals(clazz.getName())) {
                screens.remove(rdeScreen);
            }
        }
    }
}