package org.archiviststoolkit.exporter;

import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.structure.MARCXML.RecordType;

/**
 * Archivists' Toolkit(TM) Copyright � 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
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
 * 
 *
 * @author: Nathan Stevens
 * Date: Oct 28, 2009
 * Time: 1:53:22 PM
 */
public interface ExportHelper {

    /**
     * Method to allow plugins to add additional information to a marcxml file
     *
     * @param resource
     * @param record
     * @param progressPanel
     * @param internal
     */
    public void addInformationToMarcXML(Resources resource, RecordType record,
                                      InfiniteProgressPanel progressPanel,
                                      boolean internal);


    /**
     * Method to set any options when outputing used when adding information to the
     * MARCXML file.  Those will be stored in a hashmap
     *
     * @param option
     */
    public void setOuputOption(String option, String value);

}
