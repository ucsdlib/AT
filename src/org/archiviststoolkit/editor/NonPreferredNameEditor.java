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
import org.archiviststoolkit.model.NonPreferredNames;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ValueHolder;

/**
 * Editor for dealing with the contact domain object.
 */

public class NonPreferredNameEditor extends DomainEditor {

	public PresentationModel detailsModel = new PresentationModel(new ValueHolder(null, true));

	private NamePersonalNonPreferredFields personalNameFields = null;
	private NameCorpNonPreferredFields corporateNameFields;
	private NameFamilyNonPreferredFields familyNameFields;

	/**
	 * Constructor.
	 * @param parentFrame the parentframe to render this editor to.
	 */
	public NonPreferredNameEditor (JDialog parentFrame) {
		super (NonPreferredNames.class, parentFrame);

		personalNameFields = new NamePersonalNonPreferredFields(detailsModel);
		corporateNameFields = new NameCorpNonPreferredFields(detailsModel);
		familyNameFields = new NameFamilyNonPreferredFields(detailsModel);
		this.setContentPanel(familyNameFields);
	}

	/**
	 * Sets the model for this editor.
	 * @param model the model to be used
	 */

	public final void setModel (final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		String recordType = ((NonPreferredNames)this.getModel()).getNameType();
		if(recordType.equalsIgnoreCase("Person")) {
			personalNameFields.setModel(model, null);
            editorFields = personalNameFields;
            this.setContentPanel(personalNameFields);
		} else if(recordType.equalsIgnoreCase("Family")) {
			familyNameFields.setModel(model, null);
            editorFields = familyNameFields;
			this.setContentPanel(familyNameFields);
		} else if(recordType.equalsIgnoreCase("Corporate Body")) {
			corporateNameFields.setModel(model, null);
            editorFields = corporateNameFields;
			this.setContentPanel(corporateNameFields);
		}
	}

	/**
	 * get the model that this editor is using.
	 * @return the domain model
	 */

	public final DomainObject getModel () {

		NonPreferredNames nonPreferredNamesModel = (NonPreferredNames)super.getModel();
		String recordType = nonPreferredNamesModel.getNameType();
		if(recordType.equalsIgnoreCase("Person")) {
			nonPreferredNamesModel = (NonPreferredNames)personalNameFields.getModel();
		} else if(recordType.equalsIgnoreCase("Family")) {
			nonPreferredNamesModel = (NonPreferredNames)familyNameFields.getModel();
		} else if(recordType.equalsIgnoreCase("Corporate Body")) {
			nonPreferredNamesModel = (NonPreferredNames)corporateNameFields.getModel();
		}

		return (super.getModel());
	}

}