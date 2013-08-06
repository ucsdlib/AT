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
 *
 * @author Lee Mandell
 * Date: Mar 23, 2006
 * Time: 12:20:20 PM
 */

package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.mydomain.SequencedObject;
import org.archiviststoolkit.structure.*;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.swing.jTreeDnD.treeDnDSequencedObject;

import java.io.Serializable;
import java.util.*;

public class DigitalObjects extends ArchDescription implements Serializable, treeDnDSequencedObject {

    public static final String PROPERTYNAME_OBJECT_TYPE = "objectType";
    public static final String PROPERTYNAME_LABEL = "label";
    public static final String PROPERTYNAME_LANGUAGE_CODE = "languageCode";
    public static final String PROPERTYNAME_FILE_VERSIONS = "fileVersions";
    public static final String PROPERTYNAME_EAD_DAO_ACTUATE = "eadDaoActuate";
    public static final String PROPERTYNAME_EAD_DAO_SHOW = "eadDaoShow";
    public static final String PROPERTYNAME_METS_IDENTIFIER = "metsIdentifier";
    public static final String PROPERTYNAME_COMPONENT_ID = "componentId";
    public static final String PROPERTYNAME_DISPLAY_DATE = "date";
    public static final String PROPERTYNAME_DIGITAL_INSTANCE = "digitalInstance";

    private boolean debug = false;

    @IncludeInApplicationConfiguration
    @ExcludeFromDefaultValues
    private Long digitalObjectId;

    // this variable is only used for setting return screen order.
    // Its actually defined in the super class
    @IncludeInApplicationConfiguration(1)
    @DefaultIncludeInSearchEditor
    private String title = "";

    // this variable is only used for setting return screen order.
    // Its actually defined in the super class
    @IncludeInApplicationConfiguration
    @DefaultIncludeInSearchEditor
    private String dateExpression = "";

    @IncludeInApplicationConfiguration(2)
    @DefaultIncludeInSearchEditor
    private String objectType = "";

    @IncludeInApplicationConfiguration
    @StringLengthValidationRequried
    private String label = "";

    @IncludeInApplicationConfiguration
    @StringLengthValidationRequried
    private String languageCode = "";

    @IncludeInApplicationConfiguration
    @StringLengthValidationRequried
    private String eadDaoActuate = "";

    @IncludeInApplicationConfiguration
    @StringLengthValidationRequried
    private String eadDaoShow = "";

    @IncludeInApplicationConfiguration
    @DefaultIncludeInSearchEditor
    @ExcludeFromDefaultValues
    @StringLengthValidationRequried
    private String metsIdentifier = "";

    @IncludeInApplicationConfiguration
    @StringLengthValidationRequried
    private String componentId = "";

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Repositories repository;

    @IncludeInApplicationConfiguration
    @ExcludeFromDefaultValues
    private Set<FileVersions> fileVersions = new HashSet<FileVersions>();

    private Integer objectOrder = 0;
    private Set<DigitalObjects> digitalObjectChildren = new HashSet<DigitalObjects>();
    private DigitalObjects parent = null;
    private ArchDescriptionDigitalInstances digitalInstance;

    public DigitalObjects() {
        super();
    }

    public String getObjectLabel() {
        String returnLabel = "";
        if (this.getTitle() != null && this.getTitle().length() > 0) {
            returnLabel = this.getTitle();
        } else if (this.getDateExpression() != null && this.getDateExpression().length() > 0) {
            returnLabel = this.getDateExpression();
        } else {
            returnLabel = this.getLabel();
        }
        if (debug) {
            return getObjectOrder() + "- " + returnLabel + " (" + getIdentifier() + ")";
        } else {
            return returnLabel;
        }

    }

    public Long getIdentifier() {
        return getDigitalObjectId();
    }

    public void setIdentifier(Long identifier) {
        this.setDigitalObjectId(identifier);
    }

    public Long getDigitalObjectId() {
        return digitalObjectId;
    }

    public void setDigitalObjectId(Long digitalObjectId) {
        this.digitalObjectId = digitalObjectId;
    }

