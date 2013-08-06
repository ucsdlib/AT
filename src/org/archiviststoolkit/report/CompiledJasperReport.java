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
 * @author Lee Mandell
 * Date: Sep 18, 2006
 * Time: 1:39:28 PM
 */

package org.archiviststoolkit.report;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.design.JasperDesign;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.archiviststoolkit.exceptions.ReportCompilationException;

public class CompiledJasperReport extends ATReport {

    private JasperReport jasperReport;
	private String basePath = "";
	private ArrayList<JRField> fields;

	public CompiledJasperReport(JasperDesign jasperDesign) throws ReportCompilationException {

        init(jasperDesign);

    }

    public CompiledJasperReport(File file) throws ReportCompilationException, JRException {

        JasperDesign jasperDesign = JRXmlLoader.load(file);
        init(jasperDesign);
        basePath = file.getParent();
    }

    private void init(JasperDesign jasperDesign) throws ReportCompilationException {
		this.setReportType(REPORT_TYPE_JASPER);
		try {
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
        } catch (Exception e) {
            throw new ReportCompilationException(e);
        }
        setReportTitle(jasperReport.getProperty("reportTitle"));
        if (getReportTitle() == null || getReportTitle().length() == 0) {
            setReportTitle(jasperReport.getName());
        }
        setReportDescription(jasperReport.getProperty("reportDescription"));
        if (super.getReportDescription() == null) {
            setReportDescription("");
        }
		fields = new ArrayList<JRField>(Arrays.asList(jasperReport.getFields()));
	}

    public JasperReport getJasperReport() {
        return jasperReport;
    }

    public String getBasePath() {
        return basePath;
    }

	public ArrayList<JRField> getFields() {
		return fields;
	}

	public void setFields(ArrayList<JRField> fields) {
		this.fields = fields;
	}
}
