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
 * Time: 2:30:57 PM
 */

package org.archiviststoolkit.maintenance.common;

import org.netbeans.spi.wizard.WizardException;
import org.netbeans.spi.wizard.WizardPage;
import org.archiviststoolkit.maintenance.ChooseOperation;
import org.archiviststoolkit.maintenance.utilities.UtilitiesBackgroundResultCreator;
import org.archiviststoolkit.maintenance.upgrades.UpgradeDatabaseBackgroundResultCreator;
import org.archiviststoolkit.maintenance.initDatabase.InitDatabaseBackgroundResultCreator;

import javax.swing.*;
import java.util.Map;

public class ResultProducer implements WizardPage.WizardResultProducer{

    private String wizardType = "";

    public ResultProducer(String wizardType) {
        this.wizardType = wizardType;
    }


	public Object finish(Map wizardData) throws WizardException {
        if (wizardType.equals(ChooseOperation.VALUE_INITIALIZE)) {
            return new InitDatabaseBackgroundResultCreator();
		} else if (wizardType.equals(ChooseOperation.VALUE_UPGRADE)){
			return new UpgradeDatabaseBackgroundResultCreator();
		} else if (wizardType.equals(ChooseOperation.VALUE_UTILITIES)){
			return new UtilitiesBackgroundResultCreator();
        } else {
            return null;
        }
    }

	public boolean cancel(Map settings) {
		String cancelMessage = "Are you sure you want to cancel?";
		if (wizardType.equals(ChooseOperation.VALUE_INITIALIZE)) {
			cancelMessage = "Are you sure you want to cancel the database initialization?";
		} else if (wizardType.equals(ChooseOperation.VALUE_UPGRADE)) {
		cancelMessage = "Are you sure you want to cancel the database upgrade?";
		}
		boolean dialogShouldClose = JOptionPane.showConfirmDialog(null,
				cancelMessage,
				"", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
		return dialogShouldClose;
	}

}
