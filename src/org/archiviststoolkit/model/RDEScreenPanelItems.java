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

@ExcludeFromDefaultValues
public class RDEScreenPanelItems extends SequencedObject {

	public static final String PROPERTYNAME_PROPERTY_NAME = "propertyName";
	public static final String PROPERTYNAME_STICKY = "sticky";


	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long rdeScreenPanelItemId;

	@ExcludeFromDefaultValues
	private RDEScreenPanels rdeScreenPanel;

	@ExcludeFromDefaultValues
	@IncludeInApplicationConfiguration(1)
	private String propertyName;

	@ExcludeFromDefaultValues
	@IncludeInApplicationConfiguration(2)
	private Boolean sticky = false;

	public RDEScreenPanelItems() {
	}

	public RDEScreenPanelItems(RDEScreenPanels rdeScreenPanel, String propertyName) {
		this.rdeScreenPanel = rdeScreenPanel;
		this.propertyName = propertyName;
	}

	/**
	 * @return Returns the identifier.
	 */
	public Long getIdentifier() {
		return getRdeScreenPanelItemId();
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(Long identifier) {
		setRdeScreenPanelItemId(identifier);
	}


	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public Long getRdeScreenPanelItemId() {
		return rdeScreenPanelItemId;
	}

	public void setRdeScreenPanelItemId(Long rdeScreenPanelItemId) {
		this.rdeScreenPanelItemId = rdeScreenPanelItemId;
	}

	public RDEScreenPanels getRdeScreenPanel() {
		return rdeScreenPanel;
	}

	public void setRdeScreenPanel(RDEScreenPanels rdeScreenPanel) {
		this.rdeScreenPanel = rdeScreenPanel;
	}

	public Boolean getSticky() {
		return sticky;
	}

	public void setSticky(Boolean sticky) {
		this.sticky = sticky;
	}

	public void toggleStickiness() {
		setSticky(!getSticky());
	}
}