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
 * Created by JFormDesigner on Wed Apr 19 09:26:06 EDT 2006
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import org.archiviststoolkit.model.Locations;
import org.archiviststoolkit.model.Users;
import org.archiviststoolkit.model.Repositories;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.structure.ATFieldInfo;

public class LocationFields extends DomainEditorFields {
	public LocationFields() {
		super();
		initComponents();
        hideRepositoryPopupIfNecessary();
    }

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label1 = new JLabel();
        building = ATBasicComponentFactory.createTextField(detailsModel.getModel(Locations.PROPERTYNAME_BUILDING));
        label2 = new JLabel();
        floor = ATBasicComponentFactory.createTextField(detailsModel.getModel(Locations.PROPERTYNAME_FLOOR));
        label3 = new JLabel();
        room = ATBasicComponentFactory.createTextField(detailsModel.getModel(Locations.PROPERTYNAME_ROOM));
        label4 = new JLabel();
        area = ATBasicComponentFactory.createTextField(detailsModel.getModel(Locations.PROPERTYNAME_AREA));
        separator2 = new JSeparator();
        label5 = new JLabel();
        coordinate1 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Locations.PROPERTYNAME_COORDINATE_1_LABEL));
        label_subjectSource2 = new JLabel();
        container1Value2 = ATBasicComponentFactory.createDoubleField(detailsModel,Locations.PROPERTYNAME_COORDINATE_1_NUMERIC_INDICATOR);
        label_subjectSource6 = new JLabel();
        containerLabel5 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Locations.PROPERTYNAME_COORDINATE_1_ALPHA_NUMERIC_INDICATOR));
        separator3 = new JSeparator();
        label6 = new JLabel();
        coordinate2 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Locations.PROPERTYNAME_COORDINATE_2_LABEL));
        label_subjectSource3 = new JLabel();
        container1Value3 = ATBasicComponentFactory.createDoubleField(detailsModel,Locations.PROPERTYNAME_COORDINATE_2_NUMERIC_INDICATOR);
        label_subjectSource7 = new JLabel();
        containerLabel6 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Locations.PROPERTYNAME_COORDINATE_2_ALPHA_NUMERIC_INDICATOR));
        separator4 = new JSeparator();
        label7 = new JLabel();
        coordinate3 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Locations.PROPERTYNAME_COORDINATE_3_LABEL));
        label_subjectSource4 = new JLabel();
        container1Value4 = ATBasicComponentFactory.createDoubleField(detailsModel,Locations.PROPERTYNAME_COORDINATE_3_NUMERIC_INDICATOR);
        label_subjectSource8 = new JLabel();
        containerLabel7 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Locations.PROPERTYNAME_COORDINATE_3_ALPHA_NUMERIC_INDICATOR));
        separator5 = new JSeparator();
        label8 = new JLabel();
        classificationNumber = ATBasicComponentFactory.createTextField(detailsModel.getModel(Locations.PROPERTYNAME_CLASSIFICATION_NUMBER));
        label9 = new JLabel();
        barcode = ATBasicComponentFactory.createTextField(detailsModel.getModel(Locations.PROPERTYNAME_BARCODE));
        label10 = new JLabel();
        repository = ATBasicComponentFactory.createComboBox(detailsModel, Locations.PROPERTYNAME_REPOSITORY, Repositories.getRepositoryList());
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBorder(Borders.DLU4_BORDER);
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

        //---- label1 ----
        label1.setText("Building");
        label1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label1, Locations.class, Locations.PROPERTYNAME_BUILDING);
        add(label1, cc.xy(1, 1));
        add(building, cc.xy(3, 1));

        //---- label2 ----
        label2.setText("Floor");
        label2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label2, Locations.class, Locations.PROPERTYNAME_FLOOR);
        add(label2, cc.xy(1, 3));
        add(floor, cc.xy(3, 3));

        //---- label3 ----
        label3.setText("Room");
        label3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label3, Locations.class, Locations.PROPERTYNAME_ROOM);
        add(label3, cc.xy(1, 5));
        add(room, cc.xy(3, 5));

        //---- label4 ----
        label4.setText("Area");
        label4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label4, Locations.class, Locations.PROPERTYNAME_AREA);
        add(label4, cc.xy(1, 7));
        add(area, cc.xy(3, 7));

        //---- separator2 ----
        separator2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        separator2.setForeground(new Color(147, 131, 86));
        add(separator2, cc.xywh(1, 9, 3, 1));

        //---- label5 ----
        label5.setText("Coordinate 1 Label");
        label5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label5, Locations.class, Locations.PROPERTYNAME_COORDINATE_1_LABEL);
        add(label5, cc.xy(1, 11));
        add(coordinate1, cc.xy(3, 11));

        //---- label_subjectSource2 ----
        label_subjectSource2.setText("Coordinate 1 Numeric Indicator");
        label_subjectSource2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectSource2, Locations.class, Locations.PROPERTYNAME_COORDINATE_1_NUMERIC_INDICATOR);
        add(label_subjectSource2, cc.xy(1, 13));

        //---- container1Value2 ----
        container1Value2.setColumns(5);
        add(container1Value2, cc.xywh(3, 13, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- label_subjectSource6 ----
        label_subjectSource6.setText("Coordinate 1 Alphanumeric Indicator");
        label_subjectSource6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectSource6, Locations.class, Locations.PROPERTYNAME_COORDINATE_1_ALPHA_NUMERIC_INDICATOR);
        add(label_subjectSource6, cc.xy(1, 15));
        add(containerLabel5, cc.xy(3, 15));

        //---- separator3 ----
        separator3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        separator3.setForeground(new Color(147, 131, 86));
        add(separator3, cc.xywh(1, 17, 3, 1));

        //---- label6 ----
        label6.setText("Coordinate 2 Label");
        label6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label6, Locations.class, Locations.PROPERTYNAME_COORDINATE_2_LABEL);
        add(label6, cc.xy(1, 19));
        add(coordinate2, cc.xy(3, 19));

        //---- label_subjectSource3 ----
        label_subjectSource3.setText("Coordinate 2 Numeric Indicator");
        label_subjectSource3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectSource3, Locations.class, Locations.PROPERTYNAME_COORDINATE_2_NUMERIC_INDICATOR);
        add(label_subjectSource3, cc.xy(1, 21));

        //---- container1Value3 ----
        container1Value3.setColumns(5);
        add(container1Value3, cc.xywh(3, 21, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- label_subjectSource7 ----
        label_subjectSource7.setText("Coordinate 2 Alphanumeric Indicator");
        label_subjectSource7.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectSource7, Locations.class, Locations.PROPERTYNAME_COORDINATE_2_ALPHA_NUMERIC_INDICATOR);
        add(label_subjectSource7, cc.xy(1, 23));
        add(containerLabel6, cc.xy(3, 23));

        //---- separator4 ----
        separator4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        separator4.setForeground(new Color(147, 131, 86));
        add(separator4, cc.xywh(1, 25, 3, 1));

        //---- label7 ----
        label7.setText("Coordinate 3 Label");
        label7.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label7, Locations.class, Locations.PROPERTYNAME_COORDINATE_3_LABEL);
        add(label7, cc.xy(1, 27));
        add(coordinate3, cc.xy(3, 27));

        //---- label_subjectSource4 ----
        label_subjectSource4.setText("Coordinate 3 Numeric Indicator");
        label_subjectSource4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectSource4, Locations.class, Locations.PROPERTYNAME_COORDINATE_3_NUMERIC_INDICATOR);
        add(label_subjectSource4, cc.xy(1, 29));

        //---- container1Value4 ----
        container1Value4.setColumns(5);
        add(container1Value4, cc.xywh(3, 29, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- label_subjectSource8 ----
        label_subjectSource8.setText("Coordinate 3 Alphanumeric Indicator");
        label_subjectSource8.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectSource8, Locations.class, Locations.PROPERTYNAME_COORDINATE_3_ALPHA_NUMERIC_INDICATOR);
        add(label_subjectSource8, cc.xy(1, 31));
        add(containerLabel7, cc.xy(3, 31));

        //---- separator5 ----
        separator5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        separator5.setForeground(new Color(147, 131, 86));
        add(separator5, cc.xywh(1, 33, 3, 1));

        //---- label8 ----
        label8.setText("Classification #");
        label8.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label8, Locations.class, Locations.PROPERTYNAME_CLASSIFICATION_NUMBER);
        add(label8, cc.xy(1, 35));
        add(classificationNumber, cc.xy(3, 35));

        //---- label9 ----
        label9.setText("Barcode");
        label9.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label9, Locations.class, Locations.PROPERTYNAME_BARCODE);
        add(label9, cc.xy(1, 37));
        add(barcode, cc.xy(3, 37));

        //---- label10 ----
        label10.setText("Repository");
        label10.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label10, Locations.class, Locations.PROPERTYNAME_REPOSITORY);
        add(label10, cc.xy(1, 39));

        //---- repository ----
        repository.setOpaque(false);
        repository.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(repository, cc.xy(3, 39));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label1;
    public JTextField building;
    private JLabel label2;
    public JTextField floor;
    private JLabel label3;
    public JTextField room;
    private JLabel label4;
    public JTextField area;
    private JSeparator separator2;
    private JLabel label5;
    public JTextField coordinate1;
    private JLabel label_subjectSource2;
    public JTextField container1Value2;
    private JLabel label_subjectSource6;
    public JTextField containerLabel5;
    private JSeparator separator3;
    private JLabel label6;
    public JTextField coordinate2;
    private JLabel label_subjectSource3;
    public JTextField container1Value3;
    private JLabel label_subjectSource7;
    public JTextField containerLabel6;
    private JSeparator separator4;
    private JLabel label7;
    public JTextField coordinate3;
    private JLabel label_subjectSource4;
    public JTextField container1Value4;
    private JLabel label_subjectSource8;
    public JTextField containerLabel7;
    private JSeparator separator5;
    private JLabel label8;
    public JTextField classificationNumber;
    private JLabel label9;
    public JTextField barcode;
    private JLabel label10;
    public JComboBox repository;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
    private JTextField repositoryTextField = new JTextField();

    /**
     * Set the domain model for this editor.
     *
     * @param model the model
     */

    @Override
    public void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
        super.setModel(model, progressPanel);
        Locations locationModel = (Locations)model;
        if (locationModel.getRepository() != null) {
            repositoryTextField.setText(locationModel.getRepository().getRepositoryName());
        }
    }

	public Component getInitialFocusComponent() {
		return building;
	}

	private void hideRepositoryPopupIfNecessary() {
		if (!Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_SUPERUSER)) {
			repositoryTextField.setOpaque(false);
			repositoryTextField.setEditable(false);
			FormLayout formLayout = (FormLayout) this.getLayout();
			CellConstraints cc = formLayout.getConstraints(repository);
			this.remove(repository);
			this.add(repositoryTextField, cc);

		}
	}
}
