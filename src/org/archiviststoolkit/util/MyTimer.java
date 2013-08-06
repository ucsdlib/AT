/**********************************************************************
 * Timer - Timer
 * Copyright (c) 2001 by the President and Fellows of Harvard College
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 *
 * Contact information
 *
 * Office for Information Systems
 * Harvard University Library
 * Harvard University
 * Cambridge, MA  02138
 * (617)495-3724
 * hulois@hulmail.harvard.edu
 **********************************************************************/

package org.archiviststoolkit.util;


/**
 * Timer
 * @author Lee Mandell
 * @version 1.0 2001/Apr/25
 * <li>. Replaced bitwise operator with boolean operator
 * 12Jan04 - CM
 */
public class MyTimer
{
    /******************************************************************
	 * PPRIVATE INSTANCE FIELDS.
	 ******************************************************************/
	
    /** Initial time in milliseconds from 00:00:00, Jan 1, 1970 UTC. */
    private long _t0;
    /** The start time of the last split */
    private long _tSplit;
	
    /******************************************************************
	 * CLASS CONSTRUCTOR.
	 ******************************************************************/
	
    /**
	 * Instantiate a Timer object.
	 */
    public MyTimer ()
    {
		_t0 = System.currentTimeMillis ();
		_tSplit = _t0;
    }
	
    /**
	 * Return a String representation of the elapsed time.
	 * @param time Elapsed time, in milliseconds
	 * @return String
	 */
    static public String toString (long time)
    {
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
	
    /******************************************************************
	 * PUBLIC CLASS METHODS.
	 ******************************************************************/
	
	
    /**
	 * Return elapsed time in milliseconds.
	 * @return Elapsed time
	 */
    public long elapsedTimeMillis ()
    {
		return System.currentTimeMillis () - _t0;
    }
	
    /**
	 * Return elapsed time since the last split in milliseconds.
	 * @return Elapsed time since last split
	 */
    public long elapsedTimeMillisSplit ()
    {
		long currentTime = System.currentTimeMillis();
		long splitTime = currentTime - _tSplit;
		_tSplit = currentTime;
		return splitTime;
    }
    public void reset()
    {
		//reset the timer
		_t0 = System.currentTimeMillis ();
		_tSplit = _t0;
    }
	
}
