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
 * @author Lee Mandell
 */

package org.archiviststoolkit.editor;

import java.awt.event.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.mydomain.DomainEditor;
import org.archiviststoolkit.model.FileVersions;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.util.SequencedObjectsUtils;
import org.archiviststoolkit.structure.ATFieldInfo;

import javax.swing.*;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.centerkey.utils.BareBonesBrowserLaunch;

import java.awt.*;
import java.util.Vector;

public class FileVersionFields extends DomainEditorFields {

	protected FileVersionFields() {
		super();
		initComponents();
	}

    /**
     * Method to open the file URI in the browser
     */
    private void openFileBrowser() {
        BareBonesBrowserLaunch.openURL(uri.getText());    
    }

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label_subjectTerm = new JLabel();
        uri = ATBasicComponentFactory.createTextField(detailsModel.getModel(FileVersions.PROPERTYNAME_FILE_VERSIONS_URI));
        label_subjectTermType = new JLabel();
        useStatement = ATBasicComponentFactory.createComboBox(detailsModel, FileVersions.PROPERTYNAME_FILE_VERSIONS_USE_STATEMENT, FileVersions.class);
        actuateLabel = new JLabel();
        actuate = ATBasicComponentFactory.createComboBox(detailsModel, FileVersions.PROPERTYNAME_EAD_DAO_ACTUATE, FileVersions.class);
        showLabel = new JLabel();
        show = ATBasicComponentFactory.createComboBox(detailsModel, FileVersions.PROPERTYNAME_EAD_DAO_SHOW, FileVersions.class);
        button1 = new JButton();
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
                FormFactory.DEFAULT_ROWSPEC
            }));

        //---- label_subjectTerm ----
        label_subjectTerm.setText("URI");
        label_subjectTerm.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectTerm, FileVersions.class, FileVersions.PROPERTYNAME_FILE_VERSIONS_URI);
        add(label_subjectTerm, cc.xy(1, 1));
        add(uri, cc.xy(3, 1));

        //---- label_subjectTermType ----
        label_subjectTermType.setText("Use Statement");
        label_subjectTermType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectTermType, FileVersions.class, FileVersions.PROPERTYNAME_FILE_VERSIONS_USE_STATEMENT);
        add(label_subjectTermType, cc.xy(1, 3));

        //---- useStatement ----
        useStatement.setOpaque(false);
        useStatement.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(useStatement, cc.xywh(3, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- actuateLabel ----
        actuateLabel.setText("EAD DAO Actuate");
        actuateLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(actuateLabel, FileVersions.class, FileVersions.PROPERTYNAME_EAD_DAO_ACTUATE);
        add(actuateLabel, cc.xy(1, 5));

        //---- actuate ----
        actuate.setOpaque(false);
        actuate.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(actuate, cc.xywh(3, 5, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- showLabel ----
        showLabel.setText("EAD DAO Show");
        showLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(showLabel, FileVersions.class, FileVersions.PROPERTYNAME_EAD_DAO_SHOW);
        add(showLabel, cc.xy(1, 7));

        //---- show ----
        show.setOpaque(false);
        show.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(show, cc.xywh(3, 7, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- button1 ----
        button1.setText("Open URI In Web Browser");
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFileBrowser();
            }
        });
        add(button1, cc.xy(3, 9));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label_subjectTerm;
    public JTextField uri;
    private JLabel label_subjectTermType;
    public JComboBox useStatement;
    private JLabel actuateLabel;
    public JComboBox actuate;
    private JLabel showLabel;
    public JComboBox show;
    private JButton button1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public Component getInitialFocusComponent() {
		return uri;
	}
}
