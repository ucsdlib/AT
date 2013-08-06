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
 * @author Lee Mandell
 * Date: Jan 20, 2007
 * Time: 3:56:02 PM
 */

package org.archiviststoolkit.util;

import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.swing.VersionMismatch;

import java.sql.*;
import java.util.*;
import java.io.*;

public class DatabaseConnectionUtils {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("org.archiviststoolkit.resources.messages.messages");
    private static String versionString = resourceBundle.getString("archiviststoolkit.releasenumber");

    public static final String CHECK_VERSION_FROM_MAIN = "main";
    public static final String CHECK_VERSION_FROM_UPGRADE = "upgrade";
    public static final String CHECK_VERSION_FROM_UTILITIES = "utilities";

    private static String errorString = "";

    private static final String CONNECTION_INFO_FILENAME = "atdbinfo.txt";

    private static String databaseVersionString = "";

    public static Boolean testDbConnection() {

        try {
            Connection conn = openConnection();
            String dummySQL = "SELECT count(*) FROM Constants";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(dummySQL);
            rs.close();
            stmt.close();
            return true;
            // everything is ok
        }
        catch (SQLException se) {
            ErrorDialog dialog;
            if (se.getMessage().contains("Unknown database")) {
                dialog = new ErrorDialog("The database you are accessing does not exist. Please check the settings and try again.",
                        ErrorDialog.DIALOG_TYPE_ERROR);
            } else if (se.getMessage().contains("No suitable driver")) {
                dialog = new ErrorDialog("The database you are accessing is not a " + SessionFactory.getDatabaseType()
                        + " database. Please check the settings and try again.",
                        ErrorDialog.DIALOG_TYPE_ERROR);
            } else if (se.getMessage().contains("constants")) {
                dialog = new ErrorDialog("It appears that the database you are using has not been initialized."
                        + " Please contact your database administrator and make sure the database has been initialized using the " +
                        "AT Maintenance program.",
                        ErrorDialog.DIALOG_TYPE_ERROR);
            } else if (se.getMessage().contains("Access denied for user")) {
                dialog = new ErrorDialog("The username and password to not have permission for this database. Please check the settings and try again.",
                        ErrorDialog.DIALOG_TYPE_ERROR);
            } else {
                dialog = new ErrorDialog("There is a problem with the database connection. Please check the settings and try again",
                        StringHelper.getStackTrace(se));
            }
            dialog.showDialog();
            return false;
        } catch (ClassNotFoundException e) {
            ErrorDialog dialog = new ErrorDialog("The jdbc driver is missing",
                    StringHelper.getStackTrace(e), ErrorDialog.DIALOG_TYPE_ERROR);
            dialog.showDialog();
            return false;
        }

    }


    public static Boolean checkVersion(String calledFrom) throws ClassNotFoundException, SQLException {

        Connection conn = openConnection();
        DatabaseVersion databaseVersion = getDatabaseVersionInfo(conn);
        int majorVersion = databaseVersion.getMajorVersion();
        int minorVersion = databaseVersion.getMinorVersion();
        int updateVersion = databaseVersion.getUpdateVersion();

        if (databaseVersion != null) {
            // set the database version
            databaseVersionString = majorVersion + "." + minorVersion + "." + updateVersion;

            Integer compareVersion = Constants.compareVersions(versionString,
                    majorVersion,
                    minorVersion,
                    updateVersion);
            if (calledFrom.equals(DatabaseConnectionUtils.CHECK_VERSION_FROM_MAIN)) {
                if (compareVersion == Constants.VERSION_LESS) {
                    /**
                    VersionMismatch dialog = new VersionMismatch("AT version " + versionString
                            + " will not work with this database. This database is using version "
                            + majorVersion + "." + minorVersion + "." + updateVersion + ".", Constants.VERSION_LESS);
                    dialog.showDialog();
                     */

                    // to allow the AT to work with database that have been extended
                    // for derivative of the AT, then ignore the version mismatch if the
                    // database is at a greater version. Doing this is only valid
                    // if the data model of the derivative is a superset of the AT data model
                    return true;
                } else if (compareVersion == Constants.VERSION_GREATER) {
                    VersionMismatch dialog = new VersionMismatch("AT version " + versionString
                            + " will not work with this database. This database is using version "
                            + majorVersion + "." + minorVersion + "." + updateVersion + ".", Constants.VERSION_GREATER);
                    dialog.showDialog();
                    return false;
                }
            } else if (calledFrom.equals(DatabaseConnectionUtils.CHECK_VERSION_FROM_UPGRADE)) {
                if (compareVersion == Constants.VERSION_LESS) {
                    errorString = "AT version " + versionString + " will not work with this database. This database is using version "
                            + majorVersion + "." + minorVersion + "." + updateVersion + ".";
                    return false;
                } else if (compareVersion == Constants.VERSION_EQUAL) {
                    errorString = "Both client and database are at version " + versionString
                            + ". No upgrade is necessary.";
                    return false;
                }
            } else if (calledFrom.equals(DatabaseConnectionUtils.CHECK_VERSION_FROM_UTILITIES)) {
                if (compareVersion != Constants.VERSION_EQUAL) {
                    errorString = "AT version " + versionString
                            + " will not work with this database. This database is using version "
                            + majorVersion + "." + minorVersion + "." + updateVersion + ".";
                    return false;
                }
            }
        } else {
            //incorrect number of constants records
            ErrorDialog dialog = new ErrorDialog("There are more than one constants record. Please contact your systemn administrator");
            dialog.showDialog();
            return false;
        }
        return true;

    }

