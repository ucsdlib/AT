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
 * Date: May 27, 2006
 * Time: 6:12:52 PM
 */

package org.archiviststoolkit.dialog;

import ca.odell.glazedlists.TextFilterator;

import java.util.List;

import org.archiviststoolkit.model.Subjects;

public class SubjectsFilterator implements TextFilterator {
	public void getFilterStrings(List baseList, Object element) {
		 Subjects subject = (Subjects)element;

		 baseList.add(subject.getSubjectTerm());
	 }
}
