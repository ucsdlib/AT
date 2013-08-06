/**
 * Archivists' Toolkit(TM) Copyright � 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
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
 * Date: Jan 23, 2007
 * Time: 2:32:02 PM
 */

package org.archiviststoolkit.maintenance.upgrades;

import org.netbeans.spi.wizard.DeferredWizardResult;
import org.netbeans.spi.wizard.Summary;
import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.util.LookupListUtils;

import javax.swing.*;
import java.util.ResourceBundle;
import java.util.Map;
import java.util.ArrayList;
import java.awt.*;
import java.io.File;
import java.sql.*;

public class UpgradeDatabaseBackgroundResultCreator extends DeferredWizardResult {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("org.archiviststoolkit.resources.messages.messages");
    private static String versionString = resourceBundle.getString("archiviststoolkit.releasenumber");
//    private static Repositories repository;
    private static boolean abortable = true;
    private int totalNumberOfSteps = 0;

	private boolean structuralChanges = false;
	private String warningMessage = "";

	private boolean runFieldInit = false;
	private boolean runLoadLookupLists = false;
	private boolean runDetermineSequenceNumbers = false;

	public UpgradeDatabaseBackgroundResultCreator() {
        super(UpgradeDatabaseBackgroundResultCreator.abortable);
    }

    private volatile boolean aborted;

    public void abort() {
        aborted = true;
    }

    public void start(Map wizardData, ResultProgressHandle progress) {
        assert !EventQueue.isDispatchThread();

        try {
            Class.forName(SessionFactory.getDriverClass());
            SessionFactory.setUpdateStructure(true);
            Connection conn = DriverManager.getConnection(SessionFactory.getDatabaseUrl(),
                    SessionFactory.getUserName(),
                    SessionFactory.getPassword());

            ArrayList<Upgrade> upgradeSteps = determineStepsNeeded(conn);
            int initialStep = 0;
			System.out.println("Transaction Isolation level: " + conn.getTransactionIsolation());
			conn.setAutoCommit(false);
			boolean doUpgrade = true;
			if (structuralChanges) {
				UpgradeWarning warningDialog = new UpgradeWarning(warningMessage);
				if (warningDialog.showDialog() == JOptionPane.CANCEL_OPTION) {
					doUpgrade = false;
				}
			}

			if (doUpgrade) {
				for (Upgrade step: upgradeSteps) {
					if (step.doUpgrade(conn, initialStep, totalNumberOfSteps, progress)) {
						initialStep += step.getNumberOfSteps();
						conn.commit();
					} else {
						progress.failed(step.getErrorString(), false);
						conn.rollback();
						return;
					}
				}

				conn.close();
				if (runFieldInit) {
					progress.setProgress("Updating field information", initialStep++, totalNumberOfSteps);
					ATFieldInfo.loadFieldList();
					ATFieldInfo.initDatabaseTables();
					ATFieldInfo.updateFieldInfoForAllTables();
					ATFieldInfo.fixBlankFieldLabels(progress, initialStep, initialStep);
					boolean preserveExistingLabels = (Boolean)wizardData.get("preserveExistingLabels");
					ATFieldInfo.importFieldDefinitionsAndExamples(new File("conf/fieldDefinitionAndExamples.xml"), progress, initialStep, totalNumberOfSteps, preserveExistingLabels);
				}
				if (runLoadLookupLists) {
					progress.setProgress("Updating lookup lists", initialStep++, totalNumberOfSteps);
					LookupListUtils.loadLookupLists();
					LookupListUtils.importLookupLists(new File("conf/lookupListValues.xml"));
				}

				conn = DriverManager.getConnection(SessionFactory.getDatabaseUrl(),
						SessionFactory.getUserName(),
						SessionFactory.getPassword());
				if (runDetermineSequenceNumbers) {
					determineSequenceNumbers(conn);
				}

                //get a new db connection for runing sql commands after hibernate has finished
                conn = DriverManager.getConnection(SessionFactory.getDatabaseUrl(),
                        SessionFactory.getUserName(),
                        SessionFactory.getPassword());
                conn.setAutoCommit(false);

                for (Upgrade step : upgradeSteps) {
                    progress.setProgress("Running Final Initialization Code", initialStep, totalNumberOfSteps);
                    step.setResultProgressHandle(progress, initialStep, totalNumberOfSteps);

                    if (step.runPostDBInitializationCode()) {
                        //initialStep += step.getNumberOfSteps(); don't increment cause this leads to an error
                    } else {
                        progress.failed(step.getErrorString(), false);
                        return;
                    }

                    // run any sql code that need to run after hibernate has been initialized
                    if (step.runPostDBInitializationSQLCode(conn)) {
                        progress.setProgress("Finnished Initialization Code", initialStep, totalNumberOfSteps);
                        conn.commit();
                    } else {
                        progress.failed(step.getErrorString(), false);
                        conn.rollback();
                        return;
                    }
                }
                conn.close();

                //update the constant record now
                progress.setProgress("Updating constants record", totalNumberOfSteps, totalNumberOfSteps);
				Constants.updateOrCreateVersionRecord(versionString);

                String successString = "The database has been upgraded to " + versionString +
						". It will no longer work with earlier versions";
				progress.finished(Summary.create(successString, wizardData));
			}
		} catch (ClassNotFoundException e) {
            progress.failed(e.getMessage(), false);
        } catch (SQLException e) {
            progress.failed(e.getMessage(), false);
        }
    }

