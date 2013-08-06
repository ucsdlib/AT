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
 * Date: Oct 11, 2005
 * Time: 3:44:18 PM
 */

package org.archiviststoolkit.importer;

import org.archiviststoolkit.util.UserPreferences;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.swing.SimpleFileFilter;
import org.apache.commons.beanutils.BeanUtils;

import javax.swing.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.io.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class ImportUtils {

	public static ArrayList<String> loadFileIntoStringArray(File file)

	{
		String line;
		ArrayList<String> array = new ArrayList<String>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));

			if (!in.ready())
				throw new IOException();

			while ((line = in.readLine()) != null)
				array.add(line);

			in.close();
		}
		catch (IOException e) {
			System.out.println(e);
			return null;
		}

		return array;
	}

	public static String loadFileIntoString(String filePath) throws IOException {
		FileInputStream fis = new FileInputStream(filePath);
		int x = fis.available();
		byte b[] = new byte[x];
		fis.read(b);
		return new String(b);
	}

	public static File chooseFile(Component parent) {
		return chooseFile(parent, null);
	}

	public static File chooseFile(Component parent, SimpleFileFilter fileFilter) {

		UserPreferences userPrefs = UserPreferences.getInstance();
		JFileChooser chooser;
		if (userPrefs.getSavePath() == null) {
			chooser = new JFileChooser();
		} else {
			chooser = new JFileChooser(userPrefs.getSavePath());
		}

		if (fileFilter != null) {
			chooser.setFileFilter(fileFilter);
		}

//		chooser.setCurrentDirectory(".");
		if (chooser.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION) {
			return null;
		} else {
			userPrefs.setSavePath(chooser.getCurrentDirectory().getPath());
			return chooser.getSelectedFile();
		}
	}

	public static void nullSafeSet(DomainObject domainObject, String propertyName, Object value) throws IllegalAccessException, InvocationTargetException, UnknownLookupListException {
        if (value != null) {
            //check to see if this field has a lookup list
            ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(domainObject.getClass(), propertyName);
            String lookupListName = fieldInfo.getLookupList();
            if (lookupListName != null && lookupListName.length() > 0) {
                //first trim to length
                String stringValue = StringHelper.cleanUpWhiteSpace((String) value);
                if (fieldInfo.getStringLengthLimit() != null && fieldInfo.getStringLengthLimit() > 0) {
                    stringValue = StringHelper.trimToLength(stringValue, fieldInfo.getStringLengthLimit());
                }
                //then check to see if it in the lookup list
                stringValue = LookupListUtils.addItemToList(lookupListName, stringValue, false);
                BeanUtils.setProperty(domainObject, propertyName, stringValue);
            } else {
                if (value instanceof String) {
                    String stringValue = StringHelper.cleanUpWhiteSpace((String) value);
                    if (fieldInfo.getStringLengthLimit() != null && fieldInfo.getStringLengthLimit() > 0) {
                        stringValue = StringHelper.trimToLength(stringValue, fieldInfo.getStringLengthLimit());
                    }
                    BeanUtils.setProperty(domainObject, propertyName, stringValue);
                } else {
                    BeanUtils.setProperty(domainObject, propertyName, value);
                }
            }

        }
	}

	public static void nullSafeDateSet(DomainObject domainObject, String propertyName, XMLGregorianCalendar date) throws IllegalAccessException, InvocationTargetException, UnknownLookupListException {
		if (date != null) {
			nullSafeSet(domainObject, propertyName, date.toGregorianCalendar().getTime());
		}
	}


}
