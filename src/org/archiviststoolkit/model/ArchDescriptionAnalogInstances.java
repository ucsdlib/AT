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

import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.StringLengthValidationRequried;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.util.StringHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.text.NumberFormat;

public class ArchDescriptionAnalogInstances extends ArchDescriptionInstances implements Serializable, Comparable {


    public static final String PROPERTYNAME_CONTAINER1_NUMERIC_INDICATOR = "container1NumericIndicator";
    public static final String PROPERTYNAME_CONTAINER1_ALPHA_NUMERIC_INDICATOR = "container1AlphaNumericIndicator";
    public static final String PROPERTYNAME_CONTAINER1_TYPE = "container1Type";
    public static final String PROPERTYNAME_CONTAINER2_NUMERIC_INDICATOR = "container2NumericIndicator";
    public static final String PROPERTYNAME_CONTAINER2_ALPHA_NUMERIC_INDICATOR = "container2AlphaNumericIndicator";
    public static final String PROPERTYNAME_BARCODE = "barcode";
    public static final String PROPERTYNAME_CONTAINER2_TYPE = "container2Type";
    public static final String PROPERTYNAME_CONTAINER3_NUMERIC_INDICATOR = "container3NumericIndicator";
    public static final String PROPERTYNAME_CONTAINER3_ALPHA_NUMERIC_INDICATOR = "container3AlphaNumericIndicator";
    public static final String PROPERTYNAME_CONTAINER3_TYPE = "container3Type";
    public static final String PROPERTYNAME_CONTAINER_LABEL = "containerLabel";
	public static final String PROPERTYNAME_LOCATION_LABEL = "locationLabel";
	public static final String PROPERTYNAME_LOCATION = "location";
    public static final String PROPERTYNAME_CONTAINER1_INDICATOR = "container1Indicator";
    public static final String PROPERTYNAME_CONTAINER2_INDICATOR = "container2Indicator";
    public static final String PROPERTYNAME_CONTAINER3_INDICATOR = "container3Indicator";
    public static final String PROPERTYNAME_USER_DEFINED_BOOLEAN1 = "userDefinedBoolean1";
    public static final String PROPERTYNAME_USER_DEFINED_BOOLEAN2 = "userDefinedBoolean2";
    public static final String PROPERTYNAME_USER_DEFINED_STRING1 = "userDefinedString1";
    public static final String PROPERTYNAME_USER_DEFINED_STRING2 = "userDefinedString2";

    // Variable to match an alpha-numeric indicator which could be converted
    // into a number i.e. 1d, 1.03D, or 1.0f etc ...
    private static final String ALPHA_NUMERIC_REGEXP = "\\d+\\.*\\d*\\D+";

    @StringLengthValidationRequried
    @IncludeInApplicationConfiguration
    private String container1Indicator = "";

    //@IncludeInApplicationConfiguration
    @ExcludeFromDefaultValues
    private Double container1NumericIndicator;

    //@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	@ExcludeFromDefaultValues
    private String container1AlphaNumericIndicator = "";

    @IncludeInApplicationConfiguration
	@StringLengthValidationRequried(20)
	private String container1Type = "";

    @StringLengthValidationRequried
    @IncludeInApplicationConfiguration
    private String container2Indicator = "";

    //@IncludeInApplicationConfiguration
    @ExcludeFromDefaultValues
    private Double container2NumericIndicator;

    //@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
    @ExcludeFromDefaultValues
    private String container2AlphaNumericIndicator = "";

    @IncludeInApplicationConfiguration
	@StringLengthValidationRequried(20)
    private String container2Type = "";

    @StringLengthValidationRequried
    @IncludeInApplicationConfiguration
    private String container3Indicator = "";

    //@IncludeInApplicationConfiguration
    @ExcludeFromDefaultValues
    private Double container3NumericIndicator;

    //@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
    @ExcludeFromDefaultValues
    private String container3AlphaNumericIndicator = "";

    @IncludeInApplicationConfiguration
	@StringLengthValidationRequried(20)
    private String container3Type = "";

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(20)
    private String containerLabel = "";

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(80)
    private String barcode = "";

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    private Locations location;

