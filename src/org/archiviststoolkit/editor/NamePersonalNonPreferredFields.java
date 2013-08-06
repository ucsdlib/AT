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
 * Created by JFormDesigner on Mon Aug 29 12:46:54 EDT 2005
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
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.model.NonPreferredNames;
import org.archiviststoolkit.model.BasicNames;
import org.archiviststoolkit.model.Users;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.structure.ATFieldInfo;

public class NamePersonalNonPreferredFields extends NameNonPreferredNameFields {
	public NamePersonalNonPreferredFields(PresentationModel parentPresentationModel) {
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
        label_personalPrefix = new JLabel();
        personalPrefix = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(NonPreferredNames.PROPERTYNAME_PERSONAL_PREFIX));
        personalDirectOrder = ATBasicComponentFactory.createCheckBox(parentPresentationModel, NonPreferredNames.PROPERTYNAME_PERSONAL_DIRECT_ORDER, NonPreferredNames.class);
        label_personalPrimaryName = new JLabel();
        personalPrimaryName = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(NonPreferredNames.PROPERTYNAME_PERSONAL_PRIMARY_NAME));
        label_personalRestOfName = new JLabel();
        personalRestOfName = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(NonPreferredNames.PROPERTYNAME_PERSONAL_REST_OF_NAME));
        label_personalTitle = new JLabel();
        personalSuffix2 = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(NonPreferredNames.PROPERTYNAME_PERSONAL_TITLE));
        label_personalSuffix = new JLabel();
        personalSuffix = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(NonPreferredNames.PROPERTYNAME_PERSONAL_SUFFIX));
        label_personalNumber = new JLabel();
        personalNumber = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(NonPreferredNames.PROPERTYNAME_NUMBER));
        label_personalDates = new JLabel();
        personalDates = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(NonPreferredNames.PROPERTYNAME_PERSONAL_DATES));
        label_personalFullerForm = new JLabel();
        personalFullerForm = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(NonPreferredNames.PROPERTYNAME_PERSONAL_FULLER_FORM));
        label_personalQualifier = new JLabel();
        personalQualifier = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(NonPreferredNames.PROPERTYNAME_QUALIFIER));
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
        setPreferredSize(new Dimension(500, 350));
        setLayout(new FormLayout(
            new ColumnSpec[] {
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                FormFactory.PREF_COLSPEC
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

        //---- label_personalPrefix ----
        label_personalPrefix.setText("Prefix");
        label_personalPrefix.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_personalPrefix, NonPreferredNames.class, NonPreferredNames.PROPERTYNAME_PERSONAL_PREFIX);
        add(label_personalPrefix, cc.xy(1, 3));

        //---- personalPrefix ----
        personalPrefix.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                sortNameConstruction(e);
            }
        });
        add(personalPrefix, cc.xy(3, 3));

        //---- personalDirectOrder ----
        personalDirectOrder.setBackground(new Color(231, 188, 251));
        personalDirectOrder.setText("Direct Order");
        personalDirectOrder.setOpaque(false);
        personalDirectOrder.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        personalDirectOrder.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                namePersonalDirectOrderStateChanged(e);
            }
        });
        personalDirectOrder.setText(ATFieldInfo.getLabel(NonPreferredNames.class, NonPreferredNames.PROPERTYNAME_PERSONAL_DIRECT_ORDER));
        add(personalDirectOrder, cc.xy(5, 3));

        //---- label_personalPrimaryName ----
        label_personalPrimaryName.setText("Primary Name");
        label_personalPrimaryName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_personalPrimaryName, NonPreferredNames.class, NonPreferredNames.PROPERTYNAME_PERSONAL_PRIMARY_NAME);
        add(label_personalPrimaryName, cc.xy(1, 5));

        //---- personalPrimaryName ----
        personalPrimaryName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                sortNameConstruction(e);
            }
        });
        add(personalPrimaryName, cc.xywh(3, 5, 3, 1));

        //---- label_personalRestOfName ----
        label_personalRestOfName.setText("Rest of Name");
        label_personalRestOfName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_personalRestOfName, NonPreferredNames.class, NonPreferredNames.PROPERTYNAME_PERSONAL_REST_OF_NAME);
        add(label_personalRestOfName, cc.xy(1, 7));

        //---- personalRestOfName ----
        personalRestOfName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                sortNameConstruction(e);
            }
        });
        add(personalRestOfName, cc.xywh(3, 7, 3, 1));

        //---- label_personalTitle ----
        label_personalTitle.setText("Title");
        label_personalTitle.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_personalTitle, NonPreferredNames.class, NonPreferredNames.PROPERTYNAME_PERSONAL_TITLE);
        add(label_personalTitle, cc.xy(1, 9));

        //---- personalSuffix2 ----
        personalSuffix2.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                sortNameConstruction(e);
            }
        });
        add(personalSuffix2, cc.xywh(3, 9, 3, 1));

        //---- label_personalSuffix ----
        label_personalSuffix.setText("Suffix");
        label_personalSuffix.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_personalSuffix, NonPreferredNames.class, NonPreferredNames.PROPERTYNAME_PERSONAL_SUFFIX);
        add(label_personalSuffix, cc.xy(1, 11));

        //---- personalSuffix ----
        personalSuffix.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                sortNameConstruction(e);
            }
        });
        add(personalSuffix, cc.xywh(3, 11, 3, 1));

        //---- label_personalNumber ----
        label_personalNumber.setText("Number");
        label_personalNumber.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_personalNumber, NonPreferredNames.class, NonPreferredNames.PROPERTYNAME_NUMBER);
        add(label_personalNumber, cc.xy(1, 13));

        //---- personalNumber ----
        personalNumber.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                sortNameConstruction(e);
            }
        });
        add(personalNumber, cc.xywh(3, 13, 3, 1));

        //---- label_personalDates ----
        label_personalDates.setText("Dates");
        label_personalDates.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_personalDates, NonPreferredNames.class, NonPreferredNames.PROPERTYNAME_PERSONAL_DATES);
        add(label_personalDates, cc.xy(1, 15));

        //---- personalDates ----
        personalDates.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                sortNameConstruction(e);
            }
        });
        add(personalDates, cc.xywh(3, 15, 3, 1));

        //---- label_personalFullerForm ----
        label_personalFullerForm.setText("Fuller Form");
        label_personalFullerForm.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_personalFullerForm, NonPreferredNames.class, NonPreferredNames.PROPERTYNAME_PERSONAL_FULLER_FORM);
        add(label_personalFullerForm, cc.xy(1, 17));

        //---- personalFullerForm ----
        personalFullerForm.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                sortNameConstruction(e);
            }
        });
        add(personalFullerForm, cc.xywh(3, 17, 3, 1));

        //---- label_personalQualifier ----
        label_personalQualifier.setText("Qualifier");
        label_personalQualifier.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_personalQualifier, NonPreferredNames.class, NonPreferredNames.PROPERTYNAME_QUALIFIER);
        add(label_personalQualifier, cc.xy(1, 19));

        //---- personalQualifier ----
        personalQualifier.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                sortNameConstruction(e);
            }
        });
        add(personalQualifier, cc.xywh(3, 19, 3, 1));

        //---- separator3 ----
        separator3.setBackground(new Color(220, 220, 232));
        separator3.setForeground(new Color(147, 131, 86));
        separator3.setMinimumSize(new Dimension(1, 10));
        separator3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(separator3, cc.xywh(1, 21, 5, 1));

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
            namePersonalDirectOrder2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            namePersonalDirectOrder2.setOpaque(false);
            namePersonalDirectOrder2.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    createAutomaticallyStateChanged(e);
                }
            });
            namePersonalDirectOrder2.setText(ATFieldInfo.getLabel(NonPreferredNames.class, NonPreferredNames.PROPERTYNAME_CREATE_SORT_NAME_AUTOMATICALLY));
            panel1.add(namePersonalDirectOrder2, cc.xy(5, 1));
        }
        add(panel1, cc.xywh(1, 23, 5, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	private void sortNameConstruction(FocusEvent e) {
		((BasicNames)this.getModel()).createSortName();
	}

	private void namePersonalDirectOrderStateChanged(ChangeEvent e) {
		if (this.getModel() != null) {
			((BasicNames)this.getModel()).createSortName();
		}
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel nameTypeLabel;
    private JLabel nameType;
    private JLabel label_personalPrefix;
    private JTextField personalPrefix;
    public JCheckBox personalDirectOrder;
    private JLabel label_personalPrimaryName;
    private JTextField personalPrimaryName;
    private JLabel label_personalRestOfName;
    private JTextField personalRestOfName;
    private JLabel label_personalTitle;
    private JTextField personalSuffix2;
    private JLabel label_personalSuffix;
    private JTextField personalSuffix;
    private JLabel label_personalNumber;
    private JTextField personalNumber;
    private JLabel label_personalDates;
    private JTextField personalDates;
    private JLabel label_personalFullerForm;
    private JTextField personalFullerForm;
    private JLabel label_personalQualifier;
    private JTextField personalQualifier;
    private JSeparator separator3;
    private JPanel panel1;
    private JLabel label_sortName;
    private JTextField sortName;
    public JCheckBox namePersonalDirectOrder2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	private PresentationModel parentPresentationModel;

	public final void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		NonPreferredNames nameModel = (NonPreferredNames)this.getModel();
		parentPresentationModel.setBean(model);
		this.nameType.setText(nameModel.getNameType());
		enableDisableSortName(sortName, false);
	}

	public Component getInitialFocusComponent() {
		return personalPrefix;
	}
}
