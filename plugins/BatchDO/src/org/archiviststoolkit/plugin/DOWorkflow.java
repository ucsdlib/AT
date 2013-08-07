package org.archiviststoolkit.plugin;

import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.model.ResourcesCommon;

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
 * Created by IntelliJ IDEA.
 *
 * @author: Nathan Stevens
 * Date: Jul 13, 2010
 * Time: 3:21:17 PM
 */
public class DOWorkflow implements RDEPlugin {
    private Resources parentRecord = null; // The parent resource record
    private ResourcesCommon resourcesCommon = null; // This is either a resource component or resource record

    /**
     * The default constructor
     */
    public DOWorkflow() {

    }

    public void setModel(Resources parentRecord, ResourcesCommon resourcesCommon) {
        this.parentRecord = parentRecord;
        this.resourcesCommon = resourcesCommon;
    }

    /**
     * Method to specify if this plugin display a dialog or just executes a task
     *
     * @return true if it displays a dialog, false otherwise
     */
    public boolean hasDialog() {
        return true;
    }

    /**
     * Method to execute a task. It's not used here
     */
    public void doTask() { }

    /**
     * Display the dialog that allows importing of digital objects or exporting of box list
     *
     * @param owner The dialog from which this was opened
     * @param title The title of the dialog
     */
    public void showPlugin(JDialog owner, String title) {
        DOWorkflowDialog workflowDialog = new DOWorkflowDialog(owner, title, parentRecord, resourcesCommon);
        workflowDialog.pack();
        workflowDialog.setVisible(true);

    }
}
