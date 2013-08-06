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

package

org.archiviststoolkit.exporter;

import java.io.File;

import java.sql.SQLException;
import java.util.Vector;

import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.importer.ImportExportLogDialog;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.mydomain.DomainAccessObject;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.mydomain.ResourcesDAO;
import org.archiviststoolkit.swing.ExportOptionsEAD;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

import org.archiviststoolkit.util.MyTimer;
import org.archiviststoolkit.util.StringHelper;

public class EADExportHandler implements Runnable {
    public File file = null;
    Resources resource = null;
    private ExportOptionsEAD exportOptionsEAD;
    StringBuffer message = null;;


    public EADExportHandler(ExportOptionsEAD exportOptionsEAD) {
        super();
        this.exportOptionsEAD = exportOptionsEAD;
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
        EADExport ead = new EADExport();
    }

    public void export(File exportFile, Vector<DomainObject> resources, 
                       InfiniteProgressPanel progressPanel) {

        message = new StringBuffer();
        StringBuffer details = new StringBuffer();
        //if (exportFile.isFile() && resources.size() == 1) {
        if (resources.size() == 1) {
            export(exportFile,(Resources)resources.get(0),progressPanel);
        }

        else {
        	
            ApplicationFrame.getInstance().getTimer().reset();
            boolean val;
            int count=0,notCount=0;
			int totalRecords = resources.size();
			int recordIndex = 1;
			 String timer;
			for (DomainObject rs: resources) {
                // check to see if the process was cancelled if so then just return right away
                if(progressPanel.isProcessCancelled()) {
                    return;
                }

                Resources resource = (Resources)rs;
                if (exportFile.isDirectory()) {

					if(StringHelper.isNotEmpty(resource.getResourceIdentifier())){
						String fileName = resource.getResourceIdentifier();
						fileName = StringHelper.removeInvalidFileNameCharacters(fileName);
						this.file = new File(exportFile, fileName + "-ead.xml");
					}
                    else                        
                        this.file = new File(exportFile, resource.getIdentifier() + "-ead.xml");
					//update heartbeat message
					progressPanel.setTextLine("Exporting file  " + recordIndex++ + " of " + totalRecords + " - " +
													file.getAbsolutePath(), 1);

                } else {
					this.file = exportFile;
				}
                this.resource = resource;
                val = export(progressPanel,true);
				timer = MyTimer.toString(ApplicationFrame.getInstance().getTimer().elapsedTimeMillisSplit());
                if(val){
                    count++;
                    details.append("Resource Id:"+resource.getIdentifier()+" -- "+"success"+" -- "+this.file.getName()+ " -- " + timer + "\n");
                }
                else{ 
                    notCount++;
                    details.append("Resource Id:"+resource.getIdentifier()+" -- "+"failed" + " -- " + timer + "\n");
                }
            }

            timer = MyTimer.toString(ApplicationFrame.getInstance().getTimer().elapsedTimeMillis());
            message.append("Summary"+"\n");
            message.append("-------"+"\n");
            message.append("Time to Export Record:\t"+timer+"\n\n");
            message.append("Total records exported:\t"+count+"\n\n");            
            message.append("Total records not exported:\t"+notCount+"\n\n");            
            message.append("Details\n");
            message.append("-------"+"\n");
            message.append(details);
            ImportExportLogDialog dialog = new ImportExportLogDialog(message.toString(), ImportExportLogDialog.DIALOG_TYPE_EXPORT);
			progressPanel.close();
			dialog.showDialog();
        }

    }

    public void export(File exportFile, Resources resource, 
                       InfiniteProgressPanel progressPanel) {
                       
        message = new StringBuffer();
        this.file = exportFile;
        this.resource = resource;
        progressPanel.setTextLine("start the exporter", 2);
        progressPanel.setTextLine("export to file: " + file.getAbsolutePath(), 3);
        ApplicationFrame.getInstance().getTimer().reset();
        boolean success = export(progressPanel,false);
        String timer = MyTimer.toString(ApplicationFrame.getInstance().getTimer().elapsedTimeMillis());
        message.append("Summary"+"\n");
        message.append("-------"+"\n");
        message.append("Time to Export Record:\t"+timer+"\n\n");
        message.append("Output file:\t"+this.file.getAbsolutePath()+"\n\n");
        ImportExportLogDialog dialog = new ImportExportLogDialog(message.toString(), ImportExportLogDialog.DIALOG_TYPE_EXPORT);
        if(success) {
			progressPanel.close();
			dialog.showDialog();
		}
	}

    public boolean export(InfiniteProgressPanel progressPanel,boolean batchProcess) {
        DomainAccessObject access= new ResourcesDAO();
    	try {

            EADExport ead = new EADExport();
			Resources fullResourceObject = (Resources)access.findByPrimaryKeyLongSession(resource.getIdentifier());
            
            
            ead.convertResourceToFile(fullResourceObject, file, progressPanel,
                                      exportOptionsEAD.suppressInternalOnly(), 
                                      exportOptionsEAD.useNumberedComponents(), 
                                      exportOptionsEAD.includeDaos(), exportOptionsEAD.useDigitalObjectIDAsHREF());
            return true;

        } catch (Exception e) {
            if(!batchProcess){
				progressPanel.close();
                new ErrorDialog("", StringHelper.getStackTrace(e)).showDialog();
            } else {
                //todo error message must be added to the feedback
				progressPanel.close();
                //new ErrorDialog("", StringHelper.getStackTrace(e)).showDialog();
            }
            return false;
        }
    }


}
