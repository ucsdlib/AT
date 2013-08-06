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
 */


package org.archiviststoolkit.mydomain;

//==============================================================================    
// Import Declarations
//==============================================================================    

import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.exceptions.UnsupportedDomainEditorException;
import org.archiviststoolkit.exceptions.DomainEditorCreationException;
import org.archiviststoolkit.structure.*;
import org.archiviststoolkit.dialog.QueryEditor;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.editor.*;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.model.RDEScreenPanelItems;

import javax.swing.*;
import java.util.Hashtable;
import java.awt.event.ActionListener;

/**
 * A singleton in which we store the dialogs associated with a specific model.
 */

public final class DomainEditorFactory {

	/**
	 * The singleton reference.
	 */

	private static DomainEditorFactory singleton = null;

	/**
	 * The cache where we store the associated dialogs with their classes.
	 */

	private Hashtable<Class, DomainEditor> dialogCache = null;

	/**
	 * The cache where we store the associated dialogs with their classes.
	 */

	private Hashtable<Class, QueryEditor> searchDialogCache = null;

//	/**
//	 * The cache where we store the associated dialogs with their classes.
//	 */
//
//	private Hashtable dialogBasicCache = null;

	/**
	 * Default Constructor.
	 */

	private DomainEditorFactory() {
		dialogCache = new Hashtable<Class, DomainEditor>();
		searchDialogCache = new Hashtable<Class, QueryEditor>();
	}

	/**
	 * Singleton access method.
	 *
	 * @return the instance of this singleton
	 */

	public static DomainEditorFactory getInstance() {
		if (singleton == null) {
			singleton = new DomainEditorFactory();
		}
		return (singleton);
	}


	/**
	 * Get an editor associated with a class.
	 *
	 * @param clazz the class of a specific model
	 * @return the editor associated with the model
	 */

	public DomainEditor getDialog(final Class clazz) {
		return retrieveDomainEditorFromCache(clazz);
	}

	/**
	 * Get an editor associated with a class.
	 *
	 * @param clazz the class of a specific model
	 * @return the editor associated with the model
	 */

	public DomainEditor getDialog(final Class clazz, JDialog parent) throws UnsupportedDomainEditorException, DomainEditorCreationException {
		return createDomainEditorWithParent(clazz, parent, false);
	}

	/**
	 * Get an editor associated with a class.
	 *
	 * @param clazz the class of a specific model
	 * @return the editor associated with the model
	 */

	public DomainEditor getDialog(final Class clazz,
								  final JTable callingTable) {
		return assignCallingTableAndModel(retrieveDomainEditorFromCache(clazz), callingTable, null);
	}

	/**
	 * Get an editor associated with a class.
	 *
	 * @param clazz the class of a specific model
	 * @return the editor associated with the model
	 */

	public DomainEditor getDialog(final Class clazz,
								  final JDialog parent,
								  final JTable callingTable) throws UnsupportedDomainEditorException, DomainEditorCreationException {
		return assignCallingTableAndModel(createDomainEditorWithParent(clazz, parent, false), callingTable, null);
	}

	/**
	 * Get an editor associated with a class.
	 *
	 * @param clazz the class of a specific model
	 * @return the editor associated with the model
	 */

	public DomainEditor getDialog(final Class clazz,
								  final JTable callingTable,
								  final DomainObject callingDomainModel) {
		return assignCallingTableAndModel(retrieveDomainEditorFromCache(clazz), callingTable, callingDomainModel);
	}

//	/**
//	 * Get an editor associated with a class.
//	 *
//	 * @param clazz the class of a specific model
//	 * @return the editor associated with the model
//	 */
//
//	public DomainEditor getDialog(final Class clazz,
//								  final JDialog parent,
//								  final JTable callingTable,
//								  final DomainObject callingDomainModel) {
//		return assignCallingTableAndModel(createDomainEditorWithParent(clazz, parent), callingTable, callingDomainModel);
//	}

