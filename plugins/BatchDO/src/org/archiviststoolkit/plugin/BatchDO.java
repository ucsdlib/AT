package org.archiviststoolkit.plugin;

import org.archiviststoolkit.plugin.utils.NaiveTrustProvider;
import org.java.plugin.Plugin;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.mydomain.DomainTableWorkSurface;
import org.archiviststoolkit.mydomain.ResourcesDAO;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.swing.SimpleFileFilter;
import org.archiviststoolkit.swing.ATProgressUtil;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.dialog.ATFileChooser;
import org.archiviststoolkit.editor.ArchDescriptionFields;
import org.archiviststoolkit.model.Resources;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.io.File;

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
 * This is a very simple plugin that reads in a csv file containing information
 * that will be used to create digital objects which will be attached to the
 * selected resource record.
 *
 *
 * @author: Nathan Stevens
 * @modified: J. Varghese
 * Date: April 20, 2009
 * Time: 12:10:04 PM
 */
public class BatchDO extends Plugin implements ATPlugin {
    protected ApplicationFrame mainFrame;
    protected HashMap rdePlugins = new HashMap();

    /**
     * Get the category this plugin belongs to
     *
     * @return The category of this plugin
     */
    public String getCategory() {
        return ATPlugin.IMPORT_CATEGORY + " " +
               ATPlugin.EMBEDDED_EDITOR_CATEGORY;
    }

    /**
     * Get the name of the plugin
     *
     * @return The name of this plugin
     */
    public String getName() {
        return "Import Digital Objects (NYU) 2.2";
    }

    // Method to set the AT main frame
    public void setApplicationFrame(ApplicationFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    // Method to display the file chooser dialog 
    public void showPlugin() {
        final ATFileChooser filechooser = new ATFileChooser(new SimpleFileFilter(".txt"));

        if (filechooser.showOpenDialog(mainFrame, "Import") == JFileChooser.APPROVE_OPTION) {
            Thread performer = new Thread(new Runnable() {
                public void run() {
                    InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000, false);
                    monitor.start("Importing Digital Objects...");

                    try {
                        // try loading the currently resorce record
                        DomainTableWorkSurface worksurface = mainFrame.getWorkSurfaceContainer().getCurrentWorkSurface();
                        Resources record = (Resources)worksurface.getCurrentDomainObjectFromDatabase();

                        // if resource is not null then create and and try attach digital objects to it
                        if(record != null) {
                            File file = filechooser.getSelectedFile();
                            DOCreatorNYU doCreator = new DOCreatorNYU(mainFrame.getAtVersionNumber());
                            doCreator.createAndAttachDigitalObjects(file, monitor, record);

                            // now save the resource record
                            // update the progress monitor
                            monitor.setTextLine("Saving Resource Record", 2);
                            monitor.setTextLine("", 4);
                            
                            ResourcesDAO access = new ResourcesDAO();
                            access.updateLongSession(record);
                        } else {
                            monitor.close();
                            JOptionPane.showMessageDialog(mainFrame,
                                    "Please select a Resource record ...",
                                    "No Record Selected",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (ClassCastException e) { // any problem with loading the record
                        monitor.close();
                        JOptionPane.showMessageDialog(mainFrame,
                                "The record selected is not a Resource record ...",
                                "Error Loading Record",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (Exception e) { // any other problem (opening file, etc)
                        monitor.close();
                        e.printStackTrace();
                        
                        JOptionPane.showMessageDialog(mainFrame,
                                "There was a problem procesing the specified file",
                                "Error Loading File",
                                JOptionPane.ERROR_MESSAGE);
                    } finally {
                        monitor.close();
                    }

                }
            }, "Import NYU Digital Objects");
            performer.start();
        }
    }

    // method to display a plugin that needs a parent frame
    public void showPlugin(Frame owner) { }

    // method to display a plugin that needs a parent dialog
    public void showPlugin(Dialog owner) { }

    // Method to return any embedded plugins. This can be any thing
    public HashMap getEmbeddedPanels() {
        return null;  
    }

    // Method to return hashmap containing list of RDE plugins
    public HashMap getRapidDataEntryPlugins() {
        rdePlugins.put("DO Workflow", new DOWorkflow());
        rdePlugins.put("Barcode Cleaner", new BarcodeCleaner());
        return rdePlugins;
    }

    // Method to set the editor field. This is not used here
    public void setEditorField(ArchDescriptionFields editorField) {  }

    // Method to set the editor field. This is not used here
    public void setEditorField(DomainEditorFields editorField) {  }

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
    public void doTask(String task) { }

    // Method to specify a specific task passing in parameters
    public boolean doTask(String task, String[] taskParams) {
        return false;
    }

    // Method to get the list of specific task the plugin can perform
    public String[] getTaskList() {
        return null;
    }

    // Method to return the editor type for this plugin
    public String getEditorType() {
        return ATPlugin.RAPID_DATA_ENTRY_EDITOR;
    }

    // code that is executed when plugin starts. It basically allows
    // ssl to be used without throwing an error
    protected void doStart()  {
        NaiveTrustProvider.setAlwaysTrust(true);
    }

    // code that is executed after plugin stops. not used here
    protected void doStop()  { }

    // main method for running this plugin as a stand alone application
    public static void main(String[] args) {
        // set up ssl to accept any certificate
        NaiveTrustProvider.setAlwaysTrust(true);

        // launch the main window
        BatchDOFrame frame = new BatchDOFrame();
        frame.setVisible(true);
    }
}
