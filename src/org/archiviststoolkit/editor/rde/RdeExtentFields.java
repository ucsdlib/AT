/*
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
 * Created by JFormDesigner on Fri Jul 18 10:57:13 EDT 2008
 */

package org.archiviststoolkit.editor.rde;

import java.awt.event.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;
import java.util.Collection;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.DefaultValues;
import org.archiviststoolkit.model.ResourcesComponents;
import org.archiviststoolkit.model.LookupListItems;
import org.archiviststoolkit.model.RDEScreenPanelItems;
import org.archiviststoolkit.exceptions.RDEPopulateException;
import org.archiviststoolkit.exceptions.RDEPanelCreationException;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.util.LookupListUtils;

public class RdeExtentFields extends RdePanel {

	private ATFieldInfo extentNumberFieldInfo;
	private ATFieldInfo extentMeasurementFieldInfo;
	private boolean extentNumberSticky = false;
	private boolean extentMeasurementSticky = false;

	public RdeExtentFields(RdePanelContainer parentPanel, ATFieldInfo extentNumberFieldInfo, ATFieldInfo extentMeasurementFieldInfo) {
		super(parentPanel);
		this.extentNumberFieldInfo = extentNumberFieldInfo;
		this.extentMeasurementFieldInfo = extentMeasurementFieldInfo;
		initComponents();
		if (this.extentNumberFieldInfo != null) {
			this.label_extentNumber.setText(this.extentNumberFieldInfo.getFieldLabel());
		}
		if (this.extentMeasurementFieldInfo != null) {
			this.label_extentMeasurement.setText(this.extentMeasurementFieldInfo.getFieldLabel());
			Vector<LookupListItems> values = LookupListUtils.getLookupListValues2(this.extentMeasurementFieldInfo.getLookupList());
			this.extentMeasurement.setModel(new DefaultComboBoxModel(values));
		}
	}

	public void populateComponent(ResourcesComponents component) throws RDEPopulateException {
		try {
			if (extentNumber.getValue() != null) {
				extentNumberFieldInfo.getSetMethod().invoke(component, (Double)extentNumber.getValue());
			}
			String selectedValue = ((LookupListItems)extentMeasurement.getSelectedItem()).getListItem();
			extentMeasurementFieldInfo.getSetMethod().invoke(component, selectedValue);

		} catch (IllegalAccessException e) {
			throw new RDEPopulateException(e);
		} catch (InvocationTargetException e) {
			throw new RDEPopulateException(e);
		}
	}

	public void clearFields() {
		if (!extentNumberSticky) {
			extentNumber.setText("");
		}
		if (!extentMeasurementSticky) {
			extentMeasurement.setSelectedIndex(0);
		}
	}

	public void setStickyLabels() {
		setLabelColor(extentNumberSticky, label_extentNumber);
		setLabelColor(extentMeasurementSticky, label_extentMeasurement);
	}

	public void initializeStickyLabels(Collection<RDEScreenPanelItems> panelItems) throws RDEPanelCreationException {
		for(RDEScreenPanelItems panelItem: panelItems) {
			if (panelItem.getPropertyName().equals(ResourcesComponents.PROPERTYNAME_EXTENT_NUMBER)) {
				this.extentNumberSticky = panelItem.getSticky();
			} else if (panelItem.getPropertyName().equals(ResourcesComponents.PROPERTYNAME_EXTENT_TYPE)){
				this.extentMeasurementSticky = panelItem.getSticky();
			} else {
				throw new RDEPanelCreationException(panelItem.getPropertyName() + "is not supported here");
			}
		}
	}

    // Method to set the default value
    public void setDefaultValues() {
        DefaultValues defaultValue1 = getDefaultValue(extentNumberFieldInfo);
        DefaultValues defaultValue2 = getDefaultValue(extentMeasurementFieldInfo);

        if(defaultValue1 != null && (!extentNumberSticky || showDefaultValues)) { // not used for now
           extentNumber.setText(defaultValue1.getStringValue());
        }

        if(defaultValue2 != null && (!extentMeasurementSticky || showDefaultValues)) {
            String value = defaultValue2.getStringValue();
            int size = extentMeasurement.getItemCount();

            for (int i = 1; i < size; i++) {
                Object o = extentMeasurement.getItemAt(i);
                if (o.toString().equals(value)) {
                    extentMeasurement.setSelectedIndex(i);
                    break;
                }
            }
        }

        showDefaultValues = false;
    }

    private void extentNumberFocusGained(FocusEvent e) {
		parentPanel.componentFocusGained(e, this);
	}

	private void extentMeasurementFocusGained(FocusEvent e) {
		parentPanel.componentFocusGained(e, this);
	}

	private void label_extentNumberMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			extentNumberSticky = !extentNumberSticky;
			setStickyLabels();
		}
	}

	private void label_extentMeasurementMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			extentMeasurementSticky = !extentMeasurementSticky;
			setStickyLabels();
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        separator3 = new JSeparator();
        label_extentNumber = new JLabel();
        extentNumber = ATBasicComponentFactory.createUnboundDoubleField();
        label_extentMeasurement = new JLabel();
        extentMeasurement = new JComboBox();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBackground(new Color(200, 205, 232));
        setLayout(new FormLayout(
            new ColumnSpec[] {
                new ColumnSpec(ColumnSpec.LEFT, Sizes.DEFAULT, FormSpec.NO_GROW),
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
            },
            new RowSpec[] {
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC
            }));

        //---- separator3 ----
        separator3.setBackground(new Color(220, 220, 232));
        separator3.setForeground(new Color(147, 131, 86));
        separator3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(separator3, cc.xywh(1, 1, 9, 1));

        //---- label_extentNumber ----
        label_extentNumber.setText("Extent Number");
        label_extentNumber.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                label_extentNumberMouseClicked(e);
            }
        });
        add(label_extentNumber, cc.xy(1, 3));

        //---- extentNumber ----
        extentNumber.setColumns(5);
        extentNumber.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                extentNumberFocusGained(e);
            }
        });
        add(extentNumber, cc.xywh(3, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- label_extentMeasurement ----
        label_extentMeasurement.setText("Extent Measurement");
        label_extentMeasurement.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                label_extentMeasurementMouseClicked(e);
            }
        });
        add(label_extentMeasurement, cc.xy(5, 3));

        //---- extentMeasurement ----
        extentMeasurement.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                extentMeasurementFocusGained(e);
            }
        });
        add(extentMeasurement, cc.xywh(7, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}


	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JSeparator separator3;
    private JLabel label_extentNumber;
    private JFormattedTextField extentNumber;
    private JLabel label_extentMeasurement;
    private JComboBox extentMeasurement;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}