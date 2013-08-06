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
 * Created by JFormDesigner on Fri Aug 08 16:39:07 EDT 2008
 */

package org.archiviststoolkit.swing;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
public class FilterPanel extends JPanel {

	public FilterPanel() {
		initComponents();
	}

	public FilterPanel(String label) {
		initComponents();
		filterLabel.setText(label);
	}

	public JTextField getFilterField() {
		return filterField;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        panel1 = new JPanel();
        filterLabel = new JLabel();
        filterField = new JTextField();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setOpaque(false);
        setBackground(new Color(200, 205, 232));
        setLayout(new FormLayout(
            "right:default:grow",
            "default"));

        //======== panel1 ========
        {
            panel1.setBackground(new Color(200, 205, 232));
            panel1.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC
                },
                RowSpec.decodeSpecs("default")));

            //---- filterLabel ----
            filterLabel.setText("Filter");
            filterLabel.setOpaque(true);
            filterLabel.setBackground(new Color(200, 205, 232));
            panel1.add(filterLabel, cc.xy(1, 1));

            //---- filterField ----
            filterField.setColumns(20);
            panel1.add(filterField, cc.xy(3, 1));
        }
        add(panel1, cc.xy(1, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel panel1;
    private JLabel filterLabel;
    private JTextField filterField;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

}
