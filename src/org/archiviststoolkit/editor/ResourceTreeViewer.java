/**
 * Archivists' Toolkit(TM) Copyright � 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
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
 * Created by JFormDesigner on Wed Oct 11 11:06:00 EDT 2006
 */

package org.archiviststoolkit.editor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;
import net.antonioshome.swing.treewrapper.TreeWrapper;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.plugin.ATPluginFactory;
import org.archiviststoolkit.plugin.ATPlugin;
import org.archiviststoolkit.plugin.RDEPlugin;
import org.archiviststoolkit.dialog.ATFileChooser;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.dialog.LocationAssignmentResources;
import org.archiviststoolkit.dialog.ResourceLookup;
import org.archiviststoolkit.editor.rde.RapidResourceComponentDataEntry2;
import org.archiviststoolkit.editor.rde.RdePanelContainer;
import org.archiviststoolkit.exceptions.InvalidTreeNode;
import org.archiviststoolkit.exceptions.MergeException;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.exceptions.RDEPopulateException;
import org.archiviststoolkit.exporter.EADExportHandler;
import org.archiviststoolkit.exporter.MARCExportHandler;
import org.archiviststoolkit.model.ContainerGroup;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.model.ResourcesCommon;
import org.archiviststoolkit.model.ResourcesComponents;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.swing.*;
import org.archiviststoolkit.swing.jTreeDnD.DnDUtils;
import org.archiviststoolkit.swing.jTreeDnD.sequencedObjectDnDDropListener;
import org.archiviststoolkit.util.FileUtils;
import org.archiviststoolkit.util.MyTimer;
import org.archiviststoolkit.util.RDEFactory;
import org.archiviststoolkit.util.StringHelper;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.EventObject;
import java.util.HashMap;
import java.util.ArrayList;

public class ResourceTreeViewer extends DomainEditorFields implements MouseListener {
    private ArrayList<ATPlugin> plugins = null; // stores any RDE plugins
    private HashMap<String, RDEPlugin> rdePlugins = null; // stores any panels from the RDE plugins
	private static String ABOVE = "above";
	private static String BELOW = "below";

	private LocationAssignmentResources locationDialogResources;
	//    private ATFileChooser filechooser;
	private EADExportHandler handler;
	private MARCExportHandler marcHandler;
	private boolean debug = false;

  /**
   * caches rdeScreens so that sticky values persist as long has editor remain open
   */
  private HashMap<String, RapidResourceComponentDataEntry2> rdeDialogs = new HashMap<String, RapidResourceComponentDataEntry2>();

	private Resources res;

	public ResourceTreeViewer() {
		super();
		initComponents();
//        initAccess();
		initMenu();
		resourcePanel = new ResourceFields();
		resourceComponentsPanel = new ResourceComponentsFields();

        // load any rapid data entry screen plugins
        initPlugins();
	}

	private void addChildActionPerformed(ActionEvent e) {
		addChildActionPerformed(null, true, e);
	}

	private void addChildActionPerformed(ResourcesComponents component, Boolean setSelectionToNewNode, ActionEvent e) {
		TreePath selectedPath = resourceTree.getSelectionPath();
		if (selectedPath == null) {
			JOptionPane.showMessageDialog(this, "You must select a node first");
		} else {
			try {
				if (commitChangesToCurrentResourceComponent(e)) {
					if (debug) {
						System.out.println("Child added: " + currentResourcesComponent);
					}
					ResourcesComponents newComponent = createResourceTreeNode(true, component);

					DefaultMutableTreeNode newTreeNode = this.addObject(newComponent);
					TreePath pathToNewNode = new TreePath(newTreeNode.getPath());
					setNewNodeOrder(newTreeNode);

					if (currentResourcesComponent == null) {
						resourceModel.addChild(newComponent);
						resourceModel.resortComponents();
					} else {
						currentResourcesComponent.addChild(newComponent);
						currentResourcesComponent.resortComponents();
					}

					if (setSelectionToNewNode) {
						resourceTree.setSelectionPath(pathToNewNode);
					}

					if (newComponent.getResource() == null && newComponent.getResourceComponentParent() == null) {
						throw new InvalidTreeNode("Component is not properly linked");
					}
				}
			} catch (InvalidTreeNode invalidTreeNode) {
				new ErrorDialog(getParentEditor(), "Error adding child component", invalidTreeNode).showDialog();
			}
		}
	}

//    private void addSiblingAboveActionPerformed(ActionEvent e) {
//        addSibling(ResourceTreeViewer.ABOVE, e);
//    }

	private void addSiblingBelowActionPerformed(ActionEvent e) {
		addSibling(ResourceTreeViewer.BELOW, e);
	}

