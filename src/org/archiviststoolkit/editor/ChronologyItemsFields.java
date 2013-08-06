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
 * Created by JFormDesigner on Thu May 04 12:53:22 EDT 2006
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.model.ChronologyItems;
import org.archiviststoolkit.model.Events;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.StandardEditor;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.util.SequencedObjectsUtils;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.exceptions.DomainEditorCreationException;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.dialog.ErrorDialog;

public class ChronologyItemsFields extends DomainEditorFields {
	public ChronologyItemsFields() {
		super();
		initComponents();
		eventsTable.setTransferable();
	}

	private void addEventButtonActionPerformed(ActionEvent e) {
		ChronologyItems structuredDataModel = (ChronologyItems) super.getModel();
		DomainEditor dialogStructuredData = null;
		try {
			dialogStructuredData = DomainEditorFactory.getInstance().createDomainEditorWithParent(Events.class, this.getParentEditor(), false);
		} catch (DomainEditorCreationException e1) {
			new ErrorDialog(getParentEditor(), "Error creating editor for Events", e1).showDialog();

		}
		dialogStructuredData.setNewRecord(true);
		Boolean done = false;
		int sequenceNumber = 0;
		Boolean first = true;
		int returnStatus;
		Events newItem;
		String whereString = SequencedObjectsUtils.ADD_AT_END;

		while (!done) {
			newItem = new Events(structuredDataModel);
			if (first) {
				sequenceNumber = SequencedObjectsUtils.determineSequenceOfNewItem(whereString, getEventsTable());
				first = false;
			} else {
				sequenceNumber++;
			}
			newItem.setSequenceNumber(sequenceNumber);
			dialogStructuredData.setModel(newItem, null);
			returnStatus = dialogStructuredData.showDialog();
			if (returnStatus == javax.swing.JOptionPane.OK_OPTION) {
				structuredDataModel.addEvent(newItem);
				getEventsTable().getEventList().add(newItem);
				done = true;
			} else if (returnStatus == StandardEditor.OK_AND_ANOTHER_OPTION) {
				structuredDataModel.addEvent(newItem);
				getEventsTable().getEventList().add(newItem);
			} else {
				done = true;
			}
		}
		dialogStructuredData.setNewRecord(false);
	}

	private void removeEventButtonActionPerformed(ActionEvent e) {
		try {
			this.removeRelatedTableRow(getEventsTable(), null, super.getModel());
		} catch (ObjectNotRemovedException e1) {
			new ErrorDialog("Event not removed", e1).showDialog();
		}
		getEventsTable().updateCollection(((ChronologyItems)super.getModel()).getEvents());
	}

	public JButton getRemoveEventButton() {
		return removeEventButton;
	}

	public DomainSortedTable getEventsTable() {
		return eventsTable;
	}

	private void eventsTableMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			int selectedRow = getEventsTable().getSelectedRow();
			if (selectedRow != -1) {
				DomainObject currentNote = getEventsTable().getSortedList().get(selectedRow);
				DomainEditor dialog = null;
				try {
					dialog = DomainEditorFactory.getInstance().createDomainEditorWithParent(Events.class, getParentEditor(), getEventsTable(), super.getModel());
				} catch (DomainEditorCreationException e1) {
					new ErrorDialog(getParentEditor(), "Error creating editor for Events", e1).showDialog();

				}
				dialog.setSelectedRow(selectedRow);
				dialog.setModel(currentNote, null);
				dialog.setNavigationButtons();
				int status = dialog.showDialog();

				if (status == javax.swing.JOptionPane.OK_OPTION) {
					getEventsTable().getSortedList().set(getEventsTable().getSelectedRow(),
							getEventsTable().getSortedList().get(getEventsTable().getSelectedRow()));
				}
			}
		}
	}

	public JButton getAddEventButton() {
		return addEventButton;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label_subjectScopeNote = new JLabel();
        eventDate = ATBasicComponentFactory.createTextField(detailsModel.getModel(ChronologyItems.PROPERTYNAME_EVENT_DATE));
        label_subjectScopeNote2 = new JLabel();
        scrollPane7 = new JScrollPane();
        eventsTable = new DomainSortedTable(Events.class);
        panel30 = new JPanel();
        addEventButton = new JButton();
        removeEventButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBorder(Borders.DLU4_BORDER);
        setBackground(new Color(200, 205, 232));
        setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
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
                new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC
            }));

        //---- label_subjectScopeNote ----
        label_subjectScopeNote.setText("Event Date");
        label_subjectScopeNote.setVerticalAlignment(SwingConstants.TOP);
        label_subjectScopeNote.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectScopeNote, ChronologyItems.class, ChronologyItems.PROPERTYNAME_EVENT_DATE);
        add(label_subjectScopeNote, cc.xywh(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));
        add(eventDate, cc.xy(3, 1));

        //---- label_subjectScopeNote2 ----
        label_subjectScopeNote2.setText("Events");
        label_subjectScopeNote2.setVerticalAlignment(SwingConstants.TOP);
        label_subjectScopeNote2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectScopeNote2, ChronologyItems.class, ChronologyItems.PROPERTYNAME_EVENT_DATE);
        add(label_subjectScopeNote2, cc.xywh(1, 3, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

        //======== scrollPane7 ========
        {
            scrollPane7.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane7.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

            //---- eventsTable ----
            eventsTable.setFocusable(false);
            eventsTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    eventsTableMouseClicked(e);
                }
            });
            scrollPane7.setViewportView(eventsTable);
        }
        add(scrollPane7, cc.xywh(1, 5, 3, 1));

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

            //---- addEventButton ----
            addEventButton.setText("Add Event");
            addEventButton.setOpaque(false);
            addEventButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            addEventButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addEventButtonActionPerformed(e);
                }
            });
            panel30.add(addEventButton, cc.xy(1, 1));

            //---- removeEventButton ----
            removeEventButton.setText("Remove Event");
            removeEventButton.setOpaque(false);
            removeEventButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            removeEventButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    removeEventButtonActionPerformed(e);
                }
            });
            panel30.add(removeEventButton, cc.xy(3, 1));
        }
        add(panel30, cc.xywh(1, 7, 3, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label_subjectScopeNote;
    public JTextField eventDate;
    private JLabel label_subjectScopeNote2;
    private JScrollPane scrollPane7;
    private DomainSortedTable eventsTable;
    private JPanel panel30;
    private JButton addEventButton;
    private JButton removeEventButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public Component getInitialFocusComponent() {
		return eventDate;
	}

	public void setModel(DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		ChronologyItems chronologyListItemModel = (ChronologyItems) model;
		getEventsTable().updateCollection(chronologyListItemModel.getEvents());
	}
}
