package org.archiviststoolkit.swing.jTreeDnD;

/**
 * Archivists' Toolkit(TM) Copyright © 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
 * All rights reserved.
 * <p/>
 * This software is free. You can redistribute it and / or modify it under the terms of the Educational Community License (ECL)
 * version 1.0 (http://www.opensource.org/licenses/ecl1.php)
 * <p/>
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the ECL license for more details about permissions and limitations.
 * <p/>
 * <p/>
 * Archivists' Toolkit(TM)
 * http://www.archiviststoolkit.org
 * info@archiviststoolkit.org
 * <p/>
 * Created by IntelliJ IDEA.
 * User: Nathan Stevens
 * Date: Aug 14, 2008
 * Time: 11:10:05 AM
 * To change this template use File | Settings | File Templates.
 */

import net.antonioshome.swing.treewrapper.TreeTreeDnDListener;
import net.antonioshome.swing.treewrapper.TreeTreeDnDEvent;
import net.antonioshome.swing.treewrapper.DnDVetoException;

import javax.swing.tree.DefaultMutableTreeNode;

import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.ApplicationFrame;

public class sequencedObjectDnDDropListener implements TreeTreeDnDListener {
    public void mayDrop(TreeTreeDnDEvent event) throws DnDVetoException {
//		throw new DnDVetoException("Drag and Drop is not allowed here");
    }

    public void drop(TreeTreeDnDEvent event) throws DnDVetoException {
        DefaultMutableTreeNode sourceNode = (DefaultMutableTreeNode) event.getSourceNode();
        if (sourceNode != null) {
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) sourceNode.getParent();
            treeDnDSequencedObject parentSO = (treeDnDSequencedObject) parent.getUserObject();
            treeDnDSequencedObject droppedSO = (treeDnDSequencedObject) sourceNode.getUserObject();


            //remove from parent
            try {
                parentSO.removeChild(droppedSO);
                droppedSO.setParentObject(null);
            }
            catch (ObjectNotRemovedException e) {
                throw new DnDVetoException(e.toString());
            }

            //resequence parent to get rid of holes
            DnDUtils.resequenceSequencedObjects(parentSO.getChildren());
            DefaultMutableTreeNode target = (DefaultMutableTreeNode) event.getTargetNode();
            treeDnDSequencedObject targetSO = (treeDnDSequencedObject) target.getUserObject();

            String dropLocation = event.getDropLocation();

            if (dropLocation.equals("above") || dropLocation.equals("below")) {
                //System.out.println("Droping above or below ...");
                //get sequence number
                int newSequenceNumber;
                if (dropLocation.equals("above")) {
                    newSequenceNumber = targetSO.getSequenceNumber();
                } else {
                    newSequenceNumber = targetSO.getSequenceNumber() + 1;
                }

                droppedSO.setSequenceNumber(newSequenceNumber);

                //prepare new parent
                treeDnDSequencedObject targetSOParent = targetSO.getParentObject();
                if (targetSOParent != null) { // found parent so proceed as normal
                    DnDUtils.adjustSequenceNumberForAdd(targetSOParent.getChildren(), droppedSO);

                    //add dropped node to new parent
                    droppedSO.setParentObject(targetSOParent);
                    targetSOParent.addChild(droppedSO);
                } else { // parent is null so just do add it to end of targetDO. should really be at top but treeWrapper doesn't work that way?
                    newSequenceNumber = DnDUtils.getMaxSequenceNumber(targetSO.getChildren()) + 1;

                    droppedSO.setSequenceNumber(newSequenceNumber);

                    //add dropped node to new parent
                    droppedSO.setParentObject(targetSO);
                    targetSO.addChild(droppedSO);
                }
            } else if (dropLocation.equals("into") && target.isLeaf()) {
                //System.out.println("Droping into leaf ...");
                int newSequenceNumber = 0;

                droppedSO.setSequenceNumber(newSequenceNumber);

                //add dropped node to new parent
                droppedSO.setParentObject(targetSO);
                targetSO.addChild(droppedSO);
            } else { // droping into folder
                //System.out.println("Droping into folder ...");
                int newSequenceNumber = DnDUtils.getMaxSequenceNumber(targetSO.getChildren()) + 1;

                droppedSO.setSequenceNumber(newSequenceNumber);

                //add dropped node to new parent
                droppedSO.setParentObject(targetSO);
                targetSO.addChild(droppedSO);
            }
        }

        // set the dirty flag so that the user is alerted that a change was made
        ApplicationFrame.getInstance().setRecordDirty();
    }
}
