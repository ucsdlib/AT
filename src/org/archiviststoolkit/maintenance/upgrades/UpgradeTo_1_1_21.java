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

import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.DomainAccessObject;
import org.archiviststoolkit.mydomain.DomainAccessObjectFactory;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.util.DatabaseConnectionUtils;
import org.archiviststoolkit.util.RDEFactory;
import org.archiviststoolkit.exceptions.RDEPanelCreationException;
import org.archiviststoolkit.maintenance.common.MaintenanceUtils;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.netbeans.spi.wizard.DeferredWizardResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UpgradeTo_1_1_21 extends Upgrade {


	protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {

		int currentStep = initialStep;
		try {
			String sqlString = "";
			Statement stmt = conn.createStatement();
			// execute statement for removing unique indexes from database
			if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_MYSQL)) {

				progress.setProgress("Modifying field datatypes", currentStep++, numberOfSteps);
				sqlString = "ALTER TABLE Events MODIFY COLUMN eventDescription text";
				stmt.execute(sqlString);

				progress.setProgress("Modifying field datatypes", currentStep++, numberOfSteps);
				sqlString = "ALTER TABLE ArchDescriptionRepeatingData MODIFY COLUMN actuate VARCHAR(255)";
				stmt.execute(sqlString);

				progress.setProgress("Modifying field datatypes", currentStep++, numberOfSteps);
				sqlString = "ALTER TABLE ArchDescriptionRepeatingData MODIFY COLUMN showz VARCHAR(255)";
				stmt.execute(sqlString);

			} else if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_ORACLE)) {
                /* Since Oracle doesn't allow direct conversion from varchar2 to CLOB then using the
                 * work around found at http://forums.oracle.com/forums/thread.jspa?threadID=411705
                 */

                progress.setProgress("Modifying field datatypes", currentStep++, numberOfSteps);
				//sqlString = "ALTER TABLE Events MODIFY (eventDescription CLOB)";
                sqlString = "ALTER TABLE Events ADD (tmp_eventDescription CLOB NULL)";
                stmt.execute(sqlString);

                sqlString = "UPDATE Events SET tmp_eventDescription = eventDescription";
                stmt.execute(sqlString);

                sqlString = "ALTER TABLE Events DROP COLUMN eventDescription";
                stmt.execute(sqlString);

                sqlString = "ALTER TABLE Events RENAME COLUMN tmp_eventDescription TO eventDescription";
                stmt.execute(sqlString);

                progress.setProgress("Modifying field datatypes", currentStep++, numberOfSteps);
				//sqlString = "ALTER TABLE ArchDescriptionRepeatingData MODIFY (actuate VARCHAR2(255))";
				sqlString = "ALTER TABLE ArchDescriptionRepeatingData ADD (tmp_actuate VARCHAR2(255) NULL)";
                stmt.execute(sqlString);

                sqlString = "UPDATE ArchDescriptionRepeatingData SET tmp_actuate = actuate";
                stmt.execute(sqlString);

                sqlString = "ALTER TABLE ArchDescriptionRepeatingData DROP COLUMN actuate";
                stmt.execute(sqlString);

                sqlString = "ALTER TABLE ArchDescriptionRepeatingData RENAME COLUMN tmp_actuate TO actuate";
                stmt.execute(sqlString);

                progress.setProgress("Modifying field datatypes", currentStep++, numberOfSteps);
				//sqlString = "ALTER TABLE ArchDescriptionRepeatingData MODIFY (showz VARCHAR2(255))";
                sqlString = "ALTER TABLE ArchDescriptionRepeatingData ADD (tmp_showz VARCHAR2(255) NULL)";
                stmt.execute(sqlString);

                sqlString = "UPDATE ArchDescriptionRepeatingData SET tmp_showz = showz";
                stmt.execute(sqlString);

                sqlString = "ALTER TABLE ArchDescriptionRepeatingData DROP COLUMN showz";
                stmt.execute(sqlString);

                sqlString = "ALTER TABLE ArchDescriptionRepeatingData RENAME COLUMN tmp_showz TO showz";
                stmt.execute(sqlString);
            } else if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_MICROSOFT_SQL_SERVER)) {

				progress.setProgress("Modifying field datatypes", currentStep++, numberOfSteps);
				sqlString = "ALTER TABLE Events ALTER COLUMN eventDescription text";
				stmt.execute(sqlString);

				progress.setProgress("Modifying field datatypes", currentStep++, numberOfSteps);
				sqlString = "ALTER TABLE ArchDescriptionRepeatingData ALTER COLUMN actuate VARCHAR(255)";
				stmt.execute(sqlString);

				progress.setProgress("Modifying field datatypes", currentStep++, numberOfSteps);
				sqlString = "ALTER TABLE ArchDescriptionRepeatingData ALTER COLUMN showz VARCHAR(255)";
				stmt.execute(sqlString);

			}
		} catch (SQLException e) {
			setErrorString(e.getMessage());
			return false;
		}
		return true;
		// everything is ok
	}

	protected boolean runFieldInit() {
		return true;
	}

	protected int getNumberOfSteps() {
		return 3;
	}

	protected boolean upgradeNeeded(Connection conn) throws SQLException {
		return Constants.compareVersions("1.1.21", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
	}


}