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
 * Date: Oct 27, 2007
 * Time: 12:31:31 PM
 */

package org.archiviststoolkit.report;

import org.archiviststoolkit.dialog.ReportDialog;
import org.archiviststoolkit.dialog.ATFileChooser;
import org.archiviststoolkit.dialog.ReportDialogFields;
import org.archiviststoolkit.exceptions.UnsupportedReportDestination;
import org.archiviststoolkit.exceptions.UnsupportedClassException;
import org.archiviststoolkit.exceptions.UnsupportedReportType;
import org.archiviststoolkit.swing.SimpleFileFilter;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.util.StringHelper;

import javax.swing.*;
import java.io.File;
import java.awt.print.PrinterJob;
import java.awt.*;

public class ReportDestinationProperties {

	public File reportSaveDestination = null;
	public Boolean multipleRecords = false;
	private PrinterJob printerJob = null;
	private String reportDestination;
	private String reportHeader;
	private CompiledJasperReport compiledJasperReport;
	private ReportDialog reportDialog;
	private Window parent;


	public ReportDestinationProperties(Class clazz, Frame parent) throws UnsupportedClassException {
		this.parent = parent;
		reportDialog = ReportFactory.getInstance().getReportPrinter(clazz, parent);
	}

	public ReportDestinationProperties(Frame parent,
									   String reportDestination, String reportHeader,
									   CompiledJasperReport compiledJasperReport) throws UnsupportedClassException {
		this.parent = parent;
		this.reportDestination = reportDestination;
		this.reportHeader = reportHeader;
		this.compiledJasperReport = compiledJasperReport;
		reportDialog = null;
	}

    public ReportDestinationProperties(Class clazz, Dialog parent, boolean includePrintScreen) throws UnsupportedClassException {
		this.parent = parent;
		reportDialog = ReportFactory.getInstance().getReportDialog(clazz, parent, includePrintScreen);
	}

	public ReportDestinationProperties(Class clazz, Dialog parent) throws UnsupportedClassException {
		this.parent = parent;
		reportDialog = ReportFactory.getInstance().getReportDialog(clazz, parent);
	}

	public boolean showDialog(int numberOfRecords) throws UnsupportedReportDestination, UnsupportedReportType {
		if (reportDialog.showDialog() == JOptionPane.OK_OPTION) {
			if (reportDialog.getSelectedReport() instanceof PrintFindingAid) {
				boolean continueProcessing = false;
				reportDestination = reportDialog.getReportDesitation();
				if (reportDestination.equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_PDF) ||
							reportDestination.equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_HTML)) {
						ATFileChooser fileChooser = new ATFileChooser();
						if (numberOfRecords == 1) {
							if (reportDestination.equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_PDF)) {
								fileChooser.setFileFilter(new SimpleFileFilter(".pdf"));
							} else {
								fileChooser.setFileFilter(new SimpleFileFilter(".html"));
							}
							fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
							if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
								continueProcessing = true;
								reportSaveDestination = fileChooser.getSelectedFile();
							}
						} else {
							fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
							if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
								continueProcessing = true;
								reportSaveDestination = fileChooser.getSelectedFile();
								multipleRecords = true;
							}
						}
					} else if (reportDialog.getReportDesitation().equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_PRINTER)) {
						printerJob = displayPrintSettings();
						if (printerJob != null) {
							continueProcessing = true;
						}
					} else {
						throw new UnsupportedReportDestination(reportDialog.getReportDesitation());
					}

				return continueProcessing;
			} else if (reportDialog.getSelectedReport() instanceof CompiledJasperReport){
				String printDestination = getReportDesitation();
				if (printDestination.equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_PRINTER) ||
						printDestination.equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_PREVIEW)) {
					return true;
				} else {
					ATFileChooser fileChooser = null;
					if (printDestination.equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_HTML)) {
						fileChooser = new ATFileChooser(new SimpleFileFilter(".html"));
					} else if (printDestination.equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_PDF)) {
						fileChooser = new ATFileChooser(new SimpleFileFilter(".pdf"));
					} else if (printDestination.equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_XML)) {
						fileChooser = new ATFileChooser(new SimpleFileFilter(".xml"));
					} else if (printDestination.equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_EXCEL)) {
						fileChooser = new ATFileChooser(new SimpleFileFilter(".xls"));
					} else if (printDestination.equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_RTF)) {
						fileChooser = new ATFileChooser(new SimpleFileFilter(".rtf"));
					} else if (printDestination.equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_CSV)) {
						fileChooser = new ATFileChooser(new SimpleFileFilter(".csv"));
					} else {
						throw new UnsupportedReportDestination(printDestination);
					}
					if (fileChooser.showSaveDialog(ApplicationFrame.getInstance()) == JFileChooser.APPROVE_OPTION) {
						reportSaveDestination = fileChooser.getSelectedFile();
						return true;
					} else {
						return false;
					}

				}
			} else throw new UnsupportedReportType(reportDialog.getSelectedReport().getClass());
		} else {
			return false;
		}
	}

	public String getSaveFilePath(Resources resource) {
		String fileName;
		if (multipleRecords) {
			fileName = resource.getResourceIdentifier();
			if (fileName.length() == 0) {
				fileName = resource.getResourceIdentifier();
			}
			fileName = StringHelper.convertToNmtoken(fileName);
//			fileName = fileName.replaceAll("/", ".");
			if (reportDestination.equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_PDF)) {
				fileName = reportSaveDestination.getPath() + "/" + fileName + ".pdf";
			} else {
				fileName = reportSaveDestination.getPath() + "/" + fileName + ".html";
			}
		} else {
			fileName = reportSaveDestination.getPath();
		}

		return fileName;
	}


	public PrinterJob getPrinterJob() {
		return printerJob;
	}

	public void setPrinterJob(PrinterJob printerJob) {
		this.printerJob = printerJob;
	}

	public PrinterJob displayPrintSettings() {
		PrinterJob printerJob = PrinterJob.getPrinterJob();
		if (printerJob.printDialog()) {
			return printerJob;
		} else {
			return null;
		}
	}

	public ATReport getSelectedReport() {
		if (reportDialog != null) {
			return reportDialog.getSelectedReport();
		} else {
			return compiledJasperReport;
		}
	}

	public String getReportDesitation() {
		if (reportDialog != null) {
			return reportDialog.getReportDesitation();
		} else {
			return reportDestination;
		}
	}

    public void setReportDesitation(String reportDistination) {
		if (reportDialog != null) {
			reportDialog.setReportDesitation(reportDistination);
		}
	}
    
	public ReportDialogFields getFields() {
		return reportDialog.getFields();
	}

	public Window getParent() {
		return parent;
	}

	public String getReportHeader() {
		if (reportDialog != null) {
			return reportDialog.getReportHeader();
		} else {
			return reportHeader;
		}
	}

}
