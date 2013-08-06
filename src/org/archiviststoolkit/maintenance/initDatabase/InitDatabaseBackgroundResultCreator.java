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
 * Date: Jan 23, 2007
 * Time: 2:32:02 PM
 */

package org.archiviststoolkit.maintenance.initDatabase;

import org.netbeans.spi.wizard.DeferredWizardResult;
import org.netbeans.spi.wizard.Summary;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.NotesEtcTypes;
import org.archiviststoolkit.structure.InLineTags;
import org.archiviststoolkit.mydomain.RepositoriesDAO;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.mydomain.DomainAccessObject;
import org.archiviststoolkit.mydomain.DomainAccessObjectFactory;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.NoteEtcTypesUtils;
import org.archiviststoolkit.util.InLineTagsUtils;
import org.archiviststoolkit.exceptions.UnsupportedDatabaseType;
import org.archiviststoolkit.exceptions.RDEPanelCreationException;
import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.model.Repositories;
import org.archiviststoolkit.model.Users;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.importer.ImportUtils;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.maintenance.common.MaintenanceUtils;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Element;
import org.xml.sax.InputSource;

import java.util.Map;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.security.NoSuchAlgorithmException;

public class InitDatabaseBackgroundResultCreator extends DeferredWizardResult {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("org.archiviststoolkit.resources.messages.messages");
    private static Repositories repository;
    private static boolean abortable = true;
    private static String databaseType;

    public InitDatabaseBackgroundResultCreator() {
        super(abortable);
    }

    private volatile boolean aborted;

    public void abort() {
        aborted = true;
    }

    public void start(Map wizardData, DeferredWizardResult.ResultProgressHandle progress) {
        assert !EventQueue.isDispatchThread();

        int numberOfSteps = 11;
        int currentStep = 0;
        try {
            SessionFactory.resetFactory();
//				String driverClass = resourceBundle.getString("archiviststoolkit.jdbc.driver");
//				JComboBox databaseTypeComboBox = (JComboBox) wizardData.get("connectionDatabaseType");
            databaseType = (String) wizardData.get("connectionDatabaseType");
            SessionFactory.setDatabaseType(databaseType);
            SessionFactory.setDatabaseUrl((String) wizardData.get("connectionURL"));
            SessionFactory.setUserName((String) wizardData.get("connectionUsername"));
            SessionFactory.setPassword((String) wizardData.get("connectionPassword"));
            SessionFactory.setUpdateStructure(true);

//			progress.setProgress("Creating constants record", currentStep++, numberOfSteps);
//			applySqlFromFile("conf/constantsSQL.xml");

            progress.setProgress("Initializing database", currentStep++, numberOfSteps);
            initializeDatabase();

            progress.setProgress("setting database version", currentStep++, numberOfSteps);
            setDatabaseVersion();

            progress.setProgress("Creating repository record", currentStep++, numberOfSteps);
            addRepository(wizardData);

            progress.setProgress("Creating user record", currentStep++, numberOfSteps);
            addUser(wizardData);

            progress.setProgress("Defining unique keys", currentStep++, numberOfSteps);
            if (databaseType == SessionFactory.DATABASE_TYPE_ORACLE) {
                applySqlFromFile("conf/uniqueKeysSQL_oracle.xml");
            } else if (databaseType == SessionFactory.DATABASE_TYPE_MYSQL) {
                applySqlFromFile("conf/uniqueKeysSQL_mysql.xml");
            } else if (databaseType == SessionFactory.DATABASE_TYPE_MICROSOFT_SQL_SERVER) {
                applySqlFromFile("conf/uniqueKeysSQL_mssqlserver.xml");
            } else if (databaseType == SessionFactory.DATABASE_TYPE_INTERNAL) {
                applySqlFromFile("conf/uniqueKeysSQL_hpsql.xml");
            } else {
                progress.failed("Unsupported database type: " + databaseType, false);
                return;
            }

            progress.setProgress("Creating table and field records", currentStep++, numberOfSteps);
            ATFieldInfo.loadFieldList();
            ATFieldInfo.initDatabaseTables();
            //todo why is this called also
            ATFieldInfo.restoreDefaults(null);

            progress.setProgress("Loading notes etc. types", currentStep++, numberOfSteps);
            NoteEtcTypesUtils.importRecords(new File("conf/NoteEtcTypes.xml"));
            NoteEtcTypesUtils.loadNotesEtcTypes();
            new RepositoriesDAO().addNotesDefautsRecords();

            progress.setProgress("Loading inline tags", currentStep++, numberOfSteps);
            InLineTagsUtils.importRecords(new File("conf/inLineTags.xml"));

            progress.setProgress("Loading field definitions and examples", currentStep++, numberOfSteps);
            ATFieldInfo.importFieldDefinitionsAndExamples(new File("conf/fieldDefinitionAndExamples.xml"), progress, currentStep, numberOfSteps, false);
            ATFieldInfo.fixBlankFieldLabels(progress, currentStep, numberOfSteps);

            progress.setProgress("Loading lookup lists", currentStep++, numberOfSteps);
            LookupListUtils.importLookupLists(new File("conf/lookupListValues.xml"));

            progress.setProgress("Creating default RDE screen", currentStep++, numberOfSteps);
            MaintenanceUtils.createDefaultRDEScreen();

            progress.finished(Summary.create("Success", wizardData));
        } catch (SQLException e) {
            progress.failed(e.getMessage(), false);
        } catch (ClassNotFoundException e) {
            progress.failed(e.getMessage(), false);
        } catch (IOException e) {
            progress.failed(e.getMessage(), false);
        } catch (PersistenceException e) {
            progress.failed(e.getMessage(), false);
        } catch (NoSuchAlgorithmException e) {
            progress.failed(e.getMessage(), false);
//			} catch (Exception e) {
//				progress.failed (e.getMessage(), false);
        } catch (UnsupportedDatabaseType e) {
            progress.failed(e.getMessage(), false);
        } catch (RDEPanelCreationException e) {
            progress.failed(e.getMessage(), false);
        }
    }

