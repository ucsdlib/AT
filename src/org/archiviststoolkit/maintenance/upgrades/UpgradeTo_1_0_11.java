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
 * Date: Apr 18, 2007
 * Time: 10:38:48 AM
 */

package org.archiviststoolkit.maintenance.upgrades;

import org.netbeans.spi.wizard.DeferredWizardResult;
import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.util.DatabaseConnectionUtils;

import java.sql.*;

public class UpgradeTo_1_0_11 extends Upgrade {

	protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {

		int currentStep = initialStep;
		String progressMessage = "Modifying locations table";
		try {
			Statement stmt = conn.createStatement();
			String sqlString;

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "Alter table LocationsTable drop index UniqueLocations ";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE LocationsTable CHANGE coordinate1 coordinate1Label varchar(50)";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE LocationsTable CHANGE coordinate2 coordinate2Label varchar(50)";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE LocationsTable CHANGE coordinate3 coordinate3Label varchar(50)";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE LocationsTable ADD coordinate1NumericIndicator double";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE LocationsTable ADD coordinate2NumericIndicator double";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE LocationsTable ADD coordinate3NumericIndicator double";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE LocationsTable ADD coordinate1AlphaNumericIndicator varchar(20)";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE LocationsTable ADD coordinate2AlphaNumericIndicator varchar(20)";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE LocationsTable ADD coordinate3AlphaNumericIndicator varchar(20)";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "UPDATE DatabaseFields SET stringLengthLimit = null WHERE fieldName = 'coordinate1'";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "UPDATE DatabaseFields SET stringLengthLimit = null WHERE fieldName = 'coordinate2'";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "UPDATE DatabaseFields SET stringLengthLimit = null WHERE fieldName = 'coordinate3'";
			stmt.execute(sqlString);

			//first clean up any null values then change to int not null
			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "UPDATE DigitalObjects SET objectOrder = 0 WHERE objectOrder is null";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE DigitalObjects CHANGE objectOrder objectOrder int NOT NULL";
			stmt.execute(sqlString);

			progress.setProgress(progressMessage, currentStep++, numberOfSteps);
			sqlString = "ALTER TABLE LocationsTable\n" +
					"  ADD UNIQUE UniqueLocations (building,floor,room,area,coordinate1Label,coordinate1NumericIndicator,coordinate1AlphaNumericIndicator,\n" +
					"  coordinate2Label,coordinate2NumericIndicator,coordinate2AlphaNumericIndicator,\n" +
					"  coordinate3Label,coordinate3NumericIndicator,coordinate3AlphaNumericIndicator,classificationNumber,barcode)";
			stmt.execute(sqlString);



			sqlString = "SELECT LocationsTable.locationId, LocationsTable.coordinate1Label, LocationsTable.coordinate2Label, LocationsTable.coordinate3Label\n" +
					"FROM LocationsTable";

			PreparedStatement pstmtUpdate = conn.prepareStatement("UPDATE LocationsTable " +
					"SET coordinate1Label = ?, coordinate1NumericIndicator= ?, coordinate1AlphaNumericIndicator= ?," +
					"coordinate2Label = ?, coordinate2NumericIndicator= ?, coordinate2AlphaNumericIndicator= ?," +
					"coordinate3Label = ?, coordinate3NumericIndicator= ?, coordinate3AlphaNumericIndicator= ?\n" +
					"WHERE LocationsTable.locationId = ?");


			ResultSet rs = stmt.executeQuery(sqlString);
			int locationId;
			String coordinate1LabelOld, coordinate2LabelOld, coordinate3LabelOld;
			String coordinate1LabelNew, coordinate2LabelNew, coordinate3LabelNew;
			Double coordinate1NumericIndicator, coordinate2NumericIndicator, coordinate3NumericIndicator;
			String coordinate1AlphaNumericIndicator, coordinate2AlphaNumericIndicator, coordinate3AlphaNumericIndicator;
			Boolean update;
			int lastSpaceLocation;
			Double number;
			String beginning;
			String end;
			Integer count = 1;

			while (rs.next()) {
				locationId = rs.getInt(1);
				coordinate1LabelOld = rs.getString(2);
				coordinate2LabelOld = rs.getString(3);
				coordinate3LabelOld = rs.getString(4);

				coordinate1LabelNew = coordinate1LabelOld;
				coordinate1NumericIndicator = null;
				coordinate1AlphaNumericIndicator = "";
				coordinate2LabelNew = coordinate2LabelOld;
				coordinate2NumericIndicator = null;
				coordinate2AlphaNumericIndicator = "";
				coordinate3LabelNew = coordinate3LabelOld;
				coordinate3NumericIndicator = null;
				coordinate3AlphaNumericIndicator = "";

				update = false;

				if (coordinate1LabelOld != null && coordinate1LabelOld.length() > 0) {
					lastSpaceLocation = coordinate1LabelOld.lastIndexOf(" ");
					if (lastSpaceLocation == -1) {
						//just check to see if this is a number
						try {
							number = Double.valueOf(coordinate1LabelOld);
							coordinate1LabelNew = "";
							coordinate1AlphaNumericIndicator = "";
							coordinate1NumericIndicator = number;
							update = true;
						} catch (NumberFormatException e) {
							//do nothing and leave things alone
						}
					} else {
						beginning = coordinate1LabelOld.substring(0, lastSpaceLocation).trim();
						end = coordinate1LabelOld.substring(lastSpaceLocation).trim();
						try {
							number = Double.valueOf(end);
							coordinate1LabelNew = beginning;
							coordinate1AlphaNumericIndicator = "";
							coordinate1NumericIndicator = number;
							update = true;
						} catch (NumberFormatException e) {
							if (end.length() <= 3) {
								coordinate1LabelNew = beginning;
								coordinate1AlphaNumericIndicator = end;
								coordinate1NumericIndicator = null;
								update = true;
							}
						}
					}
				}

				if (coordinate2LabelOld != null && coordinate2LabelOld.length() > 0) {
					lastSpaceLocation = coordinate2LabelOld.lastIndexOf(" ");
					if (lastSpaceLocation == -1) {
						//just check to see if this is a number
						try {
							number = Double.valueOf(coordinate2LabelOld);
							coordinate2LabelNew = "";
							coordinate2AlphaNumericIndicator = "";
							coordinate2NumericIndicator = number;
							update = true;
						} catch (NumberFormatException e) {
							//do nothing and leave things alone
						}
					} else {
						beginning = coordinate2LabelOld.substring(0, lastSpaceLocation).trim();
						end = coordinate2LabelOld.substring(lastSpaceLocation).trim();
						try {
							number = Double.valueOf(end);
							coordinate2LabelNew = beginning;
							coordinate2AlphaNumericIndicator = "";
							coordinate2NumericIndicator = number;
							update = true;
						} catch (NumberFormatException e) {
							if (end.length() <= 3) {
								coordinate2LabelNew = beginning;
								coordinate2AlphaNumericIndicator = end;
								coordinate2NumericIndicator = null;
								update = true;
							}
						}
					}
				}

				if (coordinate3LabelOld != null && coordinate3LabelOld.length() > 0) {
					lastSpaceLocation = coordinate3LabelOld.lastIndexOf(" ");
					if (lastSpaceLocation == -1) {
						//just check to see if this is a number
						try {
							number = Double.valueOf(coordinate3LabelOld);
							coordinate3LabelNew = "";
							coordinate3AlphaNumericIndicator = "";
							coordinate3NumericIndicator = number;
							update = true;
						} catch (NumberFormatException e) {
							//do nothing and leave things alone
						}
					} else {
						beginning = coordinate3LabelOld.substring(0, lastSpaceLocation).trim();
						end = coordinate3LabelOld.substring(lastSpaceLocation).trim();
						try {
							number = Double.valueOf(end);
							coordinate3LabelNew = beginning;
							coordinate3AlphaNumericIndicator = "";
							coordinate3NumericIndicator = number;
							update = true;
						} catch (NumberFormatException e) {
							if (end.length() <= 3) {
								coordinate3LabelNew = beginning;
								coordinate3AlphaNumericIndicator = end;
								coordinate3NumericIndicator = null;
								update = true;
							}
						}
					}
				}

				progressMessage = "Parsing locations: " + count++;
				progress.setProgress(progressMessage, currentStep, numberOfSteps);

				if (update) {
					pstmtUpdate.setString(1, coordinate1LabelNew);
					if (coordinate1NumericIndicator == null) {
						pstmtUpdate.setNull(2, Types.DOUBLE);
					} else {
						pstmtUpdate.setDouble(2, coordinate1NumericIndicator);
					}
					pstmtUpdate.setString(3, coordinate1AlphaNumericIndicator);

					pstmtUpdate.setString(4, coordinate2LabelNew);
					if (coordinate2NumericIndicator == null) {
						pstmtUpdate.setNull(5, Types.DOUBLE);
					} else {
						pstmtUpdate.setDouble(5, coordinate2NumericIndicator);
					}
					pstmtUpdate.setString(6, coordinate2AlphaNumericIndicator);

					pstmtUpdate.setString(7, coordinate3LabelNew);
					if (coordinate3NumericIndicator == null) {
						pstmtUpdate.setNull(8, Types.DOUBLE);
					} else {
						pstmtUpdate.setDouble(8, coordinate3NumericIndicator);
					}
					pstmtUpdate.setString(9, coordinate3AlphaNumericIndicator);					

					pstmtUpdate.setInt(10, locationId);
					pstmtUpdate.executeUpdate();
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

	protected String getWarningMessage() {
		return "This upgrade will parse existing locations coordinates to allow proper sorting. Some coordinates may need to be cleaned up by hand.";
	}

	protected int getNumberOfSteps() {
		return 15;
	}

	protected boolean upgradeNeeded(Connection conn) throws SQLException {
		return Constants.compareVersions("1.0.11", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
	}

	protected boolean runFieldInit() {
		return true;
	}

	protected boolean runLoadLookupLists() {
		return true;
	}

}
