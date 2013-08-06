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
 * Date: Dec 6, 2006
 * Time: 2:31:46 PM
 */

package org.archiviststoolkit.maintenance.initDatabase;

import org.netbeans.spi.wizard.*;
import org.netbeans.api.wizard.WizardDisplayer;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.importer.ImportUtils;
import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.model.Repositories;
import org.archiviststoolkit.model.Users;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.mydomain.DomainAccessObject;
import org.archiviststoolkit.mydomain.DomainAccessObjectFactory;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.mydomain.RepositoriesDAO;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.NotesEtcTypes;
import org.archiviststoolkit.structure.InLineTags;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.NoteEtcTypesUtils;
import org.archiviststoolkit.util.InLineTagsUtils;
import org.archiviststoolkit.exceptions.UnsupportedDatabaseType;
import org.archiviststoolkit.maintenance.initDatabase.RepositoryInformation;
import org.archiviststoolkit.maintenance.common.UserInformation;
import org.archiviststoolkit.maintenance.common.ConnectionInformation;
import org.archiviststoolkit.maintenance.ChooseOperation;
import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Element;
import org.xml.sax.InputSource;

import javax.swing.*;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.awt.*;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;

public class InitDatabaseWizard extends WizardPanelProvider{

	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("org.archiviststoolkit.resources.messages.messages");
	private static Repositories repository;

	public static void main(String[] ignored) {
		//All we do here is assemble the list of WizardPage subclasses we
		//want to show:
		Class[] pages = new Class[]{
				ConnectionInformation.class,
				RepositoryInformation.class,
				UserInformation.class,
		};

		//Use the utility method to compose a Wizard
		Wizard wizard = WizardPage.createWizard(pages, new WRP());

		//And show it onscreen
		WizardDisplayer.showWizard(wizard, new Rectangle(50, 50, 800, 400));
//		System.exit(0);
	}

	public InitDatabaseWizard() {
		super("Initialze Database",
				new String[] {"connectionInfo", "repositoryInfo", "userInfo"},
				new String[] {"Connection Information", "Repository Information", "User Information"});
	}

	protected JComponent createPanel(WizardController controller, String id, Map settings) {
		switch (indexOfStep(id)) {
			case 0 :
				return new ConnectionInformation(ChooseOperation.VALUE_INITIALIZE);
			case 1 :
				return new RepositoryInformation ();
			case 2 :
				return new UserInformation(ChooseOperation.VALUE_INITIALIZE);
			default :
				throw new IllegalArgumentException (id);
		}
	}

	private static class WRP implements WizardPage.WizardResultProducer {
		public Object finish(Map wizardData) throws WizardException {
			return new BackgroundResultCreator();
		}

		public boolean cancel(Map settings) {
			boolean dialogShouldClose = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to cancel the installation?",
					"", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
			return dialogShouldClose;
		}
	}

	static boolean abortable = true;

	static class BackgroundResultCreator extends DeferredWizardResult {
		public BackgroundResultCreator() {
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
				SessionFactory.setDatabaseType((String) wizardData.get("connectionDatabaseType"));
				SessionFactory.setDatabaseUrl((String) wizardData.get("connectionURL"));
				SessionFactory.setUserName((String) wizardData.get("connectionUsername"));
				SessionFactory.setPassword((String) wizardData.get("connectionPassword"));

				progress.setProgress("Creating constants record", currentStep++, numberOfSteps);
				applySqlFromFile("conf/constantsSQL.xml");

				progress.setProgress("Initializing database", currentStep++, numberOfSteps);
				initializeDatabase();

				progress.setProgress("setting database version", currentStep++, numberOfSteps);
				setDatabaseVersion();

				progress.setProgress("Creating repository record", currentStep++, numberOfSteps);
				addRepository(wizardData);

				progress.setProgress("Creating user record", currentStep++, numberOfSteps);
				addUser(wizardData);

				progress.setProgress("Defining unique keys", currentStep++, numberOfSteps);
				applySqlFromFile("conf/uniqueKeysSQL.xml");

				progress.setProgress("Creating table and field records", currentStep++, numberOfSteps);
				ATFieldInfo.loadFieldList();
				ATFieldInfo.initDatabaseTables();
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

				progress.finished(Summary.create("Sucess", wizardData));
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
			}
		}
	}

	private static void applySqlFromFile(String filePath) throws ClassNotFoundException, SQLException, IOException {
		Class.forName(SessionFactory.getDriverClass());
		Connection conn = DriverManager.getConnection(SessionFactory.getDatabaseUrl(),
				SessionFactory.getUserName(),
				SessionFactory.getPassword());
		Statement stmt = conn.createStatement();
		for (String sql : readSqlFromFile(filePath)) {
			stmt.executeUpdate(sql);
		}
		stmt.close();
	}

	private static void initializeDatabase() throws SQLException {
		SessionFactory.testHibernate();
	}

	private static void setDatabaseVersion() {
		Constants.updateOrCreateVersionRecord(resourceBundle.getString("archiviststoolkit.releasenumber"));
	}

	private static void addRepository(Map wizardData) throws PersistenceException {
		repository = new Repositories();
		repository.setRepositoryName((String) wizardData.get("repositoryName"));
		repository.setShortName((String) wizardData.get("shortName"));
		DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Repositories.class);
		access.add(repository);
	}

	private static void addUser(Map wizardData) throws PersistenceException, NoSuchAlgorithmException {
		Users user = new Users();
		user.setRepository(repository);
		user.setUserName((String) wizardData.get("userName"));
		user.setPassword(Users.hashString((String) wizardData.get("password1")));
		user.setAccessClass(Users.ACCESS_CLASS_SUPERUSER);
		DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Names.class);
		access.add(user);
//		return Summary.create("Sucess", wizardData);
	}

	private static ArrayList<String> readSqlFromFile(String filePath) throws IOException {

		ArrayList<String> returnArray = new ArrayList<String>();

		SAXBuilder builder = new SAXBuilder();

		InputSource input = new InputSource(new StringReader(ImportUtils.loadFileIntoString(filePath)));
		input.setEncoding("UTF-8");
		Document currentRecordJdom = null;
		try {
			currentRecordJdom = builder.build(input);
		} catch (JDOMException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (IOException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
