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
 * Date: Jun 10, 2006
 * Time: 11:32:42 AM
 */

package org.archiviststoolkit.mydomain;

import ca.odell.glazedlists.TextFilterator;

import java.util.List;
import java.util.TreeSet;
import java.lang.reflect.InvocationTargetException;

import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.TableNotConfiguredException;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.util.FuzzyStringMatcher;
import com.ibm.icu.text.Normalizer;
//import com.ibm.icu.text.Normalizer;

public class DomainFilterator implements TextFilterator {

	public void getFilterStrings(List list, Object o) {

		try {
			TreeSet<ATFieldInfo> fieldList = ATFieldInfo.getReturnScreenList(o.getClass());
			for (ATFieldInfo field : fieldList) {
				Object target = field.getGetMethod().invoke(o);
				if (target != null) {
					String normal = Normalizer.normalize(target.toString(), Normalizer.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
					list.add(normal);
//					list.add(target.toString());
				}
			}
		} catch (TableNotConfiguredException e) {
			ErrorDialog dialog = new ErrorDialog("Table not configured: " + o.getClass().getSimpleName(),
					StringHelper.getStackTrace(e));
			dialog.showDialog();
		} catch (IllegalAccessException e) {
			new ErrorDialog("", StringHelper.getStackTrace(e)).showDialog();
		} catch (InvocationTargetException e) {
			new ErrorDialog("", StringHelper.getStackTrace(e)).showDialog();
		}
	}
}
