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
 * Created by JFormDesigner on Tue Aug 08 13:17:11 EDT 2006
 */

package org.archiviststoolkit.swing;

import java.awt.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.model.Users;
import org.archiviststoolkit.dialog.ErrorDialog;

public class LoginDialog extends JDialog {

	public static final int CHOOSE_SERVER = 100;

	public LoginDialog(Frame frame) throws HeadlessException {
		super(frame);
		initComponents();
	}

	public LoginDialog() {
		initComponents();
	}

	private void selectServerActionPerformed(ActionEvent e) {
		status = CHOOSE_SERVER;
		setVisible(false);
	}

	private void loginButtonActionPerformed(ActionEvent e) {
		status = JOptionPane.OK_OPTION;
		setVisible(false);
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		status = JOptionPane.CANCEL_OPTION;
		setVisible(false);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        userNameField = new JTextField();
        label = new JLabel();
        passwordField = new JPasswordField();
        buttonBar = new JPanel();
        selectServer = new JButton();
        cancelButton = new JButton();
        loginButton = new JButton();
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

            //======== contentPanel ========
            {
                contentPanel.setOpaque(false);
                contentPanel.setLayout(new FormLayout(
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

                //---- label1 ----
                label1.setText("Username");
                contentPanel.add(label1, cc.xy(1, 1));
                contentPanel.add(userNameField, cc.xy(3, 1));

                //---- label ----
                label.setText("Password");
                contentPanel.add(label, cc.xy(1, 3));
                contentPanel.add(passwordField, cc.xy(3, 3));
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
                        FormFactory.BUTTON_COLSPEC,
                        FormFactory.RELATED_GAP_COLSPEC,
                        FormFactory.BUTTON_COLSPEC
                    },
                    RowSpec.decodeSpecs("pref")));

                //---- selectServer ----
                selectServer.setText("Select Server");
                selectServer.setOpaque(false);
                selectServer.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        selectServerActionPerformed(e);
                    }
                });
                buttonBar.add(selectServer, cc.xy(2, 1));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.setOpaque(false);
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        cancelButtonActionPerformed(e);
                    }
                });
                buttonBar.add(cancelButton, cc.xy(4, 1));

                //---- loginButton ----
                loginButton.setText("Login");
                loginButton.setOpaque(false);
                loginButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        loginButtonActionPerformed(e);
                    }
                });
                buttonBar.add(loginButton, cc.xy(6, 1));
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
    private JLabel label1;
    private JTextField userNameField;
    private JLabel label;
    private JPasswordField passwordField;
    private JPanel buttonBar;
    private JButton selectServer;
    private JButton cancelButton;
    private JButton loginButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	protected int status = 0;


	public String getUserName() {
		return userNameField.getText();
	}

	public byte[] getPassword() {
		byte[] returnPassword = null;
		try {
			returnPassword = Users.hashString(passwordField.getPassword());
		} catch (NoSuchAlgorithmException e) {
			new ErrorDialog(this, "", e).showDialog();
		}
		return returnPassword;
	}

    public String getPasswordAsText() {
        String password = new String(passwordField.getPassword());
        return password;
    }

	public final int showDialog() {

		//send to logger
		this.pack();
		setLocationRelativeTo(null);
		this.getRootPane().setDefaultButton(loginButton);
		userNameField.requestFocusInWindow();
		this.setVisible(true);

		return (status);

	}


}
