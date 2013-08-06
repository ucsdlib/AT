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
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.util.NameUtils;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.DefaultIncludeInSearchEditor;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.StringLengthValidationRequried;

/**
 * The Names class represents a name authority record.
 */

public abstract class BasicNames extends DomainObject {

	public static final String PROPERTYNAME_CORPORATE_PRIMARY_NAME = "corporatePrimaryName";
	public static final String PROPERTYNAME_CORPORATE_SUBORDINATE_1 = "corporateSubordinate1";
	public static final String PROPERTYNAME_CORPORATE_SUBORDINATE_2 = "corporateSubordinate2";
	public static final String PROPERTYNAME_NUMBER = "number";
	public static final String PROPERTYNAME_QUALIFIER = "qualifier";

	public static final String PROPERTYNAME_PERSONAL_PRIMARY_NAME = "personalPrimaryName";
	public static final String PROPERTYNAME_PERSONAL_REST_OF_NAME = "personalRestOfName";
	public static final String PROPERTYNAME_PERSONAL_PREFIX = "personalPrefix";
	public static final String PROPERTYNAME_PERSONAL_SUFFIX = "personalSuffix";
	public static final String PROPERTYNAME_PERSONAL_DATES = "personalDates";
	public static final String PROPERTYNAME_PERSONAL_FULLER_FORM = "personalFullerForm";
	public static final String PROPERTYNAME_PERSONAL_TITLE = "personalTitle";
	public static final String PROPERTYNAME_PERSONAL_DIRECT_ORDER = "personalDirectOrder";

	public static final String PROPERTYNAME_FAMILY_NAME = "familyName";
	public static final String PROPERTYNAME_FAMILY_NAME_PREFIX = "familyNamePrefix";

	public static final String PROPERTYNAME_SORT_NAME = "sortName";
	public static final String PROPERTYNAME_CREATE_SORT_NAME_AUTOMATICALLY = "createSortNameAutomatically";
	public static final String PROPERTYNAME_NAME_ID = "nameId";
	public static final String PROPERTYNAME_NAME_TYPE = "nameType";
	public static final String PROPERTYNAME_NAME_MD5_HASH = "md5Hash";

	public static final String PERSON_TYPE = "Person";
	public static final String FAMILY_TYPE = "Family";
	public static final String CORPORATE_BODY_TYPE = "Corporate Body";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long nameId;
	/**
	 * The core name fields
	 */
	/**
	 * type.
	 */
	@IncludeInApplicationConfiguration(2)
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String nameType = "";

	@IncludeInApplicationConfiguration(1)
	@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String sortName = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Boolean createSortNameAutomatically = true;

	/**
	 * corporate name fields
	 */
	/**
	 * Corporate Primary name.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String corporatePrimaryName = "";
	/**
	 * Corporate Subordinate 1.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String corporateSubordinate1 = "";
	/**
	 * Corporate Subordinate 2.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String corporateSubordinate2 = "";
	/**
	 * Corporate Number.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String number = "";
	/**
	 * corporate qualifier.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String qualifier = "";

	/**
	 * personal name fields
	 */
	/**
	 * primary name (surname etc).
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String personalPrimaryName = "";
	/**
	 * rest of name.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String personalRestOfName = "";
	/**
	 * prefix.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String personalPrefix = "";
	/**
	 * suffix.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String personalSuffix = "";
	/**
	 * dates.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String personalDates = "";
	/**
	 * fuller form.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String personalFullerForm = "";
	/**
	 * direct order.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Boolean personalDirectOrder = false;
	/**
	 * Title.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String personalTitle = "";

	/**
	 * family name fields
	 */
	/**
	 * family name.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String familyName = "";
	/**
	 * family name prefix.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String familyNamePrefix = "";

  /**
	 * No-arg constructor for JavaBean tools.
	 */
	BasicNames() {
	}

	/**
	 * @return nameType
	 */
	public String getNameType() {
		if (this.nameType != null) {
			return this.nameType;
		} else {
			return "";
		}
	}

	public Long getIdentifier() {
		return this.getNameId();
	}

	public void setIdentifier(Long identifier) {
		this.setNameId(identifier);
	}

	public String toString() {
		return sortName;
	}

	public Long getNameId() {
		return nameId;
	}

	public void setNameId(Long nameId) {
		this.nameId = nameId;
	}

	/**
	 * @param type
	 */
	public void setNameType(String type) {
		this.nameType = type;
	}


	public String getCorporatePrimaryName() {
		if (this.corporatePrimaryName != null) {
			return this.corporatePrimaryName;
		} else {
			return "";
		}
	}

	public void setCorporatePrimaryName(String corporatePrimaryName) {
		this.corporatePrimaryName = corporatePrimaryName;
  }

	public String getCorporateSubordinate1() {
		if (this.corporateSubordinate1 != null) {
			return this.corporateSubordinate1;
		} else {
			return "";
		}
	}

	public void setCorporateSubordinate1(String corporateSubordinate1) {
		this.corporateSubordinate1 = corporateSubordinate1;
  }

	public String getCorporateSubordinate2() {
		if (this.corporateSubordinate2 != null) {
			return this.corporateSubordinate2;
		} else {
			return "";
		}
	}

	public void setCorporateSubordinate2(String corporateSubordinate2) {
		this.corporateSubordinate2 = corporateSubordinate2;
  }

	public String getNumber() {
		if (this.number != null) {
			return this.number;
		} else {
			return "";
		}
	}

	public void setNumber(String number) {
		this.number = number;
  }

	public String getQualifier() {
		if (this.qualifier != null) {
			return this.qualifier;
		} else {
			return "";
		}
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
  }

