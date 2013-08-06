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
 * Date: Oct 26, 2005
 * Time: 1:28:41 PM
 */

package org.archiviststoolkit.structure;

import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.util.ATBeanUtils;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.ApplicationFrame;
import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.netbeans.spi.wizard.DeferredWizardResult;
import org.hibernate.LockMode;

import javax.swing.*;
import java.util.*;
import java.lang.reflect.Method;
import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;

public class ATFieldInfo implements Comparable {

	private String fieldName = "";
	private Class clazz;
	private Class fieldClass;
	private String fieldLabel = "";
	private String popupHelpText = "";
	private String lookupList = "";
	private Integer returnScreenOrder = 0;
	private String dataType = "";
	private Method getMethod;
	private Method setMethod;
	private Integer stringLengthLimit = null;
	private Boolean includeInRDE = false;

	public ATFieldInfo(Class clazz, String fieldName) {
		this.clazz = clazz;
		this.fieldName = fieldName;
		try {
			getMethod = ATBeanUtils.getReadMethod(clazz, fieldName);
			setMethod = ATBeanUtils.getWriteMethod(clazz, fieldName);
			fieldClass = ATBeanUtils.getPropertyType(clazz, fieldName);
		} catch (IntrospectionException e) {
			new ErrorDialog("", StringHelper.getStackTrace(e)).showDialog();
		}
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldLabel() {
		return fieldLabel;
	}

	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	public String getPopupHelpText() {
		return popupHelpText;
	}

	public void setPopupHelpText(String definition, String examples) {
		if (definition == null) {
			definition = "";
		}
		if (examples == null) {
			examples = "";
		}
		String examplesHelpText = "";
		String definitionHelpText = "";

		if (examples.length() != 0 || definition.length() != 0) {
			if (examples.length() != 0) {
				examplesHelpText = "<h4>Examples</h4><blockquote><table width=\"500\"><tr><td>" + examples.replace("\n", "<br>") + "</td></tr></table></blockquote>";
			}
			if (definition.length() != 0) {
				definitionHelpText = "<h4>Definition</h4><blockquote><table width=\"500\"><tr><td>" + definition.replace("\n", "<br>") + "</td></tr></table></blockquote>";
			}
			this.popupHelpText = "<HTML>" + definitionHelpText + examplesHelpText + "</HTML>";
		}
	}

	private static Hashtable<String, ATFieldInfo> fieldList;
	private static TreeSet<String> tableList;
	private static Hashtable<String, DatabaseTables> tableByTableNameLookup;
	private static Hashtable<String, TreeSet<DatabaseFields>> fieldsByTableLookup;

	public static Object[] getTableList() {
		return tableList.toArray();
	}

	public static Object[] getTableListForDefaultValues() {
		TreeSet<DatabaseTables> tables = new TreeSet<DatabaseTables>();
		for (DatabaseTables table : tableByTableNameLookup.values()) {
			if (!table.getClazz().isAnnotationPresent(ExcludeFromDefaultValues.class)) {
				tables.add(table);
			}
		}
		return addBlankToBeginningOfArray(tables);
	}

	private static Object[] addBlankToBeginningOfArray(Set set) {
		ArrayList temp = new ArrayList(set);
		temp.add(0, "");
		return temp.toArray();
	}

	public static Object[] getFieldListByTableNameForDefaultValues(DatabaseTables table) {
		TreeSet<DatabaseFields> fields = new TreeSet<DatabaseFields>();
		for (DatabaseFields field : table.getDatabaseFields()) {
			if (!field.getExcludeFromDefaultValues()) {
				fields.add(field);
			}
		}
		return addBlankToBeginningOfArray(fields);
	}

	public static Object[] getFieldListByTableName(String tableName) {
		if (tableName == null) {
			return new Object[]{};
		} else {
			TreeSet<DatabaseFields> fieldsByTableName = fieldsByTableLookup.get(tableName);
			if (fieldsByTableName == null) {
				return new Object[]{};
			} else {
				return fieldsByTableName.toArray();
			}
		}
	}

	public static DatabaseTables getTableByTableName(String tableName) {
		return tableByTableNameLookup.get(tableName);
	}

	public static void loadFieldList() {

		fieldList = new Hashtable<String, ATFieldInfo>();
		tableList = new TreeSet<String>();
		fieldsByTableLookup = new Hashtable<String, TreeSet<DatabaseFields>>();
		tableByTableNameLookup = new Hashtable<String, DatabaseTables>();

//		DomainAccessObject access = new DomainAccessObjectImpl(DatabaseTables.class);
		try {
			DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(DatabaseTables.class);
			DatabaseTables table;
			String tableName;
			TreeSet<DatabaseFields> fieldsByTable;
			for (Object o : access.findAll(LockMode.READ)) {
				table = (DatabaseTables) o;

                // need to check the class name to make sure we don't try
                // to load any classes which are none core AT 2.0
                String className = table.getClassName();
                if(className.contains("org.archiviststoolkit")) {
				    tableName = table.getClazz().getSimpleName();
				    tableByTableNameLookup.put(tableName, table);
				    tableList.add(tableName);
				    fieldsByTable = new TreeSet<DatabaseFields>();
				    fieldsByTableLookup.put(tableName, fieldsByTable);
				    addFieldInfo(table, fieldsByTable);
                }
			}

		} catch (LookupException e) {
			new ErrorDialog("Error loading field list", StringHelper.getStackTrace(e)).showDialog();
		} catch (PersistenceException e) {
			new ErrorDialog("Error loading field list", StringHelper.getStackTrace(e)).showDialog();
		}
	}

	public static TreeSet<ATFieldInfo> getReturnScreenList(Class clazz) throws TableNotConfiguredException {

//		addTableInfoIfNecessary(clazz);
		Collection<ATFieldInfo> fields = fieldList.values();
		TreeSet<ATFieldInfo> returnSet = new TreeSet<ATFieldInfo>();
		for (ATFieldInfo field : fields) {
			if (field.getClazz() == clazz && field.getReturnScreenOrder() > 0) {
				returnSet.add(field);
			}
		}
		if (returnSet.size() == 0) {
			throw new TableNotConfiguredException(clazz.getName() + " not configured");
		}
		return returnSet;
	}

	public static ArrayList<ATFieldInfo> getFieldsForRDE(Class clazz) throws TableNotConfiguredException {

//		addTableInfoIfNecessary(clazz);
		Collection<ATFieldInfo> fields = fieldList.values();
		ArrayList<ATFieldInfo> returnSet = new ArrayList<ATFieldInfo>();
		for (ATFieldInfo field : fields) {
			if (field.getClazz() == clazz && field.getIncludeInRDE()) {
				returnSet.add(field);
			}
		}
		if (returnSet.size() == 0) {
			throw new TableNotConfiguredException(clazz.getName() + " not configured");
		}
		return returnSet;
	}

	public static ArrayList<ATFieldInfo> getFieldsThatNeedStringValidation(Class clazz) {

//		addTableInfoIfNecessary(clazz);
		Collection<ATFieldInfo> fields = fieldList.values();
		ArrayList<ATFieldInfo> returnSet = new ArrayList<ATFieldInfo>();
		for (ATFieldInfo field : fields) {
			if (field.getClazz() == clazz && field.getStringLengthLimit() != null && field.getStringLengthLimit() > 0) {
				returnSet.add(field);
			}
		}
		return returnSet;
	}

	public static ATFieldInfo getFieldInfo(String key) {
		return fieldList.get(key);
	}

	public static ATFieldInfo getFieldInfo(Class clazz, String fieldName) {
		return getFieldInfo(clazz.getName() + "." + fieldName);
	}

	public static ATFieldInfo getFieldInfo(String className, String fieldName) {
		return getFieldInfo(className + "." + fieldName);
	}

	public static String getLabel(Class clazz, String fieldName) {
		String key = clazz.getName() + "." + fieldName;
		ATFieldInfo fieldInfo = getFieldInfo(key);
		if (fieldInfo == null) {
			return "";
		} else {
			return fieldInfo.getFieldLabel();
		}
	}

	public static String getToolTip(Class clazz, String fieldName) {
		String key = clazz.getName() + "." + fieldName;
		ATFieldInfo fieldInfo = getFieldInfo(key);
		if (fieldInfo == null) {
			return "";
		} else {
			return fieldInfo.getPopupHelpText();
		}
	}

	public static void assignLabelInfo(JLabel label, Class clazz, String fieldName) {
		String key = clazz.getName() + "." + fieldName;
		ATFieldInfo fieldInfo = getFieldInfo(key);
		if (fieldInfo != null) {
			label.setText(fieldInfo.getFieldLabel());
			label.setToolTipText(fieldInfo.getPopupHelpText());
		}
	}

    /**
     * Method to assign the label tooltip and not rename the label like above
     * @param label The JLabel to assign tool tip to
     * @param clazz The class where to find the information from
     * @param fieldName the name of the field in the data model
     */
    public static void assignLabelTooltip(JLabel label, Class clazz, String fieldName) {
		String key = clazz.getName() + "." + fieldName;
		ATFieldInfo fieldInfo = getFieldInfo(key);
		if (fieldInfo != null) {
			label.setToolTipText(fieldInfo.getPopupHelpText());
		}
	}

	private static void addTableInfoIfNecessary(Class clazz) {
		Collection<ATFieldInfo> fields = fieldList.values();
		for (ATFieldInfo field : fields) {
			if (field.getClazz() == clazz) {
				return;
			}
		}
		//if we got here then there is no info for this class so create it
		DatabaseTables table = new DatabaseTables(clazz);
		table.addFieldInfo();
		addFieldInfo(table);
		DomainAccessObject access = null;
		try {
			access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(DatabaseTables.class);
			access.add(table);
		} catch (PersistenceException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "Error initializing tables", e).showDialog();
		}
	}

