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
 * Created on July 19, 2005, 11:54 AM
 * @author leemandell
 *
 */

/*
 * Subject.java
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.util.NoteEtcTypesUtils;
import org.archiviststoolkit.structure.*;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.DeleteException;
import org.hibernate.LockMode;

import java.util.*;

@ExcludeFromDefaultValues
public class Repositories extends DomainObject {

	public static final String PROPERTYNAME_REPOSITORY_NAME = "repositoryName";
	public static final String PROPERTYNAME_SHORT_NAME = "shortName";
	public static final String PROPERTYNAME_REPOSITORY_ADDRESS1 = "address1";
	public static final String PROPERTYNAME_REPOSITORY_ADDRESS2 = "address2";
	public static final String PROPERTYNAME_REPOSITORY_ADDRESS3 = "address3";
	public static final String PROPERTYNAME_REPOSITORY_CITY = "city";
	public static final String PROPERTYNAME_REPOSITORY_REGION = "region";
	public static final String PROPERTYNAME_REPOSITORY_COUNTRY = "country";
	public static final String PROPERTYNAME_REPOSITORY_MAILCODE = "mailCode";
	public static final String PROPERTYNAME_REPOSITORY_TELEPHONE = "telephone";
	public static final String PROPERTYNAME_REPOSITORY_FAX = "fax";
	public static final String PROPERTYNAME_REPOSITORY_EMAIL = "email";
	public static final String PROPERTYNAME_REPOSITORY_URL = "url";
	public static final String PROPERTYNAME_REPOSITORY_COUNTRY_CODE = "countryCode";
	public static final String PROPERTYNAME_REPOSITORY_AGENCY_CODE = "agencyCode";
	public static final String PROPERTYNAME_REPOSITORY_NOTE = "reposityNote";
    public static final String PROPERTYNAME_REPOSITORY_NOTE_TYPE = "repositoryNoteType";
	public static final String PROPERTYNAME_BRANDING_DEVICE = "brandingDevice";
    public static final String PROPERTYNAME_DESCRIPTIVE_LANGUAGE = "descriptiveLanguage";
    public static final String PROPERTYNAME_NCES_ID = "ncesId";
    public static final String PROPERTYNAME_INSTITUTION_NAME = "institutionName";

	private static ArrayList<Repositories> repositoryList = new ArrayList<Repositories>();

	@IncludeInApplicationConfiguration
	private Long RepositoryId;

	@IncludeInApplicationConfiguration(1)
	@StringLengthValidationRequried
	private String repositoryName = "";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String shortName = "";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String address1 ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String address2 ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String address3 ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String city ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String region ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String country ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String mailCode ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String telephone ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String fax ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String email ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String url ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String countryCode ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
    private String agencyCode ="";

    @IncludeInApplicationConfiguration
	@StringLengthValidationRequried
    private String brandingDevice ="";

    @IncludeInApplicationConfiguration
	@StringLengthValidationRequried
    private String descriptiveLanguage ="";

    @IncludeInApplicationConfiguration
	@StringLengthValidationRequried(20)
    private String ncesId ="";

    @IncludeInApplicationConfiguration
	@StringLengthValidationRequried
    private String institutionName ="";



	private Set accessions = new HashSet();
	private Set resources = new HashSet();
	private Set users = new HashSet();
	private Set<DefaultValues> defaultValues = new HashSet<DefaultValues>();
	private Set<RepositoryNotes> notes = new HashSet<RepositoryNotes>();
    private Set<RepositoryNotesDefaultValues> noteDefaultValues = new HashSet<RepositoryNotesDefaultValues>();
    private Set<RepositoryStatistics> statistics = new HashSet<RepositoryStatistics>();


	/**
	 * Creates a new instance of Repository
	 */
	public Repositories() {
	}

	public Long getRepositoryId() {
		return RepositoryId;
	}

	public void setRepositoryId(Long repositoryId) {
		this.RepositoryId = repositoryId;
	}

	public Long getIdentifier() {
		return this.getRepositoryId();
	}

	public void setIdentifier(Long identifier) {
		this.setRepositoryId(identifier);
	}

	//A place holder to determine if an object is safe to delete. To be
