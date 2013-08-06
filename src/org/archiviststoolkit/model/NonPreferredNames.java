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
 */


package org.archiviststoolkit.model;

import org.archiviststoolkit.structure.ExcludeFromDefaultValues;

//==============================================================================
// Import Declarations
//==============================================================================

/**
 * The Names class represents a name authority record.
 */

@ExcludeFromDefaultValues
public class NonPreferredNames extends BasicNames {

	private Long nonPreferredNameId;
	private Names primaryName;


	/**
	 * No-arg constructor for JavaBean tools.
	 */
	NonPreferredNames() {}
	/**
	 * Full constructor;
	 */

	public NonPreferredNames(Names parentName) {
		this.primaryName = parentName;
	}


	public Names getPrimaryName() { return primaryName; }

	public Long getIdentifier() {
		return this.getNonPreferredNameId();
	}

	public void setIdentifier(Long identifier) {
		this.setNonPreferredNameId(identifier);
	}

	public Long getNonPreferredNameId() {
		return nonPreferredNameId;
	}

	public void setNonPreferredNameId(Long nonPreferredNameId) {
		this.nonPreferredNameId = nonPreferredNameId;
	}
}
    
