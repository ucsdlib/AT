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
 * Created by JFormDesigner on Thu Sep 14 13:55:06 EDT 2006
 */


package org.archiviststoolkit.dialog;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.report.CompiledJasperReport;
import org.archiviststoolkit.report.ReportFactory;
import org.archiviststoolkit.report.ATReport;
import org.archiviststoolkit.swing.StandardEditor;
import org.archiviststoolkit.importer.ImportUtils;
import org.archiviststoolkit.exceptions.ReportCompilationException;
import net.sf.jasperreports.engine.JRException;

public class ReportDialog extends JDialog {
    private String reportDistination;

    public ReportDialog(Frame owner) {
		super(owner);
		initComponents();
	}

	public ReportDialog(Dialog owner) {
		super(owner);
		initComponents();
	}

	private void cancelButtonActionPerformed() {
		status = JOptionPane.CANCEL_OPTION;
		this.setVisible(false);
	}

	private void printButtonActionPerformed() {
		status = JOptionPane.OK_OPTION;
		this.setVisible(false);
	}

	private void openReportFileButtonActionPerformed() {
		File reportFile = ImportUtils.chooseFile(this);
		 if (reportFile == null) {
			 return;
		 } else {
			 try {
				 userSelectedJasperReport = new CompiledJasperReport(reportFile);
			 } catch (JRException e) {
				 String errorMessage = "Please make sure the file is a valid Jasper Reports definition file and try again.";
				 new ErrorDialog(this, "Error compiling report: " + reportFile.getPath(), errorMessage, ErrorDialog.DIALOG_TYPE_ERROR).showDialog();
			 } catch (ReportCompilationException e) {
				 String errorMessage = "Please make sure the file is a valid Jasper Reports definition file and try again.";
				 new ErrorDialog(this, "Error compiling report: " + reportFile.getPath(), errorMessage, ErrorDialog.DIALOG_TYPE_ERROR).showDialog();
			 } catch (Exception e) {
				 String errorMessage = "Please make sure the file is a valid Jasper Reports definition file and try again.";
				 new ErrorDialog(this, "Error compiling report: " + reportFile.getPath(), errorMessage, ErrorDialog.DIALOG_TYPE_ERROR).showDialog();
              }
         }
	}

	public JButton getPrintButton() {
		return printButton;
	}

	private void previewButtonActionPerformed() {
		status = JOptionPane.OK_OPTION;
		previewButtonSelected = true;
		this.setVisible(false);
	}

	public JButton getPreviewButton() {
		return previewButton;
	}

	public JPanel getMainHeaderPanel() {
		return mainHeaderPanel;
	}

	public JLabel getMainHeaderLabel() {
		return mainHeaderLabel;
	}

	public JLabel getSubHeaderLabel() {
		return subHeaderLabel;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		dialogPane = new JPanel();
		HeaderPanel = new JPanel();
		mainHeaderPanel = new JPanel();
		mainHeaderLabel = new JLabel();
		subHeaderPanel = new JPanel();
		subHeaderLabel = new JLabel();
		mainPanel = new JPanel();
		buttonBar = new JPanel();
		openReportFileButton = new JButton();
		cancelButton = new JButton();
		previewButton = new JButton();
		printButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setModal(true);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(null);
			dialogPane.setBackground(new Color(200, 205, 232));
			dialogPane.setLayout(new BorderLayout());

			//======== HeaderPanel ========
			{
				HeaderPanel.setBackground(new Color(80, 69, 57));
				HeaderPanel.setOpaque(false);
				HeaderPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				HeaderPanel.setPreferredSize(new Dimension(300, 30));
				HeaderPanel.setLayout(new FormLayout(
					new ColumnSpec[] {
						new ColumnSpec(Sizes.bounded(Sizes.MINIMUM, Sizes.dluX(100), Sizes.dluX(200))),
						new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
					},
					RowSpec.decodeSpecs("fill:default")));

				//======== mainHeaderPanel ========
				{
					mainHeaderPanel.setBackground(new Color(80, 69, 57));
					mainHeaderPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					mainHeaderPanel.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.RELATED_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC
						},
						new RowSpec[] {
							FormFactory.RELATED_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.RELATED_GAP_ROWSPEC
						}));

