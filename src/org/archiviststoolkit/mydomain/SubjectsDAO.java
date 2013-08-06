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

import org.archiviststoolkit.model.Subjects;
import org.archiviststoolkit.model.ArchDescriptionSubjects;
import org.archiviststoolkit.model.validators.ATValidator;
import org.archiviststoolkit.model.validators.ValidatorFactory;
import org.archiviststoolkit.exceptions.ValidationException;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.MergeException;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.ApplicationFrame;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.LockMode;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Expression;
import com.jgoodies.validation.ValidationResult;

import java.util.Collection;

public class SubjectsDAO extends DomainAccessObjectImpl {

	/**
	 * Constructor which builds a DAO for this class.
	 */
	public SubjectsDAO() {
		super(Subjects.class);
	}


	public Subjects lookupSubject(String subjectValue,
								  String subjectTermType,
								  String subjectSource,
								  boolean create) throws PersistenceException, ValidationException, UnknownLookupListException {

		if (subjectValue.length() == 0) {
			//do not add blank subjects
			return null;
		} else {
			Session session = SessionFactory.getInstance().openSession();
			Subjects subject = (Subjects) session.createCriteria(Subjects.class)
					.add(Expression.eq(Subjects.PROPERTYNAME_SUBJECT_TERM, subjectValue))
					.add(Expression.eq(Subjects.PROPERTYNAME_SUBJECT_TERM_TYPE, subjectTermType))
					.add(Expression.eq(Subjects.PROPERTYNAME_SUBJECT_SOURCE, subjectSource))
					.uniqueResult();
			session.close();
			if (subject != null) {
				return subject;
			} else if (create) {
				subject = new Subjects();
				subject.setSubjectTerm(subjectValue);
				subject.setSubjectTermType(subjectTermType);
				subject.setSubjectSource(subjectSource);
				ATValidator validator = ValidatorFactory.getInstance().getValidator(subject);
				ValidationResult validationResult = validator.validate();
				if (validationResult.hasErrors()) {
					throw new ValidationException(validationResult.getMessagesText());
				}
				this.add(subject);
				//make sure that subject term type and source have the values added to the lookup lists
				ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(Subjects.class.getName() + "." + Subjects.PROPERTYNAME_SUBJECT_SOURCE);
				LookupListUtils.addItemToList(fieldInfo.getLookupList(), subjectSource);
				fieldInfo = ATFieldInfo.getFieldInfo(Subjects.class.getName() + "." + Subjects.PROPERTYNAME_SUBJECT_TERM_TYPE);
				LookupListUtils.addItemToList(fieldInfo.getLookupList(), subjectTermType);
				Subjects.addSubjectToLookupList(subject);
				return subject;
			} else {
				return null;
			}

		}
	}

	public int merge(Collection<DomainObject> mergeFrom, DomainObject mergeTo, InfiniteProgressPanel progressPanel) throws MergeException {
		Session session = SessionFactory.getInstance().openSession();
		Transaction tx = session.beginTransaction();
		Subjects subjectMergeTo = (Subjects)mergeTo;
		session.lock(subjectMergeTo, LockMode.NONE);
		Subjects subject;
		String message;
		int totalCount = 0;
		int subjectsToMerge = mergeFrom.size() - 1;
		int subjectsMerged = 1;
		for (DomainObject domainObject: mergeFrom) {
			try {
				subject = (Subjects)domainObject;
				if (!subject.equals(subjectMergeTo)) {
					session.lock(subject, LockMode.NONE);
					progressPanel.setTextLine("Merging (record " + subjectsMerged++ + " of " + subjectsToMerge + ")...", 1);
					progressPanel.setTextLine(subject + " -> " + subjectMergeTo, 2);
					int count = 1;
					int numberOfLinks = subject.getArchDescriptionSubjects().size();
					for (ArchDescriptionSubjects subjectLink: subject.getArchDescriptionSubjects()) {
						try {
							message = "relationship " + count++ + " of " + numberOfLinks;
							System.out.println(message);
							progressPanel.setTextLine(message, 3);
							subjectMergeTo.addArchDesctiption(subjectLink.getArchDescription());
							totalCount++;
						} catch (DuplicateLinkException e) {
							//do nothing
						}
					}
					session.delete(subject);
				}
			} catch (HibernateException e) {
				throw new MergeException("Error merging subjects", e);
			}
		}
		session.update(subjectMergeTo);
		tx.commit();
		session.close();
		return totalCount;
	}
}
