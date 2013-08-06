/*
 * Created by JFormDesigner on Wed Oct 01 14:30:51 EDT 2008
 */

package org.archiviststoolkit.dialog;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.io.*;
import java.util.Collection;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
//import com.thoughtworks.xstream.XStream;
import org.archiviststoolkit.mydomain.*;
import org.hibernate.Session;
import org.hibernate.LockMode;
import org.archiviststoolkit.model.*;

/**
 * @author Nathan Stevens
 */
public class ATDebugDialog extends JFrame {
    private DomainTableWorkSurface worksurface;
    private DomainEditor dialog;
    private boolean lockTest = true; // use to test locking of the program when open a resource record
    private DomainObject record; // the text domain object

    public ATDebugDialog() {
        initComponents();
    }

    // constructor that takes a domain table work surface
    public ATDebugDialog(DomainTableWorkSurface worksurface, DomainEditor dialog) {
        initComponents();
        this.worksurface = worksurface;
        this.dialog = dialog;
    }

    private void okButtonActionPerformed(ActionEvent e) {
        setVisible(false);
        dispose();
    }

    private void button1ActionPerformed(ActionEvent e) {
        Thread performer = new Thread(new Runnable() {
            public void run() {
                DomainObject record = worksurface.getCurrentDomainObjectFromDatabase();
                // try exporting to xml file now
                if(record != null) {
                    /*XStream xstream = new XStream();
                    if(record instanceof Resources) {
                        Resources res = (Resources)record;
                        res.getResourcesComponents();
                        textArea1.setText(xstream.toXML(res));
                    } else {
                        textArea1.setText(xstream.toXML(record));
                    }*/
                }
            }
        });
        performer.start();
    }

    private void button2ActionPerformed(ActionEvent e) {
        textArea1.setText("");
    }

    private void button3ActionPerformed(ActionEvent e) {
        /*XStream xstream = new XStream();
        System.out.println("Creating DomainObject from xml text ...");
        DomainObject record = (DomainObject)xstream.fromXML(textArea1.getText());
        try {
            System.out.println("Saving DomainObject to database ...");
            DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(worksurface.getClazz());
            Session session = access.getLongSession();
            //session.lock(record, LockMode.NONE);
            Object object = session.merge(record);
            //session.saveOrUpdate(record);
            session.saveOrUpdate(object);
            session.flush();
            session.connection().commit();
        } catch(PersistenceException pe) {
            pe.printStackTrace();
        } catch(SQLException se) {
            se.printStackTrace();
        } */
    }

    private void button4ActionPerformed(ActionEvent e) {
        //save record to a file
        try {
            DomainObject record = worksurface.getCurrentDomainObjectFromDatabase();
            String filename = System.getProperty("user.home") + "/AT_Record.ser";
            File file = new File(filename);

            // need to load al l components and data if resource record
            if(record instanceof Resources) {
                Resources res = (Resources)record;

                for(ArchDescriptionRepeatingData rd : res.getRepeatingData()) {
                    for(ArchDescriptionRepeatingData rd2 : rd.getChildren()) {
                        rd2.getChildren();
                    }
                }

                res.getDeaccessions();
                res.getSubjects();
                res.getNames();
                res.getInstances();
                for (ResourcesComponents component : res.getResourcesComponents()) {
				    loadComponents(component);
			    }
            }

            textArea1.setText("Writing record to file : " + record.toString() + "\n\n");
            // Serialize to a file
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(record);
            out.close();

            textArea1.append("Done Writing record to file ...\n\n");

            // read from a file
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            DomainObject newRecord = (DomainObject) in.readObject();
            in.close();

            textArea1.append("Read record from file ..." + newRecord.toString() + "\n\n");
        } catch (ClassNotFoundException ce) {
            ce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }


        /*// try saving all record to the database
        try {
            XStream xstream = new XStream();

            File theFile = new File("/Users/nathan/TestATPrint");                           // simply a file name
            FileOutputStream outStream = new FileOutputStream(theFile);             // generic stream to the file
            ObjectOutputStream out = xstream.createObjectOutputStream(outStream);

            // get all names

            // get all subjects

            // get all the resource records
            ResourcesDAO resourcesDAO = new ResourcesDAO();

            textArea1.append("Exporting Resources to Files...\n");
            Resources resource;

            Collection resources = resourcesDAO.findAllLongSession(true);
            int count = 1;

            for (Object o : resources) {
                resource = (Resources) o;
                // load all components
                loadComponents(resource);
                out.writeObject(resource);
                textArea1.append("Resources : " + count + " = " + resource + "\n");
                count++;
            }

            resourcesDAO.closeLongSession();
            out.close();
        } catch(FileNotFoundException fe) {
            fe.printStackTrace();
        } catch(IOException ie) {
            ie.printStackTrace();
        } catch(LookupException le) {
            le.printStackTrace();
        } catch(SQLException se) {
            se.printStackTrace();
        }
        */
    }

