package org.archiviststoolkit.importer;

//==============================================================================
// Import Declarations
//==============================================================================

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.security.NoSuchAlgorithmException;

import org.archiviststoolkit.model.*;
import org.archiviststoolkit.exceptions.ValidationException;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.NameUtils;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.swing.ImportOptionsAccessions;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Imports Contact domain objects from CSV files.
 */
public class   DelimitedAccessionImportHandler extends TabImportHandler {

	public static final String RESOURCE_CREATION_NONE = "Do not create resource record";
	public static final String RESOURCE_CREATION_STUB = "Create resource record with resource id only";
	public static final String RESOURCE_CREATION_FULL = "Create resource record using all fields";

	private Repositories repository;

	private String dateFormat = "d/M/yyyy";

	private String resourceCreation = "";

//	private String ingestDateStamp = DateFormat.getDateInstance(DateFormat.SHORT).format(new Date());


	/**
	 * Constructor.
	 */
	public DelimitedAccessionImportHandler(ImportOptionsAccessions importOptions) {
		super();
		this.repository = importOptions.getRepository();
		this.dateFormat = importOptions.getDateFormat();
		this.resourceCreation = importOptions.getResourceRecordCreation();
	}

	/**
	 * Can this file be imported.
	 *
	 * @param file the file to import.
	 * @return yes or no
	 */
	public boolean canImportFile(File file) {
		return (true);
	}


