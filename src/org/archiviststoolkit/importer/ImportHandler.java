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
// $Revision: 1.4 $
//==============================================================================

package org.archiviststoolkit.importer;

//==============================================================================
// Import Declarations
//==============================================================================

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.archiviststoolkit.mydomain.DomainImportController;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.swing.ATProgressUtil;
import org.archiviststoolkit.ApplicationFrame;

/**
 * Base class for all import handlers.
 */
public abstract class ImportHandler implements Runnable{

	/** the file to import. */
	public File file = null;

	/** the domain controller to use. */
	public DomainImportController importController = null;

	/**
	 * Get the file to use.
	 * @return the file.
	 */
	public File getFile () {
		return (this.file);
	}

	/**
	 * Get the domaincontroller to use.
	 * @return the domain controller.
	 */
	public DomainImportController getImportController () {
		return (this.importController);
	}

	/**
	 * Can we import this file.
	 * @param importFile the file to import.
	 * @return can we.
	 */
	public abstract boolean canImportFile (File importFile);

	/**
	 * Import the file.
	 * @param importFile the file to import.
	 * @param controller the controller to use.
	 * @param progressPanel
	 * @return if we succeded
	 */
	public abstract boolean importFile(File importFile, DomainImportController controller, InfiniteProgressPanel progressPanel) throws ImportException;
	/**
	 * Kick off the import thread.
	 * @param importFile the file to import
	 * @param controller the controller to use.
	 * @return if we succeeded or not
	 */
	public boolean startImportThread (File importFile, DomainImportController controller) {

		this.file = importFile;
		this.importController = controller;

		new Thread(this).start();

		return (true);
	}

	/**
	 * The import thread run method.
	 */
	public void run () {
		InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000);
		try {
			importFile (getFile(),getImportController(), monitor);
		} catch (ImportException e) {
			monitor.close();
			ErrorDialog dialog = new ErrorDialog(e.getMessage(),e.getErrorText());
			dialog.showDialog();
			return;
		} finally {
			monitor.close();
		}

		ImportExportLogDialog dialog = new ImportExportLogDialog(getImportController().constructFinalImportLogText(), ImportExportLogDialog.DIALOG_TYPE_IMPORT);
		dialog.showDialog();
	}

	/**
	 * Get the initial file chunk (32 bytes).
	 * @param importFile the file to import
	 * @return the initial string
	 */
	public String getInitialChunk (File importFile) {

		String str = null;

		try {
			BufferedReader in = new BufferedReader(new FileReader(importFile));

			str = in.readLine();

			in.close();
		} catch (IOException e) {
			System.out.println ("Failed to get chunk "+e);
		}

		return (str);
	}
}