					//---- mainHeaderLabel ----
					mainHeaderLabel.setText("Main Header");
					mainHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
					mainHeaderLabel.setForeground(Color.white);
					mainHeaderPanel.add(mainHeaderLabel, cc.xy(2, 2));
				}
				HeaderPanel.add(mainHeaderPanel, cc.xy(1, 1));

				//======== subHeaderPanel ========
				{
					subHeaderPanel.setBackground(new Color(66, 60, 111));
					subHeaderPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					subHeaderPanel.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.RELATED_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						},
						new RowSpec[] {
							FormFactory.RELATED_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.RELATED_GAP_ROWSPEC
						}));

					//---- subHeaderLabel ----
					subHeaderLabel.setText("Reports");
					subHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
					subHeaderLabel.setForeground(Color.white);
					subHeaderPanel.add(subHeaderLabel, cc.xy(2, 2));
				}
				HeaderPanel.add(subHeaderPanel, cc.xy(2, 1));
			}
			dialogPane.add(HeaderPanel, BorderLayout.NORTH);

			//======== mainPanel ========
			{
				mainPanel.setOpaque(false);
				mainPanel.setBorder(Borders.DIALOG_BORDER);
				mainPanel.setLayout(new BorderLayout());

				//======== buttonBar ========
				{
					buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
					buttonBar.setOpaque(false);
					buttonBar.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.GLUE_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.BUTTON_COLSPEC,
							FormFactory.RELATED_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.BUTTON_COLSPEC
						},
						RowSpec.decodeSpecs("pref")));

					//---- openReportFileButton ----
					openReportFileButton.setText("Load Report Definition File");
					openReportFileButton.setOpaque(false);
					openReportFileButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							openReportFileButtonActionPerformed();
						}
					});
					buttonBar.add(openReportFileButton, cc.xy(4, 1));

					//---- cancelButton ----
					cancelButton.setText("Cancel");
					cancelButton.setOpaque(false);
					cancelButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							cancelButtonActionPerformed();
						}
					});
					buttonBar.add(cancelButton, cc.xy(6, 1));

					//---- previewButton ----
					previewButton.setText("Preview");
					previewButton.setOpaque(false);
					previewButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							previewButtonActionPerformed();
						}
					});
					buttonBar.add(previewButton, cc.xy(8, 1));

					//---- printButton ----
					printButton.setText("Print");
					printButton.setOpaque(false);
					printButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							printButtonActionPerformed();
						}
					});
					buttonBar.add(printButton, cc.xy(10, 1));
				}
				mainPanel.add(buttonBar, BorderLayout.SOUTH);
			}
			dialogPane.add(mainPanel, BorderLayout.CENTER);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel dialogPane;
	private JPanel HeaderPanel;
	private JPanel mainHeaderPanel;
	private JLabel mainHeaderLabel;
	private JPanel subHeaderPanel;
	private JLabel subHeaderLabel;
	private JPanel mainPanel;
	private JPanel buttonBar;
	private JButton openReportFileButton;
	private JButton cancelButton;
	private JButton previewButton;
	private JButton printButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	private int status = 0;
	private ReportDialogFields fields;
	ATReport userSelectedJasperReport;
	Boolean previewButtonSelected;

	public final int showDialog() {

		userSelectedJasperReport = null;
		this.pack();
		setLocationRelativeTo(this.getOwner());
		previewButtonSelected = false;
		this.setVisible(true);

		return (status);

	}

	public String getReportHeader() {
		return fields.getReportHeader();
	}

	public String getReportDesitation() {
		if(previewButtonSelected == null) { // this happens when we need to run printscreen on different type of record
            return reportDistination;
        } else if (previewButtonSelected) {
			return ReportFactory.REPORT_DESTINATION_PREVIEW;
		} else {
			return fields.getReportDesitation();
		}
	}

    /**
     * Method to set the report distination. Used when printing screen preview using records
     * that are not those from the database as is the case with assessments and resources search results
     *
     * @param reportDistination The distination of the report
     */
    public void setReportDesitation(String reportDistination) {
		this.reportDistination = reportDistination;
	}

	public ATReport getSelectedReport() {
		if (userSelectedJasperReport != null) {
			return userSelectedJasperReport;
		} else {
			return fields.getSelectedReport();
		}
	}

	public ReportDialogFields getFields() {
		return fields;
	}

	public void setFields(ReportDialogFields fields, Class clazz) {
		this.fields = fields;
		fields.setParentDialog(this);
		mainPanel.add(fields, BorderLayout.CENTER);
		StandardEditor.setMainHeaderColorAndTextByClass(clazz, mainHeaderPanel, mainHeaderLabel);
	}

	protected void enableDisableButtons(ATReport report) {
		if (report instanceof CompiledJasperReport) {
			this.openReportFileButton.setEnabled(true);
			this.previewButton.setEnabled(true);
		} else {
			this.openReportFileButton.setEnabled(false);
			this.previewButton.setEnabled(false);
		}
	}
}
