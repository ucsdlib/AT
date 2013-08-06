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
 * Created by JFormDesigner on Mon Jul 31 20:31:46 EDT 2006
 */

package org.archiviststoolkit.dialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import org.archiviststoolkit.swing.ATBasicDialog;
import org.archiviststoolkit.model.Users;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.exceptions.ATClient;

/**
 * @author Lee Mandell
 */
public class JiraReportDialog extends ATBasicDialog {
	public JiraReportDialog(Frame owner) {
		super(owner);
		initComponents();
		populateUserInfo();
	}

	public JiraReportDialog(Dialog owner) {
		super(owner);
		initComponents();
		populateUserInfo();
	}

	private void populateUserInfo() {
		Users currentUser = ApplicationFrame.getInstance().getCurrentUser();
		if (currentUser.getEmail() != null) {
			emailAddress.setText(currentUser.getEmail());
		}
		if (currentUser.getFullName() != null) {
			fullName.setText(currentUser.getFullName());
		}
	}

	private void sendButtonActionPerformed(ActionEvent e) {
		boolean sendIssue = true;

		if (summary.getText().length() == 0) {
			JOptionPane.showMessageDialog(this, "You must enter a summary for this issue");
			sendIssue = false;
		} else if (detailedDescription.getText().length() == 0) {
			if (JOptionPane.showConfirmDialog(this, "You have not entered a detailed description.\nMany times the " +
					"detail will help us track down the problem. \nAre you sure you want to send the issue?",
					"", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION) {
				sendIssue = false;

			}
		} else if (emailAddress.getText().length() == 0) {
			if (JOptionPane.showConfirmDialog(this, "You have not entered an email address. \nWe need an email " +
					"email address in case we need more information about the issue you are reporting. \nAre you sure you want to send the issue?",
					"", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION) {
				sendIssue = false;
			}
		}


		if (sendIssue) {
			try {
                                issueId = ATClient.processReport(summary.getText(),detailedDescription.getText(),stackTrace,fullName.getText(),emailAddress.getText(),ApplicationFrame.gatherSystemInformation(),ApplicationFrame.getInstance().getIssueReportingURL());
                                //remoteIssue = SoapClient.createIssue(summary.getText(), detailedDescription.getText(), stackTrace, fullName.getText(), emailAddress.getText());
			} /*catch (ServiceException e1) {
				new ErrorDialog(this, "Error submitting bug report", e1).showDialog();
			} catch (java.rmi.RemoteException e1) {
				new ErrorDialog(this, "Error submitting bug report", e1).showDialog();
			}*/ catch (Exception e1) {
				new ErrorDialog(this, "Error submitting bug report", e1).showDialog();
			}
                        
			performOkAction();
		}
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		performCancelAction();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        panel2 = new JPanel();
        label3 = new JLabel();
        label4 = new JLabel();
        label1 = new JLabel();
        summary = new JTextField();
        label2 = new JLabel();
        scrollPane1 = new JScrollPane();
        detailedDescription = new JTextArea();
        panel1 = new JPanel();
        label5 = new JLabel();
        emailAddress = new JTextField();
        label6 = new JLabel();
        fullName = new JTextField();
        buttonBar = new JPanel();
        cancelButton = new JButton();
        sendButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(Borders.DIALOG_BORDER);
            dialogPane.setBackground(new Color(200, 205, 232));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setOpaque(false);
                contentPanel.setLayout(new FormLayout(
                    ColumnSpec.decodeSpecs("max(default;600px):grow"),
                    new RowSpec[] {
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.UNRELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC
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

                    //---- label3 ----
                    label3.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/bug.png")));
                    panel2.add(label3, cc.xy(1, 1));

                    //---- label4 ----
                    label4.setText("Bug Reporter");
                    label4.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
                    panel2.add(label4, cc.xywh(3, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));
                }
                contentPanel.add(panel2, cc.xy(1, 1));

                //---- label1 ----
                label1.setText("Please enter a brief summary...");
                label1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                contentPanel.add(label1, cc.xy(1, 3));
                contentPanel.add(summary, cc.xy(1, 5));

                //---- label2 ----
                label2.setText("...and a detailed description");
                label2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                contentPanel.add(label2, cc.xy(1, 7));

                //======== scrollPane1 ========
                {
                    scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                    //---- detailedDescription ----
                    detailedDescription.setRows(20);
                    detailedDescription.setLineWrap(true);
                    scrollPane1.setViewportView(detailedDescription);
                }
                contentPanel.add(scrollPane1, cc.xy(1, 9));

                //======== panel1 ========
                {
                    panel1.setOpaque(false);
                    panel1.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        },
                        new RowSpec[] {
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC
                        }));

                    //---- label5 ----
                    label5.setText("Email Address");
                    panel1.add(label5, cc.xy(1, 1));
                    panel1.add(emailAddress, cc.xy(3, 1));

                    //---- label6 ----
                    label6.setText("Full Name");
                    panel1.add(label6, cc.xy(1, 3));
                    panel1.add(fullName, cc.xy(3, 3));
                }
                contentPanel.add(panel1, cc.xy(1, 11));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

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

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.setOpaque(false);
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        cancelButtonActionPerformed(e);
                    }
                });
                buttonBar.add(cancelButton, cc.xy(4, 1));

                //---- sendButton ----
                sendButton.setText("Send");
                sendButton.setOpaque(false);
                sendButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        sendButtonActionPerformed(e);
                    }
                });
                buttonBar.add(sendButton, cc.xy(6, 1));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JPanel panel2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label1;
    private JTextField summary;
    private JLabel label2;
    private JScrollPane scrollPane1;
    private JTextArea detailedDescription;
    private JPanel panel1;
    private JLabel label5;
    private JTextField emailAddress;
    private JLabel label6;
    private JTextField fullName;
    private JPanel buttonBar;
    private JButton cancelButton;
    private JButton sendButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	private String stackTrace = null;
	//private RemoteIssue remoteIssue = null;
        private String issueId = null;
	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	/*public RemoteIssue getRemoteIssue() {
		return remoteIssue;
	}

	public void setRemoteIssue(RemoteIssue remoteIssue) {
		this.remoteIssue = remoteIssue;
	}*/
        
        public String getIssueId(){
            return issueId;
        }
        
        public void setIssueId(String id){
            this.issueId=id;
        }
}
