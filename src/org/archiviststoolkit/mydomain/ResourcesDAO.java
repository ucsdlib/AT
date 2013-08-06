/**
 * Archivists' Toolkit(TM) Copyright � 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
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
 * Date: Nov 28, 2005
 * Time: 3:17:18 PM
 */

package org.archiviststoolkit.mydomain;

import org.archiviststoolkit.model.*;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.util.ResourceUtils;
import org.archiviststoolkit.exceptions.MergeException;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.exceptions.DeleteException;
import org.hibernate.*;
import org.hibernate.criterion.Expression;

import java.util.*;
import java.sql.SQLException;

public class ResourcesDAO extends DomainAccessObjectImpl {

	private Boolean newRecordCreated = false;

	/**
	 * Constructor which builds a DAO for this class.
	 */
	public ResourcesDAO() {
		super(Resources.class);
	}

	public Resources lookupResource(String resourceId1, boolean create) {
		return lookupResource(resourceId1, null, null, null, create, null);
	}

	public Resources lookupResource(String resourceId1, boolean create, Repositories repository) {
		return lookupResource(resourceId1, null, null, null, create, repository);
	}

	public Resources lookupResource(String resourceId1, String resourceId2, boolean create) {
		return lookupResource(resourceId1, resourceId2, null, null, create, null);
	}

	public Resources lookupResource(String resourceId1, String resourceId2, boolean create, Repositories repository) {
		return lookupResource(resourceId1, resourceId2, null, null, create, repository);
	}

	public Resources lookupResource(String resourceId1, String resourceId2, String resourceId3, boolean create) {
		return lookupResource(resourceId1, resourceId2, resourceId3, null, create, null);
	}

	public Resources lookupResource(String resourceId1, String resourceId2, String resourceId3, boolean create, Repositories repository) {
		return lookupResource(resourceId1, resourceId2, resourceId3, null, create, repository);
	}

	public Resources lookupResource(String resourceId1, String resourceId2, String resourceId3, String resourceId4, boolean create) {
		return lookupResource(resourceId1, resourceId2, resourceId3, null, create, null);
	}

	public Resources lookupResource(String resourceId1, String resourceId2, String resourceId3, String resourceId4, boolean create, Repositories repository) {

		Session session = SessionFactory.getInstance().openSession();
		newRecordCreated = false;
		try {
			Criteria criteria = session.createCriteria(Resources.class);
			if (resourceId1 != null && resourceId1.length() > 0) {
				criteria.add(Expression.eq("resourceIdentifier1", resourceId1));
			}
			if (resourceId2 != null && resourceId2.length() > 0) {
				criteria.add(Expression.eq("resourceIdentifier2", resourceId2));
			}
			if (resourceId3 != null && resourceId3.length() > 0) {
				criteria.add(Expression.eq("resourceIdentifier3", resourceId3));
			}
			if (resourceId4 != null && resourceId4.length() > 0) {
				criteria.add(Expression.eq("resourceIdentifier4", resourceId4));
			}
			Resources resource = (Resources) criteria.uniqueResult();
			session.close();
			if (resource != null) {
				return resource;
			} else if (create) {
				resource = new Resources();
				resource.setResourceIdentifier1(resourceId1);
				if (resourceId2 != null) {
					resource.setResourceIdentifier2(resourceId2);
				}
				if (resourceId3 != null) {
					resource.setResourceIdentifier3(resourceId3);
				}
				if (resourceId4 != null) {
					resource.setResourceIdentifier4(resourceId4);
				}
				if (repository != null) {
					resource.setRepository(repository);
				}
				this.add(resource);
				newRecordCreated = true;
				return resource;
			} else {
				return null;
			}

		} catch (PersistenceException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			return null;
		}
	}

	public Resources findResourceByComponent(ResourcesComponents component) {
		Resources resource;
//		System.out.println(component.getTitle() + component.getIdentifier());
		if (component.getResource() != null) {
			resource = component.getResource();
		} else {
			resource = findResourceByComponent(component.getResourceComponentParent());
		}
		resource.setHitAtComponentLevel(true);
		return resource;
	}

	public Boolean getNewRecordCreated() {
		return newRecordCreated;
	}

	public void updateHasNotesAndHasChildrenFlags(InfiniteProgressPanel progressPanel) {

		int errorCount = 0;
		int recordCount = 0;
		try {
			Collection resources = findAllLongSession();
			Resources resource;
			for (Object o : resources) {
				resource = (Resources) o;
				progressPanel.setTextLine(++recordCount + " of " + resources.size() +
						"    " + resource.getTitle() + " - Errors corrected: " + errorCount, 2);
				for (ResourcesComponents component : resource.getResourcesComponents()) {
					errorCount += updateHasNotesAndHasChildrenFlags(component);
				}
				updateLongSession(resource, false);
			}
			closeLongSession();
		} catch (LookupException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
		} catch (PersistenceException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
		} catch (SQLException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
		}
	}

