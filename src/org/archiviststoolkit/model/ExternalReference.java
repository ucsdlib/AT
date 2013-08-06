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

import org.archiviststoolkit.structure.NotesEtcTypes;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.StringLengthValidationRequried;

@ExcludeFromDefaultValues
public class ExternalReference extends ArchDescriptionRepeatingData {

	public static final String PROPERTYNAME_HREF = "href";
	public static final String PROPERTYNAME_ACTUATE = "actuate";
	public static final String PROPERTYNAME_SHOW = "show";

	@IncludeInApplicationConfiguration(1)
	private String href = "";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String actuate = "";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String show = "";


	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public ExternalReference() {
		super(NotesEtcTypes.DATA_TYPE_EXTERNAL_REFERENCE);
	}

	/**
	 * Full constructor;
	 * @param archDescription - the parent record to link to
	 */

	public ExternalReference(ArchDescription archDescription) {
		super(archDescription, NotesEtcTypes.DATA_TYPE_EXTERNAL_REFERENCE);
	}

	public String getContent() {
		return this.getHref();
	}

	public String getType() {
		return NotesEtcTypes.DATA_TYPE_EXTERNAL_REFERENCE;
	}

	public String getFullDescription() {
		return getType() + ": " + getContent();
	}

	// ********************** Accessor Methods ********************** //

	public String getHref() {
		if (this.href != null) {
			return this.href;
		} else {
			return "";
		}
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getActuate() {
		if (this.actuate != null) {
			return this.actuate;
		} else {
			return "";
		}
	}

	public void setActuate(String actuate) {
		this.actuate = actuate;
	}

	public String getShow() {
		if (this.show != null) {
			return this.show;
		} else {
			return "";
		}
	}

	public void setShow(String show) {
		this.show = show;
	}
}
