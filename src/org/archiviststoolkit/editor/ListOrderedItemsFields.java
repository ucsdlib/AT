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
 * Created by JFormDesigner on Thu Aug 24 15:54:10 EDT 2006
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.model.ListOrderedItems;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.InLineTags;
import org.archiviststoolkit.util.InLineTagsUtils;

public class ListOrderedItemsFields extends DomainEditorFields {
	public ListOrderedItemsFields() {
		initComponents();
		initUndo(value);
	}

	public Component getInitialFocusComponent() {
		return value;
	}

	private void insertInlineTagActionPerformed() {
		InLineTagsUtils.wrapInTagActionPerformed(insertInlineTag, value,  this.getParentEditor());
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
        value = ATBasicComponentFactory.createTextArea(detailsModel.getModel(ListOrderedItems.PROPERTYNAME_ITEM_VALUE), false);
        tagApplicatorPanel = new JPanel();
        insertInlineTag = ATBasicComponentFactory.createUnboundComboBox(InLineTagsUtils.getInLineTagList(InLineTagsUtils.ORDERED_LIST_ITEM_VALUE));
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
        label_subjectScopeNote.setText("Value");
        label_subjectScopeNote.setVerticalAlignment(SwingConstants.TOP);
        label_subjectScopeNote.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectScopeNote, ListOrderedItems.class, ListOrderedItems.PROPERTYNAME_ITEM_VALUE);
        add(label_subjectScopeNote, cc.xywh(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

        //======== scrollPane1 ========
        {
            scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane1.setMaximumSize(new Dimension(32767, 100));
            scrollPane1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

            //---- value ----
            value.setRows(4);
            value.setLineWrap(true);
            value.setTabSize(20);
            value.setWrapStyleWord(true);
            scrollPane1.setViewportView(value);
        }
        add(scrollPane1, cc.xy(3, 1));

        //======== tagApplicatorPanel ========
        {
            tagApplicatorPanel.setOpaque(false);
            tagApplicatorPanel.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC
                },
                RowSpec.decodeSpecs("default")));

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
    public JTextArea value;
    private JPanel tagApplicatorPanel;
    public JComboBox insertInlineTag;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
