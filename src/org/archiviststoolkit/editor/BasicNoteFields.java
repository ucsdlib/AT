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
 * Created by JFormDesigner on Sat Jul 22 19:49:14 EDT 2006
 */


package org.archiviststoolkit.editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.mydomain.DomainEditor;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.model.ArchDescriptionNotes;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.InLineTags;
import org.archiviststoolkit.util.InLineTagsUtils;

public class BasicNoteFields extends DomainEditorFields {

	protected BasicNoteFields(DomainEditor parentEditor) {
		super();
		this.setParentEditor(parentEditor);
		initComponents();
		initUndo(noteContent);
	}

	private void insertInlineTagActionPerformed() {
		InLineTagsUtils.wrapInTagActionPerformed(insertInlineTag, noteContent,  this.getParentEditor());
	}

	private void undoButtonActionPerformed() {
		handleUndoButtonAction();
	}

	/**
	 * Set the domain model for this editor.
	 *
	 * @param model the model
	 */

	public void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		inSetModel = true;
		super.setModel(model, progressPanel);
		inSetModel = false;
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
        label14 = new JLabel();
        scrollPane1 = new JScrollPane();
        noteContent = ATBasicComponentFactory.createTextArea(detailsModel.getModel(ArchDescriptionNotes.PROPERTYNAME_ARCH_DESCRIPTION_NOTE_CONTENT), false);
        tagApplicatorPanel = new JPanel();
        insertInlineTag = ATBasicComponentFactory.createUnboundComboBox(InLineTagsUtils.getInLineTagList());
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBorder(Borders.DLU4_BORDER);
        setOpaque(false);
        setPreferredSize(new Dimension(600, 500));
        setLayout(new FormLayout(
            new ColumnSpec[] {
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
            },
            new RowSpec[] {
                new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC
            }));

        //---- label14 ----
        label14.setText("Text");
        label14.setVerticalAlignment(SwingConstants.TOP);
        label14.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label14, ArchDescriptionNotes.class, ArchDescriptionNotes.PROPERTYNAME_ARCH_DESCRIPTION_NOTE_CONTENT);
        add(label14, cc.xywh(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

        //======== scrollPane1 ========
        {
            scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane1.setMaximumSize(new Dimension(32767, 100));

            //---- noteContent ----
            noteContent.setRows(20);
            noteContent.setLineWrap(true);
            noteContent.setTabSize(20);
            noteContent.setWrapStyleWord(true);
            scrollPane1.setViewportView(noteContent);
        }
        add(scrollPane1, cc.xywh(3, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));

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
    private JLabel label14;
    private JScrollPane scrollPane1;
    public JTextArea noteContent;
    private JPanel tagApplicatorPanel;
    public JComboBox insertInlineTag;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public Component getInitialFocusComponent() {
		return noteContent;
	}
}
