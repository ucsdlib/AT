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
 * Date: Jul 29, 2008
 * Time: 4:28:52 PM
 */
package org.archiviststoolkit.util;

import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.editor.rde.RdePanelContainer;
import org.archiviststoolkit.exceptions.RDEPanelCreationException;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.DomainAccessObject;
import org.archiviststoolkit.mydomain.DomainAccessObjectFactory;
import org.archiviststoolkit.mydomain.LookupException;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.hibernate.LockMode;

import java.util.ArrayList;
import java.util.Iterator;

public class RDEFactory {

	private static RDEFactory ourInstance = new RDEFactory();
	private static ArrayList<RdePanelContainer> panelContainers;

	public static RDEFactory getInstance() {
		return ourInstance;
	}

	private RDEFactory() {
	}

	public boolean loadRDEs() {
		try {
			DomainAccessObject dao = DomainAccessObjectFactory.getInstance().getDomainAccessObject(RDEScreen.class);
			panelContainers = new ArrayList<RdePanelContainer>();
			RDEScreen thisScreen;
			for (Object o : dao.findAll(LockMode.READ)) {
				thisScreen = (RDEScreen) o;
				if (RDEUtils.canUseRDEScreen(thisScreen)) {
					RdePanelContainer container = new RdePanelContainer(thisScreen.getRdeScreenName());
					for (RDEScreenPanels panel : thisScreen.getScreenPanels()) {
						try {
							container.addSubPanel(panel.getRDEPanel(container));
						} catch (RDEPanelCreationException e) {
							new ErrorDialog("Error adding subpanels", e).showDialog();
							return false;
						}
					}
					container.initComponents();
					panelContainers.add(container);
				}
			}
		} catch (PersistenceException e) {
			new ErrorDialog("There is a problem loading Rapid Data Entry Screens", e).showDialog();
			return false;
		} catch (LookupException e) {
			new ErrorDialog("There is a problem loading Rapid Data Entry Screens", e).showDialog();
			return false;
		}
		return true;
	}

	public Object[] getRDEContainers() {
		Iterator containers = panelContainers.iterator();
		ArrayList set = new ArrayList();
		Object item;
		set.add("Rapid Data Entry");
		while (containers.hasNext()) {
			item = containers.next();
			set.add(item);
		}
		return set.toArray();
	}

    public Object[] getRDEContainerTitles() {
        Iterator containers = panelContainers.iterator();
        ArrayList set = new ArrayList();
        Object item;
        set.add("Rapid Data Entry");
        while (containers.hasNext()) {
            item = containers.next();
            set.add(item.toString());
        }
        return set.toArray();
    }

    // return the specific container by name
    public RdePanelContainer getContainer(String title) {
        //TODO should have method that loads only the specified container from database
        loadRDEs();
        Iterator containers = panelContainers.iterator();
        RdePanelContainer container = null;

        while (containers.hasNext()) {
            RdePanelContainer item = (RdePanelContainer)containers.next();
            if(item.getTitle().equals(title)) {
                container = item;
                break;
            }
        }
        return container;
    }

    public RDEScreenPanels createRDEScreenPanel(RDEScreen rdeScreen, String panelType) throws RDEPanelCreationException {
		return createRDEScreenPanel(rdeScreen, panelType, "");
	}

	public RDEScreenPanels createRDEScreenPanel(RDEScreen rdeScreen, String panelType, String propertyName) throws RDEPanelCreationException {
		RDEScreenPanels returnPanel = new RDEScreenPanels(rdeScreen, panelType, propertyName);
		if (panelType.equals(RDEScreenPanels.PANEL_TYPE_SIMPLE)) {
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, propertyName));
		} else if (panelType.equals(RDEScreenPanels.PANEL_TYPE_ANALOG_INSTANCE)) {
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ArchDescriptionAnalogInstances.PROPERTYNAME_INSTANCE_TYPE));
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER1_TYPE));
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER1_INDICATOR));
            //returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER1_NUMERIC_INDICATOR));
			//returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER1_ALPHA_NUMERIC_INDICATOR));
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER2_TYPE));
            returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER2_INDICATOR));
            //returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER2_NUMERIC_INDICATOR));
			//returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER2_ALPHA_NUMERIC_INDICATOR));
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER3_TYPE));
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER3_INDICATOR));
            //returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER3_NUMERIC_INDICATOR));
			//returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER3_ALPHA_NUMERIC_INDICATOR));
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ArchDescriptionAnalogInstances.PROPERTYNAME_BARCODE));
		} else if (panelType.equals(RDEScreenPanels.PANEL_TYPE_DIGITAL_INSTANCE)) {
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, DigitalObjects.PROPERTYNAME_TITLE));
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, DigitalObjects.PROPERTYNAME_DATE_EXPRESSION));
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, DigitalObjects.PROPERTYNAME_DATE_BEGIN));
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, DigitalObjects.PROPERTYNAME_DATE_END));
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, FileVersions.PROPERTYNAME_FILE_VERSIONS_URI + "1"));
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, FileVersions.PROPERTYNAME_FILE_VERSIONS_USE_STATEMENT + "1"));
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, FileVersions.PROPERTYNAME_EAD_DAO_ACTUATE + "1"));
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, FileVersions.PROPERTYNAME_EAD_DAO_SHOW + "1"));
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, FileVersions.PROPERTYNAME_FILE_VERSIONS_URI + "2"));
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, FileVersions.PROPERTYNAME_FILE_VERSIONS_USE_STATEMENT + "2"));
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, FileVersions.PROPERTYNAME_EAD_DAO_ACTUATE + "2"));
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, FileVersions.PROPERTYNAME_EAD_DAO_SHOW + "2"));
		} else if (panelType.equals(RDEScreenPanels.PANEL_TYPE_YEAR_RANGE_BULK)) {
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ResourcesComponents.PROPERTYNAME_BULK_DATE_BEGIN));
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ResourcesComponents.PROPERTYNAME_BULK_DATE_END));
		} else if (panelType.equals(RDEScreenPanels.PANEL_TYPE_YEAR_RANGE_INCLUSIVE)) {
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ResourcesComponents.PROPERTYNAME_DATE_BEGIN));
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ResourcesComponents.PROPERTYNAME_DATE_END));
		} else if (panelType.equals(RDEScreenPanels.PANEL_TYPE_EXTENT)) {
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ResourcesComponents.PROPERTYNAME_EXTENT_TYPE));
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ResourcesComponents.PROPERTYNAME_EXTENT_NUMBER));
		} else if (panelType.equals(RDEScreenPanels.PANEL_TYPE_NOTE)) {
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ArchDescriptionNotes.PROPERTYNAME_ARCH_DESCRIPTION_NOTE_TYPE));
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ArchDescriptionNotes.PROPERTYNAME_ARCH_DESCRIPTION_NOTE_INTERNAL_ONLY));
		} else if (panelType.equals(RDEScreenPanels.PANEL_TYPE_SUBJECT_LINK)) {
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ArchDescriptionSubjects.PROPERTYNAME_SUBJECT));
		} else if (panelType.equals(RDEScreenPanels.PANEL_TYPE_NAME_LINK)) {
			returnPanel.addScreenPanelItem(new RDEScreenPanelItems(returnPanel, ArchDescriptionNames.PROPERTYNAME_NAME));
		} else {
			throw new RDEPanelCreationException("Panel type: " + panelType + "is not supported");
		}

		return returnPanel;
	}


}
