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
 * Date: Nov 28, 2005
 * Time: 3:17:18 PM
 */

package org.archiviststoolkit.mydomain;

import org.archiviststoolkit.model.Repositories;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.ApplicationFrame;
import org.netbeans.spi.wizard.DeferredWizardResult;

public class RepositoriesDAO extends DomainAccessObjectImpl {

	/**
	 * Constructor which builds a DAO for this class.
	 */
	public RepositoriesDAO() {
		super(Repositories.class);
	}

	public void addNotesDefautsRecords() {
		addNotesDefautsRecords(null);
	}

	public void addNotesDefautsRecords(DeferredWizardResult.ResultProgressHandle progress) {

		Repositories repository;
		try {
			for (Object o: findAllLongSession(Repositories.PROPERTYNAME_REPOSITORY_NAME)) {
				repository = (Repositories)o;
				repository.createNoteDefaultValueLinks();
				if (progress != null) {
					progress.setProgress(repository.getRepositoryName(), 0, 0);
				}
				updateLongSession(repository, false);
			}

		} catch (LookupException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
		} catch (PersistenceException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
		}
	}
}
