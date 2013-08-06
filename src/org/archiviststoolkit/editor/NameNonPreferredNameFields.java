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
 * Date: Jul 18, 2006
 * Time: 12:49:28 PM
 */

package org.archiviststoolkit.editor;

import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.model.Users;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.model.NonPreferredNames;

import javax.swing.*;

public abstract class NameNonPreferredNameFields extends DomainEditorFields {
	public NameNonPreferredNameFields() {
		super();
	}

	protected void enableDisableSortName(JTextField sortName, Boolean updateSortName) {
		NonPreferredNames nameModel = (NonPreferredNames)this.getModel();
		if (nameModel != null && nameModel.getCreateSortNameAutomatically()) {
			sortName.setEditable(false);
			sortName.setOpaque(false);
			if (updateSortName) {
				nameModel.createSortName();
			}
		} else {
			sortName.setEditable(true);
			sortName.setOpaque(true);
		}
	}
}
