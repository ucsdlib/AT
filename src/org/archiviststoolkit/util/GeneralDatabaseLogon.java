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
 * Date: Jun 9, 2009
 * Time: 1:40:13 PM
 */

package org.archiviststoolkit.util;

import org.archiviststoolkit.util.UserPreferences;
import org.archiviststoolkit.util.DatabaseConnectionUtils;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.exceptions.UnsupportedDatabaseType;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.model.Users;
import org.archiviststoolkit.model.Repositories;
import org.archiviststoolkit.swing.LoginDialog;
import org.archiviststoolkit.hibernate.SessionFactory;

import javax.swing.*;
import java.sql.SQLException;

public class GeneralDatabaseLogon {


	private static Users currentUser = null;

	public void logonToDatabase() {
		//get user preferences
		UserPreferences userPrefs = UserPreferences.getInstance();
		userPrefs.populateFromPreferences();

		//determine if this is the first time the AT has been run from this computer
		//if so show the user prefs dialog so they can enter database connection information
		if (!userPrefs.checkForDatabaseUrl()) {
			JOptionPane.showMessageDialog(null, "It appears the AT was never run on this machine. Please fill out the database" +
					" connection information");
			userPrefs.displayUserPreferencesDialog();
		}
		//create a session factory for interaction with the database
		try {
			userPrefs.updateSessionFactoryInfo();
		} catch (UnsupportedDatabaseType unsupportedDatabaseType) {
			new ErrorDialog("Error connecting to database", unsupportedDatabaseType).showDialog();
		}
		//test the connection with hibernate (the persistance layer) to make sure everyting is running properly
		try {
			connectAndTest(userPrefs);
		} catch (UnsupportedDatabaseType unsupportedDatabaseType) {
			new ErrorDialog("Error connecting to database", unsupportedDatabaseType).showDialog();
		}
		//logon and make sure the logon is a superuser
		try {
			currentUser = diaplayLoginAndRetrunUser(userPrefs);
			if (!Users.doesUserHaveAccess(currentUser, Users.ACCESS_CLASS_SUPERUSER)) {
				new ErrorDialog("You must be a superuser").showDialog();
			}
		} catch (UnsupportedDatabaseType unsupportedDatabaseType) {
			new ErrorDialog("", unsupportedDatabaseType).showDialog();
		}
	}

		/**
		 *
		 */
		public static void checkJavaVersion() {
			String javaVersion = System.getProperty("java.version");
			if (javaVersion.startsWith("1.4") || javaVersion.startsWith("1.3") || javaVersion.startsWith("1.2")) {
				new ErrorDialog("Incorrect Java Version",
						"This program requires Java 1.5.x or greater, you are currently running version " + javaVersion,
						ErrorDialog.DIALOG_TYPE_ERROR).showDialog();
				System.exit(1);
			}
		}

		/**
		 * @param userPrefs
		 * @return
		 * @throws UnsupportedDatabaseType
		 */
		private static Users diaplayLoginAndRetrunUser(UserPreferences userPrefs) throws UnsupportedDatabaseType {
			LoginDialog login = new LoginDialog();
			boolean tryAgain = true;
			Users user;

			int returnStatus;
			do {
				returnStatus = login.showDialog();

				if (returnStatus == JOptionPane.CANCEL_OPTION) {
					System.exit(1);
				} else if (returnStatus == JOptionPane.OK_OPTION) {
					user = Users.lookupUser(login.getUserName(), login.getPassword());
					if (user != null) {
						tryAgain = false;
						return user;
					} else {
						JOptionPane.showMessageDialog(null,
								"Unknown user or wrong password",
								"Login error",
								JOptionPane.WARNING_MESSAGE);
					}
				} else if (returnStatus == LoginDialog.CHOOSE_SERVER) {
					if (userPrefs.displayUserPreferencesDialog() == JOptionPane.OK_OPTION) {
						userPrefs.updateSessionFactoryInfo();
						connectAndTest(userPrefs);
						Repositories.loadRepositories();
					}
				}

			} while (tryAgain);

			return null;
		}

		/**
		 * @param userPrefs
		 * @throws UnsupportedDatabaseType
		 */
		private static void connectAndTest(UserPreferences userPrefs) throws UnsupportedDatabaseType {
			while (!DatabaseConnectionUtils.testDbConnection()) {
				if (userPrefs.displayUserPreferencesDialog() == javax.swing.JOptionPane.OK_OPTION) {
					userPrefs.updateSessionFactoryInfo();
				} else {
					System.exit(1);
				}
			}

			try {
				while (!DatabaseConnectionUtils.checkVersion(DatabaseConnectionUtils.CHECK_VERSION_FROM_MAIN)) {
					if (userPrefs.displayUserPreferencesDialog() == JOptionPane.OK_OPTION) {
						userPrefs.updateSessionFactoryInfo();
					} else {
						System.exit(1);
					}
				}
			} catch (ClassNotFoundException e) {
				new ErrorDialog("There is a problem with the database connection. Please check the settings and try again",
						StringHelper.getStackTrace(e)).showDialog();
			} catch (SQLException e) {
				new ErrorDialog("The jdbc driver is missing",
						StringHelper.getStackTrace(e)).showDialog();
			}

			boolean done = false;
			while (!done) {
				try {
					SessionFactory.testHibernate();
					done = true;
				} catch (Exception e) {
					ErrorDialog dialog = new ErrorDialog("There is a problem initializing hibernate. Please contact your system administrator.",
							StringHelper.getStackTrace(e));
					dialog.showDialog();
					if (userPrefs.displayUserPreferencesDialog() == JOptionPane.OK_OPTION) {
						userPrefs.updateSessionFactoryInfo();
					} else {
						System.exit(1);
					}
				}
			}

		}


	}
