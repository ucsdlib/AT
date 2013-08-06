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
 * @author Lee Mandell
 */

package org.archiviststoolkit.structure;

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.mydomain.DomainAccessObject;
import org.archiviststoolkit.mydomain.DomainAccessObjectImpl;
import org.archiviststoolkit.mydomain.LookupException;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.DatabaseFields;
import org.archiviststoolkit.model.Repositories;
import org.archiviststoolkit.model.RepositoryNotesDefaultValues;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.util.ATBeanUtils;
import org.archiviststoolkit.ApplicationFrame;
import org.hibernate.LockMode;

import java.util.Date;
import java.util.Hashtable;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.beans.IntrospectionException;

@ExcludeFromDefaultValues
public class DefaultValues  extends DomainObject {

	// Names of the Bound Bean Properties *************************************

	public static final String PROPERTYNAME_AT_FIELD = "atField";
	public static final String PROPERTYNAME_TABLE_NAME = "tableName";
	public static final String PROPERTYNAME_STRING_VALUE = "stringValue";
	public static final String PROPERTYNAME_TEXT_VALUE = "textValue";
	public static final String PROPERTYNAME_LONG_VALUE = "longValue";
	public static final String PROPERTYNAME_DOUBLE_VALUE = "doubleValue";
	public static final String PROPERTYNAME_INT_VALUE = "intValue";
	public static final String PROPERTYNAME_BOOLEAN_VALUE = "booleanValue";
	public static final String PROPERTYNAME_DATE_VALUE = "dateValue";
	public static final String PROPERTYNAME_VALUE_AS_STRING = "valueAsString";


	private Long defaultValueId;

	private String stringValue;
	private String textValue;
	private Long longValue;
	private Double doubleValue;
	private Integer intValue;
	private Boolean booleanValue;
	private Date dateValue;

	private Method writeMethod;
	private Method readMethod;

	@IncludeInApplicationConfiguration(1)
	private DatabaseFields atField;

	@IncludeInApplicationConfiguration(2)
	private String tableName;

	private Repositories repository;

	private static Hashtable<Repositories, Hashtable<String, ArrayList<DefaultValues>>> repositoryDefaultValues;
	private static Hashtable<Repositories, Hashtable<NotesEtcTypes, RepositoryNotesDefaultValues>> repositoryNotesDefaultValues;

	public static void initDefaultValueLookup() {
		repositoryDefaultValues = new Hashtable<Repositories, Hashtable<String, ArrayList<DefaultValues>>>();
		repositoryNotesDefaultValues = new Hashtable<Repositories, Hashtable<NotesEtcTypes, RepositoryNotesDefaultValues>>();

		try {
			DomainAccessObject access = new DomainAccessObjectImpl(DefaultValues.class);
			DefaultValues defaultValue;
			Repositories repository ;
			Hashtable<String, ArrayList<DefaultValues>> tableLookup;
			ArrayList<DefaultValues> defaultValueList;

			for (Object o : access.findAll(LockMode.READ)) {
				defaultValue = (DefaultValues) o;

                // check to the table class name to make sure only AT related default values
                // values are loaded
                String className = defaultValue.getAtField().getDatabaseTable().getClassName();
                if(!className.contains("org.archiviststoolkit")) {
                    continue;
                }

				repository = defaultValue.getRepository();
				tableLookup = repositoryDefaultValues.get(repository);
				if (tableLookup == null) {
					tableLookup = new Hashtable<String, ArrayList<DefaultValues>>();
					repositoryDefaultValues.put(repository, tableLookup);
				}

				defaultValueList = tableLookup.get(defaultValue.getTableName());
				if (defaultValueList == null) {
					defaultValueList = new ArrayList<DefaultValues>();
					tableLookup.put(defaultValue.getTableName(), defaultValueList);
				}
				defaultValue.setReadWriteMethodFromField();
				defaultValueList.add(defaultValue);
			}

			access = new DomainAccessObjectImpl(RepositoryNotesDefaultValues.class);
			RepositoryNotesDefaultValues repositoryNoteDefaultValue;
			Hashtable<NotesEtcTypes, RepositoryNotesDefaultValues> noteLookup;
			for (Object o : access.findAll(LockMode.READ)) {
				repositoryNoteDefaultValue = (RepositoryNotesDefaultValues) o;
				repository = repositoryNoteDefaultValue.getRepository();
				noteLookup = repositoryNotesDefaultValues.get(repository);
				if (noteLookup == null) {
					noteLookup = new Hashtable<NotesEtcTypes, RepositoryNotesDefaultValues>();
					repositoryNotesDefaultValues.put(repository, noteLookup);
				}
				noteLookup.put(repositoryNoteDefaultValue.getNoteType(), repositoryNoteDefaultValue);
			}



		} catch (LookupException e) {
			new ErrorDialog("Error loading default values", StringHelper.getStackTrace(e)).showDialog();
		} catch (IntrospectionException e) {
			new ErrorDialog("Error loading default values", StringHelper.getStackTrace(e)).showDialog();
		}
	}

