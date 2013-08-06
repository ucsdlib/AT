/**
 * Archivists' Toolkit(TM) Copyright © 2005-2009 Regents of the University of California, New York University, & Five Colleges, Inc.
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
 * @author Nathan Stevens
 * Date: May 11, 2009
 * Time: 11:16:26 AM
 */

package org.archiviststoolkit.model.validators;

import org.archiviststoolkit.model.Assessments;
import org.archiviststoolkit.util.ATPropertyValidationSupport;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.ValidationUtils;

public class AssessmentsValidator extends ATAbstractValidator {


	// Instance Creation ******************************************************

	/**
	 * Constructs an validator
	 *
	 * @param assessments The assessments to be validated
	 */
	public AssessmentsValidator(Assessments assessments) {
		this.objectToValidate = assessments;
	}

	public AssessmentsValidator() {
	}


	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the assessments validation
	 */
	public ValidationResult validate() {

		Assessments modelToValidate = (Assessments)objectToValidate;

		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "Assessments");

		if (!modelToValidate.hasLinkRecords()) {
			support.addError("Linked Record", "(Accession, Resource, or Digital Object) is mandatory");
        }

		checkForStringLengths(modelToValidate, support);

		return support.getResult();
	}
}