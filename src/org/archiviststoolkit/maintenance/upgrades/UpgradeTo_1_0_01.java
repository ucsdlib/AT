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

import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.util.DatabaseConnectionUtils;
import org.netbeans.spi.wizard.DeferredWizardResult;

import java.sql.*;

public class UpgradeTo_1_0_01 extends Upgrade {

    protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {

        int currentStep = initialStep;
		String progressMessage = "Upgrading persistent IDs";
        try {
            Statement stmt = conn.createStatement();

            progress.setProgress(progressMessage, currentStep++, numberOfSteps);
            String sqlString = "ALTER TABLE ResourcesComponents modify persistentId varchar(20)";
            stmt.execute(sqlString);

            progress.setProgress(progressMessage, currentStep++, numberOfSteps);
            sqlString = "UPDATE ResourcesComponents SET persistentId = concat('ref' , persistentId)";
            stmt.execute(sqlString);

            progress.setProgress(progressMessage, currentStep++, numberOfSteps);
            sqlString = "ALTER TABLE ArchDescriptionRepeatingData modify persistentId varchar(20)";
            stmt.execute(sqlString);

            progress.setProgress(progressMessage, currentStep++, numberOfSteps);
            sqlString = "UPDATE ArchDescriptionRepeatingData SET persistentId = concat('ref' , persistentId)";
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
        return 4;
    }

    protected boolean upgradeNeeded(Connection conn) throws SQLException {
        return Constants.compareVersions("1.0.1", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
    }

}
