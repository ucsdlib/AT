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
import org.archiviststoolkit.util.ATPropertyValidationSupport;

public class ArchDescriptionNamesValidator extends ATAbstractValidator {
	public ArchDescriptionNamesValidator() {
	}

	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the objectToValidate validation
	 */
	public ValidationResult validate() {

		ArchDescriptionNames modelToValidate = (ArchDescriptionNames)objectToValidate;
		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "Arch Description Namee");

		//objectToValidate function is manditory
		if (ValidationUtils.isBlank(modelToValidate.getNameLinkFunction()))
			support.addError("Function", "is mandatory");


		//objectToValidate name is manditory
		if (modelToValidate.getName() == null)
			support.addError("Name", "is mandatory");

		//objectToValidate name is manditory
		if (modelToValidate.getAccession() == null &&
				modelToValidate.getResource() == null &&
				modelToValidate.getResourceComponent() == null &&
				modelToValidate.getDigitalObject() == null)
			support.addSimpleError("A link to a Resource, Resource Component, Accession or Digital Object is required");

		if (modelToValidate.getNameLinkFunction().equalsIgnoreCase("source") &&
				(modelToValidate.getResourceComponent() != null || modelToValidate.getDigitalObject() != null))
			support.addSimpleError("Sources can only be linked to a Resource or Accession record");

		checkForStringLengths(modelToValidate, support);

		return support.getResult();
	}

}
