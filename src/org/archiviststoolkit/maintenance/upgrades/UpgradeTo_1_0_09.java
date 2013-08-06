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
 * Date: Apr 4, 2007
 * Time: 12:49:20 PM
 */

package org.archiviststoolkit.maintenance.upgrades;

import org.netbeans.spi.wizard.DeferredWizardResult;
import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.util.DatabaseConnectionUtils;
import org.archiviststoolkit.structure.ATFieldInfo;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class UpgradeTo_1_0_09 extends Upgrade {

	protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {

		int currentStep = initialStep;
		String progressMessage = "Modifying accessions table";
		try {
			Statement stmt = conn.createStatement();
			String sqlString;

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE Accessions CHANGE agreementSent agreementSentDate date";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE Accessions CHANGE agreementReceived agreementReceivedDate date";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE Accessions CHANGE dateProcessed accessionProcessedDate date";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE Accessions CHANGE accessRestrictions accessRestrictionsNote text";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE Accessions CHANGE useRestrictions useRestrictionsNote text";
			stmt.execute(sqlString);

//			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
//			sqlString = "DELETE DefaultValues\n" +
//					"FROM DefaultValues, DatabaseFields\n" +
//					"WHERE DatabaseFields.fieldName = 'accessRestrictions' and DatabaseFields.fieldId = DefaultValues.fieldId and DefaultValues.tableName = 'Accessions'";
//			stmt.execute(sqlString);
//
//			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
//			sqlString = "DELETE DefaultValues\n" +
//					"FROM DefaultValues, DatabaseFields\n" +
//					"WHERE DatabaseFields.fieldName = 'useRestrictions' and DatabaseFields.fieldId = DefaultValues.fieldId and DefaultValues.tableName = 'Accessions'";
//			stmt.execute(sqlString);

			stmt.close();

			return true;
			// everything is ok
		}
		catch (SQLException e) {
			setErrorString(e.getMessage());
			return false;
		}
	}

//	protected String getWarningMessage() {
//		return "This upgrade will delete default values for use and access restrictions for the Accessions table. They will need to be re-entered";
//	}

	protected int getNumberOfSteps() {
		return 5;
	}

	protected boolean upgradeNeeded(Connection conn) throws SQLException {
		return Constants.compareVersions("1.0.9", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
	}

	protected boolean runFieldInit() {
		return true;
	}


	protected boolean runLoadLookupLists() {
		return true;
	}
}
