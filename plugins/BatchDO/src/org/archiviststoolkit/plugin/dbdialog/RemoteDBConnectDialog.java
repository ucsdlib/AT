/*
 * Created by JFormDesigner on Sat Mar 07 06:21:42 EST 2009
 * This is a simple class which allows connection to a remote
 * database for loading resource records.
 *
 * The main purpose of this is so that a user can still access
 * collections from a database which is not running the same client
 * version.
 *
 */

package org.archiviststoolkit.plugin.dbdialog;


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.sql.SQLException;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.model.ATPluginData;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.model.LookupListItems;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.util.DatabaseConnectionUtils;
import org.archiviststoolkit.util.DatabaseConnectionInformation;
import org.hibernate.cfg.Configuration;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

/**
 * @author Nathan Stevens
 */
public class RemoteDBConnectDialog extends JDialog {
    // the hibernate session factory
    private org.hibernate.SessionFactory sessionFactory = null;

    // The session that is used through out to load records
    Session session = null;

    // The saved connection information
    private HashMap savedConnections = new HashMap();

    // This stores configuration require to export data
    ATPluginData pluginData = null;

    // List that holds the records returned from the database
    java.util.List recordList;

    public RemoteDBConnectDialog(Frame owner) {
        super(owner, "Remote Database Connection");
        initComponents();
        loadDatabaseConnectionInformation();
    }

    public RemoteDBConnectDialog(Dialog owner) {
        super(owner, "Remote Database Connection");
        initComponents();
        loadDatabaseConnectionInformation();
    }


    /**
     * Method to update the connection url information displayed to user
     */
    private void updateConnectionUrlInformation() {
        String selectedUrl = (String) connectionUrl.getSelectedItem();
        if (savedConnections.containsKey(selectedUrl)) {
            DatabaseConnectionInformation dbInfo = (DatabaseConnectionInformation) savedConnections.get(selectedUrl);
            textField1.setText(dbInfo.getUsername());
            passwordField1.setText(dbInfo.getPassword());
            comboBox2.setSelectedItem(dbInfo.getDatabaseType());
        }
    }

    /**
     * Just set this window invisible
     */
    private void okButtonActionPerformed() {
        setVisible(false);
    }

    /**
     * The connect button calls but all the work is done in connectToDatabase2.
     */
    private void connectToDatabase() {
        // disable the connect button and start the progress bar going
        button1.setEnabled(false);
        progressBar1.setIndeterminate(true);
        progressBar1.setStringPainted(true);
        progressBar1.setString("Connecting to database");

        Thread performer = new Thread(new Runnable() {
            public void run() {
                // try connecting to the database and if successfully try loading the resource
                // records into the list
                if (connectToDatabase2()) {
                    progressBar1.setString("Loading records");
                    loadResourceRecords();
                }

                // all done, so enable the process button and stop the progress bar
                progressBar1.setIndeterminate(false);
                progressBar1.setStringPainted(false);
                button1.setEnabled(true);
            }
        });
        performer.start();
    }

