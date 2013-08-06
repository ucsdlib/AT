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
 * Programmer: Lee Mandell
 * Date: Nov 6, 2006
 * Time: 4:23:14 PM
 */

package org.archiviststoolkit.editor.rde;

import com.jgoodies.validation.Validator;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.ValidationUtils;

import org.archiviststoolkit.model.validators.ATAbstractValidator;
import org.archiviststoolkit.editor.rde.RapidResourceComponentDataEntry;

public class RapidResourceComponentDataEntryValidator implements Validator {

	private RapidResourceComponentDataEntry dialog;

	public RapidResourceComponentDataEntryValidator(RapidResourceComponentDataEntry dialog) {
		this.dialog = dialog;
	}

	public ValidationResult validate() {

		ValidationResult result = new ValidationResult();

		if (ATAbstractValidator.isFutureYear(dialog.getDateBegin())) {
			result.addError("Date Begin can't be in the future");
		}
		if (ATAbstractValidator.isFutureYear(dialog.getDateEnd())) {
			result.addError("End Begin can't be in the future");
		}

		if (ValidationUtils.isBlank(dialog.getLevel()))
			result.addError("Level is mandatory");

		if (!checkForTitleOrDate())
			result.addError("Title or Date is mandatory");

		String container1Type = dialog.getContainer1Type();
		String container2Type = dialog.getContainer2Type();
		Double container1NumericValue = dialog.getContainer1NumericValue();
		String container1AlphaNumericValue = dialog.getContainer1AlphaNumericValue();
		Double container2NumericValue = dialog.getContainer2NumericValue();
		String container2AlphaNumericValue = dialog.getContainer2AlphaNumericValue();
		
		if (container2Type.length() > 0 && container1Type.length() == 0) {
			result.addError("You must enter container 1 if you enter container 2");
		}

		if (isValueEntered(container1NumericValue, container1AlphaNumericValue) && container1Type.length() == 0) {
			result.addError("You must enter container 1 type if you enter container 1 value");
		}

		if (isValueEntered(container2NumericValue, container2AlphaNumericValue) && container2Type.length() == 0) {
			result.addError("You must enter container 2 type if you enter container 2 value");
		}

		if (!isValueEntered(container1NumericValue, container1AlphaNumericValue) && container1Type.length() > 0) {
			result.addError("You must enter container 1 value if you enter container 1 type");
		}

		if (!isValueEntered(container2NumericValue, container2AlphaNumericValue) && container2Type.length() > 0) {
			result.addError("You must enter container 2 value if you enter container 2 type");
		}

        if (isValueEntered(container1NumericValue, container1AlphaNumericValue) && dialog.getInstanceType().length() == 0) {
            result.addError("You must enter an instance type when you enter container information");
        }
        return result;
	}

	private boolean isValueEntered(Double doubleValue, String stringValue) {
		return (doubleValue > 0 || stringValue.length() > 0);
	}

	private boolean checkForTitleOrDate() {
		if (ValidationUtils.isNotBlank(dialog.getComponentTitle())) {
//			System.out.println(dialog.getT)
			return true;
		} else if (ValidationUtils.isNotBlank(dialog.getDateExpression())) {
			return true;
		} else if (dialog.getDateBegin() != null &&
				dialog.getDateBegin() != 0) {
			return true;
		} else {
			return false;
		}
	}

}
