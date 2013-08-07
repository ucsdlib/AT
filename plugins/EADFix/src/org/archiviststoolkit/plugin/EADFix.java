package org.archiviststoolkit.plugin;

import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.java.plugin.Plugin;
import org.archiviststoolkit.plugin.ATPlugin;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.dialog.ATFileChooser;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.swing.SimpleFileFilter;
import org.archiviststoolkit.swing.ATProgressUtil;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.editor.ArchDescriptionFields;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.io.File;

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
 *
 * Archivists' Toolkit(TM)
 * http://www.archiviststoolkit.org
 * info@archiviststoolkit.org
 *
 * A simple plugin to fix problems with EAD files
 *
 * Created by IntelliJ IDEA.
 *                  
 * @author: Nathan Stevens
 * Date: Feb 10, 2009
 * Time: 1:07:45 PM
 */

public class EADFix extends Plugin implements ATPlugin {
    protected ApplicationFrame mainFrame;

    // the default constructor
    public EADFix() { }

    // get the category this plugin belongs to
    public String getCategory() {
        return ATPlugin.DEFAULT_CATEGORY;
    }

    // get the name of this plugin
    public String getName() {
        return "EAD Fix 1.1 (2/23/2012)";
    }

    // Method to set the main frame
    public void setApplicationFrame(ApplicationFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    // Method that display the main plugin window. not used here
    public void showPlugin() { }

    // method to display a plugin that needs a parent frame
    public void showPlugin(Frame owner) { }

    // method to display a plugin that needs a parent dialog
    public void showPlugin(Dialog owner) { }

    // Method to return the jpanels for plugins that are in an AT editor
    public HashMap getEmbeddedPanels() {
        return null;
    }

    public HashMap getRapidDataEntryPlugins() {
        return null;
    }

    // Method to set the editor field component
    public void setEditorField(ArchDescriptionFields editorField) { }

    public void setEditorField(DomainEditorFields domainEditorFields) { }

    /**
     * Method to set the domain object for this plugin
     */
    public void setModel(DomainObject domainObject, InfiniteProgressPanel monitor) { }

    /**
     * Method to get the table from which the record was selected
     * @param callingTable The table containing the record
     */
    public void setCallingTable(JTable callingTable) { }

    /**
     * Method to set the selected row of the calling table
     * @param selectedRow
     */
    public void setSelectedRow(int selectedRow) { }

    /**
     * Method to set the current record number along with the total number of records
     * @param recordNumber The current record number
     * @param totalRecords The total number of records
     */
    public void setRecordPositionText(int recordNumber, int totalRecords) { }

    // Method to do a specific task in the plugin
    public void doTask(final String task) {
        final ATFileChooser filechooser = new ATFileChooser(new SimpleFileFilter(".xml"));

        if (filechooser.showOpenDialog(mainFrame, "Open EAD") == JFileChooser.APPROVE_OPTION) {
            Thread performer = new Thread(new Runnable() {
                public void run() {
                    InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 0, true);
                    monitor.start("Opening EAD Document ...");
                    if(task.equals("Assign Persistent IDs")) {
                        EADFixHelper.assignPersistentIDs(filechooser.getSelectedFile(), monitor);
                    }
                    else {
                        System.out.println("Unknown task in EADFix " + task);
                    }

                    // display message informing user that their task has been completed
                    if(!monitor.isProcessCancelled()) {
                        JOptionPane.showMessageDialog(mainFrame,
                                "Task " + task + " completed ...",
                                "Task Completed", JOptionPane.INFORMATION_MESSAGE);
                    }

                    monitor.close();
                }
            }, "Fix EAD file");
            performer.start();
        }
    }

    public boolean doTask(String s, String[] strings) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    // Method to get the list of specific task the plugin can perform
    public String[] getTaskList() {
        String[] tasks = {"Assign Persistent IDs"};
        return tasks;
    }

    // Method to return the editor type for this plugin
    public String getEditorType() {
        return null;
    }

    // code that is executed when plugin starts. not used here
    protected void doStart()  { }

    // code that is executed after plugin stops. not used here
    protected void doStop()  { }

    // main method for testing only
    public static void main(String[] args) {
        EADFix eadFix = new EADFix();
        eadFix.doTask("Assign Persistent IDs");
    }
}
