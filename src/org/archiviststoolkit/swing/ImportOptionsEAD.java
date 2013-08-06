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
 * Created by JFormDesigner on Mon Sep 25 15:48:27 EDT 2006
 */

package org.archiviststoolkit.swing;

import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.model.Repositories;
import org.archiviststoolkit.model.Users;
import org.archiviststoolkit.ApplicationFrame;

public class ImportOptionsEAD extends JPanel implements ImportOptions{
	public ImportOptionsEAD() {
		initComponents();
		initComboBoxes();
		setRepository(ApplicationFrame.getInstance().getCurrentUser().getRepository());
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		label1 = new JLabel();
		repositorySelector = new JComboBox();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBorder(Borders.DLU2_BORDER);
		setLayout(new FormLayout(
			ColumnSpec.decodeSpecs("default"),
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC
			}));

		//---- label1 ----
		label1.setText("Select a Repository");
		add(label1, cc.xy(1, 1));
		add(repositorySelector, cc.xywh(1, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel label1;
	private JComboBox repositorySelector;
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

	public boolean allDataEntered() {
		return true;
	}
}
