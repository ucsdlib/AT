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
 */


package org.archiviststoolkit.mydomain;

//==============================================================================
// Import Declarations
//==============================================================================

import java.util.EventListener;


/**
 * Is implemented by a class who wants to understand when a domain has changed.
 */

public interface DomainAccessListener extends EventListener {

	/**
	 * Called to notify the listener that an event has occurred.
	 *
	 * @param event denotes that a domainobject has changed.
	 */

	void domainChanged(DomainAccessEvent event);
}