    @IncludeInApplicationConfiguration
    @StringLengthValidationRequried
    private String userDefinedString1 = "";

    @IncludeInApplicationConfiguration
    @StringLengthValidationRequried
    private String userDefinedString2 = "";

    @IncludeInApplicationConfiguration
    private Boolean userDefinedBoolean1;

    @IncludeInApplicationConfiguration
    private Boolean userDefinedBoolean2;

    /**
     * No-arg constructor for JavaBean tools.
     */
    public ArchDescriptionAnalogInstances() {
    }


    public ArchDescriptionAnalogInstances(ArchDescription archDescription) {
        super(archDescription);

    }

    public String getInstanceLabel() {
        if (containerLabel != null && containerLabel.length() > 0) {
            return containerLabel;
        } else {
            ArrayList<String> temp = new ArrayList<String>();
            if (container1Type != null && container1Type.length() > 0) {
                temp.add(container1Type + " " + constructIndicator(container1NumericIndicator, container1AlphaNumericIndicator));
            }
            if (container2Type != null && container2Type.length() > 0) {
                temp.add(container2Type + " " + constructIndicator(container2NumericIndicator, container2AlphaNumericIndicator));
            }
            if (container3Type != null && container3Type.length() > 0) {
                temp.add(container3Type + " " + constructIndicator(container3NumericIndicator, container3AlphaNumericIndicator));
            }
            return StringHelper.concat(temp, " / ");
        }
    }

    private String constructIndicator(Double numericIndicator, String alphaIndicator) {
        if (alphaIndicator == null || alphaIndicator.length() == 0) {
            if (numericIndicator == null) {
                return "";
            } else {
                return NumberFormat.getInstance().format(numericIndicator);
            }
        } else {
            return alphaIndicator;
        }
    }

	public String getTopLevelLabel() {
		if (container1Type != null && container1Type.length() > 0 && (container1NumericIndicator != null || container1AlphaNumericIndicator.length() > 0)) {
			try {
				if (container1NumericIndicator != null) {
					return container1Type + " " + NumberFormat.getInstance().format(container1NumericIndicator);
				} else {
					return container1Type + " " + container1AlphaNumericIndicator;
				}
			} catch (Exception e) {
				new ErrorDialog("Error creating container label", e).showDialog();
				return "no container";
			}
		} else {
			return "no container";
		}

	}

	public String getTopLevelLabelForSorting() {
		if (container1Type != null && container1Type.length() > 0 && container1NumericIndicator != null) {
			try {
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMinimumIntegerDigits(5);
				nf.setGroupingUsed(false);
				return container1Type + " " + nf.format(container1NumericIndicator);
			} catch (Exception e) {
				new ErrorDialog("Error creating container label", e).showDialog();
				return "no container";
			}
		} else {
			return "no container";
		}

	}

    public void setContainer1NumericIndicator(Double container1NumericIndicator) {
        this.container1NumericIndicator = container1NumericIndicator;
    }

	/**
	 * Compares this object to another.
	 *
	 * @param object the object to compare this to.
	 * @return a integer result of the comparison.
	 */
	public int compareTo(Object object) {
		if (object instanceof ArchDescriptionAnalogInstances) {
			ArchDescriptionAnalogInstances compareInstance = (ArchDescriptionAnalogInstances)object;
			if (this.getContainer1NumericIndicator() == null || compareInstance.getContainer1NumericIndicator() == null) {
				return super.compareTo(object);
			} else {
				return this.getContainer1NumericIndicator().compareTo(compareInstance.getContainer1NumericIndicator());
			}
		} else {
			return super.compareTo(object);
		}
	}

	public Double getContainer1NumericIndicator() {
		return container1NumericIndicator;
	}

    public void setContainer1Type(String container1Type) {
        this.container1Type = container1Type;
    }

    public String getContainer1Type() {
		if (this.container1Type != null) {
			return this.container1Type;
		} else {
			return "";
		}
	}

