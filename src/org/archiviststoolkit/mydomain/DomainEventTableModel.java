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
 * Date: Jun 9, 2006
 * Time: 1:49:43 PM
 */

package org.archiviststoolkit.mydomain;

import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.gui.TableFormat;

import java.util.Collection;
import java.util.ArrayList;

public class DomainEventTableModel extends EventTableModel {

	public DomainEventTableModel(EventList eventList, TableFormat tableFormat) {
		super(eventList, tableFormat);
	}

	/**
	 * The list of domainobjects to display.
	 */

	protected ArrayList<DomainObject> domainCollection = null;

	public void updateCollection(Collection newCollection) {

		if (newCollection == null) {
			domainCollection = new ArrayList<DomainObject>();
		} else {
			domainCollection = new ArrayList<DomainObject>(newCollection);
		}

	}

	/**
	 * Get the row index of a specific domain object.
	 *
	 * @param object the object in question
	 * @return the index within the row list
	 */

	public int getIndex(final DomainObject object) {
		return (getDomainCollection().indexOf(object));
	}

	public ArrayList<DomainObject> getDomainCollection() {
		return domainCollection;
	}

	public void setDomainObject(final int index, final DomainObject object) {
		getDomainCollection().set(index, object);
	}

}
