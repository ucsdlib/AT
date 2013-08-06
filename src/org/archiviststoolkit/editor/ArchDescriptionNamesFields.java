/**
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
 * Created by JFormDesigner on Tue Aug 29 14:14:15 EDT 2006
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.list.SelectionInList;
import org.archiviststoolkit.exceptions.UnsupportedDomainObjectModelException;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.StandardEditor;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.util.LookupListUtils;

public class ArchDescriptionNamesFields extends DomainEditorFields {
    private Object oldFunction = null; // used to store the old function so it can be reset

	public ArchDescriptionNamesFields() {
		super();
		initComponents();
	}

	public Component getInitialFocusComponent() {
		return function;
	}

	private void functionActionPerformed(ActionEvent e) {
        if (function.getSelectedItem() != null && !function.getSelectedItem().toString().equals("")) {
			populateComboBoxes(function.getSelectedItem().toString());
			this.invalidate();
			this.validate();
			this.repaint();
		} else {
            JOptionPane.showMessageDialog(this, "You must enter a function");
            function.setSelectedItem(oldFunction); // just set it to the old function
        }
	}

	private void populateComboBoxes(String selectedFunction) {
		if (selectedFunction.equalsIgnoreCase(ArchDescriptionNames.PROPERTYNAME_FUNCTION_SUBJECT)) {
			form.setVisible(true);
			label_form.setForeground(Color.BLACK);
			Bindings.bind(role,
					new SelectionInList(LookupListUtils.getLookupListValues(LookupListUtils.LIST_NAME_NAME_LINK_SUBJECT_CREATOR_ROLE),
							detailsModel.getBufferedModel(ArchDescriptionNames.PROPERTYNAME_ROLE)));
			role.setVisible(true);
			label_role.setVisible(true);
		} else if (selectedFunction.equalsIgnoreCase(ArchDescriptionNames.PROPERTYNAME_FUNCTION_SOURCE)) {
			form.setVisible(false);
			label_form.setForeground(ApplicationFrame.BACKGROUND_COLOR);
			Bindings.bind(role,
					new SelectionInList(LookupListUtils.getLookupListValues(LookupListUtils.LIST_NAME_NAME_LINK_SOURCE_ROLE),
							detailsModel.getBufferedModel(ArchDescriptionNames.PROPERTYNAME_ROLE)));
			role.setVisible(true);
			label_role.setVisible(true);
		} else if (selectedFunction.equalsIgnoreCase(ArchDescriptionNames.PROPERTYNAME_FUNCTION_CREATOR)) {
			form.setVisible(false);
			label_form.setForeground(ApplicationFrame.BACKGROUND_COLOR);
			Bindings.bind(role,
					new SelectionInList(LookupListUtils.getLookupListValues(LookupListUtils.LIST_NAME_NAME_LINK_SUBJECT_CREATOR_ROLE),
							detailsModel.getBufferedModel(ArchDescriptionNames.PROPERTYNAME_ROLE)));
			role.setVisible(true);
			label_role.setVisible(true);
		} else {
			form.setVisible(false);
			label_form.setForeground(ApplicationFrame.BACKGROUND_COLOR);
			role.setVisible(false);
			label_role.setVisible(false);
		}

	}

    /**
     * Metohd to store the old function value so that if the
     * user tries to select the empty function it can be reset
     * with no problem
     */
    private void functionFocusGained() {
        oldFunction = function.getSelectedItem();
    }

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        sortNameDisplay = new JTextField();
        label_function = new JLabel();
        function = new JComboBox();
        label_role = new JLabel();
        role = new JComboBox();
        label_form = new JLabel();
        form = ATBasicComponentFactory.createComboBox(detailsModel, ArchDescriptionNames.PROPERTYNAME_FORM, ArchDescriptionNames.class, true);
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
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC
            }));

        //---- sortNameDisplay ----
        sortNameDisplay.setEditable(false);
        sortNameDisplay.setBorder(null);
        sortNameDisplay.setForeground(new Color(0, 0, 102));
        sortNameDisplay.setSelectionColor(new Color(204, 0, 51));
        sortNameDisplay.setOpaque(false);
        add(sortNameDisplay, cc.xywh(1, 1, 3, 1));

        //---- label_function ----
        label_function.setText("Function");
        ATFieldInfo.assignLabelInfo(label_function, ArchDescriptionNames.class, ArchDescriptionNames.PROPERTYNAME_NAME_LINK_FUNCTION);
        add(label_function, cc.xy(1, 3));

        //---- function ----
        function.setOpaque(false);
        function.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                functionActionPerformed(e);
            }
        });
        function.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                functionFocusGained();
            }
        });
        add(function, cc.xywh(3, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- label_role ----
        label_role.setText("Role");
        ATFieldInfo.assignLabelInfo(label_role, ArchDescriptionNames.class, ArchDescriptionNames.PROPERTYNAME_ROLE);
        add(label_role, cc.xy(1, 5));

        //---- role ----
        role.setOpaque(false);
        add(role, cc.xywh(3, 5, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- label_form ----
        label_form.setText("Form Subdivision");
        ATFieldInfo.assignLabelInfo(label_form, ArchDescriptionNames.class, ArchDescriptionNames.PROPERTYNAME_FORM);
        add(label_form, cc.xy(1, 7));

        //---- form ----
        form.setOpaque(false);
        add(form, cc.xywh(3, 7, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JTextField sortNameDisplay;
    private JLabel label_function;
    private JComboBox function;
    private JLabel label_role;
    private JComboBox role;
    private JLabel label_form;
    private JComboBox form;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	Vector nameLinkFunctionvalues;

	private void init(ArchDescriptionNames archDescriptionNamesModel) throws UnsupportedDomainObjectModelException {
			if (archDescriptionNamesModel.getAccession() != null) {
				nameLinkFunctionvalues = LookupListUtils.getLookupListValues(LookupListUtils.LIST_NAME_NAME_LINK_FUNCTION);
				this.getParentEditor().getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_ACCESSIONS);
				this.getParentEditor().getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_ACCESSIONS);

			} else if (archDescriptionNamesModel.getResource() != null) {
				nameLinkFunctionvalues = LookupListUtils.getLookupListValues(LookupListUtils.LIST_NAME_NAME_LINK_FUNCTION);
				this.getParentEditor().getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_RESOURCES);
				this.getParentEditor().getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_RESOURCES);

			} else if (archDescriptionNamesModel.getResourceComponent() != null) {
				nameLinkFunctionvalues = LookupListUtils.getLookupListValues(LookupListUtils.LIST_NAME_NAME_LINK_FUNCTION);
				this.getParentEditor().getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_RESOURCES);
				this.getParentEditor().getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_RESOURCES);
				nameLinkFunctionvalues.remove("Source");

			} else if (archDescriptionNamesModel.getDigitalObject() != null) {
				nameLinkFunctionvalues = LookupListUtils.getLookupListValues(LookupListUtils.LIST_NAME_NAME_LINK_FUNCTION);
				nameLinkFunctionvalues.remove("Source");
				this.getParentEditor().getMainHeaderPanel().setBackground(StandardEditor.MODULE_HEADER_COLOR_RESOURCES);
				this.getParentEditor().getMainHeaderLabel().setText(StandardEditor.MODULE_HEADER_RESOURCES);
			} else {
				throw new UnsupportedDomainObjectModelException();
			}
		this.getParentEditor().getSubHeaderLabel().setText("Name Link");
		}

	/**
	 * Set the domain model for this editor.
	 *
	 * @param model the model
	 */
	public void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		ArchDescriptionNames archDescriptionNamesModel = (ArchDescriptionNames)model;
		try {
			init(archDescriptionNamesModel);
		} catch (UnsupportedDomainObjectModelException e) {
			new ErrorDialog(getParentEditor(), "", e).showDialog();
		}
		sortNameDisplay.setText(archDescriptionNamesModel.getName().getSortName());
		Bindings.bind(function, new SelectionInList(nameLinkFunctionvalues, detailsModel.getBufferedModel(ArchDescriptionNames.PROPERTYNAME_NAME_LINK_FUNCTION)));
		populateComboBoxes(archDescriptionNamesModel.getNameLinkFunction());
	}

}