	private void deleteComponentActionPerformed(ActionEvent e) {
		TreePath selectedPath = resourceTree.getSelectionPath();
		if (selectedPath == null) {
			JOptionPane.showMessageDialog(this, "You must select a node first");
		} else {
			if (JOptionPane.showConfirmDialog(this,
					"Are you sure you want to delete the component?",
					"Delete component", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
				suppressTreeValueChangeAction = true;
				treeModel.removeNodeFromParent(selectedNode);
				if (debug) {
					System.out.println("Component removed: " + currentResourcesComponent);
				}
				try {
					if (currentResourcesComponent.getResource() != null) {
						currentResourcesComponent.getResource().removeRelatedObject(currentResourcesComponent);
						currentResourcesComponent.setResource(null);
					} else if (currentResourcesComponent.getResourceComponentParent() != null) {
						currentResourcesComponent.getResourceComponentParent().removeResourcesComponents(currentResourcesComponent);
						currentResourcesComponent.setResourceComponentParent(null);
					} else {
						try {
							throw new InvalidTreeNode("Component is not properly linked");
						} catch (InvalidTreeNode invalidTreeNode) {
							new ErrorDialog("Problem deleting node", invalidTreeNode).showDialog();
						}

					}
					//set the record to dirty
					ApplicationFrame.getInstance().setRecordDirty();
				} catch (ObjectNotRemovedException e1) {
					new ErrorDialog("Problem deleting node", e1).showDialog();
				}
				((Resources) this.getModel()).addComponentToDelete(currentResourcesComponent);
				currentResourcesComponent = null;
//            suppressTreeValueChangeAction = true;
				this.resourceTree.setSelectionRow(0);
			}
		}
	}

	private void manageLocationsButtonActionPerformed(ActionEvent e) {
		if (resourceModel.getIdentifier() == null) {
			//we have a new record
			String message = "This is a new resource and needs to be saved to manage locations.\nPlease save the record and try again";
			JOptionPane.showMessageDialog(this.getParentEditor(), message);
		} else {
			if (commitChangesToCurrentResourceComponent(e)) {
				locationDialogResources = new LocationAssignmentResources(getParentEditor());
				Thread performer = new Thread(new Runnable() {
					public void run() {
						InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000);
						monitor.start("Gathering Containers...");
						try {
							locationDialogResources.assignContainerListValues(resourceModel.gatherContainers(monitor));
						} finally {
							monitor.close();
						}
						finishManageLocationsButtonActionPerformed();
					}
				}, "ManageLocations");
				performer.start();
			}
		}
	}

	private void finishManageLocationsButtonActionPerformed() {
		locationDialogResources.showDialog();
	}

