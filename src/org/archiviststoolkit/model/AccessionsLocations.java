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

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;

import java.io.Serializable;

@ExcludeFromDefaultValues
public class AccessionsLocations  extends DomainObject implements Serializable, Comparable {

	public static final String PROPERTYNAME_ACCESSION_NUMBER    = "accessionNumber";
	public static final String PROPERTYNAME_ACCESSION_TITLE    = "accessionTitle";
	public static final String PROPERTYNAME_LOCATION    = "location";
	public static final String PROPERTYNAME_NOTE    = "note";

	private Long accessionsLocationsId = null;

	@IncludeInApplicationConfiguration(1)
	private Locations location;

	@IncludeInApplicationConfiguration
	private Accessions accession;

	@IncludeInApplicationConfiguration(2)
	private String note ="";

	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public AccessionsLocations() {}

	/**
	 * Full constructor;
	 */
	public AccessionsLocations(Locations location, Accessions accession) {
		this.setLocation(location);
		this.setAccession(accession);
	}

	// ********************** Accessor Methods ********************** //


	public Long getIdentifier() {
		return this.getAccessionsLocationsId();
	}

	public void setIdentifier(Long identifier) {
		this.setAccessionsLocationsId(identifier);
	}

	public Accessions getAccession() {
		return accession;
	}

	public void setAccession(Accessions accession) {
		this.accession = accession;
	}

	public Long getAccessionsLocationsId() {
		return accessionsLocationsId;
	}

	public void setAccessionsLocationsId(Long accessionsLocationsId) {
		this.accessionsLocationsId = accessionsLocationsId;
	}

	public Locations getLocation() {
		return location;
	}

	public void setLocation(Locations location) {
		Object oldValue = getLocation();
		this.location = location;
		firePropertyChange(PROPERTYNAME_LOCATION, oldValue, location);
	}

	public String getAccessionNumber() {
		return this.getAccession().getAccessionNumber();
	}

	public String getAccessionTitle() {
		return this.getAccession().getTitle();
	}


	public String getNote() {
		if (this.note != null) {
			return this.note;
		} else {
			return "";
		}
	}

	public void setNote(String note) {
		this.note = note;
	}
}
