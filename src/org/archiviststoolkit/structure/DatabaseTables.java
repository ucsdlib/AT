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
 */

package org.archiviststoolkit.structure;

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.util.StringHelper;

import java.util.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.jgoodies.binding.beans.Model;

@ExcludeFromDefaultValues
public class DatabaseTables extends DomainObject {

	public static final String PROPERTYNAME_TABLE_NAME = "tableName";
	public static final String PROPERTYNAME_CLASS_NAME = "className";

	private Long tableId;
	@IncludeInApplicationConfiguration(1)
	private String tableName = "";
	private String className = "";

	private Set<DatabaseFields> databaseFields = new HashSet<DatabaseFields>();

	/**
	 * Creates a new instance of database tables
	 */
	public DatabaseTables() {
	}

	public DatabaseTables(Class clazz) {
		this.className = clazz.getName();
		this.tableName = clazz.getSimpleName();
	}

	public String toString() {
		return getTableName();
	}

	public Long getTableId() {
		return tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public String getTableName() {
		if (this.tableName != null) {
			return this.tableName;
		} else {
			return "";
		}
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Long getIdentifier() {
		return this.getTableId();
	}

	public void setIdentifier(Long identifier) {
		this.setTableId(identifier);
	}


	public Set<DatabaseFields> getDatabaseFields() {
		return databaseFields;
	}

	public void setDatabaseFields(Set<DatabaseFields> databaseFields) {
		this.databaseFields = databaseFields;
	}

	public void addDatabaseField(DatabaseFields field) {
		if (field == null)
			throw new IllegalArgumentException("Can't add a null database field.");
		this.getDatabaseFields().add(field);

	}

	public void removeDatabaseField(DatabaseFields field) {
		if (field == null)
			throw new IllegalArgumentException("Can't add a null database field.");
		getDatabaseFields().remove(field);
	}

	public DatabaseFields getDatabaseField(int index) {
		ArrayList<DatabaseFields> fields = new ArrayList<DatabaseFields>(getDatabaseFields());
		return fields.get(index);
	}

	public DatabaseFields getDatabaseField(String fieldName) {
		DatabaseFields fieldToReturn = null;
		for (DatabaseFields field : databaseFields) {
			if (field.getFieldName().equalsIgnoreCase(fieldName)) {
				return field;
			}
		}
		return fieldToReturn;
	}

	public String getClassName() {
		if (this.className != null) {
			return this.className;
		} else {
			return "";
		}
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Class getClazz() {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			ErrorDialog dialog = new ErrorDialog("Class not found: " + className,
					StringHelper.getStackTrace(e));
			dialog.showDialog();
			return null;
		}
	}

	public void setClassName(Class clazz) {
		this.setClassName(clazz.getName());
	}

	private void recurseThroughSuperClasses(Class clazz) {
		if (clazz != Model.class) {
			createFieldInfoRecords(clazz);
			recurseThroughSuperClasses(clazz.getSuperclass());
		}
	}

	private void createFieldInfoRecords(Class clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(IncludeInApplicationConfiguration.class)) {
				Integer stringLengthLimit = null;
				if (field.isAnnotationPresent(StringLengthValidationRequried.class)) {
					stringLengthLimit = field.getAnnotation(StringLengthValidationRequried.class).value();
				}

				createOrUpdateDatabaseField(field,
						field.getAnnotation(IncludeInApplicationConfiguration.class).value(),
						field.isAnnotationPresent(DefaultIncludeInSearchEditor.class),
						field.isAnnotationPresent(ExcludeFromDefaultValues.class),
						field.isAnnotationPresent(IncludeInRDE.class),
						stringLengthLimit);
			}
		}

		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(IncludeMethodInApplicationConfiguration.class)) {
				createOrUpdateDatabaseField(
						method.getAnnotation(IncludeMethodInApplicationConfiguration.class).fieldName(),
						method.getAnnotation(IncludeMethodInApplicationConfiguration.class).fieldType(),
						method.getAnnotation(IncludeMethodInApplicationConfiguration.class).value(),
						method.isAnnotationPresent(DefaultIncludeInSearchEditor.class),
						method.isAnnotationPresent(ExcludeFromDefaultValues.class),
						method.isAnnotationPresent(IncludeInRDE.class));
			}
		}
	}

	private void createOrUpdateDatabaseField(String fieldName,
											 String dataType,
											 int returnScreenOrder,
											 Boolean includeInSearchEditor,
											 Boolean excludeFromDefaultValues,
											 Boolean includeInRDE) {
		DatabaseFields databaseField = this.getDatabaseField(fieldName);
		if (databaseField == null) {
			databaseField = new DatabaseFields(this);
			databaseField.setFieldName(fieldName);
			databaseField.setDataType(dataType);
			databaseField.setReturnScreenOrder(returnScreenOrder);
			databaseField.setIncludeInSearchEditor(includeInSearchEditor);
			databaseField.setExcludeFromDefaultValues(excludeFromDefaultValues);
			databaseField.setIncludeInRDE(includeInRDE);
			databaseField.setKeep(true);
			this.addDatabaseField(databaseField);
		} else {
			databaseField.setFieldName(fieldName);
			databaseField.setDataType(dataType);
			databaseField.setIncludeInSearchEditor(includeInSearchEditor);
			databaseField.setReturnScreenOrder(returnScreenOrder);
			databaseField.setExcludeFromDefaultValues(excludeFromDefaultValues);
			databaseField.setIncludeInRDE(includeInRDE);
			databaseField.setKeep(true);
		}
	}

	private void createOrUpdateDatabaseField(Field field, int returnScreenOrder,
											 Boolean includeInSearchEditor,
											 Boolean excludeFromDefaultValues,
											 Boolean includeInRDE,
											 Integer stringLenghtLimit) {
		DatabaseFields databaseField = this.getDatabaseField(field.getName());
		if (databaseField == null) {
			databaseField = new DatabaseFields(this);
			databaseField.setFieldName(field.getName());
			databaseField.setDataType(field.getType().getName());
			databaseField.setIncludeInSearchEditor(includeInSearchEditor);
			databaseField.setReturnScreenOrder(returnScreenOrder);
			databaseField.setExcludeFromDefaultValues(excludeFromDefaultValues);
			databaseField.setIncludeInRDE(includeInRDE);
			databaseField.setKeep(true);
			databaseField.setStringLengthLimit(stringLenghtLimit);
			this.addDatabaseField(databaseField);
		} else {
			databaseField.setFieldName(field.getName());
			databaseField.setDataType(field.getType().getName());
			databaseField.setIncludeInSearchEditor(includeInSearchEditor);
			databaseField.setReturnScreenOrder(returnScreenOrder);
			databaseField.setExcludeFromDefaultValues(excludeFromDefaultValues);
			databaseField.setIncludeInRDE(includeInRDE);
			databaseField.setKeep(true);
			databaseField.setStringLengthLimit(stringLenghtLimit);
		}
	}

	public void addFieldInfo() {

		try {
			Class clazz = Class.forName(this.getClassName());
			//set keep flag to false for all fields
			for (DatabaseFields field : this.getDatabaseFields()) {
				field.setKeep(false);
			}

            recurseThroughSuperClasses(clazz.getSuperclass());
            createFieldInfoRecords(clazz);
			//recurseThroughSuperClasses(clazz.getSuperclass());

			//remove any fields that are not marked to keep
			ArrayList<DatabaseFields> fieldsToRemove = new ArrayList<DatabaseFields>();
			for (DatabaseFields field2 : this.getDatabaseFields()) {
				if (!field2.getKeep()) {
					fieldsToRemove.add(field2);
				}
			}
			for (DatabaseFields field3 : fieldsToRemove) {
				this.removeDatabaseField(field3);
			}
		} catch (ClassNotFoundException e) {
			new ErrorDialog("", StringHelper.getStackTrace(e)).showDialog();
		}
	}

	public void reorderReturnScreenList() {
		List<DatabaseFields> fields = new ArrayList<DatabaseFields>(databaseFields);
		Collections.sort(fields, new Comparator<DatabaseFields>() {
			public int compare(DatabaseFields o, DatabaseFields o1) {
				return o.getReturnScreenOrder().compareTo(o1.getReturnScreenOrder());
			}
		});
		int returnScreenOrder = 1;
		for (DatabaseFields field: fields) {
			if (field.getReturnScreenOrder() > 0) {
				field.setReturnScreenOrder(returnScreenOrder++);
			}
		}
	}
}
