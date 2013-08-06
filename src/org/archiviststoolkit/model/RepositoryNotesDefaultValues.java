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
 */

package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.NotesEtcTypes;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.StringLengthValidationRequried;

import java.io.Serializable;

@ExcludeFromDefaultValues
public class RepositoryNotesDefaultValues  extends DomainObject implements Serializable, Comparable {

	public static final String PROPERTYNAME_DEFAULT_TITLE    = "defaultTitle";
	public static final String PROPERTYNAME_DEFAULT_CONTENT    = "defaultContent";
	public static final String PROPERTYNAME_NOTE_TYPE    = "noteType";

	private Long repositoryNotesDefaultValueId = null;

	@IncludeInApplicationConfiguration
	private Repositories repository;

	@IncludeInApplicationConfiguration(1)
	private NotesEtcTypes noteType;

	@IncludeInApplicationConfiguration(2)
	@StringLengthValidationRequried
	private String defaultTitle ="";

	@IncludeInApplicationConfiguration(3)
	private String defaultContent ="";

	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public RepositoryNotesDefaultValues() {}

	public RepositoryNotesDefaultValues(Repositories repository, NotesEtcTypes noteType) {
		this.repository = repository;
		this.noteType = noteType;
	}
	// ********************** Accessor Methods ********************** //


	public Long getIdentifier() {
		return this.getRepositoryNotesDefaultValueId();
	}

	public void setIdentifier(Long identifier) {
		this.setRepositoryNotesDefaultValueId(identifier);
	}


	public Long getRepositoryNotesDefaultValueId() {
		return repositoryNotesDefaultValueId;
	}

	public void setRepositoryNotesDefaultValueId(Long repositoryNotesDefaultValueId) {
		this.repositoryNotesDefaultValueId = repositoryNotesDefaultValueId;
	}

	public Repositories getRepository() {
		return repository;
	}

	public void setRepository(Repositories repository) {
		this.repository = repository;
	}

	public NotesEtcTypes getNoteType() {
		return noteType;
	}

	public void setNoteType(NotesEtcTypes noteType) {
		this.noteType = noteType;
	}

	public String getDefaultTitle() {
		if (this.defaultTitle != null) {
			return this.defaultTitle;
		} else {
			return "";
		}
	}

	public void setDefaultTitle(String defaultTitle) {
		this.defaultTitle = defaultTitle;
	}

	public String getDefaultContent() {
		if (this.defaultContent != null) {
			return this.defaultContent;
		} else {
			return "";
		}
	}

	public void setDefaultContent(String defaultContent) {
		this.defaultContent = defaultContent;
	}
}
