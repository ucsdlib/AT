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
 */

package org.archiviststoolkit.swing;

//==============================================================================
// Import Declarations
//==============================================================================

import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.exceptions.*;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.DomainTableWorkSurface;
import org.archiviststoolkit.mydomain.LookupException;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.report.ReportDestinationProperties;
import org.archiviststoolkit.report.ReportUtils;

import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import java.util.ArrayList;

/**
 * The workbench is the main interface component which marshalls all others
 * It contains a number of worksurfaces which allow the client to interact with
 * the backend data model.
 */

public class WorkSurfaceContainer extends JPanel implements ActionListener {

	/**
	 * The main splitpane which contains the surface tree and the surface itself.
	 */
	private JSplitPane mainSplitViewPane = new JSplitPane();

	/**
	 * The navigatorPane which contains the JTree worksurface navigator.
	 */
	private JPanel navigatorPane = new JPanel(new BorderLayout());

	/**
	 * The worksurfacePane which contains all the worksurfaces ordered in a card layout.
	 */
	private JPanel worksurfacePane = new JPanel(new BorderLayout());

	/**
	 * The workbench tree model which contains the list of surfaces.
	 */
	private WorkSurfaceTreeModel model = new WorkSurfaceTreeModel();

	/**
	 * The cardpane used to store worksurfaces.
	 */
	private JPanel cardPane = new JPanel(new CardLayout());

	/**
	 * The tree used to hold the worksurfaces.
	 */
	private JTree surfaceTree = null;

	/**
	 * The title bar which sits above the worksurface.
	 */
	private WorkSurfaceTitleBar workSurfaceTitleBar = null;

	private DomainTableWorkSurface currentWorkSurface = null;

	/**
	 * MAGIC NUMBER which represents the default divider size.
	 */

	private static final int DIVIDER_SIZE = 8;

	/**
	 * MAGIC NUMBER which represents the default divider weighting.
	 */

	private static final double DIVIDER_WEIGHTING = .15D;

    /**
     * The spacing between the rows in the JTree, surfaceTree
     */
    private final int ROW_SPACING = 25;

	private ConcreteAction searchAction = null;
	private ConcreteAction listAllAction = null;
	private ConcreteAction newRecordAction = null;
	private ConcreteAction reportsAction = null;
	private ConcreteAction deleteAction = null;
	private ConcreteAction exportEADAction = null;
	private ConcreteAction exportMARCAction = null;
	private ConcreteAction exportMETSAction = null;
	private ConcreteAction exportMODSAction = null;
	private ConcreteAction exportDublinCoreAction = null;

	private ConcreteAction mergeAction = null;

	private static Hashtable<Class, DomainTableWorkSurface> workSurfaces = new Hashtable<Class, DomainTableWorkSurface>();
	private static final boolean debug = false;

	/**
	 * Constructor which builds the workbench userinterface.
	 */

