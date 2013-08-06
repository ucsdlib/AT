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
 * Date: May 16, 2006
 * Time: 11:16:26 AM
 */

package org.archiviststoolkit.model.validators;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.ValidationUtils;
import org.archiviststoolkit.model.ArchDescriptionNames;
import org.archiviststoolkit.model.ArchDescriptionNotes;
import org.archiviststoolkit.util.ATPropertyValidationSupport;

public class ArchDescriptionNotesValidator extends ATAbstractValidator {
	public ArchDescriptionNotesValidator() {
	}

	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the objectToValidate validation
	 */
	public ValidationResult validate() {

		ArchDescriptionNotes modelToValidate = (ArchDescriptionNotes)objectToValidate;
		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "Arch Description Note");

		if (modelToValidate.getBasic()) {
			//objectToValidate name is manditory
			if (modelToValidate.getParentNote() == null)
				support.addSimpleError("A link to a parent note is required");			

			//objectToValidate Note Content is manditory
			if (ValidationUtils.isBlank(modelToValidate.getNoteContent()))
				support.addError("Note Content", "is mandatory");

		} else {
			//objectToValidate Note Type is manditory
			if (ValidationUtils.isBlank(modelToValidate.getNoteType()))
				support.addError("Note Type", "is mandatory");

			//objectToValidate name is manditory
			if (modelToValidate.getResource() == null &&
					modelToValidate.getResourceComponent() == null &&
					modelToValidate.getDigitalObject() == null)
				support.addSimpleError("A link to a Resource, Resource Component or Digital Object is required");

			if (!modelToValidate.getMultiPart()) {
				//objectToValidate Note Content is manditory
				if (ValidationUtils.isBlank(modelToValidate.getNoteContent()))
					support.addError("Note Content", "is mandatory");
			}
		}


		checkForStringLengths(modelToValidate, support);


		return support.getResult();
	}

}
