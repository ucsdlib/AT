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
import org.archiviststoolkit.model.LookupList;
import org.archiviststoolkit.model.BasicNames;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.util.ATPropertyValidationSupport;

public class NamesValidator extends ATAbstractValidator {
	public NamesValidator() {
	}

	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the objectToValidate validation
	 */
	public ValidationResult validate() {

		Names modelToValidate = (Names)objectToValidate;
		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "Names");

		//Name Type is manditory
		if (ValidationUtils.isBlank(modelToValidate.getNameType()))
			support.addError("Name Type", "is mandatory");

		//Sort Name is manditory
		if (ValidationUtils.isBlank(modelToValidate.getSortName()))
			support.addError("Sort Name", "is mandatory");

		String nameType = modelToValidate.getNameType();
		if (nameType.equalsIgnoreCase(BasicNames.PERSON_TYPE)) {
			if (ValidationUtils.isBlank(modelToValidate.getPersonalPrimaryName()))
				support.addError("Primary Name", "is mandatory");
		} else if (nameType.equalsIgnoreCase(BasicNames.CORPORATE_BODY_TYPE)) {
			if (ValidationUtils.isBlank(modelToValidate.getCorporatePrimaryName()))
				support.addError("Primary Name", "is mandatory");
		} else if (nameType.equalsIgnoreCase(BasicNames.FAMILY_TYPE)) {
			if (ValidationUtils.isBlank(modelToValidate.getFamilyName()))
				support.addError("Family Name", "is mandatory");
		}
        if (ValidationUtils.isNotBlank(modelToValidate.getDescriptionNote()) &&
                ValidationUtils.isBlank(modelToValidate.getDescriptionType()))  {
            support.addError("Description Type", "is mandatory when a Description Note is entered");
        }

        if (ValidationUtils.isBlank(modelToValidate.getNameSource()) &&
				ValidationUtils.isBlank(modelToValidate.getNameRule())) {
			support.addError("Source or Rules", "are mandatory");
		}

		checkForStringLengths(modelToValidate, support);

		return support.getResult();
	}

}
