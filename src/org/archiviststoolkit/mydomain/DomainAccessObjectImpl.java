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
 */

package org.archiviststoolkit.mydomain;

//==============================================================================
// Import Declarations
//==============================================================================

import com.jgoodies.validation.ValidationResult;
import org.apache.commons.beanutils.BeanUtils;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.dialog.QueryEditor;
import org.archiviststoolkit.exceptions.DeleteException;
import org.archiviststoolkit.exceptions.MergeException;
import org.archiviststoolkit.exceptions.ValidationException;
import org.archiviststoolkit.hibernate.ATSearchCriterion;
import org.archiviststoolkit.hibernate.AuditInterceptor;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.model.validators.ATValidator;
import org.archiviststoolkit.model.validators.ValidatorFactory;
import org.archiviststoolkit.structure.DatabaseTables;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.util.NameUtils;
import org.archiviststoolkit.util.StringHelper;
import org.hibernate.*;
import org.hibernate.criterion.*;

import javax.swing.*;
import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Default implementation of a DomainAccessObject specific to
 * hibernate version 2.
 */

public class DomainAccessObjectImpl implements DomainAccessObject, DomainAccessListener {

    private boolean debug = false;

    /**
     * Class logger.
     */

    private static Logger logger = Logger.getLogger(DomainAccessObjectImpl.class.getPackage().getName());


    /**
     * Class which this DOA implementation refers to.
     */
    private Class persistentClass = null;

    /**
     * listeners who are interested in this class changing.
     * TODO: need to change this to a collection type not vector
     */
    private Vector<DomainAccessListener> listeners = new Vector<DomainAccessListener>();

    private static Session longSession = null; // This needs to be static to prevent those can't save messages as in ART-1680

    private String humanReadableSearchString = "";

    /**
     * Constructor which builds a DAO for this class.
     *
     * @param clazz the class to build to
     */

    public DomainAccessObjectImpl(final Class clazz) {
        this.persistentClass = clazz;
    }

    /**
     * Add a listener for this DAO.
     *
     * @param listener the listener to add
     */

