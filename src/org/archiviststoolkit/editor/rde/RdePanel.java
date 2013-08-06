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
 * Date: Jul 18, 2008
 * Time: 2:07:39 PM
 */

package org.archiviststoolkit.editor.rde;

import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.model.ResourcesComponents;
import org.archiviststoolkit.model.RDEScreenPanelItems;
import org.archiviststoolkit.exceptions.RDEPopulateException;
import org.archiviststoolkit.exceptions.RDEPanelCreationException;
import org.archiviststoolkit.structure.DefaultValues;
import org.archiviststoolkit.ApplicationFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public abstract class RdePanel extends JPanel {

	protected RdePanelContainer parentPanel;
	protected ATFieldInfo fieldInfo;
    protected boolean showDefaultValues = true; // used to show the default values the first time dialog is opened

    public RdePanel(RdePanelContainer parentPanel) {
		this.parentPanel = parentPanel;
	}

	public RdePanel(RdePanelContainer parentPanel, ATFieldInfo fieldInfo) {
		this.parentPanel = parentPanel;
		this.fieldInfo = fieldInfo;
	}

	protected void setLabelColor(boolean sticky, JLabel label) {
		if (sticky) {
			label.setForeground(Color.RED);
		} else {
			label.setForeground(Color.BLACK);
		}
	}

	public abstract void populateComponent(ResourcesComponents component) throws RDEPopulateException;

	public abstract void clearFields();

	public abstract void setStickyLabels();

	public abstract void initializeStickyLabels(Collection<RDEScreenPanelItems> panelItems) throws RDEPanelCreationException;

    public void setDefaultValues() {} // needed since some of the RDEScreen don't have a default values

    // get the default value
    protected DefaultValues getDefaultValue() {
        return DefaultValues.getDefaultValue(ApplicationFrame.getInstance().getCurrentUserRepository(),
                fieldInfo.getTableName(), fieldInfo.getFieldName());
    }

    protected DefaultValues getDefaultValue(String fieldName) {
        return DefaultValues.getDefaultValue(ApplicationFrame.getInstance().getCurrentUserRepository(),
                fieldInfo.getTableName(), fieldName);
    }

    protected DefaultValues getDefaultValue(String tableName, String fieldName) {
       return DefaultValues.getDefaultValue(ApplicationFrame.getInstance().getCurrentUserRepository(),
                tableName, fieldName);
    }

    protected DefaultValues getDefaultValue(ATFieldInfo fieldInfo) {
        return DefaultValues.getDefaultValue(ApplicationFrame.getInstance().getCurrentUserRepository(),
                fieldInfo.getTableName(), fieldInfo.getFieldName());
    }
}
