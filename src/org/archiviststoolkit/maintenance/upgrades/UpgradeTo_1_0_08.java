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
 * Time: 3:38:24 PM
 */

package org.archiviststoolkit.maintenance.upgrades;

import org.netbeans.spi.wizard.DeferredWizardResult;
import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.util.DatabaseConnectionUtils;
import org.archiviststoolkit.structure.ATFieldInfo;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class UpgradeTo_1_0_08 extends Upgrade {

	protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {

		int currentStep = initialStep;
		String progressMessage = "Modifying locations table";
		try {
			Statement stmt = conn.createStatement();
			String sqlString;

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE LocationsTable CHANGE building building varchar(100)";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE LocationsTable CHANGE floor floor varchar(100)";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE LocationsTable CHANGE room room varchar(100)";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE LocationsTable CHANGE area area varchar(100)";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE LocationsTable CHANGE coordinate1 coordinate1 varchar(100)";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE LocationsTable CHANGE coordinate2 coordinate2 varchar(100)";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE LocationsTable CHANGE coordinate3 coordinate3 varchar(100)";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE LocationsTable CHANGE classificationNumber classificationNumber varchar(100)";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE LocationsTable CHANGE barcode barcode varchar(100)";
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
		return 9;
	}

	protected boolean upgradeNeeded(Connection conn) throws SQLException {
		return Constants.compareVersions("1.0.8", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
	}

	protected boolean runFieldInit() {
		return true;
	}

}
