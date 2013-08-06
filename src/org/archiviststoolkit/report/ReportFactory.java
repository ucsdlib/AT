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
 */

package org.archiviststoolkit.report;

//==============================================================================    
// Import Declarations
//==============================================================================    

import org.archiviststoolkit.model.*;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.exceptions.UnsupportedClassException;
import org.archiviststoolkit.exceptions.ReportCompilationException;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.dialog.ReportDialog;
import org.archiviststoolkit.dialog.ReportDialogFields;
import org.archiviststoolkit.swing.WorkSurfaceContainer;
import org.archiviststoolkit.mydomain.DomainTableFormat;
import org.archiviststoolkit.ApplicationFrame;

import java.util.*;
import java.io.File;
import java.io.InputStream;
import java.awt.*;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;


/**
 * A singleton in which we store the reports.
 */

public final class ReportFactory {

	/**
	 * resource bundle used for i18n of text messages.
	 */

//	private ResourceBundle resourceBundle = ResourceBundle.getBundle("org.archiviststoolkit.resources.messages.messages", Locale.getDefault(), ReportFactory.class.getClassLoader());

	public static final String REPORT_DESTINATION_PRINTER = "Printer";
	public static final String REPORT_DESTINATION_PREVIEW = "Preview";
	public static final String REPORT_DESTINATION_PDF = "PDF File";
	public static final String REPORT_DESTINATION_XML = "XML File";
	public static final String REPORT_DESTINATION_HTML = "HTML File";
	public static final String REPORT_DESTINATION_RTF = "RTF File";
	public static final String REPORT_DESTINATION_EXCEL = "Excel File";
	public static final String REPORT_DESTINATION_CSV = "CSV file";

	/**
	 * The singleton reference.
	 */

	private static ReportFactory singleton = null;

	private Hashtable<Class, Vector<ATReport>> compiledReportsLookup;

	private Hashtable<Class, ReportDialogFields> reportDialogFields;

	/**
	 * Default Constructor.
	 */

	private ReportFactory() {

        init();
	}

    private void init() {
        compiledReportsLookup = new Hashtable<Class, Vector<ATReport>>();
        try {
            initClassLookups(Subjects.class);
            initClassLookups(Names.class);
            initClassLookups(Accessions.class);
            initClassLookups(Resources.class);
            initClassLookups(Locations.class);
            initClassLookups(Assessments.class);
            initClassLookups(AssessmentsSearchResult.class);
            initClassLookups(DigitalObjects.class);
            initClassLookups(ResourcesComponentsSearchResult.class);
        } catch (UnsupportedClassException e) {
            new ErrorDialog(ApplicationFrame.getInstance(), "Error initializing reports", e).showDialog();
        } catch (JRException e) {
            new ErrorDialog(ApplicationFrame.getInstance(), "Error initializing reports", e).showDialog();
        }
    }

    private void initClassLookups(Class clazz) throws UnsupportedClassException, JRException {
		compiledReportsLookup.put(clazz, new Vector<ATReport>());
//		addPrintScreen(clazz);
	}

	private void initDialogFields() {
		reportDialogFields = new Hashtable<Class, ReportDialogFields>();
		for (Class clazz: compiledReportsLookup.keySet()) {
			try {
				ReportDialogFields fields = new ReportDialogFields();
				fields.setReportList(getReportListByClass(clazz));
				reportDialogFields.put(clazz, fields);
			} catch (UnsupportedClassException e) {
				new ErrorDialog(ApplicationFrame.getInstance(), "Error initializing report dialogs", e).showDialog();
			}
		}
	}

    /**
     * Singleton access method.
     *
     * @return the instance of this singleton
     */

    public static ReportFactory getInstance() {
        if (singleton == null) {
            singleton = new ReportFactory();
        }
        return (singleton);
    }

