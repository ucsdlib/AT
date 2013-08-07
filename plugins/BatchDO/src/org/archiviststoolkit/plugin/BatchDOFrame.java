/*
 * Created by JFormDesigner on Wed Sep 15 10:08:55 EDT 2010
 *
 * Simple class for testing the functionality of the BatchDO plugin
 * without having to start up the AT
 */

package org.archiviststoolkit.plugin;

import org.archiviststoolkit.model.*;
import org.archiviststoolkit.plugin.dbdialog.RemoteDBConnectDialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Simple class for testing the functionality of the BatchDO plugin without
 * running in the AT.
 * 
 * @author Nathan Stevens
 */
public class BatchDOFrame extends JFrame {
    // used to connect to the AT database when running in stand alone mode
    private RemoteDBConnectDialog dbDialog;

    public BatchDOFrame() {
        initComponents();
    }

    private void okButtonActionPerformed() {
        System.exit(0);
    }

    private void loadRecordButtonActionPerformed() {
        if (dbDialog == null) {
            dbDialog = new RemoteDBConnectDialog(this);
            dbDialog.pack();
        }

        dbDialog.setVisible(true);
    }

    /**
     * Method to open up the DO Workflow dialog. This is mainly for
     * testing purposes.
     */
    private void boxProcessorButtonActionPerformed() {
        // check connection to the database and that there is a record loaded
        if (dbDialog == null || dbDialog.getCurrentRecord() == null) {
            return;
        }

        try {
            Resources fullRecord = dbDialog.getResourceRecord();

            DOWorkflowDialog workflowDialog = new DOWorkflowDialog(this, "Box Processor Debug", fullRecord, fullRecord);
            workflowDialog.pack();
            workflowDialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Launch the Barcode cleaner
     */
    private void cleanBarcodeButtonActionPerformed() {
        // check connection to the database and that there is a record loaded
        if (dbDialog == null || dbDialog.getCurrentRecord() == null) {
            return;
        }

        try {
            Resources fullRecord = dbDialog.getResourceRecord();

            BarcodeCleaner barcodeCleaner = new BarcodeCleaner();
            barcodeCleaner.setModel(fullRecord, null);
            barcodeCleaner.showPlugin(this, "Barcode Cleaner", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        doWorkflowButton = new JButton();
        cleanBarcodeButton = new JButton();
        buttonBar = new JPanel();
        loadRecordButton = new JButton();
        okButton = new JButton();

        //======== this ========
        setTitle("BatchDO 2.2");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new GridBagLayout());
                ((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
                ((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 0};
                ((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0, 1.0E-4};
                ((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

                //---- doWorkflowButton ----
                doWorkflowButton.setText("Digital Object Workflow");
                doWorkflowButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        boxProcessorButtonActionPerformed();
                    }
                });
                contentPanel.add(doWorkflowButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- cleanBarcodeButton ----
                cleanBarcodeButton.setText("Clean Barcode");
                cleanBarcodeButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        cleanBarcodeButtonActionPerformed();
                    }
                });
                contentPanel.add(cleanBarcodeButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 0, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0};

                //---- loadRecordButton ----
                loadRecordButton.setText("Record Selector");
                loadRecordButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        loadRecordButtonActionPerformed();
                    }
                });
                buttonBar.add(loadRecordButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        okButtonActionPerformed();
                    }
                });
                buttonBar.add(okButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        setSize(415, 140);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JButton doWorkflowButton;
    private JButton cleanBarcodeButton;
    private JPanel buttonBar;
    private JButton loadRecordButton;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
