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
 * Date: Nov 8, 2006
 * Time: 8:13:51 PM
 */

package org.archiviststoolkit.util;

import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.FieldNotFoundException;
import org.archiviststoolkit.structure.DatabaseTables;
import org.archiviststoolkit.structure.DatabaseFields;
import org.archiviststoolkit.structure.lookupListSchema.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.model.LookupListItems;
import org.archiviststoolkit.model.LookupList;
import org.archiviststoolkit.importer.ImportUtils;
import org.archiviststoolkit.ApplicationFrame;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.*;
import java.io.File;

import org.archiviststoolkit.model.Subjects;
import org.netbeans.spi.wizard.DeferredWizardResult;
import org.hibernate.LockMode;

public class LookupListUtils {

	public static final String LIST_NAME_INSTANCE_TYPES = "Instance Types";
	public static final String LIST_NAME_NAME_LINK_FUNCTION = "Name link function";
	public static final String LIST_NAME_NAME_LINK_SUBJECT_CREATOR_ROLE = "Name link creator / subject role";
	public static final String LIST_NAME_NAME_LINK_SOURCE_ROLE = "Name link source role";
	public static final String LIST_NAME_NAME_LINK_FORM = "Name link form";

	public static final int EDITABLE = 1;
	public static final int NONEDITABLE = 2;
	public static final int MIXED = 3;

	private static Hashtable<String, TreeSet<String>> lookupLists = new Hashtable<String, TreeSet<String>>();
	private static ArrayList<String> lookupListList = new ArrayList<String>();

	private static Hashtable<String, TreeSet<LookupListItems>> lookupListsObjects = new Hashtable<String, TreeSet<LookupListItems>>();

	private static boolean accumulateIngestReport = false;
	private static TreeMap<String, TreeSet<String>> ingestMap;

	public static void addLookupList(LookupList listToAdd) {
		lookupLists.put(listToAdd.getListName().toLowerCase(), listToAdd.getListItemsWithBlankAtTop());
		lookupListsObjects.put(listToAdd.getListName().toLowerCase(), listToAdd.getListItemsWithBlankAtTopObjects());
		lookupListList.add(listToAdd.getListName());
	}

	public static Vector<String> getLookupListValues(String listName) {
		if (listName != null && listName.length() > 0) {
			TreeSet<String> returnValues = lookupLists.get(listName.toLowerCase());
			if (returnValues == null || returnValues.size() <= 1) {
				ErrorDialog dialog = new ErrorDialog("Lookup list not configured: " + listName);
				dialog.showDialog();
				createListIfNecessary(listName);
				return new Vector<String>();
			} else {
				return new Vector<String>(returnValues);
			}
		} else {
			return new Vector<String>();
		}
	}

	public static Vector<LookupListItems> getLookupListValues2(String listName) {
		if (listName != null && listName.length() > 0) {
			TreeSet<LookupListItems> returnValues = lookupListsObjects.get(listName.toLowerCase());
			if (returnValues == null || returnValues.size() <= 1) {
				ErrorDialog dialog = new ErrorDialog("Lookup list not configured: " + listName);
				dialog.showDialog();
				createListIfNecessary(listName);
				return new Vector<LookupListItems>();
			} else {
				return new Vector<LookupListItems>(returnValues);
			}
		} else {
			return new Vector<LookupListItems>();
		}
	}

	public static Vector<String> getLookupListValues(ATFieldInfo fieldInfo) {
		if (fieldInfo != null) {
			return getLookupListValues(fieldInfo.getLookupList());
		} else {
			return new Vector<String>();
		}
	}

	public static LookupListItems getLookupListItem(String listName, String listItemValue) {
		if (listName != null && listName.length() > 0) {
			TreeSet<LookupListItems> returnValues = lookupListsObjects.get(listName.toLowerCase());
			if (returnValues == null || returnValues.size() <= 1) {
				ErrorDialog dialog = new ErrorDialog("Lookup list not configured: " + listName);
				dialog.showDialog();
				createListIfNecessary(listName);
				return null;
			} else {
				for (LookupListItems item : returnValues) {
					if (item.getListItem().equalsIgnoreCase(listItemValue)) {
						return item;
					}
				}
				return null;
			}
		} else {
			return null;
		}

	}

