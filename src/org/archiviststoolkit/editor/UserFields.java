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
 * Created by JFormDesigner on Wed Aug 31 13:17:31 EDT 2005
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import org.archiviststoolkit.model.Users;
import org.archiviststoolkit.model.Repositories;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.mydomain.DomainEditor;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.util.SequencedObjectsUtils;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.dialog.ChangePasswordDialog;

public class UserFields extends DomainEditorFields {

    protected UserFields() {
        super();
        initComponents();
        initAccess();
        accessClass.setInputVerifier(new InputVerifier() {
            public boolean verify(JComponent comp) {
                boolean returnValue = true;
                JTextField textField = (JTextField) comp;
                String content = textField.getText();
                if (content.length() != 0) {
                    try {
                        Integer editedValue = Integer.parseInt(textField.getText());
                        if (editedValue == null || editedValue < 1 || editedValue > ApplicationFrame.getInstance().getCurrentUserAccessClass())
                        {
                            JOptionPane.showMessageDialog(getParentEditor(), "Access Class must be between 1 and " + ApplicationFrame.getInstance().getCurrentUserAccessClass());
                            returnValue = false;
                        }
                    } catch (NumberFormatException e) {
                        returnValue = false;
                    }
                }
                return returnValue;
            }

            public boolean shouldYieldFocus(JComponent input) {
                boolean valid = super.shouldYieldFocus(input);
                if (!valid) {
                    getToolkit().beep();
                }
                return valid;
            }
        });
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label1 = new JLabel();
        userName = ATBasicComponentFactory.createTextField(
                        detailsModel.getModel(Users.PROPERTYNAME_USERNAME));
        label13 = new JLabel();
        fullName = ATBasicComponentFactory.createTextField(detailsModel.getModel(Users.PROPERTYNAME_FULL_NAME));
        label14 = new JLabel();
        title = ATBasicComponentFactory.createTextField(detailsModel.getModel(Users.PROPERTYNAME_TITLE));
        label15 = new JLabel();
        department = ATBasicComponentFactory.createTextField(detailsModel.getModel(Users.PROPERTYNAME_DEPARTMENT));
        label17 = new JLabel();
        email = ATBasicComponentFactory.createTextField(detailsModel.getModel(Users.PROPERTYNAME_EMAIL));
        label16 = new JLabel();
        accessClass = ATBasicComponentFactory.createIntegerField(detailsModel,Users.PROPERTYNAME_ACCESS_CLASS);
        label142 = new JLabel();
        repository = ATBasicComponentFactory.createComboBox(detailsModel, Users.PROPERTYNAME_REPOSITORY, Repositories.getRepositoryList());
        changePasswordButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBorder(Borders.DLU4_BORDER);
        setBackground(new Color(234, 201, 250));
        setOpaque(false);
        setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        setLayout(new FormLayout(
            new ColumnSpec[] {
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                new ColumnSpec("max(default;400px):grow")
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
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC
            }));

        //---- label1 ----
        label1.setText("Username");
        label1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label1, Users.class, Users.PROPERTYNAME_USERNAME);
        add(label1, cc.xy(1, 1));
        add(userName, cc.xy(3, 1));

