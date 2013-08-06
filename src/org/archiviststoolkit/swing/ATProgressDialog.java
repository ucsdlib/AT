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
 * Created by JFormDesigner on Thu Oct 25 14:21:58 EDT 2007
 */

package org.archiviststoolkit.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class ATProgressDialog extends JDialog implements ChangeListener {

	InfiniteProgressPanel monitor;
	private boolean debug = false;
 
	public ATProgressDialog(Frame owner, InfiniteProgressPanel monitor) {
		super(owner);
		initComponents();
		init(monitor);
	}

	public ATProgressDialog(Dialog owner, InfiniteProgressPanel monitor) {
		super(owner);
		initComponents();
		init(monitor);
	}

	private void init(final InfiniteProgressPanel monitor) {
		this.monitor = monitor;

		line1.setText(monitor.getLine1());

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		monitor.addChangeListener(this);
		if (debug) {
			System.out.println("AT progress message init");
        }

        //start thread that that will close the window incase the change listener not working
        Thread performer = new Thread(new Runnable() {
            public void run() {
                boolean run = true;
                while (run) {
                    if (monitor.isProcessDone()) {
                        run = false;
                        setVisible(false);
                        dispose();
						if (debug) {
							System.out.println("Using thread to close Progress Dialog ...");
						}
					}
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ie) {
                    }
                }
            }
        });
        performer.start();
    }


	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        panel1 = new JPanel();
        line1 = new JLabel();
        line2 = new JLabel();
        line3 = new JLabel();
        line4 = new JLabel();
        line5 = new JLabel();
        line6 = new JLabel();
        line7 = new JLabel();
        panel2 = new JPanel();
        cancelButton = new JButton();
        action1 = new CancelAction();

        //======== this ========
        setModal(true);
        setUndecorated(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setBorder(new LineBorder(Color.black, 2));
            panel1.setPreferredSize(new Dimension(500, 300));
            panel1.setBackground(Color.white);
            panel1.setLayout(new GridLayout(8, 1));

            //---- line1 ----
            line1.setText("line 1");
            line1.setBorder(null);
            line1.setHorizontalAlignment(SwingConstants.CENTER);
            line1.setHorizontalTextPosition(SwingConstants.CENTER);
            panel1.add(line1);

            //---- line2 ----
            line2.setText(" ");
            line2.setBorder(null);
            line2.setHorizontalAlignment(SwingConstants.CENTER);
            line2.setHorizontalTextPosition(SwingConstants.CENTER);
            panel1.add(line2);

            //---- line3 ----
            line3.setText(" ");
            line3.setBorder(null);
            line3.setHorizontalAlignment(SwingConstants.CENTER);
            line3.setHorizontalTextPosition(SwingConstants.CENTER);
            panel1.add(line3);

            //---- line4 ----
            line4.setText(" ");
            line4.setBorder(null);
            line4.setHorizontalAlignment(SwingConstants.CENTER);
            line4.setHorizontalTextPosition(SwingConstants.CENTER);
            panel1.add(line4);

            //---- line5 ----
            line5.setText(" ");
            line5.setBorder(null);
            line5.setHorizontalAlignment(SwingConstants.CENTER);
            line5.setHorizontalTextPosition(SwingConstants.CENTER);
            panel1.add(line5);

            //---- line6 ----
            line6.setText(" ");
            line6.setBorder(null);
            line6.setHorizontalAlignment(SwingConstants.CENTER);
            line6.setHorizontalTextPosition(SwingConstants.CENTER);
            panel1.add(line6);

            //---- line7 ----
            line7.setText(" ");
            line7.setBorder(null);
            line7.setHorizontalAlignment(SwingConstants.CENTER);
            line7.setHorizontalTextPosition(SwingConstants.CENTER);
            panel1.add(line7);

            //======== panel2 ========
            {
                panel2.setBackground(Color.white);
                panel2.setLayout(new FlowLayout());

                //---- cancelButton ----
                cancelButton.setAction(action1);
                showCancelButton(false);
                panel2.add(cancelButton);
            }
            panel1.add(panel2);
        }
        contentPane.add(panel1, BorderLayout.CENTER);
        setSize(545, 285);
        setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel panel1;
    private JLabel line1;
    private JLabel line2;
    private JLabel line3;
    private JLabel line4;
    private JLabel line5;
    private JLabel line6;
    private JLabel line7;
    private JPanel panel2;
    private JButton cancelButton;
    private CancelAction action1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public void stateChanged(final ChangeEvent ce) {
		// to ensure EDT thread
		if (debug) {
			System.out.println("stateChanged: " + ce);
		}
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					stateChanged(ce);
				}
			});
			return;
		}

		if (!monitor.isProcessDone()) {
			line1.setText(monitor.getLine1());
			line2.setText(monitor.getLine2());
			line3.setText(monitor.getLine3());
			line4.setText(monitor.getLine4());
			line5.setText(monitor.getLine5());
			line6.setText(monitor.getLine6());
			line7.setText(monitor.getLine7());
		} else {
			if (debug) {
				System.out.println("stateChanged process done");
			}
//			this.setVisible(false);
			dispose();
		}
	}

    public void showCancelButton(boolean hide) {
        cancelButton.setVisible(hide);
    }

    private class CancelAction extends AbstractAction {
        private CancelAction() {
            // JFormDesigner - Action initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
            // Generated using JFormDesigner non-commercial license
            putValue(NAME, "Cancel");
            // JFormDesigner - End of action initialization  //GEN-END:initComponents
        }

        public void actionPerformed(ActionEvent e) {
            monitor.setProcessCancelled(true);
        }
    }
}
