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
 * @author Lee Mandell
 * Date: Aug 7, 2008
 * Time: 12:23:49 PM
 */

package org.archiviststoolkit.mydomain;

import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.exceptions.DefaultExceptionHandler;
import org.archiviststoolkit.exporter.MARCExportHandler;
import org.archiviststoolkit.exporter.METSExportHandler;
import org.archiviststoolkit.exporter.MODSExportHandler;
import org.archiviststoolkit.exporter.DCExportHandler;
import org.archiviststoolkit.util.FileUtils;
import org.archiviststoolkit.editor.DigitalObjectTreeViewer;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.model.DigitalObjects;
import org.archiviststoolkit.swing.*;

import javax.swing.*;
import java.util.Vector;
import java.io.File;

public class DigitalObjectTableWorkSurface extends DomainTableWorkSurface {
    private DigitalObjects digitalObject = null;

	/**
	 * Constructor.
	 *
	 * @param clazz	   the domain model class
	 * @param name		the name of this worksurface
	 * @param icon		the icon of this worksurface
	 * @param tableFormat the table format to use for the domain table
	 */
	public DigitalObjectTableWorkSurface(final Class clazz, final String name, final Icon icon, DomainTableFormat tableFormat) {
		super(clazz, name, icon, tableFormat);
	}

	/**
	 * Constructor.
	 *
	 * @param clazz the domain model class
	 * @param name  the name of this worksurface
	 * @param icon  the icon of this worksurface
	 */
	public DigitalObjectTableWorkSurface(final Class clazz, final String name, final Icon icon) {
		super(clazz, name, icon);
	}

    /**
     * Method to return the full digital object from the database or if the digital
     * object passed in is a child digital object the the full parent digital object
     * should be returned
     *
     * @param domainObject The digital object to load the full one for
     * @throws LookupException If the digital object can't be found
     */
	protected void getCurrentDomainObjectFromDatabase(DomainObject domainObject) throws LookupException {
        digitalObject = (DigitalObjects)domainObject;
        DigitalObjectTreeViewer treeViewer = (DigitalObjectTreeViewer)dialog.editorFields;

        if(digitalObject.getParent() == null) { // no parent, so just get the entire object from the database
		    currentDomainObject = access.findByPrimaryKeyLongSession(domainObject.getIdentifier());
            treeViewer.setSelectedDigitalObject(null);
        } else { // must loop until we find the top parent of the digital object
            DigitalObjects currentDigitalObject = (DigitalObjects)access.findByPrimaryKeyLongSession(digitalObject.getIdentifier());

            while(currentDigitalObject.getParent() != null) {
                currentDigitalObject = currentDigitalObject.getParent();
            }

            // now set the current digital object the one that was found
            // currentDomainObject = access.findByPrimaryKeyLongSession(currentDigitalObject.getIdentifier());
            currentDomainObject = currentDigitalObject;

            // Navigate to digital object in drop down menu
            treeViewer.setSelectedDigitalObject(digitalObject);
	    }
    }