	private void exportEADActionPerformed(ActionEvent e) {
		if (commitChangesToCurrentResourceComponent(e)) {
			final ExportOptionsEAD exportHandler = new ExportOptionsEAD();
			final File selectedFileOrDirectory = FileUtils.chooseFileOrDirectory(1, exportHandler);

			if (selectedFileOrDirectory != null) {
				handler = new EADExportHandler(exportHandler);
				res = (Resources) this.getModel();
				Thread performer = new Thread(new Runnable() {
					public void run() {
						InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000);
						monitor.start("Exporting EAD...");
						try {
							handler.export(selectedFileOrDirectory, res, monitor);
						} finally {
							monitor.close();
						}
					}
				}, "ExportEAD");
				performer.start();
			}
		}
	}

	private void exportMARCActionPerformed(ActionEvent e) {
		if (commitChangesToCurrentResourceComponent(e)) {
			final ExportOptionsMARC exportHandler = new ExportOptionsMARC();
			final File selectedFileOrDirectory = FileUtils.chooseFileOrDirectory(1, exportHandler);
			System.out.println("STATUS" + exportHandler.suppressInternalOnly());

			if (selectedFileOrDirectory != null) {
				marcHandler = new MARCExportHandler(exportHandler);
				res = (Resources) this.getModel();
				Thread performer = new Thread(new Runnable() {
					public void run() {
						InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000);
						monitor.start("Exporting MARCXML...");
						try {
							marcHandler.export(selectedFileOrDirectory, res, monitor);
						}
						catch (Exception e) {
							monitor.close();
							new ErrorDialog("", StringHelper.getStackTrace(e)).showDialog();
						} finally {
							monitor.close();
						}
					}
				}, "ExportMARCXML");
				performer.start();
			}
		}
	}

	private void rapidDataentryScreensActionPerformed(ActionEvent e) {
        if (rapidDataentryScreens.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "You must select an RDE first");
        } else {
            if (commitChangesToCurrentResourceComponent(e)) {
                TreePath selectedPath = resourceTree.getSelectionPath();
                if (selectedPath == null) {
                    JOptionPane.showMessageDialog(this, "You must select a node first");
                } else {
                    String title = (String) rapidDataentryScreens.getSelectedItem();

                    RapidResourceComponentDataEntry2 dialog;

                    if (rdeDialogs.containsKey(title)) { // get cached dialog if it exist
                        dialog = rdeDialogs.get(title);
                    } else { // create new dialog and catch it
                        RdePanelContainer container = RDEFactory.getInstance().getContainer(title);

                        // check to see if to load rde dialog or rde plugin
                        if (container != null) {
                        dialog = new RapidResourceComponentDataEntry2(this.getParentEditor(), resourceModel, container);
                        rdeDialogs.put(title, dialog);
                        } else {
                            selectRapidDataEntryPlugin(title);
                            return;
                        }
                    }

                    Boolean done = false;
                    int returnStatus;

                    while (!done) {
                        returnStatus = dialog.showDialog();
                        if (returnStatus == JOptionPane.OK_OPTION) {
                            done = true;
                            try {
                                addChildActionPerformed(dialog.getComponentRecord(), false, e);

								//set the record to dirty if not auto save
                                if(dialog.autoSaveRecord()) {
                                    saveRecord(e);
                                } else {
								    ApplicationFrame.getInstance().setRecordDirty();
                                }
                            } catch (RDEPopulateException e1) {
                                new ErrorDialog("Error populating component data", e1).showDialog();
                                done = true;
                            }
                        } else if (returnStatus == StandardEditor.OK_AND_ANOTHER_OPTION) {
                            try {
                                addChildActionPerformed(dialog.getComponentRecord(), false, e);

                                //set the record to dirty if not auto save
								if(dialog.autoSaveRecord()) {
                                    saveRecord(e);
                                } else {
                                    ApplicationFrame.getInstance().setRecordDirty();
                                }
                            } catch (RDEPopulateException e1) {
                                new ErrorDialog("Error populating component data", e1).showDialog();
                                done = true;
                            }
                        } else {
                            done = true;
                        }
                    }
                }
            }
            rapidDataentryScreens.setSelectedIndex(0);
        }
	}

    /**
     * Method to save the record just has if the save button is pressed.
     * It's used when using the RDE screen 
     *
     * @param e
     */
    private void saveRecord(ActionEvent e) {
        getParentEditor().saveRecord(e);
    }

	private void exportContainerLabelsActionPerformed(ActionEvent e) {
		if (commitChangesToCurrentResourceComponent(e)) {
			final ATFileChooser filechooser = new ATFileChooser(new SimpleFileFilter(".txt"));

			if (filechooser.showSaveDialog(getParentEditor()) == JFileChooser.APPROVE_OPTION) {
				Thread performer = new Thread(new Runnable() {
					public void run() {
						Writer out = null;
						InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000);
						monitor.start("Exporting Container List...");
						try {
							FileOutputStream fos = new FileOutputStream(filechooser.getSelectedFile());
							OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
							out = new BufferedWriter(osw);
							out.write("Repository Name\tResource Title\tResource Identifier\tContainer Label\n");
							String prefix = resourceModel.getRepositoryName() + "\t" + resourceModel.getTitle() + "\t" + resourceModel.getResourceIdentifier() + "\t";
							for (ContainerGroup containerGroup : resourceModel.gatherContainers(monitor)) {
								out.write(prefix + containerGroup.getTopLevelContainerName() + "\n");
							}
							out.close();
						} catch (FileNotFoundException e1) {
							new ErrorDialog(getParentEditor(), "Error exporting container labels", e1).showDialog();
						} catch (UnsupportedEncodingException e1) {
							new ErrorDialog(getParentEditor(), "Error exporting container labels", e1).showDialog();
						} catch (IOException e1) {
							new ErrorDialog(getParentEditor(), "Error exporting container labels", e1).showDialog();
						} finally {
							monitor.close();
						}
					}
				}, "ExportContainerList");
				performer.start();
			}
		}
	}

	private void transferComponentsButtonActionPerformed(ActionEvent e) {
		TreePath selectedPath = resourceTree.getSelectionPath();
		if (selectedPath == null) {
			JOptionPane.showMessageDialog(this, "You must select a node first");
		} else {
//			try {
			ResourceLookup resourcePicker = new ResourceLookup(getParentEditor(), this);
			if (resourcePicker.showDialog(this) == javax.swing.JOptionPane.OK_OPTION) {
				final Resources resourceToTransferFrom = resourcePicker.getSelectedResource();
				String message = "Are you sure you want to transfer all components from " +
						resourceToTransferFrom + "? This can't be undone and the current resource record will be saved.";
				if (JOptionPane.showConfirmDialog(this, message, "Transfer", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {


					if (commitChangesToCurrentResourceComponent(e)) {
						if (debug) {
							System.out.println("transfer components: " + currentResourcesComponent);
						}

						Thread performer = new Thread(new Runnable() {
							public void run() {
								InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000);
								monitor.start("Transferring components...");
								try {
									ResourcesDAO dao = (ResourcesDAO) DomainAccessObjectFactory.getInstance().getDomainAccessObject(Resources.class);
									if (currentResourcesComponent == null) {
										dao.transfer(resourceToTransferFrom, resourceModel, resourceModel, monitor);
									} else {
										dao.transfer(resourceToTransferFrom, resourceModel, currentResourcesComponent, monitor);
									}
									resourceModel.clearPlainJTree();
									setResourceTree(resourceModel.getPlainResourceJtree(monitor));
								} catch (MergeException e1) {
									monitor.close();
									new ErrorDialog("error transferring components", e1).showDialog();
								} catch (PersistenceException e1) {
									monitor.close();
									new ErrorDialog("error transferring components", e1).showDialog();
								} finally {
									monitor.close();
								}
							}
						}, "Transfer components");
						performer.start();
					}


				}
			}
		}
	}


	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		panel6 = new JPanel();
		label2 = new JLabel();
		resourceTitle = new JTextField();
		label3 = new JLabel();
		resourceIdentifier = new JTextField();
		splitPane = new JSplitPane();
		scrollPane = new JScrollPane();
		panel2 = new JPanel();
		addChildButton = new JButton();
		addSiblingBelowButton = new JButton();
		rapidDataentryScreens = ATBasicComponentFactory.createUnboundComboBox(RDEFactory.getInstance().getRDEContainerTitles());
		deleteComponentButton = new JButton();
		transferComponentsButton = new JButton();
		manageLocationsButton = new JButton();
		exportEAD = new JButton();
		exportMARC = new JButton();
		exportContainerLabels = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setPreferredSize(new Dimension(1050, 565));
		setOpaque(false);
		setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		setRequestFocusEnabled(false);
		setLayout(new FormLayout(
			"default:grow",
			"default, top:default:grow, fill:default"));

		//======== panel6 ========
		{
			panel6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			panel6.setBackground(new Color(200, 205, 232));
			panel6.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormFactory.UNRELATED_GAP_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.UNRELATED_GAP_COLSPEC
				},
				RowSpec.decodeSpecs("default")));

			//---- label2 ----
			label2.setText("Title: ");
			label2.setForeground(new Color(0, 0, 102));
			label2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			panel6.add(label2, cc.xy(3, 1));

			//---- resourceTitle ----
			resourceTitle.setEditable(false);
			resourceTitle.setOpaque(false);
			resourceTitle.setBorder(null);
			panel6.add(resourceTitle, cc.xywh(5, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

			//---- label3 ----
			label3.setText("Resource Identifier: ");
			label3.setForeground(new Color(0, 0, 102));
			label3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			label3.setHorizontalAlignment(SwingConstants.LEFT);
			panel6.add(label3, cc.xy(7, 1));

			//---- resourceIdentifier ----
			resourceIdentifier.setEditable(false);
			resourceIdentifier.setOpaque(false);
			resourceIdentifier.setBorder(null);
			resourceIdentifier.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			resourceIdentifier.setHorizontalAlignment(SwingConstants.RIGHT);
			panel6.add(resourceIdentifier, cc.xywh(10, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		}
		add(panel6, cc.xy(1, 1));

		//======== splitPane ========
		{
			splitPane.setDividerLocation(300);
			splitPane.setLastDividerLocation(300);
			splitPane.setOpaque(false);
			splitPane.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

			//======== scrollPane ========
			{
				scrollPane.setPreferredSize(new Dimension(250, 384));
				scrollPane.setMinimumSize(new Dimension(250, 384));
				scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			}
			splitPane.setLeftComponent(scrollPane);
		}
		add(splitPane, cc.xywh(1, 2, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));

		//======== panel2 ========
		{
			panel2.setOpaque(false);
			panel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			panel2.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
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
			addSiblingBelowButton.setToolTipText("Add Sibling");
			addSiblingBelowButton.setText("<html>Add<br>Sibling</html>");
			addSiblingBelowButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addSiblingBelowActionPerformed(e);
				}
			});
			panel2.add(addSiblingBelowButton, cc.xy(3, 1));

			//---- rapidDataentryScreens ----
			rapidDataentryScreens.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					rapidDataentryScreensActionPerformed(e);
				}
			});
			panel2.add(rapidDataentryScreens, cc.xy(5, 1));

			//---- deleteComponentButton ----
			deleteComponentButton.setOpaque(false);
			deleteComponentButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			deleteComponentButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/delete.jpg")));
			deleteComponentButton.setToolTipText("Delete Component");
			deleteComponentButton.setText("<html>Delete<br>Component</html>");
			deleteComponentButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteComponentActionPerformed(e);
				}
			});
			panel2.add(deleteComponentButton, cc.xy(7, 1));

			//---- transferComponentsButton ----
			transferComponentsButton.setOpaque(false);
			transferComponentsButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			transferComponentsButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/transfer.png")));
			transferComponentsButton.setToolTipText("Transfer Components");
			transferComponentsButton.setText("Transfer");
			transferComponentsButton.setSelectedIcon(null);
			transferComponentsButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					transferComponentsButtonActionPerformed(e);
				}
			});
			panel2.add(transferComponentsButton, cc.xy(9, 1));

			//---- manageLocationsButton ----
			manageLocationsButton.setText("<html>Manage<br>Locations</html>");
			manageLocationsButton.setOpaque(false);
			manageLocationsButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			manageLocationsButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/manageLocations.png")));
			manageLocationsButton.setToolTipText("Manage Locations");
			manageLocationsButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					manageLocationsButtonActionPerformed(e);
				}
			});
			panel2.add(manageLocationsButton, cc.xy(11, 1));

			//---- exportEAD ----
			exportEAD.setOpaque(false);
			exportEAD.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			exportEAD.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/exportEAD.jpg")));
			exportEAD.setToolTipText("Export EAD");
			exportEAD.setText("<html>Export<br>EAD</html>");
			exportEAD.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					exportEADActionPerformed(e);
				}
			});
			panel2.add(exportEAD, cc.xywh(13, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

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
			panel2.add(exportMARC, cc.xywh(15, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

			//---- exportContainerLabels ----
			exportContainerLabels.setOpaque(false);
			exportContainerLabels.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			exportContainerLabels.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/export.png")));
			exportContainerLabels.setToolTipText("Export Container Labels");
			exportContainerLabels.setText("<html>Export<br>Cont. Labels</html>");
			exportContainerLabels.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					exportContainerLabelsActionPerformed(e);
				}
			});
			panel2.add(exportContainerLabels, cc.xywh(17, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
		}
		add(panel2, cc.xy(1, 3));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel panel6;
	private JLabel label2;
	private JTextField resourceTitle;
	private JLabel label3;
	private JTextField resourceIdentifier;
	private JSplitPane splitPane;
	private JScrollPane scrollPane;
	private JPanel panel2;
	private JButton addChildButton;
	private JButton addSiblingBelowButton;
	private JComboBox rapidDataentryScreens;
	private JButton deleteComponentButton;
	private JButton transferComponentsButton;
	private JButton manageLocationsButton;
	private JButton exportEAD;
	private JButton exportMARC;
	private JButton exportContainerLabels;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	protected int status = 0;
	private ResourceFields resourcePanel;
	private ResourceComponentsFields resourceComponentsPanel;
	//    private ResourcesComponentsDAO resourceComponentAccess;
	private ResourcesComponents currentResourcesComponent = null;
	private ResourcesCommon currentResourcesCommon;
	private ResourcesComponents resourcesComponentFromSearch = null;
	DefaultMutableTreeNode selectedTreeNode;
	private Resources resourceModel;
	private JTree resourceTree;
	private DefaultMutableTreeNode root;
	private DefaultTreeModel treeModel;
	private Boolean initialSet = false;
	private Boolean suppressTreeValueChangeAction = false;
	private JPopupMenu pm = null;

	protected void setDisplayToFirstTab() {
		resourcePanel.setDisplayToFirstTab();
		resourceComponentsPanel.setDisplayToFirstTab();
	}


	protected void setParentEditors() {
		resourcePanel.setParentEditor(this.getParentEditor());
		resourceComponentsPanel.setParentEditor(this.getParentEditor());
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
//        menuItem = new JMenuItem();
//        menuItem.setText("Add Sibling");
//        menuItem.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                addSiblingAboveActionPerformed(e);
//            }
//        });
//        pm.add(menuItem);

		//---- Add Child ----
		menuItem = new JMenuItem();
		menuItem.setText("Add Sibling");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addSiblingBelowActionPerformed(e);
			}
		});
		pm.add(menuItem);

		//---- Add Child ----
		menuItem = new JMenuItem();
		menuItem.setText("Delete Component");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteComponentActionPerformed(e);
			}
		});
		pm.add(menuItem);

		pm.addMouseListener(this);

	}

	private void resourceTreeValueChanged(TreeSelectionEvent e) {
		if (initialSet) {
			initialSet = false;
			if (getResourcesComponentFromSearch() == null) {
				setComponetButtonEnabled(false);
			}
		} else if (suppressTreeValueChangeAction) {
			suppressTreeValueChangeAction = false;
		} else if (getParentEditor().getStatus() == JOptionPane.CANCEL_OPTION) {
			// do nothing because the user has canceled
		} else {
			resourceTree.requestFocus();
			if (commitChangesToCurrentResourceComponent(e)) {
				treeModel.nodeChanged(selectedTreeNode);
				TreePath path = e.getPath();
				selectedTreeNode = (DefaultMutableTreeNode) path.getLastPathComponent();

				currentResourcesCommon = (ResourcesCommon) selectedTreeNode.getUserObject();


				if (currentResourcesCommon instanceof Resources) {
					setContentPanel(resourcePanel);
					currentResourcesComponent = null;
					setComponetButtonEnabled(false);
				} else if (currentResourcesCommon instanceof ResourcesComponents) {
					currentResourcesComponent = (ResourcesComponents) currentResourcesCommon;
					resourceComponentsPanel.setModel(currentResourcesComponent, null);
					setContentPanel(resourceComponentsPanel);
					setComponetButtonEnabled(true);
				}
				resourceTree.invalidate();
			} else {
				suppressTreeValueChangeAction = true;
				resourceTree.setSelectionPath(new TreePath(selectedTreeNode.getPath()));
			}
		}
	}

	protected String getNextPersistentId() {
		return ((Resources) resourcePanel.getModel()).getNextPersistentIdAndIncrement();
	}

	protected boolean commitChangesToCurrentResourceComponent(EventObject event) {
		//if there is a current resources component then save it
		if (currentResourcesComponent == null) {
			if (resourceModel.validateAndDisplayDialog(event)) {
				return true;
			} else {
				return false;
			}
		} else {
			resourceComponentsPanel.commitChanges();
			if (currentResourcesComponent.validateAndDisplayDialog(event)) {
				return true;
			} else {
				return false;
			}
		}

	}

	private void setNewNodeOrder(DefaultMutableTreeNode newTreeNode) throws InvalidTreeNode {
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) newTreeNode.getParent();
		DefaultMutableTreeNode previous = (DefaultMutableTreeNode) parent.getChildBefore(newTreeNode);
		DnDUtils.assignNewOrder(newTreeNode, parent, previous);

