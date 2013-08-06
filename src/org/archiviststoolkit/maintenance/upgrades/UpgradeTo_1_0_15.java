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
 * Date: May 15, 2007
 * Time: 4:34:24 PM
 */

package org.archiviststoolkit.maintenance.upgrades;

import org.netbeans.spi.wizard.DeferredWizardResult;
import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.util.DatabaseConnectionUtils;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class UpgradeTo_1_0_15 extends Upgrade {

    protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {

        int currentStep = initialStep;
		String progressMessage = "Updating field names";
        try {
            Statement stmt = conn.createStatement();
            String sqlString;

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
            sqlString = "ALTER TABLE LocationsTable CHANGE coordinate1AlphaNumericIndicator coordinate1AlphaNumIndicator varchar(20)";
            stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
            sqlString = "ALTER TABLE LocationsTable CHANGE coordinate2AlphaNumericIndicator coordinate2AlphaNumIndicator varchar(20)";
            stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
            sqlString = "ALTER TABLE LocationsTable CHANGE coordinate3AlphaNumericIndicator coordinate3AlphaNumIndicator varchar(20)";
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
        return 3;
    }

    protected boolean upgradeNeeded(Connection conn) throws SQLException {
        return Constants.compareVersions("1.0.15", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
    }

	protected boolean runFieldInit() {
		return true;
	}

}
