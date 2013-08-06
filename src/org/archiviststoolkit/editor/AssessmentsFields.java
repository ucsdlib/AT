/*
 * Created by JFormDesigner on Tue May 05 11:57:33 EDT 2009
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.model.Assessments;
import org.archiviststoolkit.model.AssessmentsResources;
import org.archiviststoolkit.model.AssessmentsAccessions;
import org.archiviststoolkit.model.AssessmentsDigitalObjects;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.dialog.ResourceLookup;
import org.archiviststoolkit.dialog.DigitalObjectLookup;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.dialog.AccessionLookup;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.plugin.ATPlugin;
import org.archiviststoolkit.plugin.ATPluginFactory;

/**
 * @author Nathan Stevens
 */
public class AssessmentsFields extends DomainEditorFields {
    private ArrayList<ATPlugin> plugins = null; // stores any embedded assessment editor plugins

    public AssessmentsFields() {
        super();
        initComponents();
        addRelatedTableInformation();
    }

    private void addRelatedTableInformation() {
        // configure the AssessmentsAccessions table
        accessionsTable.setClazzAndColumns(AssessmentsAccessions.PROPERTYNAME_ACCESSION_NUMBER,
				AssessmentsAccessions.class,
				AssessmentsAccessions.PROPERTYNAME_ACCESSION_NUMBER,
				AssessmentsAccessions.PROPERTYNAME_ACCESSION_TITLE);

        // configure the AssessmentsResources table
        resourcesTable.setClazzAndColumns(AssessmentsResources.PROPERTYNAME_RESOURCE_IDENTIFIER,
				AssessmentsResources.class,
				AssessmentsResources.PROPERTYNAME_RESOURCE_IDENTIFIER,
				AssessmentsResources.PROPERTYNAME_RESOURCE_TITLE);

        // configure the AssessmentsDigitalObjects table
        digitalObjectsTable.setClazzAndColumns(AssessmentsDigitalObjects.PROPERTYNAME_DIGITALOBJECT_IDENTIFIER,
				AssessmentsDigitalObjects.class,
				AssessmentsDigitalObjects.PROPERTYNAME_DIGITALOBJECT_IDENTIFIER,
				AssessmentsDigitalObjects.PROPERTYNAME_DIGITALOBJECT_LABEL);
    }

    // Method to return the initialy focused component
    public Component getInitialFocusComponent() {
        return panel1;
    }

    // Method to set the model
    public void setModel(DomainObject record, InfiniteProgressPanel monitor) {
        super.setModel(record, monitor); // call the super method

        // set the assessment number label
        Assessments assessment = (Assessments)record;
        if(assessment.getIdentifier() != null) {
            assessmentNumber.setText(assessment.getIdentifier().toString());
        }

        // update the linking tables
        accessionsTable.updateCollection(assessment.getAccessions());
        resourcesTable.updateCollection(assessment.getResources());
        digitalObjectsTable.updateCollection(assessment.getDigitalObjects());
    }

    /**
     * Method to calculate the estimated processing time
     */
    private void calculateTotalEPT() {
        try {
            double total = Double.parseDouble(textField13.getText())*Double.parseDouble(textField14.getText());
            textField15.setText(total + "");
        } catch(NumberFormatException nfe) {
            textField15.setText("");
        }
    }

    /**
     * Method to calculate the research value rating based on adding interest
     * rating and quality of housing together
     */
    private void setResearchValueRating() {
        try {
            int sum = Integer.parseInt(textField8.getText()) + Integer.parseInt(textField9.getText());
            textField10.setText(sum + "");
        } catch(NumberFormatException nfe) {
            textField10.setText("");
        }
    }

    /**
     * Method to link an accession record to this assessment record
     */
    private void linkAccessionActionPerformed() {
        AccessionLookup accessionPicker = new AccessionLookup(getParentEditor(), this);
		accessionPicker.showDialog(this);
    }

    /**
     * Method to link a resorce record to the assessment record
     */
    private void linkResourceActionPerformed() {
        ResourceLookup resourcePicker = new ResourceLookup(getParentEditor(), this);
		resourcePicker.showDialog(this);    
    }

    /**
     * Method to link a digital object record to the assessment record
     */
    private void linkDigitalObjectActionPerformed() {
        DigitalObjectLookup DigitalObjectPicker = new DigitalObjectLookup(getParentEditor(), this);
		DigitalObjectPicker.showDialog(this);    
    }

    /**
     * Method to remove a linked accession record
     */
    private void removeAccessionActionPerformed() {
        try {
			removeRelatedTableRow(accessionsTable, null, super.getModel());
		} catch (ObjectNotRemovedException e1) {
			new ErrorDialog("Accession link not removed", e1).showDialog();
		}
    }

    /**
     * Method to remove a linked resource record
     */
    private void removeResourceActionPerformed() {
        try {
			removeRelatedTableRow(resourcesTable, null, super.getModel());
		} catch (ObjectNotRemovedException e1) {
			new ErrorDialog("Resource link not removed", e1).showDialog();
		}
    }