    public static Boolean checkPermissions(String databaseType) throws ClassNotFoundException {
        Connection conn = null;
        try {
            String dummySQL = "";
            conn = openConnection();
            conn.setAutoCommit(false);

            if (databaseType.equals(SessionFactory.DATABASE_TYPE_MYSQL)) {
                dummySQL = "ALTER TABLE Constants modify majorVersion varchar(10)";
            } else if (databaseType.equals(SessionFactory.DATABASE_TYPE_MICROSOFT_SQL_SERVER)) {
                dummySQL = "ALTER TABLE Constants ALTER COLUMN majorVersion INT";
            } else if (databaseType.equals(SessionFactory.DATABASE_TYPE_ORACLE)) {
                dummySQL = "ALTER TABLE Constants modify (majorVersion NUMBER(10,0))";
            } else { // unknown database type so just assume we can upgrade
                return true;
            }

            Statement stmt = conn.createStatement();
            stmt.execute(dummySQL);
            conn.rollback();
            conn.close();

            // we got this far so the can probable go ahead with the upgrade
            return true;
        } catch (SQLException e) {
            try {
                conn.close();
            } catch (SQLException e1) {
                return false;
            }
            return false;
        }
    }

    /**
     * Method to check to see if a database being initialized is empty
     *
     * @param databaseType The type of the database
     * @return Null if the database is empty or String if it is not empty, or an error occured
     */
    public static String isDatabaseEmpty(String databaseType, String databaseUrl, String username, String password) throws ClassNotFoundException {
        Connection conn = null;
        try {
            String sqlStatement = "";
            conn = openConnection(databaseUrl, username, password);
            conn.setAutoCommit(false);

            if (databaseType.equals(SessionFactory.DATABASE_TYPE_MYSQL)) {
                sqlStatement = "SHOW TABLES";
            } else if (databaseType.equals(SessionFactory.DATABASE_TYPE_MICROSOFT_SQL_SERVER)) {
                sqlStatement = "SELECT * FROM sys.tables";
            } else if (databaseType.equals(SessionFactory.DATABASE_TYPE_ORACLE)) {
                sqlStatement = "SELECT table_name FROM user_tables";
            } else if (databaseType.equals(SessionFactory.DATABASE_TYPE_INTERNAL)) {
                sqlStatement = "SELECT * FROM INFORMATION_SCHEMA.TABLES where table_schema='PUBLIC'";
            } else { // unknown database type so we can't do an checking
                return "Unknown database type so unable to do check";
            }

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlStatement);

            // check the size of the statement so if there were any tables
            if (rs.next()) {
                return "You must supply an empty database";
            } else {
                return null;
            }
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e1) {
                return "An SQL error occured when closing database connection";
            }
            return "An SQL error occured when checking if database is empty";
        }
    }

    private static Connection openConnection() throws ClassNotFoundException, SQLException {
        Class.forName(SessionFactory.getDriverClass());
        Connection conn = DriverManager.getConnection(SessionFactory.getDatabaseUrl(),
                SessionFactory.getUserName(),
                SessionFactory.getPassword());
        return conn;
    }

    /**
     * Method to return a connection based on the database url, username and password provided
     *
     * @param databaseUrl
     * @param username
     * @param password
     * @return the database connection
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static Connection openConnection(String databaseUrl, String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName(SessionFactory.getDriverClass());
        Connection conn = DriverManager.getConnection(databaseUrl,
                username,
                password);
        return conn;
    }

    public static String getErrorString() {
        return errorString;
    }

    public static void setErrorString(String errorString) {
        DatabaseConnectionUtils.errorString = errorString;
    }

    public static DatabaseVersion getDatabaseVersionInfo(Connection conn) throws SQLException {

        String dummySQL = "SELECT Constants.majorVersion, Constants.minorVersion, Constants.updateVersion FROM Constants";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(dummySQL);

        if (rs.next()) {
            return new DatabaseVersion(rs.getInt(1), rs.getInt(2), rs.getInt(3));

        } else {
            return null;
        }
    }

    public static class DatabaseVersion {
        private int majorVersion;
        private int minorVersion;
        private int updateVersion;

        public DatabaseVersion(int majorVersion, int minorVersion, int updateVersion) {
            this.majorVersion = majorVersion;
            this.minorVersion = minorVersion;
            this.updateVersion = updateVersion;
        }


        public int getMajorVersion() {
            return majorVersion;
        }

        public int getMinorVersion() {
            return minorVersion;
        }

        public int getUpdateVersion() {
            return updateVersion;
        }
    }

    /**
     * Method to read in saved connection information from a text file
     *
     * @return A hashmap containing any saved database connection objects
     */
    public static HashMap loadDatabaseConnectionInformation() {
        HashMap<String, DatabaseConnectionInformation> savedConnections =
                new HashMap<String, DatabaseConnectionInformation>();

        // the file name to look for
        String filename = System.getProperty("user.home") + File.separator + CONNECTION_INFO_FILENAME;
        File file = new File(filename);

        // if file exists then read in database information
        if (file.exists()) {
            try {
                //use buffering, reading one line at a time
                //FileReader always assumes default encoding is OK!
                BufferedReader input = new BufferedReader(new FileReader(file));
                try {
                    String line = null; //not declared within while loop
                    /*
                    * readLine is a bit quirky :
                    * it returns the content of a line MINUS the newline.
                    * it returns null only for the END of the stream.
                    * it returns an empty String if two newlines appear in a row.
                    */
                    while ((line = input.readLine()) != null) {
                        if (!line.startsWith("#") && line.length() != 0) { // skip comments or blank lines
                            String[] sa = line.split("\\s*\\t\\s*");
                            if(sa.length == 4) {
                                DatabaseConnectionInformation dbInfo = new DatabaseConnectionInformation();
                                dbInfo.setDatabaseURL(sa[0]);
                                dbInfo.setUsername(sa[1]);
                                dbInfo.setPassword(sa[2]);
                                dbInfo.setDatabaseType(sa[3]);

                                savedConnections.put(sa[0], dbInfo);
                            }
                        }
                    }
                }
                finally {
                    input.close();
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        return savedConnections;
    }

    /**
     * Method to return the database version string to print to the about dialog
     *
     * @return
     */
    public static String getDatabaseVersionString() {
        return databaseVersionString;
    }

    /**
     * Method to save database connection information to a tab delimited
     * text file in the user's home directory.
     *
     * @param savedConnections The HashMap containing saved database connection information
     */
    public static void saveDatabaseConnectionInformation(HashMap savedConnections) {
        String filename = System.getProperty("user.home") + File.separator + CONNECTION_INFO_FILENAME;
        File file = new File(filename);

        // write the database information out to the file now
        try {
            // put the contents of the HashMap in a String buffer
            StringBuffer buffer = new StringBuffer();
            buffer.append("##This file is used to store database connection information\n");
            buffer.append("##DatabaseURL{tab}Username{tab}Password{tab}Database Type\n");

            // Get the collection of keys which are just the database urls
            Map sortedMap = new TreeMap(savedConnections); // sort the hashmap

            Collection c = sortedMap.values();

            //obtain an Iterator for Collection
            Iterator itr = c.iterator();

            //iterate through HashMap values iterator
            while (itr.hasNext()) {
                DatabaseConnectionInformation dbinfo = (DatabaseConnectionInformation) itr.next();
                buffer.append(dbinfo.getDatabaseURL()).append("\t");
                buffer.append(dbinfo.getUsername()).append("\t");
                buffer.append(dbinfo.getPassword()).append("\t");
                buffer.append(dbinfo.getDatabaseType()).append("\n");
            }


            Writer output = new BufferedWriter(new FileWriter(file));
            output.write(buffer.toString());
            output.close();
        } catch (IOException e) { // just print the stack trace for now
            e.printStackTrace();
        }


    }
}
