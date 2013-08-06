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
 * Date: Jun 27, 2006
 * Time: 3:50:07 PM
 */

package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.StringLengthValidationRequried;
import org.archiviststoolkit.structure.IncludeInRDE;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.swing.jTreeDnD.treeDnDSequencedObject;
//import org.archiviststoolkit.util.ResourceComponentComparator;

import java.util.*;

public abstract class ResourcesCommon extends AccessionsResourcesCommon implements treeDnDSequencedObject {
	public static final String PROPERTYNAME_LEVEL = "level";
	public static final String PROPERTYNAME_OTHER_LEVEL = "otherLevel";
	public static final String PROPERTYNAME_LANGUAGE_CODE = "languageCode";
	public static final String PROPERTYNAME_REPOSITORY_PROCESSING_NOTE = "repositoryProcessingNote";
	public static final String PROPERTYNAME_INTERNAL_ONLY = "internalOnly";
	public static final String PROPERTYNAME_INSTANCES = "instances";

	public static final String LEVEL_VALUE_OTHERLEVEL = "otherlevel";

	private boolean debug = false;

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried(30)
	@IncludeInRDE
	private String level = "";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried(30)
	@IncludeInRDE
	private String otherLevel = "";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	@IncludeInRDE
	private String languageCode = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@IncludeInRDE
	private String repositoryProcessingNote = "";

	@IncludeInApplicationConfiguration
	@IncludeInRDE
    private Boolean internalOnly = false;

	@ExcludeFromDefaultValues
	private String eadIngestProblem = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Set<ArchDescriptionInstances> instances = new HashSet<ArchDescriptionInstances>();
//	private SortedSet<ResourcesComponents> resourcesComponents = new TreeSet<ResourcesComponents>(new ResourceComponentComparator());
	private SortedSet<ResourcesComponents> resourcesComponents = new TreeSet<ResourcesComponents>();

	public String getLevel() {
		if (this.level != null) {
			return this.level;
		} else {
			return "";
		}
	}

