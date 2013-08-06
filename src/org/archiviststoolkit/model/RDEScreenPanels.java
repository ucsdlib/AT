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
 * Date: Jul 22, 2008
 * Time: 10:52:36 AM
 */

package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.SequencedObject;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.IncludeMethodInApplicationConfiguration;
import org.archiviststoolkit.editor.rde.*;
import org.archiviststoolkit.exceptions.RDEPanelCreationException;

import java.util.Set;
import java.util.HashSet;

@ExcludeFromDefaultValues
public class RDEScreenPanels extends SequencedObject {

	public static final String PROPERTYNAME_PANEL_TYPE = "panelType";
	public static final String PROPERTYNAME_SCREEN_LABEL = "screenLabel";

	public static final String PANEL_TYPE_INSTRUCTIONS = "instructions";
	public static final String PANEL_TYPE_SIMPLE = "simple";
	public static final String PANEL_TYPE_NOTE = "note";
	public static final String PANEL_TYPE_NAME_LINK = "nameLink";
	public static final String PANEL_TYPE_SUBJECT_LINK = "subjectLink";
	public static final String PANEL_TYPE_YEAR_RANGE_INCLUSIVE = "yearRangeInclusive";
	public static final String PANEL_TYPE_YEAR_RANGE_BULK = "yearRangeBulk";
	public static final String PANEL_TYPE_EXTENT = "extent";
	public static final String PANEL_TYPE_ANALOG_INSTANCE = "analogInstance";
	public static final String PANEL_TYPE_DIGITAL_INSTANCE = "digitalInstance";

	private boolean debug = false;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long rdeScreenPanelId;

	@ExcludeFromDefaultValues
	private RDEScreen rdeScreen;

	@ExcludeFromDefaultValues
	private String propertyName;

	@ExcludeFromDefaultValues
	private String panelType;

    @ExcludeFromDefaultValues
    private Set<RDEScreenPanelItems> screenPanelItems = new HashSet<RDEScreenPanelItems>();


	public RDEScreenPanels() {
	}

	public RDEScreenPanels(RDEScreen rdeScreen, String panelType, String propertyName) {
		this.rdeScreen = rdeScreen;
		this.panelType = panelType;
		this.propertyName = propertyName;
	}

	public RDEScreenPanels(RDEScreen rdeScreen, String panelType) {
		this.rdeScreen = rdeScreen;
		this.panelType = panelType;
	}


