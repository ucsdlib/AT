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
import org.archiviststoolkit.model.ArchDescriptionPhysicalDescriptions;
import org.archiviststoolkit.util.ATPropertyValidationSupport;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.ValidationUtils;

public class ArchDescriptionPhysicalDescriptionsValidator extends ATAbstractValidator {


	// Instance Creation ******************************************************

	/**
	 * Constructs an validator
	 *
	 * @param physicalDescription the accession to be validated
	 */
	public ArchDescriptionPhysicalDescriptionsValidator(ArchDescriptionPhysicalDescriptions physicalDescription) {
		this.objectToValidate = physicalDescription;
	}

	public ArchDescriptionPhysicalDescriptionsValidator() {
	}


	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the accession validation
	 */
	public ValidationResult validate() {

		ArchDescriptionPhysicalDescriptions modelToValidate = (ArchDescriptionPhysicalDescriptions)objectToValidate;

		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "Physical Description");

		if (ValidationUtils.isBlank(modelToValidate.getExtentType()))
			support.addError("Extent Type", "is mandatory");

		if (modelToValidate.getExtentNumber() == null ||
				modelToValidate.getExtentNumber() == 0)
			support.addError("Extent", "is mandatory");


		checkForStringLengths(modelToValidate, support);

		return support.getResult();
	}
}