    /**
     * Method to remove a linked digital object record
     */
    private void removeDigitalObjectActionPerformed() {
        try {
			removeRelatedTableRow(digitalObjectsTable, null, super.getModel());
		} catch (ObjectNotRemovedException e1) {
			new ErrorDialog("Digital Object link not removed", e1).showDialog();
		}
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        tabbedPane1 = new JTabbedPane();
        panel1 = new JPanel();
        panel5 = new JPanel();
        label2 = new JLabel();
        scrollPane1 = new JScrollPane();
        accessionsTable = new DomainSortableTable();
        panel6 = new JPanel();
        button1 = new JButton();
        button2 = new JButton();
        panel13 = new JPanel();
        label6 = new JLabel();
        textField1 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Assessments.PROPERTYNAME_WHO_DID_SURVEY));
        label35 = new JLabel();
        textField17 = ATBasicComponentFactory.createDateField(detailsModel.getModel(Assessments.PROPERTYNAME_DATE_OF_SURVEY));
        label34 = new JLabel();
        textField18 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Assessments.PROPERTYNAME_USER_WHO_CREATED_RECORD));
        label7 = new JLabel();
        textField2 = ATBasicComponentFactory.createDoubleField(detailsModel, Assessments.PROPERTYNAME_AMOUNT_OF_TIME_SURVEY_TOOK);
        label8 = new JLabel();
        checkBox2 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_REVIEW_NEEDED, Assessments.class);
        label9 = new JLabel();
        textField3 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Assessments.PROPERTYNAME_WHO_NEEDS_TO_REVIEW));
        panel7 = new JPanel();
        label3 = new JLabel();
        scrollPane2 = new JScrollPane();
        resourcesTable = new DomainSortableTable();
        panel8 = new JPanel();
        button3 = new JButton();
        button4 = new JButton();
        panel9 = new JPanel();
        label4 = new JLabel();
        scrollPane3 = new JScrollPane();
        digitalObjectsTable = new DomainSortableTable();
        panel10 = new JPanel();
        button5 = new JButton();
        button6 = new JButton();
        panel11 = new JPanel();
        label5 = new JLabel();
        scrollPane4 = new JScrollPane();
        reviewNoteTextArea = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Assessments.PROPERTYNAME_REVIEW_NOTE));
        panel12 = new JPanel();
        checkBox1 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_INACTIVE, Assessments.class);
        panel2 = new JPanel();
        panel14 = new JPanel();
        label10 = new JLabel();
        textField4 = ATBasicComponentFactory.createIntegerField(detailsModel, Assessments.PROPERTYNAME_CONDITION_OF_MATERIAL_RATING, 1, 5);
        label12 = new JLabel();
        textField6 = ATBasicComponentFactory.createIntegerField(detailsModel, Assessments.PROPERTYNAME_QUALITY_OF_HOUSING_RATING, 1, 5);
        label11 = new JLabel();
        textField5 = ATBasicComponentFactory.createIntegerField(detailsModel, Assessments.PROPERTYNAME_PHYSICAL_ACCESS_RATING, 1, 5);
        label13 = new JLabel();
        textField7 = ATBasicComponentFactory.createIntegerField(detailsModel, Assessments.PROPERTYNAME_INTELLECTUAL_ACCESS_RATING, 1, 5);
        label15 = new JLabel();
        textField9 = ATBasicComponentFactory.createIntegerField(detailsModel, Assessments.PROPERTYNAME_DOCUMENTATION_QUALITY_RATING, 1, 5);
        label14 = new JLabel();
        textField8 = ATBasicComponentFactory.createIntegerField(detailsModel, Assessments.PROPERTYNAME_INTEREST_RATING, 1, 5);
        label16 = new JLabel();
        textField10 = ATBasicComponentFactory.createIntegerField(detailsModel, Assessments.PROPERTYNAME_RESEARCH_VALUE_RATING);
        label17 = new JLabel();
        textField11 = ATBasicComponentFactory.createIntegerField(detailsModel, Assessments.PROPERTYNAME_USER_NUMERICAL_RATING1, 1, 5);
        label18 = new JLabel();
        textField12 = ATBasicComponentFactory.createIntegerField(detailsModel, Assessments.PROPERTYNAME_USER_NUMERICAL_RATING2, 1, 5);
        panel17 = new JPanel();
        label25 = new JLabel();
        checkBox3 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_CONSERVATION_ISSUE1, Assessments.class);
        checkBox6 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_CONSERVATION_ISSUE4, Assessments.class);
        checkBox4 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_CONSERVATION_ISSUE2, Assessments.class);
        checkBox7 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_CONSERVATION_ISSUE5, Assessments.class);
        checkBox5 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_CONSERVATION_ISSUE3, Assessments.class);
        checkBox8 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_CONSERVATION_ISSUE6, Assessments.class);
        panel15 = new JPanel();
        label19 = new JLabel();
        scrollPane5 = new JScrollPane();
        textArea1 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Assessments.PROPERTYNAME_GENERAL_NOTE));
        panel16 = new JPanel();
        label20 = new JLabel();
        label21 = new JLabel();
        textField13 = ATBasicComponentFactory.createDoubleField(detailsModel, Assessments.PROPERTYNAME_ESTIMATED_PROCESSING_TIME_PER_FOOT);
        label23 = new JLabel();
        textField14 = ATBasicComponentFactory.createDoubleField(detailsModel, Assessments.PROPERTYNAME_TOTAL_EXTENT);
        label22 = new JLabel();
        textField15 = ATBasicComponentFactory.createDoubleField(detailsModel, Assessments.PROPERTYNAME_TOTAL_ESTIMATED_PROCESSING_TIME);
        label24 = new JLabel();
        panel18 = new JPanel();
        panel19 = new JPanel();
        label26 = new JLabel();
        checkBox9 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_OTHER_CONSERVATION_ISSUE1, Assessments.class);
        checkBox13 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_OTHER_CONSERVATION_ISSUE5, Assessments.class);
        checkBox10 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_OTHER_CONSERVATION_ISSUE2, Assessments.class);
        checkBox14 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_OTHER_CONSERVATION_ISSUE6, Assessments.class);
        checkBox11 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_OTHER_CONSERVATION_ISSUE3, Assessments.class);
        checkBox15 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_OTHER_CONSERVATION_ISSUE7, Assessments.class);
        checkBox12 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_OTHER_CONSERVATION_ISSUE4, Assessments.class);
        checkBox16 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_OTHER_CONSERVATION_ISSUE8, Assessments.class);
        label27 = new JLabel();
        scrollPane6 = new JScrollPane();
        textArea2 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Assessments.PROPERTYNAME_CONSERVATION_NOTE));
        panel3 = new JPanel();
        panel20 = new JPanel();
        label28 = new JLabel();
        label29 = new JLabel();
        checkBox17 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_FORMAT1, Assessments.class);
        checkBox25 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_FORMAT9, Assessments.class);
        checkBox18 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_FORMAT2, Assessments.class);
        checkBox26 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_FORMAT10, Assessments.class);
        checkBox19 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_FORMAT3, Assessments.class);
        checkBox27 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_FORMAT11, Assessments.class);
        checkBox20 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_FORMAT4, Assessments.class);
        checkBox28 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_FORMAT12, Assessments.class);
        checkBox21 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_FORMAT5, Assessments.class);
        checkBox29 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_FORMAT13, Assessments.class);
        checkBox22 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_FORMAT6, Assessments.class);
        checkBox30 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_FORMAT14, Assessments.class);
        checkBox23 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_FORMAT7, Assessments.class);
        checkBox31 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_FORMAT15, Assessments.class);
        checkBox24 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_FORMAT8, Assessments.class);
        checkBox32 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_FORMAT16, Assessments.class);
        checkBox33 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_FORMAT17, Assessments.class);
        checkBox34 = ATBasicComponentFactory.createCheckBox(detailsModel, Assessments.PROPERTYNAME_SPECIAL_FORMAT18, Assessments.class);
        panel22 = new JPanel();
        label31 = new JLabel();
        scrollPane8 = new JScrollPane();
        textArea4 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Assessments.PROPERTYNAME_EXHIBITION_VALUE_NOTE));
        panel23 = new JPanel();
        label32 = new JLabel();
        textField16 = ATBasicComponentFactory.createCurrencyField(detailsModel, Assessments.PROPERTYNAME_MONETARY_VALUE, true);
        panel21 = new JPanel();
        label30 = new JLabel();
        scrollPane7 = new JScrollPane();
        textArea3 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Assessments.PROPERTYNAME_SPECIAL_FORMAT_NOTE));
        panel25 = new JPanel();
        label33 = new JLabel();
        scrollPane9 = new JScrollPane();
        textArea5 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Assessments.PROPERTYNAME_MONETARY_VALUE_NOTE));
        panel4 = new JPanel();
        label1 = new JLabel();
        assessmentNumber = new JLabel();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBackground(new Color(200, 205, 232));
        setLayout(new BorderLayout());

        //======== tabbedPane1 ========
        {
            tabbedPane1.setBackground(new Color(200, 205, 232));
            tabbedPane1.setBorder(new EmptyBorder(0, 10, 0, 10));

            //======== panel1 ========
            {
                panel1.setBackground(new Color(200, 205, 232));
                panel1.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC
                    },
                    new RowSpec[] {
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC
                    }));

                //======== panel5 ========
                {
                    panel5.setBackground(new Color(200, 205, 232));
                    panel5.setLayout(new BorderLayout());

                    //---- label2 ----
                    label2.setText("Accessions Linked to this assessment record");
                    panel5.add(label2, BorderLayout.NORTH);

                    //======== scrollPane1 ========
                    {

                        //---- accessionsTable ----
                        accessionsTable.setPreferredScrollableViewportSize(new Dimension(450, 100));
                        scrollPane1.setViewportView(accessionsTable);
                    }
                    panel5.add(scrollPane1, BorderLayout.CENTER);

                    //======== panel6 ========
                    {
                        panel6.setBackground(new Color(200, 205, 232));
                        panel6.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                            },
                            RowSpec.decodeSpecs("default")));

                        //---- button1 ----
                        button1.setText("Link Accession");
                        button1.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                linkAccessionActionPerformed();
                            }
                        });
                        panel6.add(button1, cc.xy(3, 1));

                        //---- button2 ----
                        button2.setText("Remove Link");
                        button2.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                removeAccessionActionPerformed();
                            }
                        });
                        panel6.add(button2, cc.xy(5, 1));
                    }
                    panel5.add(panel6, BorderLayout.SOUTH);
                }
                panel1.add(panel5, cc.xy(1, 1));

                //======== panel13 ========
                {
                    panel13.setBackground(new Color(200, 205, 232));
                    panel13.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            new ColumnSpec(Sizes.dluX(47)),
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC
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
                            FormFactory.DEFAULT_ROWSPEC
                        }));

                    //---- label6 ----
                    label6.setText("Surveyed by");
                    ATFieldInfo.assignLabelInfo(label6, Assessments.class, Assessments.PROPERTYNAME_WHO_DID_SURVEY);
                    panel13.add(label6, cc.xy(1, 1));
                    panel13.add(textField1, cc.xywh(3, 1, 7, 1));

                    //---- label35 ----
                    label35.setText("Date of survey");
                    ATFieldInfo.assignLabelInfo(label35, Assessments.class, Assessments.PROPERTYNAME_DATE_OF_SURVEY);
                    panel13.add(label35, cc.xy(1, 3));
                    panel13.add(textField17, cc.xywh(3, 3, 7, 1));

                    //---- label34 ----
                    label34.setText("User who created record");
                    ATFieldInfo.assignLabelInfo(label34, Assessments.class, Assessments.PROPERTYNAME_USER_WHO_CREATED_RECORD);
                    panel13.add(label34, cc.xy(1, 5));
                    panel13.add(textField18, cc.xywh(3, 5, 7, 1));

                    //---- label7 ----
                    label7.setText("Time it took to complete survey");
                    ATFieldInfo.assignLabelInfo(label7, Assessments.class, Assessments.PROPERTYNAME_AMOUNT_OF_TIME_SURVEY_TOOK);
                    panel13.add(label7, cc.xywh(1, 7, 5, 1));

                    //---- textField2 ----
                    textField2.setColumns(4);
                    panel13.add(textField2, cc.xy(7, 7));

                    //---- label8 ----
                    label8.setText("hours");
                    panel13.add(label8, cc.xy(9, 7));

                    //---- checkBox2 ----
                    checkBox2.setText("Review needed");
                    checkBox2.setBackground(new Color(200, 205, 232));
                    checkBox2.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_REVIEW_NEEDED));
                    panel13.add(checkBox2, cc.xywh(1, 9, 5, 1));

                    //---- label9 ----
                    label9.setText("Who needs to review");
                    ATFieldInfo.assignLabelInfo(label9, Assessments.class, Assessments.PROPERTYNAME_WHO_NEEDS_TO_REVIEW);
                    panel13.add(label9, cc.xy(1, 11));
                    panel13.add(textField3, cc.xywh(3, 11, 7, 1));
                }
                panel1.add(panel13, cc.xy(5, 1));

                //======== panel7 ========
                {
                    panel7.setBackground(new Color(200, 205, 232));
                    panel7.setLayout(new BorderLayout());

                    //---- label3 ----
                    label3.setText("Resources Linked to this assessment record");
                    panel7.add(label3, BorderLayout.NORTH);

                    //======== scrollPane2 ========
                    {

                        //---- resourcesTable ----
                        resourcesTable.setPreferredScrollableViewportSize(new Dimension(450, 100));
                        scrollPane2.setViewportView(resourcesTable);
                    }
                    panel7.add(scrollPane2, BorderLayout.CENTER);

                    //======== panel8 ========
                    {
                        panel8.setBackground(new Color(200, 205, 232));
                        panel8.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                            },
                            RowSpec.decodeSpecs("default")));

                        //---- button3 ----
                        button3.setText("Link Resource");
                        button3.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                linkResourceActionPerformed();
                            }
                        });
                        panel8.add(button3, cc.xy(3, 1));

                        //---- button4 ----
                        button4.setText("Remove Link");
                        button4.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                removeResourceActionPerformed();
                            }
                        });
                        panel8.add(button4, cc.xy(5, 1));
                    }
                    panel7.add(panel8, BorderLayout.SOUTH);
                }
                panel1.add(panel7, cc.xy(1, 3));

                //======== panel9 ========
                {
                    panel9.setBackground(new Color(200, 205, 232));
                    panel9.setLayout(new BorderLayout());

                    //---- label4 ----
                    label4.setText("Digital Objects Linked to this assessment record");
                    panel9.add(label4, BorderLayout.NORTH);

                    //======== scrollPane3 ========
                    {

                        //---- digitalObjectsTable ----
                        digitalObjectsTable.setPreferredScrollableViewportSize(new Dimension(450, 100));
                        scrollPane3.setViewportView(digitalObjectsTable);
                    }
                    panel9.add(scrollPane3, BorderLayout.CENTER);

                    //======== panel10 ========
                    {
                        panel10.setBackground(new Color(200, 205, 232));
                        panel10.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                            },
                            RowSpec.decodeSpecs("default")));

                        //---- button5 ----
                        button5.setText("Link Digital Object");
                        button5.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                linkDigitalObjectActionPerformed();
                            }
                        });
                        panel10.add(button5, cc.xy(3, 1));

                        //---- button6 ----
                        button6.setText("Remove Link");
                        button6.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                removeDigitalObjectActionPerformed();
                            }
                        });
                        panel10.add(button6, cc.xy(5, 1));
                    }
                    panel9.add(panel10, BorderLayout.SOUTH);
                }
                panel1.add(panel9, cc.xy(1, 5));

                //======== panel11 ========
                {
                    panel11.setBackground(new Color(200, 205, 232));
                    panel11.setLayout(new BorderLayout());

                    //---- label5 ----
                    label5.setText("Review note");
                    ATFieldInfo.assignLabelInfo(label5, Assessments.class, Assessments.PROPERTYNAME_REVIEW_NOTE);
                    panel11.add(label5, BorderLayout.NORTH);

                    //======== scrollPane4 ========
                    {

                        //---- reviewNoteTextArea ----
                        reviewNoteTextArea.setRows(10);
                        reviewNoteTextArea.setColumns(25);
                        reviewNoteTextArea.setLineWrap(true);
                        scrollPane4.setViewportView(reviewNoteTextArea);
                    }
                    panel11.add(scrollPane4, BorderLayout.CENTER);

                    //======== panel12 ========
                    {
                        panel12.setBackground(new Color(200, 205, 232));
                        panel12.setLayout(new FlowLayout(FlowLayout.LEFT));

                        //---- checkBox1 ----
                        checkBox1.setText("inactive");
                        checkBox1.setBackground(new Color(200, 205, 232));
                        checkBox1.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_INACTIVE));
                        panel12.add(checkBox1);
                    }
                    panel11.add(panel12, BorderLayout.SOUTH);
                }
                panel1.add(panel11, cc.xywh(5, 3, 1, 3));
            }
            tabbedPane1.addTab("Basic Information", panel1);


            //======== panel2 ========
            {
                panel2.setBackground(new Color(200, 205, 232));
                panel2.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                    },
                    new RowSpec[] {
                        new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                    }));

                //======== panel14 ========
                {
                    panel14.setBackground(new Color(200, 205, 232));
                    panel14.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC
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
                            FormFactory.DEFAULT_ROWSPEC
                        }));

                    //---- label10 ----
                    label10.setText("Condition of material");
                    ATFieldInfo.assignLabelInfo(label10, Assessments.class, Assessments.PROPERTYNAME_CONDITION_OF_MATERIAL_RATING);
                    panel14.add(label10, cc.xy(1, 1));

                    //---- textField4 ----
                    textField4.setColumns(2);
                    panel14.add(textField4, cc.xy(3, 1));

                    //---- label12 ----
                    label12.setText("Quality of housing");
                    ATFieldInfo.assignLabelInfo(label12, Assessments.class, Assessments.PROPERTYNAME_QUALITY_OF_HOUSING_RATING);
                    panel14.add(label12, cc.xy(5, 1));

                    //---- textField6 ----
                    textField6.setColumns(2);
                    panel14.add(textField6, cc.xy(7, 1));

                    //---- label11 ----
                    label11.setText("Physical access");
                    ATFieldInfo.assignLabelInfo(label11, Assessments.class, Assessments.PROPERTYNAME_PHYSICAL_ACCESS_RATING);
                    panel14.add(label11, cc.xy(1, 3));

                    //---- textField5 ----
                    textField5.setColumns(2);
                    panel14.add(textField5, cc.xy(3, 3));

                    //---- label13 ----
                    label13.setText("Intellectual Access");
                    ATFieldInfo.assignLabelInfo(label13, Assessments.class, Assessments.PROPERTYNAME_INTELLECTUAL_ACCESS_RATING);
                    panel14.add(label13, cc.xy(5, 3));

                    //---- textField7 ----
                    textField7.setColumns(2);
                    panel14.add(textField7, cc.xy(7, 3));

                    //---- label15 ----
                    label15.setText("Document Quality");
                    ATFieldInfo.assignLabelInfo(label15, Assessments.class, Assessments.PROPERTYNAME_DOCUMENTATION_QUALITY_RATING);
                    panel14.add(label15, cc.xy(1, 5));

                    //---- textField9 ----
                    textField9.setColumns(2);
                    textField9.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            setResearchValueRating();
                        }
                    });
                    panel14.add(textField9, cc.xy(3, 5));

                    //---- label14 ----
                    label14.setText("Interest");
                    ATFieldInfo.assignLabelInfo(label14, Assessments.class, Assessments.PROPERTYNAME_INTEREST_RATING);
                    panel14.add(label14, cc.xy(5, 5));

                    //---- textField8 ----
                    textField8.setColumns(2);
                    textField8.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            setResearchValueRating();
                        }
                    });
                    panel14.add(textField8, cc.xy(7, 5));

                    //---- label16 ----
                    label16.setText("Research Value");
                    ATFieldInfo.assignLabelInfo(label16, Assessments.class, Assessments.PROPERTYNAME_RESEARCH_VALUE_RATING);
                    panel14.add(label16, cc.xy(5, 7));

                    //---- textField10 ----
                    textField10.setColumns(2);
                    textField10.setEditable(false);
                    panel14.add(textField10, cc.xy(7, 7));

                    //---- label17 ----
                    label17.setText("User Rating 1");
                    ATFieldInfo.assignLabelInfo(label17, Assessments.class, Assessments.PROPERTYNAME_USER_NUMERICAL_RATING1);
                    panel14.add(label17, cc.xy(1, 9));

                    //---- textField11 ----
                    textField11.setColumns(2);
                    panel14.add(textField11, cc.xy(3, 9));

                    //---- label18 ----
                    label18.setText("User Rating 2");
                    ATFieldInfo.assignLabelInfo(label18, Assessments.class, Assessments.PROPERTYNAME_USER_NUMERICAL_RATING2);
                    panel14.add(label18, cc.xy(5, 9));

                    //---- textField12 ----
                    textField12.setColumns(2);
                    panel14.add(textField12, cc.xy(7, 9));
                }
                panel2.add(panel14, cc.xy(1, 1));

                //======== panel17 ========
                {
                    panel17.setBackground(new Color(200, 205, 232));
                    panel17.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC
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
                            FormFactory.DEFAULT_ROWSPEC
                        }));

                    //---- label25 ----
                    label25.setText("Special Conservation issues");
                    ATFieldInfo.assignLabelTooltip(label25, Assessments.class, Assessments.PROPERTYNAME_SPECIAL_CONSERVATION_ISSUE1);
                    panel17.add(label25, cc.xywh(1, 1, 3, 1));

                    //---- checkBox3 ----
                    checkBox3.setText("mold damage");
                    checkBox3.setBackground(new Color(200, 205, 232));
                    checkBox3.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_CONSERVATION_ISSUE1));
                    panel17.add(checkBox3, cc.xy(3, 3));

                    //---- checkBox6 ----
                    checkBox6.setText("Special conservation issue1");
                    checkBox6.setBackground(new Color(200, 205, 232));
                    checkBox6.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_CONSERVATION_ISSUE4));
                    panel17.add(checkBox6, cc.xy(5, 3));

                    //---- checkBox4 ----
                    checkBox4.setText("pest damage");
                    checkBox4.setBackground(new Color(200, 205, 232));
                    checkBox4.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_CONSERVATION_ISSUE2));
                    panel17.add(checkBox4, cc.xy(3, 5));

                    //---- checkBox7 ----
                    checkBox7.setText("Special conservation issue2");
                    checkBox7.setBackground(new Color(200, 205, 232));
                    checkBox7.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_CONSERVATION_ISSUE5));
                    panel17.add(checkBox7, cc.xy(5, 5));

                    //---- checkBox5 ----
                    checkBox5.setText("deteriorating film base");
                    checkBox5.setBackground(new Color(200, 205, 232));
                    checkBox5.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_CONSERVATION_ISSUE3));
                    panel17.add(checkBox5, cc.xy(3, 7));

                    //---- checkBox8 ----
                    checkBox8.setText("Special conservation issue3");
                    checkBox8.setBackground(new Color(200, 205, 232));
                    checkBox8.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_CONSERVATION_ISSUE6));
                    panel17.add(checkBox8, cc.xy(5, 7));
                }
                panel2.add(panel17, cc.xy(5, 1));

                //======== panel15 ========
                {
                    panel15.setBackground(new Color(200, 205, 232));
                    panel15.setLayout(new BorderLayout());

                    //---- label19 ----
                    label19.setText("General note");
                    ATFieldInfo.assignLabelTooltip(label19, Assessments.class, Assessments.PROPERTYNAME_GENERAL_NOTE);
                    panel15.add(label19, BorderLayout.NORTH);

                    //======== scrollPane5 ========
                    {

                        //---- textArea1 ----
                        textArea1.setRows(8);
                        textArea1.setColumns(25);
                        textArea1.setLineWrap(true);
                        scrollPane5.setViewportView(textArea1);
                    }
                    panel15.add(scrollPane5, BorderLayout.CENTER);
                }
                panel2.add(panel15, cc.xy(1, 3));

                //======== panel16 ========
                {
                    panel16.setBackground(new Color(200, 205, 232));
                    panel16.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            new ColumnSpec(Sizes.dluX(82)),
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            new ColumnSpec(Sizes.dluX(23))
                        },
                        new RowSpec[] {
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW),
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC
                        }));

                    //---- label20 ----
                    label20.setText("Estimated processing time (EPT)");
                    panel16.add(label20, cc.xywh(1, 5, 9, 1));

                    //---- label21 ----
                    label21.setText("Hours per foot");
                    ATFieldInfo.assignLabelTooltip(label21, Assessments.class, Assessments.PROPERTYNAME_ESTIMATED_PROCESSING_TIME_PER_FOOT);
                    panel16.add(label21, cc.xy(3, 7));

                    //---- textField13 ----
                    textField13.setColumns(5);
                    textField13.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            calculateTotalEPT();
                        }
                    });
                    panel16.add(textField13, cc.xy(5, 7));

                    //---- label23 ----
                    label23.setText("Multiplied by total extent");
                    ATFieldInfo.assignLabelTooltip(label23, Assessments.class, Assessments.PROPERTYNAME_TOTAL_EXTENT);
                    panel16.add(label23, cc.xy(7, 7));

                    //---- textField14 ----
                    textField14.setColumns(5);
                    textField14.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            calculateTotalEPT();
                        }
                    });
                    panel16.add(textField14, cc.xy(9, 7));

                    //---- label22 ----
                    label22.setText("Equals Total EPT");
                    ATFieldInfo.assignLabelTooltip(label22, Assessments.class, Assessments.PROPERTYNAME_TOTAL_ESTIMATED_PROCESSING_TIME);
                    panel16.add(label22, cc.xy(3, 9));

                    //---- textField15 ----
                    textField15.setColumns(5);
                    textField15.setEditable(false);
                    panel16.add(textField15, cc.xy(5, 9));

                    //---- label24 ----
                    label24.setText("hours");
                    panel16.add(label24, cc.xy(7, 9));
                }
                panel2.add(panel16, cc.xy(1, 5));

                //======== panel18 ========
                {
                    panel18.setBackground(new Color(200, 205, 232));
                    panel18.setLayout(new BorderLayout());

                    //======== panel19 ========
                    {
                        panel19.setBackground(new Color(200, 205, 232));
                        panel19.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC
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
                                FormFactory.DEFAULT_ROWSPEC
                            }));

                        //---- label26 ----
                        label26.setText("Other Conservation issues");
                        ATFieldInfo.assignLabelTooltip(label26, Assessments.class, Assessments.PROPERTYNAME_OTHER_CONSERVATION_ISSUE1);
                        panel19.add(label26, cc.xywh(1, 1, 5, 1));

                        //---- checkBox9 ----
                        checkBox9.setText("Brittle paper");
                        checkBox9.setBackground(new Color(200, 205, 232));
                        checkBox9.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_OTHER_CONSERVATION_ISSUE1));
                        panel19.add(checkBox9, cc.xy(3, 3));

                        //---- checkBox13 ----
                        checkBox13.setText("Thermofax paper");
                        checkBox13.setBackground(new Color(200, 205, 232));
                        checkBox13.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_OTHER_CONSERVATION_ISSUE5));
                        panel19.add(checkBox13, cc.xy(5, 3));

                        //---- checkBox10 ----
                        checkBox10.setText("Metal fasteners");
                        checkBox10.setBackground(new Color(200, 205, 232));
                        checkBox10.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_OTHER_CONSERVATION_ISSUE2));
                        panel19.add(checkBox10, cc.xy(3, 5));

                        //---- checkBox14 ----
                        checkBox14.setText("Other conservation issue1");
                        checkBox14.setBackground(new Color(200, 205, 232));
                        checkBox14.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_OTHER_CONSERVATION_ISSUE6));
                        panel19.add(checkBox14, cc.xy(5, 5));

                        //---- checkBox11 ----
                        checkBox11.setText("Newspaper");
                        checkBox11.setBackground(new Color(200, 205, 232));
                        checkBox11.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_OTHER_CONSERVATION_ISSUE3));
                        panel19.add(checkBox11, cc.xy(3, 7));

                        //---- checkBox15 ----
                        checkBox15.setText("Other conservation issue2");
                        checkBox15.setBackground(new Color(200, 205, 232));
                        checkBox15.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_OTHER_CONSERVATION_ISSUE7));
                        panel19.add(checkBox15, cc.xy(5, 7));

                        //---- checkBox12 ----
                        checkBox12.setText("Tape");
                        checkBox12.setBackground(new Color(200, 205, 232));
                        checkBox12.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_OTHER_CONSERVATION_ISSUE4));
                        panel19.add(checkBox12, cc.xy(3, 9));

                        //---- checkBox16 ----
                        checkBox16.setText("Other conservation issue3");
                        checkBox16.setBackground(new Color(200, 205, 232));
                        checkBox16.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_OTHER_CONSERVATION_ISSUE8));
                        panel19.add(checkBox16, cc.xy(5, 9));

                        //---- label27 ----
                        label27.setText("Conservation note");
                        ATFieldInfo.assignLabelTooltip(label27, Assessments.class, Assessments.PROPERTYNAME_CONSERVATION_NOTE);
                        panel19.add(label27, cc.xywh(1, 11, 5, 1));
                    }
                    panel18.add(panel19, BorderLayout.NORTH);

                    //======== scrollPane6 ========
                    {

                        //---- textArea2 ----
                        textArea2.setColumns(25);
                        textArea2.setRows(8);
                        textArea2.setLineWrap(true);
                        scrollPane6.setViewportView(textArea2);
                    }
                    panel18.add(scrollPane6, BorderLayout.CENTER);
                }
                panel2.add(panel18, cc.xywh(5, 3, 1, 3));
            }
            tabbedPane1.addTab("Survey", panel2);


            //======== panel3 ========
            {
                panel3.setBackground(new Color(200, 205, 232));
                panel3.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                    },
                    new RowSpec[] {
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        new RowSpec(RowSpec.BOTTOM, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC
                    }));

                //======== panel20 ========
                {
                    panel20.setBackground(new Color(200, 205, 232));
                    panel20.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            new ColumnSpec(Sizes.dluX(88))
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
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC
                        }));

                    //---- label28 ----
                    label28.setText("Special Formats");
                    ATFieldInfo.assignLabelTooltip(label28, Assessments.class, Assessments.PROPERTYNAME_SPECIAL_FORMAT1);
                    panel20.add(label28, cc.xywh(1, 1, 5, 1));

                    //---- label29 ----
                    label29.setText("Check all that apply");
                    panel20.add(label29, cc.xywh(3, 3, 3, 1));

                    //---- checkBox17 ----
                    checkBox17.setText("Architectural materials");
                    checkBox17.setBackground(new Color(200, 205, 232));
                    checkBox17.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_FORMAT1));
                    panel20.add(checkBox17, cc.xy(3, 5));

                    //---- checkBox25 ----
                    checkBox25.setText("Glass");
                    checkBox25.setBackground(new Color(200, 205, 232));
                    checkBox25.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_FORMAT9));
                    panel20.add(checkBox25, cc.xy(5, 5));

                    //---- checkBox18 ----
                    checkBox18.setText("Art originals");
                    checkBox18.setBackground(new Color(200, 205, 232));
                    checkBox18.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_FORMAT2));
                    panel20.add(checkBox18, cc.xy(3, 7));

                    //---- checkBox26 ----
                    checkBox26.setText("Photographs");
                    checkBox26.setBackground(new Color(200, 205, 232));
                    checkBox26.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_FORMAT10));
                    panel20.add(checkBox26, cc.xy(5, 7));

                    //---- checkBox19 ----
                    checkBox19.setText("Artifacts");
                    checkBox19.setBackground(new Color(200, 205, 232));
                    checkBox19.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_FORMAT3));
                    panel20.add(checkBox19, cc.xy(3, 9));

                    //---- checkBox27 ----
                    checkBox27.setText("Scrapbooks");
                    checkBox27.setBackground(new Color(200, 205, 232));
                    checkBox27.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_FORMAT11));
                    panel20.add(checkBox27, cc.xy(5, 9));

                    //---- checkBox20 ----
                    checkBox20.setText("Audio materials");
                    checkBox20.setBackground(new Color(200, 205, 232));
                    checkBox20.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_FORMAT4));
                    panel20.add(checkBox20, cc.xy(3, 11));

                    //---- checkBox28 ----
                    checkBox28.setText("<html>Technical Drawings<br>& Schematics");
                    checkBox28.setBackground(new Color(200, 205, 232));
                    //checkBox28.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_FORMAT12));
                    panel20.add(checkBox28, cc.xy(5, 11));

                    //---- checkBox21 ----
                    checkBox21.setText("Biological specimens");
                    checkBox21.setBackground(new Color(200, 205, 232));
                    checkBox21.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_FORMAT5));
                    panel20.add(checkBox21, cc.xy(3, 13));

                    //---- checkBox29 ----
                    checkBox29.setText("Textiles");
                    checkBox29.setBackground(new Color(200, 205, 232));
                    checkBox29.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_FORMAT13));
                    panel20.add(checkBox29, cc.xy(5, 13));

                    //---- checkBox22 ----
                    checkBox22.setText("Botanical specimens");
                    checkBox22.setBackground(new Color(200, 205, 232));
                    checkBox22.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_FORMAT6));
                    panel20.add(checkBox22, cc.xy(3, 15));

                    //---- checkBox30 ----
                    checkBox30.setText("Vellum & Parchment");
                    checkBox30.setBackground(new Color(200, 205, 232));
                    //checkBox30.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_FORMAT14));
                    panel20.add(checkBox30, cc.xy(5, 15));

                    //---- checkBox23 ----
                    checkBox23.setText("Computer storage units");
                    checkBox23.setBackground(new Color(200, 205, 232));
                    checkBox23.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_FORMAT7));
                    panel20.add(checkBox23, cc.xy(3, 17));

                    //---- checkBox31 ----
                    checkBox31.setText("Video Materials");
                    checkBox31.setBackground(new Color(200, 205, 232));
                    checkBox31.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_FORMAT15));
                    panel20.add(checkBox31, cc.xy(5, 17));

                    //---- checkBox24 ----
                    checkBox24.setText("<html>Film (negative, slide, <br>or motion picture)");
                    checkBox24.setBackground(new Color(200, 205, 232));
                    //checkBox24.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_FORMAT8));
                    panel20.add(checkBox24, cc.xy(3, 19));

                    //---- checkBox32 ----
                    checkBox32.setText("Other");
                    checkBox32.setBackground(new Color(200, 205, 232));
                    checkBox32.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_FORMAT16));
                    panel20.add(checkBox32, cc.xy(5, 19));

                    //---- checkBox33 ----
                    checkBox33.setText("Special Format1");
                    checkBox33.setBackground(new Color(200, 205, 232));
                    checkBox33.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_FORMAT17));
                    panel20.add(checkBox33, cc.xy(3, 21));

                    //---- checkBox34 ----
                    checkBox34.setText("Special Format2");
                    checkBox34.setBackground(new Color(200, 205, 232));
                    checkBox34.setText(ATFieldInfo.getLabel(Assessments.class, Assessments.PROPERTYNAME_SPECIAL_FORMAT18));
                    panel20.add(checkBox34, cc.xy(5, 21));
                }
                panel3.add(panel20, cc.xywh(1, 1, 1, 3));

                //======== panel22 ========
                {
                    panel22.setBackground(new Color(200, 205, 232));
                    panel22.setLayout(new FormLayout(
                        "default:grow",
                        "fill:default, default, default:grow, bottom:default:grow"));

                    //---- label31 ----
                    label31.setText("Exhibition Value note");
                    ATFieldInfo.assignLabelInfo(label31, Assessments.class, Assessments.PROPERTYNAME_EXHIBITION_VALUE_NOTE);
                    panel22.add(label31, cc.xy(1, 1));

                    //======== scrollPane8 ========
                    {

                        //---- textArea4 ----
                        textArea4.setColumns(25);
                        textArea4.setRows(14);
                        textArea4.setLineWrap(true);
                        scrollPane8.setViewportView(textArea4);
                    }
                    panel22.add(scrollPane8, cc.xy(1, 2));

                    //======== panel23 ========
                    {
                        panel23.setBackground(new Color(200, 205, 232));
                        panel23.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC
                            },
                            RowSpec.decodeSpecs("fill:default")));

                        //---- label32 ----
                        label32.setText("Monetary Value");
                        ATFieldInfo.assignLabelInfo(label32, Assessments.class, Assessments.PROPERTYNAME_MONETARY_VALUE);
                        panel23.add(label32, cc.xy(1, 1));

                        //---- textField16 ----
                        textField16.setColumns(10);
                        panel23.add(textField16, cc.xy(3, 1));
                    }
                    panel22.add(panel23, cc.xy(1, 4));
                }
                panel3.add(panel22, cc.xywh(5, 1, 1, 3));

                //======== panel21 ========
                {
                    panel21.setBackground(new Color(200, 205, 232));
                    panel21.setLayout(new BorderLayout());

                    //---- label30 ----
                    label30.setText("Special Format note");
                    label30.setBackground(new Color(200, 205, 232));
                    ATFieldInfo.assignLabelInfo(label30, Assessments.class, Assessments.PROPERTYNAME_SPECIAL_FORMAT_NOTE);
                    panel21.add(label30, BorderLayout.NORTH);

                    //======== scrollPane7 ========
                    {

                        //---- textArea3 ----
                        textArea3.setColumns(25);
                        textArea3.setRows(8);
                        textArea3.setLineWrap(true);
                        scrollPane7.setViewportView(textArea3);
                    }
                    panel21.add(scrollPane7, BorderLayout.CENTER);
                }
                panel3.add(panel21, cc.xy(1, 5));

                //======== panel25 ========
                {
                    panel25.setBackground(new Color(200, 205, 232));
                    panel25.setLayout(new BorderLayout());

                    //---- label33 ----
                    label33.setText("Monetary Value note");
                    ATFieldInfo.assignLabelInfo(label33, Assessments.class, Assessments.PROPERTYNAME_MONETARY_VALUE_NOTE);
                    panel25.add(label33, BorderLayout.NORTH);

                    //======== scrollPane9 ========
                    {

                        //---- textArea5 ----
                        textArea5.setColumns(25);
                        textArea5.setRows(8);
                        textArea5.setLineWrap(true);
                        scrollPane9.setViewportView(textArea5);
                    }
                    panel25.add(scrollPane9, BorderLayout.CENTER);
                }
                panel3.add(panel25, cc.xy(5, 5));
            }
            tabbedPane1.addTab("Special Formats & Values", panel3);

        }
        add(tabbedPane1, BorderLayout.CENTER);

        //======== panel4 ========
        {
            panel4.setBackground(new Color(200, 205, 232));
            panel4.setBorder(new EmptyBorder(0, 10, 0, 10));
            panel4.setLayout(new FlowLayout(FlowLayout.RIGHT));

            //---- label1 ----
            label1.setText("Assessment #");
            panel4.add(label1);

            //---- assessmentNumber ----
            assessmentNumber.setText("xxx");
            panel4.add(assessmentNumber);
        }
        add(panel4, BorderLayout.NORTH);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JPanel panel5;
    private JLabel label2;
    private JScrollPane scrollPane1;
    private DomainSortableTable accessionsTable;
    private JPanel panel6;
    private JButton button1;
    private JButton button2;
    private JPanel panel13;
    private JLabel label6;
    private JTextField textField1;
    private JLabel label35;
    private JTextField textField17;
    private JLabel label34;
    private JTextField textField18;
    private JLabel label7;
    private JTextField textField2;
    private JLabel label8;
    private JCheckBox checkBox2;
    private JLabel label9;
    private JTextField textField3;
    private JPanel panel7;
    private JLabel label3;
    private JScrollPane scrollPane2;
    private DomainSortableTable resourcesTable;
    private JPanel panel8;
    private JButton button3;
    private JButton button4;
    private JPanel panel9;
    private JLabel label4;
    private JScrollPane scrollPane3;
    private DomainSortableTable digitalObjectsTable;
    private JPanel panel10;
    private JButton button5;
    private JButton button6;
    private JPanel panel11;
    private JLabel label5;
    private JScrollPane scrollPane4;
    private JTextArea reviewNoteTextArea;
    private JPanel panel12;
    private JCheckBox checkBox1;
    private JPanel panel2;
    private JPanel panel14;
    private JLabel label10;
    private JTextField textField4;
    private JLabel label12;
    private JTextField textField6;
    private JLabel label11;
    private JTextField textField5;
    private JLabel label13;
    private JTextField textField7;
    private JLabel label15;
    private JTextField textField9;
    private JLabel label14;
    private JTextField textField8;
    private JLabel label16;
    private JTextField textField10;
    private JLabel label17;
    private JTextField textField11;
    private JLabel label18;
    private JTextField textField12;
    private JPanel panel17;
    private JLabel label25;
    private JCheckBox checkBox3;
    private JCheckBox checkBox6;
    private JCheckBox checkBox4;
    private JCheckBox checkBox7;
    private JCheckBox checkBox5;
    private JCheckBox checkBox8;
    private JPanel panel15;
    private JLabel label19;
    private JScrollPane scrollPane5;
    private JTextArea textArea1;
    private JPanel panel16;
    private JLabel label20;
    private JLabel label21;
    private JTextField textField13;
    private JLabel label23;
    private JTextField textField14;
    private JLabel label22;
    private JTextField textField15;
    private JLabel label24;
    private JPanel panel18;
    private JPanel panel19;
    private JLabel label26;
    private JCheckBox checkBox9;
    private JCheckBox checkBox13;
    private JCheckBox checkBox10;
    private JCheckBox checkBox14;
    private JCheckBox checkBox11;
    private JCheckBox checkBox15;
    private JCheckBox checkBox12;
    private JCheckBox checkBox16;
    private JLabel label27;
    private JScrollPane scrollPane6;
    private JTextArea textArea2;
    private JPanel panel3;
    private JPanel panel20;
    private JLabel label28;
    private JLabel label29;
    private JCheckBox checkBox17;
    private JCheckBox checkBox25;
    private JCheckBox checkBox18;
    private JCheckBox checkBox26;
    private JCheckBox checkBox19;
    private JCheckBox checkBox27;
    private JCheckBox checkBox20;
    private JCheckBox checkBox28;
    private JCheckBox checkBox21;
    private JCheckBox checkBox29;
    private JCheckBox checkBox22;
    private JCheckBox checkBox30;
    private JCheckBox checkBox23;
    private JCheckBox checkBox31;
    private JCheckBox checkBox24;
    private JCheckBox checkBox32;
    private JCheckBox checkBox33;
    private JCheckBox checkBox34;
    private JPanel panel22;
    private JLabel label31;
    private JScrollPane scrollPane8;
    private JTextArea textArea4;
    private JPanel panel23;
    private JLabel label32;
    private JTextField textField16;
    private JPanel panel21;
    private JLabel label30;
    private JScrollPane scrollPane7;
    private JTextArea textArea3;
    private JPanel panel25;
    private JLabel label33;
    private JScrollPane scrollPane9;
    private JTextArea textArea5;
    private JPanel panel4;
    private JLabel label1;
    private JLabel assessmentNumber;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    /**
     * Method to return the resouces table which displays linked resources
     * @return The table which contains the link resources
     */
    public DomainSortableTable getTableAssessmentsResources() {
        return resourcesTable;
    }

    /**
     * Method to return the accessions table which displays linked accessions
     * @return The table which contains the link accessions
     */
    public DomainSortableTable getTableAssessmentsAccessions() {
        return accessionsTable;
    }

    /**
     * Method to return the digital object table which displays linked digital objects
     * @return The table which contains the link digital objects
     */
    public DomainSortableTable getTableAssessmentsDigitalObjects() {
        return digitalObjectsTable;
    }

    /**
     * Method that initializes any embedded plugins that would add an editor
     */
    private void initPlugins() {
        plugins = ATPluginFactory.getInstance().getEmbeddedAssessmentEditorPlugins();
        if(plugins != null) {
            for(ATPlugin plugin : plugins) {
                plugin.setEditorField(this);
                HashMap pluginPanels = plugin.getEmbeddedPanels();
                for(Object key : pluginPanels.keySet()) {
                    String panelName = (String)key;
                    JPanel pluginPanel = (JPanel)pluginPanels.get(key);

                    // see where to add the tab and if to remove any tabs that are
                    // already there
                    if(panelName.indexOf("::") == -1) { // add it to end
                        tabbedPane1.addTab(panelName, pluginPanel);
                    } else { // adding table to specific location in tab
                        // get tab placement information from the name string
                        // panelPlacementInfo[0] = panel name
                        // panelPlacementInfo[1] = panel index
                        // panelPlacementInfo[2] = yes if to replace the panel already there,
                        //                         no to just insert at the index
                        String[] panelPlacementInfo = panelName.split("::");
                        try {
                            int index  = Integer.parseInt(panelPlacementInfo[1]);

                            // see whether to remove the index already there
                            if(panelPlacementInfo[2].equalsIgnoreCase("yes")) {
                                tabbedPane1.remove(index);
                            }

                            // now insert the panel at the location specified by the index
                            tabbedPane1.insertTab(panelPlacementInfo[0], null, pluginPanel, "", index);

                        } catch(NumberFormatException nfe) {
                            System.out.println("Invalid tab index for the plugin panel named " + panelPlacementInfo[2]);
                        }
                    }
                }
            }
        }
    }

    /**
     * Method set the model for any plugins loaded
     */
    private void setPluginModel() {
        if(plugins != null) {
            for(ATPlugin plugin : plugins) {
                plugin.setModel(getModel(), null);
            }
        }
    }
}
