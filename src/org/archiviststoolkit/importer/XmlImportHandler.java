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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.mydomain.DomainImportController;

/**
 * Base class for all CSV derived import handlers.
 */

public abstract class XmlImportHandler extends ImportHandler {

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
	private DomainImportController controller = null;

	protected String recordTag;

	/**
	 * the csv pattern to use for building the parsed lines.
	 */
	private Pattern csvPattern = Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*(?![^\"]*\"))");

	/**
	 * Constructor.
	 */
	public XmlImportHandler() {
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
	 * @return the constructed domain object
	 */
	public abstract Object handleInstance(String xmlInstanceString) throws UnknownLookupListException;

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

	/**
	 * Kick off the import thread.
	 * @param importFile the file to import
	 * @param controller the controller to use.
	 * @return if we succeeded or not
	 */
	public boolean startImportThread (File importFile, DomainImportController controller, String recordTag) {

		this.file = importFile;
		this.importController = controller;
		this.recordTag = recordTag;

		new Thread(this).start();

		return (true);
	}

	public boolean importFile(File file, DomainImportController domainController, InfiniteProgressPanel progressPanel) {
		return importFile(file, domainController, "", progressPanel);
	}

	/**
	 * Import the file.
	 *
	 * @param file			 the file to import.
	 * @param domainController which controller to use.
	 * @return if the import succeded.
	 */
	public boolean importFile(File file, DomainImportController domainController, String recordTag, InfiniteProgressPanel progressPanel) {

		mainFile = file;
		this.recordTag = recordTag;
		this.controller = domainController;

		Collection collection = new ArrayList();

		try {
			HandleInputFile fileHandler = new HandleInputFile();
			fileHandler.initializeInput(file.getAbsolutePath());

			byte [] recordBytes;
			String recordString;

			while ((recordBytes = fileHandler.loadRecord(recordTag)) != null) {
				recordString = new String(recordBytes);
                try {
                    collection.add(handleInstance(recordString));
                } catch (UnknownLookupListException e) {
                    controller.incrementErrorCount();
                    controller.addLineToImportLog("Error processing line " + controller.getTotalRecords() + " " + e.getMessage());
                }
            }

			controller.domainImport(collection, ApplicationFrame.getInstance(), progressPanel);
//
		} catch (IOException e) {
			System.out.println("Exception " + e);
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
	public List parse(String line) {
		List list = new ArrayList();
		StringBuffer sb = new StringBuffer();
		list.clear();			// discard previous, if any
		int i = 0;

		if (line.length() == 0) {
			list.add(line);
			return list;
		}

		StringTokenizer st = new StringTokenizer(line, "\t");
		while (st.hasMoreTokens()) {
			list.add(st.nextToken());
		}

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

//=============================================================================
// $Log: CsvImportHandler.java,v $
// Revision 1.8  2005/01/07 23:30:23  lejames
// Clean up on Javadoc and fixed reporting bug
//
// Revision 1.7  2005/01/04 02:45:19  lejames
// Added new report mechanism, based on table models
//
// Revision 1.6  2004/12/28 02:54:41  lejames
// outlook express is now fully mapped within the new import scheme.
//
// Revision 1.5  2004/12/23 23:36:33  lejames
// Reworked the model to contain a full list of fields
//
// Revision 1.4  2004/12/20 22:32:25  lejames
// Added NAICs level 2 support
//
// Revision 1.3  2004/12/20 00:24:29  lejames
// Added the DomainImportController and used it to manage the import of external data.
// We can now do duplicate, merge and update import operations.
//
// Revision 1.2  2004/12/19 02:47:41  lejames
// Added Sortable Tables, and group delete to all the domain worksurfaces.
//
// Revision 1.1  2004/12/18 03:22:41  lejames
// First cut of the new import code which can handle comma separated values.
//
//
//=============================================================================
// EOF
//=============================================================================
