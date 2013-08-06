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
 * Date: Apr 20, 2006
 * Time: 9:38:13 AM
 */
package org.archiviststoolkit.model;

import java.util.ArrayList;
import java.text.NumberFormat;

public class ContainerGroup implements Comparable{

    //todo this is not a model and should be moved to another package

    private String topLevelContainerName;
	private ArrayList<ArchDescriptionAnalogInstances> instances;
	private String sortString;

    /*
    The fields for storing information about what resource component the box belongs
    to is intended to allow viewing instances by which box they belong to. Kinda like
    what songs are in a cd.
     */
    private String resourceIdentifier = null; // Identifies the parent resource record
    private String componentIdentifier = null; // Identifies the resource components
    private String seriesComponentIdentifier = null; // identifies the series component that container group belongs to

	public ContainerGroup(String topLevelContainerName, Double numericIndicator, String alphaIndicator) {
		this.setTopLevelContainerName(topLevelContainerName);
		instances = new ArrayList<ArchDescriptionAnalogInstances>();
		if (numericIndicator != null && numericIndicator != 0) {
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setMinimumFractionDigits(5);
			nf.setMinimumIntegerDigits(5);
			sortString = nf.format(numericIndicator);
		} else if (alphaIndicator == null) {
			sortString = "";
		} else {
			sortString = alphaIndicator;
		}
	}

	public String getTopLevelContainerName() {
		if (this.topLevelContainerName != null) {
			return this.topLevelContainerName;
		} else {
			return "";
		}
	}

	public void setTopLevelContainerName(String topLevelContainerName) {
		this.topLevelContainerName = topLevelContainerName;
	}

	public ArrayList<ArchDescriptionAnalogInstances> getInstances() {
		return instances;
	}

	public void setInstances(ArrayList<ArchDescriptionAnalogInstances> instances) {
		this.instances = instances;
	}

	public void addInstance(ArchDescriptionAnalogInstances instance) {
		instances.add(instance);
	}

	public void setLocations(Locations location) {
//        try {
//            ArchDescriptionAnalogInstancesDAO access = new ArchDescriptionAnalogInstancesDAO();

			for (ArchDescriptionAnalogInstances instance : instances) {
				instance.setLocation(location);
//				access.addLocationToInstance(location, instance);
			}
//        } catch (PersistenceException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
	}

	public String toString() {
		String locationString = " - no location assigned";
		ArchDescriptionAnalogInstances firstInstance = instances.get(0);
		if (firstInstance != null) {
			Locations location = firstInstance.getLocation();
			if (location != null) {
				locationString = " - " + location.toString();
			}
		}
		return topLevelContainerName + locationString;
	}

	public int compareTo(Object o) {
		ContainerGroup containerGroup = (ContainerGroup)o;
		return this.sortString.compareTo(containerGroup.getSortString());
	}

	public String getSortString() {
		if (this.sortString != null) {
			return this.sortString;
		} else {
			return "";
		}
	}

	public void setSortString(String sortString) {
		this.sortString = sortString;
	}

    /**
     * Return the resource component identifier the container group belongs to
     *
     * @return The components identifier
     */
    public String getComponentIdentifier() {
        return componentIdentifier;
    }

    /**
     * Set the resource component identifier
     *
     * @param componentIdentifier
     */
    public void setComponentIdentifier(String componentIdentifier) {
        this.componentIdentifier = componentIdentifier;
    }

    /**
     * Returns the resource component identifier
     *
     * @return The resource component identifiers
     */
    public String getResourceIdentifier() {
        return resourceIdentifier;
    }

    /**
     * Method to set the resource component identifier
     *
     * @param resourceIdentifier
     */
    public void setResourceIdentifier(String resourceIdentifier) {
        this.resourceIdentifier = resourceIdentifier;
    }

    /**
     * return the identify of the series component this container group belongs to
     *
     * @return Identifier of the component
     */
    public String getSeriesComponentIdentifier() {
        return seriesComponentIdentifier;
    }

    /**
     * Set the identifier of the series component 
     *
     * @param seriesComponentIdentifier
     */
    public void setSeriesComponentIdentifier(String seriesComponentIdentifier) {
        this.seriesComponentIdentifier = seriesComponentIdentifier;
    }
}
