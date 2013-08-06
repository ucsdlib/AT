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

import org.netbeans.spi.wizard.DeferredWizardResult;
import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.util.DatabaseConnectionUtils;
import org.archiviststoolkit.structure.ATFieldInfo;

import java.sql.*;
import java.util.Hashtable;
import java.util.ArrayList;

public class UpgradeTo_1_0_05 extends Upgrade {

	private Hashtable<String, ArrayList<PreparedStatement>> updateStatements;

	public void createUpdateStatments(Connection conn) throws SQLException {
		updateStatements = new Hashtable<String, ArrayList<PreparedStatement>>();
		ArrayList<PreparedStatement> statements = new ArrayList<PreparedStatement>();
		statements.add(conn.prepareStatement("UPDATE Resources SET languageCode = ?\n" +
				"WHERE Resources.languageCode = ?"));
		statements.add(conn.prepareStatement("UPDATE ResourcesComponents SET languageCode = ?\n" +
				"WHERE ResourcesComponents.languageCode = ?"));
		statements.add(conn.prepareStatement("UPDATE DigitalObjects SET languageCode = ?\n" +
				"WHERE DigitalObjects.languageCode = ?"));
		statements.add(conn.prepareStatement("UPDATE Repositories SET descriptiveLanguage = ?\n" +
				"WHERE Repositories.descriptiveLanguage = ?"));
		updateStatements.put("Language codes", statements);

		statements = new ArrayList<PreparedStatement>();
		statements.add(conn.prepareStatement("UPDATE ArchDescriptionNames SET role = ?\n" +
				"WHERE ArchDescriptionNames.role = ?"));
		updateStatements.put("Name link creator / subject role", statements);
		updateStatements.put("Name link source role", statements);

		statements = new ArrayList<PreparedStatement>();
		statements.add(conn.prepareStatement("UPDATE Subjects SET subjectSource = ?\n" +
				"WHERE Subjects.subjectSource = ?"));
		updateStatements.put("Subject term source", statements);

		statements = new ArrayList<PreparedStatement>();
		statements.add(conn.prepareStatement("UPDATE Resources SET descriptionRules = ?\n" +
				"WHERE Resources.descriptionRules = ?"));
		updateStatements.put("Description rules", statements);
	}

	protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {

		int currentStep = initialStep;
		try {
			String progressMessage = "Adding codes to lookup lists";
			progress.setProgress(progressMessage, currentStep++, numberOfSteps);

			changeMysqlDatabaseEngine(conn);
			addLanguageCodeIndicies(conn);
			createUpdateStatments(conn);
			Statement stmt = conn.createStatement();
			String sqlString;

			//add code field for lookupListItems
			sqlString = "ALTER TABLE LookupListItems ADD code varchar(255)";
			try {
				stmt.execute(sqlString);
			} catch (SQLException e) {
				if (e.getMessage().contains("Duplicate column")) {
					//do nothing
				} else {
					throw e;
				}
			}

			//add pairedValues field to lookupLists
			sqlString = "ALTER TABLE LookupList ADD pairedValues bit(1) NOT NULL";
			try {
				stmt.execute(sqlString);
			} catch (SQLException e) {
				if (e.getMessage().contains("Duplicate column")) {
					//do nothing
				} else {
					throw e;
				}
			}
			addFieldToApplicationConfiguration(conn);
//			ATFieldInfo.loadFieldList();
//			ATFieldInfo.initDatabaseTables();

			sqlString = "UPDATE LookupList SET pairedValues = false";
			stmt.execute(sqlString);

			PreparedStatement pstmtUpdate = conn.prepareStatement("UPDATE LookupList SET pairedValues = true\n" +
					"WHERE LookupList.listName = ?");
			pstmtUpdate.setString(1, "Language codes");
			pstmtUpdate.executeUpdate();
			pstmtUpdate.setString(1, "Name link creator / subject role");
			pstmtUpdate.executeUpdate();
			pstmtUpdate.setString(1, "Name link source role");
			pstmtUpdate.executeUpdate();
			pstmtUpdate.setString(1, "Subject term source");
			pstmtUpdate.executeUpdate();
			pstmtUpdate.setString(1, "Description rules");
			pstmtUpdate.executeUpdate();

			sqlString = "SELECT LookupList.lookupListId, LookupList.listName\n" +
					"FROM LookupList WHERE LookupList.pairedValues = true";

			pstmtUpdate = conn.prepareStatement("UPDATE LookupListItems SET listItem = ?, code= ?\n" +
					"WHERE LookupListItems.lookupListItemId = ?");

			PreparedStatement pstmtSelect = conn.prepareStatement("SELECT LookupListItems.lookupListItemId, \n" +
					"\tLookupListItems.listItem\n" +
					"FROM LookupListItems\n" +
					"WHERE lookupListId = ? ORDER BY LookupListItems.listItem ASC");

			ResultSet rs = stmt.executeQuery(sqlString);
			ResultSet listItemsResultSet;
			int listId, listItemId, position;
			String listItemOriginal, code, listItemNew, listName;
			while (rs.next()) {
				listId = rs.getInt(1);
				listName = rs.getString(2);
				pstmtSelect.setInt(1, listId);
				listItemsResultSet = pstmtSelect.executeQuery();
				while (listItemsResultSet.next()) {
					listItemId = listItemsResultSet.getInt(1);
					listItemOriginal = listItemsResultSet.getString(2);

					position = listItemOriginal.indexOf(";");
					code = "";
					listItemNew = listItemOriginal;
					if (position != -1) {
						code = listItemOriginal.substring(position + 1).trim();
						listItemNew = listItemOriginal.substring(0, position).trim();
					} else {
						position = listItemOriginal.lastIndexOf("(");
						if (position != -1) {
							code = listItemOriginal.substring(position + 1);
							code = code.replace(")", "");
							code = code.trim();
							listItemNew = listItemOriginal.substring(0, position).trim();
						}
					}
					if (code.length() == 0) {
						code = listItemNew.replace(" ", "");
					}
					progressMessage = "Parse:" + listItemOriginal + "->" + listItemNew + ", " + code;
//					System.out.println(progressMessage);
					progress.setProgress(progressMessage, currentStep, numberOfSteps);
					pstmtUpdate.setString(1, listItemNew);
					pstmtUpdate.setString(2, code);
					pstmtUpdate.setInt(3, listItemId);
					pstmtUpdate.executeUpdate();
					updateValues(listName, listItemOriginal, listItemNew);
				}
			}



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
		return 1;
	}

	protected boolean upgradeNeeded(Connection conn) throws SQLException {
		return Constants.compareVersions("1.0.5", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
	}

	protected String getWarningMessage() {
		return "This upgrade will fail if there are duplicate subject terms. Please clean up the duplicates before running the upgrade." +
				"\nIf you receive a duplicate key error message when running the upgrade this is the problem.";
	}

	private void changeMysqlDatabaseEngine(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		String sql = "ALTER TABLE Accessions TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE AccessionsLocations TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE AccessionsResources TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE ArchDescriptionInstances TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE ArchDescriptionNames TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE ArchDescriptionRepeatingData TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE ArchDescriptionSubjects TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE BibItems TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE ChronologyItems TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE Constants TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE DatabaseFields TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE DatabaseTables TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE Deaccessions TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE DefaultValues TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE DigitalObjects TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE Events TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE FileVersions TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE InLineTagAttributes TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE InLineTags TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE IndexItems TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE ListDefinitionItems TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE ListOrderedItems TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE LocationsTable TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE LookupList TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE LookupListItems TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE Names TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE NonPreferredNames TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE NotesEtcTypes TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE Repositories TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE RepositoryNotesDefaultValues TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE Resources TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE ResourcesComponents TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE Sessions TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE SimpleRepeatableNotes TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE Subjects TYPE = InnoDB";
		stmt.execute(sql);
		sql = "ALTER TABLE Users TYPE = InnoDB";
		stmt.execute(sql);
	}

	private void updateValues(String listName, String oldValue, String newValue) throws SQLException {
		ArrayList<PreparedStatement> statements = updateStatements.get(listName);
		if (statements != null) {
			for (PreparedStatement statement: statements) {
				statement.setString(1, newValue);
				statement.setString(2, oldValue);
				statement.executeUpdate();
			}
		}

	}

	protected void addLanguageCodeIndicies(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();

		String sql = "ALTER TABLE Resources DROP INDEX languageCode";
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			//do nothing since it probably means the index does not exist
		}

		sql = "ALTER TABLE Resources ADD INDEX (languageCode)";
		stmt.execute(sql);

		sql = "ALTER TABLE ResourcesComponents DROP INDEX languageCode";
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			//do nothing since it probably means the index does not exist
		}

		sql = "ALTER TABLE ResourcesComponents ADD INDEX (languageCode)";
		stmt.execute(sql);

		sql = "ALTER TABLE DigitalObjects DROP INDEX languageCode";
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			//do nothing since it probably means the index does not exist
		}

		sql = "ALTER TABLE DigitalObjects ADD INDEX (languageCode)";
		stmt.execute(sql);

		sql = "ALTER TABLE Repositories DROP INDEX descriptiveLanguage";
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			//do nothing since it probably means the index does not exist
		}

		sql = "ALTER TABLE Repositories ADD INDEX (descriptiveLanguage)";
		stmt.execute(sql);

	}

	protected void addFieldToApplicationConfiguration(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();

		String sql = "SELECT DatabaseTables.tableId\n" +
				"FROM DatabaseTables\n" +
				"WHERE tableName = 'LookupList'";

		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next()) {
			int databaseTableId = rs.getInt(1);
			PreparedStatement pstmtInsert = conn.prepareStatement("INSERT INTO DatabaseFields (version, fieldName, fieldLabel, " +
					"definition, examples, lookupList, includeInSearchEditor, excludeFromDefaultValues, dataType, returnScreenOrder, tableId)\n" +
					"VALUES (0, 'pairedValues', 'Paired Values', '', '', '', false, false, 'java.lang.Boolean', 0, ?)");
			pstmtInsert.setInt(1, databaseTableId);
			pstmtInsert.execute();
		}
	}

	protected boolean runFieldInit() {
		return true;
	}
}
