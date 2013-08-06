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
 */

package org.archiviststoolkit.editor;

import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.model.*;

import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.util.EventObject;
import java.util.Enumeration;
import java.io.File;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import org.archiviststoolkit.exporter.DCExportHandler;
import org.archiviststoolkit.exporter.METSExportHandler;
import org.archiviststoolkit.exporter.MARCExportHandler;
import org.archiviststoolkit.exporter.MODSExportHandler;
import org.archiviststoolkit.swing.*;
import org.archiviststoolkit.swing.jTreeDnD.sequencedObjectDnDDropListener;
import org.archiviststoolkit.swing.jTreeDnD.DnDUtils;
import org.archiviststoolkit.util.FileUtils;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.exceptions.DeleteException;
import net.antonioshome.swing.treewrapper.TreeWrapper;

public class DigitalObjectTreeViewer extends DomainEditorFields implements MouseListener {

	private static String ABOVE = "above";
	private static String BELOW = "below";
	private METSExportHandler handler;
	private MARCExportHandler marcHandler;
	private MODSExportHandler modsHandler;
	private DCExportHandler dcHandler;


	private DigitalObjects digObj;
	private Resources res;
    private DigitalObjects selectedDigitalObject = null;

    /**
     * Constructor that doesn't take any arguments
     */
    public DigitalObjectTreeViewer() {
        this(null);
    }

	public DigitalObjectTreeViewer(DomainEditor parent) {
		super();
		this.setParentEditor(parent);
		initComponents();
        initMenu();
//		initDbAccess();
		initAccess();
		digitalObjectsEditor = new DigitalObjectFields(parent);
		setContentPanel(digitalObjectsEditor);
	}

	protected void setDisplayToFirstTab() {
		digitalObjectsEditor.setDisplayToFirstTab();
	}

	private void exportMetsButtonActionPerformed(ActionEvent e) {
		final ExportOptionsMETS exportHandler = new ExportOptionsMETS();
		final File selectedFileOrDirectory = FileUtils.chooseFileOrDirectory(1, exportHandler);

		if (selectedFileOrDirectory != null) {
			handler = new METSExportHandler((ExportOptionsMETS) exportHandler);

            if(super.getModel() instanceof ArchDescriptionDigitalInstances) {
			    ArchDescriptionDigitalInstances aDigInst = (ArchDescriptionDigitalInstances) super.getModel();
			    digObj = aDigInst.getDigitalObject();
            } else {
                digObj = (DigitalObjects) super.getModel();
            }

            Thread performer = new Thread(new Runnable() {
				public void run() {
					InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(getParentEditor(), 1000);
					monitor.start("Exporting...");
					try {
						handler.export(selectedFileOrDirectory, digObj, monitor);
					} finally {
						monitor.close();
					}
				}
			}, "ExportingMETS");
			performer.start();
		}
	}

	private void exportMARCActionPerformed(ActionEvent e) {
		final ExportOptionsMARC exportHandler = new ExportOptionsMARC();
		final File selectedFileOrDirectory = FileUtils.chooseFileOrDirectory(1, exportHandler);
		System.out.println("STATUS"+exportHandler.suppressInternalOnly());

		if (selectedFileOrDirectory != null) {
			marcHandler = new MARCExportHandler(exportHandler);

            if(super.getModel() instanceof ArchDescriptionDigitalInstances) {
			    ArchDescriptionDigitalInstances aDigInst = (ArchDescriptionDigitalInstances) super.getModel();
			    digObj = aDigInst.getDigitalObject();
            } else {
                digObj = (DigitalObjects) super.getModel();
            }

			Thread performer = new Thread(new Runnable() {
				public void run() {
					InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(getParentEditor(), 1000);
					monitor.start("Exporting...");
					try {
						marcHandler.exportDO(selectedFileOrDirectory, digObj, monitor);
					} finally {
						monitor.close();
					}
				}
			}, "ExportingMARCXML");
			performer.start();
		}
	}