	public static LookupListItems getLookupListItemByCode(String listName, String code) throws UnknownLookupListException {
		if (listName != null && listName.length() > 0) {
			TreeSet<LookupListItems> returnValues = lookupListsObjects.get(listName.toLowerCase());
			if (returnValues == null || returnValues.size() <= 1) {
				throw new UnknownLookupListException(listName + " does not exist");
			} else {
				for (LookupListItems item : returnValues) {
					if (item.getCode().equalsIgnoreCase(code)) {
						return item;
					}
				}
				return null;
			}
		} else {
			return null;
		}
	}

	public static String getLookupListItemFromCode(Class clazz, String field, String code) {
		ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(clazz.getName(), field);
		if (fieldInfo == null) {
			return null;
		}
		String lookupListName = fieldInfo.getLookupList();
		try {
			LookupListItems lli = LookupListUtils.getLookupListItemByCode(lookupListName, code);
			if (lli != null) {
				return lli.getListItem();
			} else {
				return null;

			}
		}

		catch (UnknownLookupListException ulle) {
			ulle.printStackTrace();
			return null;
		}
	}

	public static String getLookupListItemFromValue(Class clazz, String field, String value) {
		ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(clazz.getName(), field);
		if (fieldInfo == null) {
			return null;
		}
		String lookupListName = fieldInfo.getLookupList();
		LookupListItems lli = LookupListUtils.getLookupListItem(lookupListName, value);
		if (lli != null) {
			return lli.toString();
		} else {
			return null;
		}
	}


	public static String getLookupListCodeFromItem(Class clazz, String field, String itemValue) {
		ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(clazz.getName(), field);
		if (StringHelper.isEmpty(itemValue))
			return "";
		if (fieldInfo == null)
			return "";
		String lookupListName = fieldInfo.getLookupList();
		LookupListItems lli = LookupListUtils.getLookupListItem(lookupListName, itemValue);
		if (lli != null) {
			if (lli.getCode().length() > 0) {
				return lli.getCode();
			} else {
				return StringHelper.convertToNmtoken(lli.getListItem());
			}
		} else
			return "";

	}

	public static Vector<String> getLookupListValues(Class clazz, String fieldName) {
		return getLookupListValues(ATFieldInfo.getFieldInfo(clazz, fieldName));
	}

	private static void createListIfNecessary(String listName) {
		createListIfNecessary(listName, 1);
	}


	private static void createListIfNecessary(String listName, int listType) {
		try {
			DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(LookupList.class);
			if (!lookupLists.containsKey(listName.toLowerCase())) {
				LookupList newList = new LookupList();
				newList.setListName(listName);
				newList.setListType(listType);
				access.add(newList);
				loadLookupLists();
			} else {
				LookupList list = (LookupList) access.findByUniquePropertyValueLongSession(LookupList.PROPERTYNAME_LIST_NAME, listName);
				list.setListType(listType);
				access.updateLongSession(list, true);
			}
		} catch (PersistenceException e) {
			new ErrorDialog("There is a problem initializing lookup lists.", e).showDialog();
		} catch (LookupException e) {
			new ErrorDialog("There is a problem initializing lookup lists.", e).showDialog();
		}

	}

