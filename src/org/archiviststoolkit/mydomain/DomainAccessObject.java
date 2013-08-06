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

import org.archiviststoolkit.dialog.QueryEditor;
import org.archiviststoolkit.exceptions.ValidationException;
import org.archiviststoolkit.exceptions.DeleteException;
import org.archiviststoolkit.exceptions.MergeException;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.hibernate.Session;
import org.hibernate.LockMode;

import java.awt.Component;
import java.util.Collection;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * Implemented by all DomainAccess patterned classes.
 */

public interface DomainAccessObject {

	/**
	 * Add a listener to this domain access object.
	 *
	 * @param listener the listener to add
	 */

	void addListener(DomainAccessListener listener);

	/**
	 * Remove a listener from this domain access object.
	 *
	 * @param listener the listener to remove
	 */

	void removeListener(DomainAccessListener listener);

	/**
	 * Add a new instance to this domain access object.
	 *
	 * @param obj the object to add
	 * @throws PersistenceException we could not persist this object
	 */

	void add(DomainObject obj) throws PersistenceException;

	/**
	 * Allows the mass deletion of domain objects.
	 *
	 * @param collection the collection to delete
	 * @throws PersistenceException we could not persist the changes.
	 */
	void addGroup(Collection collection, Component parent) throws PersistenceException;

	/**
	 * Update an instance within this domain access object.
	 *
	 * @param obj the object to update
	 * @throws PersistenceException fails if we could not persist the changes
	 */

	void update(DomainObject obj) throws PersistenceException;

	/**
	 * Update an instance within this domain access object.
	 *
	 * @param obj the object to update
	 * @throws PersistenceException fails if we could not persist the changes
	 */

	void updateLongSession(DomainObject obj) throws PersistenceException;

	/**
	 * Update an instance within this domain access object.
	 *
	 * @param obj the object to update
	 * @throws PersistenceException fails if we could not persist the changes
	 */

	void updateLongSession(DomainObject obj, boolean closeSession) throws PersistenceException;

	/**
	 * How does this differ from delete group.
	 *
	 * @param collection the collection to delete.
	 * @throws PersistenceException we could not persist the changes.
	 * @throws LookupException	  we could not lookup the reference instances.
	 */

	void deleteGroup(Collection collection) throws PersistenceException, LookupException;

	/**
	 * Remove an instance from the control of the domain access object.
	 *
	 * @param obj the object to remove
	 * @throws PersistenceException fails if we could not persist the removal
	 */

	void delete(DomainObject obj) throws PersistenceException, DeleteException;

	void deleteLongSession(DomainObject obj) throws PersistenceException, DeleteException;
	void deleteLongSession(DomainObject obj, boolean closeSession) throws PersistenceException, DeleteException;

	/**
	 * Find an object instance by its identifier.
	 *
	 * @param id the identifier with which to find it
	 * @return the object
	 * @throws LookupException if we failed to execute the lookup
	 */

	DomainObject findByPrimaryKey(Long id) throws LookupException;

	/**
	 * Find an object instance by its identifier.
	 *
	 * @param id the identifier with which to find it
	 * @return the object
	 * @throws LookupException if we failed to execute the lookup
	 */

	DomainObject findByPrimaryKeyLongSession(Long id) throws LookupException;

	/**
	 * Find an object instance by its identifier.
	 *
	 * @param id the identifier with which to find it
	 * @return the object
	 * @throws LookupException if we failed to execute the lookup
	 */

	DomainObject findByPrimaryKeyLongSessionForPrinting(Long id) throws LookupException;

	void closeLongSession() throws SQLException;

	void closeLongSessionRollback() throws SQLException;

	Session getLongSession();
//	/**
//	 * Get a collection of all objects under the control of this domain access object.
//	 * @return the collection of all instances
//	 * @throws LookupException if we failed to execute the lookup
//	 */
//
//	Collection findAll() throws LookupException;


	/**
	 * Get a collection of all objects under the control of this domain access object.
	 *
	 * @return the collection of all instances
	 * @throws LookupException if we failed to execute the lookup
	 */
	//todo make these collections typed
	Collection findAll(String... sortField) throws LookupException;

	/**
	 * Get a collection of all objects under the control of this domain access object.
	 *
	 * @return the collection of all instances
	 * @throws LookupException if we failed to execute the lookup
	 */

	Collection findAll(LockMode lockmode, String... sortField) throws LookupException;

	/**
	 * Get a collection of all objects under the control of this domain access object.
	 *
	 * @return the collection of all instances
	 * @throws LookupException if we failed to execute the lookup
	 */

	Collection findAllLongSession(String... sortField) throws LookupException;

	Collection findAllLongSession(Boolean alwaysApplyFilters, String... sortField) throws LookupException;

