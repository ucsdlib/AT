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
 */


package org.archiviststoolkit.model;

//==============================================================================
// Import Declarations
//==============================================================================

import java.util.*;
import java.awt.*;
//import java.beans.PropertyChangeListener;
//import java.sql.Timestamp;

import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.DefaultIncludeInSearchEditor;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.StringLengthValidationRequried;
import org.archiviststoolkit.exceptions.DuplicateLinkException;

import javax.swing.*;

import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.BasicEventList;

/**
 * The Names class represents a name authority record.
 */

public class Names extends BasicNames {

	public static final String PROPERTYNAME_NAME_SOURCE = "nameSource";
	public static final String PROPERTYNAME_NAME_RULE = "nameRule";

	public static final String PROPERTYNAME_DESCRIPTION_TYPE    = "descriptionType";
	public static final String PROPERTYNAME_DESCRIPTION_NOTE    = "descriptionNote";
	public static final String PROPERTYNAME_CITATION    = "citation";

	public static final String PROPERTYNAME_SALUTATION = "salutation";
	public static final String PROPERTYNAME_CONTACT_ADDRESS_1 = "contactAddress1";
	public static final String PROPERTYNAME_CONTACT_ADDRESS_2 = "contactAddress2";
	public static final String PROPERTYNAME_CONTACT_CITY = "contactCity";
	public static final String PROPERTYNAME_CONTACT_REGION = "contactRegion";
	public static final String PROPERTYNAME_CONTACT_COUNTRY = "contactCountry";
	public static final String PROPERTYNAME_CONTACT_MAIL_CODE = "contactMailCode";
	public static final String PROPERTYNAME_CONTACT_PHONE = "contactPhone";
	public static final String PROPERTYNAME_CONTACT_FAX = "contactFax";
	public static final String PROPERTYNAME_CONTACT_EMAIL = "contactEmail";
	public static final String PROPERTYNAME_CONTACT_NAME = "contactName";


	/**
	 * The core name fields
	 */
	/**
	 * Source.
	 */
	@IncludeInApplicationConfiguration(3)
	@DefaultIncludeInSearchEditor
	@StringLengthValidationRequried(50)
	private String nameSource = "";
	/**
	 * Rules.
	 */
	@IncludeInApplicationConfiguration(4)
	@DefaultIncludeInSearchEditor
	@StringLengthValidationRequried(50)
	private String nameRule = "";

   /**
	 * description fields
	 */
	/** type. */
	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String descriptionType = "";
	/** note. */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private String descriptionNote = "";
	/** citation. */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private String citation = "";

	/**
	 * contact information fields
	 */
	/**
	 * address 1.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String salutation = "";
	/**
	 * address 1.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String contactAddress1 = "";
	/**
	 * address 2.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String contactAddress2 = "";
	/**
	 * city.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String contactCity = "";
	/**
	 * region.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String contactRegion = "";

	/**
	 * country.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String contactCountry = "";
	/**
	 * mail code.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String contactMailCode = "";
	/**
	 * phone.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String contactPhone = "";
	/**
	 * fax.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String contactFax = "";
	/**
	 * email.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String contactEmail = "";
	/**
	 * name.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String contactName = "";

  /**
  * md5 hash used to index the bean in the database
  */
	@ExcludeFromDefaultValues
  private String md5Hash = "";

  private Set<NameContactNotes> contactNotes = new TreeSet<NameContactNotes>();
	private Set<NonPreferredNames> nonPreferredNames = new HashSet<NonPreferredNames>();
//    private Set accessionsNames = new HashSet();
	private Set<ArchDescriptionNames> archDescriptionNames = new HashSet<ArchDescriptionNames>();

	/**
	 * @return nameSource
	 */
	public String getNameSource() {
		if (this.nameSource != null) {
			return this.nameSource;
		} else {
			return "";
		}
	}

	public void setNameSource(String nameSource) {
		this.nameSource = nameSource;
	}

	public String getNameRule() {
		if (this.nameRule != null) {
			return this.nameRule;
		} else {
			return "";
		}
	}

	public void setNameRule(String nameRule) {
		Object oldValue = getNameRule();
		this.nameRule = nameRule;
		firePropertyChange("nameRule", oldValue, nameRule);
	}