    /**
     * Method to export MARC xml files
     */
    public void exportMARC() {
		final int[] selectedIndexes = table.getSelectedRows();
		final ExportOptionsMARC exportOptions = new ExportOptionsMARC();
		final File selectedFileOrDirectory = FileUtils.chooseFileOrDirectory(selectedIndexes.length, exportOptions);

		if (selectedFileOrDirectory != null) {
			Thread performer = new Thread(new Runnable() {
				public void run() {
					// see whether to show the cancel button
                    boolean allowCancel = false;
                    if(selectedIndexes.length > 1) {
                        allowCancel = true;
                    }

                    InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000, allowCancel);
					monitor.start("Exporting...");
					MARCExportHandler batchMARCHandler = new MARCExportHandler(exportOptions);

                    // open a long session to the database for loading full digital object
                    DigitalObjectDAO access = new DigitalObjectDAO();
                    access.getLongSession();
                    try {
						int[] selectedIndexes = table.getSelectedRows();
						Vector<DigitalObjects> digitalObjects = new Vector<DigitalObjects>();
						DigitalObjects digitalObject = null;

						for (int loop = 0; loop < selectedIndexes.length; loop++) {
							// check to see if this operation wasn't cancelled
                            if(monitor.isProcessCancelled()) {
                                break;
                            }

							digitalObject = (DigitalObjects)(table.getFilteredList().get(selectedIndexes[loop]));
							digitalObjects.add((DigitalObjects)access.findByPrimaryKeyLongSession(digitalObject.getIdentifier()));
						}

                        if(!monitor.isProcessCancelled()) {
                            Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler());
						    batchMARCHandler.exportDO(selectedFileOrDirectory, digitalObjects, monitor);
                        }
                        access.closeLongSession();
					} catch (Exception e) {
						monitor.close();
						new ErrorDialog("", e).showDialog();
					} finally {
						monitor.close();
					}
				}
			}, "ExportMarc");
			performer.start();
		}
	}

    /**
     * Method to export METS xml files
     */
    public void exportMETS() {
        final int[] selectedIndexes = table.getSelectedRows();
		final ExportOptionsMETS exportOptions = new ExportOptionsMETS();
		final File selectedFileOrDirectory = FileUtils.chooseFileOrDirectory(selectedIndexes.length, exportOptions);

		if (selectedFileOrDirectory != null) {
			Thread performer = new Thread(new Runnable() {
				public void run() {
					// see whether to show the cancel button
                    boolean allowCancel = false;
                    if(selectedIndexes.length > 1) {
                        allowCancel = true;
                    }

                    InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000, allowCancel);
					monitor.start("Exporting...");
					METSExportHandler batchMETSHandler = new METSExportHandler(exportOptions);

                    // open a long session to the database for loading full digital object
                    DigitalObjectDAO access = new DigitalObjectDAO();
                    access.getLongSession();
                    try {
						int[] selectedIndexes = table.getSelectedRows();
						Vector<DigitalObjects> digitalObjects = new Vector<DigitalObjects>();
						DigitalObjects digitalObject = null;

						for (int loop = 0; loop < selectedIndexes.length; loop++) {
							// check to see if this operation wasn't cancelled
                            if(monitor.isProcessCancelled()) {
                                break;
                            }

							digitalObject = (DigitalObjects)(table.getFilteredList().get(selectedIndexes[loop]));
							digitalObjects.add((DigitalObjects)access.findByPrimaryKeyLongSession(digitalObject.getIdentifier()));
						}

                        if(!monitor.isProcessCancelled()) {
                            Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler());
						    batchMETSHandler.export(selectedFileOrDirectory, digitalObjects, monitor);
                        }
                        access.closeLongSession();
					} catch (Exception e) {
						monitor.close();
						new ErrorDialog("", e).showDialog();
					} finally {
						monitor.close();
					}
				}
			}, "ExportMets");
			performer.start();
		}
    }

    /**
     * Method to export MODS xml files.
     */
    public void exportMODS() {
        final int[] selectedIndexes = table.getSelectedRows();
		final ExportOptionsMARC exportOptions = new ExportOptionsMARC();
		final File selectedFileOrDirectory = FileUtils.chooseFileOrDirectory(selectedIndexes.length, exportOptions);

		if (selectedFileOrDirectory != null) {
			Thread performer = new Thread(new Runnable() {
				public void run() {
					// see whether to show the cancel button
                    boolean allowCancel = false;
                    if(selectedIndexes.length > 1) {
                        allowCancel = true;
                    }

                    InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000, allowCancel);
					monitor.start("Exporting...");
					MODSExportHandler batchMODSHandler = new MODSExportHandler(exportOptions);

                    // open a long session to the database for loading full digital object
                    DigitalObjectDAO access = new DigitalObjectDAO();
                    access.getLongSession();
                    try {
						int[] selectedIndexes = table.getSelectedRows();
						Vector<DigitalObjects> digitalObjects = new Vector<DigitalObjects>();
						DigitalObjects digitalObject = null;

						for (int loop = 0; loop < selectedIndexes.length; loop++) {
							// check to see if this operation wasn't cancelled
                            if(monitor.isProcessCancelled()) {
                                break;
                            }

							digitalObject = (DigitalObjects)(table.getFilteredList().get(selectedIndexes[loop]));
							digitalObjects.add((DigitalObjects)access.findByPrimaryKeyLongSession(digitalObject.getIdentifier()));
						}

                        if(!monitor.isProcessCancelled()) {
                            Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler());
						    batchMODSHandler.export(selectedFileOrDirectory, digitalObjects, monitor);
                        }
                        access.closeLongSession();
					} catch (Exception e) {
						monitor.close();
						new ErrorDialog("", e).showDialog();
					} finally {
						monitor.close();
					}
				}
			}, "ExportMODS");
			performer.start();
		}
    }

    /**
     * Method to export Dublin Core xml files.
     */
    public void exportDublinCore() {
        final int[] selectedIndexes = table.getSelectedRows();
		final ExportOptionsMARC exportOptions = new ExportOptionsMARC();
		final File selectedFileOrDirectory = FileUtils.chooseFileOrDirectory(selectedIndexes.length, exportOptions);

		if (selectedFileOrDirectory != null) {
			Thread performer = new Thread(new Runnable() {
				public void run() {
					// see whether to show the cancel button
                    boolean allowCancel = false;
                    if(selectedIndexes.length > 1) {
                        allowCancel = true;
                    }

                    InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000, allowCancel);
					monitor.start("Exporting...");
					DCExportHandler batchDCHandler = new DCExportHandler(exportOptions);

                    // open a long session to the database for loading full digital object
                    DigitalObjectDAO access = new DigitalObjectDAO();
                    access.getLongSession();
                    try {
						int[] selectedIndexes = table.getSelectedRows();
						Vector<DigitalObjects> digitalObjects = new Vector<DigitalObjects>();
						DigitalObjects digitalObject = null;

						for (int loop = 0; loop < selectedIndexes.length; loop++) {
							// check to see if this operation wasn't cancelled
                            if(monitor.isProcessCancelled()) {
                                break;
                            }

							digitalObject = (DigitalObjects)(table.getFilteredList().get(selectedIndexes[loop]));
							digitalObjects.add((DigitalObjects)access.findByPrimaryKeyLongSession(digitalObject.getIdentifier()));
						}

                        if(!monitor.isProcessCancelled()) {
                            Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler());
						    batchDCHandler.export(selectedFileOrDirectory, digitalObjects, monitor);
                        }

                        access.closeLongSession();
					} catch (Exception e) {
						monitor.close();
						new ErrorDialog("", e).showDialog();
					} finally {
						monitor.close();
					}
				}
			}, "ExportDC");
			performer.start();
		}
    }
}