package org.archiviststoolkit;

import org.archiviststoolkit.exceptions.UnsupportedDatabaseType;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.plugin.ATPlugin;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.DefaultValues;
import org.archiviststoolkit.util.*;

import java.sql.SQLException;
import java.util.ArrayList;


/**
 * This class is used when AT is running in command line mode.
 * It bascially just loads CLI plugins. The plugins then handle
 * the specific task
 * <p/>
 * Created by IntelliJ IDEA.
 * User: nathan
 * Date: Apr 16, 2010
 * Time: 2:20:47 PM
 */
public class MainCLI {
    public static void main(final String[] args) {
        // check the to make sure we have at least 2 arguments.
        // args[0] = database password
        // args[1] = plugin task
        if (args.length < 2) {
            System.out.println("Missing Input Parameters");
            System.out.println("Usage : atcli \"database password\" \"plugin task\"");
            System.exit(1);
        }

        //get user preferences
        UserPreferences userPrefs = UserPreferences.getInstance();
        userPrefs.populateFromPreferences();

        // set the database password to make sure no plugin bypassess the login mechanism
        userPrefs.setDatabasePassword(args[0]);

        // now bybass AT login since it assume that CLI will be used for command line.
        // Will have to change that in the future to a sure it's not abused
        if (!userPrefs.checkForDatabaseUrl()) {
            System.out.println("This appears to be the first time the AT was launched. \n" +
                    "Please fill out the database connection information");
            System.exit(1);
        }

        // start up hibernate
        try {
            userPrefs.updateSessionFactoryInfo();
        } catch (UnsupportedDatabaseType unsupportedDatabaseType) {
            System.out.println("Error connecting to database ...");
            System.exit(1);
        }

        // try connecting to the database
        try {
            connectAndTest(userPrefs);
        } catch (UnsupportedDatabaseType unsupportedDatabaseType) {
            System.out.println("Error connecting to database " + unsupportedDatabaseType);
            System.exit(1);
        }

        // Load the Lookup List
        if (!LookupListUtils.loadLookupLists()) {
            System.out.println("Failed to Load the Lookup List");
            System.exit(1);
        }

        // Loading Notes Etc. Types
        if (!NoteEtcTypesUtils.loadNotesEtcTypes()) {
            System.out.println("Failed to Load Notes Etc. Types");
            System.exit(1);
        }

        System.out.println("Loading Field Information");
        ATFieldInfo.loadFieldList();

        System.out.println("Loading Location Information");
        LocationsUtils.initLocationLookupList();

        System.out.println("Loading Default Value Information");
        DefaultValues.initDefaultValueLookup();

        System.out.println("Loading In-line tags");
        InLineTagsUtils.loadInLineTags();

        // load any plugins that are found here
        System.out.println("Loading CLI plugins");
        org.archiviststoolkit.plugin.ATPluginFactory.getInstance().parsePluginDirectory();

        // pass any input parameters to the Plugin Factory so plugins can have access to it
        org.archiviststoolkit.plugin.ATPluginFactory.getInstance().setCliParameters(args);

        // running the plugin now by calling the doTask method, but first check to see if the plugin task
        String task = args[1];

        // process returned plugins and see which of them is register to do this task
        boolean foundPlugin = false;

        ArrayList<ATPlugin> plugins = org.archiviststoolkit.plugin.ATPluginFactory.getInstance().getCLIPlugins();
        for (ATPlugin plugin : plugins) {
            String registeredTask = plugin.getTaskList()[0];
            if (registeredTask.indexOf(task) != -1) {
                System.out.println("\nRunning " + plugin.getName() + " for task: " + task);
                plugin.doTask(task);
                foundPlugin = true;
            }
        }

        // if no plugin found then alert the user
        if (!foundPlugin) {
            System.out.println("\nNo CLI Plugin found to run task: " + task);
        }
    }

    private static void connectAndTest(UserPreferences userPrefs) throws UnsupportedDatabaseType {
        while (!DatabaseConnectionUtils.testDbConnection()) {
            System.out.println("");
            System.exit(1);
        }

        try {
            while (!DatabaseConnectionUtils.checkVersion(DatabaseConnectionUtils.CHECK_VERSION_FROM_MAIN)) {
                System.out.println("Wrong database version connection");
                System.exit(1);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("The jdbc driver is missing");
            e.printStackTrace();
        }

        try {
            SessionFactory.testHibernate();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
