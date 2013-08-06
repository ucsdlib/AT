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
 * Created by JFormDesigner on Thu Aug 24 14:55:22 EDT 2006
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.ListOrdered;
import org.archiviststoolkit.model.ListOrderedItems;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.InLineTags;

public class ListOrderedFields extends ArchDescriptionStructuredDataFields {
	protected ListOrderedFields(DomainEditor parentEditor) {
		super();
		this.setParentEditor(parentEditor);
		initComponents();
		super.init();
	}

	public Component getInitialFocusComponent() {
		return null;
	}

	private void itemsTableMousePressed(MouseEvent e) {
		if (e.isPopupTrigger()) {
			insertItemPopUpMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	private void itemsTableMouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			insertItemPopUpMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	private void removeItemButtonActionPerformed(ActionEvent e) {
		super.removeItem();
	}

	public DomainSortedTable getItemsTable() {
		return itemsTable;
	}

	private void itemsTableMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			super.handleTableMouseClicked(ListOrderedItems.class);
		}
	}

	private void addItemButtonActionPerformed(ActionEvent e) {
		super.addItemButtonAction();
	}

	public JButton getAddItemButton() {
		return addItemButton;
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
        label_subjectTerm = new JLabel();
        title = ATBasicComponentFactory.createTextField(detailsModel.getModel(ListOrdered.PROPERTYNAME_TITLE));
        label_subjectScopeNote3 = new JLabel();
        unitType = ATBasicComponentFactory.createComboBox(detailsModel, ListOrdered.PROPERTYNAME_NUMERATION, ListOrdered.class);
        label_subjectScopeNote2 = new JLabel();
        scrollPane2 = new JScrollPane();
        ingestProblems = ATBasicComponentFactory.createTextArea(detailsModel.getModel(ListOrdered.PROPERTYNAME_EAD_INGEST_PROBLEMS));
        scrollPane7 = new JScrollPane();
        itemsTable = new DomainSortedTable(ListOrderedItems.class);
        panel30 = new JPanel();
        addItemButton = new JButton();
        removeItemButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBorder(Borders.DLU4_BORDER);
        setOpaque(false);
        setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        setPreferredSize(new Dimension(600, 500));
        setLayout(new FormLayout(
            new ColumnSpec[] {
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
            },
            new RowSpec[] {
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC
            }));

        //---- label_subjectTerm ----
        label_subjectTerm.setText("Title");
        label_subjectTerm.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectTerm, ListOrdered.class, ListOrdered.PROPERTYNAME_TITLE);
        add(label_subjectTerm, cc.xy(1, 1));
        add(title, cc.xy(3, 1));

        //---- label_subjectScopeNote3 ----
        label_subjectScopeNote3.setText("Numeration");
        label_subjectScopeNote3.setVerticalAlignment(SwingConstants.TOP);
        label_subjectScopeNote3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectScopeNote3, ListOrdered.class, ListOrdered.PROPERTYNAME_NUMERATION);
        add(label_subjectScopeNote3, cc.xywh(1, 3, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

        //---- unitType ----
        unitType.setMaximumSize(new Dimension(50, 27));
        unitType.setOpaque(false);
        unitType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(unitType, cc.xywh(3, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- label_subjectScopeNote2 ----
        label_subjectScopeNote2.setText("Ingest Problems");
        label_subjectScopeNote2.setVerticalAlignment(SwingConstants.TOP);
        label_subjectScopeNote2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectScopeNote2, ListOrdered.class, ListOrdered.PROPERTYNAME_EAD_INGEST_PROBLEMS);
        add(label_subjectScopeNote2, cc.xywh(1, 5, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

        //======== scrollPane2 ========
        {
            scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane2.setMaximumSize(new Dimension(32767, 100));
            scrollPane2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

            //---- ingestProblems ----
            ingestProblems.setRows(4);
            ingestProblems.setLineWrap(true);
            ingestProblems.setTabSize(20);
            ingestProblems.setWrapStyleWord(true);
            scrollPane2.setViewportView(ingestProblems);
        }
        add(scrollPane2, cc.xy(3, 5));

        //======== scrollPane7 ========
        {
            scrollPane7.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane7.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

            //---- itemsTable ----
            itemsTable.setFocusable(false);
            itemsTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    itemsTableMouseClicked(e);
                }
                @Override
                public void mousePressed(MouseEvent e) {
                    itemsTableMousePressed(e);
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    itemsTableMouseReleased(e);
                }
            });
            scrollPane7.setViewportView(itemsTable);
        }
        add(scrollPane7, cc.xywh(1, 7, 3, 1));

        //======== panel30 ========
        {
            panel30.setBackground(new Color(231, 188, 251));
            panel30.setOpaque(false);
            panel30.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            panel30.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC
                },
                RowSpec.decodeSpecs("default")));

            //---- addItemButton ----
            addItemButton.setText("Add Item");
            addItemButton.setOpaque(false);
            addItemButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            addItemButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addItemButtonActionPerformed(e);
                }
            });
            panel30.add(addItemButton, cc.xy(1, 1));

            //---- removeItemButton ----
            removeItemButton.setBackground(new Color(231, 188, 251));
            removeItemButton.setText("Remove Item");
            removeItemButton.setOpaque(false);
            removeItemButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            removeItemButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    removeItemButtonActionPerformed(e);
                }
            });
            panel30.add(removeItemButton, cc.xy(3, 1));
        }
        add(panel30, cc.xywh(1, 9, 3, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label_subjectTerm;
    public JTextField title;
    private JLabel label_subjectScopeNote3;
    public JComboBox unitType;
    private JLabel label_subjectScopeNote2;
    private JScrollPane scrollPane2;
    public JTextArea ingestProblems;
    private JScrollPane scrollPane7;
    private DomainSortedTable itemsTable;
    private JPanel panel30;
    private JButton addItemButton;
    private JButton removeItemButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public void setModel(DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		ListOrdered listOrderedModel = (ListOrdered)model;
		itemsTable.updateCollection(listOrderedModel.getListItems());
	}

}
