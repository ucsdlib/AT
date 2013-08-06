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
 * @author Nathan Stevens
 * Date: Jul 18, 2008
 * Time: 9:09:19 PM
 */

import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.util.DatabaseConnectionUtils;
import org.archiviststoolkit.util.NameUtils;
import org.archiviststoolkit.util.UpgradeUtils;
import org.netbeans.spi.wizard.DeferredWizardResult;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class UpgradeTo_1_1_09 extends Upgrade {
	protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {
		try {
			int currentStep = initialStep;

			// Generate and add the md5hash to the BasicNames Table. Use a preapred statement here
			currentStep++;
			String progressMessage = "Modifying Names table";
			progress.setProgress(progressMessage, currentStep, numberOfSteps);

			String sqlString = "";
			Statement stmt = conn.createStatement();

			// Set the columNames which we will use to perform modifcations
			String[] columnNames = {"nameNumber", "corporatePrimaryName", "corporateSubordinate1",
					"corporateSubordinate2", "personalPrimaryName", "personalRestOfName",
					"personalPrefix", "personalSuffix", "personalDates", "personalFullerForm",
					"personalTitle", "familyName", "familyNamePrefix"};

			// Add the md5hash column to names table
			sqlString = "ALTER TABLE Names ADD md5Hash VARCHAR(255)";
			stmt.execute(sqlString);

			// execute statement for removing unique indexes from database
			if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_MYSQL)) {
				sqlString = "ALTER TABLE Names DROP INDEX UniqueNames";
				stmt.execute(sqlString);

				for (String columnName : columnNames) {
					sqlString = "ALTER TABLE Names MODIFY COLUMN " + columnName + " VARCHAR(255)";
					stmt.execute(sqlString);
				}
			} else if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_ORACLE)) {
				//sqlString = "ALTER TABLE Names DROP CONSTRAINT UniqueNames";
				sqlString = "DROP INDEX UniqueNames";
				stmt.execute(sqlString);

				for (String columnName : columnNames) {
					sqlString = "ALTER TABLE Names MODIFY (" + columnName + " VARCHAR2(255))";
					stmt.execute(sqlString);
				}
			} else if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_MICROSOFT_SQL_SERVER)) {
				sqlString = "ALTER TABLE Names DROP CONSTRAINT UniqueNames";
				stmt.execute(sqlString);

				for (String columnName : columnNames) {
					sqlString = "ALTER TABLE Names ALTER COLUMN " + columnName + " VARCHAR(255)";
					stmt.execute(sqlString);
				}
			}

			// now get all the name object and create the md5 hash for all of them
			sqlString = "SELECT nameId, nameType, sortName, nameNumber, qualifier, corporatePrimaryName, " +
					"corporateSubordinate1, corporateSubordinate2, personalPrimaryName, " +
					"personalRestOfName, personalPrefix, personalSuffix, personalDates, " +
					"personalFullerForm, personalTitle, familyName, familyNamePrefix " +
					"FROM Names";

			ResultSet rs = stmt.executeQuery(sqlString);

			PreparedStatement pstmtNames = conn.prepareStatement("UPDATE Names " +
					"SET md5Hash = ? WHERE Names.nameId = ?");

			while (rs.next()) {
				Long nameId = rs.getLong("nameId");
				String nameType = rs.getString("nameType");
				String sortName = rs.getString("sortName");
				String nameNumber = rs.getString("nameNumber");
				String qualifier = rs.getString("qualifier");
				String corporatePrimaryName = rs.getString("corporatePrimaryName");
				String corporateSubordinate1 = rs.getString("corporateSubordinate1");
				String corporateSubordinate2 = rs.getString("corporateSubordinate2");
				String personalPrimaryName = rs.getString("personalPrimaryName");
				String personalRestOfName = rs.getString("personalRestOfName");
				String personalPrefix = rs.getString("personalPrefix");
				String personalSuffix = rs.getString("personalSuffix");
				String personalDates = rs.getString("personalDates");
				String personalFullerForm = rs.getString("personalFullerForm");
				String personalTitle = rs.getString("personalTitle");
				String familyName = rs.getString("familyName");
				String familyNamePrefix = rs.getString("familyNamePrefix");

				// create a name object then use it to get the md5 hash
				Names name = new Names();
				name.setNameType(nameType);
				name.setSortName(sortName);
				name.setNumber(nameNumber);
				name.setQualifier(qualifier);
				name.setCorporatePrimaryName(corporatePrimaryName);
				name.setCorporateSubordinate1(corporateSubordinate1);
				name.setCorporateSubordinate2(corporateSubordinate2);
				name.setPersonalPrimaryName(personalPrimaryName);
				name.setPersonalRestOfName(personalRestOfName);
				name.setPersonalPrefix(personalPrefix);
				name.setPersonalSuffix(personalSuffix);
				name.setPersonalDates(personalDates);
				name.setPersonalFullerForm(personalFullerForm);
				name.setPersonalTitle(personalTitle);
				name.setFamilyName(familyName);
				name.setFamilyNamePrefix(familyNamePrefix);

				String md5Hash = NameUtils.setMd5Hash(name);

				// set the md5 hash in the table now
				pstmtNames.setString(1, md5Hash);
				pstmtNames.setLong(2, nameId);
				pstmtNames.executeUpdate();
			}

			// Add the unique constraint for the md5hash column here
			if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_MYSQL)) {
				sqlString = "ALTER TABLE Names ADD UNIQUE UniqueNames (md5Hash)";
				stmt.execute(sqlString);
			} else if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_ORACLE)) {
				sqlString = "create unique index UniqueNames on Names (md5Hash)";
				stmt.execute(sqlString);
			} else if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_MICROSOFT_SQL_SERVER)) {
				sqlString = "ALTER TABLE Names ADD CONSTRAINT UniqueNames UNIQUE (md5Hash)";
				stmt.execute(sqlString);
			}

			/* Update the databasefields table returnScreenOrder to 1 where fieldName = resource
							  and tableid = 3 AND fieldName=listItem AND tableid=30 */
			currentStep++;
			progressMessage = "Modifying DatabaseFields table";
			progress.setProgress(progressMessage, currentStep, numberOfSteps);

			// get the tableIds
			int tableId1 = UpgradeUtils.getTableId(stmt, "AccessionsResources");
			int tableId2 = UpgradeUtils.getTableId(stmt, "LookupListItems");

			sqlString = "UPDATE DatabaseFields " +
					"SET returnScreenOrder = 1 " +
					"WHERE (DatabaseFields.fieldName = 'resource' AND DatabaseFields.tableId = '" + tableId1 + "') " +
					"OR (DatabaseFields.fieldName = 'listItem' AND DatabaseFields.tableId = '" + tableId2 + "')";
			stmt.execute(sqlString);

			stmt.close();
		} catch (SQLException e) {
			setErrorString(e.getMessage());
			return false;
		} catch (NoSuchAlgorithmException e) {
			setErrorString(e.getMessage());
			return false;
		} catch (UnsupportedEncodingException e) {
			setErrorString(e.getMessage());
			return false;
		}

		return true;
		// everything is ok
	}

	protected int getNumberOfSteps() {
		return 2;
	}

	protected boolean upgradeNeeded(Connection conn) throws SQLException {
		return Constants.compareVersions("1.1.9", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
	}

	protected boolean runFieldInit() {
		return true;
	}
}
