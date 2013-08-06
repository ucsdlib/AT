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
 * Date: Jun 6, 2007
 * Time: 10:29:42 AM
 */

package org.archiviststoolkit.util;

import org.netbeans.spi.wizard.DeferredWizardResult;
import org.archiviststoolkit.importer.ImportUtils;
import org.archiviststoolkit.mydomain.DomainAccessObject;
import org.archiviststoolkit.mydomain.DomainAccessObjectFactory;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.mydomain.LookupException;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.structure.NotesEtcTypes;
import org.archiviststoolkit.structure.EAD.*;
import org.archiviststoolkit.model.UnsupportedRepeatingDataTypeException;
import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.hibernate.LockMode;

import java.util.*;
import java.io.File;
import java.io.IOException;

public class NoteEtcTypesUtils {

	private static Hashtable<String, NotesEtcTypes> notesEtcTypesList;
	private static Hashtable<String, NotesEtcTypes> notesEtcTypesByCannonicalNameList;
	private static Set<NotesEtcTypes> digitalObjectsNotesTypesValues;
	private static Set<NotesEtcTypes> notesEtcTypesValues;
	private static Set<NotesEtcTypes> notesOnlyTypesValues;
	private static Set<NotesEtcTypes> notesEmbededTypesValues;
	private static HashMap eadMap = null;

	public static void importRecords(DeferredWizardResult.ResultProgressHandle progress) {
		File file = ImportUtils.chooseFile(null);
		if (file != null) {
			importRecords(progress, file);
		}
	}

	public static void importRecords(File file) {
		importRecords(null, file);
	}

	public static void importRecords(DeferredWizardResult.ResultProgressHandle progress, File file) {

		try {
			if (progress != null) {
				progress.setProgress("Connecting to database", 0, 0);
			}
			DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(NotesEtcTypes.class);

			if (file != null) {
				SAXBuilder builder = new SAXBuilder();
				Document document = builder.build(file);
				Element root = document.getRootElement();
				Element note;
				NotesEtcTypes noteEtc;
				String noteName;
				for (Object o : root.getChildren("note")) {
					note = (Element) o;
					noteName = note.getChild("name").getText();
					if (progress != null) {
						progress.setProgress(noteName, 0, 0);
					}
					noteEtc = (NotesEtcTypes) access.findByUniquePropertyValue(NotesEtcTypes.PROPERTYNAME_REPEATING_DATA_NAME, noteName);
					if (noteEtc == null) {
						noteEtc = new NotesEtcTypes();
						noteEtc.setNotesEtcName(noteName);
					}
					noteEtc.setNotesEtcLabel(note.getChild("label").getText());
					noteEtc.setClassName(note.getChild("className").getText());
					noteEtc.setRepeatingDataType(note.getChild("type").getText());
					noteEtc.setEmbeded(Boolean.parseBoolean(note.getChild("embeded").getText()));
					noteEtc.setAllowsMultiPart(Boolean.parseBoolean(note.getChild("allowsMultiPart").getText()));
					noteEtc.setIncludeInDigitalObjects(Boolean.parseBoolean(note.getChild("includeInDigitalObjects").getText()));

					access.update(noteEtc);
				}
			}

		} catch (PersistenceException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
		} catch (IOException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
		} catch (JDOMException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
		} catch (LookupException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
		}

	}

	public static boolean loadNotesEtcTypes() {

		try {
			DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(NotesEtcTypes.class);
			notesEtcTypesList = new Hashtable<String, NotesEtcTypes>();
			notesEtcTypesByCannonicalNameList = new Hashtable<String, NotesEtcTypes>();
			digitalObjectsNotesTypesValues = new TreeSet<NotesEtcTypes>();
			notesEtcTypesValues = new TreeSet<NotesEtcTypes>();
			notesOnlyTypesValues = new TreeSet<NotesEtcTypes>();
			notesEmbededTypesValues = new TreeSet<NotesEtcTypes>();

			NotesEtcTypes noteEtcType;
			for (Object o : access.findAll(LockMode.READ)) {
				noteEtcType = (NotesEtcTypes) o;
				notesEtcTypesList.put(noteEtcType.getNotesEtcLabel(), noteEtcType);
				notesEtcTypesByCannonicalNameList.put(noteEtcType.getNotesEtcName(), noteEtcType);
				if (!noteEtcType.getEmbeded()) {
					notesEtcTypesValues.add(noteEtcType);
				} else {
					notesEmbededTypesValues.add(noteEtcType);
				}
				if (noteEtcType.getRepeatingDataType().equalsIgnoreCase(NotesEtcTypes.DATA_TYPE_NOTE) && !noteEtcType.getEmbeded()) {
					notesOnlyTypesValues.add(noteEtcType);
					if (noteEtcType.getIncludeInDigitalObjects()) {
						digitalObjectsNotesTypesValues.add(noteEtcType);
					}
				}
			}

		} catch (LookupException e) {
			new ErrorDialog("There is a problem loading lookup lists.",
					StringHelper.getStackTrace(e)).showDialog();
			return false;
		} catch (PersistenceException e) {
			new ErrorDialog("There is a problem loading lookup lists.",
					StringHelper.getStackTrace(e)).showDialog();
			return false;
		}
		return true;
	}

	public static Vector<NotesEtcTypes> getNotesEtcTypesList() {
		return getNotesEtcTypesList(false);
	}

	public static Vector<NotesEtcTypes> getNotesEtcTypesList(boolean addBlankAtTop) {
		Vector<NotesEtcTypes> returnVector = new Vector<NotesEtcTypes>(notesEtcTypesValues);
		if (addBlankAtTop) {
			returnVector.add(0, null);
		}
		return returnVector;
	}

