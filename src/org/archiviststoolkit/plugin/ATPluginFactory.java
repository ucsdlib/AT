package org.archiviststoolkit.plugin;

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
 * Created by IntelliJ IDEA.
 *
 * This class handles loading plugins for the AT. It uses the JPF plugin framework
 * http://jpf.sourceforge.net/
 *
 * @author: Nathan Stevens
 * Date: Feb 10, 2009
 * Time: 10:09:17 AM
 */

import org.java.plugin.PluginManager;
import org.java.plugin.ObjectFactory;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.standard.StandardPluginLocation;
import org.java.plugin.PluginManager.PluginLocation;
import org.archiviststoolkit.plugin.ATPlugin;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.model.*;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;


public class ATPluginFactory {
    private static ATPluginFactory singleton = null;
    private PluginManager pluginManager; // this is actually what manages the plugins
    private String[] cliParameters; // stores command line parameters

    /**
     * Default Constructor.
     */
    private ATPluginFactory() {
    }

    /**
     * Method to return the singleton of this class
     *
     * @return ATPluginFactory Singleton
     */
    public static ATPluginFactory getInstance() {
        if (singleton == null) {
            singleton = new ATPluginFactory();
        }
        return (singleton);
    }

    /**
     * Method to return the plugin manger incase a developer would like to make
     * use of it.
     *
     * @return The plugin manager
     */
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    /**
     * Method that loadsany plugins found in the plugin directory
     *
     * @return boolean indicating f any plugins were found
     */
    public boolean parsePluginDirectory() {
        File pluginDirectory = new File("plugins");
        if (!pluginDirectory.exists()) { // check to see if the directory actually exist before continuing
            return false;
        }

        // get any files which are zip and return those
        File[] plugins = pluginDirectory.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".zip");
            }
        });

        // load the plugins now
        pluginManager = ObjectFactory.newInstance().createManager();

        try {
            PluginLocation[] locations = new PluginLocation[plugins.length];

            for (int i = 0; i < plugins.length; i++) {
                locations[i] = StandardPluginLocation.create(plugins[i]);
            }

            pluginManager.publishPlugins(locations);
        } catch (Exception e) {
            System.out.println("Error loading plugins ...");
            e.printStackTrace();
            return false;
        }

        if (plugins.length > 0) { // check to see if any plugins were in the directory
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method to return an list of all the plugin names and ids in a hasmap
     *
     * @return HashMap containing the names and ids of all the plugins found
     */
    public HashMap getPluginNames() {
        HashMap<String, String> pluginNames = new HashMap<String, String>();

        try {
            Iterator it = pluginManager.getRegistry().getPluginDescriptors().iterator();

            while (it.hasNext()) {
                PluginDescriptor pluginDescriptor = (PluginDescriptor) it.next();
                String id = pluginDescriptor.getId();
                ATPlugin plugin = (org.archiviststoolkit.plugin.ATPlugin) pluginManager.getPlugin(id);
                String name = plugin.getName();
                pluginNames.put(id, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (pluginNames.size() == 0) {
            return null; // no plugins found so return null
        } else {
            return pluginNames;
        }
    }

    /**
     * Method to return the name of all plugins in a particular category.
     * It is used to add plugins to plugin menu in the main application frame
     *
     * @return HashMap containing the names and ids of all the plugins found
     */
    public HashMap getPluginNamesByCategory(String inCategory) {
        HashMap<String, String> pluginNames = new HashMap<String, String>();

        try {
            Iterator it = pluginManager.getRegistry().getPluginDescriptors().iterator();

            while (it.hasNext()) {
                PluginDescriptor pluginDescriptor = (PluginDescriptor) it.next();
                String id = pluginDescriptor.getId();
                ATPlugin plugin = (org.archiviststoolkit.plugin.ATPlugin) pluginManager.getPlugin(id);
                String name = plugin.getName();
                String category = plugin.getCategory();
                if (category.indexOf(inCategory) != -1) {
                    if (plugin.getTaskList() == null) { // no list of task so just return the id and name
                        pluginNames.put(id, name);
                    } else { // list of task so return the name::task1::task2::task3 etc ...
                        String nameWithTask = getNameWithTask(name, plugin.getTaskList());
                        pluginNames.put(id, nameWithTask);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (pluginNames.size() == 0) {
            return null; // no plugins in this category so return null
        } else {
            return pluginNames;
        }
    }

    /**
     * Method that combines the plugin name with any task the plugin has registered
     *
     * @param name     The name of the plugin
     * @param taskList The list of task
     * @return String with the combined name and task
     */
    private String getNameWithTask(String name, String[] taskList) {
        String nameWithTask = name;
        for (int i = 0; i < taskList.length; i++) {
            nameWithTask += "::" + taskList[i];
        }

        return nameWithTask;
    }

    /**
     * Method to return a plugin given an id. If it can't be found then null is returned
     *
     * @param id The id of the plugin to return
     * @return The ATPlugin found or null
     */
    public ATPlugin getPlugin(String id) {
        try {
            return (ATPlugin) pluginManager.getPlugin(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method to get a record viewer plugin
     *
     * @param domainObject The domain object to look for plugin for
     * @return The viewer plugin that is found or null
     */
    public ATPlugin getViewerPlugin(DomainObject domainObject) {
        return getATPlugin(domainObject, ATPlugin.VIEWER_CATEGORY);
    }

    /**
     * Method to get a record editor plugin
     *
     * @param domainObject The domain object to look for plugin for.
     * @return The editor plugin that is found or null
     */
    public ATPlugin getEditorPlugin(DomainObject domainObject) {
        return getATPlugin(domainObject, ATPlugin.EDITOR_CATEGORY);
    }

    /**
     * Method to return the ATPlugin for a particular and domain object
     *
     * @param domainObject The domain object to find a plugin for
     * @param category     The category to find a domain object for
     * @return The plugin found or null if none can be located.
     */
    private ATPlugin getATPlugin(DomainObject domainObject, String category) {
        String editorType = getEditorTypeForDomainObject(domainObject);

        if (editorType != null) {
            ATPlugin foundPlugin = null;

            try {
                Iterator it = pluginManager.getRegistry().getPluginDescriptors().iterator();

                while (it.hasNext()) {
                    PluginDescriptor pluginDescriptor = (PluginDescriptor) it.next();
                    String id = pluginDescriptor.getId();
                    ATPlugin plugin = (org.archiviststoolkit.plugin.ATPlugin) pluginManager.getPlugin(id);

                    String cat = plugin.getCategory();
                    String et = plugin.getEditorType();

                    if (cat.indexOf(category) != -1 && et.indexOf(editorType) != -1) {
                        foundPlugin = plugin;
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return foundPlugin;
        } else {
            return null;
        }
    }

    /**
     * Method to get a list of embedded plugins that will be added as a tab
     * to the current resource editor
     *
     * @return Returns an ArrayList containing any plugins found
     */
    public ArrayList<ATPlugin> getEmbeddedResourceEditorPlugins() {
        return getEmbeddedEditorPlugins(ATPlugin.RESOURCE_EDITOR, ATPlugin.EMBEDDED_EDITOR_CATEGORY);
    }

    /**
     * Method to get a list of embedded plugins that will be added as a tab
     * to the current resource component editor
     *
     * @return an ArrayList containing any plugins found
     */
    public ArrayList<ATPlugin> getEmbeddedResourceComponentEditorPlugins() {
        return getEmbeddedEditorPlugins(ATPlugin.RESOURCE_COMPONENT_EDITOR, ATPlugin.EMBEDDED_EDITOR_CATEGORY);
    }

    /**
     * Method to get a list of embedded plugins that will be added as a tab
     * to the current digital object editor
     *
     * @return an ArrayList containing any plugins found
     */
    public ArrayList<ATPlugin> getEmbeddedDigitalObjectEditorPlugins() {
        return getEmbeddedEditorPlugins(ATPlugin.DIGITALOBJECT_EDITOR, ATPlugin.EMBEDDED_EDITOR_CATEGORY);
    }

    /**
     * Method to get a list of embedded plugins that will be added as a tab
     * to the current names editor
     *
     * @return an ArrayList containing any plugins found
     */
    public ArrayList<ATPlugin> getEmbeddedNameEditorPlugins() {
        return getEmbeddedEditorPlugins(ATPlugin.NAME_EDITOR, ATPlugin.EMBEDDED_EDITOR_CATEGORY);
    }

    /**
     * Method to get a list of embedded plugins that will be added as a tab
     * to the current subjects editor
     *
     * @return an ArrayList containing any plugins found
     */
    public ArrayList<ATPlugin> getEmbeddedSubjectEditorPlugins() {
        return getEmbeddedEditorPlugins(ATPlugin.SUBJECT_EDITOR, ATPlugin.EMBEDDED_EDITOR_CATEGORY);
    }

    /**
     * Method to get a list of embedded plugins that will be added as a tab
     * to the current accessions editor
     *
     * @return an ArrayList containing any plugins found
     */
    public ArrayList<ATPlugin> getEmbeddedAccessionEditorPlugins() {
        return getEmbeddedEditorPlugins(ATPlugin.ACCESSION_EDITOR, ATPlugin.EMBEDDED_EDITOR_CATEGORY);
    }

    /**
     * Method to get a list of embedded plugins that will be added as a tab
     * to the current assessments editor
     *
     * @return an ArrayList containing any plugins found
     */
    public ArrayList<ATPlugin> getEmbeddedAssessmentEditorPlugins() {
        return getEmbeddedEditorPlugins(ATPlugin.ASSESSMENT_EDITOR, ATPlugin.EMBEDDED_EDITOR_CATEGORY);
    }

    /**
     * Method to get a list of embedded plugins that will be added as entries
     * in the rapid data entry drop down menu
     *
     * @return An Arraylist containing any plugins found
     */
    public ArrayList<ATPlugin> getRapidDataEntryPlugins() {
        String category = ATPlugin.EMBEDDED_EDITOR_CATEGORY;
        String editorType = ATPlugin.RAPID_DATA_ENTRY_EDITOR;

        ArrayList<ATPlugin> foundPlugins = new ArrayList<ATPlugin>();

        try {
            Iterator it = pluginManager.getRegistry().getPluginDescriptors().iterator();

            while (it.hasNext()) {
                PluginDescriptor pluginDescriptor = (PluginDescriptor) it.next();
                String id = pluginDescriptor.getId();
                ATPlugin plugin = (org.archiviststoolkit.plugin.ATPlugin) pluginManager.getPlugin(id);

                String cat = plugin.getCategory();
                String et = plugin.getEditorType();

                // check to make sure that the plugin is of the right category and
                // editor type and that it has a panel that can be embedded into
                // the resource editor panel
                if (cat.indexOf(category) != -1 && et.indexOf(editorType) != -1 &&
                        plugin.getRapidDataEntryPlugins() != null) {
                    foundPlugins.add(plugin);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return foundPlugins;
    }

    /**
     * Method to return an array list containing plugins of a certain editor type
     * and category
     *
     * @param editorType The editor type
     * @param category   The category
     * @return An array list of any plugins found
     */
    public ArrayList<ATPlugin> getEmbeddedEditorPlugins(String editorType, String category) {
        ArrayList<ATPlugin> foundPlugins = new ArrayList<ATPlugin>();

        try {
            Iterator it = pluginManager.getRegistry().getPluginDescriptors().iterator();

            while (it.hasNext()) {
                PluginDescriptor pluginDescriptor = (PluginDescriptor) it.next();
                String id = pluginDescriptor.getId();
                ATPlugin plugin = (org.archiviststoolkit.plugin.ATPlugin) pluginManager.getPlugin(id);

                String cat = plugin.getCategory();
                String et = plugin.getEditorType();

                // check to make sure that the plugin is of the right category and
                // editor type and that it has a panel that can be embedded into
                // the resource editor panel
                if (cat.indexOf(category) != -1 && et.indexOf(editorType) != -1 &&
                        plugin.getEmbeddedPanels() != null) {
                    foundPlugins.add(plugin);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return foundPlugins;
    }

    /**
     * Method to return any command line plugins
     *
     * @return ArrayList containing any command line plugins that were found
     */
    public ArrayList<ATPlugin> getCLIPlugins() {
        ArrayList<ATPlugin> foundPlugins = new ArrayList<ATPlugin>();

        try {
            Iterator it = pluginManager.getRegistry().getPluginDescriptors().iterator();

            while (it.hasNext()) {
                PluginDescriptor pluginDescriptor = (PluginDescriptor) it.next();
                String id = pluginDescriptor.getId();
                ATPlugin plugin = (org.archiviststoolkit.plugin.ATPlugin) pluginManager.getPlugin(id);

                String cat = plugin.getCategory();

                // check to make sure that the plugin is of the right category
                if (cat.indexOf(ATPlugin.CLI_CATEGORY) != -1) {
                    foundPlugins.add(plugin);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return foundPlugins;
    }

    /**
     * Method to return any authentication plugins found
     *
     * @return The authentication plugin that was found
     */
    public ATPlugin getAuthenticationPlugin() {
        ATPlugin foundPlugin = null;

        try {
            Iterator it = pluginManager.getRegistry().getPluginDescriptors().iterator();

            while (it.hasNext()) {
                PluginDescriptor pluginDescriptor = (PluginDescriptor) it.next();
                String id = pluginDescriptor.getId();
                ATPlugin plugin = (org.archiviststoolkit.plugin.ATPlugin) pluginManager.getPlugin(id);

                String cat = plugin.getCategory();

                // check to make sure that the plugin is of the right category
                if (cat.indexOf(ATPlugin.AUTHENTICATION_CATEGORY) != -1) {
                    foundPlugin = plugin;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return foundPlugin;
    }

    /**
     * Method to return the editor type based on the domain object
     *
     * @param domainObject The domain object to find the editor type for
     * @return The editor type or null if no editor can be found for this domain object
     */
    private String getEditorTypeForDomainObject(DomainObject domainObject) {
        if (domainObject instanceof Names) {
            return ATPlugin.NAME_EDITOR;
        } else if (domainObject instanceof Subjects) {
            return ATPlugin.SUBJECT_EDITOR;
        } else if (domainObject instanceof Accessions) {
            return ATPlugin.ACCESSION_EDITOR;
        } else if (domainObject instanceof Resources) {
            return ATPlugin.RESOURCE_EDITOR;
        } else if (domainObject instanceof DigitalObjects || domainObject instanceof ArchDescriptionDigitalInstances) {
            return ATPlugin.DIGITALOBJECT_EDITOR;
        } else if (domainObject instanceof ArchDescriptionAnalogInstances) {
            return ATPlugin.INSTANCE_EDITOR;
        } else { // no editor type found so return null
            return null;
        }
    }

    /**
     * Method to return any command line parameters
     *
     * @return String Array containing command line parameters
     */
    public String[] getCliParameters() {
        return cliParameters;
    }

    /**
     * Method to set any command line parameters. This is only used by command line programs
     *
     * @param cliParameters
     */
    public void setCliParameters(String[] cliParameters) {
        this.cliParameters = cliParameters;
    }
}
