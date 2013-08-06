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
 * Date: May 16, 2006
 * Time: 4:02:25 PM
 */

package org.archiviststoolkit.util;

import com.jgoodies.validation.util.PropertyValidationSupport;
import com.jgoodies.validation.Severity;
import com.jgoodies.validation.message.SimpleValidationMessage;

public class ATPropertyValidationSupport extends PropertyValidationSupport {
    public ATPropertyValidationSupport(Object o, String s) {
        super(o, s);
    }

    public void addSimple(String text, Severity severity) {
        getResult().add(new SimpleValidationMessage(text, severity));
    }

    public void addSimpleError(String text) {
        getResult().add(new SimpleValidationMessage(text, Severity.ERROR));
    }

    public void addSimpleWarning(String text) {
        getResult().add(new SimpleValidationMessage(text, Severity.WARNING));
    }

}
