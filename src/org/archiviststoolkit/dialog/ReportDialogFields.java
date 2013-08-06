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
 * Created by JFormDesigner on Tue Sep 26 15:13:36 EDT 2006
 */

package org.archiviststoolkit.dialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.report.*;
import org.archiviststoolkit.swing.ATBasicComponentFactory;

import java.util.Vector;

public class ReportDialogFields extends JPanel {

	private ReportDialog parentDialog;

	public ReportDialogFields() {
		initComponents();
		initOutputComboBoxes();
	}

	private void outputOptionsActionPerformed() {
		setPrintButtonText();
	}

	private void setPrintButtonText() {
		String selectedOutputOption = (String)outputOptions.getSelectedItem();
		if (selectedOutputOption.equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_PRINTER) ||
				selectedOutputOption.equalsIgnoreCase(ReportFactory.REPORT_DESTINATION_PREVIEW)) {
			parentDialog.getPrintButton().setText("Print");
			parentDialog.enableDisableButtons(getSelectedReport());
		} else {
			parentDialog.getPrintButton().setText("Save");
			parentDialog.getPreviewButton().setEnabled(false);
		}
	}

	private void reportListActionPerformed() {
		setReportDescription();
		setPrintButtonText();
		showHideElements();
	}

	protected void showHideElements() {
		if (getSelectedReport() instanceof PrintFindingAid) {
			this.findingAidOptionsPanel.setVisible(true);
			this.reportHeaderPanel.setVisible(false);
			this.outputOptions.setModel(outputOptionsFindingAid);
		} else {
			this.findingAidOptionsPanel.setVisible(false);
			this.reportHeaderPanel.setVisible(true);
			this.outputOptions.setModel(outputOptionsAll);
		}
		if (parentDialog != null) {
			parentDialog.enableDisableButtons(getSelectedReport());
		}
	}

	private void initOutputComboBoxes() {
		outputOptionsAll = new DefaultComboBoxModel(ReportFactory.getInstance().getOutputOptionList());
		outputOptionsFindingAid = new DefaultComboBoxModel(ReportFactory.getInstance().getOutputOptionListFindingAids());
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label1 = new JLabel();
        reportList = new JComboBox();
        reportHeaderPanel = new JPanel();
        label2 = new JLabel();
        reportHeader = new JTextField();
        findingAidOptionsPanel = new JPanel();
        suppressInternalOnly = new JCheckBox();
        panel1 = new JPanel();
        includeDaos = new JCheckBox();
        useDOIDAsHREF = new JCheckBox();
        label3 = new JLabel();
        outputOptions = new JComboBox();
        label4 = new JLabel();
        scrollPane1 = new JScrollPane();
        reportDescription = new JTextArea();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setOpaque(false);
        setPreferredSize(new Dimension(600, 171));
        setLayout(new FormLayout(
            new ColumnSpec[] {
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
            },
            new RowSpec[] {
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
            }));

        //---- label1 ----
        label1.setText("Select Report");
        add(label1, cc.xy(1, 1));

        //---- reportList ----
        reportList.setOpaque(false);
        reportList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reportListActionPerformed();
            }
        });
        add(reportList, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //======== reportHeaderPanel ========
        {
            reportHeaderPanel.setOpaque(false);
            reportHeaderPanel.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                },
                RowSpec.decodeSpecs("default")));

            //---- label2 ----
            label2.setText("Report Header");
            reportHeaderPanel.add(label2, cc.xy(1, 1));
            reportHeaderPanel.add(reportHeader, cc.xy(3, 1));
        }
        add(reportHeaderPanel, cc.xywh(1, 3, 3, 1));

        //======== findingAidOptionsPanel ========
        {
            findingAidOptionsPanel.setOpaque(false);
            findingAidOptionsPanel.setVisible(false);
            findingAidOptionsPanel.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC
                },
                RowSpec.decodeSpecs("top:default")));

            //---- suppressInternalOnly ----
            suppressInternalOnly.setText("<html>Suppress components and notes <br>when marked \"internal only\"? </html>");
            suppressInternalOnly.setVerticalAlignment(SwingConstants.TOP);
            suppressInternalOnly.setOpaque(false);
            findingAidOptionsPanel.add(suppressInternalOnly, cc.xy(1, 1));

            //======== panel1 ========
            {
                panel1.setOpaque(false);
                panel1.setLayout(new FormLayout(
                    ColumnSpec.decodeSpecs("default"),
                    new RowSpec[] {
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC
                    }));

                //---- includeDaos ----
                includeDaos.setText(" Include DAO's in output?");
                includeDaos.setOpaque(false);
                panel1.add(includeDaos, cc.xy(1, 1));

                //---- useDOIDAsHREF ----
                useDOIDAsHREF.setText("Use Digital Object ID as HREF?");
                panel1.add(useDOIDAsHREF, cc.xy(1, 3));
            }
            findingAidOptionsPanel.add(panel1, cc.xy(3, 1));
        }
        add(findingAidOptionsPanel, cc.xywh(1, 5, 3, 1));

        //---- label3 ----
        label3.setText("Select Output");
        add(label3, cc.xy(1, 7));

        //---- outputOptions ----
        outputOptions.setOpaque(false);
        outputOptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputOptionsActionPerformed();
            }
        });
        add(outputOptions, cc.xywh(3, 7, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- label4 ----
        label4.setText("Report Description");
        add(label4, cc.xy(1, 9));

        //======== scrollPane1 ========
        {
            scrollPane1.setBorder(null);
            scrollPane1.setOpaque(false);

            //---- reportDescription ----
            reportDescription.setRows(5);
            reportDescription.setBorder(null);
            reportDescription.setOpaque(false);
            reportDescription.setEditable(false);
            reportDescription.setDragEnabled(false);
            reportDescription.setFocusable(false);
            reportDescription.setWrapStyleWord(true);
            reportDescription.setLineWrap(true);
            scrollPane1.setViewportView(reportDescription);
        }
        add(scrollPane1, cc.xywh(3, 9, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}


	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label1;
    private JComboBox reportList;
    private JPanel reportHeaderPanel;
    private JLabel label2;
    private JTextField reportHeader;
    private JPanel findingAidOptionsPanel;
    private JCheckBox suppressInternalOnly;
    private JPanel panel1;
    private JCheckBox includeDaos;
    private JCheckBox useDOIDAsHREF;
    private JLabel label3;
    private JComboBox outputOptions;
    private JLabel label4;
    private JScrollPane scrollPane1;
    private JTextArea reportDescription;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	private DefaultComboBoxModel reportListWithPrintScreen;
	private DefaultComboBoxModel reportListWithoutPrintScreen;

	private DefaultComboBoxModel outputOptionsAll;
	private DefaultComboBoxModel outputOptionsFindingAid;


	public void setReportList(Vector<ATReport> reportList) {
		reportListWithPrintScreen = new DefaultComboBoxModel(reportList);
		Vector<ATReport> clone = (Vector<ATReport>)reportList.clone();
		if (clone.size() > 0) {
			clone.remove(0);
		}
		reportListWithoutPrintScreen = new DefaultComboBoxModel(clone);
	}

	public void assignReportList(boolean includePrintScreen) {
		if (includePrintScreen) {
			reportList.setModel(reportListWithPrintScreen);
		} else {
			reportList.setModel(reportListWithoutPrintScreen);
		}
		showHideElements();
	}

	public String getReportHeader() {
		return reportHeader.getText();
	}

	public String getReportDesitation() {
		return (String)outputOptions.getSelectedItem();
	}

	public ATReport getSelectedReport() {
		return (ATReport)reportList.getSelectedItem();
	}

	public void setParentDialog(ReportDialog parentDialog) {
		this.parentDialog = parentDialog;
		setPrintButtonText();
		setReportDescription();
	}

	private void setReportDescription() {
		ATReport reportDefinition = (ATReport)reportList.getSelectedItem();
        if(reportDefinition != null) {
		    reportDescription.setText(reportDefinition.getReportDescription());
        }
	}

	public Boolean getSuppressInternalOnly() {
		return this.suppressInternalOnly.isSelected();
	}

	public Boolean getIncludeDaos() {
		return this.includeDaos.isSelected();
	}

    public Boolean getUseDigitalObjectIDAsHREF() {
        return this.useDOIDAsHREF.isSelected();
    }
}
