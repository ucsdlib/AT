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
 * Date: Sep 1, 2006
 * Time: 12:02:36 PM
 */

package org.archiviststoolkit.util;

import org.archiviststoolkit.importer.EADHelper;
import org.archiviststoolkit.importer.EADIngest2;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.LookupException;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.mydomain.ResourcesDAO;
import org.archiviststoolkit.structure.EAD.Item;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

import java.util.*;

public class ResourceUtils {

	private static List<String> additionalLines = Collections.synchronizedList(new ArrayList<String>());
	private static int depth = 0;

	public static void assignPersistentIds(Long resourceId, InfiniteProgressPanel monitor) throws LookupException,
			PersistenceException {

		ResourcesDAO access = new ResourcesDAO();

		Resources resource =
				(Resources) access.findByPrimaryKeyLongSession(resourceId);

		for (ArchDescriptionRepeatingData repeatingData :
				resource.getRepeatingData()) {
			repeatingData.setPersistentId(resource.getNextPersistentIdAndIncrement());
		}

		for (ResourcesComponents component :
				resource.getResourcesComponents()) {
			assignPersistentIdsComponents(resource, component, monitor, 2);
		}

		access.updateLongSession(resource);
	}

	private static void assignPersistentIdsComponents(Resources resource,
													  ResourcesComponents component,
													  InfiniteProgressPanel monitor, int
			textDepth) {
		if (component.getPersistentId() == null || component.getPersistentId().length() == 0) {
			component.setPersistentId(resource.getNextPersistentIdAndIncrement());
		}
		monitor.setTextLine(component.getTitle(), textDepth);
		if (component.getRepeatingData().size() == 0) {
			component.setHasNotes(false);
		} else {
			component.setHasNotes(false);
			for (ArchDescriptionRepeatingData repeatingData :
					component.getRepeatingData()) {
				if (repeatingData.getPersistentId() == null || repeatingData.getPersistentId().length() == 0) {
					repeatingData.setPersistentId(resource.getNextPersistentIdAndIncrement());
				}
			}
		}

		for (ResourcesComponents childComponent :
				component.getResourcesComponents()) {
			assignPersistentIdsComponents(resource, childComponent, monitor, textDepth + 1);
		}
	}

	public static void fixMissingPersistentIds() {

	}

	public static void gatherPersistentIdList(Resources resource) {

		persistentIdList = new ArrayList<PersistentId>();
		//add a blank line to the top
		persistentIdList.add(new PersistentId("", ""));

		for (ArchDescriptionRepeatingData repeatingData :
				resource.getRepeatingData()) {
			persistentIdList.add(new PersistentId(repeatingData.getFullDescription(),
					repeatingData.getPersistentId()));
			System.out.println(repeatingData.getFullDescription());
		}

		for (ResourcesComponents component :
				resource.getResourcesComponents()) {
			gatherPersistentIdList(component, "  ");
		}
	}

	private static void gatherPersistentIdList(ResourcesComponents component,
											   String indent) {
		persistentIdList.add(new PersistentId(indent + "Component: " +
				component.getLabelForTree(),
				component.getPersistentId()));
		System.out.println(indent + "Component: " +
				component.getLabelForTree());

		for (ArchDescriptionRepeatingData repeatingData :
				component.getRepeatingData()) {
			persistentIdList.add(new PersistentId(indent + "   " +
					repeatingData.getFullDescription(),
					repeatingData.getPersistentId()));
			System.out.println(indent + "   " +
					repeatingData.getFullDescription());
		}

		for (ResourcesComponents childComponent :
				component.getResourcesComponents()) {
			gatherPersistentIdList(childComponent, indent + "  ");
		}
	}

	private static ArrayList<PersistentId> persistentIdList;

	public static Object[] getReferencesArray() {
		ArrayList temp = new ArrayList(persistentIdList);
		temp.add(0, "");
		return temp.toArray();
	}

