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
import org.archiviststoolkit.model.Subjects;
import org.archiviststoolkit.util.ATPropertyValidationSupport;
import org.archiviststoolkit.structure.DefaultValues;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.ValidationUtils;

public class DefaultValueValidator extends ATAbstractValidator {


	// Instance Creation ******************************************************

	/**
	 * Constructs an validator
	 *
	 * @param defaultValue the accession to be validated
	 */
	public DefaultValueValidator(DefaultValues defaultValue) {
		this.objectToValidate = defaultValue;
	}

	public DefaultValueValidator() {
	}


	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the accession validation
	 */
	public ValidationResult validate() {

		DefaultValues modelToValidate = (DefaultValues)objectToValidate;

		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "Default Value");

		if (ValidationUtils.isBlank(modelToValidate.getTableName()))
			support.addError("Table", "is mandatory");

		if (modelToValidate.getAtField() == null)
			support.addError("Field", "is mandatory");

		if (modelToValidate.getValue() == null)
			support.addError("Value", "is mandatory");

		checkForStringLengths(modelToValidate, support);

		return support.getResult();
	}
}
