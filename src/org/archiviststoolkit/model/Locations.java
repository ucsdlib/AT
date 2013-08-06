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
 * Date: Apr 18, 2006
 * Time: 1:32:26 PM
 */

package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.IncludeMethodInApplicationConfiguration;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.StringLengthValidationRequried;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.exceptions.DeleteException;
import org.archiviststoolkit.report.ExtentStats;
import org.archiviststoolkit.report.ShelfListItems;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.util.StringHelper;
import org.hibernate.TransientObjectException;

import java.util.*;
import java.text.NumberFormat;
import java.text.ParseException;

import com.jgoodies.validation.formatter.EmptyNumberFormatter;

public class Locations extends DomainObject {

	public static final String PROPERTYNAME_BUILDING = "building";
	public static final String PROPERTYNAME_FLOOR = "floor";
	public static final String PROPERTYNAME_ROOM = "room";
	public static final String PROPERTYNAME_AREA = "area";
	public static final String PROPERTYNAME_COORDINATE_1_LABEL = "coordinate1Label";
	public static final String PROPERTYNAME_COORDINATE_1_NUMERIC_INDICATOR = "coordinate1NumericIndicator";
	public static final String PROPERTYNAME_COORDINATE_1_ALPHA_NUMERIC_INDICATOR = "coordinate1AlphaNumIndicator";
	public static final String PROPERTYNAME_COORDINATE_2_LABEL = "coordinate2Label";
	public static final String PROPERTYNAME_COORDINATE_2_NUMERIC_INDICATOR = "coordinate2NumericIndicator";
	public static final String PROPERTYNAME_COORDINATE_2_ALPHA_NUMERIC_INDICATOR = "coordinate2AlphaNumIndicator";
	public static final String PROPERTYNAME_COORDINATE_3_LABEL = "coordinate3Label";
	public static final String PROPERTYNAME_COORDINATE_3_NUMERIC_INDICATOR = "coordinate3NumericIndicator";
	public static final String PROPERTYNAME_COORDINATE_3_ALPHA_NUMERIC_INDICATOR = "coordinate3AlphaNumIndicator";
	public static final String PROPERTYNAME_CLASSIFICATION_NUMBER = "classificationNumber";
	public static final String PROPERTYNAME_BARCODE = "barcode";
	public static final String PROPERTYNAME_REPOSITORY = "repository";
	public static final String PROPERTYNAME_COORDINATES = "coordinates";
	public static final String PROPERTYNAME_COORDINATE1 = "coordinate1";
	public static final String PROPERTYNAME_COORDINATE2 = "coordinate2";
	public static final String PROPERTYNAME_COORDINATE3 = "coordinate3";
	public static final String PROPERTYNAME_REPOSITORY_NAME = "repositoryName";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long locationId;

	@IncludeInApplicationConfiguration(1)
	@StringLengthValidationRequried(50)
	private String building = "";

	@IncludeInApplicationConfiguration(2)
	@StringLengthValidationRequried(50)
	private String floor = "";

	@IncludeInApplicationConfiguration(3)
	@StringLengthValidationRequried(50)
	private String room = "";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried(50)
	private String area = "";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried(50)
	private String coordinate1Label = "";

	@IncludeInApplicationConfiguration
	private Double coordinate1NumericIndicator = 0d;

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried(20)
	private String coordinate1AlphaNumIndicator = "";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried(50)
	private String coordinate2Label = "";

	@IncludeInApplicationConfiguration
	private Double coordinate2NumericIndicator = 0d;

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried(20)
	private String coordinate2AlphaNumIndicator = "";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried(50)
	private String coordinate3Label = "";

	@IncludeInApplicationConfiguration
	private Double coordinate3NumericIndicator = 0d;

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried (20)
	private String coordinate3AlphaNumIndicator = "";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried(50)
	private String classificationNumber = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(50)
	private String barcode = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Repositories repository;

	private Set<ArchDescriptionAnalogInstances> instances =
			new HashSet<ArchDescriptionAnalogInstances>();

