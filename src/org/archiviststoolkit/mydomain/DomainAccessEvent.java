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
 */


package org.archiviststoolkit.mydomain;

//==============================================================================
// Import Declarations
//==============================================================================

import java.util.EventObject;

/**
 * The domain access event is sent when a domain object has been modified.
 */

public class DomainAccessEvent extends EventObject {

	/**
	 * The insert event.
	 */
	public static final int INSERT = 1;

	/**
	 * The update event.
	 */
	public static final int UPDATE = 2;

	/**
	 * The remove event.
	 */
	public static final int REMOVE = 3;

	/**
	 * The remove group event.
	 */
	public static final int REMOVEGROUP = 4;

	/**
	 * The insert group event.
	 */
	public static final int INSERTGROUP = 5;

	/**
	 * The current action being executed.
	 */
	private int action = 0;

	/**
	 * Constructs a new DomainAccessEvent.
	 *
	 * @param action the action being performed.
	 * @param source the DomainAccessObject which generated this event
	 */

	public DomainAccessEvent(final int action, final Object source) {
		super(source);
		this.action = action;
	}

	/**
	 * Get the action associated with this event.
	 *
	 * @return the action
	 */
	public final int getAction() {
		return (this.action);
	}
}

//=============================================================================
// $Log: DomainAccessEvent.java,v $
// Revision 1.4  2004/12/23 23:36:32  lejames
// Reworked the model to contain a full list of fields
//
// Revision 1.3  2004/12/20 00:24:29  lejames
// Added the DomainImportController and used it to manage the import of external data.
// We can now do duplicate, merge and update import operations.
//
// Revision 1.2  2004/12/19 02:47:40  lejames
// Added Sortable Tables, and group delete to all the domain worksurfaces.
//
// Revision 1.1  2004/11/26 17:51:32  lejames
// Completion of roadmap to 1.1.0 including additional maven report plugins,
// hibernate and hsql integration, DOA layer and basic contact management
//
//
//
//=============================================================================
// EOF
//=============================================================================
