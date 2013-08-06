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
 * Created by JFormDesigner on Tue Mar 14 08:54:41 PST 2006
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import java.util.Vector;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.model.ArchDescriptionAnalogInstances;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.mydomain.DomainEditor;
import org.archiviststoolkit.util.SequencedObjectsUtils;
import org.archiviststoolkit.structure.ATFieldInfo;

public class ArchDescriptionAnalogInstancesFields extends DomainEditorFields {

	protected ArchDescriptionAnalogInstancesFields(DomainEditor parent) {
		super();
		this.setParentEditor(parent);
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label_subjectTermType = new JLabel();
        container1Type = ATBasicComponentFactory.createComboBox(detailsModel, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER1_TYPE, ArchDescriptionAnalogInstances.class);
        label_subjectSource5 = new JLabel();
        containerLabel4 = ATBasicComponentFactory.createTextField(detailsModel.getModel(ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER1_INDICATOR));
        label_subjectSource4 = new JLabel();
        containerLabel3 = ATBasicComponentFactory.createTextField(detailsModel.getModel(ArchDescriptionAnalogInstances.PROPERTYNAME_BARCODE));
        separator1 = new JSeparator();
        label_subjectTermType2 = new JLabel();
        container2Type = ATBasicComponentFactory.createComboBox(detailsModel, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER2_TYPE, ArchDescriptionAnalogInstances.class);
        label_subjectSource6 = new JLabel();
        containerLabel5 = ATBasicComponentFactory.createTextField(detailsModel.getModel(ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER2_INDICATOR));
        separator3 = new JSeparator();
        label_subjectTermType3 = new JLabel();
        container3Type = ATBasicComponentFactory.createComboBox(detailsModel, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER3_TYPE, ArchDescriptionAnalogInstances.class);
        label_subjectSource7 = new JLabel();
        containerLabel6 = ATBasicComponentFactory.createTextField(detailsModel.getModel(ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER3_INDICATOR));
        separator2 = new JSeparator();
        label_subjectTerm2 = new JLabel();
        containerLabel2 = ATBasicComponentFactory.createTextField(detailsModel.getModel(ArchDescriptionAnalogInstances.PROPERTYNAME_LOCATION_LABEL));
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

        //---- label_subjectTermType ----
        label_subjectTermType.setText("Container 1 Type");
        label_subjectTermType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectTermType, ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER1_TYPE);
        add(label_subjectTermType, cc.xy(1, 1));

        //---- container1Type ----
        container1Type.setOpaque(false);
        container1Type.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(container1Type, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- label_subjectSource5 ----
        label_subjectSource5.setText("Container 1 Indicator");
        label_subjectSource5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectSource5, ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER1_INDICATOR);
        add(label_subjectSource5, cc.xy(1, 3));
        add(containerLabel4, cc.xy(3, 3));

        //---- label_subjectSource4 ----
        label_subjectSource4.setText("Barcode");
        label_subjectSource4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectSource4, ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_BARCODE);
        add(label_subjectSource4, cc.xy(1, 5));
        add(containerLabel3, cc.xy(3, 5));

        //---- separator1 ----
        separator1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        separator1.setForeground(new Color(147, 131, 86));
        add(separator1, cc.xywh(1, 7, 3, 1));

        //---- label_subjectTermType2 ----
        label_subjectTermType2.setText("Container 2 Type");
        label_subjectTermType2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectTermType2, ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER2_TYPE);
        add(label_subjectTermType2, cc.xy(1, 9));

        //---- container2Type ----
        container2Type.setOpaque(false);
        container2Type.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(container2Type, cc.xywh(3, 9, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- label_subjectSource6 ----
        label_subjectSource6.setText("Container 2  Indicator");
        label_subjectSource6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectSource6, ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER2_INDICATOR);
        add(label_subjectSource6, cc.xy(1, 11));
        add(containerLabel5, cc.xy(3, 11));

        //---- separator3 ----
        separator3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        separator3.setForeground(new Color(147, 131, 86));
        add(separator3, cc.xywh(1, 13, 3, 1));

        //---- label_subjectTermType3 ----
        label_subjectTermType3.setText("Container 3 Type");
        label_subjectTermType3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectTermType3, ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER3_TYPE);
        add(label_subjectTermType3, cc.xy(1, 15));

        //---- container3Type ----
        container3Type.setOpaque(false);
        container3Type.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(container3Type, cc.xywh(3, 15, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- label_subjectSource7 ----
        label_subjectSource7.setText("Container 3  Indicator");
        label_subjectSource7.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectSource7, ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER3_INDICATOR);
        add(label_subjectSource7, cc.xy(1, 17));
        add(containerLabel6, cc.xy(3, 17));

        //---- separator2 ----
        separator2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        separator2.setForeground(new Color(147, 131, 86));
        add(separator2, cc.xywh(1, 19, 3, 1));

        //---- label_subjectTerm2 ----
        label_subjectTerm2.setText("Location");
        label_subjectTerm2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectTerm2, ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_LOCATION);
        add(label_subjectTerm2, cc.xy(1, 21));

        //---- containerLabel2 ----
        containerLabel2.setEditable(false);
        containerLabel2.setOpaque(false);
        containerLabel2.setBorder(null);
        add(containerLabel2, cc.xy(3, 21));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label_subjectTermType;
    public JComboBox container1Type;
    private JLabel label_subjectSource5;
    public JTextField containerLabel4;
    private JLabel label_subjectSource4;
    public JTextField containerLabel3;
    private JSeparator separator1;
    private JLabel label_subjectTermType2;
    public JComboBox container2Type;
    private JLabel label_subjectSource6;
    public JTextField containerLabel5;
    private JSeparator separator3;
    private JLabel label_subjectTermType3;
    public JComboBox container3Type;
    private JLabel label_subjectSource7;
    public JTextField containerLabel6;
    private JSeparator separator2;
    private JLabel label_subjectTerm2;
    public JTextField containerLabel2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public Component getInitialFocusComponent() {
		return container1Type;
	}
}
