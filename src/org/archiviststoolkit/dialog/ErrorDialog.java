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
 * Created by JFormDesigner on Fri Jan 06 16:01:27 EST 2006
 */

package org.archiviststoolkit.dialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.apache.log4j.Logger;
import org.archiviststoolkit.Main;
import org.archiviststoolkit.model.ModelUtil;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.util.StringHelper;

public class ErrorDialog extends JDialog {

	private static Logger logger = Logger.getLogger(Main.class.getPackage().getName());

	public static final int DIALOG_TYPE_BOZO = 1;
	public static final int DIALOG_TYPE_ERROR = 2;

	public ErrorDialog(String errorMessage, String errorText) {
		super();
		initComponents();
		setDisplay(errorMessage, errorText, DIALOG_TYPE_BOZO);
	}

	public ErrorDialog(String errorMessage, String errorText, int errorDialogType) {
		super();
		initComponents();
		setDisplay(errorMessage, errorText, errorDialogType);
	}

	public ErrorDialog(Frame owner, String errorMessage, String errorText, int errorDialogType) {
		super(owner);
		initComponents();
		setDisplay(errorMessage, errorText, errorDialogType);
	}

	public ErrorDialog(Dialog owner, String errorMessage, String errorText, int errorDialogType) {
		super(owner);
		initComponents();
		setDisplay(errorMessage, errorText, errorDialogType);
	}

	public ErrorDialog(String errorMessage) {
		super();
		initComponents();
		setDisplay(errorMessage, "", DIALOG_TYPE_BOZO);
	}

	public ErrorDialog(Frame owner, String errorMessage) {
		super(owner);
		initComponents();
		setDisplay(errorMessage, "", DIALOG_TYPE_BOZO);
	}

	public ErrorDialog(Dialog owner, String errorMessage) {
		super(owner);
		initComponents();
		setDisplay(errorMessage, "", DIALOG_TYPE_BOZO);
	}

	public ErrorDialog(String errorMessage, int errorDialogType) {
		super();
		initComponents();
		setDisplay(errorMessage, "", errorDialogType);
	}

	public ErrorDialog(Frame owner, String errorMessage, int errorDialogType) {
		super(owner);
		initComponents();
		setDisplay(errorMessage, "", errorDialogType);
	}

	public ErrorDialog(Dialog owner, String errorMessage, int errorDialogType) {
		super(owner);
		initComponents();
		setDisplay(errorMessage, "", errorDialogType);
	}

	public ErrorDialog(Throwable throwable) {
		super();
		initComponents();
		setDisplayBasedOnError(null, throwable);

	}

	public ErrorDialog(Dialog owner, Throwable throwable) {
		super(owner);
		initComponents();
		setDisplayBasedOnError(null, throwable);

	}

	public ErrorDialog(Frame owner, Throwable throwable) {
		super(owner);
		initComponents();
		setDisplayBasedOnError(null, throwable);

	}

	public ErrorDialog(String message, Throwable throwable) {
		super();
		initComponents();
		setDisplayBasedOnError(message, throwable);
	}

	public ErrorDialog(String message, Throwable throwable, int errorDialogType) {
		super();
		initComponents();
		setDisplay(message, throwable.getMessage(), errorDialogType);
	}

	public ErrorDialog(Frame owner, String message, Throwable throwable) {
		super(owner);
		initComponents();
		setDisplayBasedOnError(message, throwable);

	}

	public ErrorDialog(Dialog owner, String message, Throwable throwable) {
		super(owner);
		initComponents();
		setDisplayBasedOnError(message, throwable);

	}

	private void setDisplayBasedOnError(String message, Throwable throwable) {
		this.throwable = throwable;

		if (throwable instanceof DuplicateLinkException) {
			setDisplay(message, throwable.getMessage(), DIALOG_TYPE_ERROR);
		} else {
			setDisplay(StringHelper.concat(": ", message, throwable.getMessage()), StringHelper.getStackTrace(throwable), DIALOG_TYPE_BOZO);
		}
	}