    public String getContainerLabel() {
		if (this.containerLabel != null) {
			return this.containerLabel;
		} else {
			return "";
		}
	}

    public void setContainerLabel(String containerLabel) {
        this.containerLabel = containerLabel;
    }

    public Double getContainer2NumericIndicator() {
        return container2NumericIndicator;
    }

    public void setContainer2NumericIndicator(Double container2NumericIndicator) {
        this.container2NumericIndicator = container2NumericIndicator;
    }

    public String getContainer2Type() {
		if (this.container2Type != null) {
			return this.container2Type;
		} else {
			return "";
		}
	}

    public void setContainer2Type(String container2Type) {
        this.container2Type = container2Type;
    }

    public Double getContainer3NumericIndicator() {
        return container3NumericIndicator;
    }

    public void setContainer3NumericIndicator(Double container3NumericIndicator) {
        this.container3NumericIndicator = container3NumericIndicator;
    }

    public String getContainer3Type() {
		if (this.container3Type != null) {
			return this.container3Type;
		} else {
			return "";
		}
	}

    public void setContainer3Type(String container3Type) {
        this.container3Type = container3Type;
    }

    public Locations getLocation() {
        return location;
    }

    // Method need to be sychronized to handel multiple calls from threads
    public synchronized void setLocation(Locations location) {
		Object oldValue = getLocation();
		String oldLocationLabel = getLocationLabel();
		this.location = location;
		firePropertyChange(PROPERTYNAME_LOCATION, oldValue, this.location);
		//now fire a property change for the location label
		firePropertyChange(PROPERTYNAME_LOCATION_LABEL, oldLocationLabel, this.getLocationLabel());
	}

    public String getLocationLabel() {
        if (location == null) {
            return "No location assigned";
        } else {
            return location.toString();
        }
    }

    public String getBarcode() {
		if (this.barcode != null) {
			return this.barcode;
		} else {
			return "";
		}
	}

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * Method to get the indicator for container 1
     * @return Either the Numeric or AlphaNumeric Indicator
     */
    public String getContainer1Indicator() {
        if(this.container1NumericIndicator != null && this.container1NumericIndicator != 0) {
            return StringHelper.handleDecimal(this.container1NumericIndicator.toString());
        } else if (this.container1AlphaNumericIndicator != null && StringHelper.isNotEmpty(this.container1AlphaNumericIndicator)) {
			return this.container1AlphaNumericIndicator;
		} else {
			return "";
		}
    }

    /**
     * Set the indicator for container 1
     * @param indicator This is either the Numeric or AlphaNumeric Indicator
     */
    public void setContainer1Indicator(String indicator) {
        // try to cast it to double if number format exception then set alphanumeric indicator
        try {
            // check to see if it is not a number with D on the end, which is a valid
            // double value, but the user may actually want a D at the end of a number
            if(indicator.matches(ALPHA_NUMERIC_REGEXP)) {
                this.container1AlphaNumericIndicator = indicator;
                this.container1NumericIndicator = null;
            } else {
                this.container1NumericIndicator = new Double(indicator);
                this.container1AlphaNumericIndicator = null;
            }
        } catch(NumberFormatException nfe) {
            this.container1AlphaNumericIndicator = indicator;
            this.container1NumericIndicator = null;
        }
    }

    public String getContainer1AlphaNumericIndicator() {
		if (this.container1AlphaNumericIndicator != null) {
			return this.container1AlphaNumericIndicator;
		} else {
			return "";
		}
	}

    public void setContainer1AlphaNumericIndicator(String container1AlphaNumericIndicator) {
        this.container1AlphaNumericIndicator = container1AlphaNumericIndicator;
    }

    /**
     * Method to get the indicator for container 2
     * @return Either the Numeric or AlphaNumeric Indicator
     */
    public String getContainer2Indicator() {
        if(this.container2NumericIndicator != null && this.container2NumericIndicator != 0) {
            return StringHelper.handleDecimal(this.container2NumericIndicator.toString());
        } else if (this.container2AlphaNumericIndicator != null && StringHelper.isNotEmpty(this.container2AlphaNumericIndicator)) {
			return this.container2AlphaNumericIndicator;
		} else {
			return "";
		}
    }