	private void sortReportListAndAddPrintScreen() {
		for (Vector<ATReport> list: compiledReportsLookup.values()) {
			Collections.sort(list);
		}
		for (Class clazz: compiledReportsLookup.keySet()) {
			try {
				addPrintScreen(clazz);
			} catch (UnsupportedClassException e) {
				new ErrorDialog(ApplicationFrame.getInstance(), "Error initializing reports", e).showDialog();
			} catch (JRException e) {
				new ErrorDialog(ApplicationFrame.getInstance(), "Error initializing reports", e).showDialog();
			} catch (ReportCompilationException e) {
                new ErrorDialog(ApplicationFrame.getInstance(), "Error initializing reports", e).showDialog();
            }
        }
	}

	private void addReport(Class clazz, ATReport report) throws UnsupportedClassException {
		Vector<ATReport> reportList = compiledReportsLookup.get(clazz);
		if (reportList != null) {
			reportList.add(report);
		} else {
			throw new UnsupportedClassException(clazz);
		}
	}

	private void addPrintScreen(Class clazz) throws UnsupportedClassException, JRException, ReportCompilationException {
        Vector<ATReport> reportList = compiledReportsLookup.get(clazz);
        if (reportList != null) {
            InputStream reportStream = this.getClass().getResourceAsStream("/org/archiviststoolkit/resources/reports/returnScreen.xml");
            JasperDesign jasperDesign = JRXmlLoader.load(reportStream);
            JRReturnScreen returnScreen;
            if (WorkSurfaceContainer.getWorkSurfaceByClass(clazz) == null) {
                returnScreen = new JRReturnScreen(jasperDesign, new DomainTableFormat(clazz));
            } else {
                returnScreen = new JRReturnScreen(jasperDesign, WorkSurfaceContainer.getWorkSurfaceByClass(clazz).getTableFormat());
            }
            jasperDesign.setProperty("ReportTitle", "Print screen");
            returnScreen.createHeaderAndDetail();
            reportList.add(0, new CompiledJasperReport(jasperDesign));
        } else {
            throw new UnsupportedClassException(clazz);
        }
    }

	public Vector<ATReport> getReportListByClass(Class clazz) throws UnsupportedClassException {
		Vector<ATReport> returnVector = compiledReportsLookup.get(clazz);
		if (returnVector != null) {
			return returnVector;
		} else {
			throw new UnsupportedClassException(clazz);
		}
	}

	public ReportDialog getReportPrinter(Class clazz, Frame owner) throws UnsupportedClassException {
		ReportDialogFields dialogFields = reportDialogFields.get(clazz);
		if (dialogFields != null) {
			ReportDialog reportDialog = new ReportDialog(owner);
			dialogFields.assignReportList(true);
			reportDialog.setFields(dialogFields, clazz);
			return reportDialog;
		} else {
			throw new UnsupportedClassException(clazz);
		}
	}

	public ReportDialog getReportDialog(Class clazz, Dialog owner) throws UnsupportedClassException {
		return getReportDialog(clazz, owner, false);
	}

	public ReportDialog getReportDialog(Class clazz, Dialog owner, boolean includePrintScreen) throws UnsupportedClassException {
		ReportDialogFields dialogFields = reportDialogFields.get(clazz);
		if (dialogFields != null) {
			ReportDialog reportDialog = new ReportDialog(owner);
			dialogFields.assignReportList(includePrintScreen);
			reportDialog.setFields(dialogFields, clazz);
			return reportDialog;
		} else {
			throw new UnsupportedClassException(clazz);
		}
	}

	public Vector<String> getOutputOptionList() {
		Vector<String> returnVector = new Vector<String>();
		returnVector.add(REPORT_DESTINATION_PRINTER);
//		returnVector.add(REPORT_DESTINATION_PREVIEW);
		returnVector.add(REPORT_DESTINATION_PDF);
//		returnVector.add(REPORT_DESTINATION_XML);
		returnVector.add(REPORT_DESTINATION_HTML);
		returnVector.add(REPORT_DESTINATION_RTF);
		returnVector.add(REPORT_DESTINATION_EXCEL);
		returnVector.add(REPORT_DESTINATION_CSV);

		return returnVector;
	}

