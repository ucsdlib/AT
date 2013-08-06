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
 * Created by JFormDesigner on Sun Aug 28 20:13:54 EDT 2005
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import java.util.Vector;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.mydomain.DomainEditor;
import org.archiviststoolkit.model.NameContactNotes;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.util.SequencedObjectsUtils;
public class NameContactNoteFields extends DomainEditorFields {

	public NameContactNoteFields() {
		super();
		initComponents();
	}

	private void initComponents() {


		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        sortName = new JTextField();
        separator1 = new JSeparator();
        label1 = new JLabel();
        nameContactNoteLabel = ATBasicComponentFactory.createTextField(detailsModel.getModel(NameContactNotes.PROPERTYNAME_LABEL));
        label14 = new JLabel();
        scrollPane1 = new JScrollPane();
        nameContactNote = ATBasicComponentFactory.createTextArea(detailsModel.getModel(NameContactNotes.PROPERTYNAME_NOTE_TEXT));
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

        //---- sortName ----
        sortName.setEditable(false);
        sortName.setOpaque(false);
        sortName.setBorder(null);
        add(sortName, cc.xywh(1, 1, 3, 1));

        //---- separator1 ----
        separator1.setBackground(new Color(220, 220, 232));
        separator1.setForeground(new Color(147, 131, 86));
        separator1.setMinimumSize(new Dimension(1, 10));
        separator1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(separator1, cc.xywh(1, 3, 3, 1));

        //---- label1 ----
        label1.setText("Label");
        label1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label1, NameContactNotes.class, NameContactNotes.PROPERTYNAME_LABEL);
        add(label1, cc.xy(1, 5));
        add(nameContactNoteLabel, cc.xy(3, 5));

        //---- label14 ----
        label14.setText("Note");
        label14.setVerticalAlignment(SwingConstants.TOP);
        label14.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label14, NameContactNotes.class, NameContactNotes.PROPERTYNAME_NOTE_TEXT);
        add(label14, cc.xywh(1, 7, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

        //======== scrollPane1 ========
        {
            scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane1.setMaximumSize(new Dimension(32767, 100));
            scrollPane1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

            //---- nameContactNote ----
            nameContactNote.setRows(4);
            nameContactNote.setLineWrap(true);
            nameContactNote.setTabSize(20);
            nameContactNote.setWrapStyleWord(true);
            scrollPane1.setViewportView(nameContactNote);
        }
        add(scrollPane1, cc.xy(3, 7));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    public JTextField sortName;
    private JSeparator separator1;
    private JLabel label1;
    public JTextField nameContactNoteLabel;
    private JLabel label14;
    private JScrollPane scrollPane1;
    public JTextArea nameContactNote;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	/**
	 * Set the domain model for this editor.
	 *
	 * @param model the model
	 */

	public void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		this.sortName.setText(((NameContactNotes)model).getName().getSortName());
	}

	public Component getInitialFocusComponent() {
		return nameContactNoteLabel;
	}
}