    private ArrayList<Upgrade> determineStepsNeeded(Connection conn) throws SQLException {
        ArrayList<Upgrade> allUpgrades = new ArrayList<Upgrade>();
        allUpgrades.add(new UpgradeTo_1_0_01());
		allUpgrades.add(new UpgradeTo_1_0_03());
		allUpgrades.add(new UpgradeTo_1_0_04());
		allUpgrades.add(new UpgradeTo_1_0_05());
		allUpgrades.add(new UpgradeTo_1_0_08());
		allUpgrades.add(new UpgradeTo_1_0_09());
		allUpgrades.add(new UpgradeTo_1_0_10());
		allUpgrades.add(new UpgradeTo_1_0_11());
		allUpgrades.add(new UpgradeTo_1_0_12());
		allUpgrades.add(new UpgradeTo_1_0_15());
		allUpgrades.add(new UpgradeTo_1_0_16());
		allUpgrades.add(new UpgradeTo_1_0_20());
		allUpgrades.add(new UpgradeTo_1_0_21());
		allUpgrades.add(new UpgradeTo_1_0_22());
		allUpgrades.add(new UpgradeTo_1_0_24());
		allUpgrades.add(new UpgradeTo_1_0_28());
		allUpgrades.add(new UpgradeTo_1_0_29());
        allUpgrades.add(new UpgradeTo_1_0_30());
		allUpgrades.add(new UpgradeTo_1_0_36());
		allUpgrades.add(new UpgradeTo_1_0_40());
		allUpgrades.add(new UpgradeTo_1_1_09());
		allUpgrades.add(new UpgradeTo_1_1_10());
		allUpgrades.add(new UpgradeTo_1_1_11());
		allUpgrades.add(new UpgradeTo_1_1_20());
		allUpgrades.add(new UpgradeTo_1_1_21());
		allUpgrades.add(new UpgradeTo_1_1_24());
        allUpgrades.add(new UpgradeTo_1_5_2());
        allUpgrades.add(new UpgradeTo_1_6_0());
        allUpgrades.add(new UpgradeTo_1_7_0());

        ArrayList<Upgrade> returnSteps = new ArrayList<Upgrade>();
        for(Upgrade step: allUpgrades) {
            if (step.upgradeNeeded(conn)) {
                returnSteps.add(step);
				if (step.getWarningMessage() != null) {
					if (warningMessage.length() == 0) {
						warningMessage = step.getWarningMessage();
					} else {
						warningMessage += "\n\n" + step.getWarningMessage();
					}
				}
				totalNumberOfSteps += step.getNumberOfSteps();
				if (step.runFieldInit()) {
					runFieldInit = true;
					totalNumberOfSteps++;
				}
				if (step.runLoadLookupLists()) {
					runLoadLookupLists = true;
					totalNumberOfSteps++;
				}
				if (step.runDetermineSequenceNumbers()) {
					runDetermineSequenceNumbers = true;
					totalNumberOfSteps++;
				}
            }
        }
		if (returnSteps.size() > 0) {
			structuralChanges = true;
		}
		//add the update version step
        totalNumberOfSteps++;
        return returnSteps;
    }

	private void determineSequenceNumbers(Connection conn) throws SQLException {

		PreparedStatement insertSequence;
		Statement stmt = conn.createStatement();
        Statement deleteStatement = conn.createStatement();

        String sqlString = "SELECT MAX(archDescriptionRepeatingDataId) FROM ArchDescriptionRepeatingData";
		ResultSet rs = stmt.executeQuery(sqlString);
		if (rs.next()) {
            sqlString = "delete from repeating_data_sequence";
            deleteStatement.execute(sqlString);
            insertSequence = conn.prepareStatement("insert into repeating_data_sequence values (?)");
			insertSequence.setLong(1, rs.getLong(1));
			insertSequence.execute();
		}

		sqlString = "SELECT MAX(archDescriptionInstancesId) FROM ArchDescriptionInstances";
		rs = stmt.executeQuery(sqlString);
		if (rs.next()) {
            sqlString = "delete from instance_sequence";
            deleteStatement.execute(sqlString);
            insertSequence = conn.prepareStatement("insert into instance_sequence values (?)");
			insertSequence.setLong(1, rs.getLong(1));
			insertSequence.execute();
		}

        sqlString = "delete from structured_data_sequence";
        deleteStatement.execute(sqlString);
        insertSequence = conn.prepareStatement("insert into structured_data_sequence values (?)");
		insertSequence.setLong(1, 10000);
		insertSequence.execute();

		sqlString = "SELECT MAX(nameId) FROM Names";
		rs = stmt.executeQuery(sqlString);
		if (rs.next()) {
            sqlString = "delete from name_sequence";
            deleteStatement.execute(sqlString);
            insertSequence = conn.prepareStatement("insert into name_sequence values (?)");
			insertSequence.setLong(1, rs.getLong(1));
			insertSequence.execute();
		}

	}

}
