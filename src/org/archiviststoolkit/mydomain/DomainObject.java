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

import java.util.Collection;
import java.util.EventObject;
import java.util.Date;

import org.hibernate.classic.Lifecycle;
import org.hibernate.Session;
import org.archiviststoolkit.model.validators.ATValidator;
import org.archiviststoolkit.model.validators.ValidatorFactory;
import org.archiviststoolkit.model.ResourcesComponents;
import org.archiviststoolkit.util.JGoodiesValidationUtils;
import org.archiviststoolkit.exceptions.AddRelatedObjectException;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.DeleteException;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.DefaultIncludeInSearchEditor;
import org.archiviststoolkit.structure.IncludeMethodInApplicationConfiguration;

import java.io.Serializable;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.validation.ValidationResult;


/**
 * The DomainObject should be the base class of all data types which
 * we wish to store in our hibernate based database.
 * TODO: Integer should be a Long identifier within hibernate
 */

public abstract class DomainObject extends Model implements Lifecycle, Comparable, Auditable {

	public static final String CREATED_BY ="createdBy";
	public static final String CREATED ="created";
	public static final String LAST_UPDATED ="lastUpdated";
	public static final String LAST_UPDATED_BY ="lastUpdatedBy";

	private boolean debug = false;

	/**
	 * Hibernate Version tracker.
	 */
	@ExcludeFromDefaultValues
	private Long version = new Long(0);

	private AuditInfo auditInfo;

	protected DomainObject() {
	}

	/**
	 * @return Returns the creationdate.
	 */
	public final Date creationDate() {
		return this.getAuditInfo().getCreated();
	}


	/**
	 * @return Returns the identifier.
	 */
	public abstract Long getIdentifier();

	/**
	 * @param identifier The identifier to set.
	 */
	public abstract void setIdentifier(Long identifier);

	/**
	 * @return Returns the modifieddate.
	 */
	public Date lastUpdated() {
		return this.getAuditInfo().getLastUpdated();
	}


	/**
	 * @return Returns the version.
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * @param version The version to set.
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	public Boolean isNewRecord() {
		if (this.getIdentifier() == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Tests objects for equality.
	 *
	 * @param object the object to test against
	 * @return true if the object equals this object
	 */

