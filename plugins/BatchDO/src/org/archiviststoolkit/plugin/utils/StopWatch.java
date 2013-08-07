package org.archiviststoolkit.plugin.utils;

/**
 * Archivists' Toolkit(TM) Copyright © 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
 * All rights reserved.
 * <p/>
 * This software is free. You can redistribute it and / or modify it under the terms of the Educational Community License (ECL)
 * version 1.0 (http://www.opensource.org/licenses/ecl1.php)
 * <p/>
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the ECL license for more details about permissions and limitations.
 * <p/>
 * <p/>
 * Archivists' Toolkit(TM)
 * http://www.archiviststoolkit.org
 * info@archiviststoolkit.org
 * <p/>
 * Created by IntelliJ IDEA.
 * <p/>
 * Simple class for return the amount of time a task as taken
 *
 * @author: Nathan Stevens
 * Date: Sep 17, 2010
 * Time: 3:42:08 PM
 */
public class StopWatch {
    private long startTime = 0;
    private long stopTime = 0;
    private boolean running = false;


    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }


    public void stop() {
        this.stopTime = System.currentTimeMillis();
        this.running = false;
    }


    //elaspsed time in milliseconds
    public long getElapsedTime() {
        long elapsed;
        if (running) {
            elapsed = (System.currentTimeMillis() - startTime);
        } else {
            elapsed = (stopTime - startTime);
        }
        return elapsed;
    }


    //elaspsed time in seconds
    public long getElapsedTimeSecs() {
        long elapsed;
        if (running) {
            elapsed = ((System.currentTimeMillis() - startTime) / 1000);
        } else {
            elapsed = ((stopTime - startTime) / 1000);
        }
        return elapsed;
    }

    public String getPrettyTime() {
        return getPrettyTime(getElapsedTime());
    }

    public String getPrettyTime(long time) {
		long hr = time/3600000;
		time   -=   hr*3600000;
		long mn = time/60000;
		time   -=   mn*60000;
		long sc = time/1000;
		long ms = time - sc*1000;
		long ds = ms / 10;

        StringBuffer buffer = new StringBuffer ();

        if (hr > 0) {
			buffer.append (hr + " hr ");
		}
		if (hr > 0 || mn > 0) {
			if (hr > 0 && mn < 10) {
				buffer.append ("0");
			}
			buffer.append (mn + " min ");
		}
		if ((hr > 0 || mn > 0) && sc < 10) {
			buffer.append ("0");
		}
		buffer.append (sc + ".");
		if (ds < 10) {
			buffer.append ("0");
		}
		buffer.append (ds + " sec");

        return buffer.toString ();
    }

}