	private static void addFieldInfo(DatabaseTables table) {
		addFieldInfo(table, null);
	}

	private static void addFieldInfo(DatabaseTables table, TreeSet<DatabaseFields> fieldsByTable) {
		ATFieldInfo fieldInfo;
		String key;
		for (DatabaseFields field : table.getDatabaseFields()) {
			fieldInfo = new ATFieldInfo(table.getClazz(), field.getFieldName());
			fieldInfo.setFieldLabel(field.getFieldLabel());
			fieldInfo.setPopupHelpText(StringHelper.escapeHTML(field.getDefinition()), StringHelper.escapeHTML(field.getExamples()));
			fieldInfo.setLookupList(field.getLookupList());
			fieldInfo.setReturnScreenOrder(field.getReturnScreenOrder());
			fieldInfo.setDataType(field.getDataType());
			fieldInfo.setStringLengthLimit(field.getStringLengthLimit());
			fieldInfo.setIncludeInRDE(field.getIncludeInRDE());
			key = table.getClassName() + "." + field.getFieldName();
			fieldList.put(key, fieldInfo);
			if (fieldsByTable != null) {
				fieldsByTable.add(field);
			}
		}
	}

	public static ArrayList<ATFieldInfo> getFieldInfoByLookupListName(String listName) {
		ArrayList<ATFieldInfo> returnList = new ArrayList<ATFieldInfo>();
		for (ATFieldInfo fieldInfo : fieldList.values()) {
			if (fieldInfo.getLookupList().equalsIgnoreCase(listName)) {
				returnList.add(fieldInfo);
			}
		}
		return returnList;
	}