//      DnDResourceTree.assignNewOrder(newTreeNode, parent, previous, treeModel);
//        ResourceTreeNode treeNode = (ResourceTreeNode) newTreeNode.getUserObject();
//        if (treeNode.getResourceComponent() != null) {
//            treeNode.getResourceComponent().setSequenceNumber(treeNode.getSequenceNumber());
//        } else {
//            throw new InvalidTreeNode("Only nodes for components are allowed here");
//        }
	}

	private void addSibling(String position, ActionEvent e) {
		TreePath selectedPath = resourceTree.getSelectionPath();
		if (selectedPath == null) {
			JOptionPane.showMessageDialog(this, "You must select a node first");
		} else {
			try {
				if (commitChangesToCurrentResourceComponent(e)) {
					if (debug) {
						System.out.println("Sibling added: " + currentResourcesComponent);
					}

					// parent already set so no need to set it
					ResourcesComponents newComponent = createResourceTreeNode(false);

					DefaultMutableTreeNode newTreeNode = this.addSiblingBelow(newComponent, position);
					TreePath pathToNewNode = new TreePath(newTreeNode.getPath());
					setNewNodeOrder(newTreeNode);

					currentResourcesComponent.getParent().addChild(newComponent);
					currentResourcesComponent.getParent().resortComponents();

					resourceTree.setSelectionPath(pathToNewNode);
					if (newComponent.getResource() == null && newComponent.getResourceComponentParent() == null) {
						throw new InvalidTreeNode("Component is not properly linked");
					}
				}
			} catch (InvalidTreeNode invalidTreeNode) {
				new ErrorDialog(getParentEditor(), "Error adding sibling component", invalidTreeNode).showDialog();
			}
		}

	}

	private ResourcesComponents createResourceTreeNode(Boolean childRecord) {
		return createResourceTreeNode(childRecord, null);
	}

	private ResourcesComponents createResourceTreeNode(Boolean childRecord, ResourcesComponents component) {
		ResourcesComponents newComponent = null;
		//create the new component record
		if (childRecord) {
			if (currentResourcesComponent == null) {
				if (component == null) {
					newComponent = new ResourcesComponents(resourceModel);
				} else {
					newComponent = component;
					newComponent.setResource(resourceModel);
				}
				//resourceModel.addResourcesComponents(newComponent);
			} else {
				if (component == null) {
					newComponent = new ResourcesComponents(currentResourcesComponent);
				} else {
					newComponent = component;
					newComponent.setResourceComponentParent(currentResourcesComponent);
				}
				//currentResourcesComponent.addResourcesComponents(newComponent);
			}
		} else {
			if (currentResourcesComponent.getResource() != null) {
				if (component == null) {
					newComponent = new ResourcesComponents(currentResourcesComponent.getResource());
				} else {
					newComponent = component;
					newComponent.setResource(currentResourcesComponent.getResource());
				}
				//currentResourcesComponent.getResource().addResourcesComponents(newComponent);
			} else if (currentResourcesComponent.getResourceComponentParent() != null) {
				if (component == null) {
					newComponent = new ResourcesComponents(currentResourcesComponent.getResourceComponentParent());
				} else {
					newComponent = component;
					component.setResourceComponentParent(currentResourcesComponent.getResourceComponentParent());
				}
				//currentResourcesComponent.getResourceComponentParent().addResourcesComponents(newComponent);
			} else {
				try {
					throw new InvalidTreeNode("Component is not properly linked");
				} catch (InvalidTreeNode invalidTreeNode) {
					new ErrorDialog("Error creating new node", invalidTreeNode).showDialog();
				}
			}
		}
		//DefaultMutableTreeNode selectedTreeNode = (DefaultMutableTreeNode) resourceTree.getSelectionPath().getLastPathComponent();
		//ResourcesCommon selectedNode = (ResourcesCommon) selectedTreeNode.getUserObject();

		newComponent.setPersistentId(getNextPersistentId());
		//newComponent.setSequenceNumber(selectedNode.getSequenceNumber());

		resourceComponentsPanel.setDisplayToFirstTab();

		if (newComponent.getResource() == null && newComponent.getResourceComponentParent() == null) {
			try {
				throw new InvalidTreeNode("Component is not properly linked");
			} catch (InvalidTreeNode invalidTreeNode) {
				new ErrorDialog("Error creating new node", invalidTreeNode).showDialog();
			}
		}
		return newComponent;
	}

	public final void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		resourceModel = (Resources) model;
		setResourceTree(resourceModel.getPlainResourceJtree(progressPanel));
