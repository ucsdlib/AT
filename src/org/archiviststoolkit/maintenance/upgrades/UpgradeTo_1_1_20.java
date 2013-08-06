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
import org.netbeans.spi.wizard.DeferredWizardResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UpgradeTo_1_1_20 extends Upgrade {



	protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {

		int currentStep = initialStep;
		try {
			progress.setProgress("Fixing lookup list items - actuatenone -> none", currentStep++, numberOfSteps);
			fixListValues(conn, "actuate", "actuatenone", "none");
			updateFieldValue(conn, "ArchDescriptionRepeatingData", "actuate", "actuatenone", "none");
			updateFieldValue(conn, "FileVersions", "eadDaoActuate", "actuatenone", "none");
			updateFieldValue(conn, "DigitalObjects", "eadDaoActuate", "actuatenone", "none");

			progress.setProgress("Fixing lookup list items - actuateother -> other", currentStep++, numberOfSteps);
			fixListValues(conn, "actuate", "actuateother", "other");
			updateFieldValue(conn, "ArchDescriptionRepeatingData", "actuate", "actuateother", "other");
			updateFieldValue(conn, "FileVersions", "eadDaoActuate", "actuateother", "other");
			updateFieldValue(conn, "DigitalObjects", "eadDaoActuate", "actuateother", "other");

			progress.setProgress("Fixing lookup list items - shownone -> none", currentStep++, numberOfSteps);
			fixListValues(conn, "show", "shownone", "none");
			updateFieldValue(conn, "ArchDescriptionRepeatingData", "showz", "shownone", "none");
			updateFieldValue(conn, "FileVersions", "eadDaoShow", "shownone", "none");
			updateFieldValue(conn, "DigitalObjects", "eadDaoShow", "shownone", "none");

			progress.setProgress("Fixing lookup list items - showother -> other", currentStep++, numberOfSteps);
			fixListValues(conn, "show", "showother", "other");
			updateFieldValue(conn, "ArchDescriptionRepeatingData", "showz", "showother", "other");
			updateFieldValue(conn, "FileVersions", "eadDaoShow", "showother", "other");
			updateFieldValue(conn, "DigitalObjects", "eadDaoShow", "showother", "other");

		} catch (SQLException e) {
			setErrorString(e.getMessage());
			return false;
		}

		return true;
		// everything is ok
	}

	protected int getNumberOfSteps() {
		return 4;
	}

	protected boolean upgradeNeeded(Connection conn) throws SQLException {
		return Constants.compareVersions("1.1.20", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
	}

	protected String getWarningMessage() {
		return "This upgrade fixes two incorrect values in the Actuate and Show lookup " +
				"lists.  Correction of these values may cause duplication of the values " +
				"\"none\" and \"other\" within these lookup lists. This duplication will not " +
				"cause any operational problems.  However, it can be eliminated by " +
				"manually merging the duplicate values using the merge tool in the edit " +
				"lookup list function.";
	}


}