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
import org.archiviststoolkit.model.ArchDescriptionRepeatingData;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.util.ResourceUtils;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Expression;

import java.util.List;
import java.util.Iterator;

public class ArchDescriptionRepeatingDataDAO extends DomainAccessObjectImpl {

	/**
	 * Constructor which builds a DAO for this class.
	 */
	public ArchDescriptionRepeatingDataDAO() {
		super(ArchDescriptionRepeatingData.class);
	}


	public List lookupRepeatingDataByParentResourceComponent(ResourcesComponents component) {
		Session session = SessionFactory.getInstance().openSession();
		List al = this.lookupRepeatingDataByParentResourceComponent(component, session);
		session.close();
		return al;
	}

	public List lookupRepeatingDataByParentResourceComponent(ResourcesComponents component, Session session) {
		Criteria criteria = session.createCriteria(ArchDescriptionRepeatingData.class)
				.addOrder(Order.asc(ArchDescriptionRepeatingData.PROPERTYNAME_SEQUENCE_NUMBER))
//				.setProjection(Projections.projectionList()
//						.add(Projections.property(ResourcesComponents.PROPERTYNAME_TITLE))
//						.add(Projections.property(ResourcesComponents.PROPERTYNAME_SEQUENCE_NUMBER))
//						.add(Projections.property(ResourcesComponents.PROPERTYNAME_RESOURCES_COMPONENT_ID))
//						.add(Projections.property(ResourcesComponents.PROPERTYNAME_DATE_EXPRESSION)))
//				.setResultTransformer(new AliasToBeanResultTransformer(ResourcesComponents.class))
				.add(Expression.eq(ArchDescriptionRepeatingData.PROPERTYNAME_RESOURCE_COMPONENT, component));
		return criteria.list();
	}

	public void gatherPersistentIds(ResourcesComponents component, Session session, String indent) {
		List res = session.getNamedQuery("gatherPersitentIds")
				.setLong("resourceComponentId", component.getIdentifier())
				.list();
		Iterator it = res.iterator();
		Object t[];
		String type;
		String note;
		String noteContent;
		String noteType;
		String persistentId;
		String description;
		while (it.hasNext()) {
			t = (Object[]) it.next();
			type = ((String) t[0]);
			note = ((String) t[1]);
			noteContent = ((String) t[2]);
			noteType = ((String) t[3]);
			persistentId = ((String) t[4]);
			if (type.equalsIgnoreCase("note")) {
				description =  indent + "   " + noteType + ": " + noteContent;
			} else {
				description =  indent + "   " + type + ": " + note;
			}
			ResourceUtils.addToPersistentIdList(description, persistentId);
		}

	}

}
