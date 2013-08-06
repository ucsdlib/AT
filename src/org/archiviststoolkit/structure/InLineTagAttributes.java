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
 * Date: Sep 2, 2006
 * Time: 7:53:18 PM
 */

package org.archiviststoolkit.structure;

import org.archiviststoolkit.mydomain.DomainObject;

public class InLineTagAttributes  extends DomainObject {

	private Long inLineTagAttributeId;

	private String valueList ="";

	public InLineTagAttributes() {
	}

	private String attributeName = "";

	private InLineTags tag;

	public InLineTagAttributes(String attributeName, InLineTags tag) {
		this.attributeName = attributeName;
		this.tag = tag;
	}

	public Long getInLineTagAttributeId() {
		return inLineTagAttributeId;
	}

	public void setInLineTagAttributeId(Long inLineTagAttributeId) {
		this.inLineTagAttributeId = inLineTagAttributeId;
	}

	/**
	 * @return Returns the identifier.
	 */
	public Long getIdentifier() {
		return getInLineTagAttributeId();
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(Long identifier) {
		setInLineTagAttributeId(identifier);
	}

	/**
	 * Compares this object to another.
	 *
	 * @param object the object to compare this to.
	 * @return a integer result of the comparison.
	 */
	public int compareTo(Object object) {
		if (object instanceof InLineTagAttributes) {
			return this.attributeName.compareTo(((InLineTagAttributes)object).getAttributeName());
		} else {
			return super.compareTo(object);
		}
	}

	public InLineTags getTag() {
		return tag;
	}

	public void setTag(InLineTags tag) {
		this.tag = tag;
	}

	public String getValueList() {
		if (this.valueList != null) {
			return this.valueList;
		} else {
			return "";
		}
	}

	public void setValueList(String valueList) {
		this.valueList = valueList;
	}

	public String getAttributeName() {
		if (this.attributeName != null) {
			return this.attributeName;
		} else {
			return "";
		}
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
}
