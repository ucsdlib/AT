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
 * Date: Oct 10, 2006
 * Time: 11:33:20 AM
 */

package org.archiviststoolkit.report;

import org.archiviststoolkit.model.ArchDescriptionAnalogInstances;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.util.StringHelper;

public class ShelfListItems implements Comparable{

	private Resources resource;
	private String instanceLabel;
	private String sortString;
    private String fullLabel;  // contains the barcode
    private ArchDescriptionAnalogInstances instance; // provides access to the instance record in jasper reports

	public ShelfListItems(ArchDescriptionAnalogInstances instance, Resources resource) {
		this.instance = instance;
        this.instanceLabel = instance.getTopLevelLabel();
		this.resource = resource;

        if(StringHelper.isNotEmpty(instance.getBarcode())) {
            fullLabel = instanceLabel + " (" + instance.getBarcode() + ")";
        } else {
            fullLabel = instanceLabel;
        }

        // 3/25/2013 add the instance label to sort string to fix bug printing reports
		sortString = resource.toString() + instance.getTopLevelLabelForSorting() + " " + instanceLabel;
	}

	public Resources getResource() {
		return resource;
	}

	public void setResource(Resources resource) {
		this.resource = resource;
	}

	public String getInstanceLabel() {
		return instanceLabel;
	}

	public void setInstanceLabel(String instanceLabel) {
		this.instanceLabel = instanceLabel;
	}

    public String getFullLabel() {
        return fullLabel;
    }

    public void setFullLabel(String fullLabel) {
        this.fullLabel = fullLabel;
    }

    public ArchDescriptionAnalogInstances getInstance() {
        return instance;
    }

    public void setInstance(ArchDescriptionAnalogInstances instance) {
        this.instance = instance;
    }

    @Override
	public boolean equals(final Object object) {

		if (object == null) {
			return (false);
		}

		if (this == object) {
			return true;
		}

		if (this.getClass() == object.getClass()) {
			ShelfListItems compareObject = (ShelfListItems)object;
			if (this.getResource().equals(compareObject.getResource()) &&
					this.getInstanceLabel().equalsIgnoreCase(compareObject.getInstanceLabel())) {
				return true;
			}
		}

		return false;
	}

	public int compareTo(Object o) {
		if (this.getClass() != o.getClass()) {
			throw (new ClassCastException("Cannot compare unlike objects"));
		}
		ShelfListItems compareObject = (ShelfListItems)o;
		return sortString.compareToIgnoreCase(compareObject.sortString);
	}
}
