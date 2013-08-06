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
import org.archiviststoolkit.swing.jTreeDnD.DnDUtils;
import org.archiviststoolkit.model.DigitalObjects;

import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.dnd.DropTargetDropEvent;

//TODO This is no longer used and should be removed from code base
public class DnDDigitalObjectTree extends DnDJTree {

//	private Resources resource;

	public DnDDigitalObjectTree(DefaultMutableTreeNode root) {
		super(root);
//		this.setResource(resource);
	}

	public void drop(DropTargetDropEvent dtde) {
		super.drop(dtde);
		DefaultMutableTreeNode droppedNode = getDroppedNode();
		if (droppedNode != null) {
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) droppedNode.getParent();
			DnDDigitalObjectTree.assignNewOrder(droppedNode, parent, droppedNode.getPreviousSibling());
//			updateComponent(droppedNode);
		}
	}

	public static void assignNewOrder(DefaultMutableTreeNode droppedNode,
									  DefaultMutableTreeNode parent,
									  DefaultMutableTreeNode previousSibling) {

		DigitalObjects droppedDigitalObject = (DigitalObjects) droppedNode.getUserObject();
		DigitalObjects parentDigitalObject = (DigitalObjects) parent.getUserObject();

    // figure out the component order
		int objectOrder;

    if (previousSibling == null) {
			objectOrder = 0;
		} else {
			DigitalObjects previousDigitalObject = (DigitalObjects) previousSibling.getUserObject();
			objectOrder = previousDigitalObject.getObjectOrder() + 1;
		}

    droppedDigitalObject.setObjectOrder(objectOrder);
		//DnDUtils.adjustSequenceNumberForAddDO(parentDigitalObject.getDigitalObjectChildren(), droppedDigitalObject);

    //first set the parent for digital object and tell parent it has a new child
		//droppedDigitalObject.setParent(parentDigitalObject);
    //parentDigitalObject.addChild(droppedDigitalObject);
  }
}
