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
 * Created by JFormDesigner on Sun Aug 06 09:23:34 EDT 2006
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.model.Deaccessions;
import org.archiviststoolkit.structure.ATFieldInfo;

public class DeaccessionsFields extends DomainEditorFields {
	public DeaccessionsFields() {
		super();
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        repositoryInfo = new JPanel();
        label15 = new JLabel();
        accessionDate = ATBasicComponentFactory.createDateField(detailsModel.getModel(Deaccessions.PROPERTYNAME_DATE));
        label1 = new JLabel();
        scrollPane1 = new JScrollPane();
        subjectScopeNote = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Deaccessions.PROPERTYNAME_DESCRIPTION));
        label2 = new JLabel();
        scrollPane2 = new JScrollPane();
        subjectScopeNote2 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Deaccessions.PROPERTYNAME_REASON));
        panel1 = new JPanel();
        label3 = new JLabel();
        extentNumber = ATBasicComponentFactory.createDoubleField(detailsModel,Deaccessions.PROPERTYNAME_EXTENT);
        extentType = ATBasicComponentFactory.createComboBox(detailsModel, Deaccessions.PROPERTYNAME_EXTENT_TYPE, Deaccessions.class);
        label4 = new JLabel();
        repositoryName4 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Deaccessions.PROPERTYNAME_DISPOSITION));
        rights = ATBasicComponentFactory.createCheckBox(detailsModel, Deaccessions.PROPERTYNAME_NOTIFICATION, Deaccessions.class);
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        setLayout(new FormLayout(
            "default:grow",
            "top:default:grow"));

        //======== repositoryInfo ========
        {
            repositoryInfo.setBorder(Borders.DLU4_BORDER);
            repositoryInfo.setOpaque(false);
            repositoryInfo.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            repositoryInfo.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec("max(default;400px)")
                },
                new RowSpec[] {
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
                    FormFactory.LINE_GAP_ROWSPEC,
                    new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC
                }));

            //---- label15 ----
            label15.setText("Date");
            label15.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label15, Deaccessions.class, Deaccessions.PROPERTYNAME_DATE);
            repositoryInfo.add(label15, cc.xy(1, 1));

            //---- accessionDate ----
            accessionDate.setColumns(12);
            repositoryInfo.add(accessionDate, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

            //---- label1 ----
            label1.setText("Description");
            label1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label1, Deaccessions.class, Deaccessions.PROPERTYNAME_DESCRIPTION);
            repositoryInfo.add(label1, cc.xy(1, 3));

            //======== scrollPane1 ========
            {
                scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                scrollPane1.setMaximumSize(new Dimension(32767, 100));

                //---- subjectScopeNote ----
                subjectScopeNote.setRows(4);
                subjectScopeNote.setLineWrap(true);
                subjectScopeNote.setTabSize(20);
                subjectScopeNote.setWrapStyleWord(true);
                scrollPane1.setViewportView(subjectScopeNote);
            }
            repositoryInfo.add(scrollPane1, cc.xy(3, 3));

            //---- label2 ----
            label2.setText("Reason");
            label2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label2, Deaccessions.class, Deaccessions.PROPERTYNAME_REASON);
            repositoryInfo.add(label2, cc.xy(1, 5));

            //======== scrollPane2 ========
            {
                scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                scrollPane2.setMaximumSize(new Dimension(32767, 100));

                //---- subjectScopeNote2 ----
                subjectScopeNote2.setRows(4);
                subjectScopeNote2.setLineWrap(true);
                subjectScopeNote2.setTabSize(20);
                subjectScopeNote2.setWrapStyleWord(true);
                scrollPane2.setViewportView(subjectScopeNote2);
            }
            repositoryInfo.add(scrollPane2, cc.xy(3, 5));

            //======== panel1 ========
            {
                panel1.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        FormFactory.PREF_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC
                    },
                    RowSpec.decodeSpecs("default")));

                //---- label3 ----
                label3.setText("Extent");
                ATFieldInfo.assignLabelInfo(label3, Deaccessions.class, Deaccessions.PROPERTYNAME_EXTENT);
                panel1.add(label3, cc.xy(1, 1));

                //---- extentNumber ----
                extentNumber.setColumns(6);
                panel1.add(extentNumber, cc.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

                //---- extentType ----
                extentType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                extentType.setOpaque(false);
                panel1.add(extentType, new CellConstraints(5, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT, new Insets( 0, 5, 5, 5)));
            }
            repositoryInfo.add(panel1, cc.xywh(1, 7, 3, 1));

            //---- label4 ----
            label4.setText("Disposition");
            label4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label4, Deaccessions.class, Deaccessions.PROPERTYNAME_DISPOSITION);
            repositoryInfo.add(label4, cc.xy(1, 9));
            repositoryInfo.add(repositoryName4, cc.xy(3, 9));

            //---- rights ----
            rights.setBackground(new Color(231, 188, 251));
            rights.setText("Notification");
            rights.setOpaque(false);
            rights.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            rights.setText(ATFieldInfo.getLabel(Deaccessions.class, Deaccessions.PROPERTYNAME_NOTIFICATION));
            repositoryInfo.add(rights, cc.xywh(1, 11, 3, 1));
        }
        add(repositoryInfo, cc.xy(1, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel repositoryInfo;
    private JLabel label15;
    private JFormattedTextField accessionDate;
    private JLabel label1;
    private JScrollPane scrollPane1;
    public JTextArea subjectScopeNote;
    private JLabel label2;
    private JScrollPane scrollPane2;
    public JTextArea subjectScopeNote2;
    private JPanel panel1;
    private JLabel label3;
    public JFormattedTextField extentNumber;
    public JComboBox extentType;
    private JLabel label4;
    private JTextField repositoryName4;
    public JCheckBox rights;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public Component getInitialFocusComponent() {
		return accessionDate;
	}
}
