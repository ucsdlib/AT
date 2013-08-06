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

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.ArrayList;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.DefaultValues;
import org.archiviststoolkit.structure.DatabaseFields;
import org.archiviststoolkit.model.ResourcesComponents;
import org.archiviststoolkit.model.RDEScreenPanelItems;
import org.archiviststoolkit.exceptions.RDEPopulateException;
import org.archiviststoolkit.exceptions.RDEPanelCreationException;
import org.archiviststoolkit.swing.ATBasicComponentFactory;

public class RdeTextArea extends RdePanel {

	private boolean sticky = false;

	public RdeTextArea(RdePanelContainer parentPanel, ATFieldInfo fieldInfo) {
		super(parentPanel, fieldInfo);
		initComponents();
		if (fieldInfo != null) {
			this.label.setText(fieldInfo.getFieldLabel());
		}
	}

	public void populateComponent(ResourcesComponents component) throws RDEPopulateException {
		try {
			fieldInfo.getSetMethod().invoke(component, textArea.getText());
		} catch (IllegalAccessException e) {
			throw new RDEPopulateException(e);
		} catch (InvocationTargetException e) {
			throw new RDEPopulateException(e);
		}
	}

	public void clearFields() {
		if (!sticky) {
			textArea.setText("");
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
            textArea.setText(defaultValue.getTextValue());
        }
        showDefaultValues = false;
    }

    private void labelMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			sticky = !sticky;
			setStickyLabels();
		}
	}

	private void textAreaFocusGained(FocusEvent e) {
		parentPanel.componentFocusGained(e, this);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        separator3 = new JSeparator();
        label = new JLabel();
        scrollPane1 = new JScrollPane();
        textArea = ATBasicComponentFactory.createUnboundedTextArea();
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
                new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
                FormFactory.LINE_GAP_ROWSPEC,
                new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW)
            }));

        //---- separator3 ----
        separator3.setBackground(new Color(220, 220, 232));
        separator3.setForeground(new Color(147, 131, 86));
        separator3.setMinimumSize(new Dimension(1, 10));
        separator3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        separator3.setOpaque(true);
        add(separator3, cc.xywh(1, 1, 3, 1, CellConstraints.DEFAULT, CellConstraints.CENTER));

        //---- label ----
        label.setText("Label");
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                labelMouseClicked(e);
            }
        });
        add(label, cc.xy(1, 3));

        //======== scrollPane1 ========
        {
            scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            //---- textArea ----
            textArea.setRows(4);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    textAreaFocusGained(e);
                }
            });
            scrollPane1.setViewportView(textArea);
        }
        add(scrollPane1, cc.xywh(3, 3, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JSeparator separator3;
    private JLabel label;
    private JScrollPane scrollPane1;
    private JTextArea textArea;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}