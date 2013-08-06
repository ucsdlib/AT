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
 */

package org.archiviststoolkit.exporter;

import java.io.File;

import java.util.Vector;

import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.importer.ImportExportLogDialog;
import org.archiviststoolkit.model.DigitalObjects;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.swing.ExportOptionsMARC;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.util.StringHelper;

public class MARCBatchExportHandler implements Runnable {
    public File file = null;
    Resources resource = null;
    DigitalObjects digitalObject = null;
    ExportOptionsMARC exportOptions;

    public MARCBatchExportHandler(ExportOptionsMARC exportOptions) {
        super();
        this.exportOptions = exportOptions;
    }


    /**
     * Kick off the export thread.
     * @param exportFile the file to import
     * @return if we succeeded or not
     */
    public boolean startExportThread(File exportFile, Resources resource) {

        this.file = exportFile;
        this.resource = resource;
        new Thread(this).start();
        return (true);
    }

    public void run() {
        MARCExport marc = new MARCExport();
        //marc.convertDBRecordToFile(resource, file, null,true);


    }

    public void export(File exportFile, Vector<DomainObject> resources, 
                       InfiniteProgressPanel progressPanel) {
        //this.file = exportFile;
        System.out.println("FileDirectory = "+exportFile.getPath());
        MARCExport marc = new MARCExport();
        boolean suppressInternalOnly = exportOptions.suppressInternalOnly();
        System.out.println("suppressInternalOnly"+suppressInternalOnly);
        try {
            for(DomainObject rs:resources){
                Resources resource = (Resources)rs;                
                file = new File(exportFile, resource.getIdentifier()+".xml");

                marc.convertDBRecordToFile(resource, file, progressPanel, 
                                       suppressInternalOnly);
            }
            //ImportExportLogDialog dialog = new ImportExportLogDialog("Export Completed");
            //dialog.showDialog();
        } catch (Exception ex) {
            ErrorDialog dialog = 
                new ErrorDialog("There is an error with the export process. Please contact the system adminstrator",
                                StringHelper.getStackTrace(ex));
            dialog.showDialog();
        }
    }

    public void exportDO(File exportFile, DigitalObjects digObj, 
                         InfiniteProgressPanel progressPanel) {
        this.file = exportFile;
        this.digitalObject = digObj;
        MARCExport marc = new MARCExport();
        boolean internalOnly = exportOptions.suppressInternalOnly();
        try {
            marc.convertDBRecordToFileD(digObj, file, progressPanel, 
                                        internalOnly);
            ImportExportLogDialog dialog = new ImportExportLogDialog("Export Completed", ImportExportLogDialog.DIALOG_TYPE_EXPORT);
            dialog.showDialog();

        } catch (Exception ex) {
            ErrorDialog dialog = 
                new ErrorDialog("There is an error with the export of this document. Please contact the system adminstrator",
                                StringHelper.getStackTrace(ex));
            dialog.showDialog();
        }
    }


}
