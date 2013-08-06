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
 * Created by JFormDesigner on Sat Oct 21 13:35:32 EDT 2006
 */

package org.archiviststoolkit.swing;

import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

public class ExportOptionsMETS extends JPanel   implements ImportOptions{

	public static final String MODS_METADATA = "mods";
	public static final String DC_METADATA = "dc";

	public ExportOptionsMETS() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		modsMetadata = new JRadioButton();
		dublinCoreMetadata = new JRadioButton();
		suppressInternalOnly = new JCheckBox();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBorder(Borders.DLU2_BORDER);
		setLayout(new FormLayout(
			ColumnSpec.decodeSpecs("default"),
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW)
			}));

		//---- modsMetadata ----
		modsMetadata.setText("With MODS descriptive metadata");
		add(modsMetadata, cc.xy(1, 1));

		//---- dublinCoreMetadata ----
		dublinCoreMetadata.setText("With Dublin Core descriptive metadata");
		dublinCoreMetadata.setSelected(true);
		add(dublinCoreMetadata, cc.xy(1, 3));

		//---- suppressInternalOnly ----
		suppressInternalOnly.setText("Withhold data marked \"internal only\"");
		suppressInternalOnly.setVerticalAlignment(SwingConstants.TOP);
		add(suppressInternalOnly, cc.xy(1, 5));

		//---- buttonGroup1 ----
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(modsMetadata);
		buttonGroup1.add(dublinCoreMetadata);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JRadioButton modsMetadata;
	private JRadioButton dublinCoreMetadata;
	private JCheckBox suppressInternalOnly;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public String metadataTypeToInclude() {
		if (modsMetadata.isSelected()) {
			return MODS_METADATA;
		} else if (dublinCoreMetadata.isSelected()){
			return DC_METADATA;
		} else {
			return "";
		}
	}

	public boolean allDataEntered() {
		return true;
	}

	public boolean suppressInternalOnly() {
		return suppressInternalOnly.isSelected();
	}
}
