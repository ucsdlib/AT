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
 * Created by JFormDesigner on Mon Mar 06 09:18:43 PST 2006
 */

package org.archiviststoolkit.dialog;

import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.centerkey.utils.BareBonesBrowserLaunch;
import org.archiviststoolkit.ApplicationFrame;

public class SplashScreen extends JFrame {
	public SplashScreen(String initialMessageText) {
		initComponents();
//		version.setText("version " + ResourceBundle.getBundle("org.archiviststoolkit.resources.messages.messages").
//				getString("archiviststoolkit.releasenumber"));
		version.setText("version " + ApplicationFrame.getInstance().getAtVersionNumber());
		setMessageText(initialMessageText);
		setIconImage(new ImageIcon(this.getClass().getResource("/org/archiviststoolkit/resources/images/launchIcon16x16.gif")).getImage());
	}

	private void viewLicesnseAgreementMouseClicked(MouseEvent e) {
		BareBonesBrowserLaunch.openURL(ApplicationFrame.URL_LICENSE_AGREEMENT);
	}

	public JLabel getMessage() {
		return message;
	}

	private void thisKeyPressed(KeyEvent e) {
		if (e.isControlDown()) {
			System.out.println("controll down");
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		panel1 = new JPanel();
		label1 = new JLabel();
		version = new JLabel();
		viewLicesnseAgreement = new JLabel();
		message = new JLabel();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBackground(Color.white);
		setForeground(Color.white);
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				thisKeyPressed(e);
			}
		});
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout(
			"default:grow",
			"fill:default:grow"));

		//======== panel1 ========
		{
			panel1.setBorder(new LineBorder(Color.black, 3));
			panel1.setBackground(Color.white);
			panel1.setLayout(new FormLayout(
				ColumnSpec.decodeSpecs("default:grow"),
				new RowSpec[] {
					new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.UNRELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.UNRELATED_GAP_ROWSPEC
				}));

			//---- label1 ----
			label1.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/ATLogo.jpg")));
			label1.setBorder(null);
			panel1.add(label1, cc.xy(1, 1));

			//---- version ----
			version.setText("version");
			version.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			panel1.add(version, cc.xywh(1, 2, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

			//---- viewLicesnseAgreement ----
			viewLicesnseAgreement.setText("<html><u>View license agreement</u></html>");
			viewLicesnseAgreement.setFont(new Font("Trebuchet MS", Font.PLAIN, 10));
			viewLicesnseAgreement.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					viewLicesnseAgreementMouseClicked(e);
				}
			});
			panel1.add(viewLicesnseAgreement, cc.xywh(1, 4, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
			panel1.add(message, cc.xywh(1, 6, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
		}
		contentPane.add(panel1, cc.xy(1, 1));
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel panel1;
	private JLabel label1;
	private JLabel version;
	private JLabel viewLicesnseAgreement;
	private JLabel message;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	/**
	 * Show the splash screen to the end user.
	 * <p/>
	 * <P>Once this method returns, the splash screen is realized, which means
	 * that almost all work on the splash screen should proceed through the event
	 * dispatch thread. In particular, any call to <code>dispose</code> for the
	 * splash screen must be performed in the event dispatch thread.
	 */
	public void splash() {
		this.pack();
		center();
		setVisible(true);
	}


	// PRIVATE//
	private MediaTracker fMediaTracker;
	private Image fImage;


	/**
	 * Centers the frame on the screen.
	 */
	private void center() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle frame = getBounds();
		setLocation((screen.width - frame.width) / 2, (screen.height - frame.height) / 2);
	}

	public void setMessageText(String messageText) {
		if (getMessage().getText().length() > 0) {
			ApplicationFrame.getInstance().addLineToStartupLog(getMessage().getText());
		}

//		System.out.println("\n" + messageText + "\n");
		this.getMessage().setText(messageText + "...");
	}
}
