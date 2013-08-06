package org.archiviststoolkit.importer;

import org.archiviststoolkit.util.StringHelper;

//==============================================================================
// Import Declarations
//==============================================================================

/**
 * Returned when we couldnt find a domain instance.
 */

public class ImportException extends Exception {

    private String problemData;

	/**
	 * Constructor for wrapping internal exceptions.
	 *
	 * @param message string for human consumption
	 * @param cause   the original exception which caused this one
	 */

	public ImportException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor for wrapping internal exceptions.
	 *
	 * @param message string for human consumption
	 * @param cause   the original exception which caused this one
	 */

	public ImportException(final String message, final String problemData, final Throwable cause) {
		super(message, cause);
		this.problemData = problemData;
	}

	/**
	 * Constructor for wrapping internal exceptions.
	 *
	 * @param message string for human consumption
	 * @param cause   the original exception which caused this one
	 */

	public ImportException(final String message, final String problemData) {
		super(message);
		this.problemData = problemData;
	}

     /**
     * Constructor for construction of a new persistence exception.
     *
     * @param message string for human consumption
     */

    public ImportException(final String message) {
        super(message);
    }

    public String getErrorText() {
        if(problemData == null) {
           return StringHelper.getStackTrace(this);
        } else {
            return "Problem Data: " + problemData + "\n" + StringHelper.getStackTrace(this);            
        }
    }
}