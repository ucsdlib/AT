//==============================================================================
//            _            __
//  ___  __ _| | ___  ___ / _| ___  _ __ __ _  ___
// / __|/ _` | |/ _ \/ __| |_ / _ \| '__/ _` |/ _ \
// \__ \ (_| | |  __/\__ \  _| (_) | | | (_| |  __/
// |___/\__,_|_|\___||___/_|  \___/|_|  \__, |\___|
//                                      |___/
//
// 01110011 01100001 01101100 01100101 01110011
// 01100110 01101111 01110010 01100111 01100101
//
//==============================================================================
// Copyright 2004 (c) the salesforge community. All Rights Reserved.
//==============================================================================
// Licensed under the Open Software License version 2.1
//==============================================================================
// $Author: lejames $
// $Date: 2005/01/07 23:30:23 $
// $Revision: 1.7 $
//==============================================================================

package org.archiviststoolkit.importer;

//==============================================================================
// Import Declarations
//==============================================================================

import java.io.File;
import java.util.*;
import java.io.IOException;

import org.archiviststoolkit.model.Subjects;
import org.archiviststoolkit.util.StringHelper;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Imports Contact domain objects from CSV files.
 */
public class TabSubjectImportHandler extends TabImportHandler {

	/**
	 * the identity of the source application.
	 */
	private String identity = null;
	/**
	 * the translation map being used.
	 */
	private Hashtable translationMap = new Hashtable();

	/**
	 * Constructor.
	 *
	 */
	public TabSubjectImportHandler() {
		super();
	}

	/**
	 * Can this file be imported.
	 *
	 * @param file the file to import.
	 * @return yes or no
	 */
	public boolean canImportFile(File file) {

		String identityFromFile = getInitialChunk(file);
		if (identityFromFile.startsWith(identity)) {
			return (true);
		}

		return (false);
	}

	/**
	 * Take a row and turn it into a domain object.
	 *
	 * @param dataList the list representing the rows.
	 * @return the domain object.
	 */
	public Object handleRow(ArrayList dataList) throws ImportException {
		Subjects subject = new Subjects();
		HashMap map = new HashMap();
		String dataValue;
		String fieldName;
		for (int i = 0; i < dataList.size(); i++) {
			dataValue = ((String) dataList.get(i)).trim();
			fieldName = (String) this.fieldList.get(i);
			if (dataValue.length() != 0) {
				map.put(fieldName, StringHelper.convertPipeToCarriageReturn(dataValue));
			}
		}
		try {
			BeanUtils.populate(subject, map);
		} catch (Exception e) {
			String currentRow = "";
			for (int i = 0; i < dataList.size(); i++) {
				currentRow += "\n" + this.fieldList.get(i) + ": " + ((String) dataList.get(i)).trim();
			}
			throw new ImportException("Error Precession row", currentRow, e);
		}
		return (subject);
	}

	/**
	 * Map input columns to real columns.
	 *
	 * @param dataList the list to patch.
	 * @return the patched list.
	 */
	public ArrayList mapColumn(ArrayList dataList) {

		for (int loop = 0; loop < dataList.size(); loop ++) {
			String name = (String) dataList.get(loop);

			name = name.replace(' ', '_');
			name = name.toLowerCase();
			name = name.replace('/', '_');
			name = name.replace('-', '_');
			dataList.set(loop, name);

		}

		return (dataList);
	}
}