	public static int getIndexById(String id) {
		int index = 0;
		if (id.length() > 0) {
			for (PersistentId persistentId : persistentIdList) {
				if (persistentId.getPersistentId().equals(id)) {
					return index;
				}
				index++;
			}
		}
		return -1;
	}

	public static void initPersistentIdList() {
		persistentIdList = new ArrayList<PersistentId>();
	}

	public static void addToPersistentIdList(String description,
											 String persistentId) {
		persistentIdList.add(new PersistentId(description, persistentId));
	}

	public static class PersistentId {

		private String persistentId;
		private String description;

		public PersistentId(String description, String persistentId) {
			this.description = description;
			this.persistentId = persistentId;
		}

		public String getPersistentId() {
			return persistentId;
		}

		public void setPersistentId(String persistentId) {
			this.persistentId = persistentId;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String toString() {
			String description = "";
			if (getDescription().length() == 0) {
				return "";
			} else if (getDescription().length() > 100) {
				description = getDescription().substring(0, 100) + "...";
			} else {
				description = getDescription();
			}
			return description + " (" + getPersistentId() + ")";
		}
	}

	public static void rewriteTargets(Resources resource, HashMap idPairs, InfiniteProgressPanel progressPanel) {
		additionalLines = Collections.synchronizedList(new ArrayList<String>());
		additionalLines.add("Rewriting ref ids");
		depth = 1;
		rewriteResources(resource, idPairs);
		handleResourcesComponents(resource.getResourcesComponents(), idPairs, progressPanel);
	}

	public static void rewriteTargets(ResourcesComponents component, HashMap idPairs, InfiniteProgressPanel progressPanel) {
		additionalLines = Collections.synchronizedList(new ArrayList<String>());
		additionalLines.add("Rewriting ref ids");
		depth = 1;
		rewriteResourcesComponents(component, idPairs);
		handleResourcesComponents(component.getResourcesComponents(), idPairs, progressPanel);
	}

	private static void handleResourcesComponents(Set<ResourcesComponents> resourceComponents,
												  HashMap idPairs, InfiniteProgressPanel progressPanel) {

		depth++;
		for (ResourcesComponents rc : resourceComponents) {
			progressPanel.setTextLine(rc.getTitle(), depth);
			rewriteResourcesComponents(rc, idPairs);
			if (rc.getResourcesComponents().size() > 0)
				handleResourcesComponents(rc.getResourcesComponents(),
						idPairs, progressPanel);
		}
		depth--;
	}

	public static void rewriteResources(ResourcesCommon resource,
										HashMap idPairs) {
		ResourceUtils.rewriteField(resource, "title", idPairs);
		ResourceUtils.rewriteField(resource, "repositoryProcessingNote",
				idPairs);
		ResourceUtils.rewriteField(resource, "dateExpression", idPairs);
		ResourceUtils.rewriteField(resource, "containerSummary", idPairs);

		ResourceUtils.rewriteField(resource, "findingAidTitle", idPairs);
		ResourceUtils.rewriteField(resource, "findingAidSubtitle", idPairs);
		ResourceUtils.rewriteField(resource, "author", idPairs);
		ResourceUtils.rewriteField(resource, "languageOfFindingAid", idPairs);
		ResourceUtils.rewriteField(resource, "sponsorNote", idPairs);
		ResourceUtils.rewriteField(resource, "editionStatement", idPairs);
		ResourceUtils.rewriteField(resource, "revisionDate", idPairs);
		ResourceUtils.rewriteField(resource, "revisionDescription", idPairs);
		ResourceUtils.rewriteField(resource, "findingAidNote", idPairs);

		handleNotesEtc(resource.getRepeatingData(), idPairs);
	}


