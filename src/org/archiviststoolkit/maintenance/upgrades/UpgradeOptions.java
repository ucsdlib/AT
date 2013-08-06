/*
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
 * Created by JFormDesigner on Thu Dec 13 17:25:22 EST 2007
 */

package org.archiviststoolkit.maintenance.upgrades;

import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.netbeans.spi.wizard.WizardPage;

public class UpgradeOptions extends WizardPage {
	public UpgradeOptions() {
		super("upgradeOptions", "Upgrade Options");
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		message2 = new JLabel();
		preserveFieldLabels = new JCheckBox();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
			},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC
			}));

		//---- message2 ----
		message2.setText("<html><b>Note: </b>Selecting this option means you will not get the updated labels for any existing fields. It will however preserve your existing field label customizations.</html>");
		add(message2, cc.xy(1, 3));

		//---- preserveFieldLabels ----
		preserveFieldLabels.setText("Preserve existing field labels");
		preserveFieldLabels.setName("preserveExistingLabels");
		add(preserveFieldLabels, cc.xy(1, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel message2;
	private JCheckBox preserveFieldLabels;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public boolean getPreserveFieldLabels() {
		return preserveFieldLabels.isSelected();
	}
}