	private void setDisplay(String errorMessage, String errorText, int errorDialogType) {
        // first check to see if the error text is not a JDBC connection exception
        // if it is then only alert user of this and don't allow user to submit
        // error report
        if(errorText.contains("JDBCConnectionException")) {
            String message = "Database connection has been lost due to a server timeout.\n\n" +
                "Please RESTART the program to continue.  If the problem persists, consult your System Administrator.";

            cardPanel.remove(contentPanel);
            
            ((CardLayout) cardPanel.getLayout()).show(cardPanel, "error");

            this.label4.setText("Connection Interruption!");
            this.errorMessage.setText(message);

            submitBugReport.setVisible(false);
        } else if (errorDialogType == DIALOG_TYPE_ERROR) {
			((CardLayout) cardPanel.getLayout()).show(cardPanel, "error");
			this.errorMessage.setText(errorMessage + "\n" + errorText);
			submitBugReport.setVisible(false);
		} else if (errorDialogType == DIALOG_TYPE_BOZO){
			this.errorMessageBozo.setText(errorMessage);
			this.errorText.setText(errorText);
		}

        pack();
	}

	public ErrorDialog(Frame owner) {
		super(owner);
		initComponents();
	}

	public ErrorDialog(Dialog owner) {
		super(owner);
		initComponents();
	}

	private void submitBugReportActionPerformed(ActionEvent e) {
		JiraReportDialog dialog = new JiraReportDialog(this);
		if (throwable == null) {
			dialog.setStackTrace(errorMessage.getText() + "\n" + errorText.getText());
		} else {
			dialog.setStackTrace(throwable.getMessage() + "\n" + StringHelper.getStackTrace(throwable));
		}
		if (dialog.showDialog() == JOptionPane.OK_OPTION) {
			//String message = "Your bug has been submitted and assiged a number of " + dialog.getRemoteIssue().getKey() +
					//"\n\nYou can view all issues at:\nhttps://jira.nyu.edu:8443/jira/browse/ART";
			
                        String message =    "Your bug has been submitted and assiged a number of " + dialog.getIssueId() +
                                            "\n\nYou can view all issues at:\nhttps://jira.nyu.edu:8443/jira/browse/ART";
                        JOptionPane.showMessageDialog(this, message);
			setVisible(false);
		}
	}

	private void toggleDetailsActionPerformed() {
		toggleTextAndView();
		this.pack();
		this.invalidate();
		this.validate();
		this.repaint();
	}

	private void toggleTextAndView() {
		if (toggleDetails.isSelected()) {
			errorTextScrollPane.setVisible(false);
			toggleDetailsLabel.setText("Show Details");
		} else {
			errorTextScrollPane.setVisible(true);
			toggleDetailsLabel.setText("Hide Details");
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        cardPanel = new JPanel();
        contentPanel = new JPanel();
        panel2 = new JPanel();
        label2 = new JLabel();
        label1 = new JLabel();
        errorMessageScrollPane2 = new JScrollPane();
        errorMessageBozo = new JTextArea();
        panel1 = new JPanel();
        toggleDetails = new JToggleButton();
        toggleDetailsLabel = new JLabel();
        errorTextScrollPane = new JScrollPane();
        errorText = new JTextArea();
        errorPanel = new JPanel();
        label3 = new JLabel();
        label4 = new JLabel();
        errorMessageScrollPane = new JScrollPane();
        errorMessage = new JTextArea();
        buttonBar = new JPanel();
        submitBugReport = new JButton();
        okButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setModal(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(Borders.DIALOG_BORDER);
            dialogPane.setBackground(new Color(200, 205, 232));
            dialogPane.setLayout(new BorderLayout());

            //======== cardPanel ========
            {
                cardPanel.setOpaque(false);
                cardPanel.setLayout(new CardLayout());

                //======== contentPanel ========
                {
                    contentPanel.setOpaque(false);
                    contentPanel.setPreferredSize(new Dimension(682, 450));
                    contentPanel.setLayout(new FormLayout(
                        ColumnSpec.decodeSpecs("max(default;600px):grow"),
                        new RowSpec[] {
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        }));

                    //======== panel2 ========
                    {
                        panel2.setOpaque(false);
                        panel2.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC
                            },
                            RowSpec.decodeSpecs("default")));

                        //---- label2 ----
                        label2.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/bug.png")));
                        panel2.add(label2, cc.xy(1, 1));

                        //---- label1 ----
                        label1.setText("An error has occured");
                        panel2.add(label1, cc.xywh(3, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));
                    }
                    contentPanel.add(panel2, cc.xy(1, 1));

                    //======== errorMessageScrollPane2 ========
                    {
                        errorMessageScrollPane2.setOpaque(false);
                        errorMessageScrollPane2.setBorder(null);

                        //---- errorMessageBozo ----
                        errorMessageBozo.setBackground(new Color(200, 205, 232));
                        errorMessageBozo.setBorder(null);
                        errorMessageBozo.setLineWrap(true);
                        errorMessageBozo.setWrapStyleWord(true);
                        errorMessageBozo.setRows(2);
                        errorMessageBozo.setEditable(false);
                        errorMessageScrollPane2.setViewportView(errorMessageBozo);
                    }
                    contentPanel.add(errorMessageScrollPane2, cc.xy(1, 3));

                    //======== panel1 ========
                    {
                        panel1.setOpaque(false);
                        panel1.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC
                            },
                            RowSpec.decodeSpecs("default")));

