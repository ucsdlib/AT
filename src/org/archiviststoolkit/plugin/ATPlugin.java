package org.archiviststoolkit.plugin;

import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.editor.ArchDescriptionFields;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.mydomain.DomainEditorFields;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * Archivists' Toolkit(TM) Copyright 2005-2009 Regents of the University of California, New York University, & Five Colleges, Inc.
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
 * The simple plugin interface which all AT Plugins need to be implement in
 * order to be loaded into the AT
 * <p/>
 * Created by IntelliJ IDEA.
 *
 * @author: Nathan Stevens
 * Date: Feb 10, 2009
 * Time: 11:03:24 AM
 */
public interface ATPlugin {
    // plugin will be displayed under the plugin menu
    final String DEFAULT_CATEGORY = "default";

    // plugin will be displayed under the import menu
    final String IMPORT_CATEGORY = "import";

    // plugin will be displayed under the tool menu
    final String TOOL_CATEGORY = "tool";

    // plugin will be used to view records instead of the default editor
    final String VIEWER_CATEGORY = "view";

    // plugin will be used to view/edit records instead of the default editor
    final String EDITOR_CATEGORY = "edit";

    // plugin will be used embedded into an existing domain editor
    final String EMBEDDED_EDITOR_CATEGORY = "embedded";

    // plugin will be used with the command line interface of the AT. This functionality will require additional code
    final String CLI_CATEGORY = "cli";

    // plugin will be used for user authentication using an external authentication system
    final String AUTHENTICATION_CATEGORY  = "authentication";

    /*
    The list of editor types. They can be combined to specify a plugin that
    supports viewing and editing multiple type of records. For example, a
    developer can choose to create a plugin that supports viewing and
    editing of Names, Subjects, and Digital Object Records with the following
    string:
    editorType = NAMES_EDITOR + " " + SUBJECT_EDITOR + " " + DIGITALOBJECT_EDITOR;
    */

    // plugin that can edit or view resource records
    final String RESOURCE_EDITOR = "resource";

    // plugin that can edit or view resource components
    final String RESOURCE_COMPONENT_EDITOR = "component";

    // plugin that can edit or view digital object records
    final String DIGITALOBJECT_EDITOR = "digital";

    // plugin that can edit or view name records
    final String NAME_EDITOR = "name";

    // plugin that can edit or view subject records
    final String SUBJECT_EDITOR = "subject";

    // plugin that can edit or view subject records
    final String ACCESSION_EDITOR = "accession";

    //plugin that can edit or view instant records
    final String INSTANCE_EDITOR = "instance";

    //plugin that can edit or view assessment records
    final String ASSESSMENT_EDITOR = "assessment";

    //plugin that loads in the RDE drop down menu
    final String RAPID_DATA_ENTRY_EDITOR = "rde";

    // plugin that can edit or view all the main AT records.
    final String ALL_EDITOR = "all";

    /**
     * Method to get the category of the plugin
     * i.e. default, import, view or edit
     *
     * @return String that specify the category type
     */
    public String getCategory();

    /**
     * Method to return the name of the plugin. If the plugin category
     * is either import or default, this name appears in the menu
     *
     * @return Returns the plugin names
     */
    public String getName();

    /**
     * Method to set the application frame. In the AT, the application
     * frame provides a means to access the current worksurface and hence
     * the displayed records. This method is called every time a plugin is
     * selected in the menu.
     *
     * @param mainFrame The main AT application frame
     */
    public void setApplicationFrame(ApplicationFrame mainFrame);

    /**
     * Method to display the plugin or do anything else the plugin
     * requires when selected from the plugin or import menu, if
     * it doesn't define a task list.
     */
    public void showPlugin();

    /**
     * Method to display a plugin that needs a parent frame.
     * This method is not currently used in the AT so it
     * can be left blank.
     *
     * @param owner The parent frame of this plugin
     */
    public void showPlugin(Frame owner);

    /**
     * Method to display a plugin that needs a parent dialog.
     * Not currently used in the AT, so it can be left blank.
     *
     * @param owner The parent dialog of this plugin
     */
    public void showPlugin(Dialog owner);