        //---- label13 ----
        label13.setText("Full Name");
        label13.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label13, Users.class, Users.PROPERTYNAME_FULL_NAME);
        add(label13, cc.xy(1, 3));
        add(fullName, cc.xy(3, 3));

        //---- label14 ----
        label14.setText("Title");
        label14.setVerticalAlignment(SwingConstants.TOP);
        label14.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label14, Users.class, Users.PROPERTYNAME_TITLE);
        add(label14, cc.xywh(1, 5, 1, 1, CellConstraints.DEFAULT, CellConstraints.CENTER));
        add(title, cc.xy(3, 5));

        //---- label15 ----
        label15.setText("Department");
        label15.setVerticalAlignment(SwingConstants.TOP);
        label15.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label15, Users.class, Users.PROPERTYNAME_DEPARTMENT);
        add(label15, cc.xywh(1, 7, 1, 1, CellConstraints.DEFAULT, CellConstraints.CENTER));
        add(department, cc.xy(3, 7));

        //---- label17 ----
        label17.setText("email");
        label17.setVerticalAlignment(SwingConstants.TOP);
        label17.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label17, Users.class, Users.PROPERTYNAME_EMAIL);
        add(label17, cc.xywh(1, 9, 1, 1, CellConstraints.DEFAULT, CellConstraints.CENTER));
        add(email, cc.xy(3, 9));

        //---- label16 ----
        label16.setText("Access Class");
        label16.setVerticalAlignment(SwingConstants.TOP);
        label16.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label16, Users.class, Users.PROPERTYNAME_ACCESS_CLASS);
        add(label16, cc.xywh(1, 11, 1, 1, CellConstraints.DEFAULT, CellConstraints.CENTER));

        //---- accessClass ----
        accessClass.setColumns(3);
        add(accessClass, cc.xywh(3, 11, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- label142 ----
        label142.setText("Repository");
        label142.setVerticalAlignment(SwingConstants.TOP);
        label142.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label142, Users.class, Users.PROPERTYNAME_REPOSITORY);
        add(label142, cc.xywh(1, 13, 1, 1, CellConstraints.DEFAULT, CellConstraints.CENTER));

        //---- repository ----
        repository.setOpaque(false);
        repository.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(repository, cc.xy(3, 13));

        //---- changePasswordButton ----
        changePasswordButton.setText("Change Password");
        changePasswordButton.setOpaque(false);
        changePasswordButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        changePasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changePasswordButtonActionPerformed(e);
            }
        });
        add(changePasswordButton, cc.xywh(3, 15, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void changePasswordButtonActionPerformed(ActionEvent e) {
		Users userModel = (Users)super.getModel();
		if (new ChangePasswordDialog(super.getParentEditor(), userModel).showDialog() == JOptionPane.OK_OPTION) {
//			if (userModel.getAccessClass() == Users.ACCESS_CLASS_SUPERUSER) {
			if (Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_SUPERUSER)) {
				changePasswordButton.setText("Reset Password");
			} else {
				changePasswordButton.setText("Change Password");
			}
		}
    }

    /**
     * Set the domain model for this editor.
     *
     * @param model the model
     */

    @Override
    public void setModel(DomainObject model, InfiniteProgressPanel progressPanel) {
        super.setModel(model, progressPanel);
        Users userModel = (Users) model;
        if (userModel.getRepository() != null) {
            repositoryTextField.setText(userModel.getRepository().getRepositoryName());
        }
        //allow a user to change their own password
        if (userModel.equals(ApplicationFrame.getInstance().getCurrentUser())) {
            changePasswordButton.setVisible(true);
        }
        if (ApplicationFrame.getInstance().getCurrentUserAccessClass() < ((Users) this.getModel()).getAccessClass() ||
                !Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_REPOSITORY_MANAGER)) {
          setAccessClassFieldEditable(false);
        } else {
          setAccessClassFieldEditable(true);
        }
        //if this is a new user set the change password button to create new password
        if (userModel.getPassword() == null) {
            changePasswordButton.setText("Create New Password");
        } else if (Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_SUPERUSER)) {
			changePasswordButton.setText("Reset Password");
		}
		userModel.setOldAccessClass(userModel.getAccessClass());
	}

    public Component getInitialFocusComponent() {
        return userName;
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label1;
    private JTextField userName;
    private JLabel label13;
    private JTextField fullName;
    private JLabel label14;
    private JTextField title;
    private JLabel label15;
    private JTextField department;
    private JLabel label17;
    private JTextField email;
    private JLabel label16;
    private JFormattedTextField accessClass;
    private JLabel label142;
    public JComboBox repository;
    private JButton changePasswordButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private JTextField repositoryTextField = new JTextField();

    private void initAccess() {
        //hide repository popup if necessary
        if (!Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_SUPERUSER)) {
            repositoryTextField.setOpaque(false);
            repositoryTextField.setEditable(false);
            FormLayout formLayout = (FormLayout) this.getLayout();
            CellConstraints cc = formLayout.getConstraints(repository);
            this.remove(repository);
            this.add(repositoryTextField, cc);

        }
        //if user is not at least a repository manager set the record to read only
        if (!Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_REPOSITORY_MANAGER)) {
            setFormToReadOnly();
            changePasswordButton.setVisible(false);
        }
    }

  // Method to make the accessClass field none editable
  private void setAccessClassFieldEditable(boolean b) {
    accessClass.setEditable(b);
    accessClass.setOpaque(b);
  }
}
