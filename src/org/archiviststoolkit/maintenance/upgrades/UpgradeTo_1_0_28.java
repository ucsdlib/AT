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
 * Date: Oct 8, 2007
 * Time: 10:55:54 AM
 */

package org.archiviststoolkit.maintenance.upgrades;

import org.netbeans.spi.wizard.DeferredWizardResult;
import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.util.DatabaseConnectionUtils;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class UpgradeTo_1_0_28 extends Upgrade {

    protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {

		int currentStep = initialStep;
		String progressMessage = "Updating repository table indexes";
		try {
			Statement stmt = conn.createStatement();
			String sqlString;

			try {
				progress.setProgress(progressMessage, currentStep++, numberOfSteps);
				sqlString = "ALTER TABLE Repositories ADD UNIQUE repositoryName (repositoryName)";
				stmt.execute(sqlString);
			} catch (SQLException e) {
				if (e.getMessage().contains("Duplicate key name")) {
					//do nothing
				} else {
					throw e;
				}
			}

			try {
				progress.setProgress(progressMessage, currentStep++, numberOfSteps);
				sqlString = "ALTER TABLE Repositories ADD UNIQUE shortName (shortName)";
				stmt.execute(sqlString);
			} catch (SQLException e) {
				if (e.getMessage().contains("Duplicate key name")) {
					//do nothing
				} else {
					throw e;
				}
			}

			progressMessage = "Updating database table and field audit info";
			try {
				progress.setProgress(progressMessage, currentStep++, numberOfSteps);
				sqlString = "ALTER TABLE DatabaseFields ADD created datetime";
				stmt.execute(sqlString);

				progress.setProgress(progressMessage, currentStep++, numberOfSteps);
				sqlString = "ALTER TABLE DatabaseFields ADD lastUpdated datetime";
				stmt.execute(sqlString);

				progress.setProgress(progressMessage, currentStep++, numberOfSteps);
				sqlString = "ALTER TABLE DatabaseFields ADD createdBy varchar(255) NOT NULL";
				stmt.execute(sqlString);

				progress.setProgress(progressMessage, currentStep++, numberOfSteps);
				sqlString = "ALTER TABLE DatabaseFields ADD lastUpdatedBy varchar(255) NOT NULL";
				stmt.execute(sqlString);

				progress.setProgress(progressMessage, currentStep++, numberOfSteps);
				sqlString = "ALTER TABLE DatabaseTables ADD created datetime";
				stmt.execute(sqlString);

				progress.setProgress(progressMessage, currentStep++, numberOfSteps);
				sqlString = "ALTER TABLE DatabaseTables ADD lastUpdated datetime";
				stmt.execute(sqlString);

				progress.setProgress(progressMessage, currentStep++, numberOfSteps);
				sqlString = "ALTER TABLE DatabaseTables ADD createdBy varchar(255) NOT NULL";
				stmt.execute(sqlString);

				progress.setProgress(progressMessage, currentStep++, numberOfSteps);
				sqlString = "ALTER TABLE DatabaseTables ADD lastUpdatedBy varchar(255) NOT NULL";
				stmt.execute(sqlString);
			} catch (SQLException e) {
				//do nothing this just means that the fields are already there
			}
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "UPDATE DatabaseFields SET created = '" + timestamp.toString() + "'";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "UPDATE DatabaseFields SET lastUpdated = '" + timestamp.toString() + "'";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "UPDATE DatabaseTables SET created = '" + timestamp.toString() + "'";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "UPDATE DatabaseTables SET lastUpdated = '" + timestamp.toString() + "'";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "UPDATE DatabaseFields SET createdBy =  'upgrade'";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "UPDATE DatabaseFields SET lastUpdatedBy = 'upgrade'";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "UPDATE DatabaseTables SET createdBy =  'upgrade'";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "UPDATE DatabaseTables SET lastUpdatedBy =  'upgrade'";
			stmt.execute(sqlString);

			stmt.close();
			return true;
			// everything is ok
		}
		catch (SQLException e) {
			setErrorString(e.getMessage());
			return false;
		}

    }

    protected int getNumberOfSteps() {
        return 18;
    }

	protected String getWarningMessage() {
		return "This upgrade will fail if there are duplicate repository names or repository short names. Please clean up the duplicates before running the upgrade." +
				"\nIf you receive a duplicate key error message when running the upgrade this is the problem.";
	}

    protected boolean upgradeNeeded(Connection conn) throws SQLException {
        return Constants.compareVersions("1.0.28", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
    }

	protected boolean runFieldInit() {
		return true;
	}

	protected boolean runLoadLookupLists() {
		return true;
	}

}
