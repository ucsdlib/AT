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
 * Created by JFormDesigner on Wed Dec 06 14:56:20 EST 2006
 */

package org.archiviststoolkit.maintenance.initDatabase;

import java.awt.*;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.netbeans.spi.wizard.WizardPage;

public class RepositoryInformation extends WizardPage {
	public RepositoryInformation() {
		super("repositoryInfo", "Repository Information");
		initComponents();
	}

	protected String validateContents(Component component, Object event) {
		if (getRepositoryName().getText().length() == 0) {
			return "Please enter the Repository name";
		}
		if (getShortName().getText().length() == 0) {
			return "Please enter the short name";
		}
		// everything is ok
		return null;
	}

	public JTextField getRepositoryName() {
		return repositoryName;
	}

	public JTextField getShortName() {
		return shortName;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		label1 = new JLabel();
		repositoryName = new JTextField();
		label15 = new JLabel();
		shortName = new JTextField();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setName("repositoryName");
		setLayout(new FormLayout(
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

		//---- label1 ----
		label1.setText("Repository Name");
		label1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(label1, cc.xy(1, 1));

		//---- repositoryName ----
		repositoryName.setName("repositoryName");
		add(repositoryName, new CellConstraints(3, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 5, 0, 0)));

		//---- label15 ----
		label15.setText("Short Name");
		label15.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(label15, cc.xy(1, 3));

		//---- shortName ----
		shortName.setName("shortName");
		add(shortName, new CellConstraints(3, 3, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 5, 0, 0)));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel label1;
	private JTextField repositoryName;
	private JLabel label15;
	private JTextField shortName;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
