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
 * Created by JFormDesigner on Mon Aug 29 12:48:14 EDT 2005
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.PresentationModel;
import org.archiviststoolkit.model.NonPreferredNames;
import org.archiviststoolkit.model.BasicNames;
import org.archiviststoolkit.model.Users;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.structure.ATFieldInfo;

public class NameCorpNonPreferredFields extends NameNonPreferredNameFields {
	public NameCorpNonPreferredFields(PresentationModel parentPresentationModel) {
		super();
		this.parentPresentationModel = parentPresentationModel;
		initComponents();
		if (!Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_ADVANCED_DATA_ENTRY)) {
			 setFormToReadOnly();
		 }
	}

	private void createAutomaticallyStateChanged(ChangeEvent e) {
		enableDisableSortName(sortName, true);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        nameTypeLabel = new JLabel();
        nameType = new JLabel();
        label_corporatePrimary = new JLabel();
        corporatePrimary = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(NonPreferredNames.PROPERTYNAME_CORPORATE_PRIMARY_NAME));
        label_corporateSubordinate1 = new JLabel();
        corporateSubordinate1 = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(NonPreferredNames.PROPERTYNAME_CORPORATE_SUBORDINATE_1));
        label_corporateSubordinate2 = new JLabel();
        corporateSubordinate2 = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(NonPreferredNames.PROPERTYNAME_CORPORATE_SUBORDINATE_2));
        label_corporateNumber = new JLabel();
        corporateNumber = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(NonPreferredNames.PROPERTYNAME_NUMBER));
        label_corporateQualifier = new JLabel();
        corporateQualifier = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(NonPreferredNames.PROPERTYNAME_QUALIFIER));
        separator3 = new JSeparator();
        panel1 = new JPanel();
        label_sortName = new JLabel();
        sortName = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(NonPreferredNames.PROPERTYNAME_SORT_NAME));
        namePersonalDirectOrder2 = ATBasicComponentFactory.createCheckBox(parentPresentationModel, NonPreferredNames.PROPERTYNAME_CREATE_SORT_NAME_AUTOMATICALLY, NonPreferredNames.class);
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setOpaque(false);
        setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        setBorder(Borders.DLU4_BORDER);
        setPreferredSize(new Dimension(500, 192));
        setLayout(new FormLayout(
            new ColumnSpec[] {
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                new ColumnSpec("max(default;300px):grow")
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
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC
            }));

        //---- nameTypeLabel ----
        nameTypeLabel.setText("Name Identity Record:");
        nameTypeLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(nameTypeLabel, cc.xy(1, 1));

        //---- nameType ----
        nameType.setText("nameType");
        nameType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(nameType, cc.xy(3, 1));

        //---- label_corporatePrimary ----
        label_corporatePrimary.setText("Primary Name");
        label_corporatePrimary.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_corporatePrimary, NonPreferredNames.class, NonPreferredNames.PROPERTYNAME_CORPORATE_PRIMARY_NAME);
        add(label_corporatePrimary, cc.xy(1, 3));

        //---- corporatePrimary ----
        corporatePrimary.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                sortNameConstruction(e);
            }
        });
        add(corporatePrimary, cc.xy(3, 3));

        //---- label_corporateSubordinate1 ----
        label_corporateSubordinate1.setText("Subordinate");
        label_corporateSubordinate1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_corporateSubordinate1, NonPreferredNames.class, NonPreferredNames.PROPERTYNAME_CORPORATE_SUBORDINATE_1);
        add(label_corporateSubordinate1, cc.xy(1, 5));

        //---- corporateSubordinate1 ----
        corporateSubordinate1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                sortNameConstruction(e);
            }
        });
        add(corporateSubordinate1, cc.xy(3, 5));

        //---- label_corporateSubordinate2 ----
        label_corporateSubordinate2.setText("Subordinate");
        label_corporateSubordinate2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_corporateSubordinate2, NonPreferredNames.class, NonPreferredNames.PROPERTYNAME_CORPORATE_SUBORDINATE_2);
        add(label_corporateSubordinate2, cc.xy(1, 7));

        //---- corporateSubordinate2 ----
        corporateSubordinate2.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                sortNameConstruction(e);
            }
        });
        add(corporateSubordinate2, cc.xy(3, 7));

        //---- label_corporateNumber ----
        label_corporateNumber.setText("Number");
        label_corporateNumber.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_corporateNumber, NonPreferredNames.class, NonPreferredNames.PROPERTYNAME_NUMBER);
        add(label_corporateNumber, cc.xy(1, 9));

        //---- corporateNumber ----
        corporateNumber.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                sortNameConstruction(e);
            }
        });
        add(corporateNumber, cc.xy(3, 9));

        //---- label_corporateQualifier ----
        label_corporateQualifier.setText("Qualifier");
        label_corporateQualifier.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_corporateQualifier, NonPreferredNames.class, NonPreferredNames.PROPERTYNAME_QUALIFIER);
        add(label_corporateQualifier, cc.xy(1, 11));

        //---- corporateQualifier ----
        corporateQualifier.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                sortNameConstruction(e);
            }
        });
        add(corporateQualifier, cc.xy(3, 11));

        //---- separator3 ----
        separator3.setBackground(new Color(220, 220, 232));
        separator3.setForeground(new Color(147, 131, 86));
        separator3.setMinimumSize(new Dimension(1, 10));
        separator3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(separator3, cc.xywh(1, 13, 3, 1));

        //======== panel1 ========
        {
            panel1.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC
                },
                RowSpec.decodeSpecs("default")));

            //---- label_sortName ----
            label_sortName.setText("Sort Name");
            label_sortName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label_sortName, NonPreferredNames.class, NonPreferredNames.PROPERTYNAME_SORT_NAME);
            panel1.add(label_sortName, cc.xy(1, 1));
            panel1.add(sortName, cc.xy(3, 1));

            //---- namePersonalDirectOrder2 ----
            namePersonalDirectOrder2.setText("Create automatically");
            namePersonalDirectOrder2.setOpaque(false);
            namePersonalDirectOrder2.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    createAutomaticallyStateChanged(e);
                }
            });
            namePersonalDirectOrder2.setText(ATFieldInfo.getLabel(NonPreferredNames.class, NonPreferredNames.PROPERTYNAME_CREATE_SORT_NAME_AUTOMATICALLY));
            panel1.add(namePersonalDirectOrder2, cc.xy(5, 1));
        }
        add(panel1, cc.xywh(1, 15, 3, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	private void sortNameConstruction(FocusEvent e) {
		((BasicNames)this.getModel()).createSortName();
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel nameTypeLabel;
    private JLabel nameType;
    private JLabel label_corporatePrimary;
    private JTextField corporatePrimary;
    private JLabel label_corporateSubordinate1;
    private JTextField corporateSubordinate1;
    private JLabel label_corporateSubordinate2;
    private JTextField corporateSubordinate2;
    private JLabel label_corporateNumber;
    private JTextField corporateNumber;
    private JLabel label_corporateQualifier;
    private JTextField corporateQualifier;
    private JSeparator separator3;
    private JPanel panel1;
    private JLabel label_sortName;
    private JTextField sortName;
    public JCheckBox namePersonalDirectOrder2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	private PresentationModel parentPresentationModel;

	public final void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		parentPresentationModel.setBean(model);
		this.nameType.setText(((NonPreferredNames)this.getModel()).getNameType());
		enableDisableSortName(sortName, false);
	}

	public Component getInitialFocusComponent() {
		return corporatePrimary;
	}
}