    /**
     * Set the indicator for container 2
     * @param indicator This is either the Numeric or AlphaNumeric Indicator
     */
    public void setContainer2Indicator(String indicator) {
        // try to cast it to double if number format exception then set alphanumeric indicator
        try {
            // check to see if it is not a number with D on the end, which is a valid
            // double value, but the user may actually want a D at the end of a number
            if(indicator.matches(ALPHA_NUMERIC_REGEXP)) {
                this.container2AlphaNumericIndicator = indicator;
                this.container2NumericIndicator = null;
            } else {
                this.container2NumericIndicator = new Double(indicator);
                this.container2AlphaNumericIndicator = null;
            }
        } catch(NumberFormatException nfe) {
            this.container2AlphaNumericIndicator = indicator;
            this.container2NumericIndicator = null;
        }
    }

    public String getContainer2AlphaNumericIndicator() {
		if (this.container2AlphaNumericIndicator != null) {
			return this.container2AlphaNumericIndicator;
		} else {
			return "";
		}
	}

    public void setContainer2AlphaNumericIndicator(String container2AlphaNumericIndicator) {
        this.container2AlphaNumericIndicator = container2AlphaNumericIndicator;
    }

    /**
     * Method to get the indicator for container 3
     * @return Either the Numeric or AlphaNumeric Indicator
     */
    public String getContainer3Indicator() {
        if(this.container3NumericIndicator != null && this.container3NumericIndicator != 0) {
            return StringHelper.handleDecimal(this.container3NumericIndicator.toString());
        } else if (this.container3AlphaNumericIndicator != null && StringHelper.isNotEmpty(this.container3AlphaNumericIndicator)) {
			return this.container3AlphaNumericIndicator;
		} else {
			return "";
		}
    }

    /**
     * Set the indicator for container 3
     * @param indicator This is either the Numeric or AlphaNumeric Indicator
     */
    public void setContainer3Indicator(String indicator) {
        // try to cast it to double if number format exception then set alphanumeric indicator
        try {
            // check to see if it is not a number with D on the end, which is a valid
            // double value, but the user may actually want a D at the end of a number
            if(indicator.matches(ALPHA_NUMERIC_REGEXP)) {
                this.container3AlphaNumericIndicator = indicator;
                this.container3NumericIndicator = null;
            } else {
                this.container3NumericIndicator = new Double(indicator);
                this.container3AlphaNumericIndicator = null;
            }
        } catch(NumberFormatException nfe) {
            this.container3AlphaNumericIndicator = indicator;
            this.container3NumericIndicator = null;
        }
    }

    public String getContainer3AlphaNumericIndicator() {
		if (this.container3AlphaNumericIndicator != null) {
			return this.container3AlphaNumericIndicator;
		} else {
			return "";
		}
	}

    public void setContainer3AlphaNumericIndicator(String container3AlphaNumericIndicator) {
        this.container3AlphaNumericIndicator = container3AlphaNumericIndicator;
    }

    public String getUserDefinedString1() {
        return userDefinedString1;
    }

    public void setUserDefinedString1(String userDefinedString1) {
        this.userDefinedString1 = userDefinedString1;
    }

    public String getUserDefinedString2() {
        return userDefinedString2;
    }

    public void setUserDefinedString2(String userDefinedString2) {
        this.userDefinedString2 = userDefinedString2;
    }

    public Boolean getUserDefinedBoolean2() {
        return userDefinedBoolean2;
    }

    public void setUserDefinedBoolean2(Boolean userDefinedBoolean2) {
        this.userDefinedBoolean2 = userDefinedBoolean2;
    }

    public Boolean getUserDefinedBoolean1() {
        return userDefinedBoolean1;
    }

    public void setUserDefinedBoolean1(Boolean userDefinedBoolean1) {
        this.userDefinedBoolean1 = userDefinedBoolean1;
    }
}
