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
 */

package org.archiviststoolkit.model;

import org.archiviststoolkit.exceptions.ObjectNotRemovedException;

import java.io.Serializable;

import net.antonioshome.swing.treewrapper.DnDVetoException;


public class ResourceTreeNode implements Serializable {

	private String resourceTreeNodeTitle = null;

	private Class resourceTreeNodeClass = null;

	private Long resourceTreeNodeID = null;

	private ResourcesComponents resourceComponent = null;
	private Resources resource = null;

	private Integer sequenceNumber = null;

	private ResourcesCommon parentForDrop;
	private ResourcesCommon parent = null;


	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public ResourceTreeNode() {
	}

	public ResourceTreeNode(ResourcesComponents resourceComponent) {
		this.resourceComponent = resourceComponent;
		this.parent = resourceComponent.getParent();
	}

	public ResourceTreeNode(Resources resource) {
		this.resource = resource;
	}


	// ********************** Accessor Methods ********************** //

	public Long getResourceTreeNodeId() {
		return resourceTreeNodeID;
	}

	public void setResourceTreeNodeId(Long resourceTreeNodeID) {
		this.resourceTreeNodeID = resourceTreeNodeID;
	}


	public Long getIdentifier() {
		return this.resourceTreeNodeID;
	}

	public void setIdentifier(Long identifier) {
		this.setResourceTreeNodeId(identifier);
	}


	public void setResourceTreeNodeTitle(String resourceTreeNodeTitle) {
		this.resourceTreeNodeTitle = resourceTreeNodeTitle;
	}

	public String getResourceTreeNodeTitle() {
		return resourceTreeNodeTitle;
	}

	public void setResourceTreeNodeClass(Class resourceTreeNodeClass) {
		this.resourceTreeNodeClass = resourceTreeNodeClass;
	}

	public Class getResourceTreeNodeClass() {
		return resourceTreeNodeClass;
	}

	public String toString() {
		return resourceTreeNodeTitle;
	}

	public Integer getSequenceNumber() {
		if (resource != null) {
			return 0;
		} else {
			return resourceComponent.getSequenceNumber();
		}
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public void setSequenceNumberFromComponent() {
		if (resourceComponent != null) {
			setSequenceNumber(resourceComponent.getSequenceNumber());
		}
	}

	public void updateComponentSequenceNumber() {
		if (resourceComponent != null) {
			resourceComponent.setSequenceNumber(getSequenceNumber());
			resourceTreeNodeTitle = resourceComponent.getLabelForTree();
		}
	}

//	public void updateTitle() {
//		if (resource != null) {
//			resourceTreeNodeTitle = resource.getLabelForTree();
//		} else if (resourceComponent != null) {
//			resourceTreeNodeTitle = resourceComponent.getLabelForTree();
//		}
//	}

	public ResourcesCommon getResourceOrComponent() {
		if (this.resource != null) {
			return this.resource;
		} else {
			return this.resourceComponent;
		}
	}

	public ResourcesComponents getResourceComponent() {
		return resourceComponent;
	}

	public void setResourceComponent(ResourcesComponents resourceComponent) {
		this.resourceComponent = resourceComponent;
		this.parent = resourceComponent.getParent();
	}

	public Resources getResource() {
		return resource;
	}

	public void setResource(Resources resource) {
		this.resource = resource;
	}

	public ResourcesCommon getParentForDrop() {
		return parentForDrop;
	}

	public void setParentForDrop(ResourcesCommon parentForDrop) {
		this.parentForDrop = parentForDrop;
	}

	public void removeNodeFromParent() throws ObjectNotRemovedException, DnDVetoException {
		if (resourceComponent != null) {
			if (parent != null) {
				parent.removeComponent(resourceComponent);
				resourceComponent.setResource(null);
				resourceComponent.setResourceComponentParent(null);
			} else {
				System.out.println("Dragged component has a null parent");
				throw new DnDVetoException("Dragged component has a null parent");
			}
		} else {
			System.out.println("Dragged component is not a resouce component");
			throw new DnDVetoException("Dragged component is not a resouce component");
		}
	}

	public void addNodeToNewParent(ResourceTreeNode targetForDrop) {
		if (targetForDrop.getParent() == null) {
			//we are at the root so this is the parent
			this.parentForDrop = targetForDrop.getResource();
		} else {
			this.parentForDrop = targetForDrop.getParent();
		}
		ResourcesCommon target = targetForDrop.getResourceOrComponent();
		if (target instanceof Resources) {
			resourceComponent.setSequenceNumber(0);
			resourceComponent.setResource((Resources)target);
		} else {
//			ResourcesComponents parentComponent = (ResourcesComponents)parentForDrop;
			if (((ResourcesComponents)target).isHasChild()) {
				resourceComponent.setSequenceNumber(0);
				resourceComponent.setResourceComponentParent((ResourcesComponents)target);
			} else {
				resourceComponent.setSequenceNumber(targetForDrop.getSequenceNumber() + 1);
				ResourcesCommon targetParent = ((ResourcesComponents)target).getParent();
				if (targetParent instanceof Resources) {
					resourceComponent.setResource((Resources)targetParent);
				} else {
					resourceComponent.setResourceComponentParent((ResourcesComponents)targetParent);
				}
			}
		}
		parentForDrop.addComponent(resourceComponent, true);
	}

	public ResourcesCommon getParent() {
		return parent;
	}

	public void setParent(ResourcesCommon parent) {
		this.parent = parent;
	}
}
