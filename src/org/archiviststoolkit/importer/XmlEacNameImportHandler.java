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

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import java.io.*;

import org.xml.sax.InputSource;


/**
 * Imports Contact domain objects from CSV files.
 */
public class XmlEacNameImportHandler extends XmlImportHandler {

	/**
	 * the identity of the source application.
	 */
	private String identity = null;
	/**
	 * the translation map being used.
	 */
	private Hashtable translationMap = new Hashtable();

	/**
	 * Constructor.
	 *
	 * @param importName			 the import name.
	 * @param identityName		   the identity name.
	 * @param translationMapLocation the translation map to use.
	 */
	public XmlEacNameImportHandler(String importName) {
		super();

		this.setSourceApplication(importName);
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

		Iterator iterator = dataList.keySet().iterator();

		while (iterator.hasNext()) {
			String name = (String) iterator.next();
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

	public Object handleInstance(String xmlInstanceString) {
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
		System.out.println(root.toString());
		try {
			XPath ownerCode = XPath.newInstance("eacheader/eacid/@ownercode");
			String ownerCodeString = ownerCode.valueOf(root);
			System.out.println("Owner code: " + ownerCodeString);

			XPath recordType = XPath.newInstance("@type");
			String recordTypeString = recordType.valueOf(root);
			System.out.println("Record Type: " + recordType);
			if (recordTypeString.equalsIgnoreCase("persname")){
				name.setNameType("Person");
			} else if (recordTypeString.equalsIgnoreCase("famname")) {
				name.setNameType("Family");
			} else if (recordTypeString.equalsIgnoreCase("corpname")) {
				name.setNameType("Corporate Body");
			}

			XPath rules = XPath.newInstance("eacheader/ruledecl/rule/@id");
			String rulesId = rules.valueOf(root);
			if (rulesId != null) {
				System.out.println("Rules: " + rulesId);
//				name.setNameRule(NameRules.valueOf(rulesId.toUpperCase()).toString());
			}


			XPath source = XPath.newInstance("eacheader/sourcedecl/source/@id");
			String sourceId = source.valueOf(root);
			if (sourceId != null) {
				System.out.println("Source: " + sourceId);
//				name.setNameSource(NameSources.valueOf(sourceId.toUpperCase()).toString());
			}

			XPath nameElementXpath = XPath.newInstance("condesc/identity/pershead");
			Iterator nameElements = nameElementXpath.selectNodes(root).iterator();
			Element nameElement;
			String authorized = null;
			String surname = null;
			String forname = null;
			String partType;
			Iterator parts;
			Element part;
			NonPreferredNames altName;
			while (nameElements.hasNext()) {
				surname = "";
				forname = "";
				nameElement = (Element)nameElements.next();
				authorized = nameElement.getAttributeValue("authorized");
				rulesId = nameElement.getAttributeValue("rule");
				parts = nameElement.getChildren("part").iterator();
				while(parts.hasNext()) {
					part = (Element)parts.next();
					partType = part.getAttributeValue("type");
					if (partType == null){
						surname = part.getText().trim();
					}else if(partType.equalsIgnoreCase("surname")) {
						surname = part.getText().trim();
					} else if(partType.equalsIgnoreCase("forname") ||
							partType.equalsIgnoreCase("forename")) {
						forname = part.getText().trim();
					}
				}
				if (authorized != null && authorized.equalsIgnoreCase(ownerCodeString)) {
					name.setPersonalPrimaryName(forname);
					name.setPersonalRestOfName(surname);
				} else {
					altName = new NonPreferredNames(name);
					altName.setPersonalPrimaryName(forname);
					altName.setPersonalRestOfName(surname);
					altName.setNameType(name.getNameType());
					name.addNonPreferredName(altName);
				}
			}

			String sortName = name.getPersonalRestOfName() + " " + name.getPersonalPrimaryName();
			name.setSortName(sortName.trim());

			XPath dates = XPath.newInstance("condesc/identity/nameadds/existdate");
			String datesString = dates.valueOf(root);
			name.setPersonalDates(datesString);

			XPath biogHist = XPath.newInstance("condesc/desc/bioghist/p");
			Iterator biogHistPs = biogHist.selectNodes(root).iterator();
			String biogHistString = "";

			if (biogHistPs.hasNext()) {
				biogHistString = ((Element)biogHistPs.next()).getText();
				while (biogHistPs.hasNext()) {
					biogHistString +="\n\n" + ((Element)biogHistPs.next()).getText();
				}

				name.setDescriptionType("Biographical Statement");
				name.setDescriptionNote(biogHistString);
			}

		} catch (JDOMException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}


		return name;  //To change body of implemented methods use File | Settings | File Templates.
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
