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
 * Date: Jan 23, 2007
 * Time: 10:20:55 AM
 */

package org.archiviststoolkit.maintenance;

import org.netbeans.spi.wizard.WizardBranchController;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardPage;
import org.netbeans.api.wizard.WizardDisplayer;
import org.archiviststoolkit.maintenance.initDatabase.*;
import org.archiviststoolkit.maintenance.common.ConnectionInformation;
import org.archiviststoolkit.maintenance.common.UserInformation;
import org.archiviststoolkit.maintenance.common.ResultProducer;
import org.archiviststoolkit.maintenance.utilities.SelectUtility;
import org.archiviststoolkit.maintenance.upgrades.UpgradeOptions;
import org.archiviststoolkit.Main;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class MaintenanceWizard extends WizardBranchController {

	private static ChooseOperation chooseOperationPanel;

	private static WizardPage[] initializePages = new WizardPage[]{
			new ConnectionInformation(ChooseOperation.VALUE_INITIALIZE),
			new RepositoryInformation(),
			new UserInformation(ChooseOperation.VALUE_INITIALIZE),
	};

	private static WizardPage[] upgradePages = new WizardPage[]{
			new ConnectionInformation(ChooseOperation.VALUE_UPGRADE),
			new UserInformation(ChooseOperation.VALUE_UPGRADE),
			new UpgradeOptions()
	};

	private static WizardPage[] utilitiesPages = new WizardPage[]{
			new ConnectionInformation(ChooseOperation.VALUE_UTILITIES),
			new UserInformation(ChooseOperation.VALUE_UTILITIES),
			new SelectUtility()
	};

	MaintenanceWizard() {
		super(chooseOperationPanel);
	}

	public static void main(String[] ignored) throws Exception {

		Main.checkJavaVersion();

		//Use native L&F
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		chooseOperationPanel = new ChooseOperation();
		WizardDisplayer.showWizard(new MaintenanceWizard().createWizard(),
				new Rectangle(50, 50, 800, 400));
		System.exit(0);
	}

	protected Wizard getWizardForStep(String mainHeader, Map map) {
		if (chooseOperationPanel.getOperation().equals(ChooseOperation.VALUE_INITIALIZE)) {
			return WizardPage.createWizard(initializePages, new ResultProducer(ChooseOperation.VALUE_INITIALIZE));
		} else if (chooseOperationPanel.getOperation().equals(ChooseOperation.VALUE_UPGRADE)) {
			return WizardPage.createWizard(upgradePages, new ResultProducer(ChooseOperation.VALUE_UPGRADE));
		} else if (chooseOperationPanel.getOperation().equals(ChooseOperation.VALUE_UTILITIES)) {
			return WizardPage.createWizard(utilitiesPages, new ResultProducer(ChooseOperation.VALUE_UTILITIES));
		} else {
			return null;
		}
	}

}
