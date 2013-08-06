package org.archiviststoolkit.model;

import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.BeanAdapter;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.util.LookupListUtils;

/**
 * Archivists' Toolkit(TM) Copyright � 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
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
 * Date: Sep 6, 2005
 * Time: 2:37:18 PM
 */

public class ModelUtil {

	private static String [] ingnoreProperties = {"class", "created",
			"createdBy", "lastUpdatedBy", "lastUpdated", "version",
			"identifier"};
	private static ArrayList ignoreArray = new ArrayList(Arrays.asList(ingnoreProperties));

	public static void bindModelToView(ArrayList fieldsToBind, DomainObject model) {

		BeanAdapter adapter = new BeanAdapter(model, true);
		ValueModel valueModel = null;
		Object field = null;
		ATFieldInfo fieldInfo;
		String fieldName = "";
		ComboBoxAdapter comboBoxAdapter = null;

		for (int i = 0; i < fieldsToBind.size(); i++) {
			field = fieldsToBind.get(i);
			fieldName = ((JComponent)field).getName();
			valueModel = adapter.getValueModel(fieldName);
			if (!ignoreArray.contains(fieldName)) {
					if (field instanceof JFormattedTextField) {
						Bindings.bind((JFormattedTextField) field, valueModel);
					} else if (field instanceof JTextComponent) {
//						Bindings.bind((JTextComponent) field, valueModel);
					} else if (field instanceof JCheckBox) {
						Bindings.bind((JCheckBox) field, valueModel);
					} else if (field instanceof JComboBox) {
						fieldInfo = ATFieldInfo.getFieldInfo(model.getClass().getName() + "." + fieldName);
						if (fieldInfo != null) {
							Vector<String> values = LookupListUtils.getLookupListValues(fieldInfo.getLookupList());
							if (values != null) {
								comboBoxAdapter = new ComboBoxAdapter(values, valueModel);
								((JComboBox)field).setModel(comboBoxAdapter);
							}
						}
					} else {
						System.out.println(fieldName + " not bound");
					}
				}
			}

		}

}
