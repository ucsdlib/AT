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
 * Created by JFormDesigner on Sun Nov 26 14:55:44 EST 2006
 */

package org.archiviststoolkit.swing;

import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.mydomain.*;

public class SelectFromTable extends ATBasicDialog {

	public SelectFromTable(Frame owner) {
		super(owner);
		initComponents();
	}

	public SelectFromTable(Dialog owner) {
		super(owner);
		initComponents();
	}

	public SelectFromTable(Class clazz, Dialog owner, String message, Collection items) {
		super(owner);
		initComponents();
		setClazz(clazz);
		this.setTitle("");
		this.setMessage(message);
		this.setListValues(items);
	}

	public SelectFromTable(Class clazz, Frame owner, String message, Collection items) {
		super(owner);
		initComponents();
		setClazz(clazz);
		this.setTitle("");
		this.setMessage(message);
		this.setListValues(items);
	}

	private void contentTableMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			performOkAction();
		}
	}

	private void cancelButtonActionPerformed() {
		performCancelAction();
	}

	private void selectButtonActionPerformed() {
		if (getContentTable().getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(this, "You must select an item");
		} else {
			performOkAction();
		}
	}

	public DomainSortableTable getContentTable() {
		return contentTable;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		message = new JLabel();
		scrollPane1 = new JScrollPane();
		contentTable = new DomainSortableTable();
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
					ColumnSpec.decodeSpecs("default:grow"),
					new RowSpec[] {
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
					}));

				//---- message ----
				message.setText("Select an item");
				contentPanel.add(message, cc.xy(1, 1));

				//======== scrollPane1 ========
				{

					//---- contentTable ----
					contentTable.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							contentTableMouseClicked(e);
						}
					});
					scrollPane1.setViewportView(contentTable);
				}
				contentPanel.add(scrollPane1, cc.xywh(1, 3, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
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
		contentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel message;
	private JScrollPane scrollPane1;
	private DomainSortableTable contentTable;
	private JPanel buttonBar;
	private JButton cancelButton;
	private JButton selectButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	private Class clazz;

	private void setListValues(Collection items) {
		contentTable.updateCollection(items);
	}



	public void setMessage(String message) {
		this.message.setText(message);
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
		getContentTable().setClazz(clazz, null);
	}

	public DomainObject getSelectedValue() {
		return (DomainObject)contentTable.getSortedList().get(contentTable.getSelectedRow());
	}


}
