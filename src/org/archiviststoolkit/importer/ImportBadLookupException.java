package org.archiviststoolkit.importer;

/**
 * Created by IntelliJ IDEA.
 * User: leemandell
 * Date: Feb 9, 2006
 * Time: 11:41:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImportBadLookupException  extends Exception{
	public ImportBadLookupException() {
	}

	public ImportBadLookupException(String message) {
		super(message);
	}

	public ImportBadLookupException(String message, Throwable cause) {
		super(message, cause);
	}

	public ImportBadLookupException(Throwable cause) {
		super(cause);
	}

}
