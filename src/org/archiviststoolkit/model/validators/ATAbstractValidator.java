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
 * Date: May 23, 2006
 * Time: 11:23:51 AM
 */

package org.archiviststoolkit.model.validators;

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.model.Accessions;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.AccessionsResourcesCommon;
import org.archiviststoolkit.util.ATPropertyValidationSupport;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.dialog.ErrorDialog;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.ValidationUtils;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.lang.reflect.InvocationTargetException;

public abstract class ATAbstractValidator implements ATValidator {

	/**
	 * Holds the object to be validated.
	 */
	protected Object objectToValidate;

	public void setModelToValidate(Object modelToValidate) {
		this.objectToValidate = modelToValidate;
	}

	//common methods for dealing with ArchDescription Descendants

	protected void checkBulkDates(ATPropertyValidationSupport support, AccessionsResourcesCommon archDescription) {
		//bulk end date must be equal to or after bulk begin date
		if (archDescription.getBulkDateBegin() != null
				&& archDescription.getBulkDateEnd() != null
				&& archDescription.getBulkDateBegin() > archDescription.getBulkDateEnd()) {
			support.addSimpleError("Bulk Date Begin can't be after Bulk Date End");
		}

		//If either begin or end date are present the other must be filled in
		if ((archDescription.getBulkDateBegin() != null && archDescription.getBulkDateEnd() == null) ||
				(archDescription.getBulkDateBegin() == null && archDescription.getBulkDateEnd() != null)) {
			support.addSimpleError("Bulk Date Begin and Bulk Date End must both be filled in");
		}

		if (ATAbstractValidator.isFutureYear(archDescription.getBulkDateBegin())) {
			support.addSimpleError("Bulk Date Begin can't be in the future");
		}
		if (ATAbstractValidator.isFutureYear(archDescription.getBulkDateEnd())) {
			support.addSimpleError("Bulk End Begin can't be in the future");
		}
	}

	protected void checkInclusiveDates(ATPropertyValidationSupport support, ArchDescription archDescription) {
		//end date must be equal to or after begin date
		if (archDescription.getDateBegin() != null
				&& archDescription.getDateEnd() != null
				&& archDescription.getDateBegin() > archDescription.getDateEnd()) {
			support.addSimpleError("Date Begin can't be after Date End");
		}

		//If either begin or end date are present the other must be filled in
		if ((archDescription.getDateBegin() != null && archDescription.getDateEnd() == null) ||
				(archDescription.getDateBegin() == null && archDescription.getDateEnd() != null)) {
			support.addSimpleError("Date Begin and Date End must both be filled in");
		}

		if (ATAbstractValidator.isFutureYear(archDescription.getDateBegin())) {
			support.addSimpleError("Date Begin can't be in the future");
		}
		if (ATAbstractValidator.isFutureYear(archDescription.getDateEnd())) {
			support.addSimpleError("End Begin can't be in the future");
		}
	}

	protected void checkInclusiveVsBulkDates(ATPropertyValidationSupport support, AccessionsResourcesCommon archDescription) {
		//bulk dates must be within inclusive dates
		if (archDescription.getBulkDateBegin() != null && archDescription.getDateBegin() != null &&
				archDescription.getBulkDateEnd() != null && archDescription.getDateEnd() != null) {
			if (archDescription.getBulkDateBegin() < archDescription.getDateBegin() ||
					archDescription.getBulkDateEnd() > archDescription.getDateEnd()) {
				support.addSimpleError("Bulk dates must be within inclusive dates");
			}
		}

		//if there are bulk dates present then there must also be inclusive dates
		if (archDescription.getDateBegin() == null && archDescription.getDateEnd() == null) {
			if (archDescription.getBulkDateBegin() != null || archDescription.getBulkDateEnd() != null) {
				support.addSimpleError("Bulk dates can only be entered when inclusive dates are present");
			}
		}
	}

	public static boolean isFutureYear(Integer year) {
		if (year == null) {
			return false;
		} else {
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			return year > currentYear;
		}
	}

	protected void checkForStringLengths(DomainObject domainObject, ATPropertyValidationSupport support) {
		try {
			for (ATFieldInfo field: ATFieldInfo.getFieldsThatNeedStringValidation(domainObject.getClass())) {
				Method method = field.getGetMethod();

                if(method != null) {
                    String fieldValue = (String)method.invoke(domainObject);
				    if (!ValidationUtils.hasMaximumLength(fieldValue, field.getStringLengthLimit())) {
					    support.addError(field.getFieldName(), "has a maximum length of " + field.getStringLengthLimit() + " characters");
				    }
                } else {
                    System.out.println("No getter method for " + field.getFieldName());
                }
			}
		} catch (IllegalAccessException e) {
			new ErrorDialog("Error validating string length", e).showDialog();
		} catch (InvocationTargetException e) {
			new ErrorDialog("Error validating string length", e).showDialog();
		}
	}

}