	/**
	 * Take a row and turn it into a domain object.
	 *
	 * @param dataList the list representing the rows.
	 * @return the domain object.
	 */
	public Object handleRow(ArrayList dataList) throws ImportException, ImportBadLookupException, UnknownLookupListException {
		Accessions accession = new Accessions();
		accession.setRepository(repository);
		HashMap<String, Object> map = new HashMap<String, Object>();
		String dataValue;
		String fieldName = "";
		String lookupListName;
		Names name = null;
		String nameRole = "";
		String nameFunction = "";
		String nameForm = "";
		ATFieldInfo fieldInfo;
		Date dateValue;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		ArrayList<String> subjectList = null;
		String subjectTermType = "";
		String subjectSource = "ingest";
		String resourceIdentifier = "";

		controller.incrementTotalRecordCount();
		for (int i = 0; i < dataList.size(); i++) {
			dataValue = ((String) dataList.get(i)).trim();
			try {
				fieldName = (String) this.fieldList.get(i);
			} catch (RuntimeException e) {
				throw new ImportException("Error Precession row", e);
			}
			fieldInfo = ATFieldInfo.getFieldInfo(Accessions.class.getName() + "." + fieldName);
			if (fieldInfo == null) {
				lookupListName ="";
			} else {
				lookupListName = fieldInfo.getLookupList();
			}

			if (fieldName.equalsIgnoreCase("accessionNumber")) {
				parseAccessionNumber(accession, dataValue);
            } else if (fieldName.equalsIgnoreCase("subjectTerm")) {
                subjectList = parseSubjects(dataValue);
            } else if (fieldName.equalsIgnoreCase("subjectTermType")) {
                subjectTermType = dataValue;
			} else if (fieldName.equalsIgnoreCase("subjectSource")) {
				subjectSource = dataValue;
			} else if (fieldName.equalsIgnoreCase("resourceIdentifier")) {
				resourceIdentifier = dataValue.trim();
			} else if (fieldName.equalsIgnoreCase("nameFunction")) {
				nameFunction = LookupListUtils.addItemToList(LookupListUtils.LIST_NAME_NAME_LINK_FUNCTION, dataValue);
			} else if (fieldName.equalsIgnoreCase("nameRole")) {
				//this has to be normalized downstream
				nameRole = dataValue;
			} else if (fieldName.equalsIgnoreCase("nameForm")) {
				nameForm = LookupListUtils.addItemToList(LookupListUtils.LIST_NAME_NAME_LINK_FORM, dataValue);
			} else if (fieldName.startsWith("Name_")) {
				name = accumulateNameInfo(name, fieldName, dataValue);
			} else if (fieldName.equalsIgnoreCase("dateExpression")) {
				parseDate(accession, dataValue);
				map.put(fieldName, StringHelper.convertPipeToCarriageReturn(dataValue));
			} else if (fieldInfo != null && fieldInfo.getDataType().equalsIgnoreCase("java.util.Date")) {
				try {
					if (dataValue.length() != 0) {
						dateValue = sdf.parse(dataValue);
						map.put(fieldName, dateValue);
					}
				} catch (ParseException e) {
					throw new ImportException("Error Parsing date", dataValue, e);
				}
			} else if (dataValue.length() != 0) {
				if (fieldInfo != null && fieldInfo.getDataType().equalsIgnoreCase("java.lang.String")) {
					//for string fields check to see if there is a lookup list and if so verify that this is a valid value
//					dataValue = this.checkForLookupList(fieldName, dataValue, fieldInfo.getLookupList());
					String stringDataValue = (String)dataValue;
					if (fieldInfo.getStringLengthLimit() != null && fieldInfo.getStringLengthLimit() > 0) {
						stringDataValue = StringHelper.trimToLength(stringDataValue, fieldInfo.getStringLengthLimit());
					}
					if (lookupListName.length() != 0) {
						dataValue = LookupListUtils.addItemToList(lookupListName, stringDataValue);
					}
					map.put(fieldName, StringHelper.convertPipeToCarriageReturn(stringDataValue));
				} else {
					map.put(fieldName, StringHelper.convertPipeToCarriageReturn(dataValue));
				}
			}

		}

		try {
			BeanUtils.populate(accession, map);
		} catch (Exception e) {
			String currentRow = "";
			for (int i = 0; i < dataList.size(); i++) {
				currentRow += "\n" + this.fieldList.get(i) + ": " + ((String) dataList.get(i)).trim();
			}
			throw new ImportException("Error Precession row", currentRow, e);
		}

		if (name != null) {
			if (name.getSortName().length() == 0) {
				name.createSortName();
			}
			if (name.getNameType().length() == 0) {
				String currentRow = "";
				for (int i = 0; i < dataList.size(); i++) {
					currentRow += "\n" + this.fieldList.get(i) + ": " + ((String) dataList.get(i)).trim();
				}
				throw new ImportException("Missing name type", currentRow);
			}
			if (name.getSortName().length() == 0) {
				String currentRow = "";
				for (int i = 0; i < dataList.size(); i++) {
					currentRow += "\n" + this.fieldList.get(i) + ": " + ((String) dataList.get(i)).trim();
				}
				throw new ImportException("Missing primary name", currentRow);
			}
			try {
				if (nameRole.length() > 0) {
					//normailize it
					if (nameFunction.equalsIgnoreCase(ArchDescriptionNames.PROPERTYNAME_FUNCTION_SOURCE)) {
						nameRole = LookupListUtils.addItemToList(LookupListUtils.LIST_NAME_NAME_LINK_SOURCE_ROLE, nameRole);
					} else {
						nameRole = LookupListUtils.addItemToList(LookupListUtils.LIST_NAME_NAME_LINK_SUBJECT_CREATOR_ROLE, nameRole);
					}
				}
				addName(accession, name, nameFunction, nameRole, nameForm);
			} catch (PersistenceException e) {
				throw new ImportException("Error adding name record", name.getSortName(), e);
			} catch (NoSuchAlgorithmException e) {
				throw new ImportException("Error adding name record", name.getSortName(), e);
			} catch (UnsupportedEncodingException e) {
				throw new ImportException("Error adding name record", name.getSortName(), e);
			}
		}

		if (subjectList != null) {
			try {
				addSubjects(accession, subjectList, subjectTermType, subjectSource);
			} catch (PersistenceException e) {
				String currentRow = "";
				for (int i = 0; i < dataList.size(); i++) {
					currentRow += "\n" + this.fieldList.get(i) + ": " + ((String) dataList.get(i)).trim();
				}
				throw new ImportException("Error adding subjects", currentRow);
			} catch (ValidationException e) {
				String currentRow = "";
				for (int i = 0; i < dataList.size(); i++) {
					currentRow += "\n" + this.fieldList.get(i) + ": " + ((String) dataList.get(i)).trim();
				}
				throw new ImportException("Error adding subjects. Subject does not meet validaton rules. " + e.getMessage(), currentRow);
			}
		}
		addResource(accession, resourceIdentifier);

		return (accession);
	}

//	private String checkForLookupList(String fieldName,
//									  String dataValue,
//									  String lookupListName) throws ImportBadLookupException {
//		Vector<String> values = LookupList.getLookupListValues(lookupListName);
//		if (values == null || values.size() == 0) {
//			return dataValue;
//		} else {
//			for (String value : values) {
//				if (dataValue.equalsIgnoreCase(value)) {
//					return value;
//				}
//			}
//			//if we got here then we have an invalid value
//			throw new ImportBadLookupException("Value not in lookup list. Field Name: " + fieldName + " Value: " + dataValue);
//		}
//	}