	public void updateDisplayFields(InfiniteProgressPanel progressPanel) {

		int recordCount = 0;
		try {
			Collection resources = findAllLongSession();
			Resources resource;
			for (Object o : resources) {
				resource = (Resources) o;
				progressPanel.setTextLine("Resources " + ++recordCount + " of " + resources.size(), 2);
				resource.setDisplayCreator(resource.getCreator());
				resource.setDisplayRepository(resource.getRepository().getRepositoryName());
				resource.setDisplaySource(resource.getDisplaySource());
				updateLongSession(resource, false);
			}
			closeLongSession();
		} catch (LookupException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
		} catch (PersistenceException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
		} catch (SQLException e) {
			new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
		}
	}

	public int updateHasNotesAndHasChildrenFlags(ResourcesComponents component) {

		Set repeatingData = component.getRepeatingData();
		Boolean hasNotes;
		int returnErrorCount = 0;
		if (repeatingData.size() == 0) {
			hasNotes = false;
		} else {
			hasNotes = true;
		}
		if (!hasNotes.equals(component.getHasNotes())) {
			returnErrorCount++;
			component.setHasNotes(hasNotes);
		}

		Boolean hasChildren;
		Set<ResourcesComponents> childComponents = component.getResourcesComponents();
		if (childComponents.size() == 0) {
			hasChildren = false;
		} else {
			hasChildren = true;
		}
		if (!hasChildren.equals(component.isHasChild())) {
			returnErrorCount++;
			component.setHasChild(hasChildren);
		}

		if (component.isHasChild()) {
			for (ResourcesComponents childComponent : childComponents) {
				returnErrorCount += updateHasNotesAndHasChildrenFlags(childComponent);
			}
		}

		return returnErrorCount;
	}

	/**
	 * Delete these objects from the data store.
	 *
	 * @param collection the object to delete
	 * @throws org.archiviststoolkit.mydomain.PersistenceException
	 *          fails if we cannot delete the instance
	 */

	public void deleteGroup(final Collection collection, final InfiniteProgressPanel progressPanel) throws PersistenceException {

		int count = 1;
		int numberOfResources = collection.size();
		for (Object object : collection) {
            if(!progressPanel.isProcessCancelled()) { // check to see if this process was not cancelled
			    progressPanel.setTextLine("Deleting record " + count++ + " of " + numberOfResources, 1);

                // check to make sure this is a resource record before attempting to delete it
                if(object instanceof Resources) {
                    deleteResource((Resources) object, progressPanel);
                } else { // alert the user that search result can't be deleted
                    ResourcesComponentsSearchResult searchResult = (ResourcesComponentsSearchResult)object;

                    // if the return search result is not a component
                    // get the resource record and delete it
                    if(searchResult.getCompenent() == null) {
                        deleteResource(searchResult.getParentResource(), progressPanel);
                    }
                }
            } else {
                return; // break out of loop since the process was canceled
            }
		}
	}

	/**
	 * Delete this object from the data store.
	 *
	 * @param domainObject the object to delete
	 *          fails if we cannot delete the instance
	 */

	private void deleteResource(final DomainObject domainObject, InfiniteProgressPanel progressPanel) {
		//gather components
		Session session = SessionFactory.getInstance().openSession();
		Resources resource = (Resources)session.load(getPersistentClass(), domainObject.getIdentifier());
		ArrayList<ResourcesComponents> componentList = new ArrayList<ResourcesComponents>();
		for (ResourcesComponents component: resource.getResourcesComponents()) {
            unlinkDigitalObjects(component, session); //  unlink any digital objects
			processComponetsForDelete(componentList, 1, progressPanel, component, null, session);
		}
		session.flush();
		SessionFactory.getInstance().closeSession(session);

		//delete the components
		try {
			DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(ResourcesComponents.class);
			Collection dummyCollection;
			int count = 1;
			int numberOfComponents = componentList.size();
			for (ResourcesComponents component: componentList) {
                // if process cancelled just return
                if(progressPanel.isProcessCancelled()) {
                    return;
                }

                progressPanel.setTextLine("Deleting components: " + count++ + " of " + numberOfComponents, 2);
				dummyCollection = new ArrayList();
				dummyCollection.add(component);
				access.deleteGroup(dummyCollection);
			}
		} catch (PersistenceException e) {
			progressPanel.close();
			new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
		} catch (LookupException e) {
			progressPanel.close();
			new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
		}

		//delete the resource
		session = SessionFactory.getInstance().openSession();
		resource = (Resources)session.load(getPersistentClass(), domainObject.getIdentifier());
        unlinkDigitalObjects(resource, session);
		session.delete(resource);
		session.flush();
		try {
			session.connection().commit();
		} catch (SQLException e) {
			progressPanel.close();
			new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
		}
		SessionFactory.getInstance().closeSession(session);
	}

