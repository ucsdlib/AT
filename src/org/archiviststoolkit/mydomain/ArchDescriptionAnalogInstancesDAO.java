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
 * Date: Nov 28, 2005
 * Time: 3:17:18 PM
 */

package org.archiviststoolkit.mydomain;

import org.archiviststoolkit.model.*;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

import java.util.List;
import java.util.Collections;

public class ArchDescriptionAnalogInstancesDAO extends DomainAccessObjectImpl {

	/**
	 * Constructor which builds a DAO for this class.
	 */
	public ArchDescriptionAnalogInstancesDAO() {
		super(ArchDescriptionAnalogInstances.class);
	}

	public List findByResource(Resources resource) throws PersistenceException {

		Session session = SessionFactory.getInstance().openSession();

		Criteria criteria = session.createCriteria(this.getPersistentClass());
		criteria.add(Restrictions.eq(ArchDescriptionInstances.PROPERTYNAME_RESOURCE, resource));

		List returnList = criteria.list();
		SessionFactory.getInstance().closeSession(session);
		Collections.sort(returnList);
		return returnList;
	}

	public List findByResourceComponent(ResourcesComponents resourceComponent) throws PersistenceException {

		Session session = SessionFactory.getInstance().openSession();

		Criteria criteria = session.createCriteria(this.getPersistentClass());
		criteria.add(Restrictions.eq(ArchDescriptionInstances.PROPERTYNAME_RESOURCE_COMPONENT, resourceComponent));

		List returnList = criteria.list();
		SessionFactory.getInstance().closeSession(session);
		Collections.sort(returnList);
		return returnList;
	}

	public void addLocationToInstance(Locations location, ArchDescriptionAnalogInstances instance) throws PersistenceException {
		Session session = SessionFactory.getInstance().openSession();

		try {
			session.update(instance);
			instance.setLocation(location);
			session.flush();
			session.connection().commit();
		} catch (HibernateException hibernateException) {
			System.out.println("update Exception " + hibernateException);
			throw new PersistenceException("failed to update", hibernateException);
		} catch (Exception sqlException) {
			System.out.println("update Exception " + sqlException);
			throw new PersistenceException("failed to update", sqlException);
		}

		SessionFactory.getInstance().closeSession(session);

	}
}
