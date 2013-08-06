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
 * Created by JFormDesigner on Thu Aug 24 11:25:31 EDT 2006
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.model.Events;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.InLineTags;
import org.archiviststoolkit.util.InLineTagsUtils;

public class EventsFields extends DomainEditorFields {
	public EventsFields() {
		initComponents();
		initUndo(descritpion);
	}

	public Component getInitialFocusComponent() {
		return descritpion;
	}

	private void insertInlineTagActionPerformed() {
		InLineTagsUtils.wrapInTagActionPerformed(insertInlineTag, descritpion,  this.getParentEditor());
	}

	private void undoButtonActionPerformed() {
		handleUndoButtonAction();
	}

	private void redoButtonActionPerformed() {
		handleRedoButtonAction();
	}

//	public JButton getUndoButton() {
//		return undoButton;
//	}
//
//	public JButton getRedoButton() {
//		return redoButton;
//	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label_subjectScopeNote = new JLabel();
        scrollPane1 = new JScrollPane();
        descritpion = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Events.PROPERTYNAME_EVENT_DESCRIPTION), false);
        tagApplicatorPanel = new JPanel();
        insertInlineTag = ATBasicComponentFactory.createUnboundComboBox(InLineTagsUtils.getInLineTagList(InLineTagsUtils.EVENT_DESCRIPTION));
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
                FormFactory.DEFAULT_ROWSPEC
            }));

        //---- label_subjectScopeNote ----
        label_subjectScopeNote.setText("Event");
        label_subjectScopeNote.setVerticalAlignment(SwingConstants.TOP);
        label_subjectScopeNote.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectScopeNote, Events.class, Events.PROPERTYNAME_EVENT_DESCRIPTION);
        add(label_subjectScopeNote, cc.xywh(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

        //======== scrollPane1 ========
        {
            scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane1.setMaximumSize(new Dimension(32767, 100));
            scrollPane1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

            //---- descritpion ----
            descritpion.setRows(4);
            descritpion.setLineWrap(true);
            descritpion.setTabSize(20);
            descritpion.setWrapStyleWord(true);
            scrollPane1.setViewportView(descritpion);
        }
        add(scrollPane1, cc.xy(3, 1));

        //======== tagApplicatorPanel ========
        {
            tagApplicatorPanel.setOpaque(false);
            tagApplicatorPanel.setLayout(new FormLayout(
                "default",
                "default"));

            //---- insertInlineTag ----
            insertInlineTag.setOpaque(false);
            insertInlineTag.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            insertInlineTag.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    insertInlineTagActionPerformed();
                }
            });
            tagApplicatorPanel.add(insertInlineTag, cc.xy(1, 1));
        }
        add(tagApplicatorPanel, cc.xy(3, 3));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label_subjectScopeNote;
    private JScrollPane scrollPane1;
    public JTextArea descritpion;
    private JPanel tagApplicatorPanel;
    public JComboBox insertInlineTag;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
