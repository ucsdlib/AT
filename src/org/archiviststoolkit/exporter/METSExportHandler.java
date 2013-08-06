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

import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.importer.ImportExportLogDialog;
import org.archiviststoolkit.model.DigitalObjects;
import org.archiviststoolkit.swing.ExportOptionsMETS;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

import org.archiviststoolkit.util.MyTimer;
import org.archiviststoolkit.util.StringHelper;
import edu.harvard.hul.ois.mets.helper.MetsException;

import javax.swing.*;

public class METSExportHandler implements Runnable {
    private File file = null;
    private ExportOptionsMETS exportOptions;
    private DigitalObjects digitalObject;

    public METSExportHandler(ExportOptionsMETS exportOptions) {
        super();
        this.exportOptions = exportOptions;
    }


    /**
     * Kick off the export thread.
     * @param exportFile the file to import
     * @return if we succeeded or not
     */
    public boolean startExportThread(File exportFile, 
                                     DigitalObjects digitalObject) {

        this.file = exportFile;
        this.digitalObject = digitalObject;
        new Thread(this).start();
        return (true);
    }

    public void run() {
        METSExport mets = new METSExport();
        //mets.convertDBRecordToFile(digitalObject, file, null, true);


    }

    public void export(File exportFile, DigitalObjects digitalObject, 
                       InfiniteProgressPanel progressPanel) {
        this.file = exportFile;
        this.digitalObject = digitalObject;
        METSExport mets = new METSExport();
        String metadataType = this.exportOptions.metadataTypeToInclude();
        boolean internalOnly = this.exportOptions.suppressInternalOnly();
        StringBuffer message = new StringBuffer();

        try {
            ApplicationFrame.getInstance().getTimer().reset();
            mets.convertDBRecordToFile(digitalObject, file, progressPanel, 
                                       internalOnly, metadataType);
            String timer = MyTimer.toString(ApplicationFrame.getInstance().getTimer().elapsedTimeMillisSplit());
            message.append("Summary"+"\n");
            message.append("-------"+"\n");
            message.append("Time to Export Record:\t"+timer+"\n\n");
            message.append("Output file:\t"+this.file.getAbsolutePath()+"\n\n");
            ImportExportLogDialog dialog = new ImportExportLogDialog(message.toString(), ImportExportLogDialog.DIALOG_TYPE_EXPORT);
			progressPanel.close();
			dialog.showDialog();
        } catch (Exception e) {
			progressPanel.close();

            if(e instanceof MetsException && e.getMessage().indexOf("dmnull") != -1) { // alert the user that they need to save the DO record befor exporting
                JOptionPane.showMessageDialog(null, "Please save record before exporting", "Export Error", JOptionPane.WARNING_MESSAGE);
            } else {
                ErrorDialog dialog =
                new ErrorDialog("There is an error with the export process. Please contact the system adminstrator",
                                StringHelper.getStackTrace(e));
                dialog.showDialog();
            }
        }
    }

    /**
     * Method to do batch export of digital object records to METS records
     *
     * @param exportFile The file or directory to export records to
     * @param digitalObjects Vector containing the digital objects to export
     * @param progressPanel Panel for displaying the progress of the export process
     */
    public void export(File exportFile, Vector<DigitalObjects> digitalObjects,
                       InfiniteProgressPanel progressPanel) {

        StringBuffer message = new StringBuffer();
        StringBuffer details = new StringBuffer();

        System.out.println("export File " + exportFile.getAbsolutePath());
        System.out.println("export size " + digitalObjects.size());

        if (digitalObjects.size() == 1) {
            export(exportFile, digitalObjects.get(0), progressPanel);
        } else {
            METSExport mets = new METSExport();
            String metadataType = this.exportOptions.metadataTypeToInclude();
            boolean internalOnly = this.exportOptions.suppressInternalOnly();

            // set the file extention to append to the end of the file depending on the
            // metadata type
            String fileExtension = "";
            if(metadataType.equals(ExportOptionsMETS.DC_METADATA)) {
                fileExtension = "-metsdc.xml"; // METS with DC descriptive metadata
            } else {
                fileExtension = "-metsmods.xml"; // must be METS with MODS descriptive data
            }

            ApplicationFrame.getInstance().getTimer().reset();

            int count = 0;
            int notCount = 0;
			int totalRecords = digitalObjects.size();
			int recordIndex = 1;

            for (DigitalObjects digitalObject: digitalObjects) {
                // check to see if the process was cancelled if so then just return right away
                if(progressPanel.isProcessCancelled()) {
                    return;
                }

                // set the digital object
                this.digitalObject = digitalObject;

                // set the file name of this digital object
                if(StringHelper.isNotEmpty(digitalObject.getMetsIdentifier())){
					String fileName = digitalObject.getMetsIdentifier();
					fileName = StringHelper.removeInvalidFileNameCharacters(fileName);
					this.file = new File(exportFile,fileName + fileExtension);
                }
                else {
                    this.file = new File(exportFile, digitalObject.getIdentifier() + fileExtension);
				    progressPanel.setTextLine("Exporting file  " + recordIndex++ + " of " + totalRecords + " - " +
												file.getAbsolutePath(), 1);
                }

                try {
                    mets.convertDBRecordToFile(digitalObject, file, progressPanel,
                                       internalOnly, metadataType);
                    count++;
                    details.append("Digital Object Id:" + digitalObject.getMetsIdentifier() + " -- " + "success" + " -- " + this.file.getName() + "\n" );
                } catch(Exception e) {
                    notCount++;
                    details.append("Digital Object Id:" + digitalObject.getMetsIdentifier() + " -- " + "failed\n");
                }
            }
            String timer = MyTimer.toString(ApplicationFrame.getInstance().getTimer().elapsedTimeMillisSplit());
            message.append("Summary"+"\n");
            message.append("-------"+"\n");
            message.append("Time to Export Record:\t"+timer+"\n");
            message.append("Total records exported:\t"+count+"\n");
            message.append("Total records not exported:\t"+notCount+"\n\n");
            message.append("Details\n");
            message.append("-------"+"\n");
            message.append(details);
            ImportExportLogDialog dialog = new ImportExportLogDialog(message.toString(), ImportExportLogDialog.DIALOG_TYPE_EXPORT);
			progressPanel.close();
			dialog.showDialog();
        }
    }

}
