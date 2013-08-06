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
import org.archiviststoolkit.model.validators.ATValidator;
import org.archiviststoolkit.model.validators.ValidatorFactory;
import org.archiviststoolkit.exceptions.ValidationException;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.NameUtils;
import org.archiviststoolkit.util.NoteEtcTypesUtils;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.NotesEtcTypes;
import org.archiviststoolkit.structure.DefaultValues;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.swing.ImportOptionsDigitalObjects;
import org.apache.commons.beanutils.BeanUtils;
import com.jgoodies.validation.ValidationResult;

/**
 * Imports Contact domain objects from CSV files.
 */
public class DelimitedDigitalObjectImportHandler extends TabImportHandler {
	private Repositories repository;
    private HashMap<String, DigitalObjects> processedDigitalObjects; // store digital objects which haven been procesed
	private String dateFormat = "d/M/yyyy";
    private int objectOrder = 1; // used to keep track of object added

	/**
	 * Constructor.
	 */
	public DelimitedDigitalObjectImportHandler(ImportOptionsDigitalObjects importOptions) {
		super();
		this.repository = importOptions.getRepository();
		this.dateFormat = importOptions.getDateFormat();
        processedDigitalObjects = new HashMap<String, DigitalObjects>();
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
		DigitalObjects digitalObject = new DigitalObjects();
		digitalObject.setRepository(repository);

		HashMap<String, Object> map = new HashMap<String, Object>();
		String dataValue;
		String fieldName = "";
		String lookupListName;
		Names name = null;
        NotesEtcTypes noteType = null;
        int noteSequence = 1;
        FileVersions fileVersion = null;
		String nameRole = "";
		String nameFunction = "";
		String nameForm = "";
		ATFieldInfo fieldInfo;
		Date dateValue;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		ArrayList<String> subjectList = null;
		String subjectTermType = "";
		String subjectSource = "ingest";

        // this specifies whether the digital object record that will be created 
        boolean isComponent = false;

		controller.incrementTotalRecordCount();
		for (int i = 0; i < dataList.size(); i++) {
			dataValue = ((String) dataList.get(i)).trim();

            try {
				fieldName = (String) this.fieldList.get(i);
			} catch (RuntimeException e) {
				throw new ImportException("Error Precession row", e);
			}

            fieldInfo = ATFieldInfo.getFieldInfo(DigitalObjects.class.getName() + "." + fieldName);

            if (fieldInfo == null) {
				lookupListName ="";
			} else {
				lookupListName = fieldInfo.getLookupList();
			}

			if (fieldName.equalsIgnoreCase("digitalObjectId")) {
               map.put("metsIdentifier", StringHelper.convertPipeToCarriageReturn(dataValue));
            } else if (fieldName.equalsIgnoreCase("isComponent")) {
			    if(dataValue.equalsIgnoreCase("true") || dataValue.equalsIgnoreCase("yes")) {
                    isComponent = true;
                }
            } else if (fieldName.equalsIgnoreCase("subjectTerm")) {
                subjectList = parseSubjects(dataValue);
            } else if (fieldName.equalsIgnoreCase("subjectTermType")) {
                subjectTermType = dataValue;
			} else if (fieldName.equalsIgnoreCase("subjectSource")) {
				subjectSource = dataValue;
			} else if (getNoteName(fieldName).length() != 0) {
                noteType = getNotesEtcType(getNoteName(fieldName));
                // Add the notes if the note contents (dataValue) is not empty
                if (noteType != null && !StringHelper.isEmpty(dataValue)) {
                    try {
                        addNote(digitalObject, noteType, dataValue, noteSequence++);
                    } catch (UnsupportedRepeatingDataTypeException e) {
                        throw new ImportException("Error adding note. Unsupported Note Type. " + e.getMessage(), dataValue);
                    }
                }
			} else if (isFileVersionInfo(fieldName)) {
                fileVersion = accumulateFileVersionInfo(fileVersion, fieldName, dataValue);
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
				parseDate(digitalObject, dataValue);
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
					String stringDataValue = dataValue;
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
			BeanUtils.populate(digitalObject, map);
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
				addName(digitalObject, name, nameFunction, nameRole, nameForm);
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
				addSubjects(digitalObject, subjectList, subjectTermType, subjectSource);
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

        // Add the file version to the digital record now
        if (fileVersion != null) {
			try {
				addFileVersion(digitalObject, fileVersion);
			} catch (ValidationException e) {
				String currentRow = "";
				for (int i = 0; i < dataList.size(); i++) {
					currentRow += "\n" + this.fieldList.get(i) + ": " + ((String) dataList.get(i)).trim();
				}
				throw new ImportException("Error adding file version. File version does not meet validaton rules. " + e.getMessage(), currentRow);
			}
		}

        // set the digital object order
        digitalObject.setObjectOrder(objectOrder++);

        // find any digital object with the same mets ID
        DigitalObjects foundDigitalObject = findDigitalObjectByMetsId(digitalObject);

        /*
        Check to see if this is not a component digital object if it is then we
        We need to add it to the parent digital object which is either on the server or catched
        if catched then don't save it just return null if not then update the one on the server
        */
        if(isComponent) {
            if(foundDigitalObject != null) {
                processDigitalObjectComponent(foundDigitalObject, digitalObject);
                return null; // just return null because we don't want this digital object to be saved separately
            } else {
                String currentRow = "";
				for (int i = 0; i < dataList.size(); i++) {
					currentRow += "\n" + this.fieldList.get(i) + ": " + ((String) dataList.get(i)).trim();
				}
				throw new ImportException("Error adding component digital object. No parent digital object to add to.", currentRow);    
            }
        } else {
            // add this digital object to the list of processed digital objects
            processedDigitalObjects.put(digitalObject.getMetsIdentifier(),digitalObject);
		    return digitalObject;
        }
	}

    /**
     * Method to return a digital object by the metsID
     *
     * @param  digitalObject The digital object mets id to search for
     * @return The digital object that was found or null if none was found
     */
    private DigitalObjects findDigitalObjectByMetsId(DigitalObjects digitalObject) {
        DigitalObjects foundDigitalObject = null;

        // search the processed digital object hash map first
        if(processedDigitalObjects.containsKey(digitalObject.getMetsIdentifier())) {
            foundDigitalObject = processedDigitalObjects.get(digitalObject.getMetsIdentifier());
        } else { // now try finding it from the database
            DigitalObjectDAO digitalObjectDAO = new DigitalObjectDAO();
            try {
                foundDigitalObject = digitalObjectDAO.findByMetsIdentifierLongSession(digitalObject);
            } catch (LookupException e) {
                e.printStackTrace();
            }
        }

        return foundDigitalObject;
    }

    /**
     * Method to process a digital object component record. Basically add it to the parent DO
     * 
     * @param parentDigitalObject The parent digital object to attach
     * @param digitalObject The component digital object
     * @throws ImportException Throws this if the parent digital can't be save it to the database
     */
    private void processDigitalObjectComponent(DigitalObjects parentDigitalObject, DigitalObjects digitalObject) throws ImportException {
        digitalObject.setParent(parentDigitalObject);
        digitalObject.setRepository(parentDigitalObject.getRepository());
	    parentDigitalObject.addChild(digitalObject);

        // now if the digital object ID is not null that means we got it from the
        // server so it needs to be updated
        if(parentDigitalObject.getIdentifier() != null) {
            // first find the object order based on the order of the children that already there
            int maxObjectOrder = 0;
            for(DigitalObjects childDO: parentDigitalObject.getChildren()) {
                int objectOrder = childDO.getObjectOrder();
                if(objectOrder > maxObjectOrder) {
                    maxObjectOrder = objectOrder;
                }
            }

            maxObjectOrder++;

            digitalObject.setObjectOrder(maxObjectOrder);
            
            // now update the parent DO
            DigitalObjectDAO digitalObjectDAO = new DigitalObjectDAO();
            try {
                digitalObjectDAO.updateLongSession(parentDigitalObject);
            } catch (PersistenceException e) {
                throw new ImportException("Error adding component digital object. Unable to save parent digitalObject : " + parentDigitalObject.getMetsIdentifier());
            }
        }

        // increment the total record and processed record count
        controller.incrementInsertedRecordCount();
    }

    /**
     * Method to see if the data found in a particular column is for the
     * creation of a fileVersion object
     *
     * @param fieldName The name of the column
     * @return True if data contained within the 
     */
    private boolean isFileVersionInfo(String fieldName) {
        if(fieldName.equalsIgnoreCase("eadDaoActuate") ||
                fieldName.equalsIgnoreCase("eadDaoShow") ||
                fieldName.equalsIgnoreCase("uri") ||
                fieldName.equalsIgnoreCase("useStatement")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method to populate a file version object
     *
     * @param fileVersion The file version that was created before or null
     * @param dataValue field to add to the file version
     * @return FileVersion that was created or pass in with the value added
     */
    private FileVersions accumulateFileVersionInfo(FileVersions fileVersion, String fieldName, String dataValue) throws ImportException {
        if (dataValue.length() > 0) {
			if (fileVersion == null) {
				fileVersion = new FileVersions();
			}

			try {

				int stringLengthLimit = ATFieldInfo.checkFieldLength(FileVersions.class, fieldName);
				if (stringLengthLimit != -1) {
				    dataValue = StringHelper.trimToLength(dataValue, stringLengthLimit);
				}
				BeanUtils.setProperty(fileVersion, fieldName, dataValue);
			} catch (Exception e) {
				throw new ImportException("Error processing name", fieldName + ": " + dataValue, e);
			}
		}
		return fileVersion;
    }

    /**
     * Method to return the Notes Etc Types, based on the value read in from file
     * 
     * @param noteName The name of the note type
     * @return The notes etc type
     */
    private NotesEtcTypes getNotesEtcType(String noteName) throws ImportException {
        try {
            return NoteEtcTypesUtils.lookupNoteEtcTypeByCannonicalName(noteName);
        } catch (UnsupportedRepeatingDataTypeException e) {
            throw new ImportException("Error processing Notes", noteName, e);
        }
    }

    /**
     * Method to get the note name based on the column name
     *
     * @param dataValue The string to see to return proper name name for
     * @return The proper note name or blank if the note is not supported
     */
    private String getNoteName(String dataValue) {
        String noteName = "";

        if (dataValue.equalsIgnoreCase("abstract")) {
            noteName = "Abstract";
        } else if (dataValue.equalsIgnoreCase("biographicalHistorical")) {
            noteName = "Biographical/Historical note";
        } else if (dataValue.equalsIgnoreCase("conditionsGoverningAccess")) {
            noteName = "Conditions Governing Access note";
        } else if (dataValue.equalsIgnoreCase("conditionsGoverningUse")) {
            noteName = "Conditions Governing Use note";
        } else if (dataValue.equalsIgnoreCase("custodialHistory")) {
            noteName = "Custodial History note";
        } else if (dataValue.equalsIgnoreCase("dimensions")) {
            noteName = "Dimensions note";
        } else if (dataValue.equalsIgnoreCase("existenceLocationCopies")) {
            noteName = "Existence and Location of Copies note";
        } else if (dataValue.equalsIgnoreCase("existenceLocationOriginals")) {
            noteName = "Existence and Location of Originals note";
        } else if (dataValue.equalsIgnoreCase("generalNote")) {
            noteName = "General note";
        } else if (dataValue.equalsIgnoreCase("sourceAcquisition")) {
            noteName = "Immediate Source of Acquisition note";
        } else if (dataValue.equalsIgnoreCase("legalStatus")) {
            noteName = "Legal Status note";
        } else if (dataValue.equalsIgnoreCase("genPhysDesc")) {
            noteName = "General Physical Description note";
        } else if (dataValue.equalsIgnoreCase("sourceAcquisition")) {
            noteName = "Immediate Source of Acquisition note";
        } else if (dataValue.equalsIgnoreCase("sourceAcquisition")) {
            noteName = "Immediate Source of Acquisition note";
        } else if (dataValue.equalsIgnoreCase("languageOfMaterials")) {
            noteName = "Language of Materials note";
        } else if (dataValue.equalsIgnoreCase("legalStatus")) {
            noteName = "Legal Status note";
        } else if (dataValue.equalsIgnoreCase("materialSpecificDetails")) {
            noteName = "Material Specific Details note";
        } else if (dataValue.equalsIgnoreCase("physCharacteristics")) {
            noteName = "Physical Characteristics and Technical Requirements note";
        } else if (dataValue.equalsIgnoreCase("physFacet")) {
            noteName = "Physical Facet note";
        } else if (dataValue.equalsIgnoreCase("preferredCitation")) {
            noteName = "Preferred Citation note";
        } else if (dataValue.equalsIgnoreCase("processingInformation")) {
            noteName = "Processing Information note";
        } else if (dataValue.equalsIgnoreCase("scopeContents")) {
            noteName = "Scope and Contents note";
        } else if (dataValue.equalsIgnoreCase("relatedMaterials")) {
            noteName = "Related Archival Materials note";
        }

        return noteName;
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

	private void addSubjects(DigitalObjects digitalObject,
							 ArrayList<String> subjectList,
							 String subjectTermType,
							 String subjectSource) throws PersistenceException, ValidationException, UnknownLookupListException {
        SubjectsDAO subjectDao = new SubjectsDAO();
        Subjects subject;
        ArchDescriptionSubjects digitalObjectSubject;

		//trim term type and source
		subjectSource = StringHelper.trimToLength(subjectSource, ATFieldInfo.checkFieldLength(Subjects.class, Subjects.PROPERTYNAME_SUBJECT_SOURCE));
		subjectTermType = StringHelper.trimToLength(subjectTermType, ATFieldInfo.checkFieldLength(Subjects.class, Subjects.PROPERTYNAME_SUBJECT_SOURCE));

		for (String thisSubject : subjectList) {
			thisSubject = StringHelper.trimToLength(thisSubject, ATFieldInfo.checkFieldLength(Subjects.class, Subjects.PROPERTYNAME_SUBJECT_TERM));
			subject = subjectDao.lookupSubject(thisSubject, subjectTermType, subjectSource, true);
            digitalObjectSubject = new ArchDescriptionSubjects(subject, digitalObject);
            digitalObject.addSubject(digitalObjectSubject);
        }
    }

	private void addName(DigitalObjects digitalObject, Names name, String nameFunction, String nameRole, String nameForm) throws PersistenceException, UnknownLookupListException, NoSuchAlgorithmException, UnsupportedEncodingException {
        NamesDAO nameDao = new NamesDAO();
        ArchDescriptionNames digitalObjectName;

        if (name.getNameSource().length() == 0) {
            name.setNameSource("ingest");
        }
		NameUtils.setMd5Hash(name);
		name = nameDao.lookupName(name, true);
        digitalObjectName = new ArchDescriptionNames(name, digitalObject);
		digitalObjectName.setNameLinkFunction(nameFunction);
		digitalObjectName.setRole(nameRole);
		digitalObjectName.setForm(nameForm);
        try {
            digitalObject.addName(digitalObjectName);
        } catch (DuplicateLinkException e) {
            //do nothing this is just a duplicate name
        }
    }

    /**
     * Method to add a note to the digital object. This not is invalid so it will have to be
     * fixed by the user.
     * 
     * @param digitalObject The digital object to attach the note to
     * @param noteType The type of the note to attach
     * @param noteContent The contents of the note
     * @param noteSequence The sequence of the note
     * @throws UnsupportedRepeatingDataTypeException If the note type is not supported
     */
    private void addNote(DigitalObjects digitalObject, NotesEtcTypes noteType, String noteContent, int noteSequence) throws UnsupportedRepeatingDataTypeException {
        ArchDescriptionRepeatingData repeatingData = ArchDescriptionRepeatingData.getInstance(digitalObject, NoteEtcTypesUtils.lookupRepeatingDataClass(noteType), noteType);
		repeatingData.setSequenceNumber(noteSequence);

        // set the note content now
        if (repeatingData instanceof ArchDescriptionNotes) {
	        ((ArchDescriptionNotes) repeatingData).setNoteContent(noteContent);
        }

        // must set default note contents for bibliography and other ArchDescription
        if (repeatingData instanceof ArchDescriptionStructuredData) {
	        ((ArchDescriptionStructuredData) repeatingData).setNote(noteContent);
        }

        //set any default values if noteContent is empty
        RepositoryNotesDefaultValues defaultValue = DefaultValues.getRepoistoryNoteDefaultValue(repository, noteType);
		if (defaultValue != null && noteContent.length() == 0) {
			repeatingData.setTitle(defaultValue.getDefaultTitle());
			if (repeatingData instanceof ArchDescriptionNotes)
				((ArchDescriptionNotes) repeatingData).setNoteContent(defaultValue.getDefaultContent());

            // must set default note contents for bibliography and other ArchDescription
            if (repeatingData instanceof ArchDescriptionStructuredData)
				((ArchDescriptionStructuredData) repeatingData).setNote(defaultValue.getDefaultContent());
        }

        digitalObject.addRepeatingData(repeatingData);
    }

    /**
     * Method to add a file version to the digital object
     *
     * @param digitalObject The digital object to add the file version to
     * @param fileVersion The file version to add
     * @throws ValidationException Thrown if file version fails validation
     */
    private void addFileVersion(DigitalObjects digitalObject, FileVersions fileVersion) throws ValidationException {
        // Validate the file version first
        ATValidator validator = ValidatorFactory.getInstance().getValidator(fileVersion);
        if (validator != null) {
            ValidationResult validationResult = validator.validate();
            if (validationResult.hasErrors()) {
                throw new ValidationException(validationResult.getMessagesText());
            }
        }

        // ok file version validated so add it to the digital object
        fileVersion.setDigitalObject(digitalObject);
        digitalObject.addFileVersion(fileVersion);
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

	public void parseDate(DigitalObjects digitalObject, String dateString) {

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
				digitalObject.setDateBegin(new Integer(stringBeginDate));
				digitalObject.setDateEnd(new Integer(stringBeginDate));
			}
		} else if (tokenArray3.size() == 3 && tokenArray3.get(1).equalsIgnoreCase("delimiter:-")) {
			stringBeginDate = tokenArray3.get(0);
			stringEndDate = tokenArray3.get(2);
			if (stringBeginDate.length() == 4 && stringEndDate.length() == 4) {
				digitalObject.setDateBegin(new Integer(stringBeginDate));
				digitalObject.setDateEnd(new Integer(stringEndDate));
			}
		}

	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
}