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
 * Created by JFormDesigner on Fri Jul 18 13:44:08 EDT 2008
 */

package org.archiviststoolkit.editor.rde;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Collection;
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.DefaultValues;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.model.LookupListItems;
import org.archiviststoolkit.model.ResourcesComponents;
import org.archiviststoolkit.model.RDEScreenPanelItems;
import org.archiviststoolkit.exceptions.RDEPopulateException;
import org.archiviststoolkit.exceptions.RDEPanelCreationException;

public class RdeDropDownList extends RdePanel {

	private boolean sticky = false;

	public RdeDropDownList(RdePanelContainer parentPanel, ATFieldInfo fieldInfo) {
		super(parentPanel, fieldInfo);
		initComponents();
		if (fieldInfo != null) {
			this.label.setText(fieldInfo.getFieldLabel());
			Vector<LookupListItems> values = LookupListUtils.getLookupListValues2(fieldInfo.getLookupList());
			this.comboBox.setModel(new DefaultComboBoxModel(values));
		}
	}

	public void populateComponent(ResourcesComponents component) throws RDEPopulateException {
		if (comboBox.getSelectedIndex() != 0) {
			String selectedValue = ((LookupListItems)comboBox.getSelectedItem()).getListItem();
			try {
				fieldInfo.getSetMethod().invoke(component, selectedValue);
			} catch (IllegalAccessException e) {
				throw new RDEPopulateException(e);
			} catch (InvocationTargetException e) {
				throw new RDEPopulateException(e);
			}
		}
	}

	public void clearFields() {
		if (!sticky) {
			comboBox.setSelectedIndex(0);
		}
	}

	public void setStickyLabels() {
		setLabelColor(sticky, label);
	}

	public void initializeStickyLabels(Collection<RDEScreenPanelItems> panelItems) throws RDEPanelCreationException {
		for(RDEScreenPanelItems panelItem: panelItems) {
			if (panelItem.getPropertyName().equals(this.fieldInfo.getFieldName())) {
				this.sticky = panelItem.getSticky();
			} else {
				throw new RDEPanelCreationException(panelItem.getPropertyName() + "is not supported here");
			}
		}
	}

    // Method to set the default value
    public void setDefaultValues() {
        DefaultValues defaultValue = getDefaultValue();
        if(defaultValue != null && (!sticky || showDefaultValues)) {
            String value = defaultValue.getStringValue();
            int size = comboBox.getItemCount();

            for (int i = 1; i < size; i++) {
                Object o = comboBox.getItemAt(i);
                if (o.toString().indexOf(value) != -1) {
                    comboBox.setSelectedIndex(i);
                    break;
                }
            }
        }

        showDefaultValues = false;
    }

    private void comboBoxFocusGained(FocusEvent e) {
		parentPanel.componentFocusGained(e, this);
	}

	private void labelMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			sticky = !sticky;
			setStickyLabels();
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        separator3 = new JSeparator();
        label = new JLabel();
        comboBox = new JComboBox();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBackground(new Color(200, 205, 232));
        setLayout(new FormLayout(
            new ColumnSpec[] {
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
            },
            new RowSpec[] {
                new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW),
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC
            }));

        //---- separator3 ----
        separator3.setBackground(new Color(220, 220, 232));
        separator3.setForeground(new Color(147, 131, 86));
        separator3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(separator3, cc.xywh(1, 1, 3, 1));

        //---- label ----
        label.setText("Label");
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                labelMouseClicked(e);
            }
        });
        add(label, cc.xy(1, 3));

        //---- comboBox ----
        comboBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                comboBoxFocusGained(e);
            }
        });
        add(comboBox, cc.xywh(3, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JSeparator separator3;
    private JLabel label;
    private JComboBox comboBox;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
