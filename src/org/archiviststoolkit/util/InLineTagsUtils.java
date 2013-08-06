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
 * Date: Sep 8, 2008
 * Time: 10:28:16 AM
 */

package org.archiviststoolkit.util;

import org.netbeans.spi.wizard.DeferredWizardResult;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.structure.InLineTagAttributes;
import org.archiviststoolkit.structure.InLineTagEditor;
import org.archiviststoolkit.structure.InLineTags;
import org.archiviststoolkit.structure.NotesEtcTypes;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.ApplicationFrame;
import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.hibernate.LockMode;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class InLineTagsUtils {

	public static final String EVENT_DESCRIPTION = "eventDescription";
	public static final String DEFINITION_LIST_ITEM_VALUE = "definitionListItemValue";
	public static final String ORDERED_LIST_ITEM_VALUE = "orderedListItemValue";
	public static final String INDEX_NOTE = "indexNote";
	public static final String BIB_ITEM_VALUE = "bibItemValue";
	public static final String TITLE = "title";
	public static final String ALL = "all";

	private static TreeSet<InLineTags> inLineTagList;

	private static HashMap<String , TreeSet<InLineTags>> inLineTagListMap;

	private static String[] allTagsStringList = {"blockquote","corpname","date","emph","extref","famname","function","genreform","geogname","name","occupation","persname","ref","subject","title"};
	private static ArrayList<String> allTagsList = new ArrayList<String>(Arrays.asList(allTagsStringList));

	private static String[] restrictedTagStringList1 = {"corpname","date","emph","extref","famname","function","genreform","geogname","name","occupation","persname","ref","subject","title"};
	private static ArrayList<String> restrictedTagList1 = new ArrayList<String>(Arrays.asList(restrictedTagStringList1));

	private static String[] restrictedTagStringList2 = {"emph","extref","ref","title"};
	private static ArrayList<String> restrictedTagList2 = new ArrayList<String>(Arrays.asList(restrictedTagStringList2));

	private static String[] restrictedTagStringListLegalStatusNote = {"date","emph"};
	private static ArrayList<String> restrictedTagListLegalStatusNote = new ArrayList<String>(Arrays.asList(restrictedTagStringListLegalStatusNote));

	private static String[] restrictedTagStringBibItemValue = {"corpname","emph","extref","famname","name","persname","ref","title"};
	private static ArrayList<String> restrictedTagBibItemValue = new ArrayList<String>(Arrays.asList(restrictedTagStringBibItemValue));

	public static void importRecords(File file) {
		importRecords(null, file);
	}

	public static void importRecords(DeferredWizardResult.ResultProgressHandle progress, File file) {

		try {
			DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(InLineTags.class);

			if (file != null) {
				SAXBuilder builder = new SAXBuilder();
				Document document = builder.build(file);
				Element root = document.getRootElement();
				Element tag;
				Element attribute;
				InLineTags inLineTag;
				InLineTagAttributes inLineTagAttribute;
				String tagName;
				for (Object o: root.getChildren("tag")) {
				   	tag = (Element)o;
					tagName = tag.getChild("name").getText();
					if (progress != null) {
						progress.setProgress(tagName, 0, 0);
					}
					inLineTag = (InLineTags)access.findByUniquePropertyValue(InLineTags.PROPERTYNAME_TAG_NAME, tagName);
					if (inLineTag == null) {
						inLineTag = new InLineTags();
						inLineTag.setTagName(tagName);
					}
					for (Object o2: tag.getChildren("attribute")) {
						attribute = (Element)o2;
						inLineTagAttribute = inLineTag.findAttributeByName(attribute.getChild("name").getText());
						if (attribute.getChild("valueList") != null) {
							inLineTagAttribute.setValueList(attribute.getChild("valueList").getText());
						}
					}
					access.update(inLineTag);
				}
			}

		} catch (PersistenceException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "Error importing inline tags", e).showDialog();
		} catch (IOException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "Error importing inline tags", e).showDialog();
		} catch (JDOMException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "Error importing inline tags", e).showDialog();
		} catch (LookupException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "Error importing inline tags", e).showDialog();
		}

	}

	public static boolean loadInLineTags() {

		try {
			DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(InLineTags.class);
			inLineTagList = new TreeSet<InLineTags>();

			InLineTags inLineTag;
			for (Object o : access.findAll(LockMode.READ)) {
				inLineTag = (InLineTags)o;
				inLineTagList.add(inLineTag);
			}
			inLineTagListMap = new HashMap<String , TreeSet<InLineTags>>();
			inLineTagListMap.put(NotesEtcTypes.CANNONICAL_NAME_GENERAL_PHYSICAL_DESCRIPTION, getRestrictedList(restrictedTagList1));
			inLineTagListMap.put(NotesEtcTypes.CANNONICAL_NAME_PHYSICAL_FACET, getRestrictedList(restrictedTagList1));
			inLineTagListMap.put(TITLE, getRestrictedList(restrictedTagList1));

			inLineTagListMap.put(NotesEtcTypes.CANNONICAL_NAME_ABSTRACT, getRestrictedList(restrictedTagList2));
			inLineTagListMap.put(NotesEtcTypes.CANNONICAL_NAME_LANGUAGE_OF_MATERIALS, getRestrictedList(restrictedTagList2));
			inLineTagListMap.put(NotesEtcTypes.CANNONICAL_NAME_DIMENSIONS, getRestrictedList(restrictedTagList2));
			inLineTagListMap.put(NotesEtcTypes.CANNONICAL_NAME_LOCATION_NOTE, getRestrictedList(restrictedTagList2));
			inLineTagListMap.put(NotesEtcTypes.CANNONICAL_NAME_MATERIAL_SPECIFIC_DETAILS, getRestrictedList(restrictedTagList2));

			inLineTagListMap.put(NotesEtcTypes.CANNONICAL_NAME_LEGAL_STATUS_NOTE, getRestrictedList(restrictedTagListLegalStatusNote));

			inLineTagListMap.put(BIB_ITEM_VALUE, getRestrictedList(restrictedTagBibItemValue));

			inLineTagListMap.put(ALL, getRestrictedList(allTagsList));

		} catch (LookupException e) {
			new ErrorDialog("There is a problem loading in-line tags.",
					StringHelper.getStackTrace(e)).showDialog();
			return false;
		} catch (PersistenceException e) {
			new ErrorDialog("There is a problem loading in-line tags.",
					StringHelper.getStackTrace(e)).showDialog();
			return false;
		}
		return true;
	}

	private static TreeSet<InLineTags> getRestrictedList (ArrayList<String> tagsToInclude) {
		TreeSet<InLineTags> returnSet = new TreeSet<InLineTags>();
		for (InLineTags tag: inLineTagList) {
			if (tagsToInclude.contains(tag.getTagName())) {
				returnSet.add(tag);
			}
		}
		return returnSet;
	}

	public static Vector getInLineTagList() {
		return getInLineTagList(ALL);
	}

	public static Vector getInLineTagList(String context) {
		TreeSet<InLineTags> tagList = inLineTagListMap.get(context);
		if (tagList == null) {
			 return getInLineTagList(inLineTagListMap.get(ALL));
		} else {
			return getInLineTagList(tagList);
		}

	}

	public static Vector getInLineTagList(TreeSet<InLineTags> tagList) {
		Vector returnVector = new Vector(tagList);
		returnVector.add(0, "Wrap in tag...");
		return returnVector;
	}

	public static void wrapInTagActionPerformed(JComboBox insertInlineTag,  JTextArea textArea, DomainEditor parentEditor) {
		if (insertInlineTag.getSelectedIndex() > 0) {
			if (textArea.getSelectedText() != null) {
				InLineTags inLineTag = (InLineTags) insertInlineTag.getSelectedItem();
				InLineTagEditor tagEditor = new InLineTagEditor(inLineTag);
				if (inLineTag.getAttributes().size() == 0 || tagEditor.showDialog() == JOptionPane.OK_OPTION) {
					wrapInTag(inLineTag, tagEditor, textArea);
				}
			} else {
				JOptionPane.showMessageDialog(parentEditor, "You must select text to wrap first");
			}
			insertInlineTag.setSelectedIndex(0);
		}

	}

	private static void wrapInTag(InLineTags inLineTag, InLineTagEditor tagEditor, JTextArea textArea) {
		String tagName = inLineTag.getTagName();
		String tagAndAttributes = StringHelper.concat(" ", tagName, tagEditor.constructAttributeString());
		String newText;
		if (tagName.equals("blockquote")) {
			newText = "<" + tagAndAttributes + "><p>" + textArea.getSelectedText() + "</p></" + tagName + ">";
		} else {
			newText = "<" + tagAndAttributes + ">" + textArea.getSelectedText() + "</" + tagName + ">";
		}
		textArea.replaceSelection(newText);
	}



}