	private void exportMODSActionPerformed(ActionEvent e) {
		final ExportOptionsMARC exportHandler = new ExportOptionsMARC();
		final File selectedFileOrDirectory = FileUtils.chooseFileOrDirectory(1, exportHandler);

		if (selectedFileOrDirectory != null) {
			modsHandler = new MODSExportHandler(exportHandler);

            if(super.getModel() instanceof ArchDescriptionDigitalInstances) {
			    ArchDescriptionDigitalInstances aDigInst = (ArchDescriptionDigitalInstances) super.getModel();
			    digObj = aDigInst.getDigitalObject();
            } else {
                digObj = (DigitalObjects) super.getModel();
            }

            Thread performer = new Thread(new Runnable() {
				public void run() {
					InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(getParentEditor(), 1000);
					monitor.start("Exporting...");
					try {
						modsHandler.export(selectedFileOrDirectory, digObj, monitor);
					} finally {
						monitor.close();
					}
				}
			}, "ExportMODS");
			performer.start();
		}
	}

	private void exportDCActionPerformed(ActionEvent e) {
		final ExportOptionsMARC exportHandler = new ExportOptionsMARC();
		final File selectedFileOrDirectory = FileUtils.chooseFileOrDirectory(1, exportHandler);

		if (selectedFileOrDirectory != null) {
			dcHandler = new DCExportHandler(exportHandler);

            if(super.getModel() instanceof ArchDescriptionDigitalInstances) {
			    ArchDescriptionDigitalInstances aDigInst = (ArchDescriptionDigitalInstances) super.getModel();
			    digObj = aDigInst.getDigitalObject();
            } else {
                digObj = (DigitalObjects) super.getModel();
            }

			Thread performer = new Thread(new Runnable() {
				public void run() {
					InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(getParentEditor(), 1000);
					monitor.start("Exporting...");
					try {
						dcHandler.export(selectedFileOrDirectory, digObj, monitor);
					} finally {
						monitor.close();
					}
				}
			}, "ExportingDC");
			performer.start();
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        panel3 = new JPanel();
        label2 = new JLabel();
        resourceTitle = new JTextField();
        label3 = new JLabel();
        resourceId = new JTextField();
        splitPane = new JSplitPane();
        digitalObjectTreeScrollPane = new JScrollPane();
        panel2 = new JPanel();
        addChildButton = new JButton();
        addSiblingBelowButton = new JButton();
        deleteDigitalObjectButton = new JButton();
        exportMetsButton = new JButton();
        exportMARC = new JButton();
        exportMODS = new JButton();
        exportDC = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setPreferredSize(new Dimension(1000, 600));
        setOpaque(false);
        setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        setLayout(new FormLayout(
            "default:grow",
            "default, default, fill:default"));

        //======== panel3 ========
        {
            panel3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            panel3.setBackground(new Color(200, 205, 232));
            panel3.setVisible(false);
            panel3.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC
                },
                RowSpec.decodeSpecs("default")));

            //---- label2 ----
            label2.setText("Digtal Object Title: ");
            label2.setForeground(new Color(0, 0, 102));
            label2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            panel3.add(label2, cc.xy(1, 1));

            //---- resourceTitle ----
            resourceTitle.setEditable(false);
            resourceTitle.setOpaque(false);
            resourceTitle.setBorder(null);
            panel3.add(resourceTitle, cc.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

            //---- label3 ----
            label3.setText("Resource ID: ");
            label3.setForeground(new Color(0, 0, 102));
            label3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            panel3.add(label3, cc.xy(5, 1));

            //---- resourceId ----
            resourceId.setEditable(false);
            resourceId.setOpaque(false);
            resourceId.setBorder(null);
            panel3.add(resourceId, cc.xywh(7, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        }
        add(panel3, cc.xy(1, 1));

        //======== splitPane ========
        {
            splitPane.setDividerLocation(300);
            splitPane.setLastDividerLocation(300);
            splitPane.setPreferredSize(new Dimension(1000, 600));
            splitPane.setOpaque(false);
            splitPane.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

            //======== digitalObjectTreeScrollPane ========
            {
                digitalObjectTreeScrollPane.setPreferredSize(new Dimension(250, 384));
                digitalObjectTreeScrollPane.setMinimumSize(new Dimension(250, 384));
                digitalObjectTreeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                digitalObjectTreeScrollPane.setFocusable(false);
                digitalObjectTreeScrollPane.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                digitalObjectTreeScrollPane.setAutoscrolls(true);
            }
            splitPane.setLeftComponent(digitalObjectTreeScrollPane);
        }
        add(splitPane, cc.xy(1, 2));

        //======== panel2 ========
        {
            panel2.setOpaque(false);
            panel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            panel2.setLayout(new FormLayout(
                new ColumnSpec[] {
                    new ColumnSpec(ColumnSpec.CENTER, Sizes.DEFAULT, FormSpec.NO_GROW),
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.UNRELATED_GAP_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC
                },
                RowSpec.decodeSpecs("default")));

            //---- addChildButton ----
            addChildButton.setOpaque(false);
            addChildButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            addChildButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/addChild.jpg")));
            addChildButton.setHorizontalTextPosition(SwingConstants.RIGHT);
            addChildButton.setToolTipText("Add Child");
            addChildButton.setText("<html><c>Add<br>Child</c></html>");
            addChildButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addChildActionPerformed(e);
                }
            });
            panel2.add(addChildButton, cc.xy(1, 1));

