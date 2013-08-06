package org.archiviststoolkit.swing;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

/**
 * MySwing: Advanced Swing Utilites
 * Copyright (C) 2005  Santhosh Kumar T
 * <p/>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
public class ATProgressUtil {
    static class MonitorListener implements ChangeListener, ActionListener {
        InfiniteProgressPanel monitor;
        Window owner;
        Timer timer;
		private boolean debug = false;
        private boolean showCancelButton = false; // used to indicate if to show the cancel button

        public MonitorListener(Window owner, InfiniteProgressPanel monitor){
            this.owner = owner;
            this.monitor = monitor;
        }

        /**
         * Constructor that allows show the cancel button
         * @param owner The parent wibdow
         * @param monitor The infinite progress monitor
         * @param showCancelButton whether to show the cancel button
         */
        public MonitorListener(Window owner, InfiniteProgressPanel monitor, boolean showCancelButton){
            this(owner, monitor);
            this.showCancelButton = showCancelButton;
        }

        public void stateChanged(ChangeEvent ce){
            InfiniteProgressPanel monitor = (InfiniteProgressPanel)ce.getSource();
            if(!monitor.isProcessDone()){
				if (debug) {
					System.out.println("AT progress util state changed process not done");
				}
                if(timer==null){
                    timer = new Timer(monitor.getMilliSecondsToWait(), this);
                    timer.setRepeats(false);
                    timer.start();
                }
            } else{
				if (debug) {
					System.out.println("AT progress util state changed process done");
				}
                if(timer!=null && timer.isRunning())
                    timer.stop();
                monitor.removeChangeListener(this);
            }
        }

        public void actionPerformed(ActionEvent e){
            monitor.removeChangeListener(this);
            ATProgressDialog dlg = owner instanceof Frame
                    ? new ATProgressDialog((Frame)owner, monitor)
                    : new ATProgressDialog((Dialog)owner, monitor);
            dlg.pack();
            dlg.setLocationRelativeTo(null);
            dlg.showCancelButton(showCancelButton);
            dlg.setVisible(true);
        }
    }

    public synchronized static InfiniteProgressPanel createModalProgressMonitor(Component owner, int milliSecondsToWait){
        return createModalProgressMonitor(owner, milliSecondsToWait, false);
    }

    public synchronized static InfiniteProgressPanel createModalProgressMonitor(Component owner, int milliSecondsToWait, boolean showCancelButton){
        InfiniteProgressPanel monitor = new InfiniteProgressPanel(milliSecondsToWait);
        Window window = owner instanceof Window
                ? (Window)owner
                : SwingUtilities.getWindowAncestor(owner);
        monitor.addChangeListener(new MonitorListener(window, monitor, showCancelButton));
        return monitor;
    }
}