	private DomainEditor assignCallingTableAndModel(DomainEditor domainEditor, JTable callingTable, DomainObject callingDomainModel) {
		try {
			domainEditor.setCallingTable(callingTable);
		} catch (UnsupportedTableModelException e) {
			new ErrorDialog(domainEditor, "Unsupported table model", e).showDialog();
		}
		domainEditor.setCallingModel(callingDomainModel);
		return domainEditor;
	}

	private DomainEditor retrieveDomainEditorFromCache(Class clazz) {
		DomainEditor domainEditor = dialogCache.get(clazz);
		if (domainEditor == null) {
			try {
				throw new UnsupportedDomainEditorException(clazz);
			} catch (UnsupportedDomainEditorException e) {
				new ErrorDialog("Error getting domain editor", e).showDialog();
			}
		}
		return domainEditor;
	}


	/**
	 * Place a editor into the cache.
	 *
	 * @param clazz  the class to associate
	 * @param dialog the editor to cache
	 */


	private DomainEditor putDialog(final Class clazz, final DomainEditor dialog) {
		dialogCache.put(clazz, dialog);
		return dialog;
	}

	private DomainEditor putDialog(final Class clazz, final DomainEditor dialog, ActionListener listener) {
		dialog.setNavigationButtonListeners(listener);
		return putDialog(clazz, dialog);
	}

	private DomainEditor putDialogAndSetListenerToSelf(final Class clazz, final DomainEditor dialog) {
		setListenersToSelf(dialog);
		return putDialog(clazz, dialog);
	}

	private void setListenersToSelf(DomainEditor dialog) {
		dialog.setNavigationButtonListeners(dialog);
	}

//	/**
//	 * Place a editor into the cache.
//	 *
//	 * @param clazz  the class to associate
//	 * @param dialog the editor to cache
//	 */
//
//
//	public void putDialog(final Class clazz, final JDialog dialog) {
//		dialogCache.put(clazz, dialog);
//	}

	/**
	 * Get an editor associated with a class.
	 *
	 * @param clazz the class of a specific model
	 * @return the editor associated with the model
	 */

	public QueryEditor getSearchDialog(final Class clazz) {
		return searchDialogCache.get(clazz);
	}

	/**
	 * Place a editor into the cache.
	 *
	 * @param clazz  the class to associate
	 * @param parent the parent frame
	 */


	public void putSearchDialog(final Class clazz, final JFrame parent) {
		searchDialogCache.put(clazz, new QueryEditor(parent, clazz));
	}

	public DomainEditor createDomainEditorWithParent(final Class clazz, final JDialog parent, final JTable callingTable) throws DomainEditorCreationException {
		return assignCallingTableAndModel(createDomainEditorWithParent(clazz, parent, false), callingTable, null);
	}

	public DomainEditor createDomainEditorWithParent(final Class clazz, final JDialog parent, final JTable callingTable, boolean useParentAsEventListener) throws DomainEditorCreationException {
		return assignCallingTableAndModel(createDomainEditorWithParent(clazz, parent, useParentAsEventListener), callingTable, null);
	}

	public DomainEditor createDomainEditorWithParent(final Class clazz, final JDialog parent, final JTable callingTable, final DomainObject callingDomainModel) throws DomainEditorCreationException {
		return assignCallingTableAndModel(createDomainEditorWithParent(clazz, parent, false), callingTable, callingDomainModel);
	}


