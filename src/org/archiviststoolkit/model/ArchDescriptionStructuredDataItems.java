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
 * Date: Jun 30, 2006
 * Time: 3:30:47 PM
 */

package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.SequencedObject;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;

public abstract class ArchDescriptionStructuredDataItems extends SequencedObject {

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long archDescStructDataItemId = null;
	protected ArchDescriptionStructuredData structuredDataParent;

	/**
	 * @return Returns the identifier.
	 */
	public Long getIdentifier() {
		return this.getArchDescStructDataItemId();
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(Long identifier) {
		this.setArchDescStructDataItemId(identifier);
	}

	public Long getArchDescStructDataItemId() {
		return archDescStructDataItemId;
	}

	public void setArchDescStructDataItemId(Long archDescStructDataItemId) {
		this.archDescStructDataItemId = archDescStructDataItemId;
	}

	public ArchDescriptionStructuredData getStructuredDataParent() {
		return structuredDataParent;
	}

	public void setStructuredDataParent(ArchDescriptionStructuredData structuredData) {
		this.structuredDataParent = structuredData;
	}
}