//		setResourceTree(((Resources) model).getResourceJtree(progressPanel));
		if (debug) {
			System.out.println("Build Tree: " + MyTimer.toString(ApplicationFrame.getInstance().getTimer().elapsedTimeMillisSplit()));
		}
		currentResourcesComponent = null;
		if (currentResourcesCommon instanceof Resources) {
			resourcePanel.setModel(model, null);
			setContentPanel(resourcePanel);
		} else {
			if (getResourcesComponentFromSearch() == null) {
				progressPanel.close();
				JOptionPane.showMessageDialog(this, "Resource tree does not have a resource at its root",
						"Resource Loading Error", JOptionPane.ERROR_MESSAGE);
			} else {
				resourceComponentsPanel.setModel(currentResourcesCommon, progressPanel);
				setContentPanel(resourceComponentsPanel);
			}
		}

		resourceTitle.setText(resourceModel.getTitle());
		resourceIdentifier.setText(resourceModel.getResourceIdentifier());

    // clear any cached rde screens
    rdeDialogs = new HashMap<String, RapidResourceComponentDataEntry2>();
  }

	public Component getInitialFocusComponent() {
		return null;
	}

//    private void initAccess() {
//        resourceComponentAccess = new ResourcesComponentsDAO();
//    }

	public void setContentPanel(JPanel newContentPanel) {
		int dividerLocation = splitPane.getDividerLocation();
		splitPane.setRightComponent(newContentPanel);
		splitPane.setDividerLocation(dividerLocation);
	}

	public JTree getResourceTree() {
		return resourceTree;
	}

	private void setResourceTree(JTree treeFromModel) {
		resourceTree = treeFromModel;
		resourceTree.addMouseListener(this);
		treeModel = (DefaultTreeModel) resourceTree.getModel();
		root = (DefaultMutableTreeNode) treeModel.getRoot();
		this.resourceTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				resourceTreeValueChanged(e);
			}
		});
		resourceTree.setAutoscrolls(true);
		scrollPane.setViewportView(resourceTree);

		initialSet = true;
		if (getResourcesComponentFromSearch() != null) {
			TreePath pathFromSearch = resourceModel.getTreePathFromComponent(getResourcesComponentFromSearch());
			selectedTreeNode =
					(DefaultMutableTreeNode) pathFromSearch.getLastPathComponent();
			resourceTree.setSelectionPath(pathFromSearch);
			resourceTree.scrollPathToVisible(pathFromSearch);
		} else {
			selectedTreeNode =
					(DefaultMutableTreeNode) resourceTree.getPathForRow(0).getLastPathComponent();
			this.resourceTree.setSelectionRow(0);
		}
		currentResourcesCommon = (ResourcesCommon) selectedTreeNode.getUserObject();

		//lee's stab at improving the wrap
		TreeWrapper wrapper = new TreeWrapper(treeFromModel);
		wrapper.addTreeTreeDnDListener(new sequencedObjectDnDDropListener());
	}

	/**
	 * Add child to the currently selected node.
	 */
	public DefaultMutableTreeNode addObject(Object child) {
		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = resourceTree.getSelectionPath();

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
			resourceTree.scrollPathToVisible(new TreePath(childNode.getPath()));
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
			resourceTree.scrollPathToVisible(new TreePath(childNode.getPath()));
		}
		return childNode;
	}

	private DefaultMutableTreeNode addSiblingBelow(Object child, String position) {
		TreePath siblingPath = resourceTree.getSelectionPath();
		DefaultMutableTreeNode siblingNode = (DefaultMutableTreeNode) siblingPath.getLastPathComponent();
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) siblingNode.getParent();
		if (position.equalsIgnoreCase(ResourceTreeViewer.ABOVE)) {
			return addObject(parentNode, child, parentNode.getIndex(siblingNode), true);
		} else {
			return addObject(parentNode, child, parentNode.getIndex(siblingNode) + 1, true);
		}
	}

	private void setComponetButtonEnabled(Boolean enabled) {
//		this.addSiblingAboveButton.setEnabled(enabled);
		this.addSiblingBelowButton.setEnabled(enabled);
		this.deleteComponentButton.setEnabled(enabled);
	}

	public void mouseClicked(MouseEvent mouseEvent) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public void mousePressed(MouseEvent mouseEvent) {
		if (mouseEvent.isPopupTrigger()) {
			pm.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
		}
	}

	public void mouseReleased(MouseEvent mouseEvent) {
		if (mouseEvent.isPopupTrigger()) {
			pm.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
		}
	}

	public void mouseEntered(MouseEvent mouseEvent) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public void mouseExited(MouseEvent mouseEvent) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

