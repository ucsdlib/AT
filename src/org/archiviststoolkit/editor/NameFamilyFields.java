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
 * Created by JFormDesigner on Sun Aug 28 15:39:52 EDT 2005
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.PresentationModel;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.model.LookupList;
import org.archiviststoolkit.model.ModelUtil;
import org.archiviststoolkit.model.Users;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.structure.ATFieldInfo;

public class NameFamilyFields extends NamePrimaryNameFields {
	public NameFamilyFields(PresentationModel parentPresentationModel) {
		this.parentPresentationModel = parentPresentationModel;
		initComponents();
        initAccess();
	}

	public JComboBox getNameSource() {
		return nameSource;
	}

	public JComboBox getNameRule() {
		return nameRule;
	}

	protected JPanel getRuleSourcePanel() {
		return this;
	}

	private void createAutomaticallyStateChanged(ChangeEvent e) {
		enableDisableSortName(sortName, true);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        nameTypeLabel = new JLabel();
        nameType = new JLabel();
        label_nameFamilyName = new JLabel();
        nameFamilyName = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(Names.PROPERTYNAME_FAMILY_NAME));
        label_nameFamilyNamePrefix = new JLabel();
        nameFamilyNamePrefix = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(Names.PROPERTYNAME_FAMILY_NAME_PREFIX));
        label_nameFamilyQualifier = new JLabel();
        nameFamilyQualifier = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(Names.PROPERTYNAME_QUALIFIER));
        label_nameSource = new JLabel();
        nameSource = ATBasicComponentFactory.createComboBox(parentPresentationModel, Names.PROPERTYNAME_NAME_SOURCE, Names.class);
        label_nameRule = new JLabel();
        nameRule = ATBasicComponentFactory.createComboBox(parentPresentationModel, Names.PROPERTYNAME_NAME_RULE, Names.class);
        separator3 = new JSeparator();
        panel4 = new JPanel();
        label_sortName2 = new JLabel();
        sortName = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(Names.PROPERTYNAME_SORT_NAME));
        namePersonalDirectOrder2 = ATBasicComponentFactory.createCheckBox(parentPresentationModel, Names.PROPERTYNAME_CREATE_SORT_NAME_AUTOMATICALLY, Names.class);
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBackground(new Color(231, 188, 251));
        setOpaque(false);
        setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
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

        //---- label_nameFamilyName ----
        label_nameFamilyName.setText("Family Name");
        label_nameFamilyName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_nameFamilyName, Names.class, Names.PROPERTYNAME_FAMILY_NAME);
        add(label_nameFamilyName, cc.xy(1, 3));

        //---- nameFamilyName ----
        nameFamilyName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                sortNameConstruction(e);
            }
        });
        add(nameFamilyName, cc.xy(3, 3));

        //---- label_nameFamilyNamePrefix ----
        label_nameFamilyNamePrefix.setText("Prefix");
        label_nameFamilyNamePrefix.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_nameFamilyNamePrefix, Names.class, Names.PROPERTYNAME_FAMILY_NAME_PREFIX);
        add(label_nameFamilyNamePrefix, cc.xy(1, 5));

        //---- nameFamilyNamePrefix ----
        nameFamilyNamePrefix.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                sortNameConstruction(e);
            }
        });
        add(nameFamilyNamePrefix, cc.xy(3, 5));

        //---- label_nameFamilyQualifier ----
        label_nameFamilyQualifier.setText("Qualifier");
        label_nameFamilyQualifier.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_nameFamilyQualifier, Names.class, Names.PROPERTYNAME_QUALIFIER);
        add(label_nameFamilyQualifier, cc.xy(1, 7));

        //---- nameFamilyQualifier ----
        nameFamilyQualifier.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                sortNameConstruction(e);
            }
        });
        add(nameFamilyQualifier, cc.xy(3, 7));

        //---- label_nameSource ----
        label_nameSource.setText("Source");
        label_nameSource.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_nameSource, Names.class, Names.PROPERTYNAME_NAME_SOURCE);
        add(label_nameSource, cc.xy(1, 9));

        //---- nameSource ----
        nameSource.setOpaque(false);
        nameSource.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortNameConstruction(e);
            }
        });
        add(nameSource, cc.xywh(3, 9, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- label_nameRule ----
        label_nameRule.setText("Rules");
        label_nameRule.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_nameRule, Names.class, Names.PROPERTYNAME_NAME_RULE);
        add(label_nameRule, cc.xy(1, 11));

        //---- nameRule ----
        nameRule.setOpaque(false);
        nameRule.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortNameConstruction(e);
            }
        });
        add(nameRule, cc.xywh(3, 11, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- separator3 ----
        separator3.setBackground(new Color(220, 220, 232));
        separator3.setForeground(new Color(147, 131, 86));
        separator3.setMinimumSize(new Dimension(1, 10));
        separator3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(separator3, cc.xywh(1, 13, 3, 1));

        //======== panel4 ========
        {
            panel4.setOpaque(false);
            panel4.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC
                },
                RowSpec.decodeSpecs("default")));

            //---- label_sortName2 ----
            label_sortName2.setText("Sort Name");
            label_sortName2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label_sortName2, Names.class, Names.PROPERTYNAME_SORT_NAME);
            panel4.add(label_sortName2, cc.xy(1, 1));
            panel4.add(sortName, cc.xy(3, 1));

            //---- namePersonalDirectOrder2 ----
            namePersonalDirectOrder2.setText("Create automatically");
            namePersonalDirectOrder2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            namePersonalDirectOrder2.setOpaque(false);
            namePersonalDirectOrder2.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    createAutomaticallyStateChanged(e);
                }
            });
            namePersonalDirectOrder2.setText(ATFieldInfo.getLabel(Names.class, Names.PROPERTYNAME_CREATE_SORT_NAME_AUTOMATICALLY));
            panel4.add(namePersonalDirectOrder2, cc.xy(5, 1));
        }
        add(panel4, cc.xywh(1, 15, 3, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	private void sortNameConstruction(FocusEvent e) {
		((Names)this.getModel()).createSortName(sortName);
	}

	private void sortNameConstruction(ActionEvent e) {
		if (this.getModel() != null) {
			((Names)this.getModel()).createSortName(sortName);
		}
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel nameTypeLabel;
    private JLabel nameType;
    private JLabel label_nameFamilyName;
    public JTextField nameFamilyName;
    private JLabel label_nameFamilyNamePrefix;
    public JTextField nameFamilyNamePrefix;
    private JLabel label_nameFamilyQualifier;
    public JTextField nameFamilyQualifier;
    private JLabel label_nameSource;
    public JComboBox nameSource;
    private JLabel label_nameRule;
    public JComboBox nameRule;
    private JSeparator separator3;
    private JPanel panel4;
    private JLabel label_sortName2;
    public JTextField sortName;
    public JCheckBox namePersonalDirectOrder2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	private PresentationModel parentPresentationModel;

	public final void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		parentPresentationModel.setBean(model);
		this.nameType.setText(((Names)this.getModel()).getNameType());
		enableDisableSortName(sortName, false);
	}

	public Component getInitialFocusComponent() {
		return nameFamilyName;
	}
}