	public String getPersonalPrimaryName() {
		if (this.personalPrimaryName != null) {
			return this.personalPrimaryName;
		} else {
			return "";
		}
	}

	public void setPersonalPrimaryName(String personalPrimaryName) {
		this.personalPrimaryName = personalPrimaryName;
  }

	public String getPersonalRestOfName() {
		if (this.personalRestOfName != null) {
			return this.personalRestOfName;
		} else {
			return "";
		}
	}

	public void setPersonalRestOfName(String personalRestOfName) {
		this.personalRestOfName = personalRestOfName;
  }

	public String getPersonalPrefix() {
		if (this.personalPrefix != null) {
			return this.personalPrefix;
		} else {
			return "";
		}
	}

	public void setPersonalPrefix(String personalPrefix) {
		this.personalPrefix = personalPrefix;
  }

	public String getPersonalSuffix() {
		if (this.personalSuffix != null) {
			return this.personalSuffix;
		} else {
			return "";
		}
	}

	public void setPersonalSuffix(String personalSuffix) {
		this.personalSuffix = personalSuffix;
  }

	public String getPersonalDates() {
		if (this.personalDates != null) {
			return this.personalDates;
		} else {
			return "";
		}
	}

	public void setPersonalDates(String personalDates) {
		this.personalDates = personalDates;
  }

	public String getPersonalFullerForm() {
		if (this.personalFullerForm != null) {
			return this.personalFullerForm;
		} else {
			return "";
		}
	}

	public void setPersonalFullerForm(String personalFullerForm) {
		this.personalFullerForm = personalFullerForm;
  }

	public String getFamilyName() {
		if (this.familyName != null) {
			return this.familyName;
		} else {
			return "";
		}
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
  }

	public String getFamilyNamePrefix() {
		if (this.familyNamePrefix != null) {
			return this.familyNamePrefix;
		} else {
			return "";
		}
	}

	public void setFamilyNamePrefix(String familyNamePrefix) {
		this.familyNamePrefix = familyNamePrefix;
  }

	public void createSortName() {
		createSortName(null, null);
	}

	public void createSortName(String source, String rules) {
		//only do the automatic assignment of sort name for new records

		if (this.createSortNameAutomatically) {
//			String sourceRules;
//			if (source != null && rules != null) {
//				sourceRules = ModelUtil.concat(" / ", source, rules);
//				if (sourceRules.length() > 0) {
//					sourceRules = " (" + sourceRules + ")";
//				}
//			} else {
//				sourceRules = "";
//			}

			if (this.getNameType().equalsIgnoreCase(Names.PERSON_TYPE)) {
				String fullerForm = this.getPersonalFullerForm().length() > 0 ?
						" (" + this.getPersonalFullerForm() + ")" : "";
				String primaryName = this.getPersonalDirectOrder() ?
						StringHelper.concat(" ",
								this.getPersonalRestOfName(),
								this.getPersonalPrimaryName()) :
						StringHelper.concat(", ",
								this.getPersonalPrimaryName(),
								this.getPersonalRestOfName());

				this.setSortName(StringHelper.concat(", ",
						primaryName,
						this.getPersonalPrefix(),
						this.getNumber(),
						this.getPersonalSuffix(),
						this.getPersonalTitle(),
						fullerForm,
						this.getPersonalDates(),
						this.getQualifier()));
				if (this instanceof Names) {
					Names tempName = (Names) this;
					tempName.setSalutation(StringHelper.concat(" ",
							this.getPersonalRestOfName(),
							this.getPersonalPrimaryName()));
				}

			} else if (this.getNameType().equalsIgnoreCase(Names.CORPORATE_BODY_TYPE)) {
				String number = this.getNumber().length() > 0 ?
						" (" + this.getNumber() + ")" : "";
				String qualifier = this.getQualifier().length() > 0 ?
						" (" + this.getQualifier() + ")" : "";
				this.setSortName(StringHelper.concat(". ",
						this.getCorporatePrimaryName(),
						this.getCorporateSubordinate1(),
						this.getCorporateSubordinate2())
						+ "." + number + qualifier);
                // make sure sortname has been set correctly, if not clear it
                if(this.getSortName().equals(".")) {
                   this.setSortName("");
                }

                if (this instanceof Names) {
					Names tempName = (Names) this;
					tempName.setSalutation(this.getCorporatePrimaryName());
				}

			} else if (this.getNameType().equalsIgnoreCase(Names.FAMILY_TYPE)) {
				this.setSortName(StringHelper.concat(", ",
						this.getFamilyName(),
						this.getFamilyNamePrefix(),
						this.getQualifier()));
				if (this instanceof Names) {
					Names tempName = (Names) this;
					tempName.setSalutation(this.getFamilyName());
				}
			}
		}
	}


	public String getSortName() {
		if (this.sortName != null) {
			return this.sortName;
		} else {
			return "";
		}
	}

	public void setSortName(String sortName) {
		Object oldValue = getSortName();
		this.sortName = sortName;
    firePropertyChange(PROPERTYNAME_SORT_NAME, oldValue, this.sortName);
	}

	public String getPersonalTitle() {
		if (this.personalTitle != null) {
			return this.personalTitle;
		} else {
			return "";
		}
	}

	public void setPersonalTitle(String personalTitle) {
		this.personalTitle = personalTitle;
  }

	public Boolean getPersonalDirectOrder() {
		return personalDirectOrder;
	}

	public void setPersonalDirectOrder(Boolean personalDirectOrder) {
		this.personalDirectOrder = personalDirectOrder;
	}

	public Boolean getCreateSortNameAutomatically() {
		return createSortNameAutomatically;
	}

	public void setCreateSortNameAutomatically(Boolean createSortNameAutomatically) {
		this.createSortNameAutomatically = createSortNameAutomatically;
	}
}
