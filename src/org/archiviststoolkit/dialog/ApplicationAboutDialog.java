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
 * Created by JFormDesigner on Tue Aug 08 10:54:26 EDT 2006
 */

package org.archiviststoolkit.dialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.centerkey.utils.BareBonesBrowserLaunch;
import org.archiviststoolkit.ApplicationFrame;

public class ApplicationAboutDialog extends JFrame {
	
	public ApplicationAboutDialog() {
		initComponents();
		setIconImage(new ImageIcon(this.getClass().getResource("/org/archiviststoolkit/resources/images/launchIcon16x16.gif")).getImage());
	}

	private void viewLicesnseAgreementMouseClicked(MouseEvent e) {
		BareBonesBrowserLaunch.openURL(ApplicationFrame.URL_LICENSE_AGREEMENT);
	}

	private void closeButtonActionPerformed(ActionEvent e) {
		setVisible(false);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        viewLicesnseAgreement = new JLabel();
        label2 = new JLabel();
        scrollPane1 = new JScrollPane();
        textArea1 = new JTextArea();
        buttonBar = new JPanel();
        closeButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(Borders.DIALOG_BORDER);
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new FormLayout(
                    ColumnSpec.decodeSpecs("default"),
                    new RowSpec[] {
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC
                    }));

                //---- label1 ----
                label1.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/ATLogo.jpg")));
                contentPanel.add(label1, cc.xy(1, 1));

                //---- viewLicesnseAgreement ----
                viewLicesnseAgreement.setText("<html><u>View license agreement</u></html>");
                viewLicesnseAgreement.setFont(new Font("Trebuchet MS", Font.PLAIN, 10));
                viewLicesnseAgreement.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        viewLicesnseAgreementMouseClicked(e);
                    }
                });
                contentPanel.add(viewLicesnseAgreement, cc.xywh(1, 3, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

                //---- label2 ----
                label2.setText("Application Environment");
                label2.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
                contentPanel.add(label2, cc.xywh(1, 5, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

                //======== scrollPane1 ========
                {
                    scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                    //---- textArea1 ----
                    textArea1.setRows(7);
                    textArea1.setColumns(30);
                    textArea1.setEditable(false);
                    textArea1.setFocusable(false);
                    textArea1.setDragEnabled(false);
                    textArea1.setText(ApplicationFrame.gatherSystemInformation());
                    scrollPane1.setViewportView(textArea1);
                }
                contentPanel.add(scrollPane1, cc.xywh(1, 7, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
                buttonBar.setLayout(new FormLayout(
                    "max(pref;39dlu):grow",
                    "pref"));

                //---- closeButton ----
                closeButton.setText("Close");
                closeButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        closeButtonActionPerformed(e);
                    }
                });
                buttonBar.add(closeButton, cc.xywh(1, 1, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
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
    private JLabel viewLicesnseAgreement;
    private JLabel label2;
    private JScrollPane scrollPane1;
    private JTextArea textArea1;
    private JPanel buttonBar;
    private JButton closeButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public final void showDialog() {

		this.pack();
		setLocationRelativeTo(null);
		this.getRootPane().setDefaultButton(closeButton);
		this.setVisible(true);
	}

}