    // Method to load all components components. This make recource calls
    private void loadComponents(ResourcesComponents resourceComponent) {
        for(ArchDescriptionRepeatingData rd : resourceComponent.getRepeatingData()) {
            for(ArchDescriptionRepeatingData rd2 : rd.getChildren()) {
                rd2.getChildren();
            }
        }

        for(ArchDescriptionInstances instance : resourceComponent.getInstances()) {
            instance.getInstanceLabel();
        }

        for(ArchDescriptionNames name :  resourceComponent.getNames()) {
            name.getSortName();
        }

        for(ArchDescriptionSubjects subject : resourceComponent.getSubjects()) {
            subject.getSubjectTerm();
        }

        if (resourceComponent.isHasChild()) {
			for (ResourcesComponents childComponent : resourceComponent.getResourcesComponents()) {
				loadComponents(childComponent);
			}
		}
    }

    private void button5ActionPerformed(ActionEvent e) {
        // try copying the current domain object. this is a test of serialization
        Thread performer = new Thread(new Runnable() {
            public void run() {
                DomainObject record = worksurface.getCurrentDomainObjectFromDatabase();

                if(record != null) {
                    textArea1.setText("Starting Copy of " + record.toString() + "\n\n");

                    try {
                        DomainObject recordCopy = deepCopy(record);
                    } catch (Exception e) { }

                    textArea1.append("Finish Copy of " + record.toString() + "\n\n");
                }
            }
        });
        performer.start();
    }

