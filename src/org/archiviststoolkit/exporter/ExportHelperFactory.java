package org.archiviststoolkit.exporter;

/**
 * Archivists' Toolkit(TM) Copyright © 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
 * All rights reserved.
 * <p/>
 * This software is free. You can redistribute it and / or modify it under the terms of the Educational Community License (ECL)
 * version 1.0 (http://www.opensource.org/licenses/ecl1.php)
 * <p/>
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the ECL license for more details about permissions and limitations.
 * <p/>
 * <p/>
 * Archivists' Toolkit(TM)
 * http://www.archiviststoolkit.org
 * info@archiviststoolkit.org
 * <p/>
 * Created by IntelliJ IDEA.
 *
 * This is a class that defines a factory for getting export helpers which are loaded
 * by plugins.
 *
 * @author: Nathan Stevens
 * Date: Oct 28, 2009
 * Time: 1:33:08 PM
 */
public class ExportHelperFactory {

    /**
	 * The singleton reference.
	 */
	private static ExportHelperFactory singleton = null;

    // This stores a MARC XML helper
    private ExportHelper marcXMLHelper = null;


	/**
	 * Default Constructor.
	 */
	private ExportHelperFactory() {

	}

	/**
	 * Singleton access method.
	 *
	 * @return the instance of this singleton
	 */
	public static ExportHelperFactory getInstance() {
		if (singleton == null) {
			singleton = new ExportHelperFactory();
		}
		return (singleton);
	}

    /**
     * Method to return xml helper
     *
     * @return Marc XML helper
     */
    public ExportHelper getMarcXMLHelper() {
        return marcXMLHelper;
    }

    /**
     * Method to set the Marc XML helper
     *
     * @param marcXMLHelper
     */
    public void setMarcXMLHelper(ExportHelper marcXMLHelper) {
        this.marcXMLHelper = marcXMLHelper;
    }
}
