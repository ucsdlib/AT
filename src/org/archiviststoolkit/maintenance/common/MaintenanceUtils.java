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
 * Date: Aug 5, 2008
 * Time: 8:07:57 PM
 */

package org.archiviststoolkit.maintenance.common;

import org.archiviststoolkit.mydomain.DomainAccessObject;
import org.archiviststoolkit.mydomain.DomainAccessObjectFactory;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.model.RDEScreen;
import org.archiviststoolkit.model.ResourcesComponents;
import org.archiviststoolkit.model.RDEScreenPanels;
import org.archiviststoolkit.util.RDEFactory;
import org.archiviststoolkit.exceptions.RDEPanelCreationException;

public class MaintenanceUtils {

	public static void createDefaultRDEScreen() throws PersistenceException, RDEPanelCreationException {
		DomainAccessObject dao = DomainAccessObjectFactory.getInstance().getDomainAccessObject(RDEScreen.class);
		RDEScreen rdeScreen = new RDEScreen(ResourcesComponents.class.getName(), "Default");

		int sequenceNumber = 0;
		RDEFactory factory = RDEFactory.getInstance();
		RDEScreenPanels newPanel = factory.createRDEScreenPanel(rdeScreen, RDEScreenPanels.PANEL_TYPE_SIMPLE, ResourcesComponents.PROPERTYNAME_LEVEL);
		newPanel.setSequenceNumber(sequenceNumber++);
		rdeScreen.addScreenPanels(newPanel);

		newPanel = factory.createRDEScreenPanel(rdeScreen, RDEScreenPanels.PANEL_TYPE_SIMPLE, ResourcesComponents.PROPERTYNAME_INTERNAL_ONLY);
		newPanel.setSequenceNumber(sequenceNumber++);
		rdeScreen.addScreenPanels(newPanel);

		newPanel = factory.createRDEScreenPanel(rdeScreen, RDEScreenPanels.PANEL_TYPE_SIMPLE, ResourcesComponents.PROPERTYNAME_RESTRICTIONS_APPLY);
		newPanel.setSequenceNumber(sequenceNumber++);
		rdeScreen.addScreenPanels(newPanel);

		newPanel = factory.createRDEScreenPanel(rdeScreen, RDEScreenPanels.PANEL_TYPE_SIMPLE, ResourcesComponents.PROPERTYNAME_TITLE);
		newPanel.setSequenceNumber(sequenceNumber++);
		rdeScreen.addScreenPanels(newPanel);

		newPanel = factory.createRDEScreenPanel(rdeScreen, RDEScreenPanels.PANEL_TYPE_SIMPLE, ResourcesComponents.PROPERTYNAME_CONTAINER_SUMMARY);
		newPanel.setSequenceNumber(sequenceNumber++);
		rdeScreen.addScreenPanels(newPanel);

		newPanel = factory.createRDEScreenPanel(rdeScreen, RDEScreenPanels.PANEL_TYPE_SIMPLE, ResourcesComponents.PROPERTYNAME_DATE_EXPRESSION);
		newPanel.setSequenceNumber(sequenceNumber++);
		rdeScreen.addScreenPanels(newPanel);

		newPanel = factory.createRDEScreenPanel(rdeScreen, RDEScreenPanels.PANEL_TYPE_YEAR_RANGE_INCLUSIVE);
		newPanel.setSequenceNumber(sequenceNumber++);
		rdeScreen.addScreenPanels(newPanel);

		newPanel = factory.createRDEScreenPanel(rdeScreen, RDEScreenPanels.PANEL_TYPE_EXTENT);
		newPanel.setSequenceNumber(sequenceNumber++);
		rdeScreen.addScreenPanels(newPanel);

		newPanel = factory.createRDEScreenPanel(rdeScreen, RDEScreenPanels.PANEL_TYPE_NOTE);
		newPanel.setSequenceNumber(sequenceNumber++);
		rdeScreen.addScreenPanels(newPanel);

		newPanel = factory.createRDEScreenPanel(rdeScreen, RDEScreenPanels.PANEL_TYPE_NOTE);
		newPanel.setSequenceNumber(sequenceNumber++);
		rdeScreen.addScreenPanels(newPanel);

		newPanel = factory.createRDEScreenPanel(rdeScreen, RDEScreenPanels.PANEL_TYPE_NOTE);
		newPanel.setSequenceNumber(sequenceNumber++);
		rdeScreen.addScreenPanels(newPanel);

		newPanel = factory.createRDEScreenPanel(rdeScreen, RDEScreenPanels.PANEL_TYPE_ANALOG_INSTANCE);
		newPanel.setSequenceNumber(sequenceNumber++);
		rdeScreen.addScreenPanels(newPanel);

		dao.add(rdeScreen);

	}

}
