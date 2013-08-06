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
 * Date: Nov 8, 2005
 * Time: 3:30:36 PM
 */

package org.archiviststoolkit.hibernate;

import org.archiviststoolkit.mydomain.AuditInfo;
import org.archiviststoolkit.mydomain.Auditable;
import org.archiviststoolkit.model.*;
import org.hibernate.*;
import org.hibernate.type.Type;

import java.sql.Timestamp;
import java.io.Serializable;

public class AuditInterceptor extends EmptyInterceptor implements Interceptor, Serializable {

	private String userName;

	public AuditInterceptor(Users user) {
		if (user == null) {
			userName = "";
		} else {
			userName = user.getUserName();
		}
	}

	public boolean onFlushDirty(
			Object object,
			Serializable serializable,
			Object[] currentState, Object[] previousState,
			String[] propertyNames,
			org.hibernate.type.Type[] types) throws CallbackException {


		if (object instanceof Auditable) {
			AuditInfo ai = ((Auditable) object).getAuditInfo();
			try {
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				ai.setLastUpdated(timestamp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			ai.setLastUpdatedBy(userName);
		}
		return setDisplayValues(object, currentState, propertyNames);
	}

	public boolean onSave(
			Object object,
			Serializable serializable,
			Object[] state, String[] propertyNames,
			org.hibernate.type.Type[] types) throws CallbackException {
		if (object instanceof Auditable) {
			AuditInfo ai = ((Auditable) object).getAuditInfo();
			try {
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				ai.setCreated(timestamp);
				ai.setLastUpdated(timestamp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			ai.setCreatedBy(userName);
			ai.setLastUpdatedBy(userName);
		}
		return setDisplayValues(object, state, propertyNames);
	}

	private boolean setDisplayValues(Object object, Object[] state, Object[] propertyNames) {
		if (object instanceof Accessions || object instanceof Resources) {
			ArchDescription archDescription = (ArchDescription)object;
			for ( int i=0; i < propertyNames.length; i++ ) {
				if ( ArchDescription.PROPERTYNAME_DISPLAY_CREATOR.equals( propertyNames[i] ) ) {
					state[i] = archDescription.getCreator();
				}
				if ( ArchDescription.PROPERTYNAME_DISPLAY_REPOSITORY.equals( propertyNames[i] ) ) {
					String repositoryName = "";
					if (archDescription instanceof Accessions) {
						repositoryName = ((Accessions)archDescription).getRepository().getRepositoryName();
					} else if (archDescription instanceof Resources) {
						repositoryName = ((Resources)archDescription).getRepository().getRepositoryName();
					}
					state[i] = repositoryName;
				} else if ( ArchDescription.PROPERTYNAME_DISPLAY_SOURCE.equals( propertyNames[i] ) ) {
					state[i] = archDescription.getSource();
				}

			}
			return true;
		} else if (object instanceof ArchDescriptionNames){
			ArchDescriptionNames archDescriptionName = (ArchDescriptionNames)object;
			ArchDescription archDescription = archDescriptionName.getArchDescription();
			if (archDescription instanceof Accessions) {
				((Accessions)archDescription).setDisplayCreator(archDescription.getCreator());
				((Accessions)archDescription).setDisplaySource(archDescription.getSource());
			} else if (archDescription instanceof Resources) {
				((Resources)archDescription).setDisplayCreator(archDescription.getCreator());
				((Resources)archDescription).setDisplaySource(archDescription.getSource());
			}
			return true;
		} else {
			return false;
		}
	}
}