    /**
     * Method to return a hashmap containing jpanels for plugins that are not
     * dialog or frames. This is used for plugins that are to be embedded
     * in a Domain Editor. The hasmap is keyed using the plugin names.
     * <p/>
     * The format of the plugin name can also be used to specify the location
     * and whether to remove a planel already at that location.
     * <p/>
     * Format to use is panel_name::location::yes, no, main
     * Examples >>
     * New Editor Panel::0::yes (replace panel at index zero with this one)
     * New Editor Panel::0::no (just insert the panel at zero)
     * New Editor Panel::0::main (In Subjects editor only remove all other
     * components in the panel and add this panel)
     *
     * @return The HashMap containing JPanels and their display name
     */
    public HashMap getEmbeddedPanels();

    /**
     * Method to return hashmap containing RDEPlugin objects which are listed
     * under the RDE drop down menu in the resources editor. Such plugins
     * may launch a dialog or may just execute the some logic agains the
     * currently selected respurce component or resource record.
     *
     * @return The HashMap containing RDEPlugins and display names
     */
    public HashMap getRapidDataEntryPlugins();

    /**
     * Method to set the editor field. This is used by embeddable
     * plugins so that they can gain access to public method of the
     * editor field.
     *
     * @param editorField The editor field the plugin is embedded into
     */
    public void setEditorField(ArchDescriptionFields editorField);

    /**
     * Method to set the editor field. This is used by embeddable
     * plugins so that they can gain access to public method of the
     * editor field.
     *
     * @param editorField The editor field the plugin is embedded into
     */
    public void setEditorField(DomainEditorFields editorField);

    /**
     * Method to do a specific task in the plugin. This method is
     * implemented by plugins that are can do multiple task.
     * For example an importer for multiple file types. Its called
     * each time a plugin that defines a task list is selected
     * from the import or plugin menu.
     *
     * @param task The task for the plugin to do
     */
    public void doTask(String task);

    /**
     * Method to do a specific task which takes a list of parameters
     * in a string array and returns a boolean indicating the
     * status of the task
     *
     * @param task The task to do
     * @param taskParams The parameters
     * @return boolean indicating the status of the task
     */
    public boolean doTask(String task, String[] taskParams);

    /**
     * Method to get the list of specific task the plugin can perform.
     * This task are displayed in submenus under either the import or
     * plugin menu.
     *
     * @return A list of task this plugin can perform
     */
    public String[] getTaskList();

    /*
    * Methods below this point are to be implemented by plugins which
    * are used as DomainObject Viewers and/or Editors. The are always
    * called by the AT if the plugin category is defined as a
    * viewer or editor
    */

    /**
     * Method to return the type of domain objects this plugin can edit or
     * view. This method is used by plugins that are implemented as an editor
     * for one or more of the main domain objects such Names, Subjects,
     * Accessions, Resources, and Digital Objects. Plugins that supports
     * the supports the viewing and/or editing of multiple record types
     * can be specify in the following manner: A plugin that supports viewing
     * and/or editing of Names, Subjects, and Digital Object Records
     * will return the following String:
     * editorType = NAMES_EDITOR + " " + SUBJECT_EDITOR + " " + DIGITALOBJECT_EDITOR
     * If a plugin returns ALL_EDITOR it means it can view
     * and/or edit all main AT records.
     *
     * @return The type or types of records an editor/viewer plugin can open.
     */
    public String getEditorType();

    /**
     * Method to set the domain model. This is always called by the AT
     *
     * @param domainObject The domain object
     * @param monitor      The progress monitor
     */
    public void setModel(DomainObject domainObject, InfiniteProgressPanel monitor);

    /**
     * Method to get the table from which the record was selected.
     *
     * @param callingTable The table containing the record
     */
    public void setCallingTable(JTable callingTable);

    /**
     * Method to set the selected row of the calling table. This lets the
     * plugin know the current row selection
     *
     * @param selectedRow The selected row
     */
    public void setSelectedRow(int selectedRow);

    /**
     * Method to set the current record number along with the total
     * number of records
     *
     * @param recordNumber The current record number
     * @param totalRecords The total number of records
     */
    public void setRecordPositionText(int recordNumber, int totalRecords);
}
