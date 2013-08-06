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
 * Time: 10:48:08 AM
 */

package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.editor.rde.RdePanelContainer;
import org.archiviststoolkit.exceptions.RDEPanelCreationException;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.ApplicationFrame;

import java.util.Set;
import java.util.TreeSet;
import java.util.Collection;
import java.util.SortedSet;

@ExcludeFromDefaultValues
public class RDEScreen extends DomainObject {

	public static final String PROPERTYNAME_NAME_CLASS_NAME = "className";
	public static final String PROPERTYNAME_NAME_RDE_SCREEN_NAME = "rdeScreenName";
	public static final String PROPERTYNAME_NAME_RDE_USER = "user";
	public static final String PROPERTYNAME_NAME_KEEP_PRIVATE_SCREEN = "keepScreenPrivate";
	public static final String PROPERTYNAME_NAME_LET_OTHERS_EDIT = "letOthersEdit";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long rdeScreenId;
	
	@ExcludeFromDefaultValues
	private String className;

	@ExcludeFromDefaultValues
	@IncludeInApplicationConfiguration(1)
	private String rdeScreenName;

	@ExcludeFromDefaultValues
	@IncludeInApplicationConfiguration(2)
	private Users user;

	@ExcludeFromDefaultValues
  private Boolean keepScreenPrivate;

	@ExcludeFromDefaultValues
	private Boolean letOthersEdit;

    @ExcludeFromDefaultValues
    private SortedSet<RDEScreenPanels> screenPanels = new TreeSet<RDEScreenPanels>();

	public RDEScreen() {
		if (ApplicationFrame.getInstance().getCurrentUser() != null) {
			user = ApplicationFrame.getInstance().getCurrentUser();
		}
		//todo for now just default to components for the class
		this.className = ResourcesComponents.class.getName();
	}
	
	public RDEScreen(String className, String rdeScreenName) {
		this.className = className;
		this.rdeScreenName = rdeScreenName;
	}

	public String toString() {
		return getRdeScreenName();
	}

	/**
	 * @return Returns the identifier.
	 */
	public Long getIdentifier() {
		return getRdeScreenId();
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(Long identifier) {
		setRdeScreenId(identifier);
	}

	public Long getRdeScreenId() {
		return rdeScreenId;
	}

	public void setRdeScreenId(Long rdeScreenId) {
		this.rdeScreenId = rdeScreenId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getRdeScreenName() {
		return rdeScreenName;
	}

	public void setRdeScreenName(String rdeScreenName) {
		this.rdeScreenName = rdeScreenName;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public SortedSet<RDEScreenPanels> getScreenPanels() {
		return screenPanels;
	}

	public void setScreenPanels(SortedSet<RDEScreenPanels> screenPanels) {
		this.screenPanels = screenPanels;
	}

	public void addScreenPanels(RDEScreenPanels panel) {
		getScreenPanels().add(panel);
	}

	protected void removeScreenPanel(RDEScreenPanels panel) {
		if (panel == null)
			throw new IllegalArgumentException("Can't remove null screen panel.");

		getScreenPanels().remove(panel);
	}

	public Class getClazz() throws ClassNotFoundException {
		return Class.forName(getClassName());
	}


	public void removeRelatedObject(DomainObject domainObject) {
		if (domainObject instanceof RDEScreenPanels) {
			removeScreenPanel((RDEScreenPanels) domainObject);
		}
	}

	public Collection getRelatedCollection(Class clazz) {
		if (clazz == RDEScreenPanels.class) {
			return getScreenPanels();
		} else {
			return null;
		}
	}


//	public static RDEScreen createDefaultScreen() {
//
//		RDEScreen rdeScreen = new RDEScreen(Resources.class.getName(), "Default");
//
//
//		return rdeScreen;
//	}
//
//	public static RdePanelContainer test(Resources resource) {
//		RdePanelContainer container = new RdePanelContainer();
//		container.setResource(resource);
//
//		RDEScreen rdeScreen = new RDEScreen(Resources.class.getName(), "Default");
//		rdeScreen.appendScreenPanel(new RDEScreenPanels(rdeScreen, RDEScreenPanels.PANEL_TYPE_SIMPLE, ResourcesComponents.PROPERTYNAME_LEVEL));
//		rdeScreen.appendScreenPanel(new RDEScreenPanels(rdeScreen, RDEScreenPanels.PANEL_TYPE_SIMPLE, ResourcesComponents.PROPERTYNAME_INTERNAL_ONLY));
//		rdeScreen.appendScreenPanel(new RDEScreenPanels(rdeScreen, RDEScreenPanels.PANEL_TYPE_SIMPLE, ResourcesComponents.PROPERTYNAME_RESTRICTIONS_APPLY));
//		rdeScreen.appendScreenPanel(new RDEScreenPanels(rdeScreen, RDEScreenPanels.PANEL_TYPE_SIMPLE, ResourcesComponents.PROPERTYNAME_TITLE));
//		rdeScreen.appendScreenPanel(new RDEScreenPanels(rdeScreen, RDEScreenPanels.PANEL_TYPE_SIMPLE, ResourcesComponents.PROPERTYNAME_CONTAINER_SUMMARY));
//		rdeScreen.appendScreenPanel(new RDEScreenPanels(rdeScreen, RDEScreenPanels.PANEL_TYPE_SIMPLE, ResourcesComponents.PROPERTYNAME_DATE_EXPRESSION));
//		rdeScreen.appendScreenPanel(new RDEScreenPanels(rdeScreen, RDEScreenPanels.PANEL_TYPE_YEAR_RANGE_INCLUSIVE));
//		rdeScreen.appendScreenPanel(new RDEScreenPanels(rdeScreen, RDEScreenPanels.PANEL_TYPE_EXTENT));
//		rdeScreen.appendScreenPanel(new RDEScreenPanels(rdeScreen, RDEScreenPanels.PANEL_TYPE_NOTE));
//		rdeScreen.appendScreenPanel(new RDEScreenPanels(rdeScreen, RDEScreenPanels.PANEL_TYPE_NOTE));
//		rdeScreen.appendScreenPanel(new RDEScreenPanels(rdeScreen, RDEScreenPanels.PANEL_TYPE_NOTE));
//		rdeScreen.appendScreenPanel(new RDEScreenPanels(rdeScreen, RDEScreenPanels.PANEL_TYPE_ANALOG_INSTANCE));

//		for (RDEScreenPanels panel: rdeScreen.getScreenPanels()) {
//			try {
//				container.addSubPanel(panel.getRDEPanel(container));
//			} catch (RDEPanelCreationException e) {
//				new ErrorDialog("Error adding subpanels", e).showDialog();
//				return null;
//			}
//		}
//		container.initComponents();
//		return container;
//
//	}

	public Boolean getKeepScreenPrivate() {
		return keepScreenPrivate;
	}

	public void setKeepScreenPrivate(Boolean keepScreenPrivate) {
		this.keepScreenPrivate = keepScreenPrivate;
	}

	public Boolean getLetOthersEdit() {
		return letOthersEdit;
	}

	public void setLetOthersEdit(Boolean letOthersEdit) {
		this.letOthersEdit = letOthersEdit;
	}
}
