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
 * Date: Aug 8, 2007
 * Time: 12:07:01 PM
 */

package org.archiviststoolkit.maintenance.upgrades;

import org.netbeans.spi.wizard.DeferredWizardResult;
import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.util.DatabaseConnectionUtils;
import org.archiviststoolkit.structure.lookupListSchema.AtLookupLists;
import org.archiviststoolkit.structure.lookupListSchema.Pair;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Arrays;
import java.io.File;

public class UpgradeTo_1_0_22 extends Upgrade {

    protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {

        int currentStep = initialStep;
		String progressMessage = "Dropping columns";
        try {
            Statement stmt = conn.createStatement();
            String sqlString;

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE ArchDescriptionNames DROP lastUpdated";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE ArchDescriptionNames DROP created";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE ArchDescriptionNames DROP lastUpdatedBy";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE ArchDescriptionNames DROP createdBy";
			stmt.execute(sqlString);

			progressMessage = "Deleting default values";
			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "delete from DefaultValues";
			stmt.execute(sqlString);

			progressMessage = "Deleting language code lookup list";
			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "SELECT LookupList.lookupListId FROM LookupList WHERE listName = 'Language codes'";
			ResultSet rs = stmt.executeQuery(sqlString);
			if (rs.next()) {
				int id = rs.getInt(1);
				sqlString = "delete from LookupListItems where LookupListItems.LookupListID=" + id;
				stmt.execute(sqlString);
			} else {
				setErrorString("Problem deleting language code lookup list values");
				return false;
 			}

			stmt.close();

			progressMessage = "Checking language values";
			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			updateLanguages(conn);
			return true;
            // everything is ok
        }
        catch (SQLException e) {
            setErrorString(e.getMessage());
            return false;
        } catch (JAXBException e) {
			setErrorString(e.getMessage());
			 return false;
 		}
	}

    protected int getNumberOfSteps() {
        return 7;
    }

    protected boolean upgradeNeeded(Connection conn) throws SQLException {
        return Constants.compareVersions("1.0.22", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
    }

	protected boolean runLoadLookupLists() {
		return true;
	}

	protected String getWarningMessage() {
		return "This upgrade will delete all default values. They will need to be re-entered";
	}

	private void updateLanguages(Connection conn) throws SQLException, JAXBException {
		//first get all the unique languages used in the data
		ArrayList<String> languagesInData = new ArrayList<String>();
		Statement stmt = conn.createStatement();
		String sqlString = "SELECT distinct Resources.languageCode\n" +
				"FROM Resources";
		ResultSet rs = stmt.executeQuery(sqlString);
		while (rs.next()) {
			languagesInData.add(rs.getString(1));
		}

		sqlString = "SELECT distinct ResourcesComponents.languageCode\n" +
				"FROM ResourcesComponents";
		rs = stmt.executeQuery(sqlString);
		while (rs.next()) {
			String language = rs.getString(1);
			if (!languagesInData.contains(language)) {
				languagesInData.add(language);
			}
		}

		sqlString = "SELECT distinct DigitalObjects.languageCode\n" +
				"FROM DigitalObjects";
		rs = stmt.executeQuery(sqlString);
		while (rs.next()) {
			String language = rs.getString(1);
			if (!languagesInData.contains(language)) {
				languagesInData.add(language);
			}
		}

		sqlString = "SELECT distinct Repositories.descriptiveLanguage\n" +
				"FROM Repositories";
		rs = stmt.executeQuery(sqlString);
		while (rs.next()) {
			String language = rs.getString(1);
			if (!languagesInData.contains(language)) {
				languagesInData.add(language);
			}
		}

		stmt.close();
		
		for (String language: languagesInData) {
			System.out.println("Unique language: " + language);
		}

		//now get the language list from the xml file
		Hashtable<String, String> listElements = new Hashtable<String, String>();
		JAXBContext context = JAXBContext.newInstance("org.archiviststoolkit.structure.lookupListSchema");
		AtLookupLists atLookupLists = (AtLookupLists) context.createUnmarshaller().unmarshal(new File("conf/lookupListValues.xml"));
		for (org.archiviststoolkit.structure.lookupListSchema.List list : atLookupLists.getList()) {
			if (list.getName().equals("Language codes")) {
				for (Pair pair : list.getPair()) {
					listElements.put(pair.getValue().getContent(), pair.getCode());
				}

			}
		}

		for (String key: listElements.keySet()) {
			System.out.println(listElements.get(key) + ": " + key);
		}

		PreparedStatement pstmtUpdateResources = conn.prepareStatement("UPDATE Resources\n" +
				"SET languageCode = ?\n" +
				"WHERE Resources.languageCode = ?");

		PreparedStatement pstmtUpdateComponents = conn.prepareStatement("UPDATE ResourcesComponents\n" +
				"SET languageCode = ?\n" +
				"WHERE ResourcesComponents.languageCode = ?");

		PreparedStatement pstmtUpdateDO = conn.prepareStatement("UPDATE DigitalObjects\n" +
				"SET languageCode = ?\n" +
				"WHERE DigitalObjects.languageCode = ?");

		PreparedStatement pstmtUpdateRepositories = conn.prepareStatement("UPDATE Repositories\n" +
				"SET descriptiveLanguage = ?\n" +
				"WHERE Repositories.descriptiveLanguage = ?");

		Object[] languageLookup = listElements.keySet().toArray();
		Arrays.sort(languageLookup);

		for (String language: languagesInData) {
			System.out.println("Unique language: " + language);
			if (language != null && language.length() != 0 && !listElements.containsKey(language)) {
				String selectedLanguage = (String) JOptionPane.showInputDialog(null, language + " is not a valid language type. Which language should be used",
						"", JOptionPane.PLAIN_MESSAGE, null, languageLookup, null);
				if (selectedLanguage != null) {
					System.out.println("Updating: " + language + " -> " + selectedLanguage);

					pstmtUpdateResources.setString(1, selectedLanguage);
					pstmtUpdateResources.setString(2, language);
					pstmtUpdateResources.executeUpdate();

					pstmtUpdateComponents.setString(1, selectedLanguage);
					pstmtUpdateComponents.setString(2, language);
					pstmtUpdateComponents.executeUpdate();

					pstmtUpdateDO.setString(1, selectedLanguage);
					pstmtUpdateDO.setString(2, language);
					pstmtUpdateDO.executeUpdate();

					pstmtUpdateRepositories.setString(1, selectedLanguage);
					pstmtUpdateRepositories.setString(2, language);
					pstmtUpdateRepositories.executeUpdate();

				}
			}
		}
	}

}
