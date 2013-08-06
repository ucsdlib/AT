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
 * Time: 7:50:38 PM
 */

package org.archiviststoolkit.structure;

import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.importer.ImportUtils;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.model.ModelUtil;
import org.archiviststoolkit.ApplicationFrame;
import org.jdom.input.SAXBuilder;
import org.jdom.JDOMException;
import org.jdom.Document;
import org.jdom.Element;
import org.netbeans.spi.wizard.DeferredWizardResult;
import org.hibernate.LockMode;

import javax.swing.*;
import java.util.*;
import java.io.File;
import java.io.IOException;

public class InLineTags extends DomainObject {

	public static final String PROPERTYNAME_TAG_NAME = "tagName";

	private Long inLineTagId;

	private String tagName ="";

	private Set<InLineTagAttributes> attributes  = new TreeSet<InLineTagAttributes>();


	public Long getInLineTagId() {
		return inLineTagId;
	}

	public void setInLineTagId(Long inLineTagId) {
		this.inLineTagId = inLineTagId;
	}

	/**
	 * @return Returns the identifier.
	 */
	public Long getIdentifier() {
		return getInLineTagId();
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(Long identifier) {
		setInLineTagId(identifier);
	}

	public Set<InLineTagAttributes> getAttributes() {
		return attributes;
	}

	public void setAttributes(TreeSet<InLineTagAttributes> attributes) {
		this.attributes = attributes;
	}

	public String getTagName() {
		if (this.tagName != null) {
			return this.tagName;
		} else {
			return "";
		}
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public InLineTagAttributes findAttributeByName(String attributeName) {
		for (InLineTagAttributes attribute: getAttributes()) {
			if (attribute.getAttributeName().equalsIgnoreCase(attributeName)) {
				return attribute;
			}
		}
		InLineTagAttributes attribute = new InLineTagAttributes(attributeName, this);
		attributes.add(attribute);
		return attribute;
	}

	public String toString() {
		return getTagName();
	}

}
