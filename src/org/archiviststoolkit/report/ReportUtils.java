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
 * Date: Sep 21, 2006
 * Time: 2:02:27 PM
 */

package org.archiviststoolkit.report;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JRSaveContributor;
import net.sf.jasperreports.view.save.*;
import org.apache.fop.apps.*;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.exceptions.UnsupportedParentComponentException;
import org.archiviststoolkit.exceptions.UnsupportedReportDestination;
import org.archiviststoolkit.exceptions.UnsupportedReportType;
import org.archiviststoolkit.exporter.EADExport;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.pdfbox.pdmodel.PDDocument;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ReportUtils {
	public static boolean printingJR = false; // used to prevent the prenting of tag in jasper reports

	private static void exportJasperReport(JasperPrint jasperPrint, String printDestination, File saveFile) throws JRException {
//		if (fileChooser.showSaveDialog(ApplicationFrame.getInstance()) == JFileChooser.APPROVE_OPTION) {
//			File saveFile = fileChooser.getSelectedFile();
//            File selectedFileOrDirectory = FileUtils.chooseFileOrDirectory(FileUtils.ONE_RECORD_TO_PRINT, new ExportOptionsEAD());
		JRSaveContributor saveContrib = null;
		if (printDestination.equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_HTML)) {
			saveContrib = new JRHtmlSaveContributor(null, null);
		} else if (printDestination.equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_PDF)) {
			saveContrib = new JRPdfSaveContributor(null, null);
		} else if (printDestination.equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_XML)) {
			saveContrib = new JRXmlSaveContributor(null, null);
		} else if (printDestination.equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_EXCEL)) {
			saveContrib = new JRSingleSheetXlsSaveContributor(null, null);
		} else if (printDestination.equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_RTF)) {
			saveContrib = new JRRtfSaveContributor(null, null);
		} else if (printDestination.equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_CSV)) {
			saveContrib = new JRCsvSaveContributor(null, null);
		}
		saveContrib.save(jasperPrint, saveFile);
