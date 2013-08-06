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
 * Created by JFormDesigner on Fri Dec 08 09:33:13 EST 2006
 */

package org.archiviststoolkit.maintenance.common;

import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.netbeans.spi.wizard.WizardPage;
import org.archiviststoolkit.maintenance.ChooseOperation;
import org.archiviststoolkit.model.Users;
import org.archiviststoolkit.hibernate.SessionFactory;

public class UserInformation extends WizardPage {

    private String wizardType = "";

    public UserInformation(String wizardType) {
        super("userInfo", "User Information");
        this.wizardType = wizardType;
        initComponents();
        if (wizardType.equals(ChooseOperation.VALUE_UPGRADE) ||
				wizardType.equals(ChooseOperation.VALUE_UTILITIES)) {
            password2.setVisible(false);
            password2Label.setVisible(false);
        } else {
            message.setVisible(false);
        }
    }

    protected String validateContents(Component component, Object event) {
            if (getUserName().getText().length() == 0) {
                return "Please enter a username";
            }
            if (getPassword1().getText().length() == 0) {
                return "Please enter a password";
            }
            if (wizardType.equals(ChooseOperation.VALUE_INITIALIZE)) {
                if (getPassword2().getText().length() == 0) {
                    return "Please enter a password again";
                }
                if (!getPassword1().getText().equals(getPassword2().getText())) {
                    return "Passwords do not match";
                }
            } else {
                try {
                    Integer accessClass = lookupUserAccessClass(getUserName().getText(), Users.hashString(password1.getPassword()));
                    if (accessClass == null) {
                        return "Unknown user or wrong password";
                    }
                    if (accessClass != Users.ACCESS_CLASS_SUPERUSER) {
                        return "the user is not a superuser";
                    }
                } catch (NoSuchAlgorithmException e) {
                    return e.getMessage();
                }
        }
        // everything is ok
        return null;
    }

    public JTextField getPassword1() {
        return password1;
    }

    public JTextField getPassword2() {
        return password2;
    }

    public JTextField getUserName() {
        return userName;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        message = new JLabel();
        label1 = new JLabel();
        userName = new JTextField();
        label15 = new JLabel();
        password1 = new JPasswordField();
        password2Label = new JLabel();
        password2 = new JPasswordField();
        CellConstraints cc = new CellConstraints();

        //======== this ========
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
        		FormFactory.DEFAULT_ROWSPEC
        	}));

        //---- message ----
        message.setText("<html>Upgrading the database can only be done by a superuser. Please enter the username and password for a superuser.</html>");
        add(message, cc.xywh(1, 1, 3, 1));

        //---- label1 ----
        label1.setText("Username");
        label1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(label1, cc.xy(1, 3));

        //---- userName ----
        userName.setName("userName");
        add(userName, new CellConstraints(3, 3, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 5, 0, 0)));

        //---- label15 ----
        label15.setText("Password");
        label15.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(label15, cc.xy(1, 5));

        //---- password1 ----
        password1.setName("password1");
        add(password1, new CellConstraints(3, 5, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 5, 0, 0)));

        //---- password2Label ----
        password2Label.setText("Password again");
        password2Label.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(password2Label, cc.xy(1, 7));

        //---- password2 ----
        password2.setName("password2");
        add(password2, new CellConstraints(3, 7, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 5, 0, 0)));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel message;
    private JLabel label1;
    private JTextField userName;
    private JLabel label15;
    private JPasswordField password1;
    private JLabel password2Label;
    private JPasswordField password2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private Integer lookupUserAccessClass(String userName, byte[] password) {
        try {
            Class.forName(SessionFactory.getDriverClass());
            Connection conn = DriverManager.getConnection(SessionFactory.getDatabaseUrl(),
                    SessionFactory.getUserName(),
                    SessionFactory.getPassword());
            PreparedStatement stmt = conn.prepareStatement("SELECT accessClass FROM Users WHERE userName = ? AND password = ?");
            stmt.setString(1, userName);
            stmt.setBytes(2, password);
            ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
//                if (rs.isLast()) {
                    return rs.getInt(1);
//                } else {
//                    return null;
//                }
            } else {
                return null;
            }
        } catch (ClassNotFoundException e) {
            return null;
        } catch (SQLException e) {
            return null;
        }
    }
}