	public static Vector<NotesEtcTypes> getDigitalObjectNotesTypesList() {
		return new Vector<NotesEtcTypes>(digitalObjectsNotesTypesValues);
	}

	public static Vector<NotesEtcTypes> getNotesOnlyTypesList() {
		return getNotesOnlyTypesList(false);
	}

	public static Vector<NotesEtcTypes> getNotesOnlyTypesList(boolean addBlankAtTop) {
		Vector<NotesEtcTypes> returnVector = new Vector<NotesEtcTypes>(notesOnlyTypesValues);
		if (addBlankAtTop) {
			returnVector.add(0, null);
		}
		return returnVector;
	}

	public static Vector<NotesEtcTypes> getNotesEmbeddedTypesList() {
		return new Vector<NotesEtcTypes>(notesEmbededTypesValues);
	}

	public static String lookupRepeatingDataType(String noteEtcLabel) throws UnsupportedRepeatingDataTypeException {
		NotesEtcTypes noteEtcType = notesEtcTypesList.get(noteEtcLabel);
		if (noteEtcType == null) {
			throw new UnsupportedRepeatingDataTypeException(noteEtcLabel);
		} else {
			return noteEtcType.getRepeatingDataType();
		}
	}

	public static NotesEtcTypes lookupNoteEtcTypeByCannonicalName(String cannonicalName) throws UnsupportedRepeatingDataTypeException {
		NotesEtcTypes noteEtcType = notesEtcTypesByCannonicalNameList.get(cannonicalName);
		if (noteEtcType == null) {
			throw new UnsupportedRepeatingDataTypeException(cannonicalName);
		} else {
			return noteEtcType;
		}
	}

	public static Boolean doesNoteEtcTypeAllowMultiPart(String cannonicalName) throws UnsupportedRepeatingDataTypeException {
		return lookupNoteEtcTypeByCannonicalName(cannonicalName).getAllowsMultiPart();
	}

	public static String lookupDefaultTitleByCannonicalName(String cannonicalName) throws UnsupportedRepeatingDataTypeException {
		NotesEtcTypes noteEtcType = notesEtcTypesByCannonicalNameList.get(cannonicalName);
		if (noteEtcType == null) {
			throw new UnsupportedRepeatingDataTypeException(cannonicalName);
		} else {
			//todo this will be retrieved from repositories
			return "";
		}
	}

	public static Class lookupRepeatingDataClass(NotesEtcTypes noteEtcType) throws UnsupportedRepeatingDataTypeException {
//		NotesEtcTypes noteEtcType = notesEtcTypesList.get(noteEtcLabel);
		if (noteEtcType == null) {
			throw new UnsupportedRepeatingDataTypeException(noteEtcType.getNotesEtcLabel());
		} else {
			try {
				return Class.forName(noteEtcType.getClassName());
			} catch (ClassNotFoundException e) {
				throw new UnsupportedRepeatingDataTypeException(noteEtcType.getNotesEtcLabel(), e);
			}
		}
	}

	public static Collection<NotesEtcTypes> getNotesEtcTypes() {
		return notesEtcTypesList.values();
	}

	public static String getNoteTypeFromEAD(Object note) {
		if (eadMap == null)
			setEADToNoteTypeMap();
		//if(getNotesEtcTypesList().contains(type))
		return (String) eadMap.get(note.getClass());
		//else
		//return null;
	}

	private static void setEADToNoteTypeMap() {
		eadMap = new HashMap();
		eadMap.put(Abstract.class, "Abstract");
		eadMap.put(Accruals.class, "Accruals note");
		eadMap.put(Appraisal.class, "Appraisal note");
		eadMap.put(Arrangement.class, "Arrangement note");
		eadMap.put(Bibliography.class, "Bibliography");
		eadMap.put(Bioghist.class, "Biographical/Historical note");
		eadMap.put(Chronlist.class, "Chronology");
		eadMap.put(Accessrestrict.class, "Conditions Governing Access note");
		eadMap.put(Userestrict.class, "Conditions Governing Use note");
		eadMap.put(Custodhist.class, "Custodial History note");
		eadMap.put(Dimensions.class, "Dimensions note");
		eadMap.put(Altformavail.class, "Existence and Location of Copies note");
		eadMap.put(Originalsloc.class, "Existence and Location of Originals note");
		eadMap.put(Fileplan.class, "File Plan note");
		eadMap.put(Daodesc.class, "General note");
		eadMap.put(Note.class, "General note");
		eadMap.put(Odd.class, "General note");
		eadMap.put(Physdesc.class, "General Physical Description note");
		eadMap.put(Acqinfo.class, "Immediate Source of Acquisition note");
		eadMap.put(Index.class, "Index");
		eadMap.put(Langmaterial.class, "Language of Materials note");
		eadMap.put(Legalstatus.class, "Legal Status note");
		eadMap.put(Daodesc.class, "General note");
		
		//eadMap.put("List: definition");
		//eadMap.put("List: ordered");
		//map.put("Location note");
		eadMap.put(Materialspec.class, "Material Specific Details note");
		eadMap.put(Otherfindaid.class, "Other Finding Aids note");
		eadMap.put(Phystech.class, "Physical Characteristics and Technical Requirements note");
		eadMap.put(Physfacet.class, "Physical Facet note");
		eadMap.put(Prefercite.class, "Preferred Citation note");
		eadMap.put(Processinfo.class, "Processing Information note");
		eadMap.put(Relatedmaterial.class, "Related Archival Materials note");
		eadMap.put(Scopecontent.class, "Scope and Contents note");
		eadMap.put(Separatedmaterial.class, "Separated Materials note");
		eadMap.put(String.class, "Text");


	}


}
