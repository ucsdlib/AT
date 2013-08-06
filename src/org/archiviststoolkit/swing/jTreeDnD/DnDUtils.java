package org.archiviststoolkit.swing.jTreeDnD;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: leemandell
 * Date: Aug 6, 2008
 * Time: 12:18:06 PM
 * To change this template use File | Settings | File Templates.
 */

public class DnDUtils {

  // Method to adjust the sequence number when adding an object
  public static void adjustSequenceNumberForAdd(Set<treeDnDSequencedObject> existingObjects, treeDnDSequencedObject objectToAdd) {
    if (existingObjects != null) {
      for (treeDnDSequencedObject sequencedObject : existingObjects) {
        if (sequencedObject.getSequenceNumber() >= objectToAdd.getSequenceNumber()) {
          sequencedObject.setSequenceNumber(sequencedObject.getSequenceNumber() + 1);
        }
      }
    }
  }

  // Method to resequence the objects sequence numbers starting at 0
  public static void resequenceSequencedObjects(Set<treeDnDSequencedObject> sequencedObjects) {
    TreeSet<treeDnDSequencedObject> ts = new TreeSet<treeDnDSequencedObject>(sequencedObjects);
    int sequenceNumber = 0;
    for (treeDnDSequencedObject sequencedObject : ts) {
      sequencedObject.setSequenceNumber(sequenceNumber);
      sequenceNumber++;
    }
  }

  // Method to return the maximum sequence number
  public static int getMaxSequenceNumber(Set<treeDnDSequencedObject> sequencedObjects) {
    int sequenceNumber = 0;
    for (treeDnDSequencedObject sequencedObject : sequencedObjects) {
      if (sequencedObject.getSequenceNumber() > sequenceNumber) {
        sequenceNumber = sequencedObject.getSequenceNumber();
      }
    }
    return sequenceNumber;
  }

  // function to assign a new node order
  public static void assignNewOrder(DefaultMutableTreeNode droppedNode,
                                      DefaultMutableTreeNode parent,
                                      DefaultMutableTreeNode previousSibling) {

    treeDnDSequencedObject droppedSequencedObject = (treeDnDSequencedObject) droppedNode.getUserObject();
    treeDnDSequencedObject parentSequencedObject = (treeDnDSequencedObject) parent.getUserObject();

    // figure out the component order
    int sequenceNumber;

    if (previousSibling == null) {
      sequenceNumber = 0;
    } else {
      treeDnDSequencedObject previousSequencedObject = (treeDnDSequencedObject) previousSibling.getUserObject();
      sequenceNumber = previousSequencedObject.getSequenceNumber() + 1;
    }

    droppedSequencedObject.setSequenceNumber(sequenceNumber);
    adjustSequenceNumberForAdd(parentSequencedObject.getChildren(), droppedSequencedObject);
  }
}