    /**
     * Connect to the AT database at the given location. This does not check database version
     * so the it should be able to work with version 1.5 and 1.5.7
     */
    private boolean connectToDatabase2() {
        // based on the database type set the driver and hibernate dialect
        String databaseType = (String) comboBox2.getSelectedItem();
        String driverClass = "";
        String hibernateDialect = "";

        if (databaseType.equals(SessionFactory.DATABASE_TYPE_MYSQL)) {
            driverClass = "com.mysql.jdbc.Driver";
            hibernateDialect = "org.hibernate.dialect.MySQLInnoDBDialect";
        } else if (databaseType.equals(SessionFactory.DATABASE_TYPE_MICROSOFT_SQL_SERVER)) {
            driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            hibernateDialect = "org.hibernate.dialect.SQLServerDialect";
        } else if (databaseType.equals(SessionFactory.DATABASE_TYPE_ORACLE)) {
            driverClass = "oracle.jdbc.OracleDriver";
            hibernateDialect = "org.hibernate.dialect.Oracle10gDialect";
        } else if (databaseType.equals(SessionFactory.DATABASE_TYPE_INTERNAL)) {
            driverClass = "org.hsqldb.jdbcDriver";
            hibernateDialect = "org.hibernate.dialect.HSQLDialect";
        }else { // should never get here
            System.out.println("Unknown database type : " + databaseType);
            return false;
        }

        // now attempt to build the session factory
        String databaseUrl = (String) connectionUrl.getSelectedItem();
        String userName = textField1.getText();
        String password = new String(passwordField1.getPassword());

        try {
            Configuration config = new Configuration().configure();
            Properties properties = config.getProperties();
            properties.setProperty("hibernate.connection.driver_class", driverClass);
            if (databaseType.equals(SessionFactory.DATABASE_TYPE_MYSQL)) {
                properties.setProperty("hibernate.connection.url", databaseUrl + "?useUnicode=yes&characterEncoding=utf8");
            } else {
                properties.setProperty("hibernate.connection.url", databaseUrl);
            }
            //deal with oracle specific settings
            if (databaseType.equals(SessionFactory.DATABASE_TYPE_ORACLE)) {
                properties.setProperty("hibernate.jdbc.batch_size", "0");
                properties.setProperty("hibernate.jdbc.use_streams_for_binary", "true");
                properties.setProperty("SetBigStringTryClob", "true");
            }
            properties.setProperty("hibernate.connection.username", userName);
            properties.setProperty("hibernate.connection.password", password);
            properties.setProperty("hibernate.dialect", hibernateDialect);
            config.setProperties(properties);
            sessionFactory = config.buildSessionFactory();

            //test the session factory to make sure it is working
            testHibernate();

            return true; // connected successfully so return true
        } catch (Exception hibernateException) {
            hibernateException.printStackTrace();

            JOptionPane.showMessageDialog(this,
                    "Failed to start hibernate engine ...",
                    "Hibernate Error",
                    JOptionPane.ERROR_MESSAGE);

            return false;
        }
    }