	public DomainEditor createDomainEditorWithParent(final Class clazz, final JDialog parent, boolean useParentAsEventListener) throws DomainEditorCreationException {

		DomainEditor domainEditor = null;
		if (clazz == BibItems.class) {
			domainEditor = new DomainEditor(BibItems.class, parent, "Bibliographic Items", new BibItemsFields());

		} else if (clazz == ChronologyItems.class) {
			domainEditor = new DomainEditor(ChronologyItems.class, parent, new ChronologyItemsFields());

		} else if (clazz == IndexItems.class) {
			domainEditor = new DomainEditor(IndexItems.class, parent, "Index Items", new IndexItemsFields());

		} else if (clazz == ListOrderedItems.class) {
			domainEditor = new DomainEditor(ListOrderedItems.class, parent, "List Items", new ListOrderedItemsFields());

		} else if (clazz == ListDefinitionItems.class) {
			domainEditor = new DomainEditor(ListDefinitionItems.class, parent, "List Items", new ListDefinitionItemsFields());

		} else if (clazz == Subjects.class) {
			domainEditor = new DomainEditor(Subjects.class, parent, new SubjectFields());

		} else if (clazz == AccessionsLocations.class) {
			domainEditor = new DomainEditor(AccessionsLocations.class, parent, new AccessionsLocationsFields());

		} else if (clazz == Names.class) {
			domainEditor = new NameEditor(parent, new NameFields());

		} else if (clazz == NameContactNotes.class) {
			domainEditor = new DomainEditor(NameContactNotes.class, parent, new NameContactNoteFields());

		} else if (clazz == NonPreferredNames.class) {
			domainEditor = new NonPreferredNameEditor(parent);

		} else if (clazz == Users.class) {
			domainEditor = new UserEditor(parent);

		} else if (clazz == LookupList.class) {
			domainEditor = new DomainEditor(LookupList.class, parent, new LookupListFields());

		} else if (clazz == RDEScreen.class) {
			domainEditor = new DomainEditor(RDEScreen.class, parent, new RDEScreenFields());

		} else if (clazz == ArchDescriptionRepeatingData.class) {
			domainEditor = new ArchDescriptionRepeatingDataEditor(parent);

		} else if (clazz == ArchDescriptionNames.class) {
			domainEditor = new DomainEditor(ArchDescriptionNames.class, parent, "dummy text", new ArchDescriptionNamesFields());

		} else if (clazz == RepositoryNotesDefaultValues.class) {
			domainEditor = new DomainEditor(RepositoryNotesDefaultValues.class, parent, new RepositoryNotesDefaultValuesFields());

		} else if (clazz == RepositoryNotes.class) {
			domainEditor = new DomainEditor(RepositoryNotes.class, parent, new RepositoryNoteFields());

		} else if (clazz == RepositoryStatistics.class) {
			domainEditor = new DomainEditor(RepositoryStatistics.class, parent, new RepositoryStatisticsFields());

		} else if (clazz == DefaultValues.class) {
			domainEditor = new DomainEditor(DefaultValues.class, parent, new DefaultValuesFields());

		} else if (clazz == Deaccessions.class) {
			domainEditor = new DomainEditor(Deaccessions.class, parent, new DeaccessionsFields());
			Class parentModelClass = ((DomainEditor)parent).getModel().getClass();
			domainEditor.setMainHeaderColorAndTextByClass(clazz, parentModelClass);

		} else if (clazz == Events.class) {
			domainEditor = new DomainEditor(Events.class, parent, new EventsFields());

		} else if (clazz == RDEScreenPanels.class) {
			domainEditor = new DomainEditor(RDEScreenPanels.class, parent, new RDEScreenPanelFields());

		} else if (clazz == NotesEtcTypes.class) {
			domainEditor = new DomainEditor(NotesEtcTypes.class, parent, new NotesEtcTypesFields());

		} else if (clazz == Locations.class) {
			domainEditor = new LocationEditor(parent);

		} else if (clazz == Repositories.class) {
			domainEditor = new RepositoryEditor(parent);

		} else if (clazz == Constants.class) {
			domainEditor = new DomainEditor(Constants.class, parent, new ConstantsFields());

		} else if (clazz == ArchDescriptionInstances.class) {
			domainEditor = new ArchDescriptionInstancesEditor(parent);

		} else if (clazz == ArchDescriptionRepeatingData.class) {
			domainEditor = new ArchDescriptionRepeatingDataEditor(parent);

		} else if (clazz == FileVersions.class) {
			domainEditor = new FileVersionsEditor(parent);

		} else if (clazz == DatabaseTables.class) {
			domainEditor = new DatabaseTableEditor(parent);
			if (!ApplicationFrame.getInstance().getCurrentUserName().equalsIgnoreCase(Users.USERNAME_DEVELOPER)) {
				((DatabaseTableEditor)domainEditor).hideAddRemoveButtons();
			}

		} else if (clazz == DatabaseFields.class) {
			domainEditor = new DatabaseFieldsEditor(parent);

		} else if (clazz == Assessments.class) {
            domainEditor = new DomainEditor(Assessments.class, parent, new AssessmentsFields());
        }


		if (domainEditor == null) {
			return null;
		} else if (useParentAsEventListener){
			if (parent instanceof ActionListener) {
				domainEditor.setNavigationButtonListeners((ActionListener)parent);
				return domainEditor;
			} else {
				throw new DomainEditorCreationException(parent + " is not an action listener");
			}
		} else {
			domainEditor.setNavigationButtonListeners(domainEditor);
			return domainEditor;
		}
	}