            //---- addSiblingBelowButton ----
            addSiblingBelowButton.setOpaque(false);
            addSiblingBelowButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            addSiblingBelowButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/addSibling.png")));
            addSiblingBelowButton.setToolTipText("Add Sibling Above");
            addSiblingBelowButton.setText("<html>Add<br>Sibling</html>");
            addSiblingBelowButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addSiblingBelowActionPerformed(e);
                }
            });
            panel2.add(addSiblingBelowButton, cc.xy(3, 1));

            //---- deleteDigitalObjectButton ----
            deleteDigitalObjectButton.setOpaque(false);
            deleteDigitalObjectButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            deleteDigitalObjectButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/delete.jpg")));
            deleteDigitalObjectButton.setToolTipText("Delete Digital Object");
            deleteDigitalObjectButton.setText("<html>Delete<br>Node</html>");
            deleteDigitalObjectButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    deleteDigitalObjectButtonActionPerformed(e);
                }
            });
            panel2.add(deleteDigitalObjectButton, cc.xy(5, 1));

            //---- exportMetsButton ----
            exportMetsButton.setOpaque(false);
            exportMetsButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            exportMetsButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/exportMETS.jpg")));
            exportMetsButton.setToolTipText("Export METS");
            exportMetsButton.setText("<html>Export<br>METS</html>");
            exportMetsButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    exportMetsButtonActionPerformed(e);
                }
            });
            panel2.add(exportMetsButton, cc.xywh(9, 1, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

            //---- exportMARC ----
            exportMARC.setOpaque(false);
            exportMARC.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            exportMARC.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/exportMARC.jpg")));
            exportMARC.setToolTipText("Export MARC");
            exportMARC.setText("<html>Export<br>MARC</html>");
            exportMARC.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    exportMARCActionPerformed(e);
                }
            });
            panel2.add(exportMARC, cc.xywh(11, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

            //---- exportMODS ----
            exportMODS.setOpaque(false);
            exportMODS.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            exportMODS.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/exportMODS.png")));
            exportMODS.setToolTipText("Export MODS");
            exportMODS.setText("<html>Export<br>MODS</html>");
            exportMODS.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    exportMODSActionPerformed(e);
                }
            });
            panel2.add(exportMODS, cc.xywh(13, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

            //---- exportDC ----
            exportDC.setOpaque(false);
            exportDC.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            exportDC.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/exportDC.png")));
            exportDC.setToolTipText("Export Dublin Core");
            exportDC.setText("<html>Export<br>Dublin Core</html>");
            exportDC.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    exportDCActionPerformed(e);
                }
            });
            panel2.add(exportDC, cc.xywh(15, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
        }
        add(panel2, cc.xy(1, 3));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	private void initMenu() {

		pm = new JPopupMenu();
		//---- Add Child ----
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText("Add Child");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addChildActionPerformed(e);
			}
		});
		pm.add(menuItem);

		//---- Add Child ----
		menuItem = new JMenuItem();
		menuItem.setText("Add Sibling Above");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addSiblingAboveActionPerformed(e);
			}
		});
		pm.add(menuItem);

		//---- Add Child ----
		menuItem = new JMenuItem();
		menuItem.setText("Add Sibling Below");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addSiblingBelowActionPerformed(e);
			}
		});
		pm.add(menuItem);

		//---- Add Child ----
		menuItem = new JMenuItem();
		menuItem.setText("Delete Digital Object");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteDigitalObjectButtonActionPerformed(e);
			}
		});
		pm.add(menuItem);

		pm.addMouseListener(this);

	}

	private void digitalObjectTreeValueChanged(TreeSelectionEvent e) {
		if (initialSet) {
			initialSet = false;
			setComponetButtonEnabled(false);
		} else if (suppressTreeValueChangeAction) {
			suppressTreeValueChangeAction = false;
		} else {

			if (commitChangesToCurrentDigitalObject(e)) {
				digitalObjectTree.requestFocus();
				treeModel.nodeChanged(selectedTreeNode);
				TreePath path = e.getPath();
				updateSelectedObjects(path);
			} else {
				suppressTreeValueChangeAction = true;
				digitalObjectTree.setSelectionPath(new TreePath(selectedTreeNode.getPath()));
			}
		}
	}

	private void updateSelectedObjects(TreePath path) {
		selectedTreeNode = (DefaultMutableTreeNode) path.getLastPathComponent();
		currentDigitalObject = (DigitalObjects) selectedTreeNode.getUserObject();
		if (currentDigitalObject.getParent() == null) {
			//we are at the root
			setComponetButtonEnabled(false);
		} else {
			setComponetButtonEnabled(true);
		}
		digitalObjectsEditor.setModel(currentDigitalObject, null);
	}

	protected boolean commitChangesToCurrentDigitalObject(EventObject event) {
		digitalObjectsEditor.commitChanges();
		if (currentDigitalObject != null && currentDigitalObject.validateAndDisplayDialog(event)) {
			return true;
		} else {
			return false;
		}
	}

