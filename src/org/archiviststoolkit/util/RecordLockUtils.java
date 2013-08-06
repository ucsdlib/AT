package org.archiviststoolkit.util;

import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.model.RecordLocks;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.exceptions.DeleteException;
import org.archiviststoolkit.ApplicationFrame;

import java.util.*;
import java.sql.Statement;
import java.sql.SQLException;
import java.net.InetAddress;

/**
 * Archivists' Toolkit(TM) Copyright © 2005-2008 Regents of the University of California, New York University, & Five Colleges, Inc.
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
 * Created by IntelliJ IDEA.
 * @author: Nathan Stevens
 * Date: Nov 26, 2008
 * Time: 11:08:31 AM
 */

public class RecordLockUtils {
    private static ArrayList<RecordLocks> myRecordLocks = new ArrayList();

    /**
     * Method to check to see if a record is locked and if it is return the lock
     * @param record The record being checked
     * @return null or the record lock for the particular record
     */
    public static RecordLocks getRecordLock(DomainObject record) {
        RecordLocks recordLock = null;
        String className = record.getClass().getName();
        Long recordId = record.getIdentifier();

        try {
            DomainAccessObject dao = DomainAccessObjectFactory.getInstance().getDomainAccessObject(RecordLocks.class);
            String hqlString = "from RecordLocks where className = '" + className + "' and recordId = '" + recordId + "'";
            Collection list = dao.findByQuery(hqlString);
            if(list != null) {
                Iterator iterator = list.iterator();

                while(iterator.hasNext()) {
                    RecordLocks lock = (RecordLocks)iterator.next();
                    Long updateTime = lock.getPreviousUpdateTime();
                    String userName = lock.getUserName();
                    String hostIP = lock.getHostIP();

                    // Check to make sure the time is within the record lock expire time
                    // and if it is the return it otherwise remove all the old locks
                    if(Math.abs(getCurrentTime() - updateTime) < RecordLocks.RECORD_LOCK_EXPIRE_TIME) {
                        // now check that this lock didn't belong to this user on this host anyway
                        // if it does then return null
                        if(userName.equals(ApplicationFrame.getInstance().getCurrentUserName()) && hostIP.equals(getHostIP())) {
                            dao.delete(lock);
                            recordLock = null;
                        } else {
                            recordLock = lock;
                        }
                    } else { // Must remove this old lock. TODO Should really see if this is wise to do?
                        dao.delete(lock);
                    }
                }

            }
        } catch (PersistenceException e) {
			//new ErrorDialog("There is a problem loading Record Locks", e).showDialog();
            e.printStackTrace();
		} catch (LookupException e) {
            //new ErrorDialog("There is a problem loading Record Locks", e).showDialog();
            e.printStackTrace();
        } catch (DeleteException e) {
            //new ErrorDialog("There is a problem deleting the Record Lock", e).showDialog();
            e.printStackTrace();
        }

        return recordLock;
    }

    /**
     * Method  to add a record lock of the following domain object
     * @param record The record to create a lock for
     */
    public static RecordLocks addRecordLock(DomainObject record) {
        RecordLocks recordLock = null;

        String className = record.getClass().getName();
        Long recordId = record.getIdentifier();
        String userName = ApplicationFrame.getInstance().getCurrentUserName();
        Long updateTime = getCurrentTime();
        String hostIP = getHostIP();

        try {
            recordLock = new RecordLocks(className, recordId, userName, updateTime, hostIP);
            DomainAccessObject dao = DomainAccessObjectFactory.getInstance().getDomainAccessObject(RecordLocks.class);
            dao.add(recordLock); // add the lock to the database now
            getMyRecordLocks().add(recordLock);
        } catch (PersistenceException e) {
			//new ErrorDialog("There is a problem adding a Record Lock", e).showDialog();
            e.printStackTrace();
		}

        return recordLock;
    }

    /**
     * Method to return the current time in millisecond. This method should really return a time that is
     * independent of the OS to avoid the problem of computers not having the same time
     * For now just set the time in millisonds as it would be in GMT+0
     * @return The current time in milliseconds
     */
    private static synchronized Long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * Method to clear a particular record lock
     * @param recordLock the record lock to clear
     */
    public static void clearRecordLock(RecordLocks recordLock) {
        if(recordLock == null) {
            return;
        }

        try {
            getMyRecordLocks().remove(recordLock);
            DomainAccessObject dao = DomainAccessObjectFactory.getInstance().getDomainAccessObject(RecordLocks.class);
            dao.delete(recordLock); // remove from database now
        } catch (PersistenceException e) {
			e.printStackTrace();
            //new ErrorDialog("There is a problem loading Record Locks", e).showDialog();
		} catch (DeleteException e) {
            e.printStackTrace();
            //new ErrorDialog("There is a problem deleting the Record Lock", e).showDialog();
        }
    }

    /**
     * Method to get the list of records that are locked
     * @return An array list containing the record locks
     */
    public static synchronized ArrayList<RecordLocks> getMyRecordLocks() {
        return myRecordLocks;
    }

    /**
     * Method to update the currentUpdateTime of all My records
     */
    public static void updateRecordLocksTime() {
        if(getMyRecordLocks().size() == 0) {
             return; // nothing to update so return
        }

        try {
            DomainAccessObject dao = DomainAccessObjectFactory.getInstance().getDomainAccessObject(RecordLocks.class);
            for(RecordLocks recordLock: getMyRecordLocks()) {
                recordLock.setPreviousUpdateTime(getCurrentTime()); // Need to get a OS indepent time to set here
                dao.update(recordLock);
            }
        } catch (PersistenceException e) {
			//new ErrorDialog("There is a problem updating Record Locks", e).showDialog();
            e.printStackTrace();
		}
    }

    /**
     * Method to clear all the record locks belonging to this user
     */
    public static void clearAllMyRecordLocks() {
        if(getMyRecordLocks().size() == 0) {
             return; // nothing to remove so return
        }

        try {
            DomainAccessObject dao = DomainAccessObjectFactory.getInstance().getDomainAccessObject(RecordLocks.class);
            dao.deleteGroup(getMyRecordLocks());
            getMyRecordLocks().clear();
        } catch (PersistenceException e) {
			//new ErrorDialog("There is a problem deleting all the Record Locks", e).showDialog();
            e.printStackTrace();
		} catch (LookupException e) {
            //new ErrorDialog("There is a problem deleting all the Record Locks", e).showDialog();
            e.printStackTrace();
        }
    }

    /**
     * Method to clear all record locks in the database should only
     * be used superuser during upgrade
     *
     * @param stmt The connection to the database
     */
    public static void clearAllRecordLocks(Statement stmt) throws SQLException {
        String sqlString = "DELETE FROM RecordLocks";
        stmt.execute(sqlString);
    }

    /**
     * Method to return the IP address of the host computer
     * @return The IP address of the host computer
     */
    public static String getHostIP() {
        try {
            InetAddress thisIp = InetAddress.getLocalHost();
            return thisIp.getHostAddress();
        }
        catch (Exception e) {
            e.printStackTrace();
            return "No IP Found";
        }
    }
}