                        //---- toggleDetails ----
                        toggleDetails.setOpaque(false);
                        toggleDetails.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/toggleDown.png")));
                        toggleDetails.setBorder(null);
                        toggleDetails.setSelectedIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/toggleUp.png")));
                        toggleDetails.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                toggleDetailsActionPerformed();
                            }
                        });
                        panel1.add(toggleDetails, cc.xy(1, 1));

                        //---- toggleDetailsLabel ----
                        toggleDetailsLabel.setText("Show Details");
                        panel1.add(toggleDetailsLabel, cc.xy(3, 1));
                    }
                    contentPanel.add(panel1, cc.xy(1, 5));

                    //======== errorTextScrollPane ========
                    {
                        errorTextScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                        //---- errorText ----
                        errorText.setRows(15);
                        errorText.setLineWrap(true);
                        errorText.setEditable(false);
                        errorTextScrollPane.setViewportView(errorText);
                    }
                    contentPanel.add(errorTextScrollPane, cc.xywh(1, 7, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
                }
                cardPanel.add(contentPanel, "bozo");

                //======== errorPanel ========
                {
                    errorPanel.setOpaque(false);
                    errorPanel.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        },
                        new RowSpec[] {
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        }));

                    //---- label3 ----
                    label3.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/error.png")));
                    errorPanel.add(label3, cc.xy(1, 1));

                    //---- label4 ----
                    label4.setText("An error has occured");
                    errorPanel.add(label4, cc.xywh(3, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

                    //======== errorMessageScrollPane ========
                    {
                        errorMessageScrollPane.setOpaque(false);
                        errorMessageScrollPane.setBorder(null);

                        //---- errorMessage ----
                        errorMessage.setBackground(new Color(200, 205, 232));
                        errorMessage.setBorder(null);
                        errorMessage.setLineWrap(true);
                        errorMessage.setWrapStyleWord(true);
                        errorMessage.setRows(4);
                        errorMessage.setEditable(false);
                        errorMessageScrollPane.setViewportView(errorMessage);
                    }
                    errorPanel.add(errorMessageScrollPane, cc.xywh(1, 3, 3, 1));
                }
                cardPanel.add(errorPanel, "error");
            }
            dialogPane.add(cardPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
                buttonBar.setOpaque(false);
                buttonBar.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        FormFactory.GLUE_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.BUTTON_COLSPEC
                    },
                    RowSpec.decodeSpecs("pref")));

                //---- submitBugReport ----
                submitBugReport.setText("Submit Bug Report");
                submitBugReport.setOpaque(false);
                submitBugReport.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        submitBugReportActionPerformed(e);
                    }
                });
                buttonBar.add(submitBugReport, cc.xy(2, 1));

                //---- okButton ----
                okButton.setText("OK");
                okButton.setOpaque(false);
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        okButtonActionPerformed(e);
                    }
                });
                buttonBar.add(okButton, cc.xy(6, 1));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		toggleDetails.setSelected(true);
		toggleTextAndView();

	}

	private void okButtonActionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel cardPanel;
    private JPanel contentPanel;
    private JPanel panel2;
    private JLabel label2;
    private JLabel label1;
    private JScrollPane errorMessageScrollPane2;
    private JTextArea errorMessageBozo;
    private JPanel panel1;
    private JToggleButton toggleDetails;
    private JLabel toggleDetailsLabel;
    private JScrollPane errorTextScrollPane;
    private JTextArea errorText;
    private JPanel errorPanel;
    private JLabel label3;
    private JLabel label4;
    private JScrollPane errorMessageScrollPane;
    private JTextArea errorMessage;
    private JPanel buttonBar;
    private JButton submitBugReport;
    private JButton okButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	private Throwable throwable = null;

	public void setErrorText (String errorText) {
		this.errorText.setText(errorText);
	}

    public void setErrorMessage (String errorMessage) {
        this.errorMessage.setText(errorMessage);
    }

    public final void showDialog() {

		//send to logger
		if (throwable == null) {
			logger.error(errorMessage.getText() + "\n" + errorText.getText());
		} else {
			logger.error(throwable.getMessage(), throwable);
		}
		this.pack();
		setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}
}
