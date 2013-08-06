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
 * Created by JFormDesigner on Sat Aug 26 12:13:31 EDT 2006
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ValueHolder;
import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.mydomain.DomainEditorFields;

public class ConstantsFields extends DomainEditorFields {

	public ConstantsFields() {
		super();
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		label1 = new JLabel();
		dateFormat = ATBasicComponentFactory.createComboBox(detailsModel, Constants.PROPERTYNAME_DEFAULT_DATE_FORMAT, Constants.getDateFormatsList());
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBorder(Borders.DIALOG_BORDER);
		setPreferredSize(new Dimension(450, 57));
		setBackground(new Color(200, 205, 232));
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC
			},
			RowSpec.decodeSpecs("fill:default")));

		//---- label1 ----
		label1.setText("System date format");
		add(label1, cc.xy(1, 1));

		//---- dateFormat ----
		dateFormat.setOpaque(false);
		dateFormat.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(dateFormat, cc.xy(3, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel label1;
	public JComboBox dateFormat;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public Component getInitialFocusComponent() {
		return dateFormat;
	}
}