	public boolean equals(final Object object) {

		if (debug) {
			if (object instanceof ResourcesComponents) {
				System.out.println("Equals from Domain Object this: " + this + " To: " + object);
			}
		}
		if (object == null) {
			return (false);
		}

		if (this == object) {
			return true;
		}

		if (this.getClass() == object.getClass()) {
			if (this.getIdentifier() != null && ((DomainObject) object).getIdentifier() != null
					&& (this.getIdentifier().equals(((DomainObject) object).getIdentifier()))) {
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

	public int hashCode() {
		if (this.getIdentifier() == null) {
			return (-1);
		}

		return (this.getIdentifier().intValue());
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
	public boolean onSave(final Session session) {

		return (false);
	}

	/**
	 * Called when an entity is updated.
	 *
	 * @param session the session to update on
	 * @return true to veto update
	 */
	public boolean onUpdate(final Session session) {
//		GregorianCalendar calendar = new GregorianCalendar();
//
//		this.setModifieddate(calendar);
//		this.setModifiedBy(ApplicationFrame.currentUser);

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
//		if (debug) {
//			if (object instanceof ResourcesComponents) {
//				System.out.println("Compare domain object: " + this + " To: " + object);
//			}
//		}

		return (toString().compareToIgnoreCase(object.toString()));
	}

	/**
	 * Instances must always return a non-null instance of AuditInfo
	 */
	public AuditInfo getAuditInfo() {
		if (auditInfo == null) auditInfo = new AuditInfo();
		return this.auditInfo;
	}

	public void setAuditInfo(AuditInfo auditInfo) {
		this.auditInfo = auditInfo;
	}

	@DefaultIncludeInSearchEditor
	@IncludeMethodInApplicationConfiguration(fieldName = CREATED_BY, fieldType = "java.lang.String")
	@ExcludeFromDefaultValues
	public String getCreatedBy() {
		return this.getAuditInfo().getCreatedBy();
	}

	@DefaultIncludeInSearchEditor
	@IncludeMethodInApplicationConfiguration(fieldName = LAST_UPDATED_BY, fieldType = "java.lang.String")
	@ExcludeFromDefaultValues
	public String getLastUpdatedBy() {
		return this.getAuditInfo().getLastUpdatedBy();
	}

	@DefaultIncludeInSearchEditor
	@IncludeMethodInApplicationConfiguration(fieldName = CREATED, fieldType = "java.util.Date")
	@ExcludeFromDefaultValues
	public Date getCreated() {
		return this.getAuditInfo().getCreated();
	}

	@DefaultIncludeInSearchEditor
	@IncludeMethodInApplicationConfiguration(fieldName = LAST_UPDATED, fieldType = "java.util.Date")
	@ExcludeFromDefaultValues
	public Date getLastUpdated() {
		return this.getAuditInfo().getLastUpdated();
	}

//    private ExtendedPropertyChangeSupport changeSupport = new ExtendedPropertyChangeSupport(this);
//
//
//    public void addPropertyChangeListener(PropertyChangeListener x) {
//        changeSupport.addPropertyChangeListener(x);
//    }
//
//    public void removePropertyChangeListener(PropertyChangeListener x) {
//        changeSupport.removePropertyChangeListener(x);
//    }
//

	/**
	 * A dummy place holder to be overridden by classes that have related objects
	 *
	 * @param domainObject the domain object to be removed
	 */
	public void removeRelatedObject(DomainObject domainObject) throws ObjectNotRemovedException {
	}

	/**
	 * A dummy place holder to be overridden by classes that have related objects
	 *
	 * @param domainObject the domain object to be removed
	 */
	public void addRelatedObject(DomainObject domainObject) throws AddRelatedObjectException, DuplicateLinkException {
	}

//	/**
//	 * A dummy place holder to be overridden by classes that have related objects
//	 * @param clazz the domain object to be removed
//	 */
//	public Collection getRelatedCollection(Class clazz) {
//		return null;
//	}

	/**
	 * A dummy place holder to be overridden by classes that have related objects
	 *
	 * @param domainObject the domain object to be removed
	 */
	public Collection getRelatedCollection(DomainObject domainObject) {
		return null;
	}

	/**
	 * A dummy place holder to be overridden by classes that have related objects
	 *
	 * @param rowNumber the row of the object to retrieve
	 */
	public DomainObject getRelatedObject(DomainObject domainObject, int rowNumber) {
		return null;
	}

//	/**
//	* A dummy place holder to be overridden by classes that have related objects
//	* @param rowNumber the row of the object to retrieve
//	*/
//   public DomainObject getRelatedObject(Class clazz, int rowNumber) {
//	   return null;
//   }

	/**
	 * A dummy place holder to be overridden by classes that have unique keys
	 * other than the id
	 */
	public String getUniqueConstraintKey() {
		return this.toString();
	}

	public void removeIdAndAuditInfo() {
		this.setIdentifier(null);
		if (this.auditInfo != null) {
			this.auditInfo.removeAuditInfo();
		}
	}

	public boolean validateAndDisplayDialog(EventObject ae) {

		ATValidator validator = ValidatorFactory.getInstance().getValidator(this);
		if (validator == null) {
			//nothing registered so just return true
			return true;
		} else {
			ValidationResult validationResult = validator.validate();
			if (validationResult.hasErrors()) {
				JGoodiesValidationUtils.showValidationMessage(
						ae,
						"To save the record, please fix the following errors:",
						validationResult);
				return false;
			}
			if (validationResult.hasWarnings()) {
				JGoodiesValidationUtils.showValidationMessage(
						ae,
						"Note: some fields are invalid.",
						validationResult);
			}
			return true;
		}
	}

	//A place holder to determine if an object is safe to delete. To be
	//overridden if logic is necessary.
	public void testDeleteRules() throws DeleteException, PersistenceException {
	}

}