	/**
	 * Find a collection of objects by a query string.
	 *
	 * @param queryString the hql.
	 * @return a collection of domain objects.
	 * @throws LookupException we couldnt find what you were looking for.
	 */
	Collection findByQuery(String queryString) throws LookupException;

	/**
	 * Find a collection of objects by a query string.
	 *
	 * @param editor an AT query editor.
	 * @return a collection of domain objects.
	 * @throws LookupException we couldnt find what you were looking for.
	 */
	Collection findByQueryEditor(QueryEditor editor, InfiniteProgressPanel progressPanel) throws LookupException;

    /**
	 * Find a collection of objects by a query string.
	 *
	 * @param editor an AT query editor.
	 * @return a collection of domain objects.
	 * @throws LookupException we couldnt find what you were looking for.
	 */
	Collection findByQueryEditorLongSession(QueryEditor editor, InfiniteProgressPanel progressPanel) throws LookupException;

	/**
	 * Find instances through a named query with parameters.
	 *
	 * @param namedQuery	 the query to use
	 * @param propertyObject the bean to use to provide properties to the query
	 * @return the collection of instances
	 * @throws LookupException if we failed to execute the query
	 */

	Collection findByNamedQuery(String namedQuery, Object propertyObject) throws LookupException;

	/**
	 * Find instances through a named query without parameters.
	 *
	 * @param namedQuery the query to use
	 * @return the collection of instances
	 * @throws LookupException if we failed to execute the query
	 */

	Collection findByNamedQuery(String namedQuery) throws LookupException;

	/**
	 * Find instances through a named query without parameters.
	 *
	 * @param instance the instance to use as an example
	 * @return the collection of instances
	 * @throws LookupException if we failed to execute the query
	 */

	Collection findByExample(Object instance) throws LookupException;

	/**
	 * Find instances through a named query without parameters.
	 *
	 * @param propertyName the name of the property
	 * @param value		the value of the property
	 * @return the collection of instances
	 * @throws LookupException if we failed to execute the query
	 */

	Collection findByPropertyValue(String propertyName, Object value) throws LookupException;

	/**
	 * Find instances through a named query without parameters.
	 *
	 * @param propertyName the name of the property
	 * @param values		the value of the property
	 * @return the collection of instances
	 * @throws LookupException if we failed to execute the query
	 */

	Collection findByPropertyValues(String propertyName, Object[] values) throws LookupException;

	/**
	 * Find instances through a named query without parameters.
	 *
	 * @param propertyName the name of the property
	 * @param values		the value of the property
	 * @return the collection of instances
	 * @throws LookupException if we failed to execute the query
	 */

	Collection findByPropertyValuesLongSession(String propertyName, Object[] values) throws LookupException;

	/**
	 * Find instances through a named query without parameters.
	 *
	 * @param propertyName the name of the property
	 * @param value		the value of the property
	 * @return the collection of instances
	 * @throws LookupException if we failed to execute the query
	 */

	DomainObject findByUniquePropertyValue(String propertyName, Object value) throws LookupException;

	/**
	 * Find instances through a named query without parameters.
	 *
	 * @param propertyName the name of the property
	 * @param value		the value of the property
	 * @return the collection of instances
	 * @throws LookupException if we failed to execute the query
	 */

	DomainObject findByUniquePropertyValueLongSession(String propertyName, Object value) throws LookupException;

	/**
	 * Find instances through a named query without parameters.
	 *
	 * @param propertyName the name of the property
	 * @param oldValue	 the old value of the property
	 * @param newValue	 the new value of the property
	 * @return the collection of instances
	 * @throws LookupException if we failed to execute the query
	 */

	int updateTextField(String propertyName, String oldValue, String newValue, InfiniteProgressPanel progressPanel) throws LookupException, IllegalAccessException, InvocationTargetException, ValidationException;

	Integer getCountBasedOnPropertyValue(String propertyName, Object value);

	int merge(Collection<DomainObject> mergeFrom, DomainObject mergeTo, InfiniteProgressPanel progressPanel) throws MergeException;

	String getHumanReadableSearchString();
}

//=============================================================================
// $Log: DomainAccessObject.java,v $
// Revision 1.5  2004/12/23 23:36:32  lejames
// Reworked the model to contain a full list of fields
//
// Revision 1.4  2004/12/20 00:24:29  lejames
// Added the DomainImportController and used it to manage the import of external data.
// We can now do duplicate, merge and update import operations.
//
// Revision 1.3  2004/12/18 03:22:41  lejames
// First cut of the new import code which can handle comma separated values.
//
// Revision 1.2  2004/12/16 23:08:11  lejames
// Added Account DetailView that now works
//
// Revision 1.1  2004/11/26 17:51:33  lejames
// Completion of roadmap to 1.1.0 including additional maven report plugins,
// hibernate and hsql integration, DOA layer and basic contact management
//
//
//
//=============================================================================
// EOF
//=============================================================================
