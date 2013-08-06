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
import org.archiviststoolkit.model.Users;
import org.archiviststoolkit.util.ATPropertyValidationSupport;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.exceptions.DeleteException;
import org.archiviststoolkit.mydomain.DomainAccessObject;
import org.archiviststoolkit.mydomain.DomainAccessObjectFactory;
import org.archiviststoolkit.mydomain.PersistenceException;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.ValidationUtils;

public class UserValidator extends ATAbstractValidator {


	// Instance Creation ******************************************************

	/**
	 * Constructs an validator
	 *
	 * @param accession the accession to be validated
	 */
	public UserValidator(Accessions accession) {
		this.objectToValidate = accession;
	}

	public UserValidator() {
	}


	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the accession validation
	 */
	public ValidationResult validate() {

		Users modelToValidate = (Users)objectToValidate;

		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "User");

		if (ValidationUtils.isBlank(modelToValidate.getUserName()))
			support.addError("Username", "is mandatory");

		if (modelToValidate.getPassword() == null ||
				modelToValidate.getPassword().length == 0)
			support.addError("Password", "is mandatory");

		if (modelToValidate.getRepository() == null)
			support.addError("Repository", "is mandatory");

		checkForStringLengths(modelToValidate, support);

		if (modelToValidate.getAccessClass() != Users.ACCESS_CLASS_SUPERUSER) {
			DomainAccessObject access = null;
			try {
				access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Users.class);
			} catch (PersistenceException e) {
				new ErrorDialog("Error checking for the existance of at least one superuser", e).showDialog();
			}
			int count = access.getCountBasedOnPropertyValue(Users.PROPERTYNAME_ACCESS_CLASS, Users.ACCESS_CLASS_SUPERUSER);
			if (modelToValidate.getOldAccessClass() == Users.ACCESS_CLASS_SUPERUSER) {
				if (count <= 1) {
					support.addSimpleError("There must be at least one superuser.");
				}
			} else {
				if (count < 1) {
					support.addSimpleError("There must be at least one superuser.");
				}
			}
		}


		return support.getResult();
	}
}
