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
 * Created by JFormDesigner on Mon Aug 29 12:46:01 EDT 2005
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.PresentationModel;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.mydomain.DomainEditor;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.util.SequencedObjectsUtils;

public class NameFamilyNonPreferredFields extends NameNonPreferredNameFields {

	protected NameFamilyNonPreferredFields(PresentationModel parentPresentationModel) {
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
        label_familyName = new JLabel();
        familyName = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(NonPreferredNames.PROPERTYNAME_FAMILY_NAME));
        label_familyNamePrefix = new JLabel();
        familyNamePrefix = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(NonPreferredNames.PROPERTYNAME_FAMILY_NAME_PREFIX));
        label_familyQualifier = new JLabel();
        familyQualifier = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(NonPreferredNames.PROPERTYNAME_QUALIFIER));
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
        setPreferredSize(new Dimension(500, 138));
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

        //---- label_familyName ----
        label_familyName.setText("Family Name");
        label_familyName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_familyName, NonPreferredNames.class, NonPreferredNames.PROPERTYNAME_FAMILY_NAME);
        add(label_familyName, cc.xy(1, 3));

        //---- familyName ----
        familyName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                sortNameConstruction(e);
            }
        });
        add(familyName, cc.xy(3, 3));

        //---- label_familyNamePrefix ----
        label_familyNamePrefix.setText("Prefix");
        label_familyNamePrefix.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_familyNamePrefix, NonPreferredNames.class, NonPreferredNames.PROPERTYNAME_FAMILY_NAME_PREFIX);
        add(label_familyNamePrefix, cc.xy(1, 5));

        //---- familyNamePrefix ----
        familyNamePrefix.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                sortNameConstruction(e);
            }
        });
        add(familyNamePrefix, cc.xy(3, 5));

        //---- label_familyQualifier ----
        label_familyQualifier.setText("Qualifier");
        label_familyQualifier.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_familyQualifier, NonPreferredNames.class, NonPreferredNames.PROPERTYNAME_QUALIFIER);
        add(label_familyQualifier, cc.xy(1, 7));

        //---- familyQualifier ----
        familyQualifier.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                sortNameConstruction(e);
            }
        });
        add(familyQualifier, cc.xy(3, 7));

        //---- separator3 ----
        separator3.setBackground(new Color(220, 220, 232));
        separator3.setForeground(new Color(147, 131, 86));
        separator3.setMinimumSize(new Dimension(1, 10));
        separator3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(separator3, cc.xywh(1, 9, 3, 1));

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
        add(panel1, cc.xywh(1, 11, 3, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	private void sortNameConstruction(FocusEvent e) {
		((BasicNames)this.getModel()).createSortName();
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel nameTypeLabel;
    private JLabel nameType;
    private JLabel label_familyName;
    private JTextField familyName;
    private JLabel label_familyNamePrefix;
    private JTextField familyNamePrefix;
    private JLabel label_familyQualifier;
    private JTextField familyQualifier;
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
		return familyName;
	}
}
