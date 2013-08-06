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
 * Date: Jan 20, 2007
 * Time: 3:46:02 PM
 */

package org.archiviststoolkit.maintenance.upgrades;

import org.archiviststoolkit.util.UserPreferences;
import org.archiviststoolkit.util.DatabaseConnectionUtils;
import org.archiviststoolkit.exceptions.UnsupportedDatabaseType;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.model.Constants;

import java.util.ResourceBundle;
import java.sql.SQLException;

public class UpgradeDatabase {

	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("org.archiviststoolkit.resources.messages.messages");
	private static String versionString = resourceBundle.getString("archiviststoolkit.releasenumber");

	public static void main(String[] args) {

		UserPreferences userPrefs = UserPreferences.getInstance();
		userPrefs.populateFromPreferences();

		if (userPrefs.displayUserPreferencesDialog() == javax.swing.JOptionPane.OK_OPTION) {
			try {
				userPrefs.updateSessionFactoryInfo();
			} catch (UnsupportedDatabaseType unsupportedDatabaseType) {
				new ErrorDialog("Error connecting to database", unsupportedDatabaseType, ErrorDialog.DIALOG_TYPE_ERROR).showDialog();
				System.exit(1);
			}
		} else {
			System.exit(1);
		}

		try {
			userPrefs.updateSessionFactoryInfo();
		} catch (UnsupportedDatabaseType unsupportedDatabaseType) {
			new ErrorDialog("Error connecting to database", unsupportedDatabaseType).showDialog();
		}

		while (!DatabaseConnectionUtils.testDbConnection()) {
			if (userPrefs.displayUserPreferencesDialog() == javax.swing.JOptionPane.OK_OPTION) {
				try {
					userPrefs.updateSessionFactoryInfo();
				} catch (UnsupportedDatabaseType unsupportedDatabaseType) {
					new ErrorDialog("Error connecting to database", unsupportedDatabaseType, ErrorDialog.DIALOG_TYPE_ERROR).showDialog();
				}
			} else {
				System.exit(1);
			}
		}

		try {
			while (!DatabaseConnectionUtils.checkVersion(DatabaseConnectionUtils.CHECK_VERSION_FROM_UPGRADE)) {
				if (userPrefs.displayUserPreferencesDialog() == javax.swing.JOptionPane.OK_OPTION) {
					try {
						userPrefs.updateSessionFactoryInfo();
					} catch (UnsupportedDatabaseType unsupportedDatabaseType) {
						new ErrorDialog("Error connecting to database", unsupportedDatabaseType, ErrorDialog.DIALOG_TYPE_ERROR).showDialog();
					}
				} else {
					System.exit(1);
				}
			}
		} catch (ClassNotFoundException e) {
			new ErrorDialog("", e).showDialog();
		} catch (SQLException e) {
			new ErrorDialog("", e).showDialog();
		}

		Upgrade upgradeClass = new UpgradeTo_1_0_01();
//		upgradeClass.doUpgrade();

		Constants.updateOrCreateVersionRecord(versionString);
	}

}
