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
 * Created by JFormDesigner on Fri Jun 23 11:39:07 EDT 2006
 */

package org.archiviststoolkit.structure;

import java.awt.*;
import java.util.Vector;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import org.archiviststoolkit.structure.NotesEtcTypes;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.mydomain.DomainEditor;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.util.SequencedObjectsUtils;
import org.archiviststoolkit.swing.ATBasicComponentFactory;

public class NotesEtcTypesFields extends DomainEditorFields {

	public NotesEtcTypesFields() {
		super();
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		label1 = new JLabel();
		fieldName = ATBasicComponentFactory.createTextField(detailsModel.getModel(NotesEtcTypes.PROPERTYNAME_REPEATING_DATA_NAME));
		label16 = new JLabel();
		fieldLabel = ATBasicComponentFactory.createTextField(detailsModel.getModel(NotesEtcTypes.PROPERTYNAME_REPEATING_DATA_LABEL));
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBorder(Borders.DLU4_BORDER);
		setOpaque(false);
		setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		setPreferredSize(new Dimension(600, 180));
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec("30px:grow")
			},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC
			}));

		//---- label1 ----
		label1.setText("Name");
		label1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(label1, cc.xy(1, 1));

		//---- fieldName ----
		fieldName.setEditable(false);
		fieldName.setOpaque(false);
		fieldName.setBorder(null);
		fieldName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(fieldName, cc.xy(3, 1));

		//---- label16 ----
		label16.setText("Label");
		label16.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(label16, cc.xy(1, 3));

		//---- fieldLabel ----
		fieldLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		add(fieldLabel, cc.xy(3, 3));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel label1;
	public JTextField fieldName;
	private JLabel label16;
	public JTextField fieldLabel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public Component getInitialFocusComponent() {
		return fieldLabel;
	}
}