	public String getDescriptionType() {
		if (this.descriptionType != null) {
			return this.descriptionType;
		} else {
			return "";
		}
	}

	public void setDescriptionType(String descriptionType) {
		this.descriptionType = descriptionType;
	}

	public String getDescriptionNote() {
		if (this.descriptionNote != null) {
			return this.descriptionNote;
		} else {
			return "";
		}
	}

	public void setDescriptionNote(String descriptionNote) {
		this.descriptionNote = descriptionNote;
	}

//	public String getCitation() {
//		if (this.citation != null) {
//			return this.citation;
//		} else {
//			return "";
//		}
//	}
	public String getCitation() {
		return this.citation;
	}

	public void setCitation(String citation) {
		this.citation = citation;
	}

	public String getContactAddress1() {
		if (this.contactAddress1 != null) {
			return this.contactAddress1;
		} else {
			return "";
		}
	}

	public void setContactAddress1(String contactAddress1) {
		this.contactAddress1 = contactAddress1;
	}

	public String getContactAddress2() {
		if (this.contactAddress2 != null) {
			return this.contactAddress2;
		} else {
			return "";
		}
	}

	public void setContactAddress2(String contactAddress2) {
		this.contactAddress2 = contactAddress2;
	}

	public String getContactCity() {
		if (this.contactCity != null) {
			return this.contactCity;
		} else {
			return "";
		}
	}

	public void setContactCity(String contactCity) {
		this.contactCity = contactCity;
	}

	public String getContactRegion() {
		if (this.contactRegion != null) {
			return this.contactRegion;
		} else {
			return "";
		}
	}

	public void setContactRegion(String contactRegion) {
		this.contactRegion = contactRegion;
	}

	public String getContactCountry() {
		if (this.contactCountry != null) {
			return this.contactCountry;
		} else {
			return "";
		}
	}

	public void setContactCountry(String contactCountry) {
		this.contactCountry = contactCountry;
	}

	public String getContactMailCode() {
		if (this.contactMailCode != null) {
			return this.contactMailCode;
		} else {
			return "";
		}
	}

	public void setContactMailCode(String contactMailCode) {
		this.contactMailCode = contactMailCode;
	}

	public String getContactPhone() {
		if (this.contactPhone != null) {
			return this.contactPhone;
		} else {
			return "";
		}
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactFax() {
		if (this.contactFax != null) {
			return this.contactFax;
		} else {
			return "";
		}
	}

	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}

