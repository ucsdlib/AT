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
 * Created by JFormDesigner on Tue Dec 27 12:03:14 EST 2005
 */

package org.archiviststoolkit.dialog;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.swing.SimpleFileFilter;
import org.archiviststoolkit.util.UserPreferences;
import org.archiviststoolkit.util.DatabaseConnectionInformation;
import org.archiviststoolkit.util.DatabaseConnectionUtils;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.swing.ATBasicComponentFactory;

public class UserPreferencesDialog extends JDialog {
    // hash map that contains the stored locations
    private HashMap savedConnections = new HashMap();

    // use this to prevent any update events when the list connections is being loaded
    private boolean loadingConnections = true;

    public UserPreferencesDialog() {
        super();
        initComponents();
        loadDatabaseConnectionInformation();
    }

    public UserPreferencesDialog(Frame owner) {
        super(owner);
        initComponents();
        loadDatabaseConnectionInformation();
    }

    public UserPreferencesDialog(Dialog owner) {
        super(owner);
        initComponents();
        loadDatabaseConnectionInformation();
    }

    private void storeConnectionUrlInformation() {
        String selectedUrl = (String) connectionUrl.getSelectedItem();

        // check to make sure that that selectedUrl is not blank before
        // attempting to store it
        if (!selectedUrl.startsWith("jdbc:")) {
            return;
        }

        // now see if this connection url is already there., if not create a new one
        if (!savedConnections.containsKey(selectedUrl)) {
            // create a new database connection url
            DatabaseConnectionInformation dbInfo = new DatabaseConnectionInformation();
            dbInfo.setDatabaseURL(selectedUrl);
            dbInfo.setUsername(getUserName());
            dbInfo.setPassword(new String(getPassword()));
            dbInfo.setDatabaseType(getDatabaseType());

            // add into the hashmap and then
            savedConnections.put(selectedUrl, dbInfo);
            connectionUrl.addItem(selectedUrl);
        } else { // update the information for already stored connection information
            DatabaseConnectionInformation dbInfo = (DatabaseConnectionInformation) savedConnections.get(selectedUrl);
            dbInfo.setDatabaseURL(selectedUrl);
            dbInfo.setUsername(getUserName());
            dbInfo.setPassword(new String(getPassword()));
            dbInfo.setDatabaseType(getDatabaseType());
        }

        // now save the database information to a file
        DatabaseConnectionUtils.saveDatabaseConnectionInformation(savedConnections);
    }

    /**
     * Method to read stored database connection information from a file
     * and loads it into the drop down menu.
     */
    private void loadDatabaseConnectionInformation() {
        HashMap info = DatabaseConnectionUtils.loadDatabaseConnectionInformation();
        if (info != null) {
            savedConnections = info;

            Map sortedMap = new TreeMap(savedConnections);
            Collection c = sortedMap.values();

            //obtain an Iterator for Collection
            Iterator itr = c.iterator();

            //iterate through HashMap and ad to combo box
            while (itr.hasNext()) {
                DatabaseConnectionInformation dbInfo = (DatabaseConnectionInformation) itr.next();
                connectionUrl.addItem(dbInfo.toString());
            }

            // set loading connection boolean to false
            loadingConnections = false;
        }
    }

    /**
     * Method to update the connection url information displayed to user
     */
    private void updateConnectionUrlInformation() {
        if (loadingConnections == true) {
            return;
        }

        String selectedUrl = (String) connectionUrl.getSelectedItem();
        if (savedConnections.containsKey(selectedUrl)) {
            DatabaseConnectionInformation dbInfo = (DatabaseConnectionInformation) savedConnections.get(selectedUrl);
            userName.setText(dbInfo.getUsername());
            password.setText(dbInfo.getPassword());
            databaseTypes.setSelectedItem(dbInfo.getDatabaseType());
        }
    }

    /**
     * Method to hanlde actions from the database selector combo box
     */
    private void databaseTypesActionPerformed() {
        String database = databaseTypes.getSelectedItem().toString();
        String currentUrl = connectionUrl.getSelectedItem().toString();

        if (database.equals(SessionFactory.DATABASE_TYPE_INTERNAL)) {
            String internalDBUrl = getInternalDatabaseUrl();

            // decide whether to show error messages to user
            if (internalDBUrl != null && !currentUrl.contains("jdbc:hsqldb:")) {
                connectionUrl.setSelectedItem(internalDBUrl);
                userName.setText("SA");
                password.setText("SA");
            } else if (!currentUrl.contains("jdbc:hsqldb:")) {
                // alert the user to configure the internal database
                String message = "Please run the \"Maintenance Program\" to Configure\n" +
                        "The Internal Database, or load an Internal Database file ...";

                JOptionPane.showMessageDialog(this,
                        message,
                        "Internal Database not Configured",
                        JOptionPane.WARNING_MESSAGE);
            }

            // make the open DB file button visible
            openDBFileButton.setVisible(true);
        } else {
            openDBFileButton.setVisible(false);
        }
    }

