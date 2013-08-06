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
 * Created by JFormDesigner on Tue Mar 07 13:04:11 PST 2006
 */

package org.archiviststoolkit.editor;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.exceptions.DomainEditorCreationException;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.NotesEtcTypes;
import org.archiviststoolkit.structure.DefaultValues;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.util.InLineTagsUtils;
import org.archiviststoolkit.util.NoteEtcTypesUtils;
import org.archiviststoolkit.util.SequencedObjectsUtils;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.ApplicationFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Set;

public class ArchDescriptionNotesFields extends DomainEditorFields {
	public ArchDescriptionNotesFields(DomainEditor parentEditor) {
		super();
		this.setParentEditor(parentEditor);
		initComponents();
		repeatingDataTable.setTransferable();
		initMenus();
		initUndo(noteContent);
	}

	ActionListener menuListenerAddBeforeSelection = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
				System.out.println("Menu selected add before selection: " + ((JMenuItem) e.getSource()).getText());
				NotesEtcTypes noteType = NoteEtcTypesUtils.lookupNoteEtcTypeByCannonicalName(((JMenuItem) e.getSource()).getText());
				addPart(SequencedObjectsUtils.ADD_ABOVE_SELECTION, noteType);
			} catch (UnsupportedRepeatingDataTypeException e1) {
				new ErrorDialog("", e1).showDialog();
			}
		}
	};

	ActionListener menuListenerDelete = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Menu selected delete");
			removePart();
		}
	};

	private void addPart(String where, NotesEtcTypes partType) {
		try {
			addRepeatingData(NoteEtcTypesUtils.lookupRepeatingDataClass(partType), where);
		} catch (UnsupportedRepeatingDataTypeException e) {
			new ErrorDialog(getParentEditor(), "Unsupported repeating data type: " + partType, e).showDialog();
		}
	}

	protected void addRepeatingData(Class repeatingDataClass, String whereString) throws UnsupportedRepeatingDataTypeException {
		ArchDescriptionNotes archDescriptionModel = (ArchDescriptionNotes) super.getModel();

		ArchDescriptionRepeatingData repeatingData = ArchDescriptionRepeatingData.getInstance(archDescriptionModel, repeatingDataClass);
		repeatingData.setSequenceNumber(SequencedObjectsUtils.determineSequenceOfNewItem(whereString, getRepeatingDataTable()));
		dialogRepeatingData.setNewRecord(true);
		dialogRepeatingData.setModel(repeatingData, null);
		dialogRepeatingData.setIncludeOkAndAnotherButton(false); // todo This addresses ART-1618, but really should be handeled properly
//		dialogRepeatingData.disableNavigationButtons();
		if (dialogRepeatingData.showDialog() == javax.swing.JOptionPane.OK_OPTION) {
			archDescriptionModel.addRepeatingData(repeatingData);
			getRepeatingDataTable().getEventList().add(repeatingData);
		}
		dialogRepeatingData.setNewRecord(false);
	}

	protected void removePart() {
		ArchDescriptionNotes archDescriptionModel = (ArchDescriptionNotes) super.getModel();
		try {
			this.removeRelatedTableRow(getRepeatingDataTable(), archDescriptionModel);
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("Name link not removed", e).showDialog();
		}
	}


	public DomainSortedTable getRepeatingDataTable() {
		return repeatingDataTable;
	}

	public JButton getRemovePartButton() {
		return removePartButton;
	}

	private void removePartButtonActionPerformed() {
		removePart();
	}

	private void multiPartActionPerformed(ActionEvent e) {
		showProperCardPane(multiPart.isSelected());
	}

	private void showProperCardPane(Boolean multiPart) {
		if (multiPart) {
			((CardLayout) cardPane.getLayout()).show(cardPane, "multiPartNote");
		} else {
			((CardLayout) cardPane.getLayout()).show(cardPane, "simpleNote");
		}
	}

	private void repeatingDataTableMouseEvent(MouseEvent e) {
		if (e.isPopupTrigger()) {
			repeatingDataPopupMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	private void repeatingDataTableMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			int selectedRowRepeatingData = this.getRepeatingDataTable().getSelectedRow();
			if (selectedRowRepeatingData != -1) {
				DomainObject currentRepeatingData = getRepeatingDataTable().getSortedList().get(selectedRowRepeatingData);
				try {
					dialogRepeatingData = (ArchDescriptionRepeatingDataEditor) DomainEditorFactory.getInstance().
							createDomainEditorWithParent(ArchDescriptionRepeatingData.class, this.getParentEditor(), getRepeatingDataTable(), super.getModel());
				} catch (DomainEditorCreationException e1) {
					new ErrorDialog(getParentEditor(), "Error creating editor for ArchDescriptionRepeatingData", e1).showDialog();

				}

				dialogRepeatingData.setModel(currentRepeatingData, null);
				dialogRepeatingData.setSelectedRow(selectedRowRepeatingData);
				dialogRepeatingData.setNavigationButtons();
				dialogRepeatingData.showDialog();
			}

		}

	}

	protected void setNotesEtcDropDownValues(Vector values) {
		getNotesType().setModel(new DefaultComboBoxModel(values));
	}

	private void addPartComboBoxActionPerformed(ActionEvent e) {
		if (addPartComboBox.getSelectedIndex() > 0) {
			if (getRepeatingDataTable().getSelectedRow() == -1) {
				addPart(SequencedObjectsUtils.ADD_AT_END, (NotesEtcTypes) addPartComboBox.getSelectedItem());
			} else {
				addPart(SequencedObjectsUtils.ADD_ABOVE_SELECTION, (NotesEtcTypes) addPartComboBox.getSelectedItem());
			}
			addPartComboBox.setSelectedIndex(0);
		}
	}

	private void insertInlineTagActionPerformed() {
		InLineTagsUtils.wrapInTagActionPerformed(insertInlineTag, noteContent, this.getParentEditor());
	}


	private void undoButtonActionPerformed() {
		handleUndoButtonAction();
	}

	private void redoButtonActionPerformed() {
		handleRedoButtonAction();
	}

	private void setUndoRedoWrapEnabled(boolean enabled) {
//		undoButton.setEnabled(enabled);
//		redoButton.setEnabled(enabled);
		insertInlineTag.setEnabled(enabled);
	}

	private void noteContentFocusGained() {
//		setUndoRedoWrapEnabled(true);
	}

	private void noteContentFocusLost() {
//		setUndoRedoWrapEnabled(false);
	}

	public JComboBox getNotesType() {
		return notesType;
	}

    /**
     * Method to handel action events from the noteType drop down menu
     * @param e the action event
     */
    private void notesTypeActionPerformed(ActionEvent e) {
        // get the current note type
        NotesEtcTypes newNotesEtcTypes = (NotesEtcTypes)getNotesType().getSelectedItem();
        ArchDescriptionNotes archDescriptionNotesModel = (ArchDescriptionNotes) super.getModel();

        // get the old NoteEtcTypes and set the new one if they are not the same. If they are the same just return
        NotesEtcTypes oldNotesEtcTypes = archDescriptionNotesModel.getNotesEtcType();

        if(newNotesEtcTypes.equals(oldNotesEtcTypes)) {
            return;
        }

        archDescriptionNotesModel.setNotesEtcType(newNotesEtcTypes);

        // get any default values
        RepositoryNotesDefaultValues oldDefaultValue = DefaultValues.getRepoistoryNoteDefaultValue(ApplicationFrame.getInstance().getCurrentUserRepository(), oldNotesEtcTypes);
        RepositoryNotesDefaultValues newDefaultValue = DefaultValues.getRepoistoryNoteDefaultValue(ApplicationFrame.getInstance().getCurrentUserRepository(), newNotesEtcTypes);

        // check to see if the text in title and note content are not defaults. if they are then clear them out
        if(oldDefaultValue != null && title.getText().trim().equals(oldDefaultValue.getDefaultTitle())) {
            title.setText("");
        }

        if(oldDefaultValue != null && noteContent.getText().trim().equals(oldDefaultValue.getDefaultContent())) {
            noteContent.setText("");
        }

        // assign the default value only if the title field is empty
        if(StringHelper.isEmpty(title.getText()) && newDefaultValue != null) {
            title.setText(newDefaultValue.getDefaultTitle());
        }

        // assign default value only if note content text area is empty
        if(StringHelper.isEmpty(noteContent.getText()) && newDefaultValue != null) {
            noteContent.setText(newDefaultValue.getDefaultContent());
        }
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
        panel1 = new JPanel();
        internalOnly = ATBasicComponentFactory.createCheckBox(detailsModel, ArchDescriptionNotes.PROPERTYNAME_ARCH_DESCRIPTION_NOTE_INTERNAL_ONLY, ArchDescriptionNotes.class);
        multiPart = ATBasicComponentFactory.createCheckBox(detailsModel, ArchDescriptionNotes.PROPERTYNAME_ARCH_DESCRIPTION_MULTI_PART, ArchDescriptionNotes.class);
        panel2 = new JPanel();
        label3 = new JLabel();
        persistentId = ATBasicComponentFactory.createTextField(detailsModel.getModel(ArchDescriptionRepeatingData.PROPERTYNAME_PERSISTENT_ID));
        label1 = new JLabel();
        notesType = new JComboBox();
        label2 = new JLabel();
        title = ATBasicComponentFactory.createTextField(detailsModel.getModel(ArchDescriptionRepeatingData.PROPERTYNAME_TITLE));
        cardPane = new JPanel();
        simpleNote = new JPanel();
        label14 = new JLabel();
        scrollPane1 = new JScrollPane();
        noteContent = ATBasicComponentFactory.createTextArea(detailsModel.getModel(ArchDescriptionNotes.PROPERTYNAME_ARCH_DESCRIPTION_NOTE_CONTENT), false);
        tagApplicatorPanel = new JPanel();
        insertInlineTag = new JComboBox();
        multiPartNote = new JPanel();
        scrollPane6 = new JScrollPane();
        repeatingDataTable = new DomainSortedTable(ArchDescriptionRepeatingData.class);
        panel15 = new JPanel();
        addPartComboBox = new JComboBox();
        removePartButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBorder(Borders.DLU4_BORDER);
        setPreferredSize(new Dimension(800, 500));
        setBackground(new Color(200, 205, 232));
        setLayout(new FormLayout(
            new ColumnSpec[] {
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                new ColumnSpec(ColumnSpec.FILL, Sizes.MINIMUM, FormSpec.DEFAULT_GROW)
            },
            new RowSpec[] {
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
            }));

        //======== panel1 ========
        {
            panel1.setOpaque(false);
            panel1.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.RIGHT, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                },
                RowSpec.decodeSpecs("default")));

            //---- internalOnly ----
            internalOnly.setBackground(new Color(231, 188, 251));
            internalOnly.setText("Internal Only");
            internalOnly.setOpaque(false);
            internalOnly.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            panel1.add(internalOnly, cc.xy(1, 1));

            //---- multiPart ----
            multiPart.setBackground(new Color(231, 188, 251));
            multiPart.setText("Multi-part");
            multiPart.setOpaque(false);
            multiPart.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            multiPart.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    multiPartActionPerformed(e);
                }
            });
            panel1.add(multiPart, cc.xy(3, 1));

            //======== panel2 ========
            {
                panel2.setOpaque(false);
                panel2.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC
                    },
                    RowSpec.decodeSpecs("default")));

                //---- label3 ----
                label3.setText("Persistent ID");
                label3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                panel2.add(label3, cc.xy(1, 1));

                //---- persistentId ----
                persistentId.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                persistentId.setColumns(3);
                persistentId.setBorder(null);
                persistentId.setEditable(false);
                persistentId.setOpaque(false);
                panel2.add(persistentId, cc.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
            }
            panel1.add(panel2, cc.xy(5, 1));
        }
        add(panel1, cc.xywh(1, 1, 3, 1));

        //---- label1 ----
        label1.setText("Type");
        label1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label1, ArchDescriptionNotes.class, ArchDescriptionNotes.PROPERTYNAME_ARCH_DESCRIPTION_NOTE_TYPE);
        add(label1, cc.xy(1, 3));

        //---- notesType ----
        notesType.setOpaque(false);
        notesType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        notesType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                notesTypeActionPerformed(e);
            }
        });
        add(notesType, cc.xy(3, 3));

        //---- label2 ----
        label2.setText("Title");
        label2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label2, ArchDescriptionNotes.class, ArchDescriptionNotes.PROPERTYNAME_TITLE);
        add(label2, cc.xy(1, 5));
        add(title, new CellConstraints(3, 5, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP, new Insets( 0, 0, 0, 5)));

        //======== cardPane ========
        {
            cardPane.setLayout(new CardLayout());

            //======== simpleNote ========
            {
                simpleNote.setBorder(null);
                simpleNote.setBackground(new Color(200, 205, 232));
                simpleNote.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
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
                label14.setText("Note");
                label14.setVerticalAlignment(SwingConstants.TOP);
                label14.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                ATFieldInfo.assignLabelInfo(label14, ArchDescriptionNotes.class, ArchDescriptionNotes.PROPERTYNAME_ARCH_DESCRIPTION_NOTE_CONTENT);
                simpleNote.add(label14, cc.xywh(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

                //======== scrollPane1 ========
                {
                    scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                    scrollPane1.setMaximumSize(new Dimension(32767, 100));

                    //---- noteContent ----
                    noteContent.setRows(20);
                    noteContent.setLineWrap(true);
                    noteContent.setTabSize(20);
                    noteContent.setWrapStyleWord(true);
                    noteContent.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusGained(FocusEvent e) {
                            noteContentFocusGained();
                        }
                        @Override
                        public void focusLost(FocusEvent e) {
                            noteContentFocusLost();
                        }
                    });
                    scrollPane1.setViewportView(noteContent);
                }
                simpleNote.add(scrollPane1, cc.xywh(3, 1, 3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));

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
                simpleNote.add(tagApplicatorPanel, cc.xywh(3, 3, 3, 1));
            }
            cardPane.add(simpleNote, "simpleNote");

            //======== multiPartNote ========
            {
                multiPartNote.setBorder(Borders.DLU4_BORDER);
                multiPartNote.setBackground(new Color(200, 205, 232));
                multiPartNote.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                    },
                    new RowSpec[] {
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC
                    }));

                //======== scrollPane6 ========
                {
                    scrollPane6.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                    scrollPane6.setOpaque(false);
                    scrollPane6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

                    //---- repeatingDataTable ----
                    repeatingDataTable.setFocusable(false);
                    repeatingDataTable.setDragEnabled(true);
                    repeatingDataTable.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            repeatingDataTableMouseClicked(e);
                        }
                        @Override
                        public void mousePressed(MouseEvent e) {
                            repeatingDataTableMouseEvent(e);
                        }
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            repeatingDataTableMouseEvent(e);
                        }
                    });
                    scrollPane6.setViewportView(repeatingDataTable);
                }
                multiPartNote.add(scrollPane6, cc.xywh(1, 1, 3, 1));

                //======== panel15 ========
                {
                    panel15.setBackground(new Color(231, 188, 251));
                    panel15.setOpaque(false);
                    panel15.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel15.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC
                        },
                        RowSpec.decodeSpecs("default:grow")));

                    //---- addPartComboBox ----
                    addPartComboBox.setOpaque(false);
                    addPartComboBox.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            addPartComboBoxActionPerformed(e);
                        }
                    });
                    panel15.add(addPartComboBox, cc.xy(1, 1));

                    //---- removePartButton ----
                    removePartButton.setBackground(new Color(231, 188, 251));
                    removePartButton.setText("Remove Part");
                    removePartButton.setOpaque(false);
                    removePartButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    removePartButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            removePartButtonActionPerformed();
                        }
                    });
                    panel15.add(removePartButton, cc.xy(3, 1));
                }
                multiPartNote.add(panel15, cc.xywh(1, 3, 3, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
            }
            cardPane.add(multiPartNote, "multiPartNote");
        }
        add(cardPane, cc.xywh(1, 7, 3, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel panel1;
    public JCheckBox internalOnly;
    public JCheckBox multiPart;
    private JPanel panel2;
    private JLabel label3;
    public JTextField persistentId;
    private JLabel label1;
    public JComboBox notesType;
    private JLabel label2;
    public JTextField title;
    private JPanel cardPane;
    private JPanel simpleNote;
    private JLabel label14;
    private JScrollPane scrollPane1;
    public JTextArea noteContent;
    private JPanel tagApplicatorPanel;
    public JComboBox insertInlineTag;
    private JPanel multiPartNote;
    private JScrollPane scrollPane6;
    private DomainSortedTable repeatingDataTable;
    private JPanel panel15;
    private JComboBox addPartComboBox;
    private JButton removePartButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	private JPopupMenu repeatingDataPopupMenu;
	protected ArchDescriptionRepeatingDataEditor dialogRepeatingData;
	private DomainEditor notesEditor;

	public void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		inSetModel = true;
		super.setModel(model, progressPanel);

        ArchDescriptionNotes archDescriptionNotesModel = (ArchDescriptionNotes) super.getModel();
		repeatingDataTable.updateCollection(archDescriptionNotesModel.getChildren());
		showProperCardPane(archDescriptionNotesModel.getMultiPart());
		dialogRepeatingData = new ArchDescriptionRepeatingDataEditor(notesEditor);

        try {
			dialogRepeatingData.setCallingTable(repeatingDataTable);
		} catch (UnsupportedTableModelException e) {
			new ErrorDialog(getParentEditor(), "Unsupported table type", e).showDialog();
		}

        dialogRepeatingData.setNavigationButtonListeners(notesEditor);

        if (archDescriptionNotesModel.getDigitalObject() != null) {
			tagApplicatorPanel.setVisible(false);
			multiPart.setVisible(false);
			getNotesType().setModel(new DefaultComboBoxModel(NoteEtcTypesUtils.getDigitalObjectNotesTypesList()));
		} else {
			tagApplicatorPanel.setVisible(true);
			insertInlineTag.setModel(new DefaultComboBoxModel(InLineTagsUtils.getInLineTagList(archDescriptionNotesModel.getNotesEtcType().getNotesEtcName())));
			getNotesType().setModel(new DefaultComboBoxModel(NoteEtcTypesUtils.getNotesOnlyTypesList()));
			if (archDescriptionNotesModel.getNotesEtcType().getAllowsMultiPart()) {
				multiPart.setVisible(true);
			} else {
				multiPart.setVisible(false);
			}
		}

        // set the initial selection in the notes type
        getNotesType().setSelectedItem(archDescriptionNotesModel.getNotesEtcType());

        inSetModel = false;
	}

	public Component getInitialFocusComponent() {
		return notesType;
	}

	protected void initMenus() {

		Vector values = NoteEtcTypesUtils.getNotesEmbeddedTypesList();

		repeatingDataPopupMenu = new JPopupMenu();

		JMenu popupMenu = new JMenu("Insert");
		JMenuItem jPopupMenuItem;
		NotesEtcTypes noteType;
		for (Object value : values) {
			noteType = (NotesEtcTypes) value;
			jPopupMenuItem = new JMenuItem(noteType.getNotesEtcLabel());
			jPopupMenuItem.addActionListener(menuListenerAddBeforeSelection);
			popupMenu.add(jPopupMenuItem);
		}
		repeatingDataPopupMenu.add(popupMenu);

		repeatingDataPopupMenu.add(popupMenu);

		JMenuItem deleteMenuItem = new JMenuItem("Delete");
		deleteMenuItem.addActionListener(menuListenerDelete);
		repeatingDataPopupMenu.add(deleteMenuItem);

		values.add(0, "Add part...");
		addPartComboBox.setModel(new DefaultComboBoxModel(values));
	}

	public void setNotesEditor(DomainEditor notesEditor) {
		this.notesEditor = notesEditor;
	}
}
