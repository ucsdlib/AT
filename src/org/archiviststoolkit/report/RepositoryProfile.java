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
 * Date: Oct 27, 2006
 * Time: 9:42:37 PM
 */

package org.archiviststoolkit.report;

import org.archiviststoolkit.model.*;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

import java.util.TreeMap;
import java.util.ArrayList;

public class RepositoryProfile {

	private Integer totalResources = 0;
	private Integer totalResourcesItems = 0;
	private Integer resourcesWithFindingAids = 0;
	private Integer resourcesWithRestrictions = 0;

	private Integer totalAccessions = 0;
	private Integer accessionsCataloged = 0;
	private Integer accessionsProcessed = 0;
	private Integer accessionsWithRestrictions = 0;
	private Integer accessionsWithRightsTransfered = 0;

	private Integer names = 0;
	private Integer namesPersonal = 0;
	private Integer namesCorporate = 0;
	private Integer namesFamily = 0;
	private Integer namesNonpreferred = 0;

	private Integer totalSubjects = 0;

	private String repositoryName;

	private TreeMap<String, ExtentStats> extentStatistics = new TreeMap<String, ExtentStats>();
	private TreeMap<String, ResourcesFindingAidStatus> resourceFindingAidStatusStatistics = new TreeMap<String, ResourcesFindingAidStatus>();
	private TreeMap<String, ResourcesLanguages> resourceLanguageStatistics = new TreeMap<String, ResourcesLanguages>();
    private TreeMap<String, ResourcesInstances> resourceInstanceTypeStatistics = new TreeMap<String, ResourcesInstances>();
    private TreeMap<String, SubjectTypes> subjectTypeStatistics = new TreeMap<String, SubjectTypes>();

	public RepositoryProfile() {
		setRepositoryName(ApplicationFrame.getInstance().getCurrentUserRepository().getRepositoryName());
	}

	public Integer getTotalResources() {
		return totalResources;
	}

	public void setTotalResources(Integer totalResources) {
		this.totalResources = totalResources;
	}

	public Integer getResourcesWithFindingAids() {
		return resourcesWithFindingAids;
	}

	public void setResourcesWithFindingAids(Integer resourcesWithFindingAids) {
		this.resourcesWithFindingAids = resourcesWithFindingAids;
	}

	public Integer getResourcesWithRestrictions() {
		return resourcesWithRestrictions;
	}

	public void setResourcesWithRestrictions(Integer resourcesWithRestrictions) {
		this.resourcesWithRestrictions = resourcesWithRestrictions;
	}

	public Integer getTotalAccessions() {
		return totalAccessions;
	}

	public void setTotalAccessions(Integer totalAccessions) {
		this.totalAccessions = totalAccessions;
	}

	public Integer getAccessionsCataloged() {
		return accessionsCataloged;
	}

	public void setAccessionsCataloged(Integer accessionsCataloged) {
		this.accessionsCataloged = accessionsCataloged;
	}

	public Integer getAccessionsProcessed() {
		return accessionsProcessed;
	}

	public void setAccessionsProcessed(Integer accessionsProcessed) {
		this.accessionsProcessed = accessionsProcessed;
	}

	public Integer getAccessionsWithRestrictions() {
		return accessionsWithRestrictions;
	}

	public void setAccessionsWithRestrictions(Integer accessionsWithRestrictions) {
		this.accessionsWithRestrictions = accessionsWithRestrictions;
	}

	public Integer getNames() {
		return names;
	}

	public void setNames(Integer names) {
		this.names = names;
	}

	public Integer getNamesPersonal() {
		return namesPersonal;
	}

	public void setNamesPersonal(Integer namesPersonal) {
		this.namesPersonal = namesPersonal;
	}

	public Integer getNamesCorporate() {
		return namesCorporate;
	}

	public void setNamesCorporate(Integer namesCorporate) {
		this.namesCorporate = namesCorporate;
	}

	public Integer getNamesFamily() {
		return namesFamily;
	}

	public void setNamesFamily(Integer namesFamily) {
		this.namesFamily = namesFamily;
	}

	public Integer getNamesNonpreferred() {
		return namesNonpreferred;
	}

	public void setNamesNonpreferred(Integer namesNonpreferred) {
		this.namesNonpreferred = namesNonpreferred;
	}

