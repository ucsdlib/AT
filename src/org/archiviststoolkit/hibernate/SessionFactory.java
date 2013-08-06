/**
 * Archivists' Toolkit(TM) Copyright © 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
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
 * Date: Apr 18, 2007
 * Time: 10:38:48 AM
 */

package org.archiviststoolkit.hibernate;

//==============================================================================
// Import Declarations
//==============================================================================

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Properties;
import java.util.Vector;
import java.util.Collections;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.*;
import org.archiviststoolkit.exceptions.UnsupportedDatabaseType;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.ApplicationFrame;

/**
 * This is a facade for hibernate session factory configuration
 * and initialisation.
 */

public final class SessionFactory {
    /**
     * Class logger.
     */

    private static Logger logger = Logger.getLogger(SessionFactory.class.getPackage().getName());

    public static final String DATABASE_TYPE_MYSQL = "MySQL";
    public static final String DATABASE_TYPE_ORACLE = "Oracle";
    public static final String DATABASE_TYPE_MICROSOFT_SQL_SERVER = "Microsoft SQL Server";

    // setup a database type of Internal which is just an embedded hypersql
    public static final String DATABASE_TYPE_INTERNAL = "Internal Database";

    /**
     * Singleton instance handle.
     */

    private static SessionFactory singleton = null;

    private static String databaseUrl;
    private static String userName;
    private static String password;
    private static String driverClass;
    private static String hibernateDialect;

    private static String databaseType;
    private static Boolean updateStructure = false;

    /**
     * The hibernate session factory which is used to pass off commands.
     */

    private org.hibernate.SessionFactory sessionFactory = null;

    /**
     * As this is a singleton the constructor is private.
     * In here we initialise and configure the hibernate session factory
     * making sure that it happens only once
     */

    private SessionFactory() {

        try {
            Configuration config = new Configuration().configure();
            Properties properties = config.getProperties();
            properties.setProperty("hibernate.connection.driver_class", driverClass);
            if (SessionFactory.databaseType.equals(DATABASE_TYPE_MYSQL)) {
                properties.setProperty("hibernate.connection.url", getDatabaseUrl() + "?useUnicode=yes&characterEncoding=utf8");
            } else {
                properties.setProperty("hibernate.connection.url", getDatabaseUrl());
            }

            //deal with oracle specific settings
            if (SessionFactory.databaseType.equals(DATABASE_TYPE_ORACLE)) {
                properties.setProperty("hibernate.jdbc.batch_size", "0");
                properties.setProperty("hibernate.jdbc.use_streams_for_binary", "true");
                properties.setProperty("SetBigStringTryClob", "true");
            }
            properties.setProperty("hibernate.connection.username", getUserName());
            properties.setProperty("hibernate.connection.password", getPassword());
            properties.setProperty("hibernate.dialect", getHibernateDialect());
            if (SessionFactory.updateStructure) {
                properties.setProperty("hibernate.hbm2ddl.auto", "update");
            }
            config.setProperties(properties);
            sessionFactory = config.buildSessionFactory();
            //test the session factory to make sure it is working
        } catch (Exception hibernateException) {
            logger.log(Level.SEVERE, "Failed to startup hibernate engine", hibernateException);
            throw new RuntimeException(hibernateException);
//            ErrorDialog dialog = new ErrorDialog("There is a problem initializing hibernate.",
//                    StringHelper.getStackTrace(hibernateException));
//            dialog.showDialog();
//            System.exit(1);
        }
    }

    public static void testHibernate() throws SQLException {
        SessionFactory factory = SessionFactory.getInstance();
        Session testSession = factory.openSession();
        Query query = null;
        query = testSession.createQuery("select count(*) from Constants");
        query.list();
        testSession.flush();
        testSession.connection().commit();
        testSession.close();
    }

    /**
     * The method for getting the instance of this singleton.
     *
     * @return the instance of this singleton
     */

    public static SessionFactory getInstance() {
        if (singleton == null) {
            singleton = new SessionFactory();
        }

        return (singleton);
    }

    public static void resetFactory() {
        singleton = null;
    }

    public StatelessSession openStatelessSession() {
        return sessionFactory.openStatelessSession();
    }


    /**
     * Used to create a new session from the preconfigured hibernate factory.
     *
     * @return the session which is opened
     */

    public Session openSession() {

        return openSession(null, null, false);
    }

    public Session openSession(Interceptor interceptor, Boolean alwaysApplyFilters) {
        return openSession(interceptor, null, alwaysApplyFilters);
    }

    public Session openSession(Interceptor interceptor) {
        return openSession(interceptor, null, false);
    }

    public Session openSession(Class clazz) {
        return openSession(null, clazz, false);
    }

    public Session openSession(Interceptor interceptor, Class clazz) {
        return openSession(interceptor, clazz, false);
    }

    /**
     * Used to create a new session from the preconfigured hibernate factory.
     * If the class parameter is present check to see if any filters should
     * be added
     *
     * @return the session which is opened
     */

