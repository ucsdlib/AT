package org.archiviststoolkit.maintenance.upgrades;

import org.netbeans.spi.wizard.DeferredWizardResult;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.model.ArchDescriptionAnalogInstances;
import org.archiviststoolkit.util.NameUtils;
import org.archiviststoolkit.util.UpgradeUtils;
import org.archiviststoolkit.util.DatabaseConnectionUtils;

import java.sql.*;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

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
 * Created by IntelliJ IDEA.
 *
 * User: Nathan Stevens
 * Date: Nov 25, 2008
 * Time: 10:50:35 AM
 */
public class UpgradeTo_1_5_2 extends Upgrade {
    protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {
		try {
			int currentStep = initialStep;

            String sqlString = "";
			Statement stmt = conn.createStatement();

            /** Remove all default values for Numeric and AlphaNumeric Indicator Values **/
            currentStep++;
			String progressMessage = "Modifying DefaultValues table";
			progress.setProgress(progressMessage, currentStep, numberOfSteps);

            // remove all default values for AnalogInstances which belonged to AlphaNumeric or Numeric Indicators
            int fieldId1 = UpgradeUtils.getFieldId(stmt, "ArchDescriptionAnalogInstances",ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER1_TYPE);
            int fieldId2 = UpgradeUtils.getFieldId(stmt, "ArchDescriptionAnalogInstances",ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER2_TYPE);
            int fieldId3 = UpgradeUtils.getFieldId(stmt, "ArchDescriptionAnalogInstances",ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER3_TYPE);

            sqlString = "DELETE FROM DefaultValues WHERE tableName = 'ArchDescriptionAnalogInstances' " +
                    "AND (fieldId != '"+ fieldId1 +"' AND fieldId != '"+ fieldId2 +"' AND fieldId != '"+ fieldId3 +"')";
            stmt.execute(sqlString);

            /** Create New Table Now **/
            /* execute statement for removing unique indexes from database
			if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_MYSQL)) {
				sqlString = "ALTER TABLE Names DROP INDEX UniqueNames";
				stmt.execute(sqlString);

			} else if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_ORACLE)) {
				//sqlString = "ALTER TABLE Names DROP CONSTRAINT UniqueNames";
				sqlString = "DROP INDEX UniqueNames";
				stmt.execute(sqlString);
			} else if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_MICROSOFT_SQL_SERVER)) {
				sqlString = "ALTER TABLE Names DROP CONSTRAINT UniqueNames";
				stmt.execute(sqlString);
			} */

			stmt.close();
		} catch (SQLException e) {
			setErrorString(e.getMessage());
			return false;
		}

		return true;
	}

	protected int getNumberOfSteps() {
		return 2;
	}

	protected boolean upgradeNeeded(Connection conn) throws SQLException {
		return Constants.compareVersions("1.5.2", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
	}

	protected boolean runFieldInit() {
		return true;
	}

    // run sql code after hibernate has initialized
    protected boolean runPostDBInitializationSQLCode(Connection conn) {
        try {
            String sqlString = "";
		    Statement stmt = conn.createStatement();

            /** Update the RDEScreenPanelItems table **/

			// remove all records with propertyName contains AlphaNumericIndicators
            sqlString = "DELETE FROM RDEScreenPanelItems WHERE propertyName LIKE '%AlphaNumericIndicator'";
            stmt.execute(sqlString);

            // rename all records whose propertyName contains NumericIndicators to Containe1Numeric Indicators
            sqlString = "UPDATE RDEScreenPanelItems SET propertyName = '" + ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER1_INDICATOR +
                    "' WHERE propertyName = '" + ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER1_NUMERIC_INDICATOR + "'";
            stmt.execute(sqlString);

            sqlString = "UPDATE RDEScreenPanelItems SET propertyName = '" + ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER2_INDICATOR +
                    "' WHERE propertyName = '" + ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER2_NUMERIC_INDICATOR + "'";
            stmt.execute(sqlString);

            sqlString = "UPDATE RDEScreenPanelItems SET propertyName = '" + ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER3_INDICATOR +
                    "' WHERE propertyName = '" + ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER3_NUMERIC_INDICATOR + "'";
            stmt.execute(sqlString);
        } catch (SQLException e) {
			setErrorString(e.getMessage());
			return false;
		}

        return true;
    }

    // warn the user that changes will be made to their database
    public String getWarningMessage() {
        String message = "Warning: changes to your data will be made during this upgrade. In cases where both " +
                "alphanumeric and numeric indicators are present for the same instance, only the numeric indicator " +
                "will be retained.";
        return message;
    }
}