    /**
     * Method to load all the resource records from the database
     */
    private void loadResourceRecords() {
        session = sessionFactory.openSession(); // open the session now

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(Resources.class);
            recordList = criteria.list();
            tx.commit();

        } catch (RuntimeException ex) {
            try {
                tx.rollback();
            } catch (HibernateException e) {
                JOptionPane.showMessageDialog(this,
                        "Failed to load resource records ...",
                        "Record Load Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            return;
        }

        // add the records to the list in the UDT thread
        Runnable doWorkRunnable = new Runnable() {
            public void run() {
                // now sort the returned list
                Collections.sort(recordList);

                // clear any records in the list
                list1.removeAll();

                for (Object object : recordList) {
                    list1.addElement(object);
                }
            }
        };
        SwingUtilities.invokeLater(doWorkRunnable);
    }

    /**
     * Method to test the hibernate connection to make sure it can connect to an
     * AT database.
     *
     * @throws SQLException if it can't connect to the AT database
     */
    private void testHibernate() throws SQLException {
        Session testSession = sessionFactory.openSession();
        Query query = testSession.createQuery("select count(*) from Constants");
        Transaction tx = testSession.beginTransaction();
        query.list();
        tx.commit();
        testSession.close();
    }

    /**
     * function to return the selected record or null if nothing is selected
     */
    public DomainObject getCurrentRecord() {
        Object object = list1.getSelectedValue();
        if (object != null) {
            return (DomainObject) object;
        } else {
            return null;
        }
    }

    public Resources getResourceRecord() throws Exception {
        return getResourceRecord(getCurrentRecord().getIdentifier());
    }

    public Resources getResourceRecord(Long identifier) throws Exception {
        java.util.List completeList;
        Transaction tx = null;

        // find the data we need
        tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(Resources.class);
        criteria.add(Restrictions.eq("resourceId", identifier));

        completeList = criteria.list();
        tx.commit();

        // now get the first data element
        if (completeList.size() > 0) {
            return (Resources) completeList.get(0);
        } else {
            return null;
        }
    }

    /**
     * Method to get the language codes from the AT database
     *
     * @return HashMap containing the langauge codes
     * @throws Exception If anything goes wrong an exception is thrown
     */
    public HashMap<String, String> getLanguageCodes() throws Exception {
        // find the data we need
        String hql = "from LookupListItems where lookuplistId = 21";
        Query query = session.createQuery(hql);

        List completeList = query.list();

        // now get the first data element
        if (completeList.size() > 0) {
            HashMap<String, String> languageCodes = new HashMap<String, String>();

            for (Object object : completeList) {
                LookupListItems item = (LookupListItems) object;
                languageCodes.put(item.getCode(), item.getListItem());
            }

            return languageCodes;
        } else {
            return null;
        }
    }

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
        }
    }

    public ArrayList<DomainObject> getRecordList() {
        ArrayList<DomainObject> records = new ArrayList<DomainObject>();

        for (Object object : recordList) {
            records.add((DomainObject) object);
        }

        return records;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        connectionUrl = new JComboBox();
        label2 = new JLabel();
        textField1 = new JTextField();
        label3 = new JLabel();
        passwordField1 = new JPasswordField();
        label4 = new JLabel();
        comboBox2 = new JComboBox();
        label7 = new JLabel();
        comboBox1 = new JComboBox();
        label6 = new JLabel();
        textField2 = new JTextField();
        scrollPane1 = new JScrollPane();
        list1 = new FilteringJList();
        list1.installJTextField(textField2);
        label5 = new JLabel();
        progressBar1 = new JProgressBar();
        buttonBar = new JPanel();
        button1 = new JButton();
        okButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(Borders.DIALOG_BORDER);
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
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
                label1.setText("Connection URL");
                contentPanel.add(label1, cc.xy(1, 1));

                //---- connectionUrl ----
                connectionUrl.setEditable(true);
                connectionUrl.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
                        updateConnectionUrlInformation();
                    }
                });
                contentPanel.add(connectionUrl, cc.xy(3, 1));

                //---- label2 ----
                label2.setText("Username");
                contentPanel.add(label2, cc.xy(1, 3));

                //---- textField1 ----
                textField1.setColumns(25);
                contentPanel.add(textField1, cc.xy(3, 3));

                //---- label3 ----
                label3.setText("Password");
                contentPanel.add(label3, cc.xy(1, 5));
                contentPanel.add(passwordField1, cc.xy(3, 5));

                //---- label4 ----
                label4.setText("Database Type");
                contentPanel.add(label4, cc.xy(1, 7));

                //---- comboBox2 ----
                comboBox2.setModel(new DefaultComboBoxModel(new String[] {
                    "MySQL",
                    "Oracle",
                    "Microsoft SQL Server",
                    "Internal Database"
                }));
                contentPanel.add(comboBox2, cc.xy(3, 7));

                //---- label7 ----
                label7.setText("Record Type");
                contentPanel.add(label7, cc.xy(1, 9));

                //---- comboBox1 ----
                comboBox1.setModel(new DefaultComboBoxModel(new String[] {
                    "Resource Records",
                    "Digital Object Records",
                    "Accession Records",
                    "Name Records",
                    "Subject Records"
                }));
                contentPanel.add(comboBox1, cc.xy(3, 9));

                //---- label6 ----
                label6.setText("Filter Results");
                contentPanel.add(label6, cc.xy(1, 11));
                contentPanel.add(textField2, cc.xy(3, 11));

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(list1);
                }
                contentPanel.add(scrollPane1, cc.xywh(1, 13, 3, 1));

                //---- label5 ----
                label5.setText("Progress");
                contentPanel.add(label5, cc.xy(1, 15));
                contentPanel.add(progressBar1, cc.xy(3, 15));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
                buttonBar.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.GLUE_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.BUTTON_COLSPEC
                    },
                    RowSpec.decodeSpecs("pref")));

                //---- button1 ----
                button1.setText("Connect");
                button1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        connectToDatabase();
                    }
                });
                buttonBar.add(button1, cc.xy(2, 1));

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        okButtonActionPerformed();
                    }
                });
                buttonBar.add(okButton, cc.xy(8, 1));
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
    private JComboBox connectionUrl;
    private JLabel label2;
    private JTextField textField1;
    private JLabel label3;
    private JPasswordField passwordField1;
    private JLabel label4;
    private JComboBox comboBox2;
    private JLabel label7;
    private JComboBox comboBox1;
    private JLabel label6;
    private JTextField textField2;
    private JScrollPane scrollPane1;
    private FilteringJList list1;
    private JLabel label5;
    private JProgressBar progressBar1;
    private JPanel buttonBar;
    private JButton button1;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
