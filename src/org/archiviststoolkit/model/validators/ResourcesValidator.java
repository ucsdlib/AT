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

import org.archiviststoolkit.model.Accessions;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.util.ATPropertyValidationSupport;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.ValidationUtils;

public class ResourcesValidator extends ATAbstractValidator {


	// Instance Creation ******************************************************

	/**
	 * Constructs an validator
	 *
	 * @param accession the accession to be validated
	 */
	public ResourcesValidator(Accessions accession) {
		this.objectToValidate = accession;
	}

	public ResourcesValidator() {
	}


	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the accession validation
	 */
	public ValidationResult validate() {

		Resources modelToValidate = (Resources)objectToValidate;

		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "Resources");

		if (ValidationUtils.isBlank(modelToValidate.getResourceIdentifier1()))
			support.addError("Resource Identifier", "is mandatory");

		if (ValidationUtils.isBlank(modelToValidate.getLevel()))
			support.addError("Level", "is mandatory");

		if (ValidationUtils.isBlank(modelToValidate.getTitle()))
			support.addError("Title", "is mandatory");

		if (ValidationUtils.isBlank(modelToValidate.getExtentType()))
			support.addError("Extent Type", "is mandatory");

		if (ValidationUtils.isBlank(modelToValidate.getLanguageCode()))
			support.addError("Language Code", "is mandatory");

		//digitalObject date or title are manditory
		if (!checkForTitleOrDate(modelToValidate))
			support.addError("Date", "is mandatory");

		//accession date is manditory
		if (modelToValidate.getExtentNumber() == null ||
				modelToValidate.getExtentNumber() == 0)
			support.addError("Extent", "is mandatory");

		//repository is manditory
		if (modelToValidate.getRepository() == null)
			support.addError("Repository", "is mandatory");

		checkInclusiveDates(support, modelToValidate);
		checkBulkDates(support, modelToValidate);
		checkInclusiveVsBulkDates(support, modelToValidate);

		checkForStringLengths(modelToValidate, support);

		return support.getResult();
	}

	private boolean checkForTitleOrDate(Resources resource) {
		if (ValidationUtils.isNotBlank(resource.getDateExpression())) {
			return true;
		} else if (resource.getDateBegin() != null &&
				resource.getDateBegin() != 0) {
			return true;
		} else {
			return false;
		}
	}

}