	public Vector<String> getOutputOptionListFindingAids() {
		Vector<String> returnVector = new Vector<String>();
//		returnVector.add(REPORT_DESTINATION_PRINTER);
//		returnVector.add(REPORT_DESTINATION_PREVIEW);
		returnVector.add(REPORT_DESTINATION_PDF);
		returnVector.add(REPORT_DESTINATION_HTML);

		return returnVector;
	}

    public void ParseReportDirectory() {
        ParseReportDirectory(false);
    }


    public void ParseReportDirectory(boolean init) {
        if (init) {
            init();
        }
        File reportsDirectory = new File("reports");
		Class currentClass;
		for (File directory : reportsDirectory.listFiles()) {
			currentClass = null;
			if (directory.isDirectory()) {
				String directoryName = directory.getName();

                if (directoryName.equalsIgnoreCase("subjects")) {
					currentClass = Subjects.class;
				} else if (directoryName.equalsIgnoreCase("names")) {
					currentClass = Names.class;
				} else if (directoryName.equalsIgnoreCase("accessions")) {
					currentClass = Accessions.class;
				} else if (directoryName.equalsIgnoreCase("resources")) {
					currentClass = Resources.class;
				} else if (directoryName.equalsIgnoreCase("locations")) {
					currentClass = Locations.class;
				} else if (directoryName.equalsIgnoreCase("assessments")) {
					currentClass = Assessments.class;
                } else if (directoryName.equalsIgnoreCase("digitalobjects")) {
					currentClass = DigitalObjects.class;
                } else if (directoryName.equalsIgnoreCase("resourcescomponentssearchresult")) {
					currentClass = ResourcesComponentsSearchResult.class;
                }

                if (currentClass != null) {
					for (File file : directory.listFiles()) {
						if (StringHelper.getFileExtension(file).equalsIgnoreCase("xml") || StringHelper.getFileExtension(file).equalsIgnoreCase("jrxml")) {
							if (!lookForCompiledVersion(file)) {
								try {
									addReport(currentClass, new CompiledJasperReport(file));
								} catch (UnsupportedClassException e) {
									new ErrorDialog(ApplicationFrame.getInstance(), "The report:\n" + file.getPath() + "\nfailed to load and will not be available.", e).showDialog();
                                } catch (JRException e) {
                                    new ErrorDialog(ApplicationFrame.getInstance(), "The report:\n" + file.getPath() + "\nfailed to load and will not be available.", e).showDialog();
								} catch (ReportCompilationException e) {
                                    new ErrorDialog(ApplicationFrame.getInstance(), "The report:\n" + file.getPath() + "\nfailed to load and will not be available.", e).showDialog();
                                }
                            }
						}
					}
				}
			}
		}
		try {
			addReport(Resources.class, new PrintFindingAid());
		} catch (UnsupportedClassException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "Error adding finding aid printing", e).showDialog();
		}
		sortReportListAndAddPrintScreen();
		initDialogFields();
	}

	private boolean lookForCompiledVersion(File file) {
		String compiledVesionFilePath = StringHelper.getFilePathWithoutExtension(file) + ".jasper";
		return new File(compiledVesionFilePath).exists();
	}

	public boolean doesClassHaveReports(Class clazz) {
		return compiledReportsLookup.containsKey(clazz);
	}

	public void updatePrintScreens() {
		for (Vector<ATReport> reportList: compiledReportsLookup.values()) {
			if (reportList.size() > 0) {
				reportList.remove(0);
			}
		}
		for (Class clazz: compiledReportsLookup.keySet()) {
			try {
				addPrintScreen(clazz);
				initDialogFields();
			} catch (UnsupportedClassException e) {
				new ErrorDialog(ApplicationFrame.getInstance(), "Error updating print screens", e).showDialog();
			} catch (JRException e) {
				new ErrorDialog(ApplicationFrame.getInstance(), "Error updating print screens", e).showDialog();
			} catch (ReportCompilationException e) {
                new ErrorDialog(ApplicationFrame.getInstance(), "Error updating print screens", e).showDialog();
            }
        }
	}
}