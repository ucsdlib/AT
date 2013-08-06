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
 * Created by JFormDesigner on Mon Jun 26 10:56:36 EDT 2006
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.centerkey.utils.BareBonesBrowserLaunch;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.mydomain.DomainEditor;
import org.archiviststoolkit.model.ExternalReference;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.structure.ATFieldInfo;

public class ExternalReferenceFields extends DomainEditorFields {

	protected ExternalReferenceFields(DomainEditor parentEditor) {
		super();
		this.setParentEditor(parentEditor);
		initComponents();
	}

	private void openInBrowserActionPerformed() {
		if (href.getText().length() == 0) {
			JOptionPane.showMessageDialog(this, "HREF must be filled out.");
		} else {
			BareBonesBrowserLaunch.openURL(href.getText());
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label1 = new JLabel();
        title = ATBasicComponentFactory.createTextField(detailsModel.getModel(ExternalReference.PROPERTYNAME_TITLE));
        label2 = new JLabel();
        href = ATBasicComponentFactory.createTextField(detailsModel.getModel(ExternalReference.PROPERTYNAME_HREF));
        actuateLabel = new JLabel();
        actuate = ATBasicComponentFactory.createComboBox(detailsModel, ExternalReference.PROPERTYNAME_ACTUATE, ExternalReference.class);
        showLabel = new JLabel();
        show = ATBasicComponentFactory.createComboBox(detailsModel, ExternalReference.PROPERTYNAME_SHOW, ExternalReference.class);
        openInBrowser = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBorder(Borders.DLU4_BORDER);
        setOpaque(false);
        setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        setMinimumSize(new Dimension(600, 97));
        setPreferredSize(new Dimension(600, 120));
        setLayout(new FormLayout(
            new ColumnSpec[] {
                FormFactory.DEFAULT_COLSPEC,
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
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC
            }));

        //---- label1 ----
        label1.setText("Title");
        label1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label1, ExternalReference.class, ExternalReference.PROPERTYNAME_TITLE);
        add(label1, cc.xy(1, 1));
        add(title, cc.xywh(3, 1, 7, 1));

        //---- label2 ----
        label2.setText("HREF");
        label2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label2, ExternalReference.class, ExternalReference.PROPERTYNAME_HREF);
        add(label2, cc.xy(1, 3));
        add(href, cc.xywh(3, 3, 7, 1));

        //---- actuateLabel ----
        actuateLabel.setText("Actuate");
        actuateLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(actuateLabel, ExternalReference.class, ExternalReference.PROPERTYNAME_ACTUATE);
        add(actuateLabel, cc.xy(1, 5));

        //---- actuate ----
        actuate.setOpaque(false);
        actuate.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(actuate, cc.xy(3, 5));

        //---- showLabel ----
        showLabel.setText("Show");
        showLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(showLabel, ExternalReference.class, ExternalReference.PROPERTYNAME_SHOW);
        add(showLabel, cc.xy(5, 5));

        //---- show ----
        show.setOpaque(false);
        show.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(show, cc.xy(7, 5));

        //---- openInBrowser ----
        openInBrowser.setText("Open in Browser");
        openInBrowser.setOpaque(false);
        openInBrowser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openInBrowserActionPerformed();
            }
        });
        add(openInBrowser, cc.xywh(9, 5, 1, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label1;
    public JTextField title;
    private JLabel label2;
    public JTextField href;
    private JLabel actuateLabel;
    public JComboBox actuate;
    private JLabel showLabel;
    public JComboBox show;
    private JButton openInBrowser;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public Component getInitialFocusComponent() {
		return title;
	}

	public void setVisibleShowAndActuate(boolean visible) {
		actuate.setVisible(visible);
		actuateLabel.setVisible(visible);
		showLabel.setVisible(visible);
		show.setVisible(visible);
	}
}
