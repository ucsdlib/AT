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
 * Created by JFormDesigner on Thu May 04 12:52:22 EDT 2006
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.IndexItems;
import org.archiviststoolkit.util.ResourceUtils;
import org.archiviststoolkit.structure.ATFieldInfo;

public class IndexItemsFields extends DomainEditorFields {

	public IndexItemsFields() {
		super();
		initComponents();
	}

	/**
	 * Set the domain model for this editor.
	 *
	 * @param model the model
	 */

	public void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		IndexItems indexItem = (IndexItems)super.getModel();
		reference.setModel(new DefaultComboBoxModel(ResourceUtils.getReferencesArray()));
		int selectedIndex = ResourceUtils.getIndexById(indexItem.getReference());
		if (selectedIndex >= 0) {
			//add 1 to account for the blank at the top of the list			
			reference.setSelectedIndex(selectedIndex + 1);
		}
	}

	private void referenceActionPerformed(ActionEvent e) {
		IndexItems indexItem = (IndexItems)super.getModel();
		if (reference.getSelectedIndex() == 0) {
			indexItem.setReference("");
		} else {
			ResourceUtils.PersistentId persistentID = (ResourceUtils.PersistentId)reference.getSelectedItem();
			indexItem.setReference(persistentID.getPersistentId());
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label_subjectScopeNote = new JLabel();
        scrollPane1 = new JScrollPane();
        descritpion = ATBasicComponentFactory.createTextArea(detailsModel.getModel(IndexItems.PROPERTYNAME_ITEM_VALUE));
        label_subjectScopeNote2 = new JLabel();
        type = ATBasicComponentFactory.createComboBox(detailsModel, IndexItems.PROPERTYNAME_ITEM_TYPE, IndexItems.class);
        label_subjectScopeNote3 = new JLabel();
        reference = new JComboBox();
        label_subjectScopeNote4 = new JLabel();
        referenceText = ATBasicComponentFactory.createTextField(detailsModel.getModel(IndexItems.PROPERTYNAME_REFERENCE_TEXT));
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
                new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
                FormFactory.LINE_GAP_ROWSPEC,
                new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC
            }));

        //---- label_subjectScopeNote ----
        label_subjectScopeNote.setText("Value");
        label_subjectScopeNote.setVerticalAlignment(SwingConstants.TOP);
        label_subjectScopeNote.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectScopeNote, IndexItems.class, IndexItems.PROPERTYNAME_ITEM_VALUE);
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

        //---- label_subjectScopeNote2 ----
        label_subjectScopeNote2.setText("Type");
        label_subjectScopeNote2.setVerticalAlignment(SwingConstants.TOP);
        label_subjectScopeNote2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectScopeNote2, IndexItems.class, IndexItems.PROPERTYNAME_ITEM_TYPE);
        add(label_subjectScopeNote2, cc.xywh(1, 3, 1, 1, CellConstraints.DEFAULT, CellConstraints.CENTER));

        //---- type ----
        type.setMaximumSize(new Dimension(50, 27));
        type.setOpaque(false);
        type.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(type, cc.xywh(3, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- label_subjectScopeNote3 ----
        label_subjectScopeNote3.setText("Reference");
        label_subjectScopeNote3.setVerticalAlignment(SwingConstants.TOP);
        label_subjectScopeNote3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectScopeNote3, IndexItems.class, IndexItems.PROPERTYNAME_REFERENCE);
        add(label_subjectScopeNote3, cc.xywh(1, 5, 1, 1, CellConstraints.DEFAULT, CellConstraints.CENTER));

        //---- reference ----
        reference.setMaximumSize(new Dimension(50, 27));
        reference.setOpaque(false);
        reference.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        reference.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                referenceActionPerformed(e);
            }
        });
        add(reference, cc.xywh(3, 5, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- label_subjectScopeNote4 ----
        label_subjectScopeNote4.setText("Reference Text");
        label_subjectScopeNote4.setVerticalAlignment(SwingConstants.TOP);
        label_subjectScopeNote4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectScopeNote4, IndexItems.class, IndexItems.PROPERTYNAME_REFERENCE_TEXT);
        add(label_subjectScopeNote4, cc.xywh(1, 7, 1, 1, CellConstraints.DEFAULT, CellConstraints.CENTER));
        add(referenceText, cc.xy(3, 7));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label_subjectScopeNote;
    private JScrollPane scrollPane1;
    public JTextArea descritpion;
    private JLabel label_subjectScopeNote2;
    public JComboBox type;
    private JLabel label_subjectScopeNote3;
    public JComboBox reference;
    private JLabel label_subjectScopeNote4;
    public JTextField referenceText;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public Component getInitialFocusComponent() {
		return descritpion;
	}
}
