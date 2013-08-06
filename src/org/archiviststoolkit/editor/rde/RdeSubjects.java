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
import java.util.ArrayList;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.exceptions.RDEPopulateException;
import org.archiviststoolkit.exceptions.RDEPanelCreationException;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.dialog.SubjectTermLookup;
import org.archiviststoolkit.editor.SubjectEnabledEditorFields;

public class RdeSubjects extends RdePanel implements SubjectEnabledModel, SubjectEnabledEditorFields {

	private boolean sticky = false;

	public RdeSubjects(RdePanelContainer parentPanel) {
		super(parentPanel);
		initComponents();
	}

	public void populateComponent(ResourcesComponents component) throws RDEPopulateException {

		ArchDescriptionSubjects link;
		for (DomainObject domainObject: subjectsTable.getSortedList()) {
			 link = (ArchDescriptionSubjects)domainObject;
			component.addSubject(new ArchDescriptionSubjects(link.getSubject(), component));
		}
	}

	public void clearFields() {
		if (!sticky) {
			subjectsTable.updateCollection(null);
		}
	}

	public void setStickyLabels() {
		setLabelColor(sticky, subjectsLabel);
	}

	public void initializeStickyLabels(Collection<RDEScreenPanelItems> panelItems) throws RDEPanelCreationException {
		for(RDEScreenPanelItems panelItem: panelItems) {
			if (panelItem.getPropertyName().equals(ArchDescriptionSubjects.PROPERTYNAME_SUBJECT)) {
				this.sticky = panelItem.getSticky();
			} else {
				throw new RDEPanelCreationException(panelItem.getPropertyName() + "is not supported here");
			}
		}
	}

	private void addSubjectRelationshipButtonActionPerformed() {
		SubjectTermLookup subjectTermLookupDialog = new SubjectTermLookup(this);
		subjectTermLookupDialog.setMainHeaderByClass(Resources.class);
		if (subjectTermLookupDialog.showDialog() == JOptionPane.OK_OPTION) {
			Subjects selectedSubject = subjectTermLookupDialog.getSelectedSubject();
			try {
				addSubject(selectedSubject);
			} catch (DuplicateLinkException e) {
				JOptionPane.showMessageDialog(this, e.getMessage() + " is already linked to this record");
			}
		}
	}

	private void removeSubjectRelationshipButtonActionPerformed() {
		int selectedRow = subjectsTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "You must select a row to remove.", "warning", JOptionPane.WARNING_MESSAGE);
		} else {
           int response = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete " + subjectsTable.getSelectedRows().length + " record(s)",
                    "Delete records", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.OK_OPTION) {
				subjectsTable.removeSelectedRows();
				int rowCount = subjectsTable.getRowCount();
                if (rowCount == 0) {
                    // do nothing
                } else if (selectedRow >= rowCount) {
                    subjectsTable.setRowSelectionInterval(rowCount - 1, rowCount - 1);
                } else {
                    subjectsTable.setRowSelectionInterval(selectedRow, selectedRow);
                }
            }
        }
	}

	private void SubjectsLabelMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			sticky = !sticky;
			setStickyLabels();
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		separator3 = new JSeparator();
		subjectsLabel = new JLabel();
		scrollPane1 = new JScrollPane();
		subjectsTable = new DomainSortableTable(ArchDescriptionSubjects.class, ArchDescriptionSubjects.PROPERTYNAME_SUBJECT_TERM);
		panel11 = new JPanel();
		addSubjectRelationshipButton = new JButton();
		removeSubjectRelationshipButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBackground(new Color(200, 205, 232));
		setLayout(new FormLayout(
			ColumnSpec.decodeSpecs("default:grow"),
			new RowSpec[] {
				new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC
			}));

		//---- separator3 ----
		separator3.setBackground(new Color(220, 220, 232));
		separator3.setForeground(new Color(147, 131, 86));
		separator3.setMinimumSize(new Dimension(1, 10));
		separator3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(separator3, cc.xywh(1, 1, 1, 2, CellConstraints.DEFAULT, CellConstraints.CENTER));

		//---- subjectsLabel ----
		subjectsLabel.setText("Subject link");
		subjectsLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		subjectsLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SubjectsLabelMouseClicked(e);
			}
		});
		add(subjectsLabel, cc.xy(1, 3));

		//======== scrollPane1 ========
		{
			scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

			//---- subjectsTable ----
			subjectsTable.setFocusable(false);
			subjectsTable.setPreferredScrollableViewportSize(new Dimension(450, 100));
			scrollPane1.setViewportView(subjectsTable);
		}
		add(scrollPane1, cc.xy(1, 5));

		//======== panel11 ========
		{
			panel11.setBackground(new Color(231, 188, 251));
			panel11.setOpaque(false);
			panel11.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			panel11.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC
				},
				RowSpec.decodeSpecs("default")));

			//---- addSubjectRelationshipButton ----
			addSubjectRelationshipButton.setBackground(new Color(231, 188, 251));
			addSubjectRelationshipButton.setText("Add Subject Link");
			addSubjectRelationshipButton.setOpaque(false);
			addSubjectRelationshipButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			addSubjectRelationshipButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addSubjectRelationshipButtonActionPerformed();
				}
			});
			panel11.add(addSubjectRelationshipButton, cc.xy(1, 1));

			//---- removeSubjectRelationshipButton ----
			removeSubjectRelationshipButton.setBackground(new Color(231, 188, 251));
			removeSubjectRelationshipButton.setText("Remove Subject Link");
			removeSubjectRelationshipButton.setOpaque(false);
			removeSubjectRelationshipButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			removeSubjectRelationshipButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					removeSubjectRelationshipButtonActionPerformed();
				}
			});
			panel11.add(removeSubjectRelationshipButton, cc.xy(3, 1));
		}
		add(panel11, cc.xywh(1, 7, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JSeparator separator3;
	private JLabel subjectsLabel;
	private JScrollPane scrollPane1;
	private DomainSortableTable subjectsTable;
	private JPanel panel11;
	private JButton addSubjectRelationshipButton;
	private JButton removeSubjectRelationshipButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	public DomainObject addSubject(Subjects subject) throws DuplicateLinkException {
		ArchDescriptionSubjects subjectLink = new ArchDescriptionSubjects();
		subjectLink.setSubject(subject);
		return subjectLink;
	}

	public SubjectEnabledModel getSubjectEnabledModel() {
		return this;
	}

	public DomainSortableTable getSubjectsTable() {
		return subjectsTable;
	}
}