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

import org.archiviststoolkit.model.ArchDescriptionDates;
import org.archiviststoolkit.util.ATPropertyValidationSupport;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.ValidationUtils;

public class ArchDescriptionDatesValidator extends ATAbstractValidator {


	// Instance Creation ******************************************************

	/**
	 * Constructs an validator
	 *
	 * @param dateExpressions the accession to be validated
	 */
	public ArchDescriptionDatesValidator(ArchDescriptionDates dateExpressions) {
		this.objectToValidate = dateExpressions;
	}

	public ArchDescriptionDatesValidator() {
	}


	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the accession validation
	 */
	public ValidationResult validate() {

		ArchDescriptionDates modelToValidate = (ArchDescriptionDates)objectToValidate;

		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "Physical Description");

		if (!checkForDatePresent(modelToValidate))
			support.addError("Date", "is mandatory");

		checkInclusiveDates(support, modelToValidate);
		checkBulkDates(support, modelToValidate);
		checkInclusiveVsBulkDates(support, modelToValidate);

		checkForStringLengths(modelToValidate, support);

		return support.getResult();
	}

	private boolean checkForDatePresent(ArchDescriptionDates dateExpressions) {
		if (ValidationUtils.isNotBlank(dateExpressions.getDateExpression())) {
			return true;
		} else if (ValidationUtils.isNotBlank(dateExpressions.getIsoDateBegin())) {
			return true;
		} else {
			return false;
		}
	}

	protected void checkInclusiveDates(ATPropertyValidationSupport support, ArchDescriptionDates dateExpression) {
		//end date must be equal to or after begin date
		if (dateExpression.getDateBegin() != null
				&& dateExpression.getDateEnd() != null
				&& dateExpression.getDateBegin() > dateExpression.getDateEnd()) {
			support.addSimpleError("Date Begin can't be after Date End");
		}

		//If either begin or end date are present the other must be filled in
		if ((dateExpression.getDateBegin() != null && dateExpression.getDateEnd() == null) ||
				(dateExpression.getDateBegin() == null && dateExpression.getDateEnd() != null)) {
			support.addSimpleError("Date Begin and Date End must both be filled in");
		}

		if (ATAbstractValidator.isFutureYear(dateExpression.getDateBegin())) {
			support.addSimpleError("Date Begin can't be in the future");
		}
		if (ATAbstractValidator.isFutureYear(dateExpression.getDateEnd())) {
			support.addSimpleError("End Begin can't be in the future");
		}
	}

	protected void checkInclusiveVsBulkDates(ATPropertyValidationSupport support, ArchDescriptionDates dateExpression) {
		//bulk dates must be within inclusive dates
		if (dateExpression.getBulkDateBegin() != null && dateExpression.getDateBegin() != null &&
				dateExpression.getBulkDateEnd() != null && dateExpression.getDateEnd() != null) {
			if (dateExpression.getBulkDateBegin() < dateExpression.getDateBegin() ||
					dateExpression.getBulkDateEnd() > dateExpression.getDateEnd()) {
				support.addSimpleError("Bulk dates must be within inclusive dates");
			}
		}

		//if there are bulk dates present then there must also be inclusive dates
		if (dateExpression.getDateBegin() == null && dateExpression.getDateEnd() == null) {
			if (dateExpression.getBulkDateBegin() != null || dateExpression.getBulkDateEnd() != null) {
				support.addSimpleError("Bulk dates can only be entered when inclusive dates are present");
			}
		}
	}
	protected void checkBulkDates(ATPropertyValidationSupport support, ArchDescriptionDates dateExpression) {
		//bulk end date must be equal to or after bulk begin date
		if (dateExpression.getBulkDateBegin() != null
				&& dateExpression.getBulkDateEnd() != null
				&& dateExpression.getBulkDateBegin() > dateExpression.getBulkDateEnd()) {
			support.addSimpleError("Bulk Date Begin can't be after Bulk Date End");
		}

		//If either begin or end date are present the other must be filled in
		if ((dateExpression.getBulkDateBegin() != null && dateExpression.getBulkDateEnd() == null) ||
				(dateExpression.getBulkDateBegin() == null && dateExpression.getBulkDateEnd() != null)) {
			support.addSimpleError("Bulk Date Begin and Bulk Date End must both be filled in");
		}

		if (ATAbstractValidator.isFutureYear(dateExpression.getBulkDateBegin())) {
			support.addSimpleError("Bulk Date Begin can't be in the future");
		}
		if (ATAbstractValidator.isFutureYear(dateExpression.getBulkDateEnd())) {
			support.addSimpleError("Bulk End Begin can't be in the future");
		}
	}

}