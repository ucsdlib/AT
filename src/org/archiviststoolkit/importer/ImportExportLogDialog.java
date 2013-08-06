package org.archiviststoolkit.importer;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.swing.PrintableJTextArea;
import org.archiviststoolkit.dialog.ATFileChooser;
import org.archiviststoolkit.dialog.ErrorDialog;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
/*
 * Created by JFormDesigner on Thu Feb 09 12:36:03 EST 2006
 */


/**
 * @author Lee Mandell
 * @modified Nathan Stevens
 */
public class ImportExportLogDialog extends JDialog implements ClipboardOwner {

    public static final String DIALOG_TYPE_EXPORT = "export";
    public static final String DIALOG_TYPE_IMPORT = "import";

    public ImportExportLogDialog(String dialogType) {
        super();
        initComponents();
        setDialogTitle(dialogType);
    }

    public ImportExportLogDialog(String logText, String dialogType) {
        super();
        initComponents();
        this.logText.setText(logText);
        setDialogTitle(dialogType);
    }

    public ImportExportLogDialog(Frame owner, String dialogType) {
        super(owner);
        initComponents();
        setDialogTitle(dialogType);
    }

    public ImportExportLogDialog(Dialog owner, String dialogType) {
        super(owner);
        initComponents();
        setDialogTitle(dialogType);
    }

    public ImportExportLogDialog(Dialog owner, String dialogType, String logText) {
        super(owner);
        initComponents();
        this.logText.setText(logText);
        setDialogTitle(dialogType);
    }

    private void printActionPerformed(ActionEvent e) {
        logText.print();
    }

    private void saveActionPerformed(ActionEvent e) {
        ATFileChooser filechooser = new ATFileChooser();
        if (filechooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = filechooser.getSelectedFile();

            try {
                FileWriter fileWriter = new FileWriter(selectedFile);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                bufferedWriter.write(logText.getText());

                bufferedWriter.close();
            } catch (IOException ioe) {
                new ErrorDialog(this, "Error saving log message", ioe).showDialog();
            }
        }
    }

    // Method to copy the content of the text area to the system clipboard
    private void copyButtonActionPerformed() {
        StringSelection stringSelection = new StringSelection(logText.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, this);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        dialogTitle = new JLabel();
        scrollPane1 = new JScrollPane();
        logText = new PrintableJTextArea();
        buttonBar = new JPanel();
        copyButton = new JButton();
        printButton = new JButton();
        saveButton = new JButton();
        okButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setModal(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(Borders.DIALOG_BORDER);
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new FormLayout(
                    ColumnSpec.decodeSpecs("max(default;600px):grow"),
                    new RowSpec[] {
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                    }));

                //---- dialogTitle ----
                dialogTitle.setText("Import Log");
                contentPanel.add(dialogTitle, cc.xy(1, 1));

                //======== scrollPane1 ========
                {
                    scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                    //---- logText ----
                    logText.setRows(20);
                    logText.setLineWrap(true);
                    scrollPane1.setViewportView(logText);
                }
                contentPanel.add(scrollPane1, cc.xy(1, 3));
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
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.BUTTON_COLSPEC
                    },
                    RowSpec.decodeSpecs("pref")));

                //---- copyButton ----
                copyButton.setText("Copy");
                copyButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        copyButtonActionPerformed();
                    }
                });
                buttonBar.add(copyButton, cc.xy(4, 1));

                //---- printButton ----
                printButton.setText("Print");
                printButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        printActionPerformed(e);
                    }
                });
                buttonBar.add(printButton, cc.xy(6, 1));

                //---- saveButton ----
                saveButton.setText("Save");
                saveButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        saveActionPerformed(e);
                    }
                });
                buttonBar.add(saveButton, cc.xy(8, 1));

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        okButtonActionPerformed(e);
                    }
                });
                buttonBar.add(okButton, cc.xy(10, 1));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void okButtonActionPerformed(ActionEvent e) {
        this.setVisible(false);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel dialogTitle;
    private JScrollPane scrollPane1;
    private PrintableJTextArea logText;
    private JPanel buttonBar;
    private JButton copyButton;
    private JButton printButton;
    private JButton saveButton;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public final void showDialog() {

        this.pack();
        setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void setDialogTitle(String dialogType) {
        if (dialogType.equals(DIALOG_TYPE_EXPORT)) {
            dialogTitle.setText("Export Log");
        } else if (dialogType.equals(DIALOG_TYPE_IMPORT)) {
            dialogTitle.setText("Import Log");
        }
    }

    public void lostOwnership(Clipboard clipboard, Transferable contents) { }
}
