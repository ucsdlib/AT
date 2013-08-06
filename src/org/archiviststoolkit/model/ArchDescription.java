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
 * Date: Mar 18, 2006
 * Time: 9:54:26 AM
 */

package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.mydomain.SequencedObject;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.structure.*;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.util.ATDateUtils;
import org.archiviststoolkit.util.NameUtils;

import java.util.*;

public abstract class ArchDescription extends DomainObject implements java.io.Serializable, SubjectEnabledModel, NameEnabledModel {

	public static final String PROPERTYNAME_TITLE = "title";
	public static final String PROPERTYNAME_DATE_EXPRESSION = "dateExpression";
	public static final String PROPERTYNAME_DATE_BEGIN = "dateBegin";
	public static final String PROPERTYNAME_DATE_END = "dateEnd";
    public static final String PROPERTYNAME_ISODATE_BEGIN = "isoDateBegin";
	public static final String PROPERTYNAME_ISODATE_END = "isoDateEnd";
    public static final String PROPERTYNAME_ISODATE_BEGIN_SECONDS = "isoDateBeginSeconds";
	public static final String PROPERTYNAME_ISODATE_END_SECONDS = "isoDateEndSeconds";
	public static final String PROPERTYNAME_RESTRICTIONS_APPLY = "restrictionsApply";
	public static final String PROPERTYNAME_CREATOR = "creator";
	public static final String PROPERTYNAME_DISPLAY_CREATOR = "displayCreator";
	public static final String PROPERTYNAME_DISPLAY_REPOSITORY = "displayRepository";
	public static final String PROPERTYNAME_DISPLAY_SOURCE = "displaySource";
	public static final String PROPERTYNAME_NAMES = "names";
	public static final String PROPERTYNAME_SUBJECTS = "subjects";
	public static final String PROPERTYNAME_REPEATING_DATA = "repeatingData";

	@DefaultIncludeInSearchEditor
	@IncludeInApplicationConfiguration(2)
	@IncludeInRDE
	private String title = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	@IncludeInRDE
	private String dateExpression = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Integer dateBegin;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Integer dateEnd;

    //@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private String isoDateBegin;

	//@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private String isoDateEnd;

    //@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Long isoDateBeginSeconds;

	//@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Long isoDateEndSeconds;

	@IncludeInApplicationConfiguration
	@IncludeInRDE
	private Boolean restrictionsApply = false;

	private Set<ArchDescriptionSubjects> subjects = new HashSet<ArchDescriptionSubjects>();
	private Set<ArchDescriptionNames> names = new TreeSet<ArchDescriptionNames>();
	private Set<ArchDescriptionRepeatingData> repeatingData = new TreeSet<ArchDescriptionRepeatingData>();
    private Set<ArchDescriptionDates> archDescriptionDates = new HashSet<ArchDescriptionDates>();

