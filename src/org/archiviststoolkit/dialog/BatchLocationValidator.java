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
 * Date: May 1, 2007
 * Time: 4:25:38 PM
 */

package org.archiviststoolkit.dialog;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.ValidationUtils;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.model.validators.ATAbstractValidator;
import org.archiviststoolkit.model.Locations;
import org.archiviststoolkit.structure.ATFieldInfo;

public class BatchLocationValidator extends ATAbstractValidator {

	private BatchLocationCreation dialog;

	public BatchLocationValidator(BatchLocationCreation dialog) {
		this.dialog = dialog;
	}

	public ValidationResult validate() {

		ValidationResult result = new ValidationResult();

		if (!checkForMinimumRequirements())
			result.addError("Building and Coordinate1 are mandatory");

        if (dialog.getRepository() == null)
            result.addError("Repository is mandatory");

		ATFieldInfo field = ATFieldInfo.getFieldInfo(Locations.class, Locations.PROPERTYNAME_BUILDING);
		if (!ValidationUtils.hasMaximumLength(dialog.getBuilding(), field.getStringLengthLimit())) {
			result.addError(field.getFieldName() + " has a maximum length of " + field.getStringLengthLimit() + " characters");
		}

		field = ATFieldInfo.getFieldInfo(Locations.class, Locations.PROPERTYNAME_FLOOR);
		if (!ValidationUtils.hasMaximumLength(dialog.getFloor(), field.getStringLengthLimit())) {
			result.addError(field.getFieldName() + " has a maximum length of " + field.getStringLengthLimit() + " characters");
		}

		field = ATFieldInfo.getFieldInfo(Locations.class, Locations.PROPERTYNAME_ROOM);
		if (!ValidationUtils.hasMaximumLength(dialog.getRoom(), field.getStringLengthLimit())) {
			result.addError(field.getFieldName() + " has a maximum length of " + field.getStringLengthLimit() + " characters");
		}

		field = ATFieldInfo.getFieldInfo(Locations.class, Locations.PROPERTYNAME_AREA);
		if (!ValidationUtils.hasMaximumLength(dialog.getArea(), field.getStringLengthLimit())) {
			result.addError(field.getFieldName() + " has a maximum length of " + field.getStringLengthLimit() + " characters");
		}

		field = ATFieldInfo.getFieldInfo(Locations.class, Locations.PROPERTYNAME_COORDINATE_1_LABEL);
		if (!ValidationUtils.hasMaximumLength(dialog.getCoordinate1Label(), field.getStringLengthLimit())) {
			result.addError(field.getFieldName() + " has a maximum length of " + field.getStringLengthLimit() + " characters");
		}

		field = ATFieldInfo.getFieldInfo(Locations.class, Locations.PROPERTYNAME_COORDINATE_2_LABEL);
		if (!ValidationUtils.hasMaximumLength(dialog.getCoordinate2Label(), field.getStringLengthLimit())) {
			result.addError(field.getFieldName() + " has a maximum length of " + field.getStringLengthLimit() + " characters");
		}

		field = ATFieldInfo.getFieldInfo(Locations.class, Locations.PROPERTYNAME_COORDINATE_3_LABEL);
		if (!ValidationUtils.hasMaximumLength(dialog.getCoordinate3Label(), field.getStringLengthLimit())) {
			result.addError(field.getFieldName() + " has a maximum length of " + field.getStringLengthLimit() + " characters");
		}



		return result;
	}

	private boolean checkForMinimumRequirements() {
		if (ValidationUtils.isNotBlank(dialog.getBuilding()) &&
				ValidationUtils.isNotBlank(dialog.getCoordinate1Label())) {
			return true;
		} else {
			return false;
		}
	}


}
