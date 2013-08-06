/**
 * Archivists' Toolkit(TM) Copyright � 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
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

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.*;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.util.StringHelper;

import java.util.*;


public class Accessions extends AccessionsResourcesCommon {

	public static final String PROPERTYNAME_ACCESSION_NUMBER    = "accessionNumber";
	public static final String PROPERTYNAME_ACCESSION_NUMBER_1    = "accessionNumber1";
	public static final String PROPERTYNAME_ACCESSION_NUMBER_2 = "accessionNumber2";
	public static final String PROPERTYNAME_ACCESSION_NUMBER_3  = "accessionNumber3";
	public static final String PROPERTYNAME_ACCESSION_NUMBER_4    = "accessionNumber4";
	public static final String PROPERTYNAME_RESOURCES    = "resources";
	public static final String PROPERTYNAME_ACCESSION_DATE    = "accessionDate";
	public static final String PROPERTYNAME_RESOURCE_TYPE   = "resourceType";
	public static final String PROPERTYNAME_DESCRIPTION    = "description";
	public static final String PROPERTYNAME_INVENTORY    = "inventory";
	public static final String PROPERTYNAME_CONDITION_NOTE    = "conditionNote";
	public static final String PROPERTYNAME_ACQUISITION_TYPE    = "acquisitionType";
	public static final String PROPERTYNAME_ACKNOWLEDGEMENT_DATE    = "acknowledgementDate";
	public static final String PROPERTYNAME_ACKNOWLEDGEMENT_SENT    = "acknowledgementSent";
	public static final String PROPERTYNAME_AGREEMENT_SENT_DATE    = "agreementSentDate";
	public static final String PROPERTYNAME_AGREEMENT_SENT    = "agreementSent";
	public static final String PROPERTYNAME_AGREEMENT_RECEIVED_DATE    = "agreementReceivedDate";
	public static final String PROPERTYNAME_AGREEMENT_RECEIVED    = "agreementReceived";
	public static final String PROPERTYNAME_RIGHTS_TRANSFERRED    = "rightsTransferred";
	public static final String PROPERTYNAME_RIGHTS_TRANSFERRED_DATE    = "rightsTransferredDate";
	public static final String PROPERTYNAME_RIGHTS_TRANSFERRED_NOTE = "rightsTransferredNote";
	public static final String PROPERTYNAME_PROCESSING_PLAN    = "processingPlan";
	public static final String PROPERTYNAME_ACCESSION_PROCESSED_DATE    = "accessionProcessedDate";
	public static final String PROPERTYNAME_ACCESSION_PROCESSED    = "accessionProcessed";
	public static final String PROPERTYNAME_DISPOSITION_NOTE    = "accessionDispositionNote";
	public static final String PROPERTYNAME_RETENTION_RULE    = "retentionRule";
	public static final String PROPERTYNAME_CATALOGED    = "cataloged";
	public static final String PROPERTYNAME_CATALOGED_NOTE    = "catalogedNote";
	public static final String PROPERTYNAME_CATALOGED_DATE    = "catalogedDate";
	public static final String PROPERTYNAME_ACCESS_RESTRICTIONS_NOTE = "accessRestrictionsNote";
	public static final String PROPERTYNAME_ACCESS_RESTRICTIONS = "accessRestrictions";
	public static final String PROPERTYNAME_USE_RESTRICTIONS_NOTE = "useRestrictionsNote";
	public static final String PROPERTYNAME_USE_RESTRICTIONS = "useRestrictions";
	public static final String PROPERTYNAME_REPOSITORY = "repository";
	public static final String PROPERTYNAME_LOCATIONS = "locations";
	public static final String PROPERTYNAME_ACCESSIONS_ID = "accessionId";
	public static final String PROPERTYNAME_GENERAL_ACCESSION_NOTE = "generalAccessionNote";
	public static final String PROPERTYNAME_PROCESSING_PRIORITY = "processingPriority";
	public static final String PROPERTYNAME_PROCESSORS = "processors";
	public static final String PROPERTYNAME_PROCESSING_STATUS = "processingStatus";
	public static final String PROPERTYNAME_PROCESSING_STARTED_DATE = "processingStartedDate";
	public static final String PROPERTYNAME_USER_DEFINED_DATE1 = "userDefinedDate1";
	public static final String PROPERTYNAME_USER_DEFINED_DATE2 = "userDefinedDate2";
	public static final String PROPERTYNAME_USER_DEFINED_BOOLEAN1 = "userDefinedBoolean1";
	public static final String PROPERTYNAME_USER_DEFINED_BOOLEAN2 = "userDefinedBoolean2";
	public static final String PROPERTYNAME_USER_DEFINED_INTEGER1 = "userDefinedInteger1";
	public static final String PROPERTYNAME_USER_DEFINED_INTEGER2 = "userDefinedInteger2";
	public static final String PROPERTYNAME_USER_DEFINED_REAL1 = "userDefinedReal1";
	public static final String PROPERTYNAME_USER_DEFINED_REAL2 = "userDefinedReal2";
	public static final String PROPERTYNAME_USER_DEFINED_STRING1 = "userDefinedString1";
	public static final String PROPERTYNAME_USER_DEFINED_STRING2 = "userDefinedString2";
	public static final String PROPERTYNAME_USER_DEFINED_STRING3 = "userDefinedString3";
	public static final String PROPERTYNAME_USER_DEFINED_TEXT1 = "userDefinedText1";
	public static final String PROPERTYNAME_USER_DEFINED_TEXT2 = "userDefinedText2";
	public static final String PROPERTYNAME_USER_DEFINED_TEXT3 = "userDefinedText3";
	public static final String PROPERTYNAME_USER_DEFINED_TEXT4 = "userDefinedText4";

	// Fields

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long accessionId;

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried(10)
	private String accessionNumber1 ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried(10)
    private String accessionNumber2 ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried(10)
    private String accessionNumber3 ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried(10)
    private String accessionNumber4 ="";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@DefaultIncludeInSearchEditor
	private Date accessionDate;

	@IncludeInApplicationConfiguration
	@DefaultIncludeInSearchEditor
	@StringLengthValidationRequried
	private String resourceType ="";

	@IncludeInApplicationConfiguration
	private String description ="";

	@IncludeInApplicationConfiguration
	private String inventory ="";

	@IncludeInApplicationConfiguration
	private String conditionNote ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String acquisitionType ="";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Date acknowledgementDate;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Date agreementSentDate;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Date agreementReceivedDate;

	@IncludeInApplicationConfiguration
    @ExcludeFromDefaultValues
    private Boolean rightsTransferred = false;

	@IncludeInApplicationConfiguration
	private String processingPlan ="";

	@IncludeInApplicationConfiguration
  @ExcludeFromDefaultValues
  private Date accessionProcessedDate;

	@IncludeInApplicationConfiguration
	private String accessionDispositionNote ="";

	@IncludeInApplicationConfiguration
	private String retentionRule ="";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean cataloged = false;

	@IncludeInApplicationConfiguration
	private String catalogedNote ="";

	@IncludeInApplicationConfiguration
	private String accessRestrictionsNote ="";

	@IncludeInApplicationConfiguration
	private String useRestrictionsNote ="";

	@IncludeInApplicationConfiguration
	private String generalAccessionNote ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String processingPriority ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String processors ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String processingStatus ="";

	@IncludeInApplicationConfiguration
  @ExcludeFromDefaultValues
  private Boolean acknowledgementSent;

	@IncludeInApplicationConfiguration
  @ExcludeFromDefaultValues
  private Boolean agreementSent;

	@IncludeInApplicationConfiguration
  @ExcludeFromDefaultValues
  private Boolean agreementReceived;

	@IncludeInApplicationConfiguration
    @ExcludeFromDefaultValues
    private Boolean accessionProcessed;

	@IncludeInApplicationConfiguration
	private Boolean accessRestrictions;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Boolean useRestrictions;

	@IncludeInApplicationConfiguration
  @ExcludeFromDefaultValues
  private Date catalogedDate;

	@IncludeInApplicationConfiguration
  @ExcludeFromDefaultValues
  private Date rightsTransferredDate;

	@IncludeInApplicationConfiguration
  @ExcludeFromDefaultValues
  private Date processingStartedDate;

	@IncludeInApplicationConfiguration
	private String rightsTransferredNote;

	@IncludeInApplicationConfiguration
	private Date userDefinedDate1;

	@IncludeInApplicationConfiguration
	private Date userDefinedDate2;

	@IncludeInApplicationConfiguration
	private Boolean userDefinedBoolean1;

	@IncludeInApplicationConfiguration
	private Boolean userDefinedBoolean2;

	@IncludeInApplicationConfiguration
	private Integer userDefinedInteger1;

	@IncludeInApplicationConfiguration
	private Integer userDefinedInteger2;

	@IncludeInApplicationConfiguration
	private Double userDefinedReal1;

	@IncludeInApplicationConfiguration
	private Double userDefinedReal2;

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String userDefinedString1 ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String userDefinedString2 ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String userDefinedString3 ="";

	@IncludeInApplicationConfiguration
	private String userDefinedText1 ="";

	@IncludeInApplicationConfiguration
	private String userDefinedText2 ="";

	@IncludeInApplicationConfiguration
	private String userDefinedText3 ="";

	@IncludeInApplicationConfiguration
	private String userDefinedText4 ="";

	@IncludeInApplicationConfiguration
	@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	private Repositories repository;

	private Set<AccessionsResources> resources = new HashSet<AccessionsResources>();
	public static final String PROPERTYNAME_ARCH_DESCRIPTION_LEVEL = "level";
	public static final String PROPERTYNAME_ARCH_DESCRIPTION_LANGUAGE_CODE = "languageCode";
	public static final String PROPERTYNAME_ARCH_DESCRIPTION_REPOSITORY_PROCESSING_NOTE = "repositoryProcessingNote";
	private String level = "";
	private String languageCode = "";
	private String repositoryProcessingNote = "";
	private Boolean internalOnly = false;
	private Set<ArchDescriptionInstances> instances = new HashSet<ArchDescriptionInstances>();

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Set<AccessionsLocations> locations = new HashSet<AccessionsLocations>();
	
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String displayCreator ="";
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String displayRepository ="";
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String displaySource ="";

	/**
	 *
	 */
	public String getAccessionNumber1() {
		if (this.accessionNumber1 != null) {
			return this.accessionNumber1;
		} else {
			return "";
		}
	}

	public void setAccessionNumber1(String accessionNumber1) {
		String oldValue = this.accessionNumber1;
		this.accessionNumber1 = accessionNumber1;
	}

	/**
	 *
	 */
	public String getAccessionNumber2() {
		if (this.accessionNumber2 != null) {
			return this.accessionNumber2;
		} else {
			return "";
		}
	}

	public void setAccessionNumber2(String accessionNumber2) {
		String oldValue = this.accessionNumber2;
		this.accessionNumber2 = accessionNumber2;
	}

	/**
	 *
	 */
	public String getAccessionNumber3() {
		if (this.accessionNumber3 != null) {
			return this.accessionNumber3;
		} else {
			return "";
		}
	}

	public void setAccessionNumber3(String accessionNumber3) {
		String oldValue = this.accessionNumber3;
		this.accessionNumber3 = accessionNumber3;
	}

	/**
	 *
	 */
	public String getAccessionNumber4() {
		if (this.accessionNumber4 != null) {
			return this.accessionNumber4;
		} else {
			return "";
		}
	}

	public void setAccessionNumber4(String accessionNumber4) {
		String oldValue = this.accessionNumber4;
		this.accessionNumber4 = accessionNumber4;
	}

	/**
	 *
	 */
	public Date getAccessionDate() {
		return this.accessionDate;
	}

	public void setAccessionDate(Date accessionDate) {
		this.accessionDate = accessionDate;
	}

	public String getAccessRestrictionsNote() {
		if (this.accessRestrictionsNote != null) {
			return this.accessRestrictionsNote;
		} else {
			return "";
		}
	}

	public void setAccessRestrictionsNote(String accessRestrictionsNote) {
		this.accessRestrictionsNote = accessRestrictionsNote;
	}
	/**
	 *
	 */
	public String getResourceType() {
		if (this.resourceType != null) {
			return this.resourceType;
		} else {
			return "";
		}
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	/**
	 *
	 */
	public String getDescription() {
		if (this.description != null) {
			return this.description;
		} else {
			return "";
		}
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 *
	 */
	public String getInventory() {
		if (this.inventory != null) {
			return this.inventory;
		} else {
			return "";
		}
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
	}

	/**
	 *
	 */
	public String getConditionNote() {
		if (this.conditionNote != null) {
			return this.conditionNote;
		} else {
			return "";
		}
	}

	public void setConditionNote(String conditionNote) {
		this.conditionNote = conditionNote;
	}

	/**
	 *
	 */
	public String getAcquisitionType() {
		if (this.acquisitionType != null) {
			return this.acquisitionType;
		} else {
			return "";
		}
	}

	public void setAcquisitionType(String acquisitionType) {
		this.acquisitionType = acquisitionType;
	}

	/**
	 *
	 */
	public Date getAcknowledgementDate() {
		return this.acknowledgementDate;
	}

	public void setAcknowledgementDate(Date acknowledgementDate) {
		this.acknowledgementDate = acknowledgementDate;
	}

	/**
	 *
	 */
	public Date getAgreementSentDate() {
		return this.agreementSentDate;
	}

	public void setAgreementSentDate(Date agreementSentDate) {
		this.agreementSentDate = agreementSentDate;
	}

	/**
	 *
	 */
	public Date getAgreementReceivedDate() {
		return this.agreementReceivedDate;
	}

	public void setAgreementReceivedDate(Date agreementReceivedDate) {
		this.agreementReceivedDate = agreementReceivedDate;
	}

	/**
	 *
	 */
	public Boolean getRightsTransferred() {
		return this.rightsTransferred;
	}

	public void setRightsTransferred(Boolean rightsTransferred) {
		this.rightsTransferred = rightsTransferred;
	}

	/**
	 *
	 */
	public String getProcessingPlan() {
		if (this.processingPlan != null) {
			return this.processingPlan;
		} else {
			return "";
		}
	}

	public void setProcessingPlan(String processingPlan) {
		this.processingPlan = processingPlan;
	}

	/**
	 *
	 */
	public Date getAccessionProcessedDate() {
		return this.accessionProcessedDate;
	}

	public void setAccessionProcessedDate(Date accessionProcessedDate) {
		this.accessionProcessedDate = accessionProcessedDate;
	}



	public Long getIdentifier() {
		return getAccessionId();
	}

	public void setIdentifier(Long identifier) {
		this.setAccessionId(identifier);
	}

	public Long getAccessionId() {
		return accessionId;
	}

	public void setAccessionId(Long accessionId) {
		this.accessionId = accessionId;
	}


	public AccessionsResources addResource(Resources resource) throws DuplicateLinkException {

		if (resource == null)
			throw new IllegalArgumentException("Can't add a null location.");
		if (containsResourceLink(resource)) {
			throw new DuplicateLinkException(resource.toString());
		} else {
			AccessionsResources link = new AccessionsResources(resource, this);
			this.getResources().add(link);
			return link;
		}
	}

	public boolean containsResourceLink(Resources resource) {
		for (AccessionsResources resourceLink: getResources()) {
			if (resourceLink.getResource() == resource) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds a <tt>BillingDetails</tt> to the set.
	 * <p/>
	 * This method checks if there is only one billing method
	 * in the set, then makes this the default.
	 *
	 * @param accessionResource
	 */
	public void addAccessionsResources(AccessionsResources accessionResource) {
		if (accessionResource == null)
			throw new IllegalArgumentException("Can't add a null contact note.");
		this.getResources().add(accessionResource);

	}


	/**
	 * Removes a <tt>BillingDetails</tt> from the set.
	 * <p/>
	 * This method checks if the removed is the default element,
	 * and will throw a BusinessException if there is more than one
	 * left to chose from. This might actually not be the best way
	 * to handle this situation.
	 *
	 * @param accessionResource
	 */
	private void removeAccessionsResource(AccessionsResources accessionResource) {
		if (accessionResource == null)
			throw new IllegalArgumentException("Can't remove a null resource.");

		getResources().remove(accessionResource);
	}

	/**
	 * A dummy place holder to be overridden by classes that have related objects
	 * @param domainObject the domain object to be removed
	 */
	public void removeRelatedObject(DomainObject domainObject) throws ObjectNotRemovedException {
		if (domainObject instanceof AccessionsResources) {
			removeAccessionsResource((AccessionsResources)domainObject);

		} else if (domainObject instanceof AccessionsLocations) {
			removeLocation((AccessionsLocations) domainObject);

		} else {
			super.removeRelatedObject(domainObject);
		}
	}

	public Collection getRelatedCollection(DomainObject domainObject) {
		if (domainObject instanceof AccessionsResources) {
			return getResources();
		} else if (domainObject instanceof AccessionsLocations) {
			return getLocations();
		} else {
			return super.getRelatedCollection(domainObject);
		}
	}

	public AccessionsLocations addLocation(Locations location) throws DuplicateLinkException {
		return addLocation(location, "");
	}

	public AccessionsLocations addLocation(Locations location, String note) throws DuplicateLinkException {

		if (location == null)
			throw new IllegalArgumentException("Can't add a null location.");
		if (containsLocationLink(location)) {
			throw new DuplicateLinkException(location.toString());
		} else {
			AccessionsLocations link = new AccessionsLocations(location, this);
			link.setNote(note);
			this.getLocations().add(link);
			return link;
		}
	}

	public boolean containsLocationLink(Locations location) {
		for (AccessionsLocations locationLink: getLocations()) {
			if (locationLink.getLocation() == location) {
				return true;
			}
		}
		return false;
	}

	protected void removeLocation(AccessionsLocations location) {
		if (location == null)
			throw new IllegalArgumentException("Can't remove a location.");

		getLocations().remove(location);
	}



	@DefaultIncludeInSearchEditor
	@IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_ACCESSION_NUMBER, value=1)
	@ExcludeFromDefaultValues
	public String getAccessionNumber() {
		return StringHelper.concat(".", this.getAccessionNumber1(), this.getAccessionNumber2(), this.getAccessionNumber3(), this.getAccessionNumber4());
	}

	public String getAccessionDispositionNote() {
		if (this.accessionDispositionNote != null) {
			return this.accessionDispositionNote;
		} else {
			return "";
		}
	}

	public void setAccessionDispositionNote(String accessionDispositionNote) {
		this.accessionDispositionNote = accessionDispositionNote;
	}

	public String getRetentionRule() {
		if (this.retentionRule != null) {
			return this.retentionRule;
		} else {
			return "";
		}
	}

	public void setRetentionRule(String retentionRule) {
		this.retentionRule = retentionRule;
	}

	public Set<AccessionsResources> getResources() {
		return resources;
	}

	public void setResources(Set resources) {
		this.resources = resources;
	}

	public Repositories getRepository() {
		return repository;
	}

	public void setRepository(Repositories repository) {
		this.repository = repository;
	}

//	@IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_REPOSITORY_NAME)
	public String getRepositoryName() {
		if (this.getRepository() == null) {
			return "";
		} else {
			return this.getRepository().getRepositoryName();
		}
	}

	public Boolean getCataloged() {
		return cataloged;
	}

	public void setCataloged(Boolean cataloged) {
		this.cataloged = cataloged;
	}

	public String getCatalogedNote() {
		if (this.catalogedNote != null) {
			return this.catalogedNote;
		} else {
			return "";
		}
	}

	public void setCatalogedNote(String catalogedNote) {
		this.catalogedNote = catalogedNote;
	}

	public String getUniqueConstraintKey() {
		return "Repository: " + this.getRepository().getRepositoryName() + ", Accession number: " + this.getAccessionNumber();
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getRepositoryProcessingNote() {
		return repositoryProcessingNote;
	}

	public void setRepositoryProcessingNote(String repositoryProcessingNote) {
		this.repositoryProcessingNote = repositoryProcessingNote;
	}

	public void addInstance(ArchDescriptionInstances resourceComponentInstance) {
		this.instances.add(resourceComponentInstance);
	}

	protected void removeInstance(ArchDescriptionInstances resourceComponentInstance) {
		if (resourceComponentInstance == null)
			throw new IllegalArgumentException("Can't add a null contact note.");

		this.instances.remove(resourceComponentInstance);
	}

	public ArchDescriptionInstances getInstance(int index) {
		ArrayList instances = new ArrayList<ArchDescriptionInstances>(getInstances());
		return (ArchDescriptionInstances) instances.get(index);
	}

	public Set<ArchDescriptionInstances> getInstances() {
		return instances;
	}

	public void setInstances(Set<ArchDescriptionInstances> instances) {
		this.instances = instances;
	}

	public Boolean getInternalOnly() {
		return internalOnly;
	}

	public void setInternalOnly(Boolean internalOnly) {
		this.internalOnly = internalOnly;
	}

	public Set<AccessionsLocations> getLocations() {
		return locations;
	}

	public void setLocations(Set<AccessionsLocations> locations) {
		this.locations = locations;
	}

	public String toString() {
		return this.getAccessionNumber() + " - " + this.getTitle();
	}

	public String getUseRestrictionsNote() {
		if (this.useRestrictionsNote != null) {
			return this.useRestrictionsNote;
		} else {
			return "";
		}
	}

	public void setUseRestrictionsNote(String useRestrictionsNote) {
		this.useRestrictionsNote = useRestrictionsNote;
	}

	public String getGeneralAccessionNote() {
		if (this.generalAccessionNote != null) {
			return this.generalAccessionNote;
		} else {
			return "";
		}
	}

	public void setGeneralAccessionNote(String generalAccessionNote) {
		this.generalAccessionNote = generalAccessionNote;
	}

	public String getProcessingPriority() {
		if (this.processingPriority != null) {
			return this.processingPriority;
		} else {
			return "";
		}
	}

	public void setProcessingPriority(String processingPriority) {
		this.processingPriority = processingPriority;
	}

	public String getProcessors() {
		if (this.processors != null) {
			return this.processors;
		} else {
			return "";
		}
	}

	public void setProcessors(String processors) {
		this.processors = processors;
	}

	public String getProcessingStatus() {
		if (this.processingStatus != null) {
			return this.processingStatus;
		} else {
			return "";
		}
	}

	public void setProcessingStatus(String processingStatus) {
		this.processingStatus = processingStatus;
	}

	public Date getUserDefinedDate1() {
		return userDefinedDate1;
	}

	public void setUserDefinedDate1(Date userDefinedDate1) {
		this.userDefinedDate1 = userDefinedDate1;
	}

	public Date getUserDefinedDate2() {
		return userDefinedDate2;
	}

	public void setUserDefinedDate2(Date userDefinedDate2) {
		this.userDefinedDate2 = userDefinedDate2;
	}

	public String getUserDefinedString1() {
		if (this.userDefinedString1 != null) {
			return this.userDefinedString1;
		} else {
			return "";
		}
	}

	public void setUserDefinedString1(String userDefinedString1) {
		this.userDefinedString1 = userDefinedString1;
	}

	public String getUserDefinedString2() {
		if (this.userDefinedString2 != null) {
			return this.userDefinedString2;
		} else {
			return "";
		}
	}

	public void setUserDefinedString2(String userDefinedString2) {
		this.userDefinedString2 = userDefinedString2;
	}

	public String getUserDefinedString3() {
		if (this.userDefinedString3 != null) {
			return this.userDefinedString3;
		} else {
			return "";
		}
	}

	public void setUserDefinedString3(String userDefinedString3) {
		this.userDefinedString3 = userDefinedString3;
	}

	public String getUserDefinedText1() {
		if (this.userDefinedText1 != null) {
			return this.userDefinedText1;
		} else {
			return "";
		}
	}

	public void setUserDefinedText1(String userDefinedText1) {
		this.userDefinedText1 = userDefinedText1;
	}

	public String getUserDefinedText2() {
		if (this.userDefinedText2 != null) {
			return this.userDefinedText2;
		} else {
			return "";
		}
	}

	public void setUserDefinedText2(String userDefinedText2) {
		this.userDefinedText2 = userDefinedText2;
	}

	public String getUserDefinedText3() {
		if (this.userDefinedText3 != null) {
			return this.userDefinedText3;
		} else {
			return "";
		}
	}

	public void setUserDefinedText3(String userDefinedText3) {
		this.userDefinedText3 = userDefinedText3;
	}

	public String getUserDefinedText4() {
		if (this.userDefinedText4 != null) {
			return this.userDefinedText4;
		} else {
			return "";
		}
	}

	public void setUserDefinedText4(String userDefinedText4) {
		this.userDefinedText4 = userDefinedText4;
	}

	public Boolean getAcknowledgementSent() {
		return acknowledgementSent;
	}

	public void setAcknowledgementSent(Boolean acknowledgementSent) {
		this.acknowledgementSent = acknowledgementSent;
	}

	public Boolean getAgreementSent() {
		return agreementSent;
	}

	public void setAgreementSent(Boolean agreementSent) {
		this.agreementSent = agreementSent;
	}

	public Boolean getAgreementReceived() {
		return agreementReceived;
	}

	public void setAgreementReceived(Boolean agreementReceived) {
		this.agreementReceived = agreementReceived;
	}

	public Boolean getAccessionProcessed() {
		return accessionProcessed;
	}

	public void setAccessionProcessed(Boolean accessionProcessed) {
		this.accessionProcessed = accessionProcessed;
	}

	public Boolean getAccessRestrictions() {
		return accessRestrictions;
	}

	public void setAccessRestrictions(Boolean accessRestrictions) {
		this.accessRestrictions = accessRestrictions;
	}

	public Boolean getUseRestrictions() {
		return useRestrictions;
	}

	public void setUseRestrictions(Boolean useRestrictions) {
		this.useRestrictions = useRestrictions;
	}

	public Date getCatalogedDate() {
		return catalogedDate;
	}

	public void setCatalogedDate(Date catalogedDate) {
		this.catalogedDate = catalogedDate;
	}

	public Date getRightsTransferredDate() {
		return rightsTransferredDate;
	}

	public void setRightsTransferredDate(Date rightsTransferredDate) {
		this.rightsTransferredDate = rightsTransferredDate;
	}

	public Date getProcessingStartedDate() {
		return processingStartedDate;
	}

	public void setProcessingStartedDate(Date processingStartedDate) {
		this.processingStartedDate = processingStartedDate;
	}

	public String getRightsTransferredNote() {
		if (this.rightsTransferredNote != null) {
			return this.rightsTransferredNote;
		} else {
			return "";
		}
	}

	public void setRightsTransferredNote(String rightsTransferredNote) {
		this.rightsTransferredNote = rightsTransferredNote;
	}

	public Boolean getUserDefinedBoolean1() {
		return userDefinedBoolean1;
	}

	public void setUserDefinedBoolean1(Boolean userDefinedBoolean1) {
		this.userDefinedBoolean1 = userDefinedBoolean1;
	}

	public Boolean getUserDefinedBoolean2() {
		return userDefinedBoolean2;
	}

	public void setUserDefinedBoolean2(Boolean userDefinedBoolean2) {
		this.userDefinedBoolean2 = userDefinedBoolean2;
	}

	public Integer getUserDefinedInteger1() {
		return userDefinedInteger1;
	}

	public void setUserDefinedInteger1(Integer userDefinedInteger1) {
		this.userDefinedInteger1 = userDefinedInteger1;
	}

	public Integer getUserDefinedInteger2() {
		return userDefinedInteger2;
	}

	public void setUserDefinedInteger2(Integer userDefinedInteger2) {
		this.userDefinedInteger2 = userDefinedInteger2;
	}

	public Double getUserDefinedReal1() {
		return userDefinedReal1;
	}

	public void setUserDefinedReal1(Double userDefinedReal1) {
		this.userDefinedReal1 = userDefinedReal1;
	}

	public Double getUserDefinedReal2() {
		return userDefinedReal2;
	}

	public void setUserDefinedReal2(Double userDefinedReal2) {
		this.userDefinedReal2 = userDefinedReal2;
	}

	public String getDisplayCreator() {
		if (this.displayCreator != null) {
			return this.displayCreator;
		} else {
			return "";
		}
	}

	public void setDisplayCreator(String displayCreator) {
		this.displayCreator = displayCreator;
	}

	public String getDisplayRepository() {
		if (this.displayRepository != null) {
			return this.displayRepository;
		} else {
			return "";
		}
	}

	public void setDisplayRepository(String displayRepository) {
		this.displayRepository = displayRepository;
	}

	public String getDisplaySource() {
		if (this.displaySource != null) {
			return this.displaySource;
		} else {
			return "";
		}
	}

	public void setDisplaySource(String displaySource) {
		this.displaySource = displaySource;
	}
}