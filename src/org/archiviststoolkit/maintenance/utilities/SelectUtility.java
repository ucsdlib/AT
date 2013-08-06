/*
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
 * Created by JFormDesigner on Fri May 11 13:30:03 EDT 2007
 */

package org.archiviststoolkit.maintenance.utilities;

import java.awt.event.*;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.netbeans.spi.wizard.WizardPage;

import java.util.Vector;
import java.awt.*;

public class SelectUtility extends WizardPage {

    public static final String IMPORT_NOTESETC_TYPES = "Import Notes Etc. Types";
    public static final String IMPORT_LOOKUP_LISTS = "Import Lookup Lists";
    public static final String RESTORE_TABLE_AND_FIELD_DEFAULTS = "Restore Table and Field Defaults";
    public static final String IMPORT_INLINE_TAGS = "Import Inline Tags";
    public static final String ADD_NOTE_DEFAULTS_TO_REPOSITORIES = "Add Note Defaults to Repositories";
    public static final String IMPORT_FIELD_EXAMPLES_AND_DEFINITIONS = "Import Field Examples and Definitions";
    public static final String UPDATE_DATABASE_SCHEMA = "Update Database Schema";
    public static final String RECONCILE_FIELD_DEFS_AND_EXAMPLES = "Reconcile Field Definitions and Examples";
    public static final String COMPILE_JASPER_REPORT = "Compile Jasper Report";
    public static final String ASSIGN_PERSISTENT_IDS = "Assign Persistent Ids";
    public static final String FIX_BLANK_FIELD_LABELS = "Fix Blank Field Labels";


    public SelectUtility() {
        super("selUtility", "Select Utility");
        initComponents();
        populateUtilityList();
    }

    protected String validateContents(Component component, Object event) {
        if (utilities.getSelectedIndex() == 0) {
            return "Please select a utility to run";
        }
        // everything is ok
        return null;
    }

    private void utilitiesActionPerformed() {
        String selectedUtility = (String) utilities.getSelectedItem();
        if (utilities.getSelectedIndex() == 0) {
            utilityDescription.setText("");
        } else if (selectedUtility.equals(IMPORT_NOTESETC_TYPES)) {
            utilityDescription.setText("Import note types from an XML file.");
        } else if (selectedUtility.equals(IMPORT_LOOKUP_LISTS)) {
            utilityDescription.setText("Import lookup lists from an XML file.");
        } else if (selectedUtility.equals(RESTORE_TABLE_AND_FIELD_DEFAULTS)) {
            utilityDescription.setText("Restore field labels, return screen orders and search options to the AT defaults.");
        } else if (selectedUtility.equals(IMPORT_INLINE_TAGS)) {
            utilityDescription.setText("Import inline tag information from an XML file.");
        } else if (selectedUtility.equals(ADD_NOTE_DEFAULTS_TO_REPOSITORIES)) {
            utilityDescription.setText("Add any missing note defaults to repository records.");
        } else if (selectedUtility.equals(IMPORT_FIELD_EXAMPLES_AND_DEFINITIONS)) {
            utilityDescription.setText("Import field examples and definitions from an XML file.");
        } else if (selectedUtility.equals(UPDATE_DATABASE_SCHEMA)) {
            utilityDescription.setText("Update the database schema using information from the AT client.");
//		} else if (selectedUtility.equals(RECONCILE_FIELD_DEFS_AND_EXAMPLES)) {
//			utilityDescription.setText("Reconcile Field Definitions and Examples");
        } else if (selectedUtility.equals(ASSIGN_PERSISTENT_IDS)) {
            utilityDescription.setText("Assign persistent ids to resource components and notes.");
        } else if (selectedUtility.equals(COMPILE_JASPER_REPORT)) {
            utilityDescription.setText("Compile Jasper report definition into a .jasper file.");
        } else if (selectedUtility.equals(FIX_BLANK_FIELD_LABELS)) {
            utilityDescription.setText("Insert the field name as the label for all fields without labels.");
        } else {
            utilityDescription.setText("");
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        utilities = new JComboBox();
        scrollPane1 = new JScrollPane();
        utilityDescription = new JTextArea();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setLayout(new FormLayout(
                ColumnSpec.decodeSpecs("default:grow"),
                new RowSpec[]{
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                }));

        //---- utilities ----
        utilities.setOpaque(false);
        utilities.setName("selectedUtility");
        utilities.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                utilitiesActionPerformed();
            }
        });
        add(utilities, cc.xywh(1, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //======== scrollPane1 ========
        {
            scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            //---- utilityDescription ----
            utilityDescription.setEditable(false);
            scrollPane1.setViewportView(utilityDescription);
        }
        add(scrollPane1, cc.xy(1, 3));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JComboBox utilities;
    private JScrollPane scrollPane1;
    private JTextArea utilityDescription;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public void populateUtilityList() {
        Vector<String> returnVector = new Vector<String>();
        returnVector.add("Select a utility to run");
        returnVector.add(IMPORT_NOTESETC_TYPES);
        returnVector.add(IMPORT_LOOKUP_LISTS);
        returnVector.add(RESTORE_TABLE_AND_FIELD_DEFAULTS);
        returnVector.add(IMPORT_INLINE_TAGS);
        returnVector.add(ADD_NOTE_DEFAULTS_TO_REPOSITORIES);
        returnVector.add(IMPORT_FIELD_EXAMPLES_AND_DEFINITIONS);
//		returnVector.add(RECONCILE_FIELD_DEFS_AND_EXAMPLES);
        returnVector.add(UPDATE_DATABASE_SCHEMA);
        returnVector.add(FIX_BLANK_FIELD_LABELS);
//		returnVector.add(ASSIGN_PERSISTENT_IDS);
        returnVector.add(COMPILE_JASPER_REPORT);
        utilities.setModel(new DefaultComboBoxModel(returnVector));
    }

}
