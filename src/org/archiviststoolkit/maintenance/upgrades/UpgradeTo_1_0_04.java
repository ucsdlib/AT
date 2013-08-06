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

import javax.swing.*;
import java.sql.*;
import java.util.Hashtable;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class UpgradeTo_1_0_04 extends Upgrade {

	protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {

		int currentStep = initialStep;
		try {
			Statement stmt = conn.createStatement();
			String sqlString;
			String progressMessage = "Updating note types";

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			//get the list of note types

			sqlString = "ALTER TABLE ArchDescriptionRepeatingData ADD notesEtcTypeId bigint";
			stmt.execute(sqlString);

			sqlString = "SELECT NotesEtcTypes.notesEtcTypeId, \n" +
					"NotesEtcTypes.notesEtcName\n" +
					"FROM NotesEtcTypes";
			ResultSet rs = stmt.executeQuery(sqlString);
			Hashtable<String, Integer> noteIdLookup = new Hashtable<String, Integer>();
			while (rs.next()) {
				noteIdLookup.put(rs.getString(2), rs.getInt(1));
			}

			PreparedStatement pstmtUpdate = conn.prepareStatement("UPDATE ArchDescriptionRepeatingData\n" +
					"SET notesEtcTypeId = ?\n" +
					"WHERE ArchDescriptionRepeatingData.noteType = ?");


			sqlString = "SELECT DISTINCT noteType\n" +
					"FROM ArchDescriptionRepeatingData\n" +
					"WHERE repeatingDataType = 'note'";
			rs = stmt.executeQuery(sqlString);
			String noteType;
			Integer noteId;
			List validNoteTypes = new ArrayList(noteIdLookup.keySet());
			Collections.sort(validNoteTypes);
			while (rs.next()) {
				noteType = rs.getString(1);
				if (noteType != null) {
					noteId = lookupNoteId(noteType, noteIdLookup);
					if (noteId == null) {
						boolean done = false;
						while (!done) {
							String selectedNoteType = (String) JOptionPane.showInputDialog(null, noteType + " is not a valid note type. Which note type should be used",
									"", JOptionPane.PLAIN_MESSAGE, null, validNoteTypes.toArray(), null);
							if (selectedNoteType != null) {
								noteId = noteIdLookup.get(selectedNoteType);
								done = true;
							} else {
								JOptionPane.showMessageDialog(null, "You must select a valid note type.");
							}
						}

					}
					if (noteId != null) {
						System.out.println("Updating: " + noteType);
						pstmtUpdate.setInt(1, noteId);
						pstmtUpdate.setString(2, noteType);
						pstmtUpdate.executeUpdate();
					}
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
		return Constants.compareVersions("1.0.4", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
	}

	private Integer lookupNoteId(String noteType, Hashtable<String, Integer> noteIdLookup) {
		Integer returnNoteId = noteIdLookup.get(noteType);
		if (returnNoteId != null) {
			return returnNoteId;
		} else {
			for (String key: noteIdLookup.keySet()) {
				if (key.equalsIgnoreCase(noteType.trim())) {
					return noteIdLookup.get(key);
				} else if (key.equalsIgnoreCase(noteType.trim() + " note")) {
					return noteIdLookup.get(key);
				}
			}
		}
		//if we got here then there is not match
		return null;
	}
}