	/**
	 *
	 */
	public String getTitle() {
		if (this.title != null) {
			return this.title;
		} else {
			return "";
		}
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 *
	 */
	public String getDateExpression() {
		if (this.dateExpression != null) {
			return this.dateExpression;
		} else {
			return "";
		}
	}

	public void setDateExpression(String dateExpression) {
		this.dateExpression = dateExpression;
	}

	/**
	 *
	 */
	public Integer getDateBegin() {
		return this.dateBegin;
	}

	public void setDateBegin(Integer dateBegin) {
		this.dateBegin = dateBegin;
	}

	/**
	 *
	 */
	public Integer getDateEnd() {
		return this.dateEnd;
	}

	public void setDateEnd(Integer dateEnd) {
        Object oldValue = getDateEnd();
        this.dateEnd = dateEnd;
        firePropertyChange(PROPERTYNAME_DATE_END, oldValue, dateEnd);
	}

    /**
     * Method that return the iso date begin reformated to match
     * @return String containing the iso bluck date begin
     */
    public String getIsoDateBegin() {
        if(isoDateBegin == null) { // return just the year
            if(getDateBegin() != null) {
                return getDateBegin() + "";
            } else {
                return "";
            }
        } else { // return the date begin in the correct date format
            return this.isoDateBegin;
        }
    }

    /**
     * Method to set the ISO date begins
     * @param isoDateBegin The new iso  date begins
     */
    public void setIsoDateBegin(String isoDateBegin) {
        this.isoDateBegin = isoDateBegin;

        // for backward compatibility set the year now
        setDateBegin(ATDateUtils.getYearFromISODate(isoDateBegin));

        // set this iso date in seconds used for searchng purposes
        setIsoDateBeginSeconds(ATDateUtils.getISODateInSeconds(isoDateBegin));
    }

    /**
     * Method to return the iso date begins in seconds
     * @return The date begins in seconds
     */
    public Long getIsoDateBeginSeconds() {
        return isoDateBeginSeconds;
    }

    /**
     * Method to set the ISO date begins in GMT seconds
     * @param seconds The iso date begins in GMT seconds
     */
    public void setIsoDateBeginSeconds(Long seconds) {
        this.isoDateBeginSeconds = seconds;
    }

    /**
     * Method that return the iso date begin reformated to match
     * @return String containing the iso bluck date begin
     */
    public String getIsoDateEnd() {
        if(isoDateEnd == null) { // return just the year then
            if(getDateEnd() != null) {
                return getDateEnd() + "";
            } else {
                return "";
            }
        } else { // return the date begin
            return this.isoDateEnd;
        }
    }

    /**
     * Method to set the ISO  date ends
     * @param isoDateEnd The new iso  date ends
     */
    public void setIsoDateEnd(String isoDateEnd) {
        this.isoDateEnd = isoDateEnd;

        // for backward compatibility set the year now
        setDateEnd(ATDateUtils.getYearFromISODate(isoDateEnd));

        // set this iso date in seconds used for searchng purposes
        setIsoDateEndSeconds(ATDateUtils.getISODateInSeconds(isoDateEnd));
    }

    /**
     * Method to return the iso date end in seconds
     * @return The date end in seconds
     */
    public Long getIsoDateEndSeconds() {
        return isoDateEndSeconds;
    }

    /**
     * Method to set the ISO date ends in GMT seconds
     * @param seconds The iso date ends in GMT seconds
     */
    public void setIsoDateEndSeconds(Long seconds) {
        this.isoDateEndSeconds = seconds;
    }

	public Boolean getRestrictionsApply() {
		return restrictionsApply;
	}

	public void setRestrictionsApply(Boolean restrictionsApply) {
		this.restrictionsApply = restrictionsApply;
	}


//repeating data

	public Set<ArchDescriptionRepeatingData> getRepeatingData() {
		return repeatingData;
	}
    
    public Set getRepeatingData(Class clazz){
        Set set = new HashSet();
        for (ArchDescriptionRepeatingData repeatingData : this.getRepeatingData()) {
	        if (clazz.getName().equals(repeatingData.getClass().getName()))
                set.add(repeatingData); 
        }
        return set;
    }

	public void setRepeatingData(Set<ArchDescriptionRepeatingData> repeatingData) {
		this.repeatingData = repeatingData;
	}

	public void addRepeatingData(ArchDescriptionRepeatingData repeatingData) {
		if (repeatingData == null)
			throw new IllegalArgumentException("Can't add a null repeating value.");
        SequencedObject.adjustSequenceNumberForAdd(this.getRepeatingData(), repeatingData);
        this.getRepeatingData().add(repeatingData);

	}

	protected void removeRepeatingData(ArchDescriptionRepeatingData repeatingData) {
		if (repeatingData == null)
			throw new IllegalArgumentException("Can't remove a null repeating value.");

		getRepeatingData().remove(repeatingData);
		SequencedObject.resequenceSequencedObjects(this.getRepeatingData());
	}

	public ArchDescriptionRepeatingData getRepeatingData(int index) {
		ArrayList repeatingData = new ArrayList<ArchDescriptionRepeatingData>(getRepeatingData());
		return (ArchDescriptionRepeatingData) repeatingData.get(index);
	}


	public Set<ArchDescriptionNames> getNames() {
		return names;
	}

    public Set<ArchDescriptionNames> getNames(String function) {
        Set<ArchDescriptionNames> returnSet = new TreeSet<ArchDescriptionNames>();
        for(ArchDescriptionNames nameRelationship: getNames()) {
            if (nameRelationship.getNameLinkFunction().equalsIgnoreCase(function)) {
                returnSet.add(nameRelationship);
            }
        }
        return returnSet;
    }

	public Set<ArchDescriptionNames> getNamesForPrinting() {
		return new TreeSet<ArchDescriptionNames>(names);
	}

	public Set<ArchDescriptionNames> getNamesForPrinting(String function) {
		return getNames(function);
	}

	public void setNames(Set<ArchDescriptionNames> resourcesNames) {
		this.names = resourcesNames;
	}

	/**
	 * Adds a <tt>BillingDetails</tt> to the set.
	 * <p/>
	 * This method checks if there is only one billing method
	 * in the set, then makes this the default.
	 *
	 * @param resourceName
	 */
	public boolean addName(ArchDescriptionNames resourceName) throws DuplicateLinkException {
		if (containsNameLink(resourceName)) {
			throw new DuplicateLinkException(resourceName.getName().getSortName());
		} else {
			if (resourceName == null)
				throw new IllegalArgumentException("Can't add a null contact note.");
			this.getNames().add(resourceName);
			return true;
		}
	}

	private boolean containsNameLink(ArchDescriptionNames newNameLink) {
		for (ArchDescriptionNames nameLink: getNames()) {
			if (nameLink.getName().equals(newNameLink.getName()) &&
					nameLink.getRole().equalsIgnoreCase(newNameLink.getRole()) &&
					nameLink.getNameLinkFunction().equalsIgnoreCase(newNameLink.getNameLinkFunction()) &&
					nameLink.getForm().equalsIgnoreCase(newNameLink.getForm())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes a <tt>BillingDetails</tt> from the set.
	 * <p/>
	 * This method checks if the removed is the default element,
	 * and will throw a BusinessException if there is more than one
	 * left to chose from. This might actually not be the best way
	 * to handle this situation.
	 *
	 * @param resourceName
	 */
	protected void removeName(ArchDescriptionNames resourceName) {
		if (resourceName == null)
			throw new IllegalArgumentException("Can't add a null contact note.");

		getNames().remove(resourceName);
	}


	public ArchDescriptionNames getName(int index) {
		ArrayList names = new ArrayList<ArchDescriptionNames>(getNames());
		return (ArchDescriptionNames) names.get(index);
	}

//subjects

	/**
	 * Adds a <tt>BillingDetails</tt> to the set.
	 * <p/>
	 * This method checks if there is only one billing method
	 * in the set, then makes this the default.
	 *
	 * @param resourceSubject
	 */
	public boolean addSubject(ArchDescriptionSubjects resourceSubject) {
		if (resourceSubject == null)
			throw new IllegalArgumentException("Can't add a null contact note.");
		//make sure that the subject is not already linked
		String subjectToAdd = resourceSubject.getSubjectTerm();
		for (ArchDescriptionSubjects link : getSubjects()) {
			if (link.getSubjectTerm().equalsIgnoreCase(subjectToAdd)) {
				return false;
			}
		}
		this.getSubjects().add(resourceSubject);
		return true;
	}

	public DomainObject addSubject(Subjects subject) throws DuplicateLinkException {
		if (containsSubjectLink(subject.getSubjectTerm())) {
			throw new DuplicateLinkException(subject.getSubjectTerm());
		} else {
			ArchDescriptionSubjects link = new ArchDescriptionSubjects(subject, this);
			if (addSubject(link)) {
				return (link);
			} else {
				return null;
			}
		}
	}

	public boolean containsSubjectLink(String subjectTerm) {
		for (ArchDescriptionSubjects subject: getSubjects()) {
			if (subject.getSubjectTerm().equalsIgnoreCase(subjectTerm)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes a <tt>BillingDetails</tt> from the set.
	 * <p/>
	 * This method checks if the removed is the default element,
	 * and will throw a BusinessException if there is more than one
	 * left to chose from. This might actually not be the best way
	 * to handle this situation.
	 *
	 * @param resourceSubject
	 */
	protected void removeSubject(ArchDescriptionSubjects resourceSubject) {
		if (resourceSubject == null)
			throw new IllegalArgumentException("Can't add a null contact note.");
		getSubjects().remove(resourceSubject);
	}

	public ArchDescriptionSubjects getSubject(int index) {
		ArrayList subjects = new ArrayList<ArchDescriptionSubjects>(getSubjects());
		return (ArchDescriptionSubjects) subjects.get(index);
	}


	public Set<ArchDescriptionSubjects> getSubjects() {
		return subjects;
	}

	public void setSubjects(Set<ArchDescriptionSubjects> subjects) {
		this.subjects = subjects;
	}

	public Set<ArchDescriptionSubjects> getSubjectsForPrinting() {
		return new TreeSet<ArchDescriptionSubjects>(subjects);
	}


	public void removeRelatedObject(DomainObject domainObject) throws ObjectNotRemovedException {
		if (domainObject instanceof ArchDescriptionNames) {
			removeName((ArchDescriptionNames) domainObject);
		} else if (domainObject instanceof ArchDescriptionSubjects) {
			removeSubject((ArchDescriptionSubjects) domainObject);
		} else if (domainObject instanceof ArchDescriptionRepeatingData) {
			removeRepeatingData((ArchDescriptionRepeatingData) domainObject);
		}
	}

	public Collection getRelatedCollection(DomainObject domainObject) {
		if (domainObject instanceof ArchDescriptionSubjects) {
			return getSubjects();
		} else if (domainObject instanceof ArchDescriptionNames) {
			return getNames();
		} else if (domainObject instanceof ArchDescriptionRepeatingData) {
			return getRepeatingData();
		} else {
			return null;
		}
	}

	/**
	 * A dummy place holder to be overridden by classes that have related objects
	 *
	 * @param rowNumber the row of the object to retrieve
	 */
	@Override
	public DomainObject getRelatedObject(DomainObject domainObject, int rowNumber) {
		if (domainObject instanceof ArchDescriptionSubjects) {
			return getSubject(rowNumber);
		} else if (domainObject instanceof ArchDescriptionNames) {
			return getName(rowNumber);
		} else if (domainObject instanceof ArchDescriptionRepeatingData) {
			return getRepeatingData(rowNumber);
		} else {
			return null;
		}
	}

	public String getLabelForTree() {
		if (this.getTitle() != null && this.getTitle().length() > 0) {
			return StringHelper.tagRemover(this.getTitle());
		} else {
			if (this.getDateExpression().length() > 0) {
				return this.getDateExpression();
			} else if (this.getDateBegin() == null) {
				return "";
			} else if (this.getDateBegin().equals(this.getDateEnd())) {
				return this.getDateBegin().toString();
			} else {
				return this.getDateBegin() + "-" + this.getDateEnd();
			}
		}
	}


	public DomainObject addName(Names name, String function, String role, String form) throws DuplicateLinkException {
		ArchDescriptionNames link = new ArchDescriptionNames(name, this);
        link.setNameLinkFunction(function);
        link.setRole(role);
        link.setForm(form);
        if (containsNameLink(link)) {
			throw new DuplicateLinkException(name.getSortName());
		} else {
			if (addName(link)) {
				return (link);
			} else {
				return null;
			}
		}
	}

//    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_CREATOR)
	public String getCreator() {
		//return getFirstNameByFunction(ArchDescriptionNames.PROPERTYNAME_FUNCTION_CREATOR);
        return getAllNamesByFunction(ArchDescriptionNames.PROPERTYNAME_FUNCTION_CREATOR);
	}

	private String getFirstNameByFunction(String function) {
        for (ArchDescriptionNames nameRelationship: getNames()) {
			if (nameRelationship.getNameLinkFunction().equalsIgnoreCase(function)) {
                return nameRelationship.getName().getSortName();
			}
		}
		return "";
	}

	public String getSource() {
		return getFirstNameByFunction(ArchDescriptionNames.PROPERTYNAME_FUNCTION_SOURCE);
	}

    /**
     * Method to return a list of names in alphabitcal order by function
     *
     * @param function The function to get the name for.
     * @return A list of names in alphabital order to get the list of names for
     */
    private String getAllNamesByFunction(String function) {
        String names = "";
        String creators = "";

        for (ArchDescriptionNames nameRelationship: getNamesForPrinting()) {
			if (nameRelationship.getNameLinkFunction().equalsIgnoreCase(function)) {
				String cleanName = NameUtils.getCleanName(nameRelationship.getName().getSortName());

                if(names.length() == 0) {
                    names += cleanName;
                } else {
                    names += " ; " + cleanName;
                }

                // check to if the role of this name is creator
                if(nameRelationship.getRole().equals("Creator (cre)")) {
                    if(creators.length() == 0) {
                        creators += cleanName;
                    } else {
                        creators += " ; " + cleanName;
                    }
                }
			}
		}

        // see if we have creators listed, if not return all the names found trimed
        // to length. Should really not hard code the length to trim to here though
        if(creators.length() != 0) {
            return StringHelper.trimToLength(creators, 255);
        } else {
            return StringHelper.trimToLength(names, 255);
        }
    }

    public Set<ArchDescriptionDates> getArchDescriptionDates() {
        return archDescriptionDates;
    }

    public void setArchDescriptionDates(Set<ArchDescriptionDates> archDescriptionDates) {
        this.archDescriptionDates = archDescriptionDates;
    }

    public void addArchdescriptionDate(ArchDescriptionDates dateToAdd) {
        if (dateToAdd == null)
            throw new IllegalArgumentException("Can't add a null date.");
        this.getArchDescriptionDates().add(dateToAdd);
    }

    protected void removeArchdescriptionDate(ArchDescriptionDates dateToAdd) {
        if (dateToAdd == null)
            throw new IllegalArgumentException("Can't remove a date.");

        getArchDescriptionDates().remove(dateToAdd);
    }
}
