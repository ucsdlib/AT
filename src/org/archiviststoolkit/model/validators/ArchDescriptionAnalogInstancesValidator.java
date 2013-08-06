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
import org.archiviststoolkit.model.ArchDescriptionAnalogInstances;
import org.archiviststoolkit.util.ATPropertyValidationSupport;

public class ArchDescriptionAnalogInstancesValidator extends ATAbstractValidator {
	public ArchDescriptionAnalogInstancesValidator() {
	}

	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the objectToValidate validation
	 */
	public ValidationResult validate() {

		ArchDescriptionAnalogInstances modelToValidate = (ArchDescriptionAnalogInstances)objectToValidate;
		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "Arch Description Analog Instance");

        // check for instance type
        if(ValidationUtils.isBlank(modelToValidate.getInstanceType())) {
            support.addError("Instance Type", "is mandatory");    
        }

        //objectToValidate container1Type is manditory
		if (ValidationUtils.isBlank(modelToValidate.getContainer1Type()))
			support.addError("Container 1 Type", "is mandatory");


		//objectToValidate container1Type is manditory
		if ((modelToValidate.getContainer1NumericIndicator() == null || modelToValidate.getContainer1NumericIndicator() == 0) &&
				ValidationUtils.isBlank(modelToValidate.getContainer1AlphaNumericIndicator()))
			support.addError("Container 1 Value", "is mandatory");

		checkForStringLengths(modelToValidate, support);

		return support.getResult();
	}

}
