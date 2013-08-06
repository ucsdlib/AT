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
 * Date: Aug 7, 2008
 * Time: 12:23:49 PM
 */

package org.archiviststoolkit.mydomain;

import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.editor.ResourceTreeViewer;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.dialog.QueryEditor;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.model.ResourcesComponentsSearchResult;
import org.archiviststoolkit.swing.ATProgressUtil;
import org.archiviststoolkit.swing.AlternatingRowColorTable;
import org.archiviststoolkit.swing.DateTableCellRenderer;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.Collection;
import java.util.HashSet;

public class ResourceTableWorkSurface extends DomainTableWorkSurface {

	DomainSortableTable resourceTable;
	DomainTableListEventListener resourceEventListener;

	DomainSortableTable resourcesComponentsResultsTable;
	DomainTableListEventListener resourcesComponentsResultsEventListener;

	ResourcesComponentsSearchResult currentComponentSearchResult;

	boolean inComponentSearch = false;
	int numberOfResources = 0;

	/**
	 * Constructor.
	 *
	 * @param clazz	   the domain model class
	 * @param name		the name of this worksurface
	 * @param icon		the icon of this worksurface
	 * @param tableFormat the table format to use for the domain table
	 */
	public ResourceTableWorkSurface(final Class clazz, final String name, final Icon icon, DomainTableFormat tableFormat) {
		super(clazz, name, icon, tableFormat);
		initResourceTables();
	}

	/**
	 * Constructor.
	 *
	 * @param clazz the domain model class
	 * @param name  the name of this worksurface
	 * @param icon  the icon of this worksurface
	 */
	public ResourceTableWorkSurface(final Class clazz, final String name, final Icon icon) {
		super(clazz, name, icon);
		initResourceTables();
	}


	private void initResourceTables() {

		resourceTable = new DomainSortableTable(Resources.class, filterField);
		resourceTable.setTableType(AlternatingRowColorTable.TABLE_TYPE_NORMAL);
		resourceEventListener = new DomainTableListEventListener();
		resourceTable.getFilteredList().addListEventListener(resourceEventListener);
		resourceTable.setDefaultRenderer(Date.class, new DateTableCellRenderer());
		resourceTable.addMouseListener(this);

		resourcesComponentsResultsTable = new DomainSortableTable(ResourcesComponentsSearchResult.class, filterField);
		resourcesComponentsResultsTable.setTableType(AlternatingRowColorTable.TABLE_TYPE_NORMAL);
		resourcesComponentsResultsEventListener = new DomainTableListEventListener();
		resourcesComponentsResultsTable.getFilteredList().addListEventListener(resourcesComponentsResultsEventListener);
		resourcesComponentsResultsTable.setDefaultRenderer(Date.class, new DateTableCellRenderer());
		resourcesComponentsResultsTable.addMouseListener(this);

		swapTables(resourceTable, resourceEventListener);
	}

	private synchronized void swapTables(DomainSortableTable table, DomainTableListEventListener eventListener) {
		rootComponent.remove(scrollPane);
		scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.table = table;
		this.eventListener = eventListener;

		rootComponent.add(scrollPane, BorderLayout.CENTER);
		scrollPane.invalidate();
		scrollPane.validate();
		scrollPane.repaint();
	}

	protected synchronized void updateListWithNewResultSet(Collection newResultSet) {
		if (newResultSet.size() > 0) {
			Object firstResult = newResultSet.iterator().next();
			if (firstResult instanceof ResourcesComponentsSearchResult) {
				inComponentSearch = true;
				numberOfResources = calculateNumberOfResources(newResultSet);
			} else {
				inComponentSearch = false;
			}
		}
		if (inComponentSearch) {
			swapTables(resourcesComponentsResultsTable, resourcesComponentsResultsEventListener);
			inComponentSearch = true;
		} else {
			swapTables(resourceTable, resourceEventListener);
			inComponentSearch = false;
		}
		super.updateListWithNewResultSet(newResultSet);
	}

	private int calculateNumberOfResources(Collection newResultSet) {
		ResourcesComponentsSearchResult result;
		HashSet<Resources> resources = new HashSet<Resources>();
		for (Object o: newResultSet) {
			result = (ResourcesComponentsSearchResult)o;
			resources.add(result.getParentResource());
		}
		return resources.size();
	}


	protected void getCurrentDomainObjectFromDatabase(DomainObject domainObject) throws LookupException {
		currentDomainObject = access.findByPrimaryKeyLongSession(domainObject.getIdentifier());
		ResourceTreeViewer treeViewer = (ResourceTreeViewer)dialog.editorFields;
		if (inComponentSearch) {
			currentComponentSearchResult = (ResourcesComponentsSearchResult)domainObject;
			treeViewer.setResourcesComponentFromSearch(currentComponentSearchResult.getCompenent());
		} else {
			treeViewer.setResourcesComponentFromSearch(null);
		}
	}

	protected void setSublistWithCurrentDomainObject() {
		if (inComponentSearch) {
			currentObjectSublist.set(0, currentComponentSearchResult);
		} else {
			currentObjectSublist.set(0, currentDomainObject);
		}
	}

	protected void setFilteredListWithCurrentDomainObject() {
		if (inComponentSearch) {
			table.getFilteredList().set(selectedRow, currentComponentSearchResult);
		} else {
			table.getFilteredList().set(selectedRow, currentDomainObject);
		}
	}

	protected void updateRowCount() {
		if (inComponentSearch) {
			rowCount = table.getFilteredList().size();
			int totalRecords = table.getEventList().size();
			String resultString;
			if (rowCount == totalRecords) {
				resultString = rowCount + " Hits(s) in " + numberOfResources + " Resources";
			} else {
				resultString = rowCount + " of " + totalRecords + " Hits(s) in " + numberOfResources + " Resources";
			}
			if (humanReadableSearchString != null && humanReadableSearchString.length() > 0) {
				resultString ="<html>" + resultString + " found for search \"<FONT COLOR='blue'>" + humanReadableSearchString + "</FONT>\"</html>";
			}
			resultSizeDisplay.setText(resultString);
		} else {
			super.updateRowCount();
		}
	}

}