	private void processComponetsForDelete(ArrayList<ResourcesComponents> componentList,
										   int depth,
										   InfiniteProgressPanel progressPanel,
										   ResourcesComponents component,
										   ResourcesComponents parentComponent, Session session) {
		for (ResourcesComponents childComponent: component.getResourcesComponents()) {
            unlinkDigitalObjects(childComponent, session); //  unlink any digital objects
			processComponetsForDelete(componentList, depth + 1, progressPanel, childComponent, component, session);
		}
		if (parentComponent != null) {
			componentList.add(component);
			progressPanel.setTextLine("Gathering components - Depth: " + depth + " - " + component.getLabelForTree(), 2);
		}
	}

    /**
     * Get any digital instance and unlink the digital object. This needs
     * to be done because of the circular relationship between digital instances
     * and digital objects
     *
     * @param resourceCommon The resource or resource component to unlink DO from
     */
    private void unlinkDigitalObjects(ResourcesCommon resourceCommon, Session session) {
        Set<ArchDescriptionDigitalInstances> instances = resourceCommon.getInstances(ArchDescriptionDigitalInstances.class);

        for (ArchDescriptionDigitalInstances instance : instances) {
            DigitalObjects digitalObject = instance.getDigitalObject();

            if (digitalObject != null) {
                digitalObject.setDigitalInstance(null);
                instance.setDigitalObject(null);
                try {
                    session.update(digitalObject); // must update the digital object so we don't get a foreign key constraint violation
                    session.flush();
                    session.connection().commit();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

	public int merge(Collection<DomainObject> mergeFrom, DomainObject mergeTo, InfiniteProgressPanel progressPanel) throws MergeException {
		Session session = getLongSession();
		Transaction tx = session.beginTransaction();
		Resources resourceMergeTo = null;
		try {
			resourceMergeTo = (Resources)findByPrimaryKeyLongSession(mergeTo.getIdentifier());
		} catch (LookupException e) {
			rollBackAndThrowMergeException(session, tx, e);
		}
//		session.lock(resourceMergeTo, LockMode.NONE);
		Resources resource;
		String message;
		int totalCount = 0;
		int resourcesToMerge = mergeFrom.size() - 1;
		int resourcesMerged = 1;
		ArrayList<ResourcesComponents> componentsToMove = new ArrayList<ResourcesComponents>();
		for (DomainObject domainObject: mergeFrom) {
			try {
				resource = (Resources)domainObject;
				if (!resource.equals(resourceMergeTo)) {
					session.lock(resource, LockMode.NONE);
					progressPanel.setTextLine("Merging (record " + resourcesMerged++ + " of " + resourcesToMerge + ")...", 1);
					progressPanel.setTextLine(resource + " -> " + resourceMergeTo, 2);
					totalCount += transferTopLevelComponents(progressPanel, session, tx, resourceMergeTo, resource, componentsToMove);
					deleteLongSession(resource, false);
				}
			} catch (HibernateException e) {
				rollBackAndThrowMergeException(session, tx, e);
			} catch (DeleteException e) {
				rollBackAndThrowMergeException(session, tx, e);
			}
		}
		// now add the componets to the mergeTo resource
		int count = 1;
		int numberOfLinks = componentsToMove.size();
		resourceMergeTo.resequenceSequencedObjects();
		int nextSequenceNumber = resourceMergeTo.getNextSequenceNumber();
		for (ResourcesComponents component: componentsToMove) {
			progressPanel.setTextLine("Adding components " + count++ + " of " + numberOfLinks, 2);
			component.setSequenceNumber(nextSequenceNumber++);
			resourceMergeTo.addComponent(component);
			component.setResource(resourceMergeTo);
		}
		progressPanel.setTextLine("Saving record", 2);
		session.update(resourceMergeTo);
		tx.commit();
		session.close();
		return totalCount;
	}

	private void rewriteRefIds(Resources newResourceParent, ResourcesComponents component, HashMap<String, String> idPairs) {
		String before = component.getPersistentId();
		String after = newResourceParent.getNextPersistentIdAndIncrement();
		component.setPersistentId(after);
		idPairs.put(before, after);
		for (ArchDescriptionRepeatingData repeatingData: component.getRepeatingData()) {
			before = repeatingData.getPersistentId();
			after = newResourceParent.getNextPersistentIdAndIncrement();
			repeatingData.setPersistentId(after);
			idPairs.put(before, after);
		}

		for (ResourcesComponents child: component.getResourcesComponents()) {
			rewriteRefIds(newResourceParent, child, idPairs);
		}
	}

	private int transferTopLevelComponents(InfiniteProgressPanel progressPanel, Session session, Transaction tx, Resources mergeToResource, Resources resource, ArrayList<ResourcesComponents> componentsToMove) throws MergeException, DeleteException {
		String message;
		int count = 1;
		int numberOfLinks = resource.getResourcesComponents().size();
		//first gather components to merge
		ArrayList<ResourcesComponents> componentsToMoveForThisResource = new ArrayList<ResourcesComponents>(resource.getResourcesComponents());
		HashMap<String, String> idPairs;
		for (ResourcesComponents component: componentsToMoveForThisResource) {
			try {
				message = "component " + count++ + " of " + numberOfLinks;
				progressPanel.setTextLine(message, 3);
				resource.removeComponent(component);
				component.setResource(null);
				idPairs = new HashMap<String, String>();
				rewriteRefIds(mergeToResource, component, idPairs);
				ResourceUtils.rewriteTargets(component, idPairs, progressPanel);
				componentsToMove.add(component);
			} catch (ObjectNotRemovedException e) {
				rollBackAndThrowMergeException(session, tx, e);
			}
		}
		return count;
	}

	public int transfer(Resources transferFrom, Resources mergeToParentResource, ResourcesCommon mergeTo, InfiniteProgressPanel progressPanel) throws MergeException {
		Session session = getLongSession();
		Transaction tx = session.beginTransaction();
		ArrayList<ResourcesComponents> transferedComponents = new ArrayList<ResourcesComponents>();
		Resources resourceToTransferFrom = null;
		try {
			resourceToTransferFrom = (Resources)findByPrimaryKeyLongSession(transferFrom.getIdentifier());
		} catch (LookupException e) {
			rollBackAndThrowMergeException(session, tx, e);
		}
		int numberOfTransferedComponents = 0;
		try {
			numberOfTransferedComponents = transferTopLevelComponents(progressPanel, session, tx, mergeToParentResource, resourceToTransferFrom,  transferedComponents);
		} catch (DeleteException e) {
			rollBackAndThrowMergeException(session, tx, e);
		}
		int count = 0;
		mergeTo.resequenceSequencedObjects();
		int nextSequenceNumber = mergeTo.getNextSequenceNumber();
		for (ResourcesComponents component:  transferedComponents) {
			progressPanel.setTextLine("Adding components " + count++ + " of " + numberOfTransferedComponents, 2);
			component.setSequenceNumber(nextSequenceNumber++);
			mergeTo.addComponent(component);
			if (mergeTo instanceof Resources) {
				component.setResource((Resources)mergeTo);
			} else {
				component.setResourceComponentParent((ResourcesComponents)mergeTo);
			}

            // call method to reset the parent resource for digital instances
            resetParentResourceForDigitalInstances(component, mergeToParentResource);
		}
		try {
			updateLongSession(mergeToParentResource, false);
			updateLongSession(resourceToTransferFrom, false);
		} catch (PersistenceException e) {
			rollBackAndThrowMergeException(session, tx, e);
		}
		tx.commit();
		return count;
	}

    /**
     * Method to reset the parent resource record for digital instances. This
     * is a recursive method since all the children of the components need
     * to be process also
     *
     * @param component The component to check
     * @param mergeToParentResource
     */
    private void resetParentResourceForDigitalInstances(ResourcesComponents component, Resources mergeToParentResource) {
        SortedSet<ResourcesComponents> childComponents = component.getResourcesComponents();

        if(childComponents.size() != 0) {
            for (ResourcesComponents childComponent : childComponents) {
                resetParentResourceForDigitalInstances(childComponent,  mergeToParentResource);
            }
        }

        // update any instances with the new parent resource
        for (ArchDescriptionInstances instance : component.getInstances()) {
             if (instance instanceof ArchDescriptionDigitalInstances) {
                ArchDescriptionDigitalInstances digitalInstance = (ArchDescriptionDigitalInstances) instance;
                digitalInstance.setParentResource(mergeToParentResource);
             }
        }
    }

    private int rollBackAndThrowMergeException(Session session, Transaction tx, Exception e) throws MergeException {
		tx.rollback();
		session.close();
		throw new MergeException("Error merging resources", e);
	}
}
