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
 * Created by JFormDesigner on Fri Oct 06 09:47:11 EDT 2006
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.model.RepositoryNotesDefaultValues;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

public class RepositoryNotesDefaultValuesFields extends DomainEditorFields {
	public RepositoryNotesDefaultValuesFields() {
		initComponents();
	}

	/**
	 * Set the domain model for this editor.
	 *
	 * @param model the model
	 */

	public void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		RepositoryNotesDefaultValues repositoryNotesDefaultValueModel = (RepositoryNotesDefaultValues)model;
		noteType.setText(repositoryNotesDefaultValueModel.getNoteType().getNotesEtcName());
	}

	public Component getInitialFocusComponent() {
		return defaultTitle;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        noteTypeLabel = new JLabel();
        noteType = new JLabel();
        label_subjectScopeNote2 = new JLabel();
        defaultTitle = ATBasicComponentFactory.createTextField(detailsModel.getBufferedModel(RepositoryNotesDefaultValues.PROPERTYNAME_DEFAULT_TITLE));
        label_subjectScopeNote = new JLabel();
        scrollPane1 = new JScrollPane();
        defaultContent = ATBasicComponentFactory.createTextArea(detailsModel.getBufferedModel(RepositoryNotesDefaultValues.PROPERTYNAME_DEFAULT_CONTENT));
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
                FormFactory.RELATED_GAP_ROWSPEC,
                new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
            }));

        //---- noteTypeLabel ----
        noteTypeLabel.setText("Note Type");
        noteTypeLabel.setVerticalAlignment(SwingConstants.TOP);
        noteTypeLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(noteTypeLabel, cc.xywh(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

        //---- noteType ----
        noteType.setText("Note Type Value");
        noteType.setVerticalAlignment(SwingConstants.TOP);
        noteType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(noteType, cc.xywh(3, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

        //---- label_subjectScopeNote2 ----
        label_subjectScopeNote2.setText("Default Title");
        label_subjectScopeNote2.setVerticalAlignment(SwingConstants.TOP);
        label_subjectScopeNote2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectScopeNote2, RepositoryNotesDefaultValues.class, RepositoryNotesDefaultValues.PROPERTYNAME_DEFAULT_TITLE);
        add(label_subjectScopeNote2, cc.xywh(1, 3, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));
        add(defaultTitle, cc.xy(3, 3));

        //---- label_subjectScopeNote ----
        label_subjectScopeNote.setText("Default Content");
        label_subjectScopeNote.setVerticalAlignment(SwingConstants.TOP);
        label_subjectScopeNote.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectScopeNote, RepositoryNotesDefaultValues.class, RepositoryNotesDefaultValues.PROPERTYNAME_DEFAULT_CONTENT);
        add(label_subjectScopeNote, cc.xywh(1, 5, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

        //======== scrollPane1 ========
        {
            scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane1.setMaximumSize(new Dimension(32767, 100));
            scrollPane1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

            //---- defaultContent ----
            defaultContent.setRows(10);
            defaultContent.setLineWrap(true);
            defaultContent.setTabSize(20);
            defaultContent.setWrapStyleWord(true);
            scrollPane1.setViewportView(defaultContent);
        }
        add(scrollPane1, cc.xywh(3, 5, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel noteTypeLabel;
    private JLabel noteType;
    private JLabel label_subjectScopeNote2;
    public JTextField defaultTitle;
    private JLabel label_subjectScopeNote;
    private JScrollPane scrollPane1;
    public JTextArea defaultContent;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
