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
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.exceptions.RDEPopulateException;
import org.archiviststoolkit.exceptions.RDEPanelCreationException;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.UnsupportedDomainObjectModelException;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.dialog.NameAuthorityLookup;
import org.archiviststoolkit.editor.NameEnabledEditor;

public class RdeNames extends RdePanel implements NameEnabledModel, NameEnabledEditor {

	private boolean sticky = false;

	public RdeNames(RdePanelContainer parentPanel) {
		super(parentPanel);
		initComponents();
	}

	public void populateComponent(ResourcesComponents component) throws RDEPopulateException {

		ArchDescriptionNames link;
		for (DomainObject domainObject: namesTable.getSortedList()) {
			 link = (ArchDescriptionNames)domainObject;
			try {
				component.addName(new ArchDescriptionNames(link.getName(), component, link.getRole(),link.getNameLinkFunction(), link.getForm()));
			} catch (DuplicateLinkException e) {
				new ErrorDialog("", e).showDialog();
			}
		}
	}

	public void clearFields() {
		if (!sticky) {
			namesTable.updateCollection(null);
		}
	}

	public void setStickyLabels() {
		setLabelColor(sticky, namesLabel);
	}

	public void initializeStickyLabels(Collection<RDEScreenPanelItems> panelItems) throws RDEPanelCreationException {
		for(RDEScreenPanelItems panelItem: panelItems) {
			if (panelItem.getPropertyName().equals(ArchDescriptionNames.PROPERTYNAME_NAME)) {
				this.sticky = panelItem.getSticky();
			} else {
				throw new RDEPanelCreationException(panelItem.getPropertyName() + "is not supported here");
			}
		}
	}

	private void addNameRelationshipButtonActionPerformed() {
		NameAuthorityLookup nameLookupDialog = new NameAuthorityLookup(this);
		nameLookupDialog.setMainHeaderByClass(Resources.class);
		try {
			nameLookupDialog.setFunctionLookupvalues(ResourcesComponents.class);
		} catch (UnsupportedDomainObjectModelException e) {
			new ErrorDialog("Error creating name lookup dialog", e).showDialog();
		}
		if (nameLookupDialog.showDialog() == JOptionPane.OK_OPTION) {
			Names selectedname = nameLookupDialog.getSelectedName();
			try {
				addName(selectedname, nameLookupDialog.getFunction(), nameLookupDialog.getForm(), nameLookupDialog.getRole());
			} catch (DuplicateLinkException e) {
				JOptionPane.showMessageDialog(this, e.getMessage() + " is already linked to this record");
			}
		}
	}

	private void removeNameRelationshipButtonActionPerformed() {
		int selectedRow = namesTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "You must select a row to remove.", "warning", JOptionPane.WARNING_MESSAGE);
		} else {
           int response = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete " + namesTable.getSelectedRows().length + " record(s)",
                    "Delete records", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.OK_OPTION) {
				namesTable.removeSelectedRows();
				int rowCount = namesTable.getRowCount();
                if (rowCount == 0) {
                    // do nothing
                } else if (selectedRow >= rowCount) {
                    namesTable.setRowSelectionInterval(rowCount - 1, rowCount - 1);
                } else {
                    namesTable.setRowSelectionInterval(selectedRow, selectedRow);
                }
            }
        }
	}

	private void NamesLabelMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			sticky = !sticky;
			setStickyLabels();
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		separator3 = new JSeparator();
		namesLabel = new JLabel();
		scrollPane1 = new JScrollPane();
		namesTable = new DomainSortableTable(ArchDescriptionNames.class, ArchDescriptionNames.PROPERTYNAME_SORT_NAME);
		panel11 = new JPanel();
		addNameRelationshipButton = new JButton();
		removeNameRelationshipButton = new JButton();
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

		//---- namesLabel ----
		namesLabel.setText("Name link");
		namesLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		namesLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				NamesLabelMouseClicked(e);
			}
		});
		add(namesLabel, cc.xy(1, 3));

		//======== scrollPane1 ========
		{
			scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

			//---- namesTable ----
			namesTable.setFocusable(false);
			namesTable.setPreferredScrollableViewportSize(new Dimension(450, 100));
			scrollPane1.setViewportView(namesTable);
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

			//---- addNameRelationshipButton ----
			addNameRelationshipButton.setBackground(new Color(231, 188, 251));
			addNameRelationshipButton.setText("Add Name Link");
			addNameRelationshipButton.setOpaque(false);
			addNameRelationshipButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			addNameRelationshipButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addNameRelationshipButtonActionPerformed();
				}
			});
			panel11.add(addNameRelationshipButton, cc.xy(1, 1));

			//---- removeNameRelationshipButton ----
			removeNameRelationshipButton.setBackground(new Color(231, 188, 251));
			removeNameRelationshipButton.setText("Remove Name Link");
			removeNameRelationshipButton.setOpaque(false);
			removeNameRelationshipButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			removeNameRelationshipButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					removeNameRelationshipButtonActionPerformed();
				}
			});
			panel11.add(removeNameRelationshipButton, cc.xy(3, 1));
		}
		add(panel11, cc.xywh(1, 7, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JSeparator separator3;
	private JLabel namesLabel;
	private JScrollPane scrollPane1;
	private DomainSortableTable namesTable;
	private JPanel panel11;
	private JButton addNameRelationshipButton;
	private JButton removeNameRelationshipButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public DomainObject addName(Names name, String function, String role, String form) throws DuplicateLinkException {
		ArchDescriptionNames nameLink = new ArchDescriptionNames();
		nameLink.setName(name);
		nameLink.setForm(form);
		nameLink.setNameLinkFunction(function);
		nameLink.setRole(role);
		return nameLink;
	}

	public NameEnabledModel getNameEnabledModel() {
		return this;
	}

	public DomainSortableTable getNamesTable() {
		return namesTable;
	}
}