//==============================================================================
//            _            __
//  ___  __ _| | ___  ___ / _| ___  _ __ __ _  ___
// / __|/ _` | |/ _ \/ __| |_ / _ \| '__/ _` |/ _ \
// \__ \ (_| | |  __/\__ \  _| (_) | | | (_| |  __/
// |___/\__,_|_|\___||___/_|  \___/|_|  \__, |\___|
//                                      |___/
//
// 01110011 01100001 01101100 01100101 01110011
// 01100110 01101111 01110010 01100111 01100101
//
//==============================================================================
// Copyright 2004 (c) the salesforge community. All Rights Reserved.
//==============================================================================
// Licensed under the Open Software License version 2.1
//==============================================================================
// $Author: lejames $
// $Date: 2005/01/07 23:30:23 $
// $Revision: 1.8 $
//==============================================================================

package org.archiviststoolkit.importer;

//==============================================================================
// Import Declarations
//==============================================================================

import java.io.File;
import java.util.LinkedHashMap;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.mydomain.DomainImportController;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.swing.ProgressMonitorBufferedReader;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

/**
 * Base class for all CSV derived import handlers.
 */

public abstract class TabImportHandler extends ImportHandler {

	/**
	 * Source application.
	 */
	private String sourceApplication = "unknown";
	/**
	 * the separator char for this parser.
	 */
	private char fieldSep = ',';
	/**
	 * The default separator.
	 */
	public static final char DEFAULT_SEP = ',';

	/**
	 * a list of columns.
	 */
	private ArrayList columnList = new ArrayList();

	/**
	 * should we autocomplete.
	 */
	private boolean autoComplete = false;

	/**
	 * a reference to the main file to import.
	 */
	private File mainFile = null;

	/**
	 * a reference to the domainimport controller.
	 */
	protected DomainImportController controller = null;

