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
 * Date: Jul 31, 2007
 * Time: 11:23:27 AM
 */

package org.archiviststoolkit.model.validators;

import org.archiviststoolkit.structure.DefaultValues;
import org.archiviststoolkit.structure.DatabaseFields;
import org.archiviststoolkit.util.ATPropertyValidationSupport;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.ValidationUtils;
import com.jgoodies.binding.value.BufferedValueModel;

public class DatabaseFieldsValidator extends ATAbstractValidator {


	// Instance Creation ******************************************************

	/**
	 * Constructs an validator
	 *
	 * @param defaultValue the accession to be validated
	 */
	public DatabaseFieldsValidator(DefaultValues defaultValue) {
		this.objectToValidate = defaultValue;
	}

	public DatabaseFieldsValidator() {
	}

	private BufferedValueModel returnScreenOrderBufferedModel;

	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the accession validation
	 */
	public ValidationResult validate() {

		DatabaseFields modelToValidate = (DatabaseFields)objectToValidate;

		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "Database Fields");

		if (returnScreenOrderBufferedModel.getValue() == null || (Integer)returnScreenOrderBufferedModel.getValue() < 0) {
			support.addSimpleError("Return screen order must be 0 or a positive number");
		}

		checkForStringLengths(modelToValidate, support);

		return support.getResult();
	}

	public void setReturnScreenOrderBufferedModel(BufferedValueModel returnScreenOrderBufferedModel) {
		this.returnScreenOrderBufferedModel = returnScreenOrderBufferedModel;
	}
}
