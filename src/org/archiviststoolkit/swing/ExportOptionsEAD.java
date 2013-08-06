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
 * Created by JFormDesigner on Sat Oct 21 13:20:25 EDT 2006
 */

package org.archiviststoolkit.swing;

import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

public class ExportOptionsEAD extends JPanel implements ImportOptions {
	public ExportOptionsEAD() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        numberedComponents = new JCheckBox();
        suppressInternalOnly = new JCheckBox();
        includeDaos = new JCheckBox();
        useDOIDAsHREF = new JCheckBox();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBorder(Borders.DLU2_BORDER);
        setLayout(new FormLayout(
            ColumnSpec.decodeSpecs("default"),
            new RowSpec[] {
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC
            }));

        //---- numberedComponents ----
        numberedComponents.setText("Number component levels?");
        add(numberedComponents, cc.xy(1, 1));

        //---- suppressInternalOnly ----
        suppressInternalOnly.setText("<html>Suppress components and notes <br>when marked \"internal only\"? </html>");
        suppressInternalOnly.setVerticalAlignment(SwingConstants.TOP);
        add(suppressInternalOnly, cc.xy(1, 3));

        //---- includeDaos ----
        includeDaos.setText(" Include DAO's in output?");
        add(includeDaos, cc.xy(1, 5));

        //---- useDOIDAsHREF ----
        useDOIDAsHREF.setText("Use Digital Object ID as HREF?");
        add(useDOIDAsHREF, cc.xy(1, 7));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JCheckBox numberedComponents;
    private JCheckBox suppressInternalOnly;
    private JCheckBox includeDaos;
    private JCheckBox useDOIDAsHREF;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public boolean allDataEntered() {
		return true;
	}

	public boolean useNumberedComponents() {
		return numberedComponents.isSelected();
	}

	public boolean suppressInternalOnly() {
		return suppressInternalOnly.isSelected();
	}

	public boolean includeDaos() {
		return includeDaos.isSelected();
	}

    public boolean useDigitalObjectIDAsHREF() {
        return useDOIDAsHREF.isSelected();
    }
}
