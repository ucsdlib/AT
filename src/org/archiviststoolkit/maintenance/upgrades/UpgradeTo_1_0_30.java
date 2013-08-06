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
 * Date: Oct 15, 2007
 * Time: 4:15:38 PM
 */

package org.archiviststoolkit.maintenance.upgrades;

import org.netbeans.spi.wizard.DeferredWizardResult;
import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.util.DatabaseConnectionUtils;
import org.archiviststoolkit.hibernate.SessionFactory;

import java.sql.*;
import java.util.ArrayList;

public class UpgradeTo_1_0_30 extends Upgrade {

    protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {

        int currentStep = initialStep;
		String progressMessage = "Updating id generators";

        if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_MYSQL)) {
            try {
                String sqlString;
                Statement stmt = conn.createStatement();

                progress.setProgress(progressMessage, currentStep++, numberOfSteps);
                sqlString = "alter table Subjects change subjectId subjectId bigint(20) NOT NULL auto_increment";
                stmt.execute(sqlString);

                progress.setProgress(progressMessage, currentStep++, numberOfSteps);
                sqlString = "alter table Sessions change sessionId sessionId bigint(20) NOT NULL auto_increment";
                stmt.execute(sqlString);

            } catch (SQLException e) {
                setErrorString(e.getMessage());
                return false;
            }
        }

        return true;
            // everything is ok
 	}

	protected int getNumberOfSteps() {
        return 1;
    }

    protected boolean upgradeNeeded(Connection conn) throws SQLException {
        return Constants.compareVersions("1.0.30", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
    }

}
