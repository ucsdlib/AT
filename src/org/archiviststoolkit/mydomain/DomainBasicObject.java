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

import org.hibernate.classic.Lifecycle;
import org.hibernate.Session;

import java.io.Serializable;


/**
 * The DomainObject should be the base class of all data types which
 * we wish to store in our hibernate based database.
 * TODO: Integer should be a Long identifier within hibernate
 */

public abstract class DomainBasicObject implements Lifecycle, Comparable {

	/**
	 * The identifier of the object.
	 */
	private Long identifier = null;

	/**
	 * Hibernate Version tracker.
	 */
	private Long version = new Long(0);


	/**
	 * @return Returns the identifier.
	 */
	public Long getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(Long identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return Returns the version.
	 */
	public final Long getVersion() {
		return version;
	}

	/**
	 * @param version The version to set.
	 */
	public final void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * Tests objects for equality.
	 *
	 * @param object the object to test against
	 * @return true if the object equals this object
	 */

	public final boolean equals(final Object object) {

		if (object == null) {
			return (false);
		}

		if (this == object) {
			return true;
		}

		if (this.getClass() == object.getClass()) {
			if (this.getIdentifier() != null && ((DomainBasicObject) object).getIdentifier() != null
					&& (this.getIdentifier().equals(((DomainBasicObject) object).getIdentifier()))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the hashcode for this object.
	 *
	 * @return the hashcode
	 */

	public final int hashCode() {
		if (identifier == null) {
			return (-1);
		}

		return (identifier.intValue());
	}

	/**
	 * Called when an entity is deleted.
	 *
	 * @param session the session to delete on
	 * @return true to veto delete
	 */
	public final boolean onDelete(final Session session) {
		return (false);
	}

	/**
	 * Called after an entity is loaded.
	 *
	 * @param session  the session which loaded the entity
	 * @param serialid the identifier of the entity
	 */

	public void onLoad(final Session session, final Serializable serialid) {

	}

	/**
	 * Called when an entity is saved.
	 *
	 * @param session the session to save on
	 * @return true to veto save
	 */
	public final boolean onSave(final Session session) {
//		GregorianCalendar calendar = new GregorianCalendar();
//
//		this.setCreationdate(calendar);
//		this.setModifieddate(calendar);
//		this.setCreatedBy(ApplicationFrame.currentUser);
//		this.setModifiedBy(ApplicationFrame.currentUser);

		return (false);
	}

	/**
	 * Called when an entity is updated.
	 *
	 * @param session the session to update on
	 * @return true to veto update
	 */
	public final boolean onUpdate(final Session session) {
//		GregorianCalendar calendar = new GregorianCalendar();
//
//		this.setModifieddate(calendar);
//		this.setModifiedBy(ApplicationFrame.currentUser);
//
		return (false);

	}

	/**
	 * Compares this object to another.
	 *
	 * @param object the object to compare this to.
	 * @return a integer result of the comparison.
	 */
	public int compareTo(Object object) {
		if (this.getClass() != object.getClass()) {
			throw (new ClassCastException("Cannot compare unlike objects"));
		}

		return (toString().compareToIgnoreCase(object.toString()));
	}

}