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
 */

package org.archiviststoolkit.mydomain;

//==============================================================================
// Import Declarations
//==============================================================================

import org.hibernate.exception.ConstraintViolationException;
import org.archiviststoolkit.model.validators.ATValidator;
import org.archiviststoolkit.model.validators.ValidatorFactory;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.Main;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.Component;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.jgoodies.validation.ValidationResult;

/**
 * Controls import behaviour into the model.
 */

public class DomainImportController {

	private static Logger logger = Logger.getLogger(Main.class.getPackage().getName());
//	/**
//	 * Duplicate model entries.
//	 */
//	public static final int MODE_DUPLICATE = 0;
	/**
	 * Merge the models.
	 */
	public static final int MODE_MERGE = 1;
//	/**
//	 * Overwrite the existing model.
//	 */
//	public static final int MODE_OVERWRITE = 2; // Overwrite

	/**
	 * total records processed.
	 */
	private int totalRecords = 0;
	/**
	 * total records inserted.
	 */
	private int insertedRecords = 0;
	/**
	 * total records updated.
	 */
	private int updatedRecords = 0;
	private int errors = 0;

	private String importLog = "";

    // boolean tp specify whether to clear the total record count or not
    private boolean clearTotalRecords = true;

//	/**
//	 * the mode of this import controller.
//	 */
//	private int importMode = MODE_DUPLICATE;

	/**
	 * Constructor.
	 */
	public DomainImportController() {
	}

	/**
	 * Get the total number of records processed.
	 *
	 * @return the total number of records.
	 */
	public int getTotalRecords() {
		return (totalRecords);
	}

	/**
	 * Get the total records inserted.
	 *
	 * @return the total inserted record count.
	 */
	public int getInsertedRecords() {
		return (insertedRecords);
	}

	/**
	 * Get the total records updated.
	 *
	 * @return the number of updated records.
	 */
	public int getUpdatedRecords() {
		return (updatedRecords);
	}

	public void incrementErrorCount() {
		errors++;
	}

	public void incrementTotalRecordCount() {
		totalRecords++;
	}

    public void incrementInsertedRecordCount() {
		insertedRecords++;
	}

	public String getImportLog() {
		return importLog;
	}

	public void addLineToImportLog(String line) {
		if (importLog.length() == 0) {
			importLog = line;
		} else {
			importLog += "\n\n" + line;
		}
	}

	public String constructFinalImportLogText() {
		String temp = "Total records processed: " + this.getTotalRecords() +
				"\nSuccessful records: " + this.getInsertedRecords() +
				"\nError records: " + this.getErrors() + "\n\n";
		return temp + importLog;
	}

//	/**
//	 * Import this collection into the domain.
//	 *
//	 * @param collection the collection.
//	 */
//	public void domainImport(Collection collection, Component parent) {
//
//		insertedRecords = 0;
//		updatedRecords = 0;
//
//		switch (importMode) {
//			case MODE_DUPLICATE:
//				domainImportDuplicate(collection, parent);
//				break;
//			case MODE_MERGE:
//				domainImportMerge(collection, parent);
//				break;
//			case MODE_OVERWRITE:
//				domainImportOverwrite(collection, parent);
//				break;
//			default:
//		}
//	}

	/**
	 * Import and duplicate.
	 *
	 * @param collection the collection to inject into the db
	 */

	public void domainImport(Collection<DomainObject> collection, Component parent, InfiniteProgressPanel progressPanel) {
		DomainAccessObject access = null;

        // see wheter to clear the total records
        if(clearTotalRecords) {
		    totalRecords = 0;
        }

        ATValidator validator;
		ValidationResult validationResult;
		boolean recordValid;

		for (DomainObject instance : collection) {
            // check to see if this operation was cancelled if so return
            if (progressPanel.isProcessCancelled()) {
                this.addLineToImportLog("Import of record(s) cancelled by user");
                return;
            }

            try {
				if(clearTotalRecords) { // since total records was clear then we need to increment
                    totalRecords++;
                }
				recordValid = true;
				progressPanel.setTextLine("Saving record " + totalRecords, 2);
				access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(instance.getClass());
				validator = ValidatorFactory.getInstance().getValidator(instance);
				if (validator != null) {
					validationResult = validator.validate();
					if (validationResult.hasErrors()) {
						errors = getErrors() + 1;
						this.addLineToImportLog("Invalid Record number " + totalRecords + "\n" + validationResult.getMessagesText());
						recordValid = false;
					}
				}
				if (recordValid || instance instanceof org.archiviststoolkit.model.Resources) {
					access.add(instance);
					insertedRecords++;
				}
			} catch (PersistenceException e) {
				errors = getErrors() + 1;
				if (e.getCause() instanceof ConstraintViolationException) {
					this.addLineToImportLog("Error saving, duplicate record: " + instance);
					logger.error(e.getMessage());
				} else {
					this.addLineToImportLog("Error saving record " + totalRecords + ": " + instance + "\n" + e.getCause().getCause() + "\n");
					logger.error(e.getMessage());
				}
			}
		}
	}

//	/**
//	 * import and merge.
//	 *
//	 * @param collection to import into the db
//	 */
//	private void domainImportMerge(Collection collection, Component parent) {
//
//		List mergeList = new LinkedList();
//
//		try {
//			DomainAccessObject access = null;
//			Iterator iterator = collection.iterator();
//
//			while (iterator.hasNext()) {
//				DomainObject instance = (DomainObject) iterator.next();
//				access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(instance.getClass());
//
//				Collection mergeCollection = access.findByNamedQuery(instance.getClass().getName() + ":existance", instance);
//
//				if (mergeCollection.size() == 0) {
//					mergeList.add(instance);
//					insertedRecords ++;
//				}
//			}
//
//			access.addGroup(mergeList, parent);
//
//		} catch (PersistenceException persistenceException) {
//			System.out.println("Exception " + persistenceException);
//		} catch (LookupException lookupException) {
//			System.out.println("Exception " + lookupException);
//		}
//	}
//
//	/**
//	 * Import and overwrite.
//	 *
//	 * @param collection to inject into the db.
//	 */
//	private void domainImportOverwrite(Collection collection, Component parent) {
//
//		List mergeList = new LinkedList();
//
//		try {
//			DomainAccessObject access = null;
//			Iterator iterator = collection.iterator();
//
//			while (iterator.hasNext()) {
//				DomainObject instance = (DomainObject) iterator.next();
//				access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(instance.getClass());
//
//				Collection mergeCollection = access.findByNamedQuery(instance.getClass().getName() + ":existance", instance);
//
//				if (mergeCollection.size() == 0) {
//					mergeList.add(instance);
//					insertedRecords ++;
//				} else {
//					DomainObject oldObject = (DomainObject) Collections.min(mergeCollection);
//					//instance.copyTo (oldObject);
//					mergeList.add(instance);
//					updatedRecords ++;
//
//				}
//			}
//
//			access.addGroup(mergeList, parent);
//
//		} catch (PersistenceException persistenceException) {
//			System.out.println("Exception " + persistenceException);
//		} catch (LookupException lookupException) {
//			System.out.println("Exception " + lookupException);
//		}
//	}
//

	public int getErrors() {
		return errors;
	}

    /**
     * Method to specify whether to clear the total record count. This is needed
     * for cases where records are imported and attached to existing domain objects
     * as in the case with digital object import.
     *
     * @param clearTotalRecords
     */
    public void setClearTotalRecords(boolean clearTotalRecords) {
        this.clearTotalRecords = clearTotalRecords;
    }
}