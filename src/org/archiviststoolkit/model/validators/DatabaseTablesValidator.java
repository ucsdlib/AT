package org.archiviststoolkit.model.validators;

/**
 * Created by IntelliJ IDEA.
 * User: Nathan Stevens
 * Date: Jul 17, 2008
 * Time: 4:08:14 PM
 * To change this template use File | Settings | File Templates.
 */
import org.archiviststoolkit.structure.DefaultValues;
import org.archiviststoolkit.structure.DatabaseTables;
import org.archiviststoolkit.structure.DatabaseFields;
import org.archiviststoolkit.util.ATPropertyValidationSupport;
import com.jgoodies.validation.ValidationResult;

import java.util.Set;

public class DatabaseTablesValidator extends ATAbstractValidator {
  // Instance Creation ******************************************************

	/**
	 * Constructs an validator
	 *
	 * @param defaultValue the accession to be validated
	 */
	public DatabaseTablesValidator(DefaultValues defaultValue) {
		this.objectToValidate = defaultValue;
	}

	public DatabaseTablesValidator() {
	}

	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the accession validation
	 */
	public ValidationResult validate() {
		DatabaseTables modelToValidate = (DatabaseTables)objectToValidate;

		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "Database Tables");

		Set<DatabaseFields> fields = modelToValidate.getDatabaseFields();
    
    int total = 0;
    for (DatabaseFields field: fields) {
			total += field.getReturnScreenOrder();
    }

    if(total == 0) {
      support.addError(" Return screen order", "must be 1 or a positive\nnumber for at least one of the records");
    }

		return support.getResult();
	}
}
