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
import org.archiviststoolkit.model.ArchDescriptionNames;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.MergeException;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.NameUtils;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.hibernate.*;
import org.hibernate.criterion.Expression;

import java.util.List;
import java.util.Collection;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

public class NamesDAO extends DomainAccessObjectImpl {

	/**
	 * Constructor which builds a DAO for this class.
	 */
	public NamesDAO() {
		super(Names.class);
	}

	public Names lookupName(String sortName, String subUnit, String nameType, boolean create) throws PersistenceException {
		Session session = SessionFactory.getInstance().openSession();
		Names name = (Names) session.createCriteria(Names.class)
				.add(Expression.eq("sortName", sortName))
				.uniqueResult();
		session.close();
		if (name != null) {
			return name;
		} else if (create) {
			name = new Names();
			name.setSortName(sortName);
			if (nameType.equalsIgnoreCase(Names.PERSON_TYPE)) {
				name.setNameType(Names.PERSON_TYPE);
				name.setQualifier(subUnit);
			} else if (nameType.equalsIgnoreCase(Names.CORPORATE_BODY_TYPE)) {
				name.setNameType(Names.CORPORATE_BODY_TYPE);
				name.setCorporateSubordinate1(subUnit);
			} else if (nameType.equalsIgnoreCase(Names.FAMILY_TYPE)) {
				name.setNameType(Names.FAMILY_TYPE);
				name.setQualifier(subUnit);
			} else {
				//TODO throw error
			}
			this.add(name);
			return name;
		} else {
			return null;
		}

	}

	public Names lookupName(Names name, boolean create) throws PersistenceException, UnknownLookupListException {

        //todo better feedback for more than one match
		Session session = SessionFactory.getInstance().openSession();
		Names lookedUpName = (Names) session.createCriteria(Names.class)
				.add(Expression.eq(Names.PROPERTYNAME_NAME_MD5_HASH, name.getMd5Hash()))
				.uniqueResult();
		session.close();
		if (lookedUpName != null) {
			return lookedUpName;
		} else if (create) {
			this.add(name);
			//make sure that subject term type and source have the values added to the lookup lists
			ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(Names.class.getName() + "." + Names.PROPERTYNAME_NAME_RULE);
			LookupListUtils.addItemToList(fieldInfo.getLookupList(), name.getNameRule());
			fieldInfo = ATFieldInfo.getFieldInfo(Names.class.getName() + "." + Names.PROPERTYNAME_NAME_SOURCE);
			LookupListUtils.addItemToList(fieldInfo.getLookupList(), name.getNameSource());
			Names.addNameToLookupList(name);
			return name;
		} else {
			return null;
		}

	}

	public int merge(Collection<DomainObject> mergeFrom, DomainObject mergeTo, InfiniteProgressPanel progressPanel) throws MergeException {
        Session session = SessionFactory.getInstance().openSession();
		Transaction tx = session.beginTransaction();

		Names nameMergeTo = (Names)mergeTo;
		session.lock(nameMergeTo, LockMode.NONE);
		Names name;
		String message;
		int totalCount = 0;
		int subjectsToMerge = mergeFrom.size() - 1;
		int subjectsMerged = 1;
		for (DomainObject domainObject: mergeFrom) {
			try {
				name = (Names)domainObject;
				if (!name.equals(nameMergeTo)) {
					session.lock(name, LockMode.NONE);
					progressPanel.setTextLine("Merging (record " + subjectsMerged++ + " of " + subjectsToMerge + ")...", 1);
					progressPanel.setTextLine(name + " -> " + nameMergeTo, 1);
					int count = 1;
					int numberOfLinks = name.getArchDescriptionNames().size();
					for (ArchDescriptionNames nameLink : name.getArchDescriptionNames()) {
						try {
							message = "relationship " + count++ + " of " + numberOfLinks;
							System.out.println(message);
							progressPanel.setTextLine(message, 3);
                            nameMergeTo.addArchDesctiption(nameLink);
							totalCount++;
						} catch (DuplicateLinkException e) {
							//do nothing
						}
					}
					session.delete(name);
				}
			} catch (HibernateException e) {
                tx.rollback();
                session.close();
				throw new MergeException("Error merging names", e);
			}
		}
		session.update(nameMergeTo);
        try {
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
             session.close();
            throw new MergeException("Error merging names", e);
        }
        session.close();
		return totalCount;
	}

}
