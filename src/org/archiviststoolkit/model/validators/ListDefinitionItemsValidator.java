/**
 * Archivists' Toolkit(TM) Copyright � 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
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
import org.archiviststoolkit.model.ListOrderedItems;
import org.archiviststoolkit.model.ListDefinitionItems;
import org.archiviststoolkit.util.ATPropertyValidationSupport;
import org.archiviststoolkit.mydomain.DomainObject;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.ValidationUtils;

public class ListDefinitionItemsValidator extends ATAbstractValidator {


	// Instance Creation ******************************************************

	/**
	 * Constructs an validator
	 *
	 * @param domainObject the accession to be validated
	 */
	public ListDefinitionItemsValidator(DomainObject domainObject) {
		this.objectToValidate = domainObject;
	}

	public ListDefinitionItemsValidator() {
	}


	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the accession validation
	 */
	public ValidationResult validate() {

		ListDefinitionItems modelToValidate = (ListDefinitionItems)objectToValidate;

		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "List definition items");

		if (ValidationUtils.isBlank(modelToValidate.getItemValue()))
			support.addError("Value", "is mandatory");

		if (ValidationUtils.isBlank(modelToValidate.getLabel()))
			support.addError("Label", "is mandatory");

		checkForStringLengths(modelToValidate, support);

		return support.getResult();
	}
}