	public static void initDatabaseTables() {
		ATFieldInfo.addTableInfoIfNecessary(Accessions.class);
		ATFieldInfo.addTableInfoIfNecessary(AccessionsLocations.class);
		ATFieldInfo.addTableInfoIfNecessary(AccessionsResources.class);
		ATFieldInfo.addTableInfoIfNecessary(ArchDescriptionAnalogInstances.class);
		ATFieldInfo.addTableInfoIfNecessary(ArchDescriptionInstances.class);
		ATFieldInfo.addTableInfoIfNecessary(ArchDescriptionNames.class);
		ATFieldInfo.addTableInfoIfNecessary(ArchDescriptionNotes.class);
		ATFieldInfo.addTableInfoIfNecessary(ArchDescriptionRepeatingData.class);
		ATFieldInfo.addTableInfoIfNecessary(ArchDescriptionSubjects.class);
		ATFieldInfo.addTableInfoIfNecessary(BibItems.class);
		ATFieldInfo.addTableInfoIfNecessary(Bibliography.class);
		ATFieldInfo.addTableInfoIfNecessary(ChronologyItems.class);
		ATFieldInfo.addTableInfoIfNecessary(ChronologyList.class);
		ATFieldInfo.addTableInfoIfNecessary(DatabaseFields.class);
		ATFieldInfo.addTableInfoIfNecessary(DatabaseTables.class);
		ATFieldInfo.addTableInfoIfNecessary(Deaccessions.class);
		ATFieldInfo.addTableInfoIfNecessary(DefaultValues.class);
		ATFieldInfo.addTableInfoIfNecessary(DigitalObjects.class);
		ATFieldInfo.addTableInfoIfNecessary(DigitalObjectsResources.class);
		ATFieldInfo.addTableInfoIfNecessary(Events.class);
		ATFieldInfo.addTableInfoIfNecessary(ExternalReference.class);
		ATFieldInfo.addTableInfoIfNecessary(FileVersions.class);
		ATFieldInfo.addTableInfoIfNecessary(Index.class);
		ATFieldInfo.addTableInfoIfNecessary(IndexItems.class);
		ATFieldInfo.addTableInfoIfNecessary(ListDefinition.class);
		ATFieldInfo.addTableInfoIfNecessary(ListDefinitionItems.class);
		ATFieldInfo.addTableInfoIfNecessary(ListOrdered.class);
		ATFieldInfo.addTableInfoIfNecessary(ListOrderedItems.class);
		ATFieldInfo.addTableInfoIfNecessary(Locations.class);
		ATFieldInfo.addTableInfoIfNecessary(LookupList.class);
		ATFieldInfo.addTableInfoIfNecessary(LookupListItems.class);
		ATFieldInfo.addTableInfoIfNecessary(NameContactNotes.class);
		ATFieldInfo.addTableInfoIfNecessary(Names.class);
		ATFieldInfo.addTableInfoIfNecessary(NonPreferredNames.class);
		ATFieldInfo.addTableInfoIfNecessary(NotesEtcTypes.class);
		ATFieldInfo.addTableInfoIfNecessary(Repositories.class);
		ATFieldInfo.addTableInfoIfNecessary(RepositoryNotes.class);
		ATFieldInfo.addTableInfoIfNecessary(RepositoryNotesDefaultValues.class);
		ATFieldInfo.addTableInfoIfNecessary(Resources.class);
		ATFieldInfo.addTableInfoIfNecessary(ResourcesComponents.class);
		ATFieldInfo.addTableInfoIfNecessary(Subjects.class);
		ATFieldInfo.addTableInfoIfNecessary(Users.class);
		ATFieldInfo.addTableInfoIfNecessary(RepositoryStatistics.class);
		ATFieldInfo.addTableInfoIfNecessary(RDEScreen.class);
		ATFieldInfo.addTableInfoIfNecessary(RDEScreenPanels.class);
		ATFieldInfo.addTableInfoIfNecessary(RDEScreenPanelItems.class);
		ATFieldInfo.addTableInfoIfNecessary(ResourcesComponentsSearchResult.class);
        ATFieldInfo.addTableInfoIfNecessary(Assessments.class);
        ATFieldInfo.addTableInfoIfNecessary(AssessmentsSearchResult.class);
        ATFieldInfo.addTableInfoIfNecessary(AssessmentsResources.class);
        ATFieldInfo.addTableInfoIfNecessary(AssessmentsAccessions.class);
        ATFieldInfo.addTableInfoIfNecessary(AssessmentsDigitalObjects.class);
        ATFieldInfo.addTableInfoIfNecessary(ArchDescriptionDates.class);
        ATFieldInfo.addTableInfoIfNecessary(ArchDescriptionPhysicalDescriptions.class);
	}

