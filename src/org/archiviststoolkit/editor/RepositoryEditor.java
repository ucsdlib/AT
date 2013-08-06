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
 */


package org.archiviststoolkit.editor;

//==============================================================================
// Import Declarations
//==============================================================================

import javax.swing.*;

import org.archiviststoolkit.mydomain.DomainEditor;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.model.Repositories;
import org.archiviststoolkit.model.Users;
import org.archiviststoolkit.swing.RecordNavigationButtons;

import java.awt.*;

/**
 * Editor for dealing with the contact domain object.
 */

public class RepositoryEditor extends DomainEditor {

	/**
	 * Constructor.
	 *
	 * @param parentFrame the parentframe to render this editor to.
	 */
	public RepositoryEditor(Dialog parentFrame) {
		super(Repositories.class, parentFrame, new RepositoryFields());
		if (!Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_REPOSITORY_MANAGER)) {
			this.setFormToReadOnly();
		}
	}
}