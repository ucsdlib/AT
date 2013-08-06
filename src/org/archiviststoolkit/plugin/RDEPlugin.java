package org.archiviststoolkit.plugin;

import org.archiviststoolkit.model.ResourcesCommon;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.model.Resources;

import javax.swing.*;

/**
 * Archivists' Toolkit(TM) Copyright � 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
 * All rights reserved.
 * <p/>
 * This software is free. You can redistribute it and / or modify it under the terms of the Educational Community License (ECL)
 * version 1.0 (http://www.opensource.org/licenses/ecl1.php)
 * <p/>
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the ECL license for more details about permissions and limitations.
 * <p/>
 * <p/>
 * Archivists' Toolkit(TM)
 * http://www.archiviststoolkit.org
 * info@archiviststoolkit.org
 * <p/>
 * <p/>
 * This interface is implemented by rapid data entry plugins which are
 * loaded into the rapid data entry screen drop-down menu in resource
 * editor
 * <p/>
 * Created by IntelliJ IDEA.
 *
 * @author: Nathan Stevens
 * Date: Jul 13, 2010
 * Time: 2:35:06 PM
 */
public interface RDEPlugin {
    /**
     * Method to set the domain model. This is always called when the plugin
     * are loaded
     *
     * @param parentRecord    The parent resource record
     * @param resourcesCommon This can either be a resource component or
     *                        the parent resource record
     */
    public void setModel(Resources parentRecord, ResourcesCommon
            resourcesCommon);

    /**
     * Method to specify whether this plugin launches a dialog, or
     * just executes business logic on the currently selected
     * resource component
     *
     * @return boolean to specify wheather this plugin has a dialog
     */
    public boolean hasDialog();

    /**
     * Method to run program logic. This method is called when the hasDialog
     * method returns false.
     */
    public void doTask();

    /**
     * Method to display a dialog to user. It is called when a call
     * to the hasDialog method returns true
     *
     * @param dialog The parent dialog
     * @param title  The title of dialog
     */
    public void showPlugin(JDialog dialog, String title);
}