	public void setLevel(String level) {
		this.level = level;
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

	public String getRepositoryProcessingNote() {
		if (this.repositoryProcessingNote != null) {
			return this.repositoryProcessingNote;
		} else {
			return "";
		}
	}

	public void setRepositoryProcessingNote(String repositoryProcessingNote) {
		this.repositoryProcessingNote = repositoryProcessingNote;
	}

	public void addInstance(ArchDescriptionInstances resourceComponentInstance) {
		this.instances.add(resourceComponentInstance);
	}

	protected void removeInstance(ArchDescriptionInstances resourceComponentInstance) {
		if (resourceComponentInstance == null)
			throw new IllegalArgumentException("Can't remove a null instance.");

		// if this is a digital instance then need to unlink the digital object so it doesn't get deleted
        if (resourceComponentInstance instanceof ArchDescriptionDigitalInstances) {
            DigitalObjects digitalObject = ((ArchDescriptionDigitalInstances) resourceComponentInstance).getDigitalObject();
            if(digitalObject != null) {
                digitalObject.setDigitalInstance(null);
            }
        }

        this.instances.remove(resourceComponentInstance);
	}

	public ArchDescriptionInstances getInstance(int index) {
		ArrayList instances = new ArrayList<ArchDescriptionInstances>(getInstances());
		return (ArchDescriptionInstances) instances.get(index);
	}

	public Set<ArchDescriptionInstances> getInstances() {
		return instances;
	}

    /**
     * Convinient method to retrive digital objects attached to this collection
     *
     * @return Set containing any digital objects
     */
    public Set<DigitalObjects> getDigitalObjects() {
        Set set = new HashSet();
		for (ArchDescriptionInstances instance : this.getInstances()) {
			if(instance instanceof ArchDescriptionDigitalInstances) {
                set.add(((ArchDescriptionDigitalInstances)instance).getDigitalObject());
            }
		}
		return set;
    }

	public Set getInstances(Class clazz) {
		Set set = new HashSet();

        // put this code in a try block as a work around for
        // This JIRA issue (https://jira.nyu.edu/jira/browse/ART-2359)
        try {
		    for (ArchDescriptionInstances instance : this.getInstances()) {
			    if (clazz.getName().equals(instance.getClass().getName()))
				    set.add(instance);
		    }
        } catch(NullPointerException ne) {
            ne.printStackTrace();
        }

        return set;
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

	public String getEadIngestProblem() {
		if (this.eadIngestProblem != null) {
			return this.eadIngestProblem;
		} else {
			return "";
		}
	}

	public void setEadIngestProblem(String eadIngestProblem) {
		this.eadIngestProblem = eadIngestProblem;
	}

	public void removeRelatedObject(DomainObject domainObject) throws ObjectNotRemovedException {
		if (domainObject instanceof ArchDescriptionInstances) {
			removeInstance((ArchDescriptionInstances) domainObject);
		} else {
			super.removeRelatedObject(domainObject);
		}
	}

	public Collection getRelatedCollection(DomainObject domainObject) {
		if (domainObject instanceof ArchDescriptionInstances) {
			return getInstances();
		} else {
			return super.getRelatedCollection(domainObject);
		}
	}

	/**
	 * A dummy place holder to be overridden by classes that have related objects
	 *
	 * @param rowNumber the row of the object to retrieve
	 */
	@Override
	public DomainObject getRelatedObject(DomainObject domainObject, int rowNumber) {
		if (domainObject instanceof ArchDescriptionInstances) {
			return getInstance(rowNumber);
		} else {
			return super.getRelatedObject(domainObject, rowNumber);
		}
	}

	public void incrementOrder(Double start) {
		for (ResourcesComponents component : getResourcesComponents()) {
			if (component.getSequenceNumber() >= start) {
				component.setSequenceNumber(component.getSequenceNumber() + 1);
			}
		}
	}

	public void removeComponent(ResourcesComponents component) throws ObjectNotRemovedException {
		removeComponent(component, true);
	}

	public void removeComponent(ResourcesComponents component, boolean resequence) throws ObjectNotRemovedException {
		removeResourcesComponents(component);
		if (resequence) {
			resequenceSequencedObjects();
		}
	}

	public void addComponent(ResourcesComponents component) {
		addComponent(component, true);
	}

	public void addComponent(ResourcesComponents component, Boolean adjustSequenceNumbers) {
		if (adjustSequenceNumbers) {
			adjustSequenceNumberForAdd(component);
		}
		addResourcesComponents(component);
	}
//	public abstract void incrementOrder(Double start);
//
//	public abstract void removeComponent(ResourcesComponents component);
//
//	public abstract void addComponent(ResourcesComponents component);

	public SortedSet<ResourcesComponents> getResourcesComponents() {
		return this.resourcesComponents;
	}

	public void setResourcesComponents(SortedSet resourcesComponents) {
		this.resourcesComponents = resourcesComponents;
	}

	public void addResourcesComponents(ResourcesComponents resourceComponent) {
		if (resourceComponent == null)
			throw new IllegalArgumentException("Can't add a null component.");
		if (debug) {
			System.out.println("Before add component");
		}
		boolean success = resourcesComponents.add(resourceComponent);
		if (debug) {
			System.out.println("After add component. Result: " + success);
		}
		if (this instanceof ResourcesComponents) {
			((ResourcesComponents) this).setHasChild(true);
		}
	}

	public void removeResourcesComponents(ResourcesComponents resourceComponent) throws ObjectNotRemovedException {
		if (resourceComponent == null)
			throw new IllegalArgumentException("Can't remove a null component.");
		if (debug) {
			System.out.println("Before remove component");
		}
		if (!resourcesComponents.remove(resourceComponent)) {
			if (debug) {
				System.out.println("remove component failed.");
			}
			throw new ObjectNotRemovedException();
		}
		if (debug) {
			System.out.println("After remove component.");
		}
		if (this instanceof ResourcesComponents && resourcesComponents.size() == 0) {
			((ResourcesComponents) this).setHasChild(false);
		}
	}

	public ResourcesComponents getResourcesComponent(int index) {
		ArrayList components = new ArrayList(getResourcesComponents());
		return (ResourcesComponents) components.get(index);
	}

	public void adjustSequenceNumberForAdd(ResourcesComponents componentToAdd) {
		SortedSet<ResourcesComponents> temp = resourcesComponents;
		resourcesComponents = new TreeSet<ResourcesComponents>();
		for (ResourcesComponents component : temp) {
			if (component.getSequenceNumber() >= componentToAdd.getSequenceNumber()) {
				component.incrementSequenceNumber();
			}
			boolean success = resourcesComponents.add(component);
			if (debug) {
				System.out.println("Adjust sequence add component. Result: " + success);
			}
		}
	}

	public int getNextSequenceNumber() {
		int returnSequenceNumber = -1;
		for(ResourcesComponents component: resourcesComponents) {
			if (component.getSequenceNumber() > returnSequenceNumber) {
				returnSequenceNumber = component.getSequenceNumber();
			}
		}
		return returnSequenceNumber + 1;		
	}

	public void resortComponents() {
		ArrayList<ResourcesComponents> temp = new ArrayList<ResourcesComponents>(resourcesComponents);
		resourcesComponents = new TreeSet<ResourcesComponents>();
		resourcesComponents.addAll(temp);
	}

	public int getNextChildSequenceNumber() {
		int sequenceNumber = 0;
		for (ResourcesComponents component: getResourcesComponents()) {
			if (component.getSequenceNumber() > sequenceNumber) {
				sequenceNumber = component.getSequenceNumber();
			}
		}
		return sequenceNumber;
	}

	public void resequenceSequencedObjects() {
		int sequenceNumber = 0;
		ArrayList<ResourcesComponents> temp = new ArrayList<ResourcesComponents>(resourcesComponents);
		Collections.sort(temp);
		for (ResourcesComponents component : temp) {
			component.setSequenceNumber(sequenceNumber++);
		}
	}

	public String getOtherLevel() {
		if (this.otherLevel != null) {
			return this.otherLevel;
		} else {
			return "";
		}
	}

	public void setOtherLevel(String otherLevel) {
		this.otherLevel = otherLevel;
	}

  /**
   * 8/9/2008
   * Methods below are for going to be used to allow for this object to be used directly in a DnD JTree
   * These methods make some of the above methods reduntant, but not wanted to clean up the code will remian that
   * way for now
   */
  public Integer getSequenceNumber() {
    int sequenceNumber = 0;
    if (this instanceof ResourcesComponents) {
	    sequenceNumber = ((ResourcesComponents)this).getSequenceNumber();
		}
    return sequenceNumber;
  }

  public void setSequenceNumber(Integer sequenceNumber) {
    if (this instanceof ResourcesComponents) {
		  ((ResourcesComponents)this).setSequenceNumber(sequenceNumber);
		}
  }

  public void setParent(ResourcesCommon parent) {
    if (this instanceof ResourcesComponents) {
      if(parent instanceof Resources) {
        ((ResourcesComponents)this).setResource((Resources)parent);
        ((ResourcesComponents)this).setResourceComponentParent(null);
      }
      else { // assume it is an instance of resourceComponents
        ((ResourcesComponents)this).setResourceComponentParent((ResourcesComponents)parent);
        ((ResourcesComponents)this).setResource(null);
      }
		}
  }

  // TODO this method may not be needed so might consider removing
  public ResourcesCommon getParent() {
    ResourcesCommon parent = null;
    if (this instanceof ResourcesComponents) {
		  parent = ((ResourcesComponents)this).getResourceComponentParent();
		}
    return parent;
  }

  public void addChild(ResourcesCommon child) {
    addResourcesComponents((ResourcesComponents)child);
  }

  public void removeChild(ResourcesCommon child) throws ObjectNotRemovedException {
    removeResourcesComponents((ResourcesComponents)child);
  }

  public SortedSet getChildren() {
    return resourcesComponents;
  }

  // get the name
  public String toString() {
    String s;
    if (this instanceof ResourcesComponents) {
		  s = ((ResourcesComponents)this).toString();
		}
    else {
      s = ((Resources)this).toString();
    }

    return s;
  }

  /**
	 * Compares this object to another.
   *
   * @param object the object to compare this to.
   * @return a integer result of the comparison.
   */
  public int compareTo(Object object) {
    if (object instanceof ResourcesComponents) {
      int result = this.getSequenceNumber().compareTo(((ResourcesComponents) object).getSequenceNumber());
      if (debug) {
        System.out.println("Compare ResourcesComponents: " + this + " To: " + object + " Result: " + result);
      }
      if (result == 0 && !this.equals(object)) {
        //this is necesary so that equals and compareTo are consistent
        return 1;
      } else {
        return result;
      }
    } else {
      return super.compareTo(object);
    }
  }

  /*
   * Following method are implemented to be complient with the
   * treeDnDSequencedObject interface
   */

  // Method to get the parent
  public treeDnDSequencedObject getParentObject() {
    return this.getParent();
  }

  // Method to set the parent
  public void setParentObject(treeDnDSequencedObject parent) {
    this.setParent((ResourcesCommon)parent);
  }

  // Method to add a child
  public void addChild(treeDnDSequencedObject child) {
    this.addChild((ResourcesCommon) child);
  }

  // remove a child
  public void removeChild(treeDnDSequencedObject child) throws ObjectNotRemovedException {
    this.removeChild((ResourcesCommon)child);
  }
}
