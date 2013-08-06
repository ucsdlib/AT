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
 * Date: May 2, 2007
 * Time: 11:39:25 AM
 */

package org.archiviststoolkit.maintenance.upgrades;

import org.netbeans.spi.wizard.DeferredWizardResult;
import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.util.DatabaseConnectionUtils;
import org.apache.axis.types.NMToken;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UpgradeTo_1_0_12 extends Upgrade {

	protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {
		int currentStep = initialStep;
		try {
			progress.setProgress("Fixing lookup Container types items", currentStep++, numberOfSteps);
			fixListValues(conn, "Container types");

			progress.setProgress("Fixing lookup Finding aid status items", currentStep++, numberOfSteps);
			fixListValues(conn, "Finding aid status");

			progress.setProgress("Fixing lookup Calendar items", currentStep++, numberOfSteps);
			fixListValues(conn, "Calendar");

			progress.setProgress("Fixing lookup Era items", currentStep++, numberOfSteps);
			fixListValues(conn, "Era");

		} catch (SQLException e) {
			setErrorString(e.getMessage());
			return false;
		}

		return true;
	}

	protected int getNumberOfSteps() {
		return 4;
	}

	protected boolean upgradeNeeded(Connection conn) throws SQLException {
		return Constants.compareVersions("1.0.12", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
	}

	protected String getWarningMessage() {
		return "This upgrade will remove spaces and other illegal characters from lookup lists used for XML attributes.\n" +
				"Please look at the \"Container types\", \"Finding aid status\", \"Calendar\" and \"Era\" lists and clean up \n" +
				"any items by hand that did not get updated properly.";
	}

	protected boolean runLoadLookupLists() {
		return true;
	}
	
	protected boolean runFieldInit() {
		return true;
	}

	private void fixListValues(Connection conn, String listName) throws SQLException {
		PreparedStatement pstmtSelect = conn.prepareStatement("SELECT LookupListItems.lookupListItemId, \n" +
				"\tLookupListItems.listItem\n" +
				"FROM LookupListItems, LookupList\n" +
				"WHERE LookupList.listName = ? and LookupList.lookupListId = LookupListItems.lookupListId");

		PreparedStatement pstmtUpdate = conn.prepareStatement("UPDATE LookupListItems " +
				"SET listItem = ?\n" +
				"WHERE LookupListItems.lookupListItemId = ?");

		PreparedStatement pstmtUpdateResource = conn.prepareStatement("UPDATE Resources\n" +
				"SET findingAidStatus = ?\n" +
				"WHERE Resources.findingAidStatus = ?");

		String itemValue;
		Integer lookupListItemId;
		StringBuffer updatedItemValue;

		pstmtSelect.setString(1, listName);
		ResultSet rs = pstmtSelect.executeQuery();
		while (rs.next()) {
			lookupListItemId = rs.getInt(1);
			itemValue = rs.getString(2);
			if (!NMToken.isValid(itemValue)) {
				updatedItemValue = new StringBuffer();
				for (char c: itemValue.toCharArray()) {
					if (Character.isLetterOrDigit(c)) {
						updatedItemValue.append(c);
					} else if (c == '.' || c == '-' || c == '_' || c == ':') {
						updatedItemValue.append(c);
					} else {
						updatedItemValue.append('_');
					}
				}
				pstmtUpdate.setString(1, updatedItemValue.toString());
				pstmtUpdate.setInt(2, lookupListItemId);
				pstmtUpdate.execute();

				pstmtUpdateResource.setString(1, updatedItemValue.toString());
				pstmtUpdateResource.setString(2, itemValue);
				pstmtUpdateResource.executeUpdate();

			}
		}


	}

}
