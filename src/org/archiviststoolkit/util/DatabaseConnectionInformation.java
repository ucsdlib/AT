package org.archiviststoolkit.util;

/**
 * Archivists' Toolkit(TM) Copyright © 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
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
 * This is a simple class for storing information about database connections
 *
 * @author: Nathan Stevens
 * Date: Apr 6, 2009
 * Time: 1:37:22 PM
 */
public class DatabaseConnectionInformation {
    private String databaseURL = "";
    private String username = "";
    private String password = "";
    private String databaseType = "";

    // The default constructor
    public DatabaseConnectionInformation() { }

    /**
     * Main constructor for creating database connections
     * @param databaseURL The URL to the database
     * @param username The database username
     * @param password The database password
     * @param databaseType The type of database
     */
    public DatabaseConnectionInformation(String databaseURL, String username, String password, String databaseType) {
        this.databaseURL = databaseURL;
        this.username = username;
        this.password = password;
        this.databaseType = databaseType;
    }

    public String getDatabaseURL() {
        return databaseURL;
    }

    public void setDatabaseURL(String databaseURL) {
        this.databaseURL = databaseURL;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toString() {
        return databaseURL;
    }
}
