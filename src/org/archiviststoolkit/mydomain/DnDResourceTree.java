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
 * Date: Mar 9, 2006
 * Time: 9:14:58 AM
 */

package org.archiviststoolkit.mydomain;

import org.archiviststoolkit.swing.DnDJTree;
import org.archiviststoolkit.model.ResourceTreeNode;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.model.ResourcesComponents;
import org.archiviststoolkit.model.ResourcesCommon;
import org.archiviststoolkit.exceptions.InvalidTreeNode;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.dialog.ErrorDialog;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.dnd.DropTargetDropEvent;

public class DnDResourceTree extends DnDJTree {

	private Resources resource;

	public DnDResourceTree(DefaultMutableTreeNode root, Resources resource) {
		super(root);
		this.setResource(resource);
	}

	public void drop(DropTargetDropEvent dtde) {
		super.drop(dtde);
		DefaultMutableTreeNode droppedNode = getDroppedNode();
		if (droppedNode != null) {
			DefaultTreeModel treeModel = (DefaultTreeModel)this.getModel();
			if (droppedNode != null) {
				try {
					DefaultMutableTreeNode parent = (DefaultMutableTreeNode) droppedNode.getParent();
					if (parent == null) {
//						dtde.rejectDrop();
						System.out.println("null parent");
						return;
					}
					assignNewOrder(droppedNode, parent, droppedNode.getPreviousSibling(), treeModel);
					updateComponent(droppedNode);
					treeModel.nodeChanged(droppedNode);

//						DefaultMutableTreeNode child = (DefaultMutableTreeNode)parent.getFirstChild();
//						while (child != null) {
//							((ResourceTreeNode)child.getUserObject()).updateComponentSequenceNumber();
//							child = (DefaultMutableTreeNode)child.getNextSibling();
//						}
					//if this was a drag from a different parent the update the old parent
//					if (getOldParent() != getNewParent()) {
//						child = (DefaultMutableTreeNode)getOldParent().getFirstChild();
//						int sequence = 0;
//						while (child != null) {
//							((ResourceTreeNode)child.getUserObject()).setSequenceNumber(sequence++);
//							((ResourceTreeNode)child.getUserObject()).updateComponentSequenceNumber();
//							child = (DefaultMutableTreeNode)child.getNextSibling();
//						}
//					}
				} catch (InvalidTreeNode invalidTreeNode) {
					dtde.rejectDrop();
					new ErrorDialog("Error dropping node", invalidTreeNode).showDialog();
				} catch (ObjectNotRemovedException e) {
					dtde.rejectDrop();
					new ErrorDialog("Error dropping node", e).showDialog();
				}
			}
		}
	}

	public static void assignNewOrder(DefaultMutableTreeNode droppedNode,
									  DefaultMutableTreeNode parent,
									  DefaultMutableTreeNode previousSibling,
									  DefaultTreeModel treeModel) {

		ResourceTreeNode droppedResourceNode = (ResourceTreeNode) droppedNode.getUserObject();
		ResourceTreeNode parentResourceNode = null;
		try {
			parentResourceNode = (ResourceTreeNode) parent.getUserObject();
		} catch (Exception e) {
			new ErrorDialog("", e).showDialog();
		}
		//first set the id for the parent of the dropped node
		if (parentResourceNode.getResourceTreeNodeClass() == Resources.class) {
			droppedResourceNode.setParentForDrop(parentResourceNode.getResource());
		} else {
			droppedResourceNode.setParentForDrop(parentResourceNode.getResourceComponent());
		}
		// figure out the component order
		int sequenceNumber;
		if (previousSibling == null) {
			// this is the first child
			sequenceNumber = 0;
		} else {
			//it is somewhere in between
			ResourceTreeNode previousResourceNode = (ResourceTreeNode) previousSibling.getUserObject();
			sequenceNumber = previousResourceNode.getSequenceNumber() + 1;
		}
		droppedResourceNode.setSequenceNumber(sequenceNumber++);
		droppedResourceNode.updateComponentSequenceNumber();
		DefaultMutableTreeNode nextSibling = droppedNode.getNextSibling();
		while (nextSibling != null) {
			((ResourceTreeNode)nextSibling.getUserObject()).setSequenceNumber(sequenceNumber++);
			((ResourceTreeNode)nextSibling.getUserObject()).updateComponentSequenceNumber();
			treeModel.nodeChanged(nextSibling);
			nextSibling = nextSibling.getNextSibling();
		}

//		droppedResourceNode.updateComponentSequenceNumber();
	}

	private void updateComponent(DefaultMutableTreeNode droppedNode) throws InvalidTreeNode, ObjectNotRemovedException {

		//update the corresponding component

		ResourceTreeNode droppedResourceNode = (ResourceTreeNode) droppedNode.getUserObject();
		ResourcesComponents component;
		if (droppedResourceNode.getResourceComponent() != null) {
			component = droppedResourceNode.getResourceComponent();
		} else {
			throw new InvalidTreeNode("Dropped node must be a component");
		}
//		component.setSequenceNumber(droppedResourceNode.getSequenceNumber());
		ResourcesCommon originalParent = component.getParent();
		ResourcesCommon parentForDrop = droppedResourceNode.getParentForDrop();

		originalParent.removeComponent(component);

		if (originalParent != parentForDrop) {
			if (parentForDrop instanceof Resources) {
				component.setResource((Resources)parentForDrop);
				component.setResourceComponentParent(null);
			} else if (parentForDrop instanceof ResourcesComponents) {
				component.setResource(null);
				component.setResourceComponentParent((ResourcesComponents)parentForDrop);
			} else {
				throw new InvalidTreeNode("Parent node mut be a resource or component");
			}
		}

		parentForDrop.addComponent(component, true);
        originalParent.resequenceSequencedObjects();
	}

	public Resources getResource() {
		return resource;
	}

	public void setResource(Resources resource) {
		this.resource = resource;
	}
}