	public static ArrayList<DefaultValues> getDefaultValueList(Repositories repository, String tableName) {
		if (repository == null) {
			return new ArrayList<DefaultValues>();
		} else {
			Hashtable<String, ArrayList<DefaultValues>> tableLookup = repositoryDefaultValues.get(repository);
			if (tableLookup == null) {
				return new ArrayList<DefaultValues>();
			} else {
				ArrayList<DefaultValues> defaultValueList = tableLookup.get(tableName);
				if (defaultValueList == null) {
					return new ArrayList<DefaultValues>();
				} else {
					return defaultValueList;
				}
			}
		}
	}

	public static DefaultValues getDefaultValue(Repositories repository, String tableName, String fieldName) {
		if (repository == null) {
			return null;
		} else {
			Hashtable<String, ArrayList<DefaultValues>> tableLookup = repositoryDefaultValues.get(repository);
			if (tableLookup == null) {
				return null;
			} else {
				ArrayList<DefaultValues> defaultValueList = tableLookup.get(tableName);
				if (defaultValueList == null) {
					return null;
				} else {
					for (DefaultValues value: defaultValueList) {
						if (value.getAtField().getFieldName().equalsIgnoreCase(fieldName)) {
							return value;
						}
					}
					return null;
				}
			}
		}		
	}

	public static RepositoryNotesDefaultValues getRepoistoryNoteDefaultValue(Repositories repository, NotesEtcTypes noteType) {
		if (repository == null) {
			return null;
		} else {
			Hashtable<NotesEtcTypes, RepositoryNotesDefaultValues> noteLookup = repositoryNotesDefaultValues.get(repository);
			if (noteLookup == null) {
				return null;
			} else {
				return noteLookup.get(noteType);
			}
		}
	}

	public static void assignDefaultValues(DomainObject model) {
		ArrayList<DefaultValues> defaultValueList =
				DefaultValues.getDefaultValueList(ApplicationFrame.getInstance().getCurrentUserRepository(), model.getClass().getSimpleName());
		for (DefaultValues value : defaultValueList) {
			try {
				boolean fieldEmpty = true;
				Object existingValue = value.getReadMethod().invoke(model);
				if (existingValue != null) {
					if (existingValue instanceof String) {
						if (((String)existingValue).length() > 0) {
							fieldEmpty = false;
						}
					} else if (existingValue instanceof Integer) {
						if ((Integer)existingValue != 0) {
							fieldEmpty = false;
						}
					} else if (existingValue instanceof Double) {
						if ((Double)existingValue != 0) {
							fieldEmpty = false;
						}
					}
				}
				if (fieldEmpty) {
					value.getWriteMethod().invoke(model, value.getValue());
				}
			} catch (IllegalAccessException e) {
				new ErrorDialog("Error setting default value for field: " + value.getAtField().getFieldName(), StringHelper.getStackTrace(e)).showDialog();
			} catch (InvocationTargetException e) {
				new ErrorDialog("Error setting default value for field: " + value.getAtField().getFieldName(), StringHelper.getStackTrace(e)).showDialog();
			} catch (Exception e) {
				new ErrorDialog("Error setting default value for field: " + value.getAtField().getFieldName(), StringHelper.getStackTrace(e)).showDialog();
			}
		}
	}