    public String getObjectType() {
        if (this.objectType != null) {
            return this.objectType;
        } else {
            return "";
        }
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getLabel() {
        if (this.label != null) {
            return this.label;
        } else {
            return "";
        }
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Set<FileVersions> getFileVersions() {
        return fileVersions;
    }

    public void setFileVersions(Set<FileVersions> fileVersions) {
        this.fileVersions = fileVersions;
    }

    public String toString() {
        return this.getObjectLabel();
    }

    public Integer getObjectOrder() {
        return objectOrder;
    }

    public void setObjectOrder(Integer objectOrder) {
        this.objectOrder = objectOrder;
    }

    public Set<DigitalObjects> getDigitalObjectChildren() {
        return digitalObjectChildren;
    }

    public void setDigitalObjectChildren(Set<DigitalObjects> digitalObjectChildren) {
        this.digitalObjectChildren = digitalObjectChildren;
    }

    public DigitalObjects getParent() {
        return parent;
    }

    public void setParent(DigitalObjects parent) {
        this.parent = parent;
    }

    public ArchDescriptionDigitalInstances getDigitalInstance() {
        return digitalInstance;
    }

    public void setDigitalInstance(ArchDescriptionDigitalInstances digitalInstance) {
        this.digitalInstance = digitalInstance;
    }

    public boolean isHasChild() {
        if (digitalObjectChildren.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void addChild(DigitalObjects child) {
        digitalObjectChildren.add(child);
    }

    public void removeChild(DigitalObjects child) {
        digitalObjectChildren.remove(child);
    }


    public void removeRelatedObject(DomainObject domainObject) throws ObjectNotRemovedException {
        if (domainObject instanceof FileVersions) {
            removeFileVersion((FileVersions) domainObject);
        } else {
            super.removeRelatedObject(domainObject);
        }
    }

    /**
     * A dummy place holder to be overridden by classes that have related objects
     *
     * @param domainObject the domain object to be removed
     */
    public Collection getRelatedCollection(DomainObject domainObject) {
        if (domainObject instanceof FileVersions) {
            return getFileVersions();
        } else {
            return super.getRelatedCollection(domainObject);
        }
    }

    public void addFileVersion(FileVersions fileVersion) {
        SequencedObject.adjustSequenceNumberForAdd(this.getFileVersions(), fileVersion);
        getFileVersions().add(fileVersion);
    }

    protected void removeFileVersion(FileVersions fileVersion) {
        getFileVersions().remove(fileVersion);
        SequencedObject.resequenceSequencedObjects(this.getFileVersions());
    }

    public FileVersions getFileVersion(int index) {
        ArrayList<FileVersions> fileVersions = new ArrayList<FileVersions>(getFileVersions());
        return fileVersions.get(index);
    }

    public String getLanguageCode() {
        if (this.languageCode != null) {
            return this.languageCode;
        } else {
            return "";
        }
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getEadDaoActuate() {
        if (this.eadDaoActuate != null) {
            return this.eadDaoActuate;
        } else {
            return "";
        }
    }

    public void setEadDaoActuate(String eadDaoActuate) {
        this.eadDaoActuate = eadDaoActuate;
    }

    public String getEadDaoShow() {
        if (this.eadDaoShow != null) {
            return this.eadDaoShow;
        } else {
            return "";
        }
    }

    public void setEadDaoShow(String eadDaoShow) {
        this.eadDaoShow = eadDaoShow;
    }

    public String getMetsIdentifier() {
        if (this.metsIdentifier != null) {
            return this.metsIdentifier;
        } else {
            return "";
        }
    }

    public void setMetsIdentifier(String metsIdentifier) {
        this.metsIdentifier = metsIdentifier;
    }

    // overide the compareto method to sort by objectOrder
    public int compareTo(Object object) {
        /*Due to the use of hibernate comparing by object leads to ART-1985
        so will no longer do this type of comparison

        if (this.getClass() != object.getClass()) {
            throw (new ClassCastException("Cannot compare unlike objects"));
        }*/

        int objectOrder = ((DigitalObjects) object).getObjectOrder();
        if (objectOrder < getObjectOrder()) {
            return 1;
        } else if (objectOrder == getObjectOrder() && this.equals(object)) {
            return 0;
        } else if (objectOrder > getObjectOrder()) {
            return -1;
        } else {
            return toString().compareToIgnoreCase(object.toString());
        }
    }

    /*
    * Following method are implemented to be complient with the
    * treeDnDSequencedObject interface
    */

    // Method to get the parent

    public treeDnDSequencedObject getParentObject() {
        return parent;
    }

    // Method to set the parent
    public void setParentObject(treeDnDSequencedObject parent) {
        this.parent = (DigitalObjects) parent;
    }

    // Method to add a child
    public void addChild(treeDnDSequencedObject child) {
        digitalObjectChildren.add((DigitalObjects) child);
    }

    // remove a child
    public void removeChild(treeDnDSequencedObject child) throws ObjectNotRemovedException {
        if (!digitalObjectChildren.remove(child)) {
            throw new ObjectNotRemovedException();
        }
    }

    // return the digital object children
    public Set<DigitalObjects> getChildren() {
        return digitalObjectChildren;
    }

    // Method to get the sequence number
    public Integer getSequenceNumber() {
        return objectOrder;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.objectOrder = sequenceNumber;
    }

    public Repositories getRepository() {
		return repository;
	}

	public void setRepository(Repositories repository) {
		this.repository = repository;

        // now set the repository for each of the children digital objects
        for(DigitalObjects childDigitalObject : getDigitalObjectChildren()) {
            childDigitalObject.setRepository(repository);
        }
	}

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    /**
     * Method to either display the date expression or the date begin and date end
     * 
     * @return String containing the date of this DO
     */
    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_DISPLAY_DATE, value=3)
	public String getDate() {
		if (this.getDateExpression() != null) {
			return getDateExpression();
		} else if(getDateBegin() != null){
            String date = "" + getDateBegin();
            if(getDateEnd() != null) {
                date += " - " + getDateEnd();
            }
            return date;
		} else {
            return "";
        }
	}
}
