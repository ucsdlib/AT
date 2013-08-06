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
 * Date: Dec 12, 2007
 * Time: 10:50:28 AM
 */

package org.archiviststoolkit.maintenance.upgrades;

import org.netbeans.spi.wizard.DeferredWizardResult;
import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.util.DatabaseConnectionUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UpgradeTo_1_0_40 extends Upgrade {

    protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {

		try {
			int currentStep = initialStep;
			String progressMessage = "Modifying digital objects table";
			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			Statement stmt = conn.createStatement();
			String sqlString;
			sqlString = "ALTER TABLE DigitalObjects\n" +
					" DROP COLUMN accessRestrictions;";
			stmt.execute(sqlString);

			stmt.close();
		} catch (SQLException e) {
			//do nothing the column was not there
		}
		return true;
            // everything is ok
 	}

	protected int getNumberOfSteps() {
        return 1;
    }

    protected boolean upgradeNeeded(Connection conn) throws SQLException {
        return Constants.compareVersions("1.0.40", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
    }

	protected boolean runFieldInit() {
		return true;
	}
}
