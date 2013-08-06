package org.archiviststoolkit.report;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.archiviststoolkit.util.StringHelper;

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
 * User: Nathan Stevens
 * Date: Aug 25, 2008
 * Time: 3:00:40 PM
 * To change this template use File | Settings | File Templates.
 */

public class ATBeanCollectionDataSource extends JRBeanCollectionDataSource {
  public ATBeanCollectionDataSource(java.util.Collection collection) {
    super(collection);
  }

  public ATBeanCollectionDataSource(java.util.Collection collection, boolean b) {
    super(collection, b);
  }

  // override the super getFieldValue method to allow any tags to be stripped from any returned strings
  public java.lang.Object getFieldValue(net.sf.jasperreports.engine.JRField jrField) throws net.sf.jasperreports.engine.JRException {
    Object ro = super.getFieldValue(jrField);

    if (ro != null && ro instanceof String) {
      String s = (String) ro;
      return StringHelper.tagRemover(s);
    } else {
      return ro;
    }
  }
}