    private String getInternalDatabaseUrl() {
        String prefix = "jdbc:hsqldb:file:";
        String dbDirectoryName = System.getProperty("user.home") + System.getProperty("file.separator") + "at_db";
        String dbFileName = dbDirectoryName + System.getProperty("file.separator") + "toolkitdb";

        // see if to create the database directory
        File dbFile = new File(dbFileName + ".log");

        if (dbFile.exists()) {
            return prefix + dbFileName;
        } else {
            return null;
        }
    }

    /**
     * Method to open a new internal database file
     *
     */
    private void openDBFileButtonActionPerformed() {
        ATFileChooser filechooser = new ATFileChooser(new SimpleFileFilter(".script"));
		filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		if (filechooser.showOpenDialog(this, "Open Internal Database File") == JFileChooser.APPROVE_OPTION) {
            String prefix = "jdbc:hsqldb:file:";
            String dbFileName = filechooser.getSelectedFile().getAbsolutePath().replace(".script","");
            String databaseUrl = prefix + dbFileName;

            connectionUrl.setSelectedItem(databaseUrl);
            userName.setText("SA");
            password.setText("SA");
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        HeaderPanel = new JPanel();
        panel2 = new JPanel();
        mainHeaderLabel = new JLabel();
        panel3 = new JPanel();
        subHeaderLabel = new JLabel();
        panel1 = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        connectionUrl = new JComboBox();
        label2 = new JLabel();
        userName = new JTextField();
        label3 = new JLabel();
        password = new JPasswordField();
        label4 = new JLabel();
        databaseTypes = ATBasicComponentFactory.createUnboundComboBox(SessionFactory.getDatabaseTypesList(true));
        buttonBar = new JPanel();
        openDBFileButton = new JButton();
        saveButton = new JButton();
        okButton = new JButton();
        cancelButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setModal(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(null);
            dialogPane.setBackground(new Color(200, 205, 232));
            dialogPane.setPreferredSize(new Dimension(600, 238));
            dialogPane.setMinimumSize(new Dimension(600, 238));
            dialogPane.setLayout(new BorderLayout());

            //======== HeaderPanel ========
            {
                HeaderPanel.setBackground(new Color(80, 69, 57));
                HeaderPanel.setOpaque(false);
                HeaderPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                HeaderPanel.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        new ColumnSpec(Sizes.bounded(Sizes.MINIMUM, Sizes.dluX(100), Sizes.dluX(200))),
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                    },
                    RowSpec.decodeSpecs("default")));

                //======== panel2 ========
                {
                    panel2.setBackground(new Color(80, 69, 57));
                    panel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel2.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.RELATED_GAP_COLSPEC,
                            new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        },
                        new RowSpec[] {
                            FormFactory.RELATED_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.RELATED_GAP_ROWSPEC
                        }));

                    //---- mainHeaderLabel ----
                    mainHeaderLabel.setText("Administration");
                    mainHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
                    mainHeaderLabel.setForeground(Color.white);
                    panel2.add(mainHeaderLabel, cc.xy(2, 2));
                }
                HeaderPanel.add(panel2, cc.xy(1, 1));

                //======== panel3 ========
                {
                    panel3.setBackground(new Color(66, 60, 111));
                    panel3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel3.setLayout(new FormLayout(
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
                    subHeaderLabel.setText("Connection Settings");
                    subHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
                    subHeaderLabel.setForeground(Color.white);
                    panel3.add(subHeaderLabel, cc.xy(2, 2));
                }
                HeaderPanel.add(panel3, cc.xy(2, 1));
            }
            dialogPane.add(HeaderPanel, BorderLayout.NORTH);

            //======== panel1 ========
            {
                panel1.setOpaque(false);
                panel1.setBorder(Borders.DIALOG_BORDER);
                panel1.setLayout(new FormLayout(
                    ColumnSpec.decodeSpecs("max(default;400px):grow"),
                    new RowSpec[] {
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC
                    }));

                //======== contentPanel ========
                {
                    contentPanel.setBorder(new TitledBorder(null, "Database Properties", TitledBorder.LEADING, TitledBorder.TOP));
                    contentPanel.setOpaque(false);
                    contentPanel.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            new ColumnSpec(ColumnSpec.FILL, Sizes.MINIMUM, 0.1),
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

                    //---- label1 ----
                    label1.setText("Connection URL");
                    contentPanel.add(label1, new CellConstraints(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT, new Insets(0, 5, 0, 0)));

                    //---- connectionUrl ----
                    connectionUrl.setEditable(true);
                    connectionUrl.addItemListener(new ItemListener() {
                        public void itemStateChanged(ItemEvent e) {
                            updateConnectionUrlInformation();
                        }
                    });
                    contentPanel.add(connectionUrl, new CellConstraints(3, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 5, 0, 0)));

                    //---- label2 ----
                    label2.setText("Username");
                    contentPanel.add(label2, new CellConstraints(1, 3, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 5, 0, 0)));
                    contentPanel.add(userName, new CellConstraints(3, 3, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 5, 0, 0)));

                    //---- label3 ----
                    label3.setText("Password");
                    contentPanel.add(label3, new CellConstraints(1, 5, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 5, 0, 0)));
                    contentPanel.add(password, new CellConstraints(3, 5, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 5, 0, 0)));

                    //---- label4 ----
                    label4.setText("Database Type");
                    contentPanel.add(label4, new CellConstraints(1, 7, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 5, 0, 0)));

                    //---- databaseTypes ----
                    databaseTypes.setOpaque(false);
                    databaseTypes.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            databaseTypesActionPerformed();
                        }
                    });
                    contentPanel.add(databaseTypes, cc.xy(3, 7, CellConstraints.LEFT, CellConstraints.DEFAULT));
                }
                panel1.add(contentPanel, cc.xy(1, 1));

                //======== buttonBar ========
                {
                    buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
                    buttonBar.setBackground(new Color(231, 188, 251));
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
                            FormFactory.BUTTON_COLSPEC
                        },
                        RowSpec.decodeSpecs("pref")));

                    //---- openDBFileButton ----
                    openDBFileButton.setText("Open Internal Database File");
                    openDBFileButton.setVisible(false);
                    openDBFileButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            openDBFileButtonActionPerformed();
                        }
                    });
                    buttonBar.add(openDBFileButton, cc.xy(2, 1));

                    //---- saveButton ----
                    saveButton.setText("Save");
                    saveButton.setOpaque(false);
                    saveButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            storeConnectionUrlInformation();
                        }
                    });
                    buttonBar.add(saveButton, cc.xy(4, 1));

                    //---- okButton ----
                    okButton.setText("OK");
                    okButton.setOpaque(false);
                    okButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            okButtonActionPerformed(e);
                        }
                    });
                    buttonBar.add(okButton, cc.xy(6, 1));

                    //---- cancelButton ----
                    cancelButton.setText("Cancel");
                    cancelButton.setOpaque(false);
                    cancelButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            cancelButtonActionPerformed(e);
                        }
                    });
                    buttonBar.add(cancelButton, cc.xy(8, 1));
                }
                panel1.add(buttonBar, cc.xy(1, 3));
            }
            dialogPane.add(panel1, BorderLayout.CENTER);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void okButtonActionPerformed(ActionEvent e) {
        status = javax.swing.JOptionPane.OK_OPTION;
        this.setVisible(false);
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        status = javax.swing.JOptionPane.CANCEL_OPTION;
        this.setVisible(false);
    }

    public JComboBox getConnectionUrl() {
        return connectionUrl;
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel HeaderPanel;
    private JPanel panel2;
    private JLabel mainHeaderLabel;
    private JPanel panel3;
    private JLabel subHeaderLabel;
    private JPanel panel1;
    private JPanel contentPanel;
    private JLabel label1;
    private JComboBox connectionUrl;
    private JLabel label2;
    private JTextField userName;
    private JLabel label3;
    private JPasswordField password;
    private JLabel label4;
    private JComboBox databaseTypes;
    private JPanel buttonBar;
    private JButton openDBFileButton;
    private JButton saveButton;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    /**
     * The status of the editor.
     */
    protected int status = 0;

    /**
     * Displays the dialog box representing the editor.
     *
     * @return true if it displayed okay
     */

    public final int showDialog() {

        this.pack();

        setLocationRelativeTo(null);
        this.setVisible(true);

        return (status);
    }

    public String getDatabaseUrl() {
        return (String) connectionUrl.getSelectedItem();
    }

    public String getUserName() {
        return userName.getText();
    }

    public char[] getPassword() {
        return password.getPassword();
    }

    public String getDatabaseType() {
        return (String) databaseTypes.getSelectedItem();
    }

    public void populateFromUserPreferences(UserPreferences userPrefs) {
        this.connectionUrl.setSelectedItem(userPrefs.getDatabaseUrl());
        this.userName.setText(userPrefs.getDatabaseUserName());
        this.password.setText(userPrefs.getDatabasePassword());
        this.databaseTypes.setSelectedItem(userPrefs.getDatabaseType());
    }
}
