package org.archiviststoolkit.importer;

import org.archiviststoolkit.model.*;
import org.archiviststoolkit.exceptions.ValidationException;
import org.archiviststoolkit.swing.ImportOptionsAccessions;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.util.NameUtils;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.structure.accessionImport.*;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.beans.PropertyDescriptor;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;

/**
 * Imports Contact domain objects from CSV files.
 */
public class AccessionImportXmlHandler extends ImportHandler {

	public static final String RESOURCE_CREATION_NONE = "Do not create resource record";
	public static final String RESOURCE_CREATION_STUB = "Create resource record with resource id only";
	public static final String RESOURCE_CREATION_FULL = "Create resource record using all fields";

	private Repositories repository;

	private String resourceCreation = "";


	/**
	 * Constructor.
     * @param importOptions - the import options panel
     */
	public AccessionImportXmlHandler(ImportOptionsAccessions importOptions) {
		super();
		this.repository = importOptions.getRepository();
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
	 * Import the file.
	 *
	 * @param importFile the file to import.
	 * @param controller the controller to use.
	 * @param progressPanel - the progress panel to display heartbeat messages
	 * @return if we succeded
	 */
	public boolean importFile(File importFile, DomainImportController controller, InfiniteProgressPanel progressPanel) throws ImportException {

		Collection<DomainObject> collection = new ArrayList<DomainObject>();

		if (importFile != null) {
			JAXBContext context;
			try {
				LookupListUtils.initIngestReport();
				context = JAXBContext.newInstance("org.archiviststoolkit.structure.accessionImport");
				AccessionRecords accessionRecords = (AccessionRecords) context.createUnmarshaller().unmarshal(importFile);
				Accessions accession;
				int recordCount = 1;
				progressPanel.setTextLine("Importing...", 1);
				PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(RecordType.class);
				for (RecordType record : accessionRecords.getRecord()) {
                    // check to see if the process was cancelled  at this point if so just return
                    if (progressPanel.isProcessCancelled()) {
                        return false;
                    }

                    progressPanel.setTextLine("Processing record " + recordCount, 2);
					accession = new Accessions();
					accession.setRepository(repository);

					for (PropertyDescriptor descriptor: descriptors) {
						Class propertyType = descriptor.getPropertyType();
						if (propertyType == XMLGregorianCalendar.class) {
							ImportUtils.nullSafeDateSet(accession, descriptor.getName(), (XMLGregorianCalendar)descriptor.getReadMethod().invoke(record));
						} else if (propertyType == String.class ||
								propertyType == BigInteger.class ||
								propertyType == BigDecimal.class) {
							ImportUtils.nullSafeSet(accession, descriptor.getName(), descriptor.getReadMethod().invoke(record));

						} else if (propertyType == Boolean.class) {
							//this hack is needed because jaxb has a bug and the read method starts
							//with "is" instead of "get"
							String writeMethodName = descriptor.getWriteMethod().getName();
							String readMethodName = writeMethodName.replaceFirst("set", "is");
							Method readMethod = RecordType.class.getMethod(readMethodName);
							ImportUtils.nullSafeSet(accession, descriptor.getName(), readMethod.invoke(record));
						}
					}
					parseAccessionNumber(accession, record.getAccessionNumber());
					addSubjects(accession, record.getSubjectLink());
					addNames(accession, record.getNameLink());
					addResource(accession, record.getResourceIdentifier(), controller, accession.getAccessionNumber(), recordCount);
					collection.add(accession);
                    recordCount++;
                }
				controller.domainImport(collection, ApplicationFrame.getInstance(), progressPanel);
				ImportExportLogDialog dialog = new ImportExportLogDialog(controller.constructFinalImportLogText() + "\n\n" + LookupListUtils.getIngestReport(),
						ImportExportLogDialog.DIALOG_TYPE_IMPORT);
				progressPanel.close();
				dialog.showDialog();
			} catch (JAXBException e) {
				progressPanel.close();
				new ErrorDialog(ApplicationFrame.getInstance(), "There is a problem importing accessions.", e).showDialog();
			} catch (IllegalAccessException e) {
				progressPanel.close();
				new ErrorDialog("", e).showDialog();
			} catch (InvocationTargetException e) {
				progressPanel.close();
				new ErrorDialog("", e).showDialog();
			} catch (UnknownLookupListException e) {
				progressPanel.close();
				new ErrorDialog("", e).showDialog();
			} catch (ValidationException e) {
				progressPanel.close();
				new ErrorDialog("", e).showDialog();
			} catch (PersistenceException e) {
				progressPanel.close();
				new ErrorDialog("", e).showDialog();
			} catch (DuplicateLinkException e) {
				progressPanel.close();
				new ErrorDialog("", e).showDialog();
			} catch (NoSuchMethodException e) {
				progressPanel.close();
				new ErrorDialog("", e).showDialog();
			} catch (NoSuchAlgorithmException e) {
				new ErrorDialog("", e).showDialog();
			} catch (UnsupportedEncodingException e) {
				new ErrorDialog("", e).showDialog();
			}

		}
		return (true);
	}


	private void parseAccessionNumber(Accessions accession, IdentifierType value) {
		String composite = value.getComposite();

		if (composite != null) {
			StringTokenizer st = new StringTokenizer(composite, "./-");
			int count = 0;
			String thisToken;
			while (st.hasMoreTokens()) {
				count++;
				thisToken = StringHelper.cleanUpWhiteSpace((String) st.nextElement());
				if (count == 1) {
					accession.setAccessionNumber1(StringHelper.trimToLength(thisToken, ATFieldInfo.checkFieldLength(Accessions.class, Accessions.PROPERTYNAME_ACCESSION_NUMBER_1)));
				} else if (count == 2) {
					accession.setAccessionNumber2(StringHelper.trimToLength(thisToken, ATFieldInfo.checkFieldLength(Accessions.class, Accessions.PROPERTYNAME_ACCESSION_NUMBER_2)));
				} else if (count == 3) {
					accession.setAccessionNumber3(StringHelper.trimToLength(thisToken, ATFieldInfo.checkFieldLength(Accessions.class, Accessions.PROPERTYNAME_ACCESSION_NUMBER_3)));
				} else if (count == 4) {
					accession.setAccessionNumber4(StringHelper.trimToLength(thisToken, ATFieldInfo.checkFieldLength(Accessions.class, Accessions.PROPERTYNAME_ACCESSION_NUMBER_4)));
				}

			}
		} else {
			accession.setAccessionNumber1(cleanAndTrim(value.getPart1(), Accessions.class, Accessions.PROPERTYNAME_ACCESSION_NUMBER_1));
			if (value.getPart2() != null) {
				accession.setAccessionNumber2(cleanAndTrim(value.getPart2(), Accessions.class, Accessions.PROPERTYNAME_ACCESSION_NUMBER_2));
			}
			if (value.getPart3() != null) {
				accession.setAccessionNumber3(cleanAndTrim(value.getPart3(), Accessions.class, Accessions.PROPERTYNAME_ACCESSION_NUMBER_3));
			}
			if (value.getPart4() != null) {
				accession.setAccessionNumber4(cleanAndTrim(value.getPart4(), Accessions.class, Accessions.PROPERTYNAME_ACCESSION_NUMBER_4));
			}
		}
	}

	private String cleanAndTrim(String valueToClean, Class clazz, String propertyName) {
		String returnString = StringHelper.cleanUpWhiteSpace(valueToClean);
		ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(clazz, propertyName);
		if (fieldInfo.getStringLengthLimit() != null && fieldInfo.getStringLengthLimit() > 0) {
			return StringHelper.trimToLength(returnString, fieldInfo.getStringLengthLimit());
		} else {
			return returnString;
		}
	}

	private void addSubjects(Accessions accession,
							 List<SubjectLinkType> subjectList) throws PersistenceException, ValidationException, UnknownLookupListException {
		SubjectsDAO subjectDao = new SubjectsDAO();
		Subjects subject;
		ArchDescriptionSubjects accessionSubject;
		String type;
		String source;

		for (SubjectLinkType thisSubject : subjectList) {
			type = cleanAndTrim(thisSubject.getSubjectTermType(), Subjects.class, Subjects.PROPERTYNAME_SUBJECT_TERM_TYPE);
			if (type == null) {
				type = "ingest";
			}
			source = cleanAndTrim(thisSubject.getSubjectSource(), Subjects.class, Subjects.PROPERTYNAME_SUBJECT_SOURCE);
			if (source == null) {
				source = "ingest";
			}
			subject = subjectDao.lookupSubject(cleanAndTrim(thisSubject.getSubjectTerm(), Subjects.class, Subjects.PROPERTYNAME_SUBJECT_TERM), type, source, true);
			accessionSubject = new ArchDescriptionSubjects(subject, accession);
			accession.addSubject(accessionSubject);
		}
	}

	private void addNames(Accessions accession, List<NameLinkType> nameList) throws IllegalAccessException, InvocationTargetException, DuplicateLinkException, UnknownLookupListException, PersistenceException, NoSuchAlgorithmException, UnsupportedEncodingException {
		NamesDAO namesDao = new NamesDAO();
		ArchDescriptionNames accessionName;

		for (NameLinkType thisNameLink : nameList) {
			accessionName = new ArchDescriptionNames(lookupName(namesDao, thisNameLink.getName()), accession);
			ImportUtils.nullSafeSet(accessionName, ArchDescriptionNames.PROPERTYNAME_NAME_LINK_FUNCTION, thisNameLink.getNameLinkFunction());
			ImportUtils.nullSafeSet(accessionName, ArchDescriptionNames.PROPERTYNAME_FORM, thisNameLink.getNameLinkForm());
			ImportUtils.nullSafeSet(accessionName, ArchDescriptionNames.PROPERTYNAME_ROLE, thisNameLink.getNameLinkRole());
			accession.addName(accessionName);
		}

	}

	private Names lookupName(NamesDAO namesDao, NameComplexType nameFromImport) throws IllegalAccessException, InvocationTargetException, UnknownLookupListException, PersistenceException, NoSuchAlgorithmException, UnsupportedEncodingException {
		Names name = new Names();
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_NAME_TYPE, nameFromImport.getNameType());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_SORT_NAME, nameFromImport.getSortName());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_NAME_RULE, nameFromImport.getNameRule());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_NUMBER, nameFromImport.getNumber());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_QUALIFIER, nameFromImport.getQualifier());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_DESCRIPTION_TYPE, nameFromImport.getDescriptionType());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_DESCRIPTION_NOTE, nameFromImport.getDescriptionNote());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CITATION, nameFromImport.getCitation());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_SALUTATION, nameFromImport.getSalutation());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CONTACT_ADDRESS_1, nameFromImport.getContactAddress1());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CONTACT_ADDRESS_2, nameFromImport.getContactAddress2());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CONTACT_CITY, nameFromImport.getContactCity());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CONTACT_REGION, nameFromImport.getContactRegion());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CONTACT_COUNTRY, nameFromImport.getContactCountry());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CONTACT_MAIL_CODE, nameFromImport.getContactMailCode());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CONTACT_PHONE, nameFromImport.getContactPhone());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CONTACT_FAX, nameFromImport.getContactFax());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CONTACT_EMAIL, nameFromImport.getContactEmail());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CONTACT_NAME, nameFromImport.getContactName());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_FAMILY_NAME, nameFromImport.getFamilyName());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_FAMILY_NAME_PREFIX, nameFromImport.getFamilyNamePrefix());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CORPORATE_PRIMARY_NAME, nameFromImport.getCorporatePrimaryName());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CORPORATE_SUBORDINATE_1, nameFromImport.getCorporateSubordinate1());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CORPORATE_SUBORDINATE_2, nameFromImport.getCorporateSubordinate2());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_PERSONAL_PRIMARY_NAME, nameFromImport.getPersonalPrimaryName());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_PERSONAL_REST_OF_NAME, nameFromImport.getPersonalRestOfName());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_PERSONAL_PREFIX, nameFromImport.getPersonalPrefix());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_PERSONAL_SUFFIX, nameFromImport.getPersonalSuffix());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_PERSONAL_FULLER_FORM, nameFromImport.getPersonalFullerForm());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_PERSONAL_TITLE, nameFromImport.getPersonalTitle());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_PERSONAL_DATES, nameFromImport.getPersonalDates());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_PERSONAL_DIRECT_ORDER, nameFromImport.isPersonalDirectOrder());
		if (nameFromImport.getNameSource() == null) {
			name.setNameSource("ingest");
		} else {
			name.setNameSource(nameFromImport.getNameSource());
		}
		if (name.getSortName() == null || name.getSortName().length() == 0) {
			name.createSortName();

            // check to see if sort name is still empty. If so, set it to " " so that
            // this record can be inserted into an Oracle DB without throwing an error
            if(name.getSortName().length() == 0) {
                name.setSortName(" ");
            }
		}
		NameUtils.setMd5Hash(name);

		return namesDao.lookupName(name, true);
	}


	private void addResource(Accessions accession,
                             IdentifierType resourceId,
                             DomainImportController controller,
                             String accesionNumber,
                             int recordNumber) throws ImportException {

		if (resourceId != null) {

			String resourceId1;
			String resourceId2 = null;
			String resourceId3 = null;
			String resourceId4 = null;
			if (resourceId.getComposite() != null) {
				resourceId1 = StringHelper.trimToLength(resourceId.getComposite(), ATFieldInfo.checkFieldLength(Resources.class, Resources.PROPERTYNAME_RESOURCE_IDENTIFIER_1));
			} else {
				resourceId1 = resourceId.getPart1();
				resourceId2 = resourceId.getPart2();
				resourceId3 = resourceId.getPart3();
				resourceId4 = resourceId.getPart4();
			}

			ResourcesDAO resourceDao = new ResourcesDAO();
			Resources resource;
			if (resourceCreation.equalsIgnoreCase(AccessionImportXmlHandler.RESOURCE_CREATION_NONE)) {
				resource = resourceDao.lookupResource(resourceId1, resourceId2, resourceId3, resourceId4, false, repository);
			} else {
				resource = resourceDao.lookupResource(resourceId1, resourceId2, resourceId3, resourceId4, true, repository);
			}
			if (resourceDao.getNewRecordCreated() && resourceCreation.equalsIgnoreCase(AccessionImportXmlHandler.RESOURCE_CREATION_FULL))
			{
				try {
					resource = (Resources) resourceDao.findByPrimaryKeyLongSession(resource.getIdentifier());
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
            if (!resourceCreation.equalsIgnoreCase(AccessionImportXmlHandler.RESOURCE_CREATION_NONE)) {
                controller.addLineToImportLog("Record # " + recordNumber + " - " + accesionNumber + " has no resource id so no resource record was created");
            }
        }
	}

	public static Vector<String> getResourceRecordCreationValues() {

		Vector<String> returnVector = new Vector<String>();
		returnVector.add(AccessionImportXmlHandler.RESOURCE_CREATION_NONE);
		returnVector.add(AccessionImportXmlHandler.RESOURCE_CREATION_STUB);
		returnVector.add(AccessionImportXmlHandler.RESOURCE_CREATION_FULL);
		return returnVector;
	}
}
