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
 * Date: Jun 21, 2006
 * Time: 11:05:37 AM
 */

package org.archiviststoolkit.mydomain;

import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.model.ArchDescriptionRepeatingData;
import org.archiviststoolkit.model.NameContactNotes;

import java.util.Set;

public abstract class SequencedObject extends DomainObject {

	public static final String PROPERTYNAME_SEQUENCE_NUMBER = "sequenceNumber";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Integer sequenceNumber = 0;


	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		Object oldValue = getSequenceNumber();
		this.sequenceNumber = sequenceNumber;
		firePropertyChange(PROPERTYNAME_SEQUENCE_NUMBER, oldValue, sequenceNumber);
	}

	public void incrementSequenceNumber() {
		incrementSequenceNumber(1);
	}

	public void incrementSequenceNumber(int incrementAmount) {
		setSequenceNumber(getSequenceNumber() + incrementAmount);
	}

	/**
	 * Compares this object to another.
	 *
	 * @param object the object to compare this to.
	 * @return a integer result of the comparison.
	 */
	public int compareTo(Object object) {
		if (object instanceof SequencedObject) {
			SequencedObject o2 = (SequencedObject) object;
			return this.getSequenceNumber().compareTo(o2.getSequenceNumber());
		} else {
			return super.compareTo(object);
		}
	}

	public static void adjustSequenceNumberForAdd(Set existingObjects, SequencedObject objectToAdd) {
		if (existingObjects != null) {
			for (Object o : existingObjects) {
				if (((SequencedObject) o).getSequenceNumber() >= objectToAdd.getSequenceNumber()) {
					((SequencedObject) o).incrementSequenceNumber();
				}
			}
		}

	}

	public static void resequenceSequencedObjects(Set sequencedObjects) {
		int sequenceNumber = 0;
		for (Object o : sequencedObjects) {
			((SequencedObject) o).setSequenceNumber(sequenceNumber++);
		}
	}


}
