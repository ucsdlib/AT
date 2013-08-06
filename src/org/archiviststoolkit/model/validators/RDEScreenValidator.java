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

import org.archiviststoolkit.model.*;
import org.archiviststoolkit.util.ATPropertyValidationSupport;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.ValidationUtils;

public class RDEScreenValidator extends ATAbstractValidator {


	// Instance Creation ******************************************************

	/**
	 * Constructs an validator
	 *
	 * @param rdeScreen the accession to be validated
	 */
	public RDEScreenValidator(RDEScreen rdeScreen) {
		this.objectToValidate = rdeScreen;
	}

	public RDEScreenValidator() {
	}


	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the accession validation
	 */
	public ValidationResult validate() {

		RDEScreen modelToValidate = (RDEScreen)objectToValidate;

		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "RDE Screen");

		if (ValidationUtils.isBlank(modelToValidate.getRdeScreenName()))
			support.addError("Name", "is mandatory");

		boolean isLevelPresent = false;
		boolean isTitleOrDatePresent = false;
		boolean areBulkDatesPresent = false;
		boolean areInclusiveDatesPresent = false;

		for (RDEScreenPanels panel: modelToValidate.getScreenPanels()) {
			if (panel.getPanelType().equals(RDEScreenPanels.PANEL_TYPE_YEAR_RANGE_INCLUSIVE)) {
				isTitleOrDatePresent = true;
			} else if (panel.getPanelType().equals(RDEScreenPanels.PANEL_TYPE_SIMPLE)) {
				if (panel.getPropertyName().equals(ResourcesComponents.PROPERTYNAME_LEVEL)) {
					isLevelPresent = true;
				} else if (panel.getPropertyName().equals(ResourcesComponents.PROPERTYNAME_TITLE)) {
					isTitleOrDatePresent = true;
				} else if (panel.getPropertyName().equals(ResourcesComponents.PROPERTYNAME_DATE_EXPRESSION)) {
					isTitleOrDatePresent = true;
				}
			}
			if (panel.getPanelType().equals(RDEScreenPanels.PANEL_TYPE_YEAR_RANGE_BULK)) {
				areBulkDatesPresent = true;
			} else if (panel.getPanelType().equals(RDEScreenPanels.PANEL_TYPE_YEAR_RANGE_INCLUSIVE)) {
				areInclusiveDatesPresent = true;
			}
		}

		if (!isLevelPresent) {
			support.addSimpleError("A level panel must be included.");
		}

		if (areBulkDatesPresent && !areInclusiveDatesPresent) {
			support.addSimpleError("Bulk dates cannot be present without inclusive dates.");
		}

		if (!isTitleOrDatePresent) {
			support.addSimpleError("A Title, Date Expression, or Inclusive Dates panel must be included.");
		}

		return support.getResult();
	}
}