//	public InfiniteProgressPanel getGlassPane() {
//		return glassPane;
//	}

	public ResourcesComponents getResourcesComponentFromSearch() {
		return resourcesComponentFromSearch;
	}

	public void setResourcesComponentFromSearch(ResourcesComponents resourcesComponentFromSearch) {
		this.resourcesComponentFromSearch = resourcesComponentFromSearch;
	}

	// Method to update the JTree so that if name was changed then it can be reflected on save
	public void updateJTree() {
		resourceTree.updateUI();
	}

    /**
     * Method that initializes any rapid data entry plugins. RDE plugins are subplugins that
     * are contained within regualr plugins, but get added to the rapid data entry drop down
     * menu
     */
    private void initPlugins() {
        rdePlugins = new HashMap<String, RDEPlugin>();

        plugins = ATPluginFactory.getInstance().getRapidDataEntryPlugins();
        if (plugins != null) {
            for (ATPlugin plugin : plugins) {
                plugin.setEditorField(this);

                // get the plugin panels which may be JPanels or even JDialogs
                HashMap pluginPanels = plugin.getRapidDataEntryPlugins();
                for (Object key : pluginPanels.keySet()) {
                    String panelName = (String) key;
                    RDEPlugin rdePlugin = (RDEPlugin) pluginPanels.get(key);

                    rdePlugins.put(panelName, rdePlugin);
                    rapidDataentryScreens.addItem(panelName);
                }
            }
        }
    }

    /**
     * Method to launch the appropriate Rapid Data Entry plugin.
     * This plugin can either launch a dialog, or just execute program
     * logic.
     *
     * @param title The title of the rde plugin used to find it in the
     *              hashmap
     */
    private void selectRapidDataEntryPlugin(String title) {
        RDEPlugin rdePlugin = rdePlugins.get(title);

        // ok now we have a jpanel lets do something interesting
        rdePlugin.setModel((Resources)this.getModel(), currentResourcesCommon);

        if (rdePlugin.hasDialog()) {
            rdePlugin.showPlugin(getParentEditor(), title);
        } else {
            rdePlugin.doTask();
        }
    }
}
