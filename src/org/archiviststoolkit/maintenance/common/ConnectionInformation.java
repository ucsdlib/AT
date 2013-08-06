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

 * Created by JFormDesigner on Wed Dec 06 14:43:26 EST 2006
 */

package org.archiviststoolkit.maintenance.common;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Properties;
import java.sql.SQLException;
import javax.swing.*;
import javax.sql.DataSource;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.netbeans.spi.wizard.WizardPage;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.exceptions.UnsupportedDatabaseType;
import org.archiviststoolkit.maintenance.ChooseOperation;
import org.archiviststoolkit.util.UserPreferences;
import org.archiviststoolkit.util.DatabaseConnectionUtils;

public class ConnectionInformation extends WizardPage {

    private String wizardType = "";
    private boolean debug = false;

    public ConnectionInformation(String wizardType) {
        super("dbInfo", "Database Connection Information");
        initComponents();
        this.wizardType = wizardType;
        if (wizardType.equals(ChooseOperation.VALUE_UPGRADE) ||
                wizardType.equals(ChooseOperation.VALUE_UTILITIES)) {
            populateInfo();
        }
    }

    protected String validateContents(Component component, Object event) {
        if (getConnectionUrl().getText().length() == 0) {
            return "Please enter the database connection url";
        }
        if (getUserName().getText().length() == 0) {
            return "Please enter the username for connection to the database";
        }
        if (getPassword().getText().length() == 0) {
            return "Please enter the password";
        }
        if (databaseTypes.getSelectedIndex() == 0) {
            return "Please select a database type";
        }

        String databaseType = (String) databaseTypes.getSelectedItem();
        if (wizardType.equals(ChooseOperation.VALUE_INITIALIZE)) {
            try {
                // reset the seesion factory and set the database type
                SessionFactory.resetFactory();
                SessionFactory.setDatabaseType(databaseType);

                // get the database connection information
                String databaseUrl = getConnectionUrl().getText();
                String username = getUserName().getText();
                String password = getPassword().getText();

                // check to make sure that an empty database is being initilized
                return DatabaseConnectionUtils.isDatabaseEmpty(databaseType, databaseUrl, username, password);
            } catch (UnsupportedDatabaseType unsupportedDatabaseType) {
                new ErrorDialog("", unsupportedDatabaseType).showDialog();
                return "Unsupported Database Type";
            } catch (ClassNotFoundException e) {
                return e.getMessage();
            }
        } else if (wizardType.equals(ChooseOperation.VALUE_UPGRADE)) {
            String errorString = updateSessionFactory();
            if (errorString != null) {
                return errorString;
            }
            try {
                if (!DatabaseConnectionUtils.checkVersion(DatabaseConnectionUtils.CHECK_VERSION_FROM_UPGRADE)) {
                    return DatabaseConnectionUtils.getErrorString();
                }

                if (!DatabaseConnectionUtils.checkPermissions(databaseType)) {
                    return "User does not have permission to create or alter tables";
                }
                errorString = updateSessionFactory();
                if (debug) {
                    System.out.println("error string from updateSessionFactory: " + errorString);
                }
                if (errorString != null) {
                    return errorString;
                }

            } catch (ClassNotFoundException e) {
                System.out.println("sql exception: " + e.getMessage());
                return e.getMessage();
            } catch (SQLException e) {
                return e.getMessage();
            }
        } else if (wizardType.equals(ChooseOperation.VALUE_UTILITIES)) {
            String errorString = updateSessionFactory();
            if (errorString != null) {
                return errorString;
            }
            try {
                if (!DatabaseConnectionUtils.checkVersion(DatabaseConnectionUtils.CHECK_VERSION_FROM_UTILITIES)) {
                    return DatabaseConnectionUtils.getErrorString();
                }

                if (!DatabaseConnectionUtils.checkPermissions(databaseType)) {
                    return "User does not have permission to create or alter tables";
                }

                errorString = updateSessionFactory();
                if (debug) {
                    System.out.println("error string from updateSessionFactory: " + errorString);
                }
                if (errorString != null) {
                    return errorString;
                }

            } catch (ClassNotFoundException e) {
                System.out.println("sql exception: " + e.getMessage());
                return e.getMessage();
            } catch (SQLException e) {
                System.out.println("sql exception: " + e.getMessage());
                return e.getMessage();
            }
        }

//		System.out.println("end connection info validate conetents");
        return null;
    }

    public JTextField getConnectionUrl() {
        return connectionUrl;
    }

    public JTextField getUserName() {
        return userName;
    }

    public JTextField getPassword() {
        return password;
    }

