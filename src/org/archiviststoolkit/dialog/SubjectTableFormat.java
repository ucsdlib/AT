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
 * Time: 6:19:29 PM
 */

package org.archiviststoolkit.dialog;

import ca.odell.glazedlists.gui.TableFormat;
import org.archiviststoolkit.model.Subjects;

public class SubjectTableFormat implements TableFormat {

	public int getColumnCount() {
		return 2;
	}

	public String getColumnName(int column) {
		if(column == 0)      return "Subject Term";
		else if(column == 1) return "Source";

		throw new IllegalStateException();
	}

	public Object getColumnValue(Object baseObject, int column) {
		Subjects subject = (Subjects)baseObject;

		if(column == 0)      return subject.getSubjectTerm();
		else if(column == 1) return subject.getSubjectSource();

		throw new IllegalStateException();
	}

}
