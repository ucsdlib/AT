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
 * Created by JFormDesigner on Tue Aug 08 14:16:13 EDT 2006
 */

package org.archiviststoolkit.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.centerkey.utils.BareBonesBrowserLaunch;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.model.Constants;

public class VersionMismatch extends JDialog {

	public VersionMismatch(String messageString, int clientVersionTooHigh) {
		super();
		initComponents();
		message.setText(messageString);
		if (clientVersionTooHigh == Constants.VERSION_GREATER) {
			downLoadNewVersion.setVisible(false);
		} else  if (clientVersionTooHigh == Constants.VERSION_LESS){
			contactAdmin.setVisible(false);
		} else {
			downLoadNewVersion.setVisible(false);
			contactAdmin.setVisible(false);
		}
	}

	private void downLoadNewVersionMouseClicked(MouseEvent e) {
		BareBonesBrowserLaunch.openURL(ApplicationFrame.URL_DOWNLOAD);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		panel1 = new JPanel();
		message = new JLabel();
		downLoadNewVersion = new JLabel();
		contactAdmin = new JLabel();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBackground(new Color(200, 205, 232));
		setForeground(Color.white);
		setModal(true);
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout(
			"default:grow",
			"fill:default:grow"));

		//======== panel1 ========
		{
			panel1.setBorder(Borders.DLU4_BORDER);
			panel1.setBackground(new Color(200, 205, 232));
			panel1.setLayout(new FormLayout(
				ColumnSpec.decodeSpecs("default:grow"),
				new RowSpec[] {
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.UNRELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC
				}));

			//---- message ----
			message.setText("sdfsf");
			message.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			panel1.add(message, cc.xy(1, 1));

			//---- downLoadNewVersion ----
			downLoadNewVersion.setText("<html><u>Download Latest Version</u></html>");
			downLoadNewVersion.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			downLoadNewVersion.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					downLoadNewVersionMouseClicked(e);
				}
			});
			panel1.add(downLoadNewVersion, cc.xywh(1, 3, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

			//---- contactAdmin ----
			contactAdmin.setText("Please contact your AT administrator to upgrade the database.");
			panel1.add(contactAdmin, cc.xywh(1, 5, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
		}
		contentPane.add(panel1, cc.xy(1, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel panel1;
	private JLabel message;
	private JLabel downLoadNewVersion;
	private JLabel contactAdmin;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public final void showDialog() {

		//send to logger
		this.pack();
		setLocationRelativeTo(null);
		this.setVisible(true);

	}

}
