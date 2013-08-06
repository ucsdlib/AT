/*
 * Archivists' Toolkit(TM) Copyright © 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.  
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
 * Created by JFormDesigner on Fri Jul 18 10:57:13 EDT 2008
 */

package org.archiviststoolkit.editor.rde;

import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.structure.NotesEtcTypes;
import org.archiviststoolkit.structure.DefaultValues;
import org.archiviststoolkit.model.ResourcesComponents;
import org.archiviststoolkit.model.ArchDescriptionNotes;
import org.archiviststoolkit.model.RDEScreenPanelItems;
import org.archiviststoolkit.model.RepositoryNotesDefaultValues;
import org.archiviststoolkit.exceptions.RDEPopulateException;
import org.archiviststoolkit.exceptions.RDEPanelCreationException;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.util.NoteEtcTypesUtils;
import org.archiviststoolkit.ApplicationFrame;

public class RdeNoteFields extends RdePanel {

	private boolean noteTypeSticky = false;
	private boolean noteInternalOnlySticky = false;

	public RdeNoteFields(RdePanelContainer parentPanel) {
		super(parentPanel);
		initComponents();
	}

	public void populateComponent(ResourcesComponents component) throws RDEPopulateException {
		String noteContent = note.getText();
		NotesEtcTypes selectedNoteType = (NotesEtcTypes)noteType.getSelectedItem();
		if (selectedNoteType != null || noteContent.length() > 0) {
			ArchDescriptionNotes note = new ArchDescriptionNotes(component, "", NotesEtcTypes.DATA_TYPE_NOTE, selectedNoteType, noteContent);
			note.setInternalOnly(internalOnly.isSelected());
			note.setPersistentId(parentPanel.getResource().getNextPersistentIdAndIncrement());
			component.addRepeatingData(note);
			component.setHasNotes(true);
		}
	}

	public void clearFields() {
		if (!noteTypeSticky) {
			note.setText("");
			noteType.setSelectedIndex(0);
		}
		if (!noteInternalOnlySticky) {
			internalOnly.setSelected(false);
		}
	}

	public void setStickyLabels() {
		setLabelColor(noteTypeSticky, label_Note);
		setLabelColor(noteInternalOnlySticky, label_internalOnly);
	}

	public void initializeStickyLabels(Collection<RDEScreenPanelItems> panelItems) throws RDEPanelCreationException {
		for(RDEScreenPanelItems panelItem: panelItems) {
			if (panelItem.getPropertyName().equals(ArchDescriptionNotes.PROPERTYNAME_ARCH_DESCRIPTION_NOTE_TYPE)) {
				this.noteTypeSticky = panelItem.getSticky();
			} else if (panelItem.getPropertyName().equals(ArchDescriptionNotes.PROPERTYNAME_ARCH_DESCRIPTION_NOTE_INTERNAL_ONLY)){
				this.noteInternalOnlySticky = panelItem.getSticky();
			} else {
				throw new RDEPanelCreationException(panelItem.getPropertyName() + " is not supported here");
			}
		}
	}

	private void noteTypeFocusGained(FocusEvent e) {
		parentPanel.componentFocusGained(e, this);
	}

	private void noteFocusGained(FocusEvent e) {
		parentPanel.componentFocusGained(e, this);		
	}

	private void label_NoteMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			noteTypeSticky = !noteTypeSticky;
			setStickyLabels();
		}
	}

	private void label_internalOnlyMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			noteInternalOnlySticky = !noteInternalOnlySticky;
			setStickyLabels();
		}
	}

    private void noteTypeActionPerformed(ActionEvent e) {
        if (noteType.getSelectedIndex() > 0) {
			NotesEtcTypes notesEtcType = (NotesEtcTypes)noteType.getSelectedItem();

            RepositoryNotesDefaultValues defaultValue = DefaultValues.getRepoistoryNoteDefaultValue(ApplicationFrame.getInstance().getCurrentUserRepository(), notesEtcType);
            if (defaultValue != null) {
                note.setText(defaultValue.getDefaultContent());
            }
		}
    }

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        separator3 = new JSeparator();
        label_Note = new JLabel();
        noteType = ATBasicComponentFactory.createUnboundComboBox(NoteEtcTypesUtils.getNotesOnlyTypesList(true));
        internalOnly = new JCheckBox();
        label_internalOnly = new JLabel();
        scrollPane1 = new JScrollPane();
        note = ATBasicComponentFactory.createUnboundedTextArea();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBackground(new Color(200, 205, 232));
        setLayout(new FormLayout(
            new ColumnSpec[] {
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

        //---- separator3 ----
        separator3.setBackground(new Color(220, 220, 232));
        separator3.setForeground(new Color(147, 131, 86));
        separator3.setMinimumSize(new Dimension(1, 10));
        separator3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(separator3, cc.xywh(1, 1, 7, 1));

        //---- label_Note ----
        label_Note.setText("Note");
        label_Note.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        label_Note.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                label_NoteMouseClicked(e);
            }
        });
        add(label_Note, cc.xywh(1, 3, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

        //---- noteType ----
        noteType.setOpaque(false);
        noteType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        noteType.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                noteTypeFocusGained(e);
            }
        });
        noteType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                noteTypeActionPerformed(e);
            }
        });
        add(noteType, cc.xywh(3, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- internalOnly ----
        internalOnly.setBackground(new Color(200, 205, 232));
        add(internalOnly, cc.xy(5, 3));

        //---- label_internalOnly ----
        label_internalOnly.setText("Internal only");
        label_internalOnly.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        label_internalOnly.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                label_internalOnlyMouseClicked(e);
            }
        });
        add(label_internalOnly, cc.xywh(7, 3, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

        //======== scrollPane1 ========
        {

            //---- note ----
            note.setRows(4);
            note.setWrapStyleWord(true);
            note.setLineWrap(true);
            note.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    noteFocusGained(e);
                }
            });
            scrollPane1.setViewportView(note);
        }
        add(scrollPane1, cc.xywh(1, 5, 7, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JSeparator separator3;
    private JLabel label_Note;
    public JComboBox noteType;
    private JCheckBox internalOnly;
    private JLabel label_internalOnly;
    private JScrollPane scrollPane1;
    private JTextArea note;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}