//overridden if logic is necessary.
	@Override
	public void testDeleteRules() throws DeleteException, PersistenceException {
		DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Resources.class);
		int linkedResourceCount = access.getCountBasedOnPropertyValue(Resources.PROPERTYNAME_REPOSITORY, this);
		access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Accessions.class);
		int linkedAccessionCount = access.getCountBasedOnPropertyValue(Accessions.PROPERTYNAME_REPOSITORY, this);
		access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Users.class);
		int linkedUsers = access.getCountBasedOnPropertyValue(Users.PROPERTYNAME_REPOSITORY, this);
		if (linkedResourceCount > 0 || linkedAccessionCount > 0|| linkedUsers > 0) {
			String errorString = "";
			if (linkedResourceCount > 0) {
			   errorString = linkedResourceCount + " resources";
			}
			if (linkedAccessionCount > 0) {
				errorString = StringHelper.concat(" and ", errorString, linkedAccessionCount + " accessions");
			}
			if (linkedUsers > 0) {
				errorString = StringHelper.concat(" and ", errorString, linkedUsers + " users");
			}
			throw new DeleteException("There are " + errorString + " linked to this repository");
		}
	}

	public String getRepositoryName() {
		if (this.repositoryName != null) {
			return this.repositoryName;
		} else {
			return "";
		}
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	public Set getAccessions() {
		return accessions;
	}

	public void setAccessions(Set accessions) {
		this.accessions = accessions;
	}

	public String toString() {
		return this.getShortName();
	}

	public Set getResources() {
		return resources;
	}

	public void setResources(Set resources) {
		this.resources = resources;
	}

	public Set getUsers() {
		return users;
	}

	public void setUsers(Set users) {
		this.users = users;
	}

	//Static methods for maintaining a list of repositories

	public static void addRepositoryToList(Repositories repositoryToAdd) {
		repositoryList.add(repositoryToAdd);
	}

	public static Vector<Repositories> getRepositoryList() {
		return new Vector<Repositories>(repositoryList);
	}

	public static Repositories lookupRepositoryByName(String repositoryName) {
		for (Repositories repository : repositoryList) {
			if (repositoryName.equalsIgnoreCase(repository.getRepositoryName())) {
				return repository;
			}
		}
		return null;
	}

	public static boolean loadRepositories() {
		repositoryList = new ArrayList<Repositories>();
		DomainAccessObject access = new DomainAccessObjectImpl(Repositories.class);
		try {
			for (Object o : access.findAll(LockMode.READ, "repositoryName")) {
				Repositories.addRepositoryToList((Repositories) o);
			}

		} catch (LookupException e) {
			ErrorDialog dialog = new ErrorDialog("There is a problem loading repositories.",
					StringHelper.getStackTrace(e));
			dialog.showDialog();

			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String getAddress1() {
		if (this.address1 != null) {
			return this.address1;
		} else {
			return "";
		}
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		if (this.address2 != null) {
			return this.address2;
		} else {
			return "";
		}
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		if (this.city != null) {
			return this.city;
		} else {
			return "";
		}
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		if (this.region != null) {
			return this.region;
		} else {
			return "";
		}
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCountry() {
		if (this.country != null) {
			return this.country;
		} else {
			return "";
		}
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getMailCode() {
		if (this.mailCode != null) {
			return this.mailCode;
		} else {
			return "";
		}
	}

	public void setMailCode(String mailCode) {
		this.mailCode = mailCode;
	}

	public String getTelephone() {
		if (this.telephone != null) {
			return this.telephone;
		} else {
			return "";
		}
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		if (this.fax != null) {
			return this.fax;
		} else {
			return "";
		}
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		if (this.email != null) {
			return this.email;
		} else {
			return "";
		}
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUrl() {
		if (this.url != null) {
			return this.url;
		} else {
			return "";
		}
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCountryCode() {
		if (this.countryCode != null) {
			return this.countryCode;
		} else {
			return "";
		}
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getAgencyCode() {
		if (this.agencyCode != null) {
			return this.agencyCode;
		} else {
			return "";
		}
	}

	public void setAgencyCode(String agencyCode) {
		this.agencyCode = agencyCode;
	}

	public String getAddress3() {
		if (this.address3 != null) {
			return this.address3;
		} else {
			return "";
		}
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public Set<DefaultValues> getDefaultValues() {
		return defaultValues;
	}

	public void setDefaultValues(Set<DefaultValues> defaultValues) {
		this.defaultValues = defaultValues;
	}

	public void addDefaultValue(DefaultValues defaultValue) throws DuplicateLinkException {
		if (!containsDefaultValue(defaultValue)) {
			getDefaultValues().add(defaultValue);
		} else {
			throw new DuplicateLinkException("A default value for " + defaultValue.getAtField() + " already exists");
		}
	}

	public Boolean containsDefaultValue(DefaultValues defaultValueToAdd) {
		for (DefaultValues defaultValue: getDefaultValues()) {
			if (defaultValue.getAtField().equals(defaultValueToAdd.getAtField())) {
				return true;
			}
		}
		return false;
	}

	protected void removeDefaultValue(DefaultValues defaultValue) {
		getDefaultValues().remove(defaultValue);
	}

	/**
	 * A dummy place holder to be overridden by classes that have related objects
	 *
	 * @param domainObject the domain object to be removed
	 */
	public void addRelatedObject(DomainObject domainObject)  throws DuplicateLinkException{
		if (domainObject instanceof DefaultValues) {
			addDefaultValue((DefaultValues) domainObject);
		} else if (domainObject instanceof RepositoryNotes) {
			addNote((RepositoryNotes)domainObject);
		} else if (domainObject instanceof RepositoryStatistics) {
			addStatistics((RepositoryStatistics)domainObject);
		}
	}

	public void removeRelatedObject(DomainObject domainObject) {
		if (domainObject instanceof DefaultValues) {
			removeDefaultValue((DefaultValues) domainObject);
		} else if (domainObject instanceof RepositoryNotes) {
			removeNote((RepositoryNotes)domainObject);
		} else if (domainObject instanceof RepositoryStatistics) {
            removeStatistics((RepositoryStatistics)domainObject);
        }
	}

	/**
	 * A dummy place holder to be overridden by classes that have related objects
	 *
	 * @param domainObject the domain object to be removed
	 */
	public Collection getRelatedCollection(DomainObject domainObject) {
		if (domainObject instanceof RepositoryNotes) {
			return getNotes();
		} else {
			return null;
		}
	}


	public Set<RepositoryNotes> getNotes() {
		return notes;
	}

	public void setNotes(Set<RepositoryNotes> notes) {
		this.notes = notes;
	}

	public void addNote(RepositoryNotes respositoryNote) {
		if (respositoryNote == null)
			throw new IllegalArgumentException("Can't add a null contact note.");
		SequencedObject.adjustSequenceNumberForAdd(this.getNotes(), respositoryNote);
		this.getNotes().add(respositoryNote);
	}

	private void removeNote(RepositoryNotes note) {
		getNotes().remove(note);
	}

	public String getShortName() {
		if (this.shortName != null) {
			return this.shortName;
		} else {
			return "";
		}
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Set<RepositoryNotesDefaultValues> getNoteDefaultValues() {
		return noteDefaultValues;
	}

	public void setNoteDefaultValues(Set<RepositoryNotesDefaultValues> noteDefaultValues) {
		this.noteDefaultValues = noteDefaultValues;
	}

	private boolean containsNoteDefaultValue(RepositoryNotesDefaultValues link) {
		for (RepositoryNotesDefaultValues defaultValue: noteDefaultValues) {
			if (defaultValue.getNoteType().equals(link.getNoteType())) {
				return true;
			}
		}
		return false;
	}

	private void addNoteDefaultValue(RepositoryNotesDefaultValues link) {
		if (!containsNoteDefaultValue(link)) {
			noteDefaultValues.add(link);
		}
	}

	public void createNoteDefaultValueLinks() {
		RepositoryNotesDefaultValues link;
		for (NotesEtcTypes note: NoteEtcTypesUtils.getNotesEtcTypes()) {
			if (!note.getEmbeded()) {
				link = new RepositoryNotesDefaultValues(this, note);
				addNoteDefaultValue(link);
			}
		}
	}

    public String getBrandingDevice() {
		if (this.brandingDevice != null) {
			return this.brandingDevice;
		} else {
			return "";
		}
	}

    public void setBrandingDevice(String brandingDevice) {
        this.brandingDevice = brandingDevice;
    }

	public String getDescriptiveLanguage() {
		if (this.descriptiveLanguage != null) {
			return this.descriptiveLanguage;
		} else {
			return "";
		}
	}

	public void setDescriptiveLanguage(String descriptiveLanguage) {
		this.descriptiveLanguage = descriptiveLanguage;
	}

    public String getNcesId() {
		if (this.ncesId != null) {
			return this.ncesId;
		} else {
			return "";
		}
	}

    public void setNcesId(String ncesId) {
        this.ncesId = ncesId;
    }

    public String getInstitutionName() {
		if (this.institutionName != null) {
			return this.institutionName;
		} else {
			return "";
		}
	}

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public Set<RepositoryStatistics> getStatistics() {
        return statistics;
    }

    public void setStatistics(Set<RepositoryStatistics> statistics) {
        this.statistics = statistics;
    }

	private void addStatistics(RepositoryStatistics statistic) {
		statistics.add(statistic);
	}

    private void removeStatistics(RepositoryStatistics statistic) {
        getStatistics().remove(statistic);
    }

    
}