	private Set<AccessionsLocations> accessions =
			new HashSet<AccessionsLocations>();

	public Locations() {
		if (ApplicationFrame.getInstance().getCurrentUser() != null) {
			repository = ApplicationFrame.getInstance().getCurrentUserRepository();
		}
	}

//	private Set<ArchDescriptionSubjects> archDescriptionSubjects = new HashSet<ArchDescriptionSubjects>();
	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public String getBuilding() {
		if (this.building != null) {
			return this.building;
		} else {
			return "";
		}
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getFloor() {
		if (this.floor != null) {
			return this.floor;
		} else {
			return "";
		}
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getBarcode() {
		if (this.barcode != null) {
			return this.barcode;
		} else {
			return "";
		}
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	/**
	 * @return Returns the identifier.
	 */
	public Long getIdentifier() {
		return this.getLocationId();
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(Long identifier) {
		this.setLocationId(identifier);
	}

	//A place holder to determine if an object is safe to delete. To be
//overridden if logic is necessary.
	public void testDeleteRules() throws DeleteException, PersistenceException {
		DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(ArchDescriptionAnalogInstances.class);
		int linkedInstanceCount = access.getCountBasedOnPropertyValue(ArchDescriptionAnalogInstances.PROPERTYNAME_LOCATION, this);

		access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(AccessionsLocations.class);
		int linkedAccessionCount = access.getCountBasedOnPropertyValue(AccessionsLocations.PROPERTYNAME_LOCATION, this);

		if (linkedInstanceCount > 0 || linkedAccessionCount > 0) {
			String errorString = "";
			if (linkedInstanceCount > 0) {
			   errorString = linkedInstanceCount + " instances";
			}
			if (linkedAccessionCount > 0) {
				errorString = StringHelper.concat(" and ", errorString, linkedAccessionCount + " accessions");
			}
			throw new DeleteException("There are " + errorString + " linked to this location");
		}
	}

	public String getRoom() {
		if (this.room != null) {
			return this.room;
		} else {
			return "";
		}
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getArea() {
		if (this.area != null) {
			return this.area;
		} else {
			return "";
		}
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCoordinate1Label() {
		if (this.coordinate1Label != null) {
			return this.coordinate1Label;
		} else {
			return "";
		}
	}

	public void setCoordinate1Label(String coordinate1Label) {
		this.coordinate1Label = coordinate1Label;
	}

	public String getCoordinate2Label() {
		if (this.coordinate2Label != null) {
			return this.coordinate2Label;
		} else {
			return "";
		}
	}

	public void setCoordinate2Label(String coordinate2Label) {
		this.coordinate2Label = coordinate2Label;
	}

	public String getCoordinate3Label() {
		if (this.coordinate3Label != null) {
			return this.coordinate3Label;
		} else {
			return "";
		}
	}

	public void setCoordinate3Label(String coordinate3Label) {
		this.coordinate3Label = coordinate3Label;
	}

	public String getClassificationNumber() {
		if (this.classificationNumber != null) {
			return this.classificationNumber;
		} else {
			return "";
		}
	}

	public void setClassificationNumber(String classificationNumber) {
		this.classificationNumber = classificationNumber;
	}

	@IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_COORDINATES, value = 4)
  @ExcludeFromDefaultValues
  public LocationsCoordinatesDisplay getCoordinates() {
//		return StringHelper.concat(" / ", getCoordinate1(), getCoordinate2(), getCoordinate3());
		if (coordinateDisplay == null) {
			setCoordinatesDisplay();
		}
		return coordinateDisplay;
	}

	private void setCoordinatesDisplay() {
		if (coordinateDisplay == null) {
			coordinateDisplay = new LocationsCoordinatesDisplay(StringHelper.concat(" / ", getCoordinate1(), getCoordinate2(), getCoordinate3()),
					getCoordinateSortString());
		} else {
			coordinateDisplay.setDisplayString(StringHelper.concat(" / ", getCoordinate1(), getCoordinate2(), getCoordinate3()));
			coordinateDisplay.setSortString(getCoordinateSortString());
		}
	}

	@IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_COORDINATE1)
  @ExcludeFromDefaultValues
  public String getCoordinate1() {
		if (getCoordinate1AlphaNumIndicator() != null && getCoordinate1AlphaNumIndicator().length() > 0) {
			return StringHelper.concat(" ", getCoordinate1Label(), getCoordinate1AlphaNumIndicator());
		} else if (getCoordinate1NumericIndicator() != null && getCoordinate1NumericIndicator() != 0) {
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setGroupingUsed(false);
			EmptyNumberFormatter format = new EmptyNumberFormatter(numberFormat);
			try {
				return getCoordinate1Label() + " " + format.valueToString(getCoordinate1NumericIndicator());
			} catch (ParseException e) {
				return getCoordinate1Label();
			}
		} else {
			return getCoordinate1Label();
		}
	}

	@IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_COORDINATE2)
  @ExcludeFromDefaultValues
  public String getCoordinate2() {
		if (getCoordinate2AlphaNumIndicator() != null && getCoordinate2AlphaNumIndicator().length() > 0) {
			return StringHelper.concat(" ", getCoordinate2Label(), getCoordinate2AlphaNumIndicator());
		} else if (getCoordinate2NumericIndicator() != null && getCoordinate2NumericIndicator() != 0) {
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setGroupingUsed(false);
			EmptyNumberFormatter format = new EmptyNumberFormatter(numberFormat);
			try {
				return getCoordinate2Label() + " " + format.valueToString(getCoordinate2NumericIndicator());
			} catch (ParseException e) {
				return getCoordinate2Label();
			}
		} else {
			return getCoordinate2Label();
		}
	}

	@IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_COORDINATE3)
  @ExcludeFromDefaultValues
  public String getCoordinate3() {
		if (getCoordinate3AlphaNumIndicator() != null && getCoordinate3AlphaNumIndicator().length() > 0) {
			return StringHelper.concat(" ", getCoordinate3Label(), getCoordinate3AlphaNumIndicator());
		} else if (getCoordinate3NumericIndicator() != null && getCoordinate3NumericIndicator() != 0) {
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setGroupingUsed(false);
			EmptyNumberFormatter format = new EmptyNumberFormatter(numberFormat);
			try {
				return getCoordinate3Label() + " " + format.valueToString(getCoordinate3NumericIndicator());
			} catch (ParseException e) {
				return getCoordinate3Label();
			}
		} else {
			return getCoordinate3Label();
		}
	}

	public String toString() {
		String locationString = StringHelper.concat(" - ", this.building, this.floor, this.room, this.area) + "  " +
				this.getCoordinates() + "  " + this.barcode + "  " + this.classificationNumber;

        return locationString.trim();
	}

	/**
	 * Compares this object to another.
	 *
	 * @param object the object to compare this to.
	 * @return a integer result of the comparison.
	 */
	public int compareTo(Object object) {
		if (object instanceof Locations) {
			Locations compareInstance = (Locations)object;
			return this.getSortString().compareTo(compareInstance.getSortString());
		} else {
			return super.compareTo(object);
		}
	}
	public String getSortString() {

		return this.building + this.floor + this.room + this.area + getCoordinateSortString();
	}

	public String getCoordinateSortString() {

		String coordinate1String = "";
		String coordinate2String = "";
		String coordinate3String = "";
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setGroupingUsed(false);
		numberFormat.setMinimumIntegerDigits(5);
		numberFormat.setMaximumIntegerDigits(5);
		EmptyNumberFormatter format = new EmptyNumberFormatter(numberFormat);

		if (getCoordinate1AlphaNumIndicator() != null && getCoordinate1AlphaNumIndicator().length() > 0) {
			coordinate1String =  getCoordinate1Label() + getCoordinate1AlphaNumIndicator();
		} else if (getCoordinate1NumericIndicator() != null && getCoordinate1NumericIndicator() != 0) {
			try {
				coordinate1String = getCoordinate1Label() + format.valueToString(getCoordinate1NumericIndicator());
			} catch (ParseException e) {
				coordinate1String = getCoordinate1Label();
			}
		} else {
			coordinate1String = getCoordinate1Label();
		}

		if (getCoordinate2AlphaNumIndicator() != null && getCoordinate2AlphaNumIndicator().length() > 0) {
			coordinate2String =  getCoordinate1Label() + getCoordinate2AlphaNumIndicator();
		} else if (getCoordinate1NumericIndicator() != null && getCoordinate1NumericIndicator() != 0) {
			try {
				coordinate2String = getCoordinate2Label() + format.valueToString(getCoordinate2NumericIndicator());
			} catch (ParseException e) {
				coordinate2String = getCoordinate2Label();
			}
		} else {
			coordinate2String = getCoordinate2Label();
		}

		if (getCoordinate3AlphaNumIndicator() != null && getCoordinate3AlphaNumIndicator().length() > 0) {
			coordinate3String =  getCoordinate3Label() + getCoordinate3AlphaNumIndicator();
		} else if (getCoordinate3NumericIndicator() != null && getCoordinate3NumericIndicator() != 0) {
			try {
				coordinate3String = getCoordinate3Label() + format.valueToString(getCoordinate3NumericIndicator());
			} catch (ParseException e) {
				coordinate3String = getCoordinate3Label();
			}
		} else {
			coordinate3String = getCoordinate3Label();
		}

		return coordinate1String + coordinate2String + coordinate3String;
	}
	
	public Set<ArchDescriptionAnalogInstances> getInstances() {
		return instances;
	}

	public void setInstances(Set<ArchDescriptionAnalogInstances> instances) {
		this.instances = instances;
	}

	public Repositories getRepository() {
		return repository;
	}

	public void setRepository(Repositories repository) {
		this.repository = repository;
	}

    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_REPOSITORY_NAME, value = 0)
    @ExcludeFromDefaultValues
    public String getRepositoryName() {
        return repository.getShortName();
    }

	public Set<AccessionsLocations> getAccessions() {
		return accessions;
	}

	public void setAccessions(Set<AccessionsLocations> accessions) {
		this.accessions = accessions;
	}

	public ArrayList<ShelfListItems> getContainerList() {
		TreeSet<ShelfListItems> shelfList = new TreeSet<ShelfListItems>();
		ResourcesDAO access = new ResourcesDAO();
		Resources resource;
		ShelfListItems shelfListItem;
		for(ArchDescriptionAnalogInstances instance: getInstances()) {
			if (instance.getResource() != null) {
				shelfListItem = new ShelfListItems(instance, instance.getResource());
				if (!doesShelfListContainItem(shelfList, shelfListItem)) {
					shelfList.add(shelfListItem);
				}
			} else if (instance.getResourceComponent() != null) {
				resource = access.findResourceByComponent(instance.getResourceComponent());
				shelfListItem = new ShelfListItems(instance, resource);
				if (!doesShelfListContainItem(shelfList, shelfListItem)) {
					shelfList.add(shelfListItem);
				}
			}
		}
		return new ArrayList<ShelfListItems>(shelfList);
	}

	private boolean doesShelfListContainItem(TreeSet<ShelfListItems> shelfList, ShelfListItems shelfListItem) {
		for (ShelfListItems item: shelfList) {
			if (item.equals(shelfListItem)) {
				return true;
			}
		}
		return false;
	}

	private TreeMap<Resources, ResourceContainers> extentStatistics = new TreeMap<Resources, ResourceContainers>();
	private LocationsCoordinatesDisplay coordinateDisplay = null;

	public ArrayList<ResourceContainers> getLocationList() {
		TreeMap<Locations, ResourceContainers> locations =
				new TreeMap<Locations, ResourceContainers>();

		Resources resource = null;
		ResourceContainers containerList;
//		ResourcesDAO resourceDAO = new ResourcesDAO();
		String extentType;
		Double extent;

//		List<String> additionalLines = Collections.synchronizedList(new ArrayList<String>());
//		if (progressPanel != null) {
//			additionalLines.add("Resource: " + this.getTitle());
//			progressPanel.setAdditionalLines(additionalLines, 1);
//		}

		try {
			for (ArchDescriptionAnalogInstances instance : getInstances()) {
				resource = instance.getResource();
					extentType = resource.getExtentType();
					extent = resource.getExtentNumber();
				if (resource != null) {
					containerList = extentStatistics.get(resource);
					if (containerList == null) {
						containerList = new ResourceContainers(resource, instance.getTopLevelLabel(), extentType, extent);
						extentStatistics.put(resource, containerList);
					} else {
						containerList.addContainer(instance.getTopLevelLabel(), extentType, extent);
					}
				}
			}

//			for (ResourcesComponents component : resource.getResourcesComponents()) {
//				gatherLocations(component, locations, additionalLines, 2);
//			}

		} catch (TransientObjectException e) {
			new ErrorDialog("Resource must be saved", e).showDialog();
		}

		return new ArrayList<ResourceContainers>(locations.values());
	}

	public Double getCoordinate1NumericIndicator() {
		return coordinate1NumericIndicator;
	}

	public void setCoordinate1NumericIndicator(Double coordinate1NumericIndicator) {
		this.coordinate1NumericIndicator = coordinate1NumericIndicator;
	}

	public String getCoordinate1AlphaNumIndicator() {
		if (this.coordinate1AlphaNumIndicator != null) {
			return this.coordinate1AlphaNumIndicator;
		} else {
			return "";
		}
	}

	public void setCoordinate1AlphaNumIndicator(String container1AlphaNumIndicator) {
		this.coordinate1AlphaNumIndicator = container1AlphaNumIndicator;
	}

	public Double getCoordinate2NumericIndicator() {
		return coordinate2NumericIndicator;
	}

	public void setCoordinate2NumericIndicator(Double coordinate2NumericIndicator) {
		this.coordinate2NumericIndicator = coordinate2NumericIndicator;
	}

	public String getCoordinate2AlphaNumIndicator() {
		if (this.coordinate2AlphaNumIndicator != null) {
			return this.coordinate2AlphaNumIndicator;
		} else {
			return "";
		}
	}

	public void setCoordinate2AlphaNumIndicator(String container2AlphaNumIndicator) {
		this.coordinate2AlphaNumIndicator = container2AlphaNumIndicator;
	}

	public Double getCoordinate3NumericIndicator() {
		return coordinate3NumericIndicator;
	}

	public void setCoordinate3NumericIndicator(Double coordinate3NumericIndicator) {
		this.coordinate3NumericIndicator = coordinate3NumericIndicator;
	}

	public String getCoordinate3AlphaNumIndicator() {
		if (this.coordinate3AlphaNumIndicator != null) {
			return this.coordinate3AlphaNumIndicator;
		} else {
			return "";
		}
	}

	public void setCoordinate3AlphaNumIndicator(String container3AlphaNumIndicator) {
		this.coordinate3AlphaNumIndicator = container3AlphaNumIndicator;
	}

	public class ResourceContainers {

		private Resources resource;
		private String containerList;
		private TreeMap<String, ExtentStats> extentList;

		public ResourceContainers(Resources resource, String container, String extentType, Double extent) {
			this.resource = resource;
			this.containerList = container;
			extentList = new TreeMap<String, ExtentStats>();
			ExtentStats extentStat = new ExtentStats(extentType);
			extentStat.setResources(extent);
			extentList.put(extentType, extentStat);
		}

		public Resources getLocation() {
			return resource;
		}

		public void setLocation(Resources location) {
			this.resource = location;
		}

		public String getContainerList() {
			return containerList;
		}

		public void setContainerList(String containerList) {
			this.containerList = containerList;
		}

		private void addContainer(String containerToAdd, String extentType, Double extent) {
			if (!containerList.contains(containerToAdd)) {
				 containerList += ", " + containerToAdd;
			 }
			ExtentStats extentStat = extentList.get(extentType);
			if (extentStat == null) {
				extentStat = new ExtentStats(extentType);
			}
			extentStat.addResourceExtent(extent);
		 }
	}
}
