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
 * Created by JFormDesigner on Sat Jul 29 21:47:47 EDT 2006
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.model.NameContactNotes;
import org.archiviststoolkit.model.RepositoryNotes;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.structure.ATFieldInfo;

public class RepositoryNoteFields extends DomainEditorFields {

	public RepositoryNoteFields() {
		super();
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        repositoryName = new JTextField();
        separator1 = new JSeparator();
        label1 = new JLabel();
        nameDescriptionType = ATBasicComponentFactory.createComboBox(detailsModel, RepositoryNotes.PROPERTYNAME_LABEL, RepositoryNotes.class);
        label14 = new JLabel();
        scrollPane1 = new JScrollPane();
        noteText = ATBasicComponentFactory.createTextArea(detailsModel.getModel(RepositoryNotes.PROPERTYNAME_NOTE_TEXT));
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
                FormFactory.DEFAULT_ROWSPEC
            }));

        //---- repositoryName ----
        repositoryName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        repositoryName.setEditable(false);
        repositoryName.setOpaque(false);
        repositoryName.setBorder(null);
        add(repositoryName, cc.xywh(1, 1, 3, 1));

        //---- separator1 ----
        separator1.setBackground(new Color(220, 220, 232));
        separator1.setForeground(new Color(147, 131, 86));
        separator1.setMinimumSize(new Dimension(1, 10));
        separator1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(separator1, cc.xywh(1, 3, 3, 1));

        //---- label1 ----
        label1.setText("Label");
        label1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label1, RepositoryNotes.class, RepositoryNotes.PROPERTYNAME_LABEL);
        add(label1, cc.xy(1, 5));

        //---- nameDescriptionType ----
        nameDescriptionType.setOpaque(false);
        nameDescriptionType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(nameDescriptionType, cc.xy(3, 5));

        //---- label14 ----
        label14.setText("Note");
        label14.setVerticalAlignment(SwingConstants.TOP);
        label14.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label14, RepositoryNotes.class, RepositoryNotes.PROPERTYNAME_NOTE_TEXT);
        add(label14, cc.xywh(1, 7, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

        //======== scrollPane1 ========
        {
            scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane1.setMaximumSize(new Dimension(32767, 100));
            scrollPane1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            scrollPane1.setPreferredSize(new Dimension(600, 324));

            //---- noteText ----
            noteText.setRows(20);
            noteText.setLineWrap(true);
            noteText.setTabSize(20);
            noteText.setWrapStyleWord(true);
            scrollPane1.setViewportView(noteText);
        }
        add(scrollPane1, cc.xy(3, 7));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    public JTextField repositoryName;
    private JSeparator separator1;
    private JLabel label1;
    public JComboBox nameDescriptionType;
    private JLabel label14;
    private JScrollPane scrollPane1;
    public JTextArea noteText;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public Component getInitialFocusComponent() {
		return nameDescriptionType;
	}
}
