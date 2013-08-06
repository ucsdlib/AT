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
 * Created by JFormDesigner on Mon Sep 25 21:09:13 EDT 2006
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.model.AccessionsLocations;
import org.archiviststoolkit.model.Accessions;
import org.archiviststoolkit.model.Locations;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.dialog.LocationAssignmentAccessions;
import org.archiviststoolkit.structure.ATFieldInfo;

public class AccessionsLocationsFields extends DomainEditorFields {
	public AccessionsLocationsFields() {
		initComponents();
	}

	/**
	 * Set the domain model for this editor.
	 *
	 * @param model the model
	 */

	public void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		AccessionsLocations accessionsLocationsModel = (AccessionsLocations)model;
		locationTextArea.setText(accessionsLocationsModel.getLocation().toString());
	}

	public Component getInitialFocusComponent() {
		return note;
	}

	private void changeLocationButtonActionPerformed() {
		LocationAssignmentAccessions dialog = new LocationAssignmentAccessions(getParentEditor());
		dialog.setMainHeaderByClass(Accessions.class);
		if (dialog.showDialog() == JOptionPane.OK_OPTION) {
			AccessionsLocations accessionsLocationsModel = (AccessionsLocations)super.getModel();
      Accessions accessionsModel = accessionsLocationsModel.getAccession();
      Locations location = dialog.getSelectedLocation();

      if(!accessionsModel.containsLocationLink(location)) {
        accessionsLocationsModel.setLocation(location);
			  locationTextArea.setText(accessionsLocationsModel.getLocation().toString());
      }
      else {
        JOptionPane.showMessageDialog(getParentEditor(),
                location.toString() + " is already linked to this record");
      }
    }
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label_subjectTerm = new JLabel();
        note = ATBasicComponentFactory.createTextField(detailsModel.getModel(AccessionsLocations.PROPERTYNAME_NOTE));
        label_subjectTermType = new JLabel();
        scrollPane1 = new JScrollPane();
        locationTextArea = new JTextArea();
        changeLocationButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBorder(Borders.DLU4_BORDER);
        setBackground(new Color(234, 201, 250));
        setOpaque(false);
        setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        setLayout(new FormLayout(
            new ColumnSpec[] {
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                new ColumnSpec("max(default;400px):grow")
            },
            new RowSpec[] {
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC
            }));

        //---- label_subjectTerm ----
        label_subjectTerm.setText("Note/Container Info");
        label_subjectTerm.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectTerm, Accessions.class, AccessionsLocations.PROPERTYNAME_NOTE);
        add(label_subjectTerm, cc.xy(1, 1));
        add(note, cc.xy(3, 1));

        //---- label_subjectTermType ----
        label_subjectTermType.setText("Location");
        label_subjectTermType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectTermType, Accessions.class, AccessionsLocations.PROPERTYNAME_LOCATION);
        add(label_subjectTermType, cc.xy(1, 3));

        //======== scrollPane1 ========
        {
            scrollPane1.setBorder(null);
            scrollPane1.setOpaque(false);

            //---- locationTextArea ----
            locationTextArea.setRows(3);
            locationTextArea.setOpaque(false);
            locationTextArea.setEditable(false);
            locationTextArea.setLineWrap(true);
            locationTextArea.setWrapStyleWord(true);
            scrollPane1.setViewportView(locationTextArea);
        }
        add(scrollPane1, cc.xy(3, 3));

        //---- changeLocationButton ----
        changeLocationButton.setText("Change Location");
        changeLocationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeLocationButtonActionPerformed();
            }
        });
        add(changeLocationButton, cc.xywh(3, 5, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label_subjectTerm;
    public JTextField note;
    private JLabel label_subjectTermType;
    private JScrollPane scrollPane1;
    private JTextArea locationTextArea;
    private JButton changeLocationButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