    private void applySqlFromFile(String filePath) throws ClassNotFoundException, SQLException, IOException {
        Class.forName(SessionFactory.getDriverClass());
        Connection conn = DriverManager.getConnection(SessionFactory.getDatabaseUrl(),
                SessionFactory.getUserName(),
                SessionFactory.getPassword());
        Statement stmt = conn.createStatement();
        String trimmedString;
        for (String sql : readSqlFromFile(filePath)) {
            trimmedString = sql.trim();
//			System.out.println("SQL statement: " + trimmedString);
            stmt.executeUpdate(trimmedString);
        }
        stmt.close();
    }

    private void initializeDatabase() throws SQLException, ClassNotFoundException {
        //first if this is mysql make sure the default is utf-8
        if (databaseType == SessionFactory.DATABASE_TYPE_MYSQL) {
            String databaseUrl = SessionFactory.getDatabaseUrl();
            String databaseName = databaseUrl.substring(databaseUrl.lastIndexOf("/") + 1);

            // check to make sure the character encoding information is not part database name
            // if it is then split string aprt to get just the name
            if (databaseName.indexOf("?") != -1) {
                String[] sa = databaseName.split("\\?");
                databaseName = sa[0];
            }

            Class.forName(SessionFactory.getDriverClass());
            Connection conn = DriverManager.getConnection(databaseUrl,
                    SessionFactory.getUserName(),
                    SessionFactory.getPassword());
            Statement stmt = conn.createStatement();
            String sqlString = "ALTER DATABASE " + databaseName + " CHARACTER SET utf8 COLLATE utf8_unicode_ci";
            stmt.execute(sqlString);
        }
        SessionFactory.testHibernate();
    }

    private void setDatabaseVersion() {
        Constants.updateOrCreateVersionRecord(resourceBundle.getString("archiviststoolkit.releasenumber"));
    }

    private void addRepository(Map wizardData) throws PersistenceException {
        Users user = new Users();
        user.setUserName((String) wizardData.get("userName"));
        ApplicationFrame.getInstance().setCurrentUser(user);
        repository = new Repositories();
        repository.setRepositoryName((String) wizardData.get("repositoryName"));
        repository.setShortName((String) wizardData.get("shortName"));
        DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Repositories.class);
        access.add(repository);
    }

    private void addUser(Map wizardData) throws PersistenceException, NoSuchAlgorithmException {
        Users user = new Users();
        user.setRepository(repository);
        user.setUserName((String) wizardData.get("userName"));
        user.setPassword(Users.hashString((String) wizardData.get("password1")));
        user.setAccessClass(Users.ACCESS_CLASS_SUPERUSER);
        DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Names.class);
        access.add(user);
        ApplicationFrame.getInstance().setCurrentUser(user);
//		return Summary.create("Sucess", wizardData);
    }

    private ArrayList<String> readSqlFromFile(String filePath) throws IOException {

        ArrayList<String> returnArray = new ArrayList<String>();

        SAXBuilder builder = new SAXBuilder();

        InputSource input = new InputSource(new StringReader(ImportUtils.loadFileIntoString(filePath)));
        input.setEncoding("UTF-8");
        Document currentRecordJdom = null;
        try {
            currentRecordJdom = builder.build(input);
        } catch (JDOMException e) {
            new ErrorDialog("", e).showDialog();
        } catch (IOException e) {
            new ErrorDialog("", e).showDialog();
        }
        Element root = currentRecordJdom.getRootElement();
        System.out.println(root.toString());
        Element element;
        for (Object o : root.getChildren("command")) {
            element = (Element) o;
            returnArray.add(element.getText());
        }
        return returnArray;
    }

}
