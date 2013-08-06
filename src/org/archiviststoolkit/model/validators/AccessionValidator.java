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
import org.archiviststoolkit.model.Accessions;
import org.archiviststoolkit.util.ATPropertyValidationSupport;

public class AccessionValidator extends ATAbstractValidator {


	// Instance Creation ******************************************************

	/**
	 * Constructs an validator
	 *
	 * @param accession the accession to be validated
	 */
	public AccessionValidator(Accessions accession) {
		this.objectToValidate = accession;
	}

	public AccessionValidator() {
	}


	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the accession validation
	 */
	public ValidationResult validate() {

		Accessions modelToValidate = (Accessions)objectToValidate;

		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "Accession");

		//modelToValidate number is manditory
		if (ValidationUtils.isBlank(modelToValidate.getAccessionNumber1()))
			support.addError("Accession No", "is mandatory");

		//modelToValidate date is manditory
		if (modelToValidate.getAccessionDate() == null)
			support.addError("Accession Date", "is mandatory");

		//repository is manditory
		if (modelToValidate.getRepository() == null)
			support.addError("Repository", "is mandatory");

		if (modelToValidate.getAccessionDate() != null && ValidationUtils.isFutureDay(modelToValidate.getAccessionDate())) {
			support.addError("Accession date", "can't be in the future");
		}

		if (modelToValidate.getAccessionProcessedDate() != null && ValidationUtils.isFutureDay(modelToValidate.getAccessionProcessedDate())) {
			support.addError("Date processed", "can't be in the future");
		}

		if (modelToValidate.getAcknowledgementDate() != null && ValidationUtils.isFutureDay(modelToValidate.getAcknowledgementDate())) {
			support.addError("Acknowledgement date", "can't be in the future");
		}

		if (modelToValidate.getAgreementSentDate() != null && ValidationUtils.isFutureDay(modelToValidate.getAgreementSentDate())) {
			support.addError("Agreement sent", "can't be in the future");
		}

		if (modelToValidate.getAgreementReceivedDate() != null && ValidationUtils.isFutureDay(modelToValidate.getAgreementReceivedDate())) {
			support.addError("Accession received", "can't be in the future");
		}

		checkInclusiveDates(support, modelToValidate);
		checkBulkDates(support, modelToValidate);
		checkInclusiveVsBulkDates(support, modelToValidate);

		checkForStringLengths(modelToValidate, support);

		return support.getResult();
	}
}