	public String getContactEmail() {
		if (this.contactEmail != null) {
			return this.contactEmail;
		} else {
			return "";
		}
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactName() {
		if (this.contactName != null) {
			return this.contactName;
		} else {
			return "";
		}
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Set<NameContactNotes> getContactNotes() {
		return this.contactNotes;
	}

	public void setContactNotes(Set<NameContactNotes> contactNotes) {
		this.contactNotes = contactNotes;
	}

	/**
	 * Adds a <tt>BillingDetails</tt> to the set.
	 * <p/>
	 * This method checks if there is only one billing method
	 * in the set, then makes this the default.
	 *
	 * @param nameContactNote
	 */
	public void addNameContactNote(NameContactNotes nameContactNote) {
		if (nameContactNote == null)
			throw new IllegalArgumentException("Can't add a null contact note.");
		SequencedObject.adjustSequenceNumberForAdd(this.getContactNotes(), nameContactNote);
		this.getContactNotes().add(nameContactNote);
	}

	/**
	 * Removes a <tt>BillingDetails</tt> from the set.
	 * <p/>
	 * This method checks if the removed is the default element,
	 * and will throw a BusinessException if there is more than one
	 * left to chose from. This might actually not be the best way
	 * to handle this situation.
	 *
	 * @param nameContactNote
	 */
	private void removeNameContactNote(NameContactNotes nameContactNote) {
		if (nameContactNote == null)
			throw new IllegalArgumentException("Can't add a null contact note.");

		getContactNotes().remove(nameContactNote);
		SequencedObject.resequenceSequencedObjects(this.getContactNotes());
	}

	public NameContactNotes getNameContactNote(int index) {
		ArrayList<NameContactNotes> notes = new ArrayList<NameContactNotes>(getContactNotes());
		return notes.get(index);
	}

	public int getNextContactNoteSequenceNumber() {
		int highestSequenceNumber = 0;
		for (NameContactNotes note : getContactNotes()) {
			if (note.getSequenceNumber() > highestSequenceNumber) {
				highestSequenceNumber = note.getSequenceNumber();
			}
		}
		return highestSequenceNumber + 1;
	}

	/**
	 * A dummy place holder to be overridden by classes that have related objects
	 *
	 * @param domainObject the domain object to be removed
	 */
	public void removeRelatedObject(DomainObject domainObject) {
		if (domainObject instanceof NameContactNotes) {
			removeNameContactNote((NameContactNotes) domainObject);
        } else if (domainObject instanceof NonPreferredNames) {
            removeNonPreferredName((NonPreferredNames) domainObject);
		}
	}

	/**
	 * A dummy place holder to be overridden by classes that have related objects
	 *
	 * @param clazz the domain object to be removed
	 */
	public Collection getRelatedCollection(Class clazz) {
		if (clazz == NameContactNotes.class) {
			return getContactNotes();
		} else if (clazz == NonPreferredNames.class) {
			return getNonPreferredNames();
		} else {
			return null;
		}
	}

	public Set<NonPreferredNames> getNonPreferredNames() {
		return nonPreferredNames;
	}

	public NonPreferredNames getNonPreferredName(int index) {
		ArrayList<NonPreferredNames> nonPreferredNames = new ArrayList<NonPreferredNames>(getNonPreferredNames());
		return nonPreferredNames.get(index);
	}

	public void setNonPreferredNames(Set<NonPreferredNames> nonPreferredNames) {
		this.nonPreferredNames = nonPreferredNames;
	}

	/**
	 * Adds a <tt>BillingDetails</tt> to the set.
	 * <p/>
	 * This method checks if there is only one billing method
	 * in the set, then makes this the default.
	 *
	 * @param nonPreferredName
	 */
	public void addNonPreferredName(NonPreferredNames nonPreferredName) {
		if (nonPreferredName == null)
			throw new IllegalArgumentException("Can't add a null contact note.");
		this.getNonPreferredNames().add(nonPreferredName);

	}

	/**
	 * Removes a <tt>BillingDetails</tt> from the set.
	 * <p/>
	 * This method checks if the removed is the default element,
	 * and will throw a BusinessException if there is more than one
	 * left to chose from. This might actually not be the best way
	 * to handle this situation.
	 *
	 * @param nonPreferredName
	 */
	private void removeNonPreferredName(NonPreferredNames nonPreferredName) {
		if (nonPreferredName == null)
			throw new IllegalArgumentException("Can't add a null contact note.");

		getNonPreferredNames().remove(nonPreferredName);
	}

//    public Set getAccessionsNames() {
//        return accessionsNames;
//    }
//
//    public void setAccessionsNames(Set accessionsNames) {
//        this.accessionsNames = accessionsNames;
//    }
//
//	public Boolean getNamePersonalDirectOrder() {
//		return namePersonalDirectOrder;
//	}

	public Set<ArchDescriptionNames> getArchDescriptionNames() {
		return archDescriptionNames;
	}

	public void setArchDescriptionNames(Set archDescriptionNames) {
		this.archDescriptionNames = archDescriptionNames;
	}

	public void addArchDesctiption(ArchDescriptionNames archDescriptionName) throws DuplicateLinkException {
		if (containsArchDescriptionLink(archDescriptionName.getArchDescription(), archDescriptionName.getNameLinkFunction())) {
			throw new DuplicateLinkException(archDescriptionName.getArchDescription().toString());
		} else {
			getArchDescriptionNames().add(new ArchDescriptionNames(this, archDescriptionName.getArchDescription(),
                    archDescriptionName.getRole(), archDescriptionName.getNameLinkFunction(), archDescriptionName.getForm()));
		}
	}
	
	private boolean containsArchDescriptionLink(ArchDescription archDescription, String nameLinkFunction) {
		for (ArchDescriptionNames link: getArchDescriptionNames()) {
			if (archDescription instanceof Resources &&
					link.getResource() != null &&
					link.getResource().equals(archDescription) &&
                    link.getNameLinkFunction().equals(nameLinkFunction)) {
				return true;
			} else if (archDescription instanceof ResourcesComponents &&
					link.getResourceComponent() != null &&
					link.getResourceComponent().equals(archDescription) &&
                    link.getNameLinkFunction().equals(nameLinkFunction)) {
				return true;
			} else if (archDescription instanceof Accessions &&
					link.getAccession() != null &&
					link.getAccession().equals(archDescription) &&
                    link.getNameLinkFunction().equals(nameLinkFunction)) {
				return true;
			} else if (archDescription instanceof DigitalObjects &&
					link.getDigitalObject() != null &&
					link.getDigitalObject().equals(archDescription) &&
                    link.getNameLinkFunction().equals(nameLinkFunction)) {
				return true;
			}
		}
		return false;
	}

	public void createSortName(JTextField sortNameField) {
		super.createSortName(this.getNameSource(), this.getNameRule());
	}

	public String getSalutation() {
		if (this.salutation != null) {
			return this.salutation;
		} else {
			return "";
		}
	}

	public void setSalutation(String salutation) {
		Object oldValue = getSalutation();
		this.salutation = salutation;
		firePropertyChange(PROPERTYNAME_SALUTATION, oldValue, salutation);
	}

	//convienence methods for getting just accessions and just resources

	public Set getResources () {
		Set<Resources> resources = new TreeSet<Resources>();
		ResourcesDAO access = new ResourcesDAO();
		for(ArchDescriptionNames archDescriptionName : archDescriptionNames) {
			if (archDescriptionName.getResource() != null) {
				resources.add(archDescriptionName.getResource());
			} else if (archDescriptionName.getResourceComponent() != null) {
				resources.add(access.findResourceByComponent(archDescriptionName.getResourceComponent()));
			}
		}
		return resources;
	}

	public Set getAccessions () {
		Set<Accessions> accessions = new TreeSet<Accessions>();
		for(ArchDescriptionNames archDescriptionName: archDescriptionNames) {
			if (archDescriptionName.getAccession() != null) {
				accessions.add(archDescriptionName.getAccession());
			}
		}
		return accessions;
	}

    public Set getDigitalObjects () {
		Set<DigitalObjects> digitalObjects = new TreeSet<DigitalObjects>();
		for(ArchDescriptionNames archDescriptionName: archDescriptionNames) {
			if (archDescriptionName.getDigitalObject() != null) {
				digitalObjects.add(archDescriptionName.getDigitalObject());
			}
		}
		return digitalObjects;
	}

	private static SortedList namesGlazedList;
	/** event list that hosts the issues */
	private static EventList namesEventList = new BasicEventList();
	private static ArrayList namesList;

    //todo rename method to initNameLookupList
    public static void initNamesLookupList() {
		DomainAccessObject access = new DomainAccessObjectImpl(Names.class);
		try {
			Collection names = access.findByNamedQuery("findAllNamesSorted");
			namesList = new ArrayList(names);
			Collections.sort(namesList);
			namesEventList.addAll(names);
			namesGlazedList = new SortedList(namesEventList);
		} catch (LookupException e) {
			e.printStackTrace();
		}
	}

	public static void addNameToLookupList(Names name) {
		namesList.add(name);
		Collections.sort(namesList);
		namesEventList.add(name);
	}

	public static void removeNameFromLookupList(Names name) {
		namesList.remove(name);
		Collections.sort(namesList);
		namesEventList.remove(name);
	}


	public static SortedList getNamesGlazedList() {
		return namesGlazedList;
	}

	public static void setNamesGlazedList(SortedList namesGlazedList) {
		Names.namesGlazedList = namesGlazedList;
	}

	public static String selectNameType(Component rootComponent) {
		Object[] possibilities = {"Person", "Corporate Body", "Family"};
		ImageIcon icon = null;
		return  (String) JOptionPane.showInputDialog(rootComponent, "What type of name record would you like to create",
				"", JOptionPane.PLAIN_MESSAGE, icon, possibilities, "Person");

	}

  // Method to set the md5 hash that uniquely identifies this record
  public void setMd5Hash(String md5Hash) {
    this.md5Hash = md5Hash;
  }

  // Method to return the md5 hash of this basic name object
  public String getMd5Hash() {
    return this.md5Hash;
  }
}