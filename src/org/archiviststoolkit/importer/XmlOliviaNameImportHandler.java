//==============================================================================
//            _            __
//  ___  __ _| | ___  ___ / _| ___  _ __ __ _  ___
// / __|/ _` | |/ _ \/ __| |_ / _ \| '__/ _` |/ _ \
// \__ \ (_| | |  __/\__ \  _| (_) | | | (_| |  __/
// |___/\__,_|_|\___||___/_|  \___/|_|  \__, |\___|
//                                      |___/
//
// 01110011 01100001 01101100 01100101 01110011
// 01100110 01101111 01110010 01100111 01100101
//
//==============================================================================
// Copyright 2004 (c) the salesforge community. All Rights Reserved.
//==============================================================================
// Licensed under the Open Software License version 2.1
//==============================================================================
// $Author: lejames $
// $Date: 2005/01/07 23:30:23 $
// $Revision: 1.7 $
//==============================================================================

package org.archiviststoolkit.importer;

//==============================================================================
// Import Declarations
//==============================================================================

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Iterator;
import java.util.Properties;
import java.io.IOException;

import org.archiviststoolkit.model.Subjects;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.util.LookupListUtils;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import java.io.*;

import org.xml.sax.InputSource;


/**
 * Imports Contact domain objects from CSV files.
 */
public class XmlOliviaNameImportHandler extends XmlImportHandler {

	/**
	 * the identity of the source application.
	 */
	private String identity = null;
	/**
	 * the translation map being used.
	 */
	private Hashtable translationMap = new Hashtable();

	private String sourceLookupListName;

	/**
	 * Constructor.
	 *
	 * @param importName			 the import name.
	 */
	public XmlOliviaNameImportHandler(String importName) {
		super();

		this.setSourceApplication(importName);

		ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(Names.class.getName() + "." + Names.PROPERTYNAME_NAME_SOURCE);
		sourceLookupListName = fieldInfo.getLookupList();
	}

	/**
	 * Can this file be imported.
	 *
	 * @param file the file to import.
	 * @return yes or no
	 */
	public boolean canImportFile(File file) {

//		String identityFromFile = getInitialChunk(file);
//		if (identityFromFile.startsWith(identity)) {
//			return (true);
//		}
//
//		return (false);
		return true;
	}

	/**
	 * Take a row and turn it into a domain object.
	 *
	 * @param dataList the list representing the rows.
	 * @return the domain object.
	 */
	public Object handleRow(Map dataList) {
		Subjects subject = new Subjects();

		for (Object o : dataList.keySet()) {
			String name = (String) o;
			String value = (String) dataList.get(name);
			String translated = (String) translationMap.get(name);

			if (translated == null) {
				translated = "NULL";
			}
			if (translated.equals("term")) {
				subject.setSubjectTerm(value);
			} else if (translated.equals("termType")) {
				//subject.setSubjectTermType(SubjectTermType.value);
			} else if (translated.equals("source")) {
				subject.setSubjectSource(value);
			} else if (translated.equals("scopeNote")) {
				subject.setSubjectScopeNote(value);
			}

		}
		return (subject);
	}

	/**
	 * Map input columns to real columns.
	 *
	 * @param dataList the list to patch.
	 * @return the patched list.
	 */
	public ArrayList mapColumn(ArrayList dataList) {

		for (int loop = 0; loop < dataList.size(); loop ++) {
			String name = (String) dataList.get(loop);

			name = name.replace(' ', '_');
			name = name.toLowerCase();
			name = name.replace('/', '_');
			name = name.replace('-', '_');
			dataList.set(loop, name);

		}

		return (dataList);
	}

	public Object handleInstance(String xmlInstanceString) throws UnknownLookupListException {
        Names name = new Names();
        SAXBuilder builder = new SAXBuilder();

        InputSource input = new InputSource(new StringReader(xmlInstanceString));
        input.setEncoding("UTF-8");
        Document currentRecordJdom = null;
        try {
            currentRecordJdom = builder.build(input);
        } catch (JDOMException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Element root = currentRecordJdom.getRootElement();
        try {
            XPath preferredNameXpath = XPath.newInstance("recordType/PreferredForm");
            Element preferredName = (Element) preferredNameXpath.selectSingleNode(root);

            String corporateFlag = preferredName.getChildTextTrim("corporate");
            if (corporateFlag.equalsIgnoreCase("false")) {
                name.setNameType("Person");
            } else if (corporateFlag.equalsIgnoreCase("true")) {
                name.setNameType("Corporate Body");
            }

            String sourceId = preferredName.getChildTextTrim("source");
            if (sourceId != null) {
                LookupListUtils.addItemToList(sourceLookupListName, sourceId);
                name.setNameSource(sourceId);
            }

            name.setPersonalPrimaryName(getChildTextNullSafe(preferredName,"familyName"));
            name.setPersonalRestOfName(getChildTextNullSafe(preferredName,"givenName"));
            name.setContactCountry(getChildTextNullSafe(preferredName,"country"));
            name.setSortName(getChildTextNullSafe(preferredName,"sortName"));
            name.setPersonalDates(getChildTextNullSafe(preferredName,"dates"));
            name.setPersonalPrefix(getChildTextNullSafe(preferredName,"prefix"));
            name.setCorporatePrimaryName(getChildTextNullSafe(preferredName,"institutionName"));
            name.createSortName();

            String biogHistString = preferredName.getChildTextTrim("biogHist");
            if (biogHistString != null) {
                name.setDescriptionType("Biographical Statement");
                name.setDescriptionNote(biogHistString);
            }


            XPath altFormXpath = XPath.newInstance("recordType/AlternateForm");
            Iterator altForms = altFormXpath.selectNodes(root).iterator();
            Element altFormElement;
            NonPreferredNames altName;
            while (altForms.hasNext()) {
                altFormElement = (Element) altForms.next();
                altName = new NonPreferredNames(name);
                altName.setPersonalPrimaryName(getChildTextNullSafe(altFormElement,"givenName"));
                altName.setPersonalRestOfName(getChildTextNullSafe(altFormElement,"familyName"));
                altName.setNameType(name.getNameType());
                altName.setPersonalPrefix(getChildTextNullSafe(altFormElement,"prefix"));
                altName.setCorporatePrimaryName(getChildTextNullSafe(altFormElement,"institutionName"));
                altName.createSortName();
                name.addNonPreferredName(altName);
            }


        } catch (JDOMException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        return name;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private String getChildTextNullSafe(Element element, String childElementName) {
        String childText = element.getChildTextTrim(childElementName);
        if (childText == null) {
            return "";
        } else {
            return childText;
        }
    }
}

//=============================================================================
// $Log: CsvContactImportHandler.java,v $
// Revision 1.7  2005/01/07 23:30:23  lejames
// Clean up on Javadoc and fixed reporting bug
//
// Revision 1.6  2005/01/04 02:45:19  lejames
// Added new report mechanism, based on table models
//
// Revision 1.5  2004/12/28 02:54:41  lejames
// outlook express is now fully mapped within the new import scheme.
//
// Revision 1.4  2004/12/23 23:36:33  lejames
// Reworked the model to contain a full list of fields
//
// Revision 1.3  2004/12/20 22:32:25  lejames
// Added NAICs level 2 support
//
// Revision 1.2  2004/12/20 00:24:29  lejames
// Added the DomainImportController and used it to manage the import of external data.
// We can now do duplicate, merge and update import operations.
//
// Revision 1.1  2004/12/18 03:22:41  lejames
// First cut of the new import code which can handle comma separated values.
//
//
//=============================================================================
// EOF
//=============================================================================