    public Session openSession(Interceptor interceptor, Class clazz, Boolean alwaysApplyFilters) {

        Session newSession = null;

        try {
            if (newSession == null) {
                if (interceptor == null) {
                    newSession = singleton.sessionFactory.openSession();
                } else {
                    newSession = singleton.sessionFactory.openSession(interceptor);
                }
            }

        } catch (HibernateException hibernateException) {
            logger.log(Level.SEVERE, "Failed to open a hibernate session", hibernateException);
        }
        if (clazz != null) {
            if (alwaysApplyFilters || (ApplicationFrame.getInstance().getCurrentUser() != null &&
                    !Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_SUPERUSER))) {
                if (clazz == Resources.class ||
                        clazz == Accessions.class ||
                        clazz == Users.class ||
                        clazz == Repositories.class ||
                        clazz == Locations.class ||
                        clazz == DigitalObjects.class) {
                    newSession.enableFilter("repository").setParameter("repositoryId",
                            ApplicationFrame.getInstance().getCurrentUserRepositoryId());
                }
            }

        }
        return (newSession);
    }

    /**
     * Close an open session .
     *
     * @param session the session to be closed
     */

    public void closeSession(final Session session) {
        try {
            if (session != null) {
                session.close();
            }
        } catch (HibernateException hibernateException) {
            logger.log(Level.SEVERE, "Failed to close a hibernate session", hibernateException);
        }
    }

    /**
     * Get a copy of the real hibernate sessionfactory instance.
     *
     * @return the hibernate sessionfactory
     */

    public static org.hibernate.SessionFactory getSessionFactory() {
        return (singleton.sessionFactory);
    }

    /**
     * Used to elegantly shutdown the hibernate instance.
     */

    public void shutdown() {

        try {
            Session session = openSession();
            Connection connection = session.connection();
            Statement stmt = connection.createStatement();

            ResultSet rset = stmt.executeQuery("SHUTDOWN COMPACT");
            rset.close();
            stmt.close();

            session.flush();
            session.close();

        } catch (HibernateException hibernateException) {
            logger.log(Level.SEVERE, "Failed to interact with hibernate", hibernateException);
        } catch (SQLException sqlException) {
            logger.log(Level.SEVERE, "Failed to execute the sql shutdown code", sqlException);
        }
    }

    public static void setDatabaseUrl(String databaseUrl) {
        SessionFactory.databaseUrl = databaseUrl;
    }

    public static void setUserName(String userName) {
        SessionFactory.userName = userName;
    }

    public static void setPassword(String password) {
        SessionFactory.password = password;
    }

    public static String getDriverClass() {
        return driverClass;
    }

    private static void setDriverClass(String driverClass) {
        SessionFactory.driverClass = driverClass;
    }

    public static void setDatabaseType(String databaseType) throws UnsupportedDatabaseType {
        SessionFactory.databaseType = databaseType;
        if (databaseType.equals(DATABASE_TYPE_MYSQL)) {
            setDriverClass("com.mysql.jdbc.Driver");
            //setHibernateDialect("org.hibernate.dialect.MySQLInnoDBDialect");
            setHibernateDialect("org.hibernate.dialect.MySQL5InnoDBDialect");
        } else if (databaseType.equals(DATABASE_TYPE_MICROSOFT_SQL_SERVER)) {
            setDriverClass("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            setHibernateDialect("org.hibernate.dialect.SQLServerDialect");
        } else if (databaseType.equals(DATABASE_TYPE_ORACLE)) {
            setDriverClass("oracle.jdbc.OracleDriver");
            setHibernateDialect("org.hibernate.dialect.Oracle10gDialect");
        } else if (databaseType.equals(DATABASE_TYPE_INTERNAL)) {
            setDriverClass("org.hsqldb.jdbcDriver");
            setHibernateDialect("org.hibernate.dialect.HSQLDialect");
        } else {
            throw new UnsupportedDatabaseType(databaseType);
        }
    }

    public static String getDatabaseUrl() {
        return databaseUrl;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getPassword() {
        return password;
    }

    public static String getHibernateDialect() {
        return hibernateDialect;
    }

    public static void setHibernateDialect(String hibernateDialect) {
        SessionFactory.hibernateDialect = hibernateDialect;
    }

    public static Vector<String> getDatabaseTypesList() {
        return getDatabaseTypesList(false);
    }

    public static Vector<String> getDatabaseTypesList(boolean addBlankAtBeginning) {
        Vector<String> returnVector = new Vector<String>();
        returnVector.add(DATABASE_TYPE_MYSQL);
        returnVector.add(DATABASE_TYPE_ORACLE);
        returnVector.add(DATABASE_TYPE_MICROSOFT_SQL_SERVER);
        returnVector.add(DATABASE_TYPE_INTERNAL);

//		returnVector.add("zBadDatabaseType");
        Collections.sort(returnVector);
        if (addBlankAtBeginning) {
            returnVector.add(0, "");
        }
        return returnVector;
    }

    public static String getDatabaseType() {
        return databaseType;
    }

    public static Boolean getUpdateStructure() {
        return updateStructure;
    }

    public static void setUpdateStructure(Boolean updateStructure) {
        SessionFactory.updateStructure = updateStructure;
    }
}