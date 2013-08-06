/**
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
 * @author Lee Mandell
 * Created by JFormDesigner on Thu Nov 09 20:35:18 EST 2006
 */

package org.archiviststoolkit.swing;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.model.LookupListItems;
import org.archiviststoolkit.mydomain.DomainObject;

public class SelectFromList extends ATBasicDialog {

	public SelectFromList(Frame owner) {
		super(owner);
		initComponents();
	}

	public SelectFromList(Dialog owner) {
		super(owner);
		initComponents();
	}

	public SelectFromList(Dialog owner, String message, Object[] items) {
		super(owner);
		initComponents();
		this.setTitle("");
		this.setMessage(message);
		this.setListValues(items);
	}

	public SelectFromList(Frame owner, String message, Object[] items) {
		super(owner);
		initComponents();
		this.setTitle("");
		this.setMessage(message);
		this.setListValues(items);
	}

	public SelectFromList(Dialog owner, String message, Vector items) {
		super(owner);
		initComponents();
		this.setTitle("");
		this.setMessage(message);
		this.setListValues(items);
	}

	public SelectFromList(Frame owner, String message, Vector items) {
		super(owner);
		initComponents();
		this.setTitle("");
		this.setMessage(message);
		this.setListValues(items);
	}

	private void selectButtonActionPerformed() {
		if (listOfValues.getSelectedIndex() == -1) {
			JOptionPane.showMessageDialog(this, "You must select an item first");
		} else {
			performOkAction();
		}
	}

	private void cancelButtonActionPerformed() {
		performCancelAction();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		message = new JLabel();
		scrollPane1 = new JScrollPane();
		listOfValues = new JList();
		buttonBar = new JPanel();
		cancelButton = new JButton();
		selectButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle("title");
		setModal(true);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(Borders.DIALOG_BORDER);
			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{
				contentPanel.setLayout(new FormLayout(
					ColumnSpec.decodeSpecs("default"),
					new RowSpec[] {
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC
					}));

				//---- message ----
				message.setText("Select an item");
				contentPanel.add(message, cc.xy(1, 1));

				//======== scrollPane1 ========
				{

					//---- listOfValues ----
					listOfValues.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					scrollPane1.setViewportView(listOfValues);
				}
				contentPanel.add(scrollPane1, cc.xy(1, 3));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			//======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
				buttonBar.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.GLUE_COLSPEC,
						FormFactory.BUTTON_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.BUTTON_COLSPEC
					},
					RowSpec.decodeSpecs("pref")));

				//---- cancelButton ----
				cancelButton.setText("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancelButtonActionPerformed();
					}
				});
				buttonBar.add(cancelButton, cc.xy(4, 1));

				//---- selectButton ----
				selectButton.setText("Select");
				selectButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						selectButtonActionPerformed();
					}
				});
				buttonBar.add(selectButton, cc.xy(6, 1));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel message;
	private JScrollPane scrollPane1;
	private JList listOfValues;
	private JPanel buttonBar;
	private JButton cancelButton;
	private JButton selectButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public void setMessage(String message) {
		this.message.setText(message);
	}

	public void setListValues(Object[] items) {
		this.listOfValues.setModel(new DefaultComboBoxModel(items));
	}

	public void setListValues(Vector items) {
		this.listOfValues.setModel(new DefaultComboBoxModel(items));
	}

	public DomainObject getSelectedValue() {
		return (DomainObject)listOfValues.getSelectedValue();
	}

    public void setSelectedValue(DomainObject value) {
        listOfValues.setSelectedValue(value, true);
    }

    public void setCellRenderer(ListCellRenderer renderer) {
		listOfValues.setCellRenderer(renderer);
	}

}
