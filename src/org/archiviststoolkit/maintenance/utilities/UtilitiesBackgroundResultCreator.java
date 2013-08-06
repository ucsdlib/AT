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
 * Date: May 11, 2007
 * Time: 11:55:25 AM
 */

package org.archiviststoolkit.maintenance.utilities;

import org.netbeans.spi.wizard.DeferredWizardResult;
import org.netbeans.spi.wizard.Summary;
import org.archiviststoolkit.model.Repositories;
import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.model.Users;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.NotesEtcTypes;
import org.archiviststoolkit.structure.InLineTags;
import org.archiviststoolkit.mydomain.RepositoriesDAO;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.mydomain.DomainAccessObject;
import org.archiviststoolkit.mydomain.DomainAccessObjectFactory;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.UserPreferences;
import org.archiviststoolkit.util.NoteEtcTypesUtils;
import org.archiviststoolkit.util.InLineTagsUtils;
import org.archiviststoolkit.exceptions.UnsupportedDatabaseType;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.maintenance.initDatabase.InitDatabaseBackgroundResultCreator;
import org.archiviststoolkit.importer.ImportUtils;
import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Element;
import org.xml.sax.InputSource;

import java.util.ResourceBundle;
import java.util.Map;
import java.util.ArrayList;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.security.NoSuchAlgorithmException;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JRException;

public class UtilitiesBackgroundResultCreator extends DeferredWizardResult {

	private static boolean abortable = true;
	private static String databaseType;
	private static String utility;

	public UtilitiesBackgroundResultCreator() {
		super(abortable);
	}

	public void start(Map wizardData, ResultProgressHandle progress) {
		assert !EventQueue.isDispatchThread();

		try {
			SessionFactory.resetFactory();
			databaseType = (String)wizardData.get("connectionDatabaseType");
			SessionFactory.setDatabaseType(databaseType);
			SessionFactory.setDatabaseUrl((String) wizardData.get("connectionURL"));
			SessionFactory.setUserName((String) wizardData.get("connectionUsername"));
			SessionFactory.setPassword((String) wizardData.get("connectionPassword"));
            SessionFactory.setUpdateStructure(false);

			utility = (String)wizardData.get("selectedUtility");

			if (utility.equals(SelectUtility.IMPORT_NOTESETC_TYPES)) {
				NoteEtcTypesUtils.importRecords(progress);
				UserPreferences.getInstance().saveToPreferences();

			} else if (utility.equals(SelectUtility.IMPORT_LOOKUP_LISTS)) {
				File file = ImportUtils.chooseFile(null);
				if (file != null) {
					progress.setProgress("Connecting to database", 0, 0);
					LookupListUtils.loadLookupLists();
					LookupListUtils.importLookupLists(progress, file);
					UserPreferences.getInstance().saveToPreferences();
				}

			} else if (utility.equals(SelectUtility.RESTORE_TABLE_AND_FIELD_DEFAULTS)) {
				progress.setProgress("Connecting to database", 0, 0);
				ATFieldInfo.loadFieldList();
				ATFieldInfo.restoreDefaults(progress);

			} else if (utility.equals(SelectUtility.IMPORT_INLINE_TAGS)) {
				File file = ImportUtils.chooseFile(null);
				if (file != null) {
					progress.setProgress("Connecting to database", 0, 0);
					InLineTagsUtils.loadInLineTags();
					InLineTagsUtils.importRecords(progress, file);
					UserPreferences.getInstance().saveToPreferences();
				}

			} else if (utility.equals(SelectUtility.ADD_NOTE_DEFAULTS_TO_REPOSITORIES)) {
				progress.setProgress("Connecting to database", 0, 0);
				NoteEtcTypesUtils.loadNotesEtcTypes();
				new RepositoriesDAO().addNotesDefautsRecords();

			} else if (utility.equals(SelectUtility.IMPORT_FIELD_EXAMPLES_AND_DEFINITIONS)) {
				File file = ImportUtils.chooseFile(null);
				if (file != null) {
					progress.setProgress("Connecting to database", 0, 0);
					ATFieldInfo.loadFieldList();
					ATFieldInfo.importFieldDefinitionsAndExamples(file, progress, 0, 0, false);
					UserPreferences.getInstance().saveToPreferences();
				}

			} else if (utility.equals(SelectUtility.RECONCILE_FIELD_DEFS_AND_EXAMPLES)) {
				File file = ImportUtils.chooseFile(null);
				if (file != null) {
					progress.setProgress("Connecting to database", 0, 0);
					ATFieldInfo.loadFieldList();
					ATFieldInfo.reconcileFieldDefinitionsAndExamples(file, progress, 0, 0);
					UserPreferences.getInstance().saveToPreferences();
				}

			} else if (utility.equals(SelectUtility.UPDATE_DATABASE_SCHEMA)) {
				progress.setProgress("Connecting to database", 0, 0);
				SessionFactory.setUpdateStructure(true);
				SessionFactory.testHibernate();

			} else if (utility.equals(SelectUtility.COMPILE_JASPER_REPORT)) {
				progress.setProgress("Compiling a Jasper Report", 0, 0);
				File reportFile = ImportUtils.chooseFile(null);
				JasperCompileManager.compileReportToFile(reportFile.getPath());

			} else if (utility.equals(SelectUtility.FIX_BLANK_FIELD_LABELS)) {
				progress.setProgress("Fixing blank field names", 0, 0);
				ATFieldInfo.fixBlankFieldLabels(progress, 0, 0);
			}

			progress.finished(Summary.create("Success", wizardData));
		} catch (UnsupportedDatabaseType e) {
			progress.failed(e.getMessage(), false);
		} catch (SQLException e) {
			progress.failed(e.getMessage(), false);
		} catch (JRException e) {
			progress.failed(e.getMessage(), false);
		}
	}
}
