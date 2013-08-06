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
//Import Declarations
//==============================================================================

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The model used to display lists of domain objects inside a JTable.
 */

public class DomainRelatedTableModel extends DomainTableModel implements DomainAccessListener {

	/**
	 * Default constructor.
	 *
	 * @param clazz	   the class to use
	 * @param columnModel the column model used to display
	 */

	public DomainRelatedTableModel(final Class clazz, final ArrayList columnModel) {
		super();
		this.columnModel = columnModel;
		this.clazz = clazz;
		domainCollection = new ArrayList();
	}
}