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

import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.model.LookupList;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

import java.util.List;

public class LookupListsDAO extends DomainAccessObjectImpl {

	/**
	 * Constructor which builds a DAO for this class.
	 */
	public LookupListsDAO() {
		super(LookupList.class);
	}


	public LookupList findLookupListByName(String listName) throws PersistenceException {

		Session session = SessionFactory.getInstance().openSession();
		Criteria criteria = session.createCriteria(LookupList.class)
				.add(Expression.eq("listName", listName));
		LookupList returnList = (LookupList) criteria.uniqueResult();
		session.close();
		return returnList;
	}


}
