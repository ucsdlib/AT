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
 * Date: Oct 11, 2007
 * Time: 1:35:22 PM
 */

package org.archiviststoolkit.maintenance.upgrades;

import org.netbeans.spi.wizard.DeferredWizardResult;
import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.util.DatabaseConnectionUtils;
import org.archiviststoolkit.structure.lookupListSchema.AtLookupLists;
import org.archiviststoolkit.structure.lookupListSchema.Pair;

import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBContext;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Arrays;
import java.io.File;

public class UpgradeTo_1_0_29 extends Upgrade {

    protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {

        int currentStep = initialStep;
		String progressMessage = "Updating resource and component attribute values";
        try {

			ArrayList<String> resourceLevels = new ArrayList<String>();
			resourceLevels.add("collection");
			resourceLevels.add("fonds");
			resourceLevels.add("item");
			resourceLevels.add("recordgrp");
			resourceLevels.add("otherlevel");

			ArrayList<String> resourceComponentLevels = new ArrayList<String>();
			resourceComponentLevels.add("class");
			resourceComponentLevels.add("file");
			resourceComponentLevels.add("item");
			resourceComponentLevels.add("otherlevel");
			resourceComponentLevels.add("series");
			resourceComponentLevels.add("subfonds");
			resourceComponentLevels.add("subgroup");
			resourceComponentLevels.add("subseries");

			Statement stmt = conn.createStatement();
            String sqlString;

			PreparedStatement pstmtSelectListId = conn.prepareStatement("SELECT LookupList.lookupListId " +
					"FROM LookupList\n" +
					"WHERE listName = ? ");

			PreparedStatement pstmtSelectListItems = conn.prepareStatement("SELECT LookupListItems.lookupListItemId, \n" +
					"\tLookupListItems.listItem\n" +
					"FROM LookupListItems\n" +
					"WHERE lookupListId = ? ORDER BY LookupListItems.listItem ASC");

			PreparedStatement pstmtUpdate = conn.prepareStatement("UPDATE LookupListItems SET listItem = ?, editable = ?\n" +
					"WHERE LookupListItems.lookupListItemId = ?");

			PreparedStatement pstmtUpdateResources = conn.prepareStatement("UPDATE Resources SET Resources.resourceLevel = ?\n" +
					"WHERE Resources.resourceLevel = ?");

			PreparedStatement pstmtUpdateResourceComponentss = conn.prepareStatement("UPDATE ResourcesComponents SET ResourcesComponents.resourceLevel = ?\n" +
					"WHERE ResourcesComponents.resourceLevel = ?");

			//fix resource levels
			pstmtSelectListId.setString(1, "Resource levels");
			ResultSet rs = pstmtSelectListId.executeQuery();
			if (rs.next()) {
				int listId = rs.getInt(1);
				pstmtSelectListItems.setInt(1, listId);
				ResultSet listItems = pstmtSelectListItems.executeQuery();
				int listItemId;
				String listItem;
				while (listItems.next()) {
					listItemId = listItems.getInt(1);
					listItem = listItems.getString(2);
					updateValue(listItem, resourceLevels, listItemId, pstmtUpdate, pstmtUpdateResources);
				}
			} else {
				setErrorString("Resource levels list is missing");
				return false;
			}

			//fix resource component levels
			pstmtSelectListId.setString(1, "Resource component levels");
			rs = pstmtSelectListId.executeQuery();
			if (rs.next()) {
				int listId = rs.getInt(1);
				pstmtSelectListItems.setInt(1, listId);
				ResultSet listItems = pstmtSelectListItems.executeQuery();
				int listItemId;
				String listItem;
				while (listItems.next()) {
					listItemId = listItems.getInt(1);
					listItem = listItems.getString(2);
					updateValue(listItem, resourceComponentLevels, listItemId, pstmtUpdate, pstmtUpdateResourceComponentss);
				}
			} else {
				setErrorString("Resource component levels list is missing");
				return false;
			}

			return true;
            // everything is ok
        }
        catch (SQLException e) {
            setErrorString(e.getMessage());
            return false;
 		}
	}

	private void updateValue(String term, ArrayList<String> validTerms, int listIt,
							 PreparedStatement pstmtUpdate, PreparedStatement pstmtUpdateRecords) throws SQLException {

		for (String validTerm: validTerms) {
			if (term.equals(validTerm)) {
				//do nothing this one is valid
				return;
			} else if (term.equalsIgnoreCase(validTerm)) {
				//we have a change of case
				pstmtUpdate.setString(1, validTerm);
				pstmtUpdate.setBoolean(2, false);
				pstmtUpdate.setInt(3, listIt);
				pstmtUpdate.executeUpdate();

				pstmtUpdateRecords.setString(1, validTerm);
				pstmtUpdateRecords.setString(2, term);
				pstmtUpdateRecords.executeUpdate();
				return;
			}
		}

		//if we got here none of the terms match so just make sure it is editable
		pstmtUpdate.setString(1, term);
		pstmtUpdate.setBoolean(2, true);
		pstmtUpdate.setInt(3, listIt);
		pstmtUpdate.executeUpdate();
	}

	protected int getNumberOfSteps() {
        return 1;
    }

    protected boolean upgradeNeeded(Connection conn) throws SQLException {
        return Constants.compareVersions("1.0.29", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
    }

	protected String getWarningMessage() {
		return "This upgrade will correct problems with resource and component level attributes. You will need to go back and clean up" +
				" the lists to merge any invalid entries.";
	}
}