	/**
	 * the csv pattern to use for building the parsed lines.
	 */
	private Pattern csvPattern = Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*(?![^\"]*\"))");

	protected ArrayList fieldList;
	/**
	 * Constructor.
	 */
	public TabImportHandler() {
	}

	/**
	 * Map a column.
	 *
	 * @param dataList the datalist
	 * @return a column
	 */
	public abstract ArrayList mapColumn(ArrayList dataList);

	/**
	 * Handle a newly parsed row.
	 *
	 * @param dataList the datalist map to use
	 * @return the constructed domain object
	 */
	public abstract Object handleRow(ArrayList dataList) throws ImportException, ImportBadLookupException, UnknownLookupListException;

	/**
	 * Get the source application name.
	 *
	 * @return the name of the source application
	 */
	public String getSourceApplication() {
		return (this.sourceApplication);
	}

	/**
	 * Set the source application name.
	 *
	 * @param sourceApplication the name of the source application
	 */
	public void setSourceApplication(String sourceApplication) {
		this.sourceApplication = sourceApplication;
	}

	/**
	 * Set the list of columns.
	 *
	 * @param columnList the list of columns.
	 */
	public void setColumnList(ArrayList columnList) {
		this.columnList = columnList;
	}

	/**
	 * Get the list of columns.
	 *
	 * @return the list of columns.
	 */
	public ArrayList getColumnList() {
		return (this.columnList);
	}

	/**
	 * is autocomplete on.
	 *
	 * @return autocomplete status.
	 */
	public boolean isAutoCompleteOn() {
		return (this.autoComplete);
	}

	public void readHeaderRecord(File file) {

		// Parse the header record to determine the fields to import
		String headerRecord = getInitialChunk(file);
		fieldList = new ArrayList();
		String filedName;

		String[] result = headerRecord.split("\\t");
		for (int i=0; i<result.length; i++) {
		   fieldList.add(result[i]);
		}

	}

	/**
	 * Import the file.
	 *
	 * @param file			 the file to import.
	 * @param domainController which controller to use.
	 * @param progressPanel
	 * @return if the import succeded.
	 */
	public boolean importFile(File file, DomainImportController domainController, InfiniteProgressPanel progressPanel) throws ImportException {

		mainFile = file;
		this.controller = domainController;
        this.controller.setClearTotalRecords(false);
        
		Collection collection = new ArrayList();

		try {
			this.readHeaderRecord(file);

			progressPanel.setTextLine("Reading File " + mainFile.toString(), 2);
			ProgressMonitorBufferedReader inputReader = new ProgressMonitorBufferedReader(ApplicationFrame.getInstance(), "Importing " + getSourceApplication() + " CSV File", new FileReader(mainFile));

			String str;

			str = inputReader.readLine();

			ArrayList nameList = new ArrayList(parse(str));
			nameList = this.mapColumn(nameList);
			LinkedHashMap hashMap = new LinkedHashMap();
			hashMap.clear();

			for (int loop = 0; loop < nameList.size(); loop ++) {
				hashMap.put(nameList.get(loop), null);
			}

			//setColumnList(nameList);
			int count = 1;
			LookupListUtils.initIngestReport();
			while ((str = inputReader.readLine()) != null) {
                // check to see if the process was cancelled  at this point if so just return
                if(progressPanel.isProcessCancelled()) {
                    return false;
                }

//				System.out.println(str);
				ArrayList list = new ArrayList(parse(str));

				if (list.size() > 0) {
					progressPanel.setTextLine("Reading record " + count++, 2);
					// Ditch Partial records.

//					for (int loop = 0; loop < list.size(); loop ++) {
//						hashMap.put(nameList.get(loop), list.get(loop));
//					}
					try {
						Object record  = handleRow(list);
                        if(record != null) {
                            collection.add(record);
                        }
					} catch (ImportException e) {
						controller.incrementErrorCount();
						controller.addLineToImportLog("Error processing line " + controller.getTotalRecords() + " " + e.getMessage());
					} catch (ImportBadLookupException e) {
						controller.incrementErrorCount();
						controller.addLineToImportLog("Error processing line " + controller.getTotalRecords() + " " + e.getMessage());
					} catch (UnknownLookupListException e) {
						controller.incrementErrorCount();
						controller.addLineToImportLog("Error processing line " + controller.getTotalRecords() + " " + e.getMessage());
					}
				}
				list.clear();
				hashMap.clear();
			}
			inputReader.close();

			controller.domainImport(collection, ApplicationFrame.getInstance(), progressPanel);
			ImportExportLogDialog dialog = new ImportExportLogDialog(controller.constructFinalImportLogText() + "\n\n" + LookupListUtils.getIngestReport(),
					ImportExportLogDialog.DIALOG_TYPE_IMPORT);
			progressPanel.close();
			dialog.showDialog();

		} catch (FileNotFoundException e) {
			progressPanel.close();
			new ErrorDialog("Error importing accessions", e).showDialog();
		} catch (IOException e) {
			progressPanel.close();
			new ErrorDialog("Error importing accessions", e).showDialog();
		}

		return (true);
	}

	/**
	 * parse: break the input String into fields.
	 *
	 * @param line the line to parse
	 * @return java.util.Iterator containing each field
	 *         from the original as a String, in order.
	 */
	public ArrayList parse(String line) {
		ArrayList list = new ArrayList();
		StringBuffer sb = new StringBuffer();
		list.clear();			// discard previous, if any

		String[] result = line.split("\\t");
		for (int i=0; i<result.length; i++) {
		   list.add(result[i]);
		}

		purify(list);

		return list;
	}


	/**
	 * Clean the datalist.
	 *
	 * @param arrayList the list to be cleansed.
	 */
	public void purify(ArrayList arrayList) {
		for (int loop = 0; loop < arrayList.size(); loop++) {
			//if ((String)arrayList.get(loop).equals)
			String value = (String) arrayList.get(loop);
			if (value.startsWith("\"") && value.endsWith("\"")) {
				arrayList.set(loop, value.substring(1, value.length() - 1));
			}
		}
	}

}