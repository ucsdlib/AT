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
 * Programmer: Lee Mandell
 */

package org.archiviststoolkit.editor;

import org.archiviststoolkit.mydomain.DomainEditor;
import org.archiviststoolkit.model.Repositories;
import org.archiviststoolkit.model.Users;
import org.archiviststoolkit.model.Deaccessions;

import javax.swing.*;

/**
 * Editor for dealing with the contact domain object.
 */

public class DeaccessionsEditor extends DomainEditor {

	/**
	 * Default constructor.
	 */
	public DeaccessionsEditor(JDialog parentFrame) {
		super(Deaccessions.class, parentFrame, new DeaccessionsFields());
		editorFields = new DeaccessionsFields();
		this.setContentPanel(editorFields);
	}

	/**
	 * Constructor.
	 *
	 * @param parentFrame the parentframe to render this editor to.
	 */
	public DeaccessionsEditor(JFrame parentFrame) {
		super(Repositories.class, parentFrame, new DeaccessionsFields());
		editorFields = new DeaccessionsFields();
		this.setContentPanel(editorFields);
	}
}
