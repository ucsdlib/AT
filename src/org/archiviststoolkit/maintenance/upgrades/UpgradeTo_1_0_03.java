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

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class UpgradeTo_1_0_03 extends Upgrade {

    protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {

        int currentStep = initialStep;
		String progressMessage = "Updating field names";
        try {
            Statement stmt = conn.createStatement();
            String sqlString;

            progress.setProgress(progressMessage, currentStep++, numberOfSteps);
            sqlString = "ALTER TABLE Names CHANGE number nameNumber varchar(20)";
            stmt.execute(sqlString);

            progress.setProgress(progressMessage, currentStep++, numberOfSteps);
            sqlString = "ALTER TABLE NonPreferredNames CHANGE number nameNumber varchar(20)";
            stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
            sqlString = "RENAME TABLE Locations TO LocationsTable";
            stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
            sqlString = "ALTER TABLE Resources CHANGE level resourceLevel varchar(30)";
            stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
            sqlString = "ALTER TABLE ResourcesComponents CHANGE level resourceLevel varchar(30)";
            stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
            sqlString = "ALTER TABLE Deaccessions CHANGE date deaccessionDate date";
            stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
            sqlString = "ALTER TABLE ArchDescriptionInstances CHANGE container1AlphaNumericIndicator container1AlphaNumIndicator varchar(255)";
            stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
            sqlString = "ALTER TABLE ArchDescriptionInstances CHANGE container2AlphaNumericIndicator container2AlphaNumIndicator varchar(255)";
            stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
            sqlString = "ALTER TABLE ArchDescriptionInstances CHANGE container3AlphaNumericIndicator container3AlphaNumIndicator varchar(255)";
            stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
            sqlString = "ALTER TABLE BibItems CHANGE archDescriptionStructuredDataItemId archDescStructDataItemId bigint";
            stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
            sqlString = "ALTER TABLE ListOrderedItems CHANGE archDescriptionStructuredDataItemId archDescStructDataItemId bigint";
            stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
            sqlString = "ALTER TABLE ListDefinitionItems CHANGE archDescriptionStructuredDataItemId archDescStructDataItemId bigint";
            stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "SHOW indexes from Events where column_name = 'archDescriptionStructuredDataItemId'";
			ResultSet rs = stmt.executeQuery(sqlString);
			//boolean recreateForeignKey = false;
			if (rs.next()) {
				String fk = rs.getString(3);
				sqlString = "ALTER TABLE Events drop foreign key " + fk;
				stmt.execute(sqlString);
				//recreateForeignKey = true;
			}
			
			sqlString = "ALTER TABLE ChronologyItems CHANGE archDescriptionStructuredDataItemId archDescStructDataItemId bigint";
            stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
            sqlString = "ALTER TABLE IndexItems CHANGE archDescriptionStructuredDataItemId archDescStructDataItemId bigint";
            stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
            sqlString = "ALTER TABLE Events CHANGE archDescriptionStructuredDataItemId archDescStructDataItemId bigint";
			stmt.execute(sqlString);
			//if (recreateForeignKey) {
				//sqlString = "ALTER TABLE events add  FOREIGN KEY (archDescStructDataItemId) REFERENCES chronologyitems (archDescStructDataItemId)";
				//stmt.execute(sqlString);
			//}

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
        return 15;
    }

    protected boolean upgradeNeeded(Connection conn) throws SQLException {
        return Constants.compareVersions("1.0.3", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
    }

}
