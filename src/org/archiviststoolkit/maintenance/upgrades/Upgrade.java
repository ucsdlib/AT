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
 * Date: Feb 9, 2007
 * Time: 3:33:33 PM
 */

package org.archiviststoolkit.maintenance.upgrades;

import org.netbeans.spi.wizard.DeferredWizardResult;

import java.sql.*;

public abstract class Upgrade {

	protected String errorString;
    protected DeferredWizardResult.ResultProgressHandle progress;
    protected int currentStep;
    protected int totalSteps;

    public String getErrorString() {
		return errorString;
	}

	protected void setErrorString(String errorString) {
		this.errorString = errorString;
	}


	protected abstract boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress);

	protected abstract int getNumberOfSteps();

	protected abstract boolean upgradeNeeded(Connection conn) throws SQLException;

	protected String getWarningMessage() {
		return null;
	}

	protected boolean runFieldInit() {
		return false;
	}

	protected boolean runLoadLookupLists() {
		return false;
	}

	protected boolean runDetermineSequenceNumbers() {
		return false;
	}

	protected boolean runPostDBInitializationCode() {
		return true;
	}

    /**
     * Method to run SQL code that needs to be executed after hibernate has been initialized
     * should circumvent some of the chicken and the egg problem where a table that hibernate creates needs
     * to be updated by SQL code cause it is just easier
     * @param conn The connection to the database
     * @return Whether the sql commands executed without error
     */
    protected boolean runPostDBInitializationSQLCode(Connection conn) {
        return true;
    }

    /**
     * Method to set the progress bar so that users can alerted at the progress inside
     * the step it self.
     *
     * @param progress The progress monitor
     * @param currentStep The current step
     * @param totalSteps  The total number of steps
     */
    protected void setResultProgressHandle(DeferredWizardResult.ResultProgressHandle progress, int currentStep, int totalSteps) {
        this.progress = progress;
        this.currentStep = currentStep;
        this.totalSteps = totalSteps;
    }

	protected void fixListValues(Connection conn, String listName, String oldListItem, String newListItem) throws SQLException {
		PreparedStatement pstmtSelect = conn.prepareStatement("SELECT LookupListItems.lookupListItemId \n" +
				"FROM LookupListItems, LookupList\n" +
				"WHERE LookupList.listName = ? and LookupListItems.listItem = ? and LookupList.lookupListId = LookupListItems.lookupListId");

		PreparedStatement pstmtUpdate = conn.prepareStatement("UPDATE LookupListItems " +
				"SET listItem = ?\n" +
				"WHERE LookupListItems.lookupListItemId = ?");

		Integer lookupListItemId;

		pstmtSelect.setString(1, listName);
		pstmtSelect.setString(2, oldListItem);
		ResultSet rs = pstmtSelect.executeQuery();
		if (rs.next()) {
			lookupListItemId = rs.getInt(1);
			pstmtUpdate.setString(1, newListItem);
			pstmtUpdate.setInt(2, lookupListItemId);
			pstmtUpdate.execute();
		}
	}

	//todo this needs to be made more generic to handle all data types
	protected void updateFieldValue(Connection conn, String tableName, String fieldName, String oldValue, String newValue) throws SQLException {

		PreparedStatement pstmtUpdateResource = conn.prepareStatement("UPDATE " + tableName +
				" SET " + fieldName + " = ?\n" +
				"WHERE " + tableName + "." + fieldName + " LIKE ?");

		pstmtUpdateResource.setString(1, newValue);
		pstmtUpdateResource.setString(2, oldValue);
		pstmtUpdateResource.executeUpdate();
    }
}
