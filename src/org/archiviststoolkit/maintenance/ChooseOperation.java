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
 * Created by JFormDesigner on Tue Jan 23 10:24:17 EST 2007
 */

package org.archiviststoolkit.maintenance;

import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.netbeans.spi.wizard.WizardPage;
import org.netbeans.spi.wizard.WizardController;
import org.archiviststoolkit.ApplicationFrame;

public class ChooseOperation extends WizardPage {

	public static final String VALUE_INITIALIZE = "init";
	public static final String VALUE_UPGRADE = "upgrade";
	public static final String VALUE_UTILITIES = "utilities";

	private String operation = "";

	public ChooseOperation() {
		super("one", "Choose Operation");
		initComponents();
		setProblem("No operation selected");
		version.setText("version " + ApplicationFrame.getInstance().getAtVersionNumber());
	}

	public static String getDescription() {
		return "Select Operation";
	}

	protected String validateContents(Component component, Object event) {
		if (!initializeButton.isSelected() && !upgradeButton.isSelected() && !utilitiesButton.isSelected()) {
			return "No operation selected";
		}
		return null;
	}

	private void initializeButtonActionPerformed() {
		setOperation(VALUE_INITIALIZE);
	}

	private void utilitiesButtonActionPerformed() {
		setOperation(VALUE_UTILITIES);
	}

	private void upgradeButtonActionPerformed() {
		setOperation(VALUE_UPGRADE);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		label1 = new JLabel();
		version = new JLabel();
		initializeButton = new JRadioButton();
		upgradeButton = new JRadioButton();
		utilitiesButton = new JRadioButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
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
				FormFactory.DEFAULT_ROWSPEC
			}));

		//---- label1 ----
		label1.setText("<html><center>Welcome to the<br>Archivists' Toolkit<br>Maintenance  Program</center></html>");
		label1.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		add(label1, cc.xywh(1, 1, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

		//---- version ----
		version.setText("version");
		version.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(version, cc.xywh(1, 3, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

		//---- initializeButton ----
		initializeButton.setText("Initialize a blank database");
		initializeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initializeButtonActionPerformed();
			}
		});
		add(initializeButton, cc.xywh(1, 5, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

		//---- upgradeButton ----
		upgradeButton.setText("Upgrade an existing database");
		upgradeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				upgradeButtonActionPerformed();
			}
		});
		add(upgradeButton, cc.xywh(1, 7, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

		//---- utilitiesButton ----
		utilitiesButton.setText("Utilities");
		utilitiesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				utilitiesButtonActionPerformed();
			}
		});
		add(utilitiesButton, cc.xywh(1, 9, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

		//---- buttonGroup1 ----
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(initializeButton);
		buttonGroup1.add(upgradeButton);
		buttonGroup1.add(utilitiesButton);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel label1;
	private JLabel version;
	private JRadioButton initializeButton;
	private JRadioButton upgradeButton;
	private JRadioButton utilitiesButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public String getOperation() {
		return operation;
	}

	private void setOperation(String operation) {
		this.operation = operation;
	}
}