	private static void handleNotesEtc(Set<ArchDescriptionRepeatingData> adrd,
									   HashMap idPairs) {
		for (ArchDescriptionRepeatingData rd : adrd) {
			if (rd instanceof ArchDescriptionNotes) {
				ResourceUtils.rewriteField(rd, "noteContent", idPairs, false);
				
				if(((ArchDescriptionNotes) rd).getMultiPart())
					handleNotesEtc(((ArchDescriptionNotes)rd).getChildren(),idPairs);
				
				
				
			} else if (rd instanceof Bibliography) {
				ResourceUtils.rewriteField(rd, "note", idPairs);
				for (ArchDescriptionStructuredDataItems bibItem1 :
						((Bibliography) rd).getBibItems()) {
					BibItems bibItem = (BibItems) bibItem1;
					ResourceUtils.rewriteField(bibItem, "itemValue", idPairs);
				}
			} else if (rd instanceof Index) {
				ResourceUtils.rewriteField(rd, "note", idPairs);
				for (ArchDescriptionStructuredDataItems indexItem1 :
						((Index) rd).getIndexItems()) {
					IndexItems indexItem = (IndexItems) indexItem1;
					ResourceUtils.rewriteField(indexItem, "reference",
							idPairs);
				}

			} else if (rd instanceof ListOrdered) {
				ResourceUtils.rewriteField(rd, "note", idPairs);
				for (ArchDescriptionStructuredDataItems listItem1 :
						((ListOrdered) rd).getListItems()  ) {
					ListOrderedItems listItem = (ListOrderedItems) listItem1;
					ResourceUtils.rewriteField(listItem, "itemValue",
							idPairs);
				}

			} else if (rd instanceof ListDefinition) {
				ResourceUtils.rewriteField(rd, "note", idPairs);
				for (ArchDescriptionStructuredDataItems listItem1 :
						((ListDefinition) rd).getListItems()  ) {
					ListDefinitionItems listItem = (ListDefinitionItems) listItem1;
					ResourceUtils.rewriteField(listItem, "itemValue",
							idPairs);
				}

			} else if (rd instanceof ChronologyList){
				for (ArchDescriptionStructuredDataItems cItems: (  ((ChronologyList)rd).getChronologyItems())){
					for( Events event : ((ChronologyItems)cItems).getEvents()){
						ResourceUtils.rewriteField(event, "eventDescription",idPairs);					}
				}
			}
			
		}
	}

	public static void rewriteResourcesComponents(ResourcesCommon resource,
												  HashMap idPairs) {
		ResourceUtils.rewriteField(resource, "title", idPairs);
		ResourceUtils.rewriteField(resource, "repositoryProcessingNote",
				idPairs);
		ResourceUtils.rewriteField(resource, "dateExpression", idPairs);
		ResourceUtils.rewriteField(resource, "containerSummary", idPairs);
		handleNotesEtc(resource.getRepeatingData(), idPairs);

	}

	public static void rewriteField(Object obj, String fieldName,
									HashMap idPairs) {
		rewriteField(obj, fieldName, idPairs, true);
	}

	public static void rewriteField(Object obj, String fieldName,
									HashMap idPairs, boolean cleanupField) {
		String str = (String) EADHelper.getProperty(obj, fieldName);
		if (str != null) {
			for (Object objt : idPairs.keySet()) {
				str = str.replace("\""+(String) objt+"\"", "\"" + (String) idPairs.get(objt)+"\"");
				str = str.replace("'"+(String) objt+"'", "'" + (String) idPairs.get(objt)+"'");

			}
			EADHelper.setProperty(obj, fieldName, str, null, cleanupField);
		}
	}

//    public static void rewriteTargets(Resources resource,
//                                      ArrayList<TargetPairs> targetPairs) {
//    }
//
//    public class TargetPairs {
//
//        private String targetBefore;
//        private String targetAfter;
//
//        public TargetPairs(String targetBefore, String targetAfter) {
//            this.targetBefore = targetBefore;
//            this.targetAfter = targetAfter;
//        }
//
//        public String getTargetBefore() {
//            return targetBefore;
//        }
//
//        public void setTargetBefore(String targetBefore) {
//            this.targetBefore = targetBefore;
//        }
//
//        public String getTargetAfter() {
//            return targetAfter;
//        }
//
//        public void setTargetAfter(String targetAfter) {
//            this.targetAfter = targetAfter;
//        }
//    }
}