    // Method to setup connection information for internal database
    // This should not be changed
    private void databaseTypesActionPerformed() {
        String currentURL = connectionUrl.getText();
        String database = databaseTypes.getSelectedItem().toString();

        if (database.equals(SessionFactory.DATABASE_TYPE_INTERNAL)
                && !currentURL.contains("jdbc:hsqldb")) {
            String internalDBUrl = getInternalDatabaseUrl();

            if (internalDBUrl != null) {
                connectionUrl.setText(internalDBUrl);
                userName.setText("SA");
                password.setText("SA");
            } else {
                // alert the user they need to run maintenance tool to initialize the database
                String error = "Failed to create internal database directory";
                JOptionPane.showMessageDialog(this,
                        error,
                        "Internal Database Creation Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Get the internal database url
     *
     * @return The database url
     */
    private String getInternalDatabaseUrl() {
        String prefix = "jdbc:hsqldb:file:";
        String dbDirectoryName = System.getProperty("user.home") + System.getProperty("file.separator") + "at_db";
        String dbFileName = dbDirectoryName + System.getProperty("file.separator") + "toolkitdb";

        // see if to create the database directory
        File dbDirectory = new File(dbDirectoryName);

        if (wizardType.equals(ChooseOperation.VALUE_INITIALIZE) && !dbDirectory.exists()) {
            try {
                // attempt to make te default directory where database files go
                dbDirectory.mkdir();
            } catch (Exception e) {
                return null;
            }
        }

        return prefix + dbFileName;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label4 = new JLabel();
        connectionUrl = new JTextField();
        label5 = new JLabel();
        userName = new JTextField();
        label6 = new JLabel();
        password = new JPasswordField();
        label7 = new JLabel();
        databaseTypes = ATBasicComponentFactory.createUnboundComboBox(SessionFactory.getDatabaseTypesList(true));
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setLayout(new FormLayout(
                new ColumnSpec[]{
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                },
                new RowSpec[]{
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC
                }));

        //---- label4 ----
        label4.setText("Connection URL");
        add(label4, cc.xy(1, 1));

        //---- connectionUrl ----
        connectionUrl.setName("connectionURL");
        add(connectionUrl, new CellConstraints(3, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 5, 0, 0)));

        //---- label5 ----
        label5.setText("Username");
        add(label5, cc.xy(1, 3));

        //---- userName ----
        userName.setName("connectionUsername");
        add(userName, new CellConstraints(3, 3, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 5, 0, 0)));

        //---- label6 ----
        label6.setText("Password");
        add(label6, cc.xy(1, 5));

        //---- password ----
        password.setName("connectionPassword");
        add(password, new CellConstraints(3, 5, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 5, 0, 0)));

        //---- label7 ----
        label7.setText("Database Type");
        add(label7, cc.xy(1, 7));

        //---- databaseTypes ----
        databaseTypes.setOpaque(false);
        databaseTypes.setName("connectionDatabaseType");
        databaseTypes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                databaseTypesActionPerformed();
            }
        });
        add(databaseTypes, cc.xywh(3, 7, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label4;
    private JTextField connectionUrl;
    private JLabel label5;
    private JTextField userName;
    private JLabel label6;
    private JPasswordField password;
    private JLabel label7;
    private JComboBox databaseTypes;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

//	private DataSource makeDataSource()
//			throws PropertiesDataSourceException {
//		String datasourceName = "schemacrawler";
////		ResourceBundle resourceBundle = ResourceBundle.getBundle("org.archiviststoolkit.resources.messages.messages");
////		String driverClass = resourceBundle.getString("archiviststoolkit.jdbc.driver");
//		SessionFactory.resetFactory();
//		try {
//			SessionFactory.setDatabaseType((String) databaseTypes.getSelectedItem());
//		} catch (UnsupportedDatabaseType unsupportedDatabaseType) {
//			new ErrorDialog("", unsupportedDatabaseType).showDialog();
//		}
//
//		Properties connectionProperties = new Properties();
//		connectionProperties.setProperty(datasourceName + ".driver", SessionFactory.getDriverClass());
//		connectionProperties.setProperty(datasourceName + ".url", getConnectionUrl().getText());
//		connectionProperties.setProperty(datasourceName + ".user", getUserName().getText());
//		connectionProperties.setProperty(datasourceName + ".password", getPassword().getText());
//
//		return new PropertiesDataSource(connectionProperties, datasourceName);
//	}

    private String updateSessionFactory() {
        SessionFactory.resetFactory();
        SessionFactory.setDatabaseUrl(connectionUrl.getText());
        SessionFactory.setUserName(userName.getText());
        SessionFactory.setPassword(new String(password.getPassword()));
        try {
            SessionFactory.setDatabaseType((String) databaseTypes.getSelectedItem());
        } catch (UnsupportedDatabaseType unsupportedDatabaseType) {
            return unsupportedDatabaseType.getMessage();
        }
        return null;
    }

    private void populateInfo() {
        UserPreferences userPrefs = UserPreferences.getInstance();
        userPrefs.populateFromPreferences();
        try {
            userPrefs.updateSessionFactoryInfo();
        } catch (UnsupportedDatabaseType unsupportedDatabaseType) {
            //just chew it up and try later
        }
        connectionUrl.setText(userPrefs.getDatabaseUrl());
        userName.setText(userPrefs.getDatabaseUserName());
        password.setText(userPrefs.getDatabasePassword());
        databaseTypes.setSelectedItem(userPrefs.getDatabaseType());
    }
}
