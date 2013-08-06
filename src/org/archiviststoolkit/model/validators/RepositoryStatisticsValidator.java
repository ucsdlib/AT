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
 * Date: Apr 1, 2007
 * Time: 10:00:37 AM
 */

package org.archiviststoolkit.model.validators;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.ValidationUtils;
import org.archiviststoolkit.model.Repositories;
import org.archiviststoolkit.model.RepositoryStatistics;
import org.archiviststoolkit.util.ATPropertyValidationSupport;

public class RepositoryStatisticsValidator extends ATAbstractValidator {
	public RepositoryStatisticsValidator() {
	}

	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the objectToValidate validation
	 */
	public ValidationResult validate() {

		RepositoryStatistics modelToValidate = (RepositoryStatistics)objectToValidate;
		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "Repository Statistics");

		if (modelToValidate.getYearOfReport() == null || modelToValidate.getYearOfReport() ==0) {
			support.addError("Year of report", "is mandatory");
		}

		if (ATAbstractValidator.isFutureYear(modelToValidate.getYearOfReport())) {
            support.addSimpleError("Year of report can't be in the future");
        }

		checkForStringLengths(modelToValidate, support);

		return support.getResult();
	}

}
