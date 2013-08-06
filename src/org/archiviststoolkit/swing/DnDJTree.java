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

package org.archiviststoolkit.swing;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.io.IOException;

public class DnDJTree extends JTree
    implements DragSourceListener, DropTargetListener, DragGestureListener {

    static DataFlavor localObjectFlavor;

    static {
        try {
            localObjectFlavor =
                    new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }

    static DataFlavor[] supportedFlavors = {localObjectFlavor};

    DragSource dragSource;
    DropTarget dropTarget;
    TreeNode dropTargetNode = null;
	TreeNode draggedNode = null;
	private DefaultMutableTreeNode oldParent = null;
	private DefaultMutableTreeNode newParent = null;
    DefaultMutableTreeNode droppedNode = null;

    public DnDJTree () {
        super();
        setCellRenderer (new DnDTreeCellRenderer());
        setModel (new DefaultTreeModel(new DefaultMutableTreeNode("default")));
        dragSource = new DragSource();
        DragGestureRecognizer dgr =
            dragSource.createDefaultDragGestureRecognizer (this,
                                                           DnDConstants.ACTION_MOVE,
                                                           this);
        dropTarget = new DropTarget (this, this);
    }

    public DnDJTree (DefaultMutableTreeNode root) {
        super();
        setCellRenderer (new DnDTreeCellRenderer());
        setModel (new DefaultTreeModel(root));
        dragSource = new DragSource();
        DragGestureRecognizer dgr =
            dragSource.createDefaultDragGestureRecognizer (this,
                                                           DnDConstants.ACTION_MOVE,
                                                           this);
        dropTarget = new DropTarget (this, this);
    }

    // DragGestureListener
    public void dragGestureRecognized (DragGestureEvent dge) {
        System.out.println ("dragGestureRecognized");
        // find object at this x,y
        Point clickPoint = dge.getDragOrigin();
        TreePath path = getPathForLocation (clickPoint.x, clickPoint.y);
        if (path == null) {
            System.out.println ("not on a node");
            return;
        }
        draggedNode = (TreeNode) path.getLastPathComponent();
        Transferable trans = new RJLTransferable (draggedNode);
        dragSource.startDrag (dge,Cursor.getDefaultCursor(),
                              trans, this);
    }
    // DragSourceListener events
    public void dragDropEnd (DragSourceDropEvent dsde) {
        System.out.println ("dragDropEnd()");
        dropTargetNode = null;
        draggedNode = null;
        repaint();
    }
    public void dragEnter (DragSourceDragEvent dsde) {}
    public void dragExit (DragSourceEvent dse) {}
    public void dragOver (DragSourceDragEvent dsde) {}
    public void dropActionChanged (DragSourceDragEvent dsde) {}
    // DropTargetListener events
    public void dragEnter (DropTargetDragEvent dtde) {
        System.out.println ("dragEnter");
        dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
        System.out.println ("accepted dragEnter");
    }
    public void dragExit (DropTargetEvent dte) {}
    public void dragOver (DropTargetDragEvent dtde) {
        // figure out which cell it's over, no drag to self
        Point dragPoint = dtde.getLocation();
        TreePath path = getPathForLocation (dragPoint.x, dragPoint.y);
        if (path == null)
            dropTargetNode = null;
        else
            dropTargetNode = (TreeNode) path.getLastPathComponent();
        repaint();
    }
    public void drop (DropTargetDropEvent dtde) {
        System.out.println ("drop()!");
        Point dropPoint = dtde.getLocation();
        // int index = locationToIndex (dropPoint);
        TreePath path = getPathForLocation (dropPoint.x, dropPoint.y);
		if (path != null) {
			System.out.println ("drop path is " + path);
			setDroppedNode(null);
			boolean dropped = false;
			try {
				System.out.println ("accepted");
				Object droppedObject =
					dtde.getTransferable().getTransferData(localObjectFlavor);
				/*
							// dropped on self?
							if (droppedObject == draggedNode) {
								System.out.println ("dropped onto self");
								// can't reject, because we've accepted, so no-op
								return;
							}
							*/

				DefaultMutableTreeNode dropNode =
					(DefaultMutableTreeNode) path.getLastPathComponent();
				if (dropNode.equals(droppedObject)) {
					//do nothing the node was just dropped on itself
					dtde.rejectDrop();
					return;
				} else if (droppedObject instanceof MutableTreeNode) {
					// remove from old location
					setDroppedNode((DefaultMutableTreeNode) droppedObject);
					oldParent = (DefaultMutableTreeNode)getDroppedNode().getParent();
					((DefaultTreeModel)getModel()).removeNodeFromParent(getDroppedNode());
					dtde.acceptDrop (DnDConstants.ACTION_MOVE);
				} else {
					dtde.rejectDrop();
					return;
				}
				// insert into spec'd path.  if dropped into a parent
				// make it last child of that parent
				if (dropNode.isLeaf()) {
					DefaultMutableTreeNode parent =
						(DefaultMutableTreeNode) dropNode.getParent();
					int index = parent.getIndex (dropNode);
					((DefaultTreeModel)getModel()).insertNodeInto (getDroppedNode(),
																   parent, index);
				} else {
					((DefaultTreeModel)getModel()).insertNodeInto (getDroppedNode(),
																   dropNode,
																   dropNode.getChildCount());
				}
				newParent = (DefaultMutableTreeNode)getDroppedNode().getParent();
				dropped = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			dtde.dropComplete (dropped);
		}
	}
    public void dropActionChanged (DropTargetDragEvent dtde) {}

    // test 
    public static void main (String[] args) {
        JTree tree = new DnDJTree();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("People");
        DefaultMutableTreeNode set1 = new DefaultMutableTreeNode("Set 1");
        DefaultMutableTreeNode set2 = new DefaultMutableTreeNode("Set 2");
        DefaultMutableTreeNode set3 = new DefaultMutableTreeNode("Set 3");
        set1.add (new DefaultMutableTreeNode ("Chris"));
        set1.add (new DefaultMutableTreeNode ("Kelly"));
        set1.add (new DefaultMutableTreeNode ("Keagan"));
        set2.add (new DefaultMutableTreeNode ("Joshua"));
        set2.add (new DefaultMutableTreeNode ("Kimi"));
        set3.add (new DefaultMutableTreeNode ("Michael"));
        set3.add (new DefaultMutableTreeNode ("Don"));
        set3.add (new DefaultMutableTreeNode ("Daniel"));
        root.add (set1);
        root.add (set2);
        set2.add (set3);
        DefaultTreeModel mod = new DefaultTreeModel (root);
        tree.setModel (mod);
        // expand all
        for (int i=0; i<tree.getRowCount(); i++)
            tree.expandRow (i);
        // show tree
        JScrollPane scroller =
            new JScrollPane (tree,
                            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JFrame frame = new JFrame ("DnD JTree");
        frame.getContentPane().add (scroller);
        frame.pack();
        frame.setVisible(true);
    }

    public DefaultMutableTreeNode getDroppedNode() {
        return droppedNode;
    }

    public void setDroppedNode(DefaultMutableTreeNode droppedNode) {
        this.droppedNode = droppedNode;
    }

	public DefaultMutableTreeNode getOldParent() {
		return oldParent;
	}

	public void setOldParent(DefaultMutableTreeNode oldParent) {
		this.oldParent = oldParent;
	}

	public DefaultMutableTreeNode getNewParent() {
		return newParent;
	}

	public void setNewParent(DefaultMutableTreeNode newParent) {
		this.newParent = newParent;
	}

	class RJLTransferable implements Transferable {
		Object object;
		public RJLTransferable (Object o) {
			object = o;
		}
		public Object getTransferData(DataFlavor df)
			throws UnsupportedFlavorException, IOException {
			if (isDataFlavorSupported (df))
				return object;
			else
				throw new UnsupportedFlavorException(df);
		}
		public boolean isDataFlavorSupported (DataFlavor df) {
			return (df.equals (localObjectFlavor));
		}
		public DataFlavor[] getTransferDataFlavors () {
			return supportedFlavors;
		}
	}

    // custom renderer
    class DnDTreeCellRenderer
        extends DefaultTreeCellRenderer {
        boolean isTargetNode;
        boolean isTargetNodeLeaf;
        boolean isLastItem;
        Insets normalInsets, lastItemInsets;
        int BOTTOM_PAD = 30;
        public DnDTreeCellRenderer() {
            super();
            normalInsets = super.getInsets();
            lastItemInsets =
                new Insets (normalInsets.top,
                            normalInsets.left,
                            normalInsets.bottom + BOTTOM_PAD,
                            normalInsets.right);
        }
        public Component getTreeCellRendererComponent (JTree tree,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean isExpanded,
                                                       boolean isLeaf,
                                                       int row,
                                                       boolean hasFocus) {
            isTargetNode = (value == dropTargetNode);
            isTargetNodeLeaf = (isTargetNode &&
                                ((TreeNode)value).isLeaf());
            // isLastItem = (index == list.getModel().getSize()-1);
            boolean showSelected = isSelected &
                                  (dropTargetNode == null);
            return super.getTreeCellRendererComponent (tree, value,
                                                       isSelected, isExpanded,
                                                       isLeaf, row, hasFocus);

        }

        public void paintComponent (Graphics g) {
            super.paintComponent(g);
            if (isTargetNode) {
                g.setColor(Color.black);
                if (isTargetNodeLeaf) {
                    g.drawLine (0, 0, getSize().width, 0);
                } else {
                    g.drawRect (0, 0, getSize().width-1, getSize().height-1);
                }
            }
        }
    }
}