	/**
	 * @return Returns the identifier.
	 */
	public Long getIdentifier() {
		return getRdeScreenPanelId();
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(Long identifier) {
		setRdeScreenPanelId(identifier);
	}

	public Long getRdeScreenPanelId() {
		return rdeScreenPanelId;
	}

	public void setRdeScreenPanelId(Long rdeScreenPanelId) {
		this.rdeScreenPanelId = rdeScreenPanelId;
	}

	public RDEScreen getRdeScreen() {
		return rdeScreen;
	}

	public void setRdeScreen(RDEScreen rdeScreen) {
		this.rdeScreen = rdeScreen;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPanelType() {
		return panelType;
	}

	public void setPanelType(String panelType) {
		this.panelType = panelType;
	}

	public String toString() {
		if (debug) {
			return getSequenceNumber() +  " - " + this.getScreenLabel();
		} else {
			return this.getScreenLabel();
		}
	}

	@IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_SCREEN_LABEL, value = 1)
	@ExcludeFromDefaultValues
	public String getScreenLabel() {

		String returnString;

		 if (getPanelType().equals(PANEL_TYPE_SIMPLE)) {
			 ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(getRdeScreen().getClassName(), getPropertyName());
			 returnString = fieldInfo.getFieldLabel();
		 } else if (getPanelType().equals(PANEL_TYPE_ANALOG_INSTANCE)) {
			 returnString = "Analog instance";
		 } else if (getPanelType().equals(PANEL_TYPE_NOTE)) {
			 returnString = "Note";
		 } else if (getPanelType().equals(PANEL_TYPE_EXTENT)) {
			 returnString = "Extent";
		 } else if (getPanelType().equals(PANEL_TYPE_YEAR_RANGE_INCLUSIVE)) {
			 returnString = "Inclusive Dates";
		 } else if (getPanelType().equals(PANEL_TYPE_YEAR_RANGE_BULK)) {
			 returnString = "Bulk Dates";
		 } else if (getPanelType().equals(PANEL_TYPE_SUBJECT_LINK)) {
			 returnString = "Subject link";
		 } else if (getPanelType().equals(PANEL_TYPE_NAME_LINK)) {
			 returnString = "Name link";
		 } else if (getPanelType().equals(PANEL_TYPE_DIGITAL_INSTANCE)) {
			 returnString = "Digital instance";
		 } else {
			 returnString = "Unknown panel type";
		 }

		if (debug) {
			return getSequenceNumber() +  " - " + returnString;
		} else {
			return returnString;
		}
	}

	public RdePanel getRDEPanel(RdePanelContainer container) throws RDEPanelCreationException {

		RdePanel returnPanel = null;

		if (getPanelType().equals(PANEL_TYPE_INSTRUCTIONS)) {
			returnPanel = new RdeInstructions(container);

		} else if (getPanelType().equals(PANEL_TYPE_SIMPLE)) {
			ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(getRdeScreen().getClassName(), getPropertyName());
			if (fieldInfo.getDataType().equals("java.lang.Boolean")) {
				returnPanel = new RdeBooleanField(container, fieldInfo);
			} else if (fieldInfo.getDataType().equals("java.lang.String")) {
				//for string classes check to see if we need a combo box, textarea or textfield
				if (fieldInfo.getLookupList() != null && fieldInfo.getLookupList().length() > 0) {
					returnPanel = new RdeDropDownList(container, fieldInfo);
				} else if (fieldInfo.getStringLengthLimit() == null || fieldInfo.getStringLengthLimit() == 0) {
					returnPanel = new RdeTextArea(container, fieldInfo);
				} else {
					returnPanel = new RdeTextField(container, fieldInfo);
				}

			} else {
				throw new RDEPanelCreationException("Unsupported data type: " + fieldInfo.getDataType());
			}
		} else if (getPanelType().equals(PANEL_TYPE_ANALOG_INSTANCE)) {
			returnPanel = new RdeAnalogInstance(container);
		} else if (getPanelType().equals(PANEL_TYPE_DIGITAL_INSTANCE)) {
			returnPanel = new RdeDigitalInstance(container);
		} else if (getPanelType().equals(PANEL_TYPE_NOTE)) {
			returnPanel = new RdeNoteFields(container);
		} else if (getPanelType().equals(PANEL_TYPE_SUBJECT_LINK)) {
			returnPanel = new RdeSubjects(container);
		} else if (getPanelType().equals(PANEL_TYPE_NAME_LINK)) {
			returnPanel = new RdeNames(container);
		} else if (getPanelType().equals(PANEL_TYPE_EXTENT)) {
			returnPanel = new RdeExtentFields(container,
					ATFieldInfo.getFieldInfo(getRdeScreen().getClassName(), AccessionsResourcesCommon.PROPERTYNAME_EXTENT_NUMBER),
					ATFieldInfo.getFieldInfo(getRdeScreen().getClassName(), AccessionsResourcesCommon.PROPERTYNAME_EXTENT_TYPE));
		} else if (getPanelType().equals(PANEL_TYPE_YEAR_RANGE_INCLUSIVE)) {
			returnPanel = new RdeYearRangeField(container, "Inclusive Dates",
					ATFieldInfo.getFieldInfo(getRdeScreen().getClassName(), ArchDescription.PROPERTYNAME_DATE_BEGIN),
					ATFieldInfo.getFieldInfo(getRdeScreen().getClassName(), ArchDescription.PROPERTYNAME_DATE_END));
		} else if (getPanelType().equals(PANEL_TYPE_YEAR_RANGE_BULK)) {
			returnPanel = new RdeYearRangeField(container, "Bulk Dates",
					ATFieldInfo.getFieldInfo(getRdeScreen().getClassName(), AccessionsResourcesCommon.PROPERTYNAME_BULK_DATE_BEGIN),
					ATFieldInfo.getFieldInfo(getRdeScreen().getClassName(), AccessionsResourcesCommon.PROPERTYNAME_BULK_DATE_END));
		} else {
			throw new RDEPanelCreationException("Unsupported panel type: " + getPanelType());
		}
		returnPanel.initializeStickyLabels(this.getScreenPanelItems());
        //returnPanel.setDefaultValues();
        return returnPanel;
	}

	public Set<RDEScreenPanelItems> getScreenPanelItems() {
		return screenPanelItems;
	}

	public void setScreenPanelItems(Set<RDEScreenPanelItems> screenPanelItems) {
		this.screenPanelItems = screenPanelItems;
	}

	public void addScreenPanelItem(RDEScreenPanelItems screenPanelItem) {
		getScreenPanelItems().add(screenPanelItem);
	}
}
