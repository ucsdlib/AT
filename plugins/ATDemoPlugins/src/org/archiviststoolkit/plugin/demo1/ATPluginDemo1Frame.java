/*
 * Created by JFormDesigner on Tue Feb 10 13:28:01 EST 2009
 */

package org.archiviststoolkit.plugin.demo1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.plugin.ATPlugin;
import org.archiviststoolkit.plugin.ATPluginUtils;
import org.archiviststoolkit.mydomain.DomainTableWorkSurface;
import org.archiviststoolkit.mydomain.DomainObject;

/**
 * @author Nathan Stevens
 */
public class ATPluginDemo1Frame extends JFrame {
    private ApplicationFrame mainFrame;
    private DomainTableWorkSurface worksurface;
    private DomainObject domainObject;
    private ATPlugin plugin;

    public ATPluginDemo1Frame() {
        super("ATPlugin Demo 1");
        initComponents();
    }

    public ATPluginDemo1Frame(ATPlugin plugin) {
        super(plugin.getName());
        initComponents();
        this.plugin = plugin;
    }

    private void okButtonActionPerformed(ActionEvent e) {
        setVisible(false);
        dispose();
    }

    // get the current domain object from the database
    private void button1ActionPerformed(ActionEvent e) {
        domainObject = worksurface.getCurrentDomainObjectFromDatabase();
        textArea1.setText("" + domainObject.toString() + "\n\n");
    }

    // try saving the record to the database as xml
    private void button2ActionPerformed(ActionEvent e) {
        if(domainObject != null) {
            try {
                textArea1.append("Saving " + domainObject.toString() + " to database ...\n");
                ATPluginUtils.saveData(plugin.getName(), 1, textField1.getText(), "record", domainObject);
                textArea1.append("Done ...\n\n");
            } catch(Exception e1) {
                textArea1.setText("Unable to save " + domainObject.toString() + " to database ...");
            }
        }
    }

    // try to get a saved object from the DB
    private void button3ActionPerformed(ActionEvent e) {
        String dataName = textField1.getText();

        try {
            textArea1.append("Getting saved data " + dataName + " from database ...\n");

            Object data = ATPluginUtils.getDataByName(plugin.getName(), dataName);

            if(data instanceof DomainObject) {
                DomainObject record = (DomainObject)data;
                textArea1.append("Record " + record.toString() + " retrieved from database ...\n");
            } else { // assume string data
                textArea1.append("Text data : " + data.toString() + " retrieved from database ...\n");
            }

            textArea1.append("Done ...\n\n");
        } catch (Exception e1) {
            textArea1.setText("Unable to save " + domainObject.toString() + " to database ...\n");
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        panel1 = new JPanel();
        button1 = new JButton();
        label2 = new JLabel();
        textField1 = new JTextField();
        button2 = new JButton();
        button3 = new JButton();
        scrollPane1 = new JScrollPane();
        textArea1 = new JTextArea();
        buttonBar = new JPanel();
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
                    ColumnSpec.decodeSpecs("default:grow"),
                    new RowSpec[] {
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                    }));

                //---- label1 ----
                label1.setText("Demo AT Plugin");
                contentPanel.add(label1, cc.xywh(1, 1, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

                //======== panel1 ========
                {
                    panel1.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC
                        },
                        RowSpec.decodeSpecs("default, default")));

                    //---- button1 ----
                    button1.setText("Get Current Record");
                    button1.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            button1ActionPerformed(e);
                        }
                    });
                    panel1.add(button1, cc.xywh(1, 1, 3, 1));

                    //---- label2 ----
                    label2.setText("Data Name :");
                    panel1.add(label2, cc.xy(1, 2));

                    //---- textField1 ----
                    textField1.setColumns(8);
                    textField1.setText("Test Data");
                    panel1.add(textField1, cc.xy(3, 2));

                    //---- button2 ----
                    button2.setText("Save Data");
                    button2.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            button2ActionPerformed(e);
                        }
                    });
                    panel1.add(button2, cc.xy(5, 2));

                    //---- button3 ----
                    button3.setText("Get Data");
                    button3.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            button3ActionPerformed(e);
                        }
                    });
                    panel1.add(button3, cc.xy(7, 2));
                }
                contentPanel.add(panel1, cc.xy(1, 3));

                //======== scrollPane1 ========
                {

                    //---- textArea1 ----
                    textArea1.setRows(5);
                    textArea1.setColumns(25);
                    scrollPane1.setViewportView(textArea1);
                }
                contentPanel.add(scrollPane1, cc.xy(1, 5));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
                buttonBar.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        FormFactory.GLUE_COLSPEC,
                        FormFactory.BUTTON_COLSPEC
                    },
                    RowSpec.decodeSpecs("pref")));

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        okButtonActionPerformed(e);
                    }
                });
                buttonBar.add(okButton, cc.xy(2, 1));
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
    private JPanel panel1;
    private JButton button1;
    private JLabel label2;
    private JTextField textField1;
    private JButton button2;
    private JButton button3;
    private JScrollPane scrollPane1;
    public JTextArea textArea1;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public void setApplicationFrame(ApplicationFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.worksurface = mainFrame.getWorkSurfaceContainer().getCurrentWorkSurface();
    }
}
