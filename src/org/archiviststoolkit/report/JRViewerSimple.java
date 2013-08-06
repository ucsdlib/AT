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
 * Date: Dec 26, 2005
 * Time: 11:33:08 AM
 */

package org.archiviststoolkit.report;

import net.sf.jasperreports.view.JRViewer;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class JRViewerSimple extends JDialog {

	private JRViewer viewer = null;
	private javax.swing.JPanel pnlMain;

	/**
	 * Creates new form JasperViewer
	 */
	public JRViewerSimple(
			String sourceFile,
			boolean isXMLFile
	) throws JRException {

		initComponents();

		this.viewer = new JRViewer(sourceFile, isXMLFile);
		this.pnlMain.add(this.viewer, BorderLayout.CENTER);
	}


	/**
	 * Creates new form JasperViewer
	 */
	public JRViewerSimple(
			InputStream is,
			boolean isXMLFile
	) throws JRException {

		super();
		initComponents();

		this.viewer = new JRViewer(is, isXMLFile);
		this.pnlMain.add(this.viewer, BorderLayout.CENTER);
	}


	/**
	 * Creates new form JasperViewer
	 */
	public JRViewerSimple(
			JasperPrint jasperPrint,
			Dialog parent
	) {
		super(parent);
		initComponents();

		this.viewer = new JRViewer(jasperPrint);
		this.pnlMain.add(this.viewer, BorderLayout.CENTER);
	}


	/**
	 * Creates new form JasperViewer
	 */
	public JRViewerSimple(
			JasperPrint jasperPrint,
			Frame parent) {
		super(parent);
		initComponents();

		this.viewer = new JRViewer(jasperPrint);
		this.pnlMain.add(this.viewer, BorderLayout.CENTER);
	}


	/**
	 * Creates new form JasperViewer
	 */
	public JRViewerSimple(
			String sourceFile,
			boolean isXMLFile,
			boolean isExitOnClose
	) throws JRException {

		initComponents();

		this.viewer = new JRViewer(sourceFile, isXMLFile);
		this.pnlMain.add(this.viewer, BorderLayout.CENTER);
	}


	/**
	 * Creates new form JasperViewer
	 */
	public JRViewerSimple(
			InputStream is,
			boolean isXMLFile,
			boolean isExitOnClose
	) throws JRException {

		initComponents();

		this.viewer = new JRViewer(is, isXMLFile);
		this.pnlMain.add(this.viewer, BorderLayout.CENTER);
	}


	/**
	 * Creates new form JasperViewer
	 */
	public JRViewerSimple(
			JasperPrint jasperPrint,
			boolean isExitOnClose
	) {
		initComponents();

		this.viewer = new JRViewer(jasperPrint);
		this.pnlMain.add(this.viewer, BorderLayout.CENTER);
	}

	private void initComponents() {//GEN-BEGIN:initComponents
		pnlMain = new javax.swing.JPanel();

		setTitle("JasperViewer");
//		setIconImage(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jasperreports/view/images/jricon.GIF")).getImage());

		pnlMain.setLayout(new java.awt.BorderLayout());

		getContentPane().add(pnlMain, java.awt.BorderLayout.CENTER);

		pack();

		Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
		java.awt.Dimension screenSize = toolkit.getScreenSize();
		int screenResolution = toolkit.getScreenResolution();
		float zoom = ((float) screenResolution) / JRViewer.REPORT_RESOLUTION;

		int height = (int) (550 * zoom);
		if (height > screenSize.getHeight()) {
			height = (int) screenSize.getHeight();
		}
		int width = (int) (750 * zoom);
		if (width > screenSize.getWidth()) {
			width = (int) screenSize.getWidth();
		}

		java.awt.Dimension dimension = new java.awt.Dimension(width, height);
		setSize(dimension);
		setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2);
		setPreferredSize(new Dimension(750, 750));
	}

	public final void showDialog() {

		this.pack();

		setLocationRelativeTo(null);
		this.setVisible(true);

//		return (status);
	}


}
