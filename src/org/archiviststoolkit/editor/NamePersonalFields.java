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
 * Created by JFormDesigner on Sun Aug 28 15:50:56 EDT 2005
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.structure.ATFieldInfo;

public class NamePersonalFields extends NamePrimaryNameFields {
	public NamePersonalFields(PresentationModel parentPresentationModel) {
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
		return rightSidePanel;
	}

	private void createAutomaticallyStateChanged(ChangeEvent e) {
		enableDisableSortName(sortName, true);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        panel3 = new JPanel();
        nameTypeLabel = new JLabel();
        nameType = new JLabel();
        panel1 = new JPanel();
        label_namePersonalPrefix = new JLabel();
        namePersonalPrefix = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(Names.PROPERTYNAME_PERSONAL_PREFIX));
        label_namePersonalPrimaryName = new JLabel();
        namePersonalPrimaryName = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(Names.PROPERTYNAME_PERSONAL_PRIMARY_NAME));
        label_namePersonalRestOfName = new JLabel();
        namePersonalRestOfName = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(Names.PROPERTYNAME_PERSONAL_REST_OF_NAME));
        label_namePersonalSuffix2 = new JLabel();
        namePersonalTitle = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(Names.PROPERTYNAME_PERSONAL_TITLE));
        label_namePersonalSuffix = new JLabel();
        namePersonalSuffix = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(Names.PROPERTYNAME_PERSONAL_SUFFIX));
        label_namePersonalNumber = new JLabel();
        namePersonalNumber = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(Names.PROPERTYNAME_NUMBER));
        separator1 = new JSeparator();
        rightSidePanel = new JPanel();
        label_namePersonalDates = new JLabel();
        namePersonalDates = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(Names.PROPERTYNAME_PERSONAL_DATES));
        label_namePersonalQualifier = new JLabel();
        namePersonalQualifier = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(Names.PROPERTYNAME_QUALIFIER));
        label_namePersonalFullerForm = new JLabel();
        namePersonalFullerForm = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(Names.PROPERTYNAME_PERSONAL_FULLER_FORM));
        label_nameSource = new JLabel();
        nameSource = ATBasicComponentFactory.createComboBox(parentPresentationModel, Names.PROPERTYNAME_NAME_SOURCE, Names.class);
        label_nameRule = new JLabel();
        nameRule = ATBasicComponentFactory.createComboBox(parentPresentationModel, Names.PROPERTYNAME_NAME_RULE, Names.class);
        namePersonalDirectOrder = ATBasicComponentFactory.createCheckBox(parentPresentationModel, Names.PROPERTYNAME_PERSONAL_DIRECT_ORDER, Names.class);
        separator3 = new JSeparator();
        panel4 = new JPanel();
        label_sortName = new JLabel();
        sortName = ATBasicComponentFactory.createTextField(parentPresentationModel.getModel(Names.PROPERTYNAME_SORT_NAME));
        namePersonalDirectOrder2 = ATBasicComponentFactory.createCheckBox(parentPresentationModel, Names.PROPERTYNAME_CREATE_SORT_NAME_AUTOMATICALLY, Names.class);
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBackground(new Color(200, 205, 232));
        setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        setLayout(new FormLayout(
            new ColumnSpec[] {
                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                FormFactory.RELATED_GAP_COLSPEC,
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
            },
            new RowSpec[] {
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC
            }));
        ((FormLayout)getLayout()).setColumnGroups(new int[][] {{1, 5}});

        //======== panel3 ========
        {
            panel3.setOpaque(false);
            panel3.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                },
                RowSpec.decodeSpecs("default")));

            //---- nameTypeLabel ----
            nameTypeLabel.setText("Name Identity Record:");
            nameTypeLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            panel3.add(nameTypeLabel, cc.xy(1, 1));

            //---- nameType ----
            nameType.setText("nameType");
            nameType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            panel3.add(nameType, cc.xy(3, 1));
        }
        add(panel3, cc.xywh(1, 1, 5, 1));

        //======== panel1 ========
        {
            panel1.setOpaque(false);
            panel1.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
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

            //---- label_namePersonalPrefix ----
            label_namePersonalPrefix.setText("Prefix");
            label_namePersonalPrefix.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label_namePersonalPrefix, Names.class, Names.PROPERTYNAME_PERSONAL_PREFIX);
            panel1.add(label_namePersonalPrefix, cc.xy(1, 1));

            //---- namePersonalPrefix ----
            namePersonalPrefix.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    sortNameConstruction(e);
                }
            });
            panel1.add(namePersonalPrefix, cc.xy(3, 1));

            //---- label_namePersonalPrimaryName ----
            label_namePersonalPrimaryName.setText("Primary Name");
            label_namePersonalPrimaryName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label_namePersonalPrimaryName, Names.class, Names.PROPERTYNAME_PERSONAL_PRIMARY_NAME);
            panel1.add(label_namePersonalPrimaryName, cc.xy(1, 3));

            //---- namePersonalPrimaryName ----
            namePersonalPrimaryName.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    sortNameConstruction(e);
                }
            });
            panel1.add(namePersonalPrimaryName, cc.xy(3, 3));

            //---- label_namePersonalRestOfName ----
            label_namePersonalRestOfName.setText("Rest of Name");
            label_namePersonalRestOfName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label_namePersonalRestOfName, Names.class, Names.PROPERTYNAME_PERSONAL_REST_OF_NAME);
            panel1.add(label_namePersonalRestOfName, cc.xy(1, 5));

            //---- namePersonalRestOfName ----
            namePersonalRestOfName.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    sortNameConstruction(e);
                }
            });
            panel1.add(namePersonalRestOfName, cc.xy(3, 5));

            //---- label_namePersonalSuffix2 ----
            label_namePersonalSuffix2.setText("Title");
            label_namePersonalSuffix2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label_namePersonalSuffix2, Names.class, Names.PROPERTYNAME_PERSONAL_TITLE);
            panel1.add(label_namePersonalSuffix2, cc.xy(1, 7));

            //---- namePersonalTitle ----
            namePersonalTitle.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    sortNameConstruction(e);
                }
            });
            panel1.add(namePersonalTitle, cc.xy(3, 7));

            //---- label_namePersonalSuffix ----
            label_namePersonalSuffix.setText("Suffix");
            label_namePersonalSuffix.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label_namePersonalSuffix, Names.class, Names.PROPERTYNAME_PERSONAL_SUFFIX);
            panel1.add(label_namePersonalSuffix, cc.xy(1, 9));

            //---- namePersonalSuffix ----
            namePersonalSuffix.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    sortNameConstruction(e);
                }
            });
            panel1.add(namePersonalSuffix, cc.xy(3, 9));

            //---- label_namePersonalNumber ----
            label_namePersonalNumber.setText("Number");
            label_namePersonalNumber.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label_namePersonalNumber, Names.class, Names.PROPERTYNAME_NUMBER);
            panel1.add(label_namePersonalNumber, cc.xy(1, 11));

            //---- namePersonalNumber ----
            namePersonalNumber.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    sortNameConstruction(e);
                }
            });
            panel1.add(namePersonalNumber, cc.xy(3, 11));
        }
        add(panel1, cc.xy(1, 3));

        //---- separator1 ----
        separator1.setOrientation(SwingConstants.VERTICAL);
        add(separator1, cc.xywh(3, 3, 1, 1, CellConstraints.CENTER, CellConstraints.FILL));

        //======== rightSidePanel ========
        {
            rightSidePanel.setOpaque(false);
            rightSidePanel.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
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

            //---- label_namePersonalDates ----
            label_namePersonalDates.setText("Dates");
            label_namePersonalDates.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label_namePersonalDates, Names.class, Names.PROPERTYNAME_PERSONAL_DATES);
            rightSidePanel.add(label_namePersonalDates, cc.xy(1, 1));

            //---- namePersonalDates ----
            namePersonalDates.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    sortNameConstruction(e);
                }
            });
            rightSidePanel.add(namePersonalDates, cc.xy(3, 1));

            //---- label_namePersonalQualifier ----
            label_namePersonalQualifier.setText("Qualifier");
            label_namePersonalQualifier.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label_namePersonalQualifier, Names.class, Names.PROPERTYNAME_QUALIFIER);
            rightSidePanel.add(label_namePersonalQualifier, cc.xy(1, 3));

            //---- namePersonalQualifier ----
            namePersonalQualifier.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    sortNameConstruction(e);
                }
            });
            rightSidePanel.add(namePersonalQualifier, cc.xy(3, 3));

            //---- label_namePersonalFullerForm ----
            label_namePersonalFullerForm.setText("Fuller Form");
            label_namePersonalFullerForm.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label_namePersonalFullerForm, Names.class, Names.PROPERTYNAME_PERSONAL_FULLER_FORM);
            rightSidePanel.add(label_namePersonalFullerForm, cc.xy(1, 5));

            //---- namePersonalFullerForm ----
            namePersonalFullerForm.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    sortNameConstruction(e);
                }
            });
            rightSidePanel.add(namePersonalFullerForm, cc.xy(3, 5));

            //---- label_nameSource ----
            label_nameSource.setText("Source");
            label_nameSource.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label_nameSource, Names.class, Names.PROPERTYNAME_NAME_SOURCE);
            rightSidePanel.add(label_nameSource, cc.xy(1, 7));

            //---- nameSource ----
            nameSource.setOpaque(false);
            nameSource.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    sortNameConstruction(e);
                }
            });
            rightSidePanel.add(nameSource, cc.xywh(3, 7, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

            //---- label_nameRule ----
            label_nameRule.setText("Rules");
            label_nameRule.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label_nameRule, Names.class, Names.PROPERTYNAME_NAME_RULE);
            rightSidePanel.add(label_nameRule, cc.xy(1, 9));

            //---- nameRule ----
            nameRule.setOpaque(false);
            nameRule.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    sortNameConstruction(e);
                }
            });
            rightSidePanel.add(nameRule, cc.xywh(3, 9, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

            //---- namePersonalDirectOrder ----
            namePersonalDirectOrder.setText("Direct Order");
            namePersonalDirectOrder.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            namePersonalDirectOrder.setOpaque(false);
            namePersonalDirectOrder.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    namePersonalDirectOrderStateChanged(e);
                }
            });
            namePersonalDirectOrder.setText(ATFieldInfo.getLabel(Names.class, Names.PROPERTYNAME_PERSONAL_DIRECT_ORDER));
            rightSidePanel.add(namePersonalDirectOrder, cc.xywh(1, 11, 3, 1));
        }
        add(rightSidePanel, cc.xy(5, 3));

        //---- separator3 ----
        separator3.setBackground(new Color(220, 220, 232));
        separator3.setForeground(new Color(147, 131, 86));
        separator3.setMinimumSize(new Dimension(1, 10));
        separator3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(separator3, cc.xywh(1, 5, 5, 1));

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

            //---- label_sortName ----
            label_sortName.setText("Sort Name");
            label_sortName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label_sortName, Names.class, Names.PROPERTYNAME_SORT_NAME);
            panel4.add(label_sortName, cc.xy(1, 1));
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
        add(panel4, cc.xywh(1, 7, 5, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	private void sortNameConstruction(FocusEvent e) {
		((Names)this.getModel()).createSortName(sortName);
	}
	private void namePersonalDirectOrderStateChanged(ChangeEvent e) {
        if (this.getModel() != null) {
            ((Names)this.getModel()).createSortName(sortName);
        }
	}

	private void sortNameConstruction(ActionEvent e) {
		if (this.getModel() != null) {
			((Names)this.getModel()).createSortName(sortName);
		}
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel panel3;
    private JLabel nameTypeLabel;
    private JLabel nameType;
    private JPanel panel1;
    private JLabel label_namePersonalPrefix;
    public JTextField namePersonalPrefix;
    private JLabel label_namePersonalPrimaryName;
    public JTextField namePersonalPrimaryName;
    private JLabel label_namePersonalRestOfName;
    public JTextField namePersonalRestOfName;
    private JLabel label_namePersonalSuffix2;
    public JTextField namePersonalTitle;
    private JLabel label_namePersonalSuffix;
    public JTextField namePersonalSuffix;
    private JLabel label_namePersonalNumber;
    public JTextField namePersonalNumber;
    private JSeparator separator1;
    private JPanel rightSidePanel;
    private JLabel label_namePersonalDates;
    public JTextField namePersonalDates;
    private JLabel label_namePersonalQualifier;
    public JTextField namePersonalQualifier;
    private JLabel label_namePersonalFullerForm;
    public JTextField namePersonalFullerForm;
    private JLabel label_nameSource;
    public JComboBox nameSource;
    private JLabel label_nameRule;
    public JComboBox nameRule;
    public JCheckBox namePersonalDirectOrder;
    private JSeparator separator3;
    private JPanel panel4;
    private JLabel label_sortName;
    public JTextField sortName;
    public JCheckBox namePersonalDirectOrder2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	private PresentationModel parentPresentationModel;

	public final void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		Names nameModel = (Names)this.getModel();
		parentPresentationModel.setBean(model);
		this.nameType.setText(nameModel.getNameType());
		enableDisableSortName(sortName, false);
	}

	public Component getInitialFocusComponent() {
		return namePersonalPrefix;
	}
}