    // method to perform a deep copy of an object
    private DomainObject deepCopy(DomainObject domainObject) throws Exception {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            ByteArrayOutputStream bos =
                    new ByteArrayOutputStream(); // A
            oos = new ObjectOutputStream(bos); // B
            // serialize and pass the object
            oos.writeObject(domainObject);   // C
            oos.flush();               // D
            ByteArrayInputStream bin =
                    new ByteArrayInputStream(bos.toByteArray()); // E
            ois = new ObjectInputStream(bin);                  // F
            // return the new object
            return (DomainObject) ois.readObject(); // G
        }
        catch (Exception e) {
            System.out.println("Exception in ObjectCloner = " + e);
            throw(e);
        }
        finally {
            oos.close();
            ois.close();
        }
    }

    /**
     * Method to open and close a resource record every 10 seconds or so
     * @param e
     */
    private void startLockTest(ActionEvent e) {
       Thread performer = new Thread(new Runnable() {
            public void run() {
                // alert user text has started
                textArea2.setText("Starting Program Lock Test ...\n\n");

                int count = 1; // the current count of the record opened
                Collection results = worksurface.getResultSet();
                int max = results.size() - 1;
                int selectedRow = worksurface.getSelectedRow();

                while(lockTest) {
                    textArea2.append("Record Opened " + count + " Time(s)\n");
                    count++;

                    // open the record now
                    worksurface.onUpdate();

                    try {
                        int wait1 = Integer.parseInt(textField1.getText())*1000;
                        int wait2 = Integer.parseInt(textField2.getText())*1000;

                        Thread.sleep(wait1); // wait a period of time
                        // close the dialog
                        dialog.closeAndNoSave();

                        Thread.sleep(wait2); // wait a period of time

                        // move to the next entry in the field
                        selectedRow++;
                        if(selectedRow > max) {
                            selectedRow = 0;
                        }

                        // interate until we find a record that is not locked
                        while(worksurface.setSelectedRow(selectedRow)) {
                            selectedRow++;
                        }

                        Thread.sleep(2000); // wait 2 seconds

                    } catch(InterruptedException ie) {
                        lockTest = false;
                    }
                }

                // indicate the testing was stop
                textArea2.append("\n\nProgram Lock Test Stop ...");
            }
        });
        performer.start();
    }

    private void stopLockTest(ActionEvent e) {
        lockTest = false;
    }

    private void clearLockTestOutput(ActionEvent e) {
        textArea2.setText("");
    }

    private void button9ActionPerformed(ActionEvent e) {
        record = worksurface.getCurrentDomainObjectFromDatabase();
        panel3.setModel(record, null);
    }

    private void button10ActionPerformed(ActionEvent e) {
        if(record != null) {
            try {
                DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(worksurface.getClazz());
                access.updateLongSession(record);
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        tabbedPane1 = new JTabbedPane();
        panel2 = new JPanel();
        textField1 = new JTextField();
        textField2 = new JTextField();
        button6 = new JButton();
        button7 = new JButton();
        button8 = new JButton();
        scrollPane2 = new JScrollPane();
        textArea2 = new JTextArea();
        panel1 = new JPanel();
        button1 = new JButton();
        button3 = new JButton();
        button5 = new JButton();
        button4 = new JButton();
        button2 = new JButton();
        scrollPane1 = new JScrollPane();
        textArea1 = new JTextArea();
        panel3 = new ATDebugEditorFields();
        buttonBar = new JPanel();
        button9 = new JButton();
        button10 = new JButton();
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
                contentPanel.setLayout(new BorderLayout());

                //======== tabbedPane1 ========
                {

                    //======== panel2 ========
                    {
                        panel2.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec(Sizes.dluX(80)),
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                            },
                            new RowSpec[] {
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                new RowSpec(RowSpec.FILL, Sizes.dluY(119), FormSpec.DEFAULT_GROW)
                            }));

                        //---- textField1 ----
                        textField1.setText("20");
                        panel2.add(textField1, cc.xy(1, 1));

                        //---- textField2 ----
                        textField2.setText("5");
                        panel2.add(textField2, cc.xy(3, 1));

                        //---- button6 ----
                        button6.setText("Start Test");
                        button6.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                startLockTest(e);
                            }
                        });
                        panel2.add(button6, cc.xy(1, 3));

                        //---- button7 ----
                        button7.setText("Stop Test");
                        button7.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                stopLockTest(e);
                            }
                        });
                        panel2.add(button7, cc.xy(3, 3));

                        //---- button8 ----
                        button8.setText("Clear");
                        button8.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                clearLockTestOutput(e);
                            }
                        });
                        panel2.add(button8, cc.xy(7, 3));

                        //======== scrollPane2 ========
                        {
                            scrollPane2.setViewportView(textArea2);
                        }
                        panel2.add(scrollPane2, cc.xywh(1, 5, 7, 1));
                    }
                    tabbedPane1.addTab("Program Lock Test", panel2);


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
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                            },
                            new RowSpec[] {
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                            }));

                        //---- button1 ----
                        button1.setText("Record to XML");
                        button1.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                button1ActionPerformed(e);
                            }
                        });
                        panel1.add(button1, cc.xy(1, 1));

                        //---- button3 ----
                        button3.setText("XML To Record");
                        button3.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                button3ActionPerformed(e);
                            }
                        });
                        panel1.add(button3, cc.xy(3, 1));

                        //---- button5 ----
                        button5.setText("Copy");
                        button5.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                button5ActionPerformed(e);
                            }
                        });
                        panel1.add(button5, cc.xy(5, 1));

                        //---- button4 ----
                        button4.setText("Save");
                        button4.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                button4ActionPerformed(e);
                            }
                        });
                        panel1.add(button4, cc.xy(7, 1));

                        //---- button2 ----
                        button2.setText("clear text");
                        button2.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                button2ActionPerformed(e);
                            }
                        });
                        panel1.add(button2, cc.xy(9, 1));

                        //======== scrollPane1 ========
                        {
                            scrollPane1.setViewportView(textArea1);
                        }
                        panel1.add(scrollPane1, cc.xywh(1, 3, 9, 3));
                    }
                    tabbedPane1.addTab("Export", panel1);

                    tabbedPane1.addTab("ISO Date Test", panel3);

                }
                contentPanel.add(tabbedPane1, BorderLayout.CENTER);
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
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

                //---- button9 ----
                button9.setText("Set Record");
                button9.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        button9ActionPerformed(e);
                    }
                });
                buttonBar.add(button9, cc.xy(2, 1));

                //---- button10 ----
                button10.setText("Save Record");
                button10.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        button10ActionPerformed(e);
                    }
                });
                buttonBar.add(button10, cc.xy(4, 1));

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        okButtonActionPerformed(e);
                    }
                });
                buttonBar.add(okButton, cc.xy(6, 1));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        setSize(635, 360);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JTabbedPane tabbedPane1;
    private JPanel panel2;
    private JTextField textField1;
    private JTextField textField2;
    private JButton button6;
    private JButton button7;
    private JButton button8;
    private JScrollPane scrollPane2;
    private JTextArea textArea2;
    private JPanel panel1;
    private JButton button1;
    private JButton button3;
    private JButton button5;
    private JButton button4;
    private JButton button2;
    private JScrollPane scrollPane1;
    private JTextArea textArea1;
    private ATDebugEditorFields panel3;
    private JPanel buttonBar;
    private JButton button9;
    private JButton button10;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
