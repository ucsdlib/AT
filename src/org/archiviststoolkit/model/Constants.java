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
 */

package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.util.DatabaseConnectionUtils;
import org.archiviststoolkit.exceptions.WrongNumberOfConstantsRecordsException;
import org.hibernate.LockMode;

import javax.swing.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class Constants extends DomainObject {

	public static final String PROPERTYNAME_MAJOR_VERSION = "majorVersion";
	public static final String PROPERTYNAME_MINOR_VERSION = "minorVersion";
	public static final String PROPERTYNAME_UPDATE = "updateVersion";
	public static final String PROPERTYNAME_DEFAULT_DATE_FORMAT = "defaultDateFormat";

	public static final String DEFAULT_DATE_FORMAT = "M/d/yyyy";

	public static final Integer VERSION_EQUAL = 0;
	public static final Integer VERSION_LESS = -1;
	public static final Integer VERSION_GREATER = 1;

	private Long constantsId;
	private Integer majorVersion = 0;
	private Integer minorVersion = 0;
	private Integer updateVersion = 0;
	private String defaultDateFormat = DEFAULT_DATE_FORMAT;


	/**
	 * Creates a new instance of Subject
	 */
	public Constants() {
	}

	public Constants(String version) {
		this.setSoftwareVersion(version);
	}

	public Long getIdentifier() {
		return this.getConstantsId();
	}

	public void setIdentifier(Long identifier) {
		this.setConstantsId(identifier);
	}

	public Long getConstantsId() {
		return constantsId;
	}

	public void setConstantsId(Long constantsID) {
		this.constantsId = constantsID;
	}

	public Integer getMajorVersion() {
		return majorVersion;
	}

	public void setMajorVersion(Integer majorVersion) {
		this.majorVersion = majorVersion;
	}

	public Integer getMinorVersion() {
		return minorVersion;
	}

	public void setMinorVersion(Integer minorVersion) {
		this.minorVersion = minorVersion;
	}

	public Integer getUpdateVersion() {
		return updateVersion;
	}

	public void setUpdateVersion(Integer updateVersion) {
		this.updateVersion = updateVersion;
	}

	public void setSoftwareVersion(String versionString) {
		StringTokenizer st = new StringTokenizer(versionString, ".");
		int count = 0;
		String thisToken;
		while (st.hasMoreTokens()) {
			count++;
			thisToken = (String) st.nextElement();
			if (count == 1) {
				setMajorVersion(Integer.parseInt(thisToken));
			} else if (count == 2) {
				setMinorVersion(Integer.parseInt(thisToken));
			} else if (count == 3) {
				setUpdateVersion(Integer.parseInt(thisToken));
			}
		}
	}

    public static Integer compareVersions(String currentVersion,
                                          DatabaseConnectionUtils.DatabaseVersion databaseVersion) {

        return compareVersions(currentVersion,
                databaseVersion.getMajorVersion(),
                databaseVersion.getMinorVersion(),
                databaseVersion.getUpdateVersion());
    }

    public static Integer compareVersions(String currentVersion,
                                          Integer storedMajorVersion,
                                          Integer storedMinorVersion,
                                          Integer storedUpdateVersion) {

		StringTokenizer st = new StringTokenizer(currentVersion, ".");
		int count = 0;
		String thisToken;
		Integer currentMajorVersion = 0;
		Integer currentMinorVersion = 0;
		Integer currentUpdateVersion = 0;
		while (st.hasMoreTokens()) {
			count++;
			thisToken = (String) st.nextElement();
			if (count == 1) {
				currentMajorVersion = Integer.parseInt(thisToken);
			} else if (count == 2) {
				currentMinorVersion = Integer.parseInt(thisToken);
			} else if (count == 3) {
				currentUpdateVersion = Integer.parseInt(thisToken);
			}
		}

		//check major version
		if (currentMajorVersion > storedMajorVersion) {
			return VERSION_GREATER;
		} else if (currentMajorVersion < storedMajorVersion) {
			return VERSION_LESS;
		} else {
			//check minor version
			if (currentMinorVersion > storedMinorVersion) {
				return VERSION_GREATER;
			} else if (currentMinorVersion < storedMinorVersion) {
				return VERSION_LESS;
			} else {
				//check update version
				if (currentUpdateVersion > storedUpdateVersion) {
					return VERSION_GREATER;
				} else if (currentUpdateVersion < storedUpdateVersion) {
					return VERSION_LESS;
				} else {
					return VERSION_EQUAL;
				}
			}
		}
	}

	public static Boolean updateOrCreateVersionRecord(String updateToVersion) {
		try {
			DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Constants.class);
			Collection constants = access.findAll();
			if (constants.size() == 0) {
				Constants newConstant = new Constants(updateToVersion);
				access.add(newConstant);
				return true;
			} else if (constants.size() == 1) {
				Constants constantObject = (Constants) constants.iterator().next();
				constantObject.setSoftwareVersion(updateToVersion);
				access.update(constantObject);
				return true;
			} else {
				ErrorDialog dialog = new ErrorDialog("There are more than one constants record. Please contact your systemn administrator");
				dialog.showDialog();
				return false;
			}

		} catch (PersistenceException e) {
			new ErrorDialog("There was a problem updating the version. Please contact your systemn administrator").showDialog();
			return false;
		} catch (LookupException e) {
			new ErrorDialog("There was a problem updating the version. Please contact your systemn administrator").showDialog();
			return false;
		}
	}

	public static Vector<String> getDateFormatsList() {
		Vector<String> returnVector = new Vector<String>();
		returnVector.add("M/d/yyyy");
		returnVector.add("M/d/yy");
		returnVector.add("M-d-yyyy");
		returnVector.add("M-d-yy");

		returnVector.add("yyyy/M/d");
		returnVector.add("yy/M/d");
		returnVector.add("yyyy-M-d");
		returnVector.add("yy-M-d");

		returnVector.add("d/M/yyyy");
		returnVector.add("d/M/yy");
		returnVector.add("d-M-yyyy");
		returnVector.add("d-M-yy");		
		return returnVector;
	}

	public static void loadDefaultDateFormat() throws PersistenceException, LookupException, WrongNumberOfConstantsRecordsException {
		DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Constants.class);
		Collection constants = access.findAll(LockMode.READ);
		if (constants.size() == 1) {
			Constants constantRecord = (Constants) constants.iterator().next();
			ApplicationFrame.applicationDateFormat = new SimpleDateFormat(constantRecord.getDefaultDateFormat());
			ApplicationFrame.applicationDateFormat.setLenient(false);
		} else {
			throw new WrongNumberOfConstantsRecordsException(constants.size() + " records");
		}
	}

	public static void modifyConstantsRecord() throws PersistenceException, LookupException, WrongNumberOfConstantsRecordsException {
		DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Constants.class);
		Collection constants = access.findAll();
		if (constants.size() == 1) {
			DomainEditor constantsDialog = DomainEditorFactory.getInstance().getDialog(Constants.class);
//			constantsDialog.setMainHeaderColorAndTextByClass();
			Constants constantRecord = (Constants) constants.iterator().next();
			constantsDialog.setModel(constantRecord, null);
			String oldDateFormat = constantRecord.getDefaultDateFormat();
			if (constantsDialog.showDialog() == JOptionPane.OK_OPTION && !constantRecord.getDefaultDateFormat().equals(oldDateFormat)) {
				access.update(constantRecord);
				JOptionPane.showMessageDialog(ApplicationFrame.getInstance(), "You must restart the application for the new format to take effect");
			}
		} else {
			throw new WrongNumberOfConstantsRecordsException(constants.size() + " records");
		}
	}

	public String getDefaultDateFormat() {
		if (this.defaultDateFormat != null) {
			return this.defaultDateFormat;
		} else {
			return "";
		}
	}

	public void setDefaultDateFormat(String defaultDateFormat) {
		this.defaultDateFormat = defaultDateFormat;
	}
}