	public static boolean loadLookupLists() {
		try {
			DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(LookupList.class);
			lookupListList = new ArrayList<String>();
			for (Object o : access.findAll(LockMode.READ)) {
				addLookupList((LookupList) o);
			}


		} catch (LookupException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "There is a problem loading lookup lists.", e).showDialog();
			return false;
		} catch (PersistenceException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "There is a problem loading lookup lists.", e).showDialog();
			return false;
		}
		return true;
	}

	public static String[] getLookupListList() {
		Iterator items = lookupListList.iterator();
		SortedSet<String> set = new TreeSet<String>();
		String item;
		set.add("");
		while (items.hasNext()) {
			item = items.next().toString();
			set.add(item);
		}
		return set.toArray(new String[set.size()]);
	}

	public static String addItemToList(String listName, String item) throws UnknownLookupListException {
		return addItemToList(listName, item, false);
	}

	public static String addItemToList(String listName, String item, boolean loadLookupLists) throws UnknownLookupListException {
		TreeSet<LookupListItems> list = lookupListsObjects.get(listName.toLowerCase());

		if (list == null) {
			throw new UnknownLookupListException(listName);
		} else {
			String normalizedValue = null;
			for (LookupListItems listItemObject : list) {
				if (listItemObject.getListItem().equalsIgnoreCase(item)) {
					normalizedValue = listItemObject.getListItem();
					break;
				}
			}
			if (normalizedValue == null) {
				LookupList lookupList = null;
				try {
					LookupListsDAO lookupListDAO = new LookupListsDAO();
					lookupList = lookupListDAO.findLookupListByName(listName);
					lookupList.addListItem(new LookupListItems(lookupList, item));
					lookupListDAO.update(lookupList);
					if (loadLookupLists) {
						loadLookupLists();
					}
					if (accumulateIngestReport) {
						addToIngestReport(listName, item);
					}
				} catch (PersistenceException e) {
					new ErrorDialog("Error adding " + normalizedValue + " to lookup list " + lookupList.getListName(), e).showDialog();
				}
				return item;
			} else {
				return normalizedValue;
			}
		}
	}

	public static String addItemToListByField(Class clazz, String fieldName, String value) throws FieldNotFoundException, UnknownLookupListException {
		if (value == null) {
			return null;
		} else {
			ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(clazz, fieldName);
			String lookupListName = "";
			if (fieldInfo == null) {
				throw new FieldNotFoundException(clazz.getName() + "." + fieldName);
			} else {
				lookupListName = fieldInfo.getLookupList();
				if (fieldInfo.getDataType().equalsIgnoreCase("java.lang.String")) {
					if (lookupListName != null && lookupListName.length() != 0) {
						return addItemToList(lookupListName, value, true);
					} else {
						return value;
					}
				} else {
					return value;
				}
			}
		}
	}

	public static void lookupListCheck(String lookupName, String value) {
		if (value == null) return;
		ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(lookupName);
		String lookupListName = "";
		if (fieldInfo != null)
			lookupListName = fieldInfo.getLookupList();
		if (fieldInfo != null && fieldInfo.getDataType().equalsIgnoreCase("java.lang.String")) {
			if (lookupListName != null && lookupListName.length() != 0) {
				try {
					addItemToList(lookupListName, value);
				}
				catch (UnknownLookupListException ulle) {
					ulle.printStackTrace();
				}
			}
		}
	}

	public static void importLookupLists(File file) {
		importLookupLists(null, file);
	}

	public static void importLookupLists(DeferredWizardResult.ResultProgressHandle progress, File file) {
		if (file != null) {
			JAXBContext context;
			try {
				LookupListsDAO lookupListDAO = new LookupListsDAO();
				DomainAccessObject databaseTablesAccess = DomainAccessObjectFactory.getInstance().getDomainAccessObject(DatabaseTables.class);

				LookupList lookupList;
//				LookupListItems item;
				context = JAXBContext.newInstance("org.archiviststoolkit.structure.lookupListSchema");
				AtLookupLists atLookupLists = (AtLookupLists) context.createUnmarshaller().unmarshal(file);
				DatabaseTables databaseTable;
				DatabaseFields databaseField;
				for (org.archiviststoolkit.structure.lookupListSchema.List list : atLookupLists.getList()) {
					if (progress != null) {
						progress.setProgress(list.getName(), 0, 0);
					}
					createListIfNecessary(list.getName(), list.getListType().intValue());
					lookupList = lookupListDAO.findLookupListByName(list.getName());
					parseValues(list, lookupList, lookupListDAO);

					//deal with what fields use a given list
					for (Usage usage : list.getUsage()) {
						try {
							databaseTable = (DatabaseTables) databaseTablesAccess.findByUniquePropertyValue(DatabaseTables.PROPERTYNAME_CLASS_NAME, usage.getClassName());
							if (databaseTable == null) {
								new ErrorDialog("Table not found: " + usage.getClassName()).showDialog();
							} else {
								databaseField = databaseTable.getDatabaseField(usage.getField());
								if (databaseField == null) {
									new ErrorDialog("Field not found: " + usage.getClassName() + "." + usage.getField()).showDialog();
								} else {
									databaseField.setLookupList(list.getName());
									databaseTablesAccess.update(databaseTable);
								}
							}

						} catch (LookupException e) {
							new ErrorDialog("Table not found: " + usage.getClassName()).showDialog();
						}
					}

				}
			} catch (JAXBException e) {
				new ErrorDialog(ApplicationFrame.getInstance(), "There is a problem loading lookup lists.", e).showDialog();
			} catch (PersistenceException e) {
				new ErrorDialog(ApplicationFrame.getInstance(), "There is a problem loading lookup lists.", e).showDialog();
			}

		}

	}

	private static void parseValues(org.archiviststoolkit.structure.lookupListSchema.List list, LookupList lookupList, LookupListsDAO lookupListDAO) throws PersistenceException {
		boolean editable;
		LookupListItems item;
		if (list.isRestrictToNmtoken() != null && list.isRestrictToNmtoken()) {
			lookupList.setRestrictToNmtoken(true);
		} else {
			lookupList.setRestrictToNmtoken(false);
		}
		//mark all of the items as editable and let the update remark the non
		for (LookupListItems thisItem : lookupList.getListItems()) {
			thisItem.setEditable(true);
		}
		if (list.isPairedValues() != null && list.isPairedValues()) {
			Value value;
			lookupList.setPairedValues(true);
			for (Pair pair : list.getPair()) {
				value = pair.getValue();
				//add the item to the list and then in case the item was already there
				//set its editable value
				if (lookupList.getListType() == LookupListUtils.NONEDITABLE) {
					editable = false;
				} else if (value.isEditable() == null) {
					editable = true;
				} else {
					editable = value.isEditable();
				}
				lookupList.addListItem(value.getContent(), pair.getCode(), editable);
				item = lookupList.getItem(value.getContent());
				if (item.getCode() == null || item.getCode().length() == 0) {
					item.setCode(pair.getCode());
				}
				item.setEditable(editable);
				item.setAtInitialValue(true);
			}
		} else {
			lookupList.setPairedValues(false);
			for (Value value : list.getValue()) {
				//add the item to the list and then in case the item was already there
				//set its editable value
				if (lookupList.getListType() == LookupListUtils.NONEDITABLE) {
					editable = false;
				} else if (value.isEditable() == null) {
					editable = true;
				} else {
					editable = value.isEditable();
				}
				lookupList.addListItem(value.getContent(), editable);
				item = lookupList.getItem(value.getContent());
				item.setEditable(editable);
				item.setAtInitialValue(true);
			}
		}
		lookupListDAO.update(lookupList);
	}

	public static void initIngestReport() {
		accumulateIngestReport = true;
		ingestMap = new TreeMap<String, TreeSet<String>>();
	}

	private static void addToIngestReport(String listName, String value) {
		TreeSet<String> treeSet = ingestMap.get(listName);
		if (treeSet == null) {
			treeSet = new TreeSet<String>();
			ingestMap.put(listName, treeSet);
		}
		treeSet.add(value);
	}

	public static String getIngestReport() {
		accumulateIngestReport = false;
		Set<String> lists = ingestMap.keySet();
		if (lists == null || lists.size() == 0) {
			return "";
		} else {
			String ingestReport = "Items added to lookup lists:\n";
			for (String listName : lists) {
				ingestReport += "\n\t" + listName + "\n";
				for (String item : ingestMap.get(listName)) {
					ingestReport += "\t\t" + item + "\n";
				}
			}
			return ingestReport;
		}
	}


}