	public Integer getTotalSubjects() {
		return totalSubjects;
	}

	public void setTotalSubjects(Integer totalSubjects) {
		this.totalSubjects = totalSubjects;
	}

	public TreeMap<String, ExtentStats> getExtentStatistics() {
		return extentStatistics;
	}

	public void setExtentStatistics(TreeMap<String, ExtentStats> extentStatistics) {
		this.extentStatistics = extentStatistics;
	}

	public void addAccessionExtent(String extentType, Double extent) {
		if (extent != null) {
			ExtentStats stat = getExtentStat(extentType);
			stat.setAccessions(stat.getAccessions() + extent);
		}
	}

	public void addResourceExtent(String extentType, Double extent) {
		if (extent != null) {
			ExtentStats stat = getExtentStat(extentType);
			stat.setResources(stat.getResources() + extent);
		}
	}

	private ExtentStats getExtentStat(String extentType) {
		ExtentStats stat = extentStatistics.get(extentType);
		if (stat == null) {
			stat = new ExtentStats(extentType);
			extentStatistics.put(extentType, stat);
		}
		return stat;
	}

	public void addResourceFindingAidStatus(String status) {
		ResourcesFindingAidStatus findingAidStatus = resourceFindingAidStatusStatistics.get(status);
		if (findingAidStatus == null) {
			findingAidStatus = new ResourcesFindingAidStatus(status);
			resourceFindingAidStatusStatistics.put(status, findingAidStatus);
		}
		findingAidStatus.incrementCount();
	}

	public void addLanguage(String language) {
		ResourcesLanguages languageStat = resourceLanguageStatistics.get(language);
		if (languageStat == null) {
			languageStat = new ResourcesLanguages(language);
			resourceLanguageStatistics.put(language, languageStat);
		}
		languageStat.incrementCount();
	}

	public void addResourceInstanceType(String instanceType) {
		ResourcesInstances instanceTypeStat = resourceInstanceTypeStatistics.get(instanceType);
		if (instanceTypeStat == null) {
			instanceTypeStat = new ResourcesInstances(instanceType);
			resourceInstanceTypeStatistics.put(instanceType, instanceTypeStat);
		}
		instanceTypeStat.incrementCount();
	}

	protected void addResource(Resources resource, InfiniteProgressPanel progressPanel) {

		totalResources++;
		if (resource.getLevel().equalsIgnoreCase("item")) {
			totalResourcesItems++;
		}
		if (resource.getEadFaUniqueIdentifier() != null &&
				resource.getEadFaUniqueIdentifier().length() > 0) {
			resourcesWithFindingAids++;
			addResourceFindingAidStatus(resource.getFindingAidStatus());
		}
		if (resource.getRestrictionsApply()) {
			resourcesWithRestrictions++;
		}
		addResourceExtent(resource.getExtentType(), resource.getExtentNumber());
		addLanguage(resource.getLanguageCode());
		gatherInstances(resource, progressPanel);
	}

	private void gatherInstances(Resources resource, InfiniteProgressPanel progressPanel) {

		progressPanel.setTextLine(resource.getLabelForTree(), 3);
		for (ArchDescriptionInstances instance: resource.getInstances()) {
			addResourceInstanceType(instance.getInstanceType());
		}
		for (ResourcesComponents component: resource.getResourcesComponents()) {
			gatherInstances(component, progressPanel, 4);
		}
	}

	private void gatherInstances(ResourcesComponents component, InfiniteProgressPanel progressPanel, int textDepth) {

		progressPanel.setTextLine(component.getLabelForTree(), textDepth);
		for (ArchDescriptionInstances instance: component.getInstances()) {
			addResourceInstanceType(instance.getInstanceType());
		}
		if (component.isHasChild()) {
			for (ResourcesComponents childComponent: component.getResourcesComponents()) {
				gatherInstances(childComponent, progressPanel, textDepth + 1);
			}
		}
	}

	protected void addAccession(Accessions accession) {

		totalAccessions++;

		if (accession.getAccessionProcessedDate() != null) {
			accessionsProcessed++;
		} else {
			addAccessionExtent(accession.getExtentType(), accession.getExtentNumber());
		}

		if (accession.getCataloged()) {
			accessionsCataloged++;
		}

		if (accession.getRightsTransferred()) {
			accessionsWithRightsTransfered++;
		}

		if (accession.getRestrictionsApply()) {
			accessionsWithRestrictions++;
		}
	}

