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
 * Time: 2:09:49 PM
 */

package org.archiviststoolkit.hibernate;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.type.Type;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.usertype.CompositeUserType;
import org.archiviststoolkit.mydomain.AuditInfo;

import java.sql.*;
import java.io.Serializable;

public class AuditInfoType implements CompositeUserType {

	public static final int LAST_UPDATED = 0;
	public static final int CREATED = 1;
	public static final int LAST_UPDATED_BY = 2;
	public static final int CREATED_BY = 3;

	private static final int[] SQL_TYPES = new int[]{
			Types.TIMESTAMP,
			Types.TIMESTAMP,
			Types.VARCHAR,
			Types.VARCHAR
	};

	private static final String[] NAMES = new String[]{
			"lastUpdated",
			"created",
			"lastUpdatedBy",
			"createdBy"
	};

	private static final Type[] TYPES = new Type[]{
			Hibernate.TIMESTAMP,
			Hibernate.TIMESTAMP,
			Hibernate.STRING,
			Hibernate.STRING
	};

	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	public boolean isMutable() {
		return true;
	}

	public Serializable disassemble(Object object, SessionImplementor sessionImplementor) throws HibernateException {
		return null;
	}

	public Object assemble(Serializable serializable, SessionImplementor sessionImplementor, Object object) throws HibernateException {
		return null;
	}

	public Object replace(Object object, Object object1, SessionImplementor sessionImplementor, Object object2) throws HibernateException {
		return null;
	}

	public Serializable disassemble(Object object) throws HibernateException {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	public Object assemble(Serializable serializable, Object object) throws HibernateException {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	public Object replace(Object object, Object object1, Object object2) throws HibernateException {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	public String[] getPropertyNames() {
		return NAMES;
	}

	public Type[] getPropertyTypes() {
		return TYPES;
	}

	public Object getPropertyValue(Object object, int property) throws HibernateException {
		AuditInfo auditInfo = (AuditInfo) object;
		switch (property) {
			case LAST_UPDATED:
				return auditInfo.getLastUpdated();
			case CREATED:
				return auditInfo.getCreated();
			case LAST_UPDATED_BY:
				return auditInfo.getLastUpdatedBy();
			case CREATED_BY:
				return auditInfo.getCreatedBy();
		}
		throw new IllegalArgumentException("Unknown property index: "
				+ property);
	}

	public void setPropertyValue(Object object, int property, Object value) throws HibernateException {
		AuditInfo auditInfo = (AuditInfo) object;
		switch (property) {
		case LAST_UPDATED:
			auditInfo.setLastUpdated((Timestamp) value);
			break;
		case CREATED:
			auditInfo.setCreated((Timestamp) value);
			break;
			case LAST_UPDATED_BY:
				auditInfo.setLastUpdatedBy((String) value);
				break;
			case CREATED_BY:
				auditInfo.setCreatedBy((String) value);
				break;
		default:
			throw new IllegalArgumentException("Unknown property index: "
					+ property);
		}
	}

	public Class returnedClass() {
		return AuditInfo.class;
	}

	public boolean equals(Object x, Object y) {
		if (x == y) return true;
		if (x == null || y == null) return false;
		AuditInfo aix = (AuditInfo) x;
		AuditInfo aiy = (AuditInfo) y;
		try {
			if (aix.getLastUpdated() == null ||
					aiy.getLastUpdated() == null ||
					aix.getCreated() == null ||
					aiy.getCreated() == null ||
					aix.getLastUpdatedBy() == null ||
					aiy.getLastUpdatedBy() == null ||
					aix.getCreatedBy() == null ||
					aiy.getCreatedBy() == null) {
				return false;
			} else {
				return aix.getLastUpdated().equals(aiy.getLastUpdated()) &&
						aix.getCreated().equals(aiy.getCreated()) &&
						aix.getLastUpdatedBy().equals(aiy.getLastUpdatedBy()) &&
						aix.getCreatedBy().equals(aiy.getCreatedBy());
			}
		} catch (HibernateException e) {
			return false;
		}
	}

	public int hashCode(Object object) throws HibernateException {
		return 0;  //To change body of implemented methods use File | Settings | File Templates.
	}

//	public Object nullSafeGet(ResultSet resultSet, String[] strings, SessionImplementor sessionImplementor, Object object) throws HibernateException, SQLException {
//		return null;
//	}

//	public void nullSafeSet(PreparedStatement preparedStatement, Object object, int i, SessionImplementor sessionImplementor) throws HibernateException, SQLException {
//	}

	public Object deepCopy(Object value) {
		if (value == null) return null;
		AuditInfo ai = (AuditInfo) value;
		AuditInfo result = new AuditInfo();
		try {
			result.setLastUpdated(ai.getLastUpdated());
			result.setCreated(ai.getCreated());
			result.setLastUpdatedBy(ai.getLastUpdatedBy());
			result.setCreatedBy(ai.getCreatedBy());
			return result;
		} catch (HibernateException e) {
			e.printStackTrace();
			return result;
		}
	}

	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor sessionImplementor, Object owner)
			throws HibernateException, SQLException {

		//AuditInfo can't be null
		AuditInfo ai = new AuditInfo();
		ai.setLastUpdated((java.sql.Timestamp) Hibernate.TIMESTAMP.nullSafeGet(rs, names[0]));
		ai.setCreated((java.sql.Timestamp) Hibernate.TIMESTAMP.nullSafeGet(rs, names[1]));
		ai.setLastUpdatedBy((String) Hibernate.STRING.nullSafeGet(rs, names[2]));
		ai.setCreatedBy((String) Hibernate.STRING.nullSafeGet(rs, names[3]));
		return ai;
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor sessionImplementor)
			throws HibernateException, SQLException {

		//AuditInfo can't be null
		AuditInfo ai = (AuditInfo) value;
		Hibernate.TIMESTAMP.nullSafeSet(st, ai.getLastUpdated(), index);
		Hibernate.TIMESTAMP.nullSafeSet(st, ai.getCreated(), index + 1);
		Hibernate.STRING.nullSafeSet(st, ai.getLastUpdatedBy(), index + 2);
		Hibernate.STRING.nullSafeSet(st, ai.getCreatedBy(), index + 3);
	}
}