//		}
	}

	public static void printReport(ReportDestinationProperties reportDestinationProperties,
								   Collection collectionForReport,
								   InfiniteProgressPanel progressPanel) throws UnsupportedParentComponentException, UnsupportedReportDestination, UnsupportedReportType {
		if (reportDestinationProperties.getSelectedReport() instanceof CompiledJasperReport) {
			printingJR = true;
			printJasperReport(reportDestinationProperties, collectionForReport, progressPanel);
			printingJR = false;
		} else if (reportDestinationProperties.getSelectedReport() instanceof PrintFindingAid) {
			printFindingAid(collectionForReport, progressPanel, reportDestinationProperties);
		} else {
			throw new UnsupportedReportType(reportDestinationProperties.getSelectedReport().getClass());
		}
	}

	public static void printJasperReport(ReportDestinationProperties reportDestinationProperties,
										 Collection recordsToPrint,
										 InfiniteProgressPanel progressPanel) throws UnsupportedParentComponentException {
		Window parent = reportDestinationProperties.getParent();
		String printDestination = reportDestinationProperties.getReportDesitation();
		CompiledJasperReport compiledJasperReport = (CompiledJasperReport) reportDestinationProperties.getSelectedReport();

		//JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(recordsToPrint);
		ATBeanCollectionDataSource ds = new ATBeanCollectionDataSource(recordsToPrint);
		Map parameters = new HashMap();

        // do a last check to see if the process was cancelled
        if(progressPanel.isProcessCancelled()) {
            return;
        }

		try {
			JasperReport jasperReport = compiledJasperReport.getJasperReport();
			parameters.put("basePath", compiledJasperReport.getBasePath());
			parameters.put("ReportHeader", reportDestinationProperties.getReportHeader());
			JasperPrint jasperPrint = null;
			jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ds);
			if (progressPanel != null) {
				progressPanel.close();
			}
			if (printDestination.equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_PRINTER)) {
				JasperPrintManager.printReport(jasperPrint, true);
			} else if (printDestination.equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_PREVIEW)) {
				JRViewerSimple dialog;
				if (parent instanceof Frame) {
					dialog = new JRViewerSimple(jasperPrint, (Frame) parent);
				} else if (parent instanceof Dialog) {
					dialog = new JRViewerSimple(jasperPrint, (Dialog) parent);
				} else {
					throw new UnsupportedParentComponentException();
				}
				progressPanel.close();
				dialog.showDialog();
			} else {
				exportJasperReport(jasperPrint, printDestination, reportDestinationProperties.reportSaveDestination);
			}
		} catch (JRException e) {
			new ErrorDialog("Error printing report", e).showDialog();
		}
	}

	private static void printFindingAid(Collection<DomainObject> collectionForReport,
										InfiniteProgressPanel progressPanel,
										ReportDestinationProperties reportDestinationProperties) throws UnsupportedReportDestination {
		final boolean createHTML;
		final String progressMessage;
		if (reportDestinationProperties.getReportDesitation().equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_HTML)) {
			createHTML = true;
			progressMessage = "Converting EAD to HTML";
		} else {
			createHTML = false;
			progressMessage = "Converting EAD to PDF";
		}
		final int numberOfRecords = collectionForReport.size();
		progressPanel.setTextLine(progressMessage, 2);
		EADExport exporter = new EADExport();

		int count = 0;
		for (DomainObject domainObject : collectionForReport) {
            // check to see if the process was cancelled if so just return
            if(progressPanel.isProcessCancelled()) {
                return;
            }

            count++;
			if (numberOfRecords == 1) {
				progressPanel.setTextLine("Generating EAD...", 1);
			} else {
				progressPanel.setTextLine("Generating EAD (record " + count + " of " + numberOfRecords + ")...", 1);
			}
			String eadXmlString = exporter.convertResourceToXML((Resources) domainObject, progressPanel,
					reportDestinationProperties.getFields().getSuppressInternalOnly(),
					reportDestinationProperties.getFields().getIncludeDaos(), false,
                    reportDestinationProperties.getFields().getUseDigitalObjectIDAsHREF());

			// check once again to see if the process was cancelled if so just return
            if(progressPanel.isProcessCancelled()) {
                return;
            }

            //Setup output
			try {

				try {

					//Setup XSLT
					TransformerFactory factory = TransformerFactory.newInstance();
					FileInputStream xsltStream;
					if (createHTML) {
						xsltStream = new FileInputStream("reports/Resources/eadToPdf/at_eadToHTML.xsl");
					} else if (((Resources) domainObject).getResourcesComponents().size() > 0) {
						xsltStream = new FileInputStream("reports/Resources/eadToPdf/at_eadToPDF.xsl");
					} else {
						xsltStream = new FileInputStream("reports/Resources/eadToPdf/at_eadToPDF.xsl");
					}
					Transformer transformer = factory.newTransformer(new StreamSource(xsltStream));

					//Setup input for XSLT transformation
					Source src = new StreamSource(new StringReader(eadXmlString));

					//Resulting SAX events (the generated FO) must be piped through to FOP
//					Result res = new SAXResult(driver.getContentHandler());

					//Start XSLT transformation and FOP processing
					if (numberOfRecords == 1) {
						progressPanel.setTextLine(progressMessage + "...", 1);
					} else {
						progressPanel.setTextLine(progressMessage + " (record " + count + " of " + numberOfRecords + ")...", 1);
					}
					if (createHTML) {
						FileOutputStream outputFile = new FileOutputStream(reportDestinationProperties.getSaveFilePath((Resources) domainObject));
						transformer.transform(src, new StreamResult(outputFile));
						outputFile.close();
					} else {
						// configure fopFactory as desired
						FopFactory fopFactory = FopFactory.newInstance();

						FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
						// configure foUserAgent as desired

//						try {
						// Construct fop with desired output format
						FileOutputStream outputFile = new FileOutputStream(reportDestinationProperties.getSaveFilePath((Resources) domainObject));
						Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, outputFile);

						// Set the value of a <param> in the stylesheet
						transformer.setParameter("versionParam", "2.0");

						// Resulting SAX events (the generated FO) must be piped through to FOP
						Result res = new SAXResult(fop.getDefaultHandler());

						// Start XSLT transformation and FOP processing
						transformer.transform(src, res);
						outputFile.close();
					}

				} catch (TransformerException e1) {
					new ErrorDialog("Error printing finding aid", e1).showDialog();
				} catch (FOPException e) {
					new ErrorDialog("Error printing finding aid", e).showDialog();
				} finally {
//					out.close();
				}
			} catch (IOException e1) {
				new ErrorDialog("Error printing finding aid", e1).showDialog();
			}
		}
	}


	public static void silentPrint(PrinterJob printJob, PDDocument document) throws PrinterException {
		printJob.setPageable(document);
		printJob.print();
	}
}
