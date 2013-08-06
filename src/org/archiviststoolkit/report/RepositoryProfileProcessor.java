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
 * Date: Oct 27, 2006
 * Time: 10:06:06 PM
 */

package org.archiviststoolkit.report;

import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.exceptions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.text.DateFormat;
import java.sql.SQLException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class RepositoryProfileProcessor {
	RepositoryProfile profile;
	InfiniteProgressPanel progressPanel;

	public void gatherStats(InfiniteProgressPanel progressPanel) {

		this.progressPanel = progressPanel;
		profile = new RepositoryProfile();

		ResourcesDAO resourcesDAO = new ResourcesDAO();
		AccessionsDAO accessionsDAO = new AccessionsDAO();
		NamesDAO namesDAO = new NamesDAO();
		SubjectsDAO subjectsDAO = new SubjectsDAO();

		try {
			Resources resource;
			Collection resources = resourcesDAO.findAllLongSession(true);
			int count = 1;
			for (Object o : resources) {
                // check to see if the process was cancelled  at this point if so just return
                if (progressPanel.isProcessCancelled()) {
                    return;
                }
				resource = (Resources) o;
                progressPanel.setTextLine("Processing resource " + count++ + " of " + resources.size(), 2);
				profile.addResource(resource, progressPanel);
			}
			resourcesDAO.closeLongSession();

			Accessions accession;
			Collection accessions = accessionsDAO.findAllLongSession(true);
			count = 1;
			for (Object o : accessions) {
                // check to see if the process was cancelled  at this point if so just return
                if (progressPanel.isProcessCancelled()) {
                    return;
                }
				accession = (Accessions) o;
                progressPanel.setTextLine("Processing accession " + count++ + " of " + accessions.size(), 2);
				profile.addAccession(accession);
			}
			accessionsDAO.closeLongSession();

            Names name;
            Collection names = namesDAO.findAllLongSession(true);
            count = 1;
            for (Object o : names) {
                // check to see if the process was cancelled  at this point if so just return
                if (progressPanel.isProcessCancelled()) {
                    return;
                }
                name = (Names) o;
                progressPanel.setTextLine("Processing name " + count++ + " of " + names.size(), 2);
                profile.addName(name);
            }
            namesDAO.closeLongSession();

            Subjects subject;
            Collection subjects = subjectsDAO.findAllLongSession(true);
            count = 1;
            for (Object o : subjects) {
                // check to see if the process was cancelled  at this point if so just return
                if (progressPanel.isProcessCancelled()) {
                    return;
                }
                subject = (Subjects) o;
                progressPanel.setTextLine("Processing subject " + count++ + " of " + subjects.size(), 2);
                profile.addSubject(subject);
            }
            subjectsDAO.closeLongSession();
		} catch (LookupException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
		} catch (SQLException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
		}
	}

	public void displayStats() throws JRException, UnsupportedParentComponentException {

            File reportFile = new File("reports/General/repositoryProfile/repositoryProfile.jrxml");

        try {
            CompiledJasperReport compiledReport = new CompiledJasperReport(reportFile);
            ArrayList<RepositoryProfile> profiles = new ArrayList<RepositoryProfile>();
            profiles.add(profile);
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);			
			ReportDestinationProperties properties = new ReportDestinationProperties(ApplicationFrame.getInstance(), ReportFactory.REPORT_DESTINATION_PREVIEW,
					"Comprehensive Repository Profile on " + df.format(new Date()), compiledReport);
			ReportUtils.printReport(properties, profiles, progressPanel);
		} catch (ReportCompilationException e) {
            new ErrorDialog(ApplicationFrame.getInstance(), "Error compiling report", e).showDialog();
        } catch (UnsupportedClassException e) {
			new ErrorDialog("Error generating report", e).showDialog();
		} catch (UnsupportedReportDestination unsupportedReportDestination) {
			new ErrorDialog("Error generating report", unsupportedReportDestination).showDialog();
		} catch (UnsupportedReportType unsupportedReportType) {
			new ErrorDialog("Error generating report", unsupportedReportType).showDialog();
		}
	}
}
