package org.archiviststoolkit.plugin.demo1;

import org.java.plugin.Plugin;
import org.archiviststoolkit.plugin.ATPlugin;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.importer.ImportFactory;
import org.archiviststoolkit.editor.ArchDescriptionFields;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.mydomain.DomainEditorFields;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ValueHolder;

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
 * A simple plugin to test the functionality of 
 *
 * Created by IntelliJ IDEA.
 *                  
 * @author: Nathan Stevens
 * Date: Feb 10, 2009
 * Time: 1:07:45 PM
 */

public class ATPluginDemo1 extends Plugin implements ATPlugin {
    protected ApplicationFrame mainFrame;
    private ATPluginDemo1Frame frame;
    protected DomainEditorFields editorField;
    protected DomainObject record;

    // the default constructor
    public ATPluginDemo1() { }

    // get the category this plugin belongs to
    public String getCategory() {
        //return ATPlugin.IMPORT_CATEGORY + " " + ATPlugin.EMBEDDED_EDITOR_CATEGORY;
        //return ATPlugin.EDITOR_CATEGORY;
        //return ATPlugin.DEFAULT_CATEGORY + " " + ATPlugin.EMBEDDED_EDITOR_CATEGORY;
        //return ATPlugin.IMPORT_CATEGORY;
        return ATPlugin.EMBEDDED_EDITOR_CATEGORY;
        //return ATPlugin.TOOL_CATEGORY;
    }

    // get the name of this plugin
    public String getName() {
        return "AT Demo Editor";
    }

    // Method to set the main frame
    public void setApplicationFrame(ApplicationFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    // Method that display the window
    public void showPlugin() {
        frame = new ATPluginDemo1Frame();
        frame.setApplicationFrame(mainFrame);
        frame.setVisible(true);
    }

    // method to display a plugin that needs a parent frame
    public void showPlugin(Frame owner) {
        MyEditorDialog1 dialog = new MyEditorDialog1(owner);
        dialog.setModel(record);
        dialog.setVisible(true);
    }

    // method to display a plugin that needs a parent dialog
    public void showPlugin(Dialog owner) {
        MyEditorDialog1 dialog = new MyEditorDialog1(owner);
        dialog.setModel(record);
        dialog.setVisible(true);
    }

    // Method to return the jpanels for plugins that are in an AT editor
    public HashMap getEmbeddedPanels() {
        HashMap<String, JPanel> panels = new HashMap<String,JPanel>();

        // load the details model if nessary
        if(editorField != null) {
            panels.put("My Editor 1::0::no", new MyEditorPanel1(editorField.detailsModel));
            //panels.put("My Editor 2::0::main", new MyEditorPanel1(editorField.detailsModel)); // replace the main subject
        }

        return panels;
    }

    // Method to set the editor field component
    public void setEditorField(ArchDescriptionFields editorField) {
        this.editorField = editorField;
        System.out.println("We are seting domain editor ...");
    }

    public void setEditorField(DomainEditorFields domainEditorFields) {
        this.editorField = domainEditorFields;
        System.out.println("We are seting domain editor ...");
    }

    /**
     * Method to set the domain object for this plugin
     */
    public void setModel(DomainObject domainObject, InfiniteProgressPanel monitor) {
        record = domainObject;
    }

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
    public void doTask(String task) {
        frame = new ATPluginDemo1Frame(this);
        frame.textArea1.setText("Doing Task : " + task + "\n\n");
        frame.setApplicationFrame(mainFrame);
        frame.setVisible(true);
    }

    // Method to get the list of specific task the plugin can perform
    public String[] getTaskList() {
        String[] tasks = {"Import 1", "Import 2", "Import 3"};
        return tasks;
    }

    // Method to return the editor type for this plugin
    public String getEditorType() {
        /*String editorType = ATPlugin.ACCESSION_EDITOR + " " +
                ATPlugin.RESOURCE_COMPONENT_EDITOR + " " +
                ATPlugin.DIGITALOBJECT_EDITOR + " " +
                ATPlugin.RESOURCE_EDITOR + " " +
                ATPlugin.NAME_EDITOR;*/

        //String editorType = ATPlugin.NAME_EDITOR + " " + ATPlugin.SUBJECT_EDITOR;
        String editorType = ATPlugin.RESOURCE_EDITOR;

        return editorType;
    }

    // code that is executed when plugin starts. not used here
    protected void doStart()  {
        //System.out.println("Loading custom MARC ingest");
        //ImportFactory.getInstance().setMarcIngest(new MyMARCIngest());        
    }

    // code that is executed after plugin stops. not used here
    protected void doStop()  { }

    // main method for testing only
    public static void main(String[] args) {
        ATPluginDemo1 demo = new ATPluginDemo1();
        demo.showPlugin();
    }
}