    public final void addListener(final DomainAccessListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove a listener from this DAO.
     *
     * @param listener the listener to remove
     */

    public final void removeListener(final DomainAccessListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notify all the listeners of an event.
     *
     * @param event the event which should be sent to all listeners
     */

    public final void notifyListeners(final DomainAccessEvent event) {

        for (Object listener1 : listeners) {
            DomainAccessListener listener = (DomainAccessListener) listener1;
            listener.domainChanged(event);
        }
    }

    /**
     * Add an instance to this DAO.
     *
     * @param domainObject the object to add
     * @throws PersistenceException fails if we cannot persist the instance
     */

    public final void add(final DomainObject domainObject) throws PersistenceException {
        Session session = SessionFactory.getInstance().openSession(new AuditInterceptor(ApplicationFrame.getInstance().getCurrentUser()));
        Transaction tx = null;
        try {

            updateClassSpecific(domainObject, session);
            tx = session.beginTransaction();
            session.saveOrUpdate(domainObject);
            tx.commit();
//			session.flush();
//			session.connection().commit();

        } catch (HibernateException hibernateException) {
            try {
                tx.rollback();
            } catch (HibernateException e) {
                //todo log error
            }
            throw new PersistenceException("failed to add, class: " + this.getClass() + " object: " + domainObject, hibernateException);
        } catch (Exception sqlException) {
            try {
                tx.rollback();
            } catch (HibernateException e) {
                //todo log error
            }
            throw new PersistenceException("failed to add, class: " + this.getClass() + " object: " + domainObject, sqlException);
        } finally {
            session.close();
        }

//		SessionFactory.getInstance().closeSession(session);
        this.notifyListeners(new DomainAccessEvent(DomainAccessEvent.INSERT, domainObject));
    }

    /**
     * Add a group of instances.
     *
     * @param collection the objects to add
     * @throws PersistenceException fails if we cannot persist the instance
     */

    public final void addGroup(final Collection collection, Component parent) throws PersistenceException {
        Session session = SessionFactory.getInstance().openSession(new AuditInterceptor(ApplicationFrame.getInstance().getCurrentUser()));
        DomainObject domainObject = null;
        try {
            Iterator iterator = collection.iterator();
            int numberOfRecords = collection.size();

            ProgressMonitor monitor = new ProgressMonitor(parent, "Saving Records", null, 0, numberOfRecords);
            int count = 0;

            while (iterator.hasNext()) {
                domainObject = (DomainObject) iterator.next();
                updateClassSpecific(domainObject, session);
                session.saveOrUpdate(domainObject);
                monitor.setProgress(count++);
            }
            monitor.close();
            session.flush();
            session.connection().commit();

        } catch (HibernateException hibernateException) {
            throw new PersistenceException("failed to add, class: " + this.getClass() + " object: " + domainObject, hibernateException);
        } catch (Exception sqlException) {
            throw new PersistenceException("failed to add, class: " + this.getClass() + " object: " + domainObject, sqlException);
        }

//		HibernateUtil.closeSession();
        SessionFactory.getInstance().closeSession(session);
        this.notifyListeners(new DomainAccessEvent(DomainAccessEvent.INSERTGROUP, collection));
    }

    /**
     * Update the instance within this DAO.
     *
     * @param domainObject the instance to update
     * @throws PersistenceException fails if we cannot update the instance
     */

    public final void update(final DomainObject domainObject) throws PersistenceException {
        Session session = SessionFactory.getInstance().openSession(new AuditInterceptor(ApplicationFrame.getInstance().getCurrentUser()));
        Transaction tx = null;

        try {
            updateClassSpecific(domainObject, session);
            tx = session.beginTransaction();
            session.saveOrUpdate(domainObject);
            tx.commit();
//			session.flush();
//			session.connection().commit();
        } catch (HibernateException hibernateException) {
            try {
                tx.rollback();
            } catch (HibernateException e) {
                //todo log error
            }
            throw new PersistenceException("failed to update, class: " + this.getClass() + " object: " + domainObject, hibernateException);
        } catch (Exception sqlException) {
            try {
                tx.rollback();
            } catch (HibernateException e) {
                //todo log error
            }
            throw new PersistenceException("failed to update, class: " + this.getClass() + " object: " + domainObject, sqlException);
        } finally {
            session.close();
        }

//		SessionFactory.getInstance().closeSession(session);
        this.notifyListeners(new DomainAccessEvent(DomainAccessEvent.UPDATE, domainObject));
    }

    /**
     * Method to update a domain object with that excepts a session
     *
     * @param domainObject
     * @param session
     * @throws PersistenceException
     */
    public final void update(final DomainObject domainObject, Session session) throws PersistenceException {
        Transaction tx = null;

        try {
            updateClassSpecific(domainObject, session);
            tx = session.beginTransaction();
            session.saveOrUpdate(domainObject);
            tx.commit();
        } catch (HibernateException hibernateException) {
            try {
                tx.rollback();
            } catch (HibernateException e) {
                //todo log error
            }
            throw new PersistenceException("failed to update, class: " + this.getClass() + " object: " + domainObject, hibernateException);
        } catch (Exception sqlException) {
            try {
                tx.rollback();
            } catch (HibernateException e) {
                //todo log error
            }
            throw new PersistenceException("failed to update, class: " + this.getClass() + " object: " + domainObject, sqlException);
        }

        this.notifyListeners(new DomainAccessEvent(DomainAccessEvent.UPDATE, domainObject));
    }

    /**
     * Update the instance within this DAO.
     *
     * @param domainObject the instance to update
     * @throws PersistenceException fails if we cannot update the instance
     */

    public final void updateLongSession(final DomainObject domainObject) throws PersistenceException {
        updateLongSession(domainObject, true);
    }

    /**
     * Update the instance within this DAO.
     *
     * @param domainObject the instance to update
     * @throws PersistenceException fails if we cannot update the instance
     */

    public final void updateLongSession(final DomainObject domainObject, boolean closeSession) throws PersistenceException {

        try {
            updateClassSpecific(domainObject, longSession);
            longSession.saveOrUpdate(domainObject);
            longSession.flush();
            longSession.connection().commit();
        } catch (HibernateException hibernateException) {
            throw new PersistenceException("failed to update, class: " + this.getClass() + " object: " + domainObject, hibernateException);
        } catch (Exception sqlException) {
            throw new PersistenceException("failed to update, class: " + this.getClass() + " object: " + domainObject, sqlException);
        }

        if (closeSession) {
            SessionFactory.getInstance().closeSession(longSession);
        }
        this.notifyListeners(new DomainAccessEvent(DomainAccessEvent.UPDATE, domainObject));
    }

    /**
     * Delete these objects from the data store.
     *
     * @param collection the object to delete
     * @throws PersistenceException fails if we cannot delete the instance
     */

    public void deleteGroup(final Collection collection) throws PersistenceException {

        Session session = SessionFactory.getInstance().openSession();
        DomainObject domainObject = null;
        try {

            for (Object aCollection : collection) {
                domainObject = (DomainObject) aCollection;
                domainObject.testDeleteRules();
                domainObject = (DomainObject) session.load(getPersistentClass(), domainObject.getIdentifier());
                session.delete(domainObject);
            }

            session.flush();
            session.connection().commit();

        } catch (HibernateException hibernateException) {
            logger.severe("Failed to Delete instance " + domainObject + " because " + hibernateException);
            throw new PersistenceException("failed to delete", hibernateException);
        } catch (ClassCastException classCastException) {
            logger.severe("Failed to Delete instance " + domainObject + " because " + classCastException);
            throw new PersistenceException("failed to delete", classCastException);
        } catch (SQLException sqlException) {
            logger.severe("Failed to Delete instance " + domainObject + " because " + sqlException);
            throw new PersistenceException("failed to delete", sqlException);
        } catch (DeleteException e) {
            logger.severe("Failed to Delete instance " + domainObject + " because " + e);
            throw new PersistenceException("failed to delete", e);
        }
        SessionFactory.getInstance().closeSession(session);
        this.notifyListeners(new DomainAccessEvent(DomainAccessEvent.REMOVEGROUP, collection));
    }

    /**
     * Delete this object from the data store.
     *
     * @param domainObject the object to delete
     * @throws DeleteException fails if we cannot delete the instance
     */

    public void delete(final DomainObject domainObject) throws DeleteException {

        Session session = SessionFactory.getInstance().openSession();
        try {
            domainObject.testDeleteRules();
            session.delete(domainObject);
            session.flush();
            session.connection().commit();

        } catch (HibernateException hibernateException) {
            logger.severe("Failed to Delete instance " + domainObject + " because " + hibernateException);
            throw new DeleteException("failed to delete", hibernateException);
        } catch (ClassCastException classCastException) {
            logger.severe("Failed to Delete instance " + domainObject + " because " + classCastException);
            throw new DeleteException("failed to delete", classCastException);
        } catch (SQLException sqlException) {
            logger.severe("Failed to Delete instance " + domainObject + " because " + sqlException);
            throw new DeleteException("failed to findbyprimarykey", sqlException);
        } catch (PersistenceException e) {
            new ErrorDialog("", StringHelper.getStackTrace(e)).showDialog();
        }
        SessionFactory.getInstance().closeSession(session);
        this.notifyListeners(new DomainAccessEvent(DomainAccessEvent.REMOVE, domainObject));
    }

    public final void deleteLongSession(final DomainObject domainObject) throws DeleteException {
        deleteLongSession(domainObject, true);
    }


    public final void deleteLongSession(final DomainObject domainObject, boolean closeSession) throws DeleteException {

        try {
            domainObject.testDeleteRules();
            longSession.delete(domainObject);
            longSession.flush();
            longSession.connection().commit();

        } catch (HibernateException hibernateException) {
            logger.severe("Failed to Delete instance " + domainObject + " because " + hibernateException);
            throw new DeleteException("failed to delete", hibernateException);
        } catch (ClassCastException classCastException) {
            logger.severe("Failed to Delete instance " + domainObject + " because " + classCastException);
            throw new DeleteException("failed to delete", classCastException);
        } catch (SQLException sqlException) {
            logger.severe("Failed to Delete instance " + domainObject + " because " + sqlException);
            throw new DeleteException("failed to findbyprimarykey", sqlException);
        } catch (PersistenceException e) {
            new ErrorDialog("", StringHelper.getStackTrace(e)).showDialog();
        }
        if (closeSession) {
            SessionFactory.getInstance().closeSession(longSession);
        }
        this.notifyListeners(new DomainAccessEvent(DomainAccessEvent.REMOVE, domainObject));
    }


    /**
     * Delete this object from the data store.
     *
     * @param identifier the id of the object to delete
     * @throws DeleteException fails if we cannot delete the instance
     */

    public final void deleteById(final Long identifier) throws DeleteException {

        Session session = SessionFactory.getInstance().openSession();
        DomainObject domainObject = null;
        try {
            domainObject = findByPrimaryKeyCommon(identifier, session);
            domainObject.testDeleteRules();
            session.delete(domainObject);
            session.flush();
            session.connection().commit();

        } catch (HibernateException hibernateException) {
            throw new DeleteException("failed to delete", hibernateException);
        } catch (ClassCastException classCastException) {
            throw new DeleteException("failed to delete", classCastException);
        } catch (SQLException sqlException) {
            throw new DeleteException("failed to findbyprimarykey", sqlException);
        } catch (PersistenceException e) {
            throw new DeleteException("failed to delete", e);
        } catch (LookupException e) {
            throw new DeleteException("failed to delete", e);
        }
        SessionFactory.getInstance().closeSession(session);
        this.notifyListeners(new DomainAccessEvent(DomainAccessEvent.REMOVE, domainObject));
    }

    /**
     * Find an instance by its identifier.
     *
     * @param identifier the identifier we are looking for
     * @return the domain object with the required identifier
     * @throws LookupException fails if we cannot execute the lookup
     */

    public DomainObject findByPrimaryKeyCommon(final Long identifier, Session session) throws LookupException {

        DomainObject domainObject;

        try {

//			try {
            if (getPersistentClass() == Names.class) {
                Criteria criteria = session.createCriteria(Names.class);
                criteria.setFetchMode("contactNotes", FetchMode.JOIN);
                criteria.setFetchMode("nonPreferredNames", FetchMode.JOIN);
                criteria.setFetchMode("archDescriptionNames", FetchMode.JOIN);
                criteria.add(Restrictions.idEq(identifier));
                domainObject = (DomainObject) criteria.uniqueResult();

            } else if (getPersistentClass() == Accessions.class) {
                Criteria criteria = session.createCriteria(Accessions.class);
                criteria.setFetchMode("names", FetchMode.JOIN);
                criteria.setFetchMode("repeatingData", FetchMode.JOIN);
                criteria.setFetchMode("resources", FetchMode.JOIN);
                criteria.setFetchMode("subjects", FetchMode.JOIN);
                criteria.setFetchMode("locations", FetchMode.JOIN);
                criteria.setFetchMode("deaccessions", FetchMode.JOIN);
                criteria.add(Restrictions.idEq(identifier));
                domainObject = (DomainObject) criteria.uniqueResult();

            } else if (getPersistentClass() == Subjects.class) {
                Criteria criteria = session.createCriteria(Subjects.class);
                criteria.setFetchMode("archDescriptionSubjects", FetchMode.JOIN);
                criteria.add(Restrictions.idEq(identifier));
                domainObject = (DomainObject) criteria.uniqueResult();

            } else if (getPersistentClass() == Resources.class) {
                Criteria criteria = session.createCriteria(Resources.class);
                criteria.setFetchMode("names", FetchMode.JOIN);
                criteria.setFetchMode("accessions", FetchMode.JOIN);
                criteria.setFetchMode("subjects", FetchMode.JOIN);
//					criteria.setFetchMode("repeatingData", FetchMode.JOIN);
//					criteria.setFetchMode("instances", FetchMode.JOIN);
//					criteria.setFetchMode("resourcesComponents", FetchMode.JOIN);
                criteria.add(Restrictions.idEq(identifier));
                domainObject = (DomainObject) criteria.uniqueResult();

            } else if (getPersistentClass() == ResourcesComponents.class) {
                Criteria criteria = session.createCriteria(ResourcesComponents.class);
                criteria.setFetchMode("names", FetchMode.JOIN);
                criteria.setFetchMode("instances", FetchMode.JOIN);
                criteria.setFetchMode("subjects", FetchMode.JOIN);
                criteria.setFetchMode("repeatingData", FetchMode.JOIN);
                criteria.add(Restrictions.idEq(identifier));
                domainObject = (DomainObject) criteria.uniqueResult();

            } else if (getPersistentClass() == Locations.class) {
                Criteria criteria = session.createCriteria(Locations.class);
                criteria.setFetchMode("repository", FetchMode.JOIN);
                criteria.add(Restrictions.idEq(identifier));
                domainObject = (DomainObject) criteria.uniqueResult();

            } else if (getPersistentClass() == DigitalObjects.class) {
                Criteria criteria = session.createCriteria(DigitalObjects.class);
//					criteria.setFetchMode("names", FetchMode.JOIN);
//					criteria.setFetchMode("repeatingData", FetchMode.JOIN);
//					criteria.setFetchMode("subjects", FetchMode.JOIN);
//					criteria.setFetchMode("fileVersions", FetchMode.JOIN);
                criteria.add(Restrictions.idEq(identifier));
                domainObject = (DomainObject) criteria.uniqueResult();

            } else {
                domainObject = (DomainObject) session.load(getPersistentClass(), identifier);
            }

//			} catch (ObjectNotFoundException objectNotFoundException) {
//				new ErrorDialog("", StringHelper.getStackTrace(e)).showDialog();
//				domainObject = null;
//				logger.log(Level.INFO, "Object could not be found by key " + identifier);
//			}

            session.flush();
            session.connection().commit();

        } catch (ObjectNotFoundException objectNotFoundException) {
            throw new LookupException("failed to findbyprimarykey: " + objectNotFoundException.toString(), objectNotFoundException);
        } catch (HibernateException hibernateException) {
            throw new LookupException("failed to findbyprimarykey: " + hibernateException.toString(), hibernateException);
        } catch (SQLException sqlException) {
            throw new LookupException("failed to findbyprimarykey: " + sqlException.toString(), sqlException);
        }

        return (domainObject);
    }

    /**
     * Find an instance by its identifier.
     *
     * @param identifier the identifier we are looking for
     * @return the domain object with the required identifier
     * @throws LookupException fails if we cannot execute the lookup
     */

    public final DomainObject findByPrimaryKey(final Long identifier) throws LookupException {

        Session session = SessionFactory.getInstance().openSession();
        DomainObject domainObject = findByPrimaryKeyCommon(identifier, session);
        SessionFactory.getInstance().closeSession(session);
        return domainObject;
    }

    /**
     * Find an instance by its identifier.
     *
     * @param identifier the identifier we are looking for
     * @return the domain object with the required identifier
     * @throws LookupException fails if we cannot execute the lookup
     */

    public final DomainObject findByPrimaryKeyLongSession(final Long identifier) throws LookupException {

        openLongSession();
        DomainObject domainObject = findByPrimaryKeyCommon(identifier, longSession);
//		SessionFactory.getInstance().disconnectSession(longSession);
        return domainObject;
    }

    private void openLongSession() {
        if (longSession == null || !longSession.isOpen()) {
            longSession = SessionFactory.getInstance().openSession(new AuditInterceptor(ApplicationFrame.getInstance().getCurrentUser()));
            if (debug) {
                System.out.println("open session");
            }
        }
    }

    /**
     * Find an instance by its identifier.
     *
     * @param identifier the identifier we are looking for
     * @return the domain object with the required identifier
     * @throws LookupException fails if we cannot execute the lookup
     */

    public final DomainObject findByPrimaryKeyLongSessionForPrinting(final Long identifier) throws LookupException {

        openLongSession();
        DomainObject domainObject = (DomainObject) longSession.load(getPersistentClass(), identifier);
//		SessionFactory.getInstance().disconnectSession(longSession);
        return domainObject;
    }

    public void closeLongSession() throws SQLException {
        longSession.flush();
        longSession.connection().commit();
        SessionFactory.getInstance().closeSession(longSession);
    }

    public void closeLongSessionRollback() throws SQLException {
        if (longSession.isOpen()) { // check to see of the long session is open before trying to close it
            longSession.connection().rollback();
            SessionFactory.getInstance().closeSession(longSession);
        }
    }

    public Session getLongSession() {
        openLongSession();
        return longSession;
    }

    /**
     * Return a collection with everything in this DAO.
     *
     * @return a collection of everything of this class
     * @throws LookupException fails if we cannot execute the lookup
     */

//	public Collection findAll() throws LookupException {
//		return (findAll(null));
//	}

    /**
     * Return a collection with everything in this DAO.
     *
     * @return a collection of everything of this class
     * @throws LookupException fails if we cannot execute the lookup
     */

    public Collection findAll(String... sortFields) throws LookupException {
        return findAll(null, sortFields);
    }

    public Collection findAll(LockMode lockmode, String... sortFields) throws LookupException {
        Session session = SessionFactory.getInstance().openSession(getPersistentClass());
        List completeList = (List) findAllCommon(session, lockmode, sortFields);
//		SessionFactory.getInstance().closeSession(session);
        return completeList;
    }

    public Collection findAllLongSession(String... sortFields) throws LookupException {
        longSession = SessionFactory.getInstance().openSession(new AuditInterceptor(ApplicationFrame.getInstance().getCurrentUser()), getPersistentClass());
        return findAllCommon(longSession, sortFields);

    }

    public Collection findAllLongSession(Boolean alwaysApplyFilters, String... sortFields) throws LookupException {
        longSession = SessionFactory.getInstance().openSession(new AuditInterceptor(ApplicationFrame.getInstance().getCurrentUser()),
                getPersistentClass(), alwaysApplyFilters);
        return findAllCommon(longSession, sortFields);

    }

    private Collection findAllCommon(Session session, String... sortFields) throws LookupException {
        return findAllCommon(session, null, sortFields);
    }

    private Collection findAllCommon(Session session, LockMode lockmode, String... sortFields) throws LookupException {
        List completeList;
        Transaction tx = null;
        try {
            session.setFlushMode(FlushMode.MANUAL);

            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(getPersistentClass());
            for (String field : sortFields) {
                criteria.addOrder(Order.asc(field));
            }
//			System.out.println("Find all: " + persistentClass.getName());
//			if (lockmode != null) {
//				criteria.setLockMode(lockmode);
//				System.out.println("Setting lock mode: " + lockmode);
//			}
            completeList = criteria.list();
//			if (lockmode != null && lockmode == LockMode.READ) {
//				System.out.println("Rollback because read only");
//				session.connection().rollback();
//			} else {
//				session.flush();
//				session.connection().commit();
//			}
//			session.flush();
//			session.connection().commit();
            tx.commit();

        } catch (RuntimeException ex) {
            try {
                tx.rollback();
            } catch (HibernateException e) {
                //todo log error
            }
            throw new LookupException("failed to find all", ex);
        } finally {
            if (session != longSession) {
                session.close();
            }
        }

        return (completeList);
    }

    /**
     * Find a collection of domain objects by direct hql query.
     *
     * @param queryString the hql statement
     * @return the collection of domain objects
     * @throws LookupException if we failed to find the objects.
     */
    public final Collection findByQuery(final String queryString) throws LookupException {
        List list;

        Session session = SessionFactory.getInstance().openSession(getPersistentClass());
        try {
            Query query = session.createQuery(queryString);

            list = query.list();
            session.flush();
            session.connection().commit();

        } catch (HibernateException hibernateException) {
            throw new LookupException("failed to findbynamedquery 1 ", hibernateException);
        } catch (SQLException sqlException) {
            throw new LookupException("failed to findbynamedquery 2", sqlException);
        }

        SessionFactory.getInstance().closeSession(session);

        return (list);

    }

    /**
     * Method to search by query editory using long session
     *
     * @param editor        The quesry editor
     * @param progressPanel The progress panel
     * @return The result that were found or an empty string
     */
    public final Collection findByQueryEditorLongSession(QueryEditor editor, InfiniteProgressPanel progressPanel) {

        Criteria criteria = processQueryEditorCriteria(getLongSession(), editor.getClazz(), editor);
        return criteria.list();
    }

    /**
     * Find a collection of domain objects by direct hql query.
     *
     * @param editor an AT query editor
     * @return the collection of domain objects
     */
    public final Collection findByQueryEditor(final QueryEditor editor, InfiniteProgressPanel progressPanel) {

        boolean includeComponents = false;
        if (persistentClass == Resources.class && editor.getIncludeComponents()) {
            includeComponents = true;
        }
        if (editor.getAlternateQuery()) {
            return findByQueryEditorAlt(editor, progressPanel);

        } else if (!includeComponents) {
            Session session = SessionFactory.getInstance().openSession(null, getPersistentClass(), true);
            Criteria criteria = processQueryEditorCriteria(session, editor.getClazz(), editor);

            // if searching digital object then need to see if to only search for parent digital objects
            if (persistentClass == DigitalObjects.class && !editor.getIncludeComponents()) {
                criteria.add(Restrictions.isNull("parent"));
            }

            Collection collection = criteria.list();
            SessionFactory.getInstance().closeSession(session);
            return collection;

        } else {
            Collection<ResourcesComponentsSearchResult> resourcesAndComponetsResults = new ArrayList<ResourcesComponentsSearchResult>();
            HashMap<DomainObject, String> contextMap = new HashMap<DomainObject, String>();
            HashMap<ResourcesComponents, Resources> componentParentResourceMap = new HashMap<ResourcesComponents, Resources>();


            Session session = SessionFactory.getInstance().openSession(null, getPersistentClass(), true);

            ATSearchCriterion comparison1 = editor.getCriterion1();
            ATSearchCriterion comparison2 = editor.getCriterion2();


            Criteria criteria = session.createCriteria(editor.getClazz());
            criteria.add(comparison1.getCiterion());
            Collection collection = criteria.list();

            if (comparison2.getCiterion() == null) {
                addContextInfo(contextMap, collection, comparison1.getContext());
                addResourcesCommonToComponetResultSet(collection, resourcesAndComponetsResults, contextMap);
                humanReadableSearchString = comparison1.getSearchString();
            } else {
                // we have a boolean search
                Set returnCollection = new HashSet(collection);
                criteria = session.createCriteria(editor.getClazz());
                criteria.add(comparison2.getCiterion());
                Collection collection2 = criteria.list();
                if (editor.getChosenBoolean1().equalsIgnoreCase("and")) {
                    returnCollection.retainAll(collection2);
                    humanReadableSearchString = comparison1.getSearchString() + " and " + comparison2.getSearchString();
                } else {
                    returnCollection.addAll(collection2);
                    humanReadableSearchString = comparison1.getSearchString() + " or " + comparison2.getSearchString();
                }
                addContextInfo(contextMap, collection, comparison1.getContext());
                addContextInfo(contextMap, collection2, comparison2.getContext());
                addResourcesCommonToComponetResultSet(returnCollection, resourcesAndComponetsResults, contextMap);

            }
            SessionFactory.getInstance().closeSession(session);


            Resources resource;
//			addResourcesCommonToComponetResultSet(collection, resourcesAndComponetsResults, null);

            ResourcesDAO resourceDao = new ResourcesDAO();
            progressPanel.setTextLine("Searching for components that match the criteria", 2);

            session = SessionFactory.getInstance().openSession(ResourcesComponents.class);
            criteria = session.createCriteria(ResourcesComponents.class);
            criteria.add(comparison1.getCiterion());
            Collection components = criteria.list();
            addContextInfo(contextMap, components, comparison1.getContext());
            ResourcesComponents component;
            if (comparison2.getCiterion() == null) {
                int numberOfComponents = components.size();
                int count = 1;
                for (Object object : components) {
                    component = (ResourcesComponents) object;
                    progressPanel.setTextLine("Gathering resources by component matches " + count++ + " of " + numberOfComponents, 2);
                    resource = resourceDao.findResourceByComponent(component);
                    if (doesUserHaveAccessRightsToResource(resource)) {
                        resourcesAndComponetsResults.add(new ResourcesComponentsSearchResult(resource, component, comparison1.getContext()));
                    }
                }
                SessionFactory.getInstance().closeSession(session);
                return resourcesAndComponetsResults;

            } else {

                criteria = session.createCriteria(ResourcesComponents.class);
                criteria.add(comparison2.getCiterion());
                Collection components2 = criteria.list();
                addContextInfo(contextMap, components2, comparison2.getContext());

                Set returnCollection = new HashSet(components);
                if (editor.getChosenBoolean1().equalsIgnoreCase("and")) {
                    returnCollection.retainAll(components2);
                } else {
                    returnCollection.addAll(components2);
                }
                //find the parents for all the components
                for (Object object : returnCollection) {
                    int numberOfComponents = components.size();
                    int count = 1;
                    component = (ResourcesComponents) object;
                    progressPanel.setTextLine("Gathering resources by component matches " + count++ + " of " + numberOfComponents, 2);
                    resource = resourceDao.findResourceByComponent(component);
                    componentParentResourceMap.put(component, resource);
                }
                addResourcesCommonToComponetResultSet(returnCollection, resourcesAndComponetsResults, contextMap, componentParentResourceMap);
                return resourcesAndComponetsResults;
            }


//			criteria = processQueryEditorCriteria(session, ResourcesComponents.class, editor);
////			Collection components = criteria.list();
////			ResourcesComponents component;
//			int numberOfComponents = components.size();
//			int count = 1;
//			for (Object object : components) {
//				component = (ResourcesComponents) object;
//				progressPanel.setTextLine("Gathering resources by component matches " + count++ + " of " + numberOfComponents, 2);
//				resource = resourceDao.findResourceByComponent(component);
//				resourcesAndComponetsResults.add(new ResourcesComponentsSearchResult(resource, component, "dummy string"));
//			}
//			SessionFactory.getInstance().closeSession(session);
//			return resourcesAndComponetsResults;

        }
    }

    private void addResourcesCommonToComponetResultSet(Collection collection,
                                                       Collection<ResourcesComponentsSearchResult> resourcesAndComponetsResults,
                                                       HashMap<DomainObject, String> contextMap) {
        addResourcesCommonToComponetResultSet(collection, resourcesAndComponetsResults, contextMap, null);
    }

    private void addResourcesCommonToComponetResultSet(Collection collection,
                                                       Collection<ResourcesComponentsSearchResult> resourcesAndComponetsResults,
                                                       HashMap<DomainObject, String> contextMap,
                                                       HashMap<ResourcesComponents, Resources> componentParentResourceMap) {
        Resources resource;
        ResourcesComponents component;
        for (Object o : collection) {
            if (o instanceof Resources) {
                resource = (Resources) o;
                if (doesUserHaveAccessRightsToResource(resource)) {
                    resourcesAndComponetsResults.add(new ResourcesComponentsSearchResult(resource, contextMap.get(resource)));
                }
            } else if (o instanceof ResourcesComponents && componentParentResourceMap != null) {
                component = (ResourcesComponents) o;
                resource = componentParentResourceMap.get(component);
                if (resource != null &&
                        doesUserHaveAccessRightsToResource(resource)) {
                    resourcesAndComponetsResults.add(new ResourcesComponentsSearchResult(resource, component, contextMap.get(component)));
                }
            }
        }
    }

    /**
     * Method to check to see if the user has the rights to access the resource that
     * was return from a search based on if they have the right to access records from the
     * repository of the return resource record
     *
     * @param resource The resource to check
     * @return true if the user can access the resource, false if they can't
     */
    private boolean doesUserHaveAccessRightsToResource(Resources resource) {
        if (ApplicationFrame.getInstance().getCurrentUser() != null &&
                !Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_SUPERUSER)) {

            if (ApplicationFrame.getInstance().getCurrentUser().getRepository().equals(resource.getRepository())) {
                return true;
            } else {
                return false;
            }
        }

        return true; // just return true to allow user to access the resource
    }

    private Criteria processQueryEditorCriteria(Session session, Class clazz, QueryEditor editor) {
        Criteria criteria = session.createCriteria(clazz);
        ATSearchCriterion comparison1 = editor.getCriterion1();
        ATSearchCriterion comparison2 = editor.getCriterion2();
        if (comparison2.getCiterion() == null) {
            criteria.add(comparison1.getCiterion());
            humanReadableSearchString = comparison1.getSearchString();
        } else {
            if (editor.getChosenBoolean1().equalsIgnoreCase("and")) {
                criteria.add(comparison1.getCiterion());
                criteria.add(comparison2.getCiterion());
                humanReadableSearchString = comparison1.getSearchString() + " <font color='red'>and</font> " + comparison2.getSearchString();
            } else {
                criteria.add(Expression.or(comparison1.getCiterion(), comparison2.getCiterion()));
                humanReadableSearchString = comparison1.getSearchString() + " <font color='red'>or</font> " + comparison2.getSearchString();
            }
        }
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria;
    }


    //todo this method is a mess. There must be an easier way.
    private Collection findByQueryEditorAlt(QueryEditor editor, InfiniteProgressPanel progressPanel) {
        Session session;
        Criteria criteria;
        Set returnCollection = new HashSet();
        Set subsequentCollections = null;
        Set returnComponentCollection = new HashSet();
        Set subsequentComponentCollections = null;
        HashMap<ResourcesComponents, Resources> componentParentResourceMap = new HashMap<ResourcesComponents, Resources>();
        HashMap<DomainObject, String> contextMap = new HashMap<DomainObject, String>();
        boolean includeComponents = false;
        ArrayList<ResourcesComponentsSearchResult> resourcesAndComponetsResults = new ArrayList<ResourcesComponentsSearchResult>();
        ResourcesComponents component;

        //this is a loop of set intersections. In the first pass create a set
        //in all subsequent passes create a new set and perform an intersection on the
        //two sets. Java does this with the set.retainAll() method.
        if (persistentClass == Accessions.class || persistentClass == Resources.class || persistentClass == DigitalObjects.class) {
            //if we are including components then initialize the return set
            if (persistentClass == Resources.class && editor.getIncludeComponentsRelatedSearch()) {
                includeComponents = true;
            }
            boolean firstPass = true;
            subsequentCollections = new HashSet();
            humanReadableSearchString = "";
            for (QueryEditor.CriteriaRelationshipPairs criteriaPair : editor.getAltFormCriteria()) {
                session = SessionFactory.getInstance().openSession(null, getPersistentClass(), true);
                criteria = processCriteria(session, editor.getClazz(), criteriaPair);

                // if searching digital objects then need to see if to only search for parent digital objects
                if (persistentClass == DigitalObjects.class && !editor.getIncludeComponents()) {
                    criteria.add(Restrictions.isNull("parent"));
                }

                if (firstPass) {
                    returnCollection = new HashSet(criteria.list());
                } else {
                    subsequentCollections = new HashSet(criteria.list());
                }
                if (includeComponents) {
                    addContextInfo(contextMap, criteria.list(), criteriaPair.getContext());
                }
                SessionFactory.getInstance().closeSession(session);

                if (persistentClass == Resources.class && !criteriaPair.getResourceOnly() && includeComponents) {
                    subsequentComponentCollections = new HashSet();
                    session = SessionFactory.getInstance().openSession(ResourcesComponents.class);
                    criteria = processCriteria(session, ResourcesComponents.class, criteriaPair, false);
                    ResourcesDAO resourceDao = new ResourcesDAO();
                    Resources resource;
                    progressPanel.setTextLine("Searching for components that match the criteria", 2);
                    Collection components = criteria.list();
                    int numberOfComponents = components.size();
                    int count = 1;
                    for (Object object : components) {
                        progressPanel.setTextLine("Gathering resources by component matches " + count++ + " of " + numberOfComponents, 2);
                        component = (ResourcesComponents) object;
                        resource = resourceDao.findResourceByComponent(component);
                        componentParentResourceMap.put(component, resource);
                        addContextInfo(contextMap, component, criteriaPair.getContext());
                        if (firstPass) {
                            //if you are including components then add the component to the result set
                            //otherwise add the parent resource
                            if (includeComponents) {
                                returnComponentCollection.add(component);
                            } else {
                                returnCollection.add(resource);
                            }
                        } else {
                            //if you are including components then add the component to the result set
                            //otherwise add the parent resource
                            if (includeComponents) {
                                subsequentComponentCollections.add(object);
                            } else {
                                subsequentCollections.add(resource);
                            }
                        }
                    }
                    SessionFactory.getInstance().closeSession(session);
                }
                if (firstPass) {
                    firstPass = false;
                } else {
                    returnCollection.retainAll(subsequentCollections);
                    if (includeComponents) {
                        returnComponentCollection.retainAll(subsequentComponentCollections);
                    }
                }
            }
        }
        progressPanel.close();
        if (includeComponents) {
            addResourcesCommonToComponetResultSet(returnCollection, resourcesAndComponetsResults, contextMap);
            addResourcesCommonToComponetResultSet(returnComponentCollection, resourcesAndComponetsResults, contextMap, componentParentResourceMap);
            return resourcesAndComponetsResults;
        } else {
            return returnCollection;
        }
    }

    private void addContextInfo(HashMap<DomainObject, String> contextMap, Collection resultSet, String context) {
        for (Object o : resultSet) {
            addContextInfo(contextMap, (DomainObject) o, context);
        }
    }

    private void addContextInfo(HashMap<DomainObject, String> contextMap, DomainObject domainObject, String context) {
        if (contextMap.containsKey(domainObject)) {
            contextMap.put(domainObject, contextMap.get(domainObject) + ", " + context);
        } else {
            contextMap.put(domainObject, context);
        }
    }

    private Criteria processCriteria(Session session, Class clazz, QueryEditor.CriteriaRelationshipPairs criteriaPair) {
        return processCriteria(session, clazz, criteriaPair, true);
    }

    private Criteria processCriteria(Session session, Class clazz, QueryEditor.CriteriaRelationshipPairs criteriaPair, boolean appendToHumanReadableSearchScreen) {
        Criteria criteria;
        Conjunction junction;
        Criteria addedCriteria;
        criteria = session.createCriteria(clazz);

        if (appendToHumanReadableSearchScreen) {
            humanReadableSearchString = StringHelper.concat(" <font color='red'>and</font> ", humanReadableSearchString, criteriaPair.getHumanReadableSearchString());
        }
        junction = Restrictions.conjunction();
        if (criteriaPair.getSecondPropertyName() == null) {
            addedCriteria = criteria.createCriteria(criteriaPair.getPropertyName());
        } else {
            addedCriteria = criteria.createCriteria(criteriaPair.getPropertyName()).createCriteria(criteriaPair.getSecondPropertyName());
        }
        for (Criterion criterion : criteriaPair.getCriteriaList()) {
            junction.add(criterion);
        }
        addedCriteria.add(junction);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria;
    }

    /**
     * Find a collection of domain objects by example.
     *
     * @param instance the example object
     * @return the collection of domain objects
     * @throws LookupException if we failed to find the objects.
     */
    public final Collection findByExample(final Object instance) throws LookupException {
        List results;

        Session session = SessionFactory.getInstance().openSession(getPersistentClass());
        try {
            Example example = Example.create(instance)
                    .excludeZeroes()           //exclude zero valued properties
                    .ignoreCase()              //perform case insensitive string comparisons
                    .excludeProperty("version")
                    .excludeProperty("modifieddate")
                    .excludeProperty("creationdate")
                    .excludeProperty("createdBy")
                    .excludeProperty("modifiedBy")
                    .enableLike(MatchMode.START);             //use like for string comparisons
            results = session.createCriteria(instance.getClass())
                    .add(example)
                    .list();
            session.flush();
            session.connection().commit();

        } catch (HibernateException hibernateException) {
            throw new LookupException("failed to findbynamedquery 1 ", hibernateException);
        } catch (SQLException sqlException) {
            throw new LookupException("failed to findbynamedquery 2", sqlException);
        }

        SessionFactory.getInstance().closeSession(session);

        return (results);

    }

    /**
     * Find instances through a named query without parameters.
     *
     * @param propertyName the name of the property
     * @param value        the value of the property
     * @return the collection of instances
     * @throws LookupException if we failed to execute the query
     */

    public Collection findByPropertyValue(String propertyName, Object value) throws LookupException {

        Session session = SessionFactory.getInstance().openSession(getPersistentClass());
        return session.createCriteria(this.getPersistentClass())
                .add(Expression.eq(propertyName, value)).list();
    }

    /**
     * Find instances through a named query without parameters.
     *
     * @param propertyName the name of the property
     * @param values       the value of the property
     * @return the collection of instances
     * @throws LookupException if we failed to execute the query
     */

    public Collection findByPropertyValues(String propertyName, Object[] values) throws LookupException {
        Session session = SessionFactory.getInstance().openSession(getPersistentClass());
        return session.createCriteria(this.getPersistentClass())
                .add(Restrictions.in(propertyName, values)).list();
    }

    /**
     * Find instances through a named query without parameters.
     *
     * @param propertyName the name of the property
     * @param values       the value of the property
     * @return the collection of instances
     * @throws LookupException if we failed to execute the query
     */

    public Collection findByPropertyValuesLongSession(String propertyName, Object[] values) throws LookupException {
        longSession = SessionFactory.getInstance().openSession(new AuditInterceptor(ApplicationFrame.getInstance().getCurrentUser()));
        return longSession.createCriteria(this.getPersistentClass())
                .add(Restrictions.in(propertyName, values)).list();
    }

    /**
     * Find instances through a named query without parameters.
     *
     * @param propertyName the name of the property
     * @param value        the value of the property
     * @return the collection of instances
     * @throws LookupException if we failed to execute the query
     */

    public DomainObject findByUniquePropertyValue(String propertyName, Object value) throws LookupException {

        Session session = SessionFactory.getInstance().openSession(getPersistentClass());
        DomainObject domainObject = (DomainObject) session.createCriteria(this.getPersistentClass())
                .add(Expression.eq(propertyName, value)).uniqueResult();
        session.close();
        return domainObject;
    }

    /**
     * Find instances through a named query without parameters.
     *
     * @param propertyName the name of the property
     * @param value        the value of the property
     * @return the collection of instances
     * @throws LookupException if we failed to execute the query
     */

    public DomainObject findByUniquePropertyValueLongSession(String propertyName, Object value) throws LookupException {
        longSession = SessionFactory.getInstance().openSession(new AuditInterceptor(ApplicationFrame.getInstance().getCurrentUser()));
        DomainObject domainObject = (DomainObject) longSession.createCriteria(this.getPersistentClass())
                .add(Expression.eq(propertyName, value)).uniqueResult();
        return domainObject;
    }

    /**
     * Find instances through a named query without parameters.
     *
     * @param propertyName the name of the property
     * @param oldValue     the old value of the property
     * @param newValue     the new value of the property
     * @return the number of records updated
     * @throws LookupException if we failed to execute the query
     */

    public int updateTextField(String propertyName, String oldValue, String newValue, InfiniteProgressPanel progressPanel) throws LookupException, IllegalAccessException, InvocationTargetException, ValidationException {
        Session session = SessionFactory.getInstance().openSession();
        Transaction tx = session.beginTransaction();
        ATValidator validator;
        ValidationResult validationResult;

        Collection records = session.createCriteria(this.getPersistentClass())
                .add(Expression.eq(propertyName, oldValue)).list();
        int updatedEntities = records.size();
        int count = 1;
        int textDepth = progressPanel.getTextDepth();
        String message;
        for (Object record : records) {
            message = count++ + " of " + updatedEntities;
            progressPanel.setTextLine(message, textDepth + 1);
            BeanUtils.setProperty(record, propertyName, newValue);
            validator = ValidatorFactory.getInstance().getValidator((DomainObject) record);
            if (validator != null) {
                validationResult = validator.validate();
                if (validationResult.hasErrors()) {
                    // we have an error so roll back the transaction and throw an error
                    tx.rollback();
                    session.close();
                    throw new ValidationException("For the record named \"" + record.toString() + "\"\n" +
                            validationResult.getMessagesText());
                }
            }

            session.update(record);
        }
        tx.commit();
        session.close();
        return updatedEntities;
    }

    public Integer getCountBasedOnPropertyValue(String propertyName, Object value) {
        Session session = SessionFactory.getInstance().openSession();
        Criteria criteria = session.createCriteria(this.getPersistentClass())
                .setProjection(Projections.rowCount())
                .add(Expression.eq(propertyName, value));
        Integer count = (Integer) criteria.list().get(0);
        session.close();
        return count;
    }

    public int merge(Collection<DomainObject> mergeFrom, DomainObject mergeTo, InfiniteProgressPanel progressPanel) throws MergeException {
        return 0;
    }


    /**
     * Return a collection which conforms to the named query.
     * TODO: check the bean interface to queries
     *
     * @param queryName      the name of the query
     * @param propertyObject the bean which is used to pass properties to the query
     * @return the collection generated by the query
     * @throws LookupException fails if we cannot execute the named query
     */

    public final Collection findByNamedQuery(final String queryName, final Object propertyObject) throws LookupException {
        List filteredList;

        Session session = SessionFactory.getInstance().openSession(getPersistentClass());
        try {
            Query query = session.getNamedQuery(queryName);

            query.setProperties(propertyObject);

            filteredList = query.list();
            session.flush();
            session.connection().commit();

        } catch (HibernateException hibernateException) {
            throw new LookupException("failed to findbynamedquery 1", hibernateException);
        } catch (SQLException sqlException) {
            throw new LookupException("failed to findbynamedquery 2", sqlException);
        }

        SessionFactory.getInstance().closeSession(session);

        return (filteredList);
    }

    /**
     * Return a collection which conforms to the named query.
     *
     * @param queryName the name of the query
     * @return the collection provided by the query
     * @throws LookupException fails if we cannot execute the query
     */

    public final Collection findByNamedQuery(final String queryName) throws LookupException {
        List filteredList;

        Session session = SessionFactory.getInstance().openSession(getPersistentClass());

        try {
            //session.connection().setReadOnly(true);
            session.setFlushMode(FlushMode.MANUAL);

            Query query = session.getNamedQuery(queryName);

            filteredList = query.list();
            //session.flush();  Don't flush here this causes all sorts of hell on Oracle
            session.connection().commit();

        } catch (HibernateException hibernateException) {
            throw new LookupException("failed to findbynamedquery", hibernateException);
        } catch (SQLException sqlException) {
            throw new LookupException("failed to findbynamedquery", sqlException);
        }

        SessionFactory.getInstance().closeSession(session);

        return (filteredList);
    }

    /**
     * Called to notify the listener that an event has occurred.
     *
     * @param event denotes that a domainobject has changed.
     */

    public final void domainChanged(final DomainAccessEvent event) {
        this.notifyListeners(event);
    }

    protected Class getPersistentClass() {
        return persistentClass;
    }

    private void updateClassSpecific(DomainObject object, Session session) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        if (object instanceof DatabaseTables) {
            ((DatabaseTables) object).reorderReturnScreenList();
        } else if (object instanceof Users) {
            if (ApplicationFrame.getInstance().getCurrentUser().equals(object)) {
                ApplicationFrame.getInstance().setCurrentUser((Users) object);
            }
        } else if (object instanceof Resources) {
            ResourcesComponentsDAO dao = new ResourcesComponentsDAO();
            for (ResourcesComponents component : ((Resources) object).getComponentsToDelete()) {
                dao.deleteComponent(component, session);
            }
            // must clear all components that were deleted so that we don't get a bug
            ((Resources) object).clearComponentsToDelete();
        } else if (object instanceof Names) {
            Names name = (Names) object;
            NameUtils.setMd5Hash(name);
        }
    }

    public String getHumanReadableSearchString() {
        return humanReadableSearchString;
	}

	public void setHumanReadableSearchString(String humanReadableSearchString) {
		this.humanReadableSearchString = humanReadableSearchString;
	}
}