//	protected void commitChangesToCurrentRecord() {
//		//if there is a current resources component then save it
//            if (currentDigitalObject.getIdentifier() == null) {
//                try {
//                    digitalObjectAccess.add(currentDigitalObject);
//                } catch (PersistenceException e1) {
//					new ErrorDialog("There is a problem saving changes to a digital object node.",
//							StringHelper.getStackTrace(e1)).showDialog();
//                }
//            } else {
//                try {
//                    digitalObjectsEditor.commitChanges();
//                    digitalObjectAccess.updateLongSession(currentDigitalObject);
//                } catch (PersistenceException e1) {
//					new ErrorDialog("There is a problem saving changes to a digital object node.",
//							StringHelper.getStackTrace(e1)).showDialog();
//                }
//            }
// 	}

//	private void setNewNodeOrder(DefaultMutableTreeNode newTreeNode) {
//		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) newTreeNode.getParent();
//		DefaultMutableTreeNode previous = (DefaultMutableTreeNode) parent.getChildBefore(newTreeNode);
//		DefaultMutableTreeNode next = (DefaultMutableTreeNode) parent.getChildAfter(newTreeNode);
//		DnDResourceTree.assignNewOrderDO(newTreeNode, parent, previous, next);
//	}

	private void addSibling(String position) {
		TreePath selectedPath = digitalObjectTree.getSelectionPath();
		if (selectedPath == null) {
			JOptionPane.showMessageDialog(this, "You must select a node first");
		} else {
			DigitalObjects newDigitalObject = new DigitalObjects();
			DigitalObjects parent = currentDigitalObject.getParent();
			newDigitalObject.setParent(parent);
            newDigitalObject.setRepository(parent.getRepository());
			DefaultMutableTreeNode newTreeNode =
					this.addSiblingAbove(newDigitalObject, position);
			setNewNodeOrder(newTreeNode);
			parent.addChild(newDigitalObject);
            TreePath pathToNewNode = new TreePath(newTreeNode.getPath());
			digitalObjectTree.setSelectionPath(pathToNewNode);
		}

	}

	private void setNewNodeOrder(DefaultMutableTreeNode newTreeNode) {
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) newTreeNode.getParent();
		DefaultMutableTreeNode previous = (DefaultMutableTreeNode) parentNode.getChildBefore(newTreeNode);
		DnDUtils.assignNewOrder(newTreeNode, parentNode, previous);
	}

	private void addChildActionPerformed(ActionEvent e) {
		TreePath selectedPath = digitalObjectTree.getSelectionPath();
		if (selectedPath == null) {
			JOptionPane.showMessageDialog(this, "You must select a node first");
		} else {
			DigitalObjects newDigitalObject = new DigitalObjects();
			newDigitalObject.setParent(currentDigitalObject);
            newDigitalObject.setRepository(currentDigitalObject.getRepository());
			DefaultMutableTreeNode newTreeNode = this.addObject(newDigitalObject);
			setNewNodeOrder(newTreeNode);
			currentDigitalObject.addChild(newDigitalObject);
            TreePath pathToNewNode = new TreePath(newTreeNode.getPath());
			digitalObjectTree.setSelectionPath(pathToNewNode);
		}
	}

	private void addSiblingAboveActionPerformed(ActionEvent e) {
		addSibling(DigitalObjectTreeViewer.ABOVE);
	}

	private void addSiblingBelowActionPerformed(ActionEvent e) {
		addSibling(DigitalObjectTreeViewer.BELOW);
	}

	private void deleteDigitalObjectButtonActionPerformed(ActionEvent e) {
		TreePath selectedPath = digitalObjectTree.getSelectionPath();
		if (selectedPath == null) {
			JOptionPane.showMessageDialog(this, "You must select a node first");
		} else {
			if (JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete the node?",
                    "Delete node", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {

                currentDigitalObject.getParent().removeChild(currentDigitalObject);
				currentDigitalObject.setParent(null);

                // remove the digital object from database now
                try {
                    DigitalObjectDAO access = new DigitalObjectDAO();
                    access.getLongSession(); // get the long session incase its not already created
                    access.deleteLongSession(currentDigitalObject, false);
                } catch (DeleteException e1) {
                    new ErrorDialog(getParentEditor(), "Error removing child digital record", e1).showDialog();
                    e1.printStackTrace();
                }

                currentDigitalObject = null;

                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
				suppressTreeValueChangeAction = true;
				treeModel.removeNodeFromParent(selectedNode);
				suppressTreeValueChangeAction = true;
				this.digitalObjectTree.setSelectionRow(0);
				updateSelectedObjects(digitalObjectTree.getSelectionPath());

                // add digital object to the delete list

                // set the record dirty do that the user knows to save the record
                ApplicationFrame.getInstance().setRecordDirty();
			}
		}
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel panel3;
    private JLabel label2;
    private JTextField resourceTitle;
    private JLabel label3;
    private JTextField resourceId;
    private JSplitPane splitPane;
    private JScrollPane digitalObjectTreeScrollPane;
    private JPanel panel2;
    private JButton addChildButton;
    private JButton addSiblingBelowButton;
    private JButton deleteDigitalObjectButton;
    private JButton exportMetsButton;
    private JButton exportMARC;
    private JButton exportMODS;
    private JButton exportDC;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	/**
	 * The status of the editor.
	 */
	protected int status = 0;
//	private DomainAccessObject digitalObjectAccess;
	private DigitalObjectFields digitalObjectsEditor;
	private DigitalObjects currentDigitalObject = null;
//	private GeneralTreeNode currentDigitalObjectNode;
	DefaultMutableTreeNode selectedTreeNode;
	private ArchDescriptionDigitalInstances digitalInstanceModel;
	private JTree digitalObjectTree;
	private DefaultMutableTreeNode root;
	private DefaultTreeModel treeModel;
	private Boolean initialSet = false;
	private Boolean suppressTreeValueChangeAction = false;
	/**
	 * A popup menu used to manipulate entries in the worksurface.
	 */
	private JPopupMenu pm = null;

	public final void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);

        if(model instanceof DigitalObjects) {
            digitalInstanceModel = new ArchDescriptionDigitalInstances((DigitalObjects)model);
        } else { // must be of archdescription digital instances
		    digitalInstanceModel = (ArchDescriptionDigitalInstances) model;
        }

        // create the JTree now
        digitalInstanceModel.clearPlainDigitalObjectJTree();
        setDigitalObjectTree(digitalInstanceModel.getPlainDigitalObjectJTree());

		digitalObjectsEditor.setModel(currentDigitalObject, null);
	}

	public Component getInitialFocusComponent() {
		return null;
	}

	public void setResourceInfo(Resources resource) {
        panel3.setVisible(true); // set the panel visible now
		resourceTitle.setText(resource.getTitle());
		resourceId.setText(resource.getResourceIdentifier());
	}