	public static void importFieldDefinitionsAndExamples(File file) {
		importFieldDefinitionsAndExamples(file, null, 0, 0, false);
	}

	public static void importFieldDefinitionsAndExamples(File file,
														 DeferredWizardResult.ResultProgressHandle progress,
														 int currentStep,
														 int numberOfSteps,
														 boolean preserveExistingFieldLabels) {
		if (file != null) {
			try {
				DomainAccessObject databaseTablesAccess = DomainAccessObjectFactory.getInstance().getDomainAccessObject(DatabaseTables.class);
				SAXBuilder builder = new SAXBuilder();
				Document document = builder.build(file);
				Element root = document.getRootElement();
				Element record;
				String tableName;
				String fieldName;
				DatabaseTables databaseTable;
				DatabaseFields databaseField;
				for (Object o : root.getChildren("RECORD")) {
					record = (Element) o;
					tableName = record.getChildText("ClassName");
					fieldName = record.getChildText("PropertyName");
					databaseTable = (DatabaseTables) databaseTablesAccess.findByUniquePropertyValue(DatabaseTables.PROPERTYNAME_TABLE_NAME, tableName);
					if (databaseTable != null) {
						databaseField = databaseTable.getDatabaseField(fieldName);
						if (databaseField != null) {
//							if (databaseField.getFieldName().equalsIgnoreCase(ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER1_ALPHA_NUMERIC_INDICATOR)) {
//								System.out.println();
//							}
							if (progress != null) {
								progress.setProgress(databaseTable.getTableName() + ":" + databaseField.getFieldName(), currentStep, numberOfSteps);
							}
							if (record.getChildText("Examples") != null) {
								databaseField.setExamples(record.getChildText("Examples"));
							} else {
								databaseField.setExamples("");
							}
							if (record.getChildText("Definition") != null) {
								databaseField.setDefinition(record.getChildText("Definition"));
							} else {
								databaseField.setDefinition("");
							}
							if (!preserveExistingFieldLabels || databaseField.getFieldLabel().length() == 0) {
								if (record.getChildText("Element_Label") != null) {
									databaseField.setFieldLabel(record.getChildText("Element_Label"));
								} else if (databaseField.getFieldLabel().length() == 0) {
									//the field label is blank
									databaseField.setFieldLabel(StringHelper.makePrettyName(databaseField.getFieldName()));
								}
							}
							databaseTablesAccess.update(databaseTable);
						} else {
							JOptionPane.showMessageDialog(ApplicationFrame.getInstance(), "Field: " + tableName + ":" + fieldName + " was not found.");
						}
					} else {
						JOptionPane.showMessageDialog(ApplicationFrame.getInstance(), "Table: " + tableName + " was not found.");
					}
				}
			} catch (PersistenceException e) {
				new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
			} catch (LookupException e) {
				new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
			} catch (IOException e) {
				new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
			} catch (JDOMException e) {
				new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
			}
		}

	}

