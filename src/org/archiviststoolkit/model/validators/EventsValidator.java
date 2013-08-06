package org.archiviststoolkit.model.validators;

/**
 * Archivists' Toolkit(TM) Copyright © 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
 * All rights reserved.
 * <p/>
 * This software is free. You can redistribute it and / or modify it under the terms of the Educational Community License (ECL)
 * version 1.0 (http://www.opensource.org/licenses/ecl1.php)
 * <p/>
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the ECL license for more details about permissions and limitations.
 * <p/>
 * <p/>
 * Archivists' Toolkit(TM)
 * http://www.archiviststoolkit.org
 * info@archiviststoolkit.org
 * <p/>
 * Created by IntelliJ IDEA.
 *
 * User: Nathan Stevens
 * Date: Nov 4, 2008 (Obama Day)
 * Time: 2:59:12 PM
 * To change this template use File | Settings | File Templates.
 */
import org.archiviststoolkit.model.Accessions;
import org.archiviststoolkit.model.Events;
import org.archiviststoolkit.util.ATPropertyValidationSupport;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.ValidationUtils;

public class EventsValidator extends ATAbstractValidator {
    /**
	 * Constructs an validator
	 *
	 * @param accession the accession to be validated
	 */
	public EventsValidator(Accessions accession) {
		this.objectToValidate = accession;
	}

	public EventsValidator() {
	}


	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the accession validation
	 */
	public ValidationResult validate() {

		Events modelToValidate = (Events)objectToValidate;

		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "Events");

		if (ValidationUtils.isBlank(modelToValidate.getEventDescription()))
			support.addError("Event Description", "is mandatory");

		checkForStringLengths(modelToValidate, support);

		return support.getResult();
	}
}
