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
 *
 */

package org.archiviststoolkit.mydomain;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.model.*;

import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;


public class ResourcesComponentsDAO extends DomainAccessObjectImpl {
	public static int a = 0;

	/**
	 * Constructor which builds a DAO for this class.
	 */
	public ResourcesComponentsDAO() {
		super(ResourcesComponents.class);
	}


	public List lookupResourceComponentByParentResourceComponent(ResourcesComponents rc) {
		Session session = SessionFactory.getInstance().openSession();
		List al = this.lookupResourceComponentByParentResourceComponent(rc, session);
		session.close();
		return al;
	}

	public List lookupResourceComponentByParentResourceComponent(ResourcesComponents rc, Session session) {
		long t1;
		long t2;
		ResourcesComponents rcmps = null;
		ArrayList al = new ArrayList();
		Iterator it = null;
		t1 = System.currentTimeMillis();
		List res = session.getNamedQuery("mySqlQuery")
				.setLong("resourceComponentId", rc.getIdentifier())
				.list();
		t2 = System.currentTimeMillis();
		it = res.iterator();
		Object t[];
		while (it.hasNext()) {
			rcmps = new ResourcesComponents();
			t = (Object[]) it.next();
			rcmps.setTitle((String) t[0]);
			rcmps.setDateExpression((String) t[1]);
			rcmps.setIdentifier((Long) t[2]);
			rcmps.setHasChild((Boolean) t[3]);
			rcmps.setSequenceNumber((Integer) t[4]);
			al.add(rcmps);
		}

		return al;

	}


	public List lookupResourceComponentByParentResourceComponent(Resources r) {
		Session session = SessionFactory.getInstance().openSession();
		List al = this.lookupResourceComponentByParentResource(r, session);
		session.close();
		return al;
	}

	public List lookupResourceComponentByParentResource(Resources r, Session session) {
		//Session session = SessionFactory.getInstance().openSession();
		long t1, t2;
		t1 = System.currentTimeMillis();
		Criteria criteria = session.createCriteria(ResourcesComponents.class)
				.addOrder(Order.asc("resourcesOrder"))
//				.setProjection(Projections.projectionList()
//						.add(Projections.property(ResourcesComponents.PROPERTYNAME_TITLE))
//						.add(Projections.property(ResourcesComponents.PROPERTYNAME_SEQUENCE_NUMBER))
//						.add(Projections.property(ResourcesComponents.PROPERTYNAME_RESOURCES_COMPONENT_ID))
//						.add(Projections.property(ResourcesComponents.PROPERTYNAME_DATE_EXPRESSION)))
//				.setResultTransformer(new AliasToBeanResultTransformer(ResourcesComponents.class))
				.add(Expression.eq("resource", r));
//		t2 = System.currentTimeMillis();
//        System.out.println("query1 time = "+(t2-t1)+" milliseconds");
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

    public void deleteComponent(ResourcesComponents component, Session session) {
        // Get any digital instance and unlink the digital object. This needs
        // to be done because of the circular relationship between digital instances
        // and digital objects
        Set<ArchDescriptionDigitalInstances> instances = component.getInstances(ArchDescriptionDigitalInstances.class);

        for(ArchDescriptionDigitalInstances instance: instances) {
            DigitalObjects digitalObject = instance.getDigitalObject();
            if(digitalObject != null) {
                digitalObject.setDigitalInstance(null);
                instance.setDigitalObject(null);
                try {
                    session.update(digitalObject); // must update the digital object so we don't get a foreign key constraint violation
                    session.flush();
                    session.connection().commit();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }

        for (ResourcesComponents child: component.getResourcesComponents()) {
             deleteComponent(child, session);
        }
        // now delete the resource component
        session.delete(component);
    }
}
