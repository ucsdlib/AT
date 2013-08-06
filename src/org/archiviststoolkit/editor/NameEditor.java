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

import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;
import java.lang.reflect.Method;

import javax.swing.*;

import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.swing.StandardPanel;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

/**
 * Editor for dealing with the contact domain object.
 */

public class NameEditor extends DomainEditor {

	protected StandardPanel namePersonPanel;
	protected StandardPanel nameFamilyPanel;
	protected StandardPanel nameCorporatePanel;
	protected StandardPanel detailPanel;
	protected StandardPanel descriptionPanel;
	protected StandardPanel contactPanel;

	protected JLabel typePersonPanel = new JLabel();
	protected JLabel typeFamilyPanel = new JLabel();
	protected JLabel typeCorporatePanel = new JLabel();

	/**
	 * Constructor.
	 *
	 * @param parentFrame the parentframe to render this editor to.
	 */
	public NameEditor(JFrame parentFrame, DomainEditorFields editorFields) {
		super(Names.class, parentFrame, editorFields);
	}

	public NameEditor(JDialog parentFrame, DomainEditorFields editorFields) {
		super(Names.class, parentFrame, editorFields);
	}


	/**
	 * Displays the dialog box representing the editor.
	 *
	 * @return true if it displayed okay
	 */

	public int showDialog() {
		((NameFields)editorFields).setDisplayToFirstTab();
		return super.showDialog();	//To change body of overridden methods use File | Settings | File Templates.
	}

	public void setDetailsPanel(String nameType) {
		((NameFields) editorFields).setDetailsPanel(nameType);
	}

	/**
	 * Sets the model for this editor.
	 *
	 * @param model the model to be used
	 */

	public void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		Names namesModel = (Names) model;
		setDetailsPanel(namesModel.getNameType());
	}
}