	public void createDomainEditors() {
		ApplicationFrame mainFrame = ApplicationFrame.getInstance();
		DomainTableWorkSurface worksurface;

		//Subjects
		DomainEditor subjectEditor = this.putDialog(Subjects.class, new SubjectEditor(mainFrame));
		subjectEditor.setMainHeaderColorAndTextByClass();
		subjectEditor.setIncludeSaveButton(true);
		this.putSearchDialog(Subjects.class, mainFrame);
		worksurface = mainFrame.getWorkSurface(Subjects.class);
		subjectEditor.setWorkSurface(worksurface);
        subjectEditor.setNavigationButtonListeners(worksurface);

		//Names
		DomainEditor namesEditor = this.putDialog(Names.class, new NameEditor(mainFrame, new NameFields()));
		namesEditor.setMainHeaderColorAndTextByClass();
		namesEditor.setIncludeSaveButton(true);
		this.putSearchDialog(Names.class, mainFrame);
		worksurface = mainFrame.getWorkSurface(Names.class);
		namesEditor.setWorkSurface(worksurface);
		namesEditor.setNavigationButtonListeners(worksurface);

		//constants
		DomainEditor constantsEditor = new DomainEditor(Constants.class, mainFrame, new ConstantsFields());
		constantsEditor.hidePrintAndNavigationButtons();
		DomainEditorFactory.getInstance().putDialogAndSetListenerToSelf(Constants.class, constantsEditor);

		//Accessions
		DomainEditor accessionEditor = DomainEditorFactory.getInstance().putDialog(Accessions.class, new AccessionEditor(mainFrame));
		accessionEditor.setMainHeaderColorAndTextByClass();
		accessionEditor.setIncludeSaveButton(true);
		this.putSearchDialog(Accessions.class, mainFrame);
		worksurface = mainFrame.getWorkSurface(Accessions.class);
		accessionEditor.setWorkSurface(worksurface);
		accessionEditor.setNavigationButtonListeners(worksurface);

		//Resources
		DomainEditor resourceEditor = DomainEditorFactory.getInstance().putDialog(Resources.class, new ResourceEditor(mainFrame));
		resourceEditor.setMainHeaderColorAndTextByClass();
		resourceEditor.setIncludeSaveButton(true);
		this.putSearchDialog(Resources.class, mainFrame);
		worksurface = mainFrame.getWorkSurface(Resources.class);
		resourceEditor.setWorkSurface(worksurface);
		resourceEditor.setNavigationButtonListeners(worksurface);

        //Digital Object Editor
		DomainEditor digitalObjectEditor = DomainEditorFactory.getInstance().putDialog(DigitalObjects.class, new DigitalObjectEditor(mainFrame));
		digitalObjectEditor.setMainHeaderColorAndTextByClass();
		digitalObjectEditor.setIncludeSaveButton(true);
		this.putSearchDialog(DigitalObjects.class, mainFrame);
		worksurface = mainFrame.getWorkSurface(DigitalObjects.class);
		digitalObjectEditor.setWorkSurface(worksurface);
		digitalObjectEditor.setNavigationButtonListeners(worksurface);
	}

	public void updateDomainEditors() {

		dialogCache = new Hashtable<Class, DomainEditor>();
		searchDialogCache = new Hashtable<Class, QueryEditor>();
		createDomainEditors();
	}

}