//	private void initDbAccess() {
//		try {
//			digitalObjectAccess = DomainAccessObjectFactory.getInstance().getDomainAccessObject(DigitalObjects.class);
//		} catch (PersistenceException e) {
//			new ErrorDialog("", e).showDialog();
//		}
//	}

	public void setContentPanel(JPanel newContentPanel) {
		int dividerLocation = splitPane.getDividerLocation();
		splitPane.setRightComponent(newContentPanel);
		splitPane.setDividerLocation(dividerLocation);
	}

	public JTree getDigitalObjectTree() {
		return digitalObjectTree;
	}

	public void setDigitalObjectTree(JTree treeFromModel) {
        digitalObjectTree = treeFromModel;
        digitalObjectTree.addMouseListener(this);
        treeModel = (DefaultTreeModel) digitalObjectTree.getModel();
        root = (DefaultMutableTreeNode) treeModel.getRoot();

        digitalObjectTreeScrollPane.setViewportView(digitalObjectTree);

        // set the selected tree node based on the selected digital object variable
        if(getSelectedDigitalObect() == null) {
            selectedTreeNode =
                (DefaultMutableTreeNode) digitalObjectTree.getPathForRow(0).getLastPathComponent();

            digitalObjectTree.setSelectionRow(0);
            setComponetButtonEnabled(false);
            initialSet = false;
            suppressTreeValueChangeAction = false;
        } else {
            TreePath pathFromSearch = getTreePathFromDigitalObject(getSelectedDigitalObect());
			selectedTreeNode = (DefaultMutableTreeNode) pathFromSearch.getLastPathComponent();
			digitalObjectTree.setSelectionPath(pathFromSearch);
			digitalObjectTree.scrollPathToVisible(pathFromSearch);
            setComponetButtonEnabled(true);
            initialSet = false;
            suppressTreeValueChangeAction = false;
        }

        // set the current digital object
        currentDigitalObject = (DigitalObjects) selectedTreeNode.getUserObject();

        // tree wrapper code
        TreeWrapper wrapper = new TreeWrapper(treeFromModel);
        wrapper.addTreeTreeDnDListener(new sequencedObjectDnDDropListener());

        System.out.println(digitalObjectTree.getSelectionRows());

        // now add the tree tree change listener to prevent any bug that may result
        // if tree change events are being called before the tree is actually displayed
        this.digitalObjectTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                digitalObjectTreeValueChanged(e);
            }
        });
    }

    private TreePath getTreePathFromDigitalObject(DigitalObjects digitalObject) {
        return getTreePathFromDigitalObject(digitalObject, root);
    }

    /**
     * Method to get the path in the JTree based on a digital object
     *
     * @param digitalObject The digital object to search for
     * @return The path to the selected digital object
     */
    private TreePath getTreePathFromDigitalObject(DigitalObjects digitalObject, DefaultMutableTreeNode node) {
        DefaultMutableTreeNode childNode;
		DigitalObjects childDigitalObjectTreeNode;

		//first go through the children to look for the hit
		Enumeration children = node.children();
		while (children.hasMoreElements()) {
			childNode = (DefaultMutableTreeNode)children.nextElement();
			childDigitalObjectTreeNode = (DigitalObjects)childNode.getUserObject();
			if (childDigitalObjectTreeNode != null && childDigitalObjectTreeNode.equals(digitalObject)) {
				return new TreePath(childNode.getPath());
			}
		}

        //if we get here then look through the children
		children = node.children();
		TreePath treePath;
		while (children.hasMoreElements()) {
			childNode = (DefaultMutableTreeNode)children.nextElement();
			childDigitalObjectTreeNode = (DigitalObjects)childNode.getUserObject();
			treePath = getTreePathFromDigitalObject(digitalObject, childNode);
			if (treePath != null) {
				return treePath;
			}
		}

        return null;
	}

	/**
	 * Add child to the currently selected node.
	 * @return - return the added node
	 * @param child - the object to be stored in the added node
	 */
	public DefaultMutableTreeNode addObject(Object child) {
		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = digitalObjectTree.getSelectionPath();

		if (parentPath == null) {
			parentNode = root;
		} else {
			parentNode = (DefaultMutableTreeNode)
					(parentPath.getLastPathComponent());
		}

		return addObject(parentNode, child, true);
	}

	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
											Object child) {
		return addObject(parent, child, false);
	}

	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
											Object child,
											boolean shouldBeVisible) {
		DefaultMutableTreeNode childNode =
				new DefaultMutableTreeNode(child);

		if (parent == null) {
			parent = root;
		}

		treeModel.insertNodeInto(childNode, parent,
				parent.getChildCount());

		//Make sure the user can see the lovely new node.
		if (shouldBeVisible) {
			digitalObjectTree.scrollPathToVisible(new TreePath(childNode.getPath()));
		}
		return childNode;
	}

	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
											Object child,
											int position,
											boolean shouldBeVisible) {
		DefaultMutableTreeNode childNode =
				new DefaultMutableTreeNode(child);

		if (parent == null) {
			parent = root;
		}

		treeModel.insertNodeInto(childNode, parent, position);

		//Make sure the user can see the lovely new node.
		if (shouldBeVisible) {
			digitalObjectTree.scrollPathToVisible(new TreePath(childNode.getPath()));
		}
		return childNode;
	}

	private DefaultMutableTreeNode addSiblingAbove(Object child, String position) {
		TreePath siblingPath = digitalObjectTree.getSelectionPath();
		DefaultMutableTreeNode siblingNode = (DefaultMutableTreeNode) siblingPath.getLastPathComponent();
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) siblingNode.getParent();
		if (position.equalsIgnoreCase(DigitalObjectTreeViewer.ABOVE)) {
			return addObject(parentNode, child, parentNode.getIndex(siblingNode), true);
		} else {
			return addObject(parentNode, child, parentNode.getIndex(siblingNode) + 1, true);
		}
	}

	private void setComponetButtonEnabled(Boolean enabled) {
//		this.addSiblingAboveButton.setEnabled(enabled);
		this.addSiblingBelowButton.setEnabled(enabled);
		this.deleteDigitalObjectButton.setEnabled(enabled);
	}

	public void mouseClicked(MouseEvent mouseEvent) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public void mousePressed(MouseEvent mouseEvent) {
		if (mouseEvent.isPopupTrigger()) {
			//pm.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
		}
	}

	public void mouseReleased(MouseEvent mouseEvent) {
		if (mouseEvent.isPopupTrigger()) {
			//pm.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
		}
	}

	public void mouseEntered(MouseEvent mouseEvent) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public void mouseExited(MouseEvent mouseEvent) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	protected void initAccess() {
		//if user is not at least advanced data entry set the record to read only
		if (!Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_ADVANCED_DATA_ENTRY)) {
			addChildButton.setVisible(false);
			deleteDigitalObjectButton.setVisible(false);
//			addSiblingAboveButton.setVisible(false);
			addSiblingBelowButton.setVisible(false);
			exportMetsButton.setVisible(false);
		}
	}

    /**
     * Method to set the select digital object. If null then the selected will just be
     * the parent. This is used when selection is done through the work surface
     *
     * @param selectedDigitalObject The selected Digital Object
     */
    public void setSelectedDigitalObject(DigitalObjects selectedDigitalObject) {
        this.selectedDigitalObject = selectedDigitalObject;
    }

    /**
     * Method to return the digital object that was selected from the main work surface
     * @return
     */
    public DigitalObjects getSelectedDigitalObect() {
        return selectedDigitalObject;
    }

    /**
     * Method to update the JTree when the saved button is pressed
     */
    public void updateJTree() {
        digitalObjectTree.updateUI();
    }
}
