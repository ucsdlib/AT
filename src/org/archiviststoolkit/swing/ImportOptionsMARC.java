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
 * Created by JFormDesigner on Mon Sep 25 16:42:10 EDT 2006
 */

package org.archiviststoolkit.swing;

import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.model.Repositories;
import org.archiviststoolkit.model.LookupList;
import org.archiviststoolkit.model.Users;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.util.LookupListUtils;

public class ImportOptionsMARC extends JPanel implements ImportOptions{
	public ImportOptionsMARC() {
		initComponents();
		initComboBoxes();
		setRepository(ApplicationFrame.getInstance().getCurrentUser().getRepository());
	}

	public String getResourceIdentifier1() {
		return resourceIdentifier1.getText();
	}

	public String getResourceIdentifier2() {
		return resourceIdentifier2.getText();
	}

	public String getResourceIdentifier3() {
		return resourceIdentifier3.getText();
	}

	public String getResourceIdentifier4() {
		return resourceIdentifier4.getText();
	}

	public boolean getImportEntireRecord() {
		return entireMarcRecord.isSelected();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		label1 = new JLabel();
		repositorySelector = new JComboBox();
		label2 = new JLabel();
		resourceLevelSelector = ATBasicComponentFactory.createUnboundComboBox(LookupListUtils.getLookupListValues("Resource levels"));
		label3 = new JLabel();
		panel1 = new JPanel();
		resourceIdentifier1 = new JTextField();
		resourceIdentifier2 = new JTextField();
		resourceIdentifier3 = new JTextField();
		resourceIdentifier4 = new JTextField();
		label4 = new JLabel();
		panel2 = new JPanel();
		entireMarcRecord = new JRadioButton();
		subjectHeadingsOnly = new JRadioButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBorder(Borders.DLU2_BORDER);
		setLayout(new FormLayout(
			ColumnSpec.decodeSpecs("default:grow"),
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC
			}));

		//---- label1 ----
		label1.setText("Select a Repository");
		add(label1, cc.xy(1, 1));
		add(repositorySelector, cc.xywh(1, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

		//---- label2 ----
		label2.setText("Resource Level");
		add(label2, cc.xy(1, 5));
		add(resourceLevelSelector, cc.xywh(1, 7, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

		//---- label3 ----
		label3.setText("Resource Identifier");
		add(label3, cc.xy(1, 9));

		//======== panel1 ========
		{
			panel1.setLayout(new FormLayout(
				new ColumnSpec[] {
					new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
				},
				RowSpec.decodeSpecs("default")));
			((FormLayout)panel1.getLayout()).setColumnGroups(new int[][] {{1, 3, 5, 7}});
			panel1.add(resourceIdentifier1, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
			panel1.add(resourceIdentifier2, cc.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
			panel1.add(resourceIdentifier3, cc.xywh(5, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
			panel1.add(resourceIdentifier4, cc.xywh(7, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		}
		add(panel1, cc.xywh(1, 11, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

		//---- label4 ----
		label4.setText("Import");
		add(label4, cc.xy(1, 13));

		//======== panel2 ========
		{
			panel2.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC
				},
				RowSpec.decodeSpecs("default")));

			//---- entireMarcRecord ----
			entireMarcRecord.setText("Entire MARC record");
			panel2.add(entireMarcRecord, cc.xy(1, 1));

			//---- subjectHeadingsOnly ----
			subjectHeadingsOnly.setText("Subject headings only");
			panel2.add(subjectHeadingsOnly, cc.xy(3, 1));
		}
		add(panel2, cc.xy(1, 15));

		//---- buttonGroup1 ----
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(entireMarcRecord);
		buttonGroup1.add(subjectHeadingsOnly);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel label1;
	private JComboBox repositorySelector;
	private JLabel label2;
	private JComboBox resourceLevelSelector;
	private JLabel label3;
	private JPanel panel1;
	private JTextField resourceIdentifier1;
	private JTextField resourceIdentifier2;
	private JTextField resourceIdentifier3;
	private JTextField resourceIdentifier4;
	private JLabel label4;
	private JPanel panel2;
	private JRadioButton entireMarcRecord;
	private JRadioButton subjectHeadingsOnly;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	private void initComboBoxes () {
        if(Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_SUPERUSER)) { // super user can view all repositories
            this.repositorySelector.setModel(new DefaultComboBoxModel(Repositories.getRepositoryList().toArray()));
        } else { // must be user level 4 so only show their repository
            DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
            comboBoxModel.addElement(ApplicationFrame.getInstance().getCurrentUser().getRepository());
            this.repositorySelector.setModel(comboBoxModel);
        }
	}

	public Repositories getRepository() {
		return (Repositories)repositorySelector.getSelectedItem();
	}

	public void setRepository(Repositories repository) {
		repositorySelector.setSelectedItem(repository);
	}

	public String getResourceLevel() {
		return (String)resourceLevelSelector.getSelectedItem();
	}

	public boolean allDataEntered() {
		if (resourceLevelSelector.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(this, "You must select a level");
			return false;
		} else if (!entireMarcRecord.isSelected() && !subjectHeadingsOnly.isSelected()) {
			JOptionPane.showMessageDialog(this, "You must select to either import entire record or headings only");
			return false;
		} else if (resourceIdentifier1.getText().length() == 0) {
			JOptionPane.showMessageDialog(this, "You must enter a resource identifier");
			return false;
		} else {
			return true;
		}
	}
}