	/** Creates a new instance of Users */
	public DefaultValues() {
   }

	public DefaultValues(Repositories repository) {
		this.repository = repository;
	}

	public void nullValueFields() {
		stringValue = null;
		textValue = null;
		longValue = null;
		doubleValue = null;
		intValue = null;
		booleanValue = null;
		dateValue = null;
	}

	@IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_VALUE_AS_STRING, value = 3)
	public String getValueAsString() {

		if (this.getStringValue() != null && this.getStringValue().length() > 0) {
			return this.getStringValue();

		} else if (this.getTextValue() != null) {
			return this.getTextValue().toString();

		} else if (this.getLongValue() != null) {
			return this.getLongValue().toString();

		} else if (this.getDoubleValue() != null) {
			return this.getDoubleValue().toString();

		} else if (this.getIntValue() != null) {
			return this.getIntValue().toString();

		} else if (this.getBooleanValue() != null) {
			return this.getBooleanValue().toString();

		} else if (this.getDateValue() != null) {
			return this.getDateValue().toString();

		} else if (this.getLongValue() != null) {
			return this.getLongValue().toString();

		} else {
			return "";
		}
	}

	public Object getValue() {

		if (this.getStringValue() != null && this.getStringValue().length() > 0) {
			return this.getStringValue();

		} else if (this.getTextValue() != null) {
			return this.getTextValue();

		} else if (this.getLongValue() != null) {
			return this.getLongValue();

		} else if (this.getDoubleValue() != null) {
			return this.getDoubleValue();

		} else if (this.getIntValue() != null) {
			return this.getIntValue();

		} else if (this.getBooleanValue() != null) {
			return this.getBooleanValue();

		} else if (this.getDateValue() != null) {
			return this.getDateValue();

		} else if (this.getLongValue() != null) {
			return this.getLongValue();

		} else {
			return null;
		}
	}

	public Long getIdentifier() {
		return this.getDefaultValueId();
	}

	public void setIdentifier(Long identifier) {
		this.setDefaultValueId(identifier);
	}

	public Repositories getRepository() {
		return repository;
	}

	public void setRepository(Repositories repository) {
		this.repository = repository;
	}

	public Long getDefaultValueId() {
		return defaultValueId;
	}

	public void setDefaultValueId(Long defaultValueId) {
		this.defaultValueId = defaultValueId;
	}

	public String getStringValue() {
		if (this.stringValue != null) {
			return this.stringValue;
		} else {
			return "";
		}
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public Long getLongValue() {
		return longValue;
	}

	public void setLongValue(Long longValue) {
		this.longValue = longValue;
	}

	public Double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
	}

	public Integer getIntValue() {
		return intValue;
	}

	public void setIntValue(Integer intValue) {
		this.intValue = intValue;
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public Date getDateValue() {
		return dateValue;
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	public DatabaseFields getAtField() {
		return atField;
	}

	public void setAtField(DatabaseFields field) {
		this.atField = field;
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

	public Method getWriteMethod() {
		return writeMethod;
	}

	public void setWriteMethod(Method writeMethod) {
		this.writeMethod = writeMethod;
	}

	public void setReadWriteMethodFromField() throws IntrospectionException {
		this.writeMethod = ATBeanUtils.getWriteMethod(atField.getDatabaseTable().getClazz(), atField.getFieldName());
		this.readMethod = ATBeanUtils.getReadMethod(atField.getDatabaseTable().getClazz(), atField.getFieldName());
	}

	public Method getReadMethod() {
		return readMethod;
	}

	public void setReadMethod(Method readMethod) {
		this.readMethod = readMethod;
	}

	public String getTextValue() {
		return textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}
}