	public static void fixBlankFieldLabels(DeferredWizardResult.ResultProgressHandle progress,
										   int currentStep,
										   int numberOfSteps) {
		try {
			DomainAccessObject databaseTablesAccess = DomainAccessObjectFactory.getInstance().getDomainAccessObject(DatabaseTables.class);
			DatabaseTables table;
			for (Object o : databaseTablesAccess.findAll()) {
				table = (DatabaseTables) o;
				for (DatabaseFields field : table.getDatabaseFields()) {
					if (field.getFieldLabel().length() == 0) {
						//the field label is blank
						field.setFieldLabel(StringHelper.makePrettyName(field.getFieldName()));
						if (progress != null) {
							progress.setProgress(table.getTableName() + ":" + field.getFieldName(), currentStep, numberOfSteps);
						}
					}
				}
				databaseTablesAccess.update(table);
			}
		} catch (PersistenceException e) {
			new ErrorDialog("Error accessing database", e).showDialog();
		} catch (LookupException e) {
			new ErrorDialog("Error accessing database", e).showDialog();
		}

	}

	public static void restoreDefaults(DeferredWizardResult.ResultProgressHandle progress) {
		initDatabaseTables();
		try {
			DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(DatabaseTables.class);
			for (DatabaseTables table : tableByTableNameLookup.values()) {
				if (progress != null) {
					progress.setProgress(table.getTableName(), 0, 0);
				}
				table.addFieldInfo();
				access.update(table);
			}
		} catch (PersistenceException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "Error restoring default values", e).showDialog();
		}
	}

	public String getLookupList() {
		return lookupList;
	}

	public void setLookupList(String lookupList) {
		this.lookupList = lookupList;
	}

	public Integer getReturnScreenOrder() {
		return returnScreenOrder;
	}

	public void setReturnScreenOrder(Integer returnScreenOrder) {
		this.returnScreenOrder = returnScreenOrder;
	}

	/**
	 * Compares this object with the specified object for order. Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object.<p>
	 * <p/>
	 * In the foregoing description, the notation
	 * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
	 * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
	 * <tt>0</tt>, or <tt>1</tt> according to whether the value of <i>expression</i>
	 * is negative, zero or positive.
	 * <p/>
	 * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
	 * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>. (This
	 * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
	 * <tt>y.compareTo(x)</tt> throws an exception.)<p>
	 * <p/>
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
	 * <tt>x.compareTo(z)&gt;0</tt>.<p>
	 * <p/>
	 * Finally, the implementer must ensure that <tt>x.compareTo(y)==0</tt>
	 * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
	 * all <tt>z</tt>.<p>
	 * <p/>
	 * It is strongly recommended, but <i>not</i> strictly required that
	 * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>. Generally speaking, any
	 * class that implements the <tt>Comparable</tt> interface and violates
	 * this condition should clearly indicate this fact. The recommended
	 * language is "Note: this class has a natural ordering that is
	 * inconsistent with equals."
	 *
	 * @param o the Object to be compared.
	 * @return a negative integer, zero, or a positive integer as this object
	 *         is less than, equal to, or greater than the specified object.
	 * @throws ClassCastException if the specified object's type prevents it
	 *                            from being compared to this Object.
	 */
	public int compareTo(Object o) {
		ATFieldInfo temp = (ATFieldInfo) o;
		return this.getReturnScreenOrder().compareTo(temp.getReturnScreenOrder());  //To change body of implemented methods use File | Settings | File Templates.
	}

	public boolean equals(Object o) {
		ATFieldInfo temp = (ATFieldInfo) o;
		return this.getReturnScreenOrder().equals(temp.getReturnScreenOrder());
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

    // Method to return the table name, which is just the class name
    public String getTableName() {
        String fullname = clazz.getName();
        int index = fullname.lastIndexOf(".") + 1;
        String name = fullname.substring(index);
        return name;
    }

    public Method getGetMethod() {
		return getMethod;
	}

	public void setGetMethod(Method getMethod) {
		this.getMethod = getMethod;
	}

	public Class getFieldClass() {
		return fieldClass;
	}

	public void setFieldClass(Class fieldClass) {
		this.fieldClass = fieldClass;
	}

	public static void reconcileFieldDefinitionsAndExamples(File file,
															DeferredWizardResult.ResultProgressHandle progress,
															int currentStep,
															int numberOfSteps) {
		if (file != null) {
			try {
				SAXBuilder builder = new SAXBuilder();
				Document document = builder.build(file);
				Element root = document.getRootElement();
				Element record;
				String tableName;
				String fieldName;
				DatabaseFields fieldInfo;
				HashSet<String> fieldList = new HashSet<String>();
				for (Object o : root.getChildren("RECORD")) {
					record = (Element) o;
					tableName = record.getChildText("TableName");
					fieldName = record.getChildText("FieldName");
					fieldList.add(tableName + "." + fieldName);
				}

				String fieldNameFromAt;
				for (String tableNameFromAt : fieldsByTableLookup.keySet()) {
					TreeSet<DatabaseFields> fields = fieldsByTableLookup.get(tableNameFromAt);
					for (Object o : fields.toArray()) {
						fieldInfo = (DatabaseFields) o;
						fieldNameFromAt = fieldInfo.getFieldName();

						if (progress != null) {
							progress.setProgress(tableNameFromAt + ":" + fieldNameFromAt, currentStep, numberOfSteps);
						}

						if (fieldNameFromAt.equalsIgnoreCase("created") ||
								fieldNameFromAt.equalsIgnoreCase("createdBy") ||
								fieldNameFromAt.equalsIgnoreCase("lastUpdated") ||
								fieldNameFromAt.equalsIgnoreCase("lastUpdatedBy")) {
							//ignore autid info
						} else if (fieldNameFromAt.endsWith("Id") &&
								fieldInfo.getDataType() != null &&
								fieldInfo.getDataType().equalsIgnoreCase("java.lang.Long")) {
							//ignore id fields
						} else if (!fieldList.contains(tableNameFromAt + "." + fieldNameFromAt)) {
							System.out.println(tableNameFromAt + "\t" + fieldNameFromAt + "\t" + fieldInfo.getDataType());
						}
					}
				}

			} catch (IOException e) {
				new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
			} catch (JDOMException e) {
				new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
			}
		}

	}

	public static void updateFieldInfoForAllTables() {
		try {
			DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(DatabaseTables.class);
			for (DatabaseTables table : tableByTableNameLookup.values()) {
				table.addFieldInfo();
				access.update(table);
			}
		} catch (PersistenceException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "Error initializing tables", e).showDialog();
		}
	}

	public static int checkFieldLength(Class clazz, String fieldName) {
		ATFieldInfo fieldInfo = getFieldInfo(clazz, fieldName);
		if (fieldInfo == null) {
			return -1;
		} else if (fieldInfo.getStringLengthLimit() != null && fieldInfo.getStringLengthLimit() > 0) {
			return fieldInfo.getStringLengthLimit();
		} else {
			return -1;
		}
	}

	public Integer getStringLengthLimit() {
		return stringLengthLimit;
	}

	public void setStringLengthLimit(Integer stringLengthLimit) {
		this.stringLengthLimit = stringLengthLimit;
	}

	public Method getSetMethod() {
		return setMethod;
	}

	public void setSetMethod(Method setMethod) {
		this.setMethod = setMethod;
	}

	public Boolean getIncludeInRDE() {
		return includeInRDE;
	}

	public void setIncludeInRDE(Boolean includeInRDE) {
		this.includeInRDE = includeInRDE;
	}
}