	private void parseAccessionNumber(Accessions accession, String value) {
		StringTokenizer st = new StringTokenizer(value, "./-");
		int count = 0;
		String thisToken;
		while (st.hasMoreTokens()) {
			count++;
			thisToken = (String) st.nextElement();
			if (count == 1) {
				accession.setAccessionNumber1(thisToken);
			} else if (count == 2) {
				accession.setAccessionNumber2(thisToken);
			} else if (count == 3) {
				accession.setAccessionNumber3(thisToken);
			} else if (count == 4) {
				accession.setAccessionNumber4(thisToken);
			}

		}
	}

	private Names accumulateNameInfo(Names name, String ImportFieldName, String dataValue) throws ImportException {

		if (dataValue.length() > 0) {
			if (name == null) {
				name = new Names();
			}
			String fieldName = ImportFieldName.replaceAll("Name_", "");
			try {
				if (fieldName.equalsIgnoreCase("nameType")) {
					String nameType = "";
					if (dataValue.startsWith("family") || dataValue.startsWith("Family")) {
						nameType = Names.FAMILY_TYPE;
					} else if (dataValue.startsWith("corp") || dataValue.startsWith("Corp")) {
						nameType = Names.CORPORATE_BODY_TYPE;
					} else if (dataValue.startsWith("pers") || dataValue.startsWith("Pers")) {
						nameType = Names.PERSON_TYPE;
					}
					BeanUtils.setProperty(name, fieldName, nameType);
				} else {
					int stringLengthLimit = ATFieldInfo.checkFieldLength(Names.class, fieldName);
					if (stringLengthLimit != -1) {
						dataValue = StringHelper.trimToLength(dataValue, stringLengthLimit);
					}
					BeanUtils.setProperty(name, fieldName, dataValue);
				}
			} catch (Exception e) {
				throw new ImportException("Error processing name", fieldName + ": " + dataValue, e);
			}
		}
		return name;
	}

	private ArrayList<String> parseSubjects(String value) {
		String[] result = value.split("\\|");
		String thisValue;

		ArrayList<String> subjectList = null;
		if (result.length > 0) {
			subjectList = new ArrayList<String>();
            for (String aResult : result) {
                thisValue = aResult.trim();
                if (thisValue.length() != 0) {
                    subjectList.add(thisValue);
                }
            }
		}
		return subjectList;
	}

	private void addSubjects(Accessions accession,
							 ArrayList<String> subjectList,
							 String subjectTermType,
							 String subjectSource) throws PersistenceException, ValidationException, UnknownLookupListException {
        SubjectsDAO subjectDao = new SubjectsDAO();
        Subjects subject;
        ArchDescriptionSubjects accessionSubject;

		//trim term type and source
		subjectSource = StringHelper.trimToLength(subjectSource, ATFieldInfo.checkFieldLength(Subjects.class, Subjects.PROPERTYNAME_SUBJECT_SOURCE));
		subjectTermType = StringHelper.trimToLength(subjectTermType, ATFieldInfo.checkFieldLength(Subjects.class, Subjects.PROPERTYNAME_SUBJECT_SOURCE));

		for (String thisSubject : subjectList) {
			thisSubject = StringHelper.trimToLength(thisSubject, ATFieldInfo.checkFieldLength(Subjects.class, Subjects.PROPERTYNAME_SUBJECT_TERM));
			subject = subjectDao.lookupSubject(thisSubject, subjectTermType, subjectSource, true);
            accessionSubject = new ArchDescriptionSubjects(subject, accession);
            accession.addSubject(accessionSubject);
        }
    }

	private void addName(Accessions accession, Names name, String nameFunction, String nameRole, String nameForm) throws PersistenceException, UnknownLookupListException, NoSuchAlgorithmException, UnsupportedEncodingException {
        NamesDAO nameDao = new NamesDAO();
        ArchDescriptionNames accessionName;

        if (name.getNameSource().length() == 0) {
            name.setNameSource("ingest");
        }
		NameUtils.setMd5Hash(name);
		name = nameDao.lookupName(name, true);
        accessionName = new ArchDescriptionNames(name, accession);
		accessionName.setNameLinkFunction(nameFunction);
		accessionName.setRole(nameRole);
		accessionName.setForm(nameForm);
        try {
            accession.addName(accessionName);
        } catch (DuplicateLinkException e) {
            //do nothing this is just a duplicate name
        }
    }

