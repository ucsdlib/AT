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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.swing.ExportOptionsEAD;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

import org.archiviststoolkit.util.StringHelper;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EADBatchExportHandler implements Runnable {
    public File file = null;
    Resources resource = null;
    private ExportOptionsEAD exportOptionsEAD;

    public EADBatchExportHandler(ExportOptionsEAD exportOptionsEAD) {
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
        //System.out.println("start the exporter");
        //System.out.println("export to file:"+file.getAbsolutePath());
        // try{
        //Document dom = null;
        Result result = new StreamResult(this.file);
        EADExport ead = new EADExport();
        //ead.convertDBRecordToFile(resource, file, null);
        //dom = ead.convertDBRecordToDOM(resource);
        //Source source = new DOMSource(dom);
        //Result result = new StreamResult(this.file);
        //Transformer transformer = TransformerFactory.newInstance().newTransformer();
        //transformer.transform(source,result);
        //}

        //catch(TransformerConfigurationException tce)
        //{
        //    tce.printStackTrace();
        //}
        //catch(TransformerException te)
        //{
        //    te.printStackTrace();
        //}   


    }

    public void export(File exportFile, Vector<DomainObject> resources, 
                       InfiniteProgressPanel progressPanel) {
        //this.file = exportFile;
        progressPanel.setTextLine("start the exporter", 2);

        boolean internalOnly = exportOptionsEAD.suppressInternalOnly();
        boolean numberedCs = exportOptionsEAD.useNumberedComponents();
        boolean includeDaos = exportOptionsEAD.includeDaos();
        boolean useDOIDAsHREF = exportOptionsEAD.useDigitalObjectIDAsHREF();

        EADExport ead = new EADExport();
		int count = 1;
		int totalRecords = resources.size();
		try {
            for(DomainObject rs:resources){
                Resources resource = (Resources)rs;
                if(exportFile.isDirectory()){
                    file = new File(exportFile, resource.getIdentifier()+".xml");
                }
                else {
                    file = new File(exportFile, resource.getIdentifier()+".xml");                    
                }
                progressPanel.setTextLine("Exporting file  " + count + " of " + totalRecords + " - " +
                                                file.getAbsolutePath(), 1);
                ead.convertResourceToFile(resource, file, progressPanel, 
                                      internalOnly, numberedCs, includeDaos, useDOIDAsHREF);
            }
        } catch (Exception e) {
            new ErrorDialog("", StringHelper.getStackTrace(e)).showDialog();
        }
    }


}
