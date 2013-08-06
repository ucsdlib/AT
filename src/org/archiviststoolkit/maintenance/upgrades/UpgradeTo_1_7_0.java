package org.archiviststoolkit.maintenance.upgrades;

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
 * @author Nathan Stevens
 * Date: June 10, 2009
 * Time: 9:09:19 PM
 */

import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.util.DatabaseConnectionUtils;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.hibernate.AuditInterceptor;
import org.archiviststoolkit.ApplicationFrame;
import org.netbeans.spi.wizard.DeferredWizardResult;
import org.hibernate.LockMode;
import org.hibernate.Session;

import java.sql.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;

public class UpgradeTo_1_7_0 extends Upgrade {
    private int count = 1; // keep track of the digital object being processed
    private int totalCount = 0; // total number of digital objects to process
    private int updateDone = 0; // This keeps track of when all the threads are done updating
    private HashMap<String, String> metsIDs = new HashMap<String, String>(); // keep track of mets IDs
    private Exception updateException = null; // Exception that is thrown by any of the update threads

    protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {
        return true;
    }

    /**
     * Run any sql and hibernate code that needs to be ran after the database has been
     * initialized by hibernate.
     *
     * @param conn The connection to the database
     * @return true if all the code executed fine
     */
    protected boolean runPostDBInitializationSQLCode(Connection conn) {
        // first we need to set a repository id for all digital objects otherwise
        // the dont show up in hibernates list all
        String sqlString = "";
        Statement stmt;

        try {
            stmt = conn.createStatement();
            sqlString = "UPDATE DigitalObjects SET repositoryId = 1";
            stmt.execute(sqlString);
            conn.commit();
        } catch (SQLException e) {
            setErrorString(e.getMessage());
            return false;
        }

        // now use hibernate to upgrade digital objects with correct repository and identifier information
        try {
            // this access is used for finding the repository of the DO, and findind only parent DO
            DigitalObjectDAO access = new DigitalObjectDAO();
            access.getLongSession();

            DigitalObjects digitalObject;

            // set the repository for digital objects and clear out the mets ID if they
            // are duplicates
            // HashMap<String,String> metsIDs = new HashMap<String,String>();
            String metsID;

            Collection digitalObjects = access.findAll(LockMode.READ);
            totalCount = digitalObjects.size();

            // if total count is greater than 1000 then run in
            // parallel code to help dramatically speed the process up
            if (totalCount > 1000) {
                System.out.println("Updating digital object using parallel code ...");
                return updateDigitalObjectsInParallel(digitalObjects, access);
            }

            // open digital objects in long session
            digitalObjects = access.findAllLongSession(LockMode.READ);

            // update the collection
            for (Object o : digitalObjects) {
                digitalObject = (DigitalObjects) o;

                // for debug purposes print out the digital object being processed
                String progressMessage = "Updating Digital Object : " + count + " of " + totalCount + " ( ID = " + digitalObject.getIdentifier() + " )";
                progress.setProgress("Updating Digital Object (ID:" + digitalObject.getIdentifier() + ")", count, totalCount);
                count++;

                // get the parent resource this digital object belongs to
                ArchDescriptionDigitalInstances digitalInstance = digitalObject.getDigitalInstance();
                if (digitalInstance != null) {
                    try {
                        Resources parentResource = access.findResourceByDigitalObject(digitalInstance);

                        // now update the repository
                        if (parentResource != null) {
                            setRepository(digitalObject, parentResource.getRepository());

                            // now set the parent resoure in the digital object instance to allow searching
                            // by resource for digital objects
                            digitalInstance.setParentResource(parentResource);
                        }
                    } catch (Exception e) { // lets handel any errors due to orphaned digital objects
                        digitalObject.setDigitalInstance(null);

                        System.out.println("Error processing Digital Object ID = " + digitalObject.getIdentifier() + "\n");
                        e.printStackTrace();
                    }
                }

                // modify the metId if is not unique
                metsID = digitalObject.getMetsIdentifier();
                if (metsID.length() != 0) {
                    if (!metsIDs.containsKey(metsID)) {
                        metsIDs.put(metsID, metsID);
                    } else { // key already exist so clear it out
                        digitalObject.setMetsIdentifier("");
                    }
                }

                access.updateLongSession(digitalObject, false);
            }

            access.closeLongSession();
        } catch (PersistenceException e) {
            setErrorString(e.getMessage());
            return false;
        } catch (LookupException e) {
            setErrorString(e.getMessage());
            return false;
        } catch (SQLException e) {
            setErrorString(e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            setErrorString(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Method to set the digital object repositories
     *
     * @param digitalObject The digital object
     * @param repository    The repository
     */
    protected synchronized void setRepository(DigitalObjects digitalObject, Repositories repository) {
        digitalObject.setRepository(repository);

        // now set the respository for any child digital objects through recursion
        for (DigitalObjects childDigitalObject : digitalObject.getChildren()) {
            setRepository(childDigitalObject, repository);
        }
    }

    protected int getNumberOfSteps() {
        return 2;
    }

    //load any new values for the lookup list
    protected boolean runLoadLookupLists() {
        return true;
    }

    protected boolean runFieldInit() {
        return true;
    }

    protected boolean upgradeNeeded(Connection conn) throws SQLException {
        return Constants.compareVersions("1.7.0", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
    }

    protected String getWarningMessage() {
        String message = "Digital Object Records will be updated.";

        return message;
    }

    /* Methods below this point is to test upgrading digital objects in
    parrallel code. It currently in test code */

    /**
     * Entry method for running updating of digital objects in parallel. This is
     * where the collection of digital objects is broken apart into ten seperate
     * array list
     *
     * @param digitalObjects
     * @param access
     * @return true if the upgrade succeded with no problem
     */
    private boolean updateDigitalObjectsInParallel(Collection digitalObjects, DigitalObjectDAO access) {
        // initialize 10 array list to hold digital objects which a split apart
        // from the main collection
        ArrayList<DigitalObjects> list1 = new ArrayList<DigitalObjects>();
        ArrayList<DigitalObjects> list2 = new ArrayList<DigitalObjects>();
        ArrayList<DigitalObjects> list3 = new ArrayList<DigitalObjects>();
        ArrayList<DigitalObjects> list4 = new ArrayList<DigitalObjects>();
        ArrayList<DigitalObjects> list5 = new ArrayList<DigitalObjects>();
        ArrayList<DigitalObjects> list6 = new ArrayList<DigitalObjects>();
        ArrayList<DigitalObjects> list7 = new ArrayList<DigitalObjects>();
        ArrayList<DigitalObjects> list8 = new ArrayList<DigitalObjects>();
        ArrayList<DigitalObjects> list9 = new ArrayList<DigitalObjects>();
        ArrayList<DigitalObjects> list10 = new ArrayList<DigitalObjects>();

        // get estimate of list size by dividing total by 10
        int listSize = totalCount / 10;

        // process collection and place digital objects in the seperate list
        int currentDO = 1; // This is used to see in which list to place digital object

        for (Object o : digitalObjects) {
            DigitalObjects digitalObject = (DigitalObjects) o;

            if (currentDO < (1 * listSize)) {
                list1.add(digitalObject);
                System.out.println("Adding to List 1 " + digitalObject.getIdentifier());
            } else if (currentDO < (2 * listSize)) {
                list2.add(digitalObject);
                System.out.println("Adding to List 2 " + digitalObject.getIdentifier());
            } else if (currentDO < (3 * listSize)) {
                list3.add(digitalObject);
                System.out.println("Adding to List 3 " + digitalObject.getIdentifier());
            } else if (currentDO < (4 * listSize)) {
                list4.add(digitalObject);
                System.out.println("Adding to List 4 " + digitalObject.getIdentifier());
            } else if (currentDO < (5 * listSize)) {
                list5.add(digitalObject);
                System.out.println("Adding to List 5 " + digitalObject.getIdentifier());
            } else if (currentDO < (6 * listSize)) {
                list6.add(digitalObject);
                System.out.println("Adding to List 6 " + digitalObject.getIdentifier());
            } else if (currentDO < (7 * listSize)) {
                list7.add(digitalObject);
                System.out.println("Adding to List 7 " + digitalObject.getIdentifier());
            } else if (currentDO < (8 * listSize)) {
                list8.add(digitalObject);
                System.out.println("Adding to List 8 " + digitalObject.getIdentifier());
            } else if (currentDO < (9 * listSize)) {
                list9.add(digitalObject);
                System.out.println("Adding to List 9 " + digitalObject.getIdentifier());
            } else {
                list10.add(digitalObject);
                System.out.println("Adding to List 10 " + digitalObject.getIdentifier());
            }

            currentDO++;
        }

        // now process the seperate array list concurrently
        try {
            // start up all the threads that update the digital objects
            updateDigitalObjects(list1, "List 1");
            updateDigitalObjects(list2, "List 2");
            updateDigitalObjects(list3, "List 3");
            updateDigitalObjects(list4, "List 4");
            updateDigitalObjects(list5, "List 5");
            updateDigitalObjects(list6, "List 6");
            updateDigitalObjects(list7, "List 7");
            updateDigitalObjects(list8, "List 8");
            updateDigitalObjects(list9, "List 9");
            updateDigitalObjects(list10, "List 10");

            // wait for all the threads to be done before returning
            // a boolean which indicates status of the upgrade
            while (updateDone < 10) {
                // check that an exception wasn't thrown
                if (updateException != null) {
                    setErrorString(updateException.getMessage());
                    updateException.printStackTrace();

                    // need to stop all thread here so just close the
                    // database connection which should cause them to throw
                    // exceptions
                    access.closeLongSession();
                    return false;
                }

                // sleep for 2 seconds before checking to see if we are done
                Thread.sleep(2000);
            }

            // close the long session since we should be done with it
            // at this point
            access.closeLongSession();
        } catch (Exception e) { // we have an exception so return false
            e.printStackTrace();
            return false;
        }

        // all updates completed successfully, so return true
        return true;
    }

    private void updateDigitalObjects(final ArrayList<DigitalObjects> digitalObjects, String listName) {
        // initialize a thread that actually processes the digital objects
        Thread performer = new Thread(new Runnable() {
            public void run() {
                try {
                    // inititalize new connection to the database
                    DigitalObjectDAO access2 = new DigitalObjectDAO();
                    Session session = SessionFactory.getInstance().openSession(new AuditInterceptor(ApplicationFrame.getInstance().getCurrentUser()));

                    String metsID = "";

                    for (DigitalObjects digo : digitalObjects) {
                        // get the full digital object in new session
                        DigitalObjects digitalObject = (DigitalObjects) access2.findByPrimaryKeyCommon(digo.getIdentifier(), session);

                        // for debug purposes print out the digital object being processed
                        incrementCount(digitalObject);

                        // get the parent resource this digital object belongs to
                        ArchDescriptionDigitalInstances digitalInstance = digitalObject.getDigitalInstance();
                        if (digitalInstance != null) {
                            try {
                                Resources parentResource = access2.findResourceByDigitalObject(digitalInstance);

                                // now update the repository
                                if (parentResource != null) {
                                    setRepository(digitalObject, parentResource.getRepository());

                                    // now set the parent resoure in the digital object instance to allow searching
                                    // by resource for digital objects
                                    digitalInstance.setParentResource(parentResource);
                                }
                            } catch (Exception e) { // lets handel any errors due to orphaned digital objects
                                digitalObject.setDigitalInstance(null);

                                System.out.println("Error processing Digital Object ID = " + digitalObject.getIdentifier() + "\n");
                                e.printStackTrace();
                            }
                        }

                        // modify the metId if is not unique
                        metsID = digitalObject.getMetsIdentifier();
                        if (metsID.length() != 0) {
                            if (!metsIDs.containsKey(metsID)) {
                                metsIDs.put(metsID, metsID);
                            } else { // key already exist so clear it out
                                digitalObject.setMetsIdentifier("");
                            }
                        }

                        // update the digital object in new session to prevent
                        // error
                        access2.update(digitalObject, session);
                    }

                    // close the session to save memory
                    session.close();

                    // now update the number of threads that is done
                    incrementUpdateDone();
                } catch (Exception e) {
                    setUpdateException(e);
                }
            }
        }, "Digital Object Updater for " + listName);
        performer.start();
    }


    /**
     * Method to print out the ID of the digital object being processed. It's
     * for debuging purposes
     *
     * @param digitalObject
     */
    private synchronized void incrementCount(DigitalObjects digitalObject) {
        // for debug purposes print out the digital object being processed
        String progressMessage = "Updating Digital Object : " + count + " of " + totalCount + " ( ID = " + digitalObject.getIdentifier() + " )";
        System.out.println(progressMessage);

        progress.setProgress("Updating Digital Object (ID:" + digitalObject.getIdentifier() + ")", count, totalCount);
        count++;
    }

    /**
     * Method to increment the update done count. This is used to keep
     * track of all the threads so we can return when all all the digital
     * objects have been processed
     */
    private synchronized void incrementUpdateDone() {
        updateDone++;
    }

    /**
     * Method to alert that an exception was thrown by one of the threads
     * during the digital object update process
     *
     * @param e The exception that was thrown
     */
    private synchronized void setUpdateException(Exception e) {
        updateException = e;
    }
}