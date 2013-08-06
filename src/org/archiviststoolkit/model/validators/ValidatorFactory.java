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
 * Date: May 23, 2006
 * Time: 11:04:22 AM
 */

package org.archiviststoolkit.model.validators;

import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.DatabaseFields;
import org.archiviststoolkit.structure.DatabaseTables;
import org.archiviststoolkit.structure.DefaultValues;
import org.archiviststoolkit.structure.NotesEtcTypes;

import java.util.Hashtable;

public class ValidatorFactory {

	private static ValidatorFactory ourInstance = new ValidatorFactory();

	public static ValidatorFactory getInstance() {
		return ourInstance;
	}

	private Hashtable<Class, ATValidator> validatorCache = new Hashtable<Class, ATValidator>();

	private ValidatorFactory() {
		initCache();
	}

    /**
     * Method to add a data validator
     *
     * @param clazz The class to add the validator for
     * @param validator The validator
     */
	public void addValidator(Class clazz, ATValidator validator) {
		validatorCache.put(clazz, validator);
	}

    /**
     * Method to remove a validator from the cache
     *
     * @param clazz The class which is used as the key
     */
    public void removeValidator(Class clazz) {
        ATValidator validator = validatorCache.get(clazz);
        if(validator != null) {
            validatorCache.remove(validator);
        }
    }

	private void initCache() {
		addValidator(Accessions.class, new AccessionValidator());
		addValidator(ArchDescriptionAnalogInstances.class, new ArchDescriptionAnalogInstancesValidator());
		addValidator(ArchDescriptionNames.class, new ArchDescriptionNamesValidator());
		addValidator(ArchDescriptionNotes.class, new ArchDescriptionNotesValidator());
		addValidator(DigitalObjects.class, new DigitalObjectValidator());
		addValidator(FileVersions.class, new FileVersionsValidator());
		addValidator(Locations.class, new LocationsValidator());
		addValidator(LookupList.class, new LookupListValidator());
		addValidator(LookupListItems.class, new LookupListItemsValidator());
		addValidator(Names.class, new NamesValidator());
		addValidator(NonPreferredNames.class, new NonPreferredNamesValidator());
		addValidator(NameContactNotes.class, new NameContactNoteValidator());
		addValidator(Repositories.class, new RepositoryValidator());
		addValidator(RepositoryStatistics.class, new RepositoryStatisticsValidator());
		addValidator(Resources.class, new ResourcesValidator());
		addValidator(ResourcesComponents.class, new ResourcesComponentsValidator());
		addValidator(Subjects.class, new SubjectValidator());
		addValidator(Users.class, new UserValidator());
		addValidator(DefaultValues.class, new DefaultValueValidator());
		addValidator(RepositoryNotes.class, new RepositoryNoteValidator());
		addValidator(Deaccessions.class, new DeaccessionValidator());

		addValidator(Index.class, new IndexValidator());
		addValidator(IndexItems.class, new IndexItemsValidator());

		addValidator(Bibliography.class, new BibliographyValidator());
		addValidator(BibItems.class, new BibItemsValidator());

		addValidator(ChronologyList.class, new ChronologyValidator());
		addValidator(ChronologyItems.class, new ChronItemsValidator());

		addValidator(ListOrdered.class, new ListOrderedValidator());
		addValidator(ListOrderedItems.class, new ListOrderedItemsValidator());

		addValidator(ListDefinition.class, new ListDefinitionValidator());
		addValidator(ListDefinitionItems.class, new ListDefinitionItemsValidator());

		addValidator(DatabaseTables.class, new DatabaseTablesValidator());
		addValidator(DatabaseFields.class, new DatabaseFieldsValidator());

		addValidator(RDEScreen.class, new RDEScreenValidator());

		addValidator(NotesEtcTypes.class, new NotesEtcTypesValidator());

        // add a validator for the events class
        addValidator(Events.class, new EventsValidator());

        // add validator for assessments class
        addValidator(Assessments.class, new AssessmentsValidator());

         // add a validator for the physical descriptions class
        addValidator(ArchDescriptionPhysicalDescriptions.class, new ArchDescriptionPhysicalDescriptionsValidator());
    }

	public ATValidator getValidator(DomainObject objectToValidate) {
		ATValidator validator = validatorCache.get(objectToValidate.getClass());
		if (validator == null) {
			return null;
		} else {
			validator.setModelToValidate(objectToValidate);
			return validator;
		}
	}

}