	public WorkSurfaceContainer() {
		super(new BorderLayout());

		surfaceTree = new JTree(model);
		surfaceTree.setRootVisible(false);
		surfaceTree.setCellRenderer(new WorkSurfaceTreeCellRenderer());
		surfaceTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		surfaceTree.setRowHeight(ROW_SPACING);
        this.setBackground(ApplicationFrame.BACKGROUND_COLOR);

		JScrollPane navigatorScrollPane = new JScrollPane();

		navigatorScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		navigatorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		navigatorPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2), BorderFactory.createEtchedBorder()));
		navigatorPane.add(navigatorScrollPane, BorderLayout.CENTER);
		navigatorScrollPane.getViewport().add(surfaceTree, null);

		mainSplitViewPane.setLeftComponent(navigatorPane);
		mainSplitViewPane.setRightComponent(worksurfacePane);
		mainSplitViewPane.setBorder(null);
		mainSplitViewPane.setDividerSize(DIVIDER_SIZE);
		mainSplitViewPane.setOneTouchExpandable(true);
		mainSplitViewPane.setResizeWeight(DIVIDER_WEIGHTING);
		mainSplitViewPane.setBackground(ApplicationFrame.BACKGROUND_COLOR);

		workSurfaceTitleBar = new WorkSurfaceTitleBar(new ImageIcon(WorkSurfaceContainer.class.getResource("/org/archiviststoolkit/resources/images/namesIcon.png")), "Names");
		workSurfaceTitleBar.setForeground(Color.white);

		workSurfaceTitleBar.setBackground(StandardEditor.MODULE_HEADER_COLOR_NAMES);
		worksurfacePane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2), BorderFactory.createEtchedBorder()));
		workSurfaceTitleBar.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		worksurfacePane.add(workSurfaceTitleBar, BorderLayout.NORTH);
		worksurfacePane.add(cardPane, BorderLayout.CENTER);
		this.add(mainSplitViewPane, BorderLayout.CENTER);

		searchAction = new ConcreteAction("Search");
		searchAction.addActionListener(this);
		searchAction.setIcon(new ImageIcon(WorkSurfaceContainer.class.getResource("/org/archiviststoolkit/resources/images/search.jpg")));

		listAllAction = new ConcreteAction("List All");
		listAllAction.addActionListener(this);
		listAllAction.setIcon(new ImageIcon(WorkSurfaceContainer.class.getResource("/org/archiviststoolkit/resources/images/showAll.jpg")));

		newRecordAction = new ConcreteAction("New Record");
		newRecordAction.addActionListener(this);
		newRecordAction.setIcon(new ImageIcon(WorkSurfaceContainer.class.getResource("/org/archiviststoolkit/resources/images/newRecord.jpg")));

		reportsAction = new ConcreteAction("Reports");
		reportsAction.addActionListener(this);
		reportsAction.setIcon(new ImageIcon(WorkSurfaceContainer.class.getResource("/org/archiviststoolkit/resources/images/print.jpg")));

		deleteAction = new ConcreteAction("Delete");
		deleteAction.addActionListener(this);
		deleteAction.setIcon(new ImageIcon(WorkSurfaceContainer.class.getResource("/org/archiviststoolkit/resources/images/delete.jpg")));

		exportEADAction = new ConcreteAction("Export EAD");
		exportEADAction.addActionListener(this);
		exportEADAction.setIcon(new ImageIcon(WorkSurfaceContainer.class.getResource("/org/archiviststoolkit/resources/images/exportEAD.jpg")));

		mergeAction = new ConcreteAction("Merge");
		mergeAction.addActionListener(this);
		mergeAction.setIcon(new ImageIcon(WorkSurfaceContainer.class.getResource("/org/archiviststoolkit/resources/images/merge.png")));

		exportMARCAction = new ConcreteAction("Export MARC");
		exportMARCAction.addActionListener(this);
		exportMARCAction.setIcon(new ImageIcon(WorkSurfaceContainer.class.getResource("/org/archiviststoolkit/resources/images/exportMARC.jpg")));

        exportMETSAction = new ConcreteAction("Export METS");
		exportMETSAction.addActionListener(this);
		exportMETSAction.setIcon(new ImageIcon(WorkSurfaceContainer.class.getResource("/org/archiviststoolkit/resources/images/exportMETS.jpg")));

        exportMODSAction = new ConcreteAction("Export MODS");
		exportMODSAction.addActionListener(this);
		exportMODSAction.setIcon(new ImageIcon(WorkSurfaceContainer.class.getResource("/org/archiviststoolkit/resources/images/exportMODS.png")));

        exportDublinCoreAction = new ConcreteAction("Export DublinCore");
		exportDublinCoreAction.addActionListener(this);
		exportDublinCoreAction.setIcon(new ImageIcon(WorkSurfaceContainer.class.getResource("/org/archiviststoolkit/resources/images/exportDC.png")));

		MouseListener mouseListener = new MouseAdapter() {

			/**
			 * Is triggered when the mouse is used to select a list item.
			 *
			 * @param mouseEvent the mouse event
			 */

			public void mousePressed(final MouseEvent mouseEvent) {
				if (debug) {
					System.out.println("click from:" + this);
				}
				if (mouseEvent.getClickCount() <= 2) {
					setCurrentWorkSurface();

					if (mouseEvent.getClickCount() == 2) {
						currentWorkSurface.onFindAll();
					}
				}
			}
		};

		surfaceTree.addMouseListener(mouseListener);

		/**
		 * Is triggered when the keyborad is used to select a list item.
		 * @param keyEvent the keyborad event
		 */
		KeyListener keyListener = new KeyAdapter() {
			public void keyReleased(final KeyEvent e) {
				int keyCode = e.getKeyCode();

				if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) {
					setCurrentWorkSurface();
				} else if (keyCode == KeyEvent.VK_ENTER) {
					currentWorkSurface.onFindAll();
				} else {/* simple ignore all other keys presses*/ }
			}
		};

		surfaceTree.addKeyListener(keyListener);
	}

	// Method to select the current worksurface
	private void setCurrentWorkSurface() {
		TreePath[] paths = surfaceTree.getSelectionPaths();

		if (paths != null && paths.length > 0) {
			Object base = paths[0].getLastPathComponent();

			if (base instanceof DomainTableWorkSurface) {
				setCurrentWorkSurface((DomainTableWorkSurface)base);
			}
		}
	}

    /**
     * Method to return the current work surface
     * @return Return the current work surface
     */
    public DomainTableWorkSurface getCurrentWorkSurface() {
        return currentWorkSurface;
    }

	private void setCurrentWorkSurface(DomainTableWorkSurface workSurface) {
		currentWorkSurface = workSurface;
		workSurfaceTitleBar.setText(currentWorkSurface.getName());
		workSurfaceTitleBar.setIcon(currentWorkSurface.getIcon());
		setTitleBarColor();
		((CardLayout) cardPane.getLayout()).show(cardPane, currentWorkSurface.getName());
		ApplicationFrame.getInstance().setButtonVisiblity(currentWorkSurface.getClazz());
	}

	public void setCurrentWorkSurface(Class clazz) {
		DomainTableWorkSurface workSurface = getWorkSurfaceByClass(clazz);
		setCurrentWorkSurface(workSurface);
		int index = model.getIndexOfChild(model.getRoot(), workSurface);
		surfaceTree.setSelectionInterval(index, index);
	}

	public void setTitleBarColor() {

		Class clazz = currentWorkSurface.getClazz();
		if (clazz == Resources.class) {
			workSurfaceTitleBar.setBackground(StandardEditor.MODULE_HEADER_COLOR_RESOURCES);
		} else if (clazz == Accessions.class) {
			workSurfaceTitleBar.setBackground(StandardEditor.MODULE_HEADER_COLOR_ACCESSIONS);
		} else if (clazz == Names.class) {
			workSurfaceTitleBar.setBackground(StandardEditor.MODULE_HEADER_COLOR_NAMES);
		} else if (clazz == Subjects.class) {
			workSurfaceTitleBar.setBackground(StandardEditor.MODULE_HEADER_COLOR_SUBJECTS);
		} else if (clazz == DigitalObjects.class) {
			workSurfaceTitleBar.setBackground(StandardEditor.MODULE_HEADER_COLOR_DIGITAL_OBJECTS);
        }
	}

	/**
	 * get the action which creates.
	 *
	 * @return the action
	 */

	public final ConcreteAction getSearchAction() {
		return searchAction;
	}

	/**
	 * get the action which creates.
	 *
	 * @return the action
	 */

	public final ConcreteAction getListAllAction() {
		return listAllAction;
	}

	/**
	 * Add a new worksurface to this container.
	 *
	 * @param worksurface the worksurface to add
	 */

	public final void addWorkSurface(final DomainTableWorkSurface worksurface) {
		addWorkSurface(worksurface, true);
	}

	/**
	 * Add a new worksurface to this container.
	 *
	 * @param worksurface the worksurface to add
	 */

	public final void addWorkSurface(final DomainTableWorkSurface worksurface, boolean includeInList) {
		if (currentWorkSurface == null) {
			currentWorkSurface = (DomainTableWorkSurface) worksurface;
			workSurfaceTitleBar.setText(currentWorkSurface.getName());
			workSurfaceTitleBar.setIcon(currentWorkSurface.getIcon());
			setTitleBarColor();
		}
		if (includeInList) {
			model.add(worksurface);
		}
		cardPane.add(worksurface.getComponent(), worksurface.getName());
		workSurfaces.put(worksurface.getClazz(), worksurface);
	}

	public static DomainTableWorkSurface getWorkSurfaceByClass(Class clazz) {
		return workSurfaces.get(clazz);
	}

	public void updateColumns() {
		for (DomainTableWorkSurface workSurface : workSurfaces.values()) {
			workSurface.updateColumns();
		}
	}

	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getSource() == searchAction) {
			currentWorkSurface.onSearch();
		} else if (actionEvent.getSource() == listAllAction) {
			currentWorkSurface.onFindAll();
		} else if (actionEvent.getSource() == newRecordAction) {
			currentWorkSurface.onInsert();
		} else if (actionEvent.getSource() == deleteAction) {
			currentWorkSurface.onDelete();
		} else if (actionEvent.getSource() == exportEADAction) {
			currentWorkSurface.exportEAD();
		} else if (actionEvent.getSource() == exportMARCAction) {
			currentWorkSurface.exportMARC();
		} else if (actionEvent.getSource() == exportMETSAction) {
			currentWorkSurface.exportMETS();
		} else if (actionEvent.getSource() == exportMODSAction) {
			currentWorkSurface.exportMODS();
		} else if (actionEvent.getSource() == exportDublinCoreAction) {
			currentWorkSurface.exportDublinCore();
		} else if (actionEvent.getSource() == mergeAction) {
			currentWorkSurface.merge();
		} else if (actionEvent.getSource() == reportsAction) {

			try {
				// must set the clazz here to get the current report
                final ReportDestinationProperties reportDestinationProperties = new ReportDestinationProperties(currentWorkSurface.getClazzForReport(), ApplicationFrame.getInstance());

				if (reportDestinationProperties.showDialog(currentWorkSurface.getSelectedRowCount())) {
					Thread performer = new Thread(new Runnable() {
						public void run() {
							InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 0, true);
							monitor.start("Gathering records to print");
							try {
								ArrayList<DomainObject> listForPrinting = currentWorkSurface.getResultSetForPrinting(monitor, reportDestinationProperties.getSelectedReport());
                                if(!monitor.isProcessCancelled()) { // check to make sure cancel wasnot pressed
                                    ReportUtils.printReport(reportDestinationProperties, listForPrinting, monitor);
                                }
							} catch (LookupException e) {
								closeLongSession();
								monitor.close();
								new ErrorDialog(ApplicationFrame.getInstance(), "Error printing report", e).showDialog();
							} catch (UnsupportedParentComponentException e) {
								closeLongSession();
								monitor.close();
								new ErrorDialog(ApplicationFrame.getInstance(), "Error printing report", e).showDialog();
							} catch (PersistenceException e) {
								closeLongSession();
								monitor.close();
								new ErrorDialog(ApplicationFrame.getInstance(), "Error printing report", e).showDialog();
							} catch (UnsupportedReportDestination e) {
								closeLongSession();
								monitor.close();
								new ErrorDialog(ApplicationFrame.getInstance(), "Error printing report", e).showDialog();
							} catch (UnsupportedClassException e) {
								closeLongSession();
								monitor.close();
								new ErrorDialog(ApplicationFrame.getInstance(), "Error printing report", e).showDialog();
							} catch (UnsupportedReportType e) {
								closeLongSession();
								monitor.close();
								new ErrorDialog(ApplicationFrame.getInstance(), "Error printing report", e).showDialog();
							} catch (ReportExecutionException e) {
								closeLongSession();
								monitor.close();
								new ErrorDialog(ApplicationFrame.getInstance(), "Error printing report", e).showDialog();
							} finally {
								closeLongSession();
								monitor.close();
							}
						}
					}, "Printing");
					performer.start();
				}
			} catch (UnsupportedClassException e) {
				new ErrorDialog(ApplicationFrame.getInstance(), "Error printing report", e).showDialog();
			} catch (UnsupportedReportDestination e) {
				new ErrorDialog(ApplicationFrame.getInstance(), "Error printing report", e).showDialog();
			} catch (UnsupportedReportType e) {
				new ErrorDialog(ApplicationFrame.getInstance(), "Error printing report", e).showDialog();
			}


		}
	}

	private void closeLongSession() {
		currentWorkSurface.closeLongSessionForPrinting();
	}

	public ConcreteAction getNewRecordAction() {
		return newRecordAction;
	}

	public ConcreteAction getReportsAction() {
		return reportsAction;
	}

	public void setReportsAction(ConcreteAction reportsAction) {
		this.reportsAction = reportsAction;
	}

	public ConcreteAction getDeleteAction() {
		return deleteAction;
	}

	public ConcreteAction getExportEADAction() {
		return exportEADAction;
	}

	public void setExportEADAction(ConcreteAction exportEADAction) {
		this.exportEADAction = exportEADAction;
	}

	public ConcreteAction getExportMARCAction() {
		return exportMARCAction;
	}

	public void setExportMARCAction(ConcreteAction exportMARCAction) {
		this.exportMARCAction = exportMARCAction;
	}

	public ConcreteAction getMergeAction() {
		return mergeAction;
	}

	public void setMergeAction(ConcreteAction mergeAction) {
		this.mergeAction = mergeAction;
	}

    public ConcreteAction getExportMETSAction() {
        return exportMETSAction;
    }

    public void setExportMETSAction(ConcreteAction exportMETSAction) {
        this.exportMETSAction = exportMETSAction;
    }

    public ConcreteAction getExportMODSAction() {
        return exportMODSAction;
    }

    public void setExportMODSAction(ConcreteAction exportMODSAction) {
        this.exportMODSAction = exportMODSAction;
    }

    public ConcreteAction getExportDublinCoreAction() {
        return exportDublinCoreAction;
    }

    public void setExportDublinCoreAction(ConcreteAction exportDublinCoreAction) {
        this.exportDublinCoreAction = exportDublinCoreAction;
    }
}