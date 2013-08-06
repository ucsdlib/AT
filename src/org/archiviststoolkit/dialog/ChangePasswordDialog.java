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
 * Created by JFormDesigner on Thu Feb 01 12:14:09 EST 2007
 */

package org.archiviststoolkit.dialog;

import java.awt.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.swing.ATBasicDialog;
import org.archiviststoolkit.model.Users;

/**
 * @author Lee Mandell
 */
public class ChangePasswordDialog extends ATBasicDialog {

    byte[] oldPasswordFromUser;
    Users user;
    boolean supressOldPassword = false;

    public ChangePasswordDialog(Frame owner, Users user) {
        super(owner);
        initComponents();
        init(user);
    }

    public ChangePasswordDialog(Dialog owner, Users user) {
        super(owner);
        initComponents();
        init(user);
    }

    private void init(Users user) {
        this.user = user;
        if (this.user.getPassword() == null) {
            supressOldPassword = true;
            oldPassword.setVisible(false);
            oldPasswordlabel.setVisible(false);
            this.setTitle("Create New Password");
            this.changePasswordButton.setText("Create New Password");
        } else if (Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_SUPERUSER)) {
			supressOldPassword = true;
			oldPassword.setVisible(false);
			oldPasswordlabel.setVisible(false);
			this.setTitle("Reset Password");
			this.changePasswordButton.setText("Reset Password");
		}
        oldPasswordFromUser = user.getPassword();
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        super.performCancelAction();
    }

    private void changePasswordButtonActionPerformed(ActionEvent e) {
        try {
            byte[] oldPasswordBytes = Users.hashString(oldPassword.getPassword());
            if (!Arrays.equals(oldPasswordFromUser, oldPasswordBytes) && !supressOldPassword) {
                JOptionPane.showMessageDialog(this, "The old password is not correct", "", JOptionPane.ERROR_MESSAGE);
            } else {
                char[] newPasswordChars1 = newPassword1.getPassword();
                char[] newPasswordChars2 = newPassword2.getPassword();
                if (!Arrays.equals(newPasswordChars1, newPasswordChars2)) {
                    JOptionPane.showMessageDialog(this, "The new passwords do not match", "", JOptionPane.ERROR_MESSAGE);
                } else {
                    user.setPassword(Users.hashString(newPasswordChars1));
                    super.performOkAction();
                }
            }


        } catch (NoSuchAlgorithmException e1) {
            new ErrorDialog("", e1).showDialog();
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        oldPasswordlabel = new JLabel();
        oldPassword = new JPasswordField();
        label2 = new JLabel();
        newPassword1 = new JPasswordField();
        label3 = new JLabel();
        newPassword2 = new JPasswordField();
        buttonBar = new JPanel();
        cancelButton = new JButton();
        changePasswordButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setModal(true);
        setTitle("Change Password");
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
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC
                    }));

                //---- oldPasswordlabel ----
                oldPasswordlabel.setText("Old Password");
                contentPanel.add(oldPasswordlabel, cc.xy(1, 1));

                //---- oldPassword ----
                oldPassword.setColumns(15);
                contentPanel.add(oldPassword, cc.xy(3, 1));

                //---- label2 ----
                label2.setText("New Password");
                contentPanel.add(label2, cc.xy(1, 3));

                //---- newPassword1 ----
                newPassword1.setColumns(15);
                contentPanel.add(newPassword1, cc.xy(3, 3));

                //---- label3 ----
                label3.setText("Repeat New Password");
                contentPanel.add(label3, cc.xy(1, 5));

                //---- newPassword2 ----
                newPassword2.setColumns(15);
                contentPanel.add(newPassword2, cc.xy(3, 5));
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

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.setOpaque(false);
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        cancelButtonActionPerformed(e);
                    }
                });
                buttonBar.add(cancelButton, cc.xy(4, 1));

                //---- changePasswordButton ----
                changePasswordButton.setText("Change Password");
                changePasswordButton.setOpaque(false);
                changePasswordButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        changePasswordButtonActionPerformed(e);
                    }
                });
                buttonBar.add(changePasswordButton, cc.xy(6, 1));
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
    private JLabel oldPasswordlabel;
    private JPasswordField oldPassword;
    private JLabel label2;
    private JPasswordField newPassword1;
    private JLabel label3;
    private JPasswordField newPassword2;
    private JPanel buttonBar;
    private JButton cancelButton;
    private JButton changePasswordButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
