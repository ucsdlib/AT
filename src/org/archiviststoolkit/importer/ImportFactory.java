//==============================================================================
//            _            __
//  ___  __ _| | ___  ___ / _| ___  _ __ __ _  ___
// / __|/ _` | |/ _ \/ __| |_ / _ \| '__/ _` |/ _ \
// \__ \ (_| | |  __/\__ \  _| (_) | | | (_| |  __/
// |___/\__,_|_|\___||___/_|  \___/|_|  \__, |\___|
//                                      |___/
//
//  01110011 01100001 01101100 01100101 01110011
//  01100110 01101111 01110010 01100111 01100101
//
//==============================================================================
// Copyright 2004 (c) Salesforge team. All Rights Reserved.
//==============================================================================
// Licensed under the Open Software License version 2.1
//==============================================================================
// $Author: lejames $
// $Date: 2005/01/07 23:30:23 $
// $Revision: 1.5 $
//==============================================================================

//==============================================================================
// Package Declaration
//==============================================================================

package org.archiviststoolkit.importer;

//==============================================================================
// Import Declarations
//==============================================================================

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;

import org.archiviststoolkit.mydomain.DomainImportController;
import org.archiviststoolkit.importer.MARCXML.MARCIngest;

/**
 * A singleton in which we store the import parsers.
 */

public final class ImportFactory {

	/**
	 * resource bundle used for i18n of text messages.
	 */

	private ResourceBundle resourceBundle = ResourceBundle.getBundle("org.archiviststoolkit.resources.messages.messages", Locale.getDefault(), ImportFactory.class.getClassLoader());

	/**
	 * The singleton reference.
	 */

	private static ImportFactory singleton = null;

	/**
	 * The cache where we store the report actions.
	 */

	private ArrayList importHandlerCache = null;

    // This stores an alternative EAD ingest which plugins can use to replace
    // the built in ead ingest engine
    private EADIngest2 eadIngest = null;

    // This stores an alternative MARCXML ingest which plugins can use to replace
    // the built in marcxml ingest engine
    private MARCIngest marcIngest = null;

	/**
	 * Default Constructor.
	 */

	private ImportFactory() {
		importHandlerCache = new ArrayList();

		importHandlerCache.add(new XmlEacNameImportHandler("names"));
		importHandlerCache.add(new XmlOliviaNameImportHandler("names"));

	}

	/**
	 * Singleton access method.
	 *
	 * @return the instance of this singleton
	 */

	public static ImportFactory getInstance() {
		if (singleton == null) {
			singleton = new ImportFactory();
		}
		return (singleton);
	}

	/**
	 * Import this file.
	 *
	 * @param file	   the file.
	 * @param controller the controller.
	 * @return if it succeeded or not.
	 */
	public boolean importFile(File file, DomainImportController controller) {
		// Check and see if

		Iterator iterator = importHandlerCache.iterator();

		while (iterator.hasNext()) {
			ImportHandler handler = (ImportHandler) iterator.next();

			if (handler.canImportFile(file)) {

				ProgressMonitor progressMonitor = new ProgressMonitor(null, "importing file [" + file.toString() + "]", "import", 0, 100);
				progressMonitor.setMillisToPopup(1000);

				handler.startImportThread(file, controller);

				return (true);
			}
		}

		try {
			JOptionPane.showMessageDialog(null, "File [" + file.getCanonicalFile().toString() + "] is of a type not supported for import", "Import Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException ioException) {
			System.out.println("Opps " + ioException);
		}

		return (false);
	}

    /**
     * get the EADingest2 engine
     *
     * @return User supplied EADIngest engine
     */
    public EADIngest2 getEadIngest() {
        return eadIngest;
    }

    /**
     * Set the EAD ingest engine
     * @param eadIngest
     */
    public void setEadIngest(EADIngest2 eadIngest) {
        this.eadIngest = eadIngest;
    }

    /**
     * Get the Marc XML ingest Engine
     *
     * @return The MARCXML Ingest engine
     */
    public MARCIngest getMarcIngest() {
        return marcIngest;
    }

    /**
     * Set the MARCXML Ingest engine
     *
     * @param marcIngest
     */
    public void setMarcIngest(MARCIngest marcIngest) {
        this.marcIngest = marcIngest;
    }
}

//=============================================================================
// $Log: ImportFactory.java,v $
// Revision 1.5  2005/01/07 23:30:23  lejames
// Clean up on Javadoc and fixed reporting bug
//
// Revision 1.4  2005/01/04 02:45:19  lejames
// Added new report mechanism, based on table models
//
// Revision 1.3  2004/12/20 22:32:25  lejames
// Added NAICs level 2 support
//
// Revision 1.2  2004/12/20 00:24:29  lejames
// Added the DomainImportController and used it to manage the import of external data.
// We can now do duplicate, merge and update import operations.
//
// Revision 1.1  2004/12/18 03:22:41  lejames
// First cut of the new import code which can handle comma separated values.
//
//
//=============================================================================
// EOF
//=============================================================================
