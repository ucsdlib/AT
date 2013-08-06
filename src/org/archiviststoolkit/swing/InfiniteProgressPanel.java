package org.archiviststoolkit.swing;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.util.Vector;
import java.util.Iterator;

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
public class InfiniteProgressPanel {
	int current = -1;
	private boolean processDone = false;
    private boolean processCancelled = false; // used to indicate that the process was cancelled
//	boolean indeterminate;
	int milliSecondsToWait = 2000; // 2 seconds
	private String line1;
	private String line2;
	private String line3;
	private String line4;
	private String line5;
	private String line6;
	private String line7;


    public InfiniteProgressPanel(int milliSecondsToWait) {
		this.milliSecondsToWait = milliSecondsToWait;
	}

	public InfiniteProgressPanel() {
	}

	public void start(String firstLine) {
		if (current != -1)
			throw new IllegalStateException("not started yet");
		setTextLine(firstLine, 1);
		current = 0;
		fireChangeEvent();
	}

	public void close() {
		processDone = true;
		fireChangeEvent();
	}

	public int getMilliSecondsToWait() {
		return milliSecondsToWait;
	}

	public int getCurrent() {
		return current;
	}

	public String getLine1() {
		return line1;
	}


	/*--------------------------------[ ListenerSupport ]--------------------------------*/

	private Vector listeners = new Vector();
	private ChangeEvent ce = new ChangeEvent(this);

	public synchronized void addChangeListener(ChangeListener listener) {
		listeners.add(listener);
	}

	public synchronized void removeChangeListener(ChangeListener listener) {
		listeners.remove(listener);
	}

	private void fireChangeEvent() {
		Vector targets;
		synchronized (this) {
			targets = (Vector) listeners.clone();
		}

		for (Iterator iter = targets.iterator(); iter.hasNext();) {
			ChangeListener listener = (ChangeListener)
					iter.next();
			listener.stateChanged(ce);
		}
	}

	public void initText() {
	}

	public void setTextLine(String newText, int index) {

		switch (index) {

			case 0:
				line1 = newText;
				line2 = "";
				line3 = "";
				line4 = "";
				line5 = "";
				line6 = "";
				line7 = "";
				break;

			case 1:
				line1 = newText;
				line2 = "";
				line3 = "";
				line4 = "";
				line5 = "";
				line6 = "";
				line7 = "";
				break;

			case 2:
				line2 = newText;
				line3 = "";
				line4 = "";
				line5 = "";
				line6 = "";
				line7 = "";
				break;

			case 3:
				line3 = newText;
				line4 = "";
				line5 = "";
				line6 = "";
				line7 = "";
				break;

			case 4:
				line4 = newText;
				line5 = "";
				line6 = "";
				line7 = "";
				break;

			case 5:
				line5 = newText;
				line6 = "";
				line7 = "";
				break;

			case 6:
				line6 = newText;
				line7 = "";
				break;

			case 7:
				line7 = newText;
				break;

			default:
				line7 = newText;
				break;

		}
		fireChangeEvent();
	}

	public int getTextDepth() {
		return 1;
	}

	public void initText(String s) {
		this.line1 = s;
		fireChangeEvent();
	}

	public String getLine2() {
		return line2;
	}

	public String getLine3() {
		return line3;
	}

	public String getLine4() {
		return line4;
	}

	public String getLine5() {
		return line5;
	}

	public String getLine6() {
		return line6;
	}

	public String getLine7() {
		return line7;
	}

	public boolean isProcessDone() {
		return processDone;
	}

    /**
     * Method to check if the process was canceled
     * @return boolean indicating whether the process was canceled
     */
    public boolean isProcessCancelled() {
        return processCancelled;
    }

    /**
     * Method to set the process to cancel
     * @param processCancelled
     */
    public void setProcessCancelled(boolean processCancelled) {
        this.processCancelled = processCancelled;
        close();
    }
}
