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
 * Programmer: Lee Mandell
 * Date: Dec 20, 2005
 * Time: 1:28:26 PM
 */

package org.archiviststoolkit.dialog;

import org.hibernate.criterion.Criterion;
import org.archiviststoolkit.hibernate.ATSearchCriterion;
import org.archiviststoolkit.structure.ATFieldInfo;

import javax.swing.*;

public abstract class QueryEditorPanel extends JPanel {

    public static final String BEGINS_WITH = "Begins with";
    public static final String CONTAINS = "Contains";
    public static final String EQUALS = "Equals";
    public static final String NOT_EQUALS = "Is Not Equal to";
    public static final String EMPTY = "Is Empty";
    public static final String NOT_EMPTY = "Is Not Empty";

	public static final Boolean RETURN_LONG_VALUES = true;
	public static final Boolean RETURN_INTEGER_VALUES = false;

    public abstract ATSearchCriterion getQueryCriterion(Class clazz, String field);

	public abstract void requestInitialFocus();

	public abstract boolean validDataEntered();

	protected String getFieldLabel(Class clazz, String field) {
		//strip out auditInfo if it is there
		field = field.replace("auditInfo.", "");
		ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(clazz, field);
		String fieldLabel;
		if (fieldInfo != null) {
			fieldLabel = fieldInfo.getFieldLabel();
		} else {
			fieldLabel = field;
		}
		return fieldLabel;
	}
}
