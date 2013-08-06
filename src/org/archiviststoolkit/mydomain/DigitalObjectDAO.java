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

import org.archiviststoolkit.model.ResourcesComponents;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.model.DigitalObjects;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Criterion;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;

import org.archiviststoolkit.model.ArchDescriptionDigitalInstances;

public class DigitalObjectDAO extends DomainAccessObjectImpl {
	public static int a = 0;

	/**
	 * Constructor which builds a DAO for this class.
	 */
	public DigitalObjectDAO() {
		//super(ResourcesComponents.class); TODO this was the original but looked like an error
		super(DigitalObjects.class);
	}


	public List lookupDigitalObjectByParent(DigitalObjects digitalObject) {
		Session session = SessionFactory.getInstance().openSession();
		List al = this.lookupDigitalObjectByParent(digitalObject, session);
		session.close();
		return al;
	}

	public List lookupDigitalObjectByParent(DigitalObjects digitalObject, Session session) {


		Criteria criteria = session.createCriteria(DigitalObjects.class)
				.addOrder(Order.asc("objectOrder"))
				.add(Expression.eq("parent", digitalObject));
		List al = new ArrayList();
		al = criteria.list();
		//session.close();
		return al;
	}

	public void updateFromDrop(Long resourceComponentId,
							   Long resourceParentId,
							   Long resourceComponentParentId,
							   Double componentOrder) {

		Session session = SessionFactory.getInstance().openSession();
		Query query = session.getNamedQuery("updateFromDrop");
		query.setLong("resourceComponentId", resourceComponentId);
		if (resourceParentId == null) {
			query.setLong("resourceId", 0l);
		} else {
			query.setLong("resourceId", resourceParentId);
		}
		if (resourceComponentParentId == null) {
			query.setLong("parentResourceComponentId", 0l);
		} else {
			query.setLong("parentResourceComponentId", resourceComponentParentId);
		}
		query.setDouble("resourcesOrder", componentOrder);
		query.list();
		session.close();
	}
        
    public Resources findResourceByDigitalObject(ArchDescriptionDigitalInstances instance) {
            Resources resource=null;
            
            if (instance.getResource() != null) {
                    resource = instance.getResource();
            } else {
                    ResourcesDAO resourcesDAO = new ResourcesDAO();
                    resourcesDAO.getLongSession();
                    if(instance.getResourceComponent()!=null) {
                        resource = resourcesDAO.findResourceByComponent((instance.getResourceComponent()));
                    }
            }
            return resource;
    }

    /**
     * Method to find a parent digital object by the METS identifier
     *
     * @param digitalObject The digital object whose identifer to use to do the look up
     * @return The Digital Object that was found or null otherwise
     * @throws LookupException Throws this exception is there was a problem finding the digital object
     */
    public DigitalObjects findByMetsIdentifier(DigitalObjects digitalObject) throws LookupException {
        Session session = SessionFactory.getInstance().openSession(getPersistentClass());
        return findByMetsIdentifierCommon(digitalObject, true, session);
    }

    /**
     * Method to find a parent digital object by the METS identifier using a long seesion
     *
     * @param digitalObject The digital object whose identifer to use to do the look up
     * @return The Digital Object that was found or null otherwise
     * @throws LookupException Throws this exception is there was a problem finding the digital object
     */
    public DigitalObjects findByMetsIdentifierLongSession(DigitalObjects digitalObject) throws LookupException {
        return findByMetsIdentifierCommon(digitalObject, false, getLongSession());
    }

    /**
     * Method to find a parent digital object by the METS identifier
     *
     * @param digitalObject The digital object whose identifer to use to do the look up
     * @param session The session to use to find the digital object
     * @param closeSession Whether to close the session after using it
     * @return The Digital Object that was found or null otherwise
     * @throws LookupException Throws this exception is there was a problem finding the digital object
     */
    public DigitalObjects findByMetsIdentifierCommon(DigitalObjects digitalObject, boolean closeSession, Session session) throws LookupException {
        String metsID = digitalObject.getMetsIdentifier();

        if(metsID != null && metsID.trim().length() > 0) {
            Collection foundDigitalObjects = findAllCommon(LockMode.READ, session, closeSession, Restrictions.eq("metsIdentifier", metsID));

            if (!foundDigitalObjects.isEmpty()) {
                Object[] objects = foundDigitalObjects.toArray();
                return (DigitalObjects) objects[0];
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Method that finds all the parent digital objects
     *
     * @param lockmode The lock mode to use
     * @param sortFields fields to sort by
     * @return A collection of parent objects found
     * @throws LookupException If there is a problem finding the digital objects
     */
    public Collection findAll(LockMode lockmode, String... sortFields) throws LookupException {
	    Session session = SessionFactory.getInstance().openSession(getPersistentClass());
        return findAllCommon(lockmode, session, true, null, sortFields);
	}

    /**
     * Method that finds all the parent digital objects using a long session
     *
     * @param lockmode
     * @param sortFields
     * @return Collection containing the parent digital objects that were found
     * @throws LookupException
     */
    public Collection findAllLongSession(LockMode lockmode, String... sortFields) throws LookupException {
        Session session = getLongSession();
        return findAllCommon(lockmode, session, false, null, sortFields);
	}

    /**
     * Method that finds all parent digital objects that are not linked to any resource record
     * @param lockmode
     * @param sortFields
     * @return Collection containing the parent digital objects that were found
     * @throws LookupException
     */
    public Collection findAllUnlinked(LockMode lockmode, String... sortFields) throws LookupException {
        Session session = getLongSession();
        return findAllCommon(lockmode, session, false, Restrictions.isNull("digitalInstance"), sortFields);
    }

    /**
     * Method finds all parent digital object and accepts an additional criterion for searching
     *
     * @param lockmode The lock mode
     * @param session The session
     * @param closeSession Whether to close the session after seaching is done
     * @param criterion the additional criterion to add
     * @param sortFields te field to sort by
     * @return A collection of parent digital objects
     * @throws LookupException
     */
    public Collection findAllCommon(LockMode lockmode, Session session, boolean closeSession, Criterion criterion, String... sortFields) throws LookupException {
        List completeList;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(getPersistentClass());
            criteria.add(Restrictions.isNull("parent")); // make sure we only return parent digital objects

            if(criterion != null) {
                criteria.add(criterion);
            }

            for (String field : sortFields) {
				criteria.addOrder(Order.asc(field));
			}

			completeList = criteria.list();

			tx.commit();

		} catch (RuntimeException ex) {
			try {
				tx.rollback();
			} catch (HibernateException e) {
				//todo log error
			}
			throw new LookupException("failed to find all", ex);
		} finally {
			if(closeSession) {
                session.close();
            }
		}

		return completeList;
    }
}
