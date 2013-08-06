/**
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
 * Created by JFormDesigner on Wed Aug 24 16:08:56 EDT 2005
 */

package org.archiviststoolkit.editor;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.model.RDEScreen;
import org.archiviststoolkit.model.RDEScreenPanels;
import org.archiviststoolkit.model.RDEScreenPanelItems;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.mydomain.DomainSortableTable;
import org.archiviststoolkit.mydomain.DomainSortedTable;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RDEScreenPanelFields extends DomainEditorFields {

	public RDEScreenPanelFields() {
		super();
		initComponents();
	}


	private void rdeScreenPanelFieldsMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			int selectedRow = rdeScreenPanelFields.getSelectedRow();
			if (rdeScreenPanelFields.getSelectedRow() != -1) {
				RDEScreenPanelItems selectedItem = (RDEScreenPanelItems)rdeScreenPanelFields.getSortedList().get(selectedRow);
				selectedItem.toggleStickiness();
				this.invalidate();
				this.validate();
				this.repaint();
			}
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label_RDE_name = new JLabel();
        panelType = new JLabel();
        label1 = new JLabel();
        panel2 = new JPanel();
        scrollPane1 = new JScrollPane();
        rdeScreenPanelFields = new DomainSortableTable(RDEScreenPanelItems.class);
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBorder(Borders.DLU4_BORDER);
        setOpaque(false);
        setPreferredSize(new Dimension(800, 500));
        setBackground(new Color(238, 238, 238));
        setLayout(new FormLayout(
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
                FormFactory.DEFAULT_ROWSPEC
            }));

        //---- label_RDE_name ----
        label_RDE_name.setText("Panel Type");
        label_RDE_name.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_RDE_name, RDEScreen.class, RDEScreen.PROPERTYNAME_NAME_RDE_SCREEN_NAME);
        add(label_RDE_name, cc.xy(1, 1));

        //---- panelType ----
        panelType.setText("text");
        add(panelType, cc.xy(3, 1));

        //---- label1 ----
        label1.setText("Fields: Double click to toggle stickiness");
        add(label1, cc.xywh(1, 3, 3, 1));

        //======== panel2 ========
        {
            panel2.setLayout(new FormLayout(
                "default:grow",
                "top:default"));

            //======== scrollPane1 ========
            {

                //---- rdeScreenPanelFields ----
                rdeScreenPanelFields.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        rdeScreenPanelFieldsMouseClicked(e);
                    }
                });
                scrollPane1.setViewportView(rdeScreenPanelFields);
            }
            panel2.add(scrollPane1, cc.xy(1, 1));
        }
        add(panel2, cc.xywh(1, 5, 3, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label_RDE_name;
    private JLabel panelType;
    private JLabel label1;
    private JPanel panel2;
    private JScrollPane scrollPane1;
    private DomainSortableTable rdeScreenPanelFields;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public final void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		rdeScreenPanelFields.updateCollection(((RDEScreenPanels)model).getScreenPanelItems());
	}

	public Component getInitialFocusComponent() {
		return null;
	}

}