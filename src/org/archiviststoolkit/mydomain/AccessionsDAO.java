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
 * Date: Oct 27, 2006
 * Time: 4:30:50 PM
 */

package org.archiviststoolkit.mydomain;

import org.archiviststoolkit.model.Accessions;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.ApplicationFrame;

import java.util.Collection;
import java.sql.SQLException;

public class AccessionsDAO extends DomainAccessObjectImpl{

	public AccessionsDAO() {
		super(Accessions.class);
	}

	public void updateDisplayFields(InfiniteProgressPanel progressPanel) {

		int recordCount = 0;
		try {
			Collection accessions = findAllLongSession();
			Accessions accession;
			for (Object o : accessions) {
				accession = (Accessions) o;
				progressPanel.setTextLine("Accessions " + ++recordCount + " of " + accessions.size(), 2);
				accession.setDisplayCreator(accession.getCreator());
				accession.setDisplayRepository(accession.getRepository().getRepositoryName());
				accession.setDisplaySource(accession.getSource());
				updateLongSession(accession, false);
			}
			closeLongSession();
		} catch (LookupException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "Error updating display fields", e).showDialog();
		} catch (PersistenceException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "Error updating display fields", e).showDialog();
		} catch (SQLException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "Error updating display fields", e).showDialog();
		}
	}


}
