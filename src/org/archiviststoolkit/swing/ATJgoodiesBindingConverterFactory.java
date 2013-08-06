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
 * Date: Nov 8, 2006
 * Time: 9:37:38 PM
 */

package org.archiviststoolkit.swing;

import java.text.Format;
import java.text.ParseException;

import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;
import org.archiviststoolkit.model.LookupListItems;
import org.archiviststoolkit.util.LookupListUtils;

public class ATJgoodiesBindingConverterFactory {

	/**
	 * Creates and returns a ValueModel that converts objects to Strings
	 * and vice versa. The conversion is performed by a <code>Format</code>.<p>
	 *
	 * <strong>Constraints:</strong> The subject is of type <code>Object</code>;
	 * it must be formattable and parsable via the given <code>Format</code>.
	 *
	 * @param subject  the underlying ValueModel.
	 * @param format   the <code>Format</code> used to format and parse
	 *
	 * @return a ValueModel that converts objects to Strings and vice versa
	 *
	 * @throws NullPointerException if the subject or the format
	 *         is <code>null</code>
	 */
	public static ValueModel createLookupListConverter(ValueModel subject, String format) {
		return new LookupListConverter(subject, format);
	}


	private ATJgoodiesBindingConverterFactory() {
		// Overrides default constructor; prevents instantiation.
	}

	/**
	  * Converts Values to Strings and vice-versa using a given Format.
	  */
	public static final class LookupListConverter extends AbstractConverter {

		 /**
		  * Holds the <code>Format</code> used to format and parse.
		  */
		 private final String listName;



		 /**
		  * Constructs a <code>LookupListConverter</code> on the given
		  * subject using the specified <code>Format</code>.
		  *
		  * @param subject  the underlying ValueModel.
		  * @param listName   the name of the list
		  * @throws NullPointerException if the subject or the format is null.
		  */
		 private LookupListConverter(ValueModel subject, String listName) {
			 super(subject);
			 if (listName == null) {
				 throw new NullPointerException("The format must not be null.");
			 }
			 this.listName = listName;
		 }


		 // Implementing Abstract Behavior *************************************

		 /**
		  * Retrievs the LookupListItem corresponding to the string in a given list.
		  *
		  * @param subjectValue  the subject's value
		  * @return the formatted subjectValue
		  */
		 public Object convertFromSubject(Object subjectValue) {
			 return LookupListUtils.getLookupListItem(listName, (String)subjectValue);
		 }


		 // Implementing ValueModel ********************************************

		 /**
		  * Converts a lookuplist item to its string representation and sets it as the subject's
		  * new value.
		  *
		  * @param value  the value to be converted and set as new subject value
		  */
		 public void setValue(Object value) {
				 LookupListItems item = (LookupListItems)value;
				 subject.setValue(item.getListItem());
		 }

	 }


}
