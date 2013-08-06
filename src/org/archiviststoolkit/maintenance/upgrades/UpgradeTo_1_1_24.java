package org.archiviststoolkit.maintenance.upgrades;

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
 * Date: Jul 18, 2008
 * Time: 9:09:19 PM
 */

import org.archiviststoolkit.exceptions.RDEPanelCreationException;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.maintenance.common.MaintenanceUtils;
import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.mydomain.DomainAccessObject;
import org.archiviststoolkit.mydomain.DomainAccessObjectImpl;
import org.archiviststoolkit.mydomain.LookupException;
import org.archiviststoolkit.util.DatabaseConnectionUtils;
import org.archiviststoolkit.structure.DefaultValues;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.DatabaseFields;
import org.netbeans.spi.wizard.DeferredWizardResult;
import org.hibernate.LockMode;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.beans.IntrospectionException;

public class UpgradeTo_1_1_24 extends Upgrade {

	protected boolean runPostDBInitializationCode() {
		try {
			DomainAccessObject access = new DomainAccessObjectImpl(DefaultValues.class);
			DefaultValues defaultValue;
			DatabaseFields fieldInfo;
			for (Object o : access.findAll(LockMode.READ)) {
				defaultValue = (DefaultValues) o;
				fieldInfo = defaultValue.getAtField();
				if (fieldInfo.getDataType().equals(String.class.getName())
						&& (fieldInfo.getStringLengthLimit() == null
						|| fieldInfo.getStringLengthLimit() == 0)) {
					defaultValue.setTextValue(defaultValue.getStringValue());
					defaultValue.setStringValue(null);
					access.update(defaultValue);
				}
			}
		} catch (PersistenceException e) {
			setErrorString(e.getMessage());
			return false;
		} catch (LookupException e) {
			setErrorString(e.getMessage());
			return false;
		}
		return true;
	}

	protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {
		return true;
	}

	protected int getNumberOfSteps() {
		return 1;
	}

	protected boolean upgradeNeeded(Connection conn) throws SQLException {
		return Constants.compareVersions("1.1.24", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
	}


}