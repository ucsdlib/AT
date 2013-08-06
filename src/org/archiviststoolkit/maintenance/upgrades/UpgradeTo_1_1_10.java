package org.archiviststoolkit.maintenance.upgrades;

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
 * Date: Jul 18, 2008
 * Time: 9:09:19 PM
 */

import org.archiviststoolkit.exceptions.RDEPanelCreationException;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.maintenance.common.MaintenanceUtils;
import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.util.DatabaseConnectionUtils;
import org.netbeans.spi.wizard.DeferredWizardResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UpgradeTo_1_1_10 extends Upgrade {

	protected boolean runPostDBInitializationCode() {
		try {
			MaintenanceUtils.createDefaultRDEScreen();
		} catch (PersistenceException e) {
			setErrorString(e.getMessage());
			return false;
		} catch (RDEPanelCreationException e) {
			setErrorString(e.getMessage());
			return false;
		}
		return true;
	}

	protected boolean runFieldInit() {
		return true;
	}

	protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {
		String sqlString = "";
		Statement stmt;
//		if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_ORACLE)) {
			//set the includeInRDE to false in DatabaseFields Table
			try {
				int currentStep = initialStep;
				stmt = conn.createStatement();
				String progressMessage = "";

				// add the includeRDE column in the database field table
				currentStep++;
				progressMessage = "Adding IncludeInRDE column to DatabaseField table";
				progress.setProgress(progressMessage, currentStep, numberOfSteps);

				if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_ORACLE)) {
					sqlString = "ALTER TABLE DatabaseFields ADD includeInRDE NUMBER(1,0) DEFAULT 0 NOT NULL";
					stmt.execute(sqlString);

				} else if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_MYSQL)){
					sqlString = "ALTER TABLE DatabaseFields ADD includeInRDE bit(1) NOT NULL";
					stmt.execute(sqlString);
					sqlString = "UPDATE DatabaseFields SET includeInRDE = 1";
					stmt.execute(sqlString);
					
				} else if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_MICROSOFT_SQL_SERVER)) {
					sqlString = "ALTER TABLE DatabaseFields ADD includeInRDE tinyint NOT NULL DEFAULT 0";
                    stmt.execute(sqlString);
                    sqlString = "UPDATE DatabaseFields SET includeInRDE = 1";
					stmt.execute(sqlString);
				}
			} catch (SQLException e) {
				setErrorString(e.getMessage());
				return false;
			}
//		}
		return true;
	}

	protected int getNumberOfSteps() {
		return 1;
	}

	protected boolean upgradeNeeded(Connection conn) throws SQLException {
		return Constants.compareVersions("1.1.10", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
	}


}