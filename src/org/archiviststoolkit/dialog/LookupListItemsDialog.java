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
 * Created by JFormDesigner on Fri Mar 09 15:35:38 EST 2007
 */

package org.archiviststoolkit.dialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.swing.ATBasicDialog;
import org.apache.axis.types.NMToken;

public class LookupListItemsDialog extends ATBasicDialog {

    //todo move to editor package

    private boolean supportPairedValues;
	private boolean restrictToNmtoken;

	public LookupListItemsDialog(Frame owner, boolean supportPairedValues, boolean restrictToNmtoken) {
		super(owner);
		initComponents();
		init(supportPairedValues, restrictToNmtoken);
	}

	public LookupListItemsDialog(Dialog owner, boolean supportPairedValues, boolean restrictToNmtoken) {
		super(owner);
		initComponents();
		init(supportPairedValues, restrictToNmtoken);
	}

	private void init(boolean supportPairedValues, boolean restrictToNmtoken) {
		this.supportPairedValues = supportPairedValues;
		this.restrictToNmtoken = restrictToNmtoken;
		code.setVisible(supportPairedValues);
		codeLabel.setVisible(supportPairedValues);
	}

	private void okButtonActionPerformed(ActionEvent e) {
		if (getItemValue().length() == 0) {
			JOptionPane.showMessageDialog(this, "You must enter a value");
		} else if (restrictToNmtoken) {
			if (!NMToken.isValid(getItemValue())) {
				JOptionPane.showMessageDialog(this, "The value can only contain letters, digits, ., -, _, :");
			} else {
				performOkAction();
			}
		} else if (supportPairedValues) {
			if (getCode().length() == 0 || !NMToken.isValid(getCode())) {
				JOptionPane.showMessageDialog(this, "You must enter a code and it can only contain letters, digits, ., -, _, :");
			} else {
				performOkAction();
			}
		} else {
			performOkAction();
		}
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		performCancelAction();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        value = new JTextField();
        codeLabel = new JLabel();
        code = new JTextField();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setTitle("Lookup List Items");
        setModal(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(Borders.DIALOG_BORDER);
            dialogPane.setBackground(new Color(200, 205, 232));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setOpaque(false);
                contentPanel.setLayout(new FormLayout(
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

                //---- label1 ----
                label1.setText("Value");
                contentPanel.add(label1, cc.xy(1, 1));

                //---- value ----
                value.setPreferredSize(new Dimension(400, 22));
                contentPanel.add(value, cc.xy(3, 1));

                //---- codeLabel ----
                codeLabel.setText("Code");
                contentPanel.add(codeLabel, cc.xy(1, 3));
                contentPanel.add(code, cc.xy(3, 3));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
                buttonBar.setOpaque(false);
                buttonBar.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        FormFactory.GLUE_COLSPEC,
                        FormFactory.BUTTON_COLSPEC,
                        FormFactory.RELATED_GAP_COLSPEC,
                        FormFactory.BUTTON_COLSPEC
                    },
                    RowSpec.decodeSpecs("pref")));

                //---- okButton ----
                okButton.setText("OK");
                okButton.setOpaque(false);
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        okButtonActionPerformed(e);
                    }
                });
                buttonBar.add(okButton, cc.xy(2, 1));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.setOpaque(false);
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        cancelButtonActionPerformed(e);
                    }
                });
                buttonBar.add(cancelButton, cc.xy(4, 1));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JTextField value;
    private JLabel codeLabel;
    private JTextField code;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables


	public String getItemValue() {
		return value.getText();
	}

	public void setItemValue(String itemValue) {
		this.value.setText(itemValue);
	}

	public String getCode() {
		return code.getText();
	}

	public void setCode(String code) {
		this.code.setText(code);
	}
}
