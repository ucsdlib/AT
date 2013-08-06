/*
 * Archivists' Toolkit(TM) Copyright © 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.  
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
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.DefaultValues;
import org.archiviststoolkit.model.ResourcesComponents;
import org.archiviststoolkit.model.RDEScreenPanelItems;
import org.archiviststoolkit.exceptions.RDEPopulateException;
import org.archiviststoolkit.exceptions.RDEPanelCreationException;
import org.archiviststoolkit.swing.ATBasicComponentFactory;

public class RdeYearRangeField extends RdePanel {

	private ATFieldInfo yearBeginFieldInfo;
	private ATFieldInfo yearEndFieldInfo;
	private boolean yearBeginSticky = false;
	private boolean yearEndSticky = false;

	public RdeYearRangeField(RdePanelContainer parentPanel, String mainLabel, ATFieldInfo yearBeginFieldInfo, ATFieldInfo yearEndFieldInfo) {
		super(parentPanel);
		this.yearBeginFieldInfo = yearBeginFieldInfo;
		this.yearEndFieldInfo = yearEndFieldInfo;
		initComponents();
		if (mainLabel != null) {
			this.label.setText(mainLabel);
		}
	}

	public void populateComponent(ResourcesComponents component) throws RDEPopulateException {
		try {
			if (yearBegin.getValue() != null) {
				yearBeginFieldInfo.getSetMethod().invoke(component, (Integer)yearBegin.getValue());
			}
			if (yearEnd.getValue() != null) {
				yearEndFieldInfo.getSetMethod().invoke(component, (Integer)yearEnd.getValue());
			}

		} catch (IllegalAccessException e) {
			throw new RDEPopulateException(e);
		} catch (InvocationTargetException e) {
			throw new RDEPopulateException(e);
		}
	}

	public void clearFields() {
		if (!yearBeginSticky) {
			yearBegin.setValue(null);
		}
		if (!yearEndSticky) {
			yearEnd.setValue(null);
		}
	}

	public void setStickyLabels() {
		setLabelColor(yearBeginSticky, label_yearBegin);
		setLabelColor(yearEndSticky, label_yearEnd);
	}

	public void initializeStickyLabels(Collection<RDEScreenPanelItems> panelItems) throws RDEPanelCreationException {
		for(RDEScreenPanelItems panelItem: panelItems) {
			if (panelItem.getPropertyName().equals(this.yearBeginFieldInfo.getFieldName())) {
				this.yearBeginSticky = panelItem.getSticky();
			} else if (panelItem.getPropertyName().equals(this.yearEndFieldInfo.getFieldName())){
				this.yearEndSticky = panelItem.getSticky();
			} else {
				throw new RDEPanelCreationException(panelItem.getPropertyName() + "is not supported here");
			}
		}
	}

    // Method to set the default value
    public void setDefaultValues() {
        DefaultValues defaultValue1 = getDefaultValue(yearBeginFieldInfo);
        DefaultValues defaultValue2 = getDefaultValue(yearEndFieldInfo);

        if(defaultValue1 != null && (!yearBeginSticky || showDefaultValues)) { // not used for now
            yearBegin.setValue(defaultValue1.getDateValue());
        }

        if(defaultValue2 != null && (!yearEndSticky || showDefaultValues)) { // not used for now
            yearEnd.setValue(defaultValue2.getDateValue());
        }

        showDefaultValues = false;
    }

    private void yearEndFocusGained(FocusEvent e) {
		parentPanel.componentFocusGained(e, this);
		if (yearEnd.getValue() == null) {
			yearEnd.setValue(yearBegin.getValue());
			/* After a formatted text field gains focus, it replaces its text with its
			 * current value, formatted appropriately of course. It does this _after_
			 * any focus listeners are notified. So, if we are editable, we queue
			 * up a selectAll to be done after the current events in the thread are done. */
			Runnable doSelect = new Runnable() {
				public void run() {
					yearEnd.selectAll();
				}
			};
			SwingUtilities.invokeLater(doSelect);
		}
	}

	private void yearBeginFocusGained(FocusEvent e) {
		parentPanel.componentFocusGained(e, this);
	}

	private void label_yearBeginMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			yearBeginSticky = !yearBeginSticky;
			setStickyLabels();
		}
	}

	private void label_yearEndMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			yearEndSticky = !yearEndSticky;
			setStickyLabels();
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        separator3 = new JSeparator();
        label = new JLabel();
        label_yearBegin = new JLabel();
        yearBegin = ATBasicComponentFactory.createUnboundIntegerField(false);
        label_yearEnd = new JLabel();
        yearEnd = ATBasicComponentFactory.createUnboundIntegerField(false);
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBackground(new Color(200, 205, 232));
        setLayout(new FormLayout(
            new ColumnSpec[] {
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
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
        separator3.setOpaque(true);
        add(separator3, cc.xywh(1, 1, 11, 1));

        //---- label ----
        label.setText("Label");
        add(label, cc.xy(1, 3));

        //---- label_yearBegin ----
        label_yearBegin.setText("Begin");
        label_yearBegin.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        label_yearBegin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                label_yearBeginMouseClicked(e);
            }
        });
        add(label_yearBegin, cc.xy(3, 3));

        //---- yearBegin ----
        yearBegin.setColumns(5);
        yearBegin.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                yearBeginFocusGained(e);
            }
        });
        add(yearBegin, cc.xywh(5, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- label_yearEnd ----
        label_yearEnd.setText("End");
        label_yearEnd.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        label_yearEnd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                label_yearEndMouseClicked(e);
            }
        });
        add(label_yearEnd, cc.xy(7, 3));

        //---- yearEnd ----
        yearEnd.setColumns(5);
        yearEnd.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                yearEndFocusGained(e);
            }
        });
        add(yearEnd, cc.xywh(9, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JSeparator separator3;
    private JLabel label;
    private JLabel label_yearBegin;
    private JFormattedTextField yearBegin;
    private JLabel label_yearEnd;
    private JFormattedTextField yearEnd;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}