	private void addResource(Accessions accession, String resourceId) throws ImportException {

		if (resourceId.length() > 0) {
			ResourcesDAO resourceDao = new ResourcesDAO();
			Resources resource = null;
			if (resourceCreation.equalsIgnoreCase(RESOURCE_CREATION_NONE)) {
				resource = resourceDao.lookupResource(resourceId, false, repository);
			} else {
				resource = resourceDao.lookupResource(resourceId, true, repository);
			}
			if (resourceDao.getNewRecordCreated() && resourceCreation.equalsIgnoreCase(RESOURCE_CREATION_FULL)) {
				try {
					resource = (Resources)resourceDao.findByPrimaryKeyLongSession(resource.getIdentifier());
					resource.populateResourceFromAccession(accession);
					resourceDao.updateLongSession(resource);

				} catch (LookupException e) {
					throw new ImportException("Error creating resource: " + resourceId + ", " + e.getMessage(), e);
				} catch (PersistenceException e) {
					throw new ImportException("Error creating resource: " + resourceId + ", " + e.getMessage(), e);
				} catch (DuplicateLinkException e) {
					throw new ImportException("Error creating resource: duplicate link" + resourceId + ", " + e.getMessage(), e);
				}
			}
			if (resource != null) {
				AccessionsResources accessionResource = new AccessionsResources(resource, accession);
				accession.addAccessionsResources(accessionResource);
			}
		} else {
            if (!resourceCreation.equalsIgnoreCase(RESOURCE_CREATION_NONE)) {
                controller.addLineToImportLog("Record # " + controller.getTotalRecords() + " - " + accession.getAccessionNumber() + " has no resource id so no resource record was created");                
            }
        }
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

	public void parseDate(Accessions accession, String dateString) {

		String allowedCharacters = "0123456789-,/ ;";
		String delimeters = "/-, ";

		String newString = "";
		String oldString = dateString.trim();

		String thisCharacter;

		for (int i = 0; i < oldString.length(); i++) {
			thisCharacter = oldString.substring(i, i + 1);
			if (allowedCharacters.contains(thisCharacter)) {
				newString += thisCharacter;
			}
		}

		ArrayList<String> tokenArray = new ArrayList<String>();
		String thisToken = "";
		for (int i = 0; i < newString.length(); i++) {
			thisCharacter = newString.substring(i, i + 1);
			if (delimeters.contains(thisCharacter)) {
				//we have a delimiter
				if (thisToken.length() != 0) {
					tokenArray.add(thisToken);
					thisToken = "";
				}

				tokenArray.add("delimiter:" + thisCharacter);
			} else {
				thisToken += thisCharacter;
			}
		}
		if (thisToken.length() != 0) {
			tokenArray.add(thisToken);
			thisToken = "";
		}

		//remove anything that is not 4 digits or a delimiter
		ArrayList<String> tokenArray2 = new ArrayList<String>();
		for (String s : tokenArray) {
			if (s.startsWith("delimiter:")) {
				tokenArray2.add(s);
			} else if (s.length() == 4) {
				tokenArray2.add(s);
			}
		}
		//remove consective delimiters preferencing a dash
		ArrayList<String> tokenArray3 = new ArrayList<String>();
		boolean isPreviousDelimiter = false;
		for (String s : tokenArray2) {
			if (s.startsWith("delimiter:")) {
				if (isPreviousDelimiter) {
					//do nothing for now
				} else {
					tokenArray3.add(s);
				}
			} else {
				tokenArray3.add(s);
			}
		}

		String stringBeginDate;
		String stringEndDate;

		if (tokenArray3.size() == 1) {
			stringBeginDate = tokenArray3.get(0);
			if (stringBeginDate.length() == 4) {
				accession.setDateBegin(new Integer(stringBeginDate));
				accession.setDateEnd(new Integer(stringBeginDate));
			}
		} else if (tokenArray3.size() == 3 && tokenArray3.get(1).equalsIgnoreCase("delimiter:-")) {
			stringBeginDate = tokenArray3.get(0);
			stringEndDate = tokenArray3.get(2);
			if (stringBeginDate.length() == 4 && stringEndDate.length() == 4) {
				accession.setDateBegin(new Integer(stringBeginDate));
				accession.setDateEnd(new Integer(stringEndDate));
			}
		}

	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public static Vector<String> getResourceRecordCreationValues() {

		Vector<String> returnVector = new Vector<String>();
		returnVector.add(RESOURCE_CREATION_NONE);
		returnVector.add(RESOURCE_CREATION_STUB);
		returnVector.add(RESOURCE_CREATION_FULL);
		return returnVector;
	}
}