	protected void addName(Names name) {
		names++;
		if (name.getNameType().equalsIgnoreCase(Names.PERSON_TYPE)) {
			namesPersonal++;
		} else if (name.getNameType().equalsIgnoreCase(Names.CORPORATE_BODY_TYPE)) {
			namesCorporate++;
		} else if (name.getNameType().equalsIgnoreCase(Names.FAMILY_TYPE)) {
			namesFamily++;
		}
		int nonPreferredNameCount = name.getNonPreferredNames().size();
		namesNonpreferred += nonPreferredNameCount;
	}

    protected void addSubject(Subjects subject) {
        totalSubjects++;
        SubjectTypes subjectStat = subjectTypeStatistics.get(subject.getSubjectTermType());
        if (subjectStat == null) {
            subjectStat = new SubjectTypes(subject.getSubjectTermType());
            subjectTypeStatistics.put(subject.getSubjectTermType(), subjectStat);
        }
        subjectStat.incrementCount();
    }

    public Integer getTotalResourcesItems() {
		return totalResourcesItems;
	}

	public void setTotalResourcesItems(Integer totalResourcesItems) {
		this.totalResourcesItems = totalResourcesItems;
	}

	public ArrayList<ExtentStats> getExtentStats() {
		return new ArrayList<ExtentStats>(extentStatistics.values());
	}

	public ArrayList<ResourcesFindingAidStatus> getFindingAidStatusStats() {
		return new ArrayList<ResourcesFindingAidStatus>(resourceFindingAidStatusStatistics.values());
	}

	public ArrayList<ResourcesLanguages> getLanguageStats() {
		return new ArrayList<ResourcesLanguages>(resourceLanguageStatistics.values());
	}

    public ArrayList<ResourcesInstances> getInstanceStats() {
        return new ArrayList<ResourcesInstances>(resourceInstanceTypeStatistics.values());
    }

    public ArrayList<SubjectTypes> getSubjectStats() {
        return new ArrayList<SubjectTypes>(subjectTypeStatistics.values());
    }

	public Integer getAccessionsWithRightsTransfered() {
		return accessionsWithRightsTransfered;
	}

	public void setAccessionsWithRightsTransfered(Integer accessionsWithRightsTransfered) {
		this.accessionsWithRightsTransfered = accessionsWithRightsTransfered;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}


	public class ResourcesFindingAidStatus {

		private String status;
		private Integer count = 0;

		public ResourcesFindingAidStatus(String status) {
			this.status = status;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public void incrementCount() {
			count++;
		}

		public Integer getCount() {
			return count;
		}

		public void setCount(Integer count) {
			this.count = count;
		}
	}

	public class ResourcesLanguages {

		private String language;
		private Integer count = 0;

		public ResourcesLanguages(String language) {
			this.language = language;
		}

		public String getLanguage() {
			return language;
		}

		public void setLanguage(String language) {
			this.language = language;
		}

		public void incrementCount() {
			count++;
		}

		public Integer getCount() {
			return count;
		}

		public void setCount(Integer count) {
			this.count = count;
		}
	}

	public class ResourcesInstances {

		private String instanceType;
		private Integer count = 0;

		public ResourcesInstances(String instanceType) {
			this.instanceType = instanceType;
		}

		public String getInstanceType() {
			return instanceType;
		}

		public void setInstanceType(String instanceType) {
			this.instanceType = instanceType;
		}

		public void incrementCount() {
			count++;
		}

		public Integer getCount() {
			return count;
		}

		public void setCount(Integer count) {
			this.count = count;
		}
	}
	public class SubjectTypes {

		private String subjectType;
		private Integer count = 0;

		public SubjectTypes(String instanceType) {
			this.subjectType = instanceType;
		}

		public String getSubjectType() {
			return subjectType;
		}

		public void setSubjectType(String subjectType) {
			this.subjectType = subjectType;
		}

		public void incrementCount() {
			count++;
		}

		public Integer getCount() {
			return count;
		}

		public void setCount(Integer count) {
			this.count = count;
		}
	}
}
